package io.cloudlayer.sdk.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 * Utility for HTML encoding operations.
 */
public final class HtmlUtil {

    private HtmlUtil() {}

    /**
     * Base64-encodes an HTML string for use with htmlToPdf/htmlToImage endpoints.
     *
     * @param html the raw HTML string
     * @return Base64-encoded string
     */
    public static String encodeHtml(String html) {
        Objects.requireNonNull(html, "html must not be null");
        return Base64.getEncoder().encodeToString(html.getBytes(StandardCharsets.UTF_8));
    }
}
