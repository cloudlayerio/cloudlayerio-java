package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Viewport {

    @JsonProperty("width")
    private final Integer width;

    @JsonProperty("height")
    private final Integer height;

    @JsonProperty("deviceScaleFactor")
    private final Double deviceScaleFactor;

    @JsonProperty("isMobile")
    private final Boolean isMobile;

    @JsonProperty("hasTouch")
    private final Boolean hasTouch;

    @JsonProperty("isLandscape")
    private final Boolean isLandscape;

    private Viewport(Builder builder) {
        this.width = builder.width;
        this.height = builder.height;
        this.deviceScaleFactor = builder.deviceScaleFactor;
        this.isMobile = builder.isMobile;
        this.hasTouch = builder.hasTouch;
        this.isLandscape = builder.isLandscape;
    }

    @SuppressWarnings("unused")
    private Viewport() {
        this.width = null;
        this.height = null;
        this.deviceScaleFactor = null;
        this.isMobile = null;
        this.hasTouch = null;
        this.isLandscape = null;
    }

    public static Builder builder() { return new Builder(); }

    public Integer getWidth() { return width; }
    public Integer getHeight() { return height; }
    public Double getDeviceScaleFactor() { return deviceScaleFactor; }
    public Boolean getIsMobile() { return isMobile; }
    public Boolean getHasTouch() { return hasTouch; }
    public Boolean getIsLandscape() { return isLandscape; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Viewport)) return false;
        Viewport v = (Viewport) o;
        return Objects.equals(width, v.width) && Objects.equals(height, v.height)
                && Objects.equals(deviceScaleFactor, v.deviceScaleFactor)
                && Objects.equals(isMobile, v.isMobile) && Objects.equals(hasTouch, v.hasTouch)
                && Objects.equals(isLandscape, v.isLandscape);
    }

    @Override
    public int hashCode() { return Objects.hash(width, height, deviceScaleFactor, isMobile, hasTouch, isLandscape); }

    public static final class Builder {
        private Integer width;
        private Integer height;
        private Double deviceScaleFactor;
        private Boolean isMobile;
        private Boolean hasTouch;
        private Boolean isLandscape;

        private Builder() {}

        public Builder width(int width) { this.width = width; return this; }
        public Builder height(int height) { this.height = height; return this; }
        public Builder deviceScaleFactor(double factor) { this.deviceScaleFactor = factor; return this; }
        public Builder isMobile(boolean isMobile) { this.isMobile = isMobile; return this; }
        public Builder hasTouch(boolean hasTouch) { this.hasTouch = hasTouch; return this; }
        public Builder isLandscape(boolean isLandscape) { this.isLandscape = isLandscape; return this; }

        public Viewport build() { return new Viewport(this); }
    }
}
