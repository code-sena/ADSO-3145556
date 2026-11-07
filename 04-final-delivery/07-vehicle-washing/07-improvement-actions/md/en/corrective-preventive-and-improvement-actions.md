<!-- running-head: CORRECTIVE, PREVENTIVE, AND IMPROVEMENT ACTIONS: VEHICLE WASHING SYSTEM -->

# Documentation of Corrective, Preventive, and Improvement Actions for the Software

Vehicle Washing Management System (Vehicle Washing)

Management Procedure, Action Register, Root Cause Analysis, Follow-Up, and Lessons Learned

Brayan Estiven Patiño Cabrera (Project Leader), Juan Camilo Guilombo Osorio (Frontend Leader), and Merari Urbano Quintero (Backend Leader)

Technologist in Software Analysis and Development (ADSO), Servicio Nacional de Aprendizaje (SENA), Training Center: Industria

Ficha 3145556

Instructor: Karol Daniela Correa

November 4, 2026

Document Type: Register of Corrective, Preventive, and Improvement Actions (Development Team and Instructor) · Version 3.5

## Version Control

Table 1 records the change history of this document. The action register (sections 9 to 12) is a living document: each time an action is opened, updated, or closed, the row of the action is modified and, at least once per sprint, a row is added to this table.

**Table 1**

*Version History of the Actions Document*

| Version | Date | Description of the change | Responsible |
|---|---|---|---|
| 1.0 | 2026-10-09 | Creation of the document (in Spanish and English). The management procedure is defined, and 23 closed corrective actions, 13 open corrective actions, 14 preventive actions, and 13 improvement actions are recorded, verified against the code of the eleven repositories (*develop* branch as of October 9, 2026), the Git history, the risk register, and the technical backlog of the project. | Development team |
| 2.0 | 2026-10-16 | Review against the code of October 16, 2026. Eleven open corrective actions were closed (ACO-01 to ACO-03 and ACO-05 to ACO-12, now AC-24 to AC-34) and five improvement actions (AM-01, AM-04, AM-08, AM-10, and AM-11); five nonconformities found in the complete review of the system were recorded and closed (AC-35 to AC-39); the preventive actions, indicators, lessons learned, and traceability were updated. | Development team |
| 2.1 | 2026-10-17 | Review against the code of October 17, 2026. Corrective actions AC-40 to AC-43 were closed (expired session notice when logging out, screens overflowing on phones, squashed filter inputs in the app, and service progress before it started), preventive action AP-15 was added (responsive design check before merging), and improvements AM-07 (inspection report in the mobile app) and AM-08 (frequently asked questions on the home page) were closed. | Development team |
| 2.2 | 2026-10-19 | Preventive action AP-01 was closed with continuous integration in GitHub Actions in the 10 repositories. | Development team |
| 2.3 | 2026-10-19 | Test indicator with the run of October 19 (655 automated, 19 end-to-end, and 42 functional cases). | Development team |
| 2.4 | 2026-10-20 | Review against the code of October 20, 2026: the register is still current (AM-03, rescheduling from the web and the app, and AM-12, the pause and issue states, remain open); the backend leader's full name was completed. | Development team |
| 2.5 | 2026-10-21 | Review against the code of October 21, 2026. Corrective actions AC-44 to AC-52 were recorded and closed (a token that kept working after logout, bookings nobody closed, an operator assigned twice, receipts with no size limit, cash visible although it was disabled, reports counting unfinished bookings, misleading roles screen text, tabs out of step with the session, and a database transaction log with no limit); preventive action AP-04 (verified daily backups) was closed, corrective action ACO-14 (new variables missing from *.env.example* and the compose file) was opened, AP-16 to AP-18 (container logs, security headers, and health checks) and improvement AM-16 (mandatory APK update) were added and closed; the improvements of October 17 that repeated the identifiers AM-07 and AM-08 become AM-14 (inspection report in the app) and AM-15 (frequently asked questions); indicators with the tests of October 21. | Development team |
| 2.6 | 2026-10-21 | Review against the code of the evening of October 21, 2026. AC-45 was adjusted and corrective actions AC-53 (the first version of the automatic close could record as "No-show" a service that was actually done) and AC-54 (a `null` field in an event sent the message to notification-service's dead-letter queue) were recorded and closed; preventive action AP-19 and lesson L-16 were added; ACO-14 includes the operations-service *FOLLOW_UP_\** variables; indicators with the final run of October 21 (707 tests). | Development team |
| 2.7 | 2026-10-23 | Review against *develop* of October 23, 2026, version *v1.0.0*, and that day's system review. ACO-14 was closed (AC-55), as were the corrective actions Merari Urbano fixed that day in *develop* (AC-56 to AC-71, with their commit) and the outdated documentation (AC-72). ACO-15 to ACO-32 were opened with the findings that remain open and the caveats of that day's fixes (SEC-01, BIZ-01, BIZ-02, OPS-01, CD-01, among others); two are P1, so entry criterion E6 is no longer met. Table 10 was recalculated, improvements AM-17 (continuous deployment) and AM-18 (client tests) were added, and Annex D with the instructor's evaluation (H-01 to H-25) and its status. | Development team |
| 2.8 | 2026-10-24 | Review against *develop* of October 24, 2026 (128 branches merged in the 10 repositories, with the CI green) and that day's Azure deployment. ACO-15 to ACO-18, ACO-20 to ACO-23, ACO-27, ACO-29, ACO-31, and ACO-32 were closed (AC-73 to AC-84, each with its repository and commit), plus the protection that prevents downgrading the server (AC-85) and the web without *root* on port 8080 (AC-86). ACO-13, ACO-19, ACO-24 to ACO-26, and ACO-28 were updated (ACO-24 is left with the mobile app only, because payment-service has *v1.0.0* since October 24), and ACO-33 to ACO-40 were opened with the pending items of the final cross review. No P0 or P1 remains open, so criterion E6 is met again. Table 10 (86 of 102) and the test count (1,192) were recalculated, preventive action AP-11 was closed, lessons L-17 and L-18 were added, and Annex D was updated (9 fixed, 8 partial, 6 open, 1 accepted, and 1 that cannot be fixed). | Development team |
| 2.9 | 2026-10-24 | AC-87 closed: after the October 24 deployment, notification-service could not read *pending_delivery* (VARCHAR columns of migration 047) and sent no push notifications or emails; migration 048 turned them into NVARCHAR and was deployed that day at 17:12 UTC. Table 10 was recalculated (87 of 103) and lesson L-19 on integration tests against SQL Server (RNF-003) was added. | Development team |
| 3.0 | 2026-10-26 | Payment at booking. AC-88 was closed, a preventive and improvement action born from the question of the final presentation and the instructor's recommendation: mitigation of mass bookings with the payment mode at booking, cash with a limit, and the expiry of the digital payment. AC-89 was closed: in the live test an event was lost and the job cancelled a paid booking, and now booking asks payment before cancelling. ACO-41 was opened (tolerance for lateness with cash). Table 10 (89 of 106) and the test count were recalculated, and lesson L-20 and the new actions in Annex C were added. | Development team |
| 3.1 | 2026-10-26 | Versions *v1.1.0* and *v1.1.1*. ACO-19 was closed as AC-90: continuous deployment ran on its own for the first time on the night of October 26 and the mobile OTA was published after checking the native fingerprint. ACO-24 was left with only the protection of *develop*. Table 10 (90 of 106), AM-17, H-15, and Annex C were updated. | Development team |
| 3.2 | 2026-10-27 | Test count of October 27, 2026 (booking 137, customer-service 83: 1,311) and payment persistence tests in the CI (Table 10). | Development team |
| 3.3 | 2026-10-27 | Versions *v1.1.1* and *v1.1.2* of October 27. AC-91 (400 instead of 500 with an invalid list of services or opening-hours week), AC-92 (`bookedBy` visible to the customer), and AC-93 (alternative times the vehicle could not take) were closed. ACO-42 (example legal contact data), ACO-43 (bookings without pagination), and ACO-44 (payment history screen, RF-015) were opened. Progress of ACO-24 and ACO-26; H-01 and H-02 become Fixed, H-15 becomes Partial (the machine is still turned on on demand and the external monitor is missing), and H-21 becomes Partial (backgrounds in WebP). Table 10 (93 of 112, 1,337 tests), AM-18, L-19, and Annex C were recalculated. | Development team |
| 3.4 | 2026-10-27 | ACO-25: progress of October 27 in payment-service (passwords out of the code, no default credentials, and history review). It stays open until the passwords are rotated. | Development team |
| 3.5 | 2026-11-04 | Automated tests indicator with payment-service *v1.1.3*, merged into *main* on October 27 (275 in payment and 1,348 in total); ACO-25 with the status of *v1.1.3*; and H-23 with the *main* branch of *vehicle-w-docs*, in step with *docs*. | Development team |

*Note.* Dates are written in the ISO 8601 format (year-month-day). Prepared by the development team.

## Table of Contents

<!-- toc -->

- [Version Control](#version-control)
- [4. Introduction](#4-introduction)
  - [4.1 Reference Framework](#41-reference-framework)
  - [4.2 Sources of This Document](#42-sources-of-this-document)
- [5. Objective](#5-objective)
  - [5.1 General Objective](#51-general-objective)
  - [5.2 Specific Objectives](#52-specific-objectives)
- [6. Scope](#6-scope)
- [7. Definitions and Classification](#7-definitions-and-classification)
  - [7.1 Types of Action](#71-types-of-action)
  - [7.2 Severity and Priority](#72-severity-and-priority)
  - [7.3 States of an Action](#73-states-of-an-action)
- [8. Action Management Procedure](#8-action-management-procedure)
  - [8.1 Identification Sources](#81-identification-sources)
  - [8.2 Stages of the Procedure](#82-stages-of-the-procedure)
  - [8.3 Root Cause Analysis](#83-root-cause-analysis)
  - [8.4 Effectiveness Verification](#84-effectiveness-verification)
  - [8.5 Roles](#85-roles)
- [9. Register of Closed Corrective Actions](#9-register-of-closed-corrective-actions)
- [10. Register of Open Corrective Actions](#10-register-of-open-corrective-actions)
- [11. Register of Preventive Actions](#11-register-of-preventive-actions)
- [12. Register of Improvement Actions](#12-register-of-improvement-actions)
- [13. Follow-Up Indicators](#13-follow-up-indicators)
- [14. Lessons Learned](#14-lessons-learned)
- [15. Glossary](#15-glossary)
- [References](#references)
- [16. Annexes](#16-annexes)
  - [Annex A. Action Record Form](#annex-a-action-record-form)
  - [Annex B. Root Cause Analysis Example](#annex-b-root-cause-analysis-example)
  - [Annex C. Traceability Between Actions, Risks, and Technical Debt](#annex-c-traceability-between-actions-risks-and-technical-debt)
  - [Annex D. Instructor Evaluation](#annex-d-instructor-evaluation)

<!-- /toc -->

*Note.* Numbering starts at 4 because sections 1 to 3 are the cover, the version control, and the table of contents, which have no number in their title. It is kept this way so that the cross-references between documents by section number (for example, "Installation Manual, section 10") remain valid.

## 4. Introduction

The Vehicle Washing Management System (Vehicle Washing), internally called *LavaRapido*, is a platform for bookings, operator assignment, execution and rating of services, verification of payments by QR code, and loyalty points for a car wash. It consists of a gateway, six microservices (five in Java 21 with Spring Boot 4 and one in .NET 8), a SQL Server 2022 database, RabbitMQ, and two clients, web (Angular 21) and mobile (React Native with Expo) (Vehicle Washing Development Team, 2026a).

During its construction, its reviews with the instructor, and the writing of its manuals, defects, risks, and improvement opportunities have been detected. This document establishes **how** they are managed (identification, recording, root cause analysis, planning, execution, verification, and closure) and **records** the actions taken and pending, so that any team member, the instructor, or a future maintenance team knows what was corrected, why, with what evidence, and what remains to be done.

### 4.1 Reference Framework

- **ISO/IEC/IEEE 12207:2017.** The Maintenance process (6.4.13) includes correcting defects, preventing failures, and improving the software in operation, and the Quality Assurance process (6.3.8) requires treating the incidents and problems detected and verifying that the actions are effective (International Organization for Standardization [ISO] et al., 2017).
- **ISO/IEC/IEEE 14764:2022.** It elaborates the maintenance process of ISO/IEC/IEEE 12207 and defines the types of maintenance; this document uses the corrective (correct discovered defects), preventive (correct latent defects before they become failures), adaptive (adjust the software to changes in the environment), and perfective (improve the software) types (ISO et al., 2022).
- **ISO 9001:2015.** Clause 10.2 requires reacting to a nonconformity, determining its causes, implementing the necessary action, reviewing its effectiveness, and retaining documented information; clause 10.3 requires continual improvement. The standard replaced "preventive action" as a separate requirement with risk-based thinking (clause 6.1), so in this document preventive actions arise from the risk register (ISO, 2015).
- **PDCA cycle.** Each action goes through the plan, do, check, and act cycle, which is the basis of continual improvement in ISO 9001.

### 4.2 Sources of This Document

The actions were verified against the code of the eleven repositories (*develop* branch as of October 24, 2026, which is the one running in production since that day, and version *v1.0.0* of October 23) and their Git history, against the system review of October 23, 2026 (security, business, operations, deployment, web, mobile, and documentation), against the closing audit and the final cross review of October 24, 2026, and against the instructor's end-to-end evaluation of October 21, 2026 (Annex D), and against the risk register, the technical backlog, the incident management, the testing strategy, and the architecture decision records (ADRs) of the project (Vehicle Washing Development Team, 2026a). The findings of the review of the manuals of October 7, 9, and 16, 2026, and of the complete review of the system of October 16 (Annex B of the Technical Manual and the annexes of the other manuals) are also included.

## 5. Objective

### 5.1 General Objective

To document the procedure and the register of the corrective, preventive, and improvement actions of the Vehicle Washing Management System, to ensure that each defect, risk, or improvement opportunity is treated in a traceable and verifiable way, with learning for the team.

### 5.2 Specific Objectives

- Define the types of action, their classification by severity and priority, and their life cycle of states.
- Establish a single procedure to identify, record, analyze, execute, verify, and close actions.
- Record the corrective actions already performed with their root cause and their evidence.
- Record the pending corrective, preventive, and improvement actions with their person responsible, priority, and closure criterion.
- Define indicators to measure the effectiveness of the procedure and consolidate the lessons learned.

## 6. Scope

**It covers:** the six backend services, the gateway, the database, the Docker infrastructure, the web application, the mobile application, and the project documentation (manuals, ADRs, contracts, and data models), from construction to operation after implementation.

**It does not cover:** the management of new product requirements (user stories of the product backlog), except when an improvement need of the software already built is recorded as an improvement action; nor the academic management of the project (grades, program deliveries).

## 7. Definitions and Classification

### 7.1 Types of Action

**Table 2**

*Types of Action and Their Relationship With the Types of Maintenance*

| Type | Definition | Trigger | Type of maintenance (ISO/IEC/IEEE 14764) | Prefix |
|---|---|---|---|---|
| Corrective action | Eliminates the cause of a nonconformity that **has already occurred** (defect, failure to meet a requirement, an ADR, or the documentation) so that it does not recur. | Incident, failed test, review, remark of the instructor or of acceptance. | Corrective | AC (closed) and ACO (open) |
| Preventive action | Eliminates the cause of a **potential** nonconformity before it occurs. | Risk of the risk register, technical debt, trend analysis. | Preventive and adaptive | AP |
| Improvement action | Increases the value, quality, or efficiency of the software without an existing failure. | Retrospective, user suggestion, recommended evolution of the architecture. | Perfective and adaptive | AM |

*Note.* Prepared by the authors from ISO (2015) and ISO et al. (2022). The prefixes keep the Spanish initials so that both versions of the register share the same identifiers.

A **correction** (for example, restarting a service or editing a record) is not a corrective action: it resolves the symptom, but not the cause. In the project's incident management, the correction corresponds to the "mitigate" step; the corrective action is the change of code, configuration, or process that prevents the incident from recurring.

### 7.2 Severity and Priority

Severity uses the same scale as the project's incident management (Table 3). Priority combines severity with the probability of occurrence and the effort, and is expressed as High, Medium, or Low, as in the technical backlog.

**Table 3**

*Severity Scale*

| Level | Definition | Example | Response time |
|---|---|---|---|
| P0 | Nothing works. | SQL Server down; no service starts; login impossible. | Immediate. |
| P1 | A core flow is broken. | A booking, an assignment, the start of a service, or the report or approval of a payment is impossible. | The same day. |
| P2 | A secondary function fails and a workaround exists. | Emails do not arrive, but the internal inbox works. | Within the sprint. |
| P3 | Cosmetic or documentary. | Untranslated text, wrong icon, outdated manual. | Backlog. |

*Note.* Taken from the incident management of the project (Vehicle Washing Development Team, 2026a).

### 7.3 States of an Action

Each action goes through the states **Open** (recorded), **Under analysis** (the root cause is being sought), **Planned** (it has a person responsible, a solution, and a date), **In progress** (branch and pull request under way), **Under verification** (deployed; its effectiveness is being checked), and **Closed** (effective and with evidence). An action whose verification fails returns to *Under analysis*. An action that the team decides not to perform is marked **Accepted as a risk**, with the justification and the approval of the project leader.

## 8. Action Management Procedure

### 8.1 Identification Sources

- Incidents of the project's incident management and their post-mortems (P0 and P1).
- Failed automated tests (*./mvnw verify*, *dotnet test*, *ng build*) and manual end-to-end tests before each review.
- Code reviews (pull requests) and reviews by the instructor.
- Smoke and acceptance tests of the Implementation Procedure.
- Comparison between the code, the ADRs, and the manuals (discrepancy annexes).
- Risk register and technical backlog (source of preventive actions).
- Retrospectives of each sprint and user comments (source of improvement actions).

### 8.2 Stages of the Procedure

**Table 4**

*Stages of the Action Management Procedure*

| Stage (PDCA) | Activity | Responsible | Output |
|---|---|---|---|
| 1. Identify (Plan) | Detect the nonconformity, risk, or opportunity and report it in the team chat. | Whoever detects it | Notice with description and evidence. |
| 2. Record (Plan) | Create the row in the corresponding register (sections 9 to 12) with the form of Annex A; assign ID, type, severity, and person responsible. | Leader of the affected area | Action in the Open state. |
| 3. Contain (Do) | Only for P0 and P1 corrective actions: apply the immediate correction (restart, adjust *.env*, start the missing container). | Service owner | Flow restored. |
| 4. Analyze the cause (Plan) | Apply the five whys or the cause-and-effect diagram (section 8.3). | Service owner | Documented root cause. |
| 5. Plan (Plan) | Define the solution, the closure criterion, the effort, and the date; if a technical decision changes, create or modify an ADR. | Person responsible with the project leader | Action in the Planned state. |
| 6. Execute (Do) | *fix/\** or *feat/\** branch, change with tests that reproduce the defect, pull request reviewed by another person, and documentation updated in the same pull request (definition of done). | Person responsible | Pull request merged into *develop*. |
| 7. Verify effectiveness (Check) | Check the closure criterion in the implemented environment and that the problem does not reappear (section 8.4). | Person other than the executor | Verification evidence. |
| 8. Close and learn (Act) | Mark Closed, record the lesson learned (section 14), and update the risk register and the technical backlog. | Project leader | Action in the Closed state. |

*Note.* Prepared by the authors from clause 10.2 of ISO (2015) and the definition of done of the project.

### 8.3 Root Cause Analysis

For corrective actions of severity P0, P1, and P2, documenting the root cause is mandatory. Two techniques are used:

- **Five whys:** the question "why did it happen?" is asked about each answer until reaching a cause on which the team can act (process, design, configuration, or knowledge). Annex B contains a real example from the project.
- **Cause-and-effect (Ishikawa) diagram:** when there are several possible causes, they are classified into the categories *code*, *configuration*, *data*, *infrastructure*, *process*, and *people* before choosing the main cause.

The root cause is **never** recorded as "human error": if a person made a mistake, the question is what in the process, the tool, or the documentation allowed the mistake (the blameless approach of the project's post-mortems).

### 8.4 Effectiveness Verification

An action is closed only when its closure criterion is met and there is verifiable evidence: a commit or pull request, an automated test that reproduces the defect and now passes, the result of a manual test, a database query, or a screenshot. For P0 and P1 corrective actions, it is also required that the problem does not reappear in the next review or implementation. Verification is performed by a person other than the one who executed the action.

### 8.5 Roles

**Table 5**

*Roles in Action Management*

| Role | Person | Responsibility |
|---|---|---|
| Project leader | Brayan Estiven Patiño Cabrera | Approves priorities, accepts risks, verifies closure, and maintains this document. |
| Backend leader | Merari Urbano Quintero | Responsible for the actions of services, database, and infrastructure. |
| Frontend leader | Juan Camilo Guilombo Osorio | Responsible for the actions of the web and mobile applications. |
| Author of each pull request | Any team member | Updates the affected documentation in the same pull request. |
| Instructor | Karol Daniela Correa | Source of remarks; reviews the document at the deliveries. |

*Note.* Prepared by the authors. Each service also has an owner defined in the project's service catalog.

## 9. Register of Closed Corrective Actions

Table 6 records the nonconformities already corrected, with their root cause and the closure evidence. The "Origin" column refers to the closed risk (C-), the technical debt item (TD-), or the corresponding ADR.

**Table 6**

*Closed Corrective Actions*

| ID | Nonconformity | Sev. | Root cause | Action performed | Closure evidence | Origin | Closed |
|---|---|---|---|---|---|---|---|
| AC-01 | Booking times appeared shifted by five hours. | P1 | The JVM converted dates with the machine's time zone instead of the business's. | The booking-service JVM runs in UTC; dates are stored in UTC and converted only at the edges with *BOOKING_ZONE=America/Bogota*. | *AvailabilityPolicyTest* tests and correct bookings in the end-to-end test. | C-001 | 2026-10-04 |
| AC-02 | Services failed at startup after *docker compose up*. | P0 | The services tried to connect before SQL Server was healthy. | Health checks on *sqlserver* and *rabbitmq* and *service_healthy* and *service_completed_successfully* dependencies in the Compose file. | *docker-compose.yml* with conditional *depends_on*. | C-002 | 2026-10-04 |
| AC-03 | Two different implementations of the booking service existed. | P1 | Coding started without an agreed data model. | booking-service was rebuilt from the agreed SQL model; team rule: agree on the model before coding a service. | A single repository *lavarapido-booking-service* aligned with *06-data/models.md*. | C-003 | 2026-10-04 |
| AC-04 | With *--profile app*, every payment route failed. | P1 | payment-service had no Dockerfile or Compose service, and the gateway container did not receive *ROUTE_PAYMENT_URL*. | Multi-stage .NET 8 Dockerfile, *payment-service* service in Compose, and *ROUTE_PAYMENT_URL* in the gateway. | *lavarapido-infra* commit of 2026-10-08 ("wire payment-service to booking-service and rabbitmq"). | TD-014 | 2026-10-08 |
| AC-05 | *BOOKING_SERVICE_URL* was missing for payment-service in Compose; crediting points failed in containers. | P1 | The variable was omitted when the service was created (pre-existing gap found in ADR-015). | Variable added with *http://booking-service:3003/api/v1*. | Current *docker-compose.yml*. | ADR-015 | 2026-10-08 |
| AC-06 | The customer was not notified when a payment was approved, rejected, or refunded. | P2 | payment-service raised domain events but had no RabbitMQ publisher. | Publisher with *RabbitMQ.Client* and events *payment.confirmed*, *payment.rejected*, and *payment.refunded*, with an email to the customer. | ADR-011 section 9; *PAYMENT_\** types in notification-service (migration 028). | TD-015 | 2026-10-08 |
| AC-07 | The same transaction reference could pay two bookings. | P1 | The uniqueness of the reference was not validated when a payment was reported (control of ADR-008 and RF-016). | Validation in payment-service with the error code *TRANSACTION_REFERENCE_REUSED*. | *ErrorCodes.cs* of payment-service. | TD-016 | 2026-10-08 |
| AC-08 | payment-service only tested its domain; the role and conflict (409) rules were unprotected. | P2 | Application and API tests were postponed when the service was built. | Projects *PaymentService.Application.UnitTests* and *Infrastructure.IntegrationTests*. | *tests/* folder with command, query, promotion, and persistence tests. | TD-018 | 2026-10-08 |
| AC-09 | Points earned by a booking were not reversed when its payment was refunded, and two simultaneous movements could leave a wrong balance. | P1 | The points ledger had no reversal movement and no concurrency control. | *REVERSED* movement type and *sequence_no* column unique per customer (migration *pay-020*); the second concurrent movement answers 409. | Migration *020-loyalty-reversal-and-sequence.sql*. | ADR-015 | 2026-10-08 |
| AC-10 | Payment approval compared the booking total with itself and accepted invalid images. | P1 | The amount declared by the customer was neither sent nor compared. | The paid amount is sent and compared with the amount due in the review window, and images are validated as PNG or JPG. | payment-service and web commits of 2026-10-08. | Manuals v3.0 | 2026-10-08 |
| AC-11 | A "custom roles" catalog existed with no effect on authorization, contradicting ADR-010. | P2 | A feature was built without checking it against the recorded decisions. | *security.custom_role* was removed (migration 026); the three roles stay fixed and the permission matrix of *security.role_permission* is edited. | ADR-015 section 2; endpoint */api/v1/admin/roles-permissions*. | ADR-015 | 2026-10-08 |
| AC-12 | A fixed operator-per-bay assignment had been built, contrary to ADR-010. | P2 | Same as AC-11. | The *service_bay.operator_id* column was reverted (migration 024); the bay is chosen per booking and the operator per execution. | ADR-015 section 1. | ADR-015 | 2026-10-08 |
| AC-13 | Promotions showed a package price that was never applied to the charge. | P2 | The promotions model was used for display only. | Coupons with a real percentage discount, redeemable with points (*POST /api/v1/loyalty/redeem*), and a form without price, duration, or status. | ADR-015 sections 3 and 5. | ADR-015 | 2026-10-09 |
| AC-14 | When a service was edited, its loyalty points went back to 0. | P2 | booking-service binds the field to a primitive *int* and the clients omitted the field. | The web and the app always send *loyaltyPoints*; the app keeps the price of each vehicle type when editing. | ADR-015 section 5; mobile app commits of 2026-10-09. | ADR-015 | 2026-10-09 |
| AC-15 | "Pay" was offered on bookings already paid or under review. | P2 | Each screen repeated the payment rule instead of asking payment-service. | Endpoint *GET /api/v1/payments/me/bookings* with the *payable* flag; the web and the app show the button only with that flag. | ADR-015 section 5. | ADR-015 | 2026-10-09 |
| AC-16 | The notification switches were saved only in the browser or the phone and had no effect. | P2 | There were no columns for those preferences in the account. | Columns in *security.user_preference* (migration 030), partial update of preferences, and application of the switches in notification-service. | ADR-011 section 12. | ADR-011 | 2026-10-09 |
| AC-17 | Theme and language were stored per browser, and a second account on the same device inherited them. | P2 | Preferences were read from *localStorage* without querying the account. | Load after login and save with *GET/PUT /api/v1/users/me/preferences*; palettes accepted by security-service (migration 020). | *core/services/preferences.ts* of the web. | TD-002 | 2026-10-09 |
| AC-18 | The administrator could send any notification type by hand (for example, a fake "payment approved"). | P1 | The manual endpoint did not restrict the type. | Only *SYSTEM_MESSAGE* and *INSPECTION_REPORT* are accepted; any other type answers 400 *NOTIFICATION_TYPE_NOT_ALLOWED*. | ADR-011 section 11. | Security review | 2026-10-09 |
| AC-19 | Null or nonexistent identifiers were accepted when updating the permissions of a role. | P2 | Input validation was missing in the use case. | Rejection with 400 *UNKNOWN_PERMISSION* and a translated message in the web. | security-service commit of 2026-10-08. | ADR-015 | 2026-10-08 |
| AC-20 | The inspection report was shared by WhatsApp, outside the system's channels. | P3 | Earlier product decision. | "Notify customer" button that sends the summary by notification and email (*INSPECTION_REPORT*); WhatsApp was removed from the business data and the help center. | ADR-011 section 11; web commits of 2026-10-09. | ADR-011 | 2026-10-09 |
| AC-21 | The app opened in the browser with Expo (port 8081) was rejected by CORS. | P2 | Origin 8081 was not in the default values. | The default *CORS_ALLOWED_ORIGINS* includes *http://localhost:8081* in every service. | *lavarapido-infra* and gateway commits. | — | 2026-10-09 |
| AC-22 | The old operator tasks route showed an empty page. | P3 | Route without a component after a reorganization. | Redirect to "Assigned services" and removal of the empty page. | Web commit of 2026-10-09. | — | 2026-10-09 |
| AC-23 | Manuals v3.0 became outdated by the changes of October 8 and 9 (points, coupons, refunds, roles, preferences, payments in Docker), existed only in English, and the Installation Manual said to clone the operations repository with a name that breaks the build. | P3 | The documentation was not updated in the same pull requests (risk R-010). | Manuals updated to version 3.1 in Spanish and English, and creation of the Implementation Procedure and of this document. | Version control of each manual (2026-10-09). | R-010 | 2026-10-09 |
| AC-24 | The web test suite did not compile (formerly ACO-01). | P2 | Six scaffolding *.spec.ts* files pointed to classes that no longer existed. | The test files were fixed and tests for the API error messages were added. | *npx ng test --watch=false*: 43 tests in 37 files passed (2026-10-16). | TD-001 | 2026-10-16 |
| AC-25 | There was no login attempt limit (formerly ACO-02). | P1 | RF-004b had not been implemented. | *LoginLockout*: five failed attempts within 15 minutes lock the account for 15 minutes (423 ACCOUNT_LOCKED); a successful login or a password reset clears it. | Changelog 034, *AuthenticationServiceTest* and *ApiSecurityWebTest*; message shown on the web and mobile. | TD-003, RF-004b | 2026-10-16 |
| AC-26 | The acceptance of the Terms and of the Data Processing Policy was not stored on the server (formerly ACO-03). | P1 | The consent table was left pending in ADR-010. | *security.app_user* stores the date and time each document was accepted (*terms_accepted_at*, *data_policy_accepted_at*); web and mobile registration sends *acceptsTerms* and *acceptsDataPolicy*. | Changelog 039, 147 security-service tests passed, checkboxes in both clients. | Law 1581 of 2012 | 2026-10-16 |
| AC-27 | The payment-service health answered *UP* with the database down, and the service did not handle *X-Correlation-Id* (formerly ACO-05). | P2 | The endpoint returned a fixed value. | */health* and */actuator/health* query SQL Server with a 3-second limit; *CorrelationIdMiddleware* takes or creates the identifier and forwards it to booking-service. | With the database stopped the service answers 503 DOWN; 141 payment-service tests passed. | TD-017 | 2026-10-16 |
| AC-28 | The refund texts were fixed in Spanish (formerly ACO-06). | P3 | They were left in a temporary constants file. | Texts moved to the language files in Spanish, English, French, and Portuguese; *payment-texts.ts* was deleted. | Administrator payments screen in the four languages. | Administrator Manual | 2026-10-16 |
| AC-29 | The operator's "Report progress" button said it notified the customer but sent nothing (formerly ACO-07). | P3 | The function was left with only the visual confirmation. | The button was removed on the web and mobile; the customer receives the real start and finish notices. | Assigned services screen without the button. | User Manual | 2026-10-16 |
| AC-30 | The landing page announced a service "wherever you are", and the operator history could show payment methods the system does not support (formerly ACO-08). | P3 | Mockup texts. | The home page describes the service at the car wash with the real establishment data; the operator history no longer shows card or PSE. | Home page in the four languages. | Technical Manual, Annex B | 2026-10-16 |
| AC-31 | An unknown address showed an empty page (formerly ACO-09). | P3 | The wildcard route was missing. | 404 page (*features/not-found*, route *\*\**) with a link to the home page. | A nonexistent address shows the 404 page. | TD-009 | 2026-10-16 |
| AC-32 | The inspection report and its photos were kept only in the browser (formerly ACO-10). | P2 | There was no aggregate in the backend. | *InspectionReport* aggregate in operations-service with phases, findings, and photos (changelog 033); the public link opens on any device and the notice to the customer includes it. | Test *inspection-flow.test.js* of *lavarapido-infra/e2e* passed. | RF-036 | 2026-10-16 |
| AC-33 | The business data of Settings were kept only in the browser (formerly ACO-11). | P3 | The extension of *booking.establishment* waited for the definition of its fields. | Changelog 032 and *GET/PUT /api/v1/admin/establishment*; the home page and the help center read those data. | Changes are visible from another account and on the public page. | TD-013, RF-037 | 2026-10-16 |
| AC-34 | The *lavarapido-infra* README and the team guide described services and screens that already existed as pending (formerly ACO-12). | P3 | Documents not updated when the services were integrated. | Both documents updated. | Commits of 2026-10-16 in *lavarapido-infra*. | R-010 | 2026-10-16 |
| AC-35 | Two simultaneous requests could try to create the same operator, and one failed with error 500. | P2 | The operator creation read and then inserted without handling the race between requests. | The insert runs in its own transaction and, if the row already exists, it is read again instead of failing. | *OperatorPersistenceAdapterTest*; 54 operations-service tests passed. | Review of 2026-10-16 | 2026-10-16 |
| AC-36 | When a time outside the schedule or already past was requested, the system answered "That time was just taken". | P3 | The 409 response did not distinguish the reason. | The response carries *reason* (CLOSED_DAY, OUTSIDE_HOURS, TIME_PASSED, or NO_FREE_BAY), and the web and mobile show a message for each case. | Test *access-rules.test.js* (*reason: OUTSIDE_HOURS*) and *api-error.spec.ts*. | Review of 2026-10-16 | 2026-10-16 |
| AC-37 | The "Delete account" option said it erased the account and all the data, but it only deactivates it. | P2 | Text inherited from the mockup, contrary to what the server does. | The option is called "Deactivate my account" and explains that the history is kept, on the web and mobile and in the four languages. | Web and mobile profile; User Manual 4.0. | Law 1581 of 2012 | 2026-10-16 |
| AC-38 | The manuals and project documents became outdated with respect to the code of October 16 (lockout, automatic assignment, inspection, 57 tables), and the figures were placeholders. | P3 | Changes integrated without updating the documentation in the same pull request (R-010). | Manuals 4.0 with real screenshots, Implementation Procedure 2.0, this document 2.0, and the project's technical documentation reviewed against the code. | Version control of each document (2026-10-16). | R-010 | 2026-10-16 |
| AC-39 | With two accounts of the same method (for example, two Nequi), the web payment screen marked both at once and always showed the QR of the first one. | P3 | The chosen option was stored by method type and not by account. | The selection is stored by account and, if two accounts share a method, each option shows its holder. | *payment.spec.ts* (two Nequi accounts); 43 web tests passed. | Test Documentation, defect 6 | 2026-10-16 |
| AC-40 | When logging out, the web and the app showed "Your session expired". | P2 | A 401 that arrived after the token was deleted was taken as an expired session. | A 401 only ends the session with a notice while a session is still open (*auth-interceptor* on the web and *AuthServiceContext* in the app). | The notice no longer appears when logging out; it does when the token really expires. | Usage report | 2026-10-16 |
| AC-41 | On a phone (360 px) there were buttons off the screen, overlapping titles, the ☰ button over the titles, and filters outside their card. | P2 | Styles designed for desktop, without phone rules, and a fixed 20 px line height inherited from the Material theme. | Phone rules in the landing, authentication, customer dashboard, assigned services, management, and settings; global room for the ☰ button and a relative line height for titles. | 60 web screens (360 and 768 px) and 26 app screens with no overflow (tests, Table 5b). | Responsive design check | 2026-10-17 |
| AC-42 | In the app, the date and search inputs were squashed when placed in a column. | P3 | The box used *flex: 1*, which starts from a height of 0 in a column. | It grows only in width and keeps a minimum height of 46 px (*FilterInputs.tsx*). | History and Ratings show the full inputs. | Responsive design check | 2026-10-17 |
| AC-43 | The next service progress showed 10% or 25% although the wash had not started. | P3 | The progress table gave a percentage to the Scheduled and Confirmed statuses. | Scheduled and Confirmed stay at 0% on the web and in the app. | The dashboard shows 0% until the operator starts the service. | Usage report | 2026-10-16 |
| AC-44 | A token kept working up to an hour after logout or after the administrator disabled the account. | P2 | Each service only checked the JWT signature and expiry (ADR-006); nobody asked whether the session was still open. | security-service rejects the token of a closed session (*sid* claim, *ActiveSessionValidator*) and the gateway confirms it with *GET /api/v1/auth/session* before passing the request on, keeping the answer 30 seconds per token. | *SessionCheckFilterTest*, *ApiSecurityWebTest* (401 with the token of a closed session); 19 end-to-end tests with the check on. | TD-024 | 2026-10-21 |
| AC-45 | Bookings nobody started stayed Scheduled or Confirmed forever, and each admin screen counted "without an operator" its own way. | P2 | There was no close for expired bookings and the "without an operator" rule was repeated in the screens. | *ExpiredBookingsJob* cancels every 5 minutes, with the *EXPIRED* reason, a Scheduled booking one hour past its end without being confirmed; Confirmed ones are handled as AC-53 describes. *GET /api/v1/admin/assignments/unassigned* is the single count of bookings without an operator, on the web and in the mobile app. | *BookingTest*, *BookingServiceTest*, *OperationsServiceTest*, *reservations-store.spec.ts*. | TD-025 | 2026-10-21 |
| AC-46 | A manual and an automatic assignment at the same time could give an operator two services at the same time. | P2 | Both assignments read the operator schedule and saved without coordinating. | Both take the same SQL Server lock (*sp_getapplock* on *operations-assignment*) before reading and saving. | *OperationsServiceTest* checks that the lock is taken before saving. | TD-026 | 2026-10-21 |
| AC-47 | The server accepted payment receipts of any size, and a phone photo of several MB was stored as it was in the database. | P3 | The size limit was not in the payment domain. | payment-service rejects a receipt over 2 MB (*RECEIPT_TOO_LARGE*) and the web and the app compress it before sending it. | *PaymentTests* (over 2 MB and exactly 2 MB). | Review of 2026-10-21 | 2026-10-21 |
| AC-48 | The customer saw the Cash option even when the administrator had disabled that account. | P3 | *GET /api/v1/payment-accounts* did not return the cash account and the web and the app always added it on their own. | The API returns the cash account while it is active and the clients show Cash only if it comes in the list. | Payment screen with the cash account active and disabled. | Software Design, finding 8 | 2026-10-21 |
| AC-49 | The Reports cards and charts counted bookings that had not been finished as services done. | P3 | The count took the bookings by date without looking at their status. | Only completed bookings count. | *reports.spec.ts*. | Review of 2026-10-21 | 2026-10-21 |
| AC-50 | The roles tab promised that the permissions define what each user can do. | P3 | Real authorization uses the role in the token and the permission matrix is for reference (ADR-015), but the text did not say so. | The tab and the dialog explain that the roles are fixed and that the role decides the access. | *management.spec.ts* (fixed roles). | ADR-015 | 2026-10-21 |
| AC-51 | After signing out or signing in with another account in one tab, the other browser tabs kept showing the previous session. | P3 | Each tab read the session only when it opened. | The tabs listen to the *storage* event: if another one signs out they go back to sign-in, and if another account signs in they reload. | *auth.spec.ts* (tabs). | Review of 2026-10-21 | 2026-10-21 |
| AC-52 | The transaction log of the server database grew with no limit. | P2 | The database used the full recovery model without transaction log backups. | The database moves to the simple recovery model and is backed up in full every day (AP-04). | *RECOVERY SIMPLE* on the server; *lavarapido-infra*, commit of 2026-10-21. | R-008 | 2026-10-21 |
| AC-53 | The first version of AC-45 set to No-show every Confirmed booking nobody started one hour after its end. If the operator washed the car and forgot to press "Start", the history, reports, and metrics recorded that the customer did not come, and No-show is final. | P1 | The rule decided a final status on its own without knowing whether the service was done: the system cannot tell a no-show from a forgotten record when the booking has an operator. | The system only decides when it is sure: a Confirmed booking that never had an operator becomes No-show (operations-service asks booking through *POST /internal/v1/bookings/{id}/no-show*, which checks again that it expired); one with an operator that nobody started or finished goes to the administrator's "To close" list, on the web and in the mobile app, who closes it as "Attended" (without made-up times and without telling the customer to pick up the vehicle) or as "No-show"; the operator and the administrators get a single notice (migrations 040 and 041). | *FollowUpPolicyTest*, *FollowUpServiceTest*, *BookingServiceTest*, *BookingApiWebTest*, *reservations-store.spec.ts*; live test of October 21 on the web and in the mobile app. | Team review of 2026-10-21 | 2026-10-21 |
| AC-54 | A `null` field in an event payload sent the whole message to notification-service's dead-letter queue (DLQ), and the notice was not created. | P2 | Reading the event envelope failed on a `null` value instead of ignoring it. | The `null` field is ignored and the rest of the message is processed. | *DomainEventEnvelopeTest*. | R-005 | 2026-10-21 |
| AC-55 | The variables of October 21, 2026 (*SESSION_CHECK_\**, *BOOKING_EXPIRY_\**, and *FOLLOW_UP_\**) were not in *.env.example* or in the *environment* block of *docker-compose.yml*; in Docker only their defaults applied (formerly ACO-14). | P3 | They were added to the *application.yml* of each service without updating *lavarapido-infra*. | The nine variables, including *FOLLOW_UP_LOOK_BACK_DAYS*, are commented out in *.env.example* and *.env.production.example*, and the compose file passes them to *api-gateway*, *booking-service*, and *operations-service*. | *lavarapido-infra*, commit *70b990c*; *docker compose config* shows them (verified on 2026-10-23). | ACO-14 | 2026-10-22 |
| AC-56 | The gateway published */api/v1/operator/bookings/\** and booking-service let an operator start or finish any booking without checking that they were assigned or the 15-minute rule, and without publishing the execution events (E-01). | P1 | The route meant for operations-service's internal call was left published, and booking treated staff as administrators. | The gateway stops publishing the route; booking requires the internal key on the operator routes and applies the 15 minutes; operations-service sends the key. | Commits *b9107aa* (gateway, 404 test), *a73467d* (booking), *144a0c6* (operations), and *f07802a* (infra); CI green. | Review of 2026-10-22 (E-01, OPS-02, SEC-02, BIZ-03) | 2026-10-23 |
| AC-57 | Removing the administrator role in the database did not cut their token, which kept working for up to an hour (E-02). | P2 | The session was checked by its identifier (*sid*), not by the token's roles. | *ActiveSessionValidator* compares the token's roles with *security.user_role* and, if the token carries a removed role, rejects it and revokes the sessions. | security-service, commit *579480e* (*AuthenticationServiceTest*, *ApiSecurityWebTest*). Its effect was limited by ACO-15 (closed as AC-73). | Review of 2026-10-22 (E-02, SEC-03) | 2026-10-23 |
| AC-58 | The default profile was *dev*, and the passwords of the demo accounts and of SQL Server were in the code, the team guide, the compose file, and payment-service's README (E-05, H-06, H-07, and H-08). | P2 | Convenience of the local environment. | security-service no longer sets the *dev* profile; demo passwords are read from *DEMO_ADMIN_PASSWORD*, *DEMO_OPERATOR_PASSWORD*, and *DEMO_CLIENT_PASSWORD* (empty: the account is not created); infra and payment have no literal passwords. | Commits *e952fe2* (security), *f0800ef* (infra), and *1c7df54* (payment). The old password is still in payment's history and *main*, a demo password is still written in an example of *GUIA-EQUIPO.md*, and *payment-migrate* receives the password as an argument: ACO-25. | Instructor evaluation (H-06 to H-08) | 2026-10-23 |
| AC-59 | A 100 % coupon or a fixed discount greater than or equal to the total left the booking with amount 0, which the payment does not accept: the booking could not be paid (E-07). | P1 | The domain accepted 100 % and did not cap the fixed discount. | The percentage goes from 1 to 99 and the fixed discount is capped at the total minus 1 peso; the web and the app limit the form to 99 %. | Commits *827746f* (payment), *1175eb0* (web), and *c0cd099* (mobile). BIZ-04 and BIZ-05 were closed later in AC-77. | Review of 2026-10-22 (E-07, RF-024) | 2026-10-23 |
| AC-60 | "Remove coupon" only removed it from the screen: the server kept subtracting the redemption (E-08, B-05). | P2 | There was no way to cancel a redemption. | *DELETE /api/v1/loyalty/redemptions/{bookingId}/{promotionId}* (only the owning customer; 409 if there is already a payment), used by the web and the app. | Commits *6e2df05* (payment), *2158196* (web), and *ce630a5* (mobile). Recovering the redemption after reloading was closed in AC-75. | Review of 2026-10-22 (E-08) | 2026-10-23 |
| AC-61 | A booking could accumulate several coupons (E-09). | P2 | The rule and the database index were missing. | One promotion per booking and a unique index. | payment-service, commit *6e2df05* and migration *021-promotion-one-per-booking.sql* (with *onFail: CONTINUE*: before deploying, check that no booking has two coupons). | Review of 2026-10-22 (E-09) | 2026-10-23 |
| AC-62 | The @gmail email rule was only in some forms and the server accepted any domain (A-01). | P2 | The rule was placed in the clients and not on the server. | security-service validates *ALLOWED_EMAIL_DOMAINS* (400 *EMAIL_DOMAIN_NOT_ALLOWED*) and the web and the app apply the same rule in every email form. | Commits *bef15d5* (security), *fed8465* (infra), *9f24ce8* (web), and *ee6e339* (mobile). Caveat of October 23, resolved in AC-84: the server does not restrict sign-in, but the web and the app do, so an account already created with another domain cannot sign in. | Review of 2026-10-22 (A-01, SEC-08) | 2026-10-23 |
| AC-63 | The web did not allow reassigning a booking's operator (A-02). | P2 | The Assign button only appeared without an operator. | Assign also appears with an operator, in the list and in the detail. | Web, commit *eb3e16d*. | Review of 2026-10-22 (A-02, RF-009) | 2026-10-23 |
| AC-64 | The notes for the operator when assigning were discarded (B-01). | P3 | The assignment contract did not have the field. | operations-service stores the notes and the web sends them. | Commits *cf28fbe* (operations, migration 042) and *dbe0a49* (web). Caveat of October 23, resolved in AC-84: the operator still does not see the notes. | Review of 2026-10-22 (B-01) | 2026-10-23 |
| AC-65 | Manual assignment did not check whether the operator's account was deactivated, and rescheduling kept the operator without validating them again (A-13, E-12). | P2 | Only automatic assignment checked the account. | The active account is validated when assigning and the operator is validated again when rescheduling; the web and the app show the deactivated-account message. | Commits *e94319c* and *c653b4e* (operations), *5f4377c* (web), and *437bd03* (mobile). | Review of 2026-10-22 (A-13, E-12) | 2026-10-23 |
| AC-66 | Status changes made by the administrator did not notify the customer (A-11). | P3 | booking-service published no event on that change. | booking publishes the event and notification-service notifies the customer. | Commits *369bd85* (booking) and *f9c9adf* (notification). Caveat of October 23, resolved in AC-84: that change does not cancel the reminders. | Review of 2026-10-22 (A-11) | 2026-10-23 |
| AC-67 | A booking's loyalty points changed if the administrator changed the service's points (A-09). | P2 | The booking line did not store the points. | The points are frozen on each booking line. | booking-service, commit *8926287* (migration 043). | Review of 2026-10-22 (A-09) | 2026-10-23 |
| AC-68 | The cancellation reason chosen by the administrator was ignored (A-20, H-24). | P3 | The controller called the cancellation without the reason. | booking uses the chosen reason and the web asks for it. | Commits *5c23630* (booking) and *48e60e8* (web). | Instructor evaluation (H-24) | 2026-10-23 |
| AC-69 | The web's revenue was calculated by the report date and added up every year, and the operators' "In service" status was not calculated (A-17, A-19). | P3 | Calculations made with the wrong data. | Revenue by approval date and calculated status. | Web, commit *3c02a4d*. | Review of 2026-10-22 | 2026-10-23 |
| AC-70 | The operator's history and assigned services showed fake or empty data on the web (A-18). | P3 | Test data remained in the ViewModel. | Real data from the server. | Web, commit *b20d3a9*. Caveat of October 23, resolved in AC-84: the payment method and the status in the history are still fixed values. | Review of 2026-10-22 | 2026-10-23 |
| AC-71 | A plate longer than 6 characters was cut instead of rejected (E-16). | P3 | The value object truncated the text. | It is rejected with a validation error. | customer-service, commit *645fa5c*. Caveat of October 23, resolved in AC-84: a long plate already in the database breaks reading that vehicle. | Review of 2026-10-22 (E-16) | 2026-10-23 |
| AC-72 | The documentation became outdated after *v1.0.0*: it said there were no tags, cloned and rolled back with nonexistent tags, used *COMPRESSION* in the backup, did not document the mobile app for the user or the MVVM pattern, and this register did not have the real nonconformities (DOC-02 to DOC-16; A-03 to A-08, B-02, B-06, C-01 to C-03). | P3 | The documentation was not reviewed after the changes of October 22 and 23. | Review of documents 01, 02, 04 to 11, the README, and the CHANGELOG, in Spanish and English. | *lavarapido-docs*, branch *docs/update-october-23* (includes Merari Urbano's *fix/\** branches). The PDF and Word files are still to be regenerated. | R-010 | 2026-10-23 |
| AC-73 | The gateway did not check the session if the header said *bearer* in lowercase: the token of a closed session kept working for up to an hour in the services other than security (SEC-01, formerly ACO-15). | P1 | *SessionCheckFilter* only checked headers that started exactly with *Bearer *. | It checks every request with *Authorization*, with any scheme and on any route; the cache uses only the token, so signing out clears it in all its variants. | api-gateway, merge *2e20204* (*SessionCheckFilterTest*, 12 cases). In production since October 24. | Review of 2026-10-22 (SEC-01) | 2026-10-24 |
| AC-74 | A paid booking, or one whose payment was under review, could be rescheduled with other services and the approved payment did not match what was done (BIZ-01, formerly ACO-16). | P1 | booking accepted *serviceIds* when rescheduling and payment did not recalculate the amount when approving. | The customer only changes the date and time (409 *BOOKING_SERVICES_LOCKED*); when approving, if the amount does not match what is owed today, 409 *AMOUNT_CHANGED*; the administrator sees a warning when changing the services of a booking with a payment. | booking *2983d76*, payment *a898a98*, and web *19138db*. | Review of 2026-10-22 (BIZ-01) | 2026-10-24 |
| AC-75 | The customer could not see the amount the server charges or recover the coupon when opening the payment again (BIZ-02, remainder of E-08, formerly ACO-17). | P2 | The amount was only exposed by an administrator route. | *GET /api/v1/loyalty/redemptions/{bookingId}*; the web and the app show *amountDue*, recover the coupon after reloading, and "Remove" uses the DELETE. | payment *fb90b0c*, web *0bdc8c1*, and mobile *2ce105f*. | Review of 2026-10-22 (BIZ-02) | 2026-10-24 |
| AC-76 | Starting or finishing a service was not atomic with booking-service and the operator could be blocked (OPS-01, formerly ACO-18). | P2 | operations called booking before saving its own state. | It saves first and, if booking answers 409, reads the booking again and treats the step as done; row locks on the executions (OPS-03) and HTTP calls outside the assignment lock (OPS-11). | operations *6d18916*, *16175fc*, and *5e9d15b*. Caveats in ACO-35 and ACO-36. | Review of 2026-10-22 (OPS-01) | 2026-10-24 |
| AC-77 | Some bookings still could not be paid: price 0 in the catalog and a discount frozen in pesos that exceeded the total after rescheduling (BIZ-04, BIZ-05, formerly ACO-20); also, two simultaneous changes to the same booking overwrote each other (BIZ-09). | P2 | The catalog accepted price 0, the redemption stored the value and not the percentage, and the booking had no optimistic locking. | Price greater than 0 (400 *INVALID_PRICE*, migration 044); the redemption stores the percentage and the cap and the amount is recalculated with today's total (migration 022); *@Version* on the booking (migration 045). | booking *66a6998* and *348683e*; payment *6a924b4*. | Review of 2026-10-22 (BIZ-04, BIZ-05, BIZ-09) | 2026-10-24 |
| AC-78 | Events and notices could be lost without warning (OPS-04, OPS-05, OPS-06, formerly ACO-21). | P2 | Risk accepted in ADR-004 with no mitigation in the operations queue or in the deliveries. | operations: 3 retries and the dead-letter queue *operations-service.booking-confirmed.dlq*, with broker confirmation; notification: a *pending_delivery* table (migration 047) with retries after 1, 2, 4… minutes up to 5 attempts, and a retry if security does not answer when asking for the administrators. | operations *1fda373*; notification *511c022* and *a345ac9*. The Outbox remains an accepted risk (AP-02). | Review of 2026-10-22 (OPS-04, OPS-05) | 2026-10-24 |
| AC-79 | There was no request limit on sign-in or recovery, and the response time revealed which accounts existed (SEC-04, SEC-05, SEC-12, E-04, formerly ACO-22). | P2 | No limit was configured in the gateway. | 60 requests per minute per IP on */api/v1/auth/\** (429 *RATE_LIMITED* with *Retry-After*), with the client's real IP (*TRUSTED_PROXIES*); even timing for emails that do not exist; a cap of 5 attempts for the recovery code and the current password (423 *TOO_MANY_ATTEMPTS*). | api-gateway *3cfa9e9* and *5744839*; security *8987e9b*, *6450d2e*, and *c2fd4ec*. | Review of 2026-10-22 (SEC-04) | 2026-10-24 |
| AC-80 | The server did not require the Law 1581 consent when signing up (SEC-07, E-03, formerly ACO-23). | P2 | *RegisterRequest* did not validate the checkboxes. | Without both checkboxes, or with only one, it answers 400 *CONSENT_REQUIRED*. | security *c5ecf17*. | Review of 2026-10-22 (SEC-07) | 2026-10-24 |
| AC-81 | Two simultaneous payment reports could leave two payments under review and the reference was not unique (A-16, formerly ACO-27). | P2 | The rule was checked without a lock or index. | Migration 023: filtered unique index (one open payment per booking) and an indexed view for the reference among live payments; 409 with each rule's code. | payment *3e79a00*. Minor caveat in ACO-40. | Review of 2026-10-22 (A-16) | 2026-10-24 |
| AC-82 | Going back on the server was manual and global, old images were not cleaned up, and restoring did not run *payment-migrate* (CD-10, CD-11, E-20, formerly ACO-29). | P3 | The scripts were designed to update, not to go back. | *update-server.sh* keeps the running version as *:prev* and cleans images and cache after a healthy update; *rollback.sh* goes back to *:prev* without building (asks to type VOLVER, uses the lock, and waits for health); retention of 10 *pre-update* backups; *restore-db.sh* with a lock and *payment-migrate*. | lavarapido-infra *166c121*, *bae88a8*, and *1150821*. Caveats in ACO-37. | Review of 2026-10-23 (CD-10, CD-11) | 2026-10-24 |
| AC-83 | The role permission matrix was saved but not applied when authorizing (H-05, formerly ACO-31). | P3 | Authorization uses the token's role (ADR-015). | The permissions screen is read-only and the saving was removed. | web *a4d3715*. | Instructor evaluation (H-05) | 2026-10-24 |
| AC-84 | Caveats of the October 23 fixes (formerly ACO-32): the operator did not see the notes, a status change by the administrator did not cancel the reminders, accounts with another domain could not sign in, a stored long plate broke reading, the operator's history had fixed data, French and Portuguese texts were missing, and the EAS *production* profile did not set the URL (MOV-08). | P3 | The fixes were made on the same day as the review. | Notes visible to the operator; IN_PROGRESS, COMPLETED, or NO_SHOW cancel the reminders (type *BOOKING_NO_SHOW*, seed 116); sign-in accepts any domain and sign-up still requires gmail.com; the stored plate is normalized without validating it; the history status comes from the data; complete texts in the 4 languages; HTTPS URL in *production*. | operations *b943858*, web *7ac143d*, mobile *558d848* (B-01); notification *952cd7e* (A-11); web *31594ac*, mobile *eb7621c*, and security *0b5ed17* (A-01); customer *eaad74e* (E-16); web *86fea9b* (A-18) and *08d36ab*; mobile *7b5f095* (MOV-08). | Review of 2026-10-23 | 2026-10-24 |
| AC-85 | The new server scripts could downgrade it: when switching from *develop* to a *main* that does not have *v1.1.0* yet, *setup-server.sh* moved the repositories back without an error and left a mix of versions (cross review C1). | P1 | *git checkout main* goes back silently when *main* is an ancestor of *develop*. | *sync_repo* stops without touching anything if what is deployed is not contained in the requested branch (*ALLOW_DOWNGRADE=1* allows it on purpose). | lavarapido-infra *d5d6909* (merge *cf28ed0*). Before the October 24 deployment. | Cross review of 2026-10-24 (C1) | 2026-10-24 |
| AC-86 | The web image ran nginx as *root* (H-19, WEB-15). | P3 | The official nginx image was used on port 80. | *nginx-unprivileged* on port 8080, *npm ci --ignore-scripts*, and SRI on the flags; Caddy points to *web:8080* and so does the health check. | web *835395d*; lavarapido-infra *9e56536*. In production since October 24. | Instructor evaluation (H-19) | 2026-10-24 |
| AC-87 | After the October 24 deployment (around 15:10) notification-service sent no push notification or email at all (welcome, bookings, payments, and reminders): the service could not read the new table *notification.pending_delivery* ("The conversion from varchar to NCHAR is unsupported"). The inbox notices still worked, and so did the password-recovery email, which security-service sends directly. 2 deliveries were stuck (1 push and 1 welcome email). It was found by the live test after the deployment. | P1 | Migration 047 created *channel* VARCHAR(10) and *language* VARCHAR(5), but Hibernate reads all text as national characters (*use_nationalized_character_data: true*) and the SQL Server driver does not convert VARCHAR to NCHAR. The unit tests run without SQL Server and did not catch it. | Migration *048-notification-pending-delivery-nvarchar.sql*: *channel* becomes NVARCHAR(10) NOT NULL and *language* NVARCHAR(5) NULL, and *uq_pending_delivery_channel* UNIQUE (notification_id, channel) and *ck_pending_delivery_channel* CHECK (channel IN (N'PUSH', N'EMAIL')) are dropped and re-created; MARK_RAN precondition (only while *channel* is still VARCHAR) and *rollback*. It was first tested in *tempdb* with the data kept, and the notification suite passed. | notification *612417a* (merged into *develop* as *3f99df4*). Only notification-service was deployed, on October 24 at 17:12 UTC; the 2 deliveries went out at 17:13:50 (push) and 17:13:52 (email) and there have been no errors since. | Live test after the 2026-10-24 deployment | 2026-10-24 |
| AC-88 | *Preventive and improvement action* (mitigation of mass bookings: payment mode at booking, cash with a limit, and expiry of the digital payment). During the preparation of the final presentation, the question was what prevents a bot or a person from making many bookings and blocking the schedule. There was a limit on */auth* (60 requests per minute per IP and lockout after 5 failures), capacity by bays, and the rule that a vehicle cannot have overlapping bookings, but no limit per customer and no obligation to pay. The instructor recommended requiring payment at booking. | P2 | The booking required no payment commitment: an account could occupy bays with bookings it never intended to pay for, and the payment was only reported afterwards, whenever the customer wanted. | When booking, the customer chooses *paymentMode* = *DIGITAL* or *CASH* (400 *INVALID_PAYMENT_MODE*; the administrator does not have to). **Cash with a limit:** at most *BOOKING_CASH_ACTIVE_LIMIT* (1) active cash bookings made by the customer themselves (*booked_by*), counted with the schedule locked (409 *CASH_BOOKING_LIMIT*); the slot comes back when the booking stops being active, not when the administrator records the payment (team decision). **Expiry of the digital payment:** *paymentDeadline* = min(creation + *BOOKING_PAYMENT_WINDOW* (30 min), start); once overdue without a receipt, *PaymentDeadlineJob* cancels it with the reason *PAYMENT_TIMEOUT* and frees the slot. payment-service publishes *payment.reported* and booking consumes it in *booking.payment-events*. Booking migration 049 and seed 117. The web and the app add the "How will you pay?" step, the deadline, the cash label, and the Terms of October 26. Earlier bookings have no mode and no rules; rescheduling changes neither the mode nor the deadline. | booking *4c4f3f1*, *e2f339c*, and *6af4a91* (merge into *develop* *db156cb*); payment *f9ba11e*, *bd2a721*, and *c2fb5a8* (merge *d94cde5*); web *7b59401*, *abcfb7e*, *4932fda*, *3b3dd6f*, *64c9318*, *17af707*, and *4a3c77c* (merge *7f8aca5*); mobile *c680d40*, *3c7aac6*, *c7a1c2f*, *50819d6*, *8980d9e*, and *f9ef78f* (merge *acf4e6d*); lavarapido-infra *4fa22f3* (merge *0f949e5*). Tests: booking 133, payment 247, web 239, and mobile 100. Verification: live test of October 26 locally (400 without a mode, 201 and 409 with cash, the unpaid digital booking cancelled automatically after ~70 s, the paid one stayed confirmed) and deployment to production that day around 12:02 (Colombia time), with a cash booking (#12) and a digital one (#13). The app reaches phones by OTA with *v1.1.0*. | Question of the final presentation and recommendation of the instructor (RNF-004) | 2026-10-26 |
| AC-89 | In the live test of October 26, 2026 the *payment.reported* event was lost and *PaymentDeadlineJob* cancelled with *PAYMENT_TIMEOUT* a booking with digital payment that had in fact been paid. | P1 | There is no Outbox (AP-02): an event that does not reach RabbitMQ is lost, and the job made a final decision based only on what it had received through events (the same cause as AC-53). | Before cancelling, the job asks payment-service through *GET /internal/v1/payments/bookings/{bookingId}/reported* (port *PaymentStatusDirectory*, adapter *PaymentServiceDirectory*, 3 s, *X-Internal-Key*): with *reported=true* (an *IN_REVIEW* or *APPROVED* payment) it marks the booking and does not cancel it; with *false* it cancels it as usual; if payment does not answer or returns an error, it does not cancel it in that round and checks it again in the next one (*PaymentTimeoutOutcome*). payment-service now requires *INTERNAL_API_KEY* on */internal/\*\** (401 without a key or with a wrong one; a user's JWT does not work), and the gateway does not route that path. | booking *1e44d9a* (merge *db156cb*); payment *c2fb5a8* (merge *d94cde5*); *PaymentDeadlineJobTest*, *PaymentServiceDirectoryTest*, and *InternalApiKeyTests*. Simulated failures locally: (1) payment without RabbitMQ, the paid booking was not cancelled because the job asked payment; (2) payment down, nothing was cancelled ("not cancelled this round: PAYMENT_SERVICE_UNAVAILABLE") and, when payment came back, it cancelled it. In production, the internal path answered 401 without a key and 200 *reported:true* with a key for a booking with an approved payment. | Live test of 2026-10-26 | 2026-10-26 |
| AC-90 | Continuous deployment was merged but had never run on its own: the *AZURE_SSH_KEY*, *DEPLOY_DISPATCH_TOKEN*, and *EXPO_TOKEN* secrets were missing, and *main* did not have *v1.1.0* (formerly ACO-19; CD-01, H-15). | P2 | Each owner's repository secrets can only be created by the owner or an administrator. | The secrets were created and *v1.1.0* was released in the 10 repositories with infra last; infra *v1.1.1* removed payment-service from *DEVELOP_ONLY*. | Merge of the "Release v1.1.1" PR into *main* of *lavarapido-infra*: the **Desplegar en Azure** workflow ran on its own (*automatic=true*) at 22:19 on October 26 (Colombia time) and the requests from the other repositories kept coming until 22:48, all green (healthy services and web and API at 200); *boot-update* is installed since October 24. In the mobile app, APK *versionCode* 6 and the OTA were published after checking the native fingerprint (Deployment Document, section 1.3.12). | Deployment of 2026-10-26 | 2026-10-26 |
| AC-91 | booking-service returned 500 when the list of services of a booking held a null value or was too long (a list with no cap broke SQL Server's `IN`), and also when a day of the opening-hours week was invalid. | P3 | The body validation did not check each element of the list or cap it. | `serviceIds` with `@NotEmpty`, `@Size(max = 10)`, and `@NotNull @Positive` elements when creating and rescheduling (at most 10 services per booking), and validation of each day of the opening-hours week: they return 400 `VALIDATION_ERROR`. | booking *87b25f5* and *54d4125* (merge *8032aef*); `BookingApiWebTest.nullOrTooManyServicesAreValidationErrors` and `anInvalidDayInTheWeekIsAValidationError`, green in the CI. Version *v1.1.1*, deployed on October 27. | Review of 2026-10-27 | 2026-10-27 |
| AC-92 | The booking response to the customer included `bookedBy`, the identifier of the account that created it, although only the staff needs it. | P3 | The customer's and the staff's views used the same response conversion. | `BookingResponse.from(view, includeOwner)`: `bookedBy` and the vehicle owner are only filled in the administrator's and the operator's views. | booking *6cc2b86* (merge *04cdb94*). Version *v1.1.1*, deployed on October 27. | Review of 2026-10-27 | 2026-10-27 |
| AC-93 | With the time taken, booking offered as alternatives times that clashed with another booking of the same vehicle; when chosen, the booking failed with 409 `VEHICLE_ALREADY_BOOKED`. | P3 | The alternatives only looked at the free bays, not at the vehicle's other bookings. | `AvailabilityPolicy.withoutBusyTimes` removes from the alternatives the times that clash with another booking of the same vehicle. | booking *578c5a1* and *da233d5* (merge *6d9c8e9*). Version *v1.1.1*, deployed on October 27. | Review of 2026-10-27 | 2026-10-27 |

*Note.* Prepared by the authors from the Git history of the repositories, the risk register, and the ADRs of the project (Vehicle Washing Development Team, 2026a). AC-56 to AC-71 are in *develop* since October 23, 2026, and AC-73 to AC-87 since October 24, with the CI green. All of them run in production since the October 24, 2026 deployment (the server was left on *develop*; AC-87, since the notification-service update at 17:12 UTC) and reach *main* with version *v1.1.0*. AC-88 and AC-89 are in *develop* and in production since October 26, 2026 (booking-service, payment-service, and the web); AC-88 is a preventive and improvement action, but it is recorded here with the next AC number because it was born from a concrete finding (the question of the final presentation) and was closed with evidence. AC-91 to AC-93 are on *main* since booking-service *v1.1.1* (October 27, 2026) and in production since that early morning's continuous deployment.

## 10. Register of Open Corrective Actions

**Table 7**

*Open Corrective Actions*

| ID | Nonconformity | Sev. | Root cause | Proposed action | Responsible | Priority | Closure criterion |
|---|---|---|---|---|---|---|---|
| ACO-04 | The table *audit.audit_log* exists but nothing writes to it. | P2 | The triggers were not created and *SESSION_CONTEXT(N'user_id')* is not set. | Audit triggers on the sensitive tables and session context in each service, or remove the table from the design through an ADR. | Backend leader | Medium | Changes of prices, payment accounts, and roles visible in the audit trail. |
| ACO-13 | The entities that administrators edit in customer-service and security-service do not use optimistic locking: in concurrent edits, the last write wins. booking-service's bookings (BIZ-09, AC-77) and operations-service's executions (OPS-03, AC-76) are already protected. | P3 | The *row_version* column exists but is not mapped with *@Version*. | Map *@Version* in customer and security and answer 409 on conflict. | Backend leader | Low | Test of two updates with the same version. (TD-008) |
| ACO-24 | *develop* is not protected in any repository (H-02). Progress as of October 27, 2026: the 10 repositories have their release on *main* (*v1.1.0*, *v1.1.1*, or *v1.1.2*), *main* is protected in all of them, and the public server runs *main*; payment-service is deployed from *main* since infra *v1.1.1*. | P2 | Protecting *develop* was left for after the releases. | Protect *develop* in the 10 repositories (PR and green CI). | Backend leader | High | Tag in the 10 repositories (done), *main* protected (done), and *develop* protected. |
| ACO-25 | Secrets are still exposed: the old development SQL Server password in the Git history and in payment-service's *main*, and the Firebase key in *google-services.json* (BIZ-11, H-09, MOV-18). Since October 24, *payment-migrate* receives the password through an environment variable and the team guide uses placeholders (E-05, *lavarapido-infra* *b63f85a*). On October 27 (payment-service PR #15, in *v1.1.3*, which was merged into *main* that day and tagged on November 4) the demo account passwords left the end-to-end script, the service stopped using default credentials (*DB_PASSWORD* and RabbitMQ), and *.gitignore* covers any *.env.\**; the review of the 99 commits found two old SQL Server passwords and the demo account ones in the history, none equal to the local *.env* ones. | P2 | They were committed at the start of the project. | Rotate the password and restrict the Firebase key by package and SHA-1 fingerprint. | Backend leader | Medium | The old password no longer works and the key only works for the app. |
| ACO-26 | The end-to-end tests do not run on their own: the remote battery (*e2e.yml*, 44 cases) exists but has only been tested against a simulator, and the 19 local end-to-end tests do not run in the CI (F-01 to F-05, H-13). Progress as of October 27, 2026: payment-service's 17 integration tests already run in the CI ("Pruebas de integración" job, against SQL Server 2022 with the Liquibase migrations) and the 19 end-to-end tests passed 19 of 19 locally that day. | P2 | The CI does not start the complete system and the test accounts are missing in production. | Create the 3 test accounts and the *E2E_\** secrets, run *e2e.yml* against Azure (without flags, then with *allow_writes*), bring the end-to-end tests into the CI, and add integration tests to the other services. | Development team | Medium | *e2e.yml* green against Azure, the end-to-end tests in the CI, and the other services' integration tests run (payment's, done). |
| ACO-28 | The app allows unencrypted traffic in all its builds (*usesCleartextTraffic*, MOV-11). The EAS *production* profile already sets the HTTPS URL (MOV-08, AC-84). | P3 | Changing the native configuration changes the app's fingerprint and requires distributing a new APK. | Remove *usesCleartextTraffic* in the next APK. | Backend leader | Low | A new APK without cleartext traffic. |
| ACO-30 | There are no issues or tracking board in the repositories (H-22). | P3 | Tracking was kept in documents. | One issue per user story, linked PRs, and a GitHub Projects board. | Development team | Medium | Each PR closes an issue and the board shows the status. |
| ACO-33 | The web requests the administrator's payment queue without pagination, with each receipt's image, and requests it again every 45 seconds: with real data it puts payment-service's memory (384 MB) at risk. | P2 | The paged route (*?page&size*, without image, with *hasReceiptImage*) was added to payment (E-10) and to the app, but did not reach the web in time. | Request the queue with *page* and *size* and the image in the detail (*GET /admin/payments/{id}*). | Frontend leader | High | The web's queue requests pages of 20 without images. |
| ACO-34 | booking-service takes the schedule lock and, while holding it, calls customer-service: if customer is slow, every booking write queues up and may answer 409 *SCHEDULE_BUSY* (E-11). | P2 | Cancelling, changing the status, and the operator's progress ask for the vehicle after the lock. | Ask for the vehicle before taking the lock and read the booking again afterwards. | Frontend leader (booking-service) | Medium | A test with a slow customer that does not block the other bookings. |
| ACO-35 | A booking that the administrator marks COMPLETED with open lines generates false "not started" notices to the operator and the administrators, and shows up in "To close" (OPS-01). | P2 | The follow-up checks the lines without looking at the booking's status. | Do not notify COMPLETED bookings; close their lines as attended or leave them only in "To close". Before the release, count in the database the COMPLETED bookings with open lines. | Project leader | Medium | A test with a booking completed by the administrator that generates no notices. |
| ACO-36 | On each *BookingConfirmed*, operations-service asks security-service for the account of every operator, including those with no shift; since the check fails closed (A-13), a single transient failure breaks the assignment (OPS-11). | P3 | The account check is done before filtering the candidates. | Filter first with *AssignmentPolicy* (no network) and ask only about the candidates. | Project leader | Medium | A confirmation asks security only about the candidates. |
| ACO-37 | Three risks remain in the server scripts: *restore-db.sh* makes a backup with the 14-file retention that may delete the file about to be restored; *rollback.sh* uses the current compose with the *:prev* images and does not detect a contract change (port, variables); and *update-server.sh* run by hand does not take the lock. | P2 | The new scripts of October 24 were reviewed after merging them. | Decompress before backing up; keep the *repo-heads* next to *:prev* and warn if infra changed; take the lock in *update-server.sh*. | Project leader | Medium | A simulation of the three cases with the expected result. |
| ACO-38 | The inspection opened by link or after reloading shows the customer, the status, the service, and the date empty on the web. | P3 | With the automatic refresh (WEB-16) the stores no longer load in their constructor and the inspection does not request the booking. | Call the bookings' *load()* when opening the inspection. | Frontend leader | Medium | Opening an inspection by link shows all its data. |
| ACO-39 | If the session expires with the app in the background, the phone is not unregistered from push and keeps receiving it (MOV-10). | P3 | The timer does not run in the background and, on return, the unregistration answers 401. | Check the expiration when returning to the foreground before any request, or unregister by the push token on the server. | Backend leader | Medium | A test on a device: the session expires in the background and no more push arrives. |
| ACO-40 | Minor findings of the cross review of October 24, 2026: *reasonCode* unused in notification-service, the automatic NO_SHOW without an event, the package (PACKAGE) that cannot be edited, the FIXED amount in the coupon notice, old booking variables (*BOOKING_EXPIRY_\**) in infra, *markDelivered* inside the same *try* as the delivery (possible duplicate), the backfill of payment migration 022 with today's promotion, and the view of 023, stricter than the service within the same booking; also, the new gateway and operations variables (*AUTH_RATE_LIMIT_**, *TRUSTED_PROXIES*, *ROUTE_*_TIMEOUT*, *START_LATE_LIMIT*) are not in *.env.example* or the compose file. | P3 | Details that do not affect the main flows. | Fix them one by one and, before the release, look in the database for the duplicate open payments that 023 rejected. | Development team | Low | Each item with its commit or discarded with a justification. |
| ACO-41 | Cash with a limit (AC-88) has no tolerance for lateness: a cash booking whose customer does not arrive keeps occupying the bay and their cash slot until the administrator closes it or the operations follow-up resolves it. | P3 | When designing payment at booking, the team chose only the limit and the expiry of the digital payment, and left the tolerance for lateness for later. | Cancel the cash booking if it has not started N minutes after its start time (configurable value), with its own reason used only by the system, and notify the customer. | Backend lead | Low | Rule implemented with its test and tested live, or discarded with a justification. |
| ACO-42 | The Terms and Conditions and the Data Processing Policy of the web and the app carry example contact data: email *datos@expresscarwash.co*, phone *+57 300 123 4567*, tax ID (NIT), and address. | P2 | The legal texts were written before having the real data of the data controller. | Replace the email, phone, tax ID, and address with the car wash's in the 4 languages of the web and the app. | Project leader | High | The published legal texts show the real data of the controller (Law 1581 of 2012). |
| ACO-43 | The booking lists are not requested by pages: *GET /bookings/me* returns all of the customer's and *GET /admin/bookings* all of the range, with no cap. | P3 | The screens were built with little test data. | Add *page* and *size* to both routes and request them by pages in the web and the app. | Frontend leader (booking-service) | Medium | The booking lists load by pages with real data. |
| ACO-44 | The web has no payment history screen (RF-015 partial): */client/history* is the booking history and nobody uses the filters of *GET /payments/me*. | P3 | The payment history was covered with the payment status of each booking. | Customer payment history screen with the API's date and service filters. | Frontend leader | Medium | RF-015 implemented and with its test case green. |

*Note.* Prepared by the authors from the technical backlog, Annex B of the Technical Manual, and the verification of the code. ACO-01 to ACO-03 and ACO-05 to ACO-12 moved to Table 6 as AC-24 to AC-34, and ACO-14 as AC-55 (October 23, 2026). On October 24, 2026, ACO-15 to ACO-18, ACO-20 to ACO-23, ACO-27, ACO-29, ACO-31, and ACO-32 were closed (AC-73 to AC-84) and ACO-33 to ACO-40 were opened with the pending items of that day's final cross review. On October 26, 2026 ACO-41 was opened, the pending improvement of payment at booking, and that night ACO-19 was closed as AC-90 (first run of continuous deployment). On October 27, 2026 ACO-42 to ACO-44 were opened with real pending items that were not recorded.

## 11. Register of Preventive Actions

Preventive actions address the causes of risks that have not yet materialized or that could recur.

**Table 8**

*Preventive Actions*

| ID | Risk or potential cause | Preventive action | Responsible | Priority | Status | Origin |
|---|---|---|---|---|---|---|
| AP-01 | Regressions reach *develop* unnoticed. | Continuous integration per repository (*./mvnw verify*, *dotnet test*, *ng build*, *ng test*, and *tsc*) and the rule of merging only with a green result. | Project leader | High | Closed (2026-10-19): GitHub Actions in the 10 repositories, on every push or pull request to *develop* and *main* | R-003, TD-006 |
| AP-02 | An event is lost between the commit and the publication. | Outbox pattern in security, booking, operations, and payment. | Backend leader | Low | Accepted as a risk until leaving the academic scope; mitigated on 2026-10-24 with broker confirmations, a dead-letter queue in operations, and pending deliveries with retries in notification (AC-78) | R-005, TD-011 |
| AP-03 | Secret leakage (*.env*, mail app password, *JWT_SECRET*). | Keep *.env* ignored, check with *git check-ignore* before each implementation, and rotate secrets every quarter or on suspicion. | Whole team | High | In progress (permanent) | R-004 |
| AP-04 | Data loss before a demonstration or in operation. | Automated daily backup with verification and a restoration test (Administrator Manual, section 14). | IT administrator | High | Closed (2026-10-21): *backup-db.sh* backs up the database every day and before every update, *verify-backup.sh* restores each backup under another name to check it, and the last 14 are kept (Deployment Document) | R-008 |
| AP-05 | The documentation drifts from the code again. | Pull request checklist that requires updating the affected models, OpenAPI contracts, ADRs, and manuals; review of the manuals before each implementation. | Author of each pull request | Medium | In progress (permanent) | R-010 |
| AP-06 | Implementing with *MESSAGING_ENABLED=false*, without *INTERNAL_API_KEY*, or with the *dev* profile. | Checklist of Annex A of the Implementation Procedure. | IT administrator | Medium | Closed (included in the procedure) | R-I02, R-I03 |
| AP-07 | Persistence defects (locks and triggers) seen only by hand. | Persistence tests with Testcontainers and a real SQL Server in booking-service. | Backend leader | Low | In progress (the end-to-end suite covers part of the risk) | TD-010 |
| AP-08 | All services use the *sa* account. | One login per service with permissions only on its schemas and a separate account for the migrations. | Backend leader | Medium | Accepted as a risk (2026-10-24): documented in *lavarapido-infra*'s *DEPLOY.md* (H-10), with the proposed change for after the presentation | Security policy |
| AP-09 | A user action cannot be followed across services. | Propagate *X-Correlation-Id* in all REST clients and messages and generate it in the gateway. | Backend leader | Low | In progress (booking, operations, and payment already forward the header) | TD-007 |
| AP-10 | Lack of metrics to check the response time (RNF-001). | *micrometer-registry-prometheus* in the Java services and equivalent metrics in .NET. | Backend leader | Low | Open | TD-012 |
| AP-11 | Brute-force attacks or API abuse if the system is exposed. | Request rate limit in the gateway or in Caddy (TD-022); account lockout is already active (AC-25). | Backend leader | Medium | Closed (2026-10-24): 60 requests per minute per IP on */api/v1/auth/\** in the gateway (AC-79) | Security policy |
| AP-12 | Components with known vulnerabilities. | Quarterly review with *./mvnw versions:display-dependency-updates*, *dotnet list package --vulnerable*, and *npm audit*. | Backend and frontend leaders | Medium | In progress (permanent) | OWASP A06 |
| AP-13 | Confusion caused by the own numbering of the payment changelogs, which repeats the global numbers. | Document the rule in the migration strategy and use the *pay-* prefix in the identifiers. | Backend leader | Low | Closed (documented) | TD-019 |
| AP-14 | Features built that contradict decisions already recorded (cause of AC-11 and AC-12). | Review the current ADRs and the navigation map before starting a feature, and consult the team when there is a contradiction. | Whole team | Medium | In progress (permanent) | Lesson of ADR-015 |
| AP-15 | Interface changes that break the phone layout without anyone noticing (cause of AC-41). | Measure the screens at 360 and 768 px with Playwright before merging interface changes, starting from the *e2e/width-360.js* script, and review the screenshots. | Frontend and backend leads | Medium | In progress (permanent) | Lesson from AC-41 |
| AP-16 | Container logs filling the server disk. | Cap the logs of each container at 3 files of 10 MB. | Project leader | Medium | Closed (2026-10-20): *docker-compose.prod.yml* | Server review |
| AP-17 | Browser-side attacks against the public site (embedding it in another page, loading scripts from other origins). | Security headers and a content security policy (CSP) in Caddy. | Project leader | Medium | Closed (2026-10-20): *deploy/Caddyfile* | OWASP A05 |
| AP-18 | A container hanging without anyone noticing. | Health check every 30 seconds in every container, visible in *docker compose ps*. | Project leader | Medium | Closed (2026-10-20): *docker-compose.prod.yml* | Server review |
| AP-19 | An automatic job recording a final status with incomplete data (cause of AC-53). | Every automatic rule that changes a final status only acts when there is no doubt; anything doubtful goes to a list for a person to decide, and the rule is reviewed by the team before merging it. | Whole team | High | In progress (permanent) | Lesson from AC-53 |

*Note.* Prepared by the authors from the risk register, the technical backlog, and the security policy of the project.

## 12. Register of Improvement Actions

**Table 9**

*Improvement Actions*

| ID | Improvement opportunity | Expected benefit | Responsible | Priority | Status | Origin |
|---|---|---|---|---|---|---|
| AM-01 | Automatic operator assignment when a booking is confirmed, reusing *AssignmentPolicy*. | The administrator stops assigning every booking by hand (RF-008). | Backend leader | Medium | Closed (2026-10-16): *BookingConfirmedListener* and *AutoAssignmentPolicy*, with the optional choice of the customer; test *booking-flow.test.js* | TD-020, ADR-014 |
| AM-02 | Apply the permission matrix of *security.role_permission* to the real authorization of each endpoint. | Granular access control configurable from the panel. | Backend leader | Low | Open (requires an ADR); since 2026-10-24 the screen is read-only (AC-83) | ADR-015 |
| AM-03 | Rescheduling of bookings by the customer from the web and the app (the API already offers *PUT /api/v1/bookings/{id}*). | The customer does not have to cancel and book again. | Frontend leader | Medium | Open | User Manual |
| AM-04 | Complete the mobile application and build a version with EAS for push notifications. | Immediate notices on the phone. | Frontend leader | Medium | Closed (2026-10-16): APK built with EAS and published at */app/* on the server, with push notices of new versions | ADR-011 |
| AM-05 | RS256 signing and refresh tokens. | Lower impact of a secret leak and longer sessions without logging in again. | Backend leader | Low | Open | ADR-006 |
| AM-06 | Store receipts, QR codes, and photos outside the database. | Lighter database and faster backups. | Backend leader | Low | Open | Recommended evolution |
| AM-07 | Separate the display fields and the real discount fields in *promotion.promotion*. | Prevents the displayed package and the applied discount from contradicting each other. | Backend leader | Low | Open | ADR-015 |
| AM-08 | Publish the built web application behind the gateway with HTTPS. | Production-ready environment, without a development server. | Frontend leader | Medium | Closed (2026-10-16): web container with nginx behind Caddy and HTTPS on the public server (Deployment Document) | Implementation Procedure, R-I08 |
| AM-09 | Coverage (JaCoCo) and static analysis (SonarQube) tools. | Objective code quality metrics. | Project leader | Low | Open | Testing strategy |
| AM-10 | Automated end-to-end test across services (registration → booking → notification → payment). | Early detection of integration failures. | Backend leader | Medium | Closed (2026-10-16): *lavarapido-infra/e2e*, 19 tests passed against the containers | Testing strategy |
| AM-11 | Replace the figure placeholders of the manuals with real screenshots of the final version. | Complete manuals according to the SENA guide. | Frontend leader | High | Closed (2026-10-16): manuals 4.0 with real screenshots of the system | Annex A of each manual |
| AM-12 | Use the execution statuses *PAUSED* and *WITH_ISSUE* in the operator interface. | Record pauses and issues of the service. | Frontend leader | Low | Open | Data model |
| AM-13 | Expose in the panel the sending of general announcements (*SYSTEM_MESSAGE*) to groups of users. | Direct communication with customers and operators without using the API. | Frontend leader | Low | Open | Administrator Manual |
| AM-14 | Manage the inspection report from the mobile app. | The administrator records the vehicle condition next to the vehicle, with camera photos shrunk on the phone. | Backend lead | Medium | Closed (2026-10-17): *AdminInspection* screen in the app | Team request |
| AM-15 | Frequently asked questions on the home page. | Visitors solve the most common doubts (booking, payment, cancellation, points) without writing to the car wash. | Backend lead | Low | Closed (2026-10-17) | Landing review |
| AM-17 | Continuous deployment: every approved merge into *main* reaches the server on its own and, when the machine is turned on, what came in while it was off is deployed. | Nobody logs in over SSH to deploy and the server does not fall behind *main*. | Project leader | High | Implemented (2026-10-26): first automatic run when merging "Release v1.1.1" into infra's *main* and mobile OTA published after checking the native fingerprint (AC-90) | Review of 2026-10-23 |
| AM-18 | Behavior tests in the clients. | Catch the web's and the app's errors in time. | Development team | High | In progress (2026-10-27): the web has 242 tests in 68 files, with coverage in the CI (real guard and interceptor, coupon payment, refresh, and translations of the 4 languages), and the app 106 tests (ACO-26) | Instructor evaluation (H-03, H-04) |
| AM-16 | Require installing the new APK when there are native changes that do not arrive over the internet. | Nobody keeps using an app version without the fixes that depend on the APK. | Backend leader | Medium | Closed (2026-10-21): a screen that only lets the user download the published APK if its *versionCode* is higher; if the server does not answer, the app goes on | Review of 2026-10-21 |

*Note.* Prepared by the authors from the recommended evolution of the Technical Manual, the ADRs, and the limitations documented in the manuals.

## 13. Follow-Up Indicators

**Table 10**

*Action Management Indicators*

| Indicator | Formula | Target | Value as of 2026-10-27 |
|---|---|---|---|
| Closed corrective actions | Closed corrective actions / recorded corrective actions × 100 | ≥ 70% per delivery | 93 / 112 = 83% (on 2026-10-26 it was 90 / 106 and in the afternoon 89 / 106; on 2026-10-24, 87 / 103; on 2026-10-23, 72 / 92; on 2026-10-21, 54 / 57) |
| Open P0 and P1 corrective actions | Number of ACO with severity P0 or P1 | 0 before an implementation | 0 (ACO-15 and ACO-16 were closed as AC-73 and AC-74) |
| On-time closure | Actions closed within the time of their severity / closed actions × 100 | ≥ 90% | To be measured from version 1.0 |
| Recurrence | Closed corrective actions whose problem reappears / closed corrective actions × 100 | 0% | 0% |
| Escaped defects | Defects found in acceptance or operation / total defects of the period × 100 | Downward trend | To be measured from the first implementation |
| Automated tests | Tests run and passed in every repository | Increasing | 1,348 in the *main* CI of October 27, 2026, all passed: gateway 62, security-service 200, customer-service 83, booking-service 137, operations-service 110, notification-service 133, payment-service 275 with *v1.1.3* (25 in the integration project, 12 of them against SQL Server), web 242, and mobile app 106; the 19 end-to-end tests passed locally that day. Before: with payment-service *v1.1.2* (264), 1,337; on October 26, 2026 booking (133), payment-service (247, without the integration tests), the web (239), and the mobile app (100) changed; with the other artifacts of the latest CI of October 24, 1,306 without payment's infrastructure project (on October 24 it was 1,187 with the same criterion and 1,192 with it); on October 27, 2026 booking went up to 137 and customer-service to 83, for 1,311 with the same criterion, and payment's persistence tests started running in the CI against SQL Server. In addition, the live test of payment at booking of October 26 (11 tests, 2 of them simulated failures) and 42 functional cases on October 19 |

*Note.* Prepared by the authors. The test count corresponds to the latest *develop* CI of each repository on October 24, 2026 (on October 23 there were 838; in *v1.0.0*, 757; on October 21, 707); version 1.0 counted annotations (about 400). On October 26, 2026 the counts of booking, payment, the web, and the mobile app were added (Test document, Table 5f). On October 27, 2026 the count of the *main* CI was taken (Test document, Table 5f). The recorded corrective actions count AC-01 to AC-93 (AC-88 is preventive and improvement, but has an AC number) and the 19 open ACOs.

The project leader reviews the indicators at each retrospective and before each implementation. As of October 27, 2026 there are 19 open corrective actions (ACO-04, ACO-13, ACO-24 to ACO-26, ACO-28, ACO-30, and ACO-33 to ACO-44), all P2 or P3 and with a recorded action. Since no P0 or P1 remains, **entry criterion E6** of the Implementation Procedure **is met**. The highest priority is ACO-33 (the web's unpaginated payment queue), which should be closed before loading real data.

## 14. Lessons Learned

**Table 11**

*Lessons Learned*

| No. | Lesson | Related actions |
|---|---|---|
| L-01 | Storing time in UTC and converting it only at the edges prevents time shifts. | AC-01 |
| L-02 | A service must not start before its dependencies are healthy; the orchestration must guarantee it. | AC-02 |
| L-03 | Agreeing on the data model before coding a service prevents duplicate implementations. | AC-03 |
| L-04 | Every decision already recorded in an ADR must be reviewed before building; a contradiction is discussed before coding. | AC-11, AC-12, AP-14 |
| L-05 | A business rule must live in a single service, and the screens must query it, not repeat it. | AC-15, AC-45 |
| L-06 | A preference that the user changes must be saved in their account to have an effect. | AC-16, AC-17 |
| L-07 | A new service is not finished until it has a container, a gateway route, Compose variables, and published events. | AC-04, AC-05, AC-06 |
| L-08 | Administrative endpoints also need strict input validation and allowlists. | AC-18, AC-19 |
| L-09 | Documentation is updated in the same pull request as the code; otherwise, it becomes outdated within days. | AC-23, AP-05 |
| L-10 | An accounting ledger needs reversal movements and concurrency control from day one. | AC-09 |
| L-11 | A "create if it does not exist" operation must handle the race between two simultaneous requests. | AC-35 |
| L-12 | Each interface option must say exactly what it does with the person's data: deactivating is not deleting. | AC-37 |
| L-13 | Signing out must void the token in every service, not just delete it from the browser. | AC-44 |
| L-14 | What is done by hand and what the system does on its own over the same resource need the same lock. | AC-46 |
| L-15 | A backup only counts once it has been shown that it can be restored. | AP-04, AC-52 |
| L-16 | The system must not decide something final on its own with incomplete data: if the operator may have forgotten to register the service, the administrator decides. | AC-53, AP-19 |
| L-17 | A deployment script must never be able to downgrade: before switching branches, check that what is deployed is contained in the requested branch. | AC-85 |
| L-18 | A contract change between two parts (the web's port and the Caddyfile, a new API field) is deployed in the same step or with the consumer first; otherwise the first deployment leaves the feature down. | AC-86, ACO-33, ACO-37 |
| L-19 | A migration is not tested until the service reads and writes the table in a real SQL Server: unit tests, which run without a database, do not see type clashes between the database, the driver, and Hibernate. The live test after deploying found the one in 047; since October 27, 2026 payment-service's integration tests run in the CI against SQL Server 2022 with the Liquibase migrations, and those of the other services are still pending (RNF-003, ACO-26, and AP-07); the live test after each deployment stays. | AC-87, ACO-26, AP-07 |
| L-20 | As long as there is no Outbox, an event can be lost. Before an automatic task does something final because of what it received (or did not receive) through events, it must confirm it with the service that owns the data, and if that service does not answer, not act and try again. The live test with simulated failures (without RabbitMQ and with the service down) showed it before reaching production. | AC-89, AP-02, AP-19 |

*Note.* Prepared by the authors from the register of closed risks and the actions of this document.

## 15. Glossary

**Table 12**

*Glossary*

| Term | Definition |
|---|---|
| ADR | Architecture decision record. |
| Correction | Immediate action that eliminates the symptom of a nonconformity without addressing its cause. |
| Corrective action | Action that eliminates the cause of a nonconformity that occurred, to prevent its recurrence. |
| Effectiveness | Extent to which an action achieves the planned result. |
| Improvement action | Action that increases the value or quality of the software without an existing failure. |
| Ledger | Accounting record of point movements, whose balance is that of the last movement. |
| Nonconformity | Failure to meet a requirement, a recorded decision, or what is documented. |
| PDCA | Plan, do, check, and act cycle of continual improvement. |
| Post-mortem | Blameless analysis of a serious incident, with its cause and its actions. |
| Preventive action | Action that eliminates the cause of a potential nonconformity. |
| Root cause | Fundamental cause of a problem on which action can be taken so that it does not recur. |
| Technical debt | Pending work that makes the software work, but not as it should. |

*Note.* Prepared by the authors from ISO (2015), ISO et al. (2022), and the project documentation.

## References

Congreso de la República de Colombia. (2012). *Ley Estatutaria 1581 de 2012, por la cual se dictan disposiciones generales para la protección de datos personales* [Statutory Law 1581 of 2012, which issues general provisions for the protection of personal data]. http://www.secretariasenado.gov.co/senado/basedoc/ley_1581_2012.html

International Organization for Standardization. (2015). *Quality management systems — Requirements* (ISO 9001:2015). https://www.iso.org/standard/62085.html

International Organization for Standardization, International Electrotechnical Commission, & Institute of Electrical and Electronics Engineers. (2017). *Systems and software engineering — Software life cycle processes* (ISO/IEC/IEEE 12207:2017). https://www.iso.org/standard/63712.html

International Organization for Standardization, International Electrotechnical Commission, & Institute of Electrical and Electronics Engineers. (2022). *Software engineering — Software life cycle processes — Maintenance* (ISO/IEC/IEEE 14764:2022). https://www.iso.org/standard/80710.html

Servicio Nacional de Aprendizaje. (2026). *Guía para elaborar manuales de software: Manual de usuario, manual técnico, manual de instalación y manual de administrador* [Guide for writing software manuals: User manual, technical manual, installation manual, and administrator manual] [Unpublished instructional material]. Technologist in Software Analysis and Development program.

Vehicle Washing Development Team. (2026a). *Vehicle washing management system: Project documentation* (vehicle-w-docs) [Unpublished project documentation]. GitHub. https://github.com/code-sena/vehicle-w-docs

## 16. Annexes

### Annex A. Action Record Form

**Table 13**

*Action Record Form*

| Field | Content |
|---|---|
| ID | AC-NN, ACO-NN, AP-NN, or AM-NN |
| Type | Corrective / Preventive / Improvement |
| Recording date and who detected it |  |
| Description of the nonconformity, risk, or opportunity |  |
| Initial evidence (log, screenshot, test, error code) |  |
| Severity (P0 to P3) and priority (High, Medium, Low) |  |
| Immediate correction applied (if any) |  |
| Root cause and technique used |  |
| Planned action, person responsible, and committed date |  |
| Branch, pull request, and related ADR |  |
| Closure criterion |  |
| Effectiveness verification (who, when, evidence) |  |
| Status and closure date |  |
| Lesson learned |  |

### Annex B. Root Cause Analysis Example

**Table 14**

*Five Whys of Action AC-01 (Times Shifted by Five Hours)*

| Question | Answer |
|---|---|
| Why did the customer see their booking at another time? | Because the time shown differed by five hours from the one chosen. |
| Why did it differ? | Because the time was stored converted with one time zone and read with another. |
| Why were different time zones used? | Because the JVM of the service took the time zone of the machine where it ran. |
| Why did the machine's time zone affect the data? | Because the code converted dates without an explicit time zone. |
| Why was there no explicit time zone? | Because there was no team rule for handling time. |
| **Root cause** | Absence of a time convention: "store in UTC and convert only at the edges with *BOOKING_ZONE*" was adopted, and the JVM runs in UTC. |

*Note.* Prepared by the authors from the closed risk C-001 of the project.

### Annex C. Traceability Between Actions, Risks, and Technical Debt

**Table 15**

*Traceability*

| Project risk | Technical debt | Actions |
|---|---|---|
| R-002 Partial payment-service (closed as C-005) | TD-014, TD-015, TD-016, TD-017, TD-018 | AC-04 to AC-10, AC-27 |
| R-003 Regressions without a safety net | TD-001, TD-006, TD-010, TD-027 | AC-24, AC-87, AP-01, AP-07, AM-10 |
| R-004 Secret leakage | — | AP-03 |
| R-005 Lost event | TD-011 | AP-02, AC-54, AC-89 |
| R-007 Known requirement gaps (closed as C-006) | TD-002, TD-003, TD-009 | AC-16, AC-17, AC-25, AC-31 |
| R-008 Data loss | — | AP-04, AC-52 |
| R-010 Documentation drift | — | AC-23, AC-34, AC-38, AP-05 |
| R-011 Public server off | — | Implementation Procedure, R-I04 |
| API abuse and mass bookings | TD-022 | AP-11, AC-79, AC-88, ACO-41 |
| Closed sessions that stay active | TD-024 | AC-44, AC-73 |
| Deployment, rollback, and server versions | — | AC-82, AC-85, AC-86, AC-90, AM-17, ACO-24, ACO-37 |
| Booking and operator schedule | TD-025, TD-026 | AC-45, AC-46, AC-53, AC-91, AC-93, AP-19, ACO-43 |
| Availability of the public server | — | AP-16, AP-17, AP-18 |
| R-001 operations-service integration (closed as C-004) | TD-020 | AM-01 |
| Law 1581 of 2012 | — | AC-26, AC-37, AC-92, ACO-42 |

*Note.* Prepared by the authors from the risk register and the technical backlog of the project.

### Annex D. Instructor Evaluation

The instructor evaluated the system end to end with a cutoff of October 20, 2026 (report of October 21, team 8): **85/100, grade A**. The five main flows (session, booking, payment, operator execution, and notices) were complete. The lowest dimensions were Documentation and traceability (59.0), Process and collaboration (68.4), API and contracts (76.7), and Tests (78.8); the documentation repository was not among those reviewed. Table 16 records the 25 findings and their status as of October 27, 2026, with *main* in production: 11 fixed, 7 partial, 5 open, 1 accepted as a risk, and 1 that cannot be fixed (on October 24 they were 9, 8, 6, 1, and 1; on October 23, 5, 7, 12, 0, and 1).

**Table 16**

*Findings of the Instructor Evaluation and Their Status*

| ID | Instructor finding | Level | Status as of 2026-10-27 | Evidence or action |
|---|---|---|---|---|
| H-01 | *main* does not contain the deliverable work in any of the 10 repositories. | High | Fixed | The 10 repositories have their release on *main*: *v1.1.0* (October 26) in the gateway, security, operations, and notification; *v1.1.1* (October 27) in customer and booking; and *v1.1.2* (October 27) in the web, the mobile app, payment, and infra. Production runs *main* through continuous deployment (AC-90). |
| H-02 | No pull requests or peer review. | Medium | Fixed | *main* protected in the 10 repositories: it only takes changes through a PR approved by another member with a green CI; each release goes in through an approved «Release» PR (those of October 27 of customer, booking, payment, and infra were approved by Brayan Patiño). Protecting *develop* remains (ACO-24). |
| H-03 | The web tests are templates. | High | Fixed | 242 tests in 68 files as of October 27, with the real guard and interceptor, the coupon payment, the refresh, and the translations, and coverage in the CI (web *7edd87f*, *2245758*; AM-18). |
| H-04 | The mobile app has no tests. | High | Partial | 106 tests with *jest-expo* in the *main* CI as of October 27, including the payment (*useClientPayment*), the errors, and the refresh; coverage is not measured yet (AM-18, ACO-26). |
| H-05 | The permission matrix is saved but not applied. | High | Fixed | The screen is read-only (AC-83, web *a4d3715*); applying the matrix remains an improvement (AM-02). |
| H-06 | The default profile is *dev* with demo accounts whose password is published. | Medium | Fixed | AC-58. |
| H-07 | The team guide publishes development credentials. | Medium | Fixed | AC-58 and *lavarapido-infra* *b63f85a* (placeholders in the guide). |
| H-08 | Development SQL Server password in the compose file and the README. | Medium | Fixed | AC-58; the old password is still in the history (ACO-25). |
| H-09 | *google-services.json* committed with the API key. | Low | Open | ACO-25. |
| H-10 | A single database and a single administrator user for the six services. | Medium | Accepted as a risk | Schema per service (Design Documentation, Table 8); the shared *sa* user is an accepted risk in *DEPLOY.md* (*4a865aa*), with the proposed change (AP-08). |
| H-11 | If RabbitMQ does not answer, the event is lost. | Medium | Partial | Broker confirmations and a dead-letter queue in operations, and pending deliveries with retries in notification (AC-78); no Outbox (AP-02, ADR-004). |
| H-12 | *booking.created* is listened to but never published. | Low | Fixed | notification no longer listens to *booking.created* (*8fde2ee*). |
| H-13 | The *e2e* tests do not run in the CI. | Medium | Partial | Remote battery and *e2e.yml* (manual) in *lavarapido-infra* (*573d10b*); payment's 17 integration tests run in the CI since October 27 and the 19 end-to-end tests passed 19 of 19 locally that day, but they still do not run on their own in the CI and the battery has not been run against Azure with the test accounts (ACO-26). |
| H-14 | A *production* build of the app would point to the emulator. | Medium | Partial | *production* uses the HTTPS URL and the app fails with a clear message if the URL is local (*7b5f095*, AC-84); *usesCleartextTraffic* remains (ACO-28). |
| H-15 | Deployment depends on turning the machine on and could not be checked online. | Medium | Partial | Deployment with the health wait, *:prev*, and the protection against downgrading; since October 26, 2026 continuous deployment runs on its own when merging into *main* and checks the web and the API (AM-17, AC-90). Availability is missing: the machine is still turned on on demand and there is no external monitor that warns if the site goes down (UptimeRobot, pending; R-011, AP-16 to AP-18). |
| H-16 | The administrator's reports are calculated in the browser. | Medium | Open | Pending improvement (RF-023 partial); the charts now update when the data arrives (WEB-04). |
| H-17 | The gateway README contradicted the code. | Low | Fixed | Fixed on October 22; README up to date with the limits and the accepted risks on October 24 (*07737a5*). |
| H-18 | The token is stored in *localStorage*. | Low | Open | Mitigated with the script content security policy (CSP) in Caddy and SRI on the flags. |
| H-19 | The web image runs nginx as *root* and without a health check. | Low | Fixed | *nginx-unprivileged* on port 8080 and a health check (AC-86). |
| H-20 | *OperationsService* concentrates 617 lines and five responsibilities. | Low | Open | Technical debt. |
| H-21 | The web commits 8.2 MB of unoptimized images. | Low | Partial | The home and sign-in backgrounds are served as WebP and an unused image was removed (web *a117d7f*, *v1.1.2*); some images are still to be optimized (technical debt). |
| H-22 | No issues or tracking board. | Medium | Open | ACO-30. |
| H-23 | The OpenAPI contract only exists in development. | Low | Partial | OpenAPI contracts in *vehicle-w-docs* (*docs* and *main* branches, in step since November 4, 2026); they are not published per service. |
| H-24 | Cancellation accepts a reason and ignores it. | Low | Fixed | AC-68; the *booking.cancelled* event also carries *reasonCode* (booking *62eea3a*). |
| H-25 | The services were born as single commits of 63 to 142 files. | Low | Cannot be fixed | It is history; since then one commit per logical change is applied (each of the 128 branches of October 24 has one commit). |

*Note.* Prepared by the authors from the instructor's end-to-end evaluation (cutoff of October 20, 2026), the system review of October 23, and the closing audit of October 24, 2026.
