package io.cloudlayer.sdk.validation;

import io.cloudlayer.sdk.exception.ValidationException;
import io.cloudlayer.sdk.model.endpoint.*;
import io.cloudlayer.sdk.model.options.Batch;
import io.cloudlayer.sdk.util.FileInput;

/**
 * Client-side input validation for fast feedback before API calls.
 */
public final class InputValidator {

    private InputValidator() {}

    public static void validateUrlToPdf(UrlToPdfOptions opts) {
        validateUrlOrBatch(opts.getUrl(), opts.getBatch());
        validateBaseFields(opts.getAsync(), opts.getStorage() != null, opts.getWebhook(),
                opts.getTimeout(), null);
    }

    public static void validateUrlToImage(UrlToImageOptions opts) {
        validateUrlOrBatch(opts.getUrl(), opts.getBatch());
        validateBaseFields(opts.getAsync(), opts.getStorage() != null, opts.getWebhook(),
                opts.getTimeout(), opts.getQuality());
    }

    public static void validateHtmlToPdf(HtmlToPdfOptions opts) {
        if (opts.getHtml() == null || opts.getHtml().isEmpty()) {
            throw new ValidationException("html must not be empty");
        }
        validateBaseFields(opts.getAsync(), opts.getStorage() != null, opts.getWebhook(),
                opts.getTimeout(), null);
    }

    public static void validateHtmlToImage(HtmlToImageOptions opts) {
        if (opts.getHtml() == null || opts.getHtml().isEmpty()) {
            throw new ValidationException("html must not be empty");
        }
        validateBaseFields(opts.getAsync(), opts.getStorage() != null, opts.getWebhook(),
                opts.getTimeout(), opts.getQuality());
    }

    public static void validateTemplateToPdf(TemplateToPdfOptions opts) {
        validateTemplateFields(opts.getTemplateId(), opts.getTemplate());
        validateBaseFields(opts.getAsync(), opts.getStorage() != null, opts.getWebhook(),
                opts.getTimeout(), null);
    }

    public static void validateTemplateToImage(TemplateToImageOptions opts) {
        validateTemplateFields(opts.getTemplateId(), opts.getTemplate());
        validateBaseFields(opts.getAsync(), opts.getStorage() != null, opts.getWebhook(),
                opts.getTimeout(), opts.getQuality());
    }

    public static void validateDocxToPdf(DocxToPdfOptions opts) {
        validateFileInput(opts.getFile());
        validateBaseFields(opts.getAsync(), opts.getStorage() != null, opts.getWebhook(),
                opts.getTimeout(), null);
    }

    public static void validateDocxToHtml(DocxToHtmlOptions opts) {
        validateFileInput(opts.getFile());
        validateBaseFields(opts.getAsync(), opts.getStorage() != null, opts.getWebhook(),
                opts.getTimeout(), null);
    }

    public static void validatePdfToDocx(PdfToDocxOptions opts) {
        validateFileInput(opts.getFile());
        validateBaseFields(opts.getAsync(), opts.getStorage() != null, opts.getWebhook(),
                opts.getTimeout(), null);
    }

    public static void validateMergePdfs(MergePdfsOptions opts) {
        validateUrlOrBatch(opts.getUrl(), opts.getBatch());
        validateBaseFields(opts.getAsync(), opts.getStorage() != null, opts.getWebhook(),
                opts.getTimeout(), null);
    }

    // --- Private helpers ---

    private static void validateUrlOrBatch(String url, Batch batch) {
        boolean hasUrl = url != null && !url.isEmpty();
        boolean hasBatch = batch != null && batch.getUrls() != null && !batch.getUrls().isEmpty();

        if (!hasUrl && !hasBatch) {
            throw new ValidationException("Either url or batch.urls must be provided");
        }
        if (hasUrl && hasBatch) {
            throw new ValidationException("url and batch.urls are mutually exclusive");
        }
        if (hasBatch && batch.getUrls().size() > 20) {
            throw new ValidationException("batch.urls must not exceed 20 items");
        }
    }

    private static void validateTemplateFields(String templateId, String template) {
        boolean hasId = templateId != null && !templateId.isEmpty();
        boolean hasTemplate = template != null && !template.isEmpty();

        if (!hasId && !hasTemplate) {
            throw new ValidationException("Either templateId or template must be provided");
        }
        if (hasId && hasTemplate) {
            throw new ValidationException("templateId and template are mutually exclusive");
        }
    }

    private static void validateFileInput(FileInput file) {
        if (file == null) {
            throw new ValidationException("file must not be null");
        }
    }

    private static void validateBaseFields(Boolean async, boolean hasStorage, String webhook,
                                           Integer timeout, Integer quality) {
        if (webhook != null && !webhook.isEmpty() && !webhook.startsWith("https://")) {
            throw new ValidationException("webhook must use HTTPS");
        }
        if (timeout != null && timeout < 1000) {
            throw new ValidationException("timeout must be at least 1000 milliseconds");
        }
        if (quality != null && (quality < 0 || quality > 100)) {
            throw new ValidationException("quality must be between 0 and 100");
        }
    }
}
