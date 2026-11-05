# Resumen de la suite completa (backend)

Versión 1.1 del plan de pruebas. Sin variables de entorno ni credenciales.

**Resultado final (20 de octubre de 2026):** `./mvnw test` completo, perfil test, contra MySQL 8.4: **1131 pruebas, 0 fallos, 0 errores, 0 omitidas**. Incluye las 37 clases que en la versión 1.0 quedaron «Por ejecutar (MySQL)».

**Integración continua:** el *check* obligatorio `Tests` del workflow `Backend CI/CD` (GitHub Actions) ejecuta la misma suite contra un contenedor de servicio `mysql:8.4` en cada *pull request* y *push* a `dev` y `main`. Ejecución sobre `main`, commit `90ae5ef` (19 de octubre de 2026): `Tests` en verde en 23 min 9 s.

**Distribución por módulo** del reporte Surefire local más reciente (`target/surefire-reports`, 16 de octubre de 2026). Entre este reporte y la ejecución final se agregaron 7 pruebas.

| Módulo | Clases | Ejecutadas | Aprobadas | Fallidas | Omitidas | Tiempo (s) |
|---|---|---|---|---|---|---|
| (raíz) | 8 | 25 | 25 | 0 | 0 | 305.0 |
| alert | 10 | 52 | 52 | 0 | 0 | 81.9 |
| device | 6 | 48 | 48 | 0 | 0 | 77.0 |
| home | 28 | 179 | 179 | 0 | 0 | 68.8 |
| measurement | 13 | 75 | 75 | 0 | 0 | 78.8 |
| notification | 13 | 93 | 93 | 0 | 0 | 199.0 |
| recommendation | 5 | 26 | 26 | 0 | 0 | 62.8 |
| security | 49 | 626 | 626 | 0 | 0 | 773.9 |
| **Total** | 132 | 1124 | 1124 | 0 | 0 | 1647.2 |

**Fallas:** ninguna. DEF-01 y DEF-02 quedaron cerrados (ver la Tabla 35 del plan de pruebas).

**Clases nuevas desde la versión 1.0:** PushDeviceTest (5), ExpoPushSenderTest (9) y PushDevicePersistenceTest (7), del módulo notification.

**Cobertura:** JaCoCo no está en el `pom.xml`; sigue sin reporte de cobertura.
