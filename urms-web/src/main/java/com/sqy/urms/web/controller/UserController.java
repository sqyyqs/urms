package com.sqy.urms.web.controller;

import com.sqy.urms.core.service.UserService;
import com.sqy.urms.dto.loginout.LoginRequest;
import com.sqy.urms.dto.loginout.LoginTokenResponse;
import com.sqy.urms.dto.user.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAll() {
        logger.info("Invoke getAll().");
        return userService.getAll();
    }

    @PostMapping("/opp")
    public ResponseEntity<UserDto> makeOperator(@RequestParam("id") long id) {
        logger.info("Invoke makeOperator({}).", id);
        return userService.makeOpp(id);
    }

}
