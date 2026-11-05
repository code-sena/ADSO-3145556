package com.eduaircontrol.msusermanagement.shared.security;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * Resuelve la clave pública RSA con la que está firmado un access token (ADR-006).
 *
 * <p>Dos modos:
 * <ul>
 *   <li><b>Producción</b>: {@code security.jwks-uri} apunta al JWKS de ms-security
 *       ({@code GET /api/v1/auth/jwks}). El resultado se cachea y, si llega un
 *       {@code kid} desconocido, se refresca una vez: así se soporta la rotación
 *       de claves sin reiniciar el servicio.</li>
 *   <li><b>Tests / entornos sin red</b>: {@code security.rsa.public-key-path} carga
 *       una clave PEM fija y no hace ninguna llamada.</li>
 * </ul>
 *
 * <p>Con clave fija no se valida el {@code kid}: hay una sola clave y el riesgo de
 * sustitución no existe fuera de un entorno controlado.
 */
@Slf4j
@Component
public class JwksKeyResolver {

    private final RestClient restClient;
    private final String jwksUri;
    private final String publicKeyPath;
    private final AtomicReference<Map<String, RSAPublicKey>> cache =
            new AtomicReference<>(Map.of());

    public JwksKeyResolver(
            @Value("${security.jwks-uri:}") String jwksUri,
            @Value("${security.rsa.public-key-path:}") String publicKeyPath) {
        this.jwksUri = jwksUri;
        this.publicKeyPath = publicKeyPath;
        this.restClient = RestClient.create();
    }

    /**
     * @throws IllegalStateException si no hay forma de resolver la clave: el token no
     *         se puede validar y hay que rechazarlo.
     */
    public RSAPublicKey resolve(String kid) {
        if (publicKeyPath != null && !publicKeyPath.isBlank()) {
            return staticKey();
        }
        if (jwksUri == null || jwksUri.isBlank()) {
            throw new IllegalStateException(
                    "Sin fuente de claves: configure security.jwks-uri o security.rsa.public-key-path");
        }
        RSAPublicKey cached = cache.get().get(kid);
        if (cached != null) {
            return cached;
        }
        // Kid desconocido: puede ser una clave recién rotada. Un refresco y nada más,
        // para que un token con un kid inventado no provoque un bucle de consultas.
        RSAPublicKey resolved = refresh().get(kid);
        if (resolved == null) {
            throw new IllegalStateException(
                    "No hay clave pública para el kid '" + kid + "' en " + jwksUri);
        }
        return resolved;
    }

    private RSAPublicKey staticKey() {
        try (java.io.InputStream in = open(publicKeyPath)) {
            String pem = new String(in.readAllBytes(), StandardCharsets.US_ASCII);
            String encoded = pem
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] der = Base64.getDecoder().decode(encoded);
            return (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(der));
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo leer la clave pública: " + publicKeyPath, e);
        }
    }

    /**
     * Primero el classpath (asi es como empaqueta ms-security sus claves), y si no,
     * un fichero del disco. Mismo criterio que {@code ms-security.JwtService}.
     */
    private static java.io.InputStream open(String path) throws java.io.IOException {
        java.io.InputStream in =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (in == null) {
            in = JwksKeyResolver.class.getClassLoader().getResourceAsStream(path);
        }
        if (in == null) {
            in = Files.newInputStream(Path.of(path));
        }
        return in;
    }

    private Map<String, RSAPublicKey> refresh() {
        try {
            Map<String, Object> body = restClient.get()
                    .uri(jwksUri)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            Map<String, RSAPublicKey> keys = parse(body);
            cache.set(keys);
            return keys;
        } catch (RuntimeException e) {
            log.warn("No se pudo obtener el JWKS de {}: {}", jwksUri, e.getMessage());
            return cache.get();
        }
    }

    private Map<String, RSAPublicKey> parse(Map<?, ?> body) {
        Object keys = body == null ? null : body.get("keys");
        if (!(keys instanceof List<?> list)) {
            return Map.of();
        }
        Map<String, RSAPublicKey> result = new HashMap<>();
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
