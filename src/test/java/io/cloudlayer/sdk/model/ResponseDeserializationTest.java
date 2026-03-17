package io.cloudlayer.sdk.model;

import com.fasterxml.jackson.core.type.TypeReference;
import io.cloudlayer.sdk.model.constants.JobStatus;
import io.cloudlayer.sdk.model.constants.JobType;
import io.cloudlayer.sdk.model.response.*;
import io.cloudlayer.sdk.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseDeserializationTest {

    private <T> T readFixture(String name, Class<T> type) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/fixtures/" + name)) {
            assertThat(is).as("fixture %s not found", name).isNotNull();
            return JsonUtil.mapper().readValue(is, type);
        }
    }

    private <T> T readFixture(String name, TypeReference<T> type) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/fixtures/" + name)) {
            assertThat(is).as("fixture %s not found", name).isNotNull();
            return JsonUtil.mapper().readValue(is, type);
        }
    }

    @Test
    void job_success() throws Exception {
        Job job = readFixture("job-success.json", Job.class);
        assertThat(job.getId()).isEqualTo("job-123");
        assertThat(job.getUid()).isEqualTo("user-456");
        assertThat(job.getType()).isEqualTo(JobType.URL_PDF);
        assertThat(job.getStatus()).isEqualTo(JobStatus.SUCCESS);
        assertThat(job.getProcessTime()).isEqualTo(2500);
        assertThat(job.getTotalCost()).isEqualTo(0.017);
        assertThat(job.getAssetUrl()).isEqualTo("https://storage.cloudlayer.io/assets/abc.pdf");
        assertThat(job.getAssetId()).isEqualTo("asset-789");
        assertThat(job.getError()).isNull();
        // Firestore timestamp format
        assertThat(job.getTimestampMillis()).isEqualTo(1710000000500L);
    }

    @Test
    void job_pending() throws Exception {
        Job job = readFixture("job-pending.json", Job.class);
        assertThat(job.getStatus()).isEqualTo(JobStatus.PENDING);
        assertThat(job.getType()).isEqualTo(JobType.HTML_PDF);
        // Numeric timestamp
        assertThat(job.getTimestampMillis()).isEqualTo(1710000000000L);
    }

    @Test
    void job_error() throws Exception {
        Job job = readFixture("job-error.json", Job.class);
        assertThat(job.getStatus()).isEqualTo(JobStatus.ERROR);
        assertThat(job.getError()).isEqualTo("Navigation timeout of 30000 ms exceeded");
    }

    @Test
    void asset() throws Exception {
        Asset asset = readFixture("asset.json", Asset.class);
        assertThat(asset.getUid()).isEqualTo("user-456");
        assertThat(asset.getId()).isEqualTo("asset-789");
        assertThat(asset.getExt()).isEqualTo("pdf");
        assertThat(asset.getSize()).isEqualTo(125000);
        assertThat(asset.getTimestampMillis()).isEqualTo(1710000000000L);
    }

    @Test
    void storageList() throws Exception {
        List<StorageListItem> items = readFixture("storage-list.json", new TypeReference<List<StorageListItem>>() {});
        assertThat(items).hasSize(2);
        assertThat(items.get(0).getId()).isEqualTo("storage-1");
        assertThat(items.get(0).getTitle()).isEqualTo("My S3 Bucket");
    }

    @Test
    void storageDetail() throws Exception {
        StorageDetail detail = readFixture("storage-detail.json", StorageDetail.class);
        assertThat(detail.getId()).isEqualTo("storage-1");
        assertThat(detail.getTitle()).isEqualTo("My S3 Bucket");
    }

    @Test
    void storageNotAllowed() throws Exception {
        StorageNotAllowedResponse resp = readFixture("storage-not-allowed.json", StorageNotAllowedResponse.class);
        assertThat(resp.getAllowed()).isFalse();
        assertThat(resp.getReason()).contains("does not support");
        assertThat(resp.getStatusCode()).isEqualTo(403);
    }

    @Test
    void accountInfo_withDynamicFields() throws Exception {
        AccountInfo info = readFixture("account.json", AccountInfo.class);
        assertThat(info.getEmail()).isEqualTo("test@example.com");
        assertThat(info.getCalls()).isEqualTo(42);
        assertThat(info.getCallsLimit()).isEqualTo(1000);
        assertThat(info.getSubType()).isEqualTo("limit");
        assertThat(info.getCredit()).isEqualTo(10.50);
        assertThat(info.getSubActive()).isTrue();
        // Dynamic fields captured
        assertThat(info.getExtras()).containsEntry("url-pdf", 15);
        assertThat(info.getExtras()).containsEntry("html-pdf", 20);
        assertThat(info.getExtras()).containsEntry("template-image", 7);
    }

    @Test
    void statusResponse_preservesTrailingSpace() throws Exception {
        StatusResponse resp = readFixture("status.json", StatusResponse.class);
        assertThat(resp.getStatus()).isEqualTo("ok ");
    }

    @Test
    void publicTemplate() throws Exception {
        PublicTemplate tmpl = readFixture("template.json", PublicTemplate.class);
        assertThat(tmpl.getId()).isEqualTo("tmpl-doc-001");
        assertThat(tmpl.getTemplateId()).isEqualTo("invoice-standard");
        assertThat(tmpl.getTitle()).isEqualTo("Standard Invoice");
        assertThat(tmpl.getTags()).containsExactly("business", "finance");
        assertThat(tmpl.getSearchKeywords()).containsExactly("invoice", "billing", "receipt");
        assertThat(tmpl.getCategory()).isEqualTo("business");
        assertThat(tmpl.getType()).isEqualTo("pdf");
        assertThat(tmpl.getSampleData()).containsEntry("companyName", "Acme Inc");
        assertThat(tmpl.getTimestampMillis()).isEqualTo(1710000000000L);
    }

    @Test
    void unknownFieldsIgnored() throws Exception {
        String json = "{\"id\":\"job-1\",\"status\":\"success\",\"unknownField\":\"value\"}";
        Job job = JsonUtil.mapper().readValue(json, Job.class);
        assertThat(job.getId()).isEqualTo("job-1");
        assertThat(job.getStatus()).isEqualTo(JobStatus.SUCCESS);
    }
}
