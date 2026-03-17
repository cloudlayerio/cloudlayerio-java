package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a generatePreview option that can be a boolean or a {@link PreviewOptions} object.
 */
@JsonSerialize(using = GeneratePreviewOption.Serializer.class)
@JsonDeserialize(using = GeneratePreviewOption.Deserializer.class)
public final class GeneratePreviewOption {

    private final Boolean boolValue;
    private final PreviewOptions objectValue;

    private GeneratePreviewOption(Boolean boolValue, PreviewOptions objectValue) {
        this.boolValue = boolValue;
        this.objectValue = objectValue;
    }

    public static GeneratePreviewOption of(boolean value) {
        return new GeneratePreviewOption(value, null);
    }

    public static GeneratePreviewOption of(PreviewOptions options) {
        Objects.requireNonNull(options, "options must not be null");
        return new GeneratePreviewOption(null, options);
    }

    public boolean isBooleanValue() {
        return boolValue != null;
    }

    public boolean isObjectValue() {
        return objectValue != null;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public PreviewOptions getObjectValue() {
        return objectValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeneratePreviewOption)) return false;
        GeneratePreviewOption that = (GeneratePreviewOption) o;
        return Objects.equals(boolValue, that.boolValue) && Objects.equals(objectValue, that.objectValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boolValue, objectValue);
    }

    @Override
    public String toString() {
        return boolValue != null ? String.valueOf(boolValue) : String.valueOf(objectValue);
    }

    static final class Serializer extends JsonSerializer<GeneratePreviewOption> {
        @Override
        public void serialize(GeneratePreviewOption value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value.boolValue != null) {
                gen.writeBoolean(value.boolValue);
            } else {
                provider.defaultSerializeValue(value.objectValue, gen);
            }
        }
    }

    static final class Deserializer extends JsonDeserializer<GeneratePreviewOption> {
        @Override
        public GeneratePreviewOption deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.currentToken() == JsonToken.VALUE_TRUE || p.currentToken() == JsonToken.VALUE_FALSE) {
                return GeneratePreviewOption.of(p.getBooleanValue());
            }
            if (p.currentToken() == JsonToken.START_OBJECT) {
                PreviewOptions opts = ctxt.readValue(p, PreviewOptions.class);
                return GeneratePreviewOption.of(opts);
            }
            throw ctxt.wrongTokenException(p, GeneratePreviewOption.class,
                    p.currentToken(), "Expected boolean or object for GeneratePreviewOption");
        }
    }
}
