package com.eduaircontrol.mssecurity.application;

import com.eduaircontrol.mssecurity.application.port.RoleRepository;
import com.eduaircontrol.mssecurity.domain.model.Role;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Garantiza el catálogo de roles (ADMIN, USER, VIEWER) al arrancar. Idempotente:
 * solo inserta los que falten. Fuente única de la semilla (ver decisions.md).
 */
@Component
@Order(1)
@RequiredArgsConstructor
public class RoleBootstrap implements ApplicationRunner {

    static final List<String> REQUIRED_ROLES = List.of(
            "SUPER_ADMIN", "ADMIN", "USER", "VIEWER");

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        for (String name : REQUIRED_ROLES) {
            if (roleRepository.findByName(name).isEmpty()) {
                Role role = new Role();
                role.setId(java.util.UUID.randomUUID());
                role.setName(name);
                role.setDescription(defaultDescription(name));
                role.setCreatedAt(Instant.now());
                roleRepository.save(role);
            }
        }
    }

    private String defaultDescription(String name) {
        return switch (name) {
            case "SUPER_ADMIN" -> "Global administrator (manages institutions)";
            case "ADMIN" -> "Institution administrator (full access)";
            case "USER" -> "Standard user";
            case "VIEWER" -> "Read-only user";
            default -> null;
        };
    }
}
