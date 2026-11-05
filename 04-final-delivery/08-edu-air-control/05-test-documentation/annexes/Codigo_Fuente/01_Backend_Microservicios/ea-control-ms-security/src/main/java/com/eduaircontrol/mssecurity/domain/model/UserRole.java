package com.eduaircontrol.mssecurity.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_roles")
@IdClass(UserRoleKey.class)
@Getter
@Setter
@NoArgsConstructor
public class UserRole {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Id
    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;

    @Column(name = "assigned_by")
    private UUID assignedBy;

    public UserRole(UUID userId, UUID roleId, Instant assignedAt) {
        this.userId = userId;
        this.roleId = roleId;
        this.assignedAt = assignedAt;
    }
}
