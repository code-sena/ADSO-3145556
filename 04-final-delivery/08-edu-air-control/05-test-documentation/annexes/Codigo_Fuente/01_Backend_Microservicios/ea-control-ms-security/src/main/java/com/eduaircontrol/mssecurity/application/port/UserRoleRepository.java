package com.eduaircontrol.mssecurity.application.port;

import com.eduaircontrol.mssecurity.domain.model.UserRole;
import java.util.List;
import java.util.UUID;

public interface UserRoleRepository {

    List<UserRole> findByUserId(UUID userId);

    UserRole save(UserRole userRole);

    void delete(UserRole userRole);
}
