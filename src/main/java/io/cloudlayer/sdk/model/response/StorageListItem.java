package io.cloudlayer.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class StorageListItem {

    @JsonProperty("id") private String id;
    @JsonProperty("title") private String title;

    @SuppressWarnings("unused")
    private StorageListItem() {}

    public String getId() { return id; }
    public String getTitle() { return title; }
}
