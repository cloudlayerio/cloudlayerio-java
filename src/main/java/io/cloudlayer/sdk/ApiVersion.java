package io.cloudlayer.sdk;

/**
 * API version for CloudLayer.io endpoints.
 */
public enum ApiVersion {
    V1("v1"),
    V2("v2");

    private final String value;

    ApiVersion(String value) {
        this.value = value;
    }

    /**
     * Returns the URL path segment for this API version.
     */
    public String getValue() {
        return value;
    }
}
