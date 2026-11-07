# Data Models per Service

> Save Your Water — SQL Server (`sy_water_db`).
> Authoritative source: `sy-water-db/cuida-tu-agua-model-v5.dbml` and the per-service migration repos (`sy-water-db/ms-iam-db`, `sy-water-db/ms-places-db`).
> All bounded contexts share one database, separated by schema for logical organization.
> Cross-schema FK are real — SQL Server enforces referential integrity.

---

## As-built status (2026-10-10)

This document is the **target model** (v5). The database actually created by the Liquibase repositories
differs as follows; the per-service `data-model.md` files in `09-microservices/services/` describe the
real tables, and `08-uml/diagrams/exports/ER-0*.png` draws them.

| Schema (this document) | As built | Differences |
|------------------------|----------|-------------|
| `security` | `security` (ms-iam) | Adds `roles`, `user_roles` (USER/ADMIN) and `action_codes` (valve close confirmation) |
| `places` | `places` (ms-places) | Also holds `place_tariffs`, `tariff_catalog`, `tips`, `tip_favorites`; `place_members` and `invitations` not created yet |
| `devices` | `devices` (ms-devices) | As designed |
| `monitoring` | `monitoring` (ms-consumption) | Only `readings` and `consumption_hourly`; day/week/month are computed on demand |
| `valve` | `valve` (ms-valve) | `valve_states`, `valve_commands`; `valve_rules` not created |
| `notifications` | `notification` (ms-notifications, singular) | `notifications`, `notification_reads`, `notification_preferences`, `place_devices`, `email_outbox`, `push_tokens` |
| `alerts` | — | Not created; device alerts are produced by ms-notifications |
| `goals`, `billing`, `content` | — | Not created; tariffs and tips live in `places` |

Total as built: 6 schemas, 31 tables.

---

## Data modeling principles

### 1. Schema-per-BC (shared database)

All bounded contexts share a single SQL Server instance (`sy_water_db`). Each BC owns its own schema — **10 schemas** for 11 BCs (BC-10 Reports has no tables, reads from `monitoring`).

```
✓ auth-service  → security.users
✓ place-service → places.places
✗ place-service → security.users (direct JOIN) — use FK reference + API
```

Cross-schema foreign keys ARE used for referential integrity. Data access across services is still via REST or events at the application level, but the database enforces FK constraints.

### 2. PK strategy: single UUID (ADR-003)

Every table has **one** identifier, `id UNIQUEIDENTIFIER`. It is both the primary key and the public ID exposed in REST, JWT and events. There is no `BIGINT IDENTITY` column and no separate `uuid` column. All FKs, same-schema and cross-schema, are `UNIQUEIDENTIFIER`.

```sql
id UNIQUEIDENTIFIER NOT NULL CONSTRAINT DF_<table>_id DEFAULT (NEWID()),
-- ...
CONSTRAINT PK_<table> PRIMARY KEY (id)
```

- The backend may supply the `id` (generated in the domain); otherwise SQL Server generates it.
- High-volume append-only tables (`monitoring.readings`, `notifications.notification_log`) use `DEFAULT (NEWSEQUENTIALID())` to avoid page splits. The application does not supply their `id`.
- The DDL in this document is abbreviated (inline `DEFAULT` / `PRIMARY KEY`). The migrations in `sy-water-db` name every constraint (`PK_`, `DF_`, `FK_`, `CK_`), and FKs are added in separate `ALTER TABLE` changesets.

### 3. Timestamps

All tables use `DATETIME2` with `SYSUTCDATETIME()` (UTC). No timezone offset stored — application layer handles timezone conversion.

### 4. Soft delete

Only `places.places` uses soft delete (`deleted_at`). Other tables use hard delete or status-based lifecycle (e.g., `revoked_at` for tokens, `unlinked_at` for device history).

### 5. Naming conventions

```sql
-- Schemas:     lowercase, plural where natural     → security, places, devices
-- Tables:      snake_case, plural                  → users, readings, valve_commands
-- Columns:     snake_case                          → flow_rate_lph, last_report_at
-- FKs:         [referenced_table_singular]_id      → user_id, place_id
-- Indexes:     IX_[abbrev]_[columns]               → IX_reading_place_time
-- Unique:      UQ_[abbrev]_[columns]               → UQ_cred_user_type
-- Filtered UQ: UX_[abbrev]_[columns]               → UX_inv_pending
-- Timestamps:  DATETIME2 with SYSUTCDATETIME()
-- IDs:         id UNIQUEIDENTIFIER (single PK, also public) — ADR-003
```

### 6. Documented denormalizations (3NF violations)

| Code | Table | Column | Reason |
|------|-------|--------|--------|
| D1 | monitoring.readings | place_id | Avoid JOIN to devices on every reading query |
| D2 | valve.valve_commands | place_id | Command belongs to the place, not just the device |
| D3 | alerts.alert_events | severity | Snapshot of classification at alert creation time |
| D4 | places.places | owner_id | List "my places" without JOIN to place_members |

---

## Schema: security — BC-01 IAM (Supporting)

**Owner:** auth-service (Spring Boot)

### Table: security.users

```sql
CREATE TABLE security.users (
    id                   UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    name                 NVARCHAR(200)    NOT NULL,
    email                NVARCHAR(320)    NOT NULL,
    phone                NVARCHAR(20)     NOT NULL,
    phone_verified       BIT              NOT NULL DEFAULT 0,
    avatar_url           NVARCHAR(500)    NULL,
    email_verified       BIT              NOT NULL DEFAULT 0,
    account_locked_until DATETIME2        NULL,
    created_at           DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),
    updated_at           DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_users_email UNIQUE (email)
);
```

### Table: security.user_credentials

```sql
CREATE TABLE security.user_credentials (
    id              UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    user_id         UNIQUEIDENTIFIER NOT NULL REFERENCES security.users(id),
    credential_type NVARCHAR(10)     NOT NULL
                    CONSTRAINT CK_cred_type CHECK (credential_type IN ('LOCAL', 'GOOGLE')),
    password_hash   NVARCHAR(256)    NULL,
    google_id       NVARCHAR(256)    NULL,
    created_at      DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_cred_user_type UNIQUE (user_id, credential_type)
);
```

### Table: security.email_verifications

```sql
CREATE TABLE security.email_verifications (
    id          UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    user_id     UNIQUEIDENTIFIER NOT NULL REFERENCES security.users(id),
    token_hash  NVARCHAR(256)    NOT NULL,
    expires_at  DATETIME2        NOT NULL,
    verified_at DATETIME2        NULL,
    created_at  DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_emailver_token UNIQUE (token_hash)
);
```

### Table: security.password_resets

```sql
CREATE TABLE security.password_resets (
    id         UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    user_id    UNIQUEIDENTIFIER NOT NULL REFERENCES security.users(id),
    token_hash NVARCHAR(256)    NOT NULL,
    expires_at DATETIME2        NOT NULL,
    used_at    DATETIME2        NULL,
    created_at DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_pwdreset_token UNIQUE (token_hash)
);
```

### Table: security.refresh_tokens

```sql
CREATE TABLE security.refresh_tokens (
    id          UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    user_id     UNIQUEIDENTIFIER NOT NULL REFERENCES security.users(id),
    token_hash  NVARCHAR(256)    NOT NULL,
    device_info NVARCHAR(500)    NULL,
    expires_at  DATETIME2        NOT NULL,
    revoked_at  DATETIME2        NULL,
    created_at  DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_rtoken_hash UNIQUE (token_hash)
);

CREATE INDEX IX_rtoken_user ON security.refresh_tokens (user_id);
```

### Table: security.login_attempts

```sql
CREATE TABLE security.login_attempts (
    id             UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    user_id        UNIQUEIDENTIFIER NULL REFERENCES security.users(id),
    email          NVARCHAR(320)    NOT NULL,
    ip_address     NVARCHAR(45)     NOT NULL,
    success        BIT              NOT NULL,
    failure_reason NVARCHAR(50)     NULL,
    attempted_at   DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    INDEX IX_login_user_time (user_id, attempted_at)
);
```

### Table: security.activity_log

```sql
CREATE TABLE security.activity_log (
    id          UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    user_id     UNIQUEIDENTIFIER NOT NULL REFERENCES security.users(id),
    action      NVARCHAR(50)     NOT NULL,
    entity_type NVARCHAR(50)     NULL,
    entity_id   NVARCHAR(100)    NULL,
    metadata    NVARCHAR(4000)   NULL,
    ip_address  NVARCHAR(45)     NULL,
    created_at  DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    INDEX IX_actlog_user_time (user_id, created_at),
    CONSTRAINT CK_actlog_json CHECK (metadata IS NULL OR ISJSON(metadata) = 1)
);
```

---

## Schema: places — BC-02 Place Management (Supporting)

**Owner:** place-service (.NET 8)

### Location catalog (ADR-004)

The market is limited to Colombia and Ecuador (ADR-004). A place's location is a reference to `places.cities`; country and subdivision are reached through `city → subdivision → country`.

| Level | Table | Colombia (CO) | Ecuador (EC) |
|-------|-------|---------------|--------------|
| Country | `places.countries` | Colombia — COP, LITERS | Ecuador — USD, LITERS |
| Subdivision | `places.subdivisions` | Departamento — 33 rows, DANE 2-digit code | Provincia — 24 rows, INEC 2-digit code |
| City | `places.cities` | Municipio — 1,122 rows, DANE 5-digit code | Cantón — 221 rows, INEC 4-digit code |

The catalogs are reference data. Migrations seed them in every environment (`02-dml/00-inserts/001..004` in `ms-places-db`), and the application role `places_rw` has `SELECT` only on them (`INSERT`/`UPDATE`/`DELETE` denied).

#### Table: places.countries

```sql
CREATE TABLE places.countries (
    id               UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    code             NVARCHAR(2)      NOT NULL, -- ISO 3166-1 alpha-2: CO, EC
    name             NVARCHAR(100)    NOT NULL,
    default_currency NVARCHAR(3)      NOT NULL, -- ISO 4217: CO → COP, EC → USD
    default_unit     NVARCHAR(15)     NOT NULL
                     CONSTRAINT CK_countries_unit CHECK (default_unit IN ('LITERS', 'CUBIC_METERS', 'GALLONS')),

    CONSTRAINT UQ_countries_code UNIQUE (code)
);
```

**Seed data:**

| code | name | default_currency | default_unit |
|------|------|------------------|--------------|
| CO | Colombia | COP | LITERS |
| EC | Ecuador | USD | LITERS |

#### Table: places.subdivisions

First-level administrative division: *departamento* in CO, *provincia* in EC.

```sql
CREATE TABLE places.subdivisions (
    id         UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    country_id UNIQUEIDENTIFIER NOT NULL REFERENCES places.countries(id),
    code       NVARCHAR(2)      NOT NULL, -- DANE (CO) / INEC (EC) code, unique per country only
    name       NVARCHAR(100)    NOT NULL,

    CONSTRAINT UQ_subdivisions_country_code UNIQUE (country_id, code)
);
```

#### Table: places.cities

*Municipio* in CO (DANE DIVIPOLA, 5 digits, includes non-municipalized areas) and *cantón* in EC (INEC DPA, 4 digits, zone 90 excluded). The code starts with the subdivision code (e.g. `05001` Medellín → `05` Antioquia).

```sql
CREATE TABLE places.cities (
    id             UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    subdivision_id UNIQUEIDENTIFIER NOT NULL REFERENCES places.subdivisions(id),
    code           NVARCHAR(5)      NOT NULL,
    name           NVARCHAR(100)    NOT NULL,

    CONSTRAINT UQ_cities_subdivision_code UNIQUE (subdivision_id, code)
);
```

### Table: places.places

```sql
CREATE TABLE places.places (
    id               UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    owner_id         UNIQUEIDENTIFIER NOT NULL, -- FK cross → security.users.id [D4]
    city_id          UNIQUEIDENTIFIER NOT NULL REFERENCES places.cities(id),
    name             NVARCHAR(200)    NOT NULL,
    place_type       NVARCHAR(15)     NOT NULL
                     CONSTRAINT CK_places_type CHECK (place_type IN ('RESIDENTIAL', 'COMMERCIAL')),
    address          NVARCHAR(500)    NOT NULL,
    currency         NVARCHAR(3)      NOT NULL -- initialized from countries.default_currency
                     CONSTRAINT CK_places_currency CHECK (currency IN ('COP', 'USD')),
    measurement_unit NVARCHAR(15)     NOT NULL DEFAULT 'LITERS'
                     CONSTRAINT CK_places_unit CHECK (measurement_unit IN ('LITERS', 'CUBIC_METERS', 'GALLONS')),
    is_default       BIT              NOT NULL DEFAULT 0,
    created_at       DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),
    updated_at       DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),
    deleted_at       DATETIME2        NULL
);

CREATE INDEX        IX_places_owner         ON places.places (owner_id);
-- At most one active default place per owner (HU-009/010)
CREATE UNIQUE INDEX UX_places_owner_default ON places.places (owner_id) WHERE is_default = 1 AND deleted_at IS NULL;
```

- **`city_id` replaces the free-text `city` and `country_code` columns** (ADR-004). Storing the country as well would be a transitive dependency (`city_id → subdivision_id → country_id`), so it is derived with a JOIN inside the `places` schema.
- **`currency` and `measurement_unit` belong to the place.** They are copied from the country's defaults when the place is created (HU-053), and the user can change them afterwards. That is why they are not a 3NF violation: they don't depend on `city_id`.
- **`FK_places_owner` has no `ON DELETE CASCADE`.** Account deletion (HU-008) is a soft delete, and a physical cascade would remove places without warning.

### Table: places.place_members *(Entrega 3)*

```sql
CREATE TABLE places.place_members (
    id         UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    place_id   UNIQUEIDENTIFIER NOT NULL REFERENCES places.places(id),
    user_id    UNIQUEIDENTIFIER NOT NULL, -- FK cross → security.users.id
    role       NVARCHAR(10)     NOT NULL
               CONSTRAINT CK_member_role CHECK (role IN ('OWNER', 'GUEST')),
    joined_at  DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),
    revoked_at DATETIME2        NULL,

    CONSTRAINT UQ_member_place_user UNIQUE (place_id, user_id)
);
```

> **Nota de dominio:** El valor `GUEST` en la BD corresponde al rol "Miembro (Member)" en el lenguaje ubicuo del dominio (ver `01-context/glossary.md`).

### Table: places.invitations *(Entrega 3)*

```sql
CREATE TABLE places.invitations (
    id           UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    place_id     UNIQUEIDENTIFIER NOT NULL REFERENCES places.places(id),
    invited_by   UNIQUEIDENTIFIER NOT NULL, -- FK cross → security.users.id
    email        NVARCHAR(320)    NOT NULL,
    status       NVARCHAR(10)     NOT NULL DEFAULT 'PENDING'
                 CONSTRAINT CK_inv_status CHECK (status IN ('PENDING', 'ACCEPTED', 'EXPIRED', 'REJECTED')),
    token_hash   NVARCHAR(256)    NOT NULL,
    expires_at   DATETIME2        NOT NULL,
    created_at   DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),
    responded_at DATETIME2        NULL,

    CONSTRAINT UQ_inv_token UNIQUE (token_hash)
);

CREATE INDEX IX_inv_place_email ON places.invitations (place_id, email);
-- Filtered unique: one pending invitation per place+email
-- CREATE UNIQUE INDEX UX_inv_pending ON places.invitations(place_id, email) WHERE status = 'PENDING';
```

---

## Schema: devices — BC-03 Device Management (Supporting)

**Owner:** device-service (.NET 8)

### Table: devices.devices

```sql
CREATE TABLE devices.devices (
    id                       UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    serial_number            NVARCHAR(100)    NOT NULL,
    place_id                 UNIQUEIDENTIFIER NULL, -- FK cross → places.places.id (NULL = available)
    auth_token_hash          NVARCHAR(256)    NOT NULL,
    auth_token_last_used_at  DATETIME2        NULL,
    auth_token_revoked_at    DATETIME2        NULL,
    status                   NVARCHAR(20)     NOT NULL DEFAULT 'NEVER_REPORTED'
                             CONSTRAINT CK_dev_status CHECK (status IN ('CONNECTED', 'DISCONNECTED', 'NEVER_REPORTED')),
    last_report_at           DATETIME2        NULL,
    inactivity_threshold_min INT              NOT NULL DEFAULT 15,
    linked_at                DATETIME2        NULL,
    created_at               DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),
    updated_at               DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_dev_serial UNIQUE (serial_number)
);

CREATE INDEX IX_dev_place ON devices.devices (place_id) WHERE place_id IS NOT NULL;
```

### Table: devices.device_link_history

```sql
CREATE TABLE devices.device_link_history (
    id          UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    device_id   UNIQUEIDENTIFIER NOT NULL REFERENCES devices.devices(id),
    place_id    UNIQUEIDENTIFIER NOT NULL, -- FK cross → places.places.id
    linked_by   UNIQUEIDENTIFIER NOT NULL, -- FK cross → security.users.id
    linked_at   DATETIME2        NOT NULL,
    unlinked_at DATETIME2        NULL,
    unlinked_by UNIQUEIDENTIFIER NULL,     -- FK cross → security.users.id

    INDEX IX_linkh_dev_time (device_id, linked_at)
);
-- Filtered unique: one active link per device
-- CREATE UNIQUE INDEX UX_device_active_link ON devices.device_link_history(device_id) WHERE unlinked_at IS NULL;
```

---

## Schema: monitoring — BC-04 Consumption Monitoring (Core)

**Owner:** consumption-service (.NET 8)

### Table: monitoring.readings

```sql
CREATE TABLE monitoring.readings (
    id                UNIQUEIDENTIFIER NOT NULL DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    device_id         UNIQUEIDENTIFIER NOT NULL, -- FK cross → devices.devices.id
    place_id          UNIQUEIDENTIFIER NOT NULL, -- FK cross → places.places.id [D1]
    flow_rate_lph     DECIMAL(18,3)    NOT NULL CONSTRAINT CK_read_flow CHECK (flow_rate_lph >= 0),
    cumulative_liters DECIMAL(18,3)    NOT NULL CONSTRAINT CK_read_cumul CHECK (cumulative_liters >= 0),
    recorded_at       DATETIME2        NOT NULL,
    received_at       DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_reading_dev_time UNIQUE (device_id, recorded_at)
);

CREATE INDEX IX_reading_place_time ON monitoring.readings (place_id, recorded_at);
```

**Performance:** Highest write-volume table. Partition by `recorded_at` when exceeding ~50M rows.

### Table: monitoring.consumption_hourly

```sql
CREATE TABLE monitoring.consumption_hourly (
    id            UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    place_id      UNIQUEIDENTIFIER NOT NULL, -- FK cross → places.places.id
    hour_start    DATETIME2        NOT NULL,
    total_liters  DECIMAL(18,3)    NOT NULL CONSTRAINT CK_hourly_vol CHECK (total_liters >= 0),
    reading_count INT              NOT NULL CONSTRAINT CK_hourly_cnt CHECK (reading_count >= 0),
    created_at    DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_hourly_place_hour UNIQUE (place_id, hour_start)
);
```

### Table: monitoring.consumption_daily

```sql
CREATE TABLE monitoring.consumption_daily (
    id            UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    place_id      UNIQUEIDENTIFIER NOT NULL, -- FK cross → places.places.id
    date          DATE             NOT NULL,
    total_liters  DECIMAL(18,3)    NOT NULL CONSTRAINT CK_daily_vol CHECK (total_liters >= 0),
    avg_hourly    DECIMAL(18,3)    NULL,
    peak_hour     TINYINT          NULL CONSTRAINT CK_daily_peak CHECK (peak_hour BETWEEN 0 AND 23),
    reading_count INT              NOT NULL CONSTRAINT CK_daily_cnt CHECK (reading_count >= 0),
    created_at    DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_daily_place_date UNIQUE (place_id, date)
);
```

### Table: monitoring.consumption_monthly

```sql
CREATE TABLE monitoring.consumption_monthly (
    id             UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    place_id       UNIQUEIDENTIFIER NOT NULL, -- FK cross → places.places.id
    month_start    DATE             NOT NULL CONSTRAINT CK_monthly_day CHECK (DAY(month_start) = 1),
    total_liters   DECIMAL(18,3)    NOT NULL CONSTRAINT CK_monthly_vol CHECK (total_liters >= 0),
    avg_daily      DECIMAL(18,3)    NULL,
    peak_day       DATE             NULL,
    reading_count  INT              NOT NULL CONSTRAINT CK_monthly_cnt CHECK (reading_count >= 0),
    days_with_data INT              NOT NULL,
    created_at     DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_monthly_place_month UNIQUE (place_id, month_start)
);
```

---

## Schema: goals — BC-05 Consumption Goals (Supporting) *(Entrega 2)*

**Owner:** consumption-service (.NET 8)

### Table: goals.consumption_goals

```sql
CREATE TABLE goals.consumption_goals (
    id            UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    place_id      UNIQUEIDENTIFIER NOT NULL, -- FK cross → places.places.id
    month_start   DATE             NOT NULL CONSTRAINT CK_goal_day CHECK (DAY(month_start) = 1),
    target_liters DECIMAL(18,3)    NOT NULL CONSTRAINT CK_goal_target CHECK (target_liters > 0),
    source        NVARCHAR(15)     NOT NULL
                  CONSTRAINT CK_goal_source CHECK (source IN ('MANUAL', 'SUGGESTED', 'CARRIED_OVER')),
    created_at    DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),
    updated_at    DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_goal_place_month UNIQUE (place_id, month_start)
);
```

### Table: goals.goal_suggestions

```sql
CREATE TABLE goals.goal_suggestions (
    id               UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    place_id         UNIQUEIDENTIFIER NOT NULL, -- FK cross → places.places.id
    month_start      DATE             NOT NULL CONSTRAINT CK_suggest_day CHECK (DAY(month_start) = 1),
    suggested_liters DECIMAL(18,3)    NOT NULL CONSTRAINT CK_suggest_val CHECK (suggested_liters > 0),
    based_on_months  INT              NOT NULL CONSTRAINT CK_suggest_months CHECK (based_on_months >= 1),
    accepted         BIT              NULL,
    created_at       DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_suggestion_place_month UNIQUE (place_id, month_start)
);
```

---

## Schema: billing — BC-06 Billing & Tariffs (Supporting) *(Entrega 3)*

**Owner:** consumption-service (.NET 8)

### Table: billing.water_bills

```sql
CREATE TABLE billing.water_bills (
    id                 UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    place_id           UNIQUEIDENTIFIER NOT NULL, -- FK cross → places.places.id
    billed_consumption DECIMAL(18,3)    NOT NULL CONSTRAINT CK_bill_vol CHECK (billed_consumption >= 0),
    billed_unit        NVARCHAR(20)     NOT NULL,
    total_amount       DECIMAL(18,2)    NOT NULL CONSTRAINT CK_bill_amt CHECK (total_amount >= 0),
    currency           NVARCHAR(3)      NOT NULL,
    period_start       DATE             NOT NULL,
    period_end         DATE             NOT NULL,
    due_date           DATE             NULL,
    paid               BIT              NOT NULL DEFAULT 0,
    paid_at            DATETIME2        NULL,
    created_at         DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),
    updated_at         DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT CK_bill_period CHECK (period_start < period_end)
);

CREATE INDEX IX_bill_place_period ON billing.water_bills (place_id, period_start);
CREATE INDEX IX_bill_place_due    ON billing.water_bills (place_id, due_date);
```

### Table: billing.tariffs

```sql
CREATE TABLE billing.tariffs (
    id                   UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    place_id             UNIQUEIDENTIFIER NOT NULL, -- FK cross → places.places.id
    price_per_unit       DECIMAL(18,2)    NOT NULL CONSTRAINT CK_tariff_price CHECK (price_per_unit > 0),
    unit                 NVARCHAR(20)     NOT NULL,
    fixed_monthly_charge DECIMAL(18,2)    NOT NULL DEFAULT 0
                         CONSTRAINT CK_tariff_fixed CHECK (fixed_monthly_charge >= 0),
    currency             NVARCHAR(3)      NOT NULL,
    source               NVARCHAR(10)     NOT NULL DEFAULT 'MANUAL'
                         CONSTRAINT CK_tariff_source CHECK (source IN ('MANUAL', 'CATALOG')),
    catalog_tariff_id    UNIQUEIDENTIFIER NULL, -- FK → billing.tariff_catalog.id
    effective_from       DATE             NOT NULL,
    effective_to         DATE             NULL,
    created_at           DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT CK_tariff_period CHECK (effective_to IS NULL OR effective_from < effective_to)
);

CREATE INDEX IX_tariff_place_from ON billing.tariffs (place_id, effective_from);
```

### Table: billing.tariff_catalog *(Entrega 4 — Could)*

```sql
CREATE TABLE billing.tariff_catalog (
    id              UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    country_code    NVARCHAR(2)      NOT NULL, -- CO | EC (ADR-004); align with places.countries / places.cities when designed (HU-066)
    region          NVARCHAR(200)    NOT NULL,
    category        NVARCHAR(100)    NULL,
    tier_name       NVARCHAR(100)    NULL,
    min_consumption DECIMAL(18,3)    NULL CONSTRAINT CK_cat_min CHECK (min_consumption >= 0),
    max_consumption DECIMAL(18,3)    NULL,
    price_per_unit  DECIMAL(18,2)    NOT NULL CONSTRAINT CK_cat_price CHECK (price_per_unit > 0),
    fixed_charge    DECIMAL(18,2)    NOT NULL DEFAULT 0 CONSTRAINT CK_cat_fixed CHECK (fixed_charge >= 0),
    currency        NVARCHAR(3)      NOT NULL,
    unit            NVARCHAR(20)     NOT NULL,
    last_updated    DATE             NOT NULL,
    source_url      NVARCHAR(500)    NULL,
    created_at      DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT CK_cat_range CHECK (max_consumption IS NULL OR max_consumption > min_consumption),
    INDEX IX_catalog_location (country_code, region, category)
);
```

---

## Schema: valve — BC-07 Valve Control (Core) *(Entrega 2)*

**Owner:** valve-service (.NET 8)

### Table: valve.valve_states

```sql
CREATE TABLE valve.valve_states (
    id                UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    device_id         UNIQUEIDENTIFIER NOT NULL, -- FK cross → devices.devices.id
    place_id          UNIQUEIDENTIFIER NOT NULL, -- FK cross → places.places.id
    state             NVARCHAR(10)     NOT NULL DEFAULT 'UNKNOWN'
                      CONSTRAINT CK_vstate CHECK (state IN ('OPEN', 'CLOSED', 'UNKNOWN')),
    last_confirmed_at DATETIME2        NULL,
    updated_at        DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_vstate_device UNIQUE (device_id),
    CONSTRAINT UQ_vstate_place  UNIQUE (place_id)
);
```

### Table: valve.valve_commands

```sql
CREATE TABLE valve.valve_commands (
    id                UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    device_id         UNIQUEIDENTIFIER NOT NULL, -- FK cross → devices.devices.id
    place_id          UNIQUEIDENTIFIER NOT NULL, -- FK cross → places.places.id [D2]
    commanded_by      UNIQUEIDENTIFIER NULL,     -- FK cross → security.users.id (NULL = auto)
    command           NVARCHAR(10)     NOT NULL
                      CONSTRAINT CK_vcmd_cmd CHECK (command IN ('OPEN', 'CLOSE')),
    origin            NVARCHAR(15)     NOT NULL
                      CONSTRAINT CK_vcmd_origin CHECK (origin IN ('MANUAL', 'AUTO_LEAK', 'AUTO_GOAL')),
    status            NVARCHAR(25)     NOT NULL DEFAULT 'PENDING_CONFIRMATION'
                      CONSTRAINT CK_vcmd_status CHECK (status IN (
                          'PENDING_CONFIRMATION', 'CONFIRMED', 'SENT', 'ACK_SUCCESS', 'ACK_TIMEOUT', 'FAILED'
                      )),
    confirmation_code NVARCHAR(10)     NULL,
    confirmed_at      DATETIME2        NULL,
    sent_at           DATETIME2        NULL,
    device_ack_at     DATETIME2        NULL,
    timeout_at        DATETIME2        NULL,
    failure_reason    NVARCHAR(500)    NULL,
    created_at        DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME()
);

CREATE INDEX IX_vcmd_place_time ON valve.valve_commands (place_id, created_at);
```

**Command lifecycle:** `PENDING_CONFIRMATION → CONFIRMED → SENT → ACK_SUCCESS | ACK_TIMEOUT`. Reopening is always `MANUAL`. 30s without device ACK → `ACK_TIMEOUT`.

### Table: valve.valve_rules *(Entrega 3)*

```sql
CREATE TABLE valve.valve_rules (
    id                       UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    place_id                 UNIQUEIDENTIFIER NOT NULL, -- FK cross → places.places.id
    rule_type                NVARCHAR(20)     NOT NULL
                             CONSTRAINT CK_vrule_type CHECK (rule_type IN ('LEAK_DETECTION', 'GOAL_EXCEEDED')),
    enabled                  BIT              NOT NULL DEFAULT 0,
    requires_confirmation    BIT              NOT NULL DEFAULT 1,
    confirmation_timeout_min INT              NOT NULL DEFAULT 10
                             CONSTRAINT CK_vrule_timeout CHECK (confirmation_timeout_min > 0),
    created_by               UNIQUEIDENTIFIER NOT NULL, -- FK cross → security.users.id
    created_at               DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),
    updated_at               DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_vrule_place_type UNIQUE (place_id, rule_type)
);
```

---

## Schema: alerts — BC-08 Alerts & Detection (Core) *(Entrega 2)*

**Owner:** notification-service (.NET 8)

### Table: alerts.alert_type_config

```sql
CREATE TABLE alerts.alert_type_config (
    id              UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    alert_type      NVARCHAR(30)     NOT NULL
                    CONSTRAINT CK_atc_type CHECK (alert_type IN (
                        'SUSPECTED_LEAK', 'GOAL_80_PERCENT', 'GOAL_EXCEEDED',
                        'SENSOR_DISCONNECTED', 'VALVE_CLOSED',
                        'BILL_MISALIGNED', 'BILL_REGISTER_REMINDER', 'BILL_PAYMENT_REMINDER'
                    )),
    severity        NVARCHAR(15)     NOT NULL
                    CONSTRAINT CK_atc_sev CHECK (severity IN ('CRITICAL', 'IMPORTANT', 'INFORMATIVE')),
    description     NVARCHAR(500)    NOT NULL,
    default_enabled BIT              NOT NULL DEFAULT 1,
    max_per_period  INT              NULL,
    period_unit     NVARCHAR(10)     NULL,

    CONSTRAINT UQ_atc_type UNIQUE (alert_type)
);
```

**Seed data:**

| alert_type | severity | max_per_period | period_unit |
|-----------|----------|---------------|-------------|
| SUSPECTED_LEAK | CRITICAL | 1 | HOUR |
| VALVE_CLOSED | CRITICAL | — | — |
| GOAL_EXCEEDED | IMPORTANT | 1 | MONTH |
| SENSOR_DISCONNECTED | IMPORTANT | 1 | (until reconnect) |
| GOAL_80_PERCENT | INFORMATIVE | 1 | MONTH |

### Table: alerts.alert_events

```sql
CREATE TABLE alerts.alert_events (
    id           UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    place_id     UNIQUEIDENTIFIER NOT NULL, -- FK cross → places.places.id
    alert_type   NVARCHAR(30)     NOT NULL,
    severity     NVARCHAR(15)     NOT NULL, -- [D3] snapshot at creation time
    title        NVARCHAR(200)    NOT NULL,
    message      NVARCHAR(2000)   NOT NULL,
    metadata     NVARCHAR(4000)   NULL,
    triggered_at DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),
    read_at      DATETIME2        NULL,
    deleted_at   DATETIME2        NULL,

    CONSTRAINT CK_alertev_json CHECK (metadata IS NULL OR ISJSON(metadata) = 1)
);

CREATE INDEX IX_alert_place_time ON alerts.alert_events (place_id, triggered_at);
CREATE INDEX IX_alert_dedup      ON alerts.alert_events (place_id, alert_type, triggered_at);
```

### Table: alerts.notification_preferences

```sql
CREATE TABLE alerts.notification_preferences (
    id             UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    user_id        UNIQUEIDENTIFIER NOT NULL, -- FK cross → security.users.id
    alert_severity NVARCHAR(15)     NOT NULL
                   CONSTRAINT CK_notpref_sev CHECK (alert_severity IN ('CRITICAL', 'IMPORTANT', 'INFORMATIVE')),
    channel_app    BIT              NOT NULL DEFAULT 1,
    channel_push   BIT              NOT NULL DEFAULT 1,
    channel_email  BIT              NOT NULL DEFAULT 0,
    channel_sms    BIT              NOT NULL DEFAULT 0,
    updated_at     DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_notpref_user_sev UNIQUE (user_id, alert_severity),
    CONSTRAINT CK_notpref_critical CHECK (alert_severity <> 'CRITICAL' OR channel_app = 1)
);
```

---

## Schema: notifications — BC-09 Notification Delivery (Generic) *(Entrega 2)*

**Owner:** notification-service (.NET 8)

### Table: notifications.notification_log

```sql
CREATE TABLE notifications.notification_log (
    id             UNIQUEIDENTIFIER NOT NULL DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    alert_event_id UNIQUEIDENTIFIER NOT NULL, -- FK cross → alerts.alert_events.id
    user_id        UNIQUEIDENTIFIER NOT NULL, -- FK cross → security.users.id
    channel        NVARCHAR(10)     NOT NULL
                   CONSTRAINT CK_nlog_channel CHECK (channel IN ('APP', 'PUSH', 'EMAIL', 'SMS', 'WHATSAPP')),
    status         NVARCHAR(15)     NOT NULL DEFAULT 'PENDING'
                   CONSTRAINT CK_nlog_status CHECK (status IN ('PENDING', 'SENT', 'DELIVERED', 'FAILED')),
    provider_ref   NVARCHAR(200)    NULL,
    sent_at        DATETIME2        NULL,
    delivered_at   DATETIME2        NULL,
    failed_at      DATETIME2        NULL,
    error_message  NVARCHAR(1000)   NULL,
    created_at     DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_nlog_event_user_channel UNIQUE (alert_event_id, user_id, channel)
);

CREATE INDEX IX_nlog_user_time ON notifications.notification_log (user_id, created_at);
```

### Table: notifications.push_subscriptions

```sql
CREATE TABLE notifications.push_subscriptions (
    id           UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    user_id      UNIQUEIDENTIFIER NOT NULL, -- FK cross → security.users.id
    device_token NVARCHAR(500)    NOT NULL,
    platform     NVARCHAR(10)     NOT NULL,
    active       BIT              NOT NULL DEFAULT 1,
    created_at   DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),
    updated_at   DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_push_user_token UNIQUE (user_id, device_token)
);
```

---

## Schema: content — BC-11 Savings Tips (Generic) *(Entrega 4)*

**Owner:** notification-service (.NET 8)

### Table: content.savings_tips

```sql
CREATE TABLE content.savings_tips (
    id         UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    category   NVARCHAR(15)     NOT NULL
               CONSTRAINT CK_tip_cat CHECK (category IN ('RESIDENTIAL', 'COMMERCIAL', 'GENERAL')),
    title      NVARCHAR(200)    NOT NULL,
    body       NVARCHAR(2000)   NOT NULL,
    sort_order INT              NOT NULL DEFAULT 0,
    active     BIT              NOT NULL DEFAULT 1,
    created_at DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME()
);
```

### Table: content.tip_favorites

```sql
CREATE TABLE content.tip_favorites (
    id         UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID() PRIMARY KEY,
    user_id    UNIQUEIDENTIFIER NOT NULL, -- FK cross → security.users.id
    tip_id     UNIQUEIDENTIFIER NOT NULL REFERENCES content.savings_tips(id),
    created_at DATETIME2        NOT NULL DEFAULT SYSUTCDATETIME(),

    CONSTRAINT UQ_fav_user_tip UNIQUE (user_id, tip_id)
);
```

---

## Data ownership matrix

| Entity / Data | Owner schema | Owner BC | Service | How other services access |
|---------------|-------------|---------|---------|--------------------------|
| User identity | `security` | BC-01 IAM | auth-service | REST API + JWT claims |
| User credentials | `security` | BC-01 IAM | auth-service | Internal only |
| Place info | `places` | BC-02 Place Mgmt | place-service | REST API |
| Location catalog (countries, subdivisions, cities) | `places` | BC-02 Place Mgmt | place-service | REST API (read-only); seed data |
| Members & invitations | `places` | BC-02 Place Mgmt | place-service | REST API |
| Device registry | `devices` | BC-03 Device Mgmt | device-service | REST API + events |
| Device link history | `devices` | BC-03 Device Mgmt | device-service | REST API |
| Raw readings | `monitoring` | BC-04 Monitoring | consumption-service | `ReadingReceived` events |
| Consumption aggregates | `monitoring` | BC-04 Monitoring | consumption-service | REST API |
| Goals & suggestions | `goals` | BC-05 Goals | consumption-service | REST API |
| Bills & tariffs | `billing` | BC-06 Billing | consumption-service | REST API |
| Tariff catalog | `billing` | BC-06 Billing | consumption-service | REST API (read-only) |
| Valve state | `valve` | BC-07 Valve Control | valve-service | REST API + events |
| Valve commands | `valve` | BC-07 Valve Control | valve-service | REST API |
| Valve rules | `valve` | BC-07 Valve Control | valve-service | REST API |
| Alert type config | `alerts` | BC-08 Alerts | notification-service | Internal (seed data) |
| Alert events | `alerts` | BC-08 Alerts | notification-service | REST API |
| Notification preferences | `alerts` | BC-08 Alerts | notification-service | REST API |
| Notification log | `notifications` | BC-09 Delivery | notification-service | REST API |
| Push subscriptions | `notifications` | BC-09 Delivery | notification-service | Internal |
| Savings tips | `content` | BC-11 Tips | notification-service | REST API |
| Tip favorites | `content` | BC-11 Tips | notification-service | REST API |

---

## Cross-schema FK summary

All cross-schema foreign keys are real constraints enforced by SQL Server.

### → security.users

| Source table | Column |
|-------------|--------|
| places.places | owner_id |
| places.place_members | user_id |
| places.invitations | invited_by |
| devices.device_link_history | linked_by, unlinked_by |
| valve.valve_commands | commanded_by |
| valve.valve_rules | created_by |
| alerts.notification_preferences | user_id |
| notifications.notification_log | user_id |
| notifications.push_subscriptions | user_id |
| content.tip_favorites | user_id |

### → places.places

| Source table | Column |
|-------------|--------|
| devices.devices | place_id |
| devices.device_link_history | place_id |
| monitoring.readings | place_id |
| monitoring.consumption_hourly | place_id |
| monitoring.consumption_daily | place_id |
| monitoring.consumption_monthly | place_id |
| goals.consumption_goals | place_id |
| goals.goal_suggestions | place_id |
| valve.valve_states | place_id |
| valve.valve_commands | place_id |
| valve.valve_rules | place_id |
| alerts.alert_events | place_id |
| billing.water_bills | place_id |
| billing.tariffs | place_id |

### → devices.devices

| Source table | Column |
|-------------|--------|
| monitoring.readings | device_id |
| valve.valve_states | device_id |
| valve.valve_commands | device_id |

### → alerts.alert_events

| Source table | Column |
|-------------|--------|
| notifications.notification_log | alert_event_id |

---

## ER Diagrams

### security schema (BC-01)

```
┌──────────────────────┐
│   security.users     │
├──────────────────────┤       ┌──────────────────────────────┐
│ id (PK, UUID)        │──┐    │ security.user_credentials    │
│                      │  │    ├──────────────────────────────┤
│ name, email (UQ)     │  ├───▶│ user_id (FK)                 │
│ phone                │  │    │ credential_type (LOCAL|GOOGLE)│
│ email_verified       │  │    │ password_hash, google_id     │
│ account_locked_until │  │    └──────────────────────────────┘
│ created_at           │  │
└──────────────────────┘  │    ┌──────────────────────────────┐
                          │    │ security.email_verifications  │
                          ├───▶│ user_id (FK)                 │
                          │    │ token_hash, expires_at        │
                          │    └──────────────────────────────┘
                          │
                          │    ┌──────────────────────────────┐
                          │    │ security.password_resets      │
                          ├───▶│ user_id (FK)                 │
                          │    │ token_hash, expires_at        │
                          │    └──────────────────────────────┘
                          │
                          │    ┌──────────────────────────────┐
                          ├───▶│ security.refresh_tokens      │
                          │    │ user_id (FK), token_hash     │
                          │    │ device_info, revoked_at      │
                          │    └──────────────────────────────┘
                          │
                          │    ┌──────────────────────────────┐
                          ├───▶│ security.login_attempts      │
                          │    │ user_id (FK, nullable)       │
                          │    │ email, ip, success           │
                          │    └──────────────────────────────┘
                          │
                          │    ┌──────────────────────────────┐
                          └───▶│ security.activity_log        │
                               │ user_id (FK), action         │
                               │ entity_type, metadata (JSON) │
                               └──────────────────────────────┘
```

### places schema (BC-02)

```
┌──────────────────────┐    ┌──────────────────────┐    ┌──────────────────────┐
│  places.countries    │    │ places.subdivisions  │    │    places.cities     │
│  (seed — CO, EC)     │    │ (seed — 33 CO+24 EC) │    │ (seed — 1122+221)    │
├──────────────────────┤    ├──────────────────────┤    ├──────────────────────┤
│ id (PK, UUID)        │───▶│ country_id (FK)      │───▶│ subdivision_id (FK)  │
│ code (UQ)            │    │ code (UQ w/country)  │    │ code (UQ w/subdiv)   │
│ name                 │    │ name                 │    │ name                 │
│ default_currency     │    └──────────────────────┘    └──────────┬───────────┘
│ default_unit         │                                           │
└──────────────────────┘                                           │
┌────────────────────────────┐                                     │
│      places.places         │◀────────────────────────────────────┘
├────────────────────────────┤
│ id (PK, UUID)              │──┐
│ owner_id (FK → users) [D4] │  │    ┌───────────────────────────┐
│ city_id (FK → cities)      │  │    │   places.place_members    │
│ name, place_type, address  │  │    │      *(Entrega 3)*        │
│ currency, measurement_unit │  ├───▶│ place_id (FK)             │
│ is_default (UX per owner)  │  │    │ user_id (FK → users)      │
│ deleted_at (soft delete)   │  │    │ role (OWNER|GUEST)        │
└────────────────────────────┘  │    │ joined_at, revoked_at     │
                                │    └───────────────────────────┘
                                │
                                │    ┌───────────────────────────┐
                                │    │   places.invitations      │
                                │    │      *(Entrega 3)*        │
                                └───▶│ place_id (FK)             │
                                     │ invited_by (FK → users)   │
                                     │ email, token_hash         │
                                     │ status, expires_at        │
                                     └───────────────────────────┘
```

### devices schema (BC-03)

```
┌──────────────────────────┐
│    devices.devices       │
├──────────────────────────┤       ┌────────────────────────────────┐
│ id (PK, UUID)           │──────▶│ devices.device_link_history    │
│ serial_number (UQ)       │       ├────────────────────────────────┤
│ place_id (FK → places)   │       │ device_id (FK)                 │
│ auth_token_hash          │       │ place_id (FK → places)         │
│ status                   │       │ linked_by (FK → users)         │
│ last_report_at           │       │ linked_at, unlinked_at         │
│ inactivity_threshold_min │       │ unlinked_by (FK → users)       │
└──────────────────────────┘       └────────────────────────────────┘
```

### monitoring schema (BC-04)

```
┌─────────────────────────┐    ┌─────────────────────────────┐
│  monitoring.readings    │    │ monitoring.consumption_hourly│
├─────────────────────────┤    ├─────────────────────────────┤
│ device_id (FK → devices)│    │ place_id (FK → places)      │
│ place_id (FK → places)  │    │ hour_start (UQ w/place)     │
│ flow_rate_lph           │    │ total_liters, reading_count │
│ cumulative_liters       │    └─────────────────────────────┘
│ recorded_at (UQ w/dev)  │
│ received_at             │    ┌─────────────────────────────┐
└─────────────────────────┘    │ monitoring.consumption_daily│
                               ├─────────────────────────────┤
┌─────────────────────────────┐│ place_id, date (UQ w/place) │
│monitoring.consumption_monthly│ total_liters, avg_hourly   │
├─────────────────────────────┤│ peak_hour, reading_count   │
│ place_id, month_start (UQ)  │└─────────────────────────────┘
│ total_liters, avg_daily     │
│ peak_day, days_with_data    │
└─────────────────────────────┘

All tables linked by place_id (FK cross → places.places)
```

### valve schema (BC-07)

```
┌─────────────────────────┐
│   valve.valve_states    │
├─────────────────────────┤
│ device_id (UQ, FK)      │
│ place_id (UQ, FK)       │
│ state (OPEN|CLOSED|     │
│        UNKNOWN)         │
│ last_confirmed_at       │
└─────────────────────────┘

┌──────────────────────────────┐    ┌────────────────────────────┐
│    valve.valve_commands      │    │    valve.valve_rules        │
├──────────────────────────────┤    │       *(Entrega 3)*        │
│ device_id (FK → devices)     │    ├────────────────────────────┤
│ place_id (FK → places)       │    │ place_id (UQ w/rule_type)  │
│ commanded_by (FK → users)    │    │ rule_type                  │
│ command (OPEN|CLOSE)         │    │ enabled, requires_confirm  │
│ origin (MANUAL|AUTO_*)       │    │ confirmation_timeout_min   │
│ status (lifecycle)           │    │ created_by (FK → users)    │
│ confirmation_code            │    └────────────────────────────┘
│ confirmed_at → sent_at →    │
│   device_ack_at / timeout_at │
└──────────────────────────────┘
```

### alerts + notifications + content schemas (BC-08, BC-09, BC-11)

```
┌───────────────────────────────┐
│  alerts.alert_type_config     │
│  (seed — 8 types)             │
├───────────────────────────────┤
│ alert_type (UQ)               │
│ severity, description         │
│ max_per_period, period_unit   │
└───────────────────────────────┘

┌───────────────────────────────┐       ┌──────────────────────────────────┐
│    alerts.alert_events        │──────▶│ notifications.notification_log   │
├───────────────────────────────┤       ├──────────────────────────────────┤
│ place_id (FK → places)        │       │ alert_event_id (FK)              │
│ alert_type, severity [D3]     │       │ user_id (FK → users)             │
│ title, message                │       │ channel (APP|PUSH|EMAIL|SMS|WA)  │
│ metadata (JSON)               │       │ status (PENDING→DELIVERED|FAILED)│
│ triggered_at, read_at         │       │ provider_ref, sent_at            │
└───────────────────────────────┘       └──────────────────────────────────┘

┌──────────────────────────────────┐    ┌───────────────────────────┐
│ alerts.notification_preferences  │    │ notifications.push_subs   │
├──────────────────────────────────┤    ├───────────────────────────┤
│ user_id (FK → users)             │    │ user_id (FK → users)      │
│ alert_severity (UQ w/user)       │    │ device_token (UQ w/user)  │
│ channel_app, _push, _email, _sms │    │ platform, active          │
└──────────────────────────────────┘    └───────────────────────────┘

┌────────────────────────────┐       ┌────────────────────────────┐
│   content.savings_tips     │──────▶│   content.tip_favorites    │
│      *(Entrega 4)*         │       │      *(Entrega 4)*         │
├────────────────────────────┤       ├────────────────────────────┤
│ category, title, body      │       │ user_id (FK → users)       │
│ sort_order, active         │       │ tip_id (FK)                │
└────────────────────────────┘       └────────────────────────────┘
```

---

## Migration strategy

**Tool:** Flyway (Spring Boot — auth-service), EF Core Migrations (.NET — other services).

**File naming:**
```
V{version}__{snake_case_description}.sql

Examples:
  V001__create_security_schema.sql
  V002__create_users_table.sql
  V003__create_user_credentials_table.sql
```

**Rules:**
- Migrations are ALWAYS forward-only
- One migration per logical change
- Seed data uses prefix `S`: `S001__seed_alert_type_config.sql`
- Never modify a migration already executed in any environment
- Incompatible changes use a 2-phase approach across releases

---

## Correlations

- Authoritative data model (DBML) → `sy-water-db/cuida-tu-agua-model-v5.dbml`
- Bounded contexts and domain map → `02-domain/domain-map.md`
- Domain entities and aggregates → `02-domain/entities-and-rules.md`
- Domain events → `02-domain/domain-events.md`
- BC-to-service mapping → `05-architecture/overview.md` § 4
- Schema-per-service decision → `05-architecture/decisions/records/ADR-002-shared-database-schema-per-service.md`
- Single UUID PK decision → `05-architecture/decisions/records/ADR-003-single-uuid-primary-key.md`
- Market scope (CO/EC) and location catalog → `05-architecture/decisions/records/ADR-004-market-scope-co-ec.md`
- API contracts → `07-api/contracts/openapi/`
