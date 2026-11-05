package com.eduaircontrol.mssecurity.infrastructure.security;

import io.jsonwebtoken.Claims;
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

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);

        try {
            Claims claims = jwtService.parse(token);
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Object rolesClaim = claims.get("roles");
            if (rolesClaim instanceof List<?> roles) {
                for (Object role : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.setAttribute("jwtJti", claims.getId());
            request.setAttribute("jwtExp", claims.getExpiration() == null
                    ? null
                    : claims.getExpiration().toInstant());
        } catch (Exception e) {
            log.debug("Invalid token: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
