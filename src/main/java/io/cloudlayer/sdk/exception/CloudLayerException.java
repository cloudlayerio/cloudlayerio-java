package io.cloudlayer.sdk.exception;

/**
 * Base exception for all CloudLayer.io SDK errors.
 */
public class CloudLayerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CloudLayerException(String message) {
        super(message);
    }

    public CloudLayerException(String message, Throwable cause) {
        super(message, cause);
    }
}
