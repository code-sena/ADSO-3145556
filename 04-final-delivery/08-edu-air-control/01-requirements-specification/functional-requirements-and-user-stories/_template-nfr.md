# Non-Functional Requirements (NFR) — Template

> Copy this template when creating a new NFR.
>
> Remember: every NFR must have a metric and a verification method.

---

## NFR-XXX: [Category]

**Description:** [One-line description of the requirement]

| Attribute | Metric | Test condition |
|-----------|--------|---------------|
| [Attribute 1] | [Metric 1] | [Condition 1] |
| [Attribute 2] | [Metric 2] | [Condition 2] |

**Verification method:**

- [ ] [How to verify this NFR — tool, environment, procedure]

**Where is it validated?**

- [ ] CI/CD pipeline
- [ ] Staging environment
- [ ] Production monitoring
- [ ] Load test

**Related HUs:** [List any HUs that depend on this NFR]

**Status:** 🔴 Pending / 🟡 In progress / ✅ Done

---

## NFR categories

Use one of these categories for the title:

| Category | Description | Example |
|----------|-------------|---------|
| Performance | Response time, throughput, latency | P95 < 300ms |
| Availability | Uptime SLO, maintenance windows | 99.5% monthly |
| Scalability | Horizontal scaling, capacity | Up to 5 instances |
| Security | Authentication, authorization, encryption | JWT with 1h expiration |
| Observability | Logging, metrics, tracing | Structured logs with correlation IDs |
| Portability | Containerization, OS independence | Docker images |
| Data Management | Backup, recovery, retention | Daily backups with 30-day retention |
| Test Coverage | Code coverage, test types | ≥ 80% unit test coverage |

---

## Examples

### Example 1: Performance

```markdown
## NFR-001: Performance

| Attribute | Metric | Test condition |
|-----------|--------|---------------|
| P95 latency — critical endpoints | < 500ms | Under 100 concurrent requests |
| P99 latency — critical endpoints | < 1000ms | Under 100 concurrent requests |
| Minimum throughput | ≥ 20 req/s | Without degradation |

**Verification method:**
- Load tests with k6 in staging environment

**Status:** 🟡 In progress
```

### Example 2: Availability

```markdown
## NFR-002: Availability

| Attribute | Metric | Test condition |
|-----------|--------|---------------|
| Production SLO | 99.5% monthly | Continuous monitoring |
| Max downtime/month | ~3h 39min | Scheduled maintenance window |

**Verification method:**
- Health checks and Grafana monitoring

**Status:** 🔴 Pending
```
