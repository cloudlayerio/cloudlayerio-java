package io.cloudlayer.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Returned with HTTP 200 when the user's plan does not support custom storage.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class StorageNotAllowedResponse {

    @JsonProperty("allowed") private Boolean allowed;
    @JsonProperty("reason") private String reason;
    @JsonProperty("statusCode") private Integer statusCode;

    @SuppressWarnings("unused")
    private StorageNotAllowedResponse() {}

    public Boolean getAllowed() { return allowed; }
    public String getReason() { return reason; }
    public Integer getStatusCode() { return statusCode; }
}
