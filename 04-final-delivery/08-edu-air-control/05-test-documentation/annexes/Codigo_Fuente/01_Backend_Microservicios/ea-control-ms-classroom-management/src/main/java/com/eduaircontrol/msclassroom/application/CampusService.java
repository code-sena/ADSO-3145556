package com.eduaircontrol.msclassroom.application;

import com.eduaircontrol.msclassroom.domain.model.PageResult;
import com.eduaircontrol.msclassroom.domain.port.out.CampusRepository;
import com.eduaircontrol.msclassroom.shared.exception.ConflictException;
import com.eduaircontrol.msclassroom.shared.exception.NotFoundException;
import com.eduaircontrol.msclassroom.shared.exception.ValidationException;
import com.eduaircontrol.msclassroom.domain.model.Campus;
import com.eduaircontrol.msclassroom.domain.model.RecordStatus;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CampusService {

    private final CampusRepository campusRepository;

    @Transactional(readOnly = true)
    public PageResult<Campus> list(String query, RecordStatus status, int page, int limit) {
        return campusRepository.search(query, status, TenantContext.institutionId(), page, limit);
    }

    @Transactional(readOnly = true)
    public Campus get(UUID id) {
        return campusRepository.findById(id)
                .filter(campus -> !campus.isDeleted())
                .orElseThrow(() -> new NotFoundException("Campus not found: " + id));
    }

    public Campus create(String code, String name, String city, RecordStatus status) {
        String normalizedCode = requireText(code, "code").toUpperCase();
        if (campusRepository.existsByCode(normalizedCode)) {
            throw new ConflictException("Campus code already exists: " + normalizedCode);
        }
        Campus campus = Campus.builder()
                .code(normalizedCode)
                .name(requireText(name, "name"))
                .city(emptyToNull(city))
                .status(status != null ? status : RecordStatus.ACTIVE)
                .institutionId(TenantContext.institutionId())
                .build();
        return campusRepository.save(campus);
    }

    public Campus update(UUID id, String code, String name, String city, RecordStatus status) {
        Campus campus = get(id);
        if (code != null) {
            String normalizedCode = requireText(code, "code").toUpperCase();
            if (!campus.getCode().equals(normalizedCode) && campusRepository.existsByCode(normalizedCode)) {
                throw new ConflictException("Campus code already exists: " + normalizedCode);
            }
            campus.setCode(normalizedCode);
        }
        if (name != null) {
            campus.setName(requireText(name, "name"));
        }
        if (city != null) {
            campus.setCity(emptyToNull(city));
        }
        if (status != null) {
            campus.setStatus(status);
        }
        return campusRepository.save(campus);
    }

    public void delete(UUID id) {
        Campus campus = get(id);
        campus.softDelete();
        campusRepository.save(campus);
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
