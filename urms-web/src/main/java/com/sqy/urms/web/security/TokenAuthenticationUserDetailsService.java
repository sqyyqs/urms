package com.sqy.urms.web.security;

import com.sqy.urms.dto.token.Token;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.time.Instant;

public class TokenAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken) throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof Token token) {
            return new TokenUser(
                    token.subject(),
                    "",
                    true,
                    true,
                    token.expiresAt().isAfter(Instant.now()),
                    true,
                    token.authorities().stream().map(authority -> new SimpleGrantedAuthority("ROLE_" + authority)).toList(),
                    token
            );
        }
        throw new UsernameNotFoundException("Principle must be of type Token");
    }
}
