package com.sqy.urms.core.util;

import jakarta.annotation.Nullable;
import org.springframework.util.StringUtils;

public final class JwtUtils {

    private JwtUtils() {
        throw new RuntimeException();
    }

    @Nullable
    public static String extractToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
