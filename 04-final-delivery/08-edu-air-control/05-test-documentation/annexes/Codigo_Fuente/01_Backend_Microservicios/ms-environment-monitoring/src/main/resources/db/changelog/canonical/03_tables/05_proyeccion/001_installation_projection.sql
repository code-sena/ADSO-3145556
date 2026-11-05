-- Proyeccion local de instalaciones (DEC-006 §4.3).
--
-- El servicio no posee sensor_installation, educational_environment ni
-- environment_type. Para evaluar umbrales necesita resolver
-- sensor_installation_id -> educational_environment_id -> environment_type_id
-- sin llamar a otros servicios en cada medicion.
--
-- Se alimenta por eventos (SensorInstalled/SensorRemoved) y se refresca
-- periodicamente. Es un cache, no la fuente de verdad: si falta una fila, la
-- medicion se procesa sin evaluacion de umbral y se registra el hueco.
CREATE TABLE environment_monitoring.installation_projection (
    sensor_installation_id     UUID PRIMARY KEY,
    educational_environment_id UUID NOT NULL,
    environment_type_id        UUID NOT NULL,
    sensor_id                  UUID NOT NULL,
    removed_at                 TIMESTAMPTZ,
    synced_at                  TIMESTAMPTZ NOT NULL DEFAULT now()
);
