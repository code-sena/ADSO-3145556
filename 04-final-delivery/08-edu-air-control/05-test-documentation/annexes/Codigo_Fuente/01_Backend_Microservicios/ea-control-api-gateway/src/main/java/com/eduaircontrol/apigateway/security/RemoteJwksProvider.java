package com.eduaircontrol.apigateway.security;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Obtiene y cachea las claves públicas del endpoint JWKS de ms-security
 * ({@code GET /api/v1/auth/jwks}). Si un token llega con un {@code kid}
 * desconocido se refresca la caché una vez (soporta rotación de claves).
 */
@Slf4j
public class RemoteJwksProvider implements JwksProvider {

    private final WebClient webClient;
    private final String jwksUri;
    private final AtomicReference<Map<String, RSAPublicKey>> cache = new AtomicReference<>(Map.of());

    public RemoteJwksProvider(WebClient webClient, String jwksUri) {
        this.webClient = webClient;
        this.jwksUri = jwksUri;
    }

    @Override
    public Mono<RSAPublicKey> key(String kid) {
        RSAPublicKey cached = cache.get().get(kid);
        if (cached != null) {
            return Mono.just(cached);
        }
        return refresh()
                .flatMap(keys -> Mono.justOrEmpty(keys.get(kid)))
                .switchIfEmpty(Mono.error(new UnauthorizedException(
                        "INVALID_TOKEN", "Token signed with an unknown key")));
    }

    private Mono<Map<String, RSAPublicKey>> refresh() {
        return webClient.get()
                .uri(jwksUri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .doOnNext(body -> log.debug("JWKS raw response: {}", body))
                .map(this::parse)
                .doOnNext(keys -> {
                    log.debug("JWKS cache updated with {} keys: {}", keys.size(), keys.keySet());
                    cache.set(keys);
                })
                .doOnError(error -> log.warn("No se pudo obtener JWKS de {}: {}", jwksUri, error.getMessage()))
                .onErrorResume(error -> Mono.just(cache.get()));
    }

    @SuppressWarnings("unchecked")
    private Map<String, RSAPublicKey> parse(Map<?, ?> body) {
        Object keys = body.get("keys");
        if (!(keys instanceof List<?> list)) {
            return Map.of();
        }
        Map<String, RSAPublicKey> result = new java.util.HashMap<>();
        for (Object entry : list) {
            if (entry instanceof Map<?, ?> key) {
                String kid = String.valueOf(key.get("kid"));
                String n = (String) key.get("n");
                String e = (String) key.get("e");
                if (kid != null && n != null && e != null) {
                    result.put(kid, rsaKey(n, e));
                }
            }
        }
        return result;
    }

    private RSAPublicKey rsaKey(String modulus, String exponent) {
        try {
            BigInteger n = new BigInteger(1, Base64.getUrlDecoder().decode(modulus));
            BigInteger e = new BigInteger(1, Base64.getUrlDecoder().decode(exponent));
            return (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(new RSAPublicKeySpec(n, e));
        } catch (Exception ex) {
            throw new IllegalStateException("Clave JWKS inválida", ex);
        }
    }
}
