package com.eduaircontrol.msusermanagement.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Autenticación en el servicio.
 *
 * <p>Prioriza los headers internos que añade el api-gateway (ADR-006/017):
 * {@code X-User-Id} y {@code X-User-Role}. Si no vienen (llamada directa), valida
 * el JWT RS256 contra el JWKS de ms-security.
 *
 * <p><b>Deuda conocida:</b> los headers se aceptan sin comprobar su origen, así que
 * quien alcance el puerto del servicio puede suplantarse. Se cierra con red
 * solo-gateway, mTLS o secreto compartido de headers.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader("X-User-Id");
        String role = request.getHeader("X-User-Role");
        if (userId != null && !userId.isBlank()) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            if (role != null && !role.isBlank()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userId, null, authorities));
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        try {
            JwtService.AuthenticatedUser user = jwtService.authenticate(token);
            List<SimpleGrantedAuthority> authorities = user.roles().stream()
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .toList();
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            user.email() != null ? user.email() : String.valueOf(user.userId()),
                            null, authorities));
        } catch (Exception e) {
            log.debug("Invalid token: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
