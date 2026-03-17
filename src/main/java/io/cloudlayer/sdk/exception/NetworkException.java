package io.cloudlayer.sdk.exception;

/**
 * Thrown when a network-level failure occurs (connection refused, DNS failure, etc.).
 */
public class NetworkException extends CloudLayerException {

    private static final long serialVersionUID = 1L;

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
