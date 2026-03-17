package io.cloudlayer.sdk.api;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.cloudlayer.sdk.ApiVersion;
import io.cloudlayer.sdk.CloudLayer;
import io.cloudlayer.sdk.model.constants.PdfFormat;
import io.cloudlayer.sdk.model.endpoint.*;
import io.cloudlayer.sdk.model.options.Batch;
import io.cloudlayer.sdk.model.response.ConversionResult;
import io.cloudlayer.sdk.util.FileInput;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
class ConversionApiTest {

    private CloudLayer createClient(WireMockRuntimeInfo wmInfo) {
        return CloudLayer.builder("test-key", ApiVersion.V2)
                .baseUrl(wmInfo.getHttpBaseUrl())
                .maxRetries(0)
                .build();
    }

    @Test
    void urlToPdf_v2_returnsJob(WireMockRuntimeInfo wmInfo) {
        stubFor(post(urlPathEqualTo("/v2/url/pdf"))
                .willReturn(aResponse().withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("cl-worker-job-id", "wjob-1")
                        .withBody("{\"id\":\"job-1\",\"status\":\"pending\",\"type\":\"url-pdf\"}")));

        CloudLayer client = createClient(wmInfo);
        ConversionResult result = client.urlToPdf(UrlToPdfOptions.builder()
                .url("https://example.com")
                .format(PdfFormat.A4)
                .build());

        assertThat(result.isJob()).isTrue();
        assertThat(result.getJob().getId()).isEqualTo("job-1");
        assertThat(result.getStatus()).isEqualTo(202);
        assertThat(result.getHeaders().getWorkerJobId()).isEqualTo("wjob-1");
    }

    @Test
    void urlToPdf_v1_returnsBinary(WireMockRuntimeInfo wmInfo) {
        byte[] pdfBytes = "%PDF-1.4 content".getBytes();
        stubFor(post(urlPathEqualTo("/v2/url/pdf"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/pdf")
                        .withHeader("Content-Disposition", "attachment; filename=\"result.pdf\"")
                        .withBody(pdfBytes)));

        CloudLayer client = createClient(wmInfo);
        ConversionResult result = client.urlToPdf(UrlToPdfOptions.builder()
                .url("https://example.com")
                .build());

        assertThat(result.isBinary()).isTrue();
        assertThat(result.getBytes()).isEqualTo(pdfBytes);
        assertThat(result.getFilename()).isEqualTo("result.pdf");
    }

    @Test
    void htmlToPdf_sendsHtmlInBody(WireMockRuntimeInfo wmInfo) {
        stubFor(post(urlPathEqualTo("/v2/html/pdf"))
                .willReturn(aResponse().withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"job-2\",\"status\":\"pending\",\"type\":\"html-pdf\"}")));

        CloudLayer client = createClient(wmInfo);
        ConversionResult result = client.htmlToPdf(HtmlToPdfOptions.builder()
                .html("PGh0bWw+PC9odG1sPg==")
                .build());

        assertThat(result.isJob()).isTrue();
        verify(postRequestedFor(urlPathEqualTo("/v2/html/pdf"))
                .withRequestBody(containing("\"html\":\"PGh0bWw+PC9odG1sPg==\"")));
    }

    @Test
    void templateToPdf_sendsTemplateId(WireMockRuntimeInfo wmInfo) {
        stubFor(post(urlPathEqualTo("/v2/template/pdf"))
                .willReturn(aResponse().withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"job-3\",\"status\":\"pending\",\"type\":\"template-pdf\"}")));

        CloudLayer client = createClient(wmInfo);
        ConversionResult result = client.templateToPdf(TemplateToPdfOptions.builder()
                .templateId("tmpl-123")
                .data(Map.of("name", "John"))
                .build());

        assertThat(result.isJob()).isTrue();
        verify(postRequestedFor(urlPathEqualTo("/v2/template/pdf"))
                .withRequestBody(containing("\"templateId\":\"tmpl-123\"")));
    }

    @Test
    void docxToPdf_sendsMultipart(WireMockRuntimeInfo wmInfo) {
        stubFor(post(urlPathEqualTo("/v2/docx/pdf"))
                .willReturn(aResponse().withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"job-4\",\"status\":\"pending\",\"type\":\"docx-pdf\"}")));

        CloudLayer client = createClient(wmInfo);
        ConversionResult result = client.docxToPdf(DocxToPdfOptions.builder()
                .file(FileInput.fromBytes("fake-docx-content".getBytes(), "test.docx",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .build());

        assertThat(result.isJob()).isTrue();
        verify(postRequestedFor(urlPathEqualTo("/v2/docx/pdf"))
                .withHeader("Content-Type", containing("multipart/form-data")));
    }

    @Test
    void mergePdfs_sendsBatchUrls(WireMockRuntimeInfo wmInfo) {
        stubFor(post(urlPathEqualTo("/v2/pdf/merge"))
                .willReturn(aResponse().withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"job-5\",\"status\":\"pending\",\"type\":\"merge-pdf\"}")));

        CloudLayer client = createClient(wmInfo);
        ConversionResult result = client.mergePdfs(MergePdfsOptions.builder()
                .batch(Batch.of(Arrays.asList("https://a.com/1.pdf", "https://b.com/2.pdf")))
                .build());

        assertThat(result.isJob()).isTrue();
        verify(postRequestedFor(urlPathEqualTo("/v2/pdf/merge"))
                .withRequestBody(containing("\"urls\"")));
    }

    @Test
    void urlToImage_works(WireMockRuntimeInfo wmInfo) {
        stubFor(post(urlPathEqualTo("/v2/url/image"))
                .willReturn(aResponse().withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"job-6\",\"status\":\"pending\",\"type\":\"url-image\"}")));

        CloudLayer client = createClient(wmInfo);
        ConversionResult result = client.urlToImage(UrlToImageOptions.builder()
                .url("https://example.com")
                .build());

        assertThat(result.isJob()).isTrue();
    }

    @Test
    void conversionRequests_notRetryable(WireMockRuntimeInfo wmInfo) {
        stubFor(post(urlPathEqualTo("/v2/url/pdf"))
                .willReturn(serverError().withBody("{\"message\":\"Internal Server Error\"}")));

        CloudLayer client = CloudLayer.builder("key", ApiVersion.V2)
                .baseUrl(wmInfo.getHttpBaseUrl())
                .maxRetries(3) // retries enabled but should NOT retry conversions
                .build();

        try {
            client.urlToPdf(UrlToPdfOptions.builder().url("https://example.com").build());
        } catch (Exception ignored) {}

        // Should only be called once (no retry)
        verify(1, postRequestedFor(urlPathEqualTo("/v2/url/pdf")));
    }
}
