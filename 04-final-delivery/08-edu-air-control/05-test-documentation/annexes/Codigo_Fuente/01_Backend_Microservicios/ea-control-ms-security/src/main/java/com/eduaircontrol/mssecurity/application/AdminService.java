package com.eduaircontrol.mssecurity.application;

import com.eduaircontrol.mssecurity.application.port.RoleRepository;
import com.eduaircontrol.mssecurity.application.port.UserRepository;
import com.eduaircontrol.mssecurity.application.port.UserRoleRepository;
import com.eduaircontrol.mssecurity.domain.model.Role;
import com.eduaircontrol.mssecurity.domain.model.User;
import com.eduaircontrol.mssecurity.domain.model.UserRole;
import com.eduaircontrol.mssecurity.infrastructure.inbound.web.dto.UserSummary;
import com.eduaircontrol.mssecurity.domain.exception.NotFoundException;
import com.eduaircontrol.mssecurity.domain.exception.ValidationException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Gestion global de usuarios y roles (solo SUPER_ADMIN).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<UserSummary> listUsers() {
        return userRepository.findAll().stream()
                .filter(User::isActive)
                .map(user -> new UserSummary(
                        user.getId(),
                        user.getEmail(),
                        user.getUsername(),
                        roleNames(user.getId()),
                        List.of(),
                        user.getInstitutionId(),
                        user.getCampusId()))
                .toList();
    }

    public void assignRoles(UUID userId, List<String> roleNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        // Remover roles existentes
        List<UserRole> existing = userRoleRepository.findByUserId(userId);
        existing.forEach(ur -> userRoleRepository.delete(ur));

        // Asignar nuevos roles
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new ValidationException("Role not found: " + roleName));
            UserRole ur = new UserRole(userId, role.getId(), Instant.now());
            userRoleRepository.save(ur);
        }
    }

    @Transactional(readOnly = true)
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    private List<String> roleNames(UUID userId) {
        return userRoleRepository.findByUserId(userId).stream()
                .map(ur -> roleRepository.findById(ur.getRoleId())
                        .map(Role::getName)
                        .orElse(null))
                .filter(java.util.Objects::nonNull)
                .toList();
    }
}
