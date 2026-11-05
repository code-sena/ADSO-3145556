package com.eduaircontrol.mssensor.infrastructure.persistence;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.MeasurementUnit;
import com.eduaircontrol.mssensor.domain.port.out.MeasurementUnitRepository;
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
public class MeasurementUnitRepositoryAdapter implements MeasurementUnitRepository {

    private final MeasurementUnitJpaRepository jpaRepository;

    @Override
    public MeasurementUnit save(MeasurementUnit item) {
        return jpaRepository.save(item);
    }

    @Override
    public Optional<MeasurementUnit> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.exists(Specification.<MeasurementUnit>where(
                (root, query, cb) -> cb.equal(cb.lower(root.get("code")), code.toLowerCase())));
    }

    @Override
    public void delete(MeasurementUnit item) {
        jpaRepository.delete(item);
    }

    @Override
    public PageResult<MeasurementUnit> search(String query, int page, int limit) {
        Specification<MeasurementUnit> specification = (root, q, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (query != null && !query.isBlank()) {
                String pattern = "%" + query.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("code")), pattern),
                        cb.like(cb.lower(root.get("name")), pattern)));
            }
            return cb.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
        Page<MeasurementUnit> result = jpaRepository.findAll(
                specification,
                PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "code")));
        return new PageResult<>(result.getContent(), result.getTotalElements(), page, limit);
    }
}
