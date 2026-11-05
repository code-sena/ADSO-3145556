package com.eduaircontrol.apigateway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.eduaircontrol.apigateway.ratelimit.RedisStateStore;
import com.eduaircontrol.apigateway.security.JwksProvider;
import com.eduaircontrol.apigateway.security.TestTokens;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewayIntegrationTest {

    private static final KeyPair KEY_PAIR = TestTokens.generateKeyPair();
    private static HttpServer upstream;
    private static int upstreamPort;

    @LocalServerPort
    private int gatewayPort;

    private WebTestClient webTestClient;

    @MockitoBean
    private JwksProvider jwksProvider;

    @MockitoBean
    private RedisStateStore stateStore;

    @BeforeAll
    static void startUpstream() throws IOException {
        upstream = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        upstream.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();
            String body;
            if (path.endsWith("/health")) {
                body = "{\"status\":\"ok\"}";
            } else {
                body = "{\"userId\":\"" + header(exchange, "X-User-Id")
                        + "\",\"role\":\"" + header(exchange, "X-User-Role")
                        + "\",\"institutionId\":\"" + header(exchange, "X-Institution-Id")
                        + "\",\"correlationId\":\"" + header(exchange, "X-Correlation-Id") + "\"}";
            }
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(bytes);
            }
        });
        upstream.start();
        upstreamPort = upstream.getAddress().getPort();
    }

    private static String header(com.sun.net.httpserver.HttpExchange exchange, String name) {
        String value = exchange.getRequestHeaders().getFirst(name);
        return value == null ? "" : value;
    }

    @DynamicPropertySource
    static void upstreamProperties(DynamicPropertyRegistry registry) {
        // El puerto real se conoce en tiempo de ejecución: usar un supplier.
        registry.add("AUTH_URI", () -> "http://localhost:" + upstreamPort);
        registry.add("CLASSROOM_URI", () -> "http://localhost:" + upstreamPort);
        registry.add("SENSOR_URI", () -> "http://localhost:" + upstreamPort);
        registry.add("MONITORING_URI", () -> "http://localhost:" + upstreamPort);
        registry.add("USER_MANAGEMENT_URI", () -> "http://localhost:" + upstreamPort);
        registry.add("USER_EXPERIENCE_URI", () -> "http://localhost:" + upstreamPort);
        registry.add("AUTH_JWKS_URI", () -> "http://localhost:" + upstreamPort + "/api/v1/auth/jwks");
        registry.add("RATE_LIMIT_MAX", () -> "1000");
    }

    @BeforeEach
    void stubDependencies() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + gatewayPort)
                .build();
        when(jwksProvider.key(TestTokens.KID)).thenReturn(Mono.just((RSAPublicKey) KEY_PAIR.getPublic()));
        when(stateStore.isBlacklisted(anyString())).thenReturn(Mono.just(false));
        when(stateStore.increment(anyString(), any())).thenReturn(Mono.just(1L));
    }

    @Test
    void healthReportsUpstreams() {
        webTestClient.get().uri("/health").exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("ok")
                .jsonPath("$.upstreams.ms-security").isEqualTo("ok");
    }

    @Test
    void publicAuthEndpointRoutesWithoutToken() {
        webTestClient.get().uri("/api/v1/auth/health").exchange()
                .expectStatus().isOk();
    }

    @Test
    void protectedRouteWithoutTokenReturns401() {
        webTestClient.get().uri("/api/v1/sensors").exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.error").isEqualTo("MISSING_TOKEN");
    }

    @Test
    void protectedRouteWithValidTokenForwardsIdentityHeaders() {
        String token = TestTokens.token(KEY_PAIR, "user-42", "jti-42",
                List.of("ADMIN"), Instant.now().plusSeconds(600));

        webTestClient.get().uri("/api/v1/sensors")
                .header("Authorization", "Bearer " + token)
                .header("X-Correlation-Id", "corr-xyz")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.userId").isEqualTo("user-42")
                .jsonPath("$.role").isEqualTo("ADMIN")
                .jsonPath("$.institutionId").isEqualTo("inst-test")
                .jsonPath("$.correlationId").isEqualTo("corr-xyz");
    }

    @Test
    void blacklistedTokenReturns401() {
        when(stateStore.isBlacklisted("jti-revoked")).thenReturn(Mono.just(true));
        String token = TestTokens.token(KEY_PAIR, "user-7", "jti-revoked",
                List.of("USER"), Instant.now().plusSeconds(600));

        webTestClient.get().uri("/api/v1/sensors")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.error").isEqualTo("TOKEN_REVOKED");
    }

    @Test
    void generatedCorrelationIdIsReturned() {
        webTestClient.get().uri("/api/v1/auth/health").exchange()
                .expectStatus().isOk()
                .expectHeader().exists("X-Correlation-Id");
    }
}
