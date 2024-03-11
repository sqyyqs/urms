package com.sqy.urms.web.configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.sqy.urms.web.security.JwtAuthenticationConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.text.ParseException;

import static com.sqy.urms.persistence.model.UserRole.ADMINISTRATOR;
import static com.sqy.urms.persistence.model.UserRole.OPERATOR;
import static com.sqy.urms.persistence.model.UserRole.USER;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
@ComponentScan(basePackages = "com.sqy.urms.web.security")
public class SecurityConfiguration {

    @Value("${jwt.token-key}")
    private String tokenKey;

    @Bean
    public JWSSigner jwsSigner() throws ParseException, KeyLengthException {
        return new MACSigner(OctetSequenceKey.parse(tokenKey));
    }

    @Bean
    public JWSVerifier jwsVerifier() throws ParseException, JOSEException {
        return new MACVerifier(OctetSequenceKey.parse(tokenKey));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(JwtAuthenticationConfigurer jwtAuthenticationConfigurer, HttpSecurity http) throws Exception {
        http.apply(jwtAuthenticationConfigurer);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/api/user/", "/api/user/opp").hasRole(ADMINISTRATOR.name())
                                .requestMatchers("/api/request/updateStatus").hasAnyRole(USER.name(), OPERATOR.name())
                                .requestMatchers("/api/request/create", "/api/request/update").hasRole(USER.name())
                                .requestMatchers(HttpMethod.GET, "/api/request/{id}").hasRole(OPERATOR.name())
                                .requestMatchers("/api/request/search", "/api/user/logout").authenticated()
                                .requestMatchers("/api/user/login").permitAll()
                                .anyRequest().authenticated())
                .build();
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }
}
