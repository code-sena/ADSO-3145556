package com.eduaircontrol.mssecurity.infrastructure.security;

import com.eduaircontrol.mssecurity.application.port.RoleRepository;
import com.eduaircontrol.mssecurity.application.port.UserRepository;
import com.eduaircontrol.mssecurity.application.port.UserRoleRepository;
import com.eduaircontrol.mssecurity.domain.model.Role;
import com.eduaircontrol.mssecurity.domain.model.User;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * Autentica correo + contraseña contra los usuarios locales. Usado por el
 * formulario de login del Authorization Server (ADR-017).
 */
@Component
@RequiredArgsConstructor
public class CredentialsAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials() == null ? null
                : authentication.getCredentials().toString();

        User user = userRepository.findByEmail(email)
                .filter(User::isActive)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (password == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        List<SimpleGrantedAuthority> authorities = rolesOf(user.getId()).stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
        SecurityUser principal = new SecurityUser(user.getId(), user.getInstitutionId(),
                user.getCampusId(), user.getEmail(), user.getPasswordHash(), authorities);
        UsernamePasswordAuthenticationToken result =
                new UsernamePasswordAuthenticationToken(principal, null, authorities);
        // Normaliza los detalles a WebAuthenticationDetails estándar: es el tipo
        // que Spring Authorization Server puede serializar en los atributos OAuth2.
        if (authentication.getDetails() instanceof WebAuthenticationDetails details) {
            result.setDetails(new WebAuthenticationDetails(details.getRemoteAddress(), details.getSessionId()));
        }
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private List<String> rolesOf(UUID userId) {
        return userRoleRepository.findByUserId(userId).stream()
                .map(userRole -> roleRepository.findById(userRole.getRoleId()).map(Role::getName).orElse(null))
                .filter(Objects::nonNull)
                .sorted()
                .toList();
    }
}
