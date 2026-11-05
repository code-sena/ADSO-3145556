package com.eduaircontrol.msmonitoring.monitoring.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Umbral ambiental por variable y tipo de ambiente (ADR-014 aceptado).
 *
 * <p>Un umbral por (variable_id, environment_type_id, severity_id) en un periodo.
 * NO existe educational_environment_id: la especificacion lo rechaza
 * explicitamente. Los umbrales nunca se borran; se cierran con {@code validTo}.
 *
 * <p>Semantica de excedencia (DEC-006): una medicion excede el umbral si esta fuera
 * de [min_value, max_value]. Con INFO se guarda el rango normal y con WARNING los
 * limites externos; CRITICAL se deriva como "fuera del rango WARNING".
 */
@Entity
@Table(name = "variable_threshold", schema = "environment_monitoring")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariableThreshold {

    @Id
    @Column(name = "variable_threshold_id")
    private UUID id;

    @Column(name = "variable_id", nullable = false)
    private UUID variableId;

    @Column(name = "environment_type_id", nullable = false)
    private UUID environmentTypeId;

    @Column(name = "min_value", precision = 12, scale = 4)
    private BigDecimal minValue;

    @Column(name = "max_value", precision = 12, scale = 4)
    private BigDecimal maxValue;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "severity_id")
    private Severity severity;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    /** Vigente hoy: sin fecha de cierre o con cierre futuro. */
    public boolean isEffectiveOn(LocalDate date) {
        return !validFrom.isAfter(date) && (validTo == null || !validTo.isBefore(date));
    }

    /** La medicion esta fuera del rango aceptable de este umbral. */
    public boolean isExceededBy(BigDecimal value) {
        if (value == null) {
            return false;
        }
        if (minValue != null && value.compareTo(minValue) < 0) {
            return true;
        }
        return maxValue != null && value.compareTo(maxValue) > 0;
    }
}
