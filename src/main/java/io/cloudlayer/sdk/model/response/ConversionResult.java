package io.cloudlayer.sdk.model.response;

/**
 * Result from a conversion API call. Contains either a {@link Job} (v2/async) or raw bytes (v1/sync).
 */
public final class ConversionResult {

    private final Object data;
    private final ResponseHeaders headers;
    private final int status;
    private final String filename;

    public ConversionResult(Object data, ResponseHeaders headers, int status, String filename) {
        this.data = data;
        this.headers = headers;
        this.status = status;
        this.filename = filename;
    }

    /**
     * Returns true if the result contains a Job object (v2/async response).
     */
    public boolean isJob() { return data instanceof Job; }

    /**
     * Returns true if the result contains raw binary data (v1/sync response).
     */
    public boolean isBinary() { return data instanceof byte[]; }

    /**
     * Returns the Job if this is a v2/async response.
     * @throws ClassCastException if the result is binary
     */
    public Job getJob() { return (Job) data; }

    /**
     * Returns the raw bytes if this is a v1/sync response.
     * @throws ClassCastException if the result is a Job
     */
    public byte[] getBytes() { return (byte[]) data; }

    public Object getData() { return data; }
    public ResponseHeaders getHeaders() { return headers; }
    public int getStatus() { return status; }
    public String getFilename() { return filename; }
}
