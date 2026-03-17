package io.cloudlayer.sdk.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionHierarchyTest {

    @Test
    void cloudLayerException_isRuntimeException() {
        CloudLayerException ex = new CloudLayerException("test");
        assertThat(ex).isInstanceOf(RuntimeException.class);
        assertThat(ex.getMessage()).isEqualTo("test");
    }

    @Test
    void configException_extendsCloudLayerException() {
        ConfigException ex = new ConfigException("bad config");
        assertThat(ex).isInstanceOf(CloudLayerException.class);
    }

    @Test
    void validationException_extendsCloudLayerException() {
        ValidationException ex = new ValidationException("invalid input");
        assertThat(ex).isInstanceOf(CloudLayerException.class);
    }

    @Test
    void networkException_wrapsCause() {
        Exception cause = new java.io.IOException("connection refused");
        NetworkException ex = new NetworkException("network error", cause);
        assertThat(ex).isInstanceOf(CloudLayerException.class);
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    void timeoutException_extendsCloudLayerException() {
        TimeoutException ex = new TimeoutException("timed out");
        assertThat(ex).isInstanceOf(CloudLayerException.class);
    }

    @Test
    void apiException_carriesContext() {
        ApiException ex = new ApiException(500, "Internal Server Error", "/v2/url/pdf", "POST", "{\"error\":\"fail\"}");
        assertThat(ex).isInstanceOf(CloudLayerException.class);
        assertThat(ex.getStatusCode()).isEqualTo(500);
        assertThat(ex.getStatusText()).isEqualTo("Internal Server Error");
        assertThat(ex.getPath()).isEqualTo("/v2/url/pdf");
        assertThat(ex.getMethod()).isEqualTo("POST");
        assertThat(ex.getResponseBody()).isEqualTo("{\"error\":\"fail\"}");
        assertThat(ex.getMessage()).contains("POST").contains("/v2/url/pdf").contains("500");
    }

    @Test
    void authException_extendsApiException() {
        AuthException ex = new AuthException(401, "Unauthorized", "/v2/url/pdf", "POST", "");
        assertThat(ex).isInstanceOf(ApiException.class);
        assertThat(ex.getStatusCode()).isEqualTo(401);
    }

    @Test
    void rateLimitException_includesRetryAfter() {
        RateLimitException ex = new RateLimitException("/v2/url/pdf", "POST", "", 30);
        assertThat(ex).isInstanceOf(ApiException.class);
        assertThat(ex.getStatusCode()).isEqualTo(429);
        assertThat(ex.getRetryAfterSeconds()).isEqualTo(30);
    }

    @Test
    void rateLimitException_retryAfterCanBeNull() {
        RateLimitException ex = new RateLimitException("/v2/url/pdf", "POST", "", null);
        assertThat(ex.getRetryAfterSeconds()).isNull();
    }
}
