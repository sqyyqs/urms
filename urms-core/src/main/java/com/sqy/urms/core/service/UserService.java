package com.sqy.urms.core.service;

import com.sqy.urms.dto.request.LoginRequest;
import com.sqy.urms.dto.response.LoginTokenResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<LoginTokenResponse> login(LoginRequest loginRequest);

    boolean blacklistToken(String authorizationHeader);

    boolean isTokenBlacklisted(String jwtToken);
}

