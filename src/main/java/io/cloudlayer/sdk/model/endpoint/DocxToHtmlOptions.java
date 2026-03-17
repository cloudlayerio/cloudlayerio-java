package io.cloudlayer.sdk.model.endpoint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.cloudlayer.sdk.model.options.StorageOption;
import io.cloudlayer.sdk.util.FileInput;

/**
 * Options for the DOCX to HTML conversion endpoint.
 * Composes: FileInput + BaseOptions
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class DocxToHtmlOptions {

    // FileInput (not serialized — used for multipart upload)
    @JsonIgnore private final FileInput file;

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

    private DocxToHtmlOptions(Builder b) {
        this.file = b.file;
        this.name = b.name; this.timeout = b.timeout; this.delay = b.delay; this.filename = b.filename;
        this.inline = b.inline; this.async = b.async; this.storage = b.storage; this.webhook = b.webhook;
        this.apiVer = b.apiVer; this.projectId = b.projectId;
    }

    public static Builder builder() { return new Builder(); }

    // Getters
    public FileInput getFile() { return file; }
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
        // FileInput
        private FileInput file;
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

        // FileInput setter
        public Builder file(FileInput file) { this.file = file; return this; }
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

        public DocxToHtmlOptions build() { return new DocxToHtmlOptions(this); }
    }
}
