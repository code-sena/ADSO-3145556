package com.eduaircontrol.mssecurity.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Emisión y verificación de access tokens JWT con RS256 (ADR-006 / decisions.md
 * del servicio). La clave pública se publica vía JWKS para que el api-gateway y
 * los demás servicios validen sin llamar a este servicio. Las claves dev están
 * en resources/keys; en producción se sustituyen por variables de entorno.
 */
@Service
public class JwtService {

    private static final String KEY_ID_HINT = "security-rsa";

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;
    private final String kid;
    private final long ttlSeconds;

    public JwtService(
            @Value("${security.rsa.private-key-path:keys/dev-private.pem}") String privateKeyPath,
            @Value("${security.rsa.public-key-path:keys/dev-public.pem}") String publicKeyPath,
            @Value("${jwt.access-token-ttl-seconds:3600}") long ttlSeconds) {
        this.privateKey = (RSAPrivateKey) readPrivateKey(privateKeyPath);
        this.publicKey = (RSAPublicKey) readPublicKey(publicKeyPath);
        this.kid = computeKid(publicKey);
        this.ttlSeconds = ttlSeconds;
    }

    public String generateAccessToken(java.util.UUID id, String email, String username, List<String> roles,
            java.util.UUID institutionId, java.util.UUID campusId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(id.toString())
                .setId(java.util.UUID.randomUUID().toString())
                .claim("email", email)
                .claim("username", username)
                .claim("roles", roles)
                .claim("institutionId", institutionId == null ? null : institutionId.toString())
                .claim("campusId", campusId == null ? null : campusId.toString())
                .claim("permissions", List.of())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(ttlSeconds)))
                .setHeaderParam("kid", kid)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * Token de ingesta para un dispositivo (ESP32).
     *
     * <p>Es un token RS256 normal firmado con la misma clave, de modo que cualquier
     * servicio puede validarlo contra el JWKS sin codigo nuevo. Lo que lo hace de
     * dispositivo es el claim {@code roles:["DEVICE"]}: el firmware deja de forjar
     * {@code X-User-Role: ADMIN} y pasa a autenticarse de verdad.
     *
     * @param subject identidad del dispositivo (el {@code sensorId} que lo representa)
     * @param ttlSeconds duracion; los dispositivos no refrescan, asi que es larga
     */
    public String generateDeviceToken(UUID subject, long ttlSeconds) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(subject.toString())
                .setId(UUID.randomUUID().toString())
                .claim("roles", List.of(DEVICE_ROLE))
                .claim("permissions", List.of(INGEST_PERMISSION))
                .claim("tokenType", "device")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(ttlSeconds)))
                .setHeaderParam("kid", kid)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /** Rol que distingue a un token de dispositivo de uno de usuario. */
    public static final String DEVICE_ROLE = "DEVICE";

    /** Permiso que se concede al token de dispositivo: solo puede enviar medidas. */
    public static final String INGEST_PERMISSION = "ingest";

    public Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long expiresInSeconds() {
        return ttlSeconds;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public String getKid() {
        return kid;
    }

    public Map<String, Object> jwks() {
        Map<String, Object> key = new LinkedHashMap<>();
        key.put("kty", "RSA");
        key.put("use", "sig");
        key.put("alg", "RS256");
        key.put("kid", kid);
        key.put("n", Encoders.BASE64URL.encode(unsignedBytes(publicKey.getModulus())));
        key.put("e", Encoders.BASE64URL.encode(publicKey.getPublicExponent().toByteArray()));
        return Map.of("keys", List.of(key));
    }

    private static byte[] unsignedBytes(BigInteger value) {
        byte[] bytes = value.toByteArray();
        if (bytes.length > 1 && bytes[0] == 0) {
            byte[] trimmed = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, trimmed, 0, trimmed.length);
            return trimmed;
        }
        return bytes;
    }

    private static String computeKid(PublicKey key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(key.getEncoded());
            StringBuilder sb = new StringBuilder(KEY_ID_HINT).append("-");
            for (int i = 0; i < 4; i++) {
                sb.append(String.format("%02x", hash[i]));
            }
            return sb.toString();
        } catch (Exception e) {
            return KEY_ID_HINT;
        }
    }

    private static PrivateKey readPrivateKey(String path) {
        try (InputStream in = open(path)) {
            byte[] der = decodePem(in.readAllBytes());
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(der));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load RSA private key from " + path, e);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid RSA private key at " + path, e);
        }
    }

    private static PublicKey readPublicKey(String path) {
        try (InputStream in = open(path)) {
            byte[] der = decodePem(in.readAllBytes());
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(der));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load RSA public key from " + path, e);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid RSA public key at " + path, e);
        }
    }

    private static InputStream open(String path) throws IOException {
        java.nio.file.Path file = java.nio.file.Paths.get(path);
        if (java.nio.file.Files.isReadable(file)) {
            return java.nio.file.Files.newInputStream(file);
        }
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (in == null) {
            in = JwtService.class.getClassLoader().getResourceAsStream(path);
        }
        if (in == null) {
            throw new IllegalStateException("Resource not found: " + path);
        }
        return in;
    }

    private static byte[] decodePem(byte[] pem) {
        String text = new String(pem, StandardCharsets.US_ASCII)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        return Base64.getDecoder().decode(text);
    }
}
