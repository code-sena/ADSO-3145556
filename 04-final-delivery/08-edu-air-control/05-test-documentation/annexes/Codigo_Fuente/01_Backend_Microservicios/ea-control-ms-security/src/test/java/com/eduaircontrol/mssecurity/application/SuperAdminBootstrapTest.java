package com.eduaircontrol.mssecurity.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eduaircontrol.mssecurity.application.port.RoleRepository;
import com.eduaircontrol.mssecurity.application.port.UserRepository;
import com.eduaircontrol.mssecurity.application.port.UserRoleRepository;
import com.eduaircontrol.mssecurity.domain.model.Role;
import com.eduaircontrol.mssecurity.domain.model.User;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

class SuperAdminBootstrapTest {

    private static final String EMAIL = "admin@eduaircontrol.com";
    private static final String PASSWORD = "SecretPass123!";

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserRoleRepository userRoleRepository;
    private PasswordEncoder passwordEncoder;
    private SuperAdminBootstrap bootstrap;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userRoleRepository = mock(UserRoleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        bootstrap = new SuperAdminBootstrap(
                userRepository, roleRepository, userRoleRepository, passwordEncoder);
        ReflectionTestUtils.setField(bootstrap, "email", EMAIL);
        ReflectionTestUtils.setField(bootstrap, "username", "superadmin");
        ReflectionTestUtils.setField(bootstrap, "password", PASSWORD);
    }

    private Role superAdminRole() {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName("SUPER_ADMIN");
        return role;
    }

    @Test
    void createsSuperAdminWithRoleWhenMissing() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(roleRepository.findByName("SUPER_ADMIN")).thenReturn(Optional.of(superAdminRole()));
        when(passwordEncoder.encode(PASSWORD)).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        bootstrap.run(null);

        verify(userRepository).save(argThat(user -> emailAndEncodedPassword(user)));
        verify(userRoleRepository).save(argThat(userRole ->
                userRole.getAssignedAt() != null));
    }

    @Test
    void skipsWhenSuperAdminAlreadyExists() {
        User existing = new User();
        existing.setId(UUID.randomUUID());
        existing.setEmail(EMAIL);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(existing));

        bootstrap.run(null);

        verify(userRepository, never()).save(any());
        verify(userRoleRepository, never()).save(any());
    }

    private boolean emailAndEncodedPassword(User user) {
        return user.getEmail().equals(EMAIL)
                && user.getUsername().equals("superadmin")
                && user.getPasswordHash().equals("encoded")
                && user.getInstitutionId() == null;
    }
}