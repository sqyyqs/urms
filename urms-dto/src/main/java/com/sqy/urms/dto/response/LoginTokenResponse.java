package com.sqy.urms.dto.response;


public record LoginTokenResponse(
        String token,
        String expiry
) {
}
