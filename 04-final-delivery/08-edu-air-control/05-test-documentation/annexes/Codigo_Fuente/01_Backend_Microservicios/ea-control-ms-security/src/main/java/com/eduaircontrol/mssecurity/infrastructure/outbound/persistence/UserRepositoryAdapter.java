package com.eduaircontrol.mssecurity.infrastructure.outbound.persistence;

import com.eduaircontrol.mssecurity.application.port.UserRepository;
import com.eduaircontrol.mssecurity.domain.model.User;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return jpaRepository.save(user);
    }

    @Override
    public java.util.List<User> findAll() {
        return jpaRepository.findAll();
    }
}
