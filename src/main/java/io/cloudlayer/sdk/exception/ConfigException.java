package io.cloudlayer.sdk.exception;

/**
 * Thrown when the CloudLayer client is configured with invalid parameters.
 */
public class ConfigException extends CloudLayerException {

    private static final long serialVersionUID = 1L;

    public ConfigException(String message) {
        super(message);
    }
}
