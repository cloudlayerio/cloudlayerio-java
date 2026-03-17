package io.cloudlayer.sdk.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Wraps a file for upload to document conversion endpoints (DOCX, PDF).
 */
public final class FileInput {

    private final byte[] data;
    private final String filename;
    private final String contentType;

    private FileInput(byte[] data, String filename, String contentType) {
        this.data = Objects.requireNonNull(data, "data must not be null");
        this.filename = Objects.requireNonNull(filename, "filename must not be null");
        this.contentType = contentType != null ? contentType : "application/octet-stream";
    }

    /**
     * Creates a FileInput from a file path.
     */
    public static FileInput fromPath(Path path) throws IOException {
        Objects.requireNonNull(path, "path must not be null");
        byte[] data = Files.readAllBytes(path);
        String filename = path.getFileName().toString();
        String contentType = Files.probeContentType(path);
        return new FileInput(data, filename, contentType);
    }

    /**
     * Creates a FileInput from raw bytes.
     */
    public static FileInput fromBytes(byte[] data, String filename) {
        return new FileInput(data, filename, null);
    }

    /**
     * Creates a FileInput from raw bytes with explicit content type.
     */
    public static FileInput fromBytes(byte[] data, String filename, String contentType) {
        return new FileInput(data, filename, contentType);
    }

    public byte[] getData() { return data; }
    public String getFilename() { return filename; }
    public String getContentType() { return contentType; }
}
