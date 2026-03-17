package io.cloudlayer.sdk.exception;

/**
 * Thrown when the API returns an HTTP 4xx or 5xx response.
 */
public class ApiException extends CloudLayerException {

    private static final long serialVersionUID = 1L;

    private final int statusCode;
    private final String statusText;
    private final String path;
    private final String method;
    private final String responseBody;

    public ApiException(int statusCode, String statusText, String path, String method, String responseBody) {
        super(buildMessage(statusCode, statusText, path, method));
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.path = path;
        this.method = method;
        this.responseBody = responseBody;
    }

    private static String buildMessage(int statusCode, String statusText, String path, String method) {
        return method + " " + path + " returned " + statusCode + " " + statusText;
    }

    public int getStatusCode() { return statusCode; }
    public String getStatusText() { return statusText; }
    public String getPath() { return path; }
    public String getMethod() { return method; }
    public String getResponseBody() { return responseBody; }
}
