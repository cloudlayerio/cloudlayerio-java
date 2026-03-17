package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.cloudlayer.sdk.model.constants.ImageType;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ImageOptions {

    @JsonProperty("imageType")
    private final ImageType imageType;

    @JsonProperty("quality")
    private final Integer quality;

    @JsonProperty("trim")
    private final Boolean trim;

    @JsonProperty("transparent")
    private final Boolean transparent;

    @JsonProperty("generatePreview")
    private final GeneratePreviewOption generatePreview;

    private ImageOptions(Builder builder) {
        this.imageType = builder.imageType;
        this.quality = builder.quality;
        this.trim = builder.trim;
        this.transparent = builder.transparent;
        this.generatePreview = builder.generatePreview;
    }

    @SuppressWarnings("unused")
    private ImageOptions() {
        this.imageType = null;
        this.quality = null;
        this.trim = null;
        this.transparent = null;
        this.generatePreview = null;
    }

    public static Builder builder() { return new Builder(); }

    public ImageType getImageType() { return imageType; }
    public Integer getQuality() { return quality; }
    public Boolean getTrim() { return trim; }
    public Boolean getTransparent() { return transparent; }
    public GeneratePreviewOption getGeneratePreview() { return generatePreview; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageOptions)) return false;
        ImageOptions i = (ImageOptions) o;
        return imageType == i.imageType && Objects.equals(quality, i.quality)
                && Objects.equals(trim, i.trim) && Objects.equals(transparent, i.transparent)
                && Objects.equals(generatePreview, i.generatePreview);
    }

    @Override
    public int hashCode() { return Objects.hash(imageType, quality, trim, transparent, generatePreview); }

    public static final class Builder {
        private ImageType imageType;
        private Integer quality;
        private Boolean trim;
        private Boolean transparent;
        private GeneratePreviewOption generatePreview;

        private Builder() {}

        public Builder imageType(ImageType imageType) { this.imageType = imageType; return this; }
        public Builder quality(int quality) { this.quality = quality; return this; }
        public Builder trim(boolean trim) { this.trim = trim; return this; }
        public Builder transparent(boolean transparent) { this.transparent = transparent; return this; }
        public Builder generatePreview(GeneratePreviewOption generatePreview) { this.generatePreview = generatePreview; return this; }
        public Builder generatePreview(boolean value) { this.generatePreview = GeneratePreviewOption.of(value); return this; }

        public ImageOptions build() { return new ImageOptions(this); }
    }
}
