# Purpose and scope

This plan defines how FaceAttend EDU is verified before each production release. It covers the nine microservices, the shared authentication library, the web and mobile applications, the API contract and the critical MVP flows.

Load tests and tests with the physical fingerprint reader are out of scope, because they are not part of the automated flows that run in continuous integration.

# Test levels

| Level | Goal | Tool | When it runs |
|---|---|---|---|
| Unit and integration per service | Business rules, use cases and adapters | JUnit and Maven, Jest, pytest | On every pull request and before each deployment |
| Web and mobile interface | Components, view models and navigation | Jest with snapshots | On every pull request and before each deployment |
| API contract | Detect differences between code and OpenAPI | `openapi / drift` job | On every pull request and before each deployment |
| End to end | The 6 critical flows on the full system in Docker Compose | `tools/e2e/mvp-flujos.mjs` | `E2E flujos MVP` workflow |
| Full functional review | Endpoints, screens, buttons and coherence between web, API and database | `verificar-estado.mjs` and `verificar-contrato.mjs` | On demand, before a closing milestone |

# Environments

| Environment | Use | Data |
|---|---|---|
| GitHub Actions | Unit, integration and contract tests | Test doubles and disposable data |
| Docker Compose in CI | E2E flows, with a random-secret `.env` per run | Synthetic data generated on each run |
| Local with Docker Compose | Full functional review | Seeded test data |

# Entry and exit criteria

- *Entry:* the code builds and the pull request targets the integration branch.
- *Exit:* every CI job finishes green. The production deployment depends on that result, because `Deploy production` runs CI first.
- *Skipped tests:* accepted only when explicitly marked in the code. They are reported in the results table.

# Responsibilities

The three team members write the tests of the module they develop. Execution is automatic in GitHub Actions, and the final deployment approval is manual in the `production` environment.