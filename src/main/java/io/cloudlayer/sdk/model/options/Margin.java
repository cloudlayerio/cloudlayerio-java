package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Margin {

    @JsonProperty("top")
    private final LayoutDimension top;

    @JsonProperty("bottom")
    private final LayoutDimension bottom;

    @JsonProperty("left")
    private final LayoutDimension left;

    @JsonProperty("right")
    private final LayoutDimension right;

    private Margin(Builder builder) {
        this.top = builder.top;
        this.bottom = builder.bottom;
        this.left = builder.left;
        this.right = builder.right;
    }

    @SuppressWarnings("unused")
    private Margin() {
        this.top = null;
        this.bottom = null;
        this.left = null;
        this.right = null;
    }

    public static Builder builder() { return new Builder(); }

    public LayoutDimension getTop() { return top; }
    public LayoutDimension getBottom() { return bottom; }
    public LayoutDimension getLeft() { return left; }
    public LayoutDimension getRight() { return right; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Margin)) return false;
        Margin m = (Margin) o;
        return Objects.equals(top, m.top) && Objects.equals(bottom, m.bottom)
                && Objects.equals(left, m.left) && Objects.equals(right, m.right);
    }

    @Override
    public int hashCode() { return Objects.hash(top, bottom, left, right); }

    public static final class Builder {
        private LayoutDimension top;
        private LayoutDimension bottom;
        private LayoutDimension left;
        private LayoutDimension right;

        private Builder() {}

        public Builder top(LayoutDimension top) { this.top = top; return this; }
        public Builder top(String css) { this.top = LayoutDimension.of(css); return this; }
        public Builder top(int px) { this.top = LayoutDimension.of(px); return this; }
        public Builder bottom(LayoutDimension bottom) { this.bottom = bottom; return this; }
        public Builder bottom(String css) { this.bottom = LayoutDimension.of(css); return this; }
        public Builder bottom(int px) { this.bottom = LayoutDimension.of(px); return this; }
        public Builder left(LayoutDimension left) { this.left = left; return this; }
        public Builder left(String css) { this.left = LayoutDimension.of(css); return this; }
        public Builder left(int px) { this.left = LayoutDimension.of(px); return this; }
        public Builder right(LayoutDimension right) { this.right = right; return this; }
        public Builder right(String css) { this.right = LayoutDimension.of(css); return this; }
        public Builder right(int px) { this.right = LayoutDimension.of(px); return this; }

        public Margin build() { return new Margin(this); }
    }
}
