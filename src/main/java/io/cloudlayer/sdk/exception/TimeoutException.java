package io.cloudlayer.sdk.exception;

/**
 * Thrown when an SDK-level timeout is exceeded (not HTTP 408).
 */
public class TimeoutException extends CloudLayerException {

    private static final long serialVersionUID = 1L;

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
