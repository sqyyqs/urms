package com.sqy.urms.dto.user;

import java.util.Objects;

public record UserDto(
        long id,
        String login,
        String name
) {
    public UserDto(long id, String login, String name) {
        this.id = id;
        this.login = Objects.requireNonNull(login, "Login can't be null.");
        this.name = Objects.requireNonNull(name, "Name can't be null.");
    }
}
