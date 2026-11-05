package com.eduaircontrol.msmonitoring.shared.contract;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Umbrales vigentes de un ambiente, para contar excedencias.
 *
 * <p>El servicio no administra umbrales: los replica desde el dominio monitoring y
 * solo los lee.
 */
public interface ThresholdPort {

    /** Umbral de advertencia de una variable, si existe. */
    java.util.Optional<Range> warningRange(UUID environmentId, UUID variableId);

    /** Umbrales de advertencia de todas las variables del ambiente. */
    List<Range> warningRanges(UUID environmentId);

    record Range(UUID environmentId, UUID variableId, BigDecimal min, BigDecimal max) {
    }
}