-- Alertas ambientales trazables (DEC-006, modelo canonico).
--
-- Cada alerta referencia el ambiente, la variable, el umbral activo que se excedio
-- y la medicion que disparo la condicion. No es una copia de la medicion: es un
-- hecho trazable basado en ella.
--
-- Estado por columnas (acknowledged_at/by), no por catalogo: es el modelo canonico
-- de 06-data/domain/ms-environment-monitoring.md. NULL en acknowledged_at significa
-- alerta activa.
CREATE TABLE environment_monitoring.environment_alert (
    environment_alert_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    educational_environment_id UUID NOT NULL,
    variable_id               UUID NOT NULL,
    variable_threshold_id     UUID NOT NULL,
    triggering_measurement_id UUID NOT NULL,
    raised_at                 TIMESTAMPTZ NOT NULL DEFAULT now(),
    acknowledged_at           TIMESTAMPTZ,
    acknowledged_by           UUID,
    CONSTRAINT ck_environment_alert_acknowledge
        CHECK ((acknowledged_at IS NULL AND acknowledged_by IS NULL)
               OR (acknowledged_at IS NOT NULL))
);
