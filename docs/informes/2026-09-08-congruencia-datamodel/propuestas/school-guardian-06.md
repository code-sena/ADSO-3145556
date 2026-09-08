# 06 - Data Model (counter-proposal)

> Project: **school-guardian** - repository `sg-docs`  
> Generated: 2026-09-08 - review of folders 02, 03 and 06  
> Golden rule applied: **the project scope is not changed**. Every entity below comes from
> what the team already declared in `02-domain` and `03-product`.

---

## 1. Why this counter-proposal

**Current state of `06-data`:** Plantilla con una nota de ADR-002 insertada. Cero tablas.

**Normalization level found:** Sin modelo

**02 -> 06 congruence:** 0% of the declared domain entities exist as tables.

El dominio mas elaborado de la ficha sin una sola tabla. Incluso escribieron una seccion titulada 'Relacion con la base de datos' y decisiones de modelado, pero no llegaron a definirla.

## 2. Findings addressed

| Level | Finding |
|---|---|
| **Congruencia critica** | El equipo tiene el dominio mas elaborado de la ficha (30 KB de entidades, 51 KB de eventos, 14 entidades declaradas) y **cero tablas**. `06-data/models.md` es la plantilla con una nota de ADR-002 insertada: no hay modelo de datos. |
| **1FN** | `Family` no puede guardar la lista de estudiantes en un campo. La relacion acudiente-estudiante es N:M y necesita tabla propia. |
| **3FN** | La parada pertenece a la ruta y su orden es un atributo de la relacion, no de la parada: se resuelve en `route_stop` con `stop_sequence`. |
| **2FN** | Las asignaciones ruta-bus y ruta-estudiante tienen vigencia; sin fecha se pierde el historico del ano escolar. |

## 3. Candidate domains from the existing documentation

These are the bounded contexts the project itself supports. No domain was invented.

| Domain | Scope |
|---|---|
| `Identity & Access` | Persona, cuenta, roles y permisos. |
| `School` | Institucion y estructura escolar. |
| `Family` | Nucleo familiar y vinculo acudiente-estudiante. |
| `Fleet` | Bus escolar y su conductor asignado. |
| `Route` | Ruta, paradas y asignaciones de bus y estudiante. |
| `Tracking` | Dispositivo GPS y posiciones reportadas. |
| `Boarding` | Abordaje y descenso del estudiante. |
| `Alert` | Alertas de desvio, retraso y no abordaje. |

## 4. Modeling standard

Applies to every table in this document.

**Language.** All schemas, tables, columns and catalog values are written in English. 
Documentation prose may stay in Spanish; the model may not.

**Naming.** `snake_case`; tables singular; primary key `<table>_id`; foreign key named after the referenced key.

**Normalization floor: 3NF.**

- **1NF** - atomic columns only. No comma separated lists, no repeating groups, no free-text fields 
holding structured data. A repeating group becomes a child table.
- **2NF** - on composite keys, every non-key attribute depends on the *whole* key. Attributes that 
depend on part of it move to the table that owns that part.
- **3NF** - no transitive dependencies. Descriptive attributes reachable through a foreign key are 
never copied. Statuses, types and categories live in catalog tables, never as free text.
- **Derived values are not stored**, with two documented exceptions: legally frozen amounts 
(an issued invoice, a purchased ticket) and aggregates materialized for performance, which are 
explicitly marked as recomputable.

**Audit block.** Every business table carries these columns:

```sql
created_at    TIMESTAMPTZ   NOT NULL DEFAULT now()   -- Alta del registro
created_by    BIGINT        NULL REFERENCES app_user(user_id)   -- Autor del alta
updated_at    TIMESTAMPTZ   NULL   -- Ultima modificacion
updated_by    BIGINT        NULL REFERENCES app_user(user_id)   -- Autor de la modificacion
deleted_at    TIMESTAMPTZ   NULL   -- Baja logica; NULL = vigente
deleted_by    BIGINT        NULL REFERENCES app_user(user_id)   -- Autor de la baja
row_version   INTEGER       NOT NULL DEFAULT 1   -- Bloqueo optimista
```

High-volume immutable tables (time series, logs, consent records) carry only `created_at` and 
`created_by`: they are never updated or soft-deleted. This is stated per table below.

A row-level audit block answers *when and who*. The `audit_log` table answers *what changed*. 
Both are required; neither replaces the other.

## 5. Shared core (identical across the ficha)

### `identity.app_user`

Cuenta de acceso. Separada de la persona para que un mismo individuo pueda tener o no credenciales.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `user_id` | `BIGSERIAL` | PK |  |
| `person_id` | `BIGINT` | NOT NULL UNIQUE REFERENCES person(person_id) | 1:1 con la persona |
| `username` | `VARCHAR(50)` | NOT NULL UNIQUE |  |
| `password_hash` | `VARCHAR(255)` | NULL | NULL cuando el proveedor es externo |
| `auth_provider_id` | `SMALLINT` | NOT NULL REFERENCES auth_provider(auth_provider_id) | LOCAL / GOOGLE / ... |
| `external_subject` | `VARCHAR(255)` | NULL | Identificador en el proveedor externo |
| `user_status_id` | `SMALLINT` | NOT NULL REFERENCES user_status(user_status_id) | Estado catalogado, no texto libre |
| `last_login_at` | `TIMESTAMPTZ` | NULL |  |

> **Normalization:** 3FN: `user_status` y `auth_provider` salen a catalogo para eliminar la dependencia transitiva de la descripcion del estado respecto del usuario.

### `identity.person`

Datos naturales de la persona, independientes de si tiene cuenta.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `person_id` | `BIGSERIAL` | PK |  |
| `document_type_id` | `SMALLINT` | NOT NULL REFERENCES document_type(document_type_id) |  |
| `document_number` | `VARCHAR(30)` | NOT NULL | UNIQUE junto a document_type_id |
| `first_name` | `VARCHAR(80)` | NOT NULL |  |
| `last_name` | `VARCHAR(80)` | NOT NULL |  |
| `email` | `VARCHAR(150)` | NOT NULL UNIQUE |  |
| `phone` | `VARCHAR(25)` | NULL |  |
| `birth_date` | `DATE` | NULL | La edad NO se almacena: es derivable |

> **Normalization:** 1FN: nunca un campo `full_name`; nombre y apellido separados. 3FN: la edad se calcula, no se guarda.

### `identity.role`

Rol funcional.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `role_id` | `SMALLSERIAL` | PK |  |
| `code` | `VARCHAR(40)` | NOT NULL UNIQUE | ADMIN, CLIENT, ... |
| `name` | `VARCHAR(80)` | NOT NULL |  |
| `description` | `VARCHAR(255)` | NULL |  |

### `identity.permission`

Permiso atomico.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `permission_id` | `SMALLSERIAL` | PK |  |
| `code` | `VARCHAR(60)` | NOT NULL UNIQUE | recurso:accion |
| `description` | `VARCHAR(255)` | NULL |  |

### `identity.role_permission`

Resuelve la N:M entre rol y permiso.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `role_id` | `SMALLINT` | NOT NULL REFERENCES role(role_id) | PK compuesta |
| `permission_id` | `SMALLINT` | NOT NULL REFERENCES permission(permission_id) | PK compuesta |

> **Normalization:** 1FN: sin esta tabla los permisos terminarian como lista separada por comas dentro de `role`.

### `identity.user_role`

Resuelve la N:M entre usuario y rol.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) | PK compuesta |
| `role_id` | `SMALLINT` | NOT NULL REFERENCES role(role_id) | PK compuesta |
| `granted_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |

### `identity.user_session`

Sesion emitida.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `session_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `refresh_token_hash` | `VARCHAR(255)` | NOT NULL | Nunca el token en claro |
| `issued_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `expires_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `revoked_at` | `TIMESTAMPTZ` | NULL |  |
| `ip_address` | `INET` | NULL |  |
| `user_agent` | `VARCHAR(255)` | NULL |  |

### `audit.audit_log`

Bitacora de cambios. Complementa, no reemplaza, las columnas de auditoria por tabla.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `audit_log_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NULL REFERENCES app_user(user_id) | NULL en acciones del sistema |
| `entity_name` | `VARCHAR(60)` | NOT NULL | Tabla afectada |
| `entity_id` | `VARCHAR(60)` | NOT NULL | PK afectada, como texto |
| `action_id` | `SMALLINT` | NOT NULL REFERENCES audit_action(action_id) | CREATE/UPDATE/DELETE/LOGIN |
| `occurred_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `old_value` | `JSONB` | NULL | Snapshot previo |
| `new_value` | `JSONB` | NULL | Snapshot posterior |
| `ip_address` | `INET` | NULL |  |

> **Normalization:** El JSONB aqui es deliberado y no viola 1FN: es una bitacora de snapshots, no una entidad consultable por atributo.

**Shared catalogs:** `auth_provider`, `user_status`, `document_type`, `audit_action`.
Every catalog has `(<name>_id, code, name, description, is_active)` plus the audit block.

## 6. Domain model

### Schema `school`

#### `school.school`

Institucion educativa.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `school_id` | `SERIAL` | PK |  |
| `code` | `VARCHAR(30)` | NOT NULL UNIQUE |  |
| `name` | `VARCHAR(150)` | NOT NULL |  |
| `address_line` | `VARCHAR(200)` | NOT NULL |  |
| `city_id` | `INTEGER` | NOT NULL REFERENCES city(city_id) |  |
| `latitude` | `NUMERIC(9,6)` | NOT NULL |  |
| `longitude` | `NUMERIC(9,6)` | NOT NULL |  |

*Plus the full audit block from section 4.*

### Schema `family`

#### `family.family`

Nucleo familiar.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `family_id` | `BIGSERIAL` | PK |  |
| `code` | `VARCHAR(30)` | NOT NULL UNIQUE |  |
| `primary_contact_person_id` | `BIGINT` | NOT NULL REFERENCES person(person_id) |  |

*Plus the full audit block from section 4.*

#### `family.guardian_student`

Vinculo entre acudiente y estudiante. Resuelve la N:M.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `guardian_student_id` | `BIGSERIAL` | PK |  |
| `guardian_person_id` | `BIGINT` | NOT NULL REFERENCES person(person_id) |  |
| `student_person_id` | `BIGINT` | NOT NULL REFERENCES person(person_id) |  |
| `family_id` | `BIGINT` | NOT NULL REFERENCES family(family_id) |  |
| `relationship_id` | `SMALLINT` | NOT NULL REFERENCES relationship_type(relationship_id) |  |
| `is_authorized_pickup` | `BOOLEAN` | NOT NULL DEFAULT false |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: un vinculo por fila. Un estudiante puede tener varios acudientes y un acudiente varios estudiantes.

### Schema `school`

#### `school.student_profile`

Datos escolares del estudiante, sobre `person`.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `student_profile_id` | `BIGSERIAL` | PK |  |
| `person_id` | `BIGINT` | NOT NULL UNIQUE REFERENCES person(person_id) |  |
| `school_id` | `INTEGER` | NOT NULL REFERENCES school(school_id) |  |
| `grade_id` | `SMALLINT` | NOT NULL REFERENCES grade(grade_id) |  |
| `family_id` | `BIGINT` | NOT NULL REFERENCES family(family_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: no duplica nombre ni documento; los toma de `person`.

### Schema `fleet`

#### `fleet.bus`

Bus escolar.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `bus_id` | `SERIAL` | PK |  |
| `school_id` | `INTEGER` | NOT NULL REFERENCES school(school_id) |  |
| `license_plate` | `VARCHAR(10)` | NOT NULL UNIQUE |  |
| `seat_capacity` | `SMALLINT` | NOT NULL |  |
| `bus_status_id` | `SMALLINT` | NOT NULL REFERENCES bus_status(bus_status_id) |  |

*Plus the full audit block from section 4.*

#### `fleet.driver_assignment`

Conductor asignado a un bus, con vigencia.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `driver_assignment_id` | `BIGSERIAL` | PK |  |
| `bus_id` | `INTEGER` | NOT NULL REFERENCES bus(bus_id) |  |
| `driver_person_id` | `BIGINT` | NOT NULL REFERENCES person(person_id) |  |
| `assigned_from` | `DATE` | NOT NULL |  |
| `assigned_to` | `DATE` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** Historico: no se sobreescribe al cambiar de conductor.

### Schema `route`

#### `route.route`

Ruta escolar.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `route_id` | `SERIAL` | PK |  |
| `school_id` | `INTEGER` | NOT NULL REFERENCES school(school_id) |  |
| `name` | `VARCHAR(100)` | NOT NULL |  |
| `route_direction_id` | `SMALLINT` | NOT NULL REFERENCES route_direction(direction_id) | INBOUND / OUTBOUND |
| `scheduled_start` | `TIME` | NOT NULL |  |

*Plus the full audit block from section 4.*

#### `route.stop`

Parada fisica, reutilizable entre rutas.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `stop_id` | `SERIAL` | PK |  |
| `name` | `VARCHAR(120)` | NOT NULL |  |
| `address_line` | `VARCHAR(200)` | NOT NULL |  |
| `latitude` | `NUMERIC(9,6)` | NOT NULL |  |
| `longitude` | `NUMERIC(9,6)` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: la parada existe por si misma; su posicion en una ruta no es un atributo suyo.

#### `route.route_stop`

Parada dentro de una ruta, con su orden.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `route_stop_id` | `SERIAL` | PK |  |
| `route_id` | `INTEGER` | NOT NULL REFERENCES route(route_id) |  |
| `stop_id` | `INTEGER` | NOT NULL REFERENCES stop(stop_id) |  |
| `stop_sequence` | `SMALLINT` | NOT NULL |  |
| `scheduled_time` | `TIME` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** El orden es atributo de la relacion. UNIQUE(route_id, stop_sequence).

#### `route.route_bus_assignment`

Bus asignado a una ruta, con vigencia.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `route_bus_assignment_id` | `BIGSERIAL` | PK |  |
| `route_id` | `INTEGER` | NOT NULL REFERENCES route(route_id) |  |
| `bus_id` | `INTEGER` | NOT NULL REFERENCES bus(bus_id) |  |
| `assigned_from` | `DATE` | NOT NULL |  |
| `assigned_to` | `DATE` | NULL |  |

*Plus the full audit block from section 4.*

#### `route.route_student_assignment`

Estudiante asignado a una ruta y parada.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `route_student_assignment_id` | `BIGSERIAL` | PK |  |
| `route_id` | `INTEGER` | NOT NULL REFERENCES route(route_id) |  |
| `student_profile_id` | `BIGINT` | NOT NULL REFERENCES student_profile(student_profile_id) |  |
| `route_stop_id` | `INTEGER` | NOT NULL REFERENCES route_stop(route_stop_id) |  |
| `assigned_from` | `DATE` | NOT NULL |  |
| `assigned_to` | `DATE` | NULL |  |

*Plus the full audit block from section 4.*

### Schema `tracking`

#### `tracking.gps_device`

Dispositivo GPS del bus.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `gps_device_id` | `SERIAL` | PK |  |
| `bus_id` | `INTEGER` | NOT NULL REFERENCES bus(bus_id) |  |
| `serial_number` | `VARCHAR(60)` | NOT NULL UNIQUE |  |
| `device_status_id` | `SMALLINT` | NOT NULL REFERENCES device_status(device_status_id) |  |
| `installed_at` | `TIMESTAMPTZ` | NOT NULL |  |

*Plus the full audit block from section 4.*

#### `tracking.trip`

Recorrido concreto de una ruta en una fecha.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `trip_id` | `BIGSERIAL` | PK |  |
| `route_id` | `INTEGER` | NOT NULL REFERENCES route(route_id) |  |
| `bus_id` | `INTEGER` | NOT NULL REFERENCES bus(bus_id) |  |
| `driver_person_id` | `BIGINT` | NOT NULL REFERENCES person(person_id) |  |
| `trip_date` | `DATE` | NOT NULL |  |
| `started_at` | `TIMESTAMPTZ` | NULL |  |
| `ended_at` | `TIMESTAMPTZ` | NULL |  |
| `trip_status_id` | `SMALLINT` | NOT NULL REFERENCES trip_status(trip_status_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** Sin esta tabla el abordaje no tiene a que colgarse: la ruta es el plan, el viaje es el hecho.

#### `tracking.gps_location`

Posicion reportada. Serie temporal particionada por fecha.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `gps_location_id` | `BIGSERIAL` | PK |  |
| `gps_device_id` | `INTEGER` | NOT NULL REFERENCES gps_device(gps_device_id) |  |
| `trip_id` | `BIGINT` | NULL REFERENCES trip(trip_id) |  |
| `latitude` | `NUMERIC(9,6)` | NOT NULL |  |
| `longitude` | `NUMERIC(9,6)` | NOT NULL |  |
| `speed_kmh` | `NUMERIC(6,2)` | NULL |  |
| `recorded_at` | `TIMESTAMPTZ` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** Inmutable: solo `created_at`.

### Schema `boarding`

#### `boarding.boarding`

Abordaje o descenso del estudiante.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `boarding_id` | `BIGSERIAL` | PK |  |
| `trip_id` | `BIGINT` | NOT NULL REFERENCES trip(trip_id) |  |
| `student_profile_id` | `BIGINT` | NOT NULL REFERENCES student_profile(student_profile_id) |  |
| `route_stop_id` | `INTEGER` | NOT NULL REFERENCES route_stop(route_stop_id) |  |
| `boarding_event_id` | `SMALLINT` | NOT NULL REFERENCES boarding_event(boarding_event_id) | BOARD / ALIGHT |
| `occurred_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `registered_by` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: abordaje y descenso son filas distintas del mismo estudiante, no dos columnas.

### Schema `alert`

#### `alert.trip_alert`

Alerta durante un viaje.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `trip_alert_id` | `BIGSERIAL` | PK |  |
| `trip_id` | `BIGINT` | NOT NULL REFERENCES trip(trip_id) |  |
| `alert_type_id` | `SMALLINT` | NOT NULL REFERENCES alert_type(alert_type_id) | DEVIATION / DELAY / NO_BOARDING |
| `student_profile_id` | `BIGINT` | NULL REFERENCES student_profile(student_profile_id) |  |
| `severity_id` | `SMALLINT` | NOT NULL REFERENCES severity(severity_id) |  |
| `raised_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `acknowledged_at` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

## 7. Project catalogs

Beyond the shared ones, this project needs:

- `city`
- `relationship_type`
- `grade`
- `bus_status`
- `route_direction`
- `device_status`
- `trip_status`
- `boarding_event`
- `alert_type`
- `severity`

Each catalog exists precisely so that its descriptive value is stored once. Putting a status name 
or a unit directly into a transactional table is the transitive dependency 3NF forbids.

## 8. What this counter-proposal does NOT do

- It does not change the project's purpose: Seguimiento de rutas escolares con GPS y control de abordaje de estudiantes.
- It does not add features, screens or requirements that the team has not documented.
- It does not decide the database engine; the model is written in standard SQL types.
- It does not replace `02-domain`. If an entity here has no counterpart there, the gap is in `02`, 
  and closing it is part of the work.

---

*Counter-proposal generated from the review of ficha ADSO-3145556. Discuss it with the team before adopting it.*