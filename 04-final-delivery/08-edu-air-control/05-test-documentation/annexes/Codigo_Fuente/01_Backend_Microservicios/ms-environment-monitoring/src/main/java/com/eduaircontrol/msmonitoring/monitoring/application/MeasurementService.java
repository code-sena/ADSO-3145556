package com.eduaircontrol.msmonitoring.monitoring.application;

import com.eduaircontrol.msmonitoring.monitoring.domain.model.EnvironmentMeasurement;
import com.eduaircontrol.msmonitoring.monitoring.domain.port.in.MeasurementUseCase;
import com.eduaircontrol.msmonitoring.monitoring.domain.port.out.MeasurementRepository;
import com.eduaircontrol.msmonitoring.shared.contract.VariableCatalogPort;
import com.eduaircontrol.msmonitoring.shared.exception.NotFoundException;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Registro y consulta de mediciones (HU-MON-001 … HU-MON-004).
 *
 * <p>El registro es idempotente por (instalacion, variable, instante): una
 * reingesta no duplica. La calidad por defecto es VALID; el backfill y el
 * consumidor resuelven el flag que corresponda.
 */
@Service
@RequiredArgsConstructor
public class MeasurementService implements MeasurementUseCase {

    private static final int MAX_LIMIT = 100;

    private final MeasurementRepository measurementRepository;
    private final VariableCatalogPort variableCatalogPort;
    private final JdbcTemplate jdbc;
    private final Clock clock;

    @Override
    @Transactional
    public UUID record(RecordCommand command) {
        if (command.value() == null) {
            throw new IllegalArgumentException("value es obligatorio");
        }
        Instant measuredAt = command.measuredAt() != null ? command.measuredAt() : clock.instant();
        if (measurementRepository.existsByInstallationAndVariableAndInstant(
                command.sensorInstallationId(), command.variableId(), measuredAt)) {
            return null;
        }
        EnvironmentMeasurement measurement = EnvironmentMeasurement.builder()
                .id(UUID.randomUUID())
                .sensorInstallationId(command.sensorInstallationId())
                .variableId(command.variableId())
                .measuredValue(command.value())
                .measuredAt(measuredAt)
                .qualityFlagId(qualityFlagId("VALID"))
                .createdAt(clock.instant())
                .build();
        return measurementRepository.save(measurement).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CurrentValue> currentValues(UUID environmentId) {
        return measurementRepository.latestByEnvironment(environmentId).stream()
                .map(measurement -> new CurrentValue(
                        measurement.getVariableId(),
                        variableCodeOf(measurement.getVariableId()),
                        measurement.getMeasuredValue(),
                        measurement.getMeasuredAt()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MeasurementView> history(HistoryQuery query) {
        Pageable pageable = PageRequest.of(
                Math.max(query.page(), 1) - 1,
                Math.min(Math.max(query.limit(), 1), MAX_LIMIT),
                Sort.by(Sort.Direction.DESC, "measured_at"));
        return measurementRepository
                .history(query.environmentId(), query.variableId(),
                        query.from(), query.to(), pageable)
                .getContent().stream()
                .map(measurement -> new MeasurementView(
                        measurement.getId(),
                        measurement.getVariableId(),
                        variableCodeOf(measurement.getVariableId()),
                        measurement.getMeasuredValue(),
                        measurement.getMeasuredAt()))
                .toList();
    }

    private String variableCodeOf(UUID variableId) {
        return variableCatalogPort.findById(variableId)
                .map(VariableCatalogPort.VariableRef::code)
                .orElse(variableId.toString());
    }

    private UUID qualityFlagId(String code) {
        return jdbc.query("""
                        select quality_flag_id from environment_monitoring.quality_flag
                        where code = ?
                        """,
                        (rs, rowNum) -> rs.getObject("quality_flag_id", UUID.class),
                        code)
                .stream().findFirst()
                .orElseThrow(() -> new NotFoundException("quality_flag no sembrado: " + code));
    }
}
