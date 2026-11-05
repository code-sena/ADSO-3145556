package com.eduaircontrol.msusermanagement.application;

import java.util.UUID;

/**
 * Contexto de tenant de la petición (institución/sede). Lo puebla el filtro de
 * entrada a partir de los headers del api-gateway (ADR-016).
 */
public final class TenantContext {

    private static final ThreadLocal<UUID> INSTITUTION = new ThreadLocal<>();
    private static final ThreadLocal<UUID> CAMPUS = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void set(UUID institutionId, UUID campusId) {
        INSTITUTION.set(institutionId);
        CAMPUS.set(campusId);
    }

    public static UUID institutionId() {
        return INSTITUTION.get();
    }

    public static UUID campusId() {
        return CAMPUS.get();
    }

    public static void clear() {
        INSTITUTION.remove();
        CAMPUS.remove();
    }
}
