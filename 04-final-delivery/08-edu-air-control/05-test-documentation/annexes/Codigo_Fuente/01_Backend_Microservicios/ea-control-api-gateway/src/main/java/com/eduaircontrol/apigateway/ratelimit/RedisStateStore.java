package com.eduaircontrol.apigateway.ratelimit;

import java.time.Duration;
import reactor.core.publisher.Mono;

/**
 * Estado efímero compartido en Redis: contadores de rate limiting y la
 * lista negra de tokens (solo lectura — la escribe ms-security).
 */
public interface RedisStateStore {

    Mono<Boolean> isBlacklisted(String jti);

    Mono<Long> increment(String key, Duration window);

    Mono<Void> blacklist(String jti, Duration ttl);
}
