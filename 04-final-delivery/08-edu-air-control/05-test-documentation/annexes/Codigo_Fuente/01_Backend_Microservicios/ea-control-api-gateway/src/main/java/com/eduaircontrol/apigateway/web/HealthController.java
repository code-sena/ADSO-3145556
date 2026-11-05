package com.eduaircontrol.apigateway.web;

import com.eduaircontrol.apigateway.config.AppProperties;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Health check público del gateway. Consulta el /health de cada upstream y
 * resume el estado: ok, degraded o 503 si el servicio crítico de seguridad
 * no responde.
 */
@RestController
@RequiredArgsConstructor
public class HealthController {

    private static final Duration TIMEOUT = Duration.ofSeconds(2);
    private static final String CRITICAL_UPSTREAM = "ms-security";

    private final WebClient webClient;
    private final AppProperties properties;

    @GetMapping("/health")
    public Mono<ResponseEntity<HealthResponse>> health() {
        return Flux.fromIterable(properties.upstreams().entrySet())
                .flatMap(entry -> check(entry.getKey(), entry.getValue()))
                .collectMap(UpstreamStatus::name, UpstreamStatus::status)
                .map(statuses -> {
                    boolean criticalDown = "down".equals(statuses.get(CRITICAL_UPSTREAM));
                    boolean anyDegraded = statuses.values().stream().anyMatch(status -> !"ok".equals(status));
                    String status = anyDegraded ? "degraded" : "ok";
                    HttpStatus httpStatus = criticalDown ? HttpStatus.SERVICE_UNAVAILABLE : HttpStatus.OK;
                    return ResponseEntity.status(httpStatus)
                            .body(new HealthResponse(status, Instant.now(), statuses));
                });
    }

    private Mono<UpstreamStatus> check(String name, String baseUrl) {
        return webClient.get()
                .uri(baseUrl + "/health")
                .retrieve()
                .toBodilessEntity()
                .timeout(TIMEOUT)
                .map(response -> new UpstreamStatus(name,
                        response.getStatusCode().is2xxSuccessful() ? "ok" : "degraded"))
                .onErrorReturn(new UpstreamStatus(name, "down"));
    }

    private record UpstreamStatus(String name, String status) {
    }
}
