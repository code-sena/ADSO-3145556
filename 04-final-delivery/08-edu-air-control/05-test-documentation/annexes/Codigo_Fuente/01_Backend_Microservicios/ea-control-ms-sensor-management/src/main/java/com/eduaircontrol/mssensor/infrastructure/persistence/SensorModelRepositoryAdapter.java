package com.eduaircontrol.mssensor.infrastructure.persistence;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.SensorModel;
import com.eduaircontrol.mssensor.domain.port.out.SensorModelRepository;
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
public class SensorModelRepositoryAdapter implements SensorModelRepository {

    private final SensorModelJpaRepository jpaRepository;

    @Override
    public SensorModel save(SensorModel item) {
        return jpaRepository.save(item);
    }

    @Override
    public Optional<SensorModel> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.exists(Specification.<SensorModel>where(
                (root, query, cb) -> cb.equal(cb.lower(root.get("code")), code.toLowerCase())));
    }

    @Override
    public void delete(SensorModel item) {
        jpaRepository.delete(item);
    }

    @Override
    public PageResult<SensorModel> search(String query, int page, int limit) {
        Specification<SensorModel> specification = (root, q, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (query != null && !query.isBlank()) {
                String pattern = "%" + query.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("code")), pattern),
                        cb.like(cb.lower(root.get("name")), pattern)));
            }
            return cb.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
        Page<SensorModel> result = jpaRepository.findAll(
                specification,
                PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "code")));
        return new PageResult<>(result.getContent(), result.getTotalElements(), page, limit);
    }
}
