package com.eduaircontrol.msusermanagement.infrastructure.persistence;

import com.eduaircontrol.msusermanagement.domain.model.PageResult;
import com.eduaircontrol.msusermanagement.domain.port.out.UserProfileRepository;
import com.eduaircontrol.msusermanagement.domain.model.RecordStatus;
import com.eduaircontrol.msusermanagement.domain.model.UserProfile;
import jakarta.persistence.criteria.Predicate;
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
public class UserProfileRepositoryAdapter implements UserProfileRepository {

    private final UserProfileJpaRepository jpaRepository;

    @Override
    public UserProfile save(UserProfile profile) {
        return jpaRepository.save(profile);
    }

    @Override
    public Optional<UserProfile> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<UserProfile> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return jpaRepository.existsByUserId(userId);
    }

    @Override
    public PageResult<UserProfile> search(String query, RecordStatus status, UUID institutionId, int page, int limit) {
        Specification<UserProfile> specification = (root, criteriaQuery, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNull(root.get("deletedAt")));
            if (query != null && !query.isBlank()) {
                String pattern = "%" + query.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("fullName")), pattern),
                        cb.like(cb.lower(root.get("department")), pattern)));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (institutionId != null) {
                predicates.add(cb.equal(root.get("institutionId"), institutionId));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        Page<UserProfile> result = jpaRepository.findAll(
                specification,
                PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "fullName")));
        return new PageResult<>(result.getContent(), result.getTotalElements(), page, limit);
    }
}
