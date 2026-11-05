package com.eduaircontrol.mssecurity.infrastructure.outbound.persistence;

import com.eduaircontrol.mssecurity.application.port.UserRoleRepository;
import com.eduaircontrol.mssecurity.domain.model.UserRole;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRoleRepositoryAdapter implements UserRoleRepository {

    private final UserRoleJpaRepository jpaRepository;

    @Override
    public List<UserRole> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public UserRole save(UserRole userRole) {
        return jpaRepository.save(userRole);
    }

    @Override
    public void delete(UserRole userRole) {
        jpaRepository.delete(userRole);
    }
}
