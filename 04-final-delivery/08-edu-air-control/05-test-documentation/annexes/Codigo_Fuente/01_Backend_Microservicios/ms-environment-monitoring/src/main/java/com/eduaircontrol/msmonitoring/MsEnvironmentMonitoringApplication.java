package com.eduaircontrol.msmonitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ms-environment-monitoring: microservicio de analisis historico ambiental.
 *
 * <p>Extraido del modulo {@code analysis} del monolito (ADR-002). Mantiene el mismo
 * dominio y contrato OpenAPI ({@code 07-api/contracts/openapi/ms-environment_monitoring.yaml}),
 * pero con base de datos propia y alimentado por el broker mediante el outbox del
 * monolito (ADR-007).
 */
@SpringBootApplication
@EnableScheduling
public class MsEnvironmentMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsEnvironmentMonitoringApplication.class, args);
    }
}
