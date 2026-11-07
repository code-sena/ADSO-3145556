# Evidencias de pruebas

## Versión 1.4 (estado actual)

Fecha: 28 de octubre de 2026. Cambian los repositorios que recibieron las últimas funciones; el resto sigue como en la versión 1.3. Commits de `main` y totales:

| Repositorio | Rama | Commit | Nota |
|---|---|---|---|
| en-monitor-security-service | main | `de52c95` | 774 pruebas |
| en-monitor-home-service | main | `6a4e396` | 260 pruebas |
| en-monitor-notification-service | main | `ecdbc0a` | 235 pruebas |
| en-monitor-alert-service | main | `0e65430` | 128 pruebas |
| energy-monitor-web | main | `27ad9f0` | 44 archivos y 253 pruebas |
| energy-monitor-mobile | dev | `8b5edc0` | 78 pruebas unitarias |

## Versión 1.3

Fecha: 28 de octubre de 2026. Con la migración completa el servicio central se retiró y cada servicio tiene su repositorio y su suite. Commits de `main` (los desplegados en producción ese día) y totales de cada suite, todas en verde:

| Repositorio | Rama | Commit | Nota |
|---|---|---|---|
| en-monitor-api-gateway | main | `0b2c164` | API gateway; 69 pruebas |
| en-monitor-security-service | main | `f57c76b` | Servicio de seguridad; 773 pruebas |
| en-monitor-device-service | main | `06c1bdc` | Servicio de dispositivos; 129 pruebas |
| en-monitor-measurement-service | main | `8a9b544` | Servicio de mediciones; 151 pruebas |
| en-monitor-home-service | main | `f424083` | Servicio de hogares; 260 pruebas |
| en-monitor-notification-service | main | `7b10398` | Servicio de notificaciones; 234 pruebas |
| en-monitor-alert-service | main | `f050b56` | Servicio de alertas; 119 pruebas |
| en-monitor-recommendation-service | main | `aeeeec5` | Servicio de recomendaciones; 71 pruebas |
| energy-monitor-web | main | `698d4c9` | Aplicación web; 38 archivos y 228 pruebas |
| energy-monitor-mobile | dev | `1e4109b` | 78 pruebas unitarias en 8 suites, `tsc` y `expo lint` sin errores ([mobile/verificacion-estatica.md](mobile-static-verification.md)) |
| energy-monitor-infra | main | `ceb1543` | Pruebas de punta a punta sobre la pila local: `device-flow` (24), `measurement-flow` (28), `alert-flow` (36), `activity-flow` (60) y `account-deletion-flow` (12) |

Como en la versión 1.2, no se adjuntan reportes de Surefire ni logs: los totales son los que reportó cada suite. El protocolo de pruebas manuales de la aplicación móvil (MOV-01 a MOV-12) está en el plan de pruebas y sigue por ejecutar en un dispositivo.

## Versión 1.2

Fecha: 24 de octubre de 2026. Desde esta versión el sistema ya no es un único backend: el API gateway y los servicios de seguridad, dispositivos y mediciones tienen su propio repositorio y su propia suite, y `energy-monitor-backend` conserva home, alert, recommendation y notification. Commits de `main` en producción y totales de cada suite, todas en verde:

| Repositorio | Rama | Commit | Nota |
|---|---|---|---|
| en-monitor-api-gateway | main | `c070cec` | API gateway; 57 pruebas |
| en-monitor-security-service | main | `334691a` | Servicio de seguridad; 660 pruebas (ejecución del 22 de octubre de 2026) |
| en-monitor-device-service | main | `f8b8526` | Servicio de dispositivos; 130 pruebas |
| en-monitor-measurement-service | main | `ee2cb81` | Servicio de mediciones; 151 pruebas |
| energy-monitor-backend | main | `aa9a713` | Servicio central (home, alert, recommendation y notification) y firmware; 481 pruebas, `ModularityTests` en verde |
| energy-monitor-web | main | `e59bece` | Aplicación web |
| energy-monitor-mobile | main | `894b65a` | Sin pruebas automatizadas |
| energy-monitor-infra | main | `eaf961b` | Pruebas de punta a punta sobre la pila local: `e2e/device-flow.sh` (21/21) y `e2e/measurement-flow.sh` (25/25) |

De esta versión no se adjuntan reportes de Surefire ni logs: los totales son los que reportó cada suite. Los archivos de `backend/` y `web/` son evidencia histórica de las versiones 1.0 y 1.1, cuando `energy-monitor-backend` contenía los siete módulos.

## Versión 1.1

Fecha de ejecución: 20 de octubre de 2026.

| Repositorio | Rama | Commit | Nota |
|---|---|---|---|
| energy-monitor-backend | main | `90ae5ef` | Servicio central y firmware; suite completa contra MySQL (1131 pruebas, 0 fallos), también en el *check* `Tests` de GitHub Actions |
| energy-monitor-web | main | `2b41cb3` | Aplicación web; 141 pruebas aprobadas, también en el *check* `Tests and build` |
| energy-monitor-mobile | dev | `4ebccfb` | Sin pruebas automatizadas |
| energy-monitor-infra | main | `7324110` | Compose, scripts y plantilla de Nginx validados en el *check* `Validate compose and scripts` |
| energy-monitor-docs-final | dev | `00806c6` | Documentación final (este documento) |

- [backend/resumen-suite-completa.md](backend-full-suite-summary.md)
- [web/resumen-vitest.md](web-vitest-summary.md) (sección «Ejecución de la versión 1.1»)

## Versión 1.0 (histórica)

Fecha de ejecución: 14 de octubre de 2026.

| Repositorio | Rama | Commit | Nota |
|---|---|---|---|
| energy-monitor-backend | dev | `d03bbd3` | Servicio central; el árbol tenía 4 clases de prueba nuevas sin confirmar |
| energy-monitor-web | dev | `e6edc12` | Aplicación web |
| energy-monitor-mobile | dev | `a3f6875` | Sin pruebas automatizadas |
| devices | dev | `57417a7` | Repositorio ya retirado: el firmware pasó al backend y el broker a `energy-monitor-infra` |
| energy-monitor-docs-final | dev | `2af09ab` | Documentación final (este documento) |

- [backend/resumen-surefire.md](backend-surefire-summary.md)
- [backend/log-ejecucion-sin-bd.md](backend-run-without-db-log.md)
- [backend/log-pruebas-nuevas.md](backend-new-tests-log.md)
- [web/resumen-vitest.md](web-vitest-summary.md)

No hay evidencia de cobertura: JaCoCo no está en el `pom.xml` del backend y la web no define un script de cobertura.

Versión en inglés: [../EN/README.md](evidence-overview.md)
