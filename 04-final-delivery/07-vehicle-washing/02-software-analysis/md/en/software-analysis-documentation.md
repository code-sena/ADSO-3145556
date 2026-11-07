# Software Analysis: Dynamic Views of the LavaRápido System

Juan Camilo Guilombo Osorio, Brayan Estiven Patiño Cabrera and Merari Urbano Quintero

Centro de la Industria, la Empresa y los Servicios, Servicio Nacional de Aprendizaje (SENA), Huila Regional

Software Analysis and Development Technologist program (ADSO), group (ficha) 3145556

Instructor: Karol Daniela Correa

Version 2.8, October 27, 2026

---

## Introduction

This document shows how LavaRápido behaves when someone uses it: what happens, in which order, and between which screens and services. It is organized in four parts:

1. **Use cases by actor**, with their `«include»`/`«extend»` relationships.
2. **Sequence diagrams** of the main flows: from the screen to the gateway, from the gateway to the service, and from the service to the database or to another service.
3. **Activity diagrams** of the business processes.
4. **State diagrams** of the entities that change status (booking, payment, service execution, promotion and inspection report).

On October 21, 2026, the following were updated against the code of that day: the sign-out that voids the token (section 2.8), the lock on operator assignment (section 2.4), the receipt of at most 2 MB and the cash option that only appears while its account is active (sections 2.5 and 3.4), and the handling of bookings that ended without anyone closing them (sections 4.1 and 4.3): the system only changes the status when there is no doubt, and the rest goes to the administrator's "To close" list. On October 20, 2026, the content was checked again against the code: the client only cancels (the administrator reschedules), the role change of accounts was removed, the sequences and states that did not match were corrected, and the use case and other diagrams were redrawn with straight lines and no crossings. On October 19, 2026, use cases CU31 and CU32, password recovery (section 2.10) and the account life cycle (section 4.6) were added, and the screenshots of the screen inventory were taken again with demo data. On October 17, 2026, the consent to the legal documents at registration (section 2.1) and the inspection report from the mobile app (section 2.9) were added. The diagrams are written in Mermaid. They were checked against the code and the running system on October 9, 2026, and updated against the code on October 15, 2026 with the account lockout, automatic assignment, operator choice, the service start window, the alert for bookings without an operator and the inspection report. The screen inventory, with screenshots by role, is at the end. On October 23, 2026 (version 2.5) it was added that recovering the password revokes all sessions (section 2.10) and that, if security-service does not answer, the gateway lets the request through (*fail-open*); the rest of the analysis still holds against *develop* of that day. On October 24, 2026 (version 2.6), against *develop* of that day, the permission matrix became read-only (sections 2.7 and 3.6 and the screen inventory) and the automatic cancellation of expired scheduled bookings was removed, because every booking is created confirmed (section 4.1). On October 26, 2026 (version 2.7), against *develop* of that day, payment at booking was added: the customer chooses how they will pay when booking, with at most one active cash booking (sections 2.2 and 3.2); payment-service announces with `payment.reported` when the customer uploads the receipt (section 2.5), and a booking with digital payment that reaches its deadline without a receipt is cancelled automatically with the reason `PAYMENT_TIMEOUT`, after asking payment-service (sections 2.11 and 4.1). On October 27, 2026 (version 2.8), against `main` of that day (booking *v1.1.1*), a null, empty, or longer-than-10 list of services returns 400 `VALIDATION_ERROR` instead of 500, and the alternative times no longer include those that clash with another booking of the same vehicle (sections 2.2 and 3.2). The customer screenshots of the screen inventory (section 5.3) are those of October 19 and do not show these changes.

The full specification of each requirement and use case (actors, precondition, main flow, alternative flows and postcondition) is in the *Software Requirements Specification*, sections 5 and 7.3; here they are cited by their identifier (RF-XXX, CUXX).

---

## 1. Use Cases by Actor

### 1.1 Use Case Diagram

The 32 use cases are presented in four diagrams, one per actor, grouped by module.

**Use cases common to the three roles**

```mermaid
flowchart LR
    Usuario(["Any user"])
    CU01(["CU01 Sign in"])
    CU03(["CU03 Recover password"])
    CU04(["CU04 Change password"])
    CU16(["CU16 View dashboard"])
    CU17(["CU17 Manage notifications"])
    CU18(["CU18 Customize visual theme"])
    CU19(["CU19 Select language"])
    CU31(["CU31 Manage own account"])

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

**Customer use cases**

```mermaid
flowchart LR
    Cliente(["Customer"])
    Sistema(["System"])

    subgraph CTA["Account and vehicles"]
        CU02(["CU02 Create account"])
        CU26(["CU26 Manage vehicles"])
    end

    subgraph RES["Bookings"]
        CU05(["CU05 Request booking"])
        CU06(["CU06 Confirm booking based on availability"])
        CU07(["CU07 Reschedule or cancel booking"])
    end

    subgraph PAG["Payments and loyalty"]
        CU12(["CU12 Pay for a booking"])
        CU14(["CU14 View payment history"])
        CU25(["CU25 Apply promotion at payment"])
        CU15(["CU15 Rate service"])
    end

    subgraph INS["Inspection"]
        CU30(["CU30 Record inspection report"])
    end

    Cliente --- CU02
    Cliente --- CU26
    Cliente --- CU05
    Cliente --- CU07
    Cliente --- CU12
    Cliente --- CU14
    Cliente --- CU15
    Cliente --- CU25
    Cliente -. "views through a link" .- CU30
    Sistema --- CU06

    CU05 -. "«include»" .-> CU06
    CU25 -. "«extend»" .-> CU12

    classDef actor fill:#08427b,color:#fff,stroke:#052e56
    class Cliente,Sistema actor
```

**Operator use cases**

```mermaid
flowchart LR
    Operario(["Operator"])
    CU10(["CU10 View assigned services"])
    CU11(["CU11 Start and finish service"])
    CU22(["CU22 Manage operators and their availability"])

    Operario --- CU10
    Operario --- CU11
    Operario -. "views" .- CU22

    classDef actor fill:#08427b,color:#fff,stroke:#052e56
    class Operario actor
```

**Administrator and System use cases**

```mermaid
flowchart LR
    Administrador(["Administrator"])
    Sistema(["System"])

    subgraph AUTH["Users"]
        CU27(["CU27 Administer users and permissions"])
    end
    subgraph CAT["Catalog, opening hours and bays"]
        CU20(["CU20 Configure opening hours, bays and business data"])
        CU21(["CU21 Manage catalog and prices"])
    end
    subgraph RES["Bookings and operations"]
        CU08(["CU08 Assign operator automatically"])
        CU09(["CU09 Assign or reassign operator"])
        CU22b(["CU22 Manage operators and their availability"])
        CU29(["CU29 Manage bookings"])
        CU30b(["CU30 Record inspection report"])
    end
    subgraph PAG["Payments and promotions"]
        CU13(["CU13 Review payment"])
        CU28(["CU28 Manage payment accounts"])
        CU24(["CU24 Manage promotions"])
    end
    subgraph REP["Reports"]
        CU23(["CU23 View reports"])
    end
    subgraph AUT["Automatic processes"]
        CU32(["CU32 Run automatic processes"])
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

*Note.* `«include»` means that CU05 always runs CU06; `«extend»` means that CU25 optionally extends CU12. CU22 appears as view-only for the operator and as management for the administrator, who is the one recording shifts, absences and the operator profile (RF-022, RF-038). The system runs CU08 each time a booking is confirmed, and CU32 groups the processes that run without anyone asking for them (reminders, alerts, notices and the first administrator). The customer does not record the inspection report (CU30): they view it through the link the administrator shares. In CU07 the customer cancels from the history or the payment screen; a new date or time is set by the administrator from Bookings (CU29), since neither the web nor the app has a reschedule screen for the customer. In CU27 the role is chosen when the account is created; the users list does not change it.

### 1.2 Actor and Use Case Matrix

| Actor | Use cases |
|---|---|
| All roles | CU01, CU03, CU04, CU16, CU17, CU18, CU19, CU31 |
| Customer | CU02, CU05, CU06, CU07, CU12, CU14, CU15, CU25, CU26, CU30 (view only) |
| Operator | CU10, CU11, CU22 (view only) |
| Administrator | CU09, CU13, CU20, CU21, CU22, CU23, CU24, CU27, CU28, CU29, CU30 |
| System | CU06 (included in CU05), CU08, CU32 |

The specification of each of the 32 use cases (precondition, main flow, alternative flows, postcondition, status) is in the *Software Requirements Specification*, Tables 23 to 54.

---

## 2. Sequence Diagrams

Each diagram follows the path of the request (screen, gateway, services, database and RabbitMQ) with the class and endpoint names from the code. The first five are based on the diagrams in `vehicle-w-docs/08-uml/diagrams/source/`; the next ones cover loyalty, roles and permissions, the account lockout and the inspection report.

### 2.1 Registration and welcome (RF-001, RF-018)

```mermaid
sequenceDiagram
    autonumber
    actor C as Customer
    participant W as Web
    participant GW as API Gateway
    participant S as security-service
    participant MQ as RabbitMQ
    participant CU as customer-service
    participant N as notification-service
    participant M as SMTP

    C->>W: Fills in the form and checks the Terms and the Data Processing Policy
    W->>GW: POST /api/v1/auth/register (acceptsTerms, acceptsDataPolicy)
    GW->>S: routes
    S->>S: Validates (password policy, unique email and ID number), saves person + account (CLIENT) and the acceptance date of each legal document
    S-->>W: 201 Created
    S-)MQ: UserRegistered (security.user_registered), after commit
    par customer profile
        MQ-)CU: UserRegistered
        CU->>CU: Creates the customer profile (idempotent)
    and welcome
        MQ-)N: UserRegistered
        N->>N: Creates WELCOME notification (idempotent: processed_event)
        N->>M: Welcome email (the address comes in the event)
    end
    C->>W: Signs in
    W->>GW: POST /api/v1/auth/login
    GW->>S: routes
    S-->>W: 200 JWT (HS256, 1 h, roles claim)
```

*Source:* `security-service` (`AccountRegistrationService`, `AuthenticationService`, migration `039-security-app-user-legal-consent.sql`), event `security.user_registered`. The web and the mobile app do not send the form until both boxes are checked; the server stores the dates only when both arrive accepted.

### 2.2 Booking a wash (RF-005, RF-006, RF-010)

```mermaid
sequenceDiagram
    autonumber
    actor C as Customer
    participant W as Web (via gateway)
    participant B as booking-service
    participant CU as customer-service
    participant DB as SQL Server
    participant MQ as RabbitMQ
    participant N as notification-service
    participant S as security-service
    participant OP as operations-service

    C->>W: Chooses vehicle, service and date
    W->>B: GET /api/v1/bookings/availability?date=…&vehicleTypeId=…&serviceIds=…
    B->>DB: opening hours, exceptions, bays, bookings of the day
    B-->>W: free times
    C->>W: Chooses a time
    W->>OP: GET /api/v1/operators/available?date=…&time=…&minutes=…
    OP-->>W: free operators (first name and rating)
    C->>W: Chooses an operator (optional)
    C->>W: How will you pay? Digital payment now or cash at the car wash, and confirms
    W->>B: POST /api/v1/bookings {…, preferredOperatorId, paymentMode}
    alt serviceIds null, empty, or with more than 10 services
        B-->>W: 400 VALIDATION_ERROR
    end
    alt no paymentMode or an invalid value (customer only)
        B-->>W: 400 INVALID_PAYMENT_MODE
    end
    B->>CU: GET /api/v1/vehicles/{id} (customer JWT)
    CU-->>B: vehicle + type (ownership is validated, INV-BOOK-002)
    B->>DB: sp_getapplock (schedule lock)
    alt CASH and the customer already has 1 active cash booking made by themselves
        B-->>W: 409 CASH_BOOKING_LIMIT
    else free bay and the vehicle has no other booking at that time
        B->>DB: INSERT CONFIRMED booking + bay + lines with frozen prices<br/>+ payment_mode and, if DIGITAL, payment_deadline = min(now + 30 min, start)
        B-->>W: 201 booking (code RES-000042, paymentMode, paymentDeadline)
        W-->>C: DIGITAL: goes to pay ("Upload the receipt before HH:mm")<br/>CASH: "You pay in cash at the car wash"
        B-)MQ: BookingConfirmed (booking.confirmed, with preferredOperatorId), after commit
        MQ-)OP: BookingConfirmed: automatic assignment (section 2.4)
        MQ-)N: BookingConfirmed
        N->>S: GET /internal/v1/users/{id}/contact (X-Internal-Key)
        N->>N: BOOKING_CONFIRMED notification + reminders (24 h, 1 h)
        N-->>C: inbox + push + email
    else the vehicle already has another booking at that time
        B-->>W: 409 VEHICLE_ALREADY_BOOKED
    else slot taken
        B-->>W: 409 SLOT_UNAVAILABLE + reason + up to 5 alternative times without clashes with other bookings of the vehicle
    end
```

*Source:* `booking-service` (`BookingService.create`, `Booking.confirmNew`, `AvailabilityPolicy`, `PaymentPolicy`), `operations-service` (`AvailableOperatorsController`), contracts in `07-api/contracts/openapi/`. The cash limit (`BOOKING_CASH_ACTIVE_LIMIT`, 1) is counted with the schedule locked and only with the active bookings (Scheduled, Confirmed, or In progress) made by the customer themselves (`booked_by`). The administrator does not have to send `paymentMode`, and their bookings have no deadline or limit. Since October 27, 2026, a booking has at most 10 services (`BookingDtos.MAX_SERVICES`, also when rescheduling): a null, empty, or longer list returns 400 `VALIDATION_ERROR` instead of 500, and the alternative times leave out those that clash with another booking of the same vehicle (`AvailabilityPolicy.withoutBusyTimes`).

### 2.3 Reminders and alert for bookings without an operator (ADR-011)

```mermaid
sequenceDiagram
    autonumber
    participant MQ as RabbitMQ
    participant N as notification-service
    participant DB as notification.booking_reminder
    participant J as ReminderScheduler
    participant S as security-service
    actor C as Customer

    MQ-)N: BookingConfirmed (new or rescheduled)
    N->>DB: Replaces pending reminders: one per lead time (24 h, 1 h) that is still in the future
    MQ-)N: BookingCancelled, ServiceStarted or ServiceCompleted
    N->>DB: Cancels the pending reminders of that booking (soft delete)
    MQ-)N: OperatorAssigned
    N->>DB: The booking now has an operator (booking_tracking)
    loop every 5 minutes (REMINDER_CHECK_INTERVAL)
        J->>DB: Due reminders (remind_at <= now, not sent)
        J->>S: GET /internal/v1/users/{id}/contact (X-Internal-Key): email and language
        J->>N: BOOKING_REMINDER notification in the account's language
        N-->>C: inbox + push + email
        J->>DB: sets sent_at (never sent twice)
        J->>DB: Confirmed bookings starting in less than 2 h without an operator and without an alert
        J->>N: Alert to every active administrator and sets unassigned_alert_at
    end
```

*Source:* `notification-service` (`BookingReminderService`, `ReminderScheduler`, `StaffNotificationService`, migration 031).

### 2.4 Operator assignment, execution and rating (RF-008, RF-009, RF-010, RF-011, RF-012, RF-012b)

```mermaid
sequenceDiagram
    autonumber
    actor A as Administrator
    actor O as Operator
    actor C as Customer
    participant W as Web (via gateway)
    participant OP as operations-service
    participant B as booking-service
    participant S as security-service
    participant DB as SQL Server (execution)
    participant MQ as RabbitMQ
    participant N as notification-service

    MQ-)OP: BookingConfirmed (bookingId, preferredOperatorId)
    OP->>B: GET /internal/v1/bookings/{id} and ?from=&to= (X-Internal-Key)
    OP->>DB: assignment lock (sp_getapplock): one assignment at a time
    OP->>S: GET /internal/v1/users/{id}/contact (active account)
    OP->>DB: shifts, absences, executions of the day
    alt there are free operators
        OP->>OP: AutoAssignmentPolicy: the customer's choice if free or, otherwise, the one with the fewest bookings that day
        OP->>DB: one PENDING service_execution per line (actor: system)
        OP-)MQ: OperatorAssigned (execution.operator_assigned)
        MQ-)N: notifies the operator and the customer
    else nobody free or the booking already had an operator
        OP->>OP: no assignment (the administrator gets the alert, section 2.3)
    end

    A->>W: Opens a booking → Operator assignment
    W->>OP: GET /api/v1/admin/assignments/{bookingId}/candidates
    OP->>B: GET /admin/bookings/{id} and /admin/bookings?from=&to= (admin JWT)
    OP->>S: GET /admin/users?role=OPERATOR (admin JWT)
    OP->>DB: shifts, absences, executions of the day
    OP-->>W: each operator + blocking reason (available ones first)
    A->>W: Picks an operator
    W->>OP: PUT /api/v1/admin/assignments/{bookingId}
    OP->>DB: the same lock: if the automatic one is assigning, it waits and then sees it
    alt booking SCHEDULED/CONFIRMED and AssignmentPolicy finds no blocking reason
        OP->>DB: one PENDING service_execution per booking line
        OP-->>W: 200 assignment
        OP-)MQ: OperatorAssigned (execution.operator_assigned), after commit
        MQ-)N: notifies the operator and the customer
    else inactive / off shift / absent / busy
        OP-->>W: 409 OPERATOR_INACTIVE | OPERATOR_NOT_ON_SHIFT | OPERATOR_ABSENT | OPERATOR_BUSY
    end

    O->>W: Assigned services → Start service
    W->>OP: POST /api/v1/operator/services/{bookingId}/start
    alt more than 15 minutes before the time (StartPolicy)
        OP-->>W: 409 SERVICE_TOO_EARLY
    end
    OP->>B: PATCH /operator/bookings/{id}/status IN_PROGRESS (operator JWT)
    OP->>DB: lines → IN_PROGRESS, started_at
    OP-)MQ: ServiceStarted (execution.service_started)
    O->>W: Finish service
    W->>OP: POST /api/v1/operator/services/{bookingId}/finish
    OP->>B: PATCH /operator/bookings/{id}/status COMPLETED
    OP->>DB: lines → COMPLETED, finished_at
    OP-)MQ: ServiceCompleted (execution.service_completed)
    MQ-)N: tells the customer the wash is done and cancels the reminders

    C->>W: History → rates the completed booking
    W->>OP: POST /api/v1/ratings/{bookingId} {score 1–5, comment ≤ 500}
    OP->>B: GET /bookings/{id} (customer JWT: only their own bookings)
    OP->>DB: rating on each line (only once)
    OP-->>W: 201 rating
```

*Source:* `operations-service` (`BookingConfirmedListener`, `OperationsService.autoAssign / candidates / assign / start / finish / rate`, `ExecutionPersistenceAdapter.lockAssignments`, `AssignmentPolicy`, `AutoAssignmentPolicy`, `StartPolicy`, ADR-014). The lock prevents a simultaneous manual and automatic assignment from giving the same operator two services at the same time.

### 2.5 Payment by receipt and review (RF-013, RF-013b, RF-014)

```mermaid
sequenceDiagram
    autonumber
    actor C as Customer
    actor A as Administrator
    participant W as Web (via gateway)
    participant P as payment-service (.NET)
    participant B as booking-service
    participant DB as SQL Server (payment)

    C->>W: Pays for the service
    W->>P: GET /api/v1/payment-accounts
    P-->>W: active accounts (QR and instructions, the cash one only while it is active)
    C->>C: Pays from the bank app (outside the platform)
    C->>W: Account, reference code, receipt image
    W->>W: compresses the image to at most 2 MB
    W->>P: POST /api/v1/payments
    P->>B: GET /bookings/{id} (customer JWT)
    B-->>P: status, total, owner
    alt booking can be paid, has no open or approved payment and the reference was not used on another booking
        P->>DB: PENDING payment (amount = booking total minus<br/>the coupons applied) + receipt → IN_REVIEW
        P-->>W: 201 payment IN_REVIEW
        P-)B: PaymentReported (payment.reported) through RabbitMQ: booking sets payment_reported_at and the booking no longer expires (section 2.11)
    else not payable / already reported / already approved / reference reused
        P-->>W: 409 BOOKING_NOT_PAYABLE | PAYMENT_ALREADY_REPORTED | PAYMENT_ALREADY_APPROVED | TRANSACTION_REFERENCE_REUSED
    else receipt larger than 2 MB
        P-->>W: 400 RECEIPT_TOO_LARGE
    end

    A->>W: Payments → Verify
    W->>P: GET /api/v1/admin/payments?status=IN_REVIEW
    P->>B: GET /admin/bookings/{id} (admin JWT) for each payment
    P-->>W: payments with their booking data
    alt approve
        W->>P: POST /api/v1/admin/payments/{id}/approve
        P->>DB: APPROVED, approved_by, processed_at (unique index: one per booking)
        P->>DB: loyalty_transaction EARNED (points of the booking lines)
        P-)P: payment.confirmed and payment.loyalty_points_earned to RabbitMQ
    else reject
        W->>P: POST /api/v1/admin/payments/{id}/reject {reason}
        P->>DB: REJECTED + reason (the customer can report again)
        P-)P: payment.rejected to RabbitMQ
    end

    A->>W: Payments → Manual record (cash at the site)
    W->>P: POST /api/v1/admin/payments {bookingId, paymentAccountId}
    P->>B: GET /admin/bookings/{id}
    P->>DB: APPROVED payment directly (amount = amount due) + EARNED points
    A->>W: Payments → Refund an approved payment
    W->>P: POST /api/v1/admin/payments/{id}/refund
    P->>DB: REFUNDED + loyalty_transaction REVERSED
```

*Source:* `payment-service` (`PaymentCommandService.ReportAsync / ApproveAsync / RejectAsync / RegisterInPersonAsync / RefundAsync`, `BookingPaymentGuard`, `LoyaltyRewardService`, `Payment` aggregate, ADR-008, ADR-013). notification-service turns each `payment.*` event into a notice for the customer.

### 2.6 Coupon redemption and loyalty points (RF-024, RF-025, RF-026)

```mermaid
sequenceDiagram
    autonumber
    actor C as Customer
    actor A as Administrator
    participant W as Web (via gateway)
    participant P as payment-service (.NET)
    participant B as booking-service
    participant DB as SQL Server (promotion / payment)
    participant MQ as RabbitMQ
    participant N as notification-service

    A->>W: Management → Promotions → Create
    W->>P: POST /api/v1/admin/promotions {code, validity, type PERCENT or FIXED and value, required points}
    P->>DB: INSERT promotion
    P-->>W: 201 promotion

    Note over C,P: --- Points credit (when a payment is approved, flow 2.5) ---
    P->>DB: INSERT loyalty_transaction EARNED (+points of the booking lines, unique sequence_no per customer)
    Note over P: The balance is never stored separately: it is always calculated<br/>from the last row of the ledger of movements (INV-LOY-001)

    C->>W: Dashboard → sees their points and unlocked promotions
    W->>P: GET /api/v1/loyalty/balance
    P->>DB: SELECT the customer's last movement (current balance)
    P-->>W: current points
    W->>P: GET /api/v1/loyalty/promotions
    P->>DB: active promotions
    P-->>W: each promotion + whether it is already unlocked (points >= required)

    C->>W: Payment of a booking → enters the coupon code
    W->>P: POST /api/v1/loyalty/redeem {bookingId, code}
    P->>B: checks that the booking belongs to the customer and has no reported payment
    P->>DB: looks up the promotion by code
    alt valid, unlocked, not redeemed before for this booking and within the usage limits
        P->>P: Promotion.Redeem(...) calculates the discount on the subtotal (never exceeds it)
        P->>DB: INSERT booking_promotion (applied_amount)
        P-)MQ: PromotionRedeemed (payment.promotion_redeemed)
        MQ-)N: notifies the redemption in the inbox and by email
        P-->>W: 200 {discount applied, new total to pay}
    else the booking already has a reported or approved payment
        P-->>W: 409 PAYMENT_ALREADY_REPORTED | PAYMENT_ALREADY_APPROVED
    else expired / not unlocked / already redeemed / minimum purchase / no redemptions left
        P-->>W: 400 PROMOTION_NOT_REDEEMABLE | PROMOTION_ALREADY_REDEEMED | PROMOTION_MIN_PURCHASE_NOT_MET | PROMOTION_EXHAUSTED | PROMOTION_CUSTOMER_LIMIT_REACHED
    end
```

*Source:* `payment-service` (`LoyaltyController`, `CustomerPromotionService.RedeemAsync`, `Promotion.Redeem` aggregate, `LoyaltyLedgerRepository`), ADR-015.

### 2.7 Roles and permissions management (RF-029)

```mermaid
sequenceDiagram
    autonumber
    actor A as Administrator
    participant W as Web (via gateway)
    participant S as security-service
    participant DB as SQL Server (security)

    A->>W: Management → Roles
    W->>S: GET /api/v1/admin/roles-permissions
    S->>DB: SELECT permission catalog + permissions of each role
    S-->>W: 200 {permissions[], roles: [{role, permissions[]}]}, 3 fixed roles: ADMIN, OPERATOR, CLIENT
    W-->>A: Read-only matrix (since October 24, 2026 the web no longer offers checking or saving)
    Note over S: Roles cannot be created or deleted (ADR-010).<br/>This matrix is informational: real authorization still comes<br/>from the roles claim in the JWT, not from this table (open decision, section 11.3 of the SRS)
```

*Source:* `security-service` (`RolePermissionController`, `RolePermissionAdministrationService`, migration `026-security-role-permissions.sql`), ADR-015.

### 2.8 Sign-in with lockout after failed attempts and sign-out (RF-002, RF-004b, RF-027)

```mermaid
sequenceDiagram
    autonumber
    actor U as User
    participant W as Web or app
    participant GW as API Gateway
    participant S as security-service
    participant DB as SQL Server (security)

    U->>W: Email and password
    W->>GW: POST /api/v1/auth/login
    GW->>S: routes
    S->>DB: account by email and its login_lockout row
    alt account locked (locked_until in the future)
        S-->>W: 423 ACCOUNT_LOCKED
        W-->>U: Locked account message (in the app, with the option to recover the password)
    else wrong password
        S->>DB: adds the failure (15-min window) and on the fifth one sets locked_until to now + 15 min
        S-->>W: 401 INVALID_CREDENTIALS<br/>(or 423 if this failure locked it)
    else correct password and deactivated account
        S-->>W: 403 ACCOUNT_DISABLED
    else correct password
        S->>DB: clears the failure count, records the session
        S-->>W: 200 JWT (1 h)
    end
    Note over S,DB: Resetting the password with the code (RF-003) also lifts the lockout

    U->>W: Sign out
    W->>GW: POST /api/v1/auth/logout
    GW->>GW: forgets the cached answer of this token
    GW->>S: routes
    S->>DB: closes the session (revoked_at)
    W-->>U: back to sign-in (the other tabs of the browser too)
    Note over W,DB: Another call with the same token (a copied token)
    W->>GW: GET /api/v1/bookings (token of the closed session)
    GW->>S: GET /api/v1/auth/session (same token)
    S->>DB: session open and not expired?
    S-->>GW: 401 UNAUTHORIZED
    GW-->>W: 401 (booking-service never gets the call)
```

*Source:* `security-service` (`AuthenticationService.login / logout / isActive`, `ActiveSessionValidator`, `LoginLockout`, `PasswordRecoveryService`, migration `034-security-login-lockout.sql`) and the gateway (`SessionCheckFilter`). The gateway keeps each answer of `/api/v1/auth/session` (if security-service does not answer within 3 seconds, it lets the request through: *fail-open*) for 30 seconds per token, so an open session costs one extra call every half minute, not one per request. A session also closes when the administrator deactivates the account or the owner closes it; that reaches the other services within the same 30 seconds.

### 2.9 Vehicle inspection report (RF-036)

```mermaid
sequenceDiagram
    autonumber
    actor A as Administrator
    actor C as Customer
    participant W as Web or mobile app (through the gateway)
    participant OP as operations-service
    participant B as booking-service
    participant DB as SQL Server (execution)
    participant MQ as RabbitMQ
    participant N as notification-service

    A->>W: Booking of 180 min or more → Inspection report
    W->>OP: POST /api/v1/admin/inspections/{bookingId}
    OP->>B: GET /admin/bookings/{id} (admin JWT)
    alt long booking, Confirmed or In progress
        OP->>DB: report with 5 PENDING phases and a random public code
        OP-->>W: 200 report (if one already exists, the same one)
    else short, cancelled or completed
        OP-->>W: 409 INSPECTION_NOT_EDITABLE
    end
    A->>W: Marks phases and records findings with photos
    W->>OP: PATCH /phases/{phase} and POST /findings (multipart, up to 5 photos)
    OP->>DB: phases, findings and photos
    A->>W: Publish and share
    W->>OP: POST /publish and POST /share
    OP-)MQ: InspectionShared (execution.inspection_shared)
    MQ-)N: notification to the customer with the link, in their language
    C->>W: Opens the link /report/{token}
    W->>OP: GET /api/v1/public/inspections/{token} (no session)
    OP-->>W: published report with a masked license plate
```

*Source:* `operations-service` (`InspectionReport`, migration `033-execution-inspection-report.sql`), `notification-service` (shared report notice), web (`features/admin/pages/inspection`), and mobile app (`AdminInspectionScreen`, `InspectionService`). In the app the photos are taken with the camera or picked from the gallery and shrunk on the phone before the `POST /findings`; the **Publish and share link** button opens the phone share menu.

### 2.10 Password recovery with a 6-digit code (RF-003)

```mermaid
sequenceDiagram
    autonumber
    actor U as User
    participant W as Web or app
    participant S as security-service
    participant DB as SQL Server (security)
    participant M as Email (SMTP)

    U->>W: Forgot my password → email
    W->>S: POST /api/v1/auth/password/forgot
    alt active account with that email
        S->>DB: invalidates the previous codes that are still valid
        S->>DB: stores the hash of the new code (expires in 15 min)
        S->>M: email with the code
    else unknown email or inactive account
        S->>S: does nothing
    end
    S-->>W: 202 (the same answer in both cases)
    U->>W: Types the code
    W->>S: POST /api/v1/auth/password/verify
    alt right code
        S-->>W: 204
    else wrong code
        S->>S: counts the attempt, at the fifth the code is invalidated
        S-->>W: 400 INVALID_RESET_CODE
    end
    U->>W: New password
    W->>S: POST /api/v1/auth/password/reset
    alt policy met and different from the current one
        S->>DB: marks the code as used, stores the new password, clears the lockout, and revokes all sessions
        S-->>W: 204 (back to sign-in)
    else weak or same as the current one
        S-->>W: 400 WEAK_PASSWORD or SAME_PASSWORD (the code is not spent)
    end
```

*Source:* `security-service` (`PasswordRecoveryService`, `PasswordResetToken`, `PasswordRecoveryPolicy` with `code-ttl: 15m` and `max-failed-attempts: 5`, `SecureRandomResetCodeGenerator`, `EmailResetCodeNotifier`). The *forgot* answer does not reveal whether the email exists.

### 2.11 Expiry of the digital payment (RF-005, RF-033, RN-BOOK-03)

```mermaid
sequenceDiagram
    autonumber
    participant MQ as RabbitMQ (carwash.events)
    participant B as booking-service
    participant J as PaymentDeadlineJob (booking, every 60 s)
    participant P as payment-service (.NET)
    participant DB as SQL Server (booking)
    participant N as notification-service

    MQ-)B: payment.reported or payment.confirmed (queue booking.payment-events)
    B->>DB: payment_reported_at = now (idempotent)
    J->>DB: DIGITAL bookings without a report whose payment_deadline has passed (ix_booking_payment_pending)
    loop each overdue booking, in its own transaction
        J->>P: GET /internal/v1/payments/bookings/{id}/reported (X-Internal-Key, 3 s)
        alt reported = true (the event had been lost)
            P-->>J: 200 {"bookingId":…, "reported":true}
            J->>DB: payment_reported_at = now (PAYMENT_REPORTED, not cancelled)
        else reported = false
            P-->>J: 200 {"bookingId":…, "reported":false}
            J->>DB: CANCELLED with the reason PAYMENT_TIMEOUT, the bay is freed (CANCELLED)
            B-)MQ: BookingCancelled (booking.cancelled, reasonCode PAYMENT_TIMEOUT)
            MQ-)N: BookingCancelled: notice to the customer
        else payment-service does not answer or returns an error
            J->>J: not cancelled this round (PAYMENT_SERVICE_UNAVAILABLE), retried in the next one
        end
    end
```

*Source:* `booking-service` (`PaymentEventsListener`, `RabbitConsumerConfig`, `PaymentDeadlineJob`, `BookingService.cancelForPaymentTimeout`, `PaymentServiceDirectory`, `PaymentTimeoutOutcome`) and `payment-service` (`InternalPaymentsController`, `InternalApiKeyAuthenticationHandler`). Since there is no Outbox, a `payment.reported` event can be lost; that is why the job asks payment-service before cancelling (AC-89). For payment, `reported` is true if the booking has an `IN_REVIEW` or `APPROVED` payment. The job is turned off with `BOOKING_PAYMENT_EXPIRY_ENABLED=false`, and the interval is `BOOKING_PAYMENT_EXPIRY_INTERVAL` (60 s).

---

## 3. Activity Diagrams

The activity diagrams show the business processes as they work in the system. There is no payment gateway: the customer uploads a receipt and the administrator reviews it (ADR-008).

### 3.1 Authentication (RF-001 to RF-004)

```mermaid
flowchart TD
    start([Start]) --> menu[Show the sign-in screen]
    menu --> choice{What does the user want to do?}

    choice -->|Register| f1[Show the registration form]
    f1 --> d1[Enter data and accept the data policy]
    d1 --> v1{Fields complete and<br/>email valid and available?}
    v1 -->|No| e1[Show the field with the error]
    e1 --> d1
    v1 -->|Yes| v2{Does the password meet<br/>the RNF-004 policy?}
    v2 -->|No| e2[Show the password requirements]
    e2 --> d1
    v2 -->|Yes| ok1[Create account + customer profile]
    ok1 --> mail1[Send welcome email]
    mail1 --> stop1([End])

    choice -->|Sign in| f2[Show the sign-in form]
    f2 --> d2[Enter email and password]
    d2 --> v3{Account exists, is<br/>active and password is correct?}
    v3 -->|No| e3[Generic error message]
    e3 --> d2
    v3 -->|"5 failures in 15 min"| lock["Lock the account for 15 min (RF-004b)"]
    lock --> offer[Offer password recovery]
    offer --> stop2([End])
    v3 -->|Yes| ok2[Issue JWT and redirect to the dashboard of the role]
    ok2 --> stop3([End])

    choice -->|Recover password| f3[Ask for the email]
    f3 --> d3[Enter email]
    d3 --> v4{Is the email registered?}
    v4 -->|No| e4["Same generic response<br/>(does not reveal whether it exists)"]
    e4 --> stop4([End])
    v4 -->|Yes| code[Send a 6-digit code that expires in 15 min]
    code --> d4[Enter code and new password]
    d4 --> v5{Is the code valid<br/>and not expired?}
    v5 -->|No, 5 attempts| e5[Request a new code]
    e5 --> f3
    v5 -->|Yes| same{New password<br/>equal to the current one?}
    same -->|Yes| e7[Ask for a different password]
    e7 --> d4
    same -->|No| ok3[Update password, invalidate code and lift the lockout]
    ok3 --> stop5([End])

    choice -->|Change password| auth{Is the user authenticated?}
    auth -->|No| stop6([End])
    auth -->|Yes| d5[Enter current and new password]
    d5 --> v6{Current one correct and<br/>new one meets the policy?}
    v6 -->|No| e6[Show error]
    e6 --> d5
    v6 -->|Yes| ok4[Update password]
    ok4 --> stop7([End])
```

### 3.2 Booking a wash (RF-005, RF-006, RF-007, RF-010)

```mermaid
flowchart TD
    start([Start]) --> veh{Does the customer have<br/>at least one vehicle?}
    veh -->|No| regveh["Ask them to register a vehicle (CU26)"]
    regveh --> stop0([End])
    veh -->|Yes| sel[Choose vehicle and 1 to 10 services]
    sel --> price[Show the price for the vehicle type]
    price --> date[Choose date and time]
    date --> op{"Does the customer want to choose the operator?<br/>(list of those free at that time)"}
    op -->|Yes| pickop[Choose a free operator]
    op -->|No| pay{"How will you pay?"}
    pickop --> pay
    pay -->|Digital payment| check[Check opening hours, exceptions and bays]
    pay -->|Cash| cashlim{Does the customer already have<br/>an active cash booking?}
    cashlim -->|Yes| cashno["Reject (CASH_BOOKING_LIMIT): pay it or cancel it, or choose digital payment"]
    cashno --> pay
    cashlim -->|No| check
    check --> avail{Is there a free bay<br/>in that slot?}
    avail -->|No| alt[Offer up to 5 alternative times on the same day<br/>without clashes with other bookings of the vehicle]
    alt --> date
    avail -->|Yes| confirm[Confirm booking: assign bay, freeze prices]
    confirm --> notif["Send confirmation, schedule 24h/1h reminders<br/>and assign an operator (section 3.3)"]
    notif --> conf([Booking Confirmed])
    conf --> due{"Digital payment: was the receipt uploaded<br/>before the deadline (30 min or the start time)?"}
    due -->|No, and payment-service does not have the payment| timeout[Cancel with PAYMENT_TIMEOUT and free the bay]
    timeout --> stop4([End])
    due -->|Yes, or pays in cash| manage

    manage{Any change<br/>before the start?}
    manage -->|The customer cancels| state{Is the booking Scheduled<br/>or Confirmed?}
    state -->|No - in progress/finished| block[Block the action with an explanatory message]
    block --> stop2([End])
    state -->|Yes| ask[Confirm in the dialog, no reason asked]
    ask --> free[Free the bay, mark as Cancelled]
    free --> stop3([End])
    manage -->|The administrator reschedules| resched["New date and time from Bookings (CU29)"]
    resched --> check
```

### 3.3 Operator assignment and service execution (RF-008, RF-009, RF-010, RF-011, RF-012b)

```mermaid
flowchart TD
    start0((Start)) --> start
    start([Confirmed booking without an operator]) --> auto{"Does automatic assignment<br/>find an eligible operator?<br/>(RF-008)"}
    auto -->|Yes| pref{"Did the customer choose an operator<br/>who is still free? (RF-010)"}
    pref -->|Yes| chosen[Assign the chosen operator]
    pref -->|No| pick[Pick the one with the fewest bookings that day<br/>tie: lowest identifier]
    chosen --> notifyop[Notify the customer and the operator]
    pick --> notifyop
    notifyop --> wait([Wait for the booking time])
    auto -->|No eligible operators| manual["Booking without an operator; alert to the administrator<br/>when less than 2 h remain"]
    manual --> list[Admin sees candidates: available first, with rating]
    list --> chooseop[Admin picks an operator]
    chooseop --> valid{Operator active, on shift,<br/>not absent and with no schedule overlap?}
    valid -->|No| blockop[Block assignment, show reason]
    blockop --> list
    valid -->|Yes| assign[Create PENDING execution, notify the operator]
    assign --> wait

    wait --> today{"Are 15 minutes or less left<br/>before the booking time?"}
    today -->|No| blockday[Button disabled: SERVICE_TOO_EARLY]
    blockday --> stopC([End])
    today -->|Yes| startsvc[Operator marks Start service]
    startsvc --> inprog[Execution → In progress]
    inprog --> finish[Operator marks Finish service]
    finish --> completed[Execution → Completed, booking → Completed;<br/>reminders are cancelled]
    completed --> rate["Customer can rate (CU15)"]
    rate --> stopD([End])
```

### 3.4 Paying for a booking (RF-013, RF-013b, RF-014, RF-016)

```mermaid
flowchart TD
    start0((Start)) --> start
    start([Confirmed booking without an approved payment]) --> method{Which payment method?}

    method -->|QR / Transfer / Nequi / Daviplata| showqr[Show the QR of the active account and instructions]
    showqr --> extpay[Customer pays from their bank app<br/>outside the platform]
    extpay --> upload[Uploads receipt compressed to max. 2 MB<br/>+ declared amount + reference code]
    upload --> dup{Reference code<br/>already used in another booking?}
    dup -->|Yes| rejectref[Reject: TRANSACTION_REFERENCE_REUSED]
    rejectref --> stop1([End])
    dup -->|No| inreview["Payment → Under review (linked to the booking)"]
    inreview --> waitadm([Waits for the admin review])

    method -->|Cash, if its account is active| cash[Customer pays at the site]
    cash --> register[Admin records the payment manually]
    register --> approved1["Payment → Approved directly"]
    approved1 --> points1["Credit loyalty points (RF-026)"]
    points1 --> lockui

    waitadm --> review[Admin opens pending payments]
    review --> compare{Declared amount =<br/>amount due and receipt readable?}
    compare -->|No| reject[Reject with a reason, customer can resend]
    reject --> stop4([End])
    compare -->|Yes| approve["Payment → Approved"]
    approve --> points2["Credit loyalty points (RF-026)"]
    points2 --> notifyc[Notify the customer]
    notifyc --> lockui[The payment screen only shows the summary<br/>no method, QR or receipt]
    lockui --> stop5([End])
```

### 3.5 Redeeming a loyalty coupon (RF-024, RF-025, RF-026)

```mermaid
flowchart TD
    start0((Start)) --> start
    start([Customer on the payment screen]) --> has{Do they have promotions<br/>unlocked by their points?}
    has -->|No| noshow[No coupon is offered]
    noshow --> stop1([End])
    has -->|Yes| enter[Enters the coupon code]
    enter --> valid{Valid, active,<br/>unlocked and not redeemed<br/>before for this booking?}
    valid -->|No| inform["Report the reason and keep the original amount<br/>(expired coupon / not enough points / already redeemed / used up)"]
    inform --> stop2([End])
    valid -->|Yes| limit{"Within the usage limit<br/>(total and per customer)?"}
    limit -->|No| exhausted[Report: coupon used up]
    exhausted --> stop3([End])
    limit -->|Yes| calc[Calculate the discount on the subtotal<br/>without exceeding it]
    calc --> apply[Apply the discount to the total to pay]
    apply --> notify[Notify the redemption in the inbox and by email]
    notify --> stop4([Payment continues for the discounted amount])
    stop4 --> stop4x((End))
```

### 3.6 Managing roles and permissions (RF-029)

```mermaid
flowchart TD
    start0((Start)) --> start
    start([Admin opens Management → Roles]) --> load[Load the 3 fixed roles + permission catalog]
    load --> view[View the current permissions of each role: Administrator, Operator, Customer]
    view --> stop([View only since October 24, 2026: permissions are not edited and roles are not created or deleted])
    stop --> stopx((End))
```

---

## 4. State Diagrams

### 4.1 Booking life cycle (RF-006, RF-007)

```mermaid
stateDiagram-v2
    [*] --> CONFIRMED : create (a free bay is assigned immediately, RF-006)
    SCHEDULED --> CONFIRMED : confirm (needs a bay)
    SCHEDULED --> CANCELLED : cancel
    CONFIRMED --> CONFIRMED : reschedule (admin, new time/bay, prices are kept)
    CONFIRMED --> IN_PROGRESS : start or Attended (admin)
    CONFIRMED --> NO_SHOW : no show, or ended without operator
    CONFIRMED --> CANCELLED : cancel (customer before the start, or admin)
    CONFIRMED --> CANCELLED : the digital payment deadline passes without a receipt (system, PAYMENT_TIMEOUT)
    IN_PROGRESS --> COMPLETED : finish or Attended (admin)
    COMPLETED --> [*]
    CANCELLED --> [*]
    NO_SHOW --> [*]
```

*Source:* `booking-service` (`Booking.moveTo`, `Booking.cancel`, `BookingStatus`) and `operations-service` (`FollowUpJob`, `FollowUpPolicy`). *Bookings that ended and nobody closed:* the system only decides on its own when there is no doubt. Until October 23, 2026 `ExpiredBookingsJob` cancelled bookings still `SCHEDULED`; it was removed on October 24 (B-07) because it never acted: every booking is created and rescheduled as `CONFIRMED`. Every 5 minutes, the operations `FollowUpJob` asks booking (`POST /internal/v1/bookings/{id}/no-show`) to set to `NO_SHOW` a `CONFIRMED` booking that never had an operator and ended more than an hour ago; booking checks again that it expired and, if not, answers 409 `BOOKING_NOT_EXPIRED`. A `CONFIRMED` booking with an operator that nobody started (`NOT_STARTED`) and an `IN_PROGRESS` one that nobody finished one hour after its end (`NOT_FINISHED`) **never** change status on their own: they go to the administrator's "To close" list, and the administrator resolves them with "Attended" (the booking goes through `IN_PROGRESS` and ends `COMPLETED`, without publishing `ServiceCompleted`) or with "No-show" (only if it was never started). This avoids recording as a no-show a service that was actually done and that the operator forgot to mark. There is never an automatic refund: payments are reviewed by the administrator. Only `SCHEDULED` and `CONFIRMED` can be rescheduled or cancelled (RF-007). The customer only cancels, before the start time and without writing a reason (the booking stores `CUSTOMER_REQUEST`); the administrator reschedules (CU29) and may choose the cancellation reason. `SCHEDULED` exists in the model, but the current flow always creates the booking directly as `CONFIRMED`. Since October 26, 2026, `PaymentDeadlineJob` cancels with the reason `PAYMENT_TIMEOUT` a booking with digital payment that reached its deadline without a receipt, only if payment-service confirms it does not have the payment (section 2.11); rescheduling changes neither the payment mode nor the deadline.

### 4.2 Payment life cycle (RF-013b, RF-014, ADR-008)

```mermaid
stateDiagram-v2
    [*] --> PENDING : create (amount = booking total minus coupons)
    PENDING --> PENDING : attach receipt
    PENDING --> IN_REVIEW : send for review (requires at least one receipt)
    IN_REVIEW --> APPROVED : approve (admin)
    IN_REVIEW --> REJECTED : reject (admin, reason ≤ 200 characters)
    APPROVED --> REFUNDED : refund (admin)
    REJECTED --> [*]
    REFUNDED --> [*]
    APPROVED --> [*]
```

*Source:* `payment-service`, `Payment` aggregate. A booking allows only one approved payment and only one open payment at a time; after `REJECTED` the customer can report a new payment. A customer payment goes from `PENDING` to `IN_REVIEW` in the same request; the cash payment the administrator registers is `APPROVED` at once.

### 4.3 Service execution life cycle (RF-012b)

```mermaid
stateDiagram-v2
    [*] --> PENDING : assign operator (automatic or admin, RF-008, RF-009)
    PENDING --> PENDING : reassign (admin, before the start)
    PENDING --> IN_PROGRESS : start service (from 15 min before, RF-012b)
    IN_PROGRESS --> COMPLETED : finish service (RF-012b) or Attended (admin)
    PENDING --> COMPLETED : Attended (admin, no times)
    COMPLETED --> COMPLETED : rate 1 to 5 (customer, once)
    COMPLETED --> [*]
```

*Source:* `operations-service` (`ServiceExecution`, `StartPolicy`, `FollowUpPolicy`). `COMPLETED` enables the customer's rating (RF-012). When the administrator closes a service from "To close" as "Attended", the lines end `COMPLETED` without made-up times: with no start time they do not count for punctuality and with no end time they add no worked hours, but they do count as a service done and as revenue. The operator can still start the same service late, because starting has no upper limit, and then the real times count. If a service with an operator is not started or not finished on time, operations notifies the operator and every active administrator once (inbox and push, migration 040). `PAUSED` and `WITH_ISSUE` exist in the `execution.execution_status` catalog, but no use case uses them today.

### 4.4 Promotion life cycle (RF-024)

```mermaid
stateDiagram-v2
    [*] --> ACTIVE : create (code, validity, discount, required points)
    ACTIVE --> PAUSED : pause (admin)
    PAUSED --> ACTIVE : resume (admin)
    ACTIVE --> [*] : delete (soft delete)
    PAUSED --> [*] : delete (soft delete)
```

*Source:* `payment-service`, `Promotion.SetActive`, `Promotion.StatusFor`, `Promotion.IsAvailableOn`. The only thing stored is whether it is active or paused; the administrator's screen shows "scheduled" until the start date arrives. Only an active promotion within its validity period can be redeemed (RF-024, RN-PROMO-01): after the end date it is no longer offered or redeemed, although no "expired" status is stored.

### 4.5 Inspection report and its phases (RF-036)

```mermaid
stateDiagram-v2
    [*] --> DRAFT : create (booking of 180 min or more, Confirmed or In progress)
    DRAFT --> DRAFT : mark phases and record findings
    DRAFT --> PUBLISHED : publish (the link starts showing it)
    PUBLISHED --> PUBLISHED : edit or share (notifies the customer)
    PUBLISHED --> [*] : the booking is completed (phases and findings read only)

    state "Each phase" as fase {
        [*] --> PENDING
        PENDING --> IN_PROGRESS : mark in progress or record a finding
        IN_PROGRESS --> DONE : finish
        DONE --> IN_PROGRESS : reopen
        IN_PROGRESS --> PENDING : back to pending
    }
```

*Source:* `operations-service`, `InspectionReport` (`publish`, `requirePublished`, `setPhaseStatus`, `addFinding`). The phases are reception, exterior, interior, engine and delivery; only a published report can be shared, and it is edited while the booking is still Confirmed or In progress.

### 4.6 User account (RF-002, RF-004b, RF-027, RF-028)

```mermaid
stateDiagram-v2
    [*] --> ACTIVE : sign-up (CLIENT) or created by the administrator
    state ACTIVE {
        [*] --> UNLOCKED
        UNLOCKED --> LOCKED : fifth failed attempt within 15 min
        LOCKED --> UNLOCKED : 15 min pass or the password is recovered
    }
    ACTIVE --> INACTIVE : the administrator deactivates it or the user closes it
    INACTIVE --> ACTIVE : the administrator activates it
```

*Source:* `security-service` (`UserAccount`, `LoginLockout`, `UserAdministrationService.changeStatus`, `AccountDeactivationService`). A successful sign-in before the fifth attempt clears the failure count. The user closes their account with the password. A locked account answers 423 even with the right password; an inactive one answers 403 after the password is checked. No administrator account can be deactivated (403 `ADMIN_CANNOT_BE_DISABLED`), and an administrator cannot deactivate or close their own account (403 `CANNOT_DISABLE_OWN_ACCOUNT`). Each account has one role, set when it is created: there is no role change. When it becomes inactive, all its sessions are closed and their tokens stop working within 30 seconds (section 2.8).

---

## 5. Screen Inventory (mockup)

Screen by screen and by role, including the create and edit modals of each module. All screenshots are in the `capturas/` folder of this document. The Management → Users screenshot was taken again on October 20, 2026: the table only shows the user type and has the Enabled and Disabled filters. Since October 20, clicking a notification or a booking of the history opens its detail, with a button to go to the related section (for example, **Go to payment**).

### 5.1 Common screens (no session)

| Screen | Evidence |
|---|---|
| Sign-in | ![Sign-in](capturas/01_login.png) |
| Registration | ![Registration](capturas/02_registro.png) |
| Password recovery | ![Password recovery](capturas/03_recuperar_contrasena.png) |

### 5.2 Administrator role

| Screen | Evidence |
|---|---|
| Dashboard | ![Admin dashboard](capturas/04_admin_dashboard.png) |
| Management → Users | ![Users](capturas/05_admin_usuarios.png) |
| Modal: Create user | ![Create user modal](capturas/29_modal_crear_usuario.png) |
| Management → Roles | ![Roles](capturas/06_admin_roles.png) |
| Modal: Edit role permissions (removed on October 24, 2026: the matrix is read-only) | ![Edit permissions modal](capturas/30_modal_editar_permisos.png) |
| Management → Services (catalog) | ![Catalog](capturas/07_admin_catalogo.png) |
| Modal: Add service | ![Add service modal](capturas/31_modal_crear_servicio.png) |
| Management → Promotions | ![Promotions](capturas/08_admin_promociones.png) |
| Modal: Create promotion | ![Create promotion modal](capturas/32_modal_crear_promocion.png) |
| Opening hours and bays | ![Opening hours and bays](capturas/09_admin_horarios_bahias.png) |
| Modal: Add opening-hours exception | ![Add exception modal](capturas/33_modal_agregar_excepcion.png) |
| Operators | ![Operators](capturas/10_admin_operarios.png) |
| Modal: New operator | ![New operator modal](capturas/34_modal_nuevo_operario.png) |
| Modal: Assign shift | ![Assign shift modal](capturas/42_modal_asignar_turno.png) |
| Bookings | ![Bookings](capturas/11_admin_reservas.png) |
| Modal: Assign an operator to a booking | ![Assign operator modal](capturas/35_modal_asignar_operario.png) |
| Payments | ![Payments](capturas/12_admin_pagos.png) |
| Modal: Record manual payment | ![Record payment modal](capturas/36_modal_registrar_pago.png) |
| Reports | ![Reports](capturas/13_admin_reportes.png) |
| Settings → Payment accounts | ![Payment accounts](capturas/14_admin_cuentas_pago.png) |
| Modal: Add payment account | ![Payment account modal](capturas/37_modal_cuenta_pago.png) |
| Notifications | ![Admin notifications](capturas/15_admin_notificaciones.png) |
| Profile | ![Admin profile](capturas/16_admin_perfil.png) |

### 5.3 Customer role

| Screen | Evidence |
|---|---|
| Dashboard | ![Customer dashboard](capturas/17_client_dashboard.png) |
| Modal: View details of the next service | ![Booking details modal](capturas/39_modal_detalle_reserva_cliente.png) |
| My vehicles | ![Vehicles](capturas/18_client_vehiculos.png) |
| Modal: Register vehicle | ![Register vehicle modal](capturas/38_modal_registrar_vehiculo.png) |
| Book a wash | ![Book a wash](capturas/19_client_reservar.png) |
| Booking history | ![History](capturas/20_client_historial.png) |
| Pay for a service | ![Payment](capturas/21_client_pago.png) |
| Modal: Confirm booking cancellation | ![Cancel booking modal](capturas/41_modal_cancelar_reserva.png) |
| Notifications | ![Customer notifications](capturas/22_client_notificaciones.png) |
| Settings (theme and language) | ![Settings](capturas/23_client_configuracion.png) |

### 5.4 Operator role

| Screen | Evidence |
|---|---|
| Main dashboard | ![Operator dashboard](capturas/24_operario_home.png) |
| Assigned services | ![Assigned services](capturas/25_operario_servicios_asignados.png) |
| My schedule (availability) | ![My schedule](capturas/26_operario_horario.png) |
| Service history | ![Operator history](capturas/27_operario_historial.png) |
| Ratings received | ![Ratings](capturas/28_operario_calificaciones.png) |

---

## Correlation with the Software Requirements Specification

| Dynamic view in this document | Requirements it illustrates |
|---|---|
| §2.1 Registration and welcome | RF-001, RF-018 |
| §2.2 Booking a wash | RF-005, RF-006, RF-010 |
| §2.3 Reminders and alert | RF-018 |
| §2.4 Assignment and execution | RF-008, RF-009, RF-010, RF-011, RF-012, RF-012b |
| §2.5 Payment by receipt | RF-013, RF-013b, RF-014, RF-016 |
| §2.6 Coupon redemption and points | RF-024, RF-025, RF-026 |
| §2.7 Roles and permissions | RF-029 |
| §2.8 Sign-in with lockout | RF-002, RF-003, RF-004b |
| §2.9 Inspection report | RF-036, RF-018 |
| §2.11 Expiry of the digital payment | RF-005, RF-033 |
| §3.1 to §3.6 Activities | The whole authentication, booking, operations, payment and promotion module |
| §4.1 to §4.5 States | RF-006, RF-007, RF-012b, RF-013b, RF-014, RF-024, RF-036 |
