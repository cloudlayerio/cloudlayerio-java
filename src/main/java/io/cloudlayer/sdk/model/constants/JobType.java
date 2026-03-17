package io.cloudlayer.sdk.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Conversion job types supported by the CloudLayer.io API.
 */
public enum JobType {
    HTML_PDF("html-pdf"),
    HTML_IMAGE("html-image"),
    URL_PDF("url-pdf"),
    URL_IMAGE("url-image"),
    TEMPLATE_PDF("template-pdf"),
    TEMPLATE_IMAGE("template-image"),
    DOCX_PDF("docx-pdf"),
    DOCX_HTML("docx-html"),
    IMAGE_PDF("image-pdf"),
    PDF_IMAGE("pdf-image"),
    PDF_DOCX("pdf-docx"),
    PDF_MERGE("merge-pdf");

    private final String value;

    JobType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static JobType fromValue(String value) {
        for (JobType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown job type: " + value);
    }
}
