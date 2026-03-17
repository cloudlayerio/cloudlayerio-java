package io.cloudlayer.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A public template from the CloudLayer.io template library.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class PublicTemplate {

    @JsonProperty("id") private String id;
    @JsonProperty("templateId") private String templateId;
    @JsonProperty("title") private String title;
    @JsonProperty("shortDescription") private String shortDescription;
    @JsonProperty("searchKeywords") private List<String> searchKeywords;
    @JsonProperty("tags") private List<String> tags;
    @JsonProperty("category") private String category;
    @JsonProperty("type") private String type;
    @JsonProperty("previewUrl") private String previewUrl;
    @JsonProperty("exampleAssetUrl") private String exampleAssetUrl;
    @JsonProperty("highlights") private List<String> highlights;
    @JsonProperty("timestamp") private Object timestamp;
    @JsonProperty("projectId") private String projectId;
    @JsonProperty("sampleData") private Map<String, Object> sampleData;

    private final Map<String, Object> extras = new HashMap<>();

    @SuppressWarnings("unused")
    private PublicTemplate() {}

    public String getId() { return id; }
    public String getTemplateId() { return templateId; }
    public String getTitle() { return title; }
    public String getShortDescription() { return shortDescription; }
    public List<String> getSearchKeywords() { return searchKeywords; }
    public List<String> getTags() { return tags; }
    public String getCategory() { return category; }
    public String getType() { return type; }
    public String getPreviewUrl() { return previewUrl; }
    public String getExampleAssetUrl() { return exampleAssetUrl; }
    public List<String> getHighlights() { return highlights; }
    public Object getTimestamp() { return timestamp; }
    public String getProjectId() { return projectId; }
    public Map<String, Object> getSampleData() { return sampleData; }

    @JsonAnyGetter
    public Map<String, Object> getExtras() { return extras; }

    @JsonAnySetter
    public void setExtra(String key, Object value) { extras.put(key, value); }

    /**
     * Parses the timestamp into epoch milliseconds.
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
