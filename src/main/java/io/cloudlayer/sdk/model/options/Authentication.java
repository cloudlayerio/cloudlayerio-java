package io.cloudlayer.sdk.model.options;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Authentication {

    @JsonProperty("username")
    private final String username;

    @JsonProperty("password")
    private final String password;

    private Authentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @SuppressWarnings("unused")
    private Authentication() {
        this.username = null;
        this.password = null;
    }

    public static Authentication of(String username, String password) {
        return new Authentication(username, password);
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Authentication)) return false;
        Authentication a = (Authentication) o;
        return Objects.equals(username, a.username) && Objects.equals(password, a.password);
    }

    @Override
    public int hashCode() { return Objects.hash(username, password); }
}
