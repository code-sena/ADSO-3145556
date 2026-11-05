# ms-environment-monitoring

Microservicio de **monitoreo ambiental y análisis histórico** (puerto `3003`, esquema
PostgreSQL `environment_monitoring`). Dueño del pipeline:

```text
Mediciones → Umbrales → Análisis → Alertas
```

Alineado con ADR-002 (límites), ADR-003 (esquema propio), ADR-004 (canales),
ADR-006 (JWT + RBAC), ADR-008 (Liquibase), ADR-009 (hexagonal), ADR-014
(umbrales aceptado) y DEC-006 (decisiones §12).

## Responsabilidad

- Recibir y almacenar mediciones históricas (`environment_measurement`).
- Evaluar umbrales por variable y tipo de ambiente (solo `environment_type_id`).
- Calcular análisis por periodo (`DAY`, `WEEK`, `MONTH`, `YEAR`).
- Crear y gestionar alertas trazables (`environment_alert`).
- Publicar `EnvironmentalDataAnalyzed` para el dashboard.

No posee ambientes, tipos, variables ni sensores: los referencia por UUID y los
resuelve por eventos y réplicas locales.

## Contrato

`07-api/contracts/openapi/ms-environment-monitoring.yaml` (repositorio de documentación).

| Método | Ruta | Propósito |
|--------|------|-----------|
| POST | `/api/v1/measurements` | Ingesta HTTP alternativa (HU-MON-001) |
| GET | `/api/v1/environments/{id}/current` | Últimos valores por variable (HU-MON-003) |
| GET | `/api/v1/environments/{id}/measurements` | Historial paginado (HU-MON-004) |
| POST | `/api/v1/analyses` | Solicitar análisis de un periodo |
| GET | `/api/v1/analyses` | Listar análisis |
| GET | `/api/v1/analyses/{id}` | Obtener un análisis |
| GET | `/api/v1/analyses/latest` | Último análisis completado |
| GET | `/api/v1/alerts` | Listar alertas |
| POST | `/api/v1/alerts/{id}/acknowledge` | Reconocer alerta |

## Eventos

Consume `EnvironmentalDataRecorded` de `ms-sensor-management`:
- Exchange `eduaircontrol.environmental-data`, routing key `environmental.data.recorded`.
- Idempotencia por `eventId`, 4 reintentos con backoff exponencial, DLQ con 7 días.

## Arranque local

Requiere PostgreSQL en `localhost:5432` y RabbitMQ en `localhost:5672`.

```bash
# PowerShell
$env:POSTGRES_USER = "eduaircontrol"
$env:POSTGRES_PASSWORD = "eduaircontrol"
$env:JWT_SECRET = "una-clave-de-al-menos-32-caracteres-aqui"

.\mvnw.cmd spring-boot:run
```

Liquibase crea el esquema `environment_monitoring` al arrancar.
`spring.jpa.hibernate.ddl-auto=validate` impide que Hibernate invente el esquema.

## Verificación

```bash
curl http://localhost:3003/health
```

## Backfill histórico

El seed de desarrollo inserta mediciones directamente por SQL, saltándose la ingesta
y el outbox. El backfill las copia al read model:

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--app.backfill.initial-run=true
```

Es idempotente y trocea en ventanas de 30 días. La resincronización periódica
(`app.backfill.enabled=true`) cierra huecos del broker cada 6h.

## Estado

| Fase | Estado |
|---|---|
| 0 · Decisiones §12 + ADR-014 | Completada |
| 1 · Scaffold + Liquibase + seeds | Completada |
| 2 · Mediciones + umbrales + alertas | Completada |
| 3 · Análisis (dominio reutilizado) | Completada |
| 4 · RabbitMQ + DLQ | Completada |
| 5 · API REST | Completada |
| 6 · Tests (86) + OpenAPI | Completada |
| 7 · Docker + compose + E2E | Pendiente |
| JWT RS256 | Diferido (HS256 funciona sin gateway) |
| SSE tiempo real | Diferido a v2 |
| MQTT directo | Diferido a v2 (HTTP + RabbitMQ cubren) |
