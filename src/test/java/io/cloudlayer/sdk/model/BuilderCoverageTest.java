package io.cloudlayer.sdk.model;

import io.cloudlayer.sdk.model.constants.ImageType;
import io.cloudlayer.sdk.model.constants.PdfFormat;
import io.cloudlayer.sdk.model.constants.WaitUntilOption;
import io.cloudlayer.sdk.model.endpoint.*;
import io.cloudlayer.sdk.model.options.*;
import io.cloudlayer.sdk.model.response.ConversionResult;
import io.cloudlayer.sdk.model.response.ResponseHeaders;
import io.cloudlayer.sdk.util.FileInput;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Exercises ALL builder methods and getters on composite endpoint types
 * and option classes to maximize JaCoCo coverage.
 */
class BuilderCoverageTest {

    // --- Shared test fixtures ---

    private static final Authentication AUTH = Authentication.of("user", "pass");
    private static final Batch BATCH = Batch.of(List.of("https://a.com/1.pdf", "https://b.com/2.pdf"));
    private static final Cookie COOKIE = Cookie.builder("session", "abc123")
            .url("https://example.com")
            .domain("example.com")
            .path("/")
            .expires(3600)
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .build();
    private static final Margin MARGIN = Margin.builder()
            .top("10mm").bottom("20mm").left("15mm").right("15mm")
            .build();
    private static final HeaderFooterTemplate HEADER = HeaderFooterTemplate.builder()
            .method("html")
            .selector("#header")
            .margin(MARGIN)
            .style(Map.of("fontSize", "12px"))
            .imageStyle(Map.of("maxWidth", "100%"))
            .template("<header>Header</header>")
            .templateString("<span class='pageNumber'></span>")
            .build();
    private static final HeaderFooterTemplate FOOTER = HeaderFooterTemplate.builder()
            .method("html")
            .selector("#footer")
            .template("<footer>Footer</footer>")
            .build();
    private static final PreviewOptions PREVIEW_OPTS = PreviewOptions.builder()
            .width(200).height(300).type(ImageType.PNG).quality(80).maintainAspectRatio(true)
            .build();
    private static final GeneratePreviewOption GENERATE_PREVIEW = GeneratePreviewOption.of(PREVIEW_OPTS);
    private static final WaitForSelectorOptions WAIT_SELECTOR = WaitForSelectorOptions.builder()
            .selector("#content")
            .options(true, false, 5000)
            .build();
    private static final Viewport VIEWPORT = Viewport.builder()
            .width(1920).height(1080).deviceScaleFactor(2.0).isMobile(false).hasTouch(false).isLandscape(true)
            .build();
    private static final StorageOption STORAGE_ID = StorageOption.ofId("storage-abc");

    // ===========================
    // UrlToPdfOptions
    // ===========================

    @Test
    void urlToPdfOptions_allBuilderMethodsAndGetters() {
        UrlToPdfOptions opts = UrlToPdfOptions.builder()
                .url("https://example.com")
                .authentication(AUTH)
                .batch(BATCH)
                .cookies(List.of(COOKIE))
                .printBackground(true)
                .format(PdfFormat.A4)
                .margin(MARGIN)
                .headerTemplate(HEADER)
                .footerTemplate(FOOTER)
                .generatePreview(GENERATE_PREVIEW)
                .waitUntil(WaitUntilOption.NETWORK_IDLE_0)
                .waitForFrame(true)
                .waitForFrameAttachment(true)
                .waitForFrameNavigation(WaitUntilOption.LOAD)
                .waitForFrameImages(true)
                .waitForFrameSelector(WAIT_SELECTOR)
                .waitForSelector(WAIT_SELECTOR)
                .preferCSSPageSize(true)
                .scale(1.5)
                .height(LayoutDimension.of("1000px"))
                .width(LayoutDimension.of(800))
                .landscape(true)
                .pageRanges("1-5")
                .autoScroll(true)
                .viewPort(VIEWPORT)
                .timeZone("America/New_York")
                .emulateMediaType(NullableString.emulateScreen())
                .name("test-job")
                .timeout(30000)
                .delay(500)
                .filename("output.pdf")
                .inline(true)
                .async(true)
                .storage(STORAGE_ID)
                .webhook("https://hooks.example.com/done")
                .apiVer("v2")
                .projectId("proj-001")
                .build();

        assertThat(opts.getUrl()).isEqualTo("https://example.com");
        assertThat(opts.getAuthentication()).isEqualTo(AUTH);
        assertThat(opts.getBatch()).isEqualTo(BATCH);
        assertThat(opts.getCookies()).hasSize(1);
        assertThat(opts.getPrintBackground()).isTrue();
        assertThat(opts.getFormat()).isEqualTo(PdfFormat.A4);
        assertThat(opts.getMargin()).isEqualTo(MARGIN);
        assertThat(opts.getHeaderTemplate()).isEqualTo(HEADER);
        assertThat(opts.getFooterTemplate()).isEqualTo(FOOTER);
        assertThat(opts.getGeneratePreview()).isEqualTo(GENERATE_PREVIEW);
        assertThat(opts.getWaitUntil()).isEqualTo(WaitUntilOption.NETWORK_IDLE_0);
        assertThat(opts.getWaitForFrame()).isTrue();
        assertThat(opts.getPreferCSSPageSize()).isTrue();
        assertThat(opts.getScale()).isEqualTo(1.5);
        assertThat(opts.getHeight()).isEqualTo(LayoutDimension.of("1000px"));
        assertThat(opts.getWidth()).isEqualTo(LayoutDimension.of(800));
        assertThat(opts.getLandscape()).isTrue();
        assertThat(opts.getPageRanges()).isEqualTo("1-5");
        assertThat(opts.getAutoScroll()).isTrue();
        assertThat(opts.getViewPort()).isEqualTo(VIEWPORT);
        assertThat(opts.getTimeZone()).isEqualTo("America/New_York");
        assertThat(opts.getEmulateMediaType()).isEqualTo(NullableString.emulateScreen());
        assertThat(opts.getName()).isEqualTo("test-job");
        assertThat(opts.getTimeout()).isEqualTo(30000);
        assertThat(opts.getDelay()).isEqualTo(500);
        assertThat(opts.getFilename()).isEqualTo("output.pdf");
        assertThat(opts.getInline()).isTrue();
        assertThat(opts.getAsync()).isTrue();
        assertThat(opts.getStorage()).isEqualTo(STORAGE_ID);
        assertThat(opts.getWebhook()).isEqualTo("https://hooks.example.com/done");
        assertThat(opts.getApiVer()).isEqualTo("v2");
        assertThat(opts.getProjectId()).isEqualTo("proj-001");
    }

    @Test
    void urlToPdfOptions_convenienceBuilderOverloads() {
        // Test height(String), height(int), width(String), width(int) overloads
        UrlToPdfOptions opts = UrlToPdfOptions.builder()
                .url("https://example.com")
                .height("500px")
                .width(1024)
                .generatePreview(true)
                .storage(true)
                .emulateScreen()
                .build();

        assertThat(opts.getHeight()).isEqualTo(LayoutDimension.of("500px"));
        assertThat(opts.getWidth()).isEqualTo(LayoutDimension.of(1024));
        assertThat(opts.getGeneratePreview()).isEqualTo(GeneratePreviewOption.of(true));
        assertThat(opts.getStorage()).isEqualTo(StorageOption.of(true));
        assertThat(opts.getEmulateMediaType()).isEqualTo(NullableString.emulateScreen());
    }

    @Test
    void urlToPdfOptions_emulatePrintAndNone() {
        UrlToPdfOptions print = UrlToPdfOptions.builder()
                .url("https://example.com")
                .emulatePrint()
                .build();
        assertThat(print.getEmulateMediaType()).isEqualTo(NullableString.emulatePrint());

        UrlToPdfOptions none = UrlToPdfOptions.builder()
                .url("https://example.com")
                .emulateNone()
                .build();
        assertThat(none.getEmulateMediaType().isNull()).isTrue();
    }

    @Test
    void urlToPdfOptions_heightIntAndWidthString() {
        UrlToPdfOptions opts = UrlToPdfOptions.builder()
                .url("https://example.com")
                .height(768)
                .width("1024px")
                .build();
        assertThat(opts.getHeight()).isEqualTo(LayoutDimension.of(768));
        assertThat(opts.getWidth()).isEqualTo(LayoutDimension.of("1024px"));
    }

    // ===========================
    // HtmlToPdfOptions
    // ===========================

    @Test
    void htmlToPdfOptions_allBuilderMethodsAndGetters() {
        HtmlToPdfOptions opts = HtmlToPdfOptions.builder()
                .html("<html><body>Hello</body></html>")
                .printBackground(true)
                .format(PdfFormat.LETTER)
                .margin(MARGIN)
                .headerTemplate(HEADER)
                .footerTemplate(FOOTER)
                .generatePreview(GENERATE_PREVIEW)
                .waitUntil(WaitUntilOption.DOM_CONTENT_LOADED)
                .waitForFrame(false)
                .waitForFrameAttachment(false)
                .waitForFrameNavigation(WaitUntilOption.NETWORK_IDLE_2)
                .waitForFrameImages(false)
                .waitForFrameSelector(WAIT_SELECTOR)
                .waitForSelector(WAIT_SELECTOR)
                .preferCSSPageSize(false)
                .scale(0.8)
                .height(LayoutDimension.of("800px"))
                .width(LayoutDimension.of("600px"))
                .landscape(false)
                .pageRanges("1-3")
                .autoScroll(false)
                .viewPort(VIEWPORT)
                .timeZone("Europe/London")
                .emulateMediaType(NullableString.emulatePrint())
                .name("html-job")
                .timeout(20000)
                .delay(200)
                .filename("report.pdf")
                .inline(false)
                .async(false)
                .storage(StorageOption.of(false))
                .webhook("https://hooks.example.com/callback")
                .apiVer("v1")
                .projectId("proj-002")
                .build();

        assertThat(opts.getHtml()).isEqualTo("<html><body>Hello</body></html>");
        assertThat(opts.getPrintBackground()).isTrue();
        assertThat(opts.getFormat()).isEqualTo(PdfFormat.LETTER);
        assertThat(opts.getMargin()).isEqualTo(MARGIN);
        assertThat(opts.getHeaderTemplate()).isEqualTo(HEADER);
        assertThat(opts.getFooterTemplate()).isEqualTo(FOOTER);
        assertThat(opts.getGeneratePreview()).isEqualTo(GENERATE_PREVIEW);
        assertThat(opts.getWaitUntil()).isEqualTo(WaitUntilOption.DOM_CONTENT_LOADED);
        assertThat(opts.getWaitForFrame()).isFalse();
        assertThat(opts.getPreferCSSPageSize()).isFalse();
        assertThat(opts.getScale()).isEqualTo(0.8);
        assertThat(opts.getHeight()).isEqualTo(LayoutDimension.of("800px"));
        assertThat(opts.getWidth()).isEqualTo(LayoutDimension.of("600px"));
        assertThat(opts.getLandscape()).isFalse();
        assertThat(opts.getPageRanges()).isEqualTo("1-3");
        assertThat(opts.getAutoScroll()).isFalse();
        assertThat(opts.getViewPort()).isEqualTo(VIEWPORT);
        assertThat(opts.getTimeZone()).isEqualTo("Europe/London");
        assertThat(opts.getEmulateMediaType()).isEqualTo(NullableString.emulatePrint());
        assertThat(opts.getName()).isEqualTo("html-job");
        assertThat(opts.getTimeout()).isEqualTo(20000);
        assertThat(opts.getDelay()).isEqualTo(200);
        assertThat(opts.getFilename()).isEqualTo("report.pdf");
        assertThat(opts.getInline()).isFalse();
        assertThat(opts.getAsync()).isFalse();
        assertThat(opts.getStorage()).isEqualTo(StorageOption.of(false));
        assertThat(opts.getWebhook()).isEqualTo("https://hooks.example.com/callback");
        assertThat(opts.getApiVer()).isEqualTo("v1");
        assertThat(opts.getProjectId()).isEqualTo("proj-002");
    }

    @Test
    void htmlToPdfOptions_convenienceOverloads() {
        HtmlToPdfOptions opts = HtmlToPdfOptions.builder()
                .html("<html></html>")
                .height("500px")
                .width(800)
                .generatePreview(true)
                .storage(true)
                .emulateScreen()
                .build();
        assertThat(opts.getHeight()).isEqualTo(LayoutDimension.of("500px"));
        assertThat(opts.getWidth()).isEqualTo(LayoutDimension.of(800));

        HtmlToPdfOptions opts2 = HtmlToPdfOptions.builder()
                .html("<html></html>")
                .height(768)
                .width("1024px")
                .emulatePrint()
                .build();
        assertThat(opts2.getHeight()).isEqualTo(LayoutDimension.of(768));
        assertThat(opts2.getWidth()).isEqualTo(LayoutDimension.of("1024px"));

        HtmlToPdfOptions opts3 = HtmlToPdfOptions.builder()
                .html("<html></html>")
                .emulateNone()
                .build();
        assertThat(opts3.getEmulateMediaType().isNull()).isTrue();
    }

    // ===========================
    // TemplateToPdfOptions
    // ===========================

    @Test
    void templateToPdfOptions_allBuilderMethodsAndGetters() {
        Map<String, Object> data = Map.of("name", "John", "amount", 1500);

        TemplateToPdfOptions opts = TemplateToPdfOptions.builder()
                .templateId("tmpl-123")
                .template("<html>{{name}}</html>")
                .data(data)
                .printBackground(true)
                .format(PdfFormat.A3)
                .margin(MARGIN)
                .headerTemplate(HEADER)
                .footerTemplate(FOOTER)
                .generatePreview(GENERATE_PREVIEW)
                .waitUntil(WaitUntilOption.LOAD)
                .waitForFrame(true)
                .waitForFrameAttachment(true)
                .waitForFrameNavigation(WaitUntilOption.DOM_CONTENT_LOADED)
                .waitForFrameImages(true)
                .waitForFrameSelector(WAIT_SELECTOR)
                .waitForSelector(WAIT_SELECTOR)
                .preferCSSPageSize(true)
                .scale(1.0)
                .height(LayoutDimension.of(900))
                .width(LayoutDimension.of(700))
                .landscape(true)
                .pageRanges("2-4")
                .autoScroll(true)
                .viewPort(VIEWPORT)
                .timeZone("Asia/Tokyo")
                .emulateMediaType(NullableString.of("screen"))
                .name("template-job")
                .timeout(60000)
                .delay(1000)
                .filename("invoice.pdf")
                .inline(true)
                .async(true)
                .storage(STORAGE_ID)
                .webhook("https://hooks.example.com/tmpl")
                .apiVer("v2")
                .projectId("proj-003")
                .build();

        assertThat(opts.getTemplateId()).isEqualTo("tmpl-123");
        assertThat(opts.getTemplate()).isEqualTo("<html>{{name}}</html>");
        assertThat(opts.getData()).containsEntry("name", "John");
        assertThat(opts.getPrintBackground()).isTrue();
        assertThat(opts.getFormat()).isEqualTo(PdfFormat.A3);
        assertThat(opts.getMargin()).isEqualTo(MARGIN);
        assertThat(opts.getHeaderTemplate()).isEqualTo(HEADER);
        assertThat(opts.getFooterTemplate()).isEqualTo(FOOTER);
        assertThat(opts.getGeneratePreview()).isEqualTo(GENERATE_PREVIEW);
        assertThat(opts.getWaitUntil()).isEqualTo(WaitUntilOption.LOAD);
        assertThat(opts.getWaitForFrame()).isTrue();
        assertThat(opts.getPreferCSSPageSize()).isTrue();
        assertThat(opts.getScale()).isEqualTo(1.0);
        assertThat(opts.getHeight()).isEqualTo(LayoutDimension.of(900));
        assertThat(opts.getWidth()).isEqualTo(LayoutDimension.of(700));
        assertThat(opts.getLandscape()).isTrue();
        assertThat(opts.getPageRanges()).isEqualTo("2-4");
        assertThat(opts.getAutoScroll()).isTrue();
        assertThat(opts.getViewPort()).isEqualTo(VIEWPORT);
        assertThat(opts.getTimeZone()).isEqualTo("Asia/Tokyo");
        assertThat(opts.getEmulateMediaType()).isEqualTo(NullableString.of("screen"));
        assertThat(opts.getName()).isEqualTo("template-job");
        assertThat(opts.getTimeout()).isEqualTo(60000);
        assertThat(opts.getDelay()).isEqualTo(1000);
        assertThat(opts.getFilename()).isEqualTo("invoice.pdf");
        assertThat(opts.getInline()).isTrue();
        assertThat(opts.getAsync()).isTrue();
        assertThat(opts.getStorage()).isEqualTo(STORAGE_ID);
        assertThat(opts.getWebhook()).isEqualTo("https://hooks.example.com/tmpl");
        assertThat(opts.getApiVer()).isEqualTo("v2");
        assertThat(opts.getProjectId()).isEqualTo("proj-003");
    }

    @Test
    void templateToPdfOptions_convenienceOverloads() {
        TemplateToPdfOptions opts = TemplateToPdfOptions.builder()
                .templateId("tmpl-x")
                .height("500px")
                .width(800)
                .generatePreview(true)
                .storage(true)
                .emulateScreen()
                .build();
        assertThat(opts.getHeight()).isEqualTo(LayoutDimension.of("500px"));
        assertThat(opts.getWidth()).isEqualTo(LayoutDimension.of(800));

        TemplateToPdfOptions opts2 = TemplateToPdfOptions.builder()
                .templateId("tmpl-x")
                .height(768)
                .width("1024px")
                .emulatePrint()
                .build();
        assertThat(opts2.getHeight()).isEqualTo(LayoutDimension.of(768));
        assertThat(opts2.getWidth()).isEqualTo(LayoutDimension.of("1024px"));

        TemplateToPdfOptions opts3 = TemplateToPdfOptions.builder()
                .templateId("tmpl-x")
                .emulateNone()
                .build();
        assertThat(opts3.getEmulateMediaType().isNull()).isTrue();
    }

    // ===========================
    // UrlToImageOptions
    // ===========================

    @Test
    void urlToImageOptions_allBuilderMethodsAndGetters() {
        UrlToImageOptions opts = UrlToImageOptions.builder()
                .url("https://example.com")
                .authentication(AUTH)
                .batch(BATCH)
                .cookies(List.of(COOKIE))
                .imageType(ImageType.PNG)
                .quality(95)
                .trim(true)
                .transparent(true)
                .generatePreview(GENERATE_PREVIEW)
                .waitUntil(WaitUntilOption.NETWORK_IDLE_0)
                .waitForFrame(true)
                .waitForFrameAttachment(true)
                .waitForFrameNavigation(WaitUntilOption.LOAD)
                .waitForFrameImages(true)
                .waitForFrameSelector(WAIT_SELECTOR)
                .waitForSelector(WAIT_SELECTOR)
                .preferCSSPageSize(false)
                .scale(2.0)
                .height(LayoutDimension.of("1200px"))
                .width(LayoutDimension.of(1600))
                .landscape(false)
                .pageRanges("1")
                .autoScroll(true)
                .viewPort(VIEWPORT)
                .timeZone("UTC")
                .emulateMediaType(NullableString.of("screen"))
                .name("image-job")
                .timeout(15000)
                .delay(300)
                .filename("screenshot.png")
                .inline(true)
                .async(true)
                .storage(STORAGE_ID)
                .webhook("https://hooks.example.com/img")
                .apiVer("v2")
                .projectId("proj-004")
                .build();

        assertThat(opts.getUrl()).isEqualTo("https://example.com");
        assertThat(opts.getAuthentication()).isEqualTo(AUTH);
        assertThat(opts.getBatch()).isEqualTo(BATCH);
        assertThat(opts.getCookies()).hasSize(1);
        assertThat(opts.getImageType()).isEqualTo(ImageType.PNG);
        assertThat(opts.getQuality()).isEqualTo(95);
        assertThat(opts.getTrim()).isTrue();
        assertThat(opts.getTransparent()).isTrue();
        assertThat(opts.getGeneratePreview()).isEqualTo(GENERATE_PREVIEW);
        assertThat(opts.getWaitUntil()).isEqualTo(WaitUntilOption.NETWORK_IDLE_0);
        assertThat(opts.getWaitForFrame()).isTrue();
        assertThat(opts.getPreferCSSPageSize()).isFalse();
        assertThat(opts.getScale()).isEqualTo(2.0);
        assertThat(opts.getHeight()).isEqualTo(LayoutDimension.of("1200px"));
        assertThat(opts.getWidth()).isEqualTo(LayoutDimension.of(1600));
        assertThat(opts.getLandscape()).isFalse();
        assertThat(opts.getPageRanges()).isEqualTo("1");
        assertThat(opts.getAutoScroll()).isTrue();
        assertThat(opts.getViewPort()).isEqualTo(VIEWPORT);
        assertThat(opts.getTimeZone()).isEqualTo("UTC");
        assertThat(opts.getEmulateMediaType()).isEqualTo(NullableString.of("screen"));
        assertThat(opts.getName()).isEqualTo("image-job");
        assertThat(opts.getTimeout()).isEqualTo(15000);
        assertThat(opts.getDelay()).isEqualTo(300);
        assertThat(opts.getFilename()).isEqualTo("screenshot.png");
        assertThat(opts.getInline()).isTrue();
        assertThat(opts.getAsync()).isTrue();
        assertThat(opts.getStorage()).isEqualTo(STORAGE_ID);
        assertThat(opts.getWebhook()).isEqualTo("https://hooks.example.com/img");
        assertThat(opts.getApiVer()).isEqualTo("v2");
        assertThat(opts.getProjectId()).isEqualTo("proj-004");
    }

    @Test
    void urlToImageOptions_convenienceOverloads() {
        UrlToImageOptions opts = UrlToImageOptions.builder()
                .url("https://example.com")
                .height("500px")
                .width(800)
                .generatePreview(true)
                .storage(true)
                .emulateScreen()
                .build();
        assertThat(opts.getHeight()).isEqualTo(LayoutDimension.of("500px"));
        assertThat(opts.getWidth()).isEqualTo(LayoutDimension.of(800));

        UrlToImageOptions opts2 = UrlToImageOptions.builder()
                .url("https://example.com")
                .height(768)
                .width("1024px")
                .emulatePrint()
                .build();
        assertThat(opts2.getHeight()).isEqualTo(LayoutDimension.of(768));
        assertThat(opts2.getWidth()).isEqualTo(LayoutDimension.of("1024px"));

        UrlToImageOptions opts3 = UrlToImageOptions.builder()
                .url("https://example.com")
                .emulateNone()
                .build();
        assertThat(opts3.getEmulateMediaType().isNull()).isTrue();
    }

    // ===========================
    // HtmlToImageOptions
    // ===========================

    @Test
    void htmlToImageOptions_allBuilderMethodsAndGetters() {
        HtmlToImageOptions opts = HtmlToImageOptions.builder()
                .html("<html><body>Image</body></html>")
                .imageType(ImageType.JPEG)
                .quality(85)
                .trim(false)
                .transparent(false)
                .generatePreview(GENERATE_PREVIEW)
                .waitUntil(WaitUntilOption.DOM_CONTENT_LOADED)
                .waitForFrame(false)
                .waitForFrameAttachment(false)
                .waitForFrameNavigation(WaitUntilOption.NETWORK_IDLE_2)
                .waitForFrameImages(false)
                .waitForFrameSelector(WAIT_SELECTOR)
                .waitForSelector(WAIT_SELECTOR)
                .preferCSSPageSize(true)
                .scale(1.0)
                .height(LayoutDimension.of("600px"))
                .width(LayoutDimension.of("800px"))
                .landscape(true)
                .pageRanges("1")
                .autoScroll(false)
                .viewPort(VIEWPORT)
                .timeZone("Europe/Berlin")
                .emulateMediaType(NullableString.emulatePrint())
                .name("html-image-job")
                .timeout(10000)
                .delay(100)
                .filename("capture.jpg")
                .inline(false)
                .async(false)
                .storage(StorageOption.of(true))
                .webhook("https://hooks.example.com/htmlimg")
                .apiVer("v1")
                .projectId("proj-005")
                .build();

        assertThat(opts.getHtml()).isEqualTo("<html><body>Image</body></html>");
        assertThat(opts.getImageType()).isEqualTo(ImageType.JPEG);
        assertThat(opts.getQuality()).isEqualTo(85);
        assertThat(opts.getTrim()).isFalse();
        assertThat(opts.getTransparent()).isFalse();
        assertThat(opts.getGeneratePreview()).isEqualTo(GENERATE_PREVIEW);
        assertThat(opts.getWaitUntil()).isEqualTo(WaitUntilOption.DOM_CONTENT_LOADED);
        assertThat(opts.getWaitForFrame()).isFalse();
        assertThat(opts.getPreferCSSPageSize()).isTrue();
        assertThat(opts.getScale()).isEqualTo(1.0);
        assertThat(opts.getHeight()).isEqualTo(LayoutDimension.of("600px"));
        assertThat(opts.getWidth()).isEqualTo(LayoutDimension.of("800px"));
        assertThat(opts.getLandscape()).isTrue();
        assertThat(opts.getPageRanges()).isEqualTo("1");
        assertThat(opts.getAutoScroll()).isFalse();
        assertThat(opts.getViewPort()).isEqualTo(VIEWPORT);
        assertThat(opts.getTimeZone()).isEqualTo("Europe/Berlin");
        assertThat(opts.getEmulateMediaType()).isEqualTo(NullableString.emulatePrint());
        assertThat(opts.getName()).isEqualTo("html-image-job");
        assertThat(opts.getTimeout()).isEqualTo(10000);
        assertThat(opts.getDelay()).isEqualTo(100);
        assertThat(opts.getFilename()).isEqualTo("capture.jpg");
        assertThat(opts.getInline()).isFalse();
        assertThat(opts.getAsync()).isFalse();
        assertThat(opts.getStorage()).isEqualTo(StorageOption.of(true));
        assertThat(opts.getWebhook()).isEqualTo("https://hooks.example.com/htmlimg");
        assertThat(opts.getApiVer()).isEqualTo("v1");
        assertThat(opts.getProjectId()).isEqualTo("proj-005");
    }

    @Test
    void htmlToImageOptions_convenienceOverloads() {
        HtmlToImageOptions opts = HtmlToImageOptions.builder()
                .html("<html></html>")
                .height("500px")
                .width(800)
                .generatePreview(true)
                .storage(true)
                .emulateScreen()
                .build();
        assertThat(opts.getHeight()).isEqualTo(LayoutDimension.of("500px"));
        assertThat(opts.getWidth()).isEqualTo(LayoutDimension.of(800));

        HtmlToImageOptions opts2 = HtmlToImageOptions.builder()
                .html("<html></html>")
                .height(768)
                .width("1024px")
                .emulatePrint()
                .build();
        assertThat(opts2.getHeight()).isEqualTo(LayoutDimension.of(768));
        assertThat(opts2.getWidth()).isEqualTo(LayoutDimension.of("1024px"));

        HtmlToImageOptions opts3 = HtmlToImageOptions.builder()
                .html("<html></html>")
                .emulateNone()
                .build();
        assertThat(opts3.getEmulateMediaType().isNull()).isTrue();
    }

    // ===========================
    // TemplateToImageOptions
    // ===========================

    @Test
    void templateToImageOptions_allBuilderMethodsAndGetters() {
        Map<String, Object> data = Map.of("title", "Report", "year", 2024);

        TemplateToImageOptions opts = TemplateToImageOptions.builder()
                .templateId("tmpl-img-001")
                .template("<html>{{title}}</html>")
                .data(data)
                .imageType(ImageType.WEBP)
                .quality(70)
                .trim(true)
                .transparent(true)
                .generatePreview(GENERATE_PREVIEW)
                .waitUntil(WaitUntilOption.NETWORK_IDLE_0)
                .waitForFrame(true)
                .waitForFrameAttachment(true)
                .waitForFrameNavigation(WaitUntilOption.LOAD)
                .waitForFrameImages(true)
                .waitForFrameSelector(WAIT_SELECTOR)
                .waitForSelector(WAIT_SELECTOR)
                .preferCSSPageSize(false)
                .scale(1.5)
                .height(LayoutDimension.of(1000))
                .width(LayoutDimension.of(1400))
                .landscape(false)
                .pageRanges("1-2")
                .autoScroll(true)
                .viewPort(VIEWPORT)
                .timeZone("Australia/Sydney")
                .emulateMediaType(NullableString.of("screen"))
                .name("tmpl-image-job")
                .timeout(25000)
                .delay(750)
                .filename("chart.webp")
                .inline(true)
                .async(true)
                .storage(STORAGE_ID)
                .webhook("https://hooks.example.com/tmplimg")
                .apiVer("v2")
                .projectId("proj-006")
                .build();

        assertThat(opts.getTemplateId()).isEqualTo("tmpl-img-001");
        assertThat(opts.getTemplate()).isEqualTo("<html>{{title}}</html>");
        assertThat(opts.getData()).containsEntry("title", "Report");
        assertThat(opts.getImageType()).isEqualTo(ImageType.WEBP);
        assertThat(opts.getQuality()).isEqualTo(70);
        assertThat(opts.getTrim()).isTrue();
        assertThat(opts.getTransparent()).isTrue();
        assertThat(opts.getGeneratePreview()).isEqualTo(GENERATE_PREVIEW);
        assertThat(opts.getWaitUntil()).isEqualTo(WaitUntilOption.NETWORK_IDLE_0);
        assertThat(opts.getWaitForFrame()).isTrue();
        assertThat(opts.getPreferCSSPageSize()).isFalse();
        assertThat(opts.getScale()).isEqualTo(1.5);
        assertThat(opts.getHeight()).isEqualTo(LayoutDimension.of(1000));
        assertThat(opts.getWidth()).isEqualTo(LayoutDimension.of(1400));
        assertThat(opts.getLandscape()).isFalse();
        assertThat(opts.getPageRanges()).isEqualTo("1-2");
        assertThat(opts.getAutoScroll()).isTrue();
        assertThat(opts.getViewPort()).isEqualTo(VIEWPORT);
        assertThat(opts.getTimeZone()).isEqualTo("Australia/Sydney");
        assertThat(opts.getEmulateMediaType()).isEqualTo(NullableString.of("screen"));
        assertThat(opts.getName()).isEqualTo("tmpl-image-job");
        assertThat(opts.getTimeout()).isEqualTo(25000);
        assertThat(opts.getDelay()).isEqualTo(750);
        assertThat(opts.getFilename()).isEqualTo("chart.webp");
        assertThat(opts.getInline()).isTrue();
        assertThat(opts.getAsync()).isTrue();
        assertThat(opts.getStorage()).isEqualTo(STORAGE_ID);
        assertThat(opts.getWebhook()).isEqualTo("https://hooks.example.com/tmplimg");
        assertThat(opts.getApiVer()).isEqualTo("v2");
        assertThat(opts.getProjectId()).isEqualTo("proj-006");
    }

    @Test
    void templateToImageOptions_convenienceOverloads() {
        TemplateToImageOptions opts = TemplateToImageOptions.builder()
                .templateId("tmpl-x")
                .height("500px")
                .width(800)
                .generatePreview(true)
                .storage(true)
                .emulateScreen()
                .build();
        assertThat(opts.getHeight()).isEqualTo(LayoutDimension.of("500px"));
        assertThat(opts.getWidth()).isEqualTo(LayoutDimension.of(800));

        TemplateToImageOptions opts2 = TemplateToImageOptions.builder()
                .templateId("tmpl-x")
                .height(768)
                .width("1024px")
                .emulatePrint()
                .build();
        assertThat(opts2.getHeight()).isEqualTo(LayoutDimension.of(768));
        assertThat(opts2.getWidth()).isEqualTo(LayoutDimension.of("1024px"));

        TemplateToImageOptions opts3 = TemplateToImageOptions.builder()
                .templateId("tmpl-x")
                .emulateNone()
                .build();
        assertThat(opts3.getEmulateMediaType().isNull()).isTrue();
    }

    // ===========================
    // DocxToPdfOptions
    // ===========================

    @Test
    void docxToPdfOptions_allBuilderMethodsAndGetters() {
        FileInput file = FileInput.fromBytes(new byte[]{1, 2, 3}, "document.docx");

        DocxToPdfOptions opts = DocxToPdfOptions.builder()
                .file(file)
                .name("docx-job")
                .timeout(45000)
                .delay(500)
                .filename("converted.pdf")
                .inline(true)
                .async(true)
                .storage(STORAGE_ID)
                .webhook("https://hooks.example.com/docx")
                .apiVer("v2")
                .projectId("proj-007")
                .build();

        assertThat(opts.getFile()).isSameAs(file);
        assertThat(opts.getName()).isEqualTo("docx-job");
        assertThat(opts.getTimeout()).isEqualTo(45000);
        assertThat(opts.getDelay()).isEqualTo(500);
        assertThat(opts.getFilename()).isEqualTo("converted.pdf");
        assertThat(opts.getInline()).isTrue();
        assertThat(opts.getAsync()).isTrue();
        assertThat(opts.getStorage()).isEqualTo(STORAGE_ID);
        assertThat(opts.getWebhook()).isEqualTo("https://hooks.example.com/docx");
        assertThat(opts.getApiVer()).isEqualTo("v2");
        assertThat(opts.getProjectId()).isEqualTo("proj-007");
    }

    @Test
    void docxToPdfOptions_storageBoolOverload() {
        DocxToPdfOptions opts = DocxToPdfOptions.builder()
                .file(FileInput.fromBytes(new byte[]{1}, "test.docx"))
                .storage(true)
                .build();
        assertThat(opts.getStorage()).isEqualTo(StorageOption.of(true));
    }

    // ===========================
    // DocxToHtmlOptions
    // ===========================

    @Test
    void docxToHtmlOptions_allBuilderMethodsAndGetters() {
        FileInput file = FileInput.fromBytes(new byte[]{4, 5, 6}, "document.docx");

        DocxToHtmlOptions opts = DocxToHtmlOptions.builder()
                .file(file)
                .name("docx-html-job")
                .timeout(30000)
                .delay(250)
                .filename("converted.html")
                .inline(false)
                .async(false)
                .storage(StorageOption.of(false))
                .webhook("https://hooks.example.com/docxhtml")
                .apiVer("v1")
                .projectId("proj-008")
                .build();

        assertThat(opts.getFile()).isSameAs(file);
        assertThat(opts.getName()).isEqualTo("docx-html-job");
        assertThat(opts.getTimeout()).isEqualTo(30000);
        assertThat(opts.getDelay()).isEqualTo(250);
        assertThat(opts.getFilename()).isEqualTo("converted.html");
        assertThat(opts.getInline()).isFalse();
        assertThat(opts.getAsync()).isFalse();
        assertThat(opts.getStorage()).isEqualTo(StorageOption.of(false));
        assertThat(opts.getWebhook()).isEqualTo("https://hooks.example.com/docxhtml");
        assertThat(opts.getApiVer()).isEqualTo("v1");
        assertThat(opts.getProjectId()).isEqualTo("proj-008");
    }

    @Test
    void docxToHtmlOptions_storageBoolOverload() {
        DocxToHtmlOptions opts = DocxToHtmlOptions.builder()
                .file(FileInput.fromBytes(new byte[]{1}, "test.docx"))
                .storage(true)
                .build();
        assertThat(opts.getStorage()).isEqualTo(StorageOption.of(true));
    }

    // ===========================
    // PdfToDocxOptions
    // ===========================

    @Test
    void pdfToDocxOptions_allBuilderMethodsAndGetters() {
        FileInput file = FileInput.fromBytes(new byte[]{7, 8, 9}, "document.pdf");

        PdfToDocxOptions opts = PdfToDocxOptions.builder()
                .file(file)
                .name("pdf-docx-job")
                .timeout(50000)
                .delay(600)
                .filename("converted.docx")
                .inline(true)
                .async(true)
                .storage(STORAGE_ID)
                .webhook("https://hooks.example.com/pdfdocx")
                .apiVer("v2")
                .projectId("proj-009")
                .build();

        assertThat(opts.getFile()).isSameAs(file);
        assertThat(opts.getName()).isEqualTo("pdf-docx-job");
        assertThat(opts.getTimeout()).isEqualTo(50000);
        assertThat(opts.getDelay()).isEqualTo(600);
        assertThat(opts.getFilename()).isEqualTo("converted.docx");
        assertThat(opts.getInline()).isTrue();
        assertThat(opts.getAsync()).isTrue();
        assertThat(opts.getStorage()).isEqualTo(STORAGE_ID);
        assertThat(opts.getWebhook()).isEqualTo("https://hooks.example.com/pdfdocx");
        assertThat(opts.getApiVer()).isEqualTo("v2");
        assertThat(opts.getProjectId()).isEqualTo("proj-009");
    }

    @Test
    void pdfToDocxOptions_storageBoolOverload() {
        PdfToDocxOptions opts = PdfToDocxOptions.builder()
                .file(FileInput.fromBytes(new byte[]{1}, "test.pdf"))
                .storage(true)
                .build();
        assertThat(opts.getStorage()).isEqualTo(StorageOption.of(true));
    }

    // ===========================
    // MergePdfsOptions
    // ===========================

    @Test
    void mergePdfsOptions_allBuilderMethodsAndGetters() {
        MergePdfsOptions opts = MergePdfsOptions.builder()
                .url("https://example.com/doc.pdf")
                .authentication(AUTH)
                .batch(BATCH)
                .cookies(List.of(COOKIE))
                .name("merge-job")
                .timeout(60000)
                .delay(100)
                .filename("merged.pdf")
                .inline(true)
                .async(true)
                .storage(STORAGE_ID)
                .webhook("https://hooks.example.com/merge")
                .apiVer("v2")
                .projectId("proj-010")
                .build();

        assertThat(opts.getUrl()).isEqualTo("https://example.com/doc.pdf");
        assertThat(opts.getAuthentication()).isEqualTo(AUTH);
        assertThat(opts.getBatch()).isEqualTo(BATCH);
        assertThat(opts.getCookies()).hasSize(1);
        assertThat(opts.getName()).isEqualTo("merge-job");
        assertThat(opts.getTimeout()).isEqualTo(60000);
        assertThat(opts.getDelay()).isEqualTo(100);
        assertThat(opts.getFilename()).isEqualTo("merged.pdf");
        assertThat(opts.getInline()).isTrue();
        assertThat(opts.getAsync()).isTrue();
        assertThat(opts.getStorage()).isEqualTo(STORAGE_ID);
        assertThat(opts.getWebhook()).isEqualTo("https://hooks.example.com/merge");
        assertThat(opts.getApiVer()).isEqualTo("v2");
        assertThat(opts.getProjectId()).isEqualTo("proj-010");
    }

    @Test
    void mergePdfsOptions_storageBoolOverload() {
        MergePdfsOptions opts = MergePdfsOptions.builder()
                .url("https://example.com/a.pdf")
                .storage(true)
                .build();
        assertThat(opts.getStorage()).isEqualTo(StorageOption.of(true));
    }

    // ===========================
    // PuppeteerOptions (standalone)
    // ===========================

    @Test
    void puppeteerOptions_allBuilderMethodsAndGetters() {
        PuppeteerOptions opts = PuppeteerOptions.builder()
                .waitUntil(WaitUntilOption.NETWORK_IDLE_0)
                .waitForFrame(true)
                .waitForFrameAttachment(true)
                .waitForFrameNavigation(WaitUntilOption.LOAD)
                .waitForFrameImages(true)
                .waitForFrameSelector(WAIT_SELECTOR)
                .waitForSelector(WAIT_SELECTOR)
                .preferCSSPageSize(true)
                .scale(1.25)
                .height(LayoutDimension.of("900px"))
                .width(LayoutDimension.of(1200))
                .landscape(true)
                .pageRanges("1-10")
                .autoScroll(true)
                .viewPort(VIEWPORT)
                .timeZone("Pacific/Auckland")
                .emulateMediaType(NullableString.of("screen"))
                .build();

        assertThat(opts.getWaitUntil()).isEqualTo(WaitUntilOption.NETWORK_IDLE_0);
        assertThat(opts.getWaitForFrame()).isTrue();
        assertThat(opts.getWaitForFrameAttachment()).isTrue();
        assertThat(opts.getWaitForFrameNavigation()).isEqualTo(WaitUntilOption.LOAD);
        assertThat(opts.getWaitForFrameImages()).isTrue();
        assertThat(opts.getWaitForFrameSelector()).isEqualTo(WAIT_SELECTOR);
        assertThat(opts.getWaitForSelector()).isEqualTo(WAIT_SELECTOR);
        assertThat(opts.getPreferCSSPageSize()).isTrue();
        assertThat(opts.getScale()).isEqualTo(1.25);
        assertThat(opts.getHeight()).isEqualTo(LayoutDimension.of("900px"));
        assertThat(opts.getWidth()).isEqualTo(LayoutDimension.of(1200));
        assertThat(opts.getLandscape()).isTrue();
        assertThat(opts.getPageRanges()).isEqualTo("1-10");
        assertThat(opts.getAutoScroll()).isTrue();
        assertThat(opts.getViewPort()).isEqualTo(VIEWPORT);
        assertThat(opts.getTimeZone()).isEqualTo("Pacific/Auckland");
        assertThat(opts.getEmulateMediaType()).isEqualTo(NullableString.of("screen"));
    }

    @Test
    void puppeteerOptions_convenienceOverloads() {
        PuppeteerOptions opts1 = PuppeteerOptions.builder()
                .height("500px")
                .width(800)
                .emulateScreen()
                .build();
        assertThat(opts1.getHeight()).isEqualTo(LayoutDimension.of("500px"));
        assertThat(opts1.getWidth()).isEqualTo(LayoutDimension.of(800));
        assertThat(opts1.getEmulateMediaType()).isEqualTo(NullableString.emulateScreen());

        PuppeteerOptions opts2 = PuppeteerOptions.builder()
                .height(768)
                .width("1024px")
                .emulatePrint()
                .build();
        assertThat(opts2.getHeight()).isEqualTo(LayoutDimension.of(768));
        assertThat(opts2.getWidth()).isEqualTo(LayoutDimension.of("1024px"));
        assertThat(opts2.getEmulateMediaType()).isEqualTo(NullableString.emulatePrint());

        PuppeteerOptions opts3 = PuppeteerOptions.builder()
                .emulateNone()
                .build();
        assertThat(opts3.getEmulateMediaType().isNull()).isTrue();
    }

    @Test
    void puppeteerOptions_equalsAndHashCode() {
        PuppeteerOptions a = PuppeteerOptions.builder()
                .waitUntil(WaitUntilOption.LOAD)
                .waitForFrame(true)
                .scale(1.5)
                .landscape(true)
                .viewPort(VIEWPORT)
                .emulateMediaType(NullableString.emulateScreen())
                .build();
        PuppeteerOptions b = PuppeteerOptions.builder()
                .waitUntil(WaitUntilOption.LOAD)
                .waitForFrame(true)
                .scale(1.5)
                .landscape(true)
                .viewPort(VIEWPORT)
                .emulateMediaType(NullableString.emulateScreen())
                .build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a); // reflexive
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }

    // ===========================
    // WaitForSelectorOptions
    // ===========================

    @Test
    void waitForSelectorOptions_allBuilderMethodsAndGetters() {
        WaitForSelectorOptions opts = WaitForSelectorOptions.builder()
                .selector(".my-element")
                .options(true, false, 10000)
                .build();

        assertThat(opts.getSelector()).isEqualTo(".my-element");
        assertThat(opts.getOptions()).isNotNull();
        assertThat(opts.getOptions().getVisible()).isTrue();
        assertThat(opts.getOptions().getHidden()).isFalse();
        assertThat(opts.getOptions().getTimeout()).isEqualTo(10000);
    }

    @Test
    void waitForSelectorOptions_withSelectorSubOptionsObject() {
        WaitForSelectorOptions.SelectorSubOptions subOpts =
                WaitForSelectorOptions.SelectorSubOptions.of(false, true, 3000);

        WaitForSelectorOptions opts = WaitForSelectorOptions.builder()
                .selector("#target")
                .options(subOpts)
                .build();

        assertThat(opts.getSelector()).isEqualTo("#target");
        assertThat(opts.getOptions()).isEqualTo(subOpts);
        assertThat(opts.getOptions().getVisible()).isFalse();
        assertThat(opts.getOptions().getHidden()).isTrue();
        assertThat(opts.getOptions().getTimeout()).isEqualTo(3000);
    }

    @Test
    void waitForSelectorOptions_equalsAndHashCode() {
        WaitForSelectorOptions a = WaitForSelectorOptions.builder()
                .selector("#a")
                .options(true, false, 5000)
                .build();
        WaitForSelectorOptions b = WaitForSelectorOptions.builder()
                .selector("#a")
                .options(true, false, 5000)
                .build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }

    @Test
    void selectorSubOptions_equalsAndHashCode() {
        WaitForSelectorOptions.SelectorSubOptions a =
                WaitForSelectorOptions.SelectorSubOptions.of(true, false, 1000);
        WaitForSelectorOptions.SelectorSubOptions b =
                WaitForSelectorOptions.SelectorSubOptions.of(true, false, 1000);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }

    // ===========================
    // StorageRequestOptions
    // ===========================

    @Test
    void storageRequestOptions_ofAndGetters() {
        StorageRequestOptions opts = StorageRequestOptions.of("storage-123");
        assertThat(opts.getId()).isEqualTo("storage-123");
    }

    @Test
    void storageRequestOptions_equalsAndHashCode() {
        StorageRequestOptions a = StorageRequestOptions.of("id-1");
        StorageRequestOptions b = StorageRequestOptions.of("id-1");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }

    @Test
    void storageRequestOptions_nullIdThrows() {
        assertThatThrownBy(() -> StorageRequestOptions.of(null))
                .isInstanceOf(NullPointerException.class);
    }

    // ===========================
    // StorageParams
    // ===========================

    @Test
    void storageParams_allFieldsAndGetters() {
        StorageParams params = StorageParams.builder("My Bucket", "us-east-1", "AKID", "SECRET", "my-bucket")
                .endpoint("https://s3.example.com")
                .build();

        assertThat(params.getTitle()).isEqualTo("My Bucket");
        assertThat(params.getRegion()).isEqualTo("us-east-1");
        assertThat(params.getAccessKeyId()).isEqualTo("AKID");
        assertThat(params.getSecretAccessKey()).isEqualTo("SECRET");
        assertThat(params.getBucket()).isEqualTo("my-bucket");
        assertThat(params.getEndpoint()).isEqualTo("https://s3.example.com");
    }

    @Test
    void storageParams_withoutEndpoint() {
        StorageParams params = StorageParams.builder("Bucket", "eu-west-1", "AK", "SK", "bucket")
                .build();

        assertThat(params.getEndpoint()).isNull();
    }

    @Test
    void storageParams_equalsAndHashCode() {
        StorageParams a = StorageParams.builder("B", "r", "ak", "sk", "bkt").build();
        StorageParams b = StorageParams.builder("B", "r", "ak", "sk", "bkt").build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }

    // ===========================
    // Cookie
    // ===========================

    @Test
    void cookie_allBuilderMethodsAndGetters() {
        Cookie cookie = Cookie.builder("session", "xyz789")
                .url("https://example.com")
                .domain(".example.com")
                .path("/app")
                .expires(7200)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .build();

        assertThat(cookie.getName()).isEqualTo("session");
        assertThat(cookie.getValue()).isEqualTo("xyz789");
        assertThat(cookie.getUrl()).isEqualTo("https://example.com");
        assertThat(cookie.getDomain()).isEqualTo(".example.com");
        assertThat(cookie.getPath()).isEqualTo("/app");
        assertThat(cookie.getExpires()).isEqualTo(7200);
        assertThat(cookie.getHttpOnly()).isTrue();
        assertThat(cookie.getSecure()).isTrue();
        assertThat(cookie.getSameSite()).isEqualTo("Lax");
    }

    @Test
    void cookie_equalsAndHashCode() {
        Cookie a = Cookie.builder("name", "val").build();
        Cookie b = Cookie.builder("name", "val").build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }

    // ===========================
    // HeaderFooterTemplate
    // ===========================

    @Test
    void headerFooterTemplate_allBuilderMethodsAndGetters() {
        Map<String, Object> style = Map.of("color", "black", "fontSize", "10pt");
        Map<String, Object> imageStyle = Map.of("width", "50px");
        Margin m = Margin.builder().top(10).bottom(10).left(5).right(5).build();

        HeaderFooterTemplate tmpl = HeaderFooterTemplate.builder()
                .method("html")
                .selector(".header")
                .margin(m)
                .style(style)
                .imageStyle(imageStyle)
                .template("<div>Header</div>")
                .templateString("<span>{{page}}</span>")
                .build();

        assertThat(tmpl.getMethod()).isEqualTo("html");
        assertThat(tmpl.getSelector()).isEqualTo(".header");
        assertThat(tmpl.getMargin()).isEqualTo(m);
        assertThat(tmpl.getStyle()).isEqualTo(style);
        assertThat(tmpl.getImageStyle()).isEqualTo(imageStyle);
        assertThat(tmpl.getTemplate()).isEqualTo("<div>Header</div>");
        assertThat(tmpl.getTemplateString()).isEqualTo("<span>{{page}}</span>");
    }

    @Test
    void headerFooterTemplate_equalsAndHashCode() {
        HeaderFooterTemplate a = HeaderFooterTemplate.builder().method("html").selector("#h").build();
        HeaderFooterTemplate b = HeaderFooterTemplate.builder().method("html").selector("#h").build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }

    // ===========================
    // PreviewOptions
    // ===========================

    @Test
    void previewOptions_allBuilderMethodsAndGetters() {
        PreviewOptions opts = PreviewOptions.builder()
                .width(400)
                .height(600)
                .type(ImageType.JPEG)
                .quality(90)
                .maintainAspectRatio(false)
                .build();

        assertThat(opts.getWidth()).isEqualTo(400);
        assertThat(opts.getHeight()).isEqualTo(600);
        assertThat(opts.getType()).isEqualTo(ImageType.JPEG);
        assertThat(opts.getQuality()).isEqualTo(90);
        assertThat(opts.getMaintainAspectRatio()).isFalse();
    }

    @Test
    void previewOptions_equalsAndHashCode() {
        PreviewOptions a = PreviewOptions.builder().width(200).height(300).type(ImageType.PNG).quality(80).maintainAspectRatio(true).build();
        PreviewOptions b = PreviewOptions.builder().width(200).height(300).type(ImageType.PNG).quality(80).maintainAspectRatio(true).build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }

    // ===========================
    // Viewport
    // ===========================

    @Test
    void viewport_allBuilderMethodsAndGetters() {
        Viewport vp = Viewport.builder()
                .width(1440)
                .height(900)
                .deviceScaleFactor(1.5)
                .isMobile(true)
                .hasTouch(true)
                .isLandscape(false)
                .build();

        assertThat(vp.getWidth()).isEqualTo(1440);
        assertThat(vp.getHeight()).isEqualTo(900);
        assertThat(vp.getDeviceScaleFactor()).isEqualTo(1.5);
        assertThat(vp.getIsMobile()).isTrue();
        assertThat(vp.getHasTouch()).isTrue();
        assertThat(vp.getIsLandscape()).isFalse();
    }

    @Test
    void viewport_equalsAndHashCode() {
        Viewport a = Viewport.builder().width(1920).height(1080).deviceScaleFactor(2.0).isMobile(false).hasTouch(false).isLandscape(true).build();
        Viewport b = Viewport.builder().width(1920).height(1080).deviceScaleFactor(2.0).isMobile(false).hasTouch(false).isLandscape(true).build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }

    // ===========================
    // WaitForJobOptions
    // ===========================

    @Test
    void waitForJobOptions_customIntervalAndMaxWait() {
        WaitForJobOptions opts = WaitForJobOptions.builder()
                .interval(Duration.ofSeconds(10))
                .maxWait(Duration.ofMinutes(5))
                .build();

        assertThat(opts.getInterval()).isEqualTo(Duration.ofSeconds(10));
        assertThat(opts.getMaxWait()).isEqualTo(Duration.ofMinutes(5));
    }

    @Test
    void waitForJobOptions_defaults() {
        WaitForJobOptions opts = WaitForJobOptions.builder().build();
        assertThat(opts.getInterval()).isEqualTo(Duration.ofSeconds(5));
        assertThat(opts.getMaxWait()).isEqualTo(Duration.ofSeconds(300));
    }

    @Test
    void waitForJobOptions_equalsAndHashCode() {
        WaitForJobOptions a = WaitForJobOptions.builder().interval(Duration.ofSeconds(3)).maxWait(Duration.ofMinutes(2)).build();
        WaitForJobOptions b = WaitForJobOptions.builder().interval(Duration.ofSeconds(3)).maxWait(Duration.ofMinutes(2)).build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }

    @Test
    void waitForJobOptions_intervalTooSmallThrows() {
        assertThatThrownBy(() -> WaitForJobOptions.builder().interval(Duration.ofSeconds(1)).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least");
    }

    @Test
    void waitForJobOptions_nullIntervalThrows() {
        assertThatThrownBy(() -> WaitForJobOptions.builder().interval(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void waitForJobOptions_nullMaxWaitThrows() {
        assertThatThrownBy(() -> WaitForJobOptions.builder().maxWait(null))
                .isInstanceOf(NullPointerException.class);
    }

    // ===========================
    // Margin builder overloads
    // ===========================

    @Test
    void margin_allOverloads() {
        // LayoutDimension overloads
        Margin m1 = Margin.builder()
                .top(LayoutDimension.of("10mm"))
                .bottom(LayoutDimension.of(20))
                .left(LayoutDimension.of("15mm"))
                .right(LayoutDimension.of(25))
                .build();
        assertThat(m1.getTop()).isEqualTo(LayoutDimension.of("10mm"));
        assertThat(m1.getBottom()).isEqualTo(LayoutDimension.of(20));
        assertThat(m1.getLeft()).isEqualTo(LayoutDimension.of("15mm"));
        assertThat(m1.getRight()).isEqualTo(LayoutDimension.of(25));

        // String overloads
        Margin m2 = Margin.builder().top("5px").bottom("10px").left("3px").right("7px").build();
        assertThat(m2.getTop()).isEqualTo(LayoutDimension.of("5px"));

        // Int overloads
        Margin m3 = Margin.builder().top(5).bottom(10).left(3).right(7).build();
        assertThat(m3.getTop()).isEqualTo(LayoutDimension.of(5));
    }

    @Test
    void margin_equalsAndHashCode() {
        Margin a = Margin.builder().top("10mm").bottom("20mm").build();
        Margin b = Margin.builder().top("10mm").bottom("20mm").build();

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }

    // ===========================
    // StorageOption
    // ===========================

    @Test
    void storageOption_boolAndIdVariants() {
        StorageOption boolTrue = StorageOption.of(true);
        assertThat(boolTrue.isBooleanValue()).isTrue();
        assertThat(boolTrue.isIdValue()).isFalse();
        assertThat(boolTrue.getBoolValue()).isTrue();
        assertThat(boolTrue.getIdValue()).isNull();

        StorageOption boolFalse = StorageOption.of(false);
        assertThat(boolFalse.getBoolValue()).isFalse();

        StorageOption idOpt = StorageOption.ofId("abc");
        assertThat(idOpt.isBooleanValue()).isFalse();
        assertThat(idOpt.isIdValue()).isTrue();
        assertThat(idOpt.getBoolValue()).isNull();
        assertThat(idOpt.getIdValue()).isEqualTo("abc");
    }

    @Test
    void storageOption_toString() {
        assertThat(StorageOption.of(true).toString()).isEqualTo("true");
        assertThat(StorageOption.of(false).toString()).isEqualTo("false");
        assertThat(StorageOption.ofId("x").toString()).contains("x");
    }

    @Test
    void storageOption_nullIdThrows() {
        assertThatThrownBy(() -> StorageOption.ofId(null))
                .isInstanceOf(NullPointerException.class);
    }

    // ===========================
    // GeneratePreviewOption
    // ===========================

    @Test
    void generatePreviewOption_boolAndObjectVariants() {
        GeneratePreviewOption boolOpt = GeneratePreviewOption.of(true);
        assertThat(boolOpt.isBooleanValue()).isTrue();
        assertThat(boolOpt.isObjectValue()).isFalse();
        assertThat(boolOpt.getBoolValue()).isTrue();
        assertThat(boolOpt.getObjectValue()).isNull();

        PreviewOptions po = PreviewOptions.builder().width(100).build();
        GeneratePreviewOption objOpt = GeneratePreviewOption.of(po);
        assertThat(objOpt.isBooleanValue()).isFalse();
        assertThat(objOpt.isObjectValue()).isTrue();
        assertThat(objOpt.getBoolValue()).isNull();
        assertThat(objOpt.getObjectValue()).isEqualTo(po);
    }

    @Test
    void generatePreviewOption_toString() {
        assertThat(GeneratePreviewOption.of(true).toString()).isEqualTo("true");
        assertThat(GeneratePreviewOption.of(false).toString()).isEqualTo("false");
    }

    @Test
    void generatePreviewOption_nullObjectThrows() {
        assertThatThrownBy(() -> GeneratePreviewOption.of((PreviewOptions) null))
                .isInstanceOf(NullPointerException.class);
    }

    // ===========================
    // NullableString
    // ===========================

    @Test
    void nullableString_variants() {
        NullableString present = NullableString.of("screen");
        assertThat(present.isNull()).isFalse();
        assertThat(present.isPresent()).isTrue();
        assertThat(present.getValue()).isEqualTo("screen");

        NullableString nullVal = NullableString.ofNull();
        assertThat(nullVal.isNull()).isTrue();
        assertThat(nullVal.isPresent()).isFalse();
        assertThat(nullVal.getValue()).isNull();
    }

    @Test
    void nullableString_toString() {
        assertThat(NullableString.of("screen").toString()).isEqualTo("screen");
        assertThat(NullableString.ofNull().toString()).isEqualTo("null");
    }

    @Test
    void nullableString_nullValueThrows() {
        assertThatThrownBy(() -> NullableString.of(null))
                .isInstanceOf(NullPointerException.class);
    }

    // ===========================
    // LayoutDimension
    // ===========================

    @Test
    void layoutDimension_variants() {
        LayoutDimension css = LayoutDimension.of("100px");
        assertThat(css.isCssValue()).isTrue();
        assertThat(css.isPixelValue()).isFalse();
        assertThat(css.getCssValue()).isEqualTo("100px");
        assertThat(css.getPixelValue()).isNull();

        LayoutDimension px = LayoutDimension.of(100);
        assertThat(px.isCssValue()).isFalse();
        assertThat(px.isPixelValue()).isTrue();
        assertThat(px.getCssValue()).isNull();
        assertThat(px.getPixelValue()).isEqualTo(100);
    }

    @Test
    void layoutDimension_toString() {
        assertThat(LayoutDimension.of("100px").toString()).isEqualTo("100px");
        assertThat(LayoutDimension.of(100).toString()).isEqualTo("100");
    }

    @Test
    void layoutDimension_nullCssThrows() {
        assertThatThrownBy(() -> LayoutDimension.of((String) null))
                .isInstanceOf(NullPointerException.class);
    }

    // ===========================
    // Authentication
    // ===========================

    @Test
    void authentication_getters() {
        Authentication auth = Authentication.of("admin", "secret");
        assertThat(auth.getUsername()).isEqualTo("admin");
        assertThat(auth.getPassword()).isEqualTo("secret");
    }

    @Test
    void authentication_equalsAndHashCode() {
        Authentication a = Authentication.of("u", "p");
        Authentication b = Authentication.of("u", "p");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }

    // ===========================
    // Batch
    // ===========================

    @Test
    void batch_getters() {
        Batch batch = Batch.of(List.of("url1", "url2"));
        assertThat(batch.getUrls()).containsExactly("url1", "url2");
    }

    @Test
    void batch_equalsAndHashCode() {
        Batch a = Batch.of(List.of("x"));
        Batch b = Batch.of(List.of("x"));
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
    }

    @Test
    void batch_nullUrlsThrows() {
        assertThatThrownBy(() -> Batch.of(null))
                .isInstanceOf(NullPointerException.class);
    }

    // ===========================
    // ListTemplatesOptions
    // ===========================

    @Test
    void listTemplatesOptions_allBuilderMethodsAndGetters() {
        ListTemplatesOptions opts = ListTemplatesOptions.builder()
                .type("pdf")
                .category("business")
                .tags("invoice,receipt")
                .expand(true)
                .build();

        assertThat(opts.getType()).isEqualTo("pdf");
        assertThat(opts.getCategory()).isEqualTo("business");
        assertThat(opts.getTags()).isEqualTo("invoice,receipt");
        assertThat(opts.getExpand()).isTrue();
    }

    @Test
    void listTemplatesOptions_equalsAndHashCode() {
        ListTemplatesOptions a = ListTemplatesOptions.builder().type("pdf").build();
        ListTemplatesOptions b = ListTemplatesOptions.builder().type("pdf").build();
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    // ===========================
    // ConversionResult
    // ===========================

    @Test
    void conversionResult_jobVariant() {
        ResponseHeaders headers = ResponseHeaders.builder()
                .workerJobId("wjid-1")
                .clusterId("c-1")
                .worker("w-1")
                .bandwidth(1024)
                .processTime(2500)
                .callsRemaining(958)
                .chargedTime(3000)
                .bandwidthCost(0.002)
                .processTimeCost(0.005)
                .apiCreditCost(0.01)
                .build();

        // Use a placeholder - ConversionResult just holds whatever Object is passed
        String jobJson = "{\"id\":\"job-1\"}";
        ConversionResult result = new ConversionResult(jobJson, headers, 200, "output.pdf");

        assertThat(result.getData()).isEqualTo(jobJson);
        assertThat(result.getHeaders()).isSameAs(headers);
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getFilename()).isEqualTo("output.pdf");
        assertThat(result.isBinary()).isFalse();
        assertThat(result.isJob()).isFalse(); // it's a String, not a Job
    }

    @Test
    void conversionResult_binaryVariant() {
        byte[] bytes = new byte[]{1, 2, 3, 4, 5};
        ResponseHeaders headers = ResponseHeaders.builder().build();
        ConversionResult result = new ConversionResult(bytes, headers, 200, "file.pdf");

        assertThat(result.isBinary()).isTrue();
        assertThat(result.isJob()).isFalse();
        assertThat(result.getBytes()).isEqualTo(bytes);
        assertThat(result.getData()).isEqualTo(bytes);
    }

    // ===========================
    // ResponseHeaders
    // ===========================

    @Test
    void responseHeaders_allBuilderMethodsAndGetters() {
        ResponseHeaders headers = ResponseHeaders.builder()
                .workerJobId("wjid-1")
                .clusterId("cluster-a")
                .worker("worker-3")
                .bandwidth(2048)
                .processTime(3000)
                .callsRemaining(100)
                .chargedTime(3500)
                .bandwidthCost(0.003)
                .processTimeCost(0.006)
                .apiCreditCost(0.01)
                .build();

        assertThat(headers.getWorkerJobId()).isEqualTo("wjid-1");
        assertThat(headers.getClusterId()).isEqualTo("cluster-a");
        assertThat(headers.getWorker()).isEqualTo("worker-3");
        assertThat(headers.getBandwidth()).isEqualTo(2048);
        assertThat(headers.getProcessTime()).isEqualTo(3000);
        assertThat(headers.getCallsRemaining()).isEqualTo(100);
        assertThat(headers.getChargedTime()).isEqualTo(3500);
        assertThat(headers.getBandwidthCost()).isEqualTo(0.003);
        assertThat(headers.getProcessTimeCost()).isEqualTo(0.006);
        assertThat(headers.getApiCreditCost()).isEqualTo(0.01);
    }

    @Test
    void responseHeaders_fromHttpHeaders() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("cl-worker-job-id", "wjid-2");
        headerMap.put("cl-cluster-id", "cluster-b");
        headerMap.put("cl-worker", "worker-5");
        headerMap.put("cl-bandwidth", "4096");
        headerMap.put("cl-process-time", "5000");
        headerMap.put("cl-calls-remaining", "50");
        headerMap.put("cl-charged-time", "5500");
        headerMap.put("cl-bandwidth-cost", "0.005");
        headerMap.put("cl-process-time-cost", "0.008");
        headerMap.put("cl-api-credit-cost", "0.02");

        ResponseHeaders headers = ResponseHeaders.fromHttpHeaders(headerMap::get);

        assertThat(headers.getWorkerJobId()).isEqualTo("wjid-2");
        assertThat(headers.getClusterId()).isEqualTo("cluster-b");
        assertThat(headers.getWorker()).isEqualTo("worker-5");
        assertThat(headers.getBandwidth()).isEqualTo(4096);
        assertThat(headers.getProcessTime()).isEqualTo(5000);
        assertThat(headers.getCallsRemaining()).isEqualTo(50);
        assertThat(headers.getChargedTime()).isEqualTo(5500);
        assertThat(headers.getBandwidthCost()).isEqualTo(0.005);
        assertThat(headers.getProcessTimeCost()).isEqualTo(0.008);
        assertThat(headers.getApiCreditCost()).isEqualTo(0.02);
    }

    @Test
    void responseHeaders_fromHttpHeaders_withNulls() {
        ResponseHeaders headers = ResponseHeaders.fromHttpHeaders(key -> null);

        assertThat(headers.getWorkerJobId()).isNull();
        assertThat(headers.getBandwidth()).isNull();
        assertThat(headers.getBandwidthCost()).isNull();
    }

    @Test
    void responseHeaders_fromHttpHeaders_withEmptyStrings() {
        ResponseHeaders headers = ResponseHeaders.fromHttpHeaders(key -> "");

        assertThat(headers.getWorkerJobId()).isEmpty();
        assertThat(headers.getBandwidth()).isNull(); // parseInteger returns null for empty
        assertThat(headers.getBandwidthCost()).isNull(); // parseDouble returns null for empty
    }

    @Test
    void responseHeaders_fromHttpHeaders_withInvalidNumbers() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("cl-bandwidth", "not-a-number");
        headerMap.put("cl-bandwidth-cost", "invalid");

        ResponseHeaders headers = ResponseHeaders.fromHttpHeaders(headerMap::get);

        assertThat(headers.getBandwidth()).isNull();
        assertThat(headers.getBandwidthCost()).isNull();
    }

    // ===========================
    // FileInput
    // ===========================

    @Test
    void fileInput_fromBytes() {
        FileInput fi = FileInput.fromBytes(new byte[]{10, 20, 30}, "test.docx");
        assertThat(fi.getData()).isEqualTo(new byte[]{10, 20, 30});
        assertThat(fi.getFilename()).isEqualTo("test.docx");
        assertThat(fi.getContentType()).isEqualTo("application/octet-stream");
    }

    @Test
    void fileInput_fromBytesWithContentType() {
        FileInput fi = FileInput.fromBytes(new byte[]{1}, "test.pdf", "application/pdf");
        assertThat(fi.getContentType()).isEqualTo("application/pdf");
    }

    // ===========================
    // BaseOptions (standalone)
    // ===========================

    @Test
    void baseOptions_allBuilderMethodsAndGetters() {
        BaseOptions opts = BaseOptions.builder()
                .name("base-job")
                .timeout(30000)
                .delay(500)
                .filename("output.pdf")
                .inline(true)
                .async(true)
                .storage(STORAGE_ID)
                .webhook("https://hooks.example.com/base")
                .apiVer("v2")
                .projectId("proj-base")
                .build();

        assertThat(opts.getName()).isEqualTo("base-job");
        assertThat(opts.getTimeout()).isEqualTo(30000);
        assertThat(opts.getDelay()).isEqualTo(500);
        assertThat(opts.getFilename()).isEqualTo("output.pdf");
        assertThat(opts.getInline()).isTrue();
        assertThat(opts.getAsync()).isTrue();
        assertThat(opts.getStorage()).isEqualTo(STORAGE_ID);
        assertThat(opts.getWebhook()).isEqualTo("https://hooks.example.com/base");
        assertThat(opts.getApiVer()).isEqualTo("v2");
        assertThat(opts.getProjectId()).isEqualTo("proj-base");
    }

    @Test
    void baseOptions_storageBoolOverload() {
        BaseOptions opts = BaseOptions.builder().storage(true).build();
        assertThat(opts.getStorage()).isEqualTo(StorageOption.of(true));
    }

    @Test
    void baseOptions_equalsAndHashCode() {
        BaseOptions a = BaseOptions.builder().name("j").timeout(100).async(true).storage(StorageOption.of(true)).build();
        BaseOptions b = BaseOptions.builder().name("j").timeout(100).async(true).storage(StorageOption.of(true)).build();
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }

    // ===========================
    // PdfOptions (standalone)
    // ===========================

    @Test
    void pdfOptions_allBuilderMethodsAndGetters() {
        PdfOptions opts = PdfOptions.builder()
                .printBackground(true)
                .format(PdfFormat.A4)
                .margin(MARGIN)
                .headerTemplate(HEADER)
                .footerTemplate(FOOTER)
                .generatePreview(GENERATE_PREVIEW)
                .build();

        assertThat(opts.getPrintBackground()).isTrue();
        assertThat(opts.getFormat()).isEqualTo(PdfFormat.A4);
        assertThat(opts.getMargin()).isEqualTo(MARGIN);
        assertThat(opts.getHeaderTemplate()).isEqualTo(HEADER);
        assertThat(opts.getFooterTemplate()).isEqualTo(FOOTER);
        assertThat(opts.getGeneratePreview()).isEqualTo(GENERATE_PREVIEW);
    }

    @Test
    void pdfOptions_generatePreviewBoolOverload() {
        PdfOptions opts = PdfOptions.builder().generatePreview(true).build();
        assertThat(opts.getGeneratePreview()).isEqualTo(GeneratePreviewOption.of(true));
    }

    @Test
    void pdfOptions_equalsAndHashCode() {
        PdfOptions a = PdfOptions.builder().printBackground(true).format(PdfFormat.A4).build();
        PdfOptions b = PdfOptions.builder().printBackground(true).format(PdfFormat.A4).build();
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }

    // ===========================
    // ImageOptions (standalone)
    // ===========================

    @Test
    void imageOptions_allBuilderMethodsAndGetters() {
        ImageOptions opts = ImageOptions.builder()
                .imageType(ImageType.PNG)
                .quality(95)
                .trim(true)
                .transparent(false)
                .generatePreview(GENERATE_PREVIEW)
                .build();

        assertThat(opts.getImageType()).isEqualTo(ImageType.PNG);
        assertThat(opts.getQuality()).isEqualTo(95);
        assertThat(opts.getTrim()).isTrue();
        assertThat(opts.getTransparent()).isFalse();
        assertThat(opts.getGeneratePreview()).isEqualTo(GENERATE_PREVIEW);
    }

    @Test
    void imageOptions_generatePreviewBoolOverload() {
        ImageOptions opts = ImageOptions.builder().generatePreview(true).build();
        assertThat(opts.getGeneratePreview()).isEqualTo(GeneratePreviewOption.of(true));
    }

    @Test
    void imageOptions_equalsAndHashCode() {
        ImageOptions a = ImageOptions.builder().imageType(ImageType.JPEG).quality(80).build();
        ImageOptions b = ImageOptions.builder().imageType(ImageType.JPEG).quality(80).build();
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("string");
    }
}
