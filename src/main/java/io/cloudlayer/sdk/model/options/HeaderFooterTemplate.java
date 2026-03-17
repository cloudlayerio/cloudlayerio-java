package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class HeaderFooterTemplate {

    @JsonProperty("method")
    private final String method;

    @JsonProperty("selector")
    private final String selector;

    @JsonProperty("margin")
    private final Margin margin;

    @JsonProperty("style")
    private final Map<String, Object> style;

    @JsonProperty("imageStyle")
    private final Map<String, Object> imageStyle;

    @JsonProperty("template")
    private final String template;

    @JsonProperty("templateString")
    private final String templateString;

    private HeaderFooterTemplate(Builder builder) {
        this.method = builder.method;
        this.selector = builder.selector;
        this.margin = builder.margin;
        this.style = builder.style;
        this.imageStyle = builder.imageStyle;
        this.template = builder.template;
        this.templateString = builder.templateString;
    }

    @SuppressWarnings("unused")
    private HeaderFooterTemplate() {
        this.method = null;
        this.selector = null;
        this.margin = null;
        this.style = null;
        this.imageStyle = null;
        this.template = null;
        this.templateString = null;
    }

    public static Builder builder() { return new Builder(); }

    public String getMethod() { return method; }
    public String getSelector() { return selector; }
    public Margin getMargin() { return margin; }
    public Map<String, Object> getStyle() { return style; }
    public Map<String, Object> getImageStyle() { return imageStyle; }
    public String getTemplate() { return template; }
    public String getTemplateString() { return templateString; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeaderFooterTemplate)) return false;
        HeaderFooterTemplate h = (HeaderFooterTemplate) o;
        return Objects.equals(method, h.method) && Objects.equals(selector, h.selector)
                && Objects.equals(margin, h.margin) && Objects.equals(style, h.style)
                && Objects.equals(imageStyle, h.imageStyle) && Objects.equals(template, h.template)
                && Objects.equals(templateString, h.templateString);
    }

    @Override
    public int hashCode() { return Objects.hash(method, selector, margin, style, imageStyle, template, templateString); }

    public static final class Builder {
        private String method;
        private String selector;
        private Margin margin;
        private Map<String, Object> style;
        private Map<String, Object> imageStyle;
        private String template;
        private String templateString;

        private Builder() {}

        public Builder method(String method) { this.method = method; return this; }
        public Builder selector(String selector) { this.selector = selector; return this; }
        public Builder margin(Margin margin) { this.margin = margin; return this; }
        public Builder style(Map<String, Object> style) { this.style = style; return this; }
        public Builder imageStyle(Map<String, Object> imageStyle) { this.imageStyle = imageStyle; return this; }
        public Builder template(String template) { this.template = template; return this; }
        public Builder templateString(String templateString) { this.templateString = templateString; return this; }

        public HeaderFooterTemplate build() { return new HeaderFooterTemplate(this); }
    }
}
