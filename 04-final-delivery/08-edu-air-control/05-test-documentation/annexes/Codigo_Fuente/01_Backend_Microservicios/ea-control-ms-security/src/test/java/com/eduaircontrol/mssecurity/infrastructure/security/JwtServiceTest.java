package com.eduaircontrol.mssecurity.infrastructure.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private static final String PRIVATE = "target/test-keys/test-private.pem";
    private static final String PUBLIC = "target/test-keys/test-public.pem";

    private JwtService service(long ttl) {
        return new JwtService(PRIVATE, PUBLIC, ttl);
    }

    @Test
    void generatesTokenParsableWithPublicKeyAndClaims() {
        UUID id = UUID.randomUUID();
        JwtService jwt = service(3600);

        String token = jwt.generateAccessToken(id, "a@b.com", "alice", List.of("USER", "ADMIN"), null, null);
        Claims claims = jwt.parse(token);

        assertThat(claims.getSubject()).isEqualTo(id.toString());
        assertThat(claims.get("email", String.class)).isEqualTo("a@b.com");
        assertThat(claims.get("username", String.class)).isEqualTo("alice");
        assertThat(claims.get("roles", List.class)).containsExactly("USER", "ADMIN");
        assertThat(claims.get("permissions", List.class)).isEmpty();
    }

    @Test
    void tokenHeaderCarriesKidMatchingJwks() {
        JwtService jwt = service(3600);
        String token = jwt.generateAccessToken(UUID.randomUUID(), "a@b.com", "alice", List.of("USER"), null, null);

        @SuppressWarnings("unchecked")
        Map<String, Object> key = (Map<String, Object>) ((List<?>) jwt.jwks().get("keys")).get(0);
        String kid = (String) key.get("kid");
        String header = new String(java.util.Base64.getUrlDecoder().decode(token.split("\\.")[0]));

        assertThat(kid).startsWith("security-rsa-");
        assertThat(header).contains("\"kid\":\"" + kid + "\"");
    }

    @Test
    void expiryIsApproximatelyConfiguredTtl() {
        JwtService jwt = service(120);
        Claims claims = jwt.parse(jwt.generateAccessToken(UUID.randomUUID(), "a@b.com", "a", List.of(), null, null));

        long seconds = (claims.getExpiration().getTime() - claims.getIssuedAt().getTime()) / 1000;
        assertThat(seconds).isEqualTo(120);
    }

    @Test
    void alreadyExpiredTokenIsRejected() {
        JwtService jwt = service(-10);
        String token = jwt.generateAccessToken(UUID.randomUUID(), "a@b.com", "a", List.of(), null, null);

        assertThatThrownBy(() -> jwt.parse(token)).isInstanceOf(JwtException.class);
    }

    @Test
    void tamperedTokenIsRejected() {
        JwtService jwt = service(3600);
        String token = jwt.generateAccessToken(UUID.randomUUID(), "a@b.com", "a", List.of(), null, null);
        int signatureStart = token.lastIndexOf('.') + 1;
        char original = token.charAt(signatureStart);
        String tampered = token.substring(0, signatureStart)
                + (original == 'A' ? 'B' : 'A')
                + token.substring(signatureStart + 1);

        assertThatThrownBy(() -> jwt.parse(tampered)).isInstanceOf(JwtException.class);
    }

    @Test
    void jwksExposesRsaModulusAndExponent() {
        JwtService jwt = service(3600);
        @SuppressWarnings("unchecked")
        Map<String, Object> key = (Map<String, Object>) ((List<?>) jwt.jwks().get("keys")).get(0);

        assertThat(key.get("kty")).isEqualTo("RSA");
        assertThat(key.get("use")).isEqualTo("sig");
        assertThat(key.get("alg")).isEqualTo("RS256");
        assertThat(key.get("n")).isInstanceOf(String.class);
        assertThat(key.get("e")).isEqualTo("AQAB");
    }

    @Test
    void expiresInSecondsReflectsConfiguration() {
        assertThat(service(900).expiresInSeconds()).isEqualTo(900);
    }
}
