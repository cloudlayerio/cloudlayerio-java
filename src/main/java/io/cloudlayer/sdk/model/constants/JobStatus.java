package io.cloudlayer.sdk.model.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Status of a CloudLayer.io conversion job.
 */
public enum JobStatus {
    PENDING("pending"),
    SUCCESS("success"),
    ERROR("error");

    private final String value;

    JobStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static JobStatus fromValue(String value) {
        for (JobStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown job status: " + value);
    }
}
