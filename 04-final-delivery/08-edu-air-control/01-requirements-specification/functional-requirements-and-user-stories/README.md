# 04 — Requirements

> The formal specification of what the system must do.
> Functional: what it does. Non-functional: how well it does it.

---

## Documents in this section

| File | Purpose |
|------|---------|
| [functional.md](./functional.md) | All functional requirements by module |
| [non-functional.md](./non-functional.md) | Performance, availability, security requirements |
| [user-stories.md](./user-stories.md) | User story backlog with acceptance criteria |
| [traceability-matrix.md](./traceability-matrix.md) | Links HUs → FRs → Tests → Services |

---

## Why this section exists

Requirements are the contract between the team and the client/stakeholder.
Without them:
- There is no way to verify whether the system is complete
- Scope changes have no baseline for comparison
- Tests have no success criterion

---

## Correlations with other sections

| This section feeds... | Why |
|-----------------------|-----|
| `05-architecture/` | Performance/availability NFRs guide architectural decisions |
| `11-quality/testing-strategy.md` | Each FR must have at least one test case |
| `09-microservices/` | FRs are grouped by responsible service |
| `07-api/` | Integration FRs → endpoints in API contracts |
