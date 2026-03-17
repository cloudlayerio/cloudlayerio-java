package io.cloudlayer.sdk.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Puppeteer page load event options.
 */
public enum WaitUntilOption {
    LOAD("load"),
    DOM_CONTENT_LOADED("domcontentloaded"),
    NETWORK_IDLE_0("networkidle0"),
    NETWORK_IDLE_2("networkidle2");

    private final String value;

    WaitUntilOption(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static WaitUntilOption fromValue(String value) {
        for (WaitUntilOption option : values()) {
            if (option.value.equalsIgnoreCase(value)) {
                return option;
            }
        }
        throw new IllegalArgumentException("Unknown waitUntil option: " + value);
    }
}
