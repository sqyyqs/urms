package com.sqy.urms.core.mapper;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sqy.urms.dto.token.Token;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.ParseException;
import java.util.UUID;

@Component
public class TokenMapper {

    private static final Logger logger = LoggerFactory.getLogger(TokenMapper.class);

    private final JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;
    private final JWSVerifier jwsVerifier;
    private final JWSSigner jwsSigner;

    public TokenMapper(JWSVerifier jwsVerifier, JWSSigner jwsSigner) {
        this.jwsVerifier = jwsVerifier;
        this.jwsSigner = jwsSigner;
    }

    @Nullable
    public Token fromJWT(String jwt) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            if (signedJWT.verify(jwsVerifier)) {
                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                return new Token(UUID.fromString(claimsSet.getJWTID()),
                        claimsSet.getSubject(),
                        claimsSet.getStringListClaim("authorities"),
                        claimsSet.getIssueTime().toInstant(),
                        claimsSet.getExpirationTime().toInstant()
                );
            }
        } catch (ParseException | JOSEException e) {
            logger.error("Invoke fromJWT({}) with exception.", jwt, e);
        }
        return null;
    }

    @Nullable
    public String toJWT(Token token) {
        JWSHeader header = new JWSHeader.Builder(jwsAlgorithm)
                .keyID(token.id().toString())
                .build();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.subject())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .claim("authorities", token.authorities())
                .build();
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        try {
            signedJWT.sign(jwsSigner);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            logger.error("Invoke toJWT({}) with exception.", token, e);
        }
        return null;
    }
}
