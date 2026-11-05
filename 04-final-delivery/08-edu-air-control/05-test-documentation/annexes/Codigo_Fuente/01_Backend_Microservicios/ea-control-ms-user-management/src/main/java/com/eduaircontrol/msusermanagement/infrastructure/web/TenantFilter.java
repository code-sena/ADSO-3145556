package com.eduaircontrol.msusermanagement.infrastructure.web;

import com.eduaircontrol.msusermanagement.application.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Lee los headers de tenant del api-gateway (X-Institution-Id, X-Campus-Id) y
 * los publica en {@link TenantContext} (ADR-016).
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            TenantContext.set(parse(request.getHeader("X-Institution-Id")),
                    parse(request.getHeader("X-Campus-Id")));
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

    private UUID parse(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
