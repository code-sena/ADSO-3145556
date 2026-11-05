package com.eduaircontrol.apigateway.web;

import com.eduaircontrol.apigateway.security.JwtPrincipal;
import com.eduaircontrol.apigateway.security.JwtValidator;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Devuelve la identidad del usuario autenticado en el BFF (ADR-017), a partir del
 * access token guardado en la sesión. El SPA no maneja tokens: consulta aquí quién
 * es. Compatible con tokens RS256 de ms-security.
 */
@RestController
@RequiredArgsConstructor
public class MeController {

    private final ReactiveOAuth2AuthorizedClientService authorizedClientService;
    private final JwtValidator jwtValidator;

    @GetMapping("/api/v1/me")
    public Mono<ResponseEntity<Map<String, Object>>> me() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(authentication -> authentication instanceof OAuth2AuthenticationToken)
                .cast(OAuth2AuthenticationToken.class)
                .flatMap(authentication -> authorizedClientService
                        .<OAuth2AuthorizedClient>loadAuthorizedClient(
                                authentication.getAuthorizedClientRegistrationId(), authentication.getName()))
                .flatMap(client -> jwtValidator.validate(client.getAccessToken().getTokenValue()))
                .map(this::toBody)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    private Map<String, Object> toBody(JwtPrincipal principal) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("userId", principal.userId());
        body.put("email", principal.email());
        body.put("role", principal.role());
        body.put("roles", principal.roles());
        body.put("institutionId", principal.institutionId());
        body.put("campusId", principal.campusId());
        return body;
    }
}
