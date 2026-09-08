# 06 - Data Model (counter-proposal)

> Project: **faceattend-edu** - repository `fae-docs`  
> Generated: 2026-09-08 - review of folders 02, 03 and 06  
> Golden rule applied: **the project scope is not changed**. Every entity below comes from
> what the team already declared in `02-domain` and `03-product`.

---

## 1. Why this counter-proposal

**Current state of `06-data`:** Real y el mas completo de la ficha: 8 archivos por dominio, 27 tablas, auditoria en 119 puntos.

**Normalization level found:** 2FN

**02 -> 06 congruence:** 63% of the declared domain entities exist as tables.

Deriva en ambas direcciones. El modelo inventa `student`, `teacher` y `enrollment` que el dominio no declara, y omite `Report`, `Alert`, `SupportingDocument` y `AttendanceHistory` que si declara. `student` y `teacher` duplican identidad de `person`: eso es lo que lo deja en 2FN.

## 2. Findings addressed

| Level | Finding |
|---|---|
| **Congruencia** | El modelo introduce `student`, `teacher` y `enrollment`, que el dominio no declara, y omite `Report`, `Alert`, `SupportingDocument` y `AttendanceHistory`, que si declara. Se alinean ambos lados. |
| **3FN** | `student` y `teacher` duplican nombre, documento y correo de `person`. Se unifican en `academic_actor`, un rol sobre la misma persona. |
| **1FN** | La plantilla biometrica no es un atributo de la persona: sale a `facial_template`, versionada, porque una persona tiene varias en el tiempo. |
| **Auditoria** | Es el proyecto con mejor auditoria de la ficha (119 menciones de `created_by`/`updated_by`). Se conserva ese criterio y se agrega `row_version`. |

## 3. Candidate domains from the existing documentation

These are the bounded contexts the project itself supports. No domain was invented.

| Domain | Scope |
|---|---|
| `Identity` | Persona, cuenta, sesiones y politica de contrasena. |
| `Authorization` | Roles, permisos y su asignacion. |
| `Academic` | Institucion, programa, curso, ficha, periodo y matricula. |
| `Scheduling` | Ambiente, bloque horario y sesion de clase. |
| `Attendance` | Registro de asistencia, justificacion y soporte documental. |
| `Biometric` | Plantilla facial y su ciclo de actualizacion. |
| `IoT` | Dispositivo de captura y su asignacion a un ambiente. |
| `Audit & Configuration` | Bitacora, errores y parametros del sistema. |

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

### Schema `academic`

#### `academic.school`

Institucion educativa.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `school_id` | `SERIAL` | PK |  |
| `code` | `VARCHAR(30)` | NOT NULL UNIQUE |  |
| `name` | `VARCHAR(150)` | NOT NULL |  |
| `city_id` | `INTEGER` | NOT NULL REFERENCES city(city_id) |  |

*Plus the full audit block from section 4.*

#### `academic.program`

Programa de formacion.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `program_id` | `SERIAL` | PK |  |
| `school_id` | `INTEGER` | NOT NULL REFERENCES school(school_id) |  |
| `code` | `VARCHAR(30)` | NOT NULL |  |
| `name` | `VARCHAR(150)` | NOT NULL |  |

*Plus the full audit block from section 4.*

#### `academic.academic_period`

Periodo lectivo.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `academic_period_id` | `SERIAL` | PK |  |
| `name` | `VARCHAR(60)` | NOT NULL |  |
| `starts_on` | `DATE` | NOT NULL |  |
| `ends_on` | `DATE` | NOT NULL |  |

*Plus the full audit block from section 4.*

#### `academic.cohort`

Ficha o grupo. El dominio declara `Cohort` y el modelo actual no la tiene.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `cohort_id` | `BIGSERIAL` | PK |  |
| `program_id` | `INTEGER` | NOT NULL REFERENCES program(program_id) |  |
| `academic_period_id` | `INTEGER` | NOT NULL REFERENCES academic_period(academic_period_id) |  |
| `code` | `VARCHAR(30)` | NOT NULL UNIQUE |  |

*Plus the full audit block from section 4.*

> **Normalization:** Congruencia 02 a 06: cierra una entidad declarada y no modelada.

#### `academic.course`

Curso o competencia.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `course_id` | `SERIAL` | PK |  |
| `program_id` | `INTEGER` | NOT NULL REFERENCES program(program_id) |  |
| `code` | `VARCHAR(30)` | NOT NULL |  |
| `name` | `VARCHAR(150)` | NOT NULL |  |
| `credit_hours` | `SMALLINT` | NOT NULL |  |

*Plus the full audit block from section 4.*

#### `academic.academic_actor`

Rol academico de una persona. Reemplaza `student` y `teacher`.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `academic_actor_id` | `BIGSERIAL` | PK |  |
| `person_id` | `BIGINT` | NOT NULL REFERENCES person(person_id) |  |
| `actor_type_id` | `SMALLINT` | NOT NULL REFERENCES academic_actor_type(actor_type_id) | STUDENT / INSTRUCTOR |
| `school_id` | `INTEGER` | NOT NULL REFERENCES school(school_id) |  |
| `started_on` | `DATE` | NOT NULL |  |
| `ended_on` | `DATE` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: elimina la duplicacion de identidad entre `student`, `teacher` y `person`. Una misma persona puede ser instructor y aprendiz sin duplicar sus datos.

#### `academic.enrollment`

Matricula de un aprendiz en una ficha.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `enrollment_id` | `BIGSERIAL` | PK |  |
| `academic_actor_id` | `BIGINT` | NOT NULL REFERENCES academic_actor(academic_actor_id) |  |
| `cohort_id` | `BIGINT` | NOT NULL REFERENCES cohort(cohort_id) |  |
| `enrolled_on` | `DATE` | NOT NULL |  |
| `enrollment_status_id` | `SMALLINT` | NOT NULL REFERENCES enrollment_status(enrollment_status_id) |  |

*Plus the full audit block from section 4.*

### Schema `scheduling`

#### `scheduling.environment`

Ambiente de formacion. El dominio lo llama `Environment`; el modelo lo llamaba `classroom`.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `environment_id` | `SERIAL` | PK |  |
| `school_id` | `INTEGER` | NOT NULL REFERENCES school(school_id) |  |
| `code` | `VARCHAR(30)` | NOT NULL |  |
| `name` | `VARCHAR(100)` | NOT NULL |  |
| `capacity` | `SMALLINT` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** Congruencia de nomenclatura entre 02 y 06.

#### `scheduling.schedule_block`

Bloque horario planificado.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `schedule_block_id` | `BIGSERIAL` | PK |  |
| `cohort_id` | `BIGINT` | NOT NULL REFERENCES cohort(cohort_id) |  |
| `course_id` | `INTEGER` | NOT NULL REFERENCES course(course_id) |  |
| `environment_id` | `INTEGER` | NOT NULL REFERENCES environment(environment_id) |  |
| `instructor_actor_id` | `BIGINT` | NOT NULL REFERENCES academic_actor(academic_actor_id) |  |
| `day_of_week` | `SMALLINT` | NOT NULL CHECK (day_of_week BETWEEN 1 AND 7) |  |
| `starts_at` | `TIME` | NOT NULL |  |
| `ends_at` | `TIME` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: un bloque por fila; nunca un campo `days` con varios dias.

#### `scheduling.class_session`

Instancia real de un bloque en una fecha.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `class_session_id` | `BIGSERIAL` | PK |  |
| `schedule_block_id` | `BIGINT` | NOT NULL REFERENCES schedule_block(schedule_block_id) |  |
| `session_date` | `DATE` | NOT NULL |  |
| `opened_at` | `TIMESTAMPTZ` | NULL |  |
| `closed_at` | `TIMESTAMPTZ` | NULL |  |
| `session_status_id` | `SMALLINT` | NOT NULL REFERENCES session_status(session_status_id) |  |

*Plus the full audit block from section 4.*

### Schema `attendance`

#### `attendance.attendance_record`

Asistencia de un aprendiz a una sesion.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `attendance_record_id` | `BIGSERIAL` | PK |  |
| `class_session_id` | `BIGINT` | NOT NULL REFERENCES class_session(class_session_id) |  |
| `academic_actor_id` | `BIGINT` | NOT NULL REFERENCES academic_actor(academic_actor_id) |  |
| `attendance_status_id` | `SMALLINT` | NOT NULL REFERENCES attendance_status(attendance_status_id) |  |
| `captured_at` | `TIMESTAMPTZ` | NULL |  |
| `capture_method_id` | `SMALLINT` | NOT NULL REFERENCES capture_method(capture_method_id) | FACIAL / MANUAL |
| `iot_device_id` | `BIGINT` | NULL REFERENCES iot_device(iot_device_id) |  |
| `match_score` | `NUMERIC(5,4)` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** UNIQUE(class_session_id, academic_actor_id): una sola asistencia por sesion y persona. Esa restriccion es la que hace innecesaria una tabla `attendance_history` aparte.

#### `attendance.justification`

Justificacion de inasistencia.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `justification_id` | `BIGSERIAL` | PK |  |
| `attendance_record_id` | `BIGINT` | NOT NULL REFERENCES attendance_record(attendance_record_id) |  |
| `justification_type_id` | `SMALLINT` | NOT NULL REFERENCES justification_type(justification_type_id) |  |
| `reason` | `TEXT` | NOT NULL |  |
| `submitted_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `reviewed_by` | `BIGINT` | NULL REFERENCES app_user(user_id) |  |
| `review_status_id` | `SMALLINT` | NOT NULL REFERENCES review_status(review_status_id) |  |

*Plus the full audit block from section 4.*

#### `attendance.supporting_document`

Soporte adjunto. Declarado en el dominio, ausente del modelo actual.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `supporting_document_id` | `BIGSERIAL` | PK |  |
| `justification_id` | `BIGINT` | NOT NULL REFERENCES justification(justification_id) |  |
| `file_name` | `VARCHAR(200)` | NOT NULL |  |
| `storage_uri` | `VARCHAR(500)` | NOT NULL |  |
| `mime_type` | `VARCHAR(80)` | NOT NULL |  |
| `size_bytes` | `BIGINT` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: un documento por fila; una justificacion puede tener varios.

### Schema `biometric`

#### `biometric.facial_template`

Plantilla biometrica versionada.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `facial_template_id` | `BIGSERIAL` | PK |  |
| `person_id` | `BIGINT` | NOT NULL REFERENCES person(person_id) |  |
| `template_version` | `SMALLINT` | NOT NULL |  |
| `encoding` | `BYTEA` | NOT NULL |  |
| `algorithm_id` | `SMALLINT` | NOT NULL REFERENCES biometric_algorithm(algorithm_id) |  |
| `enrolled_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `is_active` | `BOOLEAN` | NOT NULL DEFAULT true |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN y 3FN: saca la biometria de `person`. UNIQUE(person_id, template_version) y una sola activa a la vez.

### Schema `iot`

#### `iot.iot_device`

Dispositivo de captura.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `iot_device_id` | `BIGSERIAL` | PK |  |
| `serial_number` | `VARCHAR(60)` | NOT NULL UNIQUE |  |
| `device_model_id` | `SMALLINT` | NOT NULL REFERENCES device_model(device_model_id) |  |
| `device_status_id` | `SMALLINT` | NOT NULL REFERENCES device_status(device_status_id) |  |

*Plus the full audit block from section 4.*

#### `iot.device_assignment`

Asignacion del dispositivo a un ambiente, con vigencia.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `device_assignment_id` | `BIGSERIAL` | PK |  |
| `iot_device_id` | `BIGINT` | NOT NULL REFERENCES iot_device(iot_device_id) |  |
| `environment_id` | `INTEGER` | NOT NULL REFERENCES environment(environment_id) |  |
| `assigned_from` | `TIMESTAMPTZ` | NOT NULL |  |
| `assigned_to` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** Historico: no se sobreescribe la asignacion anterior.

### Schema `notification`

#### `notification.alert`

Alerta academica. Declarada en el dominio, ausente del modelo actual.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `alert_id` | `BIGSERIAL` | PK |  |
| `academic_actor_id` | `BIGINT` | NOT NULL REFERENCES academic_actor(academic_actor_id) |  |
| `alert_type_id` | `SMALLINT` | NOT NULL REFERENCES alert_type(alert_type_id) |  |
| `raised_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `resolved_at` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

## 7. Project catalogs

Beyond the shared ones, this project needs:

- `city`
- `academic_actor_type`
- `enrollment_status`
- `session_status`
- `attendance_status`
- `capture_method`
- `justification_type`
- `review_status`
- `biometric_algorithm`
- `device_model`
- `device_status`
- `alert_type`

Each catalog exists precisely so that its descriptive value is stored once. Putting a status name 
or a unit directly into a transactional table is the transitive dependency 3NF forbids.

## 8. What this counter-proposal does NOT do

- It does not change the project's purpose: Control de asistencia academica por reconocimiento facial en ambientes de formacion.
- It does not add features, screens or requirements that the team has not documented.
- It does not decide the database engine; the model is written in standard SQL types.
- It does not replace `02-domain`. If an entity here has no counterpart there, the gap is in `02`, 
  and closing it is part of the work.

---

*Counter-proposal generated from the review of ficha ADSO-3145556. Discuss it with the team before adopting it.*