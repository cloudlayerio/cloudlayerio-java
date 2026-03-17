package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.cloudlayer.sdk.model.constants.ImageType;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PreviewOptions {

    @JsonProperty("width")
    private final Integer width;

    @JsonProperty("height")
    private final Integer height;

    @JsonProperty("type")
    private final ImageType type;

    @JsonProperty("quality")
    private final Integer quality;

    @JsonProperty("maintainAspectRatio")
    private final Boolean maintainAspectRatio;

    private PreviewOptions(Builder builder) {
        this.width = builder.width;
        this.height = builder.height;
        this.type = builder.type;
        this.quality = builder.quality;
        this.maintainAspectRatio = builder.maintainAspectRatio;
    }

    @SuppressWarnings("unused")
    private PreviewOptions() {
        this.width = null;
        this.height = null;
        this.type = null;
        this.quality = null;
        this.maintainAspectRatio = null;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getWidth() { return width; }
    public Integer getHeight() { return height; }
    public ImageType getType() { return type; }
    public Integer getQuality() { return quality; }
    public Boolean getMaintainAspectRatio() { return maintainAspectRatio; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreviewOptions)) return false;
        PreviewOptions that = (PreviewOptions) o;
        return Objects.equals(width, that.width) && Objects.equals(height, that.height)
                && type == that.type && Objects.equals(quality, that.quality)
                && Objects.equals(maintainAspectRatio, that.maintainAspectRatio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, type, quality, maintainAspectRatio);
    }

    public static final class Builder {
        private Integer width;
        private Integer height;
        private ImageType type;
        private Integer quality;
        private Boolean maintainAspectRatio;

        private Builder() {}

        public Builder width(int width) { this.width = width; return this; }
        public Builder height(int height) { this.height = height; return this; }
        public Builder type(ImageType type) { this.type = type; return this; }
        public Builder quality(int quality) { this.quality = quality; return this; }
        public Builder maintainAspectRatio(boolean maintainAspectRatio) { this.maintainAspectRatio = maintainAspectRatio; return this; }

        public PreviewOptions build() {
            return new PreviewOptions(this);
        }
    }
}
