package io.cloudlayer.sdk.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudlayer.sdk.exception.*;
import io.cloudlayer.sdk.http.HttpTransport;
import io.cloudlayer.sdk.http.RequestOptions;
import io.cloudlayer.sdk.model.options.ListTemplatesOptions;
import io.cloudlayer.sdk.model.options.StorageParams;
import io.cloudlayer.sdk.model.options.WaitForJobOptions;
import io.cloudlayer.sdk.model.response.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data management API methods: jobs, assets, storage, account, templates, and utility methods.
 */
public final class DataApi {

    private static final RequestOptions RETRYABLE = RequestOptions.of(true);
    private static final RequestOptions NOT_RETRYABLE = RequestOptions.of(false);

    private final HttpTransport transport;
    private final ObjectMapper mapper;
    private final HttpClient httpClient;
    private final String userAgent;

    public DataApi(HttpTransport transport, ObjectMapper mapper, HttpClient httpClient, String userAgent) {
        this.transport = transport;
        this.mapper = mapper;
        this.httpClient = httpClient;
        this.userAgent = userAgent;
    }

    // --- Jobs API ---

    public List<Job> listJobs() {
        return transport.sendJsonRequest("GET", "jobs", null,
                new TypeReference<List<Job>>() {}, RETRYABLE);
    }

    public Job getJob(String jobId) {
        validateNotEmpty(jobId, "jobId");
        return transport.sendJsonRequest("GET", "jobs/" + jobId, null, Job.class, RETRYABLE);
    }

    // --- Assets API ---

    public List<Asset> listAssets() {
        return transport.sendJsonRequest("GET", "assets", null,
                new TypeReference<List<Asset>>() {}, RETRYABLE);
    }

    public Asset getAsset(String assetId) {
        validateNotEmpty(assetId, "assetId");
        return transport.sendJsonRequest("GET", "assets/" + assetId, null, Asset.class, RETRYABLE);
    }

    // --- Storage API ---

    public List<StorageListItem> listStorage() {
        return transport.sendJsonRequest("GET", "storage", null,
                new TypeReference<List<StorageListItem>>() {}, RETRYABLE);
    }

    public StorageDetail getStorage(String storageId) {
        validateNotEmpty(storageId, "storageId");
        return transport.sendJsonRequest("GET", "storage/" + storageId, null, StorageDetail.class, RETRYABLE);
    }

    public StorageCreateResponse addStorage(StorageParams params) {
        validateStorageParams(params);
        // Send request and get raw response to detect "not allowed" quirk
        HttpTransport.RawResponse raw = transport.sendRawRequest("POST", "storage", params, NOT_RETRYABLE);
        try {
            // Check for "not allowed" response (HTTP 200 with allowed: false)
            Map<?, ?> parsed = mapper.readValue(raw.getBody(), Map.class);
            Object allowed = parsed.get("allowed");
            if (allowed instanceof Boolean && !(Boolean) allowed) {
                String reason = parsed.get("reason") != null ? parsed.get("reason").toString() : "Storage not allowed";
                int statusCode = parsed.get("statusCode") instanceof Number
                        ? ((Number) parsed.get("statusCode")).intValue() : 403;
                throw new ApiException(statusCode, reason, "storage", "POST", new String(raw.getBody()));
            }
            return mapper.readValue(raw.getBody(), StorageCreateResponse.class);
        } catch (ApiException e) {
            throw e; // Re-throw ApiExceptions from our "not allowed" detection
        } catch (IOException e) {
            throw new CloudLayerException("Failed to parse storage response: " + e.getMessage(), e);
        }
    }

    public void deleteStorage(String storageId) {
        validateNotEmpty(storageId, "storageId");
        HttpTransport.RawResponse raw = transport.sendRawRequest("DELETE", "storage/" + storageId, null, NOT_RETRYABLE);
        try {
            Map<?, ?> parsed = mapper.readValue(raw.getBody(), Map.class);
            String status = parsed.get("status") != null ? parsed.get("status").toString() : "";
            if ("error".equals(status)) {
                String message = parsed.get("message") != null ? parsed.get("message").toString() : "Delete failed";
                throw new ApiException(400, message, "storage/" + storageId, "DELETE", new String(raw.getBody()));
            }
            // status: "success" → void return
        } catch (ApiException e) {
            throw e;
        } catch (IOException e) {
            throw new CloudLayerException("Failed to parse delete response: " + e.getMessage(), e);
        }
    }

    // --- Account & Status API ---

    public AccountInfo getAccount() {
        return transport.sendJsonRequest("GET", "account", null, AccountInfo.class, RETRYABLE);
    }

    public StatusResponse getStatus() {
        return transport.sendJsonRequest("GET", "getStatus", null, StatusResponse.class, RETRYABLE);
    }

    // --- Templates API ---

    public List<PublicTemplate> listTemplates() {
        return listTemplates(null);
    }

    public List<PublicTemplate> listTemplates(ListTemplatesOptions options) {
        Map<String, String> queryParams = null;
        if (options != null) {
            queryParams = new HashMap<>();
            if (options.getType() != null) queryParams.put("type", options.getType());
            if (options.getCategory() != null) queryParams.put("category", options.getCategory());
            if (options.getTags() != null) queryParams.put("tags", options.getTags());
            if (options.getExpand() != null && options.getExpand()) queryParams.put("expand", "true");
        }
        RequestOptions reqOpts = RequestOptions.of(true, true, queryParams);
        return transport.sendJsonRequest("GET", "v2/templates", null,
                new TypeReference<List<PublicTemplate>>() {}, reqOpts);
    }

    public PublicTemplate getTemplate(String templateId) {
        validateNotEmpty(templateId, "templateId");
        return transport.sendJsonRequest("GET", "v2/template/" + templateId, null,
                PublicTemplate.class, RequestOptions.of(true, true));
    }

    // --- Utility Methods ---

    public Job waitForJob(String jobId) {
        return waitForJob(jobId, WaitForJobOptions.builder().build());
    }

    public Job waitForJob(String jobId, WaitForJobOptions options) {
        validateNotEmpty(jobId, "jobId");
        Duration interval = options.getInterval();
        Duration maxWait = options.getMaxWait();
        Instant deadline = Instant.now().plus(maxWait);

        while (true) {
            Job job = getJob(jobId);
            if (job.getStatus() != null) {
                switch (job.getStatus()) {
                    case SUCCESS:
                        return job;
                    case ERROR:
                        String errorMsg = job.getError() != null ? job.getError() : "Job failed";
                        throw new ApiException(400, errorMsg, "jobs/" + jobId, "GET", errorMsg);
                    default:
                        break;
                }
            }

            if (Instant.now().plus(interval).isAfter(deadline)) {
                throw new TimeoutException("waitForJob timed out after " + maxWait.getSeconds() + " seconds for job " + jobId);
            }

            try {
                Thread.sleep(interval.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new TimeoutException("waitForJob interrupted for job " + jobId, e);
            }
        }
    }

    public byte[] downloadJobResult(Job job) {
        if (job == null || job.getAssetUrl() == null || job.getAssetUrl().isEmpty()) {
            throw new ValidationException("Job must have a non-empty assetUrl (populated after successful processing)");
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(job.getAssetUrl()))
                    .header("User-Agent", userAgent)
                    .GET()
                    .build();

            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 403) {
                throw new ApiException(403, "Asset URL may have expired (presigned URLs have a TTL)",
                        job.getAssetUrl(), "GET", new String(response.body()));
            }
            if (response.statusCode() >= 400) {
                throw new ApiException(response.statusCode(), "Failed to download asset",
                        job.getAssetUrl(), "GET", new String(response.body()));
            }
            return response.body();
        } catch (ApiException e) {
            throw e;
        } catch (java.net.http.HttpTimeoutException e) {
            throw new TimeoutException("Download timed out: " + job.getAssetUrl(), e);
        } catch (IOException e) {
            throw new NetworkException("Network error downloading asset: " + job.getAssetUrl(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new NetworkException("Download interrupted: " + job.getAssetUrl(), e);
        }
    }

    // --- Private helpers ---

    private void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " must not be empty");
        }
    }

    private void validateStorageParams(StorageParams params) {
        if (params == null) throw new ValidationException("StorageParams must not be null");
        if (params.getTitle() == null || params.getTitle().isEmpty())
            throw new ValidationException("storage title must not be empty");
        if (params.getRegion() == null || params.getRegion().isEmpty())
            throw new ValidationException("storage region must not be empty");
        if (params.getAccessKeyId() == null || params.getAccessKeyId().isEmpty())
            throw new ValidationException("storage accessKeyId must not be empty");
        if (params.getSecretAccessKey() == null || params.getSecretAccessKey().isEmpty())
            throw new ValidationException("storage secretAccessKey must not be empty");
        if (params.getBucket() == null || params.getBucket().isEmpty())
            throw new ValidationException("storage bucket must not be empty");
    }
}
