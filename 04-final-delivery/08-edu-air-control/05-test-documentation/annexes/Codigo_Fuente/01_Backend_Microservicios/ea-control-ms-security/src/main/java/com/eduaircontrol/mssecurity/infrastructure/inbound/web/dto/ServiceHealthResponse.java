package com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto;

/** Salud según auth-service.yaml: status + db + redis (blacklist de tokens). */
public record ServiceHealthResponse(String status, String db, String redis) {

    public static ServiceHealthResponse healthy(String redis) {
        return new ServiceHealthResponse("ok", "connected", redis);
    }

    public static ServiceHealthResponse databaseDown(String redis) {
        return new ServiceHealthResponse("down", "disconnected", redis);
    }
}
