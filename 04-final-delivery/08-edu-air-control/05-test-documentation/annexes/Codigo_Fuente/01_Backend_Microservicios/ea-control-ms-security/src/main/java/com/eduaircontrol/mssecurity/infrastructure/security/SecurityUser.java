package com.eduaircontrol.mssecurity.infrastructure.security;

import java.util.Collection;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Principal autenticado con el contexto de tenant (institución/sede) para que el
 * customizer de tokens pueda añadir los claims (ADR-016/017).
 */
public class SecurityUser extends User {

    private final UUID userId;
    private final UUID institutionId;
    private final UUID campusId;

    public SecurityUser(UUID userId, UUID institutionId, UUID campusId, String email,
            String passwordHash, Collection<? extends GrantedAuthority> authorities) {
        super(email, passwordHash, authorities);
        this.userId = userId;
        this.institutionId = institutionId;
        this.campusId = campusId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getInstitutionId() {
        return institutionId;
    }

    public UUID getCampusId() {
        return campusId;
    }
}
