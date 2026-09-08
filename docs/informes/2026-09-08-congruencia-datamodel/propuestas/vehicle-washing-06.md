# 06 - Data Model (counter-proposal)

> Project: **vehicle-washing** - repository `vehicle-w-docs`  
> Generated: 2026-09-08 - review of folders 02, 03 and 06  
> Golden rule applied: **the project scope is not changed**. Every entity below comes from
> what the team already declared in `02-domain` and `03-product`.

---

## 1. Why this counter-proposal

**Current state of `06-data`:** Plantilla intacta.

**Normalization level found:** Sin modelo

**02 -> 06 congruence:** 0% of the declared domain entities exist as tables.

Caso invertido respecto al resto de la ficha: definieron el producto sin modelar el dominio. Las entrevistas identifican clientes, vehiculos, servicios, citas, promociones y pagos, pero ninguno de esos conceptos llego a 02 ni a 06.

## 2. Findings addressed

| Level | Finding |
|---|---|
| **Congruencia invertida** | Es el caso opuesto al resto: `03-product` tiene 3613 lineas (discovery, backlog, roadmap, vision) y `02-domain` es **la plantilla intacta**. Se documento el producto sin modelar el dominio, y por tanto sin datos. |
| **Origen del modelo** | Al no existir 02-domain, las entidades de esta propuesta se derivan del propio `discovery-brief` y `product-backlog` del equipo: clientes, vehiculos, servicios, citas, promociones y pagos son los conceptos que ellos mismos identificaron en las entrevistas. |
| **2FN** | El precio depende del servicio y del tipo de vehiculo, no solo del servicio: vive en `service_price`. |
| **1FN** | La disponibilidad no es un campo de texto de horarios: se modela como franjas por dia en `business_hour`. |

## 3. Candidate domains from the existing documentation

These are the bounded contexts the project itself supports. No domain was invented.

| Domain | Scope |
|---|---|
| `Identity & Access` | Usuario, roles y sesiones. |
| `Customer` | Cliente y sus vehiculos. |
| `Service Catalog` | Servicios ofrecidos, precios por tipo de vehiculo y duracion. |
| `Appointment` | Cita agendada, franjas disponibles y estado. |
| `Execution` | Ejecucion del servicio y operario asignado. |
| `Promotion` | Promociones, vigencias y aplicacion a una cita. |
| `Payment` | Pagos y comprobantes. |

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

### Schema `customer`

#### `customer.customer`

Cliente del establecimiento.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `customer_id` | `BIGSERIAL` | PK |  |
| `person_id` | `BIGINT` | NOT NULL UNIQUE REFERENCES person(person_id) |  |
| `loyalty_points` | `INTEGER` | NOT NULL DEFAULT 0 |  |
| `customer_since` | `DATE` | NOT NULL DEFAULT CURRENT_DATE |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: no duplica nombre ni telefono; los alcanza por `person`.

#### `customer.vehicle_type`

Tipo de vehiculo: moto, automovil, camioneta.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `vehicle_type_id` | `SMALLSERIAL` | PK |  |
| `code` | `VARCHAR(30)` | NOT NULL UNIQUE |  |
| `name` | `VARCHAR(60)` | NOT NULL |  |
| `size_factor` | `NUMERIC(4,2)` | NOT NULL |  |

*Plus the full audit block from section 4.*

#### `customer.customer_vehicle`

Vehiculo del cliente.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `customer_vehicle_id` | `BIGSERIAL` | PK |  |
| `customer_id` | `BIGINT` | NOT NULL REFERENCES customer(customer_id) |  |
| `license_plate` | `VARCHAR(10)` | NOT NULL UNIQUE |  |
| `vehicle_type_id` | `SMALLINT` | NOT NULL REFERENCES vehicle_type(vehicle_type_id) |  |
| `brand` | `VARCHAR(50)` | NULL |  |
| `color` | `VARCHAR(30)` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: un vehiculo por fila. Un cliente con tres carros tiene tres filas, no un campo con placas separadas por comas.

### Schema `catalog`

#### `catalog.service`

Servicio ofrecido.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `service_id` | `SERIAL` | PK |  |
| `code` | `VARCHAR(30)` | NOT NULL UNIQUE |  |
| `name` | `VARCHAR(100)` | NOT NULL |  |
| `description` | `TEXT` | NULL |  |
| `service_category_id` | `SMALLINT` | NOT NULL REFERENCES service_category(service_category_id) |  |
| `is_active` | `BOOLEAN` | NOT NULL DEFAULT true |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: el precio NO va aqui, porque depende del tipo de vehiculo.

#### `catalog.service_price`

Precio y duracion por servicio y tipo de vehiculo, con vigencia.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `service_price_id` | `BIGSERIAL` | PK |  |
| `service_id` | `INTEGER` | NOT NULL REFERENCES service(service_id) |  |
| `vehicle_type_id` | `SMALLINT` | NOT NULL REFERENCES vehicle_type(vehicle_type_id) |  |
| `price` | `NUMERIC(12,2)` | NOT NULL |  |
| `estimated_minutes` | `SMALLINT` | NOT NULL |  |
| `valid_from` | `DATE` | NOT NULL |  |
| `valid_to` | `DATE` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 2FN: la dependencia es del par completo (servicio, tipo de vehiculo). Guardar el precio en `service` seria una dependencia parcial.

### Schema `appointment`

#### `appointment.business_hour`

Franja de atencion por dia de la semana.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `business_hour_id` | `SMALLSERIAL` | PK |  |
| `day_of_week` | `SMALLINT` | NOT NULL CHECK (day_of_week BETWEEN 1 AND 7) |  |
| `opens_at` | `TIME` | NOT NULL |  |
| `closes_at` | `TIME` | NOT NULL |  |
| `concurrent_bays` | `SMALLINT` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: una franja por fila, en lugar de un campo de texto con el horario.

#### `appointment.appointment`

Cita agendada.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `appointment_id` | `BIGSERIAL` | PK |  |
| `customer_vehicle_id` | `BIGINT` | NOT NULL REFERENCES customer_vehicle(customer_vehicle_id) |  |
| `scheduled_start` | `TIMESTAMPTZ` | NOT NULL |  |
| `scheduled_end` | `TIMESTAMPTZ` | NOT NULL |  |
| `appointment_status_id` | `SMALLINT` | NOT NULL REFERENCES appointment_status(appointment_status_id) |  |
| `booked_by` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `cancellation_reason_id` | `SMALLINT` | NULL REFERENCES cancellation_reason(reason_id) |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: el estado y el motivo de cancelacion salen a catalogo.

#### `appointment.appointment_service`

Servicios incluidos en una cita.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `appointment_service_id` | `BIGSERIAL` | PK |  |
| `appointment_id` | `BIGINT` | NOT NULL REFERENCES appointment(appointment_id) |  |
| `service_price_id` | `BIGINT` | NOT NULL REFERENCES service_price(service_price_id) | Precio vigente al agendar |
| `quantity` | `SMALLINT` | NOT NULL DEFAULT 1 |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1FN: un servicio por fila. Resuelve la N:M cita-servicio y congela la tarifa por FK, no por copia.

### Schema `execution`

#### `execution.operator`

Operario que ejecuta el lavado.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `operator_id` | `SERIAL` | PK |  |
| `person_id` | `BIGINT` | NOT NULL UNIQUE REFERENCES person(person_id) |  |
| `hired_on` | `DATE` | NOT NULL |  |
| `is_active` | `BOOLEAN` | NOT NULL DEFAULT true |  |

*Plus the full audit block from section 4.*

#### `execution.service_execution`

Ejecucion real del servicio.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `service_execution_id` | `BIGSERIAL` | PK |  |
| `appointment_service_id` | `BIGINT` | NOT NULL UNIQUE REFERENCES appointment_service(appointment_service_id) |  |
| `operator_id` | `INTEGER` | NOT NULL REFERENCES operator(operator_id) |  |
| `started_at` | `TIMESTAMPTZ` | NULL |  |
| `finished_at` | `TIMESTAMPTZ` | NULL |  |
| `execution_status_id` | `SMALLINT` | NOT NULL REFERENCES execution_status(execution_status_id) |  |
| `quality_rating` | `SMALLINT` | NULL CHECK (quality_rating BETWEEN 1 AND 5) |  |

*Plus the full audit block from section 4.*

> **Normalization:** Responde a la falta de visibilidad del servicio que el equipo identifico en sus entrevistas.

### Schema `promotion`

#### `promotion.promotion`

Promocion vigente.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `promotion_id` | `SERIAL` | PK |  |
| `code` | `VARCHAR(30)` | NOT NULL UNIQUE |  |
| `name` | `VARCHAR(100)` | NOT NULL |  |
| `discount_type_id` | `SMALLINT` | NOT NULL REFERENCES discount_type(discount_type_id) | PERCENT / FIXED |
| `discount_value` | `NUMERIC(12,2)` | NOT NULL |  |
| `valid_from` | `DATE` | NOT NULL |  |
| `valid_to` | `DATE` | NOT NULL |  |
| `max_redemptions` | `INTEGER` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** Responde a la incertidumbre de promociones que el equipo documento.

#### `promotion.appointment_promotion`

Promocion aplicada a una cita.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `appointment_promotion_id` | `BIGSERIAL` | PK |  |
| `appointment_id` | `BIGINT` | NOT NULL REFERENCES appointment(appointment_id) |  |
| `promotion_id` | `INTEGER` | NOT NULL REFERENCES promotion(promotion_id) |  |
| `applied_amount` | `NUMERIC(12,2)` | NOT NULL | Descuento efectivo calculado al aplicar |

*Plus the full audit block from section 4.*

> **Normalization:** Se guarda el monto aplicado porque la promocion puede cambiar despues; el descuento concedido es un hecho historico.

### Schema `payment`

#### `payment.payment`

Cobro de la cita.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `payment_id` | `BIGSERIAL` | PK |  |
| `appointment_id` | `BIGINT` | NOT NULL REFERENCES appointment(appointment_id) |  |
| `amount` | `NUMERIC(12,2)` | NOT NULL |  |
| `payment_method_type_id` | `SMALLINT` | NOT NULL REFERENCES payment_method_type(payment_method_type_id) |  |
| `payment_status_id` | `SMALLINT` | NOT NULL REFERENCES payment_status(payment_status_id) |  |
| `processed_at` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

## 7. Project catalogs

Beyond the shared ones, this project needs:

- `service_category`
- `appointment_status`
- `cancellation_reason`
- `execution_status`
- `discount_type`
- `payment_method_type`
- `payment_status`

Each catalog exists precisely so that its descriptive value is stored once. Putting a status name 
or a unit directly into a transactional table is the transitive dependency 3NF forbids.

## 8. What this counter-proposal does NOT do

- It does not change the project's purpose: Lavado de vehiculos con agendamiento de citas, catalogo de servicios y promociones.
- It does not add features, screens or requirements that the team has not documented.
- It does not decide the database engine; the model is written in standard SQL types.
- It does not replace `02-domain`. If an entity here has no counterpart there, the gap is in `02`, 
  and closing it is part of the work.

---

*Counter-proposal generated from the review of ficha ADSO-3145556. Discuss it with the team before adopting it.*