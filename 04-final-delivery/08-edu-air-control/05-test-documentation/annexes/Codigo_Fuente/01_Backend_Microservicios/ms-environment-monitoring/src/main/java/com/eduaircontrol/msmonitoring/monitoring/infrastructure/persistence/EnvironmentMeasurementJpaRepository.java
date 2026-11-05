package com.eduaircontrol.msmonitoring.monitoring.infrastructure.persistence;

import com.eduaircontrol.msmonitoring.monitoring.domain.model.EnvironmentMeasurement;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnvironmentMeasurementJpaRepository
        extends JpaRepository<EnvironmentMeasurement, UUID> {

    boolean existsBySensorInstallationIdAndVariableIdAndMeasuredAt(
            UUID sensorInstallationId, UUID variableId, Instant measuredAt);

    @Query(value = """
            select m.* from environment_monitoring.environment_measurement m
            join environment_monitoring.installation_projection p
                on p.sensor_installation_id = m.sensor_installation_id
            where p.educational_environment_id = :environmentId
              and p.removed_at is null
              and (m.variable_id, m.measured_at) in (
                  select m2.variable_id, max(m2.measured_at)
                  from environment_monitoring.environment_measurement m2
                  join environment_monitoring.installation_projection p2
                      on p2.sensor_installation_id = m2.sensor_installation_id
                  where p2.educational_environment_id = :environmentId
                    and p2.removed_at is null
                  group by m2.variable_id
              )
            """, nativeQuery = true)
    List<EnvironmentMeasurement> latestByEnvironment(
            @Param("environmentId") UUID environmentId);

    @Query(value = """
            select m.* from environment_monitoring.environment_measurement m
            join environment_monitoring.installation_projection p
                on p.sensor_installation_id = m.sensor_installation_id
            where p.educational_environment_id = :environmentId
              and p.removed_at is null
              and (:variableId is null or m.variable_id = :variableId)
              and m.measured_at >= :from
              and m.measured_at < :to
            """,
            countQuery = """
                    select count(*) from environment_monitoring.environment_measurement m
                    join environment_monitoring.installation_projection p
                        on p.sensor_installation_id = m.sensor_installation_id
                    where p.educational_environment_id = :environmentId
                      and p.removed_at is null
                      and (:variableId is null or m.variable_id = :variableId)
                      and m.measured_at >= :from
                      and m.measured_at < :to
                    """,
            nativeQuery = true)
    Page<EnvironmentMeasurement> history(@Param("environmentId") UUID environmentId,
                                         @Param("variableId") UUID variableId,
                                         @Param("from") Instant from,
                                         @Param("to") Instant to,
                                         Pageable pageable);
}
