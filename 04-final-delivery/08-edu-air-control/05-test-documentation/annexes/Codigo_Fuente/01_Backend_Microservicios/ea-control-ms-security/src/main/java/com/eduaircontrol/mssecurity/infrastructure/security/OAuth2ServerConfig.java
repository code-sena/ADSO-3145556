package com.eduaircontrol.mssecurity.infrastructure.security;

import com.eduaircontrol.mssecurity.infrastructure.security.JwtService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.time.Duration;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * Configuración de Spring Authorization Server 7 (OAuth2/OIDC) en ms-security
 * (ADR-017). Reutiliza las claves RSA de {@link JwtService} para firmar y
 * publicar JWKS, y añade los claims de tenant al access token (ADR-016).
 */
@Configuration
public class OAuth2ServerConfig {

    @Bean
    JWKSource<SecurityContext> jwkSource(JwtService jwtService) {
        RSAKey rsaKey = new RSAKey.Builder(jwtService.getPublicKey())
                .privateKey(jwtService.getPrivateKey())
                .keyID(jwtService.getKid())
                .build();
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings(
            @Value("${app.oauth2.issuer:http://localhost:8081}") String issuer) {
        return AuthorizationServerSettings.builder().issuer(issuer).build();
    }

    @Bean
    RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(
            com.eduaircontrol.mssecurity.application.port.UserRepository userRepository,
            com.eduaircontrol.mssecurity.application.port.UserRoleRepository userRoleRepository,
            com.eduaircontrol.mssecurity.application.port.RoleRepository roleRepository) {
        return context -> {
            if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())
                    || context.getPrincipal() == null) {
                return;
            }
            String email = context.getPrincipal().getName();
            userRepository.findByEmail(email).ifPresent(user -> {
                context.getClaims().claim("userId", user.getId().toString());
                context.getClaims().claim("email", user.getEmail());
                context.getClaims().claim("roles", userRoleRepository.findByUserId(user.getId()).stream()
                        .map(userRole -> roleRepository.findById(userRole.getRoleId())
                                .map(com.eduaircontrol.mssecurity.domain.model.Role::getName).orElse(null))
                        .filter(java.util.Objects::nonNull)
                        .sorted()
                        .toList());
                if (user.getInstitutionId() != null) {
                    context.getClaims().claim("institutionId", user.getInstitutionId().toString());
                }
                if (user.getCampusId() != null) {
                    context.getClaims().claim("campusId", user.getCampusId().toString());
                }
                context.getClaims().claim("permissions", java.util.List.of());
            });
        };
    }

    @Bean
    ApplicationRunner registeredClientBootstrap(RegisteredClientRepository repository,
            @Value("${app.oauth2.web-redirect-uri:http://localhost:8080/login/oauth2/code/web}") String webRedirectUri,
            @Value("${app.oauth2.mobile-redirect-uri:eduaircontrol://oauth2/callback}") String mobileRedirectUri) {
        return (ApplicationArguments args) -> {
            if (repository.findByClientId("ea-control-web") == null) {
                repository.save(publicClient("ea-control-web", "EduAirControl Web", webRedirectUri));
            }
            if (repository.findByClientId("ea-control-mobile") == null) {
                repository.save(publicClient("ea-control-mobile", "EduAirControl Mobile", mobileRedirectUri));
            }
        };
    }

    private RegisteredClient publicClient(String clientId, String name, String redirectUri) {
        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientName(name)
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(redirectUri)
                .scope("openid")
                .scope("profile")
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(true)
                        .requireAuthorizationConsent(false)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(15))
                        .refreshTokenTimeToLive(Duration.ofDays(7))
                        .reuseRefreshTokens(false)
                        .build())
                .build();
    }
}
