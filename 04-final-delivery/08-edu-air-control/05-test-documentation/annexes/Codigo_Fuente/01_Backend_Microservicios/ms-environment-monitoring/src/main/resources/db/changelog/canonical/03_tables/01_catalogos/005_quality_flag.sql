-- Catalogo de calidad de medicion (DEC-006).
-- Catálogo local propio: VALID, SUSPECT, BAD. No se referencia al monolito.
CREATE TABLE environment_monitoring.quality_flag (
    quality_flag_id UUID PRIMARY KEY,
    code            VARCHAR(20) NOT NULL UNIQUE,
    name            VARCHAR(60) NOT NULL
);
