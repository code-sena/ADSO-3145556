package com.eduaircontrol.mssensor.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sensor_variable", schema = "sensors")
@IdClass(SensorVariableId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorVariable {

    @Id
    @Column(name = "sensor_id")
    private UUID sensorId;

    @Id
    @Column(name = "variable_id")
    private UUID variableId;
}
