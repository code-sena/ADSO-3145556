package com.eduaircontrol.mssecurity.infrastructure.outbound.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

class RedisTokenBlacklistTest {

    private StringRedisTemplate redis;
    private ValueOperations<String, String> ops;
    private RedisTokenBlacklist blacklist;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        redis = mock(StringRedisTemplate.class);
        ops = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(ops);
        blacklist = new RedisTokenBlacklist(redis);
    }

    @Test
    void storesJtiWithRemainingTtl() {
        Instant expiresAt = Instant.now().plusSeconds(120);

        blacklist.blacklist("jti-abc", expiresAt);

        ArgumentCaptor<Duration> ttl = ArgumentCaptor.forClass(Duration.class);
        verify(ops).set(eq("blacklist:jti-abc"), eq("1"), ttl.capture());
        assertThat(ttl.getValue()).isPositive();
        assertThat(ttl.getValue()).isLessThanOrEqualTo(Duration.ofSeconds(120));
    }

    @Test
    void skipsAlreadyExpiredToken() {
        blacklist.blacklist("jti-old", Instant.now().minusSeconds(5));

        verify(ops, never()).set(anyString(), anyString(), any(Duration.class));
    }

    @Test
    void skipsBlankJti() {
        blacklist.blacklist(" ", Instant.now().plusSeconds(60));
        blacklist.blacklist(null, Instant.now().plusSeconds(60));

        verify(ops, never()).set(anyString(), anyString(), any(Duration.class));
    }
}
