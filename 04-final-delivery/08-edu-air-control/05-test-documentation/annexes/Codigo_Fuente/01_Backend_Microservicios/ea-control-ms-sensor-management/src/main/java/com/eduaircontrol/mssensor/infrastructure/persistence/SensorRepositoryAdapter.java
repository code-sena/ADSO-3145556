package com.eduaircontrol.mssensor.infrastructure.persistence;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.port.out.SensorRepository;
import com.eduaircontrol.mssensor.domain.model.Sensor;
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
public class SensorRepositoryAdapter implements SensorRepository {

    private final SensorJpaRepository jpaRepository;

    @Override
    public Sensor save(Sensor sensor) {
        return jpaRepository.save(sensor);
    }

    @Override
    public Optional<Sensor> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public boolean existsBySerialNumber(String serialNumber) {
        return jpaRepository.exists(Specification.<Sensor>where(
                (root, query, cb) -> cb.equal(cb.lower(root.get("serialNumber")),
                        serialNumber.toLowerCase())));
    }

    @Override
    public boolean existsBySensorModelId(UUID sensorModelId) {
        return jpaRepository.exists(Specification.<Sensor>where(
                (root, query, cb) -> cb.equal(root.get("sensorModelId"), sensorModelId)));
    }

    @Override
    public boolean existsBySensorStatusId(UUID sensorStatusId) {
        return jpaRepository.exists(Specification.<Sensor>where(
                (root, query, cb) -> cb.equal(root.get("sensorStatusId"), sensorStatusId)));
    }

    @Override
    public void delete(Sensor sensor) {
        jpaRepository.delete(sensor);
    }

    @Override
    public PageResult<Sensor> search(String query, UUID sensorModelId, UUID sensorStatusId, UUID institutionId,
            int page, int limit) {
        Specification<Sensor> specification = (root, q, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (query != null && !query.isBlank()) {
                String pattern = "%" + query.trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("serialNumber")), pattern));
            }
            if (sensorModelId != null) {
                predicates.add(cb.equal(root.get("sensorModelId"), sensorModelId));
            }
            if (sensorStatusId != null) {
                predicates.add(cb.equal(root.get("sensorStatusId"), sensorStatusId));
            }
            if (institutionId != null) {
                predicates.add(cb.equal(root.get("institutionId"), institutionId));
            }
            return cb.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
        Page<Sensor> result = jpaRepository.findAll(
                specification,
                PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "serialNumber")));
        return new PageResult<>(result.getContent(), result.getTotalElements(), page, limit);
    }
}
