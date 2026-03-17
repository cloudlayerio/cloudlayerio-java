package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.cloudlayer.sdk.model.constants.PdfFormat;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PdfOptions {

    @JsonProperty("printBackground")
    private final Boolean printBackground;

    @JsonProperty("format")
    private final PdfFormat format;

    @JsonProperty("margin")
    private final Margin margin;

    @JsonProperty("headerTemplate")
    private final HeaderFooterTemplate headerTemplate;

    @JsonProperty("footerTemplate")
    private final HeaderFooterTemplate footerTemplate;

    @JsonProperty("generatePreview")
    private final GeneratePreviewOption generatePreview;

    private PdfOptions(Builder builder) {
        this.printBackground = builder.printBackground;
        this.format = builder.format;
        this.margin = builder.margin;
        this.headerTemplate = builder.headerTemplate;
        this.footerTemplate = builder.footerTemplate;
        this.generatePreview = builder.generatePreview;
    }

    @SuppressWarnings("unused")
    private PdfOptions() {
        this.printBackground = null;
        this.format = null;
        this.margin = null;
        this.headerTemplate = null;
        this.footerTemplate = null;
        this.generatePreview = null;
    }

    public static Builder builder() { return new Builder(); }

    public Boolean getPrintBackground() { return printBackground; }
    public PdfFormat getFormat() { return format; }
    public Margin getMargin() { return margin; }
    public HeaderFooterTemplate getHeaderTemplate() { return headerTemplate; }
    public HeaderFooterTemplate getFooterTemplate() { return footerTemplate; }
    public GeneratePreviewOption getGeneratePreview() { return generatePreview; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdfOptions)) return false;
        PdfOptions p = (PdfOptions) o;
        return Objects.equals(printBackground, p.printBackground) && format == p.format
                && Objects.equals(margin, p.margin) && Objects.equals(headerTemplate, p.headerTemplate)
                && Objects.equals(footerTemplate, p.footerTemplate) && Objects.equals(generatePreview, p.generatePreview);
    }

    @Override
    public int hashCode() { return Objects.hash(printBackground, format, margin, headerTemplate, footerTemplate, generatePreview); }

    public static final class Builder {
        private Boolean printBackground;
        private PdfFormat format;
        private Margin margin;
        private HeaderFooterTemplate headerTemplate;
        private HeaderFooterTemplate footerTemplate;
        private GeneratePreviewOption generatePreview;

        private Builder() {}

        public Builder printBackground(boolean printBackground) { this.printBackground = printBackground; return this; }
        public Builder format(PdfFormat format) { this.format = format; return this; }
        public Builder margin(Margin margin) { this.margin = margin; return this; }
        public Builder headerTemplate(HeaderFooterTemplate headerTemplate) { this.headerTemplate = headerTemplate; return this; }
        public Builder footerTemplate(HeaderFooterTemplate footerTemplate) { this.footerTemplate = footerTemplate; return this; }
        public Builder generatePreview(GeneratePreviewOption generatePreview) { this.generatePreview = generatePreview; return this; }
        public Builder generatePreview(boolean value) { this.generatePreview = GeneratePreviewOption.of(value); return this; }

        public PdfOptions build() { return new PdfOptions(this); }
    }
}
