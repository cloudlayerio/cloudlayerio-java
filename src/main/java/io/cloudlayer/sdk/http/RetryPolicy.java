package io.cloudlayer.sdk.http;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

/**
 * Retry policy with exponential backoff and jitter.
 */
public final class RetryPolicy {

    private static final Logger LOG = Logger.getLogger(RetryPolicy.class.getName());
    private static final Set<Integer> RETRYABLE_STATUS_CODES = Set.of(429, 500, 502, 503, 504);
    private static final long MAX_BACKOFF_SECONDS = 16;
    private static final long MAX_JITTER_MS = 500;

    private final int maxRetries;

    public RetryPolicy(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    /**
     * Returns true if the given HTTP status code should be retried.
     */
    public boolean isRetryableStatus(int statusCode) {
        return RETRYABLE_STATUS_CODES.contains(statusCode);
    }

    /**
     * Returns whether another retry attempt should be made.
     */
    public boolean shouldRetry(int attempt) {
        return attempt < maxRetries;
    }

    /**
     * Computes the backoff delay for the given attempt (0-based).
     * Formula: min(2^attempt, 16) seconds + random jitter [0, 500ms]
     */
    public long getBackoffMillis(int attempt) {
        long baseSeconds = Math.min((long) Math.pow(2, attempt), MAX_BACKOFF_SECONDS);
        long jitter = ThreadLocalRandom.current().nextLong(MAX_JITTER_MS);
        return baseSeconds * 1000 + jitter;
    }

    /**
     * Computes backoff respecting Retry-After header if present.
     * Returns delay in milliseconds.
     */
    public long getBackoffMillis(int attempt, Integer retryAfterSeconds) {
        if (retryAfterSeconds != null && retryAfterSeconds > 0) {
            long jitter = ThreadLocalRandom.current().nextLong(MAX_JITTER_MS);
            return retryAfterSeconds * 1000L + jitter;
        }
        return getBackoffMillis(attempt);
    }

    /**
     * Sleeps for the computed backoff duration. Handles InterruptedException by
     * re-interrupting the current thread.
     *
     * @return true if sleep completed normally, false if interrupted
     */
    public boolean sleep(long millis) {
        try {
            LOG.fine("Retry backoff: sleeping " + millis + "ms");
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
