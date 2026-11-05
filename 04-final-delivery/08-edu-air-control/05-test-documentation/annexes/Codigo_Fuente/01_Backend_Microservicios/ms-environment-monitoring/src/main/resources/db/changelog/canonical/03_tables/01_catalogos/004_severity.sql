-- Catalogo de severidades de alerta (ADR-014).
-- Sembrado con INFO, WARNING y CRITICAL. Los umbrales referencian esta tabla por
-- FK; nunca se borran severidades, solo se dejan de usar.
CREATE TABLE environment_monitoring.severity (
    severity_id UUID PRIMARY KEY,
    code        VARCHAR(20) NOT NULL UNIQUE,
    name        VARCHAR(40) NOT NULL,
    level       SMALLINT NOT NULL UNIQUE
);
