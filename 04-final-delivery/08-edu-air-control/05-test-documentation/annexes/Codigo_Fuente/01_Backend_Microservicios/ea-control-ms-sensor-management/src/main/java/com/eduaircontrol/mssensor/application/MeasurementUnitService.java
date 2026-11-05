package com.eduaircontrol.mssensor.application;

import com.eduaircontrol.mssensor.domain.model.PageResult;
import com.eduaircontrol.mssensor.domain.model.MeasurementUnit;
import com.eduaircontrol.mssensor.domain.port.out.MeasurementUnitRepository;
import com.eduaircontrol.mssensor.domain.port.out.VariableRepository;
import com.eduaircontrol.mssensor.shared.exception.ConflictException;
import com.eduaircontrol.mssensor.shared.exception.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MeasurementUnitService {

    private final MeasurementUnitRepository repository;
    private final VariableRepository guardRepository;

    @Transactional(readOnly = true)
    public PageResult<MeasurementUnit> list(String query, int page, int limit) {
        return repository.search(query, page, limit);
    }

    @Transactional(readOnly = true)
    public MeasurementUnit get(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("MeasurementUnit not found: " + id));
    }

    public MeasurementUnit create(String code, String symbol, String name) {
        String normalizedCode = SensorService.requireText(code, "code").toUpperCase();
        if (repository.existsByCode(normalizedCode)) {
            throw new ConflictException("MeasurementUnit code already exists: " + normalizedCode);
        }
        MeasurementUnit item = MeasurementUnit.builder()
                .code(normalizedCode)
                .symbol(SensorService.requireText(symbol, "symbol"))
                .name(SensorService.requireText(name, "name"))
                .build();
        return repository.save(item);
    }

    public void delete(UUID id) {
        MeasurementUnit item = get(id);
        if (guardRepository.existsByMeasurementUnitId(id)) {
            throw new ConflictException("Measurement unit is referenced by variables and cannot be deleted: " + id);
        }
        repository.delete(item);
    }
}
