package com.eduaircontrol.msusermanagement.shared.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Emisor de access tokens RS256 para tests.
 *
 * <p>Genera un par de claves RSA <b>en memoria al arrancar</b> y solo publica la
 * clave publica en {@code target/test-keys/}, que es donde la lee
 * {@link JwksKeyResolver} en el perfil de test. Asi el codigo de validacion real
 * se ejercita de punta a punta sin depender de ms-security ni de red, y <b>ninguna
 * clave privada se versiona</b>.
 *
 * <p>La forma de los claims replica la de ms-security: {@code sub}, {@code userId},
 * {@code email}, {@code username} y {@code roles} como lista.
 */
@org.springframework.stereotype.Component
public class TestTokenMint {

    /** Donde el perfil de test busca la clave publica (ver application-test.properties). */
    public static final String PUBLIC_KEY_PATH = "target/test-keys/test-public.pem";

    private static final Path PUBLIC_KEY_FILE = Path.of(PUBLIC_KEY_PATH);

    private static final RSAPrivateKey PRIVATE_KEY;

    static {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair pair = generator.generateKeyPair();
            PRIVATE_KEY = (RSAPrivateKey) pair.getPrivate();
            writePublicKey((RSAPublicKey) pair.getPublic());
        } catch (Exception e) {
            throw new ExceptionInInitializerError(
                    new IllegalStateException("No se pudo generar el par de claves de test", e));
        }
    }

    /**
     * Firma un token con la clave de test.
     *
     * <p>Sin necesidad de un bean: para tests que construian el token a mano.
     */
    public static String mint(UUID userId, String email, String role) {
        return mint(userId, email, List.of(role));
    }

    public static String mint(UUID userId, String email, List<String> roles) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userId == null ? email : userId.toString())
                .setId(UUID.randomUUID().toString())
                .claim("email", email)
                .claim("username", email)
                .claim("userId", userId == null ? null : userId.toString())
                .claim("roles", roles)
                .claim("role", roles.isEmpty() ? null : roles.get(0))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 3_600_000L))
                .signWith(PRIVATE_KEY, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * Mismo contrato que tenia el JwtService interino HS256: lo usan los tests que
     * inyectan un emisor de tokens.
     */
    public String generateToken(String subject, String role) {
        return mint(UUID.randomUUID(), subject, role);
    }

    public String generateToken(String subject, String role, List<String> roles) {
        return mint(UUID.randomUUID(), subject, roles);
    }

    private static void writePublicKey(RSAPublicKey key) throws Exception {
        Files.createDirectories(PUBLIC_KEY_FILE.getParent());
        byte[] der = key.getEncoded();
        String body = Base64.getMimeEncoder(64, "\n".getBytes(StandardCharsets.US_ASCII))
                .encodeToString(der);
        Files.writeString(PUBLIC_KEY_FILE,
                "-----BEGIN PUBLIC KEY-----\n" + body + "\n-----END PUBLIC KEY-----\n",
                StandardCharsets.US_ASCII);
    }
}
