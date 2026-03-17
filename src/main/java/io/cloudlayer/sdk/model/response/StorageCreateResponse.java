package io.cloudlayer.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class StorageCreateResponse {

    @JsonProperty("title") private String title;
    @JsonProperty("id") private String id;

    @SuppressWarnings("unused")
    private StorageCreateResponse() {}

    public String getTitle() { return title; }
    public String getId() { return id; }
}
