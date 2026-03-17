# cloudlayer.io Java SDK

Official Java SDK for the [cloudlayer.io](https://cloudlayer.io) document generation API.

[![Maven Central](https://img.shields.io/maven-central/v/io.cloudlayer/cloudlayerio-java)](https://central.sonatype.com/artifact/io.cloudlayer/cloudlayerio-java)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java 11+](https://img.shields.io/badge/Java-11%2B-orange)](https://openjdk.org/)

## Installation

### Maven

```xml
<dependency>
    <groupId>io.cloudlayer</groupId>
    <artifactId>cloudlayerio-java</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle (Groovy)

```groovy
implementation 'io.cloudlayer:cloudlayerio-java:0.1.0'
```

### Gradle (Kotlin DSL)

```kotlin
implementation("io.cloudlayer:cloudlayerio-java:0.1.0")
```

## Quick Start

```java
import io.cloudlayer.sdk.*;
import io.cloudlayer.sdk.model.endpoint.*;
import io.cloudlayer.sdk.model.constants.*;
import io.cloudlayer.sdk.model.response.*;

// Create a client
CloudLayer client = CloudLayer.builder("your-api-key", ApiVersion.V2).build();

// Convert URL to PDF
ConversionResult result = client.urlToPdf(UrlToPdfOptions.builder()
        .url("https://example.com")
        .format(PdfFormat.A4)
        .printBackground(true)
        .build());

// v2 returns a Job — wait for completion then download
Job job = client.waitForJob(result.getJob().getId());
byte[] pdf = client.downloadJobResult(job);

// Save to file
java.nio.file.Files.write(java.nio.file.Path.of("output.pdf"), pdf);
```

## API Versions

cloudlayer.io supports two API versions:

| | v1 | v2 |
|---|---|---|
| **Response** | Raw binary (PDF/image bytes) | JSON Job object |
| **Async** | Synchronous by default | Asynchronous by default |
| **Workflow** | Get bytes directly | Get Job → `waitForJob()` → `downloadJobResult()` |

**v2 is recommended** for production use — it provides job tracking, async processing, and asset URLs.

```java
// v1: returns binary directly
CloudLayer v1Client = CloudLayer.builder("key", ApiVersion.V1).build();
ConversionResult v1Result = v1Client.urlToPdf(UrlToPdfOptions.builder()
        .url("https://example.com").build());
byte[] pdfBytes = v1Result.getBytes();

// v2: returns Job, then download
CloudLayer v2Client = CloudLayer.builder("key", ApiVersion.V2).build();
ConversionResult v2Result = v2Client.urlToPdf(UrlToPdfOptions.builder()
        .url("https://example.com").build());
Job job = client.waitForJob(v2Result.getJob().getId());
byte[] pdf = client.downloadJobResult(job);
```

## Configuration

```java
CloudLayer client = CloudLayer.builder("your-api-key", ApiVersion.V2)
        .baseUrl("https://api.cloudlayer.io")  // default
        .timeout(Duration.ofSeconds(30))         // default; request timeout
        .maxRetries(2)                           // default; range [0, 5]
        .userAgent("my-app/1.0")                 // custom user agent
        .headers(Map.of("X-Custom", "value"))    // additional headers
        .httpClient(customHttpClient)            // inject custom HttpClient
        .build();
```

The `CloudLayer` instance is **immutable and thread-safe** — create one and share it across threads.

## Conversion Methods

### URL to PDF / Image

```java
ConversionResult result = client.urlToPdf(UrlToPdfOptions.builder()
        .url("https://example.com")
        .format(PdfFormat.A4)
        .printBackground(true)
        .margin(Margin.builder().top("20mm").bottom("20mm").build())
        .waitUntil(WaitUntilOption.NETWORK_IDLE_2)
        .build());

ConversionResult imageResult = client.urlToImage(UrlToImageOptions.builder()
        .url("https://example.com")
        .imageType(ImageType.PNG)
        .quality(90)
        .build());
```

### HTML to PDF / Image

HTML must be Base64-encoded. Use `HtmlUtil.encodeHtml()`:

```java
import io.cloudlayer.sdk.util.HtmlUtil;

String html = "<html><body><h1>Hello World</h1></body></html>";
ConversionResult result = client.htmlToPdf(HtmlToPdfOptions.builder()
        .html(HtmlUtil.encodeHtml(html))
        .format(PdfFormat.LETTER)
        .build());
```

### Template to PDF / Image

```java
ConversionResult result = client.templateToPdf(TemplateToPdfOptions.builder()
        .templateId("your-template-id")
        .data(Map.of("name", "John Doe", "amount", 1500))
        .build());
```

### Document Conversion (File Upload)

```java
import io.cloudlayer.sdk.util.FileInput;

// DOCX to PDF
ConversionResult result = client.docxToPdf(DocxToPdfOptions.builder()
        .file(FileInput.fromPath(Path.of("document.docx")))
        .build());

// PDF to DOCX
ConversionResult result = client.pdfToDocx(PdfToDocxOptions.builder()
        .file(FileInput.fromPath(Path.of("document.pdf")))
        .build());
```

### Merge PDFs

```java
ConversionResult result = client.mergePdfs(MergePdfsOptions.builder()
        .batch(Batch.of(List.of(
                "https://example.com/doc1.pdf",
                "https://example.com/doc2.pdf"
        )))
        .build());
```

## Data Management

### Jobs

```java
List<Job> jobs = client.listJobs();          // Recent jobs (~10)
Job job = client.getJob("job-id");           // Get specific job
```

### Assets

```java
List<Asset> assets = client.listAssets();    // Recent assets (~10)
Asset asset = client.getAsset("asset-id");
```

### Storage

```java
List<StorageListItem> configs = client.listStorage();
StorageDetail detail = client.getStorage("storage-id");

// Add custom S3 storage
StorageCreateResponse created = client.addStorage(
        StorageParams.builder("My Bucket", "us-east-1", "AKIA...", "secret", "bucket-name")
                .endpoint("https://s3.amazonaws.com")
                .build());

client.deleteStorage("storage-id");
```

### Account & Status

```java
AccountInfo account = client.getAccount();
StatusResponse status = client.getStatus();
```

### Templates

```java
List<PublicTemplate> templates = client.listTemplates();
List<PublicTemplate> filtered = client.listTemplates(
        ListTemplatesOptions.builder().type("pdf").category("business").build());
PublicTemplate template = client.getTemplate("template-id");
```

## Working with v2 Results

v2 conversions return a `Job` object. Use `waitForJob()` and `downloadJobResult()` to get the final binary:

```java
// Start conversion
ConversionResult result = client.urlToPdf(UrlToPdfOptions.builder()
        .url("https://example.com").build());

// Wait for completion (polls every 5 seconds, timeout 5 minutes)
Job job = client.waitForJob(result.getJob().getId());

// Download the result
byte[] pdf = client.downloadJobResult(job);

// Or customize polling
Job job = client.waitForJob(result.getJob().getId(),
        WaitForJobOptions.builder()
                .interval(Duration.ofSeconds(3))
                .maxWait(Duration.ofMinutes(10))
                .build());
```

## Error Handling

All SDK exceptions extend `CloudLayerException` (which extends `RuntimeException`):

```java
try {
    client.urlToPdf(options);
} catch (AuthException e) {
    // 401 or 403 — invalid API key
    System.err.println("Auth failed: " + e.getStatusCode());
} catch (RateLimitException e) {
    // 429 — rate limited
    System.err.println("Rate limited, retry after: " + e.getRetryAfterSeconds() + "s");
} catch (ValidationException e) {
    // Client-side validation failed
    System.err.println("Invalid input: " + e.getMessage());
} catch (ApiException e) {
    // Other API error (400, 404, 500, etc.)
    System.err.println("API error " + e.getStatusCode() + ": " + e.getMessage());
} catch (TimeoutException e) {
    // Request or waitForJob timed out
    System.err.println("Timed out: " + e.getMessage());
} catch (NetworkException e) {
    // Connection or DNS failure
    System.err.println("Network error: " + e.getMessage());
}
```

### Exception Hierarchy

```
CloudLayerException (RuntimeException)
├── ConfigException          — invalid client configuration
├── ValidationException      — client-side input validation
├── NetworkException         — connection/DNS failure
├── TimeoutException         — SDK timeout exceeded
└── ApiException             — HTTP 4xx/5xx response
    ├── AuthException        — HTTP 401/403
    └── RateLimitException   — HTTP 429
```

## Compatibility

- **Java 11+** required (uses `java.net.http.HttpClient`)
- Tested on Java 11, 17, and 21 (LTS versions)
- Works with Kotlin projects via standard Java interop
- Only runtime dependency: Jackson (JSON serialization)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes with tests
4. Run `mvn verify` to ensure all tests pass
5. Submit a pull request

## License

[MIT](LICENSE)
