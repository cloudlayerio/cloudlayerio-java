package io.cloudlayer.sdk.exception;

/**
 * Thrown when the API returns HTTP 401 (Unauthorized) or 403 (Forbidden).
 */
public class AuthException extends ApiException {

    private static final long serialVersionUID = 1L;

    public AuthException(int statusCode, String statusText, String path, String method, String responseBody) {
        super(statusCode, statusText, path, method, responseBody);
    }
}
