# Checklist de ensayo de sustentación — EduAirControl

> Fecha del ensayo: ______
> Duración objetivo: < 15 minutos
> Equipo: Jota (backend/infra) + Mariangel (frontend/firmware)

---

## 1. Pre-condiciones (antes del ensayo)

- [ ] Stack desplegado y todos los contenedores healthy (`docker compose ps`).
- [ ] Smoke test pasa (`bash infra/scripts/smoke.sh`).
- [ ] Login web funciona (admin@eduaircontrol.com).
- [ ] Login móvil funciona (app conectada a api.eduaircontrol.tech).
- [ ] Al menos 1 ambiente con sensor instalado y mediciones recientes.
- [ ] Alertas activas en el sistema (para demostrar reconocimiento/eliminación).
- [ ] Diapositivas actualizadas (arquitectura + almacenamiento + offline + capacidad).
- [ ] Capturas de pantalla listas (login, dashboard, alertas, histórico, exportación).
- [ ] Resultados de k6 documentados (tabla de capacidad).
- [ ] Costos y modelo de suscripción documentados.
- [ ] Conexión a internet estable (para demo en vivo).

## 2. Flujo de demostración (cronometrado)

| # | Paso | Tiempo est. | Responsable | Verificado |
|---|---|---|---|---|
| 1 | Login web → dashboard con datos en tiempo real | 1 min | Mariangel | [ ] |
| 2 | Crear ambiente → instalar sensor → enviar medición → ver alerta | 2 min | Jota | [ ] |
| 3 | Consultar histórico (raw + agregado) → exportar CSV/Excel | 2 min | Mariangel | [ ] |
| 4 | Mostrar análisis por período (DAY/WEEK/MONTH/YEAR) | 1 min | Jota | [ ] |
| 5 | Tenant isolation: login como USER de otra institución → no ve datos ajenos | 1 min | Jota | [ ] |
| 6 | Offline: desconectar nodo → buffer → reconectar → recuperación sin duplicados | 2 min | Mariangel | [ ] |
| 7 | Capacidad: mostrar resultados k6 (RPS, p95, errores) | 1 min | Jota | [ ] |
| 8 | Arquitectura: diagrama C4 + hexagonal + patrones | 2 min | Ambos | [ ] |
| 9 | ADR-020: particionamiento, retención, agregaciones | 1 min | Jota | [ ] |
| 10 | Cierre: preguntas y respuestas | 2 min | Ambos | [ ] |

**Total estimado:** ~15 min.

## 3. Puntos clave de defensa (preguntas típicas)

1. **¿Por qué microservicios?** → ADR-002: bounded contexts, ownership de datos, escalabilidad independiente. Migración strangler-fig.
2. **¿Cómo se comunican?** → REST síncrono + RabbitMQ asíncrono (outbox → DLQ). ADR-004, ADR-007.
3. **¿Cómo evitan lectura cruzada de tablas?** → Database per Service (6 PostgreSQL), referencias por UUID lógico. ADR-003.
4. **¿Cómo se maneja la auth?** → OAuth2/OIDC + BFF (ADR-017), JWT RS256 + JWKS, httpOnly cookies. Service-level RBAC (ADR-006).
5. **¿Cómo se evita spoofing de headers?** → ADR-018: solo JWT autentica.
6. **¿Cómo se aísla por institución?** → ADR-016: institutionId en JWT, filtro en monitoring y UX.
7. **¿Cómo se despliega?** → Docker Compose + Jenkins (CI/CD, auto-deploy en main → GHCR → VPS) + Caddy.
8. **¿Qué pruebas hay?** → Backend: JUnit + H2/Testcontainers (~300+). Frontend: Vitest (105). Smoke + k6.
9. **¿Qué falla o deuda queda?** → Circuit breaker no implementado (ADR-013 deprecated), OTel no cableado, móvil sin tests.
10. **¿Cómo se maneja el almacenamiento histórico?** → ADR-020: particionamiento mensual, retención por drop, agregaciones 5m/1h/1d, job de recálculo 25h.
11. **¿Qué pasa si el nodo pierde conexión?** → Buffer LittleFS (~1h), reenvío por lotes, idempotencia por (installation, variable, measuredAt).

## 4. Resultado del ensayo

| Fecha | Duración real | Problemas encontrados | Acciones |
|---|---|---|---|
| ______ | ______ min | — | — |

## 5. Pendientes conocidos (documentar para la sustentación)

- Grafana: restart loop por provisioning del datasource (infra, no core).
- Client Secret de Google: compartido en chat, pendiente de rotación por P1.
- Facebook: app en modo desarrollo, pendiente Live + permiso email.
- G-12.5: 4 servicios de P1 pendientes de migración al starter.
- G-08.3: tests del móvil no implementados (decidido dejar pendiente).

---

*Completar tras el ensayo.*
