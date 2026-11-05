-- Replica local del catalogo de variables del dominio sensors.
-- Los UUID coinciden con los del monolito para que el backfill y los eventos
-- puedan referenciar la variable sin traduccion.
-- measurement_unit_id: referencia logica; la unidad se copia desnormalizada
-- porque el servicio solo la lee para etiquetar resultados.
CREATE TABLE environment_monitoring.variable (
    variable_id          UUID PRIMARY KEY,
    code                 VARCHAR(30) NOT NULL UNIQUE,
    name                 VARCHAR(80) NOT NULL,
    measurement_unit_id  UUID NOT NULL,
    unit_symbol          VARCHAR(10) NOT NULL,
    created_at           TIMESTAMPTZ NOT NULL DEFAULT now()
);
