package io.cloudlayer.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Account information including usage stats.
 * Unknown fields (job type counters, etc.) are captured in {@link #getExtras()}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class AccountInfo {

    @JsonProperty("email") private String email;
    @JsonProperty("callsLimit") private Integer callsLimit;
    @JsonProperty("calls") private Integer calls;
    @JsonProperty("storageUsed") private Integer storageUsed;
    @JsonProperty("storageLimit") private Integer storageLimit;
    @JsonProperty("subscription") private String subscription;
    @JsonProperty("bytesTotal") private Integer bytesTotal;
    @JsonProperty("bytesLimit") private Integer bytesLimit;
    @JsonProperty("computeTimeTotal") private Integer computeTimeTotal;
    @JsonProperty("computeTimeLimit") private Integer computeTimeLimit;
    @JsonProperty("subType") private String subType;
    @JsonProperty("uid") private String uid;
    @JsonProperty("credit") private Double credit;
    @JsonProperty("subActive") private Boolean subActive;

    private final Map<String, Object> extras = new HashMap<>();

    @SuppressWarnings("unused")
    private AccountInfo() {}

    public String getEmail() { return email; }
    public Integer getCallsLimit() { return callsLimit; }
    public Integer getCalls() { return calls; }
    public Integer getStorageUsed() { return storageUsed; }
    public Integer getStorageLimit() { return storageLimit; }
    public String getSubscription() { return subscription; }
    public Integer getBytesTotal() { return bytesTotal; }
    public Integer getBytesLimit() { return bytesLimit; }
    public Integer getComputeTimeTotal() { return computeTimeTotal; }
    public Integer getComputeTimeLimit() { return computeTimeLimit; }
    public String getSubType() { return subType; }
    public String getUid() { return uid; }
    public Double getCredit() { return credit; }
    public Boolean getSubActive() { return subActive; }

    @JsonAnyGetter
    public Map<String, Object> getExtras() { return extras; }

    @JsonAnySetter
    public void setExtra(String key, Object value) { extras.put(key, value); }
}
