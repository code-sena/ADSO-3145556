package com.eduaircontrol.apigateway.ratelimit;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RedisStateStoreImpl implements RedisStateStore {

    static final String BLACKLIST_PREFIX = "blacklist:";

    private final ReactiveStringRedisTemplate redis;

    @Override
    public Mono<Boolean> isBlacklisted(String jti) {
        if (jti == null || jti.isBlank()) {
            return Mono.just(false);
        }
        return redis.hasKey(BLACKLIST_PREFIX + jti);
    }

    @Override
    public Mono<Long> increment(String key, Duration window) {
        return redis.opsForValue().increment(key)
                .flatMap(count -> {
                    if (count != null && count == 1L) {
                        return redis.expire(key, window).thenReturn(count);
                    }
                    return Mono.just(count == null ? 0L : count);
                });
    }

    @Override
    public Mono<Void> blacklist(String jti, Duration ttl) {
        return redis.opsForValue().set(BLACKLIST_PREFIX + jti, "1", ttl).then();
    }
}
