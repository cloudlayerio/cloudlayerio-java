package io.cloudlayer.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Storage configuration detail. Note: the public API only returns {@code title} and {@code id},
 * not the encrypted data fields.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class StorageDetail {

    @JsonProperty("title") private String title;
    @JsonProperty("id") private String id;

    @SuppressWarnings("unused")
    private StorageDetail() {}

    public String getTitle() { return title; }
    public String getId() { return id; }
}
