package com.eduaircontrol.apigateway.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

class RemoteJwksProviderTest {

    private static String jwksJson(RSAPublicKey key) {
        String n = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(unsigned(key.getModulus()));
        String e = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(unsigned(key.getPublicExponent()));
        return "{\"keys\":[{\"kty\":\"RSA\",\"kid\":\"k1\",\"alg\":\"RS256\",\"n\":\"" + n
                + "\",\"e\":\"" + e + "\"}]}";
    }

    private static byte[] unsigned(BigInteger value) {
        byte[] bytes = value.toByteArray();
        if (bytes.length > 1 && bytes[0] == 0) {
            byte[] trimmed = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, trimmed, 0, trimmed.length);
            return trimmed;
        }
        return bytes;
    }

    private RemoteJwksProvider provider(KeyPair keyPair, AtomicInteger calls) {
        WebClient webClient = WebClient.builder()
                .exchangeFunction(request -> {
                    calls.incrementAndGet();
                    return Mono.just(ClientResponse.create(HttpStatus.OK)
                            .header("Content-Type", "application/json")
                            .body(jwksJson((RSAPublicKey) keyPair.getPublic()))
                            .build());
                })
                .build();
        return new RemoteJwksProvider(webClient, "http://auth/jwks");
    }

    @Test
    void resolvesPublicKeyFromJwks() {
        KeyPair keyPair = TestTokens.generateKeyPair();
        RemoteJwksProvider provider = provider(keyPair, new AtomicInteger());

        RSAPublicKey key = provider.key("k1").block();

        assertThat(key).isNotNull();
        assertThat(key.getModulus()).isEqualTo(((RSAPublicKey) keyPair.getPublic()).getModulus());
    }

    @Test
    void cachesKeysBetweenCalls() {
        KeyPair keyPair = TestTokens.generateKeyPair();
        AtomicInteger calls = new AtomicInteger();
        RemoteJwksProvider provider = provider(keyPair, calls);

        provider.key("k1").block();
        provider.key("k1").block();

        assertThat(calls.get()).isEqualTo(1);
    }

    @Test
    void rejectsUnknownKid() {
        KeyPair keyPair = TestTokens.generateKeyPair();
        RemoteJwksProvider provider = provider(keyPair, new AtomicInteger());

        assertThatThrownBy(() -> provider.key("missing").block())
                .isInstanceOf(UnauthorizedException.class);
    }
}
