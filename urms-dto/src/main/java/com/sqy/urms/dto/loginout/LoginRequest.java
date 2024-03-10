package com.sqy.urms.dto.loginout;

import java.util.Objects;

public record LoginRequest(
        String login,
        String password
) {

    public LoginRequest(String login, String password) {
        this.login = Objects.requireNonNull(login, "Login can't be null.");
        this.password = Objects.requireNonNull(password, "Password can't be null.");
    }
}
