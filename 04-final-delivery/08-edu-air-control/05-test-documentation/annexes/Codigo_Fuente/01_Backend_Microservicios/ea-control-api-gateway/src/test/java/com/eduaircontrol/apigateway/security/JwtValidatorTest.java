package com.eduaircontrol.apigateway.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.KeyPair;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

class JwtValidatorTest {

    private static KeyPair keyPair;
    private static JwtValidator validator;

    @BeforeAll
    static void setUp() {
        keyPair = TestTokens.generateKeyPair();
        validator = new JwtValidator(TestTokens.provider(keyPair));
    }

    @Test
    void validatesTokenAndExtractsPrincipal() {
        String token = TestTokens.token(keyPair, "user-1", "jti-1",
                List.of("ADMIN", "USER"), Instant.now().plusSeconds(600));

        JwtPrincipal principal = validator.validate(token).block();

        assertThat(principal.userId()).isEqualTo("user-1");
        assertThat(principal.role()).isEqualTo("ADMIN");
        assertThat(principal.jti()).isEqualTo("jti-1");
        assertThat(principal.expiresAt()).isAfter(Instant.now());
    }

    @Test
    void takesFirstRoleAsPrimaryRole() {
        String token = TestTokens.token(keyPair, "user-2", "jti-2",
                List.of("VIEWER", "ADMIN"), Instant.now().plusSeconds(600));

        assertThat(validator.validate(token).block().role()).isEqualTo("VIEWER");
    }

    @Test
    void rejectsExpiredToken() {
        String token = TestTokens.token(keyPair, "user-3", "jti-3",
                List.of("USER"), Instant.now().minusSeconds(60));

        assertThatThrownBy(() -> validator.validate(token).block())
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void rejectsTokenWithUnknownKid() {
        JwtValidator other = new JwtValidator(kid -> Mono.error(
                new UnauthorizedException("INVALID_TOKEN", "unknown kid")));
        String token = TestTokens.token(keyPair, "user-4", "jti-4",
                List.of("USER"), Instant.now().plusSeconds(600));

        assertThatThrownBy(() -> other.validate(token).block())
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void rejectsMalformedHeader() {
        assertThatThrownBy(() -> JwtValidator.readKid("not-a-jwt"))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void rejectsTokenSignedWithAnotherKey() {
        KeyPair attacker = TestTokens.generateKeyPair();
        String token = TestTokens.token(attacker, "user-5", "jti-5",
                List.of("USER"), Instant.now().plusSeconds(600));

        assertThatThrownBy(() -> validator.validate(token).block())
                .isInstanceOf(UnauthorizedException.class);
    }
}
