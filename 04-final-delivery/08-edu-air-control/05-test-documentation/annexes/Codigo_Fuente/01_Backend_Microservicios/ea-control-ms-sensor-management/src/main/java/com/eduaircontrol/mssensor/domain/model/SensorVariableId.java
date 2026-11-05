package com.eduaircontrol.mssensor.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record SensorVariableId(UUID sensorId, UUID variableId) implements Serializable {
}
