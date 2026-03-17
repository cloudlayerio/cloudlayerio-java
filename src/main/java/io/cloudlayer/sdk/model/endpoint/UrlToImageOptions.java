package io.cloudlayer.sdk.model.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.cloudlayer.sdk.model.constants.ImageType;
import io.cloudlayer.sdk.model.constants.WaitUntilOption;
import io.cloudlayer.sdk.model.options.*;

import java.util.List;

/**
 * Options for the URL to Image conversion endpoint.
 * Composes: UrlOptions + ImageOptions + PuppeteerOptions + BaseOptions
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class UrlToImageOptions {

    // UrlOptions
    @JsonProperty("url") private final String url;
    @JsonProperty("authentication") private final Authentication authentication;
    @JsonProperty("batch") private final Batch batch;
    @JsonProperty("cookies") private final List<Cookie> cookies;

    // ImageOptions
    @JsonProperty("imageType") private final ImageType imageType;
    @JsonProperty("quality") private final Integer quality;
    @JsonProperty("trim") private final Boolean trim;
    @JsonProperty("transparent") private final Boolean transparent;
    @JsonProperty("generatePreview") private final GeneratePreviewOption generatePreview;

    // PuppeteerOptions
    @JsonProperty("waitUntil") private final WaitUntilOption waitUntil;
    @JsonProperty("waitForFrame") private final Boolean waitForFrame;
    @JsonProperty("waitForFrameAttachment") private final Boolean waitForFrameAttachment;
    @JsonProperty("waitForFrameNavigation") private final WaitUntilOption waitForFrameNavigation;
    @JsonProperty("waitForFrameImages") private final Boolean waitForFrameImages;
    @JsonProperty("waitForFrameSelector") private final WaitForSelectorOptions waitForFrameSelector;
    @JsonProperty("waitForSelector") private final WaitForSelectorOptions waitForSelector;
    @JsonProperty("preferCSSPageSize") private final Boolean preferCSSPageSize;
    @JsonProperty("scale") private final Double scale;
    @JsonProperty("height") private final LayoutDimension height;
    @JsonProperty("width") private final LayoutDimension width;
    @JsonProperty("landscape") private final Boolean landscape;
    @JsonProperty("pageRanges") private final String pageRanges;
    @JsonProperty("autoScroll") private final Boolean autoScroll;
    @JsonProperty("viewPort") private final Viewport viewPort;
    @JsonProperty("timeZone") private final String timeZone;
    @JsonProperty("emulateMediaType") private final NullableString emulateMediaType;

    // BaseOptions
    @JsonProperty("name") private final String name;
    @JsonProperty("timeout") private final Integer timeout;
    @JsonProperty("delay") private final Integer delay;
    @JsonProperty("filename") private final String filename;
    @JsonProperty("inline") private final Boolean inline;
    @JsonProperty("async") private final Boolean async;
    @JsonProperty("storage") private final StorageOption storage;
    @JsonProperty("webhook") private final String webhook;
    @JsonProperty("apiVer") private final String apiVer;
    @JsonProperty("projectId") private final String projectId;

    private UrlToImageOptions(Builder b) {
        this.url = b.url; this.authentication = b.authentication; this.batch = b.batch; this.cookies = b.cookies;
        this.imageType = b.imageType; this.quality = b.quality; this.trim = b.trim;
        this.transparent = b.transparent; this.generatePreview = b.generatePreview;
        this.waitUntil = b.waitUntil; this.waitForFrame = b.waitForFrame; this.waitForFrameAttachment = b.waitForFrameAttachment;
        this.waitForFrameNavigation = b.waitForFrameNavigation; this.waitForFrameImages = b.waitForFrameImages;
        this.waitForFrameSelector = b.waitForFrameSelector; this.waitForSelector = b.waitForSelector;
        this.preferCSSPageSize = b.preferCSSPageSize; this.scale = b.scale; this.height = b.height; this.width = b.width;
        this.landscape = b.landscape; this.pageRanges = b.pageRanges; this.autoScroll = b.autoScroll;
        this.viewPort = b.viewPort; this.timeZone = b.timeZone; this.emulateMediaType = b.emulateMediaType;
        this.name = b.name; this.timeout = b.timeout; this.delay = b.delay; this.filename = b.filename;
        this.inline = b.inline; this.async = b.async; this.storage = b.storage; this.webhook = b.webhook;
        this.apiVer = b.apiVer; this.projectId = b.projectId;
    }

    public static Builder builder() { return new Builder(); }

    // Getters
    public String getUrl() { return url; }
    public Authentication getAuthentication() { return authentication; }
    public Batch getBatch() { return batch; }
    public List<Cookie> getCookies() { return cookies; }
    public ImageType getImageType() { return imageType; }
    public Integer getQuality() { return quality; }
    public Boolean getTrim() { return trim; }
    public Boolean getTransparent() { return transparent; }
    public GeneratePreviewOption getGeneratePreview() { return generatePreview; }
    public WaitUntilOption getWaitUntil() { return waitUntil; }
    public Boolean getWaitForFrame() { return waitForFrame; }
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
    public String getName() { return name; }
    public Integer getTimeout() { return timeout; }
    public Integer getDelay() { return delay; }
    public String getFilename() { return filename; }
    public Boolean getInline() { return inline; }
    public Boolean getAsync() { return async; }
    public StorageOption getStorage() { return storage; }
    public String getWebhook() { return webhook; }
    public String getApiVer() { return apiVer; }
    public String getProjectId() { return projectId; }

    public static final class Builder {
        // UrlOptions
        private String url;
        private Authentication authentication;
        private Batch batch;
        private List<Cookie> cookies;
        // ImageOptions
        private ImageType imageType;
        private Integer quality;
        private Boolean trim;
        private Boolean transparent;
        private GeneratePreviewOption generatePreview;
        // PuppeteerOptions
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
        // BaseOptions
        private String name;
        private Integer timeout;
        private Integer delay;
        private String filename;
        private Boolean inline;
        private Boolean async;
        private StorageOption storage;
        private String webhook;
        private String apiVer;
        private String projectId;

        private Builder() {}

        // UrlOptions setters
        public Builder url(String url) { this.url = url; return this; }
        public Builder authentication(Authentication auth) { this.authentication = auth; return this; }
        public Builder batch(Batch batch) { this.batch = batch; return this; }
        public Builder cookies(List<Cookie> cookies) { this.cookies = cookies; return this; }
        // ImageOptions setters
        public Builder imageType(ImageType val) { this.imageType = val; return this; }
        public Builder quality(int val) { this.quality = val; return this; }
        public Builder trim(boolean val) { this.trim = val; return this; }
        public Builder transparent(boolean val) { this.transparent = val; return this; }
        public Builder generatePreview(GeneratePreviewOption val) { this.generatePreview = val; return this; }
        public Builder generatePreview(boolean val) { this.generatePreview = GeneratePreviewOption.of(val); return this; }
        // PuppeteerOptions setters
        public Builder waitUntil(WaitUntilOption val) { this.waitUntil = val; return this; }
        public Builder waitForFrame(boolean val) { this.waitForFrame = val; return this; }
        public Builder waitForFrameAttachment(boolean val) { this.waitForFrameAttachment = val; return this; }
        public Builder waitForFrameNavigation(WaitUntilOption val) { this.waitForFrameNavigation = val; return this; }
        public Builder waitForFrameImages(boolean val) { this.waitForFrameImages = val; return this; }
        public Builder waitForFrameSelector(WaitForSelectorOptions val) { this.waitForFrameSelector = val; return this; }
        public Builder waitForSelector(WaitForSelectorOptions val) { this.waitForSelector = val; return this; }
        public Builder preferCSSPageSize(boolean val) { this.preferCSSPageSize = val; return this; }
        public Builder scale(double val) { this.scale = val; return this; }
        public Builder height(LayoutDimension val) { this.height = val; return this; }
        public Builder height(String css) { this.height = LayoutDimension.of(css); return this; }
        public Builder height(int px) { this.height = LayoutDimension.of(px); return this; }
        public Builder width(LayoutDimension val) { this.width = val; return this; }
        public Builder width(String css) { this.width = LayoutDimension.of(css); return this; }
        public Builder width(int px) { this.width = LayoutDimension.of(px); return this; }
        public Builder landscape(boolean val) { this.landscape = val; return this; }
        public Builder pageRanges(String val) { this.pageRanges = val; return this; }
        public Builder autoScroll(boolean val) { this.autoScroll = val; return this; }
        public Builder viewPort(Viewport val) { this.viewPort = val; return this; }
        public Builder timeZone(String val) { this.timeZone = val; return this; }
        public Builder emulateMediaType(NullableString val) { this.emulateMediaType = val; return this; }
        public Builder emulateScreen() { this.emulateMediaType = NullableString.emulateScreen(); return this; }
        public Builder emulatePrint() { this.emulateMediaType = NullableString.emulatePrint(); return this; }
        public Builder emulateNone() { this.emulateMediaType = NullableString.emulateNone(); return this; }
        // BaseOptions setters
        public Builder name(String val) { this.name = val; return this; }
        public Builder timeout(int val) { this.timeout = val; return this; }
        public Builder delay(int val) { this.delay = val; return this; }
        public Builder filename(String val) { this.filename = val; return this; }
        public Builder inline(boolean val) { this.inline = val; return this; }
        public Builder async(boolean val) { this.async = val; return this; }
        public Builder storage(StorageOption val) { this.storage = val; return this; }
        public Builder storage(boolean val) { this.storage = StorageOption.of(val); return this; }
        public Builder webhook(String val) { this.webhook = val; return this; }
        public Builder apiVer(String val) { this.apiVer = val; return this; }
        public Builder projectId(String val) { this.projectId = val; return this; }

        public UrlToImageOptions build() { return new UrlToImageOptions(this); }
    }
}
