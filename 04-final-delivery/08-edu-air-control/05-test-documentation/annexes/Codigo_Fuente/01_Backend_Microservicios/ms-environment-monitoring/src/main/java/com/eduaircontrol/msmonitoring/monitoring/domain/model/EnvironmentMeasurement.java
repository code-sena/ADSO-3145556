package com.eduaircontrol.msmonitoring.monitoring.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Medicion ambiental atomica (modelo canonico §4.1).
 *
 * <p>Una fila = una instalacion + una variable + un instante. La tabla no guarda el
 * ambiente: se resuelve por la proyeccion local. El instante real ({@code measuredAt})
 * nunca se reemplaza; {@code createdAt} es cuando el servicio la persistio.
 */
@Entity
@Table(name = "environment_measurement", schema = "environment_monitoring")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentMeasurement {

    @Id
    @Column(name = "environment_measurement_id")
    private UUID id;

    @Column(name = "sensor_installation_id", nullable = false)
    private UUID sensorInstallationId;

    @Column(name = "variable_id", nullable = false)
    private UUID variableId;

    @Column(name = "measured_value", nullable = false, precision = 12, scale = 4)
    private BigDecimal measuredValue;

    @Column(name = "measured_at", nullable = false)
    private Instant measuredAt;

    @Column(name = "quality_flag_id", nullable = false)
    private UUID qualityFlagId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
