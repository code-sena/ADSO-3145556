# 07_Pruebas — Plan, informes y evidencias

Carpeta de pruebas de EduAirControl. Reemplaza a los antiguos `07_testing/` y
`07_unit-tests-report/`, fusionados aquí.

## Contenido

| Archivo | Qué es |
|---------|--------|
| `md/Plan_Pruebas.md` · `pdf/Plan_Pruebas.pdf` | Plan de pruebas: alcance, casos (50), nomenclatura y §15 de evidencias |
| `md/07_Informe_Pruebas_Unitarias_ES.md` · `pdf/…_ES.pdf` | Informe de pruebas unitarias (español) |
| `md/07_Unit_Tests_Report_EN.md` · `pdf/…_EN.pdf` | Unit tests report (English) |
| `Inventario_Pruebas_Automatizadas.csv` | Un fila por método de prueba: servicio, clase, tipo, Docker y resultado |
| `ejecutar_pruebas.ps1` | Script que ejecuta las suites (PowerShell) |
| `Evidencias/` | Plantilla y reglas para las evidencias de ejecución manual (Plan §15) |

## Última corrida automatizada

**13 de octubre de 2026** · `./mvnw -B clean verify` en los siete servicios.

| Servicio | Pruebas | Errores | Fallos | Seg. |
|----------|---------|---------|--------|------|
| api-gateway | 27 | 0 | 0 | 16,9 |
| ms-security | 67 | 0 | 0 | 73,9 |
| ms-classroom-management | 35 | 0 | 0 | 27,9 |
| ms-sensor-management | 63 | 0 | 0 | 29,8 |
| ms-user-management | 22 | 0 | 0 | 24,7 |
| ms-environment-monitoring | 103 | 0 | 0 | 78,3 |
| ms-user-experience | 10 | 0 | 0 | 29,8 |
| **Total** | **327** | **0** | **0** | **281,3** |

Reparto por tipo: **136 unitarias · 100 de rebanada · 6 con simulacros · 85 Testcontainers**
(en 45 clases). El CSV registra esos 327 métodos como `PASS` más 12 como `OBSOLETE`
(pruebas que ya no existen).

## Nomenclatura de evidencias

`<caso>_<resultado>_<fecha>_<consecutivo>.<ext>`, p. ej. `SEC-003_PASS_2026-10-09_01.png`.

Reglas: ocultar contraseñas, tokens, API keys y correos personales; un caso no ejecutado
nunca se marca `PASS`. Detalle en `Evidencias/README.md`.
