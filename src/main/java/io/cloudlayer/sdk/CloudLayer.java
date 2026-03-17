package io.cloudlayer.sdk;

import io.cloudlayer.sdk.api.ConversionApi;
import io.cloudlayer.sdk.api.DataApi;
import io.cloudlayer.sdk.exception.ConfigException;
import io.cloudlayer.sdk.http.HttpTransport;
import io.cloudlayer.sdk.model.endpoint.*;
import io.cloudlayer.sdk.model.options.ListTemplatesOptions;
import io.cloudlayer.sdk.model.options.StorageParams;
import io.cloudlayer.sdk.model.options.WaitForJobOptions;
import io.cloudlayer.sdk.model.response.*;
import io.cloudlayer.sdk.util.JsonUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Main client for the CloudLayer.io document generation API.
 * <p>
 * Create instances via {@link #builder(String, ApiVersion)}. The client is immutable and thread-safe.
 */
public final class CloudLayer {

    public static final String VERSION = "0.1.0";
    private static final String DEFAULT_BASE_URL = "https://api.cloudlayer.io";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final int DEFAULT_MAX_RETRIES = 2;
    private static final int MAX_RETRIES_CEILING = 5;
    private static final String DEFAULT_USER_AGENT = "cloudlayerio-java/" + VERSION;

    private final CloudLayerConfig config;
    private final HttpTransport transport;
    private final ObjectMapper objectMapper;
    private final ConversionApi conversionApi;
    private final DataApi dataApi;

    private CloudLayer(CloudLayerConfig config) {
        this.config = config;
        this.objectMapper = JsonUtil.mapper();
        this.transport = new HttpTransport(config, objectMapper);
        this.conversionApi = new ConversionApi(transport, objectMapper);
        this.dataApi = new DataApi(transport, objectMapper, config.getHttpClient(), config.getUserAgent());
    }

    /**
     * Creates a new builder for configuring a CloudLayer client.
     *
     * @param apiKey     your CloudLayer.io API key (required)
     * @param apiVersion the API version to use (required)
     * @return a new builder instance
     */
    public static Builder builder(String apiKey, ApiVersion apiVersion) {
        return new Builder(apiKey, apiVersion);
    }

    public CloudLayerConfig getConfig() { return config; }
    public HttpTransport getTransport() { return transport; }
    ObjectMapper getObjectMapper() { return objectMapper; }

    // --- Conversion API methods ---

    public ConversionResult urlToPdf(UrlToPdfOptions options) { return conversionApi.urlToPdf(options); }
    public ConversionResult urlToImage(UrlToImageOptions options) { return conversionApi.urlToImage(options); }
    public ConversionResult htmlToPdf(HtmlToPdfOptions options) { return conversionApi.htmlToPdf(options); }
    public ConversionResult htmlToImage(HtmlToImageOptions options) { return conversionApi.htmlToImage(options); }
    public ConversionResult templateToPdf(TemplateToPdfOptions options) { return conversionApi.templateToPdf(options); }
    public ConversionResult templateToImage(TemplateToImageOptions options) { return conversionApi.templateToImage(options); }
    public ConversionResult docxToPdf(DocxToPdfOptions options) { return conversionApi.docxToPdf(options); }
    public ConversionResult docxToHtml(DocxToHtmlOptions options) { return conversionApi.docxToHtml(options); }
    public ConversionResult pdfToDocx(PdfToDocxOptions options) { return conversionApi.pdfToDocx(options); }
    public ConversionResult mergePdfs(MergePdfsOptions options) { return conversionApi.mergePdfs(options); }

    // --- Data Management API methods ---

    public java.util.List<Job> listJobs() { return dataApi.listJobs(); }
    public Job getJob(String jobId) { return dataApi.getJob(jobId); }
    public java.util.List<Asset> listAssets() { return dataApi.listAssets(); }
    public Asset getAsset(String assetId) { return dataApi.getAsset(assetId); }
    public java.util.List<StorageListItem> listStorage() { return dataApi.listStorage(); }
    public StorageDetail getStorage(String storageId) { return dataApi.getStorage(storageId); }
    public StorageCreateResponse addStorage(StorageParams params) { return dataApi.addStorage(params); }
    public void deleteStorage(String storageId) { dataApi.deleteStorage(storageId); }
    public AccountInfo getAccount() { return dataApi.getAccount(); }
    public StatusResponse getStatus() { return dataApi.getStatus(); }
    public java.util.List<PublicTemplate> listTemplates() { return dataApi.listTemplates(); }
    public java.util.List<PublicTemplate> listTemplates(ListTemplatesOptions options) { return dataApi.listTemplates(options); }
    public PublicTemplate getTemplate(String templateId) { return dataApi.getTemplate(templateId); }
    public Job waitForJob(String jobId) { return dataApi.waitForJob(jobId); }
    public Job waitForJob(String jobId, WaitForJobOptions options) { return dataApi.waitForJob(jobId, options); }
    public byte[] downloadJobResult(Job job) { return dataApi.downloadJobResult(job); }

    public static final class Builder {
        private final String apiKey;
        private final ApiVersion apiVersion;
        private String baseUrl = DEFAULT_BASE_URL;
        private Duration timeout = DEFAULT_TIMEOUT;
        private int maxRetries = DEFAULT_MAX_RETRIES;
        private HttpClient httpClient;
        private String userAgent = DEFAULT_USER_AGENT;
        private Map<String, String> customHeaders;

        private Builder(String apiKey, ApiVersion apiVersion) {
            this.apiKey = apiKey;
            this.apiVersion = apiVersion;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.customHeaders = headers;
            return this;
        }

        public CloudLayer build() {
            if (apiKey == null || apiKey.trim().isEmpty()) {
                throw new ConfigException("apiKey must not be null or empty");
            }
            Objects.requireNonNull(apiVersion, "apiVersion must not be null");

            if (baseUrl == null || baseUrl.trim().isEmpty()) {
                throw new ConfigException("baseUrl must not be null or empty");
            }
            try {
                new URL(baseUrl);
            } catch (MalformedURLException e) {
                throw new ConfigException("baseUrl is not a valid URL: " + baseUrl);
            }

            if (timeout == null || timeout.isNegative() || timeout.isZero()) {
                throw new ConfigException("timeout must be a positive duration");
            }

            // Clamp maxRetries silently to [0, 5]
            int clampedRetries = Math.max(0, Math.min(maxRetries, MAX_RETRIES_CEILING));

            // Strip trailing slash from baseUrl
            String normalizedBaseUrl = baseUrl.endsWith("/")
                    ? baseUrl.substring(0, baseUrl.length() - 1)
                    : baseUrl;

            HttpClient client = httpClient != null
                    ? httpClient
                    : HttpClient.newBuilder()
                    .connectTimeout(timeout)
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

            CloudLayerConfig config = new CloudLayerConfig(
                    apiKey, apiVersion, normalizedBaseUrl, timeout,
                    clampedRetries, client, userAgent,
                    customHeaders != null ? new HashMap<>(customHeaders) : null
            );

            return new CloudLayer(config);
        }
    }
}
