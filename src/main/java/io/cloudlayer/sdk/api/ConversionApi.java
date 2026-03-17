package io.cloudlayer.sdk.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudlayer.sdk.http.HttpTransport;
import io.cloudlayer.sdk.http.MultipartBodyBuilder;
import io.cloudlayer.sdk.http.RequestOptions;
import io.cloudlayer.sdk.model.endpoint.*;
import io.cloudlayer.sdk.model.response.ConversionResult;
import io.cloudlayer.sdk.model.response.Job;
import io.cloudlayer.sdk.model.response.ResponseHeaders;
import io.cloudlayer.sdk.util.FileInput;
import io.cloudlayer.sdk.validation.InputValidator;

import java.io.IOException;

/**
 * Conversion API methods for URL, HTML, Template, and Document conversions.
 */
public final class ConversionApi {

    private static final RequestOptions NOT_RETRYABLE = RequestOptions.of(false);

    private final HttpTransport transport;
    private final ObjectMapper mapper;

    public ConversionApi(HttpTransport transport, ObjectMapper mapper) {
        this.transport = transport;
        this.mapper = mapper;
    }

    public ConversionResult urlToPdf(UrlToPdfOptions options) {
        InputValidator.validateUrlToPdf(options);
        return sendConversion("url/pdf", options);
    }

    public ConversionResult urlToImage(UrlToImageOptions options) {
        InputValidator.validateUrlToImage(options);
        return sendConversion("url/image", options);
    }

    public ConversionResult htmlToPdf(HtmlToPdfOptions options) {
        InputValidator.validateHtmlToPdf(options);
        return sendConversion("html/pdf", options);
    }

    public ConversionResult htmlToImage(HtmlToImageOptions options) {
        InputValidator.validateHtmlToImage(options);
        return sendConversion("html/image", options);
    }

    public ConversionResult templateToPdf(TemplateToPdfOptions options) {
        InputValidator.validateTemplateToPdf(options);
        return sendConversion("template/pdf", options);
    }

    public ConversionResult templateToImage(TemplateToImageOptions options) {
        InputValidator.validateTemplateToImage(options);
        return sendConversion("template/image", options);
    }

    public ConversionResult docxToPdf(DocxToPdfOptions options) {
        InputValidator.validateDocxToPdf(options);
        return sendFileConversion("docx/pdf", options.getFile(), options);
    }

    public ConversionResult docxToHtml(DocxToHtmlOptions options) {
        InputValidator.validateDocxToHtml(options);
        return sendFileConversion("docx/html", options.getFile(), options);
    }

    public ConversionResult pdfToDocx(PdfToDocxOptions options) {
        InputValidator.validatePdfToDocx(options);
        return sendFileConversion("pdf/docx", options.getFile(), options);
    }

    public ConversionResult mergePdfs(MergePdfsOptions options) {
        InputValidator.validateMergePdfs(options);
        return sendConversion("pdf/merge", options);
    }

    // --- Private implementation ---

    private ConversionResult sendConversion(String path, Object options) {
        HttpTransport.RawResponse raw = transport.sendRawRequest("POST", path, options, NOT_RETRYABLE);
        return buildConversionResult(raw);
    }

    private ConversionResult sendFileConversion(String path, FileInput file, Object options) {
        try {
            MultipartBodyBuilder multipart = new MultipartBodyBuilder();
            multipart.addFile("file", file.getFilename(), file.getContentType(), file.getData());

            // Serialize remaining options as JSON and add as form fields
            String optionsJson = mapper.writeValueAsString(options);
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> fields = mapper.readValue(optionsJson, java.util.Map.class);
            for (java.util.Map.Entry<String, Object> entry : fields.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    String strValue = value instanceof String
                            ? (String) value
                            : mapper.writeValueAsString(value);
                    multipart.addField(entry.getKey(), strValue);
                }
            }

            HttpTransport.RawResponse raw = transport.sendMultipartRawRequest("POST", path, multipart, NOT_RETRYABLE);
            return buildConversionResult(raw);
        } catch (IOException e) {
            throw new io.cloudlayer.sdk.exception.CloudLayerException(
                    "Failed to build multipart request: " + e.getMessage(), e);
        }
    }

    private ConversionResult buildConversionResult(HttpTransport.RawResponse raw) {
        ResponseHeaders headers = raw.getHeaders();
        int status = raw.getStatus();
        String filename = raw.getFilename();
        byte[] body = raw.getBody();

        // Try to parse as Job JSON first
        try {
            Job job = mapper.readValue(body, Job.class);
            if (job.getId() != null || job.getStatus() != null) {
                return new ConversionResult(job, headers, status, filename);
            }
        } catch (IOException ignored) {
            // Not JSON — treat as binary
        }

        // Raw binary response (v1 sync mode)
        return new ConversionResult(body, headers, status, filename);
    }
}
