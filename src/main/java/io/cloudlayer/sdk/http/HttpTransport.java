package io.cloudlayer.sdk.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudlayer.sdk.CloudLayerConfig;
import io.cloudlayer.sdk.exception.*;
import io.cloudlayer.sdk.model.response.ResponseHeaders;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Internal HTTP transport layer. Handles request construction, retry, and error mapping.
 */
public final class HttpTransport {

    private static final Logger LOG = Logger.getLogger(HttpTransport.class.getName());
    private static final Pattern FILENAME_PATTERN = Pattern.compile("filename=\"?([^\"]+)\"?");

    private final CloudLayerConfig config;
    private final ObjectMapper mapper;
    private final RetryPolicy retryPolicy;

    public HttpTransport(CloudLayerConfig config, ObjectMapper mapper) {
        this.config = config;
        this.mapper = mapper;
        this.retryPolicy = new RetryPolicy(config.getMaxRetries());
    }

    /**
     * Sends a JSON request and deserializes the JSON response.
     */
    public <T> T sendJsonRequest(String method, String path, Object body, Class<T> returnType,
                                 RequestOptions options) {
        HttpResponse<byte[]> response = executeWithRetry(method, path, body, options);
        return handleJsonResponse(response, returnType, method, buildUrl(path, options));
    }

    /**
     * Sends a JSON request and deserializes using a TypeReference (for generic types like List).
     */
    public <T> T sendJsonRequest(String method, String path, Object body, TypeReference<T> typeRef,
                                 RequestOptions options) {
        HttpResponse<byte[]> response = executeWithRetry(method, path, body, options);
        String url = buildUrl(path, options);
        int status = response.statusCode();
        if (status >= 400) {
            throwApiException(status, method, url, new String(response.body(), StandardCharsets.UTF_8));
        }
        if (status == 204 || response.body() == null || response.body().length == 0) {
            return null;
        }
        try {
            return mapper.readValue(response.body(), typeRef);
        } catch (IOException e) {
            throw new CloudLayerException("Failed to parse response: " + e.getMessage(), e);
        }
    }

    /**
     * Sends a JSON request and returns raw binary response (for v1 sync mode).
     */
    public RawResponse sendRawRequest(String method, String path, Object body, RequestOptions options) {
        HttpResponse<byte[]> response = executeWithRetry(method, path, body, options);
        String url = buildUrl(path, options);
        int status = response.statusCode();

        if (status >= 400) {
            throwApiException(status, method, url, new String(response.body(), StandardCharsets.UTF_8));
        }

        ResponseHeaders headers = parseResponseHeaders(response);
        String filename = parseContentDisposition(response);
        return new RawResponse(response.body(), headers, status, filename);
    }

    /**
     * Sends a multipart request.
     */
    public <T> T sendMultipartRequest(String method, String path, MultipartBodyBuilder multipart,
                                      Class<T> returnType, RequestOptions options) {
        String url = buildUrl(path, options);
        try {
            HttpRequest.BodyPublisher publisher = multipart.build();
            HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(config.getTimeout())
                    .header("X-API-Key", config.getApiKey())
                    .header("User-Agent", config.getUserAgent())
                    .header("Content-Type", multipart.getContentType())
                    .method(method, publisher);

            addCustomHeaders(reqBuilder);

            HttpResponse<byte[]> response = config.getHttpClient()
                    .send(reqBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());

            return handleJsonResponse(response, returnType, method, url);
        } catch (java.net.http.HttpTimeoutException e) {
            throw new TimeoutException("Request timed out: " + method + " " + url, e);
        } catch (IOException e) {
            throw new NetworkException("Network error: " + method + " " + url, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NetworkException("Request interrupted: " + method + " " + url, e);
        }
    }

    /**
     * Sends a multipart request returning raw bytes (for v1 document conversions).
     */
    public RawResponse sendMultipartRawRequest(String method, String path, MultipartBodyBuilder multipart,
                                               RequestOptions options) {
        String url = buildUrl(path, options);
        try {
            HttpRequest.BodyPublisher publisher = multipart.build();
            HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(config.getTimeout())
                    .header("X-API-Key", config.getApiKey())
                    .header("User-Agent", config.getUserAgent())
                    .header("Content-Type", multipart.getContentType())
                    .method(method, publisher);

            addCustomHeaders(reqBuilder);

            HttpResponse<byte[]> response = config.getHttpClient()
                    .send(reqBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());

            int status = response.statusCode();
            if (status >= 400) {
                throwApiException(status, method, url, new String(response.body(), StandardCharsets.UTF_8));
            }

            ResponseHeaders headers = parseResponseHeaders(response);
            String filename = parseContentDisposition(response);
            return new RawResponse(response.body(), headers, status, filename);
        } catch (java.net.http.HttpTimeoutException e) {
            throw new TimeoutException("Request timed out: " + method + " " + url, e);
        } catch (IOException e) {
            throw new NetworkException("Network error: " + method + " " + url, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NetworkException("Request interrupted: " + method + " " + url, e);
        }
    }

    // --- Private implementation ---

    private HttpResponse<byte[]> executeWithRetry(String method, String path, Object body, RequestOptions options) {
        String url = buildUrl(path, options);
        String jsonBody = serializeBody(body);

        for (int attempt = 0; ; attempt++) {
            try {
                HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(config.getTimeout())
                        .header("X-API-Key", config.getApiKey())
                        .header("User-Agent", config.getUserAgent())
                        .header("Content-Type", "application/json");

                addCustomHeaders(reqBuilder);

                if ("GET".equals(method)) {
                    reqBuilder.GET();
                } else if ("DELETE".equals(method)) {
                    reqBuilder.DELETE();
                } else {
                    reqBuilder.method(method, jsonBody != null
                            ? HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8)
                            : HttpRequest.BodyPublishers.noBody());
                }

                HttpResponse<byte[]> response = config.getHttpClient()
                        .send(reqBuilder.build(), HttpResponse.BodyHandlers.ofByteArray());

                int status = response.statusCode();

                if (status >= 200 && status < 300) {
                    return response;
                }

                if (options.isRetryable() && retryPolicy.isRetryableStatus(status) && retryPolicy.shouldRetry(attempt)) {
                    Integer retryAfter = parseRetryAfterHeader(response);
                    long backoff = retryPolicy.getBackoffMillis(attempt, retryAfter);
                    LOG.log(Level.INFO, "Retrying {0} {1} (status {2}, attempt {3}/{4}, backoff {5}ms)",
                            new Object[]{method, url, status, attempt + 1, config.getMaxRetries(), backoff});
                    if (!retryPolicy.sleep(backoff)) {
                        throw new NetworkException("Request interrupted during retry backoff: " + method + " " + url,
                                new InterruptedException());
                    }
                    continue;
                }

                return response; // Non-retryable or exhausted retries — will be handled by caller
            } catch (java.net.http.HttpTimeoutException e) {
                throw new TimeoutException("Request timed out: " + method + " " + url, e);
            } catch (IOException e) {
                if (options.isRetryable() && retryPolicy.shouldRetry(attempt)) {
                    long backoff = retryPolicy.getBackoffMillis(attempt);
                    LOG.log(Level.INFO, "Retrying {0} {1} (IO error, attempt {2}/{3})",
                            new Object[]{method, url, attempt + 1, config.getMaxRetries()});
                    if (!retryPolicy.sleep(backoff)) {
                        throw new NetworkException("Request interrupted during retry backoff: " + method + " " + url,
                                new InterruptedException());
                    }
                    continue;
                }
                throw new NetworkException("Network error: " + method + " " + url, e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new NetworkException("Request interrupted: " + method + " " + url, e);
            }
        }
    }

    private <T> T handleJsonResponse(HttpResponse<byte[]> response, Class<T> returnType, String method, String url) {
        int status = response.statusCode();

        if (status >= 400) {
            throwApiException(status, method, url, new String(response.body(), StandardCharsets.UTF_8));
        }

        if (status == 204 || response.body() == null || response.body().length == 0) {
            return null;
        }

        // Check content type for binary vs JSON
        String contentType = response.headers().firstValue("content-type").orElse("");
        if (returnType == byte[].class || (!contentType.contains("json") && !contentType.contains("text"))) {
            @SuppressWarnings("unchecked")
            T result = (T) response.body();
            return result;
        }

        try {
            return mapper.readValue(response.body(), returnType);
        } catch (IOException e) {
            throw new CloudLayerException("Failed to parse response: " + e.getMessage(), e);
        }
    }

    private void throwApiException(int status, String method, String url, String responseBody) {
        String statusText = httpStatusText(status);
        String message = extractErrorMessage(responseBody);

        if (status == 401 || status == 403) {
            throw new AuthException(status, statusText, url, method, responseBody);
        }
        if (status == 429) {
            throw new RateLimitException(url, method, responseBody, null);
        }
        throw new ApiException(status, message != null ? message : statusText, url, method, responseBody);
    }

    private String extractErrorMessage(String body) {
        if (body == null || body.isEmpty()) return null;
        try {
            Map<?, ?> parsed = mapper.readValue(body, Map.class);
            Object msg = parsed.get("message");
            if (msg != null) return msg.toString();
            Object err = parsed.get("error");
            if (err != null) return err.toString();
        } catch (IOException ignored) {
            // Not JSON — return null
        }
        return null;
    }

    String buildUrl(String path, RequestOptions options) {
        String base;
        if (options.isAbsolutePath()) {
            base = config.getBaseUrl() + "/" + path;
        } else {
            base = config.getBaseUrl() + "/" + config.getApiVersion().getValue() + "/" + path;
        }

        Map<String, String> params = options.getQueryParams();
        if (params == null || params.isEmpty()) {
            return base;
        }

        StringBuilder sb = new StringBuilder(base);
        sb.append('?');
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) sb.append('&');
            sb.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            sb.append('=');
            sb.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            first = false;
        }
        return sb.toString();
    }

    private String serializeBody(Object body) {
        if (body == null) return null;
        try {
            return mapper.writeValueAsString(body);
        } catch (IOException e) {
            throw new CloudLayerException("Failed to serialize request body: " + e.getMessage(), e);
        }
    }

    private void addCustomHeaders(HttpRequest.Builder builder) {
        Map<String, String> headers = config.getCustomHeaders();
        if (headers != null) {
            headers.forEach(builder::header);
        }
    }

    ResponseHeaders parseResponseHeaders(HttpResponse<?> response) {
        return ResponseHeaders.fromHttpHeaders(name ->
                response.headers().firstValue(name).orElse(null)
        );
    }

    private Integer parseRetryAfterHeader(HttpResponse<?> response) {
        return response.headers().firstValue("retry-after")
                .map(value -> {
                    try {
                        return Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .orElse(null);
    }

    String parseContentDisposition(HttpResponse<?> response) {
        return response.headers().firstValue("content-disposition")
                .map(value -> {
                    Matcher m = FILENAME_PATTERN.matcher(value);
                    return m.find() ? m.group(1) : null;
                })
                .orElse(null);
    }

    private static String httpStatusText(int status) {
        switch (status) {
            case 400: return "Bad Request";
            case 401: return "Unauthorized";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 422: return "Unprocessable Entity";
            case 429: return "Too Many Requests";
            case 500: return "Internal Server Error";
            case 502: return "Bad Gateway";
            case 503: return "Service Unavailable";
            case 504: return "Gateway Timeout";
            default: return "HTTP " + status;
        }
    }

    /**
     * Container for raw (binary) responses.
     */
    public static final class RawResponse {
        private final byte[] body;
        private final ResponseHeaders headers;
        private final int status;
        private final String filename;

        public RawResponse(byte[] body, ResponseHeaders headers, int status, String filename) {
            this.body = body;
            this.headers = headers;
            this.status = status;
            this.filename = filename;
        }

        public byte[] getBody() { return body; }
        public ResponseHeaders getHeaders() { return headers; }
        public int getStatus() { return status; }
        public String getFilename() { return filename; }
    }
}
