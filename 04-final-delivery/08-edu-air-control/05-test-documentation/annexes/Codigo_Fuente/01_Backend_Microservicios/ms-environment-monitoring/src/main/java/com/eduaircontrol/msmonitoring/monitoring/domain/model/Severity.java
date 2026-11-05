package com.eduaircontrol.msmonitoring.monitoring.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Severidad de alerta (ADR-014).
 *
 * <p>Catalogo sembrado con INFO (1), WARNING (2) y CRITICAL (3). Las severidades
 * nunca se borran; si dejan de usarse, simplemente no se referencian en umbrales
 * nuevos.
 */
@Entity
@Table(name = "severity", schema = "environment_monitoring")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Severity {

    @Id
    @Column(name = "severity_id")
    private UUID id;

    @Column(nullable = false, length = 20, unique = true)
    private String code;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, unique = true)
    private Short level;

    public boolean is(String code) {
        return this.code != null && this.code.equals(code);
    }
}
