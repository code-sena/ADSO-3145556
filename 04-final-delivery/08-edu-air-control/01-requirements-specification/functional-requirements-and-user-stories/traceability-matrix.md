# Traceability Matrix

> Traceability connects every line of code to its business justification.
>
> It allows answering: "Why does this function exist?" and "Which HU covers this part of the system?"
>
> It also identifies: unimplemented requirements and code without a requirement (possible technical debt).

---

## How to use this matrix

```text
Requirement → HU → Test Case → Implementation → Service

If a requirement has no HU: it is not planned

If a HU has no test case: it has no completeness criterion

If a test case has no implementation: there is test technical debt

If there is code without an HU: possible gold-plating or bug introduced without a story
````

---

## FR → HU → Test → Service matrix

| FR ID  | FR Description                                                                                                  | HU(s)        | Tests that verify it           | Service        | Status         |
| ------ | --------------------------------------------------------------------------------------------------------------- | ------------ | ------------------------------ | -------------- | -------------- |
| FR-001 | The system allows user registration.                                                                            | HU-IAM-001   | `auth.register.test`           | `ms-security`  | 🟡 In progress |
| FR-002 | The system allows user authentication through login.                                                            | HU-IAM-002   | `auth.login.test`              | `ms-security`  | 🟡 In progress |
| FR-003 | The system allows administrators to manage users and their information.                                         | HU-USER-001  | `users.management.test`        | `ms-user-management`  | 🟡 In progress |
| FR-004 | The system allows authorized users to manage educational classrooms or environments.                            | HU-CLASS-001 | `classroom.management.test`    | `ms-classroom-management`     | 🟡 In progress |
| FR-005 | The system allows the registration and management of environmental sensors.                                     | HU-SENS-001  | `sensor.management.test`       | `ms-sensor-management`  | 🟡 In progress |
| FR-006 | The system receives environmental measurements from sensors.                                                    | HU-MON-001   | `environmental.data.test`      | `ms-environment-monitoring` | 🟡 In progress |
| FR-007 | The system stores environmental measurements for historical consultation.                                       | HU-MON-002   | `environmental.history.test`   | `ms-environment-monitoring` | 🟡 In progress |
| FR-008 | The system allows users to consult current environmental conditions.                                            | HU-MON-003   | `environmental.current.test`   | `ms-environment-monitoring` | 🟡 In progress |
| FR-009 | The system allows users to consult historical environmental information by day.                                 | HU-ANA-001   | `analysis.daily.test`          | `ms-environment-monitoring` | 🟡 In progress |
| FR-010 | The system allows users to consult historical environmental information by week.                                | HU-ANA-002   | `analysis.weekly.test`         | `ms-environment-monitoring` | 🟡 In progress |
| FR-011 | The system allows users to consult historical environmental information by month.                               | HU-ANA-003   | `analysis.monthly.test`        | `ms-environment-monitoring` | 🟡 In progress |
| FR-012 | The system allows users to consult historical environmental information by year.                                | HU-ANA-004   | `analysis.yearly.test`         | `ms-environment-monitoring` | 🟡 In progress |
| FR-013 | The system displays environmental information through a dashboard.                                              | HU-ANA-005   | `dashboard.environmental.test` | `ms-environment-monitoring` | 🟡 In progress |
| FR-014 | The system provides information about temperature, humidity, CO₂ and noise levels.                              | HU-MON-004   | `environmental.variables.test` | `ms-environment-monitoring` | 🟡 In progress |
| FR-015 | The system controls access to protected functionalities according to user roles.                                | HU-IAM-003   | `authorization.roles.test`     | `ms-security`  | 🟡 In progress |
| FR-016 | The system communicates between the frontend and backend services through the API Gateway.                      | HU-ARCH-001  | `gateway.routing.test`         | `API Gateway`  | 🟡 In progress |
| FR-017 | The system allows environmental data to be transmitted from sensors using the defined communication mechanisms. | HU-SENS-002  | `sensor.communication.test`    | `ms-sensor-management`  | 🟡 In progress |

---

## NFR → Validation matrix

| NFR ID  | Description                                                                                       | How it is validated                       | Tool                     | Status         |
| ------- | ------------------------------------------------------------------------------------------------- | ----------------------------------------- | ------------------------ | -------------- |
| NFR-001 | Critical API endpoints should maintain P95 latency below 500ms under the defined concurrent load. | Load test in staging                      | k6 / JMeter              | 🔴 Pending     |
| NFR-002 | Production availability target is 99.5% monthly.                                                  | Health checks and availability monitoring | Grafana / monitoring     | 🔴 Pending     |
| NFR-003 | Services must support horizontal scaling up to 5 instances when workload increases.               | Load and scaling test                     | Docker / Kubernetes / k6 | 🔴 Pending     |
| NFR-004 | Private endpoints require JWT authentication and role-based authorization.                        | Security and authentication tests         | Postman / OWASP ZAP      | 🟡 In progress |
| NFR-005 | Services must provide structured logs and correlation identifiers for request tracing.            | Log and observability tests               | Logback / Grafana        | 🔴 Pending     |
| NFR-006 | Application code should maintain at least 80% test coverage.                                      | Automated coverage analysis               | JaCoCo / SonarQube       | 🔴 Pending     |
| NFR-007 | Services must be deployable as Docker containers independently of the host operating system.      | Docker image build and execution          | Docker / Docker Compose  | 🟡 In progress |
| NFR-008 | Critical services and database must have documented backup and recovery procedures.               | Backup and restoration test               | PostgreSQL / Docker     | 🔴 Pending     |

---

## Entity → table traceability

> Maps every domain entity in `02-domain/entities-and-rules.md` to its
> canonical table in `06-data/domain/`. Full detail:
> [`06-data/normalization-assessment.md`](../06-data/normalization-assessment.md)

| Entity | Domain context | Canonical table | Owning service | Documented |
| ------ | -------------- | --------------- | -------------- | ---------- |
| `User` | User Management | `users` | `ms-iam` | ✅ |
| `EducationalEnvironment` | Environmental Monitoring | `educational_environment` | `ms-classroom-management` | ✅ |
| `EnvironmentalData` | Environmental Monitoring | `environment_measurement` | `ms-environment-monitoring` | ✅ |
| `EnvironmentalDataAnalysis` | Environmental Data Analysis | `environmental_analysis` + `analysis_result` | `ms-environment-monitoring` | ✅ |
| `Sensor` | Environmental Monitoring | `sensor` | `ms-sensor-management` | ✅ |
| `Variable` | Environmental Monitoring | `variable` | `ms-sensor-management` | ✅ |
| `VariableThreshold` | Environmental Monitoring | `variable_threshold` | `ms-environment-monitoring` | ✅ |

**Coverage: 7/7 entities mapped to a canonical table.**

---

## Inverse traceability: HU → FR

| HU           | Title                                | FR(s) it implements | Sprint   |
| ------------ | ------------------------------------ | ------------------- | -------- |
| HU-IAM-001   | User registration                    | FR-001              | Sprint 1 |
| HU-IAM-002   | User authentication / Login          | FR-002              | Sprint 1 |
| HU-IAM-003   | Role-based access control            | FR-015              | Sprint 1 |
| HU-USER-001  | User management                      | FR-003              | Sprint 2 |
| HU-CLASS-001 | Educational classroom management     | FR-004              | Sprint 2 |
| HU-SENS-001  | Environmental sensor management      | FR-005              | Sprint 2 |
| HU-SENS-002  | Sensor communication                 | FR-017              | Sprint 3 |
| HU-MON-001   | Environmental data reception         | FR-006              | Sprint 3 |
| HU-MON-002   | Environmental data storage           | FR-007              | Sprint 3 |
| HU-MON-003   | Current environmental monitoring     | FR-008              | Sprint 3 |
| HU-MON-004   | Environmental variables consultation | FR-014              | Sprint 3 |
| HU-ANA-001   | Daily environmental analysis         | FR-009              | Sprint 4 |
| HU-ANA-002   | Weekly environmental analysis        | FR-010              | Sprint 4 |
| HU-ANA-003   | Monthly environmental analysis       | FR-011              | Sprint 4 |
| HU-ANA-004   | Yearly environmental analysis        | FR-012              | Sprint 4 |
| HU-ANA-005   | Environmental dashboard              | FR-013              | Sprint 4 |
| HU-ARCH-001  | API Gateway communication            | FR-016              | Sprint 2 |

---

## Status legend

| Status         | Meaning                                 |
| -------------- | --------------------------------------- |
| ✅ Done         | Implemented, tested, and in production  |
| 🟡 In progress | Under development in the current sprint |
| 🔴 Pending     | In the backlog, not started             |
| ⏸ Blocked      | Has an external blocker                 |
| ❌ Cancelled    | Removed from scope                      |

---

## Identified gaps (requirements without coverage)

> This section is updated automatically or manually when reviewing the matrix.
>
> A gap is: an FR without an HU, or an HU without a test, or a test without implementation.

| Gap type                         | Description                                                                                                    | Required action                                                                  | Owner                 | Date       |
| -------------------------------- | -------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------- | --------------------- | ---------- |
| HU without test                  | Some HUs currently have planned test names but their automated test implementation has not yet been confirmed. | Create and execute the corresponding test cases before marking the HU as Done.   | QA / Development Team | 2026-09-04 |
| NFR without automated validation | Several NFRs are defined but their automated validation has not yet been implemented.                          | Add performance, security, coverage and Docker validation to the CI/CD pipeline. | Development / DevOps  | 2026-09-04 |
| Monitoring validation pending    | Observability requirements are defined but complete monitoring infrastructure has not yet been confirmed.      | Configure and validate logs, metrics and health checks.                          | Development / DevOps  | 2026-09-04 |
| DR validation pending            | Backup and restoration procedures have not yet been fully validated.                                           | Perform a database backup and restoration test and document the procedure.       | Development / DevOps  | 2026-09-04 |

---

## How to maintain this matrix

1. When an FR is identified: create or associate the corresponding HU.
2. When an HU is created: add the row in the FR → HU → Test → Service section.
3. When a test is written: note the test file in the "Tests that verify it" column.
4. When an HU is implemented: identify the responsible service and implementation.
5. When an HU is completed: change the status to ✅ only after its acceptance criteria and tests have been completed.
6. At each Sprint Planning: review gaps and assign actions.
7. At each Sprint Review: update the implementation and status of each HU.
8. When a requirement is removed from the project scope: mark the corresponding requirement and HU as ❌ Cancelled instead of deleting the traceability record.
9. When a new microservice is created: update the Service column and verify that the new service has corresponding requirements and HUs.
10. When code is added without an associated requirement or HU: review whether it is necessary functionality, technical debt, refactoring or gold-plating.

---

## Correlations

* User Stories → `04-requirements/user-stories.md`
* Functional Requirements → `04-requirements/functional-requirements.md`
* Non-Functional Requirements → `04-requirements/non-functional.md`
* Testing strategy → `11-quality/testing-strategy.md`
* Definition of Done → `00-governance/definition-of-done.md`
* Microservice catalog → `09-microservices/service-catalog.md`
* Architecture overview → `05-architecture/overview.md`
* Domain entities → `02-domain/entities-and-rules.md`
* Entity → table crosswalk → `06-data/normalization-assessment.md`
