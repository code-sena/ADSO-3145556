package com.eduaircontrol.msmonitoring.infrastructure.messaging;

import com.eduaircontrol.msmonitoring.shared.contract.VariableCatalogPort;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Aplana el sobre {@code EnvironmentalDataRecorded} a lecturas atomicas.
 *
 * <p>El evento transporta un campo por variable, pero el read model guarda una fila
 * por variable e instante (regla 4.1: prohibida la fila ancha). Esta clase es la
 * unica que conoce esa conversion.
 *
 * <p>Las variables ausentes simplemente no generan fila: el dispositivo puede
 * reportar solo algunas, y no inventar un cero seria falsear el analisis.
 */
@Component
public class EnvironmentalReadingMapper {

    /**
     * Campo del payload -> codigo del catalogo de variables.
     *
     * <p>El evento llama a la variable de ruido {@code noiseLevel} y el catalogo la
     * llama {@code noise}. El alias se resuelve aqui, en un solo sitio, para que el
     * contrato del evento y el catalogo puedan evolucionar por separado.
     */
    private static final Map<String, String> FIELD_TO_VARIABLE_CODE = Map.of(
            "temperature", "temperature",
            "humidity", "humidity",
            "co2", "co2",
            "noiseLevel", "noise");

    private final VariableCatalogPort variableCatalog;

    public EnvironmentalReadingMapper(VariableCatalogPort variableCatalog) {
        this.variableCatalog = variableCatalog;
    }

    public List<Reading> toReadings(EnvironmentalDataRecorded event) {
        EnvironmentalDataRecorded.Payload payload = event.payload();
        if (payload == null || payload.environmentId() == null) {
            return List.of();
        }

        List<Reading> readings = new ArrayList<>(FIELD_TO_VARIABLE_CODE.size());
        readings.addAll(resolve(payload.environmentId(), "temperature",
                payload.temperature(), event.occurredAt()));
        readings.addAll(resolve(payload.environmentId(), "humidity",
                payload.humidity(), event.occurredAt()));
        readings.addAll(resolve(payload.environmentId(), "co2",
                payload.co2(), event.occurredAt()));
        readings.addAll(resolve(payload.environmentId(), "noiseLevel",
                payload.noiseLevel(), event.occurredAt()));
        return readings;
    }

    private List<Reading> resolve(UUID environmentId, String field,
                                  BigDecimal value, Instant measuredAt) {
        if (value == null) {
            return List.of();
        }
        String code = FIELD_TO_VARIABLE_CODE.get(field);
        UUID variableId = variableCatalog.findByCode(code)
                .map(VariableCatalogPort.VariableRef::id)
                // Sin id de variable la lectura no se puede atribuir: descartarla es
                // preferible a guardarla huerfana.
                .orElse(null);
        if (variableId == null) {
            return List.of();
        }
        return List.of(new Reading(environmentId, variableId, code, value, measuredAt));
    }

    public record Reading(UUID environmentId, UUID variableId, String variableCode,
                          BigDecimal value, Instant measuredAt) {
    }
}