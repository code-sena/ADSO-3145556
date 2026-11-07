# Non-Functional Requirements (NFR)

> These requirements define how well Save Your Water must perform.
> Every NFR has a metric and a validation method.
> Targets are calibrated for a university project deployed on a single VPS.

---

## NFR-001: Performance

| Flow | Metric | Condition |
|------|--------|-----------|
| `POST /auth/login` | p95 < 400 ms | Normal load |
| `GET /consumption/today` | p95 < 500 ms | Single place, 1 day of readings |
| `GET /consumption/history` | p95 < 1 s | 30-day range |
| `POST /valve/command` | p95 < 300 ms (API) + MQTT round-trip < 2 s | Device online |
| MQTT reading ingestion | < 500 ms from broker receive to DB persist | Normal flow |
| Service cold start | < 30 s | Docker on VPS |

**Validation method:** manual timing with Postman/k6 in staging. Automated load tests are aspirational for later phases.

---

## NFR-002: Availability

| Environment | Target | Notes |
|-------------|--------|-------|
| Production (VPS) | 99% uptime (~7 h downtime/month) | Realistic for single VPS, no redundancy |
| Local/Dev | Best effort | No SLO |

**Health checks (per service):**
- `GET /health` — liveness (process alive)
- `GET /health/ready` — readiness (DB connection, broker connection)

> **Note:** 99.9% availability requires redundant infrastructure. With a single VPS and a team of 3, 99% is a realistic and honest target.

---

## NFR-003: Scalability

| Scenario | Target |
|----------|--------|
| Concurrent users | Up to 50 simultaneous users without degradation |
| Connected devices | Up to 100 ESP32 devices reporting every 10 seconds |
| Readings per day | ~864,000 readings/day (100 devices × 6 readings/min × 1440 min) |
| Alert evaluation | Process all readings without queue backlog |

**Strategy:** stateless API services behind Docker containers. Vertical scaling on VPS initially; horizontal scaling documented as future evolution.

> **Note:** These are design targets. The university deployment will likely serve 5–10 devices during testing.

---

## NFR-004: Security

### Authentication and authorization
- All protected endpoints require a valid JWT in `Authorization: Bearer <token>`
- Access token expiration: **1 hour**
- Refresh token expiration: **7 days**
- Role-based access control (RBAC): User (base), Owner, Guest (anonymous, link-based, read-only — no authentication required), Collaborator (registered, partial permissions set by Owner), Admin
- Login attempt rate limiting: max 5 failed attempts per 15 minutes, then account is temporarily locked

### Data protection
- HTTPS mandatory in production (TLS termination at reverse proxy)
- Passwords hashed with bcrypt (cost factor ≥ 10)
- Secrets stored in environment variables, never in source code or version control
- Soft delete on all entities — no physical data deletion

### MQTT security
- MQTT broker requires username/password authentication per device
- Device credentials are unique per serial number
- MQTT topics follow the pattern `devices/{deviceId}/readings` — devices can only publish to their own topic

### Input validation
- All API inputs validated at the controller layer
- SQL injection prevention via parameterized queries / ORM
- XSS prevention in any rendered user content

---

## NFR-005: Reliability (IoT)

| Scenario | Expected behavior |
|----------|-------------------|
| Device disconnects | DeviceDisconnected event published; status changes to "offline"; readings stop |
| Device reconnects | DeviceReconnected event published; readings resume from current time (no backfill) |
| MQTT broker restarts | Devices auto-reconnect (ESP32 firmware retry loop); QoS 1 ensures at-least-once delivery |
| Duplicate reading received | Idempotent processing — same timestamp + device ignored |
| Valve command fails | ValveCommandFailed event published; user notified; command can be retried |

**MQTT QoS levels:**
- Readings: QoS 1 (at-least-once) — duplicates are safe
- Valve commands: QoS 1 (at-least-once) — idempotent by design

---

## NFR-006: Observability

| Pillar | Requirement | Tool (planned) |
|--------|------------|----------------|
| Logs | Structured JSON logs with correlation ID per request | Logback (Spring) / Serilog (.NET) |
| Metrics | Request count, error rate, latency per endpoint | Prometheus + Grafana (aspirational) |
| Alerts | Log-based alerts for critical errors | Manual review initially |

**Correlation ID:** every HTTP request generates a UUID that propagates through logs and inter-service calls.

> **Note:** Full observability (traces, dashboards, alerting) is aspirational. Minimum for university delivery: structured logs with correlation ID.

---

## NFR-007: Maintainability

| Metric | Target |
|--------|--------|
| Unit test coverage (domain logic) | ≥ 80% |
| Code review | At least 1 reviewer per PR |
| Local environment setup | New developer can run the project in under 30 minutes |
| Documentation | All services documented in `09-microservices/` |
| Architecture | Hexagonal (Ports & Adapters) per service |

---

## NFR-008: Portability and Deployment

- All services packaged as Docker containers
- Environment configuration via environment variables (no hardcoded config)
- `docker-compose.yml` for local development (all services + SQL Server + Mosquitto)
- No dependency on host OS-specific behavior
- Deployable to any Linux VPS with Docker installed

---

## NFR priority matrix

| NFR | Area | Priority | Validation | Notes |
|-----|------|----------|-----------|-------|
| NFR-001 | Performance | P1 | Manual timing / k6 | Critical for user experience |
| NFR-004 | Security | P1 | Code review + manual testing | JWT + RBAC + input validation |
| NFR-005 | IoT Reliability | P1 | Integration tests with MQTT broker | Core to the product |
| NFR-008 | Deployment | P1 | Docker smoke test | Required for VPS deployment |
| NFR-002 | Availability | P2 | Health check monitoring | Realistic target: 99% |
| NFR-007 | Maintainability | P2 | Coverage reports + PR reviews | Team discipline |
| NFR-006 | Observability | P3 | Log review | Minimum: structured logs |
| NFR-003 | Scalability | P3 | Manual capacity check | Design for it, prove later |

---

## Correlations

- Product vision → `03-product/vision.md`
- User stories → `04-requirements/user-stories.md`
- Architecture decisions → `05-architecture/overview.md`
- IoT communication → `05-architecture/overview.md` (MQTT patterns section)
- Database strategy → `06-data/models.md`
- Testing strategy → `11-quality/testing-strategy.md`
