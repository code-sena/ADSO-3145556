package com.eduaircontrol.msmonitoring.shared.contract;

import java.util.List;
import java.util.UUID;

/**
 * Agregacion de mediciones para el modulo de analisis.
 *
 * <p>Es la costura que mantiene el dominio independiente de como se guardan los
 * datos. Antes de la extraccion la implementaba el modulo monitoring del monolito;
 * ahora la implementa la proyeccion local sobre {@code environment_monitoring.environment_measurement}.
 */
public interface MeasurementAggregationPort {

    List<Aggregate> aggregate(UUID environmentId, java.time.Instant start, java.time.Instant end);

    record Aggregate(UUID variableId,
                     String variableCode,
                     java.math.BigDecimal minValue,
                     java.math.BigDecimal maxValue,
                     java.math.BigDecimal avgValue,
                     long sampleCount,
                     long exceedanceCount) {
    }
}