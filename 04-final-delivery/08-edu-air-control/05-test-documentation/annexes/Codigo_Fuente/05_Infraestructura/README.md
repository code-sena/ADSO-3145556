# infra — EduAirControl

Infraestructura como código del sistema EduAirControl: orquestación local con
Docker Compose, despliegue en Kubernetes, CI/CD y observabilidad.

> Este repositorio **no contiene código de aplicación**. Los microservicios viven
> en sus propios repositorios (`api-gateway`, `ms-security`,
> `ms-classroom-management`, `ms-sensor-management`, ...).

## Estructura

```
infra/
├── docker-compose.yml               # Stack local completo (gateway + servicios + PG + Redis)
├── docker-compose.observability.yml # Prometheus + Grafana + OTel Collector
├── .env.example                     # Variables del stack
├── scripts/
│   ├── wait-for-services.sh         # Espera a que todo responda
│   └── smoke.sh                     # Smoke end-to-end a través del gateway
├── deploy/k8s/                      # Manifiestos de producción
│   ├── namespace.yaml
│   ├── configmap.yaml
│   ├── secrets.example.yaml
│   ├── postgres.yaml                # 3 PostgreSQL (uno por dominio, ADR-003)
│   ├── redis.yaml
│   ├── apps.yaml                    # gateway + microservicios
│   └── ingress.yaml
├── observability/                   # prometheus.yml + otel-collector.yaml
└── .github/workflows/build-services.yml
```

## Arranque local

Requisito: los repositorios de los servicios clonados como hermanos de `infra/`
(el compose construye con `build: ../<servicio>`).

```bash
cp .env.example .env
docker compose up -d --build
./scripts/wait-for-services.sh
./scripts/smoke.sh
```

## Puertos

| Servicio | Puerto host | Puerto contenedor |
|----------|-------------|-------------------|
| api-gateway | 8080 | 8080 |
| ms-security | 8081 | 8081 |
| ms-classroom-management | 3002 | 3002 |
| ms-sensor-management | 3004 | 3004 |
| ms-user-management | 3007 | 3007 |
| ms-environment-monitoring | 3003 | 3003 |
| PostgreSQL (security) | 5437 | 5432 |
| PostgreSQL (classroom) | 5433 | 5432 |
| PostgreSQL (sensor) | 5434 | 5432 |
| PostgreSQL (user-management) | 5438 | 5432 |
| PostgreSQL (monitoring) | 5435 | 5432 |
| RabbitMQ | 5672 | 5672 |
| RabbitMQ (panel) | 15672 | 15672 |
| Redis | 6379 | 6379 |
| Prometheus | 9090 | 9090 |
| Grafana | 3001 | 3000 |

## Despliegue en producción (Kubernetes)

```bash
kubectl apply -f deploy/k8s/namespace.yaml
kubectl apply -f deploy/k8s/configmap.yaml
# Secretos reales desde la bóveda (no subir secrets.example.yaml con valores reales):
kubectl apply -f deploy/k8s/secrets.example.yaml   # solo plantilla
kubectl apply -f deploy/k8s/redis.yaml
kubectl apply -f deploy/k8s/postgres.yaml
kubectl apply -f deploy/k8s/apps.yaml
kubectl apply -f deploy/k8s/ingress.yaml
```

Sustituye `ghcr.io/eduaircontrol/ea-control-<servicio>:latest` por la imagen publicada en tu registro
(`ghcr.io/<owner>/ea-control-<servicio>:<tag>`), generada por el workflow de CI.

## Observabilidad

```bash
docker compose -f docker-compose.yml -f docker-compose.observability.yml up -d
```

Prometheus scrapea `/actuator/prometheus`; los servicios deben incluir
`spring-boot-starter-actuator` y `micrometer-registry-prometheus` para exponer métricas.
El OTel Collector recibe trazas/métricas/logs en `4317` (gRPC) y `4318` (HTTP).
