# EduAirControl: Project Annexes

**María de los Ángeles Olaya García; Jhon Jairo Cortez Vargas**

Servicio Nacional de Aprendizaje (SENA), Programa ADSO-3145556

Instructora: Karol Daniela Correa

12 de octubre de 2026

---

# EduAirControl: Project Annexes

Technologist in Software Analysis and Development · SENA Industria · Group (Ficha) 3145556
Jhon Jairo Cortez Vargas · María de los Ángeles Olaya García · Instructor: Karol Daniela Correa
Neiva, Huila · October 10, 2026 · Version 1.0

---

## Purpose

This folder gathers the supporting material behind the main deliverables: API contracts, architecture decisions, infrastructure files, risks, screenshots and the test inventory. They are unmodified copies of the project files, except for Annexes D and G, which were written for this delivery. Annexes D and G and this index exist in Spanish (`_ES`) and English (`_EN`).

## Contents

| Annex | Content | Location |
|---|---|---|
| A | OpenAPI 3 contracts of the services (8 files, including the shared component) | `Anexo_A_Contratos_OpenAPI/` |
| B | 17 architecture decision records (ADR-001 to ADR-017) | `Anexo_B_Decisiones_Arquitectura/` |
| C | Docker Compose, wait and smoke-test scripts, Kubernetes manifests | `Anexo_C_Infraestructura/` |
| D | Project risk register (summary) | `Anexo_D_Registro_Riesgos_ES` and `Annex_D_Risk_Register_EN` (`.pdf` and `.md`) |
| E | 39 screenshots used in the manuals | `Anexo_E_Capturas_Pantalla/` |
| F | Inventory of the 285 automated tests | `Anexo_F_Inventario_Pruebas/` |
| G | Glossary | `Anexo_G_Glosario_ES` and `Annex_G_Glossary_EN` (`.pdf` and `.md`) |

## Annex A. OpenAPI contracts

| File | Service | Documented paths |
|---|---|---|
| `api-gateway.yaml` | API Gateway | 2 |
| `auth-service.yaml` | ms-security | 12 |
| `ms-classroom-management.yaml` | Environments and campuses | 7 |
| `ms-environment-monitoring.yaml` | Monitoring and analysis | 10 |
| `ms-sensor-management.yaml` | Sensors and devices | 10 |
| `ms-user-experience.yaml` | Favorites and preferences | 8 |
| `ms-user-management.yaml` | User profile | 4 |
| `_shared.yaml` | Shared components | – |

## Annex B. Architecture decisions

The ADR files themselves are written in English.

| ADR | Topic |
|---|---|
| 001 | Documentation language |
| 002 | Bounded contexts and microservice boundaries |
| 003 | Database strategy |
| 004 | Communication patterns |
| 005 | API Gateway |
| 006 | Authentication and authorization (token flow partially superseded by 017) |
| 007 | Event delivery and consistency |
| 008 | Database migration tool (Liquibase) |
| 009 | Internal hexagonal architecture |
| 010 | Containerization strategy |
| 011 | Production deployment strategy |
| 012 | Observability stack |
| 013 | Circuit breaker adoption |
| 014 | Environmental sensor variables and thresholds |
| 015 | Folding alerts into ms-environment-monitoring |
| 016 | Multi-tenancy by institution and campus |
| 017 | OAuth2/OIDC authorization server and Backend-for-Frontend |

## Annex C. Infrastructure

- `docker-compose.yml`: orchestrates the services, PostgreSQL, Redis and RabbitMQ locally.
- `wait-for-services.sh` and `smoke.sh`: wait until the services are healthy and run an end-to-end check (register, login, logout and reuse of a revoked token).
- `k8s/`: namespace, configmap, ingress, PostgreSQL, RabbitMQ, Redis, applications and a secrets template (`secrets.example.yaml`).

**Warning.** These files contain sample development values only (for example `REPLACE_ME` or `change-me-in-vault`). No `.env` files or keys were copied. In a real environment, secrets must come from a vault and must not be repeated.

## Annex E. Screenshots

The images `image.png` to `image-38.png` come from the technical, installation, administrator and user manuals. They keep their original names so the manuals can still link to them.

## Annex F. Test inventory

`Inventario_Pruebas_Automatizadas.csv` lists every test method by service and class, its type and whether it needs Docker. The result column reflects the **October 13, 2026** run: **327 methods marked `PASS`** and 12 marked `OBSOLETE` (tests that no longer exist in the code); the detail is in the report under the `07_Pruebas` folder.

## Cited material that was not in the zip

The ESP32 firmware files (`esp32_sensor_sender.ino` and `infra/iot/esp32-node`, mentioned in the risk register) and the `iot/esp32/` sensor-test folder were not in the zip received. If they exist in the repository, they should be copied to an `Anexo_H_Firmware_IoT/` folder.
