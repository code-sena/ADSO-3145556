package com.eduaircontrol.msusermanagement.application;

import com.eduaircontrol.msusermanagement.domain.model.PageResult;
import com.eduaircontrol.msusermanagement.domain.port.out.UserProfileRepository;
import com.eduaircontrol.msusermanagement.shared.exception.ConflictException;
import com.eduaircontrol.msusermanagement.shared.exception.NotFoundException;
import com.eduaircontrol.msusermanagement.shared.exception.ValidationException;
import com.eduaircontrol.msusermanagement.domain.model.RecordStatus;
import com.eduaircontrol.msusermanagement.domain.model.UserProfile;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Gestión de la información de perfil de los usuarios (HU-USER-001).
 * La identidad (credenciales, roles) pertenece a ms-security; aquí solo se
 * administra el perfil, referenciado por userId.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Transactional(readOnly = true)
    public PageResult<UserProfile> list(String query, RecordStatus status, int page, int limit) {
        return userProfileRepository.search(query, status, TenantContext.institutionId(), page, limit);
    }

    @Transactional(readOnly = true)
    public UserProfile get(UUID id) {
        return userProfileRepository.findById(id)
                .filter(profile -> !profile.isDeleted())
                .orElseThrow(() -> new NotFoundException("User profile not found: " + id));
    }

    @Transactional(readOnly = true)
    public UserProfile getByUserId(UUID userId) {
        return userProfileRepository.findByUserId(userId)
                .filter(profile -> !profile.isDeleted())
                .orElseThrow(() -> new NotFoundException("User profile not found for user: " + userId));
    }

    public UserProfile create(UUID userId, String fullName, String phone, String department,
            String position, String locale, String avatarUrl) {
        if (userId == null) {
            throw new ValidationException("userId must not be null");
        }
        if (userProfileRepository.existsByUserId(userId)) {
            throw new ConflictException("A profile already exists for user: " + userId);
        }
        UserProfile profile = UserProfile.builder()
                .userId(userId)
                .institutionId(TenantContext.institutionId())
                .fullName(requireText(fullName, "fullName"))
                .phone(emptyToNull(phone))
                .department(emptyToNull(department))
                .position(emptyToNull(position))
                .locale(emptyToNull(locale))
                .avatarUrl(emptyToNull(avatarUrl))
                .status(RecordStatus.ACTIVE)
                .build();
        return userProfileRepository.save(profile);
    }

    public UserProfile update(UUID id, String fullName, String phone, String department,
            String position, String locale, String avatarUrl, RecordStatus status) {
        UserProfile profile = get(id);
        if (fullName != null) {
            profile.setFullName(requireText(fullName, "fullName"));
        }
        if (phone != null) {
            profile.setPhone(emptyToNull(phone));
        }
        if (department != null) {
            profile.setDepartment(emptyToNull(department));
        }
        if (position != null) {
            profile.setPosition(emptyToNull(position));
        }
        if (locale != null) {
            profile.setLocale(emptyToNull(locale));
        }
        if (avatarUrl != null) {
            profile.setAvatarUrl(emptyToNull(avatarUrl));
        }
        if (status != null) {
            profile.setStatus(status);
        }
        return userProfileRepository.save(profile);
    }

    public void delete(UUID id) {
        UserProfile profile = get(id);
        profile.softDelete();
        userProfileRepository.save(profile);
    }

    static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(field + " must not be blank");
        }
        return value.trim();
    }

    static String emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
