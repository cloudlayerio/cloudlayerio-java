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

/**
 * Exercises ALL getters on response model classes via fixture deserialization
 * to maximize JaCoCo coverage on response types.
 */
class ResponseGetterCoverageTest {

    private <T> T readFixture(String name, Class<T> type) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/fixtures/" + name)) {
            assertThat(is).as("fixture %s not found", name).isNotNull();
            return JsonUtil.mapper().readValue(is, type);
        }
    }

    // ===========================
    // Job — ALL getters
    // ===========================

    @Test
    void job_allGetters() throws Exception {
        Job job = readFixture("job-success.json", Job.class);

        assertThat(job.getId()).isEqualTo("job-123");
        assertThat(job.getUid()).isEqualTo("user-456");
        assertThat(job.getName()).isEqualTo("test-conversion");
        assertThat(job.getType()).isEqualTo(JobType.URL_PDF);
        assertThat(job.getStatus()).isEqualTo(JobStatus.SUCCESS);
        assertThat(job.getTimestamp()).isNotNull();
        assertThat(job.getWorkerName()).isEqualTo("worker-1");
        assertThat(job.getProcessTime()).isEqualTo(2500);
        assertThat(job.getApiKeyUsed()).isEqualTo("key-abc");
        assertThat(job.getProcessTimeCost()).isEqualTo(0.005);
        assertThat(job.getApiCreditCost()).isEqualTo(0.01);
        assertThat(job.getBandwidthCost()).isEqualTo(0.002);
        assertThat(job.getTotalCost()).isEqualTo(0.017);
        assertThat(job.getSize()).isEqualTo(125000);
        assertThat(job.getParams()).isNotNull();
        assertThat(job.getParams()).containsEntry("url", "https://example.com");
        assertThat(job.getParams()).containsEntry("format", "a4");
        assertThat(job.getAssetUrl()).isEqualTo("https://storage.cloudlayer.io/assets/abc.pdf");
        assertThat(job.getPreviewUrl()).isEqualTo("https://storage.cloudlayer.io/assets/abc-preview.png");
        assertThat(job.getSelf()).isEqualTo("/v2/jobs/job-123");
        assertThat(job.getAssetId()).isEqualTo("asset-789");
        assertThat(job.getProjectId()).isEqualTo("proj-001");
        assertThat(job.getError()).isNull();
        assertThat(job.getTimestampMillis()).isEqualTo(1710000000500L);
    }

    @Test
    void job_errorGetters() throws Exception {
        Job job = readFixture("job-error.json", Job.class);
        assertThat(job.getError()).isEqualTo("Navigation timeout of 30000 ms exceeded");
        assertThat(job.getStatus()).isEqualTo(JobStatus.ERROR);
    }

    @Test
    void job_pendingGetters() throws Exception {
        Job job = readFixture("job-pending.json", Job.class);
        assertThat(job.getStatus()).isEqualTo(JobStatus.PENDING);
        assertThat(job.getType()).isEqualTo(JobType.HTML_PDF);
        // Numeric timestamp
        assertThat(job.getTimestampMillis()).isEqualTo(1710000000000L);
    }

    @Test
    void job_timestampMillis_nullTimestamp() throws Exception {
        String json = "{\"id\":\"j1\",\"status\":\"success\"}";
        Job job = JsonUtil.mapper().readValue(json, Job.class);
        assertThat(job.getTimestampMillis()).isNull();
    }

    @Test
    void job_timestampMillis_invalidTimestampType() throws Exception {
        String json = "{\"id\":\"j1\",\"timestamp\":\"not-a-number\"}";
        Job job = JsonUtil.mapper().readValue(json, Job.class);
        assertThat(job.getTimestampMillis()).isNull();
    }

    @Test
    void job_timestampMillis_firestoreMapMissingSeconds() throws Exception {
        String json = "{\"id\":\"j1\",\"timestamp\":{\"other\":123}}";
        Job job = JsonUtil.mapper().readValue(json, Job.class);
        assertThat(job.getTimestampMillis()).isNull();
    }

    @Test
    void job_timestampMillis_firestoreMapWithoutNanos() throws Exception {
        String json = "{\"id\":\"j1\",\"timestamp\":{\"_seconds\":1710000000}}";
        Job job = JsonUtil.mapper().readValue(json, Job.class);
        assertThat(job.getTimestampMillis()).isEqualTo(1710000000000L);
    }

    // ===========================
    // Asset — ALL getters
    // ===========================

    @Test
    void asset_allGetters() throws Exception {
        Asset asset = readFixture("asset.json", Asset.class);

        assertThat(asset.getUid()).isEqualTo("user-456");
        assertThat(asset.getId()).isEqualTo("asset-789");
        assertThat(asset.getFileId()).isEqualTo("file-abc");
        assertThat(asset.getPreviewFileId()).isEqualTo("file-abc-preview");
        assertThat(asset.getType()).isEqualTo("application/pdf");
        assertThat(asset.getExt()).isEqualTo("pdf");
        assertThat(asset.getPreviewExt()).isEqualTo("png");
        assertThat(asset.getUrl()).isEqualTo("https://storage.cloudlayer.io/assets/abc.pdf");
        assertThat(asset.getPreviewUrl()).isEqualTo("https://storage.cloudlayer.io/assets/abc-preview.png");
        assertThat(asset.getSize()).isEqualTo(125000);
        assertThat(asset.getTimestamp()).isNotNull();
        assertThat(asset.getProjectId()).isEqualTo("proj-001");
        assertThat(asset.getJobId()).isEqualTo("job-123");
        assertThat(asset.getName()).isEqualTo("test-conversion");
        assertThat(asset.getTimestampMillis()).isEqualTo(1710000000000L);
    }

    @Test
    void asset_timestampMillis_nullTimestamp() throws Exception {
        String json = "{\"id\":\"a1\"}";
        Asset asset = JsonUtil.mapper().readValue(json, Asset.class);
        assertThat(asset.getTimestampMillis()).isNull();
    }

    @Test
    void asset_timestampMillis_numericTimestamp() throws Exception {
        String json = "{\"id\":\"a1\",\"timestamp\":1710000000}";
        Asset asset = JsonUtil.mapper().readValue(json, Asset.class);
        assertThat(asset.getTimestampMillis()).isEqualTo(1710000000000L);
    }

    @Test
    void asset_timestampMillis_invalidTimestamp() throws Exception {
        String json = "{\"id\":\"a1\",\"timestamp\":\"invalid\"}";
        Asset asset = JsonUtil.mapper().readValue(json, Asset.class);
        assertThat(asset.getTimestampMillis()).isNull();
    }

    @Test
    void asset_timestampMillis_firestoreMapMissingSeconds() throws Exception {
        String json = "{\"id\":\"a1\",\"timestamp\":{\"other\":1}}";
        Asset asset = JsonUtil.mapper().readValue(json, Asset.class);
        assertThat(asset.getTimestampMillis()).isNull();
    }

    @Test
    void asset_timestampMillis_firestoreMapWithoutNanos() throws Exception {
        String json = "{\"id\":\"a1\",\"timestamp\":{\"_seconds\":1710000000}}";
        Asset asset = JsonUtil.mapper().readValue(json, Asset.class);
        assertThat(asset.getTimestampMillis()).isEqualTo(1710000000000L);
    }

    // ===========================
    // PublicTemplate — ALL getters
    // ===========================

    @Test
    void publicTemplate_allGetters() throws Exception {
        PublicTemplate tmpl = readFixture("template.json", PublicTemplate.class);

        assertThat(tmpl.getId()).isEqualTo("tmpl-doc-001");
        assertThat(tmpl.getTemplateId()).isEqualTo("invoice-standard");
        assertThat(tmpl.getTitle()).isEqualTo("Standard Invoice");
        assertThat(tmpl.getShortDescription()).isEqualTo("A clean invoice template");
        assertThat(tmpl.getSearchKeywords()).containsExactly("invoice", "billing", "receipt");
        assertThat(tmpl.getTags()).containsExactly("business", "finance");
        assertThat(tmpl.getCategory()).isEqualTo("business");
        assertThat(tmpl.getType()).isEqualTo("pdf");
        assertThat(tmpl.getPreviewUrl()).isEqualTo("https://storage.cloudlayer.io/previews/invoice.png");
        assertThat(tmpl.getExampleAssetUrl()).isEqualTo("https://storage.cloudlayer.io/examples/invoice.pdf");
        assertThat(tmpl.getHighlights()).containsExactly("Professional layout", "Customizable fields");
        assertThat(tmpl.getTimestamp()).isNotNull();
        assertThat(tmpl.getProjectId()).isEqualTo("proj-001");
        assertThat(tmpl.getSampleData()).containsEntry("companyName", "Acme Inc");
        assertThat(tmpl.getExtras()).isEmpty(); // no extra fields in fixture
        assertThat(tmpl.getTimestampMillis()).isEqualTo(1710000000000L);
    }

    @Test
    void publicTemplate_extraFieldsCaptured() throws Exception {
        String json = "{\"id\":\"t1\",\"title\":\"Test\",\"customField\":\"value\",\"numericField\":42}";
        PublicTemplate tmpl = JsonUtil.mapper().readValue(json, PublicTemplate.class);

        assertThat(tmpl.getId()).isEqualTo("t1");
        assertThat(tmpl.getExtras()).containsEntry("customField", "value");
        assertThat(tmpl.getExtras()).containsEntry("numericField", 42);
    }

    @Test
    void publicTemplate_timestampMillis_nullTimestamp() throws Exception {
        String json = "{\"id\":\"t1\"}";
        PublicTemplate tmpl = JsonUtil.mapper().readValue(json, PublicTemplate.class);
        assertThat(tmpl.getTimestampMillis()).isNull();
    }

    @Test
    void publicTemplate_timestampMillis_numericTimestamp() throws Exception {
        String json = "{\"id\":\"t1\",\"timestamp\":1710000000}";
        PublicTemplate tmpl = JsonUtil.mapper().readValue(json, PublicTemplate.class);
        assertThat(tmpl.getTimestampMillis()).isEqualTo(1710000000000L);
    }

    @Test
    void publicTemplate_timestampMillis_invalidTimestamp() throws Exception {
        String json = "{\"id\":\"t1\",\"timestamp\":\"invalid\"}";
        PublicTemplate tmpl = JsonUtil.mapper().readValue(json, PublicTemplate.class);
        assertThat(tmpl.getTimestampMillis()).isNull();
    }

    @Test
    void publicTemplate_timestampMillis_firestoreMapMissingSeconds() throws Exception {
        String json = "{\"id\":\"t1\",\"timestamp\":{\"other\":1}}";
        PublicTemplate tmpl = JsonUtil.mapper().readValue(json, PublicTemplate.class);
        assertThat(tmpl.getTimestampMillis()).isNull();
    }

    @Test
    void publicTemplate_timestampMillis_firestoreMapWithoutNanos() throws Exception {
        String json = "{\"id\":\"t1\",\"timestamp\":{\"_seconds\":1710000000}}";
        PublicTemplate tmpl = JsonUtil.mapper().readValue(json, PublicTemplate.class);
        assertThat(tmpl.getTimestampMillis()).isEqualTo(1710000000000L);
    }

    @Test
    void publicTemplate_setExtra() throws Exception {
        String json = "{\"id\":\"t1\"}";
        PublicTemplate tmpl = JsonUtil.mapper().readValue(json, PublicTemplate.class);
        tmpl.setExtra("dynamic", "data");
        assertThat(tmpl.getExtras()).containsEntry("dynamic", "data");
    }

    // ===========================
    // AccountInfo — ALL getters
    // ===========================

    @Test
    void accountInfo_allGetters() throws Exception {
        AccountInfo info = readFixture("account.json", AccountInfo.class);

        assertThat(info.getEmail()).isEqualTo("test@example.com");
        assertThat(info.getCallsLimit()).isEqualTo(1000);
        assertThat(info.getCalls()).isEqualTo(42);
        assertThat(info.getStorageUsed()).isEqualTo(2);
        assertThat(info.getStorageLimit()).isEqualTo(5);
        assertThat(info.getSubscription()).isEqualTo("growth");
        assertThat(info.getBytesTotal()).isEqualTo(5000000);
        assertThat(info.getBytesLimit()).isEqualTo(100000000);
        assertThat(info.getComputeTimeTotal()).isEqualTo(30000);
        assertThat(info.getComputeTimeLimit()).isEqualTo(600000);
        assertThat(info.getSubType()).isEqualTo("limit");
        assertThat(info.getUid()).isEqualTo("user-456");
        assertThat(info.getCredit()).isEqualTo(10.50);
        assertThat(info.getSubActive()).isTrue();
        assertThat(info.getExtras()).containsEntry("url-pdf", 15);
        assertThat(info.getExtras()).containsEntry("html-pdf", 20);
        assertThat(info.getExtras()).containsEntry("template-image", 7);
    }

    @Test
    void accountInfo_setExtra() throws Exception {
        String json = "{\"email\":\"a@b.com\",\"uid\":\"u1\"}";
        AccountInfo info = JsonUtil.mapper().readValue(json, AccountInfo.class);
        info.setExtra("customCounter", 99);
        assertThat(info.getExtras()).containsEntry("customCounter", 99);
    }

    // ===========================
    // StorageCreateResponse — ALL getters
    // ===========================

    @Test
    void storageCreateResponse_allGetters() throws Exception {
        StorageCreateResponse resp = readFixture("storage-detail.json", StorageCreateResponse.class);
        assertThat(resp.getTitle()).isEqualTo("My S3 Bucket");
        assertThat(resp.getId()).isEqualTo("storage-1");
    }

    // ===========================
    // StorageDetail — ALL getters
    // ===========================

    @Test
    void storageDetail_allGetters() throws Exception {
        StorageDetail detail = readFixture("storage-detail.json", StorageDetail.class);
        assertThat(detail.getTitle()).isEqualTo("My S3 Bucket");
        assertThat(detail.getId()).isEqualTo("storage-1");
    }

    // ===========================
    // StorageListItem
    // ===========================

    @Test
    void storageListItem_allGetters() throws Exception {
        List<StorageListItem> items = readFixture("storage-list.json", new TypeReference<List<StorageListItem>>() {});
        assertThat(items).hasSizeGreaterThanOrEqualTo(1);
        StorageListItem item = items.get(0);
        assertThat(item.getId()).isEqualTo("storage-1");
        assertThat(item.getTitle()).isEqualTo("My S3 Bucket");
    }

    private <T> T readFixture(String name, TypeReference<T> type) throws Exception {
        try (InputStream is = getClass().getResourceAsStream("/fixtures/" + name)) {
            assertThat(is).as("fixture %s not found", name).isNotNull();
            return JsonUtil.mapper().readValue(is, type);
        }
    }

    // ===========================
    // ConversionResult — ALL getters
    // ===========================

    @Test
    void conversionResult_jobVariant() throws Exception {
        Job job = readFixture("job-success.json", Job.class);
        ResponseHeaders headers = ResponseHeaders.builder().workerJobId("w1").build();
        ConversionResult result = new ConversionResult(job, headers, 200, "output.pdf");

        assertThat(result.isJob()).isTrue();
        assertThat(result.isBinary()).isFalse();
        assertThat(result.getJob()).isSameAs(job);
        assertThat(result.getData()).isSameAs(job);
        assertThat(result.getHeaders()).isSameAs(headers);
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getFilename()).isEqualTo("output.pdf");
    }

    @Test
    void conversionResult_binaryVariant() {
        byte[] bytes = {1, 2, 3};
        ResponseHeaders headers = ResponseHeaders.builder().build();
        ConversionResult result = new ConversionResult(bytes, headers, 200, "doc.pdf");

        assertThat(result.isBinary()).isTrue();
        assertThat(result.isJob()).isFalse();
        assertThat(result.getBytes()).isEqualTo(bytes);
        assertThat(result.getData()).isEqualTo(bytes);
    }

    // ===========================
    // StatusResponse
    // ===========================

    @Test
    void statusResponse_allGetters() throws Exception {
        StatusResponse resp = readFixture("status.json", StatusResponse.class);
        assertThat(resp.getStatus()).isNotNull();
    }

    // ===========================
    // StorageNotAllowedResponse
    // ===========================

    @Test
    void storageNotAllowedResponse_allGetters() throws Exception {
        StorageNotAllowedResponse resp = readFixture("storage-not-allowed.json", StorageNotAllowedResponse.class);
        assertThat(resp.getAllowed()).isFalse();
        assertThat(resp.getReason()).contains("does not support");
        assertThat(resp.getStatusCode()).isEqualTo(403);
    }
}
