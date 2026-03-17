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
 * Represents a storage option that can be a boolean or a storage config reference {@code {"id": "storage-id"}}.
 */
@JsonSerialize(using = StorageOption.Serializer.class)
@JsonDeserialize(using = StorageOption.Deserializer.class)
public final class StorageOption {

    private final Boolean boolValue;
    private final String idValue;

    private StorageOption(Boolean boolValue, String idValue) {
        this.boolValue = boolValue;
        this.idValue = idValue;
    }

    public static StorageOption of(boolean value) {
        return new StorageOption(value, null);
    }

    public static StorageOption ofId(String id) {
        Objects.requireNonNull(id, "storage id must not be null");
        return new StorageOption(null, id);
    }

    public boolean isBooleanValue() {
        return boolValue != null;
    }

    public boolean isIdValue() {
        return idValue != null;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public String getIdValue() {
        return idValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StorageOption)) return false;
        StorageOption that = (StorageOption) o;
        return Objects.equals(boolValue, that.boolValue) && Objects.equals(idValue, that.idValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boolValue, idValue);
    }

    @Override
    public String toString() {
        return boolValue != null ? String.valueOf(boolValue) : "{\"id\":\"" + idValue + "\"}";
    }

    static final class Serializer extends JsonSerializer<StorageOption> {
        @Override
        public void serialize(StorageOption value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value.boolValue != null) {
                gen.writeBoolean(value.boolValue);
            } else {
                gen.writeStartObject();
                gen.writeStringField("id", value.idValue);
                gen.writeEndObject();
            }
        }
    }

    static final class Deserializer extends JsonDeserializer<StorageOption> {
        @Override
        public StorageOption deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.currentToken() == JsonToken.VALUE_TRUE || p.currentToken() == JsonToken.VALUE_FALSE) {
                return StorageOption.of(p.getBooleanValue());
            }
            if (p.currentToken() == JsonToken.START_OBJECT) {
                String id = null;
                while (p.nextToken() != JsonToken.END_OBJECT) {
                    if ("id".equals(p.currentName())) {
                        p.nextToken();
                        id = p.getText();
                    }
                }
                if (id == null) {
                    throw ctxt.weirdStringException("", StorageOption.class, "Missing 'id' field in storage object");
                }
                return StorageOption.ofId(id);
            }
            throw ctxt.wrongTokenException(p, StorageOption.class,
                    p.currentToken(), "Expected boolean or object for StorageOption");
        }
    }
}
