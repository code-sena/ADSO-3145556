package com.eduaircontrol.mssecurity.domain.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class UserRoleKey implements Serializable {

    private UUID userId;
    private UUID roleId;

    public UserRoleKey() {
    }

    public UserRoleKey(UUID userId, UUID roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRoleKey other)) {
            return false;
        }
        return Objects.equals(userId, other.userId) && Objects.equals(roleId, other.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }
}
