package com.sqy.urms.core.util;

import org.springframework.util.StringUtils;

import javax.annotation.Nullable;

public final class JwtUtils {

    private JwtUtils() {
        throw new RuntimeException();
    }

    @Nullable
    public static String extractToken(@Nullable String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
