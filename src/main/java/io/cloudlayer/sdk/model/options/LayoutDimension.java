package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a layout dimension that can be either a CSS string ("100px") or an integer (100).
 */
@JsonSerialize(using = LayoutDimension.Serializer.class)
@JsonDeserialize(using = LayoutDimension.Deserializer.class)
public final class LayoutDimension {

    private final String cssValue;
    private final Integer pixelValue;

    private LayoutDimension(String cssValue, Integer pixelValue) {
        this.cssValue = cssValue;
        this.pixelValue = pixelValue;
    }

    public static LayoutDimension of(String cssValue) {
        Objects.requireNonNull(cssValue, "cssValue must not be null");
        return new LayoutDimension(cssValue, null);
    }

    public static LayoutDimension of(int pixelValue) {
        return new LayoutDimension(null, pixelValue);
    }

    public boolean isCssValue() {
        return cssValue != null;
    }

    public boolean isPixelValue() {
        return pixelValue != null;
    }

    public String getCssValue() {
        return cssValue;
    }

    public Integer getPixelValue() {
        return pixelValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LayoutDimension)) return false;
        LayoutDimension that = (LayoutDimension) o;
        return Objects.equals(cssValue, that.cssValue) && Objects.equals(pixelValue, that.pixelValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cssValue, pixelValue);
    }

    @Override
    public String toString() {
        return cssValue != null ? cssValue : String.valueOf(pixelValue);
    }

    static final class Serializer extends JsonSerializer<LayoutDimension> {
        @Override
        public void serialize(LayoutDimension value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value.cssValue != null) {
                gen.writeString(value.cssValue);
            } else {
                gen.writeNumber(value.pixelValue);
            }
        }
    }

    static final class Deserializer extends JsonDeserializer<LayoutDimension> {
        @Override
        public LayoutDimension deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            switch (p.currentToken()) {
                case VALUE_STRING:
                    return LayoutDimension.of(p.getText());
                case VALUE_NUMBER_INT:
                    return LayoutDimension.of(p.getIntValue());
                default:
                    throw ctxt.wrongTokenException(p, LayoutDimension.class,
                            p.currentToken(), "Expected string or integer for LayoutDimension");
            }
        }
    }
}
