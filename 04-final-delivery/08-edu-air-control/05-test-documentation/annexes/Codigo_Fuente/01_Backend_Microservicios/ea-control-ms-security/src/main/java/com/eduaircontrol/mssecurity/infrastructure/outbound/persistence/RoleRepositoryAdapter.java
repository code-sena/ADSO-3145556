package com.eduaircontrol.mssecurity.infrastructure.outbound.persistence;

import com.eduaircontrol.mssecurity.application.port.RoleRepository;
import com.eduaircontrol.mssecurity.domain.model.Role;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository jpaRepository;

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRepository.findByName(name);
    }

    @Override
    public List<Role> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Role save(Role role) {
        return jpaRepository.save(role);
    }
}
