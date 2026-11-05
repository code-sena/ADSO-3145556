# Reporte de capacidad — EduAirControl

> Fecha de ejecución: ______
> Entorno: VPS Hostinger KVM 2 (2 vCPU, 8 GB RAM, 100 GB NVMe)
> Stack: docker compose (18 contenedores) + Caddy + observabilidad

---

## 1. Configuración de la prueba

| Parámetro | Valor |
|---|---|
| Herramienta | k6 v0.55.0 |
| Escenarios | smoke, load (10→200 VUs), spike (0→200), stress (50→400) |
| Duración total | ~22 min |
| Endpoints probados | POST /measurements, POST /measurements/batch, GET /environments/{id}/measurements, GET /dashboard/summary, GET /dashboard/series |
| Ambiente de prueba | VPS de producción (no aislado) |

## 2. Resultados por nivel de carga

| Ambientes | RPS | p95 (ms) | p99 (ms) | Errores (%) | CPU (%) | RAM (MB) | Disco (GB) | Resultado |
|---:|---:|---:|---:|---:|---:|---:|---:|---|
| 10 | — | — | — | — | — | — | — | Pendiente |
| 25 | — | — | — | — | — | — | — | Pendiente |
| 50 | — | — | — | — | — | — | — | Pendiente |
| 100 | — | — | — | — | — | — | — | Pendiente |
| 200 | — | — | — | — | — | — | — | Pendiente |

## 3. Criterios de detención

- p95 > 1 segundo → detener incremento de carga.
- Errores > 1% → detener.
- Registrar el último nivel estable como capacidad del sistema.

## 4. Capacidad máxima estable

| Métrica | Valor |
|---|---|
| Ambientes simultáneos (estable) | — |
| RPS sostenido | — |
| p95 en carga estable | — ms |
| Tasa de error en carga estable | — % |

## 5. Crecimiento de PostgreSQL

| Tabla | Filas (inicio) | Filas (fin) | Tamaño (MB) |
|---|---:|---:|---:|
| environment_measurement | — | — | — |
| measurement_agg_5m | — | — | — |
| measurement_agg_1h | — | — | — |
| measurement_agg_1d | — | — | — |

## 6. Conclusiones

- Capacidad máxima estable: ______ ambientes.
- El sistema soporta ______ RPS con p95 < 1s.
- La retención y agregaciones (ADR-020) reducen el crecimiento de almacenamiento.
- Recomendación: ______.

## 7. Evidencias

- Logs de k6: `load-tests/results/*.log`
- JSON de métricas: `load-tests/results/*.json`
- Capturas de Grafana (CPU, RAM, JVM): `Evidencias/07_carga/`
- Captura de `docker stats` durante la prueba.

---

*Completar tras ejecutar las pruebas de carga en el VPS.*
