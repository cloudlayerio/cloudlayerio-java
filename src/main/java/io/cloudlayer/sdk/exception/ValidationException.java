package io.cloudlayer.sdk.exception;

/**
 * Thrown when client-side input validation fails.
 */
public class ValidationException extends CloudLayerException {

    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super(message);
    }
}
