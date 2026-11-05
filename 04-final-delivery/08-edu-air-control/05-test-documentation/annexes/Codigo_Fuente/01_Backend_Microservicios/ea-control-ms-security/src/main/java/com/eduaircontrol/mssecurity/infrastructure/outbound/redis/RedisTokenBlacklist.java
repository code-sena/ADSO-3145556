package com.eduaircontrol.mssecurity.infrastructure.outbound.redis;

import com.eduaircontrol.mssecurity.application.port.TokenBlacklist;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Implementa la lista negra en Redis con clave {@code blacklist:{jti}} y TTL
 * igual a la vida restante del token (data-model.md del servicio). Al expirar
 * el token la entrada se autolimpia.
 */
@Component
@RequiredArgsConstructor
public class RedisTokenBlacklist implements TokenBlacklist {

    static final String PREFIX = "blacklist:";

    private final StringRedisTemplate redis;

    @Override
    public void blacklist(String jti, Instant expiresAt) {
        if (jti == null || jti.isBlank() || expiresAt == null) {
            return;
        }
        Duration ttl = Duration.between(Instant.now(), expiresAt);
        if (ttl.isNegative() || ttl.isZero()) {
            return;
        }
        redis.opsForValue().set(PREFIX + jti, "1", ttl);
    }
}
