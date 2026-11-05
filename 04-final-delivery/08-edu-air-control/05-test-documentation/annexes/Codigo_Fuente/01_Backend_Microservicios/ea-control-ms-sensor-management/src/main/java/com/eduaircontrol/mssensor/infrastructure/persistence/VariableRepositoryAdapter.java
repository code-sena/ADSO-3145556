package com.eduaircontrol.mssensor.infrastructure.persistence;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.port.out.VariableRepository;
import com.eduaircontrol.mssensor.domain.model.Variable;
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
public class VariableRepositoryAdapter implements VariableRepository {

    private final VariableJpaRepository jpaRepository;

    @Override
    public Variable save(Variable variable) {
        return jpaRepository.save(variable);
    }

    @Override
    public Optional<Variable> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.exists(Specification.<Variable>where(
                (root, query, cb) -> cb.equal(cb.lower(root.get("code")), code.toLowerCase())));
    }

    @Override
    public boolean existsByMeasurementUnitId(UUID measurementUnitId) {
        return jpaRepository.exists(Specification.<Variable>where(
                (root, query, cb) -> cb.equal(root.get("measurementUnitId"), measurementUnitId)));
    }

    @Override
    public void delete(Variable variable) {
        jpaRepository.delete(variable);
    }

    @Override
    public PageResult<Variable> search(String query, int page, int limit) {
        Specification<Variable> specification = (root, q, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (query != null && !query.isBlank()) {
                String pattern = "%" + query.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("code")), pattern),
                        cb.like(cb.lower(root.get("name")), pattern)));
            }
            return cb.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
        Page<Variable> result = jpaRepository.findAll(
                specification,
                PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "code")));
        return new PageResult<>(result.getContent(), result.getTotalElements(), page, limit);
    }
}
