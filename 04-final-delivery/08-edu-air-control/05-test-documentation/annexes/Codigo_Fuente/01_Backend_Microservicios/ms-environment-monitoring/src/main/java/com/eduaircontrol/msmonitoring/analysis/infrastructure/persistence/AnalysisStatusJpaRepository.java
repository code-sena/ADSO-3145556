package com.eduaircontrol.msmonitoring.analysis.infrastructure.persistence;

import com.eduaircontrol.msmonitoring.analysis.domain.model.AnalysisStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface AnalysisStatusJpaRepository extends JpaRepository<AnalysisStatus, UUID> {

    Optional<AnalysisStatus> findByCode(String code);

    List<AnalysisStatus> findAllByOrderByCodeAsc();
}