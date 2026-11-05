package com.eduaircontrol.mssensor.application;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.SensorModel;
import com.eduaircontrol.mssensor.domain.port.out.SensorModelRepository;
import com.eduaircontrol.mssensor.domain.port.out.SensorRepository;
import com.eduaircontrol.mssensor.shared.exception.ConflictException;
import com.eduaircontrol.mssensor.shared.exception.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SensorModelService {

    private final SensorModelRepository repository;
    private final SensorRepository guardRepository;

    @Transactional(readOnly = true)
    public PageResult<SensorModel> list(String query, int page, int limit) {
        return repository.search(query, page, limit);
    }

    @Transactional(readOnly = true)
    public SensorModel get(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("SensorModel not found: " + id));
    }

    public SensorModel create(String code, String name, String description) {
        String normalizedCode = SensorService.requireText(code, "code").toUpperCase();
        if (repository.existsByCode(normalizedCode)) {
            throw new ConflictException("SensorModel code already exists: " + normalizedCode);
        }
        SensorModel item = SensorModel.builder()
                .code(normalizedCode)
                .name(SensorService.requireText(name, "name"))
                .description(SensorService.emptyToNull(description))
                .build();
        return repository.save(item);
    }

    public void delete(UUID id) {
        SensorModel item = get(id);
        if (guardRepository.existsBySensorModelId(id)) {
            throw new ConflictException("Sensor model is referenced by sensors and cannot be deleted: " + id);
        }
        repository.delete(item);
    }
}
