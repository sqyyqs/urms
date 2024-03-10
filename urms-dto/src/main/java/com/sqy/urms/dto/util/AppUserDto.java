package com.sqy.urms.dto.util;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record AppUserDto(String currentUsername, List<String> roles) {
    public AppUserDto(String currentUsername, List<String> roles) {
        this.currentUsername = Objects.requireNonNull(currentUsername, "Current username can't be null.");
        this.roles = Objects.requireNonNullElse(roles, Collections.emptyList());
    }
}
