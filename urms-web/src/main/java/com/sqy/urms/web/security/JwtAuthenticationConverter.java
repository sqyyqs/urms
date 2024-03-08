package com.sqy.urms.web.security;

import com.sqy.urms.core.mapper.TokenMapper;
import com.sqy.urms.core.service.UserService;
import com.sqy.urms.core.util.JwtUtils;
import com.sqy.urms.dto.token.Token;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationConverter implements AuthenticationConverter {

    private final TokenMapper tokenMapper;
    private final UserService userService;

    public JwtAuthenticationConverter(TokenMapper tokenMapper, UserService userService) {
        this.tokenMapper = tokenMapper;
        this.userService = userService;
    }

    @Override
    @Nullable
    public Authentication convert(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtToken = JwtUtils.extractToken(authorizationHeader);
        if (jwtToken == null) {
            return null;
        }

        Token accessToken = tokenMapper.fromJWT(jwtToken);
        if (accessToken == null) {
            return null;
        }

        if (userService.isTokenBlacklisted(jwtToken)) {
            return null;
        }

        return new PreAuthenticatedAuthenticationToken(accessToken, jwtToken);
    }
}
