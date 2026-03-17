package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.cloudlayer.sdk.model.constants.WaitUntilOption;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class PuppeteerOptions {

    @JsonProperty("waitUntil")
    private final WaitUntilOption waitUntil;

    @JsonProperty("waitForFrame")
    private final Boolean waitForFrame;

    @JsonProperty("waitForFrameAttachment")
    private final Boolean waitForFrameAttachment;

    @JsonProperty("waitForFrameNavigation")
    private final WaitUntilOption waitForFrameNavigation;

    @JsonProperty("waitForFrameImages")
    private final Boolean waitForFrameImages;

    @JsonProperty("waitForFrameSelector")
    private final WaitForSelectorOptions waitForFrameSelector;

    @JsonProperty("waitForSelector")
    private final WaitForSelectorOptions waitForSelector;

    @JsonProperty("preferCSSPageSize")
    private final Boolean preferCSSPageSize;

    @JsonProperty("scale")
    private final Double scale;

    @JsonProperty("height")
    private final LayoutDimension height;

    @JsonProperty("width")
    private final LayoutDimension width;

    @JsonProperty("landscape")
    private final Boolean landscape;

    @JsonProperty("pageRanges")
    private final String pageRanges;

    @JsonProperty("autoScroll")
    private final Boolean autoScroll;

    @JsonProperty("viewPort")
    private final Viewport viewPort;

    @JsonProperty("timeZone")
    private final String timeZone;

    @JsonProperty("emulateMediaType")
    private final NullableString emulateMediaType;

    private PuppeteerOptions(Builder builder) {
        this.waitUntil = builder.waitUntil;
        this.waitForFrame = builder.waitForFrame;
        this.waitForFrameAttachment = builder.waitForFrameAttachment;
        this.waitForFrameNavigation = builder.waitForFrameNavigation;
        this.waitForFrameImages = builder.waitForFrameImages;
        this.waitForFrameSelector = builder.waitForFrameSelector;
        this.waitForSelector = builder.waitForSelector;
        this.preferCSSPageSize = builder.preferCSSPageSize;
        this.scale = builder.scale;
        this.height = builder.height;
        this.width = builder.width;
        this.landscape = builder.landscape;
        this.pageRanges = builder.pageRanges;
        this.autoScroll = builder.autoScroll;
        this.viewPort = builder.viewPort;
        this.timeZone = builder.timeZone;
        this.emulateMediaType = builder.emulateMediaType;
    }

    @SuppressWarnings("unused")
    private PuppeteerOptions() {
        this.waitUntil = null;
        this.waitForFrame = null;
        this.waitForFrameAttachment = null;
        this.waitForFrameNavigation = null;
        this.waitForFrameImages = null;
        this.waitForFrameSelector = null;
        this.waitForSelector = null;
        this.preferCSSPageSize = null;
        this.scale = null;
        this.height = null;
        this.width = null;
        this.landscape = null;
        this.pageRanges = null;
        this.autoScroll = null;
        this.viewPort = null;
        this.timeZone = null;
        this.emulateMediaType = null;
    }

    public static Builder builder() { return new Builder(); }

    public WaitUntilOption getWaitUntil() { return waitUntil; }
    public Boolean getWaitForFrame() { return waitForFrame; }
    public Boolean getWaitForFrameAttachment() { return waitForFrameAttachment; }
    public WaitUntilOption getWaitForFrameNavigation() { return waitForFrameNavigation; }
    public Boolean getWaitForFrameImages() { return waitForFrameImages; }
    public WaitForSelectorOptions getWaitForFrameSelector() { return waitForFrameSelector; }
    public WaitForSelectorOptions getWaitForSelector() { return waitForSelector; }
    public Boolean getPreferCSSPageSize() { return preferCSSPageSize; }
    public Double getScale() { return scale; }
    public LayoutDimension getHeight() { return height; }
    public LayoutDimension getWidth() { return width; }
    public Boolean getLandscape() { return landscape; }
    public String getPageRanges() { return pageRanges; }
    public Boolean getAutoScroll() { return autoScroll; }
    public Viewport getViewPort() { return viewPort; }
    public String getTimeZone() { return timeZone; }
    public NullableString getEmulateMediaType() { return emulateMediaType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PuppeteerOptions)) return false;
        PuppeteerOptions p = (PuppeteerOptions) o;
        return waitUntil == p.waitUntil && Objects.equals(waitForFrame, p.waitForFrame)
                && Objects.equals(scale, p.scale) && Objects.equals(landscape, p.landscape)
                && Objects.equals(viewPort, p.viewPort) && Objects.equals(emulateMediaType, p.emulateMediaType);
    }

    @Override
    public int hashCode() { return Objects.hash(waitUntil, waitForFrame, scale, landscape, viewPort, emulateMediaType); }

    public static final class Builder {
        private WaitUntilOption waitUntil;
        private Boolean waitForFrame;
        private Boolean waitForFrameAttachment;
        private WaitUntilOption waitForFrameNavigation;
        private Boolean waitForFrameImages;
        private WaitForSelectorOptions waitForFrameSelector;
        private WaitForSelectorOptions waitForSelector;
        private Boolean preferCSSPageSize;
        private Double scale;
        private LayoutDimension height;
        private LayoutDimension width;
        private Boolean landscape;
        private String pageRanges;
        private Boolean autoScroll;
        private Viewport viewPort;
        private String timeZone;
        private NullableString emulateMediaType;

        private Builder() {}

        public Builder waitUntil(WaitUntilOption waitUntil) { this.waitUntil = waitUntil; return this; }
        public Builder waitForFrame(boolean waitForFrame) { this.waitForFrame = waitForFrame; return this; }
        public Builder waitForFrameAttachment(boolean val) { this.waitForFrameAttachment = val; return this; }
        public Builder waitForFrameNavigation(WaitUntilOption val) { this.waitForFrameNavigation = val; return this; }
        public Builder waitForFrameImages(boolean val) { this.waitForFrameImages = val; return this; }
        public Builder waitForFrameSelector(WaitForSelectorOptions val) { this.waitForFrameSelector = val; return this; }
        public Builder waitForSelector(WaitForSelectorOptions val) { this.waitForSelector = val; return this; }
        public Builder preferCSSPageSize(boolean val) { this.preferCSSPageSize = val; return this; }
        public Builder scale(double scale) { this.scale = scale; return this; }
        public Builder height(LayoutDimension height) { this.height = height; return this; }
        public Builder height(String css) { this.height = LayoutDimension.of(css); return this; }
        public Builder height(int px) { this.height = LayoutDimension.of(px); return this; }
        public Builder width(LayoutDimension width) { this.width = width; return this; }
        public Builder width(String css) { this.width = LayoutDimension.of(css); return this; }
        public Builder width(int px) { this.width = LayoutDimension.of(px); return this; }
        public Builder landscape(boolean landscape) { this.landscape = landscape; return this; }
        public Builder pageRanges(String pageRanges) { this.pageRanges = pageRanges; return this; }
        public Builder autoScroll(boolean autoScroll) { this.autoScroll = autoScroll; return this; }
        public Builder viewPort(Viewport viewPort) { this.viewPort = viewPort; return this; }
        public Builder timeZone(String timeZone) { this.timeZone = timeZone; return this; }
        public Builder emulateMediaType(NullableString emulateMediaType) { this.emulateMediaType = emulateMediaType; return this; }
        public Builder emulateScreen() { this.emulateMediaType = NullableString.emulateScreen(); return this; }
        public Builder emulatePrint() { this.emulateMediaType = NullableString.emulatePrint(); return this; }
        public Builder emulateNone() { this.emulateMediaType = NullableString.emulateNone(); return this; }

        public PuppeteerOptions build() { return new PuppeteerOptions(this); }
    }
}
