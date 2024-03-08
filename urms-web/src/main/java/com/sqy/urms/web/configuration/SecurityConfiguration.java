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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.text.ParseException;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
@ComponentScan(basePackages = "com.sqy.urms.web.security")
public class SecurityConfiguration {

    @Value("${jwt.token-key}")
    private String tokenKey;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWSSigner jwsSigner() throws ParseException, KeyLengthException {
        return new MACSigner(OctetSequenceKey.parse(tokenKey));
    }

    @Bean
    public JWSVerifier jwsVerifier() throws ParseException, JOSEException {
        return new MACVerifier(OctetSequenceKey.parse(tokenKey));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            JwtAuthenticationConfigurer jwtAuthenticationConfigurer,
            HttpSecurity http) throws Exception {
        http.apply(jwtAuthenticationConfigurer);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/api/user/**").permitAll()
                                .anyRequest().authenticated())
                .build();
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }
}
