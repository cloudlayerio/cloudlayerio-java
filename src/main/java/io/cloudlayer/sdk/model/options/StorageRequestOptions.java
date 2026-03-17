package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * References a saved storage configuration by ID.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class StorageRequestOptions {

    @JsonProperty("id")
    private final String id;

    private StorageRequestOptions(String id) {
        this.id = Objects.requireNonNull(id, "storage id required");
    }

    @SuppressWarnings("unused")
    private StorageRequestOptions() {
        this.id = null;
    }

    public static StorageRequestOptions of(String id) {
        return new StorageRequestOptions(id);
    }

    public String getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StorageRequestOptions)) return false;
        return Objects.equals(id, ((StorageRequestOptions) o).id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
