-- Mediciones ambientales atomicas (modelo canonico §4.1).
--
-- Una fila = una instalacion + una variable + un instante. Prohibida la fila ancha.
-- El ambiente se resuelve por la proyeccion local (installation_projection), no se
-- duplica aqui: el umbral se evalua por tipo de ambiente en tiempo de consulta.
--
-- measured_at es el instante real de la medicion y nunca se reemplaza; created_at
-- es cuando el servicio la persistio. Son conceptos distintos.
CREATE TABLE environment_monitoring.environment_measurement (
    environment_measurement_id UUID PRIMARY KEY,
    sensor_installation_id     UUID NOT NULL,
    variable_id                UUID NOT NULL,
    measured_value             NUMERIC(12,4) NOT NULL,
    measured_at                TIMESTAMPTZ NOT NULL,
    quality_flag_id            UUID NOT NULL,
    created_at                 TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_environment_measurement_inst_var_time
        UNIQUE (sensor_installation_id, variable_id, measured_at)
);
