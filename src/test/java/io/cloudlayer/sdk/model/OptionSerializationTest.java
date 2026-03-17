package io.cloudlayer.sdk.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.cloudlayer.sdk.model.constants.ImageType;
import io.cloudlayer.sdk.model.constants.PdfFormat;
import io.cloudlayer.sdk.model.constants.WaitUntilOption;
import io.cloudlayer.sdk.model.endpoint.*;
import io.cloudlayer.sdk.model.options.*;
import io.cloudlayer.sdk.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OptionSerializationTest {

    // --- Union type round-trip tests ---

    @Test
    void layoutDimension_cssValue_roundTrip() throws Exception {
        LayoutDimension dim = LayoutDimension.of("100px");
        String json = JsonUtil.mapper().writeValueAsString(dim);
        assertThat(json).isEqualTo("\"100px\"");
        LayoutDimension deserialized = JsonUtil.mapper().readValue(json, LayoutDimension.class);
        assertThat(deserialized).isEqualTo(dim);
    }

    @Test
    void layoutDimension_pixelValue_roundTrip() throws Exception {
        LayoutDimension dim = LayoutDimension.of(100);
        String json = JsonUtil.mapper().writeValueAsString(dim);
        assertThat(json).isEqualTo("100");
        LayoutDimension deserialized = JsonUtil.mapper().readValue(json, LayoutDimension.class);
        assertThat(deserialized).isEqualTo(dim);
    }

    @Test
    void storageOption_boolTrue_roundTrip() throws Exception {
        StorageOption opt = StorageOption.of(true);
        String json = JsonUtil.mapper().writeValueAsString(opt);
        assertThat(json).isEqualTo("true");
        StorageOption deserialized = JsonUtil.mapper().readValue(json, StorageOption.class);
        assertThat(deserialized).isEqualTo(opt);
    }

    @Test
    void storageOption_boolFalse_roundTrip() throws Exception {
        StorageOption opt = StorageOption.of(false);
        String json = JsonUtil.mapper().writeValueAsString(opt);
        assertThat(json).isEqualTo("false");
        StorageOption deserialized = JsonUtil.mapper().readValue(json, StorageOption.class);
        assertThat(deserialized).isEqualTo(opt);
    }

    @Test
    void storageOption_id_roundTrip() throws Exception {
        StorageOption opt = StorageOption.ofId("storage-123");
        String json = JsonUtil.mapper().writeValueAsString(opt);
        assertThat(json).isEqualTo("{\"id\":\"storage-123\"}");
        StorageOption deserialized = JsonUtil.mapper().readValue(json, StorageOption.class);
        assertThat(deserialized).isEqualTo(opt);
    }

    @Test
    void nullableString_value_roundTrip() throws Exception {
        NullableString ns = NullableString.of("screen");
        String json = JsonUtil.mapper().writeValueAsString(ns);
        assertThat(json).isEqualTo("\"screen\"");
        NullableString deserialized = JsonUtil.mapper().readValue(json, NullableString.class);
        assertThat(deserialized).isEqualTo(ns);
    }

    @Test
    void nullableString_null_roundTrip() throws Exception {
        NullableString ns = NullableString.ofNull();
        String json = JsonUtil.mapper().writeValueAsString(ns);
        assertThat(json).isEqualTo("null");
        NullableString deserialized = JsonUtil.mapper().readValue(json, NullableString.class);
        assertThat(deserialized.isNull()).isTrue();
    }

    @Test
    void nullableString_convenienceMethods() {
        assertThat(NullableString.emulateScreen().getValue()).isEqualTo("screen");
        assertThat(NullableString.emulatePrint().getValue()).isEqualTo("print");
        assertThat(NullableString.emulateNone().isNull()).isTrue();
    }

    @Test
    void generatePreviewOption_bool_roundTrip() throws Exception {
        GeneratePreviewOption opt = GeneratePreviewOption.of(true);
        String json = JsonUtil.mapper().writeValueAsString(opt);
        assertThat(json).isEqualTo("true");
        GeneratePreviewOption deserialized = JsonUtil.mapper().readValue(json, GeneratePreviewOption.class);
        assertThat(deserialized).isEqualTo(opt);
    }

    @Test
    void generatePreviewOption_object_roundTrip() throws Exception {
        PreviewOptions preview = PreviewOptions.builder().quality(80).type(ImageType.PNG).build();
        GeneratePreviewOption opt = GeneratePreviewOption.of(preview);
        String json = JsonUtil.mapper().writeValueAsString(opt);
        assertThat(json).contains("\"quality\":80");
        assertThat(json).contains("\"type\":\"png\"");
        GeneratePreviewOption deserialized = JsonUtil.mapper().readValue(json, GeneratePreviewOption.class);
        assertThat(deserialized.isObjectValue()).isTrue();
        assertThat(deserialized.getObjectValue().getQuality()).isEqualTo(80);
    }

    // --- Composite type flat serialization tests ---

    @Test
    void urlToPdfOptions_serializesFlat() throws Exception {
        UrlToPdfOptions opts = UrlToPdfOptions.builder()
                .url("https://example.com")
                .format(PdfFormat.A4)
                .printBackground(true)
                .waitUntil(WaitUntilOption.NETWORK_IDLE_2)
                .name("test")
                .async(true)
                .storage(true)
                .build();
        String json = JsonUtil.mapper().writeValueAsString(opts);
        JsonNode node = JsonUtil.mapper().readTree(json);

        // All fields at top level — no nesting
        assertThat(node.get("url").asText()).isEqualTo("https://example.com");
        assertThat(node.get("format").asText()).isEqualTo("a4");
        assertThat(node.get("printBackground").asBoolean()).isTrue();
        assertThat(node.get("waitUntil").asText()).isEqualTo("networkidle2");
        assertThat(node.get("name").asText()).isEqualTo("test");
        assertThat(node.get("async").asBoolean()).isTrue();
        assertThat(node.get("storage").asBoolean()).isTrue();

        // No nested objects for option groups
        assertThat(node.has("pdfOptions")).isFalse();
        assertThat(node.has("puppeteerOptions")).isFalse();
        assertThat(node.has("baseOptions")).isFalse();
        assertThat(node.has("urlOptions")).isFalse();
    }

    @Test
    void htmlToPdfOptions_serializesFlat() throws Exception {
        HtmlToPdfOptions opts = HtmlToPdfOptions.builder()
                .html("PGh0bWw+PC9odG1sPg==")
                .format(PdfFormat.LETTER)
                .build();
        String json = JsonUtil.mapper().writeValueAsString(opts);
        JsonNode node = JsonUtil.mapper().readTree(json);

        assertThat(node.get("html").asText()).isEqualTo("PGh0bWw+PC9odG1sPg==");
        assertThat(node.get("format").asText()).isEqualTo("letter");
    }

    @Test
    void templateToPdfOptions_serializesFlat() throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "John");
        TemplateToPdfOptions opts = TemplateToPdfOptions.builder()
                .templateId("tmpl-123")
                .data(data)
                .format(PdfFormat.A4)
                .build();
        String json = JsonUtil.mapper().writeValueAsString(opts);
        JsonNode node = JsonUtil.mapper().readTree(json);

        assertThat(node.get("templateId").asText()).isEqualTo("tmpl-123");
        assertThat(node.get("data").get("name").asText()).isEqualTo("John");
        assertThat(node.get("format").asText()).isEqualTo("a4");
    }

    @Test
    void urlToImageOptions_serializesFlat() throws Exception {
        UrlToImageOptions opts = UrlToImageOptions.builder()
                .url("https://example.com")
                .imageType(ImageType.PNG)
                .quality(90)
                .build();
        String json = JsonUtil.mapper().writeValueAsString(opts);
        JsonNode node = JsonUtil.mapper().readTree(json);

        assertThat(node.get("url").asText()).isEqualTo("https://example.com");
        assertThat(node.get("imageType").asText()).isEqualTo("png");
        assertThat(node.get("quality").asInt()).isEqualTo(90);
    }

    @Test
    void mergePdfsOptions_serializesFlat() throws Exception {
        MergePdfsOptions opts = MergePdfsOptions.builder()
                .batch(Batch.of(Arrays.asList("https://a.com/1.pdf", "https://b.com/2.pdf")))
                .name("merged")
                .build();
        String json = JsonUtil.mapper().writeValueAsString(opts);
        JsonNode node = JsonUtil.mapper().readTree(json);

        assertThat(node.get("batch").get("urls").size()).isEqualTo(2);
        assertThat(node.get("name").asText()).isEqualTo("merged");
    }

    @Test
    void nullFieldsOmitted() throws Exception {
        UrlToPdfOptions opts = UrlToPdfOptions.builder()
                .url("https://example.com")
                .build();
        String json = JsonUtil.mapper().writeValueAsString(opts);
        JsonNode node = JsonUtil.mapper().readTree(json);

        // Only url should be present, all null fields omitted
        assertThat(node.get("url").asText()).isEqualTo("https://example.com");
        assertThat(node.has("format")).isFalse();
        assertThat(node.has("margin")).isFalse();
        assertThat(node.has("async")).isFalse();
    }

    // --- Enum serialization ---

    @Test
    void enumsSerialization() throws Exception {
        assertThat(JsonUtil.mapper().writeValueAsString(PdfFormat.A4)).isEqualTo("\"a4\"");
        assertThat(JsonUtil.mapper().writeValueAsString(ImageType.PNG)).isEqualTo("\"png\"");
        assertThat(JsonUtil.mapper().writeValueAsString(WaitUntilOption.NETWORK_IDLE_2)).isEqualTo("\"networkidle2\"");
    }

    @Test
    void enumsDeserialization() throws Exception {
        assertThat(JsonUtil.mapper().readValue("\"a4\"", PdfFormat.class)).isEqualTo(PdfFormat.A4);
        assertThat(JsonUtil.mapper().readValue("\"A4\"", PdfFormat.class)).isEqualTo(PdfFormat.A4);
        assertThat(JsonUtil.mapper().readValue("\"png\"", ImageType.class)).isEqualTo(ImageType.PNG);
        assertThat(JsonUtil.mapper().readValue("\"NETWORKIDLE2\"", WaitUntilOption.class)).isEqualTo(WaitUntilOption.NETWORK_IDLE_2);
    }

    // --- Viewport JSON name ---

    @Test
    void viewport_usesCapitalP_inJson() throws Exception {
        PuppeteerOptions opts = PuppeteerOptions.builder()
                .viewPort(Viewport.builder().width(1920).height(1080).build())
                .build();
        String json = JsonUtil.mapper().writeValueAsString(opts);
        assertThat(json).contains("\"viewPort\"");
        assertThat(json).doesNotContain("\"viewport\"");
    }

    // --- HtmlUtil ---

    @Test
    void htmlUtil_encodeHtml() {
        String encoded = io.cloudlayer.sdk.util.HtmlUtil.encodeHtml("<html></html>");
        assertThat(encoded).isEqualTo("PGh0bWw+PC9odG1sPg==");
    }

    @Test
    void nullableString_absentField_notInJson() throws Exception {
        // PuppeteerOptions with no emulateMediaType set — field should be absent from JSON
        PuppeteerOptions opts = PuppeteerOptions.builder()
                .autoScroll(true)
                .build();
        String json = JsonUtil.mapper().writeValueAsString(opts);
        JsonNode node = JsonUtil.mapper().readTree(json);

        assertThat(node.has("autoScroll")).isTrue();
        assertThat(node.has("emulateMediaType")).isFalse();
    }

    @Test
    void margin_mixedLayoutDimensionTypes() throws Exception {
        Margin margin = Margin.builder()
                .top("10mm")
                .bottom(20)
                .left("1.5cm")
                .right(50)
                .build();
        String json = JsonUtil.mapper().writeValueAsString(margin);
        JsonNode node = JsonUtil.mapper().readTree(json);

        assertThat(node.get("top").asText()).isEqualTo("10mm");
        assertThat(node.get("bottom").asInt()).isEqualTo(20);
        assertThat(node.get("left").asText()).isEqualTo("1.5cm");
        assertThat(node.get("right").asInt()).isEqualTo(50);
    }

    @Test
    void htmlUtil_encodeHtml_unicode() {
        String html = "<h1>H\u00e9llo W\u00f6rld</h1>";
        String encoded = io.cloudlayer.sdk.util.HtmlUtil.encodeHtml(html);
        // Decode and verify round-trip
        byte[] decoded = java.util.Base64.getDecoder().decode(encoded);
        String roundTripped = new String(decoded, java.nio.charset.StandardCharsets.UTF_8);
        assertThat(roundTripped).isEqualTo(html);
    }

    @Test
    void htmlUtil_encodeHtml_emptyString() {
        String encoded = io.cloudlayer.sdk.util.HtmlUtil.encodeHtml("");
        // Base64 of empty bytes is empty string
        assertThat(encoded).isEmpty();
    }
}
