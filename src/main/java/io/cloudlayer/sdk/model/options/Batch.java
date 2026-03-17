package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Batch {

    @JsonProperty("urls")
    private final List<String> urls;

    private Batch(List<String> urls) {
        this.urls = urls != null ? Collections.unmodifiableList(new ArrayList<>(urls)) : null;
    }

    @SuppressWarnings("unused")
    private Batch() {
        this.urls = null;
    }

    public static Batch of(List<String> urls) {
        Objects.requireNonNull(urls, "urls must not be null");
        return new Batch(urls);
    }

    public List<String> getUrls() { return urls; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Batch)) return false;
        return Objects.equals(urls, ((Batch) o).urls);
    }

    @Override
    public int hashCode() { return Objects.hash(urls); }
}
