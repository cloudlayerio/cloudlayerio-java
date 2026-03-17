package io.cloudlayer.sdk.model.options;

import java.util.Objects;

/**
 * Query parameters for the list templates endpoint.
 * Note: {@code tags} is a comma-separated string in query params but an array in responses.
 */
public final class ListTemplatesOptions {

    private final String type;
    private final String category;
    private final String tags;
    private final Boolean expand;

    private ListTemplatesOptions(Builder builder) {
        this.type = builder.type;
        this.category = builder.category;
        this.tags = builder.tags;
        this.expand = builder.expand;
    }

    public static Builder builder() { return new Builder(); }

    public String getType() { return type; }
    public String getCategory() { return category; }
    public String getTags() { return tags; }
    public Boolean getExpand() { return expand; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListTemplatesOptions)) return false;
        ListTemplatesOptions l = (ListTemplatesOptions) o;
        return Objects.equals(type, l.type) && Objects.equals(category, l.category)
                && Objects.equals(tags, l.tags) && Objects.equals(expand, l.expand);
    }

    @Override
    public int hashCode() { return Objects.hash(type, category, tags, expand); }

    public static final class Builder {
        private String type;
        private String category;
        private String tags;
        private Boolean expand;

        private Builder() {}

        public Builder type(String type) { this.type = type; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder tags(String tags) { this.tags = tags; return this; }
        public Builder expand(boolean expand) { this.expand = expand; return this; }

        public ListTemplatesOptions build() { return new ListTemplatesOptions(this); }
    }
}
