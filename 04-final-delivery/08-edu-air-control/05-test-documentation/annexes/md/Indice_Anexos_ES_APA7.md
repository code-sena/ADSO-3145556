# EduAirControl: Anexos del proyecto

**María de los Ángeles Olaya García; Jhon Jairo Cortez Vargas**

Servicio Nacional de Aprendizaje (SENA), Programa ADSO-3145556

Instructora: Karol Daniela Correa

12 de octubre de 2026

---

# EduAirControl: Anexos del proyecto

Tecnólogo en Análisis y Desarrollo de Software · SENA Industria · Ficha 3145556
Jhon Jairo Cortez Vargas · María de los Ángeles Olaya García · Instructora: Karol Daniela Correa
Neiva, Huila · 10 de octubre de 2026 · Versión 1.0

---

## Propósito

Esta carpeta reúne el material de soporte que respalda los documentos principales de la entrega: contratos de API, decisiones de arquitectura, archivos de infraestructura, riesgos, capturas de pantalla e inventario de pruebas. Son copias de los archivos del proyecto, sin modificar, salvo los anexos D y G, que se redactaron para esta entrega.

## Contenido

| Anexo | Contenido | Ubicación |
|---|---|---|
| A | Contratos OpenAPI 3 de los servicios (8 archivos, incluido el componente compartido) | `Anexo_A_Contratos_OpenAPI/` |
| B | 17 registros de decisión de arquitectura (ADR-001 a ADR-017) | `Anexo_B_Decisiones_Arquitectura/` |
| C | Docker Compose, scripts de espera y smoke test, manifiestos Kubernetes | `Anexo_C_Infraestructura/` |
| D | Registro de riesgos del proyecto (resumen en español) | `Anexo_D_Registro_Riesgos_ES` y `_EN` (`.pdf` y `.md`) |
| E | 39 capturas de pantalla usadas en los manuales | `Anexo_E_Capturas_Pantalla/` |
| F | Inventario de las 285 pruebas automatizadas | `Anexo_F_Inventario_Pruebas/` |
| G | Glosario | `Anexo_G_Glosario_ES` y `_EN` (`.pdf` y `.md`) |

## Anexo A. Contratos OpenAPI

| Archivo | Servicio | Rutas documentadas |
|---|---|---|
| `api-gateway.yaml` | API Gateway | 2 |
| `auth-service.yaml` | ms-security | 12 |
| `ms-classroom-management.yaml` | Ambientes y campus | 7 |
| `ms-environment-monitoring.yaml` | Monitoreo y análisis | 10 |
| `ms-sensor-management.yaml` | Sensores y dispositivos | 10 |
| `ms-user-experience.yaml` | Favoritos y preferencias | 8 |
| `ms-user-management.yaml` | Perfil de usuario | 4 |
| `_shared.yaml` | Componentes compartidos | – |

## Anexo B. Decisiones de arquitectura

| ADR | Tema |
|---|---|
| 001 | Idioma de la documentación |
| 002 | Contextos delimitados y fronteras de microservicios |
| 003 | Estrategia de base de datos |
| 004 | Patrones de comunicación |
| 005 | API Gateway |
| 006 | Autenticación y autorización (flujo de tokens reemplazado en parte por la 017) |
| 007 | Entrega de eventos y consistencia |
| 008 | Herramienta de migración de base de datos (Liquibase) |
| 009 | Arquitectura interna hexagonal |
| 010 | Estrategia de contenerización |
| 011 | Estrategia de despliegue en producción |
| 012 | Pila de observabilidad |
| 013 | Adopción de circuit breaker |
| 014 | Variables de sensores ambientales y umbrales |
| 015 | Integración de alertas en ms-environment-monitoring |
| 016 | Multi-tenencia por institución y campus |
| 017 | Servidor de autorización OAuth2/OIDC y Backend-for-Frontend |

## Anexo C. Infraestructura

- `docker-compose.yml`: orquesta los servicios, PostgreSQL, Redis y RabbitMQ en local.
- `wait-for-services.sh` y `smoke.sh`: espera a que los servicios estén sanos y prueba de extremo a extremo (registro, inicio de sesión, cierre de sesión y reutilización de token revocado).
- `k8s/`: namespace, configmap, ingress, PostgreSQL, RabbitMQ, Redis, aplicaciones y una plantilla de secretos (`secrets.example.yaml`).

**Advertencia.** Estos archivos contienen únicamente valores de ejemplo para desarrollo (por ejemplo, `REPLACE_ME` o `change-me-in-vault`). No se copiaron archivos `.env` ni claves. En un ambiente real, los secretos deben venir de una bóveda y no repetirse.

## Anexo E. Capturas de pantalla

Las imágenes `image.png` a `image-38.png` provienen de los manuales técnico, de instalación, de administración y de usuario. Conservan los nombres originales para que los manuales sigan enlazándolas.

## Anexo F. Inventario de pruebas

`Inventario_Pruebas_Automatizadas.csv` lista cada método de prueba por servicio y clase, su tipo y si requiere Docker. La columna de resultado refleja la corrida del **13 de octubre de 2026**: **327 métodos en `PASS`** y 12 en `OBSOLETE` (pruebas que ya no existen en el código); el detalle está en el informe de la carpeta `07_Pruebas`.

## Material citado que no estaba en el zip

Los archivos de firmware del ESP32 (`esp32_sensor_sender.ino` e `infra/iot/esp32-node`, mencionados en el registro de riesgos) y la carpeta `iot/esp32/` de pruebas de sensores no venían en el zip recibido. Si existen en el repositorio, deben copiarse a una carpeta `Anexo_H_Firmware_IoT/`.
