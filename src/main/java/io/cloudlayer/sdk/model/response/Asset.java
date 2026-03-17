package io.cloudlayer.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Represents a CloudLayer.io asset (output file from a conversion job).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Asset {

    @JsonProperty("uid") private String uid;
    @JsonProperty("id") private String id;
    @JsonProperty("fileId") private String fileId;
    @JsonProperty("previewFileId") private String previewFileId;
    @JsonProperty("type") private String type;
    @JsonProperty("ext") private String ext;
    @JsonProperty("previewExt") private String previewExt;
    @JsonProperty("url") private String url;
    @JsonProperty("previewUrl") private String previewUrl;
    @JsonProperty("size") private Integer size;
    @JsonProperty("timestamp") private Object timestamp;
    @JsonProperty("projectId") private String projectId;
    @JsonProperty("jobId") private String jobId;
    @JsonProperty("name") private String name;

    @SuppressWarnings("unused")
    private Asset() {}

    public String getUid() { return uid; }
    public String getId() { return id; }
    public String getFileId() { return fileId; }
    public String getPreviewFileId() { return previewFileId; }
    public String getType() { return type; }
    public String getExt() { return ext; }
    public String getPreviewExt() { return previewExt; }
    public String getUrl() { return url; }
    public String getPreviewUrl() { return previewUrl; }
    public Integer getSize() { return size; }
    public Object getTimestamp() { return timestamp; }
    public String getProjectId() { return projectId; }
    public String getJobId() { return jobId; }
    public String getName() { return name; }

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
