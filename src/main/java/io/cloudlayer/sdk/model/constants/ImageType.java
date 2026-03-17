package io.cloudlayer.sdk.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Image output format options.
 */
public enum ImageType {
    PNG("png"),
    JPEG("jpeg"),
    JPG("jpg"),
    WEBP("webp"),
    SVG("svg");

    private final String value;

    ImageType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ImageType fromValue(String value) {
        for (ImageType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown image type: " + value);
    }
}
