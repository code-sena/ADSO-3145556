package com.eduaircontrol.msmonitoring.monitoring.infrastructure.persistence;

import com.eduaircontrol.msmonitoring.monitoring.domain.model.EnvironmentAlert;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnvironmentAlertJpaRepository extends JpaRepository<EnvironmentAlert, UUID> {

    @Query("""
            select a from EnvironmentAlert a
            where (:environmentId is null or a.educationalEnvironmentId = :environmentId)
              and (:active is null
                   or (:active = true and a.acknowledgedAt is null)
                   or (:active = false and a.acknowledgedAt is not null))
              and (cast(:from as timestamp) is null or a.raisedAt >= :from)
              and (cast(:to as timestamp) is null or a.raisedAt < :to)
            order by a.raisedAt desc
            """)
    Page<EnvironmentAlert> search(@Param("environmentId") UUID environmentId,
                                  @Param("active") Boolean active,
                                  @Param("from") Instant from,
                                  @Param("to") Instant to,
                                  Pageable pageable);

    List<EnvironmentAlert> findByTriggeringMeasurementIdAndAcknowledgedAtIsNull(
            UUID triggeringMeasurementId);
}
