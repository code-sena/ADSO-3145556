package com.eduaircontrol.apigateway.web;

import java.time.Instant;
import java.util.Map;

/**
 * Respuesta de /health: estado del gateway y de cada upstream
 * (ver contrato 07-api/contracts/openapi/api-gateway.yaml).
 */
public record HealthResponse(String status, Instant timestamp, Map<String, String> upstreams) {
}
