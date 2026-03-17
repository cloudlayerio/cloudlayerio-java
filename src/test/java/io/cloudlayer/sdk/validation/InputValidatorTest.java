package io.cloudlayer.sdk.validation;

import io.cloudlayer.sdk.exception.ValidationException;
import io.cloudlayer.sdk.model.endpoint.*;
import io.cloudlayer.sdk.model.options.Batch;
import io.cloudlayer.sdk.util.FileInput;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

class InputValidatorTest {

    @Test
    void urlToPdf_noUrlOrBatch_throws() {
        assertThatThrownBy(() -> InputValidator.validateUrlToPdf(
                UrlToPdfOptions.builder().build()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("url or batch");
    }

    @Test
    void urlToPdf_bothUrlAndBatch_throws() {
        assertThatThrownBy(() -> InputValidator.validateUrlToPdf(
                UrlToPdfOptions.builder()
                        .url("https://example.com")
                        .batch(Batch.of(List.of("https://a.com")))
                        .build()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("mutually exclusive");
    }

    @Test
    void urlToPdf_batchOver20_throws() {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < 21; i++) urls.add("https://example.com/" + i);
        assertThatThrownBy(() -> InputValidator.validateUrlToPdf(
                UrlToPdfOptions.builder().batch(Batch.of(urls)).build()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("20");
    }

    @Test
    void urlToPdf_validUrl_passes() {
        assertThatCode(() -> InputValidator.validateUrlToPdf(
                UrlToPdfOptions.builder().url("https://example.com").build()))
                .doesNotThrowAnyException();
    }

    @Test
    void htmlToPdf_emptyHtml_throws() {
        assertThatThrownBy(() -> InputValidator.validateHtmlToPdf(
                HtmlToPdfOptions.builder().build()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("html");
    }

    @Test
    void htmlToPdf_validHtml_passes() {
        assertThatCode(() -> InputValidator.validateHtmlToPdf(
                HtmlToPdfOptions.builder().html("PGh0bWw+").build()))
                .doesNotThrowAnyException();
    }

    @Test
    void templateToPdf_neitherIdNorTemplate_throws() {
        assertThatThrownBy(() -> InputValidator.validateTemplateToPdf(
                TemplateToPdfOptions.builder().build()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("templateId or template");
    }

    @Test
    void templateToPdf_bothIdAndTemplate_throws() {
        assertThatThrownBy(() -> InputValidator.validateTemplateToPdf(
                TemplateToPdfOptions.builder()
                        .templateId("tmpl-1")
                        .template("base64")
                        .build()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("mutually exclusive");
    }

    @Test
    void templateToPdf_validTemplateId_passes() {
        assertThatCode(() -> InputValidator.validateTemplateToPdf(
                TemplateToPdfOptions.builder().templateId("tmpl-1").build()))
                .doesNotThrowAnyException();
    }

    @Test
    void docxToPdf_nullFile_throws() {
        assertThatThrownBy(() -> InputValidator.validateDocxToPdf(
                DocxToPdfOptions.builder().build()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("file");
    }

    @Test
    void docxToPdf_validFile_passes() {
        assertThatCode(() -> InputValidator.validateDocxToPdf(
                DocxToPdfOptions.builder()
                        .file(FileInput.fromBytes("data".getBytes(), "test.docx"))
                        .build()))
                .doesNotThrowAnyException();
    }

    @Test
    void webhook_httpNotAllowed() {
        assertThatThrownBy(() -> InputValidator.validateUrlToPdf(
                UrlToPdfOptions.builder()
                        .url("https://example.com")
                        .webhook("http://insecure.com/hook")
                        .build()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("HTTPS");
    }

    @Test
    void webhook_httpsAllowed() {
        assertThatCode(() -> InputValidator.validateUrlToPdf(
                UrlToPdfOptions.builder()
                        .url("https://example.com")
                        .webhook("https://secure.com/hook")
                        .build()))
                .doesNotThrowAnyException();
    }

    @Test
    void quality_outOfRange_throws() {
        assertThatThrownBy(() -> InputValidator.validateUrlToImage(
                UrlToImageOptions.builder()
                        .url("https://example.com")
                        .quality(150)
                        .build()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("quality");
    }

    @Test
    void timeout_tooLow_throws() {
        assertThatThrownBy(() -> InputValidator.validateUrlToPdf(
                UrlToPdfOptions.builder()
                        .url("https://example.com")
                        .timeout(500)
                        .build()))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("1000");
    }

    @Test
    void mergePdfs_validBatch_passes() {
        assertThatCode(() -> InputValidator.validateMergePdfs(
                MergePdfsOptions.builder()
                        .batch(Batch.of(List.of("https://a.com/1.pdf", "https://b.com/2.pdf")))
                        .build()))
                .doesNotThrowAnyException();
    }
}
