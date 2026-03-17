package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class BaseOptions {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("timeout")
    private final Integer timeout;

    @JsonProperty("delay")
    private final Integer delay;

    @JsonProperty("filename")
    private final String filename;

    @JsonProperty("inline")
    private final Boolean inline;

    @JsonProperty("async")
    private final Boolean async;

    @JsonProperty("storage")
    private final StorageOption storage;

    @JsonProperty("webhook")
    private final String webhook;

    @JsonProperty("apiVer")
    private final String apiVer;

    @JsonProperty("projectId")
    private final String projectId;

    private BaseOptions(Builder builder) {
        this.name = builder.name;
        this.timeout = builder.timeout;
        this.delay = builder.delay;
        this.filename = builder.filename;
        this.inline = builder.inline;
        this.async = builder.async;
        this.storage = builder.storage;
        this.webhook = builder.webhook;
        this.apiVer = builder.apiVer;
        this.projectId = builder.projectId;
    }

    @SuppressWarnings("unused")
    private BaseOptions() {
        this.name = null;
        this.timeout = null;
        this.delay = null;
        this.filename = null;
        this.inline = null;
        this.async = null;
        this.storage = null;
        this.webhook = null;
        this.apiVer = null;
        this.projectId = null;
    }

    public static Builder builder() { return new Builder(); }

    public String getName() { return name; }
    public Integer getTimeout() { return timeout; }
    public Integer getDelay() { return delay; }
    public String getFilename() { return filename; }
    public Boolean getInline() { return inline; }
    public Boolean getAsync() { return async; }
    public StorageOption getStorage() { return storage; }
    public String getWebhook() { return webhook; }
    public String getApiVer() { return apiVer; }
    public String getProjectId() { return projectId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseOptions)) return false;
        BaseOptions b = (BaseOptions) o;
        return Objects.equals(name, b.name) && Objects.equals(timeout, b.timeout)
                && Objects.equals(async, b.async) && Objects.equals(storage, b.storage);
    }

    @Override
    public int hashCode() { return Objects.hash(name, timeout, async, storage); }

    public static final class Builder {
        private String name;
        private Integer timeout;
        private Integer delay;
        private String filename;
        private Boolean inline;
        private Boolean async;
        private StorageOption storage;
        private String webhook;
        private String apiVer;
        private String projectId;

        private Builder() {}

        public Builder name(String name) { this.name = name; return this; }
        public Builder timeout(int timeout) { this.timeout = timeout; return this; }
        public Builder delay(int delay) { this.delay = delay; return this; }
        public Builder filename(String filename) { this.filename = filename; return this; }
        public Builder inline(boolean inline) { this.inline = inline; return this; }
        public Builder async(boolean async) { this.async = async; return this; }
        public Builder storage(StorageOption storage) { this.storage = storage; return this; }
        public Builder storage(boolean storage) { this.storage = StorageOption.of(storage); return this; }
        public Builder webhook(String webhook) { this.webhook = webhook; return this; }
        public Builder apiVer(String apiVer) { this.apiVer = apiVer; return this; }
        public Builder projectId(String projectId) { this.projectId = projectId; return this; }

        public BaseOptions build() { return new BaseOptions(this); }
    }
}
