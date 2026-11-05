package com.eduaircontrol.mssecurity.application.port;

import com.eduaircontrol.mssecurity.domain.model.Role;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {

    Optional<Role> findByName(String name);

    Optional<Role> findById(UUID id);

    List<Role> findAll();

    Role save(Role role);
}
