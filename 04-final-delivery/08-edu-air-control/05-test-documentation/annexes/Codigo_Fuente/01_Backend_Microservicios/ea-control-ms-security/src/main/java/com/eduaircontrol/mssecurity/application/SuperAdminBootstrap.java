package com.eduaircontrol.mssecurity.application;

import com.eduaircontrol.mssecurity.application.port.RoleRepository;
import com.eduaircontrol.mssecurity.application.port.UserRepository;
import com.eduaircontrol.mssecurity.application.port.UserRoleRepository;
import com.eduaircontrol.mssecurity.domain.model.Role;
import com.eduaircontrol.mssecurity.domain.model.User;
import com.eduaircontrol.mssecurity.domain.model.UserRole;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Garantiza la cuenta SUPER_ADMIN al arrancar (único seed del servicio, ver
 * decisions.md). Idempotente: no crea la cuenta si el email ya existe. Las
 * instituciones se crean desde esta cuenta via /api/v1/institutions.
 */
@Component
@Order(2)
@RequiredArgsConstructor
public class SuperAdminBootstrap implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.superadmin.email}")
    private String email;

    @Value("${app.superadmin.username}")
    private String username;

    @Value("${app.superadmin.password}")
    private String password;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.findByEmail(email).isPresent()) {
            return;
        }
        Role superAdminRole = roleRepository.findByName("SUPER_ADMIN")
                .orElseThrow(() -> new IllegalStateException(
                        "Required role SUPER_ADMIN is not seeded"));

        Instant now = Instant.now();
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setEmailVerified(true);
        user.setFailedAttempts(0);
        user.setInstitutionId(null);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user = userRepository.save(user);

        userRoleRepository.save(new UserRole(user.getId(), superAdminRole.getId(), now));
    }
}