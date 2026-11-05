package com.eduaircontrol.msmonitoring.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Autentica por tres vias, en orden:
 *
 * <ol>
 *   <li>Headers {@code X-User-*} del gateway (ya valido el JWT).</li>
 *   <li>JWT RS256 via JWKS de ms-security (ADR-006).</li>
 *   <li>JWT HS256 con secreto compartido (local y tests).</li>
 * </ol>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;
    private final ObjectProvider<JwtDecoder> jwtDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. Headers de identidad del gateway.
        String gatewayUserId = request.getHeader("X-User-Id");
        if (gatewayUserId != null && !gatewayUserId.isBlank()) {
            String role = request.getHeader("X-User-Role");
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            gatewayUserId,
                            null,
                            List.of(new SimpleGrantedAuthority(
                                    "ROLE_" + (role != null && !role.isBlank() ? role : "USER"))));
            auth.setDetails(gatewayUserId);
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        // 2. RS256 via JWKS.
        JwtDecoder decoder = jwtDecoder.getIfAvailable();
        if (decoder != null) {
            try {
                var jwt = decoder.decode(token);
                String subject = jwt.getSubject();
                String role = jwt.getClaimAsString("role");
                if (role == null || role.isBlank()) {
                    role = "USER";
                }
                String userId = jwt.getClaimAsString("userId");
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                subject != null ? subject : "unknown",
                                token,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role)));
                auth.setDetails(userId);
                SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
                return;
            } catch (Exception e) {
                log.debug("RS256 no valido, intento HS256: {}", e.getMessage());
            }
        }

        // 3. HS256 con secreto compartido.
        try {
            String email = jwtService.extractEmail(token);
            String role = jwtService.extractRole(token);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            token,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role)));
            auth.setDetails(null);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            log.debug("Token invalido: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    @SuppressWarnings("unused")
    private UUID parseUserId(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(raw);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
