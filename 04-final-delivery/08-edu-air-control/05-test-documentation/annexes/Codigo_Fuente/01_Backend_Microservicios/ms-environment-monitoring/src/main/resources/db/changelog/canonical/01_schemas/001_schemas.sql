-- Esquema propio del microservicio (ADR-003: cada servicio tiene su namespace).
CREATE SCHEMA IF NOT EXISTS environment_monitoring;

-- gen_random_uuid() viene de pgcrypto; el core de Postgres 15 ya lo expone, pero se
-- asegura la extension para no depender de la version de la imagen.
CREATE EXTENSION IF NOT EXISTS pgcrypto;
