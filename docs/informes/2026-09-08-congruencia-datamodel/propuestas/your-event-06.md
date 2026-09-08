# 06 - Data Model (counter-proposal)

> Project: **your-event** - repository `yev-docs`  
> Generated: 2026-09-08 - review of folders 02, 03 and 06  
> Golden rule applied: **the project scope is not changed**. Every entity below comes from
> what the team already declared in `02-domain` and `03-product`.

---

## 1. Why this counter-proposal

**Current state of `06-data`:** Plantilla intacta (7369 bytes, identica al scaffold).

**Normalization level found:** Sin modelo

**02 -> 06 congruence:** 0% of the declared domain entities exist as tables.

Dominio claro y pequeno, gobernanza y contexto completos, y ni una tabla. Es el proyecto donde menos cuesta cerrar la brecha: cinco entidades bien definidas se traducen casi directamente.

## 2. Findings addressed

| Level | Finding |
|---|---|
| **Congruencia** | El dominio declara `User`, `Event`, `Seat`, `Ticket` y `Order`, con reglas de negocio explicitas, y no hay ninguna tabla: `06-data/models.md` es la plantilla. |
| **3FN** | El asiento pertenece al recinto, no al evento. Modelarlo colgado del evento obligaria a duplicar el mapa de asientos en cada funcion. |
| **2FN** | El precio depende de la zona y la funcion, no del asiento individual: vive en `event_zone_price`. |
| **1FN** | La reserva temporal necesita tabla propia con expiracion; no es un booleano dentro de `seat`, que impediria dos funciones simultaneas. |

## 3. Candidate domains from the existing documentation

These are the bounded contexts the project itself supports. No domain was invented.

| Domain | Scope |
|---|---|
| `Identity & Access` | Usuario, roles y sesiones. |
| `Venue` | Recinto, zonas y asientos fisicos. |
| `Event` | Evento, funcion y disponibilidad por zona. |
| `Ticketing` | Entrada emitida y su ciclo de vida. |
| `Order` | Pedido, lineas y reserva temporal de asiento. |
| `Payment` | Pagos y reembolsos. |

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

### Schema `venue`

#### `venue.venue`

Recinto.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `venue_id` | `SERIAL` | PK |  |
| `name` | `VARCHAR(150)` | NOT NULL |  |
| `address_line` | `VARCHAR(200)` | NOT NULL |  |
| `city_id` | `INTEGER` | NOT NULL REFERENCES city(city_id) |  |
| `total_capacity` | `INTEGER` | NOT NULL |  |

*Plus the full audit block from section 4.*

#### `venue.venue_zone`

Zona o localidad del recinto.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `venue_zone_id` | `SERIAL` | PK |  |
| `venue_id` | `INTEGER` | NOT NULL REFERENCES venue(venue_id) |  |
| `code` | `VARCHAR(30)` | NOT NULL |  |
| `name` | `VARCHAR(80)` | NOT NULL |  |

*Plus the full audit block from section 4.*

#### `venue.seat`

Asiento fisico. Pertenece al recinto, no al evento.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `seat_id` | `BIGSERIAL` | PK |  |
| `venue_zone_id` | `INTEGER` | NOT NULL REFERENCES venue_zone(venue_zone_id) |  |
| `row_label` | `VARCHAR(10)` | NOT NULL |  |
| `seat_number` | `SMALLINT` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: UNIQUE(venue_zone_id, row_label, seat_number). El mapa se define una vez y se reutiliza en todas las funciones.

### Schema `event`

#### `event.event`

Evento comercial.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `event_id` | `BIGSERIAL` | PK |  |
| `name` | `VARCHAR(200)` | NOT NULL |  |
| `event_category_id` | `SMALLINT` | NOT NULL REFERENCES event_category(event_category_id) |  |
| `organizer_user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `description` | `TEXT` | NULL |  |
| `event_status_id` | `SMALLINT` | NOT NULL REFERENCES event_status(event_status_id) |  |

*Plus the full audit block from section 4.*

#### `event.event_session`

Funcion concreta de un evento en fecha y recinto.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `event_session_id` | `BIGSERIAL` | PK |  |
| `event_id` | `BIGINT` | NOT NULL REFERENCES event(event_id) |  |
| `venue_id` | `INTEGER` | NOT NULL REFERENCES venue(venue_id) |  |
| `starts_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `ends_at` | `TIMESTAMPTZ` | NULL |  |
| `doors_open_at` | `TIMESTAMPTZ` | NULL |  |
| `sales_open_at` | `TIMESTAMPTZ` | NOT NULL |  |
| `sales_close_at` | `TIMESTAMPTZ` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** Un evento con varias fechas no duplica su descripcion ni su organizador.

#### `event.event_zone_price`

Precio por zona y funcion.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `event_zone_price_id` | `BIGSERIAL` | PK |  |
| `event_session_id` | `BIGINT` | NOT NULL REFERENCES event_session(event_session_id) |  |
| `venue_zone_id` | `INTEGER` | NOT NULL REFERENCES venue_zone(venue_zone_id) |  |
| `price` | `NUMERIC(12,2)` | NOT NULL |  |
| `currency_code` | `CHAR(3)` | NOT NULL DEFAULT 'COP' |  |

*Plus the full audit block from section 4.*

> **Normalization:** 2FN: el precio depende de zona mas funcion, no del asiento. UNIQUE(event_session_id, venue_zone_id).

### Schema `order`

#### `order.seat_hold`

Reserva temporal de un asiento mientras se completa la compra.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `seat_hold_id` | `BIGSERIAL` | PK |  |
| `event_session_id` | `BIGINT` | NOT NULL REFERENCES event_session(event_session_id) |  |
| `seat_id` | `BIGINT` | NOT NULL REFERENCES seat(seat_id) |  |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `held_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |
| `expires_at` | `TIMESTAMPTZ` | NOT NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** Indice unico parcial sobre (event_session_id, seat_id) cuando no ha expirado: impide vender dos veces el mismo asiento.

#### `order.customer_order`

Pedido de compra.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `customer_order_id` | `BIGSERIAL` | PK |  |
| `user_id` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |
| `order_number` | `VARCHAR(30)` | NOT NULL UNIQUE |  |
| `order_status_id` | `SMALLINT` | NOT NULL REFERENCES order_status(order_status_id) |  |
| `placed_at` | `TIMESTAMPTZ` | NOT NULL DEFAULT now() |  |

*Plus the full audit block from section 4.*

> **Normalization:** 3FN: sin `total`. El total se calcula desde las lineas; asi no puede quedar desincronizado.

#### `order.order_line`

Linea del pedido: un asiento en una funcion.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `order_line_id` | `BIGSERIAL` | PK |  |
| `customer_order_id` | `BIGINT` | NOT NULL REFERENCES customer_order(customer_order_id) |  |
| `event_session_id` | `BIGINT` | NOT NULL REFERENCES event_session(event_session_id) |  |
| `seat_id` | `BIGINT` | NOT NULL REFERENCES seat(seat_id) |  |
| `unit_price` | `NUMERIC(12,2)` | NOT NULL | Precio congelado al comprar |

*Plus the full audit block from section 4.*

> **Normalization:** Excepcion justificada: el precio se congela porque un cambio de tarifa posterior no puede alterar una compra ya hecha.

### Schema `ticketing`

#### `ticketing.ticket`

Entrada emitida a partir de una linea de pedido.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `ticket_id` | `BIGSERIAL` | PK |  |
| `order_line_id` | `BIGINT` | NOT NULL UNIQUE REFERENCES order_line(order_line_id) |  |
| `ticket_code` | `VARCHAR(40)` | NOT NULL UNIQUE | Codigo QR |
| `holder_person_id` | `BIGINT` | NULL REFERENCES person(person_id) |  |
| `ticket_status_id` | `SMALLINT` | NOT NULL REFERENCES ticket_status(ticket_status_id) |  |
| `checked_in_at` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** 1:1 con la linea: una entrada por asiento vendido.

### Schema `payment`

#### `payment.payment`

Intento de cobro del pedido.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `payment_id` | `BIGSERIAL` | PK |  |
| `customer_order_id` | `BIGINT` | NOT NULL REFERENCES customer_order(customer_order_id) |  |
| `amount` | `NUMERIC(12,2)` | NOT NULL |  |
| `currency_code` | `CHAR(3)` | NOT NULL DEFAULT 'COP' |  |
| `payment_method_type_id` | `SMALLINT` | NOT NULL REFERENCES payment_method_type(payment_method_type_id) |  |
| `payment_status_id` | `SMALLINT` | NOT NULL REFERENCES payment_status(payment_status_id) |  |
| `gateway_reference` | `VARCHAR(100)` | NULL |  |
| `processed_at` | `TIMESTAMPTZ` | NULL |  |

*Plus the full audit block from section 4.*

> **Normalization:** Un intento fallido tambien deja fila; nunca se borra.

#### `payment.refund`

Reembolso sobre un pago.

| Column | Type | Constraints | Notes |
|---|---|---|---|
| `refund_id` | `BIGSERIAL` | PK |  |
| `payment_id` | `BIGINT` | NOT NULL REFERENCES payment(payment_id) |  |
| `amount` | `NUMERIC(12,2)` | NOT NULL |  |
| `refund_reason_id` | `SMALLINT` | NOT NULL REFERENCES refund_reason(refund_reason_id) |  |
| `processed_at` | `TIMESTAMPTZ` | NULL |  |
| `authorized_by` | `BIGINT` | NOT NULL REFERENCES app_user(user_id) |  |

*Plus the full audit block from section 4.*

## 7. Project catalogs

Beyond the shared ones, this project needs:

- `city`
- `event_category`
- `event_status`
- `order_status`
- `ticket_status`
- `payment_method_type`
- `payment_status`
- `refund_reason`

Each catalog exists precisely so that its descriptive value is stored once. Putting a status name 
or a unit directly into a transactional table is the transitive dependency 3NF forbids.

## 8. What this counter-proposal does NOT do

- It does not change the project's purpose: Venta de entradas para eventos con seleccion de asiento y compra en linea.
- It does not add features, screens or requirements that the team has not documented.
- It does not decide the database engine; the model is written in standard SQL types.
- It does not replace `02-domain`. If an entity here has no counterpart there, the gap is in `02`, 
  and closing it is part of the work.

---

*Counter-proposal generated from the review of ficha ADSO-3145556. Discuss it with the team before adopting it.*