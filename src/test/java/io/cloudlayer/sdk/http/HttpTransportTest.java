package io.cloudlayer.sdk.http;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.cloudlayer.sdk.ApiVersion;
import io.cloudlayer.sdk.CloudLayer;
import io.cloudlayer.sdk.exception.*;
import io.cloudlayer.sdk.model.response.Job;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WireMockTest
class HttpTransportTest {

    private CloudLayer createClient(WireMockRuntimeInfo wmInfo) {
        return CloudLayer.builder("test-api-key", ApiVersion.V2)
                .baseUrl(wmInfo.getHttpBaseUrl())
                .maxRetries(0)
                .build();
    }

    private CloudLayer createClientWithRetries(WireMockRuntimeInfo wmInfo, int retries) {
        return CloudLayer.builder("test-api-key", ApiVersion.V2)
                .baseUrl(wmInfo.getHttpBaseUrl())
                .maxRetries(retries)
                .build();
    }

    @Test
    void sendJsonRequest_success(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/jobs/job-123"))
                .willReturn(okJson("{\"id\":\"job-123\",\"status\":\"success\",\"type\":\"url-pdf\"}")));

        CloudLayer client = createClient(wmInfo);
        Job job = client.getTransport().sendJsonRequest("GET", "jobs/job-123", null, Job.class,
                RequestOptions.of(true));

        assertThat(job.getId()).isEqualTo("job-123");
        verify(getRequestedFor(urlPathEqualTo("/v2/jobs/job-123"))
                .withHeader("X-API-Key", equalTo("test-api-key"))
                .withHeader("User-Agent", containing("cloudlayerio-java/")));
    }

    @Test
    void sendJsonRequest_postWithBody(WireMockRuntimeInfo wmInfo) {
        stubFor(post(urlPathEqualTo("/v2/url/pdf"))
                .willReturn(okJson("{\"id\":\"job-new\",\"status\":\"pending\",\"type\":\"url-pdf\"}")));

        CloudLayer client = createClient(wmInfo);
        Job job = client.getTransport().sendJsonRequest("POST", "url/pdf",
                Map.of("url", "https://example.com"), Job.class, RequestOptions.of(false));

        assertThat(job.getId()).isEqualTo("job-new");
        verify(postRequestedFor(urlPathEqualTo("/v2/url/pdf"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(containing("\"url\":\"https://example.com\"")));
    }

    @Test
    void sendJsonRequest_202Accepted_treatedAsSuccess(WireMockRuntimeInfo wmInfo) {
        stubFor(post(urlPathEqualTo("/v2/url/pdf"))
                .willReturn(aResponse().withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":\"async-job\",\"status\":\"pending\",\"type\":\"url-pdf\"}")));

        CloudLayer client = createClient(wmInfo);
        Job job = client.getTransport().sendJsonRequest("POST", "url/pdf",
                Map.of("url", "https://example.com"), Job.class, RequestOptions.of(false));

        assertThat(job.getId()).isEqualTo("async-job");
        assertThat(job.getStatus().getValue()).isEqualTo("pending");
    }

    @Test
    void sendJsonRequest_401_throwsAuthException(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/account"))
                .willReturn(unauthorized().withBody("{\"message\":\"Unauthorized\"}")));

        CloudLayer client = createClient(wmInfo);
        assertThatThrownBy(() -> client.getTransport().sendJsonRequest("GET", "account", null,
                Object.class, RequestOptions.of(true)))
                .isInstanceOf(AuthException.class)
                .satisfies(e -> {
                    AuthException ae = (AuthException) e;
                    assertThat(ae.getStatusCode()).isEqualTo(401);
                });
    }

    @Test
    void sendJsonRequest_403_throwsAuthException(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/account"))
                .willReturn(forbidden().withBody("{\"message\":\"Forbidden\"}")));

        CloudLayer client = createClient(wmInfo);
        assertThatThrownBy(() -> client.getTransport().sendJsonRequest("GET", "account", null,
                Object.class, RequestOptions.of(true)))
                .isInstanceOf(AuthException.class)
                .satisfies(e -> assertThat(((ApiException) e).getStatusCode()).isEqualTo(403));
    }

    @Test
    void sendJsonRequest_429_throwsRateLimitException(WireMockRuntimeInfo wmInfo) {
        stubFor(post(urlPathEqualTo("/v2/url/pdf"))
                .willReturn(aResponse().withStatus(429)
                        .withBody("{\"message\":\"Too Many Requests\"}")));

        CloudLayer client = createClient(wmInfo);
        assertThatThrownBy(() -> client.getTransport().sendJsonRequest("POST", "url/pdf",
                Map.of("url", "https://example.com"), Object.class, RequestOptions.of(false)))
                .isInstanceOf(RateLimitException.class);
    }

    @Test
    void sendJsonRequest_500_throwsApiException(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/getStatus"))
                .willReturn(serverError().withBody("{\"message\":\"Internal Server Error\"}")));

        CloudLayer client = createClient(wmInfo);
        assertThatThrownBy(() -> client.getTransport().sendJsonRequest("GET", "getStatus", null,
                Object.class, RequestOptions.of(false)))
                .isInstanceOf(ApiException.class)
                .satisfies(e -> assertThat(((ApiException) e).getStatusCode()).isEqualTo(500));
    }

    @Test
    void sendRawRequest_returnsBinary(WireMockRuntimeInfo wmInfo) {
        byte[] pdfBytes = "%PDF-1.4 test content".getBytes();
        stubFor(post(urlPathEqualTo("/v2/url/pdf"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/pdf")
                        .withHeader("Content-Disposition", "attachment; filename=\"output.pdf\"")
                        .withHeader("cl-worker-job-id", "wjob-1")
                        .withHeader("cl-process-time", "1500")
                        .withBody(pdfBytes)));

        CloudLayer client = createClient(wmInfo);
        HttpTransport.RawResponse resp = client.getTransport().sendRawRequest("POST", "url/pdf",
                Map.of("url", "https://example.com"), RequestOptions.of(false));

        assertThat(resp.getBody()).isEqualTo(pdfBytes);
        assertThat(resp.getFilename()).isEqualTo("output.pdf");
        assertThat(resp.getHeaders().getWorkerJobId()).isEqualTo("wjob-1");
        assertThat(resp.getHeaders().getProcessTime()).isEqualTo(1500);
    }

    @Test
    void customHeaders_included(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/getStatus"))
                .willReturn(okJson("{\"status\":\"ok \"}")));

        CloudLayer client = CloudLayer.builder("key", ApiVersion.V2)
                .baseUrl(wmInfo.getHttpBaseUrl())
                .maxRetries(0)
                .headers(Map.of("X-Custom", "value123"))
                .build();

        client.getTransport().sendJsonRequest("GET", "getStatus", null, Object.class, RequestOptions.of(true));

        verify(getRequestedFor(urlPathEqualTo("/v2/getStatus"))
                .withHeader("X-Custom", equalTo("value123")));
    }

    @Test
    void queryParams_urlEncoded(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/templates"))
                .willReturn(okJson("[]")));

        CloudLayer client = createClient(wmInfo);
        client.getTransport().sendJsonRequest("GET", "v2/templates", null, Object.class,
                RequestOptions.of(true, true, Map.of("type", "pdf", "tags", "a,b")));

        verify(getRequestedFor(urlPathEqualTo("/v2/templates"))
                .withQueryParam("type", equalTo("pdf"))
                .withQueryParam("tags", equalTo("a,b")));
    }

    @Test
    void absolutePath_skipsApiVersion(WireMockRuntimeInfo wmInfo) {
        stubFor(get(urlPathEqualTo("/v2/templates"))
                .willReturn(okJson("[]")));

        CloudLayer client = createClient(wmInfo);
        client.getTransport().sendJsonRequest("GET", "v2/templates", null, Object.class,
                RequestOptions.of(true, true));

        verify(getRequestedFor(urlPathEqualTo("/v2/templates")));
    }

    @Test
    void buildUrl_normalPath() {
        CloudLayer client = CloudLayer.builder("key", ApiVersion.V2)
                .baseUrl("https://api.cloudlayer.io")
                .build();
        String url = client.getTransport().buildUrl("jobs/123", RequestOptions.of(true));
        assertThat(url).isEqualTo("https://api.cloudlayer.io/v2/jobs/123");
    }

    @Test
    void buildUrl_absolutePath() {
        CloudLayer client = CloudLayer.builder("key", ApiVersion.V1)
                .baseUrl("https://api.cloudlayer.io")
                .build();
        String url = client.getTransport().buildUrl("v2/templates", RequestOptions.of(true, true));
        assertThat(url).isEqualTo("https://api.cloudlayer.io/v2/templates");
    }

    @Test
    void sendJsonRequest_429_retriedSuccessfully(WireMockRuntimeInfo wmInfo) {
        // First request returns 429, second returns 200
        stubFor(get(urlPathEqualTo("/v2/getStatus"))
                .inScenario("retry-429")
                .whenScenarioStateIs(com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED)
                .willReturn(aResponse().withStatus(429)
                        .withBody("{\"message\":\"Too Many Requests\"}"))
                .willSetStateTo("after-429"));

        stubFor(get(urlPathEqualTo("/v2/getStatus"))
                .inScenario("retry-429")
                .whenScenarioStateIs("after-429")
                .willReturn(okJson("{\"status\":\"ok \"}")));

        CloudLayer client = createClientWithRetries(wmInfo, 1);
        Object result = client.getTransport().sendJsonRequest("GET", "getStatus", null,
                Object.class, RequestOptions.of(true));

        assertThat(result).isNotNull();
        verify(2, getRequestedFor(urlPathEqualTo("/v2/getStatus")));
    }

    @Test
    void sendJsonRequest_204_returnsNull(WireMockRuntimeInfo wmInfo) {
        stubFor(delete(urlPathEqualTo("/v2/storage/s1"))
                .willReturn(aResponse().withStatus(204)));

        CloudLayer client = createClient(wmInfo);
        Object result = client.getTransport().sendJsonRequest("DELETE", "storage/s1", null,
                Object.class, RequestOptions.of(true));

        assertThat(result).isNull();
    }

    @Test
    void sendRawRequest_noContentDisposition_nullFilename(WireMockRuntimeInfo wmInfo) {
        byte[] pdfBytes = "%PDF-1.4 test content".getBytes();
        stubFor(post(urlPathEqualTo("/v2/url/pdf"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/pdf")
                        .withBody(pdfBytes)));

        CloudLayer client = createClient(wmInfo);
        HttpTransport.RawResponse resp = client.getTransport().sendRawRequest("POST", "url/pdf",
                Map.of("url", "https://example.com"), RequestOptions.of(false));

        assertThat(resp.getBody()).isEqualTo(pdfBytes);
        assertThat(resp.getFilename()).isNull();
    }

    @Test
    void sendJsonRequest_noRetryWhenRetryableFalse_evenOn429(WireMockRuntimeInfo wmInfo) {
        stubFor(post(urlPathEqualTo("/v2/url/pdf"))
                .willReturn(aResponse().withStatus(429)
                        .withBody("{\"message\":\"Too Many Requests\"}")));

        CloudLayer client = createClientWithRetries(wmInfo, 3);
        assertThatThrownBy(() -> client.getTransport().sendJsonRequest("POST", "url/pdf",
                Map.of("url", "https://example.com"), Object.class, RequestOptions.of(false)))
                .isInstanceOf(RateLimitException.class);

        // retryable=false means no retry despite maxRetries=3
        verify(1, postRequestedFor(urlPathEqualTo("/v2/url/pdf")));
    }
}
