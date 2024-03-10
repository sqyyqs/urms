package com.sqy.urms.dto.loginout;


import java.util.Objects;

public record LoginTokenResponse(
        String token,
        String expiry
) {

    public LoginTokenResponse(String token, String expiry) {
        this.token = Objects.requireNonNull(token, "Token can't be null.");
        this.expiry = Objects.requireNonNull(expiry, "Expiry can't be null.");
    }
}
