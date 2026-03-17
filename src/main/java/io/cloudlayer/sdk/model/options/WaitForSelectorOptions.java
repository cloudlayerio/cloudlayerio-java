package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class WaitForSelectorOptions {

    @JsonProperty("selector")
    private final String selector;

    @JsonProperty("options")
    private final SelectorSubOptions options;

    private WaitForSelectorOptions(Builder builder) {
        this.selector = builder.selector;
        this.options = builder.options;
    }

    @SuppressWarnings("unused")
    private WaitForSelectorOptions() {
        this.selector = null;
        this.options = null;
    }

    public static Builder builder() { return new Builder(); }

    public String getSelector() { return selector; }
    public SelectorSubOptions getOptions() { return options; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaitForSelectorOptions)) return false;
        WaitForSelectorOptions w = (WaitForSelectorOptions) o;
        return Objects.equals(selector, w.selector) && Objects.equals(options, w.options);
    }

    @Override
    public int hashCode() { return Objects.hash(selector, options); }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class SelectorSubOptions {

        @JsonProperty("visible")
        private final Boolean visible;

        @JsonProperty("hidden")
        private final Boolean hidden;

        @JsonProperty("timeout")
        private final Integer timeout;

        private SelectorSubOptions(Boolean visible, Boolean hidden, Integer timeout) {
            this.visible = visible;
            this.hidden = hidden;
            this.timeout = timeout;
        }

        @SuppressWarnings("unused")
        private SelectorSubOptions() {
            this.visible = null;
            this.hidden = null;
            this.timeout = null;
        }

        public static SelectorSubOptions of(Boolean visible, Boolean hidden, Integer timeout) {
            return new SelectorSubOptions(visible, hidden, timeout);
        }

        public Boolean getVisible() { return visible; }
        public Boolean getHidden() { return hidden; }
        public Integer getTimeout() { return timeout; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SelectorSubOptions)) return false;
            SelectorSubOptions s = (SelectorSubOptions) o;
            return Objects.equals(visible, s.visible) && Objects.equals(hidden, s.hidden) && Objects.equals(timeout, s.timeout);
        }

        @Override
        public int hashCode() { return Objects.hash(visible, hidden, timeout); }
    }

    public static final class Builder {
        private String selector;
        private SelectorSubOptions options;

        private Builder() {}

        public Builder selector(String selector) { this.selector = selector; return this; }
        public Builder options(SelectorSubOptions options) { this.options = options; return this; }
        public Builder options(Boolean visible, Boolean hidden, Integer timeout) {
            this.options = SelectorSubOptions.of(visible, hidden, timeout);
            return this;
        }

        public WaitForSelectorOptions build() { return new WaitForSelectorOptions(this); }
    }
}
