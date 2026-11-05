package com.eduaircontrol.mssensor.application;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.SensorStatus;
import com.eduaircontrol.mssensor.domain.port.out.SensorStatusRepository;
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
public class SensorStatusService {

    private final SensorStatusRepository repository;
    private final SensorRepository guardRepository;

    @Transactional(readOnly = true)
    public PageResult<SensorStatus> list(String query, int page, int limit) {
        return repository.search(query, page, limit);
    }

    @Transactional(readOnly = true)
    public SensorStatus get(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("SensorStatus not found: " + id));
    }

    public SensorStatus create(String code, String name) {
        String normalizedCode = SensorService.requireText(code, "code").toUpperCase();
        if (repository.existsByCode(normalizedCode)) {
            throw new ConflictException("SensorStatus code already exists: " + normalizedCode);
        }
        SensorStatus item = SensorStatus.builder()
                .code(normalizedCode)
                .name(SensorService.requireText(name, "name"))
                .build();
        return repository.save(item);
    }

    public void delete(UUID id) {
        SensorStatus item = get(id);
        if (guardRepository.existsBySensorStatusId(id)) {
            throw new ConflictException("Sensor status is referenced by sensors and cannot be deleted: " + id);
        }
        repository.delete(item);
    }
}
