package com.eduaircontrol.msclassroom.application;

import com.eduaircontrol.msclassroom.domain.model.PageResult;
import com.eduaircontrol.msclassroom.domain.port.out.EnvironmentTypeRepository;
import com.eduaircontrol.msclassroom.shared.exception.ConflictException;
import com.eduaircontrol.msclassroom.shared.exception.NotFoundException;
import com.eduaircontrol.msclassroom.domain.model.EnvironmentType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EnvironmentTypeService {

    private final EnvironmentTypeRepository environmentTypeRepository;

    @Transactional(readOnly = true)
    public PageResult<EnvironmentType> list(String query, int page, int limit) {
        return environmentTypeRepository.search(query, page, limit);
    }

    @Transactional(readOnly = true)
    public EnvironmentType get(UUID id) {
        return environmentTypeRepository.findById(id)
                .filter(type -> !type.isDeleted())
                .orElseThrow(() -> new NotFoundException("Environment type not found: " + id));
    }

    public EnvironmentType create(String code, String name, String description) {
        String normalizedCode = CampusService.requireText(code, "code").toUpperCase();
        String normalizedName = CampusService.requireText(name, "name");
        if (environmentTypeRepository.existsByCode(normalizedCode)) {
            throw new ConflictException("Environment type code already exists: " + normalizedCode);
        }
        if (environmentTypeRepository.existsByName(normalizedName)) {
            throw new ConflictException("Environment type name already exists: " + normalizedName);
        }
        EnvironmentType type = EnvironmentType.builder()
                .code(normalizedCode)
                .name(normalizedName)
                .description(CampusService.emptyToNull(description))
                .build();
        return environmentTypeRepository.save(type);
    }

    public EnvironmentType update(UUID id, String code, String name, String description) {
        EnvironmentType type = get(id);
        if (code != null) {
            String normalizedCode = CampusService.requireText(code, "code").toUpperCase();
            if (!type.getCode().equals(normalizedCode) && environmentTypeRepository.existsByCode(normalizedCode)) {
                throw new ConflictException("Environment type code already exists: " + normalizedCode);
            }
            type.setCode(normalizedCode);
        }
        if (name != null) {
            String normalizedName = CampusService.requireText(name, "name");
            if (!type.getName().equals(normalizedName) && environmentTypeRepository.existsByName(normalizedName)) {
                throw new ConflictException("Environment type name already exists: " + normalizedName);
            }
            type.setName(normalizedName);
        }
        if (description != null) {
            type.setDescription(CampusService.emptyToNull(description));
        }
        return environmentTypeRepository.save(type);
    }

    public void delete(UUID id) {
        EnvironmentType type = get(id);
        type.softDelete();
        environmentTypeRepository.save(type);
    }
}
