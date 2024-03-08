package com.sqy.urms.web.controller;

import com.sqy.urms.core.service.UserService;
import com.sqy.urms.dto.request.LoginRequest;
import com.sqy.urms.dto.response.LoginTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginTokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        return userService.blacklistToken(authorizationHeader) ?
                ResponseEntity.ok("Logout successful") : ResponseEntity.ok("Logout unsuccessful");
    }

}
