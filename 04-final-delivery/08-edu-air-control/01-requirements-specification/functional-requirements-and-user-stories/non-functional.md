# Non-Functional Requirements (NFR)

> NFRs define the **qualities of the system** — not what it does but how well it does it.
>
> The golden rule: every NFR must have a metric. "The system must be fast" is not an NFR.
>
> "The P95 latency of a critical endpoint must be < 300ms under 100 concurrent requests" is.

---

## Purpose and classification

Non-functional requirements define measurable qualities and technical constraints that apply across EduAirControl. They complement the functional and user requirements in user-stories.md.

## How to write a measurable NFR?

| ID | Category | Priority | Verification owner |
|----|----------|----------|--------------------|
| NFR-001 | Performance | P1 | Development and QA |
| NFR-002 | Availability | P1 | Development and DevOps |
| NFR-003 | Scalability | P2 | Development and DevOps |
| NFR-004 | Security | P1 | Development and QA |
| NFR-005 | Observability | P1 | Development and DevOps |
| NFR-006 | Maintainability | P2 | Development and QA |
| NFR-007 | Portability | P2 | DevOps |
| NFR-008 | Disaster recovery | P2 | DevOps and database owner |

Targets are release criteria unless a documented exception is approved. A target marked for staging or production must be measured in an environment representative of the release.

---

## NFR-001: Performance

| Measurement | Target | Verification |
|-----------|--------|---------------|
| P95 ≤ 500 ms under expected MVP load | P95 ≤ 500 ms under expected MVP load | Automated performance test in staging |
| API response time for historical analysis | P95 ≤ 2 s for a supported period and expected data range | Analysis load test. |
| Dashboard initial data request | P95 ≤ 3 s after the authenticated request reaches the API | End-to-end staging test |
| Sensor measurement ingestion acknowledgment | P95 ≤ 1 s under expected ingestion load | Ingestion performance test |
| Error rate under expected load | < 1% of requests, excluding client validation errors | Load test and service metrics. |

**Defined critical endpoints:**

- `POST /api/auth/register` — Critical because it is responsible for creating user accounts and is part of the authentication flow.
- `POST /api/auth/login` — Critical because it allows users to authenticate and obtain access to the platform.
- `GET /api/users/{id}` — Critical because authenticated services may require user information.
- `GET /api/environments/{id}` — Critical because educational environments are required for environmental monitoring.
- `GET /api/monitoring/current` — Critical because it provides environmental information to the frontend.
- `GET /api/monitoring/history` — Critical because it provides historical environmental information used by the analysis and dashboard functionality.

**Load testing tools:**

- k6
- Apache JMeter
- Locust

**Where is it validated?**

CI/CD or staging environment before production deployment.

---

## NFR-002: Availability and resilience

| Requirement | Target | Verification |
|------------|---------|--------------|
| API Gateway and critical services | Monthly availability ≥ 99.5% during the agreed service window | Health checks and operational metrics |
| Health endpoints | Every deployable service exposes a machine-readable health signal | Deployment smoke test |
| Stateless restart | Restart does not lose committed stateless request state | Restart test |
| Dependency failure | Controlled error with correlation ID when a dependency is unavailable | Fault-injection test |
| Retried input | Retries do not duplicate accepted measurements or side effects | Idempotency test |

**Monthly error budget in production:** Approximately 3 hours 39 minutes.

**Error Budget policy:**

If more than 50% of the monthly error budget is consumed before the middle of the month, new feature deployments should be reviewed and stability and corrective actions should be prioritized.

**Health checks:**

- `GET /health` — Liveness: responds with HTTP 200 when the service process is running.
- `GET /health/ready` — Readiness: responds with HTTP 200 when the service is ready to process requests and its required dependencies are available.

---

## NFR-003: Scalability

| Scenario | Expected behavior |
|---------|------------------|
| Gradual load growth | Additional service instances can be started when CPU usage exceeds 70%. |
| Sudden traffic spike | The system should increase available service instances within 2 minutes. |
| Load reduction | Instances can be reduced without interrupting active requests. |
| Horizontal scaling limit | Up to 5 instances per service in the initial deployment. |

**Strategy:**

Stateless horizontal scaling. Microservice instances should not depend on local in-memory state to maintain user sessions or persistent application data.

Persistent information is stored in the corresponding PostgreSQL database (see ADR-003). Temporary information and caching may use an appropriate distributed cache if required by the implementation.

The architecture allows services such as `ms-security`, `ms-user-management`, `ms-classroom-management`, `ms-sensor-management` and `ms-environment-monitoring` to be scaled independently according to their workload.

---

## NFR-004: Security

### Authentication and Authorization

- All private endpoints require a valid JWT in the `Authorization: Bearer <token>` header
- JWTs expire in 1 hour
- Refresh tokens are valid for 7 days
- RBAC (Role-Based Access Control): roles defined in `00-governance/security-policy.md`

### Data transmission

- HTTPS is mandatory in production.
- TLS 1.2 or higher must be used for secure communications.
- HTTP may be used only in local development environments when necessary.
- Communication between frontend, API Gateway and backend services must use defined API contracts.

### Sensitive data

- Passwords must never be stored in plain text.
- Passwords must be securely hashed using BCrypt with an appropriate cost factor or another approved password hashing algorithm.
- Authentication credentials and secrets must not be stored directly in source code.
- Environment-specific configuration must be provided through environment variables or secure configuration mechanisms.
- Database credentials and JWT secrets must not be committed to the Git repository.
- Personal information must only be stored when required by the functionality of EduAirControl.

### OWASP Top 10

Code must be reviewed against the OWASP Top 10 during development and before production release.

**Tools:**

- SonarQube for static code analysis.
- Dependency scanning for vulnerable libraries.
- DAST or security testing in the staging environment when available.

### Regulatory compliance

- The project must comply with the Colombian personal data protection framework applicable to EduAirControl.
- Personal data must be processed according to the principles of authorization, security, confidentiality, purpose and restricted access.
- The project must consider Colombian Habeas Data requirements applicable to the information stored by the platform.
- EduAirControl must not collect or process personal information that is not necessary for its operation.

---

## NFR-005: Observability

| Pillar | Requirement | Tool |
|--------|------------|------|
| Logs | Structured logs with timestamp, service, level and correlation ID | Logback / Spring Boot |
| Metrics | Requests, errors, response duration and service health | Prometheus + Grafana |
| Traces | Trace propagation between microservices when implemented | OpenTelemetry |
| Alerts | Alert when a critical service violates its defined availability or performance threshold | Alertmanager / Grafana |

**Correlation ID:**

Each external request should generate or propagate a unique `correlationId`. The identifier must be propagated through the API Gateway and microservices involved in the same transaction so that a request can be traced through the system.

**Minimum information recorded in logs:**

- Timestamp.
- Service name.
- Log level.
- Correlation ID.
- HTTP method.
- Endpoint.
- Response status.
- Request processing time.
- Error information when applicable.

Sensitive information such as passwords, JWT secrets and authentication credentials must never be written to logs.

---

## NFR-006: Maintainability

| Metric | Target |
|--------|--------|
| Test coverage | ≥ 80% of lines in the application |
| Domain/business logic coverage | ≥ 90% where applicable |
| Cyclomatic complexity | ≤ 10 per function whenever reasonably achievable |
| Technical debt | Critical issues resolved before production release |
| Onboarding time | A new developer can run the project locally in < 1 hour following the project setup documentation |
| Average build time | < 5 minutes in CI under normal conditions |

**Maintainability requirements:**

- The backend must follow a consistent package and layer structure.
- Microservices must maintain separation of responsibilities.
- Business rules must not be unnecessarily duplicated between services.
- APIs must have documented contracts.
- Code must follow the project's established Java/Spring Boot conventions.
- Pull requests must be reviewed before merging into the main development branches.
- Technical documentation must be updated when significant architectural or implementation changes are introduced.

---

## NFR-007: Portability

- All backend microservices must be deployable as Docker images.
- The frontend must be deployable as a Docker container.
- The application must run independently of the host operating system when Docker is available.
- Environment-specific configuration must be provided through environment variables or external configuration.
- Services must not depend on hard-coded local paths.
- Database connection parameters must be configurable without modifying the application source code.
- The system should be deployable in development, testing and production environments using the same containerized architecture with environment-specific configuration.
- The architecture must allow future deployment using Docker Compose and, if required, Kubernetes.

---

## NFR-008: Disaster Recovery (DR / Recovery)

| Scenario | RTO (Recovery Time Objective) | RPO (Recovery Point Objective) |
|---------|------------------------------|-------------------------------|
| Single microservice failure | < 5 minutes | 0 for stateless service data |
| API Gateway failure | < 5 minutes | 0 |
| Primary database failure | < 30 minutes | ≤ 24 hours with daily backups |
| Container or host failure | < 10 minutes | 0 for stateless services |
| Complete deployment/environment failure | < 4 hours | ≤ 24 hours |

**Backup requirements:**

- The Postgres database must have periodic backups.
- Database backups must be verified periodically through restoration tests.
- Backups must be stored separately from the primary database environment.
- Critical configuration and deployment files must be maintained in version control.
- Recovery procedures must be documented before the system is considered production-ready.

---

## NFR priority matrix

| NFR | Priority (P1/P2/P3) | Validated in CI? | Owner |
|-----|---------------------|-----------------|-------|
| Performance | P1 | Yes — automated tests and staging load tests when configured | Development Team |
| Availability | P1 | Yes — health checks | DevOps / Development Team |
| Security | P1 | Yes — SAST, dependency scanning and security tests | Development Team |
| Scalability | P2 | Manual — staging/load testing | DevOps / Development Team |
| Observability | P1 | Yes — service health and logging smoke tests | Development Team |
| Maintainability | P2 | Yes — tests, coverage and static analysis | Development Team |
| Portability | P2 | Yes — Docker build in CI | DevOps / Development Team |
| Disaster Recovery | P2 | Manual — backup and restoration tests | DevOps / Development Team |

---

## Correlations

- Detailed SLOs and operational requirements → `13-operations/README.md`
- Pipeline that validates NFRs → `10-devops/README.md`
- Incidents related to NFR violations → `13-operations/incident-management.md`
- Security checklist → `00-governance/security-policy.md`
- Microservice architecture → `05-architecture/overview.md`
- Microservice catalog → `09-microservices/service-catalog.md`