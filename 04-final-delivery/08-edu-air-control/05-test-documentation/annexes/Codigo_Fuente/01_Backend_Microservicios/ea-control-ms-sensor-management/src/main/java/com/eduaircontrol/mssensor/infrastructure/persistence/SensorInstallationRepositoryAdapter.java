package com.eduaircontrol.mssensor.infrastructure.persistence;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.port.out.SensorInstallationRepository;
import com.eduaircontrol.mssensor.domain.model.SensorInstallation;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SensorInstallationRepositoryAdapter implements SensorInstallationRepository {

    private final SensorInstallationJpaRepository jpaRepository;

    @Override
    public SensorInstallation save(SensorInstallation installation) {
        return jpaRepository.save(installation);
    }

    @Override
    public Optional<SensorInstallation> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public PageResult<SensorInstallation> search(UUID sensorId, UUID educationalEnvironmentId,
            Boolean active, int page, int limit) {
        Specification<SensorInstallation> specification = (root, q, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (sensorId != null) {
                predicates.add(cb.equal(root.get("sensorId"), sensorId));
            }
            if (educationalEnvironmentId != null) {
                predicates.add(cb.equal(root.get("educationalEnvironmentId"),
                        educationalEnvironmentId));
            }
            if (active != null) {
                predicates.add(active
                        ? cb.isNull(root.get("removedAt"))
                        : cb.isNotNull(root.get("removedAt")));
            }
            return cb.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
        Page<SensorInstallation> result = jpaRepository.findAll(
                specification,
                PageRequest.of(page - 1, limit,
                        Sort.by(Sort.Direction.DESC, "installedAt")));
        return new PageResult<>(result.getContent(), result.getTotalElements(), page, limit);
    }

    @Override
    public boolean existsOverlapForSensor(UUID sensorId, Instant installedAt) {
        Specification<SensorInstallation> specification = (root, q, cb) ->
                cb.and(
                        cb.equal(root.get("sensorId"), sensorId),
                        cb.or(
                                cb.isNull(root.get("removedAt")),
                                cb.greaterThan(root.get("removedAt"), installedAt)));
        return jpaRepository.exists(specification);
    }

    @Override
    public boolean hasAnyForSensor(UUID sensorId) {
        return jpaRepository.existsBySensorId(sensorId);
    }
}
