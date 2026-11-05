package com.eduaircontrol.mssecurity.application;

import com.eduaircontrol.mssecurity.application.port.InstitutionRepository;
import com.eduaircontrol.mssecurity.domain.exception.ConflictException;
import com.eduaircontrol.mssecurity.domain.exception.NotFoundException;
import com.eduaircontrol.mssecurity.domain.exception.ValidationException;
import com.eduaircontrol.mssecurity.domain.model.Institution;
import com.eduaircontrol.mssecurity.domain.model.InstitutionStatus;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Gestión de instituciones (tenants). Solo accesible por SUPER_ADMIN (ADR-016).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class InstitutionService {

    private final InstitutionRepository institutionRepository;

    @Transactional(readOnly = true)
    public List<Institution> list() {
        return institutionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Institution get(UUID id) {
        return institutionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Institution not found: " + id));
    }

    public Institution create(String code, String name, String type) {
        String normalizedCode = requireText(code, "code").toUpperCase();
        if (institutionRepository.existsByCode(normalizedCode)) {
            throw new ConflictException("INSTITUTION_CODE_EXISTS",
                    "Institution code already exists: " + normalizedCode);
        }
        Institution institution = Institution.builder()
                .code(normalizedCode)
                .name(requireText(name, "name"))
                .type(emptyToNull(type))
                .status(InstitutionStatus.ACTIVE)
                .build();
        return institutionRepository.save(institution);
    }

    public Institution update(UUID id, String name, String type, InstitutionStatus status) {
        Institution institution = get(id);
        if (name != null) {
            institution.setName(requireText(name, "name"));
        }
        if (type != null) {
            institution.setType(emptyToNull(type));
        }
        if (status != null) {
            institution.setStatus(status);
        }
        return institutionRepository.save(institution);
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
