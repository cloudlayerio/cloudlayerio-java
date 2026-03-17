package io.cloudlayer.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class StatusResponse {

    @JsonProperty("status") private String status;

    @SuppressWarnings("unused")
    private StatusResponse() {}

    /**
     * Returns the status value. Note: the API returns {@code "ok "} with a trailing space.
     */
    public String getStatus() { return status; }
}
