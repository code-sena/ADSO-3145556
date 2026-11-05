-- Registro de mensajes ya procesados, para idempotencia del consumidor.
-- Se apoya en message_id (el id del evento en el origen) y no en la clave
-- natural del mensaje, de modo que un redelivery del mismo evento se descarta
-- aunque el contenido haya cambiado.
CREATE TABLE environment_monitoring.inbox_message (
    message_id   UUID PRIMARY KEY,
    consumer     VARCHAR(60) NOT NULL,
    message_type VARCHAR(80) NOT NULL,
    received_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
