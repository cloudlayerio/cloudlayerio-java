package io.cloudlayer.sdk.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Constructs multipart/form-data request bodies for java.net.http.HttpClient.
 */
public final class MultipartBodyBuilder {

    private static final byte[] CRLF = "\r\n".getBytes(StandardCharsets.UTF_8);
    private static final byte[] DASHES = "--".getBytes(StandardCharsets.UTF_8);

    private final String boundary;
    private final ByteArrayOutputStream baos;

    public MultipartBodyBuilder() {
        this.boundary = UUID.randomUUID().toString();
        this.baos = new ByteArrayOutputStream(4096);
    }

    /**
     * Adds a text form field.
     */
    public MultipartBodyBuilder addField(String name, String value) throws IOException {
        baos.write(DASHES);
        baos.write(boundary.getBytes(StandardCharsets.UTF_8));
        baos.write(CRLF);
        String header = "Content-Disposition: form-data; name=\"" + name + "\"";
        baos.write(header.getBytes(StandardCharsets.UTF_8));
        baos.write(CRLF);
        baos.write(CRLF);
        baos.write(value.getBytes(StandardCharsets.UTF_8));
        baos.write(CRLF);
        return this;
    }

    /**
     * Adds a file part.
     */
    public MultipartBodyBuilder addFile(String name, String filename, String contentType, byte[] data) throws IOException {
        baos.write(DASHES);
        baos.write(boundary.getBytes(StandardCharsets.UTF_8));
        baos.write(CRLF);
        String header = "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"";
        baos.write(header.getBytes(StandardCharsets.UTF_8));
        baos.write(CRLF);
        String ct = "Content-Type: " + (contentType != null ? contentType : "application/octet-stream");
        baos.write(ct.getBytes(StandardCharsets.UTF_8));
        baos.write(CRLF);
        baos.write(CRLF);
        baos.write(data);
        baos.write(CRLF);
        return this;
    }

    /**
     * Returns the Content-Type header value including the boundary.
     */
    public String getContentType() {
        return "multipart/form-data; boundary=" + boundary;
    }

    /**
     * Builds the body publisher with the closing boundary.
     */
    public HttpRequest.BodyPublisher build() throws IOException {
        baos.write(DASHES);
        baos.write(boundary.getBytes(StandardCharsets.UTF_8));
        baos.write(DASHES);
        baos.write(CRLF);
        return HttpRequest.BodyPublishers.ofByteArray(baos.toByteArray());
    }
}
