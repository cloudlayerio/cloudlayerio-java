package io.cloudlayer.sdk.model.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.cloudlayer.sdk.model.options.*;

import java.util.List;

/**
 * Options for the Merge PDFs endpoint.
 * Composes: UrlOptions + BaseOptions
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class MergePdfsOptions {

    // UrlOptions
    @JsonProperty("url") private final String url;
    @JsonProperty("authentication") private final Authentication authentication;
    @JsonProperty("batch") private final Batch batch;
    @JsonProperty("cookies") private final List<Cookie> cookies;

    // BaseOptions
    @JsonProperty("name") private final String name;
    @JsonProperty("timeout") private final Integer timeout;
    @JsonProperty("delay") private final Integer delay;
    @JsonProperty("filename") private final String filename;
    @JsonProperty("inline") private final Boolean inline;
    @JsonProperty("async") private final Boolean async;
    @JsonProperty("storage") private final StorageOption storage;
    @JsonProperty("webhook") private final String webhook;
    @JsonProperty("apiVer") private final String apiVer;
    @JsonProperty("projectId") private final String projectId;

    private MergePdfsOptions(Builder b) {
        this.url = b.url; this.authentication = b.authentication; this.batch = b.batch; this.cookies = b.cookies;
        this.name = b.name; this.timeout = b.timeout; this.delay = b.delay; this.filename = b.filename;
        this.inline = b.inline; this.async = b.async; this.storage = b.storage; this.webhook = b.webhook;
        this.apiVer = b.apiVer; this.projectId = b.projectId;
    }

    public static Builder builder() { return new Builder(); }

    // Getters
    public String getUrl() { return url; }
    public Authentication getAuthentication() { return authentication; }
    public Batch getBatch() { return batch; }
    public List<Cookie> getCookies() { return cookies; }
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

    public static final class Builder {
        // UrlOptions
        private String url;
        private Authentication authentication;
        private Batch batch;
        private List<Cookie> cookies;
        // BaseOptions
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

        // UrlOptions setters
        public Builder url(String url) { this.url = url; return this; }
        public Builder authentication(Authentication auth) { this.authentication = auth; return this; }
        public Builder batch(Batch batch) { this.batch = batch; return this; }
        public Builder cookies(List<Cookie> cookies) { this.cookies = cookies; return this; }
        // BaseOptions setters
        public Builder name(String val) { this.name = val; return this; }
        public Builder timeout(int val) { this.timeout = val; return this; }
        public Builder delay(int val) { this.delay = val; return this; }
        public Builder filename(String val) { this.filename = val; return this; }
        public Builder inline(boolean val) { this.inline = val; return this; }
        public Builder async(boolean val) { this.async = val; return this; }
        public Builder storage(StorageOption val) { this.storage = val; return this; }
        public Builder storage(boolean val) { this.storage = StorageOption.of(val); return this; }
        public Builder webhook(String val) { this.webhook = val; return this; }
        public Builder apiVer(String val) { this.apiVer = val; return this; }
        public Builder projectId(String val) { this.projectId = val; return this; }

        public MergePdfsOptions build() { return new MergePdfsOptions(this); }
    }
}
