package io.cloudlayer.sdk.model.options;

import java.time.Duration;
import java.util.Objects;

/**
 * Options for the {@code waitForJob()} polling method.
 */
public final class WaitForJobOptions {

    private static final Duration DEFAULT_INTERVAL = Duration.ofSeconds(5);
    private static final Duration MIN_INTERVAL = Duration.ofSeconds(2);
    private static final Duration DEFAULT_MAX_WAIT = Duration.ofSeconds(300);

    private final Duration interval;
    private final Duration maxWait;

    private WaitForJobOptions(Duration interval, Duration maxWait) {
        this.interval = interval;
        this.maxWait = maxWait;
    }

    public static Builder builder() { return new Builder(); }

    public Duration getInterval() { return interval; }
    public Duration getMaxWait() { return maxWait; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaitForJobOptions)) return false;
        WaitForJobOptions w = (WaitForJobOptions) o;
        return Objects.equals(interval, w.interval) && Objects.equals(maxWait, w.maxWait);
    }

    @Override
    public int hashCode() { return Objects.hash(interval, maxWait); }

    public static final class Builder {
        private Duration interval = DEFAULT_INTERVAL;
        private Duration maxWait = DEFAULT_MAX_WAIT;

        private Builder() {}

        public Builder interval(Duration interval) {
            Objects.requireNonNull(interval, "interval must not be null");
            if (interval.compareTo(MIN_INTERVAL) < 0) {
                throw new IllegalArgumentException("interval must be at least " + MIN_INTERVAL.getSeconds() + " seconds");
            }
            this.interval = interval;
            return this;
        }

        public Builder maxWait(Duration maxWait) {
            Objects.requireNonNull(maxWait, "maxWait must not be null");
            this.maxWait = maxWait;
            return this;
        }

        public WaitForJobOptions build() { return new WaitForJobOptions(interval, maxWait); }
    }
}
