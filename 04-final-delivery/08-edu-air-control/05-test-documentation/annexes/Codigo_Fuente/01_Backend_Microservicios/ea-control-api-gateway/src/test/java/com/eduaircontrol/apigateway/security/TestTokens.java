package com.eduaircontrol.apigateway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public final class TestTokens {

    public static final String KID = "test-kid";

    private TestTokens() {
    }

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String token(KeyPair keyPair, String subject, String jti, List<String> roles, Instant expiration) {
        return Jwts.builder()
                .setSubject(subject)
                .setId(jti)
                .claim("roles", roles)
                .claim("institutionId", "inst-test")
                .claim("campusId", "campus-test")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiration))
                .setHeaderParam("kid", KID)
                .signWith((RSAPrivateKey) keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    public static JwksProvider provider(KeyPair keyPair) {
        return kid -> KID.equals(kid)
                ? reactor.core.publisher.Mono.just((RSAPublicKey) keyPair.getPublic())
                : reactor.core.publisher.Mono.error(new UnauthorizedException("INVALID_TOKEN", "unknown kid"));
    }
}
