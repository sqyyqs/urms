package com.sqy.urms.core.service;

import com.sqy.urms.dto.loginout.LoginRequest;
import com.sqy.urms.dto.loginout.LoginTokenResponse;
import com.sqy.urms.dto.user.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<LoginTokenResponse> login(LoginRequest loginRequest);

    boolean blacklistToken(String authorizationHeader);

    boolean isTokenBlacklisted(String jwtToken);

    ResponseEntity<List<UserDto>> getAll();

}

