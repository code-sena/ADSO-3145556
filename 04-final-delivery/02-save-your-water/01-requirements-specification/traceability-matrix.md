# Traceability Matrix

> Maps each functional requirement to its user stories, owning service, and delivery.
> Answers: what validates each requirement and which service implements it.
>
> **Updated:** 2026-10-15 — aligned with Product Backlog v2.1 (69 HUs: 40 implemented in the current release, 29 kept as Version 2 improvements).

---

## How to read this matrix

```
FR (functional requirement) → HU (user stories) → Service → Entrega

If an FR has no HU → the requirement is not planned
If an HU has no test → there is test debt
If code exists without an HU → unplanned work (gold plating)
```

---

## Functional requirements catalog

| FR ID | Area | Description | Priority |
|-------|------|-------------|----------|
| FR-001 | Authentication | Users register, verify account, login (email + Google), recover password, logout, and manage sessions | Must Have |
| ~~FR-002~~ | ~~Place management~~ | ~~First place registration as part of onboarding~~ — Covered by FR-004 (HU-009 crear lugar) | — |
| FR-003 | Account management | Users view/edit profile and delete account (soft delete) | Should Have |
| FR-004 | Place management | Owners create, list, switch between, edit, and delete places | Must Have |
| FR-005 | Device linking | Owners link/unlink ESP32 devices to places via serial number and MQTT | Must Have |
| FR-006 | Device monitoring | Users view device status (online/offline, 10-min threshold) and connectivity info | Must Have |
| FR-007 | Real-time consumption | Dashboard shows accumulated consumption, period selector (day/week/month), chart, and live flow rate | Must Have |
| FR-008 | Data export | Users export consumption data as CSV | Should Have |
| FR-009 | Consumption goals | Owners set monthly goals and view progress toward them | Should Have |
| FR-010 | Leak detection | System detects continuous flow during inactivity and alerts the user | Must Have |
| FR-011 | Alerts & notifications | Notification center, push/email/WhatsApp channels, leak/goal/sensor/valve alerts, and user preferences | Must Have |
| FR-012 | Valve control | Owners open/close the electrovalve remotely with confirmation code | Must Have |
| FR-013 | Valve audit | Users view valve status and action history (who, when, why) | Should Have |
| FR-014a | Guest access (anonymous) | Owner generates shareable link; anonymous guests view consumption read-only without registration | Should Have |
| FR-014b | Collaborator access | Owner invites registered users with custom permissions; collaborators act within those permissions and receive alerts | Should Have |
| FR-015 | Bill management | Owners register bills, view history, compare against sensor measurements, and receive discrepancy/payment alerts | Should Have |
| FR-016 | Tariff configuration | Owners configure tariffs and units for cost estimation from sensor data; pre-loaded tariffs for Colombia | Should Have |
| FR-017 | Automatic shutoff | Rule-based valve closure with mandatory confirmation; manual reopen only | Should Have |
| FR-018 | Admin panel | Admin views users (no sensitive data), blocks/unblocks users, views devices and platform metrics, creates/manages recommendations; users view saving tips | Must Have |
| FR-019 | Reports | Period-based consumption reports with charts and PDF export | Could Have |
| ~~FR-020~~ | ~~Multi-place~~ | ~~Users manage multiple places with place switcher and aggregated consumption~~ — Covered by E2 (HU-009 crear lugar, HU-010 listar/cambiar entre lugares) | — |

---

## FR → HU → Service traceability

### Entrega 1 — MVP

| FR | HU(s) | Service(s) | Test file (planned) | Status |
|----|-------|-----------|---------------------|--------|
| FR-001 | HU-001, HU-002, HU-003, HU-004, HU-005, HU-006 | ms-iam | `auth-register.spec`, `auth-verify.spec`, `auth-login.spec`, `auth-oauth.spec`, `auth-recovery.spec`, `auth-logout.spec` | Done (Version 2: HU-004) |
| FR-003 | HU-007, HU-008 | ms-iam | `profile-edit.spec`, `account-delete.spec` | Done |
| FR-004 | HU-009, HU-010, HU-011 | ms-places | `place-crud.spec`, `place-switch.spec` | Done |
| FR-005 | HU-012, HU-014 | ms-device | `device-link.spec`, `device-unlink.spec` | Done |
| FR-006 | HU-013 | ms-device | `device-status.spec` | Done |
| FR-007 | HU-015, HU-016, HU-017, HU-018 | ms-consumption, ms-device | `consumption-realtime.spec`, `consumption-period.spec`, `consumption-chart.spec`, `flow-rate.spec` | Done |

### Entrega 2 — Smart Monitoring

| FR | HU(s) | Service(s) | Test file (planned) | Status |
|----|-------|-----------|---------------------|--------|
| FR-008 | HU-038 | ms-consumption | `consumption-export.spec` | Version 2 |
| FR-009 | HU-023, HU-024 | ms-consumption | `goal-crud.spec`, `goal-progress.spec` | Version 2 |
| FR-010 | HU-029 | ms-consumption, ms-notification | `leak-detection.spec` | Version 2 |
| FR-011 | HU-025, HU-026, HU-027, HU-028, HU-029, HU-030, HU-031, HU-032, HU-033, HU-034 | ms-notification | `notification-center.spec`, `notification-push.spec`, `notification-email.spec`, `notification-whatsapp.spec`, `alert-leak.spec`, `alert-goal-exceeded.spec`, `alert-goal-preventive.spec`, `alert-sensor-offline.spec`, `alert-valve-closed.spec`, `notification-prefs.spec` | Done (Version 2: HU-028, HU-029, HU-030, HU-031) |
| FR-012 | HU-020, HU-021 | ms-valve, ms-device, ms-notification | `valve-close.spec`, `valve-open.spec` | Done |
| FR-013 | HU-019, HU-022 | ms-valve | `valve-status.spec`, `valve-history.spec` | Done |
| FR-017 | HU-035, HU-036, HU-037 | ms-valve, ms-notification | `shutoff-rule.spec`, `auto-shutoff.spec`, `auto-shutoff-reopen.spec` | Version 2 |

### Entrega 3 — Social & Billing

| FR | HU(s) | Service(s) | Test file (planned) | Status |
|----|-------|-----------|---------------------|--------|
| FR-014a | HU-039, HU-040, HU-041 | ms-places | `guest-link.spec`, `guest-view.spec`, `guest-restriction.spec` | Version 2 |
| FR-014b | HU-042, HU-043, HU-044, HU-045, HU-046, HU-047 | ms-places, ms-notification | `collaborator-invite.spec`, `collaborator-accept.spec`, `collaborator-permissions.spec`, `collaborator-enforce.spec`, `collaborator-revoke.spec`, `collaborator-alerts.spec` | Version 2 |
| FR-015 | HU-048, HU-049, HU-050, HU-051, HU-052 | ms-consumption, ms-notification | `bill-register.spec`, `bill-history.spec`, `bill-compare.spec`, `bill-alert.spec`, `bill-reminder.spec` | Version 2 |
| FR-016 | HU-053, HU-054, HU-055, HU-056 | ms-places, ms-consumption | `tariff-country.spec`, `tariff-manual.spec`, `tariff-units.spec`, `consumption-cost.spec` | Done |

### Entrega 4 — Administration & Analytics

| FR | HU(s) | Service(s) | Test file (planned) | Status |
|----|-------|-----------|---------------------|--------|
| FR-016 (ext.) | HU-066, HU-067, HU-068, HU-069 | ms-places, ms-consumption | `tariff-colombia.spec`, `tariff-ecuador.spec`, `tariff-usa.spec`, `tariff-auto.spec` | Done (Version 2: HU-067, HU-068) |
| FR-018 | HU-059, HU-060, HU-061, HU-062, HU-063, HU-064, HU-065 | ms-iam, ms-device, ms-places | `admin-users.spec`, `admin-block.spec`, `admin-devices.spec`, `admin-metrics.spec`, `admin-recommendations.spec`, `admin-edit-rec.spec`, `user-tips.spec` | Done |
| FR-019 | HU-057, HU-058 | ms-consumption | `report-period.spec`, `report-pdf.spec` | Version 2 |

---

## NFR → Validation traceability

| NFR | Description | Validation method | Tool | Status |
|-----|-------------|-------------------|------|--------|
| NFR-001 | API performance (p95 targets) | Manual timing / load test | Postman / k6 | Planned |
| NFR-002 | 99% availability | Health check monitoring | Docker health checks | Planned |
| NFR-003 | 50 concurrent users, 100 devices | Capacity test | k6 (aspirational) | Planned |
| NFR-004 | JWT + RBAC + input validation | Code review + manual test | Manual | Planned |
| NFR-005 | IoT reliability (reconnect, QoS, idempotency) | Integration test with MQTT | Mosquitto test client | Planned |
| NFR-006 | Structured logs with correlation ID | Log inspection | Manual | Planned |
| NFR-007 | 80% domain test coverage | Coverage report | JaCoCo / dotnet-coverage | Planned |
| NFR-008 | Docker deployment | Smoke test | docker-compose up | Planned |

---

## Inverse traceability: HU → FR

### Entrega 1 — MVP (E1 + E2 + E3 + E4)

| HU | Title | FR(s) | Entrega | Scope (v2.1) |
|----|-------|-------|---------|--------------|
| HU-001 | Registro con correo y contrasena | FR-001 | 1 | Current release |
| HU-002 | Verificacion de cuenta por correo | FR-001 | 1 | Current release |
| HU-003 | Inicio de sesion | FR-001 | 1 | Current release |
| HU-004 | Registro e inicio de sesion con Google | FR-001 | 1 | Version 2 |
| HU-005 | Recuperacion de contrasena | FR-001 | 1 | Current release |
| HU-006 | Cierre de sesion | FR-001 | 1 | Current release |
| HU-007 | Gestion de perfil | FR-003 | 1 | Current release |
| HU-008 | Eliminar cuenta | FR-003 | 1 | Current release |
| HU-009 | Crear un lugar | FR-004 | 1 | Current release |
| HU-010 | Listar y cambiar entre lugares | FR-004 | 1 | Current release |
| HU-011 | Editar y eliminar un lugar | FR-004 | 1 | Current release |
| HU-012 | Vincular dispositivo a un lugar | FR-005 | 1 | Current release |
| HU-013 | Ver estado del dispositivo | FR-006 | 1 | Current release |
| HU-014 | Desvincular dispositivo | FR-005 | 1 | Current release |
| HU-015 | Ver consumo en tiempo real | FR-007 | 1 | Current release |
| HU-016 | Selector de periodo (dia / semana / mes) | FR-007 | 1 | Current release |
| HU-017 | Grafica de consumo | FR-007 | 1 | Current release |
| HU-018 | Ver caudal en tiempo real | FR-007 | 1 | Current release |

### Entrega 2 — Smart Monitoring (E5 + E6 + E7 + E8 + HU-038)

| HU | Title | FR(s) | Entrega | Scope (v2.1) |
|----|-------|-------|---------|--------------|
| HU-019 | Ver estado de la valvula | FR-013 | 2 | Current release |
| HU-020 | Cerrar la valvula manualmente | FR-012 | 2 | Current release |
| HU-021 | Abrir la valvula | FR-012 | 2 | Current release |
| HU-022 | Historial de acciones sobre la valvula | FR-013 | 2 | Current release |
| HU-023 | Definir meta mensual | FR-009 | 2 | Version 2 |
| HU-024 | Ver avance frente a la meta | FR-009 | 2 | Version 2 |
| HU-025 | Centro de notificaciones | FR-011 | 2 | Current release |
| HU-026 | Notificacion push al celular | FR-011 | 2 | Current release |
| HU-027 | Notificacion por correo | FR-011 | 2 | Current release |
| HU-028 | Notificacion por WhatsApp / SMS | FR-011 | 2 | Version 2 |
| HU-029 | Alerta de fuga sospechosa | FR-010, FR-011 | 2 | Version 2 |
| HU-030 | Alerta de meta superada | FR-011 | 2 | Version 2 |
| HU-031 | Alerta preventiva de meta | FR-011 | 2 | Version 2 |
| HU-032 | Alerta de sensor desconectado | FR-011 | 2 | Current release |
| HU-033 | Alerta de valvula cerrada | FR-011 | 2 | Current release |
| HU-034 | Preferencias de notificacion | FR-011 | 2 | Current release |
| HU-035 | Configurar reglas de cierre automatico | FR-017 | 2 | Version 2 |
| HU-036 | Cierre automatico por fuga con confirmacion | FR-017 | 2 | Version 2 |
| HU-037 | Reapertura tras cierre automatico | FR-017 | 2 | Version 2 |
| HU-038 | Exportar datos a Excel / CSV | FR-008 | 2 | Version 2 |

### Entrega 3 — Social & Billing (E9 + E10 + E11 + E12)

| HU | Title | FR(s) | Entrega | Scope (v2.1) |
|----|-------|-------|---------|--------------|
| HU-039 | Generar enlace de acceso para invitados | FR-014a | 3 | Version 2 |
| HU-040 | Ver consumo como invitado anonimo | FR-014a | 3 | Version 2 |
| HU-041 | Restriccion de solo lectura del invitado | FR-014a | 3 | Version 2 |
| HU-042 | Invitar colaborador por correo | FR-014b | 3 | Version 2 |
| HU-043 | Aceptar invitacion como colaborador | FR-014b | 3 | Version 2 |
| HU-044 | Definir permisos del colaborador | FR-014b | 3 | Version 2 |
| HU-045 | Colaborador ejecuta acciones segun permisos | FR-014b | 3 | Version 2 |
| HU-046 | Revocar acceso de colaborador | FR-014b | 3 | Version 2 |
| HU-047 | Colaborador recibe alertas del lugar | FR-014b | 3 | Version 2 |
| HU-048 | Registrar un recibo manualmente | FR-015 | 3 | Version 2 |
| HU-049 | Ver historial de recibos | FR-015 | 3 | Version 2 |
| HU-050 | Comparar recibo contra sensor | FR-015 | 3 | Version 2 |
| HU-051 | Alerta de recibo desalineado | FR-015 | 3 | Version 2 |
| HU-052 | Recordatorio de pago del recibo | FR-015 | 3 | Version 2 |
| HU-053 | Seleccionar pais y moneda | FR-016 | 3 | Current release |
| HU-054 | Ingresar la tarifa manualmente | FR-016 | 3 | Current release |
| HU-055 | Seleccionar unidad de medida | FR-016 | 3 | Current release |
| HU-056 | Ver el consumo en dinero | FR-016 | 3 | Current release |

### Entrega 4 — Administration & Analytics (E13 + E14 + E15)

| HU | Title | FR(s) | Entrega | Scope (v2.1) |
|----|-------|-------|---------|--------------|
| HU-057 | Generar reporte por periodo | FR-019 | 4 | Version 2 |
| HU-058 | Exportar reporte a PDF | FR-019 | 4 | Version 2 |
| HU-059 | Admin ve lista de usuarios registrados | FR-018 | 4 | Current release |
| HU-060 | Admin bloquea/desbloquea usuario | FR-018 | 4 | Current release |
| HU-061 | Admin ve lista de dispositivos | FR-018 | 4 | Current release |
| HU-062 | Admin ve metricas de plataforma | FR-018 | 4 | Current release |
| HU-063 | Admin crea recomendacion de ahorro | FR-018 | 4 | Current release |
| HU-064 | Admin edita o desactiva recomendacion | FR-018 | 4 | Current release |
| HU-065 | Ver consejos de ahorro de agua | FR-018 | 4 | Current release |
| HU-066 | Catalogo de tarifas de Colombia | FR-016 | 4 | Current release |
| HU-067 | Catalogo de tarifas de Ecuador | FR-016 | 4 | Version 2 |
| HU-068 | Catalogo de tarifas de Estados Unidos | FR-016 | 4 | Version 2 |
| HU-069 | Calculo automatico con tarifa precargada | FR-016 | 4 | Current release |

---

## Coverage summary

| Entrega | FRs covered | HUs | Test files (planned) |
|---------|------------|-----|---------------------|
| 1 (MVP) | FR-001, FR-003 to FR-007 | 18 | 12 |
| 2 | FR-008 to FR-013, FR-017 | 20 | 17 |
| 3 | FR-014a, FR-014b, FR-015, FR-016 | 18 | 10 |
| 4 | FR-016 (ext.), FR-018, FR-019 | 13 | 10 |
| **Total** | **18 FRs (+ 2 eliminated)** | **69 HUs** | **49 test files** |

---

## Identified gaps

| Gap type | Description | Action | Owner |
|----------|-------------|--------|-------|
| Test files named by story not created | The automated tests exist per service (626 passing, see `11-quality/test-report.md`), not with the planned file names | Keep the per-service suites; map them to stories when Version 2 starts | Team |
| NFR validation manual | Most NFR validation relies on manual testing | Automate gradually; minimum: structured logs | Team |
| No E2E tests planned | End-to-end flows not yet defined | Define critical path E2E tests before Entrega 1 launch | Team |

---

## Status legend

| Status | Meaning |
|--------|---------|
| Planned | In backlog, not yet started |
| In progress | Under development |
| Done | Implemented, tested, validated |
| Blocked | Requires external dependency |
| Version 2 | Kept in the backlog as an improvement for the next version (reason in `user-stories.md`) |

---

## Correlations

- User stories → `04-requirements/user-stories.md`
- Non-functional requirements → `04-requirements/non-functional.md`
- Delivery roadmap → `03-product/roadmap.md`
- Testing strategy → `11-quality/testing-strategy.md`
- Domain events → `02-domain/domain-events.md`
