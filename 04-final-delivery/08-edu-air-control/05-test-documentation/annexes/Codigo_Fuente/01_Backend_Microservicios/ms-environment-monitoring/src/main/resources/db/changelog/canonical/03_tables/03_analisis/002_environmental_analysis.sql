-- Dominio: analisis historico por periodo (core de ms-environment-monitoring).
-- educational_environment_id y variable_id: referencias logicas a las replicas
-- locales del servicio; requested_by: referencia logica al dominio identity.
CREATE TABLE environment_monitoring.environmental_analysis (
    environmental_analysis_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    educational_environment_id UUID NOT NULL,
    period_start TIMESTAMPTZ NOT NULL,
    period_end   TIMESTAMPTZ NOT NULL,
    analysis_status_id UUID NOT NULL,
    requested_by UUID,
    computed_at  TIMESTAMPTZ,
    CONSTRAINT ck_environmental_analysis_period CHECK (period_start < period_end)
);
