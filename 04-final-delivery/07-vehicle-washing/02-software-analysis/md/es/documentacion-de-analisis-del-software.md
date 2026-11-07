# Análisis del Software: Vistas Dinámicas del Sistema LavaRápido

Juan Camilo Guilombo Osorio, Brayan Estiven Patiño Cabrera y Merari Urbano Quintero

Centro de la Industria, la Empresa y los Servicios, Servicio Nacional de Aprendizaje (SENA), Regional Huila

Tecnólogo en Análisis y Desarrollo de Software (ADSO), ficha 3145556

Instructora: Karol Daniela Correa

Versión 2.8, 27 de octubre de 2026

---

## Introducción

Este documento muestra cómo se comporta LavaRápido cuando alguien lo usa: qué pasa, en qué orden y entre qué pantallas y servicios. Está organizado en cuatro partes:

1. **Casos de uso por actor**, con sus relaciones `«include»`/`«extend»`.
2. **Diagramas de secuencia** de los flujos principales: de la pantalla al gateway, del gateway al servicio, del servicio a la base de datos o a otro servicio.
3. **Diagramas de actividades** de los procesos de negocio.
4. **Diagramas de estados** de las entidades que cambian de estado (reserva, pago, ejecución del servicio, promoción e informe de inspección).

El 21 de octubre de 2026 se actualizaron, contra el código de ese día, el cierre de sesión que deja sin efecto el token (sección 2.8), el candado de la asignación de operarios (sección 2.4), el comprobante de máximo 2 MB y el efectivo que solo aparece si su cuenta está activa (secciones 2.5 y 3.4), y el tratamiento de las reservas que terminaron sin que nadie las cerrara (secciones 4.1 y 4.3): el sistema solo cambia el estado cuando no hay duda y lo demás pasa a la lista "Por cerrar" del administrador. El 20 de octubre de 2026 el contenido se revisó otra vez contra el código: el cliente solo cancela (la reprogramación la hace el administrador), se quitó el cambio de rol de las cuentas, se corrigieron las secuencias y los estados que no coincidían, y los casos de uso y diagramas se dibujaron de nuevo con líneas rectas y sin cruces. El 19 de octubre de 2026 se agregaron los casos de uso CU31 y CU32, la recuperación de la contraseña (sección 2.10) y el ciclo de vida de la cuenta (sección 4.6), y las capturas del inventario de pantallas se tomaron de nuevo con datos de demostración. El 17 de octubre de 2026 se agregaron el consentimiento de los documentos legales en el registro (sección 2.1) y el informe de inspección desde la aplicación móvil (sección 2.9). Los diagramas están hechos en Mermaid. Se revisaron contra el código y el sistema en ejecución el 9 de octubre de 2026, y se actualizaron contra el código el 15 de octubre de 2026 con el bloqueo de cuenta, la asignación automática, la elección de operario, la ventana de inicio del servicio, la alerta de reserva sin operario y el informe de inspección. Al final está el inventario de pantallas, con capturas por rol. El 23 de octubre de 2026 (versión 2.5) se agregó que recuperar la contraseña revoca todas las sesiones (sección 2.10) y que, si security-service no responde, el gateway deja pasar la petición (*fail-open*); el resto del análisis sigue vigente contra *develop* de ese día. El 24 de octubre de 2026 (versión 2.6), contra *develop* de ese día, la matriz de permisos pasó a ser de solo lectura (secciones 2.7 y 3.6 e inventario de pantallas) y se quitó la cancelación automática de las reservas programadas vencidas, porque todas nacen confirmadas (sección 4.1). El 26 de octubre de 2026 (versión 2.7), contra *develop* de ese día, se agregó el pago al reservar: el cliente elige cómo va a pagar al reservar, con un máximo de una reserva activa en efectivo (secciones 2.2 y 3.2); payment-service avisa con `payment.reported` cuando el cliente sube el comprobante (sección 2.5), y una reserva con pago digital que llega a su plazo sin comprobante se cancela sola con el motivo `PAYMENT_TIMEOUT`, después de preguntarle a payment-service (secciones 2.11 y 4.1). El 27 de octubre de 2026 (versión 2.8), contra `main` de ese día (booking *v1.1.1*), una lista de servicios nula, vacía o de más de 10 responde 400 `VALIDATION_ERROR` en lugar de 500, y las horas alternativas ya no incluyen las que chocan con otra reserva del mismo vehículo (secciones 2.2 y 3.2). Las capturas del rol Cliente del inventario de pantallas (sección 5.3) son las del 19 de octubre y no muestran esos cambios.

La especificación completa de cada requisito y caso de uso (actores, precondición, flujo normal, flujos alternos y postcondición) está en el *Informe de especificación de requisitos*, secciones 5 y 7.3; aquí se citan por su identificador (RF-XXX, CUXX).

---

## 1. Casos de Uso por Actor

### 1.1 Diagrama de Casos de Uso

Los 32 casos de uso se presentan en cuatro diagramas, uno por actor, agrupados por módulo.

**Casos de uso comunes a los tres roles**

```mermaid
flowchart LR
    Usuario(["Cualquier usuario"])
    CU01(["CU01 Iniciar sesión"])
    CU03(["CU03 Recuperar contraseña"])
    CU04(["CU04 Cambiar contraseña"])
    CU16(["CU16 Consultar panel"])
    CU17(["CU17 Gestionar notificaciones"])
    CU18(["CU18 Personalizar tema visual"])
    CU19(["CU19 Seleccionar idioma"])
    CU31(["CU31 Gestionar la cuenta propia"])

    Usuario --- CU01
    Usuario --- CU03
    Usuario --- CU04
    Usuario --- CU16
    Usuario --- CU17
    Usuario --- CU18
    Usuario --- CU19
    Usuario --- CU31

    classDef actor fill:#08427b,color:#fff,stroke:#052e56
    class Usuario actor
```

**Casos de uso del Cliente**

```mermaid
flowchart LR
    Cliente(["Cliente"])
    Sistema(["Sistema"])

    subgraph CTA["Cuenta y vehículos"]
        CU02(["CU02 Crear cuenta"])
        CU26(["CU26 Gestionar vehículos"])
    end

    subgraph RES["Reservas"]
        CU05(["CU05 Solicitar reserva"])
        CU06(["CU06 Confirmar reserva según disponibilidad"])
        CU07(["CU07 Modificar o cancelar reserva"])
    end

    subgraph PAG["Pagos y fidelización"]
        CU12(["CU12 Pagar una reserva"])
        CU14(["CU14 Consultar historial de pagos"])
        CU25(["CU25 Aplicar promoción en el pago"])
        CU15(["CU15 Calificar servicio"])
    end

    subgraph INS["Inspección"]
        CU30(["CU30 Registrar informe de inspección"])
    end

    Cliente --- CU02
    Cliente --- CU26
    Cliente --- CU05
    Cliente --- CU07
    Cliente --- CU12
    Cliente --- CU14
    Cliente --- CU15
    Cliente --- CU25
    Cliente -. "consulta por enlace" .- CU30
    Sistema --- CU06

    CU05 -. "«include»" .-> CU06
    CU25 -. "«extend»" .-> CU12

    classDef actor fill:#08427b,color:#fff,stroke:#052e56
    class Cliente,Sistema actor
```

**Casos de uso del Operario**

```mermaid
flowchart LR
    Operario(["Operario"])
    CU10(["CU10 Consultar servicios asignados"])
    CU11(["CU11 Iniciar y finalizar servicio"])
    CU22(["CU22 Gestionar operarios y su disponibilidad"])

    Operario --- CU10
    Operario --- CU11
    Operario -. "consulta" .- CU22

    classDef actor fill:#08427b,color:#fff,stroke:#052e56
    class Operario actor
```

**Casos de uso del Administrador y del Sistema**

```mermaid
flowchart LR
    Administrador(["Administrador"])
    Sistema(["Sistema"])

    subgraph AUTH["Usuarios"]
        CU27(["CU27 Administrar usuarios y permisos"])
    end
    subgraph CAT["Catálogo, horario y bahías"]
        CU20(["CU20 Configurar horario, bahías y datos del negocio"])
        CU21(["CU21 Gestionar catálogo y precios"])
    end
    subgraph RES["Reservas y operación"]
        CU08(["CU08 Asignar operario automáticamente"])
        CU09(["CU09 Asignar o reasignar operario"])
        CU22b(["CU22 Gestionar operarios y su disponibilidad"])
        CU29(["CU29 Gestionar reservas"])
        CU30b(["CU30 Registrar informe de inspección"])
    end
    subgraph PAG["Pagos y promociones"]
        CU13(["CU13 Revisar pago"])
        CU28(["CU28 Gestionar cuentas de pago"])
        CU24(["CU24 Gestionar promociones"])
    end
    subgraph REP["Reportes"]
        CU23(["CU23 Consultar reportes"])
    end
    subgraph AUT["Procesos automáticos"]
        CU32(["CU32 Ejecutar procesos automáticos"])
    end

    Administrador --- CU27
    Administrador --- CU20
    Administrador --- CU21
    Administrador --- CU09
    Administrador --- CU22b
    Administrador --- CU29
    Administrador --- CU30b
    Administrador --- CU13
    Administrador --- CU28
    Administrador --- CU24
    Administrador --- CU23
    Sistema --- CU08
    Sistema --- CU32

    classDef actor fill:#08427b,color:#fff,stroke:#052e56
    class Administrador,Sistema actor
```

*Nota.* `«include»` indica que CU05 siempre ejecuta CU06; `«extend»` indica que CU25 amplía opcionalmente CU12. CU22 aparece como consulta para el operario y como gestión para el administrador, que es quien registra los turnos, las ausencias y la ficha del operario (RF-022, RF-038). El sistema ejecuta CU08 cada vez que se confirma una reserva, y CU32 agrupa los procesos que corren sin que nadie los pida (recordatorios, alertas, avisos y primer administrador). El cliente no registra el informe de inspección (CU30): lo consulta por el enlace que le comparte el administrador. En CU07 el cliente cancela desde el historial o la pantalla de pago; la nueva fecha u hora la fija el administrador desde Reservas (CU29), porque ni la web ni la app tienen una pantalla de reprogramación para el cliente. En CU27 el rol se elige al crear la cuenta; la lista de usuarios no lo cambia.

### 1.2 Matriz de Actores y Casos de Uso

| Actor | Casos de uso |
|---|---|
| Todos los roles | CU01, CU03, CU04, CU16, CU17, CU18, CU19, CU31 |
| Cliente | CU02, CU05, CU06, CU07, CU12, CU14, CU15, CU25, CU26, CU30 (consulta) |
| Operario | CU10, CU11, CU22 (consulta) |
| Administrador | CU09, CU13, CU20, CU21, CU22, CU23, CU24, CU27, CU28, CU29, CU30 |
| Sistema | CU06 (incluido en CU05), CU08, CU32 |

La especificación de cada uno de los 32 casos de uso (precondición, flujo normal, flujos alternos, postcondición, estado) está en el *Informe de especificación de requisitos*, Tablas 23 a 54.

---

## 2. Diagramas de Secuencia

Cada diagrama sigue el recorrido de la petición (pantalla, gateway, servicios, base de datos y RabbitMQ) con los nombres de clases y endpoints del código. Los cinco primeros parten de los diagramas de `vehicle-w-docs/08-uml/diagrams/source/`; los siguientes cubren la fidelización, los roles y permisos, el bloqueo de cuenta y el informe de inspección.

### 2.1 Registro y bienvenida (RF-001, RF-018)

```mermaid
sequenceDiagram
    autonumber
    actor C as Cliente
    participant W as Web
    participant GW as API Gateway
    participant S as security-service
    participant MQ as RabbitMQ
    participant CU as customer-service
    participant N as notification-service
    participant M as SMTP

    C->>W: Llena el formulario y marca los Términos y la Política de Tratamiento de Datos
    W->>GW: POST /api/v1/auth/register (acceptsTerms, acceptsDataPolicy)
    GW->>S: enruta
    S->>S: Valida (política de contraseña, correo/documento únicos), guarda persona + cuenta (CLIENT) y la fecha de aceptación de cada documento legal
    S-->>W: 201 Created
    S-)MQ: UserRegistered (security.user_registered), tras el commit
    par perfil de cliente
        MQ-)CU: UserRegistered
        CU->>CU: Crea el perfil de cliente (idempotente)
    and bienvenida
        MQ-)N: UserRegistered
        N->>N: Crea notificación WELCOME (idempotente: processed_event)
        N->>M: Correo de bienvenida (la dirección viene en el evento)
    end
    C->>W: Inicia sesión
    W->>GW: POST /api/v1/auth/login
    GW->>S: enruta
    S-->>W: 200 JWT (HS256, 1 h, claim roles)
```

*Fuente:* `security-service` (`AccountRegistrationService`, `AuthenticationService`, migración `039-security-app-user-legal-consent.sql`), evento `security.user_registered`. La web y la app móvil no envían el formulario hasta que ambas casillas están marcadas; el servidor guarda las fechas solo si las dos llegan aceptadas.

### 2.2 Reservar un lavado (RF-005, RF-006, RF-010)

```mermaid
sequenceDiagram
    autonumber
    actor C as Cliente
    participant W as Web (vía gateway)
    participant B as booking-service
    participant CU as customer-service
    participant DB as SQL Server
    participant MQ as RabbitMQ
    participant N as notification-service
    participant S as security-service
    participant OP as operations-service

    C->>W: Elige vehículo, servicio y fecha
    W->>B: GET /api/v1/bookings/availability?date=…&vehicleTypeId=…&serviceIds=…
    B->>DB: horario, excepciones, bahías, reservas del día
    B-->>W: horas libres
    C->>W: Elige una hora
    W->>OP: GET /api/v1/operators/available?date=…&time=…&minutes=…
    OP-->>W: operarios libres (primer nombre y calificación)
    C->>W: Elige un operario (opcional)
    C->>W: ¿Cómo vas a pagar? Pago digital ahora o efectivo en el lavadero, y confirma
    W->>B: POST /api/v1/bookings {…, preferredOperatorId, paymentMode}
    alt serviceIds nulo, vacío o con más de 10 servicios
        B-->>W: 400 VALIDATION_ERROR
    end
    alt sin paymentMode o con un valor no válido (solo el cliente)
        B-->>W: 400 INVALID_PAYMENT_MODE
    end
    B->>CU: GET /api/v1/vehicles/{id} (JWT del cliente)
    CU-->>B: vehículo + tipo (se valida la propiedad, INV-BOOK-002)
    B->>DB: sp_getapplock (bloqueo del horario)
    alt CASH y el cliente ya tiene 1 reserva activa en efectivo hecha por él
        B-->>W: 409 CASH_BOOKING_LIMIT
    else hay bahía libre y el vehículo no tiene otra reserva a esa hora
        B->>DB: INSERT reserva CONFIRMED + bahía + líneas con precios congelados<br/>+ payment_mode y, si es DIGITAL, payment_deadline = min(ahora + 30 min, inicio)
        B-->>W: 201 reserva (código RES-000042, paymentMode, paymentDeadline)
        W-->>C: DIGITAL: va a pagar ("Sube el comprobante antes de las HH:mm")<br/>CASH: "Pagas en efectivo en el lavadero"
        B-)MQ: BookingConfirmed (booking.confirmed, con preferredOperatorId), tras el commit
        MQ-)OP: BookingConfirmed: asignación automática (sección 2.4)
        MQ-)N: BookingConfirmed
        N->>S: GET /internal/v1/users/{id}/contact (X-Internal-Key)
        N->>N: Notificación BOOKING_CONFIRMED + recordatorios (24 h, 1 h)
        N-->>C: bandeja + push + correo
    else el vehículo ya tiene otra reserva a esa hora
        B-->>W: 409 VEHICLE_ALREADY_BOOKED
    else horario ocupado
        B-->>W: 409 SLOT_UNAVAILABLE + motivo + hasta 5 horas alternativas sin cruce con otras reservas del vehículo
    end
```

*Fuente:* `booking-service` (`BookingService.create`, `Booking.confirmNew`, `AvailabilityPolicy`, `PaymentPolicy`), `operations-service` (`AvailableOperatorsController`), contratos en `07-api/contracts/openapi/`. El límite de efectivo (`BOOKING_CASH_ACTIVE_LIMIT`, 1) se cuenta con la agenda bloqueada y solo con las reservas activas (Programada, Confirmada o En proceso) que hizo el propio cliente (`booked_by`). El administrador no está obligado a enviar `paymentMode`, y sus reservas no tienen plazo ni límite. Desde el 27 de octubre de 2026, una reserva lleva como máximo 10 servicios (`BookingDtos.MAX_SERVICES`, también al reprogramar): una lista nula, vacía o más larga responde 400 `VALIDATION_ERROR` en lugar de 500, y las horas alternativas quitan las que se cruzan con otra reserva del mismo vehículo (`AvailabilityPolicy.withoutBusyTimes`).

### 2.3 Recordatorios y alerta de reserva sin operario (ADR-011)

```mermaid
sequenceDiagram
    autonumber
    participant MQ as RabbitMQ
    participant N as notification-service
    participant DB as notification.booking_reminder
    participant J as ReminderScheduler
    participant S as security-service
    actor C as Cliente

    MQ-)N: BookingConfirmed (nueva o reprogramada)
    N->>DB: Reemplaza recordatorios pendientes: uno por anticipación (24 h, 1 h) que siga en el futuro
    MQ-)N: BookingCancelled, ServiceStarted o ServiceCompleted
    N->>DB: Cancela los recordatorios pendientes de esa reserva (borrado lógico)
    MQ-)N: OperatorAssigned
    N->>DB: La reserva ya tiene operario (booking_tracking)
    loop cada 5 minutos (REMINDER_CHECK_INTERVAL)
        J->>DB: Recordatorios vencidos (remind_at <= ahora, no enviados)
        J->>S: GET /internal/v1/users/{id}/contact (X-Internal-Key): correo e idioma
        J->>N: Notificación BOOKING_REMINDER en el idioma de la cuenta
        N-->>C: bandeja + push + correo
        J->>DB: marca sent_at (nunca se envía dos veces)
        J->>DB: Reservas confirmadas que empiezan en menos de 2 h sin operario y sin alerta
        J->>N: Alerta a cada administrador activo y marca unassigned_alert_at
    end
```

*Fuente:* `notification-service` (`BookingReminderService`, `ReminderScheduler`, `StaffNotificationService`, migración 031).

### 2.4 Asignación de operario, ejecución y calificación (RF-008, RF-009, RF-010, RF-011, RF-012, RF-012b)

```mermaid
sequenceDiagram
    autonumber
    actor A as Administrador
    actor O as Operario
    actor C as Cliente
    participant W as Web (vía gateway)
    participant OP as operations-service
    participant B as booking-service
    participant S as security-service
    participant DB as SQL Server (execution)
    participant MQ as RabbitMQ
    participant N as notification-service

    MQ-)OP: BookingConfirmed (bookingId, preferredOperatorId)
    OP->>B: GET /internal/v1/bookings/{id} y ?from=&to= (X-Internal-Key)
    OP->>DB: candado de asignaciones (sp_getapplock): una asignación a la vez
    OP->>S: GET /internal/v1/users/{id}/contact (cuenta activa)
    OP->>DB: turnos, ausencias, ejecuciones del día
    alt hay operarios libres
        OP->>OP: AutoAssignmentPolicy: el elegido por el cliente si está libre o, si no, el de menos reservas ese día
        OP->>DB: una service_execution PENDING por línea (actor: sistema)
        OP-)MQ: OperatorAssigned (execution.operator_assigned)
        MQ-)N: notifica al operario y al cliente
    else nadie libre o la reserva ya tenía operario
        OP->>OP: no asigna (el administrador recibe la alerta, sección 2.3)
    end

    A->>W: Abre una reserva → Asignación de operario
    W->>OP: GET /api/v1/admin/assignments/{bookingId}/candidates
    OP->>B: GET /admin/bookings/{id} y /admin/bookings?from=&to= (JWT admin)
    OP->>S: GET /admin/users?role=OPERATOR (JWT admin)
    OP->>DB: turnos, ausencias, ejecuciones del día
    OP-->>W: cada operario + motivo de bloqueo (disponibles primero)
    A->>W: Elige un operario
    W->>OP: PUT /api/v1/admin/assignments/{bookingId}
    OP->>DB: el mismo candado: si la automática está asignando, espera y luego la ve
    alt reserva SCHEDULED/CONFIRMED y AssignmentPolicy no encuentra motivo de bloqueo
        OP->>DB: una service_execution PENDING por línea de la reserva
        OP-->>W: 200 asignación
        OP-)MQ: OperatorAssigned (execution.operator_assigned), tras el commit
        MQ-)N: notifica al operario y al cliente
    else inactivo / sin turno / ausente / ocupado
        OP-->>W: 409 OPERATOR_INACTIVE | OPERATOR_NOT_ON_SHIFT | OPERATOR_ABSENT | OPERATOR_BUSY
    end

    O->>W: Servicios asignados → Iniciar servicio
    W->>OP: POST /api/v1/operator/services/{bookingId}/start
    alt faltan más de 15 minutos (StartPolicy)
        OP-->>W: 409 SERVICE_TOO_EARLY
    end
    OP->>B: PATCH /operator/bookings/{id}/status IN_PROGRESS (JWT operario)
    OP->>DB: líneas → IN_PROGRESS, started_at
    OP-)MQ: ServiceStarted (execution.service_started)
    O->>W: Finalizar servicio
    W->>OP: POST /api/v1/operator/services/{bookingId}/finish
    OP->>B: PATCH /operator/bookings/{id}/status COMPLETED
    OP->>DB: líneas → COMPLETED, finished_at
    OP-)MQ: ServiceCompleted (execution.service_completed)
    MQ-)N: avisa al cliente que el lavado terminó y cancela los recordatorios

    C->>W: Historial → califica la reserva completada
    W->>OP: POST /api/v1/ratings/{bookingId} {puntuación 1–5, comentario ≤ 500}
    OP->>B: GET /bookings/{id} (JWT cliente: solo sus reservas)
    OP->>DB: calificación en cada línea (una sola vez)
    OP-->>W: 201 calificación
```

*Fuente:* `operations-service` (`BookingConfirmedListener`, `OperationsService.autoAssign / candidates / assign / start / finish / rate`, `ExecutionPersistenceAdapter.lockAssignments`, `AssignmentPolicy`, `AutoAssignmentPolicy`, `StartPolicy`, ADR-014). El candado evita que una asignación manual y una automática simultáneas le den al mismo operario dos servicios a la misma hora.

### 2.5 Pago por comprobante y revisión (RF-013, RF-013b, RF-014)

```mermaid
sequenceDiagram
    autonumber
    actor C as Cliente
    actor A as Administrador
    participant W as Web (vía gateway)
    participant P as payment-service (.NET)
    participant B as booking-service
    participant DB as SQL Server (payment)

    C->>W: Paga el servicio
    W->>P: GET /api/v1/payment-accounts
    P-->>W: cuentas activas (QR e instrucciones, la de efectivo solo si está activa)
    C->>C: Paga desde la app del banco (fuera de la plataforma)
    C->>W: Cuenta, código de referencia, imagen del comprobante
    W->>W: comprime la imagen a máximo 2 MB
    W->>P: POST /api/v1/payments
    P->>B: GET /bookings/{id} (JWT cliente)
    B-->>P: estado, total, dueño
    alt reserva pagable, sin pago abierto/aprobado y referencia no usada en otra reserva
        P->>DB: pago PENDING (monto = total de la reserva menos<br/>los cupones aplicados) + comprobante → IN_REVIEW
        P-->>W: 201 pago IN_REVIEW
        P-)B: PaymentReported (payment.reported) por RabbitMQ: booking marca payment_reported_at y la reserva ya no vence (sección 2.11)
    else no pagable / ya reportado / ya aprobado / referencia repetida
        P-->>W: 409 BOOKING_NOT_PAYABLE | PAYMENT_ALREADY_REPORTED | PAYMENT_ALREADY_APPROVED | TRANSACTION_REFERENCE_REUSED
    else comprobante de más de 2 MB
        P-->>W: 400 RECEIPT_TOO_LARGE
    end

    A->>W: Pagos → Verificar
    W->>P: GET /api/v1/admin/payments?status=IN_REVIEW
    P->>B: GET /admin/bookings/{id} (JWT admin) por cada pago
    P-->>W: pagos con los datos de su reserva
    alt aprobar
        W->>P: POST /api/v1/admin/payments/{id}/approve
        P->>DB: APPROVED, approved_by, processed_at (índice único: uno por reserva)
        P->>DB: loyalty_transaction EARNED (puntos de las líneas de la reserva)
        P-)P: payment.confirmed y payment.loyalty_points_earned a RabbitMQ
    else rechazar
        W->>P: POST /api/v1/admin/payments/{id}/reject {motivo}
        P->>DB: REJECTED + motivo (el cliente puede reportar de nuevo)
        P-)P: payment.rejected a RabbitMQ
    end

    A->>W: Pagos → Registro manual (efectivo en el local)
    W->>P: POST /api/v1/admin/payments {bookingId, paymentAccountId}
    P->>B: GET /admin/bookings/{id}
    P->>DB: pago APPROVED directamente (monto = valor pendiente) + puntos EARNED
    A->>W: Pagos → Reembolsar un pago aprobado
    W->>P: POST /api/v1/admin/payments/{id}/refund
    P->>DB: REFUNDED + loyalty_transaction REVERSED
```

*Fuente:* `payment-service` (`PaymentCommandService.ReportAsync / ApproveAsync / RejectAsync / RegisterInPersonAsync / RefundAsync`, `BookingPaymentGuard`, `LoyaltyRewardService`, agregado `Payment`, ADR-008, ADR-013). notification-service convierte cada evento `payment.*` en un aviso para el cliente.

### 2.6 Canje de cupón y puntos de fidelización (RF-024, RF-025, RF-026)

```mermaid
sequenceDiagram
    autonumber
    actor C as Cliente
    actor A as Administrador
    participant W as Web (vía gateway)
    participant P as payment-service (.NET)
    participant B as booking-service
    participant DB as SQL Server (promotion / payment)
    participant MQ as RabbitMQ
    participant N as notification-service

    A->>W: Gestión → Promociones → Crear
    W->>P: POST /api/v1/admin/promotions {código, vigencia, tipo PERCENT o FIXED y valor, puntos requeridos}
    P->>DB: INSERT promotion
    P-->>W: 201 promoción

    Note over C,P: --- Acreditación de puntos (al aprobar un pago, flujo 2.5) ---
    P->>DB: INSERT loyalty_transaction EARNED (+puntos de las<br/>líneas de la reserva, sequence_no único por cliente)
    Note over P: El saldo nunca se guarda aparte: se calcula siempre<br/>desde la última fila del libro de movimientos (INV-LOY-001)

    C->>W: Dashboard → ve sus puntos y promociones desbloqueadas
    W->>P: GET /api/v1/loyalty/balance
    P->>DB: SELECT último movimiento del cliente (saldo vigente)
    P-->>W: puntos actuales
    W->>P: GET /api/v1/loyalty/promotions
    P->>DB: promociones vigentes
    P-->>W: cada promoción + si ya está desbloqueada (puntos >= requeridos)

    C->>W: Pago de una reserva → ingresa el código del cupón
    W->>P: POST /api/v1/loyalty/redeem {bookingId, código}
    P->>B: valida que la reserva sea del cliente y no tenga pago reportado
    P->>DB: busca la promoción por código
    alt vigente, desbloqueada, no canjeada antes en esta reserva y dentro de los límites de uso
        P->>P: Promotion.Redeem(...) calcula el descuento sobre el subtotal (nunca lo supera)
        P->>DB: INSERT booking_promotion (applied_amount)
        P-)MQ: PromotionRedeemed (payment.promotion_redeemed)
        MQ-)N: notifica el canje en la bandeja y por correo
        P-->>W: 200 {descuento aplicado, nuevo total a pagar}
    else la reserva ya tiene un pago reportado o aprobado
        P-->>W: 409 PAYMENT_ALREADY_REPORTED | PAYMENT_ALREADY_APPROVED
    else vencida / no desbloqueada / ya canjeada / compra mínima / sin cupos
        P-->>W: 400 PROMOTION_NOT_REDEEMABLE | PROMOTION_ALREADY_REDEEMED | PROMOTION_MIN_PURCHASE_NOT_MET | PROMOTION_EXHAUSTED | PROMOTION_CUSTOMER_LIMIT_REACHED
    end
```

*Fuente:* `payment-service` (`LoyaltyController`, `CustomerPromotionService.RedeemAsync`, agregado `Promotion.Redeem`, `LoyaltyLedgerRepository`), ADR-015.

### 2.7 Gestión de roles y permisos (RF-029)

```mermaid
sequenceDiagram
    autonumber
    actor A as Administrador
    participant W as Web (vía gateway)
    participant S as security-service
    participant DB as SQL Server (security)

    A->>W: Gestión → Roles
    W->>S: GET /api/v1/admin/roles-permissions
    S->>DB: SELECT catálogo de permisos + permisos de cada rol
    S-->>W: 200 {permisos[], roles: [{rol, permisos[]}]}, 3 roles fijos: ADMIN, OPERATOR, CLIENT
    W-->>A: Matriz de solo lectura (desde el 24 de octubre de 2026 la web ya no ofrece marcar ni guardar)
    Note over S: No se pueden crear ni borrar roles (ADR-010).<br/>Esta matriz es informativa: la autorización real sigue viniendo<br/>del claim roles del JWT, no de esta tabla (decisión abierta, sección 11.3 del SRS)
```

*Fuente:* `security-service` (`RolePermissionController`, `RolePermissionAdministrationService`, migración `026-security-role-permissions.sql`), ADR-015.

### 2.8 Inicio de sesión con bloqueo por intentos fallidos y cierre de sesión (RF-002, RF-004b, RF-027)

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant W as Web o app
    participant GW as API Gateway
    participant S as security-service
    participant DB as SQL Server (security)

    U->>W: Correo y contraseña
    W->>GW: POST /api/v1/auth/login
    GW->>S: enruta
    S->>DB: cuenta por correo y su fila de login_lockout
    alt cuenta bloqueada (locked_until en el futuro)
        S-->>W: 423 ACCOUNT_LOCKED
        W-->>U: Mensaje de cuenta bloqueada (en la app, con la opción de recuperar la contraseña)
    else contraseña incorrecta
        S->>DB: suma el fallo (ventana de 15 min) y al quinto pone locked_until en ahora + 15 min
        S-->>W: 401 INVALID_CREDENTIALS<br/>(o 423 si este fallo la bloqueó)
    else contraseña correcta y cuenta desactivada
        S-->>W: 403 ACCOUNT_DISABLED
    else contraseña correcta
        S->>DB: borra el conteo de fallos, registra la sesión
        S-->>W: 200 JWT (1 h)
    end
    Note over S,DB: Restablecer la contraseña con el código (RF-003) también borra el bloqueo

    U->>W: Cerrar sesión
    W->>GW: POST /api/v1/auth/logout
    GW->>GW: olvida la respuesta guardada de este token
    GW->>S: enruta
    S->>DB: cierra la sesión (revoked_at)
    W-->>U: vuelve al inicio de sesión (las otras pestañas del navegador también)
    Note over W,DB: Otra llamada con el mismo token (un token copiado)
    W->>GW: GET /api/v1/bookings (token de la sesión cerrada)
    GW->>S: GET /api/v1/auth/session (mismo token)
    S->>DB: ¿sesión abierta y sin vencer?
    S-->>GW: 401 UNAUTHORIZED
    GW-->>W: 401 (booking-service no recibe la llamada)
```

*Fuente:* `security-service` (`AuthenticationService.login / logout / isActive`, `ActiveSessionValidator`, `LoginLockout`, `PasswordRecoveryService`, migración `034-security-login-lockout.sql`) y gateway (`SessionCheckFilter`). El gateway guarda 30 segundos (si security-service no responde en 3 segundos, el gateway deja pasar la petición: *fail-open*) por token la respuesta de `/api/v1/auth/session`, así que una sesión abierta cuesta una llamada extra cada medio minuto y no una por petición. Una sesión también se cierra cuando el administrador desactiva la cuenta o el dueño la cierra; eso llega a los demás servicios en esos mismos 30 segundos.

### 2.9 Informe de inspección del vehículo (RF-036)

```mermaid
sequenceDiagram
    autonumber
    actor A as Administrador
    actor C as Cliente
    participant W as Web o app móvil (vía gateway)
    participant OP as operations-service
    participant B as booking-service
    participant DB as SQL Server (execution)
    participant MQ as RabbitMQ
    participant N as notification-service

    A->>W: Reserva de 180 min o más → Informe de inspección
    W->>OP: POST /api/v1/admin/inspections/{bookingId}
    OP->>B: GET /admin/bookings/{id} (JWT admin)
    alt reserva larga y Confirmada o En proceso
        OP->>DB: informe con 5 fases PENDING y un código público aleatorio
        OP-->>W: 200 informe (si ya existía, devuelve el mismo)
    else corta, cancelada o completada
        OP-->>W: 409 INSPECTION_NOT_EDITABLE
    end
    A->>W: Marca fases y registra hallazgos con fotos
    W->>OP: PATCH /phases/{phase} y POST /findings (multipart, hasta 5 fotos)
    OP->>DB: fases, hallazgos y fotos
    A->>W: Publicar y compartir
    W->>OP: POST /publish y POST /share
    OP-)MQ: InspectionShared (execution.inspection_shared)
    MQ-)N: notificación al cliente con el enlace, en su idioma
    C->>W: Abre el enlace /report/{token}
    W->>OP: GET /api/v1/public/inspections/{token} (sin sesión)
    OP-->>W: informe publicado con la placa enmascarada
```

*Fuente:* `operations-service` (`InspectionReport`, migración `033-execution-inspection-report.sql`), `notification-service` (aviso del informe compartido), web (`features/admin/pages/inspection`) y app móvil (`AdminInspectionScreen`, `InspectionService`). En la app las fotos se toman con la cámara o se eligen de la galería y se reducen en el celular antes del `POST /findings`; el botón **Publicar y compartir enlace** abre el menú de compartir del teléfono.

### 2.10 Recuperación de la contraseña con un código de 6 dígitos (RF-003)

```mermaid
sequenceDiagram
    autonumber
    actor U as Usuario
    participant W as Web o app
    participant S as security-service
    participant DB as SQL Server (security)
    participant M as Correo (SMTP)

    U->>W: Olvidé mi contraseña → correo
    W->>S: POST /api/v1/auth/password/forgot
    alt cuenta activa con ese correo
        S->>DB: invalida los códigos anteriores que sigan vigentes
        S->>DB: guarda el hash del código nuevo (vence en 15 min)
        S->>M: correo con el código
    else correo desconocido o cuenta inactiva
        S->>S: no hace nada
    end
    S-->>W: 202 (la misma respuesta en los dos casos)
    U->>W: Escribe el código
    W->>S: POST /api/v1/auth/password/verify
    alt código correcto
        S-->>W: 204
    else código incorrecto
        S->>S: suma el intento, al quinto invalida el código
        S-->>W: 400 INVALID_RESET_CODE
    end
    U->>W: Contraseña nueva
    W->>S: POST /api/v1/auth/password/reset
    alt política cumplida y distinta de la actual
        S->>DB: marca el código como usado, guarda la contraseña nueva, borra el bloqueo y revoca todas las sesiones
        S-->>W: 204 (vuelve al inicio de sesión)
    else débil o igual a la actual
        S-->>W: 400 WEAK_PASSWORD o SAME_PASSWORD (el código no se gasta)
    end
```

*Fuente:* `security-service` (`PasswordRecoveryService`, `PasswordResetToken`, `PasswordRecoveryPolicy` con `code-ttl: 15m` y `max-failed-attempts: 5`, `SecureRandomResetCodeGenerator`, `EmailResetCodeNotifier`). La respuesta de *forgot* no revela si el correo existe.

### 2.11 Vencimiento del pago digital (RF-005, RF-033, RN-BOOK-03)

```mermaid
sequenceDiagram
    autonumber
    participant MQ as RabbitMQ (carwash.events)
    participant B as booking-service
    participant J as PaymentDeadlineJob (booking, cada 60 s)
    participant P as payment-service (.NET)
    participant DB as SQL Server (booking)
    participant N as notification-service

    MQ-)B: payment.reported o payment.confirmed (cola booking.payment-events)
    B->>DB: payment_reported_at = ahora (idempotente)
    J->>DB: reservas DIGITAL sin reporte con payment_deadline vencido (ix_booking_payment_pending)
    loop cada reserva vencida, en su propia transacción
        J->>P: GET /internal/v1/payments/bookings/{id}/reported (X-Internal-Key, 3 s)
        alt reported = true (el aviso se había perdido)
            P-->>J: 200 {"bookingId":…, "reported":true}
            J->>DB: payment_reported_at = ahora (PAYMENT_REPORTED, no se cancela)
        else reported = false
            P-->>J: 200 {"bookingId":…, "reported":false}
            J->>DB: CANCELLED con el motivo PAYMENT_TIMEOUT, se libera la bahía (CANCELLED)
            B-)MQ: BookingCancelled (booking.cancelled, reasonCode PAYMENT_TIMEOUT)
            MQ-)N: BookingCancelled: aviso al cliente
        else payment-service no responde o da error
            J->>J: no se cancela en esta vuelta (PAYMENT_SERVICE_UNAVAILABLE), se reintenta en la siguiente
        end
    end
```

*Fuente:* `booking-service` (`PaymentEventsListener`, `RabbitConsumerConfig`, `PaymentDeadlineJob`, `BookingService.cancelForPaymentTimeout`, `PaymentServiceDirectory`, `PaymentTimeoutOutcome`) y `payment-service` (`InternalPaymentsController`, `InternalApiKeyAuthenticationHandler`). Como no hay Outbox, un evento `payment.reported` puede perderse; por eso el job le pregunta a payment-service antes de cancelar (AC-89). Para payment, `reported` es verdadero si la reserva tiene un pago `IN_REVIEW` o `APPROVED`. La tarea se apaga con `BOOKING_PAYMENT_EXPIRY_ENABLED=false`, y el intervalo es `BOOKING_PAYMENT_EXPIRY_INTERVAL` (60 s).

---

## 3. Diagramas de Actividades

Los diagramas de actividades muestran los procesos de negocio tal como funcionan en el sistema. En los pagos no hay pasarela: el cliente sube un comprobante y el administrador lo revisa (ADR-008).

### 3.1 Autenticación (RF-001 a RF-004)

```mermaid
flowchart TD
    start([Inicio]) --> menu[Mostrar pantalla de ingreso]
    menu --> choice{¿Qué desea hacer?}

    choice -->|Registrarse| f1[Mostrar formulario de registro]
    f1 --> d1[Ingresar datos y aceptar política de datos]
    d1 --> v1{¿Campos completos y<br/>correo válido y disponible?}
    v1 -->|No| e1[Mostrar el campo con error]
    e1 --> d1
    v1 -->|Sí| v2{¿Contraseña cumple<br/>la política RNF-004?}
    v2 -->|No| e2[Mostrar requisitos de contraseña]
    e2 --> d1
    v2 -->|Sí| ok1[Crear cuenta + perfil de cliente]
    ok1 --> mail1[Enviar correo de bienvenida]
    mail1 --> stop1([Fin])

    choice -->|Iniciar sesión| f2[Mostrar formulario de login]
    f2 --> d2[Ingresar correo y contraseña]
    d2 --> v3{¿Cuenta existe,<br/>activa y contraseña correcta?}
    v3 -->|No| e3[Mensaje genérico de error]
    e3 --> d2
    v3 -->|"5 fallos en 15 min"| lock["Bloquear cuenta 15 min (RF-004b)"]
    lock --> offer[Ofrecer recuperar la contraseña]
    offer --> stop2([Fin])
    v3 -->|Sí| ok2[Emitir JWT y redirigir al panel del rol]
    ok2 --> stop3([Fin])

    choice -->|Recuperar contraseña| f3[Pedir el correo]
    f3 --> d3[Ingresar correo]
    d3 --> v4{¿Correo registrado?}
    v4 -->|No| e4["Misma respuesta genérica<br/>(no revela si existe)"]
    e4 --> stop4([Fin])
    v4 -->|Sí| code[Enviar código de 6 dígitos, vence en 15 min]
    code --> d4[Ingresar código y nueva contraseña]
    d4 --> v5{¿Código válido<br/>y no vencido?}
    v5 -->|No, 5 intentos| e5[Pedir código nuevo]
    e5 --> f3
    v5 -->|Sí| same{¿Nueva contraseña<br/>igual a la actual?}
    same -->|Sí| e7[Pedir una contraseña distinta]
    e7 --> d4
    same -->|No| ok3[Actualizar contraseña, invalidar código y levantar el bloqueo]
    ok3 --> stop5([Fin])

    choice -->|Cambiar contraseña| auth{¿Usuario autenticado?}
    auth -->|No| stop6([Fin])
    auth -->|Sí| d5[Ingresar contraseña actual y nueva]
    d5 --> v6{¿Actual correcta y<br/>nueva cumple la política?}
    v6 -->|No| e6[Mostrar error]
    e6 --> d5
    v6 -->|Sí| ok4[Actualizar contraseña]
    ok4 --> stop7([Fin])
```

### 3.2 Reservar un lavado (RF-005, RF-006, RF-007, RF-010)

```mermaid
flowchart TD
    start([Inicio]) --> veh{¿Cliente tiene<br/>al menos un vehículo?}
    veh -->|No| regveh["Pedir que registre un vehículo (CU26)"]
    regveh --> stop0([Fin])
    veh -->|Sí| sel[Elegir vehículo y de 1 a 10 servicios]
    sel --> price[Mostrar precio según el tipo de vehículo]
    price --> date[Elegir fecha y hora]
    date --> op{"¿Quiere elegir operario?<br/>(lista de libres a esa hora)"}
    op -->|Sí| pickop[Elegir un operario libre]
    op -->|No| pay{"¿Cómo vas a pagar?"}
    pickop --> pay
    pay -->|Pago digital| check[Validar horario del local, excepciones y bahías]
    pay -->|Efectivo| cashlim{¿Ya tiene una reserva<br/>activa en efectivo?}
    cashlim -->|Sí| cashno["Rechazar (CASH_BOOKING_LIMIT): pagarla o cancelarla, o elegir pago digital"]
    cashno --> pay
    cashlim -->|No| check
    check --> avail{¿Hay bahía libre<br/>en esa franja?}
    avail -->|No| alt[Ofrecer hasta 5 horas alternativas del mismo día<br/>sin cruce con otras reservas del vehículo]
    alt --> date
    avail -->|Sí| confirm[Confirmar reserva: asignar bahía, congelar precios]
    confirm --> notif["Notificar confirmación, programar recordatorios 24h/1h<br/>y asignar operario (sección 3.3)"]
    notif --> conf([Reserva Confirmada])
    conf --> due{"Pago digital: ¿subió el comprobante<br/>antes del plazo (30 min o la hora de inicio)?"}
    due -->|No, y payment-service no tiene el pago| timeout[Cancelar con PAYMENT_TIMEOUT y liberar la bahía]
    timeout --> stop4([Fin])
    due -->|Sí, o paga en efectivo| manage

    manage{¿Hay un cambio<br/>antes de iniciar?}
    manage -->|El cliente cancela| state{¿Reserva Programada<br/>o Confirmada?}
    state -->|No - en proceso/terminada| block[Bloquear la acción con mensaje explicativo]
    block --> stop2([Fin])
    state -->|Sí| ask[Confirmar en el diálogo, sin pedir motivo]
    ask --> free[Liberar la bahía, marcar Cancelada]
    free --> stop3([Fin])
    manage -->|El administrador reprograma| resched["Nueva fecha y hora desde Reservas (CU29)"]
    resched --> check
```

### 3.3 Asignación de operario y ejecución del servicio (RF-008, RF-009, RF-010, RF-011, RF-012b)

```mermaid
flowchart TD
    start0((Inicio)) --> start
    start([Reserva Confirmada sin operario]) --> auto{"¿Asignación automática<br/>encuentra un elegible?<br/>(RF-008)"}
    auto -->|Sí| pref{"¿El cliente eligió un operario<br/>y sigue libre? (RF-010)"}
    pref -->|Sí| chosen[Asignar al operario elegido]
    pref -->|No| pick[Elegir el de menos reservas ese día<br/>empate: menor identificador]
    chosen --> notifyop[Notificar al cliente y al operario]
    pick --> notifyop
    notifyop --> wait([Esperar la hora de la reserva])
    auto -->|No hay elegibles| manual["Reserva sin operario; alerta al administrador<br/>cuando faltan menos de 2 h"]
    manual --> list[Admin ve candidatos: disponibles primero, con calificación]
    list --> chooseop[Admin elige un operario]
    chooseop --> valid{¿Operario activo, en turno,<br/>sin ausencia y sin cruce de horario?}
    valid -->|No| blockop[Bloquear asignación, mostrar motivo]
    blockop --> list
    valid -->|Sí| assign[Crear ejecución PENDING, notificar al operario]
    assign --> wait

    wait --> today{"¿Faltan 15 minutos o menos<br/>para la hora de la reserva?"}
    today -->|No| blockday[Botón deshabilitado: SERVICE_TOO_EARLY]
    blockday --> stopC([Fin])
    today -->|Sí| startsvc[Operario marca Iniciar servicio]
    startsvc --> inprog[Ejecución → En proceso]
    inprog --> finish[Operario marca Finalizar servicio]
    finish --> completed[Ejecución → Completada; reserva → Completada;<br/>se cancelan los recordatorios]
    completed --> rate["Cliente puede calificar (CU15)"]
    rate --> stopD([Fin])
```

### 3.4 Pagar una reserva (RF-013, RF-013b, RF-014, RF-016)

```mermaid
flowchart TD
    start0((Inicio)) --> start
    start([Reserva Confirmada sin pago aprobado]) --> method{¿Qué método de pago?}

    method -->|QR / Transferencia / Nequi / Daviplata| showqr[Mostrar QR de la cuenta activa e instrucciones]
    showqr --> extpay[Cliente paga desde su app bancaria<br/>fuera de la plataforma]
    extpay --> upload[Sube comprobante comprimido a máx. 2 MB<br/>+ valor declarado + código de referencia]
    upload --> dup{¿Código de referencia<br/>ya usado en otra reserva?}
    dup -->|Sí| rejectref[Rechazar: TRANSACTION_REFERENCE_REUSED]
    rejectref --> stop1([Fin])
    dup -->|No| inreview["Pago → En revisión (ligado a la reserva)"]
    inreview --> waitadm([Espera la revisión del admin])

    method -->|Efectivo, si su cuenta está activa| cash[Cliente paga en el local]
    cash --> register[Admin registra el pago manualmente]
    register --> approved1["Pago → Aprobado directamente"]
    approved1 --> points1["Acreditar puntos de fidelización (RF-026)"]
    points1 --> lockui

    waitadm --> review[Admin abre pagos pendientes]
    review --> compare{¿Valor declarado =<br/>valor a pagar y comprobante legible?}
    compare -->|No| reject[Rechazar con motivo; cliente puede reenviar]
    reject --> stop4([Fin])
    compare -->|Sí| approve["Pago → Aprobado"]
    approve --> points2["Acreditar puntos de fidelización (RF-026)"]
    points2 --> notifyc[Notificar al cliente]
    notifyc --> lockui[La pantalla de pago solo muestra el resumen<br/>sin método, QR ni comprobante]
    lockui --> stop5([Fin])
```

### 3.5 Canjear un cupón de fidelización (RF-024, RF-025, RF-026)

```mermaid
flowchart TD
    start0((Inicio)) --> start
    start([Cliente en la pantalla de pago]) --> has{¿Tiene promociones<br/>desbloqueadas por sus puntos?}
    has -->|No| noshow[No se ofrece ningún cupón]
    noshow --> stop1([Fin])
    has -->|Sí| enter[Ingresa el código del cupón]
    enter --> valid{¿Vigente, activa,<br/>desbloqueada y no canjeada<br/>antes en esta reserva?}
    valid -->|No| inform["Informar el motivo y mantener el valor original<br/>(cupón vencido / sin puntos / ya canjeado / agotado)"]
    inform --> stop2([Fin])
    valid -->|Sí| limit{"¿Dentro del límite de usos<br/>(total y por cliente)?"}
    limit -->|No| exhausted[Informar: cupón agotado]
    exhausted --> stop3([Fin])
    limit -->|Sí| calc[Calcular descuento sobre el subtotal<br/>sin superarlo]
    calc --> apply[Aplicar descuento al total a pagar]
    apply --> notify[Notificar el canje en la bandeja y por correo]
    notify --> stop4([Continúa el pago por el valor con descuento])
    stop4 --> stop4x((Fin))
```

### 3.6 Gestionar roles y permisos (RF-029)

```mermaid
flowchart TD
    start0((Inicio)) --> start
    start([Admin abre Gestión → Roles]) --> load[Cargar los 3 roles fijos + catálogo de permisos]
    load --> view[Ver permisos actuales de cada rol: Administrador, Operario, Cliente]
    view --> stop([Solo consulta desde el 24 de octubre de 2026: los permisos no se editan y los roles no se crean ni se borran])
    stop --> stopx((Fin))
```

---

## 4. Diagramas de Estados

### 4.1 Ciclo de vida de la reserva (RF-006, RF-007)

```mermaid
stateDiagram-v2
    [*] --> CONFIRMED : crear (se asigna bahía libre de inmediato, RF-006)
    SCHEDULED --> CONFIRMED : confirmar (necesita bahía)
    SCHEDULED --> CANCELLED : cancelar
    CONFIRMED --> CONFIRMED : reprogramar (admin, nueva hora/bahía, se conservan precios)
    CONFIRMED --> IN_PROGRESS : iniciar o Se atendió (admin)
    CONFIRMED --> NO_SHOW : no asistió, o terminó sin operario
    CONFIRMED --> CANCELLED : cancelar (cliente antes del inicio o admin)
    CONFIRMED --> CANCELLED : vence el plazo del pago digital sin comprobante (sistema, PAYMENT_TIMEOUT)
    IN_PROGRESS --> COMPLETED : finalizar o Se atendió (admin)
    COMPLETED --> [*]
    CANCELLED --> [*]
    NO_SHOW --> [*]
```

*Fuente:* `booking-service` (`Booking.moveTo`, `Booking.cancel`, `BookingStatus`) y `operations-service` (`FollowUpJob`, `FollowUpPolicy`). *Reservas que terminaron y nadie cerró:* el sistema solo decide por su cuenta cuando no hay duda. Hasta el 23 de octubre de 2026 `ExpiredBookingsJob` cancelaba las reservas que seguían `SCHEDULED`; se quitó el 24 de octubre (B-07) porque nunca actuaba: todas las reservas nacen y se reprograman `CONFIRMED`. Cada 5 minutos, `FollowUpJob` de operations pide a booking (`POST /internal/v1/bookings/{id}/no-show`) que deje en `NO_SHOW` la reserva `CONFIRMED` que nunca tuvo operario y terminó hace más de una hora; booking vuelve a comprobar que venció y, si no, responde 409 `BOOKING_NOT_EXPIRED`. La reserva `CONFIRMED` con operario que nadie inició (`NOT_STARTED`) y la `IN_PROGRESS` que nadie finalizó una hora después de su fin (`NOT_FINISHED`) **nunca** cambian de estado solas: pasan a la lista "Por cerrar" del administrador, quien las resuelve con "Se atendió" (la reserva pasa por `IN_PROGRESS` y queda `COMPLETED`, sin publicar `ServiceCompleted`) o con "No asistió" (solo si nunca se inició). Así se evita registrar como inasistencia un servicio que sí se hizo y que el operario olvidó marcar. En ningún caso hay reembolso automático: los pagos los revisa el administrador. Solo `SCHEDULED` y `CONFIRMED` se pueden reprogramar o cancelar (RF-007). El cliente solo cancela, antes de la hora de inicio y sin escribir motivo (la reserva guarda `CUSTOMER_REQUEST`); el administrador reprograma (CU29) y puede elegir el motivo de la cancelación. `SCHEDULED` existe en el modelo pero el flujo actual siempre crea la reserva directo en `CONFIRMED`. Desde el 26 de octubre de 2026, `PaymentDeadlineJob` cancela con el motivo `PAYMENT_TIMEOUT` la reserva con pago digital que llegó a su plazo sin comprobante, solo si payment-service confirma que no tiene el pago (sección 2.11); reprogramar no cambia ni el modo de pago ni el plazo.

### 4.2 Ciclo de vida del pago (RF-013b, RF-014, ADR-008)

```mermaid
stateDiagram-v2
    [*] --> PENDING : crear (monto = total de la reserva menos cupones)
    PENDING --> PENDING : adjuntar comprobante
    PENDING --> IN_REVIEW : enviar a revisión (requiere al menos un comprobante)
    IN_REVIEW --> APPROVED : aprobar (admin)
    IN_REVIEW --> REJECTED : rechazar (admin, motivo ≤ 200 caracteres)
    APPROVED --> REFUNDED : reembolsar (admin)
    REJECTED --> [*]
    REFUNDED --> [*]
    APPROVED --> [*]
```

*Fuente:* `payment-service`, agregado `Payment`. Una reserva admite un solo pago aprobado y un solo pago abierto a la vez; después de `REJECTED` el cliente puede reportar un pago nuevo. El pago del cliente pasa de `PENDING` a `IN_REVIEW` en la misma petición; el pago en efectivo que registra el administrador queda `APPROVED` de una vez.

### 4.3 Ciclo de vida de la ejecución del servicio (RF-012b)

```mermaid
stateDiagram-v2
    [*] --> PENDING : asignar operario (automático o admin, RF-008, RF-009)
    PENDING --> PENDING : reasignar (admin, antes de iniciar)
    PENDING --> IN_PROGRESS : iniciar servicio (desde 15 min antes, RF-012b)
    IN_PROGRESS --> COMPLETED : finalizar servicio (RF-012b) o Se atendió (admin)
    PENDING --> COMPLETED : Se atendió (admin, sin horas)
    COMPLETED --> COMPLETED : calificar 1 a 5 (cliente, una vez)
    COMPLETED --> [*]
```

*Fuente:* `operations-service` (`ServiceExecution`, `StartPolicy`, `FollowUpPolicy`). `COMPLETED` habilita la calificación del cliente (RF-012). Cuando el administrador cierra desde "Por cerrar" un servicio como "Se atendió", las líneas quedan `COMPLETED` sin inventar horas: sin hora de inicio no cuentan para la puntualidad y sin hora de fin no suman horas trabajadas, pero sí cuentan como servicio hecho y como ingreso. El operario todavía puede iniciar tarde el mismo servicio, porque iniciar no tiene límite superior, y en ese caso cuentan sus horas reales. Si un servicio con operario no se inicia o no se finaliza a tiempo, operations avisa una sola vez al operario y a cada administrador activo (bandeja y push, migración 040). `PAUSED` y `WITH_ISSUE` existen en el catálogo `execution.execution_status`, pero ningún caso de uso los usa hoy.

### 4.4 Ciclo de vida de una promoción (RF-024)

```mermaid
stateDiagram-v2
    [*] --> ACTIVA : crear (código, vigencia, descuento, puntos requeridos)
    ACTIVA --> PAUSADA : pausar (admin)
    PAUSADA --> ACTIVA : reanudar (admin)
    ACTIVA --> [*] : eliminar (borrado lógico)
    PAUSADA --> [*] : eliminar (borrado lógico)
```

*Fuente:* `payment-service`, `Promotion.SetActive`, `Promotion.StatusFor`, `Promotion.IsAvailableOn`. Lo único que se guarda es si está activa o pausada; la pantalla del administrador muestra "programada" mientras no llega la fecha de inicio. Solo se puede canjear una promoción activa y dentro de su vigencia (RF-024, RN-PROMO-01): pasada la fecha de fin ya no se ofrece ni se canjea, aunque no tiene un estado "vencida" guardado.

### 4.5 Informe de inspección y sus fases (RF-036)

```mermaid
stateDiagram-v2
    [*] --> BORRADOR : crear (reserva de 180 min o más, Confirmada o En proceso)
    BORRADOR --> BORRADOR : marcar fases y registrar hallazgos
    BORRADOR --> PUBLICADO : publicar (el enlace empieza a mostrarlo)
    PUBLICADO --> PUBLICADO : editar o compartir (notifica al cliente)
    PUBLICADO --> [*] : la reserva se completa (fases y hallazgos de solo lectura)

    state "Cada fase" as fase {
        [*] --> PENDING
        PENDING --> IN_PROGRESS : marcar en curso o registrar un hallazgo
        IN_PROGRESS --> DONE : terminar
        DONE --> IN_PROGRESS : reabrir
        IN_PROGRESS --> PENDING : volver a pendiente
    }
```

*Fuente:* `operations-service`, `InspectionReport` (`publish`, `requirePublished`, `setPhaseStatus`, `addFinding`). Las fases son recepción, exterior, interior, motor y entrega; solo se puede compartir un informe publicado, y se edita mientras la reserva siga Confirmada o En proceso.

### 4.6 Cuenta de usuario (RF-002, RF-004b, RF-027, RF-028)

```mermaid
stateDiagram-v2
    [*] --> ACTIVA : registro (CLIENT) o creada por el administrador
    state ACTIVA {
        [*] --> DESBLOQUEADA
        DESBLOQUEADA --> BLOQUEADA : quinto intento fallido en 15 min
        BLOQUEADA --> DESBLOQUEADA : pasan 15 min o recupera la contraseña
    }
    ACTIVA --> INACTIVA : el administrador la desactiva o el usuario la cierra
    INACTIVA --> ACTIVA : el administrador la activa
```

*Fuente:* `security-service` (`UserAccount`, `LoginLockout`, `UserAdministrationService.changeStatus`, `AccountDeactivationService`). Un ingreso correcto antes del quinto intento borra el conteo de fallos. El usuario cierra su cuenta con la contraseña. Una cuenta bloqueada responde 423 aun con la contraseña correcta; una inactiva responde 403 después de validar la contraseña. Ninguna cuenta de administrador se puede desactivar (403 `ADMIN_CANNOT_BE_DISABLED`) y un administrador no puede desactivar ni cerrar su propia cuenta (403 `CANNOT_DISABLE_OWN_ACCOUNT`). Cada cuenta tiene un solo rol, fijado al crearla: no hay cambio de rol. Al pasar a inactiva se cierran todas sus sesiones y sus tokens dejan de servir en máximo 30 segundos (sección 2.8).

---

## 5. Inventario de Pantallas (mockup)

Pantalla por pantalla y por rol, incluyendo los modales de crear/editar de cada módulo. Todas las capturas están en `capturas/`, en esta misma carpeta. La captura de Gestión → Usuarios se tomó de nuevo el 20 de octubre de 2026: la tabla solo muestra el tipo de usuario y tiene los filtros Habilitados e Inhabilitados. Desde el 20 de octubre, al hacer clic en una notificación o en una reserva del historial se abre su detalle, con un botón para ir al apartado correspondiente (por ejemplo, **Ir al pago**).

### 5.1 Pantallas comunes (sin sesión)

| Pantalla | Evidencia |
|---|---|
| Inicio de sesión | ![Login](capturas/01_login.png) |
| Registro | ![Registro](capturas/02_registro.png) |
| Recuperar contraseña | ![Recuperar](capturas/03_recuperar_contrasena.png) |

### 5.2 Rol Administrador

| Pantalla | Evidencia |
|---|---|
| Dashboard | ![Dashboard admin](capturas/04_admin_dashboard.png) |
| Gestión → Usuarios | ![Usuarios](capturas/05_admin_usuarios.png) |
| Modal: Crear usuario | ![Modal crear usuario](capturas/29_modal_crear_usuario.png) |
| Gestión → Roles | ![Roles](capturas/06_admin_roles.png) |
| Modal: Editar permisos del rol (se quitó el 24 de octubre de 2026: la matriz es de solo lectura) | ![Modal editar permisos](capturas/30_modal_editar_permisos.png) |
| Gestión → Servicios (catálogo) | ![Catálogo](capturas/07_admin_catalogo.png) |
| Modal: Agregar servicio | ![Modal agregar servicio](capturas/31_modal_crear_servicio.png) |
| Gestión → Promociones | ![Promociones](capturas/08_admin_promociones.png) |
| Modal: Crear promoción | ![Modal crear promoción](capturas/32_modal_crear_promocion.png) |
| Horarios y bahías | ![Horarios y bahías](capturas/09_admin_horarios_bahias.png) |
| Modal: Agregar excepción de horario | ![Modal agregar excepción](capturas/33_modal_agregar_excepcion.png) |
| Operarios | ![Operarios](capturas/10_admin_operarios.png) |
| Modal: Nuevo operario | ![Modal nuevo operario](capturas/34_modal_nuevo_operario.png) |
| Modal: Asignar turno | ![Modal asignar turno](capturas/42_modal_asignar_turno.png) |
| Reservas | ![Reservas](capturas/11_admin_reservas.png) |
| Modal: Asignar operario a una reserva | ![Modal asignar operario](capturas/35_modal_asignar_operario.png) |
| Pagos | ![Pagos](capturas/12_admin_pagos.png) |
| Modal: Registrar pago manual | ![Modal registrar pago](capturas/36_modal_registrar_pago.png) |
| Reportes | ![Reportes](capturas/13_admin_reportes.png) |
| Configuración → Cuentas de pago | ![Cuentas de pago](capturas/14_admin_cuentas_pago.png) |
| Modal: Agregar cuenta de pago | ![Modal cuenta de pago](capturas/37_modal_cuenta_pago.png) |
| Notificaciones | ![Notificaciones admin](capturas/15_admin_notificaciones.png) |
| Perfil | ![Perfil admin](capturas/16_admin_perfil.png) |

### 5.3 Rol Cliente

| Pantalla | Evidencia |
|---|---|
| Dashboard | ![Dashboard cliente](capturas/17_client_dashboard.png) |
| Modal: Ver detalle del próximo servicio | ![Modal detalle reserva](capturas/39_modal_detalle_reserva_cliente.png) |
| Mis vehículos | ![Vehículos](capturas/18_client_vehiculos.png) |
| Modal: Registrar vehículo | ![Modal registrar vehículo](capturas/38_modal_registrar_vehiculo.png) |
| Reservar lavado | ![Reservar](capturas/19_client_reservar.png) |
| Historial de reservas | ![Historial](capturas/20_client_historial.png) |
| Pagar servicio | ![Pago](capturas/21_client_pago.png) |
| Modal: Confirmar cancelación de reserva | ![Modal cancelar reserva](capturas/41_modal_cancelar_reserva.png) |
| Notificaciones | ![Notificaciones cliente](capturas/22_client_notificaciones.png) |
| Configuración (tema e idioma) | ![Configuración](capturas/23_client_configuracion.png) |

### 5.4 Rol Operario

| Pantalla | Evidencia |
|---|---|
| Panel principal | ![Panel operario](capturas/24_operario_home.png) |
| Servicios asignados | ![Servicios asignados](capturas/25_operario_servicios_asignados.png) |
| Mi agenda (disponibilidad) | ![Mi agenda](capturas/26_operario_horario.png) |
| Historial de servicios | ![Historial operario](capturas/27_operario_historial.png) |
| Calificaciones recibidas | ![Calificaciones](capturas/28_operario_calificaciones.png) |

---

## Correlación con el Informe de Especificación de Requisitos

| Vista dinámica de este documento | Requisitos que ilustra |
|---|---|
| §2.1 Registro y bienvenida | RF-001, RF-018 |
| §2.2 Reservar un lavado | RF-005, RF-006, RF-010 |
| §2.3 Recordatorios y alerta | RF-018 |
| §2.4 Asignación y ejecución | RF-008, RF-009, RF-010, RF-011, RF-012, RF-012b |
| §2.5 Pago por comprobante | RF-013, RF-013b, RF-014, RF-016 |
| §2.6 Canje de cupón y puntos | RF-024, RF-025, RF-026 |
| §2.7 Roles y permisos | RF-029 |
| §2.8 Inicio de sesión con bloqueo | RF-002, RF-003, RF-004b |
| §2.9 Informe de inspección | RF-036, RF-018 |
| §2.11 Vencimiento del pago digital | RF-005, RF-033 |
| §3.1 a §3.6 Actividades | Todo el módulo de autenticación, reservas, operación, pagos y promociones |
| §4.1 a §4.5 Estados | RF-006, RF-007, RF-012b, RF-013b, RF-014, RF-024, RF-036 |
