package io.cloudlayer.sdk.exception;

/**
 * Thrown when the API returns HTTP 429 (Too Many Requests).
 */
public class RateLimitException extends ApiException {

    private static final long serialVersionUID = 1L;

    private final Integer retryAfterSeconds;

    public RateLimitException(String path, String method, String responseBody, Integer retryAfterSeconds) {
        super(429, "Too Many Requests", path, method, responseBody);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    /**
     * Returns the number of seconds to wait before retrying, or null if the server did not include a Retry-After header.
     */
    public Integer getRetryAfterSeconds() { return retryAfterSeconds; }
}
