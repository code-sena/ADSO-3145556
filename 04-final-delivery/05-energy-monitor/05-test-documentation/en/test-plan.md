# EnergyMonitor Test Plan and Report

Juan Esteban Cortés Parra and Miguel Ángel García Artunduaga

Servicio Nacional de Aprendizaje (SENA)

Technologist in Software Analysis and Development (ADSO), group (ficha) 3145556

October 14, 2026 (updated October 28, 2026)

## Version Control

**Table 1**

*Document version control*

| Version | Date       | Description of change                                                             | Authors                                       |
|---------|------------|-----------------------------------------------------------------------------------|-----------------------------------------------|
| 1.0     | 10/14/2026 | First version: inventory, traceability and results of the run without a database. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.1     | 10/20/2026 | Run of the full suite against MySQL (1131 tests, 0 failures) locally and in GitHub Actions; DEF-01 and DEF-02 closed; 3 new push notification test classes; traceability, risks and evidence updated. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.2     | 10/24/2026 | Services architecture: items under test per repository (gateway, security, device and measurement services, backend) with the `main` commits in production, totals of each suite, end-to-end tests of `energy-monitor-infra` and pipelines per repository. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.3     | 10/28/2026 | Complete migration: eight Java suites per repository (backend retired), web with 228 tests, five end-to-end tests of `energy-monitor-infra`, static verification and manual test protocol of the mobile application, traceability of the acceptance of terms (RF-SEC-13) and of the rules for Google accounts, and updated risks. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.4     | 10/28/2026 | New tests of the unusual consumption alert, the photo viewer, the photos of the members, the Settings help modals and push in Brave; updated `main` commits; traceability with RF-ALE-07, RF-HOM-12, RF-CLI-07 and RF-CLI-08 (112 requirements, 75 with an automated test). | Cortés Parra, J. E.; García Artunduaga, M. A. |

## Abstract

This document is the test plan and report of EnergyMonitor. It inventories the 129 automated test classes of the core service (1097 declared test methods) and the 20 suites of the web application (141 tests), traces the 107 functional and non-functional requirements of the specification against those tests, and records the run of October 14, 2026. The core service suite without a database ran 835 tests, 833 passed and 2 failed; the web ran 141 tests, all passed. The 37 classes that need MySQL were not run because the SPRING_DATASOURCE_PASSWORD variable is missing. The mobile application has no automated tests. 4 new test classes were written and 2 defects and 1 documentary observation were recorded.

Version 1.1 (October 20, 2026) records the run of the full core service suite against MySQL 8.4: 1131 tests in 132 classes, 0 failures, both locally and in the required `Tests` check of GitHub Actions. The 37 pending classes now run, defects DEF-01 and DEF-02 are closed and the web passes its 141 tests again. Traceability stands at 68 requirements with an automated test run and 39 with no coverage.

Version 1.2 (October 24, 2026) corresponds to the services architecture in production: the security, device and measurement modules left the core service for their own repositories (`en-monitor-security-service`, `en-monitor-device-service` and `en-monitor-measurement-service`), behind an API gateway (`en-monitor-api-gateway`), and the core service keeps home, alert, recommendation and notification. Each repository runs its suite in its pipeline: backend 481 tests, measurement service 151, device service 130 and gateway 57 (October 24), and security service 660 (October 22), all green, with `ModularityTests` green. Two end-to-end tests on the local stack (`infra/e2e/device-flow.sh`, 21/21, and `infra/e2e/measurement-flow.sh`, 25/25) verify what crosses the services. The per-class inventory and the traceability of this document are still those of version 1.1.

Version 1.3 (October 28, 2026) corresponds to the complete migration: home, notification, alert and recommendation are also independent services (`en-monitor-home-service`, `en-monitor-notification-service`, `en-monitor-alert-service` and `en-monitor-recommendation-service`) and the core service was retired. Eight Java suites green (security 773, homes 260, notifications 234, measurements 151, devices 129, alerts 119, recommendations 71 and gateway 69), the web with 38 files and 228 tests, and five end-to-end tests of `energy-monitor-infra` (`device-flow` 24, `measurement-flow` 28, `alert-flow` 36, `activity-flow` 60 and `account-deletion-flow` 12). Traceability goes to 108 requirements with the acceptance of terms (RF-SEC-13): 71 with an automated test (with the two language requirements of the mobile application) and 37 without coverage. For the mobile application a unit suite of 78 tests in 8 suites was added, all passed, and type checking and `expo lint` ran without errors; a protocol of 12 manual tests was also defined, still to be run on a device.

Version 1.4 (October 28, 2026) adds tests of the latest work: the unusual consumption rule of a device (`RegisterAnomalyAlertServiceTest`, 8 cases, and its place in the readings consumer), the wording of that alert in mail and push, the profile with a photo (`UserProfileQueryServiceTest`, the home adapter and the members endpoint), the photo viewer and the photos of the members, the Settings help modals (documentation by role, support only for users and news) and the explanation of push in Brave. Totals: 1,817 tests in the eight Java services (security 774, homes 260, notifications 235, measurements 151, devices 129, alerts 128, recommendations 71 and gateway 69), 253 in the web (44 files) and 78 in the mobile. Traceability goes to 112 requirements: 75 with an automated test and 37 without coverage.

*Keywords:* software testing, traceability, JUnit, Vitest, Spring Modulith, EnergyMonitor

## Introduction

### Purpose

To document the tests that already exist and run in EnergyMonitor, and show which requirements they cover and which they do not before the delivery on October 28, 2026. The structure is adapted from IEEE 829-2008 and ISO/IEC/IEEE 29119-3.

### Scope

The API gateway and the seven services (security, devices, measurements, homes, notifications, alerts and recommendations), the web application, MQTT ingestion and broker authorization are tested. Up to version 1.1 those contexts were the seven modules of the core service; since 1.2 they were split into services, the core service was retired on October 28, 2026 and each service is tested in its own repository, and the end-to-end tests of `energy-monitor-infra` verify the communication between them (gateway routes, internal API, broker and events on RabbitMQ). The device firmware, the Mosquitto broker, Bluetooth, push notifications and the Android mobile application are outside the automated tests and are declared as known gaps; for the mobile application, type checking and static analysis are also run and a manual test protocol is defined (version 1.3). Performance, load and accessibility (WCAG) are not tested; the specification sets no target for them.

### Document references

Traceability uses the codes of the requirements specification (RF-SEC, RF-HOM, RF-DEV, RF-MED, RF-ALE, RF-REC, RF-NOT, RF-CLI and RNF) and the user stories HU-01 to HU-15 (Cortés Parra & García Artunduaga, 2026). IDs, class names and codes are identical in the Spanish and English versions.

## Items Under Test

Table 2 lists the items under test in version 1.4, with the branch and the `main` commit at the close of October 28, 2026; version 1.3 used those of that day before the last changes (security `f57c76b`, homes `f424083`, notifications `7b10398`, alerts `f050b56` and web `698d4c9`). Version 1.2 used `main` of October 24, 2026 (gateway `c070cec`, security `334691a`, devices `f8b8526`, measurements `ee2cb81`, backend `aa9a713`, web `e59bece`, infra `eaf961b`). Version 1.1 used `main` (backend `90ae5ef`, web `2b41cb3`, infra `7324110`, mobile `dev` `4ebccfb`) and version 1.0, `dev` (backend `d03bbd3`, web `e6edc12`).

**Table 2**

*Items under test, version, branch and commit*

| Item                      | Version                         | Branch | Commit  | Remark                                                    |
|---------------------------|---------------------------------|--------|---------|-----------------------------------------------------------|
| en-monitor-api-gateway    | Java 21 · Spring Boot 4.0.8 · Spring Cloud Gateway | main | 0b2c164 | API gateway; 69 tests                                  |
| en-monitor-security-service | Java 21 · Spring Boot 4.1.1   | main   | de52c95 | Security service; 774 tests                                              |
| en-monitor-device-service | Java 21 · Spring Boot 4.1.1     | main   | 06c1bdc | Device service; 129 tests                                                |
| en-monitor-measurement-service | Java 21 · Spring Boot 4.1.1 | main  | 8a9b544 | Measurement service; 151 tests                                           |
| en-monitor-home-service   | Java 21 · Spring Boot 4.1.1     | main   | 6a4e396 | Home service; 260 tests                                                  |
| en-monitor-notification-service | Java 21 · Spring Boot 4.1.1 | main | ecdbc0a | Notification service; 235 tests                                          |
| en-monitor-alert-service  | Java 21 · Spring Boot 4.1.1     | main   | 0e65430 | Alert service; 128 tests                                                 |
| en-monitor-recommendation-service | Java 21 · Spring Boot 4.1.1 | main | aeeeec5 | Recommendation service; 71 tests                                     |
| energy-monitor-backend    | Java 21 · Spring Boot 4.1.1 · Spring Modulith | main | c3c6df8 | Retired on 10/28/2026: its modules are the four services above; keeps the firmware (`firmware/`, version 1.3.8) |
| energy-monitor-web        | React 19 · Vite 8               | main   | 27ad9f0 | Web application; 44 files and 253 tests                                  |
| energy-monitor-mobile     | Expo SDK 57 · React Native 0.86 | dev    | 8b5edc0 | 78 unit tests in 8 suites; `tsc` and `expo lint` without errors. `main` is still at `894b65a`, 193 commits behind `dev` |
| energy-monitor-infra      | Docker Compose · Mosquitto 2 · RabbitMQ 4.1 | main | ceb1543 | Compose, broker and scripts validated in CI; end-to-end tests `e2e/`; replaces `devices`, retired |
| energy-monitor-docs-final | Markdown ES/EN                  | dev    | —       | Final documentation (this document)                       |

## Test Strategy

### Test levels

**Table 3**

*Test levels and their status in the project*

| No. | Level                                           | Classes/suites | Test methods | Classes run |
|-----|-------------------------------------------------|----------------|--------------|-------------|
| 1   | Domain                                          | 21             | 342          | 21          |
| 2   | Use cases and adapters without a Spring context | 51             | 285          | 51          |
| 3   | Web (controllers)                               | 10             | 144          | 10          |
| 4   | Security                                        | 18             | 195          | 18          |
| 5   | Integration with real MySQL                     | 28             | 133          | 28          |
| 6   | Architecture                                    | 1              | 2            | 1           |
| 7   | Events between modules                          | 1              | 1            | 1           |
| 8   | MQTT and broker                                 | 2              | 16           | 2           |
| 9   | Web frontend (Vitest)                           | 20             | 141          | 20          |

All the levels in the list exist in the project. Level 9 corresponds to the web suites. The mobile application has no automated tests. In version 1.1 every core service class runs; the counts include the 3 new push notification classes (PushDeviceTest, ExpoPushSenderTest and PushDevicePersistenceTest).

### Techniques and tools

White-box tests (domain and use cases with mocked ports) and black-box tests (HTTP contracts) are combined, with equivalence partitioning and boundary values (for example the power levels at 500, 1500 and 3000 W, the thresholds and the 150 seconds of silence). The new tests follow the AAA style and the project's conventions (descriptive names with @DisplayName).

**Table 4**

*Test tools and verified versions*

| Tool                                           | Version                    | Use                                                            |
|------------------------------------------------|----------------------------|----------------------------------------------------------------|
| Spring Boot / spring-boot-starter-test         | 4.1.1                      | Context tests, MockMvc and @MockitoBean (never @MockBean)      |
| JUnit Jupiter                                  | 6.0.3                      | Execution and parameterized tests                              |
| Mockito                                        | 5.x      | Mocked ports (5.20.0 and 5.23.0 in the local Maven repository) |
| AssertJ                                        | 3.27.7                     | Assertions                                                     |
| Spring Modulith (spring-modulith-starter-test) | 2.1.1                      | ModularityTests: module boundaries                             |
| Vitest / Testing Library (React)               | 5.0.3 / 16.3.3             | Web application tests (jsdom)                                  |
| Java / Node.js / Docker                        | 21.0.12 / 22.22.2 / 29.8.2 | Execution environment                                          |
| JaCoCo                                         | Not in pom.xml             | No coverage report (see recommendations)                       |
| GitHub Actions                                 | ubuntu-latest runner       | Continuous integration: `Tests` (gateway, services and backend), `Tests and build` (web) and `Validate compose and scripts` (infra) checks |
| End-to-end scripts (`infra/e2e/`)              | bash, `jq`, `openssl`, `mosquitto_pub` | Walkthroughs across services on the full local stack (version 1.2) |

## Test Environment

Persistence and full-context tests run against real MySQL 8.4 (container energy-monitor-mysql), not against H2. Liquibase owns the schema and Hibernate is set to ddl-auto: validate; an H2 database or @AutoConfigureTestDatabase(replace = ANY) would change the dialect and defeat the validation of the real schema. The cost is that these tests are not hermetic: they need MySQL running, the test profile and the SPRING_DATASOURCE_PASSWORD variable; the default user is energy_app.

**Table 5**

*Environment components*

| Component      | Detail                                                                         |
|----------------|--------------------------------------------------------------------------------|
| MySQL 8.4      | Docker, energy-monitor-mysql (healthy)                                         |
| Spring profile | test (src/test/resources/application-test.yaml); recommendation.enabled: false |
| CI (GitHub Actions) | `mysql:8.4` service container (user energy_app), `application-local.yaml` built from the example and a throwaway RSA pair; the `Tests` job takes about 23 minutes |

## Entry, Exit, Suspension and Resumption Criteria

**Table 6**

*Test criteria*

| Criterion  | Condition                                                                                                                   |
|------------|-----------------------------------------------------------------------------------------------------------------------------|
| Entry      | Compilable code on dev; MySQL running and SPRING_DATASOURCE_PASSWORD defined for context tests; npm install run on the web. |
| Exit       | All automated tests passed, no open high-severity defect and the classes with MySQL run.                                    |
| Suspension | MySQL unavailable, missing credentials or a build failure.                                                                  |
| Resumption | Environment restored; resume from the class that stopped.                                                                   |

## Inventory of Automated Tests

Each table corresponds to a module of the core service as it was in version 1.1, when the seven modules lived in `energy-monitor-backend`. Since version 1.2 the code of security, device and measurement is in their own repositories, which have their own suites (Table 2); this inventory was not redone per class for version 1.2. “Status” is Passed, Fails (with its defect) or “To run (MySQL)” for the classes that boot a context with a database. New classes are marked NEW in their description. In version 1.1 every class ran against MySQL and passed, so the status of all of them is “Passed”.

**Table 7**

*Tests of the security module*

| Level                  | Test class                            | What it verifies                                                                                    | Methods | Requirements covered                                                                    | Status         |
|------------------------|---------------------------------------|-----------------------------------------------------------------------------------------------------|---------|-----------------------------------------------------------------------------------------|----------------|
| Domain                 | AuditLogTest                          | Rules and invariants of AuditLog                                                                    | 9       | RF-SEC-12                                                                               | Passed         |
| Domain                 | PasswordResetTokenTest                | Rules and invariants of PasswordResetToken                                                          | 19      | RF-SEC-06                                                                               | Passed         |
| Domain                 | RbacTest                              | Rules and invariants of Rbac                                                                        | 14      | RF-SEC-09                                                                               | Passed         |
| Domain                 | RefreshTokenTest                      | Rules and invariants of RefreshToken                                                                | 41      | RF-SEC-04, RNF-SEG-02                                                                   | Passed         |
| Domain                 | SecuritySupportModelTest              | Rules and invariants of SecuritySupportModel                                                        | 28      | RF-SEC-12                                                                               | Passed         |
| Domain                 | UserSessionTest                       | Rules and invariants of UserSession                                                                 | 45      | RF-SEC-04                                                                               | Passed         |
| Domain                 | UserTest                              | Rules and invariants of User                                                                        | 21      | RF-SEC-01                                                                               | Passed         |
| Domain                 | ValueObjectTest                       | Rules and invariants of ValueObject                                                                 | 14      | RF-SEC-01                                                                               | Passed         |
| Use cases and adapters | AccountLifecycleServiceTest           | Account query, profile edit and account state change                                                | 9       | RF-SEC-08                                                                               | Passed         |
| Use cases and adapters | AuthenticateUserServiceTest           | Account state and credentials classified; every failure is recorded                                 | 6       | RF-SEC-03                                                                               | Passed         |
| Use cases and adapters | ChangePasswordServiceTest             | Password change with current-password check and policy                                              | 3       | RF-SEC-07                                                                               | Passed         |
| Use cases and adapters | DeleteAccountServiceTest              | Deleting the account checks the password and announces AccountDeleted                               | 2       | RF-SEC-11                                                                               | Passed         |
| Use cases and adapters | EmailVerificationServiceTest          | Six-digit verification code valid for at least 15 minutes                                           | 6       | RF-SEC-02                                                                               | Passed         |
| Use cases and adapters | PasswordResetServiceTest              | Recovery: single-use code, validity window and attempt budget                                       | 16      | RF-SEC-06                                                                               | Passed         |
| Use cases and adapters | RefreshSessionServiceTest             | Renewal: single use, lineage, session identity and replay escalation                                | 20      | RF-SEC-04, RNF-SEG-01, RNF-SEG-02                                                       | Passed         |
| Use cases and adapters | RegisterUserServiceTest               | Registration: password hash, person and user, duplicate and policy rejection                        | 7       | RF-SEC-01, RF-SEC-12                                                                    | Passed         |
| Use cases and adapters | SignInWithGoogleServiceTest           | Sign in with Google and account creation                                                            | 7       | RF-SEC-05, RNF-INT-03                                                                   | Passed         |
| Use cases and adapters | UserAdministrationServiceTest         | Account administration: an administrator never acts on their own account                            | 18      | —                                                                                       | Passed         |
| Use cases and adapters | UserProfileQueryServiceTest           | Name and email of known accounts                                                                    | 1       | RF-SEC-08                                                                               | Passed         |
| Use cases and adapters | UserSessionServiceTest                | Session opening with opaque refresh token and logout                                                | 6       | RF-SEC-04                                                                               | Passed         |
| Web (controllers)      | AdminControllerWebTest                | Administration endpoints /api/v1/admin with RBAC permissions and 409 on self-administration         | 15      | —                                                                                       | Passed         |
| Web (controllers)      | AuthControllerWebTest                 | HTTP contract of authentication, account, password, profile, Google and permissions (401, 403, 429) | 66      | RF-SEC-01, RF-SEC-02, RF-SEC-03, RF-SEC-05, RF-SEC-07, RF-SEC-08, RF-SEC-09, RNF-SEG-06 | Passed         |
| Security               | AuthRefreshEndpointWebTest            | HTTP contract of POST /api/v1/auth/refresh                                                          | 18      | RF-SEC-04                                                                               | Passed         |
| Security               | BCryptPasswordHasherAdapterTest       | BCryptPasswordHasherAdapter                                                                         | 10      | RF-SEC-03                                                                               | Passed         |
| Security               | InMemoryResetAttemptLimiterTest       | Limit of 10 attempts per IP every 15 minutes                                                        | 8       | RNF-SEG-06                                                                              | Passed         |
| Security               | JwtTokenIssuerTest                    | RS256 JWT issuing and validation (15 minutes)                                                       | 18      | RF-SEC-03, RNF-SEG-01                                                                   | Passed         |
| Security               | PemRsaKeysTest                        | Which PEM keys are accepted and the message on refusal                                              | 19      | —                                                                                       | Passed         |
| Security               | PepperedHmacResetCodeHasherTest       | PepperedHmacResetCodeHasher                                                                         | 15      | RF-SEC-06, RNF-SEG-05                                                                   | Passed         |
| Security               | PoolSaturatedLimiterTest              | The per-IP limit applies without waiting for a pool connection (429)                                | 2       | RNF-SEG-06                                                                              | Passed         |
| Security               | RbacServiceTest                       | Assign and revoke roles with audit and permission checks                                            | 17      | RF-SEC-09                                                                               | Passed         |
| Security               | ResetAttemptLimiterBeforeDatabaseTest | A redemption refused by the limit opens no transaction                                              | 3       | RNF-SEG-06                                                                              | Passed         |
| Security               | SecureIdentifierGeneratorAdapterTest  | SecureIdentifierGeneratorAdapter                                                                    | 5       | —                                                                                       | Passed         |
| Security               | SecureTokenGeneratorAdapterTest       | SecureTokenGeneratorAdapter                                                                         | 13      | —                                                                                       | Passed         |
| Security               | SecurityCorsConfigurationTest         | CORS policy guards without booting a context                                                        | 11      | RNF-SEG-03                                                                              | Passed         |
| Security               | SecurityCorsWebTest                   | CORS contract: origins answered and preflight response                                              | 23      | RNF-SEG-03                                                                              | Passed         |
| Security               | SecurityLoginFlowTest                 | Order of the login exchange and what it refuses                                                     | 11      | RF-SEC-03                                                                               | Passed         |
| Security               | Sha256TokenHasherTest                 | Sha256TokenHasher                                                                                   | 9       | RF-SEC-04                                                                               | Passed         |
| Integration with real  | ConfigurationPolicyPersistenceTest    | ConfigurationPolicy round trips against real MySQL                                                  | 6       | RF-SEC-12                                                                               | Passed         |
| Integration with real  | LogPersistenceTest                    | Log round trips against real MySQL                                                                  | 5       | RF-SEC-12                                                                               | Passed         |
| Integration with real  | PasswordPolicySeedTest                | Password policy seeded by changeset security-019                                                    | 5       | RF-SEC-12                                                                               | Passed         |
| Integration with real  | PasswordResetTokenReservationTest     | Redemption attempt reservation against MySQL                                                        | 6       | RF-SEC-06                                                                               | Passed         |
| Integration with real  | RbacAssignmentPersistenceTest         | RbacAssignment round trips against real MySQL                                                       | 2       | RF-SEC-09                                                                               | Passed         |
| Integration with real  | RefreshLifecycleIntegrationTest       | Full lifecycle: open session, rotate, detect replay and revoke the family                           | 15      | RF-SEC-04, RNF-SEG-02                                                                   | Passed         |
| Integration with real  | RefreshSessionTransactionTest         | Refresh token rotation is atomic against the real database                                          | 6       | RF-SEC-04                                                                               | Passed         |
| Integration with real  | RefreshTokenPersistenceTest           | RefreshToken round trips against real MySQL                                                         | 10      | RF-SEC-04                                                                               | Passed         |
| Integration with real  | SecurityTransactionBoundaryTest       | Where the transaction boundary sits and that Spring honours it                                      | 5       | —                                                                                       | Passed         |
| Integration with real  | SecurityTransactionRollbackTest       | The transaction really rolls back                                                                   | 3       | —                                                                                       | Passed         |
| Integration with real  | TokenSessionPersistenceTest           | TokenSession round trips against real MySQL                                                         | 6       | RF-SEC-04                                                                               | Passed         |
| Integration with real  | UserPersonPersistenceTest             | UserPerson round trips against real MySQL                                                           | 5       | RF-SEC-01, RNF-MAN-03                                                                   | Passed         |

**Table 8**

*Tests of the home module*

| Level                  | Test class                       | What it verifies                                                                      | Methods | Requirements covered                                                                      | Status         |
|------------------------|----------------------------------|---------------------------------------------------------------------------------------|---------|-------------------------------------------------------------------------------------------|----------------|
| Domain                 | HomeTest                         | Rules and invariants of Home                                                          | 16      | RF-HOM-01                                                                                 | Passed         |
| Domain                 | HomeThresholdsTest               | Rules and invariants of HomeThresholds                                                | 17      | RF-HOM-08                                                                                 | Passed         |
| Domain                 | HomeTypeTest                     | Rules and invariants of HomeType                                                      | 8       | —                                                                                         | Passed         |
| Domain                 | MemberPermissionsTest            | Rules and invariants of MemberPermissions                                             | 3       | RF-HOM-05                                                                                 | Passed         |
| Domain                 | OwnerPermissionsTest             | Rules and invariants of OwnerPermissions                                              | 3       | RF-HOM-05                                                                                 | Passed         |
| Domain                 | RoleTest                         | Rules and invariants of Role                                                          | 3       | RF-HOM-05                                                                                 | Passed         |
| Domain                 | UserHomeTest                     | Rules and invariants of UserHome                                                      | 26      | RF-HOM-05                                                                                 | Passed         |
| Use cases and adapters | CreateHomeServiceTest            | CreateHome use case: business rules and errors with mocked ports                      | 6       | RF-HOM-01                                                                                 | Passed         |
| Use cases and adapters | DeleteHomeServiceTest            | Only the owner deletes the household; announces HomeDeleted; a member gets 403        | 4       | RF-HOM-09, RNF-MAN-03                                                                     | Passed         |
| Use cases and adapters | GetHomeThresholdsServiceTest     | GetHomeThresholds use case: business rules and errors with mocked ports               | 2       | RF-HOM-08                                                                                 | Passed         |
| Use cases and adapters | GetHomeTypeServiceTest           | GetHomeType use case: business rules and errors with mocked ports                     | 2       | —                                                                                         | Passed         |
| Use cases and adapters | JoinHomeServiceTest              | JoinHome use case: business rules and errors with mocked ports                        | 4       | RF-HOM-03                                                                                 | Passed         |
| Use cases and adapters | LeaveHomeServiceTest             | LeaveHome use case: business rules and errors with mocked ports                       | 4       | RF-HOM-06                                                                                 | Passed         |
| Use cases and adapters | ListHomeTypesServiceTest         | ListHomeTypes use case: business rules and errors with mocked ports                   | 2       | —                                                                                         | Passed         |
| Use cases and adapters | ListHomesServiceTest             | ListHomes use case: business rules and errors with mocked ports                       | 5       | RF-HOM-02                                                                                 | Passed         |
| Use cases and adapters | ListMembersServiceTest           | ListMembers use case: business rules and errors with mocked ports                     | 4       | RF-HOM-04                                                                                 | Passed         |
| Use cases and adapters | RemoveDeletedAccountServiceTest  | Deleted account: drops owned households and refuses if sole owner with members        | 2       | RF-SEC-11                                                                                 | Passed         |
| Use cases and adapters | RemoveUserServiceTest            | RemoveUser use case: business rules and errors with mocked ports                      | 5       | RF-HOM-06                                                                                 | Passed         |
| Use cases and adapters | ToggleFavoriteServiceTest        | ToggleFavorite use case: business rules and errors with mocked ports                  | 2       | RF-HOM-07                                                                                 | Passed         |
| Use cases and adapters | TransferHomeOwnershipServiceTest | Ownership transfer: roles swapped, single owner, invalid targets                      | 6       | RF-HOM-10                                                                                 | Passed         |
| Use cases and adapters | UpdateHomeThresholdsServiceTest  | UpdateHomeThresholds use case: business rules and errors with mocked ports            | 7       | RF-HOM-08                                                                                 | Passed         |
| Web (controllers)      | HomeControllerTest               | Household endpoints: create, list, join, favorite, remove, leave, delete and transfer | 26      | RF-HOM-01, RF-HOM-02, RF-HOM-03, RF-HOM-04, RF-HOM-06, RF-HOM-07, RF-HOM-08, RF-HOM-09, … | Passed         |
| Web (controllers)      | HomeExceptionHandlerTest         | What the module answers when a request cannot be attributed to a user                 | 2       | RF-SEC-10                                                                                 | Passed         |
| Integration with real  | HomePersistenceTest              | Home round trips against real MySQL                                                   | 6       | RF-HOM-01, RNF-MAN-03                                                                     | Passed         |
| Integration with real  | HomeThresholdsPersistenceTest    | HomeThresholds round trips against real MySQL                                         | 3       | RF-HOM-08                                                                                 | Passed         |
| Integration with real  | HomeTypePersistenceTest          | HomeType round trips against real MySQL                                               | 3       | —                                                                                         | Passed         |
| Integration with real  | HomeTypeSeedTest                 | The catalog holds exactly the six seeded household types                              | 1       | RF-HOM-01                                                                                 | Passed         |
| Integration with real  | UserHomePersistenceTest          | UserHome round trips against real MySQL                                               | 7       | RF-HOM-05                                                                                 | Passed         |

**Table 9**

*Tests of the device module*

| Level                  | Test class                    | What it verifies                                                                            | Methods | Requirements covered                                  | Status |
|------------------------|-------------------------------|---------------------------------------------------------------------------------------------|---------|-------------------------------------------------------|--------|
| Use cases and adapters | BrokerAccessServiceTest       | Broker authentication of the clear key against the hash and topic authorization             | 8       | RF-DEV-07, RNF-SEG-09                                 | Passed |
| Use cases and adapters | DeviceConnectivityServiceTest | Online and offline status, change log and 150 s of silence                                  | 6       | RF-ALE-02, RF-MED-05, RF-MED-06, RNF-DIS-02           | Passed |
| Use cases and adapters | HomeDeviceServiceTest         | Link, list, edit, reissue credentials and unlink with owner rules                           | 9       | RF-DEV-02, RF-DEV-03, RF-DEV-04, RF-DEV-05, RF-DEV-06 | Passed |
| Web (controllers)      | HomeDeviceControllerTest      | NEW. Household device endpoints: link, edit, credentials, unlink and 400/403/404/409 errors | 10      | RF-DEV-02, RF-DEV-03, RF-DEV-04, RF-DEV-05, RF-DEV-06 | Passed |
| Security               | ApiKeyHashTest                | SHA-256 hash of the device api_key                                                          | 3       | RF-DEV-07, RNF-SEG-09                                 | Passed |
| MQTT and broker        | BrokerAuthControllerTest      | NEW. /internal/mqtt/auth/user and /acl: 200 grants, 403 denies, access codes 1 to 4         | 8       | RF-DEV-07                                             | Passed |

**Table 10**

*Tests of the measurement module*

| Level                  | Test class                          | What it verifies                                                                           | Methods | Requirements covered            | Status         |
|------------------------|-------------------------------------|--------------------------------------------------------------------------------------------|---------|---------------------------------|----------------|
| Domain                 | ConsumptionLevelTest                | LOW, MEDIUM, HIGH and CRITICAL levels and their limits                                     | 14      | RF-MED-09                       | Passed         |
| Domain                 | MeasurementTest                     | Rules and invariants of Measurement                                                        | 12      | RF-MED-02                       | Passed         |
| Use cases and adapters | GetConsumptionStatisticsServiceTest | GetConsumptionStatistics use case: business rules and errors with mocked ports             | 5       | RF-MED-07                       | Passed         |
| Use cases and adapters | GetLatestMeasurementServiceTest     | GetLatestMeasurement use case: business rules and errors with mocked ports                 | 2       | RF-MED-07                       | Passed         |
| Use cases and adapters | HomeConsumptionUseCaseTest          | Household consumption summary and history                                                  | 8       | RF-MED-08                       | Passed         |
| Use cases and adapters | ListConsumptionLevelsServiceTest    | ListConsumptionLevels use case: business rules and errors with mocked ports                | 2       | RF-MED-09                       | Passed         |
| Use cases and adapters | ListMeasurementsServiceTest         | ListMeasurements use case: business rules and errors with mocked ports                     | 4       | RF-MED-07                       | Passed         |
| Use cases and adapters | RegisterMeasurementServiceTest      | RegisterMeasurement use case: business rules and errors with mocked ports                  | 4       | RF-MED-02                       | Passed         |
| Web (controllers)      | ConsumptionLevelControllerTest      | HTTP codes and body of ConsumptionLevelController                                          | 1       | RF-MED-09                       | Passed         |
| Web (controllers)      | MeasurementControllerTest           | HTTP codes and body of MeasurementController                                               | 5       | RF-MED-07                       | Passed         |
| Integration with real  | ConsumptionLevelPersistenceTest     | ConsumptionLevel round trips against real MySQL                                            | 4       | RF-MED-09                       | Passed         |
| Integration with real  | MeasurementPersistenceTest          | Measurement round trips against real MySQL                                                 | 4       | RF-MED-02                       | Passed         |
| MQTT and broker        | TelemetryMqttConsumerTest           | NEW. Telemetry ingestion: the topic identifies the device and invalid messages are dropped | 8       | RF-DEV-07, RF-MED-02, RF-MED-03 | Passed         |

**Table 11**

*Tests of the alert module*

| Level                  | Test class                           | What it verifies                                                              | Methods | Requirements covered | Status         |
|------------------------|--------------------------------------|-------------------------------------------------------------------------------|---------|----------------------|----------------|
| Domain                 | AlertTest                            | Rules and invariants of Alert                                                 | 16      | RF-ALE-03, RF-ALE-04 | Passed         |
| Use cases and adapters | GetAlertServiceTest                  | GetAlert use case: business rules and errors with mocked ports                | 2       | RF-ALE-05            | Passed         |
| Use cases and adapters | ListAlertsServiceTest                | ListAlerts use case: business rules and errors with mocked ports              | 3       | RF-ALE-05            | Passed         |
| Use cases and adapters | RegisterDeviceLinkedAlertServiceTest | Device linked notice on the first reading                                     | 4       | RF-ALE-01            | Passed         |
| Use cases and adapters | RegisterLimitAlertServiceTest        | Daily or monthly limit alerts                                                 | 4       | RF-ALE-04            | Passed         |
| Use cases and adapters | RegisterThresholdAlertServiceTest    | High or critical consumption alerts by power level                            | 7       | RF-ALE-03            | Passed         |
| Use cases and adapters | ResolveAlertServiceTest              | ResolveAlert use case: business rules and errors with mocked ports            | 4       | RF-ALE-05            | Passed         |
| Web (controllers)      | AlertControllerTest                  | Alert endpoints: list, get, read, delete and 404                              | 7       | RF-ALE-05            | Passed         |
| Integration with real  | AlertPersistenceTest                 | Alert round trips against real MySQL                                          | 4       | RF-ALE-05            | Passed         |
| Events between modules | MeasurementRecordedListenerTest      | The MeasurementRecorded event triggers threshold and limit alert registration | 1       | RF-ALE-03, RF-ALE-06 | Passed         |

**Table 12**

*Tests of the recommendation module*

| Level                  | Test class                         | What it verifies                                                                              | Methods | Requirements covered                                                                   | Status         |
|------------------------|------------------------------------|-----------------------------------------------------------------------------------------------|---------|----------------------------------------------------------------------------------------|----------------|
| Domain                 | RecommendationRulesTest            | Five saving rules: peak hours, standby, weekly comparison, projection and per-device increase | 8       | RF-REC-01, RF-REC-02, RF-REC-03, RF-REC-04, RF-REC-05, RF-REC-06, RF-REC-07, RF-REC-09 | Passed         |
| Use cases and adapters | EvaluateRecommendationsServiceTest | Rule evaluation per household and no repeats within 7 days                                    | 4       | RF-REC-01, RF-REC-07                                                                   | Passed         |
| Use cases and adapters | RecommendationInboxServiceTest     | RecommendationInbox use case: business rules and errors with mocked ports                     | 4       | RF-REC-08                                                                              | Passed         |
| Web (controllers)      | RecommendationControllerTest       | NEW. Recommendation inbox: list, read, delete, 404 and 409                                    | 7       | RF-REC-08                                                                              | Passed         |
| Integration with real  | RecommendationPersistenceTest      | Recommendation round trips against real MySQL                                                 | 3       | RF-REC-08                                                                              | Passed         |

**Table 13**

*Tests of the notification module*

| Level                  | Test class                            | What it verifies                                                                    | Methods | Requirements covered                                   | Status         |
|------------------------|---------------------------------------|-------------------------------------------------------------------------------------|---------|--------------------------------------------------------|----------------|
| Domain                 | NotificationTest                      | Rules and invariants of Notification                                                | 20      | RF-NOT-01, RF-NOT-05                                   | Passed         |
| Use cases and adapters | AbandonedDeliverySweeperTest          | Sweep of abandoned deliveries at startup                                            | 4       | RF-NOT-05                                              | Passed         |
| Use cases and adapters | AdminNotificationsTest                | Administrator notifications: who receives each event and reads scoped to the caller | 5       | —                                                      | Passed         |
| Use cases and adapters | EmailPasswordResetDeliveryAdapterTest | Recovery code delivery; nothing is sent if the transaction rolls back               | 5       | RF-NOT-06, RF-SEC-06                                   | Passed         |
| Use cases and adapters | EmailSenderTest                       | Recording of the attempt before sending and sending the email                       | 9       | RF-NOT-01, RF-NOT-05, RF-NOT-06, RF-SEC-02, RNF-INT-02 | Passed         |
| Use cases and adapters | NotifyAlertTest                       | Delivery of alerts and recommendations to members by email and push per preferences | 8       | RF-NOT-01, RF-NOT-02, RF-NOT-07                        | Passed         |
| Use cases and adapters | SendPasswordResetEmailTest            | Password recovery message and its record                                            | 11      | RF-NOT-06, RF-SEC-06                                   | Passed         |
| Use cases and adapters | WebPushSenderTest                     | Push message encryption (RFC 8291) as a browser decrypts it                         | 2       | RF-NOT-01, RF-NOT-04                                   | Passed         |
| Domain                 | PushDeviceTest                        | NEW (1.1). Invariants of the mobile device registered for push                      | 5       | RF-NOT-01                                              | Passed         |
| Use cases and adapters | ExpoPushSenderTest                    | NEW (1.1). Interpretation of the Expo push relay's answer                           | 9       | RF-NOT-01                                              | Passed         |
| Integration with real  | PushDevicePersistenceTest             | NEW (1.1). Registration and removal of mobile installations against the real schema | 7       | RF-NOT-01                                              | Passed         |
| Web (controllers)      | AdminNotificationsControllerWebTest   | Administrator notification endpoints                                                | 5       | —                                                      | Passed         |
| Integration with real  | NotificationPersistenceTest           | Delivery log against the real schema                                                | 4       | RF-NOT-05                                              | Passed         |

**Table 14**

*Tests of the cross-module module*

| Level                  | Test class                           | What it verifies                                                                               | Methods | Requirements covered  | Status         |
|------------------------|--------------------------------------|------------------------------------------------------------------------------------------------|---------|-----------------------|----------------|
| Security               | AdminRbacIntegrationTest             | Seeded ADMIN role, USER.READ, USER.MANAGE and ROLE.MANAGE codes and directory searches         | 6       | RF-SEC-09             | Passed         |
| Security               | HomeCallerIdentityIntegrationTest    | Caller identity on /homes through the real filter chain and an RS256 JWT; X-User-Id is ignored | 4       | RF-SEC-10             | Passed         |
| Integration with real  | AccountDeletionIntegrationTest       | Account deletion across security and home against the real schema                              | 3       | RF-SEC-11             | Passed         |
| Integration with real  | DeviceApiKeyStorageIntegrationTest   | Only the SHA-256 hash of the api_key reaches the table; broker login keeps working             | 3       | RF-DEV-02, RNF-SEG-09 | Passed         |
| Integration with real  | EnergyMonitorBackendApplicationTests | Boot of the full application context                                                           | 1       | —                     | Passed         |
| Integration with real  | HomeDeletionIntegrationTest          | Deleting a household drops memberships and thresholds and unlinks devices; a member cannot     | 2       | RF-DEV-06, RF-HOM-09  | Passed         |
| Integration with real  | HomeOwnershipTransferIntegrationTest | Atomic swap of OWNER and MEMBER roles against the real schema                                  | 4       | RF-HOM-05, RF-HOM-10  | Passed         |
| Architecture           | ModularityTests                      | ApplicationModules.verify(): the boundaries of the seven modules hold                          | 2       | RNF-MAN-01            | Passed         |

**Table 15**

*Web application tests (Vitest)*

| Suite                                                   | Tests | Requirements covered                       | Status |
|---------------------------------------------------------|-------|--------------------------------------------|--------|
| components/layout/MainLayout/Sidebar/Sidebar.test.jsx   | 2     | RF-CLI-01                                  | Passed |
| context/HomeContext.test.jsx                            | 11    | —                                          | Passed |
| features/Account/Account.test.jsx                       | 6     | RF-CLI-01, RF-SEC-08, RF-SEC-11            | Passed |
| features/Account/AccountDeleteError.test.jsx            | 1     | RF-SEC-11                                  | Passed |
| features/Admin/AdminNotifications.test.jsx              | 5     | —                                          | Passed |
| features/Admin/AdminUsers.test.jsx                      | 9     | —                                          | Passed |
| features/DetailHome/Home/DeleteHome/DeleteHome.test.jsx | 5     | RF-CLI-01, RF-HOM-09                       | Passed |
| features/DetailHome/Users/Users.test.jsx                | 12    | RF-CLI-01, RF-HOM-04, RF-HOM-06, RF-HOM-10 | Passed |
| features/auth/components/RPasswordForm/recover.test.jsx | 4     | RF-CLI-01                                  | Passed |
| features/auth/components/RegisterForm/verify.test.jsx   | 4     | RF-CLI-01                                  | Passed |
| features/dashboard/pages/DashboardPage.test.jsx         | 5     | RF-CLI-01, RF-HOM-02                       | Passed |
| features/shared/homeTypes.test.js                       | 6     | —                                          | Passed |
| routes/RequirePermission.test.jsx                       | 3     | —                                          | Passed |
| routes/UserOnlyRoute.test.jsx                           | 4     | RF-CLI-01                                  | Passed |
| services/admin/adminApi.test.js                         | 4     | —                                          | Passed |
| services/auth/authApi.test.js                           | 4     | —                                          | Passed |
| services/home/homeApi.test.js                           | 15    | —                                          | Passed |
| services/http/errorMessages.test.js                     | 27    | —                                          | Passed |
| services/http/httpClient.refresh.test.js                | 3     | —                                          | Passed |
| services/http/httpClient.test.js                        | 11    | —                                          | Passed |

Automated tests of the mobile application do not exist: the repository has no test files or test script (only expo lint). It is a declared gap. Since version 1.3 type checking (`tsc`) and `expo lint` are run, and a manual test protocol is defined (Table 38).

## Requirement → Test Traceability Matrix

The 107 requirements of the specification are crossed with the test classes. “Coverage” distinguishes what was run from what depends on MySQL. Result in version 1.0: 66 with an automated test run, 1 with automated tests that depend on MySQL and 40 with no coverage. Result in version 1.1: 68 with an automated test run (RNF-MAN-01 now runs and RF-NOT-02 is covered by NotifyAlertTest) and 39 with no coverage.

**Table 16**

*Traceability of the RF-SEC requirements*

| Requirement | Description                                                                                                 | Automated tests                                                                                                                                                                            | Coverage  |
|-------------|-------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------|
| RF-SEC-01   | The system must allow requesting the registration of an account with personal data, email and password… | RegisterUserServiceTest, MistypedEmailDomainsTest, AuthControllerWebTest, UserPersonPersistenceTest, UserTest, ValueObjectTest | Automated |
| RF-SEC-02   | The system must send a six-digit verification code to the given email and create the account… | EmailVerificationServiceTest, EmailSenderTest, AuthControllerWebTest | Automated |
| RF-SEC-03   | The system must allow signing in securely with email and password                                           | AuthenticateUserServiceTest, SecurityLoginFlowTest, AuthControllerWebTest, JwtTokenIssuerTest, BCryptPasswordHasherAdapterTest                                                             | Automated |
| RF-SEC-04   | The system must keep the user's session without asking for credentials continuously, let the user sign out… | RefreshSessionServiceTest, UserSessionServiceTest, UserSessionTest, RefreshTokenTest, AuthRefreshEndpointWebTest, RefreshLifecycleIntegrationTest, …                                       | Automated |
| RF-SEC-05   | The system must allow registering and signing in with a Google account, with acceptance of the terms | SignInWithGoogleServiceTest, AuthControllerWebTest | Automated |
| RF-SEC-06   | The system must allow password recovery with a six-digit code sent to the email, valid for 15 minutes and…  | PasswordResetServiceTest, PasswordResetTokenTest, PepperedHmacResetCodeHasherTest, SendPasswordResetEmailTest, EmailPasswordResetDeliveryAdapterTest, PasswordResetTokenReservationTest, … | Automated |
| RF-SEC-07   | The system must allow a signed-in user to change their password (not Google accounts) | ChangePasswordServiceTest, AuthControllerWebTest, account-deletion-flow.sh | Automated |
| RF-SEC-08   | The system must allow viewing the account data and updating the profile (first and last name separately) | AccountLifecycleServiceTest, UserProfileQueryServiceTest, AuthControllerWebTest, Account.test.jsx | Automated |
| RF-SEC-09   | The system must allow knowing the user's permissions                                                        | RbacServiceTest, RbacTest, RbacAssignmentPersistenceTest, AdminRbacIntegrationTest, AuthControllerWebTest                                                                                  | Automated |
| RF-SEC-10   | The system must identify the user only from their session and must not accept identities indicated by the…  | HomeCallerIdentityIntegrationTest, HomeExceptionHandlerTest                                                                                                                                | Automated |
| RF-SEC-11   | The system must allow the user to delete their account by confirming their password or, with Google, a code | DeleteAccountServiceTest, RemoveDeletedAccountServiceTest, AccountDeletionIntegrationTest, AccountDeleteError.test.jsx, Account.test.jsx, account-deletion-flow.sh | Automated |
| RF-SEC-12   | The system must apply a password policy (minimum length of 8 and maximum of 64 characters) and… | PasswordPolicySeedTest, ConfigurationPolicyPersistenceTest, LogPersistenceTest, AuditLogTest, SecuritySupportModelTest, RegisterUserServiceTest | Automated |
| RF-SEC-13   | The system must not allow access to an account that has not accepted the terms and conditions… | RegisterUserServiceTest, SignInWithGoogleServiceTest, AuthControllerWebTest, SecurityLoginFlowTest, UserPersonPersistenceTest, registerSchema.test.ts, registerPayload.test.ts, termsConsent.test.ts (mobile) | Automated |

**Table 17**

*Traceability of the RF-HOM requirements*

| Requirement | Description                                                                                             | Automated tests                                                                                                                      | Coverage    |
|-------------|---------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|-------------|
| RF-HOM-01   | The system must allow creating a home by choosing a home type from a catalog                            | CreateHomeServiceTest, HomeControllerTest, HomeTest, HomePersistenceTest, HomeTypeSeedTest                                           | Automated   |
| RF-HOM-02   | The system must show the user the homes they belong to                                                  | ListHomesServiceTest, HomeControllerTest, DashboardPage.test.jsx                                                                     | Automated   |
| RF-HOM-03   | The system must allow joining a home with its access code, with the member role                         | JoinHomeServiceTest, HomeControllerTest                                                                                              | Automated   |
| RF-HOM-04   | The system must show the members of a home                                                              | ListMembersServiceTest, HomeControllerTest, Users.test.jsx                                                                           | Automated   |
| RF-HOM-05   | The system must distinguish users' general permissions from the owner and member roles within each home | UserHomeTest, OwnerPermissionsTest, MemberPermissionsTest, RoleTest, UserHomePersistenceTest, HomeOwnershipTransferIntegrationTest   | Automated   |
| RF-HOM-06   | The owner must be able to remove a member and any member must be able to leave the home                 | RemoveUserServiceTest, LeaveHomeServiceTest, HomeControllerTest, Users.test.jsx                                                      | Automated   |
| RF-HOM-07   | The user must be able to mark a home as a favorite                                                      | ToggleFavoriteServiceTest, HomeControllerTest                                                                                        | Automated   |
| RF-HOM-08   | The system must allow viewing, updating and resetting to system values the thresholds of a home         | GetHomeThresholdsServiceTest, UpdateHomeThresholdsServiceTest, HomeThresholdsTest, HomeThresholdsPersistenceTest, HomeControllerTest | Automated   |
| RF-HOM-09   | The owner must be able to delete the home by soft delete                                                | DeleteHomeServiceTest, HomeDeletionIntegrationTest, HomeControllerTest, DeleteHome.test.jsx                                          | Automated   |
| RF-HOM-10   | The owner must be able to transfer home ownership to another member                                     | TransferHomeOwnershipServiceTest, HomeOwnershipTransferIntegrationTest, HomeControllerTest, Users.test.jsx                           | Automated   |
| RF-HOM-11   | Pending: limit a device's history to the linking period                                                 | —                                                                                                                                    | No coverage |
| RF-HOM-12   | The system must show the members of a home the profile photo of each one and allow enlarging it | UserProfileQueryServiceTest, HttpUserDirectoryAdapterTest, HomeControllerTest, Users.test.jsx | Automated |

**Table 18**

*Traceability of the RF-DEV requirements*

| Requirement | Description                                                                                                 | Automated tests                                                                              | Coverage    |
|-------------|-------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|-------------|
| RF-DEV-01   | The system must provide a catalog of appliance types                                                        | —                                                                                            | No coverage |
| RF-DEV-02   | The owner must be able to link a measuring device to their home                                             | HomeDeviceServiceTest, HomeDeviceControllerTest, DeviceApiKeyStorageIntegrationTest          | Automated   |
| RF-DEV-03   | Members must be able to see the devices of a home                                                           | HomeDeviceServiceTest, HomeDeviceControllerTest                                              | Automated   |
| RF-DEV-04   | The owner must be able to edit the appliance, name and room of a device without intervening on the…         | HomeDeviceServiceTest, HomeDeviceControllerTest                                              | Automated   |
| RF-DEV-05   | The owner must be able to change the Wi-Fi network of an already linked device                              | HomeDeviceServiceTest, HomeDeviceControllerTest                                              | Automated   |
| RF-DEV-06   | The owner must be able to unlink a device                                                                   | HomeDeviceServiceTest, HomeDeviceControllerTest, HomeDeletionIntegrationTest                 | Automated   |
| RF-DEV-07   | The system must only accept data from linked devices, and each device can only send its own data            | BrokerAccessServiceTest, BrokerAuthControllerTest, ApiKeyHashTest, TelemetryMqttConsumerTest | Automated   |
| RF-DEV-08   | Linking must be possible from the web application over Bluetooth: the application shows the Wi-Fi networks… | —                                                                                            | No coverage |
| RF-DEV-09   | The device must reconnect without user intervention after a Wi-Fi loss or a temporary system failure, keep… | —                                                                                            | No coverage |

**Table 19**

*Traceability of the RF-MED requirements*

| Requirement | Description                                                                                                 | Automated tests                                                                                                              | Coverage    |
|-------------|-------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|-------------|
| RF-MED-01   | The device must send every 60 seconds the voltage (V), current (A), active power (W) and accumulated…       | —                                                                                                                            | No coverage |
| RF-MED-02   | The system must store each reading with the date and time it was taken                                      | RegisterMeasurementServiceTest, MeasurementTest, MeasurementPersistenceTest, TelemetryMqttConsumerTest                       | Automated   |
| RF-MED-03   | The system must drop messages that claim to belong to a device other than the one sending them              | TelemetryMqttConsumerTest                                                                                                    | Automated   |
| RF-MED-04   | The device must report its status every five minutes and on connect: whether it is online or offline, its…  | —                                                                                                                            | No coverage |
| RF-MED-05   | The system must keep each device's state changes                                                            | DeviceConnectivityServiceTest                                                                                                | Automated   |
| RF-MED-06   | The system must mark a device as offline when the device announces it is disconnecting or when 150 seconds… | DeviceConnectivityServiceTest                                                                                                | Automated   |
| RF-MED-07   | The system must allow querying measurements, the latest measurement and consumption statistics              | ListMeasurementsServiceTest, GetLatestMeasurementServiceTest, GetConsumptionStatisticsServiceTest, MeasurementControllerTest | Automated   |
| RF-MED-08   | The system must provide the consumption summary and history of a home, with date range selection            | HomeConsumptionUseCaseTest                                                                                                   | Automated   |
| RF-MED-09   | The system must classify power into the levels LOW (0 to 500 W), MEDIUM (500 to 1500 W), HIGH (1500 to…     | ConsumptionLevelTest, ListConsumptionLevelsServiceTest, ConsumptionLevelControllerTest, ConsumptionLevelPersistenceTest      | Automated   |

**Table 20**

*Traceability of the RF-ALE requirements*

| Requirement | Description                                                                                                   | Automated tests                                                                                                | Coverage  |
|-------------|---------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|-----------|
| RF-ALE-01   | The system must raise a device linked notice with the first reading a device sends after being linked,…       | RegisterDeviceLinkedAlertServiceTest                                                                           | Automated |
| RF-ALE-02   | The system must raise a disconnection alert when a device announces it is disconnecting or 150 seconds…       | DeviceConnectivityServiceTest                                                                                  | Automated |
| RF-ALE-03   | The system must raise a high or critical consumption alert when a reading falls in the HIGH or CRITICAL level | RegisterThresholdAlertServiceTest, AlertTest, MeasurementRecordedListenerTest                                  | Automated |
| RF-ALE-04   | The system must raise a daily or monthly limit alert when the home's energy since the start of the day or…    | RegisterLimitAlertServiceTest, AlertTest                                                                       | Automated |
| RF-ALE-05   | The system must allow listing alerts, viewing the detail of one, marking it as read and deleting it           | ListAlertsServiceTest, GetAlertServiceTest, ResolveAlertServiceTest, AlertControllerTest, AlertPersistenceTest | Automated |
| RF-ALE-06   | The system must raise consumption alerts from each new reading and disconnection alerts from the loss of…     | MeasurementRecordedListenerTest                                                                                | Automated |
| RF-ALE-07   | The system must raise an unusual consumption alert when a reading is at least 200 W and 2.5 times the usual power of the device… | RegisterAnomalyAlertServiceTest, MeasurementRecordedConsumerTest, AlertMessagesAnomalyTest | Automated |

**Table 21**

*Traceability of the RF-REC requirements*

| Requirement | Description                                                                                                 | Automated tests                                                                             | Coverage  |
|-------------|-------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|-----------|
| RF-REC-01   | The system must evaluate every hour the hourly energy of homes whose devices reported in the last week,…    | EvaluateRecommendationsServiceTest, RecommendationRulesTest                                 | Automated |
| RF-REC-02   | Peak hours rule: at least 40% of the last 7 days' energy (minimum 2 kWh) was used between 18:00 and 22:00   | RecommendationRulesTest                                                                     | Automated |
| RF-REC-03   | Standby consumption rule: a device that is not a refrigerator drew an average of 15 W or more between…      | RecommendationRulesTest                                                                     | Automated |
| RF-REC-04   | Weekly comparison rule: the last 7 days are 25% or more above the previous 7 (minimum 1 kWh)                | RecommendationRulesTest                                                                     | Automated |
| RF-REC-05   | Limit projection rule: at the current pace, the home will exceed its daily or monthly limit, whichever the… | RecommendationRulesTest                                                                     | Automated |
| RF-REC-06   | Device increase rule: a device's mean power in the last 7 days is 30% or more above that of the previous…   | RecommendationRulesTest                                                                     | Automated |
| RF-REC-07   | The same recommendation (home, type and device) must not repeat within 7 days, even if it was deleted       | EvaluateRecommendationsServiceTest, RecommendationRulesTest                                 | Automated |
| RF-REC-08   | The system must allow listing a home's recommendations, marking them as read and deleting the read ones,…   | RecommendationInboxServiceTest, RecommendationControllerTest, RecommendationPersistenceTest | Automated |
| RF-REC-09   | The rules need history: peak hours and standby consumption work after a few days of readings, the weekly…   | RecommendationRulesTest                                                                     | Automated |

**Table 22**

*Traceability of the RF-NOT requirements*

| Requirement | Description                                                                                        | Automated tests                                                                              | Coverage    |
|-------------|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|-------------|
| RF-NOT-01   | The system must deliver every alert to all members of the home by email and browser notification,… | NotifyAlertTest, NotificationTest, WebPushSenderTest, EmailSenderTest, ExpoPushSenderTest, PushDeviceTest | Automated   |
| RF-NOT-02   | The system must deliver every new recommendation through the same channels                         | NotifyAlertTest                                                                              | Automated   |
| RF-NOT-03   | The user must be able to view and change their notification preferences                            | —                                                                                            | No coverage |
| RF-NOT-04   | The user must be able to turn browser notifications on and off                                     | WebPushSenderTest                                                                            | Automated   |
| RF-NOT-05   | Every delivery attempt must be recorded as sent or failed, without storing the message content     | NotificationTest, NotificationPersistenceTest, AbandonedDeliverySweeperTest, EmailSenderTest | Automated   |
| RF-NOT-06   | The system must send the verification code and the password recovery code by email                 | SendPasswordResetEmailTest, EmailPasswordResetDeliveryAdapterTest, EmailSenderTest           | Automated   |
| RF-NOT-07   | Every alert email must include a link to the web application                                       | NotifyAlertTest                                                                              | Automated   |

**Table 23**

*Traceability of the RF-CLI requirements*

| Requirement | Description                                                                                                | Automated tests                                                                                                     | Coverage    |
|-------------|------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|-------------|
| RF-CLI-01   | The web application must offer registration, sign-in (including Google), password recovery, account,…      | Account.test.jsx, DashboardPage.test.jsx, DeleteHome.test.jsx, Users.test.jsx, verify.test.jsx, recover.test.jsx, … | Automated   |
| RF-CLI-02   | The web application must show a badge with pending alerts, a counter in the tab title and a pop-up notice… | —                                                                                                                   | No coverage |
| RF-CLI-03   | The web application must link devices over Bluetooth and allow browser notifications, which arrive even…   | —                                                                                                                   | No coverage |
| RF-CLI-04   | The mobile application must be available for Android only and allow access to the account, homes, alerts,… | —                                                                                                                   | No coverage |
| RF-CLI-05   | Both applications must allow choosing the language among Spanish, English, French and Portuguese | locales.test.ts (mobile) | Automated |
| RF-CLI-06   | The applications must allow choosing a color palette with dark mode                                        | —                                                                                                                   | No coverage |
| RF-CLI-07   | The web application must offer help in Settings in modals: documentation by role, support only for users and news | HelpCenter.test.jsx, DocsModal.test.jsx, NewsModal.test.jsx, Support.test.jsx | Automated |
| RF-CLI-08   | The web application must allow viewing the profile photo enlarged inside the page itself | PhotoViewer.test.jsx, Account.test.jsx | Automated |

**Table 24**

*Traceability of the RNF-SEG requirements*

| Requirement | Description                                                                                                | Automated tests                                                                                                         | Coverage    |
|-------------|------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|-------------|
| RNF-SEG-01  | Each access granted to a user must last 15 minutes and renew automatically while the session remains valid | JwtTokenIssuerTest, RefreshSessionServiceTest                                                                           | Automated   |
| RNF-SEG-02  | If an attempt is made to reuse a renewal credential that was already used, the system must invalidate the… | RefreshTokenTest, RefreshSessionServiceTest, RefreshLifecycleIntegrationTest                                            | Automated   |
| RNF-SEG-03  | The system must only serve requests from authorized web applications                                       | SecurityCorsConfigurationTest, SecurityCorsWebTest                                                                      | Automated   |
| RNF-SEG-04  | The data that measuring devices send must travel encrypted                                                 | —                                                                                                                       | No coverage |
| RNF-SEG-05  | Recovery codes must be stored in a protected way, so that a data leak does not allow them to be used       | PepperedHmacResetCodeHasherTest                                                                                         | Automated   |
| RNF-SEG-06  | Redeeming recovery codes must be limited to 10 attempts per address per 15-minute window, and tell the…    | InMemoryResetAttemptLimiterTest, ResetAttemptLimiterBeforeDatabaseTest, PoolSaturatedLimiterTest, AuthControllerWebTest | Automated   |
| RNF-SEG-07  | The internal functions used to authenticate devices must only be reachable from the system's own network…  | —                                                                                                                       | No coverage |
| RNF-SEG-08  | The system must protect user information in accordance with Law 1581 of 2012 (Congreso de la República de… | —                                                                                                                       | No coverage |
| RNF-SEG-09  | Device credentials must be checked securely                                                                | ApiKeyHashTest, DeviceApiKeyStorageIntegrationTest, BrokerAccessServiceTest                                             | Automated   |
| RNF-SEG-10  | The system's passwords and keys must not be exposed in the project's shared files                          | —                                                                                                                       | No coverage |

**Table 25**

*Traceability of the RNF-REN requirements*

| Requirement | Description                                                                                    | Automated tests | Coverage    |
|-------------|------------------------------------------------------------------------------------------------|-----------------|-------------|
| RNF-REN-01  | Data queries and charts must respond in under 2 seconds                                        | —               | No coverage |
| RNF-REN-02  | The system must process each device's information at a cadence of one reading every 60 seconds | —               | No coverage |
| RNF-REN-03  | Sending emails must not keep the user waiting more than 5 seconds                              | —               | No coverage |

**Table 26**

*Traceability of the RNF-DIS requirements*

| Requirement | Description                                                                                                 | Automated tests               | Coverage    |
|-------------|-------------------------------------------------------------------------------------------------------------|-------------------------------|-------------|
| RNF-DIS-01  | No target availability percentage is set for this version                                                            | —                             | No coverage |
| RNF-DIS-02  | The system must tolerate the occasional loss of readings                                                    | DeviceConnectivityServiceTest | Automated   |
| RNF-DIS-03  | If the system is unavailable, the device must keep measuring and hold up to 30 samples (30 minutes)         | —                             | No coverage |
| RNF-DIS-04  | The service that receives the devices' data must retry its connection to the system every 10 seconds while… | —                             | No coverage |
| RNF-DIS-05  | If there is no Wi-Fi for 10 minutes, the device must restart                                                | —                             | No coverage |
| RNF-DIS-06  | The system must allow its health status to be checked                                                       | —                             | No coverage |

**Table 27**

*Traceability of the RNF-COM requirements*

| Requirement | Description                                                                                              | Automated tests | Coverage    |
|-------------|----------------------------------------------------------------------------------------------------------|-----------------|-------------|
| RNF-COM-01  | The web application must work in current browsers (Chrome, Firefox, Safari and Edge)                     | —               | No coverage |
| RNF-COM-02  | Linking over Bluetooth from the web is only available in desktop Chrome and Edge and Chrome on Android,… | —               | No coverage |
| RNF-COM-03  | The mobile application must run on Android devices                                                       | —               | No coverage |
| RNF-COM-04  | The measuring device must include a dedicated electrical measurement sensor, with one sensor per board,… | —               | No coverage |

**Table 28**

*Traceability of the RNF-MAN requirements*

| Requirement | Description                                                                                               | Automated tests                                                       | Coverage                           |
|-------------|-----------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------|------------------------------------|
| RNF-MAN-01  | The system must be organized in functional modules (User and Profile Management, Home Management, Device… | ModularityTests                                                       | Automated                          |
| RNF-MAN-02  | Every change to the data structure must be reversible                                                     | —                                                                     | No coverage                        |
| RNF-MAN-03  | Deleted data must be kept marked as deleted and not physically erased                                     | HomePersistenceTest, UserPersonPersistenceTest, DeleteHomeServiceTest | Automated                          |
| RNF-MAN-04  | The system must have automated tests that verify its main functions                                       | —                                                                     | No coverage                        |

**Table 29**

*Traceability of the RNF-I18 requirements*

| Requirement | Description                                                                         | Automated tests | Coverage    |
|-------------|-------------------------------------------------------------------------------------|-----------------|-------------|
| RNF-I18-01  | The web application must be available in Spanish, English, French and Portuguese    | —               | No coverage |
| RNF-I18-02  | The mobile application must be available in Spanish, English, French and Portuguese | locales.test.ts (mobile) | Automated |
| RNF-I18-03  | Alerts and recommendations must be shown in the language each user has chosen       | —               | No coverage |

**Table 30**

*Traceability of the RNF-ESC requirements*

| Requirement | Description                                                                                          | Automated tests | Coverage    |
|-------------|------------------------------------------------------------------------------------------------------|-----------------|-------------|
| RNF-ESC-01  | The system must be able to grow in number of users and devices without affecting the user experience | —               | No coverage |
| RNF-ESC-02  | No target numbers of concurrent users and devices are set for this version                             | —               | No coverage |

**Table 31**

*Traceability of the RNF-USA requirements*

| Requirement | Description                                                                                              | Automated tests | Coverage    |
|-------------|----------------------------------------------------------------------------------------------------------|-----------------|-------------|
| RNF-USA-01  | The interface must be clear, adaptable to different screen sizes and consistent, with immediate feedback | —               | No coverage |

**Table 32**

*Traceability of the RNF-INT requirements*

| Requirement | Description                                                                                                 | Automated tests             | Coverage    |
|-------------|-------------------------------------------------------------------------------------------------------------|-----------------------------|-------------|
| RNF-INT-01  | Measuring devices must communicate with the system through connections started from the home, so that they… | —                           | No coverage |
| RNF-INT-02  | The system must send emails through an email account configured for that purpose                            | EmailSenderTest             | Automated   |
| RNF-INT-03  | The system must allow access with Google accounts, requesting only the profile name, email and photo        | SignInWithGoogleServiceTest | Automated   |
| RNF-INT-04  | Browser notifications must only work on a secure connection and require the system to have that service…    | —                           | No coverage |
| RNF-INT-05  | Device configuration over Bluetooth must be available for up to 10 minutes, must not interrupt measuring…   | —                           | No coverage |

Requirements with no coverage are known gaps (see risks and limitations). Account administration has no requirement in the specification (DOC-01); the specification now covers it with the RF-ADM requirements.

## Results

### Full backend suite with MySQL (version 1.1)

On October 20, 2026 the full core service suite (`./mvnw test`, test profile, MySQL 8.4) finished with **1131 tests, 0 failures and 0 errors**. The required `Tests` check of GitHub Actions gives the same result on `main` (commit `90ae5ef`, 23 min 9 s), and it runs on every pull request to `dev` and `main`. The per-module distribution of the most recent local Surefire report (October 16, 2026: 132 classes, 1124 tests, 0 failures) is in [full-suite-summary.md](evidence/backend-full-suite-summary.md); 7 tests were added between that report and the final run.

### Suites per repository (version 1.2, 10/24/2026)

With the services architecture, each repository runs its own suite with `./mvnw test` (against MySQL 8.4 for those that need it) locally and in the `Tests` check of its pipeline. Results, all green:

- `energy-monitor-backend` (home, alert, recommendation, notification and intermediaries): 481 tests; `ModularityTests` green.
- `en-monitor-measurement-service`: 151 tests.
- `en-monitor-device-service`: 130 tests.
- `en-monitor-api-gateway`: 57 tests.
- `en-monitor-security-service`: 660 tests (run of October 22, 2026).

End-to-end tests on the full local stack (`energy-monitor-infra`, with the five services `healthy`):

- `e2e/device-flow.sh` (21/21): gateway routes, linking a module (the device service queries the backend), broker authentication against the device service with a correct and a wrong key, telemetry (`ONLINE` and the reading stored by the measurement service), `offline` status (`device.connectivity-lost` event and `CONNECTIVITY` alert in the backend) and home deletion (`home.deleted`, the module is unlinked and receives `unlink`).
- `e2e/measurement-flow.sh` (25/25): two readings published through the broker that the measurement service stores only once and announces through its *outbox* (`measurement.recorded`), a `THRESHOLD` alert raised and resolved in the backend, consumption, history, statistics and levels through the gateway, and the `energy-monitor-backend.measurement.recorded` queue and its `.dlq` empty.

No Surefire reports of version 1.2 are attached to the evidence.

### Backend without a database (version 1.0 run, 10/14/2026)

`./mvnw -o test` was run limited to the classes that do not boot a context with a database (88 existing classes) plus the 4 new classes. The totals per module come from target/surefire-reports and are detailed in [surefire-summary.md](evidence/backend-surefire-summary.md).

**Table 33**

*Backend results per module*

| Module         | Classes | Run | Passed | Failed | Skipped | Time (s) |
|----------------|---------|-----|--------|--------|---------|----------|
| alert          | 9       | 48  | 48     | 0      | 0       | 1.2      |
| device         | 6       | 48  | 48     | 0      | 0       | 2.0      |
| home           | 23      | 159 | 159    | 0      | 0       | 1.5      |
| measurement    | 11      | 67  | 67     | 0      | 0       | 0.8      |
| notification   | 8       | 60  | 60     | 0      | 0       | 1.4      |
| recommendation | 4       | 23  | 22     | 1      | 0       | 0.3      |
| security       | 31      | 430 | 429    | 1      | 0       | 18.8     |
| Total          | 92      | 835 | 833    | 2      | 0       | 26.0     |

37 classes that need MySQL (270 declared methods) were not run because the SPRING_DATASOURCE_PASSWORD variable was not defined in the execution environment. They are the persistence tests, the integration tests, the web tests with a full context, ModularityTests and the security tests with a context; their status is “To run (MySQL)”.

### Web application

`npm test` was run: 20 files and 141 tests, all passed ([vitest-summary.md](evidence/web-vitest-summary.md)). In version 1.1 (October 20, 2026, branch `main`, commit `2b41cb3`) `npx vitest run` repeated the result: 20 files and 141 tests passed. The `Tests and build` check of GitHub Actions runs these tests and the production build on every pull request.

### New tests

4 classes were written for critical requirements with no automated coverage at all. They follow the style of the existing tests (@WebMvcTest with @MockitoBean, or plain Mockito) and the AAA structure.

**Table 34**

*New tests and why they were written*

| Class                        | Tests | Passed | Failed | Reason                                                                                       | Requirements          |
|------------------------------|-------|--------|--------|----------------------------------------------------------------------------------------------|-----------------------|
| BrokerAuthControllerTest     | 12    | 12     | 0      | Device authentication and authorization at the broker (security); the controller had no test | RF-DEV-07, RNF-SEG-09 |
| HomeDeviceControllerTest     | 10    | 10     | 0      | Device linking and credentials; only the use case had a test                                 | RF-DEV-02 a RF-DEV-06 |
| RecommendationControllerTest | 7     | 6      | 1      | Recommendation inbox; the controller had no tests. In version 1.1, with DEF-02 closed, all 7 pass | RF-REC-08             |
| TelemetryMqttConsumerTest    | 10    | 10     | 0      | Reading ingestion and dropping of foreign messages; the MQTT consumer had no tests           | RF-MED-02, RF-MED-03  |

### Defects and observations

**Table 35**

*Defects and observations found*

| ID     | Severity | Item                                                           | Description                                                                                                                                                                                                                                                                                                                 | Status |
|--------|----------|----------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------|
| DEF-01 | Low      | SecuritySupportModelTest.refusesMissingNames                   | The test requires Person to reject a null last name, but the code made it optional on purpose (changeset security-023: a Google account may have no last name). The test is out of date; the code is correct. It was not changed. Version 1.1: the test was updated; it requires the first name and accepts a missing last name, and it passes. | Closed |
| DEF-02 | Medium   | RecommendationControllerTest.listRequiresHomeId (prueba nueva) | A missing required parameter (for example homeId in GET /api/v1/recommendations or /api/v1/alerts) answers 500 instead of 400: SecurityExceptionHandler declares a globally scoped @ExceptionHandler(Exception.class) that catches MissingServletRequestParameterException. The test was left failing; it was not adjusted. Version 1.1: `handleUnexpected` keeps the 4xx status of framework exceptions (`ErrorResponse`) and `TypeMismatchException` answers 400; the test passes. | Closed |
| DOC-01 | Low      | Documentación                                                  | Account administration (routes /api/v1/admin, permissions USER.READ, USER.MANAGE and ROLE.MANAGE, the /admin/users screen and administrator notifications) exists in the code but has no requirement in the requirements specification and is not described in the technical and administrator manuals. Version 1.1: the technical and administrator manuals now describe it; the specification now includes the requirement (RF-ADM). | Resolved |

### Version 1.3 (10/28/2026)

With the complete migration each repository runs its suite with `./mvnw test` (against MySQL 8.4 for those that need it) and the web with `npx vitest run`. Table 36 gathers the results, all green.

**Table 36**

*Suites per repository, version 1.3*

| Repository                       | Commit  | Tests   | Result    |
|----------------------------------|---------|---------|-----------|
| en-monitor-security-service      | f57c76b | 773     | 0 failures |
| en-monitor-home-service          | f424083 | 260     | 0 failures |
| en-monitor-notification-service  | 7b10398 | 234     | 0 failures |
| en-monitor-measurement-service   | 8a9b544 | 151     | 0 failures |
| en-monitor-device-service        | 06c1bdc | 129     | 0 failures |
| en-monitor-alert-service         | f050b56 | 119     | 0 failures |
| en-monitor-recommendation-service | aeeeec5 | 71     | 0 failures |
| en-monitor-api-gateway           | 0b2c164 | 69      | 0 failures |
| energy-monitor-web               | 698d4c9 | 228 (38 files) | 0 failures |

End-to-end tests on the complete local stack (`energy-monitor-infra`, with the eight services `healthy`). Table 37 details what each one verifies.

**Table 37**

*End-to-end tests of energy-monitor-infra*

| Script                         | Checks | What it covers |
|--------------------------------|--------|----------------|
| `e2e/device-flow.sh`           | 24     | Gateway routes, linking a module, broker authentication with a correct and an incorrect key, telemetry, `offline` status and `CONNECTIVITY` alert, and home deletion with the `unlink` command |
| `e2e/measurement-flow.sh`      | 28     | Readings stored once and announced by the *outbox*, `THRESHOLD` alert raised and resolved, consumption, history, statistics and levels through the gateway, and queues with no pending messages |
| `e2e/alert-flow.sh`            | 36     | Consumption and connectivity alerts end to end across the measurement, device, alert and notification services |
| `e2e/activity-flow.sh`         | 60     | Home and device activity through the single notification queue: welcome, members, thresholds, ownership and deletion, with its text per recipient |
| `e2e/account-deletion-flow.sh` | 12     | Account created with Google: it cannot change the password (409), asks for the code by email (security, *outbox*, notifications), a wrong code does not delete and the right one does |

The `e2e/recommendation-flow.sh` script also exists and was not run again in this version. The gateway limits (20 POST per minute per IP and path) mean that consecutive runs of the same script may answer 429.

#### Mobile Application

The mobile application had no automated tests. In version 1.3 a unit suite was added and run, together with two static checks, on `dev` (commit `1e4109b` plus the test files, October 28, 2026), and a manual protocol was defined:

- Unit tests with Jest and `jest-expo` (`npm test`): **8 suites and 78 tests, all passed** (October 28, 2026). They cover the password rules, the registration schema, the verification code, the registration body (trimmed and with `acceptTerms`), the terms prompt (accept, cancel and dismiss), the normalization of network errors and of the 429 with `Retry-After`, the mapping of each operation and status to its message (428, 409, 429, 503), and the parity of the texts in the four languages, including that every error key the API can produce exists in all of them.
- `npx tsc --noEmit`: 0 errors.
- `npx expo lint`: no errors (exit code 0).

Unit tests do not replace a test on a device: they do not walk through screens nor make the real API call.

Table 38 is the manual test protocol for an Android device. Its status is **to be run**: none has been performed on a device; the mobile application requirements that become «Automated» are only those the unit suite covers (RF-CLI-05 and RNF-I18-02). Before running them a new APK must be built, because the previous one does not send `acceptTerms` and registration would answer 428.

**Table 38**

*Manual test protocol of the mobile application*

| ID     | Test | Expected result | Requirements | Status |
|--------|------|-----------------|--------------|--------|
| MOV-01 | Register without ticking the terms and then ticking them | Unticked it is not sent; ticked the code is requested and the verification screen opens | RF-SEC-01, RF-SEC-13 | To be run |
| MOV-02 | Register with a misspelled email domain (`name@meil.com`) | The registration is rejected with a readable message | RF-SEC-01 | To be run |
| MOV-03 | Verify the email with the received code and sign in | The account is created on verification and the dashboard opens | RF-SEC-02, RF-SEC-03 | To be run |
| MOV-04 | Sign in and out; reopen the application | The session is kept on reopening and closed on exit | RF-SEC-03, RF-SEC-04 | To be run |
| MOV-05 | Sign in with Google for the first time without accepting the terms | The terms are requested and, once accepted, the account is created | RF-SEC-05, RF-SEC-13 | To be run |
| MOV-06 | Recover the password (email, code and new password) | It is possible to sign in with the new password | RF-SEC-06 | To be run |
| MOV-07 | Create a home, mark it as a favorite and change its thresholds | The home appears in the list and in favorites with the new thresholds | RF-HOM-01, RF-CLI-04 | To be run |
| MOV-08 | View the dashboard of a home with a meter sending data | Consumption and the last reading are shown | RF-MED-07, RF-MED-08, RF-CLI-04 | To be run |
| MOV-09 | Receive an alert and open the notification inbox; change the preferences | The alert is listed, marked as read and the preferences are saved | RF-ALE-04, RF-NOT-01, RF-CLI-04 | To be run |
| MOV-10 | Edit the first and last name separately; change the password | Each field is saved on its own; a Google account cannot change the password | RF-SEC-07, RF-SEC-08 | To be run |
| MOV-11 | Delete the account (with password; with Google, with the code from the email) | The account is deleted and the email can register again | RF-SEC-11 | To be run |
| MOV-12 | Change the language among the four available and turn on dark mode | Texts and colors change without restarting | RF-CLI-05, RF-CLI-06 | To be run |

### Version 1.4 (10/28/2026)

With the latest changes the totals of five repositories change. Table 39 shows which tests were added and the `main` commit.

**Table 39**

*Changes in the suites, version 1.4*

| Repository | Commit | Tests | What was added |
|---|---|---|---|
| en-monitor-security-service | de52c95 | 774 | The internal profile includes the avatar (`UserProfileQueryServiceTest`) |
| en-monitor-home-service | 6a4e396 | 260 | The members return `profileImage` (adapter and endpoint, no new tests: the existing ones were extended) |
| en-monitor-notification-service | ecdbc0a | 235 | Wording of the unusual consumption alert (`AlertMessagesAnomalyTest`) |
| en-monitor-alert-service | 0e65430 | 128 | Unusual consumption rule (`RegisterAnomalyAlertServiceTest`, 8) and its place in the consumer (1) |
| energy-monitor-web | 27ad9f0 | 253 (44 files) | Photo viewer, photos of the members, Settings help modals, homes list that closes on an outside click and push in Brave |
| energy-monitor-mobile | 8b5edc0 | 78 | No changes in the tests |

The tests that need MySQL of the alert and security services were not run on the work machine; the `Tests` check of each pipeline runs them.

### Evidence

The evidence is in [evidence](evidence/evidence-overview.md): Surefire summary, trimmed execution log and Vitest summary from version 1.0, the full suite summary and new Vitest run from version 1.1, and the status of version 1.2 (totals per repository and end-to-end tests). It contains no secrets.

## Risks, Limitations and Known Gaps

**Table 40**

*Risks and Limitations of the Tests*

| ID   | Risk or limitation                                                                                                                                                                                                                                                                                                                                               | Effect                                                            | Recommendation                                                 |
|------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------|----------------------------------------------------------------|
| R-01 | Non-hermetic tests: persistence and full context need real MySQL and SPRING_DATASOURCE_PASSWORD                                                                                                                                                                                                                                                                  | Mitigated in version 1.1: the full suite runs with MySQL locally and in CI; the tests are still not hermetic | Evaluate Testcontainers |
| R-02 | The mobile application has no automated tests | Mitigated in version 1.3 with 78 unit tests, `tsc`, `expo lint` and a manual protocol still to be run | Run the protocol (Table 38) and add component and screen tests |
| R-03 | MQTT with QoS 0                                                                                                                                                                                                                                                                                                                                                  | A reading may be lost; loss is not tested                         | Evaluate QoS 1 and add a loss test                             |
| R-04 | No over-the-air (OTA) update                                                                                                                                                                                                                                                                                                                                     | Firmware is updated only by USB                                   | Document the USB upload procedure                              |
| R-05 | Web Bluetooth limited to Chrome, Edge and Chrome for Android, and to HTTPS or localhost                                                                                                                                                                                                                                                                          | Not verified automatically, nor is the EnergyMonitor-Setup portal | Evaluate browser end-to-end tests                              |
| R-06 | The recovery attempt counter lives in memory                                                                                                                                                                                                                                                                                                                     | It resets with the service and is not shared between instances    | Shared limiter if scaled                                       |
| R-07 | No performance or accessibility tests                                                                                                                                                                                                                                                                                                                            | RNF-REN and RNF-USA-01 without coverage                           | Define the targets and measure them                            |
| R-08 | No coverage report (JaCoCo is not in pom.xml)                                                                                                                                                                                                                                                                                                                    | Coverage percentage unknown                                       | Add JaCoCo (not added without asking)                          |
| R-09 | Gaps with no automated test: StatusMqttConsumer, DeviceInactivityScheduler, HomeDeletedListener, DeviceCommandListener, MqttDeviceCommandAdapter, RecommendationScheduler, NotificationChannelsController, HomeConsumptionController, ApplianceTypeController, RegisterConnectivityAlertService, DeviceConnectivityLostListener, BrokerAuthSecurityConfiguration | No direct automated coverage                                      | Prioritize connectivity and disconnection alerts               |

| R-10 | The full backend suite takes about 23 minutes in GitHub Actions | Pull requests wait that long before they can be merged | Parallelize the suite or split out the slowest integration tests |

## Conclusions and Recommendations

The core service has a broad and fast suite: 835 tests run without a database in 26 seconds, with 2 failures that are not production code (DEF-01) or that point to an error-handling defect (DEF-02). The web passes its 141 tests. The 37 classes with MySQL remain to be run to close the verification. Recommendations: (1) define SPRING_DATASOURCE_PASSWORD and run the full `./mvnw clean test`; (2) fix DEF-02 with a MissingServletRequestParameterException handler; (3) update the DEF-01 test; (4) adopt Testcontainers to make the persistence tests hermetic; (5) add tests to the mobile application; (6) add JaCoCo and continuous integration; (7) write the administration requirements and their tests.

Version 1.1: the verification of the core service is closed with the full suite (1131 tests, 0 failures) against MySQL, locally and in GitHub Actions. Recommendations (1), (2) and (3) were carried out, and the continuous integration of (6) now exists with required checks on `dev` and `main`. Testcontainers (4), mobile application tests (5), JaCoCo (6) and the administration requirement in the specification (7) remain pending.

Version 1.2: with the services architecture, verification is split between the repositories and every suite is green (backend 481, measurements 151, devices 130, gateway 57 and security 660), with `ModularityTests` green and the two end-to-end tests of `energy-monitor-infra` passing (21/21 and 25/25) on the local stack. The same recommendations (4) to (7) remain pending, now in each Java service; JaCoCo is in none of the `pom.xml` files. Redoing the per-class inventory and the traceability per repository is pending.

Version 1.3: with the complete migration, the eight Java suites and the web are green (773, 260, 234, 151, 129, 119, 71, 69 and 228 tests) and the five end-to-end tests pass. The traceability incorporates the acceptance of terms (RF-SEC-13) and the rules for Google accounts. The biggest gap is still the mobile application: it has 78 unit tests, but none of components or screens, and its manual protocol has not been run. Recommendations: run that protocol on a device, add component tests to the mobile application (React Native Testing Library), redo the per-class inventory of the services and run `recommendation-flow.sh` again.

## References

Cortés Parra, J. E., & García Artunduaga, M. A. (2026). *EnergyMonitor software requirements specification* (version 2.0). SENA – CIES Huila Regional.

Institute of Electrical and Electronics Engineers. (2008). *IEEE standard for software and system test documentation* (IEEE Std 829-2008). https://doi.org/10.1109/IEEESTD.2008.4578383

ISO/IEC/IEEE. (2021). *Software and systems engineering — Software testing — Part 3: Test documentation* (ISO/IEC/IEEE 29119-3:2021). International Organization for Standardization.
