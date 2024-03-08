package com.sqy.urms.web.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private final JwtAuthenticationConverter converter;

    public JwtAuthenticationConfigurer(JwtAuthenticationConverter converter) {
        this.converter = converter;
    }

    @Override
    public void configure(HttpSecurity builder) {
        AuthenticationFilter jwtAuthenticationFilter = new AuthenticationFilter(builder.getSharedObject(AuthenticationManager.class), converter);

        jwtAuthenticationFilter.setSuccessHandler(((request, response, authentication) -> {}));
        jwtAuthenticationFilter.setFailureHandler(((request, response, exception) -> response.sendError(HttpServletResponse.SC_FORBIDDEN)));

        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(new TokenAuthenticationUserDetailsService());

        builder.addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class)
                .authenticationProvider(authenticationProvider);
    }
}
