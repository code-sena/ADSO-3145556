package com.eduaircontrol.msmonitoring.shared.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Lectura del catalogo de ambientes.
 *
 * <p>En el servicio se resuelve contra la replica local
 * {@code environment_monitoring.educational_environment}. La replica es minima: al analisis
 * solo le interesa saber que el ambiente existe y de que tipo es, porque el tipo
 * determina que umbral se aplica. Por eso {@code status} y
 * {@code occupancyCapacity} no se replican y se devuelven vacios.
 */
public interface EnvironmentLookupPort {

    Optional<EnvironmentInfo> findById(UUID environmentId);

    List<EnvironmentInfo> findAll();

    Optional<EnvironmentInfo> findByCode(String code);

    record EnvironmentInfo(
            UUID id,
            String code,
            String name,
            UUID campusId,
            UUID environmentTypeId,
            Integer floor) {
    }
}