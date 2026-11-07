# EnergyMonitor Quality and Improvements Report

Juan Esteban Cortés Parra and Miguel Ángel García Artunduaga

Servicio Nacional de Aprendizaje (SENA)

Technologist in Software Analysis and Development (ADSO), group (ficha) 3145556

October 16, 2026 (updated October 28, 2026)

## Version Control

**Table 1**

*Document version control*

| Version | Date       | Description of change                                                                                      | Authors                                       |
|---------|------------|------------------------------------------------------------------------------------------------------------|-----------------------------------------------|
| 1.0     | 10/16/2026 | First version: clean code audit, technical debt registry and roadmap for version 2.0.                      | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.1     | 10/20/2026 | Debt follow-up: status of each item, new static scan, continuous integration with GitHub Actions, operations debt arising from the production deployment (DT-19 to DT-22) and updated roadmap. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.2     | 10/24/2026 | Services architecture in production: extraction of security, devices and measurements into microservices with an API gateway resolved (DT-23) and migration of the four remaining modules in progress (DT-24); web session in HttpOnly cookies (DT-04 resolved); pipelines and test counts per repository; updated scalability strategy. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.3     | 10/28/2026 | Complete migration to microservices (DT-24 resolved) and retirement of the core service; web and mobile ESLint scans with no findings (DT-16 partial); notification service pipeline, the only deployable that lacked one (DT-12); test counts per repository; unit tests, static verification and manual protocol of the mobile application (DT-13); new operations debt DT-25 (deployment over SSH as `root`). | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.4     | 10/28/2026 | New debt from the last day's work: web news in the language of the commits (DT-26) and fixed thresholds of the unusual consumption alert (DT-27); updated test counts. | Cortés Parra, J. E.; García Artunduaga, M. A. |

## Abstract

This document is the quality and improvements report of EnergyMonitor and is written with a technical audit and scalability approach. It presents the result of the static scans that the project does have configured —ESLint in the web application and `expo lint` in the mobile application— and states as a finding that the core service has neither static analysis nor a coverage report. Versions 1.0 to 1.2 recorded 5 findings in the web (3 errors and 2 warnings) and 12 warnings in the mobile application; in version 1.3 (October 28, 2026) both scans end with no findings. From those results, from the defects and risks recorded in the test plan and the technical proposal, and from the go-live described in the implementation and deployment manual, a registry of 27 technical debt items is built with their evidence, impact, severity, effort, priority and status. As of October 20, 2026, three items were resolved —among them continuous integration, which now runs the tests on every pull request and deploys to production with approval— and two were partially resolved. As of October 24, 2026 the web session in `localStorage` (DT-04) and the extraction into microservices of the security, device and measurement contexts behind an API gateway (DT-23) were also resolved, and the migration of the four remaining modules was completed on October 28, 2026 with the retirement of the core service (DT-24). That day the web and mobile ESLint scans also ended with no findings (DT-16, now lacking only lint in CI). On that registry the improvement plan and the roadmap of version 2.0 are defined, prioritizing the payment of the quality, verification and operations debt (coverage, hermetic tests, off-site backups and monitoring) before the advanced features (over-the-air updates and smart meters).

*Keywords:* software quality, clean code, static analysis, technical debt, continuous integration, scalability, ESLint, EnergyMonitor

## Introduction

### Purpose

To document the quality status of the EnergyMonitor code and plan its improvement before and after the delivery of October 28, 2026. The document brings together the clean code analysis, the technical debt management and the improvement plan and roadmap for version 2.0. The structure of the report is inspired by the ISO/IEC 25010 product quality model (maintainability, reliability, security and performance efficiency), but it does not apply a certified assessment.

### Scope

The audit covers the source code of the components that are versioned in the project repositories: the Java services (API gateway, security, device and measurement services and core service, with Java 21 and Spring Boot 4.1.1; the gateway, with Spring Boot 4.0.8), the web application (React 19 and Vite 8), the mobile application (React Native 0.86 and Expo SDK 57) and, partially, the device firmware, the infrastructure files and the continuous integration pipelines. Automated static analysis can only run on the clients, because they are the only ones that declare linting tools; the Java services are assessed through targeted manual review and through the debt already documented in the test plan (folder `07-pruebas`) and in the technical proposal (folder `04-propuesta-tecnica`). Production operations are assessed with the implementation and deployment manual (folder `09-implementacion`).

### Approach of the Document

The language is that of a technical audit oriented to scalability. For that reason it does not limit itself to listing defects: every finding is turned into a technical debt item with a proposed action and a status, and every action is ordered in a roadmap that separates the payment of debt (version 1.1) from functional growth (version 2.0). The scalability decisions are justified by what the system is today —an API gateway, three microservices and a core service that is still a modular monolith, all on a single server— and do not assume an architecture that the project has not adopted.

### Document References

The technical debt identifiers (DT-01 to DT-27) and improvement identifiers (MP-01 to MP-22) are specific to this document. The defects (DEF-01, DEF-02), the observations (DOC-01) and the risks (R-01 to R-09) come from the test plan and report; the technical risks (RT-01 to RT-09) and the documentary discrepancies (DD-01 to DD-03) come from the technical proposal; the requirements (RF-*, RNF-*) come from the requirements specification. The codes and the numbering are identical in the Spanish and English versions.

## Audit Methodology

The audit combined four sources: the execution of the available static analyzers, the targeted manual review of the highest-risk code, the consolidation of the debt already detected in the other project documents and the review of the pipelines and of production operations. Version 1.1 repeated the scans on October 20, 2026 and checked the status of each debt item in the code. Table 2 summarizes the sources and their use.

**Table 2**

*Audit sources and their use*

| Source | Type | Use in this report |
|---|---|---|
| `eslint .` (ESLint 9.39.4) in `frontend/energy-monitor-web` | Actual run | Clean code findings of the web application (Table 4) |
| `eslint .` / `expo lint` (`eslint-config-expo`) in `frontend/energy-monitor-mobile` | Actual run | Clean code findings of the mobile application (Table 5) |
| `pom.xml` of the gateway, the services and the core service | Inspection | Confirms the absence of static analysis and coverage (DT-11) |
| `.github/workflows/ci-cd.yml` pipelines of the gateway, the services, the backend, the web and the infrastructure | Inspection and actual run | Status of continuous integration (DT-12, DT-16, DT-21) |
| Test plan and report (`07-pruebas`) | Consolidation | Defects, risks and requirements without coverage (DT-01, DT-07, DT-14, DT-18) |
| Technical proposal (`04-propuesta-tecnica`) | Consolidation | Technical risks, evolutionary maintenance and discrepancies (DT-03 to DT-10, DT-17) |
| Implementation and deployment manual (`09-implementacion`) | Consolidation | Production operations debt (DT-19 to DT-22) |
| Manual code review | Inspection | Bad practices with `file:line` evidence (Table 6) and debt status (Table 7) |
| Repositories of the services and `energy-monitor-infra` | Inspection | Status of the extraction into microservices (DT-23, DT-24) |

*Methodological note:* the project does not have SonarQube or any other continuous analysis platform configured. No SonarQube metrics are reported because they do not exist; their absence is recorded as a finding (DT-11) and as a proposed improvement (MP-16).

## Clean Code Analysis

### Available Static Analysis Tools

Table 3 shows which static analysis and automated verification tools actually exist in each component and in what state they are.

**Table 3**

*Static analysis tools and their state by component*

| Component | Tool | Version | State | Note |
|---|---|---|---|---|
| Web application | ESLint (flat config) with `eslint-plugin-react-hooks` and `eslint-plugin-react-refresh` | 9.39.4 | Configured and runnable (`npm run lint`) | No findings since version 1.3 (Table 4 keeps the previous ones); not run in CI |
| Mobile application | ESLint with `eslint-config-expo` and type checking (`tsc --noEmit`) | via Expo (`expo lint`) | Configured and runnable | No findings since version 1.3 (Table 5 keeps the previous ones); no CI |
| Java services (gateway and seven services) | Checkstyle / SpotBugs / PMD / Sonar | — | Not configured | No plugin in any `pom.xml`; finding DT-11 |
| Java services | JaCoCo (coverage) | — | Not configured | No coverage report; finding DT-11 |
| Infrastructure | `shellcheck`, `bash -n`, `docker compose config`, `nginx -t` | — | Configured in CI | Validate scripts, Compose files and the Nginx template on every pull request |
| Gateway, seven services, web and infrastructure | GitHub Actions with required checks | — | Configured | Tests and build required to merge; no lint or coverage threshold |
| Java services | `ModularityTests` (Spring Modulith) | 2.1.1 | Was configured and green | Verified the boundaries of the modules while they lived in the core service; retired with it on October 28, 2026 |
| All | SonarQube / SonarCloud | — | Not configured | No continuous analysis or quality gate |

### Static Scan Results of the Web Application

`eslint .` was run on `frontend/energy-monitor-web`. The result of versions 1.0 to 1.2 was 5 findings: 3 errors and 2 warnings in 4 files. **In version 1.3 (October 28, 2026, `dev` commit `8457d50`) the scan ends with exit code 0 and no findings**: all 5 were fixed. Table 4 keeps the detail of the fixed findings as a record.

**Table 4**

*ESLint findings in the web application (versions 1.0 to 1.2)*

| File:line | Severity | Rule | Description | Proposed action |
|---|---|---|---|---|
| `src/components/layout/Navbar/NavList.jsx:5` | Error | `no-unused-vars` | The variable `setOpen` is declared and not used. | Remove or use the variable |
| `src/context/ThemeContext.jsx:3` | Error | `react-refresh/only-export-components` | The file exports constants in addition to components, which breaks fast refresh. | Move constants to a separate file |
| `src/context/ThemeContext.jsx:182` | Error | `react-refresh/only-export-components` | Same case in a second export. | Move the function/constant to a separate file |
| `src/features/DetailHome/Consumption/Consumption.jsx:114` | Warning | `react-hooks/exhaustive-deps` | The `useMemo` declares an unnecessary dependency (`i18n.language`). | Review the dependency list |
| `src/features/DetailHome/ConsumptionHistory/ComsumptionHistory.jsx:167` | Warning | `react-hooks/exhaustive-deps` | Same case in the second `useMemo`. | Review the dependency list |

### Static Scan Results of the Mobile Application

The Expo lint was run on `frontend/energy-monitor-mobile`. The result of versions 1.0 to 1.2 was 12 warnings in source code files, with no errors. **In version 1.3 (October 28, 2026, `dev` commit `1e4109b`) `npx expo lint` and `npx tsc --noEmit` end with exit code 0 and no findings**: the 12 warnings were fixed and the `import/no-unresolved` errors of version 1.1 do not appear with the dependencies installed with `npm ci`. Table 5 keeps the detail of the fixed findings as a record.



**Table 5**

*Lint findings in the mobile application (versions 1.0 to 1.2)*

| File:line | Severity | Rule | Description | Proposed action |
|---|---|---|---|---|
| `src/components/layout/Sidebar/Sidebar.tsx:23` | Warning | `@typescript-eslint/no-empty-object-type` | Empty interface that accepts any non-null value. | Type the interface or use `object`/`unknown` |
| `src/features/auth/screens/LoginScreen/LoginScreen.tsx:16` | Warning | `@typescript-eslint/no-empty-object-type` | Empty interface. | Type the interface |
| `src/features/auth/screens/NewPasswordScreen/NewPasswordScreen.tsx:12` | Warning | `@typescript-eslint/no-empty-object-type` | Empty interface. | Type the interface |
| `src/features/auth/screens/RecoverPasswordScreen/RecoverPasswordScreen.tsx:12` | Warning | `@typescript-eslint/no-empty-object-type` | Empty interface. | Type the interface |
| `src/features/auth/screens/RegisterScreen/RegisterScreen.tsx:12` | Warning | `@typescript-eslint/no-empty-object-type` | Empty interface. | Type the interface |
| `src/features/auth/screens/VerifyAccountScreen/VerifyAccountScreen.tsx:12` | Warning | `@typescript-eslint/no-empty-object-type` | Empty interface. | Type the interface |
| `src/features/auth/screens/VerifyRecoverPasswordScreen/VerifyRecoverPasswordScreen.tsx:12` | Warning | `@typescript-eslint/no-empty-object-type` | Empty interface. | Type the interface |
| `src/features/dashboard/screens/DashboardScreen/DashboardScreen.tsx:25` | Warning | `@typescript-eslint/no-empty-object-type` | Empty interface. | Type the interface |
| `src/features/auth/components/VerifyAccountForm/VerifyAccountForm.tsx:44` | Warning | `react-hooks/incompatible-library` | Use of a function incompatible with the hook rules. | Review the hook usage |
| `src/features/auth/components/VerifyRecoverPasswordForm/VerifyRecoverPasswordForm.tsx:45` | Warning | `react-hooks/incompatible-library` | Same case. | Review the hook usage |
| `src/components/OtpInput/OtpInput.tsx:23` | Warning | `@typescript-eslint/array-type` | Array type declared in a non-preferred form. | Use the `Type[]` form |
| `src/services/http/httpClient.ts:19` | Warning | `import/no-named-as-default-member` | `axios.create` is used as a member of the default import. | Import `create` by name or adjust the rule |

### Bad Practices Detected by Manual Review

Beyond the analyzers, the targeted review of the highest-risk points found the practices in Table 6. All of them have evidence in the code or in the test, proposal and deployment documents.

**Table 6**

*Bad practices and risks detected with evidence*

| ID | Component | Observed practice | Evidence | Impact |
|---|---|---|---|---|
| BP-A | Core service (security; now the security service) | A global handler catches `Exception.class`, so a request with a missing required parameter responded 500 instead of 400. **Fixed in version 1.1** (DT-01). | `SecurityExceptionHandler.java` (`handleUnexpected`); defect DEF-02 of `07-pruebas` | Error contract inconsistent for the client |
| BP-B | Web application | Session tokens were stored in `localStorage`. **Fixed in version 1.2** (DT-04): the session travels in HttpOnly cookies. | Risk RT-03 of `04-propuesta-tecnica`; `src/services/auth/session.js` | Exposure to an XSS attack |
| BP-C | Security service (formerly the security module of the core service) | The recovery attempt counter lives in memory and resets with the service. | Risk RT-05; test `InMemoryResetAttemptLimiterTest` | The limit is not shared between instances |
| BP-D | Core service | The persistence and integration tests are not hermetic: they need a real MySQL and the `SPRING_DATASOURCE_PASSWORD` variable. | Risk R-01 of `07-pruebas` | More effort and fragility when running the tests |
| BP-E | Firmware | Telemetry is published over MQTT at QoS 0. | Risk RT-02 of `04-propuesta-tecnica` | Possible loss of readings |
| BP-F | Infrastructure | MySQL backups are stored on the same server as the database. | `deploy/backup-mysql.sh`; section 8 of `09-implementacion` | A server failure loses the data and its backups at the same time |

### Observed Clean Code Strengths

The audit also recognizes decisions that favor maintainability and that should be preserved in version 2.0:

- **Hexagonal architecture per module and per service.** Each module and each service separates domain, application and adapters; inside the core service modules only communicate through their `api` packages, and the `ModularityTests` verifies those boundaries.
- **Explicit contracts.** Each service exposes DTOs and a REST API under `/api/v1`, with its own error handling; between services there is an internal API (`/internal/**`) and events with their own *exchanges* and *routing keys* on RabbitMQ.
- **Extraction without breaking clients.** The intermediary packages of the core service keep the contracts the modules already used and the gateway keeps the public routes, so the web and the mobile kept calling the same routes when the services were separated.
- **Centralized HTTP access in the client.** The web gathers the calls in `src/services/`, with an HTTP client and a map of errors to i18n keys.
- **Centralized internationalization.** The texts of the web and the mobile are managed with i18n instead of being embedded.
- **Documented development conventions.** Commits in Conventional Commits, one branch per task and review through pull request (`00-governance/git-conventions.md`).
- **No pending-work markers in the code.** The search for `TODO`, `FIXME`, `HACK` and `XXX` found no matches in the source code of the core service or the web application.
- **Continuous integration and deployment (version 1.1).** Every pull request of the backend, web and infrastructure runs its tests in GitHub Actions as a required check; since version 1.2, also those of the gateway and the security, device and measurement services. Changes on `main` are deployed with manual approval, a prior backup, a health check and automatic rollback.

## Technical Debt Management

### Technical Debt Registry

Table 7 is the technical debt registry of the project. Each item has its own identifier (DT), component, description, evidence, type, severity, effort, priority, proposed action and status. Effort is estimated qualitatively (low, medium, high) and priority results from the matrix in the following section. The severity and effort values are a team assessment and not an automatic measurement, so they remain subject to review. The status was checked in the code on October 20, 2026 and again on October 24, 2026: **Resolved**, **Partial** (progress made, work remains), **In progress** (the work is being done) or **Open**. Items DT-19 to DT-22 arose from the production deployment, and DT-23 and DT-24 from the architecture of the core service.

**Table 7**

*Technical debt registry*

| ID | Component | Description | Evidence | Type | Severity | Effort | Priority | Proposed action | Status |
|----|-----------|-------------|----------|------|----------|--------|----------|-----------------|--------|
| DT-01 | Backend · security | The global handler catches `Exception.class`, and a missing required parameter responds 500 instead of 400. | `SecurityExceptionHandler.java`; DEF-02 | Corrective | High | Low | P1 | Add a handler for `MissingServletRequestParameterException` | **Resolved**: framework errors keep their 4xx status and an invalid type answers 400 |
| DT-02 | Backend · measurement/home | The consumption history of a device is not limited to the period in which it was linked to the home. | RF-HOM-11 of `02-especificacion-requisitos`; Table 17 of `07-pruebas` | Pending feature | Medium | Medium | P2 | Filter the history by the linkage range | Open |
| DT-03 | Firmware · MQTT | Telemetry is published at QoS 0. | RT-02 | Design risk | Medium | Medium | P2 | Evaluate QoS 1 and message persistence | Open |
| DT-04 | Web · security | Session tokens are stored in `localStorage`. | RT-03 | Security risk | High | Medium | P1 | Migrate to `httpOnly` cookies or to memory plus renewal | **Resolved**: the security service delivers the tokens in HttpOnly cookies and the gateway reads the token from the cookie; the web only stores non-secret account data |
| DT-05 | Firmware | There is no over-the-air firmware update (OTA). | RT-04 | Pending feature | Low | High | P3 | Design the over-the-air update | Open |
| DT-06 | Security service | The recovery attempt limit lives in memory. | RT-05 | Security risk | Medium | Medium | P2 | Shared and persistent limiter | Open |
| DT-07 | Backend · tests | The persistence and integration tests need a real MySQL; 37 classes were not run. | R-01; results section of `07-pruebas` | Verification debt | Medium | Medium | P1 | Adopt Testcontainers | **Partial**: in CI each service runs its suite against a MySQL 8.4 service container (as of October 28, 2026: 774 tests for security, 260 for homes, 235 for notifications, 151 for measurements, 129 for devices, 128 for alerts, 71 for recommendations and 69 for the gateway); locally they are still not hermetic |
| DT-08 | Security service | The access token remains valid for up to 15 minutes after deleting the account. | RT-07 | Security risk | Low | Medium | P3 | Revocation list or shorter lifetime | Open |
| DT-09 | Backend · alert | Some edge cases of connectivity alerts require manual review. | RT-08 | Corrective | Low | Medium | P3 | Automatic resolution rules | Open |
| DT-10 | Web · device | Web Bluetooth has compatibility limitations and requires a secure context. | RT-09 | Assumed limitation | Low | Low | P3 | Document and reinforce the configuration portal | Open (in production HTTPS provides the secure context) |
| DT-11 | Java services · tooling | There is no static analysis (Checkstyle, SpotBugs, PMD, Sonar) or coverage (JaCoCo) in the `pom.xml`. | `pom.xml` of the gateway, the services and the backend; Table 4 of `07-pruebas` | Quality debt | Medium | Medium | P1 | Integrate JaCoCo, a static analyzer and SonarQube | Open (not in the new services either) |
| DT-12 | All · process | There is no continuous integration or mandatory checks, although the convention requires green CI before merging. | Absence of `.github/workflows`; `git-conventions.md` | Process debt | Medium | Medium | P1 | Create pipelines and complete branch protection | **Resolved**: pipelines with required checks on `dev` and `main` in the gateway, the seven services, the web and the infrastructure (the notification service's was added on October 28, 2026, the last deployable that lacked one); the mobile application has no pipeline (DT-13) |
| DT-13 | Mobile · tests | The mobile application has no automated tests and no pipeline. | R-02 | Verification debt | Medium | High | P2 | Component and service tests, and a pipeline of its own | **Partial** (October 28, 2026): 78 unit tests in 8 suites (`npm test`), `tsc` and `expo lint` with no errors and a protocol of 12 manual tests (`07-pruebas`, Table 38) still to be run; still no component tests or pipeline |
| DT-14 | Backend · security (now the security service) | The test `SecuritySupportModelTest.refusesMissingNames` became outdated with respect to the code. | DEF-01 | Corrective | Low | Low | P2 | Update the test | **Resolved**: the test requires only the first name and an absent last name is accepted, as in the code |
| DT-15 | Documentation · admin | The administration module exists in the code, but has no requirements or manuals. | DOC-01 | Documentary debt | Low | Medium | P2 | Document requirements, API and administration manuals | **Partial**: the administrator and technical manuals now describe it; the requirement in the specification is still missing |
| DT-16 | Web and Mobile · style | ESLint findings: 5 in the web (3 errors) and 12 in the mobile (0 errors). | Tables 4 and 5 | Corrective | Low | Low | P2 | Fix the findings and enable lint in CI | **Partial**: the findings were fixed and both scans end at zero (October 28, 2026); the pipelines do not run the lint yet |
| DT-17 | Documentation | Documentary discrepancies DD-01 to DD-03 between documents and code. | `04-propuesta-tecnica`, Table 14 | Documentary debt | Low | Low | P3 | Fix the web README and the general documents | Open (the web README still says recommendations are mocked) |
| DT-18 | All · verification | Forty requirements of the specification have no automated test coverage. | Traceability section of `07-pruebas` | Verification debt | Medium | High | P2 | Complete coverage by priority | Open |
| DT-19 | Infrastructure · backups | MySQL backups are stored on the same VPS as the database and their restore has not been tested. | `deploy/backup-mysql.sh`; BP-F | Operations risk | Medium | Low | P1 | Copy every backup off the server and schedule a restore test | Open |
| DT-20 | Infrastructure · monitoring | There is no availability monitoring or alerting in production: health is only checked during a deployment. | Section 8.1 of `09-implementacion` | Operations risk | Medium | Low | P1 | External monitor of `https://energy-monitor.tech` and the broker with email alerts | Open |
| DT-21 | CI · performance | The backend suite takes about 23 minutes in GitHub Actions, which delays every pull request. | Section 9.9 of `09-implementacion` | Process debt | Low | Medium | P3 | Parallelize the suite or split out the slowest integration tests | Open (measured with the 1131 tests of the single backend; not measured again after splitting the suites across the services) |
| DT-22 | Infrastructure · environments | There is no staging environment and images are built on the production server. | Section 9.10 of `09-implementacion` | Process debt | Low | Medium | P3 | Second stack for `dev` and images published to a registry | Open |
| DT-23 | Backend · architecture | The seven contexts lived in a single deployable: a change in security, devices or measurements forced rebuilding and deploying the whole core service, and none of them could be deployed or scaled separately. | `docs/Architecture.md` of `energy-monitor-backend`; `docker-compose.prod.yml` of `energy-monitor-infra` | Architecture debt | Medium | High | P2 | Extract the contexts into microservices behind an API gateway, with internal REST and events with an *outbox* | **Resolved**: security (`en-monitor-security-service`), devices (`en-monitor-device-service`) and measurements (`en-monitor-measurement-service`) are microservices in production since October 24, 2026, behind `en-monitor-api-gateway`, with no changes to the public API |
| DT-24 | Backend · architecture | Four contexts (`home`, `alert`, `recommendation` and `notification`) remained in the `energy-monitor-backend` modular monolith. | `docs/Architecture.md` of `energy-monitor-backend`; `docker-compose.prod.yml` of `energy-monitor-infra` | Architecture debt | Medium | High | P2 | Migrate them to microservices | **Resolved**: homes, notifications, alerts and recommendations are services in production since October 28, 2026 and `energy-monitor-backend` was retired; the migration was carried out by Miguel Ángel García Artunduaga |
| DT-25 | Infrastructure · access | Continuous deployment enters the server over SSH as the `root` user; the key is restricted in `authorized_keys` to `deploy.sh`, but the command runs as that user. | `authorized_keys` of the server; section 8.5 of `09-implementacion` | Operations risk | Low | Medium | P3 | Unprivileged user with access only to Docker and periodic rotation of the deployment key | Open |
| DT-26 | Web · news | The news are built from the titles of the commits, which are in English, and only those that follow the `feat`, `fix` or `perf` format appear. | `vite.config.js`; news modal | Product debt | Low | Low | P3 | Keep a curated text per version in the four languages | Open |
| DT-27 | Alerts · unusual consumption | The thresholds of the alert (2.5 times, 200 W, 24 hours and 5 W) are constants of the service: they cannot be adjusted per home or per appliance. | `RegisterAnomalyAlertService` | Product debt | Low | Medium | P3 | Move the thresholds to the home settings and measure the false positives with real data | Open |

### Prioritization Criteria

Priority combines the severity of the impact with the correction effort. Table 8 sets the rule: what affects security or the client contract and costs little is addressed first; what is costly and low impact is scheduled for the end.

**Table 8**

*Technical debt prioritization criteria*

| Priority | Rule | Meaning |
|---|---|---|
| P1 | High severity, or medium severity with low effort | Immediate correction; blocks version 1.1 |
| P2 | Medium severity with medium or high effort, or low severity with low effort | Short-term correction |
| P3 | Low severity with medium or high effort | Planned improvement; does not block |

### Priority and Effort Matrix

Table 9 places each debt item by severity and effort. The corner of high severity and low effort (P1) is the one that must be paid first. Struck-through items are already resolved.

**Table 9**

*Severity and effort matrix*

| Severity \ Effort | Low | Medium | High |
|---|---|---|---|
| High | ~~DT-01~~ | ~~DT-04~~ | — |
| Medium | DT-19, DT-20 | DT-03, DT-06, DT-07, DT-11, ~~DT-12~~ | DT-13, DT-18, ~~DT-23~~, ~~DT-24~~ |
| Low | DT-10, ~~DT-14~~, DT-16, DT-17, DT-26 | DT-02, DT-08, DT-09, DT-15, DT-21, DT-22, DT-25, DT-27 | DT-05 |

*Note:* in version 1.0, DT-04 appeared in the low-effort column although the registry assigns it medium effort. Version 1.1 places it according to the registry; its priority remains P1 because of its high severity.

## Improvement Plan and Roadmap

### Objectives of Version 2.0

Version 2.0 pursues four quality objectives, in this order: first, make quality verifiable (coverage, static analysis and continuous integration); second, close the recognized security and operations risks; third, grow in functionality without compromising stability; and fourth, prepare the system for more users and devices. Paying the P1 debt that is still open (DT-07, DT-11, DT-19 and DT-20) is a precondition for functional growth.

### Planned Improvements

Table 10 relates each improvement to the debt it resolves or the requirement it completes, the expected benefit, its priority and its status as of October 28, 2026.

**Table 10**

*Planned improvements*

| ID | Improvement | Associated debt or requirement | Benefit | Priority | Status |
|----|-------------|--------------------------------|---------|----------|--------|
| MP-01 | Correct handling of missing required parameters (400) | DT-01, DEF-02 | Consistent error contract | P1 | **Delivered** |
| MP-02 | Test coverage with JaCoCo and a minimum threshold | DT-11 | Measurable quality | P1 | Pending |
| MP-03 | Continuous integration with lint, tests and build | DT-12 | Early detection of failures | P1 | **Delivered** (tests and build; lint moves to MP-06) |
| MP-04 | Hermetic tests with Testcontainers | DT-07 | Reproducible tests without an external MySQL | P1 | Partial (MySQL service container in CI) |
| MP-05 | `httpOnly` cookies for the web session | DT-04 | Reduces the XSS risk | P1 | **Delivered** |
| MP-06 | Cleanup of the ESLint findings and lint in CI | DT-16 | Sustained clean code | P2 | Partial (findings fixed; lint in CI missing) |
| MP-07 | Component and service tests in the mobile app, with a pipeline | DT-13 | Verification of the mobile client | P2 | Partial (78 unit tests, `tsc`, `expo lint` and manual protocol; component tests and pipeline missing) |
| MP-08 | Shared and persistent attempt limiter | DT-06 | Effective limit between instances | P2 | Pending |
| MP-09 | Telemetry with QoS 1 and persistence | DT-03 | Fewer lost readings | P2 | Pending |
| MP-10 | Consumption history by linkage period | DT-02, RF-HOM-11 | Correct data per device | P2 | Pending |
| MP-11 | Recommendation notifications (email and push) | RF-NOT-02, RF-NOT-03 | Closes unimplemented requirements | P2 | **Delivered** (the notification module consumes `RecommendationCreated`) |
| MP-12 | Complete the coverage of the pending requirements | DT-18 | Greater confidence in the requirements | P2 | Pending |
| MP-13 | Administration interface and its requirements | DT-15, DOC-01 | Documented and usable administration | P2 | Partial (interface and manuals ready; requirement missing) |
| MP-14 | Over-the-air firmware update (OTA) | DT-05, RT-04 | Firmware updatable without a cable | P3 | Pending |
| MP-15 | Smart meters and new data sources | Evolutionary maintenance | Broader product scope | P3 | Pending |
| MP-16 | Continuous analysis with SonarQube | DT-11 | Sustained quality gate | P3 | Pending |
| MP-17 | Off-site backups and periodic restore test | DT-19 | Data survives a VPS failure | P1 | Pending |
| MP-18 | Availability monitoring with alerts | DT-20 | Outages detected without relying on a user | P1 | Pending |
| MP-19 | Staging environment and images in a registry | DT-22 | Test before production and faster deployments | P3 | Pending |
| MP-20 | Faster backend suite in CI | DT-21 | Pull requests reviewable in less time | P3 | Pending |
| MP-21 | Security, device and measurement microservices with an API gateway, internal REST and events on RabbitMQ with an *outbox* | DT-23 | Deployment and scaling per service without changing the public API | P2 | **Delivered** (October 24, 2026) |
| MP-22 | Migration of `home`, `alert`, `recommendation` and `notification` to microservices | DT-24 | Completes the separation of the core service | P2 | **Delivered** (October 28, 2026) |

### Optional Modules and Advanced Features

In addition to paying the debt, version 2.0 contemplates optional modules that are currently out of scope. Table 11 lists them with the scalability criterion that justifies them.

**Table 11**

*Optional modules of version 2.0*

| Module or feature | Description | Scalability justification |
|---|---|---|
| Expanded administration interface | On top of the current administration (users, statuses, roles and notices), add support, audit and content management from the web | Reduces manual work on the database |
| Over-the-air update (OTA) | Delivery of signed firmware from the server | Allows growth in the number of devices without physical visits |
| Smart meters | Integration with commercial meters in addition to the prototype | Expands the market without changing the core service |
| Advanced analytics and recommendations | Finer rules and comparisons by home type | Leverages the accumulated history |
| Enriched notifications | More channels and preferences per home and per member | Increases user retention |
| Expanded audit log | Traceability of administration actions | Common requirement when opening the system to more organizations |

### Scalability Strategy

The system runs today on a single server, a Hostinger KVM 2 VPS (2 vCPU and 8 GB of RAM): an API gateway and seven microservices (security, devices, measurements, homes, notifications, alerts and recommendations), with a single shared MySQL database. Table 12 orders the scalability measures from lowest to highest cost, to avoid over-dimensioning the solution.

**Table 12**

*Scalability strategy*

| Level | Measure | When to apply it | Expected effect |
|---|---|---|---|
| 1 | Vertical scaling of the VPS (from KVM 2 to KVM 4: 4 vCPU and 16 GB of RAM) | As users or devices increase | Increases capacity without changing the code or reinstalling |
| 2 | Redis for cache and for the attempt limiter | When the in-memory limiter is not enough | Shares state between instances and offloads the database |
| 3 | Indexes and time partitioning of the measurements table | As the reading history grows | Faster history queries |
| 4 | Asynchronous processing with the outbox pattern | If modules are split into services | Avoids losing events when decoupling. **Applied in version 1.2**: RabbitMQ 4.1 with a *transactional outbox* between the services |
| 5 | Telemetry with QoS 1 and persistence | Along with improvement MP-09 | Reduces loss of readings under load |
| 6 | Monitoring with Actuator and metrics | Before growth, along with MP-18 | Visibility to decide the scaling |

The rule is not to adopt a higher level while the lower one is not exhausted. The separation into microservices already allows deploying each service separately; to run several instances of the device or measurement services, each one needs its own `MQTT_CLIENT_ID`. The database is still a single shared one.

### Roadmap

Table 13 orders the work in two versions: 1.1, dedicated to paying the quality, security, verification and operations debt, and 2.0, dedicated to functional growth. MP-01, MP-03, MP-05, MP-11, MP-21 and MP-22 were already delivered in October 2026. The remaining dates are tentative.

**Table 13**

*Improvement roadmap*

| Version | Period | Deliverables | Debt it closes |
|---|---|---|---|
| 1.1 | Fourth quarter of 2026 | Delivered: MP-01 (error handling), MP-03 (continuous integration), MP-05 (secure cookies) MP-21 (security, device and measurement microservices) and MP-22 (migration of the four remaining modules). Pending: MP-02 (coverage), MP-04 (hermetic tests), MP-17 (off-site backups) and MP-18 (monitoring) | DT-01, DT-04, DT-12 and DT-23 (closed); DT-24 (in progress); DT-07, DT-11, DT-19 and DT-20 |
| 2.0 | 2027 | MP-06 to MP-10, MP-12 to MP-16, MP-19 and MP-20: lint in CI, mobile tests, shared limiter, QoS 1, history by linkage, requirement coverage, administration requirements, OTA, meters, continuous analysis, staging and faster CI. MP-11 was already delivered | DT-02, DT-03, DT-05, DT-06, DT-13, DT-15 to DT-18, DT-21, DT-22 and DT-25 |

## Conclusions

The project has an orderly code base —hexagonal architecture per module and per service, explicit contracts, centralized HTTP access and no pending-work markers— and broad test suites in every service and in the web. The analyzers that exist no longer report findings: they had 5 in the web and 12 in the mobile, almost all style or typing warnings, and they were fixed in version 1.3, which confirms that the code is not degraded.

Between versions 1.0 and 1.1 three debt items were closed. The error-contract defect (DT-01) was fixed and the outdated test (DT-14) was aligned with the code. In addition, continuous integration was put in place (DT-12): no branch is merged without passing its tests, and production is updated with approval, a prior backup, a health check and automatic rollback. With it, the 1131 tests of the core service run against MySQL on every pull request, which partially reduces the verification debt (DT-07).

In version 1.2 (October 24, 2026) two more items were closed. The web session moved to HttpOnly cookies (DT-04), and the security, device and measurement contexts were extracted into microservices behind an API gateway, with internal REST and events on RabbitMQ with a *transactional outbox*, without changing the public API (DT-23). Each service has its own pipeline and its suite: 481 tests for the backend, 151 for the measurement service, 130 for the device service and 57 for the gateway as of October 24, and 660 for the security service as of October 22, all green. The migration of the four modules that remain in the core service is in progress (DT-24).

In version 1.3 (October 28, 2026) the migration was completed: homes, notifications, alerts and recommendations are independent services in production and the core service was retired (DT-24). With it, the gateway and the seven services have their pipeline, which now includes the notification service's, and their eight suites add up to 1,817 green tests (774, 260, 235, 151, 129, 128, 71 and 69), besides the 253 of the web. The web and mobile ESLint scans end with no findings (DT-16), and the mobile application adds 78 unit tests, type checking and a manual test protocol (DT-13). The go-live left a new operations debt (DT-25): deployment enters over SSH as `root`, although with a key restricted to `deploy.sh`.

The remaining shortcomings are in quality governance and operations: there is no static analysis or coverage in any of the Java services, lint does not run in CI, the mobile application has no tests, and in production the backups live on the same server and there is no availability monitoring. The open P1 debt is concentrated in four items (DT-07, DT-11, DT-19 and DT-20), of limited correction and high impact. Addressing them first —in version 1.1— leaves the system in a position to grow toward version 2.0 without dragging fragility along. Scalability is solved in levels, starting with the vertical scaling of the current VPS; the separation into microservices already allows deploying the extracted services separately.

## References

Cortés Parra, J. E., & García Artunduaga, M. A. (2026). *EnergyMonitor software requirements specification* (version 2.0). SENA – CIES Regional Huila.

Cortés Parra, J. E., & García Artunduaga, M. A. (2026). *EnergyMonitor implementation and deployment manual*. SENA – CIES Regional Huila.

Cortés Parra, J. E., & García Artunduaga, M. A. (2026). *EnergyMonitor technical proposal*. SENA – CIES Regional Huila.

Cortés Parra, J. E., & García Artunduaga, M. A. (2026). *EnergyMonitor test plan and report*. SENA – CIES Regional Huila.

ESLint. (2025). *ESLint: Find and fix problems in your JavaScript code* [Documentation]. https://eslint.org/docs/latest/

GitHub. (2026). *GitHub Actions documentation* [Documentation]. https://docs.github.com/actions

International Organization for Standardization. (2023). *Systems and software engineering — Systems and software Quality Requirements and Evaluation (SQuaRE) — Product quality model* (ISO/IEC 25010:2023). https://www.iso.org/standard/78176.html

SonarSource. (2025). *SonarQube documentation* [Documentation]. https://docs.sonarsource.com/sonarqube/

## Open Decisions for the Next Versions

- Responsible person and date for each technical debt item (columns not included so as not to assume them).
- Minimum test coverage threshold (MP-02).
- Exact periods of versions 1.1 and 2.0 of the roadmap (Table 13).
- Decision on SonarQube: self-hosted instance or SonarCloud (MP-16).
- Destination of the off-site backups and frequency of the restore test (MP-17).
- Availability monitoring tool (MP-18).
- Budget and scope of the higher-cost scalability hierarchy (levels 3 to 5 of Table 12).
