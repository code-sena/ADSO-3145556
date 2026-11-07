# 5.2 Data model

**Project:** Electrical Consumption Monitor — EnergyMonitor
**Record:** 3145556 — Software Analysis and Development (ADSO), SENA CIES Regional Huila
**Version:** 1.0

---

> **Update note (10/27/2026).** This document describes the design of version 1.0, when the backend was the `energy-monitor-backend` modular monolith (Spring Modulith). That design evolved: the seven bounded contexts are now **seven microservices** behind the `en-monitor-api-gateway` API gateway (8080) — security 8082, device 8084, measurement 8085, home 8086, notification 8087, alert 8088 and recommendation 8089 —, communicating through internal REST (`/internal/**`) and RabbitMQ events with a transactional outbox, on the same shared MySQL schema (37 tables). The modular backend was retired. The hexagonal layers, contexts and patterns described here live on inside each service; the current architecture, routes, events and configuration are in the [technical manual](../../08-manuals/en/technical-manual.md) (sections 9 to 13) and the current data model in [`06-base-datos`](data-dictionary.md).


## 1. Purpose

This section defines the data model of the system: how the 29 tables that make it up are structured, which conventions are applied uniformly, how the bounded contexts are related to each other, and which integrity rules guarantee the consistency of the information.

The data model is the most stable artifact of the design: unlike the screens or the endpoints, the tables and their constraints change very infrequently, so this document serves as a contract between the backend, the two front-ends and the data quality process.

## 2. Persistence engine and schema

| **Aspect** | **Definition** |
|---|---|
| Database engine | MySQL 8.4 |
| Schema | `energy_monitor` |
| Collation | `utf8mb4_unicode_ci` |
| Charset | `utf8mb4` |
| Time column type | `TIMESTAMP` |
| Real number format | `DOUBLE` |
| Schema management | **Liquibase** — `src/main/resources/db/changelog` |
| Object-relational mapping | Spring Data JPA + Hibernate 7 |
| Hibernate behavior | `ddl-auto = validate` (validates, never creates) |
| JPA session time zone | UTC (`hibernate.jdbc.time_zone: UTC`) |
| Session visibility | `open-in-view: false` |
| Total tables | **29** |

> **Design decision:** Hibernate operates in `validate` mode. No table is created from the JPA entities; the schema exists solely because a Liquibase *changeset* declared it. This guarantees that the data model documented in this point and the data model actually deployed are always the same.

## 3. Data design conventions

The following conventions are applied uniformly across the 29 tables and were respected throughout the modeling.

### 3.1 Structural conventions

| **Convention** | **Rule applied** | **Justification** |
|---|---|---|
| Table name | `snake_case`, singular or plural according to the semantics of the set | Readability and consistency with the columns |
| Column name | `snake_case` and descriptive (`registration_date`, `daily_limit`, `home_id`) | Self-descriptive, avoids ambiguity |
| Constraint name | `pk_<table>` and `fk_<table>_<column>` | Immediate identification in the changesets |
| Primary key | `VARCHAR(10)` in all entities | Short, uniform and readable identifier |
| Composite primary key | `user_home`, `device_home`, `user_system_role`, `system_role_permission` | Bridge tables that model N:M relationships |
| Foreign key | Only within the same bounded context | See section 4 |
| Index names | `idx_[table]_[column(s)]` → `idx_alert_home_id`, `idx_recommendation_home_id` | Immediate identification of the purpose |

### 3.2 Audit conventions

All tables, without exception, include three audit columns:

| **Column** | **Type** | **Constraint** | **Behavior** |
|---|---|---|---|
| `created_at` | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | Creation date of the record |
| `updated_at` | `TIMESTAMP` | `NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` | Updated automatically on every modification |
| `deleted_at` | `TIMESTAMP` | Nullable | Marks the soft delete of the record |

**Implementation in the code.** Each module defines its own `BaseAuditEntity` superclass (`@MappedSuperclass`) with the `@PrePersist` and `@PreUpdate` *callbacks*. The columns are sealed in Java (`updatable = false`) so that the application is the only source of the creation date.

> **Documented divergence.** The `security`, `home` and `alert` modules seal `created_at` with `@PrePersist`; `device` uses Hibernate's `@CreationTimestamp`/`@UpdateTimestamp`; `notification` additionally uses `LocalDateTime` instead of `Instant`. These are valid local decisions, but they must be unified if audit evidence becomes a quality requirement (see section 10).

### 3.3 Soft delete

**No physical `DELETE` statements are executed by default on any table.** A record is considered deleted when `deleted_at IS NOT NULL`.

| **Benefit** | **Description** |
|---|---|
| Traceability | The history of measurements, alerts and audits is preserved |
| Recovery | A record deleted by mistake can be restored |
| Regulatory compliance | Facilitates the handling of personal data under Law 1581 of 2012 (Habeas Data) |

> **Consequence in the design:** read queries filter by `deleted_at IS NULL`. This is applied explicitly in the repositories, for example `RefreshTokenRepository.findByTokenHashAndDeletedAtIsNull(...)`. The bridge tables with a composite primary key (`user_system_role`, `system_role_permission`) **do not** filter by `deleted_at` in their existence query, precisely so that a previously deleted row can be reactivated when reassigning a role.

### 3.4 Domain types

| **Type** | **Rule** | **Example** |
|---|---|---|
| Boolean | `TINYINT(1)`, mapped with the marker annotation `@TinyIntBoolean` (`@JdbcTypeCode(SqlTypes.TINYINT)`) | `user.email_verified` |
| Enumeration | MySQL `ENUM`, mapped with `@Enumerated(STRING)` | `user.status`, `alert.type` |
| Identifier | `VARCHAR(10)` generated by an `IdentifierGeneratorPort` per module | `SecureIdentifierGeneratorAdapter` |
| Nullable boolean | `Boolean` (object) instead of the primitive `boolean`, when the value can be null | `home_thresholds.use_system_default` |

> **Design detail:** `@TinyIntBoolean` exists because Hibernate 7 would map `boolean` to `bit`, which does not match the `tinyint(1)` of the schema; with `ddl-auto = validate` that discrepancy would prevent startup. The annotation forces the correct correspondence.

## 4. Boundaries between contexts: application references

This is one of the central decisions of the data model and explains the separation between contexts in the entity-relationship diagram.

### 4.1 Rule

> References **within** a bounded context are implemented with SQL `FOREIGN KEY`.
> References **between** bounded contexts are stored as application-level identifiers and **do not generate `FOREIGN KEY` in SQL**.

### 4.2 Application references between contexts

| **Origin** | **Column** | **Destination** | **Destination context** |
|---|---|---|---|
| `User` | `id_user` | `UserHome.user_id` | Home Management |
| `DeviceHome` | `home_id` | `Home.id_home` | Home Management |
| `Measurement` | `device_id` | `Device.id_device` | Devices |
| `Alert` | `home_id` | `Home.id_home` | Home Management |
| `Alert` | `device_id` | `Device.id_device` | Devices |
| `Alert` | `measurement_id` | `Measurement.id_measurement` | Monitoring |
| `Alert` | `consumption_level_id` | `ConsumptionLevel.id_consumption_level` | Monitoring |
| `Recommendation` | `home_id` | `Home.id_home` | Home Management |
| `Recommendation` | `device_id` | `Device.id_device` | Devices |
| `Notification` | `user_id` | `User.id_user` | Security |
| `Notification` | `home_id` | `Home.id_home` | Home Management |
| `Notification` | `source_type` + `source_id` | `Alert`, `Recommendation` or security event | Alerts / Recommendations / Security |
| `PushSubscription` | `user_id` | `User.id_user` | Security |

### 4.3 Justification

| **Advantage** | **Description** |
|---|---|
| Context isolation | A change in the schema of one context does not force the others to be modified |
| Validation in the application | The integrity rules between contexts are applied in the domain layer, where the error message can be understandable |
| Scalability | Avoids locks and expensive joins between tables of different contexts |
| Model readability | The diagram explicitly shows which relationships are strong (solid line) and which are logical (dashed line) |

### 4.4 Design consequence: flat relational mapping

**Across the whole project there is not a single JPA association annotation** (`@OneToMany`, `@ManyToOne`, `@ManyToMany`, `@OneToOne`). Foreign keys are modeled as simple `String`/`Long` attributes, and the bridge tables use `@EmbeddedId` for the composite primary key.

The reason is documented in the code itself (`UserEntity`):

> *"`person_id` is mapped as a plain identifier column rather than a `@ManyToOne`… The same reasoning applies to every other Security foreign key."*

| **Benefit** | **Description** |
|---|---|
| Consistency | The mapping is identical inside and across contexts |
| Query control | Avoids the *N+1* problem and accidental lazy loading, since `open-in-view` is disabled |
| Alignment with the ER model | The model in code matches the documented model exactly |

### 4.5 Design consequence: polymorphic bridge table

`Notification` cannot point to `Alert`, to `Recommendation` or to a security event with a traditional foreign key: they are tables of different contexts and the combination of several `FOREIGN KEY` would be mutually exclusive. The adopted solution is a **polymorphic reference**:

```
source_type ENUM ('ALERT', 'RECOMMENDATION', 'SECURITY')  +  source_id VARCHAR(10)
```

This pair locates the source row without coupling the Notifications context to the schema of the others. The table answers a question that `Alert` and `Recommendation` cannot answer on their own: **whether the message reached the user, through which channel, and whether it was read**.

## 5. Entity-relationship model by context

The base diagram is located at `../diagrams/er-model.wsd` (PlantUML syntax, renderable at <https://www.plantuml.com/plantuml/uml>).

**Figure 1.** *Entity-relationship model: 29 entities grouped into seven bounded contexts.*

> **Version note.** The attached diagram (`MER.wsd`) was produced in an earlier phase of the project and contains **27** entities. The current schema, derived from the Liquibase *changesets*, has **29**. Section 9 documents precisely the difference between the two.

### 5.1 Security Context — 14 tables

Models identity, credentials, sessions, access control and auditing.

| **Table** | **Description** | **Primary key** | **Internal relationships** |
|---|---|---|---|
| `person` | Personal data associated with a user | `id_person` | 1:1 `user` |
| `user` | Credentials, status and access data | `id_user` | 1:1 `person`; 1:1 `user_configuration`; 1:N `password_reset_token`; 1:N `user_session`; 1:N `user_system_role`; 1:N `audit_log`; 1:1 `login_error_log` (optional) |
| `user_configuration` | User configuration preferences | `id_configuration` | 1:1 `user` |
| `password_reset_token` | Password recovery tokens | `id_reset_token` | N:1 `user` |
| `user_session` | Active and renewable sessions | `id_user_session` | N:1 `user`; 1:N `refresh_token` |
| `refresh_token` | Refresh token generations (SHA-256 hash) | `id_refresh_token` | N:1 `user_session`; 0..1:1 `refresh_token` (`parent_id`) |
| `security_configuration` | Configurable security parameters | `id_security_configuration` | No relationships |
| `password_policy` | Password validation and expiration rules | `id_password_policy` | No relationships |
| `login_error_log` | Errors produced during sign-in | `id_login_error` | N:1 `user` (optional) |
| `system_role` | Roles available in the system | `id_system_role` | 1:N `user_system_role`; 1:N `system_role_permission` |
| `user_system_role` | User–role relationship | `user_id` + `system_role_id` | N:1 `user`; N:1 `system_role` |
| `permission` | Functional permissions (`METER.READ`, `ALERT.READ`…) | `id_permission` | 1:N `system_role_permission` |
| `system_role_permission` | Role–permission relationship | `system_role_id` + `permission_id` | N:1 `system_role`; N:1 `permission` |
| `audit_log` | Relevant actions performed by users | `id_audit_log` | N:1 `user` (optional) |

**Refresh token rotation model.** `refresh_token` is the most elaborate table of the security model. Each sign-in creates a **family** of tokens identified by `family_id`. Each renewal generates a new row whose `parent_id` points to the row it replaces. When an already rotated row is reused, the reuse (*replay*) is detected and the entire family is revoked with `revoked_reason = 'REFRESH_TOKEN_REUSE'`.

```
  Generation 1 (parent_id = NULL)   status = ROTATED  ← reused
  └─ Generation 2 (parent_id = 1)    status = ROTATED
     └─ Generation 3 (parent_id = 2) status = ACTIVE    ← current
  family_id = same value in the three rows
```

Only the `token_hash` is stored (SHA-256, 64 characters). The secret is **never** saved in clear text.

**Security decisions incorporated into the schema:**

| **Decision** | **Implementation** | **Requirement** |
|---|---|---|
| Attempt counter per token | `password_reset_token.attempts INT NOT NULL` | RNF-SEG-05, RNF-SEG-06 |
| Google link | `user.google_subject VARCHAR(255)` | RF-SEC-05 |
| Avatar as full image | `user.profile_image` `MEDIUMTEXT` (data URL in base64) | RF-SEC-08 |

### 5.2 Home Management Context — 4 tables

| **Table** | **Description** | **Primary key** | **Internal relationships** |
|---|---|---|---|
| `home_type` | Catalog of home types | `id_home_type` | 1:N `home` |
| `home` | Homes managed by the system | `id_home` | N:1 `home_type`; 1:1 `home_thresholds`; 1:N `user_home`; 1:N `device_home`; 1:N `alert`; 1:N `recommendation` |
| `home_thresholds` | Consumption limits of a home | `id_threshold` | 1:1 `home` (`home_id` is `UNIQUE`) |
| `user_home` | User–home relationship with role and favorite | `user_id` + `home_id` | N:1 `user` *(application ref.)*; N:1 `home` |

**Highlighted design — `access_code`.** The field `access_code VARCHAR(8) UNIQUE` is the member linking mechanism: the owner shares this code and whoever enters it joins the home with the role `MEMBER`. No invitations are sent by email, which simplifies the graphic interface flow and avoids the need for an invitations table.

**Highlighted design — per-home thresholds and active period.** `home_thresholds` implements a strict 1:1 relationship with `home` (`home_id` is `UNIQUE` and `NOT NULL`). The column `limit_period ENUM('DAILY','MONTHLY')` indicates which of the two limits is being edited in the interface. The default value of the column is `'DAILY'`.

**Resolution of the `use_system_default` gap.** The boolean flag `use_system_default` is indeed supported: the `home` module declares `HomeProperties` (`home.defaults`) with `dailyLimit = 6.0` kWh and `monthlyLimit = 180.0` kWh, exposed through the `SystemDefaultsPort` port and its `SystemDefaultsAdapter` adapter. The `POST /api/v1/homes/{homeId}/thresholds/defaults` endpoint allows restoring those values.

### 5.3 Devices Context — 4 tables

| **Table** | **Description** | **Primary key** | **Internal relationships** |
|---|---|---|---|
| `appliance_type` | Catalog of appliance types | `id_appliance_type` | 1:N `device` |
| `device` | Installed measuring devices | `id_device` | N:1 `appliance_type`; 1:N `device_status_log`; 1:N `device_home`; 1:N `measurement`; 1:N `alert`; 1:N `recommendation` |
| `device_status_log` | Status and connectivity of the devices | `id_device_status` | N:1 `device` |
| `device_home` | Device–home relationship | `device_id` + `home_id` | N:1 `device`; N:1 `home` *(application ref.)* |

**Highlighted design — `device_code` and `api_key`.** `device_code VARCHAR(6) UNIQUE` is the short identifier that the device (ESP32) declares when it links; `api_key VARCHAR(100)` is the integration credential. Both satisfy requirement RF-DEV-02 (secure device linking).

**Highlighted design — `device_status_log` with history.** Instead of a single status field in `device`, status and connectivity are recorded in an append-only log. This allows determining the last contact (`last_seen`) and the signal strength at the moment of the reading.

**Implementation detail.** `device_status_log.device_id` is **nullable** in the current schema, which allows keeping the historical trace of a device unlinked from the home without breaking referential integrity.

### 5.4 Monitoring and Measurements Context — 2 tables

| **Table** | **Description** | **Primary key** | **Internal relationships** |
|---|---|---|---|
| `measurement` | Recorded electrical measurements | `id_measurement` | N:1 `device` *(application ref.)* |
| `consumption_level` | Risk levels and consumption ranges | `id_consumption_level` | 1:N `alert` |

**Fields of `measurement`:**

| **Field** | **Type** | **Physical meaning** |
|---|---|---|
| `date_time` | `TIMESTAMP` | Instant of the reading |
| `voltage` | `DOUBLE` | Voltage (V) |
| `current` | `DOUBLE` | Current (A) |
| `active_power` | `DOUBLE` | Active power (W) |
| `stored_energy` | `DOUBLE` | Accumulated energy (kWh) |

> **Design decision:** **there is no independent "consumption history" entity**. History is the set of all `measurement` rows filtered by `device_id`, date range and time zone. This eliminates data duplication and guarantees that the history charts and the statistics consume exactly the same source of truth.

**Highlighted design — `consumption_level` as a range table.** Instead of having the thresholds hardcoded in the code, the four risk levels (`LOW`, `MEDIUM`, `HIGH`, `CRITICAL`) are stored as rows with `min_limit` and `max_limit`. This allows adjusting the risk scale through a *changeset* without touching code, and enables recommendations to be generated over configurable ranges. The table is seeded with a dedicated *changeset* (`003-seed-consumption-level`).

### 5.5 Alerts Context — 1 table

| **Table** | **Description** | **Primary key** | **Internal relationships** |
|---|---|---|---|
| `alert` | Threshold, limit and connectivity alerts | `id_alert` | N:1 `home`; N:1 `device`; N:1 `consumption_level`; N:1 `measurement` — all *(application ref.)* |

**Defined index:** `idx_alert_home_id (home_id)`.

**Alert taxonomy.** The current schema recognizes four types:

| **Type** | **Origin** | **`consumption_level_id`** | **`measurement_id`** |
|---|---|---|---|
| `THRESHOLD` | The measurement falls into a risk range | Mandatory | Mandatory |
| `LIMIT` | Accumulated consumption exceeds the configured threshold | `NULL` | Mandatory |
| `CONNECTIVITY` | The device stops reporting | `NULL` | `NULL` |
| `DEVICE` | Linking or reissuing of device credentials | `NULL` | `NULL` |

### 5.6 Recommendations Context — 1 table

| **Table** | **Description** | **Primary key** | **Internal relationships** |
|---|---|---|---|
| `recommendation` | Recommendations for homes and devices | `id_recommendation` | N:1 `home`; N:1 `device` — both *(application ref.)* |

**Integrity rules:**

- `device_id` is optional: `NULL` when the recommendation applies to the whole home; `NOT NULL` when it refers to a specific device.
- Recommendations are generated with `status = 'UNREAD'`; the user toggles between `READ` and `UNREAD`.
- A recommendation can originate from the analysis of **multiple** measurements, so it **does not** store direct references to `measurement`. That decision avoids a bridge table and keeps the model flexible with respect to the analysis algorithms.
- Defined index: `idx_recommendation_home_id (home_id)`.

**Generation periodicity.** The `recommendation` module incorporates `RecommendationScheduler`, which evaluates all homes every hour (`recommendation.evaluate-interval: PT1H`, initial offset `PT2M`) with a cooldown period of 7 days per recommendation (`recommendation.cool-down: P7D`) so as not to repeat the same advice.

### 5.7 Notifications Context — 3 tables

| **Table** | **Description** | **Primary key** |
|---|---|---|
| `notification` | Record of the delivery of a message to the user | `id_notification` |
| `notification_preference` | Channel preferences per user | `user_id` |
| `push_subscription` | Browser push subscriptions | `id_push_subscription` |

**`notification_preference` — design decision.** The `user_id` is the **primary key** of the table. This guarantees by construction that a user has at most one preferences row: the "one to one" uniqueness does not depend on an additional unique index, but on the model itself.

**`push_subscription` — design decision.** The `endpoint VARCHAR(500)` is `UNIQUE`, because the same browser generates a different endpoint per application and per subscription. The keys `p256dh` and `auth` complete the VAPID subscription.

**Independent state cycles.** The `notification` table separates two concepts that are frequently confused:

| **Cycle** | **Columns** | **States** |
|---|---|---|
| **Delivery** | `delivery_status` | `PENDING` → `SENT` \| `FAILED` |
| **Read** | `read_at` | `NULL` while it has not been read |

> **A delivered alert is not a resolved alert.** `notification.read_at` is orthogonal to `alert.alert_status` (`PENDING`/`RESOLVED`) and to `delivery_status`. This separation of responsibilities is a deliberate design decision and must be maintained in future iterations.

**Security decision: the message body is never stored.** The table has no column whatsoever with the text of the email or of the notification. The reason is documented in the `notification` module itself:

> *«A password recovery secret travels in that body, so a column holding it in clear text would undo the decision to keep only a hash elsewhere. Only the recipient, the type, the subject and the outcome are kept.»*

That is: if the password recovery code were stored in clear text in the notifications table, the benefit of keeping only its hash in `password_reset_token` would be lost. Only the recipient, the type, the subject (`message_key`) and the delivery result are kept.

**Channel selection.** The channel is determined according to the preferences of the recipient in `notification_preference`:

| **Preference** | **Assigned channel** |
|---|---|
| `email_enabled = TRUE` | `EMAIL` |
| `push_enabled = TRUE` | `PUSH` |

**Abandoned delivery sweeper.** `AbandonedDeliverySweeper` closes the `PENDING` rows older than `app.mail.pending-threshold` (30 minutes) and runs every `app.mail.sweep-delay` (5 minutes) and also at application startup.

## 6. Summary of the 29 tables

| **#** | **Table** | **Context** | **Functional domain** |
|---|---|---|---|
| 1 | `person` | Security | Identity |
| 2 | `user` | Security | Identity |
| 3 | `user_configuration` | Security | Preferences |
| 4 | `password_reset_token` | Security | Password recovery |
| 5 | `user_session` | Security | Sessions |
| 6 | `refresh_token` | Security | Sessions |
| 7 | `security_configuration` | Security | Configuration |
| 8 | `password_policy` | Security | Passwords |
| 9 | `login_error_log` | Security | Auditing |
| 10 | `system_role` | Security | Access control |
| 11 | `user_system_role` | Security | Access control |
| 12 | `permission` | Security | Access control |
| 13 | `system_role_permission` | Security | Access control |
| 14 | `audit_log` | Security | Auditing |
| 15 | `home_type` | Home Management | Homes |
| 16 | `home` | Home Management | Homes |
| 17 | `home_thresholds` | Home Management | Thresholds |
| 18 | `user_home` | Home Management | Members |
| 19 | `appliance_type` | Devices | Devices |
| 20 | `device` | Devices | Devices |
| 21 | `device_status_log` | Devices | Connectivity |
| 22 | `device_home` | Devices | Devices |
| 23 | `measurement` | Monitoring | Measurements |
| 24 | `consumption_level` | Monitoring | Risk levels |
| 25 | `alert` | Alerts | Alerts |
| 26 | `recommendation` | Recommendations | Recommendations |
| 27 | `notification` | Notifications | Notifications |
| 28 | `notification_preference` | Notifications | Channel preferences |
| 29 | `push_subscription` | Notifications | Push subscriptions |

## 7. Schema management with Liquibase

The schema is built exclusively through versioned *changesets*. The master chain is `db/changelog/db.changelog-master.xml`, which includes seven files, one per context, **in dependency order**.

```
db.changelog-master.xml
├── security/security-changelog.xml               (24 changesets)
├── home/home-changelog.xml                       (6)
├── device/device-changelog.xml                   (6)
├── measurement/measurement-changelog.xml         (3)
├── alert/alert-changelog.xml                     (3)
├── recommendation/recommendation-changelog.xml   (1)
└── notification/notification-changelog.xml       (3)
```

**Naming convention:** `{module}-{NNN}-{description}.xml`, stored in the `changes/` subfolder.

**Siemens changesets (data seed).** Three *changesets* seed catalogs and are declared with `preConditions/tableIsEmpty` and a `<rollback>` block so that they are reversible if the table is empty:

| **Changeset** | **Seed** |
|---|---|
| `security-019-seed-default-password-policy` | Default password policy (min. 8, max. 64, uppercase, numbers, symbols, 90 days) |
| `home-005-seed-home-type` | Home types (house, apartment, office) |
| `device-006-seed-appliance-types` | Appliance types (refrigerator, washing machine, television, microwave, air conditioner, computer equipment, water heater, lighting, other) |
| `measurement-003-seed-consumption-level` | Risk levels with their ranges |

**Maintenance rule:** already applied changesets are **never edited**. Every modification of the schema is performed through a new *changeset* with a sequential identifier.

## 8. Integrity rules

| **Category** | **Rule** | **Enforcement mechanism** |
|---|---|---|
| Email uniqueness | `user.email` is `UNIQUE` | `409 Conflict` — `EmailAlreadyRegisteredException` |
| Access code uniqueness | `home.access_code` is `UNIQUE` | `409 Conflict` — `HomeAccessCodeCollisionException` |
| Device code uniqueness | `device.device_code` is `UNIQUE` | Validation in the `device` module |
| Token hash uniqueness | `refresh_token.token_hash` is `UNIQUE` | Reuse detection |
| Push endpoint uniqueness | `push_subscription.endpoint` is `UNIQUE` | Idempotent re-subscription |
| Preferences uniqueness | `notification_preference.user_id` **is** the PK | Guarantee by model |
| Avatar uniqueness | `user.profile_image` belongs to the account, not to the person | `person` does not store an image |
| Address uniqueness | `home.address` is the authoritative copy | `person` does not store an address |
| Positive threshold | `daily_limit` and `monthly_limit` are `DOUBLE NOT NULL` | `@PositiveOrZero` in the DTO |
| Threshold consistency | The limit of the active period must be positive and consistent | Validation in `UpdateHomeThresholds` |
| Threshold period | `limit_period ∈ {DAILY, MONTHLY}` | MySQL `ENUM` |
| Single-use token | `password_reset_token.used` | `markAsUsed()` in the domain entity |
| Token expiration | `expiration_at` | `isExpired()` in the domain entity |
| Attempt limit | `password_reset_token.attempts` and in-memory limiter (10 per IP / 15 min) | `429` with `Retry-After: 900` |
| Levels without overlap | The `[min_limit, max_limit]` ranges of `consumption_level` do not overlap | Validation in the `measurement` module |
| Home membership | A user only accesses the homes where they appear in `user_home` | `HomeApi` + `CurrentUserResolver` |
| Device read authorization | The user must be a member of the home to which the device belongs | `HomeConsumptionUseCase.requireReadable(...)` |
| Last owner | The last `OWNER` of a home cannot leave; they must first transfer ownership or delete the home | `409 Conflict` — `LastOwnerCannotLeaveException` |
| Single owner | A home always has exactly one `OWNER`; ownership transfer swaps the roles in a single step (`HOME-INV-001`, `HOME-INV-015`) with a row lock | `PUT /api/v1/homes/{homeId}/owner`: `409` if the target is already `OWNER` or is the caller |
| Home deletion | Only the `OWNER` can delete the home; the soft delete (`deleted_at`) covers the home, its `user_home` rows and `home_thresholds`, and unlinks the devices | `DELETE /api/v1/homes/{homeId}`: `403` for a `MEMBER` |
| Device appropriation | Only an `OWNER` can link, edit, reissue credentials or unlink | `NotHomeOwnerException` |
| Query at read time | The IP is taken from `getRemoteAddr()`; `X-Forwarded-For` is ignored | `clientIp(HttpServletRequest)` |

## 9. Differences between the design artifact and the current schema

The diagram `../diagrams/er-model.wsd` delivered as the design artifact of point 03 was produced in an earlier phase. The following table records the difference so that both documents can coexist without contradiction.

| **#** | **Aspect** | **ER model of the artifact** | **Current schema** | **Origin of the change** |
|---|---|---|---|---|
| 1 | Number of tables | 27 | 29 | `notification-002`, `notification-003` |
| 2 | `notification_preference` | Does not exist | Channel preferences, PK = `user_id` | Need to separate preferences from `user_configuration` |
| 3 | `push_subscription` | Does not exist | Per-browser VAPID subscriptions | Web push notifications (Web Push) |
| 4 | `alert.type` | `THRESHOLD`, `CONNECTIVITY` | + `DEVICE`, `LIMIT` | `alert-002`, `alert-003` |
| 5 | `notification.source_type` | `ALERT`, `RECOMMENDATION` | + `SECURITY` | Verification and recovery emails |
| 6 | `home_thresholds` | 3 limit columns | + `limit_period ENUM('DAILY','MONTHLY')` | `home-006` — editing by period |
| 7 | `user` | — | + `google_subject` | `security-024` — Google access |
| 8 | `password_reset_token` | `reset_token` | `reset_token_hash` + `attempts` | `security-021` — never store the secret in clear |
| 9 | `user.profile_image` | `VARCHAR(255)` | `MEDIUMTEXT` | `security-022` — avatar as data URL |
| 10 | `person.last_name` | `NOT NULL` | Optional | `security-023` — users with a single name |
| 11 | `person` | With `address` and `cellphone` | Both removed | `security-018`, `security-020` |
| 12 | Threshold defaults | Undefined | `HomeProperties` (6.0 / 180.0) | Closes the gap reported in the previous version |
| 13 | Measurement ingestion | Endpoint `POST /measurements` | MQTT (`energy-monitor/devices/+/telemetry`) | Replaces writing over HTTP |

**Recommendation:** update `MER.wsd` in the documentation repository to incorporate rows 1 to 13 before the final delivery.

## 10. Pending design decisions

| **#** | **Finding** | **Impact** | **Proposed decision** |
|---|---|---|---|
| 1 | `notification.source_id` has no foreign key by design (polymorphic reference). | An alert or recommendation that is soft-deleted leaves orphan notifications. | Accept the behavior: the soft delete of the source preserves the traceability of the notice. |
| 2 | A measurement can generate several alerts of the same type if it crosses more than one threshold. | Duplicate alerts in the inbox. | Define the deduplication rule by `(home_id, type, measurement_id, date_time)` in `RegisterThresholdAlertService`. |
| 3 | Timestamps are sealed in three different ways (`@PrePersist`, `@CreationTimestamp`, `LocalDateTime`). | Ambiguity when building audit evidence. | Unify into `Instant` + `@PrePersist` across the seven modules. |
| 4 | `security_configuration` is a generic name/value pair with no seeded data. | There is no effective parameterization of security from the database. | Seed a *changeset* with the current parameters (token TTL, access key length, attempt limit) or drop the table. |
| 5 | `person` has no contact method other than email. | If the requirements specification required SMS alerts or phone, the model does not support it. | Confirm with the requirements team before delivery. |

## 11. Traceability with the requirements

| **Requirement (requirements specification)** | **Supporting tables** | **Design mechanism** |
|---|---|---|
| RF-SEC-01 to RF-SEC-12 User and profile management | `person`, `user`, `user_configuration`, `password_reset_token`, `user_session`, `refresh_token`, `audit_log` | person/account separation; verification by code; token rotation; `google_subject` |
| RF-HOM-01 to RF-HOM-09 Household management | `home`, `home_type`, `user_home`, `device`, `device_home` | `access_code` as linking mechanism; dual RBAC role + per-home role |
| RF-HOM-08, RF-NOT-03, RF-CLI-05, RF-CLI-06 Configuration | `home_thresholds`, `user_configuration`, `notification_preference`, `security_configuration`, `password_policy` | Separation of user preferences from business limits |
| RF-MED, RF-ALE, RF-REC, RF-NOT Consumption, alerts and recommendations | `measurement`, `consumption_level`, `alert`, `recommendation`, `notification` | Separate events per context; risk levels as data |
| RF-DEV-01 to RF-DEV-09; RF-MED-04 to RF-MED-06 Device | `device`, `device_code`, `api_key`, `device_status_log` | Authentication via `api_key`; connectivity log |

## 12. Related artifacts

| **Artifact** | **Location** |
|---|---|
| Base ER model (PlantUML) | `../diagrams/er-model.wsd` |
| Class diagram (PlantUML) | `../diagrams/class-diagram.wsd` — documented in `05.3-class-model.md` |
| Liquibase changesets | `energy-monitor-backend/src/main/resources/db/changelog/` |
| Complete data dictionary | `06-base-datos/diccionario-datos/` |
| Database scripts | `06-base-datos/script-DB/` |