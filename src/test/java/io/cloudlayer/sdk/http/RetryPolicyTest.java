package io.cloudlayer.sdk.http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RetryPolicyTest {

    @Test
    void retryableStatusCodes() {
        RetryPolicy policy = new RetryPolicy(3);
        assertThat(policy.isRetryableStatus(429)).isTrue();
        assertThat(policy.isRetryableStatus(500)).isTrue();
        assertThat(policy.isRetryableStatus(502)).isTrue();
        assertThat(policy.isRetryableStatus(503)).isTrue();
        assertThat(policy.isRetryableStatus(504)).isTrue();
    }

    @Test
    void nonRetryableStatusCodes() {
        RetryPolicy policy = new RetryPolicy(3);
        assertThat(policy.isRetryableStatus(400)).isFalse();
        assertThat(policy.isRetryableStatus(401)).isFalse();
        assertThat(policy.isRetryableStatus(403)).isFalse();
        assertThat(policy.isRetryableStatus(404)).isFalse();
        assertThat(policy.isRetryableStatus(422)).isFalse();
    }

    @Test
    void shouldRetry_withinLimit() {
        RetryPolicy policy = new RetryPolicy(3);
        assertThat(policy.shouldRetry(0)).isTrue();
        assertThat(policy.shouldRetry(1)).isTrue();
        assertThat(policy.shouldRetry(2)).isTrue();
        assertThat(policy.shouldRetry(3)).isFalse();
    }

    @Test
    void shouldRetry_zeroMaxRetries() {
        RetryPolicy policy = new RetryPolicy(0);
        assertThat(policy.shouldRetry(0)).isFalse();
    }

    @Test
    void backoffMillis_exponential() {
        RetryPolicy policy = new RetryPolicy(5);
        // Attempt 0: 2^0 = 1s base
        long backoff0 = policy.getBackoffMillis(0);
        assertThat(backoff0).isBetween(1000L, 1500L);

        // Attempt 1: 2^1 = 2s base
        long backoff1 = policy.getBackoffMillis(1);
        assertThat(backoff1).isBetween(2000L, 2500L);

        // Attempt 2: 2^2 = 4s base
        long backoff2 = policy.getBackoffMillis(2);
        assertThat(backoff2).isBetween(4000L, 4500L);

        // Attempt 4: min(2^4, 16) = 16s base
        long backoff4 = policy.getBackoffMillis(4);
        assertThat(backoff4).isBetween(16000L, 16500L);

        // Attempt 10: capped at 16s
        long backoff10 = policy.getBackoffMillis(10);
        assertThat(backoff10).isBetween(16000L, 16500L);
    }

    @Test
    void backoffMillis_respectsRetryAfter() {
        RetryPolicy policy = new RetryPolicy(3);
        long backoff = policy.getBackoffMillis(0, 30);
        assertThat(backoff).isBetween(30000L, 30500L);
    }

    @Test
    void backoffMillis_nullRetryAfter_usesExponential() {
        RetryPolicy policy = new RetryPolicy(3);
        long backoff = policy.getBackoffMillis(0, null);
        assertThat(backoff).isBetween(1000L, 1500L);
    }
}
