package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Parameters for creating or updating a storage configuration.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class StorageParams {

    @JsonProperty("title")
    private final String title;

    @JsonProperty("region")
    private final String region;

    @JsonProperty("accessKeyId")
    private final String accessKeyId;

    @JsonProperty("secretAccessKey")
    private final String secretAccessKey;

    @JsonProperty("bucket")
    private final String bucket;

    @JsonProperty("endpoint")
    private final String endpoint;

    private StorageParams(Builder builder) {
        this.title = Objects.requireNonNull(builder.title, "title required");
        this.region = Objects.requireNonNull(builder.region, "region required");
        this.accessKeyId = Objects.requireNonNull(builder.accessKeyId, "accessKeyId required");
        this.secretAccessKey = Objects.requireNonNull(builder.secretAccessKey, "secretAccessKey required");
        this.bucket = Objects.requireNonNull(builder.bucket, "bucket required");
        this.endpoint = builder.endpoint;
    }

    @SuppressWarnings("unused")
    private StorageParams() {
        this.title = null;
        this.region = null;
        this.accessKeyId = null;
        this.secretAccessKey = null;
        this.bucket = null;
        this.endpoint = null;
    }

    public static Builder builder(String title, String region, String accessKeyId, String secretAccessKey, String bucket) {
        return new Builder(title, region, accessKeyId, secretAccessKey, bucket);
    }

    public String getTitle() { return title; }
    public String getRegion() { return region; }
    public String getAccessKeyId() { return accessKeyId; }
    public String getSecretAccessKey() { return secretAccessKey; }
    public String getBucket() { return bucket; }
    public String getEndpoint() { return endpoint; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StorageParams)) return false;
        StorageParams s = (StorageParams) o;
        return Objects.equals(title, s.title) && Objects.equals(bucket, s.bucket);
    }

    @Override
    public int hashCode() { return Objects.hash(title, bucket); }

    public static final class Builder {
        private final String title;
        private final String region;
        private final String accessKeyId;
        private final String secretAccessKey;
        private final String bucket;
        private String endpoint;

        private Builder(String title, String region, String accessKeyId, String secretAccessKey, String bucket) {
            this.title = title;
            this.region = region;
            this.accessKeyId = accessKeyId;
            this.secretAccessKey = secretAccessKey;
            this.bucket = bucket;
        }

        public Builder endpoint(String endpoint) { this.endpoint = endpoint; return this; }

        public StorageParams build() { return new StorageParams(this); }
    }
}
