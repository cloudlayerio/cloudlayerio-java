package io.cloudlayer.sdk;

import io.cloudlayer.sdk.exception.ConfigException;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CloudLayerTest {

    @Test
    void builder_validConfig() {
        CloudLayer client = CloudLayer.builder("test-key", ApiVersion.V2).build();
        assertThat(client.getConfig().getApiKey()).isEqualTo("test-key");
        assertThat(client.getConfig().getApiVersion()).isEqualTo(ApiVersion.V2);
        assertThat(client.getConfig().getBaseUrl()).isEqualTo("https://api.cloudlayer.io");
        assertThat(client.getConfig().getTimeout()).isEqualTo(Duration.ofSeconds(30));
        assertThat(client.getConfig().getMaxRetries()).isEqualTo(2);
        assertThat(client.getConfig().getUserAgent()).isEqualTo("cloudlayerio-java/" + CloudLayer.VERSION);
    }

    @Test
    void builder_nullApiKey_throws() {
        assertThatThrownBy(() -> CloudLayer.builder(null, ApiVersion.V2).build())
                .isInstanceOf(ConfigException.class)
                .hasMessageContaining("apiKey");
    }

    @Test
    void builder_emptyApiKey_throws() {
        assertThatThrownBy(() -> CloudLayer.builder("  ", ApiVersion.V2).build())
                .isInstanceOf(ConfigException.class)
                .hasMessageContaining("apiKey");
    }

    @Test
    void builder_nullApiVersion_throws() {
        assertThatThrownBy(() -> CloudLayer.builder("key", null).build())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void builder_invalidBaseUrl_throws() {
        assertThatThrownBy(() -> CloudLayer.builder("key", ApiVersion.V2)
                .baseUrl("not-a-url").build())
                .isInstanceOf(ConfigException.class)
                .hasMessageContaining("baseUrl");
    }

    @Test
    void builder_zeroTimeout_throws() {
        assertThatThrownBy(() -> CloudLayer.builder("key", ApiVersion.V2)
                .timeout(Duration.ZERO).build())
                .isInstanceOf(ConfigException.class)
                .hasMessageContaining("timeout");
    }

    @Test
    void builder_maxRetries_clamped() {
        CloudLayer client = CloudLayer.builder("key", ApiVersion.V2)
                .maxRetries(100).build();
        assertThat(client.getConfig().getMaxRetries()).isEqualTo(5);

        client = CloudLayer.builder("key", ApiVersion.V2)
                .maxRetries(-5).build();
        assertThat(client.getConfig().getMaxRetries()).isEqualTo(0);
    }

    @Test
    void builder_customConfig() {
        CloudLayer client = CloudLayer.builder("key", ApiVersion.V1)
                .baseUrl("https://custom.api.com")
                .timeout(Duration.ofSeconds(60))
                .maxRetries(3)
                .userAgent("custom-agent/1.0")
                .build();

        assertThat(client.getConfig().getBaseUrl()).isEqualTo("https://custom.api.com");
        assertThat(client.getConfig().getTimeout()).isEqualTo(Duration.ofSeconds(60));
        assertThat(client.getConfig().getMaxRetries()).isEqualTo(3);
        assertThat(client.getConfig().getUserAgent()).isEqualTo("custom-agent/1.0");
    }

    @Test
    void builder_trailingSlashStripped() {
        CloudLayer client = CloudLayer.builder("key", ApiVersion.V2)
                .baseUrl("https://api.example.com/").build();
        assertThat(client.getConfig().getBaseUrl()).isEqualTo("https://api.example.com");
    }

    @Test
    void version_constant() {
        assertThat(CloudLayer.VERSION).isEqualTo("0.1.0");
    }

    @Test
    void builder_reusability_twoClients() {
        // Build two clients from equivalent builder params — both should work independently
        CloudLayer client1 = CloudLayer.builder("key-1", ApiVersion.V2)
                .baseUrl("https://api.cloudlayer.io")
                .timeout(Duration.ofSeconds(15))
                .build();
        CloudLayer client2 = CloudLayer.builder("key-2", ApiVersion.V1)
                .baseUrl("https://api.cloudlayer.io")
                .timeout(Duration.ofSeconds(30))
                .build();

        assertThat(client1.getConfig().getApiKey()).isEqualTo("key-1");
        assertThat(client1.getConfig().getApiVersion()).isEqualTo(ApiVersion.V2);
        assertThat(client1.getConfig().getTimeout()).isEqualTo(Duration.ofSeconds(15));

        assertThat(client2.getConfig().getApiKey()).isEqualTo("key-2");
        assertThat(client2.getConfig().getApiVersion()).isEqualTo(ApiVersion.V1);
        assertThat(client2.getConfig().getTimeout()).isEqualTo(Duration.ofSeconds(30));

        // Both have valid transports
        assertThat(client1.getTransport()).isNotNull();
        assertThat(client2.getTransport()).isNotNull();
    }

    @Test
    void builder_customHttpClient() {
        HttpClient customClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();

        CloudLayer client = CloudLayer.builder("key", ApiVersion.V2)
                .httpClient(customClient)
                .build();

        assertThat(client.getConfig().getHttpClient()).isSameAs(customClient);
    }
}
