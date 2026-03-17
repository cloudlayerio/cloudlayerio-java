package io.cloudlayer.sdk;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

/**
 * Immutable configuration for a {@link CloudLayer} client instance.
 */
public final class CloudLayerConfig {

    private final String apiKey;
    private final ApiVersion apiVersion;
    private final String baseUrl;
    private final Duration timeout;
    private final int maxRetries;
    private final HttpClient httpClient;
    private final String userAgent;
    private final Map<String, String> customHeaders;

    CloudLayerConfig(String apiKey, ApiVersion apiVersion, String baseUrl, Duration timeout,
                     int maxRetries, HttpClient httpClient, String userAgent, Map<String, String> customHeaders) {
        this.apiKey = apiKey;
        this.apiVersion = apiVersion;
        this.baseUrl = baseUrl;
        this.timeout = timeout;
        this.maxRetries = maxRetries;
        this.httpClient = httpClient;
        this.userAgent = userAgent;
        this.customHeaders = customHeaders != null ? Collections.unmodifiableMap(customHeaders) : Collections.emptyMap();
    }

    public String getApiKey() { return apiKey; }
    public ApiVersion getApiVersion() { return apiVersion; }
    public String getBaseUrl() { return baseUrl; }
    public Duration getTimeout() { return timeout; }
    public int getMaxRetries() { return maxRetries; }
    public HttpClient getHttpClient() { return httpClient; }
    public String getUserAgent() { return userAgent; }
    public Map<String, String> getCustomHeaders() { return customHeaders; }
}
