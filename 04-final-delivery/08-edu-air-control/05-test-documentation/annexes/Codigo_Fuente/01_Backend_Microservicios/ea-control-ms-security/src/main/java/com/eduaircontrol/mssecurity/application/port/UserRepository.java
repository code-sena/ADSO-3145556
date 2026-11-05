package com.eduaircontrol.mssecurity.application.port;

import com.eduaircontrol.mssecurity.domain.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    boolean existsByEmail(String email);

    User save(User user);

    List<User> findAll();
}
