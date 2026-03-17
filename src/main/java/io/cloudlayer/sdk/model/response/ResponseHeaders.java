package io.cloudlayer.sdk.model.response;

/**
 * Parsed CloudLayer.io custom response headers (cl-* prefix).
 */
public final class ResponseHeaders {

    private final String workerJobId;
    private final String clusterId;
    private final String worker;
    private final Integer bandwidth;
    private final Integer processTime;
    private final Integer callsRemaining;
    private final Integer chargedTime;
    private final Double bandwidthCost;
    private final Double processTimeCost;
    private final Double apiCreditCost;

    private ResponseHeaders(Builder builder) {
        this.workerJobId = builder.workerJobId;
        this.clusterId = builder.clusterId;
        this.worker = builder.worker;
        this.bandwidth = builder.bandwidth;
        this.processTime = builder.processTime;
        this.callsRemaining = builder.callsRemaining;
        this.chargedTime = builder.chargedTime;
        this.bandwidthCost = builder.bandwidthCost;
        this.processTimeCost = builder.processTimeCost;
        this.apiCreditCost = builder.apiCreditCost;
    }

    public static Builder builder() { return new Builder(); }

    public String getWorkerJobId() { return workerJobId; }
    public String getClusterId() { return clusterId; }
    public String getWorker() { return worker; }
    public Integer getBandwidth() { return bandwidth; }
    public Integer getProcessTime() { return processTime; }
    public Integer getCallsRemaining() { return callsRemaining; }
    public Integer getChargedTime() { return chargedTime; }
    public Double getBandwidthCost() { return bandwidthCost; }
    public Double getProcessTimeCost() { return processTimeCost; }
    public Double getApiCreditCost() { return apiCreditCost; }

    /**
     * Parses ResponseHeaders from HTTP response header values.
     */
    public static ResponseHeaders fromHttpHeaders(java.util.function.Function<String, String> headerLookup) {
        Builder b = builder();
        b.workerJobId(headerLookup.apply("cl-worker-job-id"));
        b.clusterId(headerLookup.apply("cl-cluster-id"));
        b.worker(headerLookup.apply("cl-worker"));
        b.bandwidth(parseInteger(headerLookup.apply("cl-bandwidth")));
        b.processTime(parseInteger(headerLookup.apply("cl-process-time")));
        b.callsRemaining(parseInteger(headerLookup.apply("cl-calls-remaining")));
        b.chargedTime(parseInteger(headerLookup.apply("cl-charged-time")));
        b.bandwidthCost(parseDouble(headerLookup.apply("cl-bandwidth-cost")));
        b.processTimeCost(parseDouble(headerLookup.apply("cl-process-time-cost")));
        b.apiCreditCost(parseDouble(headerLookup.apply("cl-api-credit-cost")));
        return b.build();
    }

    private static Integer parseInteger(String value) {
        if (value == null || value.isEmpty()) return null;
        try { return Integer.parseInt(value); } catch (NumberFormatException e) { return null; }
    }

    private static Double parseDouble(String value) {
        if (value == null || value.isEmpty()) return null;
        try { return Double.parseDouble(value); } catch (NumberFormatException e) { return null; }
    }

    public static final class Builder {
        private String workerJobId;
        private String clusterId;
        private String worker;
        private Integer bandwidth;
        private Integer processTime;
        private Integer callsRemaining;
        private Integer chargedTime;
        private Double bandwidthCost;
        private Double processTimeCost;
        private Double apiCreditCost;

        private Builder() {}

        public Builder workerJobId(String val) { this.workerJobId = val; return this; }
        public Builder clusterId(String val) { this.clusterId = val; return this; }
        public Builder worker(String val) { this.worker = val; return this; }
        public Builder bandwidth(Integer val) { this.bandwidth = val; return this; }
        public Builder processTime(Integer val) { this.processTime = val; return this; }
        public Builder callsRemaining(Integer val) { this.callsRemaining = val; return this; }
        public Builder chargedTime(Integer val) { this.chargedTime = val; return this; }
        public Builder bandwidthCost(Double val) { this.bandwidthCost = val; return this; }
        public Builder processTimeCost(Double val) { this.processTimeCost = val; return this; }
        public Builder apiCreditCost(Double val) { this.apiCreditCost = val; return this; }

        public ResponseHeaders build() { return new ResponseHeaders(this); }
    }
}
