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
 * Represents a three-state string: absent (not serialized), explicit null, or a string value.
 * Used for fields like {@code emulateMediaType} where null means "reset to default".
 */
@JsonSerialize(using = NullableString.Serializer.class)
@JsonDeserialize(using = NullableString.Deserializer.class)
public final class NullableString {

    private static final NullableString NULL_VALUE = new NullableString(true, null);

    private final boolean isNull;
    private final String value;

    private NullableString(boolean isNull, String value) {
        this.isNull = isNull;
        this.value = value;
    }

    /**
     * Creates a NullableString with a string value.
     */
    public static NullableString of(String value) {
        Objects.requireNonNull(value, "Use ofNull() for null values");
        return new NullableString(false, value);
    }

    /**
     * Creates a NullableString representing explicit null.
     */
    public static NullableString ofNull() {
        return NULL_VALUE;
    }

    /**
     * Convenience: emulateMediaType = "screen".
     */
    public static NullableString emulateScreen() {
        return of("screen");
    }

    /**
     * Convenience: emulateMediaType = "print".
     */
    public static NullableString emulatePrint() {
        return of("print");
    }

    /**
     * Convenience: emulateMediaType = null (reset to default).
     */
    public static NullableString emulateNone() {
        return ofNull();
    }

    public boolean isNull() {
        return isNull;
    }

    public boolean isPresent() {
        return !isNull && value != null;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NullableString)) return false;
        NullableString that = (NullableString) o;
        return isNull == that.isNull && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isNull, value);
    }

    @Override
    public String toString() {
        return isNull ? "null" : value;
    }

    static final class Serializer extends JsonSerializer<NullableString> {
        @Override
        public void serialize(NullableString value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value.isNull) {
                gen.writeNull();
            } else {
                gen.writeString(value.value);
            }
        }
    }

    static final class Deserializer extends JsonDeserializer<NullableString> {
        @Override
        public NullableString deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.currentToken() == JsonToken.VALUE_NULL) {
                return NullableString.ofNull();
            }
            return NullableString.of(p.getText());
        }

        @Override
        public NullableString getNullValue(DeserializationContext ctxt) {
            return NullableString.ofNull();
        }
    }
}
