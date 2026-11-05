package com.eduaircontrol.mssecurity.application.port;

import java.time.Instant;

/**
 * Lista negra de access tokens revocados (logout). El gateway la consulta en
 * solo lectura; ms-security es su propietario (ver data-model.md del servicio).
 */
public interface TokenBlacklist {

    void blacklist(String jti, Instant expiresAt);
}
