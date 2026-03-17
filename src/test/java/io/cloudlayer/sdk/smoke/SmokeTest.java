package io.cloudlayer.sdk.smoke;

import io.cloudlayer.sdk.ApiVersion;
import io.cloudlayer.sdk.CloudLayer;
import io.cloudlayer.sdk.model.response.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("smoke")
@EnabledIfEnvironmentVariable(named = "CLOUDLAYER_API_KEY", matches = ".+")
class SmokeTest {

    private CloudLayer createV2Client() {
        return CloudLayer.builder(System.getenv("CLOUDLAYER_API_KEY"), ApiVersion.V2).build();
    }

    @Test
    void getStatus() {
        StatusResponse status = createV2Client().getStatus();
        assertThat(status.getStatus()).startsWith("ok");
    }

    @Test
    void getAccount() {
        AccountInfo info = createV2Client().getAccount();
        assertThat(info.getEmail()).isNotEmpty();
    }

    @Test
    void listJobs() {
        List<Job> jobs = createV2Client().listJobs();
        assertThat(jobs).isNotNull();
    }

    @Test
    void listAssets() {
        List<Asset> assets = createV2Client().listAssets();
        assertThat(assets).isNotNull();
    }

    @Test
    void listTemplates() {
        List<PublicTemplate> templates = createV2Client().listTemplates();
        assertThat(templates).isNotNull();
    }
}
