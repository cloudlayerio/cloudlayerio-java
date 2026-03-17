package io.cloudlayer.sdk.http;

import java.util.Map;

/**
 * Internal request options controlling transport behavior.
 */
public final class RequestOptions {

    private final boolean retryable;
    private final boolean absolutePath;
    private final Map<String, String> queryParams;

    private RequestOptions(boolean retryable, boolean absolutePath, Map<String, String> queryParams) {
        this.retryable = retryable;
        this.absolutePath = absolutePath;
        this.queryParams = queryParams;
    }

    public static RequestOptions of(boolean retryable) {
        return new RequestOptions(retryable, false, null);
    }

    public static RequestOptions of(boolean retryable, boolean absolutePath) {
        return new RequestOptions(retryable, absolutePath, null);
    }

    public static RequestOptions of(boolean retryable, boolean absolutePath, Map<String, String> queryParams) {
        return new RequestOptions(retryable, absolutePath, queryParams);
    }

    public boolean isRetryable() { return retryable; }
    public boolean isAbsolutePath() { return absolutePath; }
    public Map<String, String> getQueryParams() { return queryParams; }
}
