package com.eduaircontrol.apigateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;

/**
 * Sesión del BFF respaldada en Redis (ADR-017): el navegador solo recibe una
 * cookie de sesión httpOnly; los tokens OAuth2 se guardan en el servidor.
 */
@Configuration
@EnableRedisWebSession
public class BffSessionConfig {
}
