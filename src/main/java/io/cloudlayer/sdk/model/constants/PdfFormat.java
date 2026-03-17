package io.cloudlayer.sdk.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * PDF page format options.
 */
public enum PdfFormat {
    LETTER("letter"),
    LEGAL("legal"),
    TABLOID("tabloid"),
    LEDGER("ledger"),
    A0("a0"),
    A1("a1"),
    A2("a2"),
    A3("a3"),
    A4("a4"),
    A5("a5"),
    A6("a6");

    private final String value;

    PdfFormat(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PdfFormat fromValue(String value) {
        for (PdfFormat format : values()) {
            if (format.value.equalsIgnoreCase(value)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Unknown PDF format: " + value);
    }
}
