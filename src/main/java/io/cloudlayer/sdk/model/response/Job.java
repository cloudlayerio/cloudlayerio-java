package io.cloudlayer.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.cloudlayer.sdk.model.constants.JobStatus;
import io.cloudlayer.sdk.model.constants.JobType;

import java.util.Map;

/**
 * Represents a CloudLayer.io conversion job.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Job {

    @JsonProperty("id")
    private String id;

    @JsonProperty("uid")
    private String uid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private JobType type;

    @JsonProperty("status")
    private JobStatus status;

    @JsonProperty("timestamp")
    private Object timestamp;

    @JsonProperty("workerName")
    private String workerName;

    @JsonProperty("processTime")
    private Integer processTime;

    @JsonProperty("apiKeyUsed")
    private String apiKeyUsed;

    @JsonProperty("processTimeCost")
    private Double processTimeCost;

    @JsonProperty("apiCreditCost")
    private Double apiCreditCost;

    @JsonProperty("bandwidthCost")
    private Double bandwidthCost;

    @JsonProperty("totalCost")
    private Double totalCost;

    @JsonProperty("size")
    private Integer size;

    @JsonProperty("params")
    private Map<String, Object> params;

    @JsonProperty("assetUrl")
    private String assetUrl;

    @JsonProperty("previewUrl")
    private String previewUrl;

    @JsonProperty("self")
    private String self;

    @JsonProperty("assetId")
    private String assetId;

    @JsonProperty("projectId")
    private String projectId;

    @JsonProperty("error")
    private String error;

    @SuppressWarnings("unused")
    private Job() {}

    public String getId() { return id; }
    public String getUid() { return uid; }
    public String getName() { return name; }
    public JobType getType() { return type; }
    public JobStatus getStatus() { return status; }
    public Object getTimestamp() { return timestamp; }
    public String getWorkerName() { return workerName; }
    public Integer getProcessTime() { return processTime; }
    public String getApiKeyUsed() { return apiKeyUsed; }
    public Double getProcessTimeCost() { return processTimeCost; }
    public Double getApiCreditCost() { return apiCreditCost; }
    public Double getBandwidthCost() { return bandwidthCost; }
    public Double getTotalCost() { return totalCost; }
    public Integer getSize() { return size; }
    public Map<String, Object> getParams() { return params; }
    public String getAssetUrl() { return assetUrl; }
    public String getPreviewUrl() { return previewUrl; }
    public String getSelf() { return self; }
    public String getAssetId() { return assetId; }
    public String getProjectId() { return projectId; }
    public String getError() { return error; }

    /**
     * Parses the timestamp into epoch milliseconds.
     * Handles both numeric seconds and Firestore {@code {_seconds, _nanoseconds}} format.
     */
    @SuppressWarnings("unchecked")
    public Long getTimestampMillis() {
        if (timestamp == null) return null;
        if (timestamp instanceof Number) {
            return ((Number) timestamp).longValue() * 1000;
        }
        if (timestamp instanceof Map) {
            Map<String, Object> ts = (Map<String, Object>) timestamp;
            Object seconds = ts.get("_seconds");
            if (seconds instanceof Number) {
                long millis = ((Number) seconds).longValue() * 1000;
                Object nanos = ts.get("_nanoseconds");
                if (nanos instanceof Number) {
                    millis += ((Number) nanos).longValue() / 1_000_000;
                }
                return millis;
            }
        }
        return null;
    }
}
