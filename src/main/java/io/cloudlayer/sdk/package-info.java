/**
 * Official Java SDK for the <a href="https://cloudlayer.io">cloudlayer.io</a> document generation API.
 *
 * <h2>Quick Start</h2>
 * <pre>{@code
 * CloudLayer client = CloudLayer.builder("your-api-key", ApiVersion.V2).build();
 *
 * // Convert URL to PDF
 * ConversionResult result = client.urlToPdf(UrlToPdfOptions.builder()
 *         .url("https://example.com")
 *         .format(PdfFormat.A4)
 *         .build());
 *
 * // Wait for job completion and download the result
 * Job job = client.waitForJob(result.getJob().getId());
 * byte[] pdf = client.downloadJobResult(job);
 * }</pre>
 *
 * @see io.cloudlayer.sdk.CloudLayer
 * @see <a href="https://cloudlayer.io/docs">cloudlayer.io API Documentation</a>
 */
package io.cloudlayer.sdk;
