package com.eduaircontrol.msmonitoring.infrastructure.web;

import java.sql.Connection;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Salud del servicio. Verifica la conexion a la base de datos propia porque el
 * gateway y el healthcheck del compose dependen de este endpoint para decidir si
 * el servicio puede recibir trafico.
 */
@RestController
@RequiredArgsConstructor
public class HealthController {

    private final DataSource dataSource;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(2)) {
                return ResponseEntity.ok(Map.of("status", "UP", "service", "ms-environment-monitoring"));
            }
        } catch (Exception e) {
            // dependencia caida, se responde abajo
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("status", "DOWN", "service", "ms-environment-monitoring"));
    }
}
