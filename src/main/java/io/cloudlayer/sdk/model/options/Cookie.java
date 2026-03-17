package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Cookie {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("value")
    private final String value;

    @JsonProperty("url")
    private final String url;

    @JsonProperty("domain")
    private final String domain;

    @JsonProperty("path")
    private final String path;

    @JsonProperty("expires")
    private final Integer expires;

    @JsonProperty("httpOnly")
    private final Boolean httpOnly;

    @JsonProperty("secure")
    private final Boolean secure;

    @JsonProperty("sameSite")
    private final String sameSite;

    private Cookie(Builder builder) {
        this.name = builder.name;
        this.value = builder.value;
        this.url = builder.url;
        this.domain = builder.domain;
        this.path = builder.path;
        this.expires = builder.expires;
        this.httpOnly = builder.httpOnly;
        this.secure = builder.secure;
        this.sameSite = builder.sameSite;
    }

    @SuppressWarnings("unused")
    private Cookie() {
        this.name = null;
        this.value = null;
        this.url = null;
        this.domain = null;
        this.path = null;
        this.expires = null;
        this.httpOnly = null;
        this.secure = null;
        this.sameSite = null;
    }

    public static Builder builder(String name, String value) {
        return new Builder(name, value);
    }

    public String getName() { return name; }
    public String getValue() { return value; }
    public String getUrl() { return url; }
    public String getDomain() { return domain; }
    public String getPath() { return path; }
    public Integer getExpires() { return expires; }
    public Boolean getHttpOnly() { return httpOnly; }
    public Boolean getSecure() { return secure; }
    public String getSameSite() { return sameSite; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cookie)) return false;
        Cookie c = (Cookie) o;
        return Objects.equals(name, c.name) && Objects.equals(value, c.value);
    }

    @Override
    public int hashCode() { return Objects.hash(name, value); }

    public static final class Builder {
        private final String name;
        private final String value;
        private String url;
        private String domain;
        private String path;
        private Integer expires;
        private Boolean httpOnly;
        private Boolean secure;
        private String sameSite;

        private Builder(String name, String value) {
            this.name = Objects.requireNonNull(name, "cookie name required");
            this.value = Objects.requireNonNull(value, "cookie value required");
        }

        public Builder url(String url) { this.url = url; return this; }
        public Builder domain(String domain) { this.domain = domain; return this; }
        public Builder path(String path) { this.path = path; return this; }
        public Builder expires(int expires) { this.expires = expires; return this; }
        public Builder httpOnly(boolean httpOnly) { this.httpOnly = httpOnly; return this; }
        public Builder secure(boolean secure) { this.secure = secure; return this; }
        public Builder sameSite(String sameSite) { this.sameSite = sameSite; return this; }

        public Cookie build() { return new Cookie(this); }
    }
}
