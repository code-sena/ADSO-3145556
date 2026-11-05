package com.eduaircontrol.mssecurity.infrastructure.outbound.persistence;

import com.eduaircontrol.mssecurity.application.port.InstitutionRepository;
import com.eduaircontrol.mssecurity.domain.model.Institution;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstitutionRepositoryAdapter implements InstitutionRepository {

    private final InstitutionJpaRepository jpaRepository;

    @Override
    public Optional<Institution> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Institution> findByCode(String code) {
        return jpaRepository.findByCode(code);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    @Override
    public List<Institution> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Institution save(Institution institution) {
        return jpaRepository.save(institution);
    }
}
