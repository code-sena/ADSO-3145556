-- Umbrales de advertencia por variable y tipo de ambiente (ADR-014).
--
-- ADR-014 aceptado: el umbral es por (variable_id, environment_type_id,
-- severity_id). NO existe educational_environment_id: la especificacion lo
-- rechaza explicitamente. La precedencia ambiente > tipo del monolito queda
-- eliminada.
--
-- Los umbrales nunca se borran; se cierran con valid_to. Sin solape para la misma
-- combinacion en un mismo rango de fechas.
CREATE TABLE environment_monitoring.variable_threshold (
    variable_threshold_id UUID PRIMARY KEY,
    variable_id           UUID NOT NULL,
    environment_type_id   UUID NOT NULL,
    min_value             NUMERIC(12,4),
    max_value             NUMERIC(12,4),
    severity_id           UUID NOT NULL,
    valid_from            DATE NOT NULL,
    valid_to              DATE,
    CONSTRAINT ck_variable_threshold_values
        CHECK (min_value IS NULL OR max_value IS NULL OR min_value < max_value),
    CONSTRAINT ck_variable_threshold_validity
        CHECK (valid_to IS NULL OR valid_from < valid_to)
);
