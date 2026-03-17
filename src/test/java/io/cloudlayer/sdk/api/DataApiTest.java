package io.cloudlayer.sdk.api;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.cloudlayer.sdk.ApiVersion;
import io.cloudlayer.sdk.CloudLayer;
import io.cloudlayer.sdk.exception.ApiException;
import io.cloudlayer.sdk.exception.TimeoutException;
import io.cloudlayer.sdk.exception.ValidationException;
import io.cloudlayer.sdk.model.options.ListTemplatesOptions;
import io.cloudlayer.sdk.model.options.StorageParams;
import io.cloudlayer.sdk.model.options.WaitForJobOptions;
import io.cloudlayer.sdk.model.response.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

@WireMockTest
class DataApiTest {

    private CloudLayer createClient(WireMockRuntimeInfo wmInfo) {
        return CloudLayer.builder("test-key", ApiVersion.V2)
                .baseUrl(wmInfo.getHttpBaseUrl())
                .maxRetries(0)
                .build();
    }

    // --- Jobs ---

    @Test
    void listJobs(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/jobs"))
                .willReturn(okJson("[{\"id\":\"job-1\",\"status\":\"success\",\"type\":\"url-pdf\"}]")));

        List<Job> jobs = createClient(wmInfo).listJobs();
        assertThat(jobs).hasSize(1);
        assertThat(jobs.get(0).getId()).isEqualTo("job-1");
    }

    @Test
    void getJob(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/jobs/job-123"))
                .willReturn(okJson("{\"id\":\"job-123\",\"status\":\"success\",\"type\":\"url-pdf\"}")));

        Job job = createClient(wmInfo).getJob("job-123");
        assertThat(job.getId()).isEqualTo("job-123");
    }

    @Test
    void getJob_emptyId_throws() {
        CloudLayer client = CloudLayer.builder("key", ApiVersion.V2).build();
        assertThatThrownBy(() -> client.getJob(""))
                .isInstanceOf(ValidationException.class);
    }

    // --- Assets ---

    @Test
    void listAssets(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/assets"))
                .willReturn(okJson("[{\"uid\":\"u1\",\"id\":\"a1\"}]")));

        List<Asset> assets = createClient(wmInfo).listAssets();
        assertThat(assets).hasSize(1);
    }

    @Test
    void getAsset(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/assets/asset-1"))
                .willReturn(okJson("{\"uid\":\"u1\",\"id\":\"asset-1\",\"ext\":\"pdf\"}")));

        Asset asset = createClient(wmInfo).getAsset("asset-1");
        assertThat(asset.getId()).isEqualTo("asset-1");
    }

    // --- Storage ---

    @Test
    void listStorage(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/storage"))
                .willReturn(okJson("[{\"id\":\"s1\",\"title\":\"My Bucket\"}]")));

        List<StorageListItem> items = createClient(wmInfo).listStorage();
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getTitle()).isEqualTo("My Bucket");
    }

    @Test
    void addStorage_success(WireMockRuntimeInfo wmInfo) {
        stubFor(post(urlPathEqualTo("/v2/storage"))
                .willReturn(okJson("{\"id\":\"new-s1\",\"title\":\"Test\"}")));

        StorageCreateResponse resp = createClient(wmInfo).addStorage(
                StorageParams.builder("Test", "us-east-1", "AKIA...", "secret", "my-bucket").build());
        assertThat(resp.getId()).isEqualTo("new-s1");
    }

    @Test
    void addStorage_notAllowed_throws(WireMockRuntimeInfo wmInfo) {
        stubFor(post(urlPathEqualTo("/v2/storage"))
                .willReturn(okJson("{\"allowed\":false,\"reason\":\"Plan does not support storage\",\"statusCode\":403}")));

        assertThatThrownBy(() -> createClient(wmInfo).addStorage(
                StorageParams.builder("Test", "us-east-1", "AKIA...", "secret", "my-bucket").build()))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Plan does not support storage");
    }

    @Test
    void deleteStorage_success(WireMockRuntimeInfo wmInfo) {
        stubFor(delete(urlPathEqualTo("/v2/storage/s1"))
                .willReturn(okJson("{\"status\":\"success\"}")));

        assertThatCode(() -> createClient(wmInfo).deleteStorage("s1"))
                .doesNotThrowAnyException();
    }

    @Test
    void deleteStorage_notFound_throws(WireMockRuntimeInfo wmInfo) {
        stubFor(delete(urlPathEqualTo("/v2/storage/s1"))
                .willReturn(okJson("{\"status\":\"error\",\"message\":\"No storage document found.\"}")));

        assertThatThrownBy(() -> createClient(wmInfo).deleteStorage("s1"))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("No storage document found");
    }

    // --- Account & Status ---

    @Test
    void getAccount(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/account"))
                .willReturn(okJson("{\"email\":\"test@example.com\",\"calls\":42}")));

        AccountInfo info = createClient(wmInfo).getAccount();
        assertThat(info.getEmail()).isEqualTo("test@example.com");
        assertThat(info.getCalls()).isEqualTo(42);
    }

    @Test
    void getStatus(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/getStatus"))
                .willReturn(okJson("{\"status\":\"ok \"}")));

        StatusResponse status = createClient(wmInfo).getStatus();
        assertThat(status.getStatus()).isEqualTo("ok ");
    }

    // --- Templates ---

    @Test
    void listTemplates_usesV2Path(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/templates"))
                .willReturn(okJson("[]")));

        // Client is V2 but templates always use /v2/
        CloudLayer client = CloudLayer.builder("key", ApiVersion.V1)
                .baseUrl(wmInfo.getHttpBaseUrl()).maxRetries(0).build();
        List<PublicTemplate> templates = client.listTemplates();
        assertThat(templates).isEmpty();

        verify(getRequestedFor(urlPathEqualTo("/v2/templates")));
    }

    @Test
    void listTemplates_withOptions(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/templates"))
                .willReturn(okJson("[]")));

        createClient(wmInfo).listTemplates(ListTemplatesOptions.builder()
                .type("pdf").tags("business,finance").expand(true).build());

        verify(getRequestedFor(urlPathEqualTo("/v2/templates"))
                .withQueryParam("type", equalTo("pdf"))
                .withQueryParam("tags", equalTo("business,finance"))
                .withQueryParam("expand", equalTo("true")));
    }

    @Test
    void getTemplate_usesSingularPath(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/template/tmpl-1"))
                .willReturn(okJson("{\"id\":\"tmpl-1\",\"title\":\"Invoice\"}")));

        PublicTemplate tmpl = createClient(wmInfo).getTemplate("tmpl-1");
        assertThat(tmpl.getTitle()).isEqualTo("Invoice");

        verify(getRequestedFor(urlPathEqualTo("/v2/template/tmpl-1")));
    }

    // --- waitForJob ---

    @Test
    void waitForJob_returnsOnSuccess(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/jobs/job-w1"))
                .willReturn(okJson("{\"id\":\"job-w1\",\"status\":\"success\",\"type\":\"url-pdf\"}")));

        Job job = createClient(wmInfo).waitForJob("job-w1",
                WaitForJobOptions.builder().interval(Duration.ofSeconds(2)).maxWait(Duration.ofSeconds(10)).build());
        assertThat(job.getStatus().getValue()).isEqualTo("success");
    }

    @Test
    void waitForJob_throwsOnError(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/jobs/job-err"))
                .willReturn(okJson("{\"id\":\"job-err\",\"status\":\"error\",\"error\":\"Navigation timeout\"}")));

        assertThatThrownBy(() -> createClient(wmInfo).waitForJob("job-err",
                WaitForJobOptions.builder().interval(Duration.ofSeconds(2)).maxWait(Duration.ofSeconds(10)).build()))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Navigation timeout");
    }

    @Test
    void waitForJob_timesOut(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/jobs/job-slow"))
                .willReturn(okJson("{\"id\":\"job-slow\",\"status\":\"pending\"}")));

        assertThatThrownBy(() -> createClient(wmInfo).waitForJob("job-slow",
                WaitForJobOptions.builder().interval(Duration.ofSeconds(2)).maxWait(Duration.ofSeconds(3)).build()))
                .isInstanceOf(TimeoutException.class);
    }

    // --- downloadJobResult ---

    @Test
    void downloadJobResult_fetchesBinary(WireMockRuntimeInfo wmInfo) {
        byte[] pdfBytes = "%PDF-test".getBytes();
        stubFor(get(urlPathEqualTo("/assets/result.pdf"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/pdf")
                        .withBody(pdfBytes)));

        CloudLayer client = createClient(wmInfo);

        // Create a fake Job with assetUrl pointing to WireMock
        String assetUrl = wmInfo.getHttpBaseUrl() + "/assets/result.pdf";
        String jobJson = "{\"id\":\"j1\",\"status\":\"success\",\"assetUrl\":\"" + assetUrl + "\"}";
        try {
            Job job = io.cloudlayer.sdk.util.JsonUtil.mapper().readValue(jobJson, Job.class);
            byte[] result = client.downloadJobResult(job);
            assertThat(result).isEqualTo(pdfBytes);

            // Should NOT send X-API-Key header (presigned URL)
            verify(getRequestedFor(urlPathEqualTo("/assets/result.pdf"))
                    .withoutHeader("X-API-Key"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void downloadJobResult_nullAssetUrl_throws() {
        CloudLayer client = CloudLayer.builder("key", ApiVersion.V2).build();
        try {
            Job job = io.cloudlayer.sdk.util.JsonUtil.mapper().readValue(
                    "{\"id\":\"j1\",\"status\":\"success\"}", Job.class);
            assertThatThrownBy(() -> client.downloadJobResult(job))
                    .isInstanceOf(ValidationException.class)
                    .hasMessageContaining("assetUrl");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
