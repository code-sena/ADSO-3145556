# User Stories — Backlog

> Complete backlog for Cuida Tu Agua organized by Entrega and Epic.
> Each story follows the format: As [role], I want [goal], so that [benefit].

---

## Backlog overview

| Entrega | Epics | User stories | Story points | Status (v2.1) |
|---------|-------|-------------|-------------|--------|
| Entrega 1 (MVP) | E1--E4 | HU-001 to HU-018 (18) | 73 SP | Done (HU-004 → Version 2) |
| Entrega 2 | E5--E8 + HU-038 | HU-019 to HU-038 (20) | 88 SP | Done: E5 and HU-025 to HU-027, HU-032 to HU-034 · Version 2: E6, E8, HU-028 to HU-031, HU-038 |
| Entrega 3 | E9--E12 | HU-039 to HU-056 (18) | 70 SP | Done: E12 · Version 2: E9, E10, E11 |
| Entrega 4 | E13--E15 | HU-057 to HU-069 (13) | 48 SP | Done: E14, HU-066, HU-069 · Version 2: E13, HU-067, HU-068 |
| **Total** | **15** | **69** | **279 SP** | |

> *Note: HU-067 and HU-068 are Won't (for now). Their 13 SP are counted but not planned. Active effort is 266 SP.*

## Scope of the current release (v2.1 — 2026-10-15)

The current release focuses on the core of the product: **measuring water consumption per place**. **40 stories (161 SP) are implemented.** The other **29 stories (118 SP) are kept in the backlog as Version 2 improvements**, each with the reason written under its title:

| Version 2 improvement | Stories |
|-----------------------|---------|
| Leak alert and automatic shutoff | HU-029, HU-035, HU-036, HU-037 |
| Consumption goals | HU-023, HU-024, HU-030, HU-031 |
| Shared access: guest and collaborator | HU-039 to HU-047 |
| Water bills | HU-048 to HU-052 |
| Reports and export | HU-038, HU-057, HU-058 |
| Extra channels and sign-in | HU-004 (Google), HU-028 (SMS / WhatsApp) |
| Tariff catalogs of other countries | HU-067, HU-068 |

> **Prioritization decision (Sprint Review, 2026-10-15):** four Must stories (HU-004, HU-029, HU-041, HU-045) move to Version 2. HU-041 and HU-045 depend on Should features (guest, collaborator) that move to Version 2 as a whole; HU-004 has an implemented alternative (email and password); HU-029 is the first improvement of the next version. Every Must story of the current scope is implemented.

---

## Epics

| ID | Epic | Entrega | Description |
|----|------|---------|-------------|
| E1 | Cuentas y acceso | 1 | Register, login, password recovery, profile, account deletion |
| E2 | Gestion de lugares | 1 | Create, list, switch, edit, and delete places |
| E3 | Vinculacion del dispositivo IoT | 1 | Link/unlink ESP32 devices to places via MQTT |
| E4 | Monitoreo de consumo en tiempo real | 1 | Real-time and historical consumption visualization |
| E5 | Control manual de la valvula | 2 | Manual remote open/close of the electrovalve |
| E6 | Metas de consumo | 2 | Set monthly goals and view progress |
| E7 | Alertas y notificaciones | 2 | Notification center, push/email/WhatsApp, leak/goal/sensor/valve alerts, preferences |
| E8 | Cierre automatico de la valvula | 2 | Rule-based automatic valve closure (LeakSuspected trigger only) |
| E9 | Invitado anonimo | 3 | Shareable link with read-only consumption access |
| E10 | Colaborador | 3 | Invited registered users with Owner-defined permissions per place |
| E11 | Recibos del acueducto | 3 | Register bills, compare against sensor data, payment reminders |
| E12 | Tarifas y unidades de medida | 3 | Configure tariffs, units, and cost estimation |
| E13 | Reportes descargables | 4 | Period reports, PDF export |
| E14 | Admin Panel (incluye consejos de ahorro) | 4 | User/device management, platform metrics, savings recommendations |
| E15 | Tarifas precargadas | 4 | Pre-loaded tariff catalog for Colombia (Ecuador and USA in Version 2) |

---


# Entrega 1 — MVP

> **Objetivo del MVP:** que un usuario pueda crear su cuenta, registrar un lugar, vincular su dispositivo y ver su consumo de agua en tiempo real. Sin esto, el producto no existe.
> **Total: 18 HUs, 73 SP**

---

## E1 — Cuentas y acceso (28 SP)

### HU-001 — Registro con correo y contrasena

> **As** an unregistered user
> **I want** to create an account with my first name, last name, email, phone number (optional), and password
> **so that** I can access the platform and start monitoring my water consumption

```gherkin
Scenario: successful registration
  Given a user provides first name, last name, email, phone number, and a valid password
  When they submit the registration form
  Then the system creates the account
  And sends a verification email

Scenario: password validation
  Given the password has fewer than 8 characters or lacks an uppercase letter, number, or special character
  When they submit the registration form
  Then the system rejects with a password policy error

Scenario: duplicate email
  Given the email is already registered
  When they submit the registration form
  Then the system rejects the request with "email already in use"

Scenario: password stored encrypted
  Given a successful registration
  Then the password is stored encrypted, never in plain text
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | auth-service |
| Dependencies | None |

---

### HU-002 — Verificacion de cuenta por correo

> **As** a newly registered user
> **I want** to confirm my account from a link sent to my email
> **so that** I can prove the email is mine and activate my access

```gherkin
Scenario: verify account with the e-mailed code
  Given the user received a 6-digit verification code after registration
  When they enter the code within 24 hours (5 attempts)
  Then the account is marked as verified
  And the user is redirected to login with a confirmation message

Scenario: expired verification code
  Given the verification code was issued more than 24 hours ago
  When the user enters it
  Then the system rejects it and allows the user to request a new code (at most one every 60 seconds)

Scenario: unverified account login attempt
  Given the account has not been verified
  When the user attempts to log in
  Then the system rejects with "Cuenta no verificada"
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | auth-service |
| Dependencies | HU-001 |

---

### HU-003 — Inicio de sesion

> **As** a registered user
> **I want** to log in with my email and password
> **so that** I can access my protected data and features

```gherkin
Scenario: valid credentials
  Given a verified user enters correct email and password
  When they submit the login form
  Then the system issues a JWT and creates a session

Scenario: invalid credentials
  Given the user enters a wrong password
  When they submit the login form
  Then the system rejects the request and increments login attempts

Scenario: account locked after failed attempts
  Given the user has failed 5 login attempts
  When they try again
  Then the system locks the account for 15 minutes

Scenario: session management
  Given a successful login
  Then the session token expires after 1 hour with 7-day renewal
  And each failed attempt is logged in the activity log
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | auth-service |
| Dependencies | HU-001 |

---

### HU-004 — Registro e inicio de sesion con Google

> **Version 2 (improvement).** Email and password sign-in (HU-001 to HU-003) already gives access; Google sign-in is a convenience.

> **As** a user
> **I want** to log in using my Google account
> **so that** I don't have to remember another password

```gherkin
Scenario: Google sign-in (new user)
  Given the user authenticates with Google for the first time
  When the OAuth callback is received
  Then the system creates an account and issues a JWT

Scenario: Google sign-in (existing user)
  Given the user already has an account linked to their Google email
  When the OAuth callback is received
  Then the system links both authentication methods to the same account
  And issues a JWT without creating a new account

Scenario: no additional verification needed
  Given the account was created via Google
  Then no additional email verification is required

Scenario: cross-platform
  Given the Google sign-in button
  Then it works on both web and mobile
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Must Have |
| Scope | Version 2 (improvement) |
| Service(s) | auth-service |
| Dependencies | HU-001 |

---

### HU-005 — Recuperacion de contrasena

> **As** a registered user who forgot their password
> **I want** to reset my password via email or phone number
> **so that** I can regain access to my account

```gherkin
Scenario: password reset request
  Given the user provides a registered email or phone number
  When they request a password reset
  Then the system e-mails a single-use 6-digit reset code that expires in 15 minutes

Scenario: password reset with valid token
  Given the user enters a valid, non-expired reset code
  When they submit a new password that meets the policy (HU-001)
  Then the system updates the password and invalidates all existing sessions

Scenario: token expiration
  Given the reset code was issued more than 15 minutes ago
  When the user attempts to use it
  Then the system rejects the request and requires a new password reset
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | auth-service |
| Dependencies | HU-001 |

---

### HU-006 — Cierre de sesion

> **As** a user
> **I want** to log out
> **so that** no one else can use my account from this device

```gherkin
Scenario: logout
  Given the user is logged in
  When they click the logout button (available on web and mobile)
  Then the token is invalidated immediately
  And the user is redirected to the login screen
```

| Field | Value |
|-------|-------|
| Story Points | 1 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | auth-service |
| Dependencies | HU-003 |

---

### HU-007 — Gestion de perfil

> **As** a registered user
> **I want** to view and update my profile information
> **so that** my account data stays current

```gherkin
Scenario: edit profile
  Given the user is logged in
  When they update their name, phone, or photo
  Then the system saves the changes and confirms the update

Scenario: change password
  Given the user wants to change their password
  When they provide the current password and a new one
  Then the system updates the password

Scenario: email not editable
  Given the user views their profile
  Then the email field is not editable in this version
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | auth-service |
| Dependencies | HU-003 |

---

### HU-008 — Eliminar cuenta

> **As** a registered user
> **I want** to delete my account
> **so that** my data is removed from the platform when I no longer use it

```gherkin
Scenario: account deletion
  Given the user confirms the deletion action explicitly
  When the request is processed
  Then the system soft-deletes the account
  And unlinks all associated devices
  And revokes all active sessions
  # Version 2: when collaborators exist, their access to other places is removed too
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | auth-service, device-service |
| Dependencies | HU-003 |

---

## E2 — Gestion de lugares (8 SP)

### HU-009 — Crear un lugar

> **As** an Owner
> **I want** to register a place (home, business, warehouse) with its location details
> **so that** I can track water consumption for each one separately

```gherkin
Scenario: create place
  Given the user provides a reference name, type (residential/commercial), address, and city
  When they submit the place registration
  Then the system creates the place linked to the user as owner

Scenario: multiple places
  Given the user already has one or more places
  When they create another place
  Then the system creates it independently with no defined limit in this version

Scenario: first place default
  Given the user creates their first place
  Then it is selected by default
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | place-service |
| Dependencies | HU-003 |

---

### HU-010 — Listar y cambiar entre lugares

> **As** an Owner with multiple places
> **I want** to see my places and switch between them
> **so that** I can review consumption for each one

```gherkin
Scenario: place switcher
  Given the user has 2+ places
  When they open the place selector in the main panel
  Then the system shows all their places and switches context on selection
  And all displayed data (charts, consumption, alerts) corresponds to the selected place

Scenario: selection persistence
  Given the user selects a place
  When they return in a later session
  Then the selection is remembered
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | place-service |
| Dependencies | HU-009 |

---

### HU-011 — Editar y eliminar un lugar

> **As** an Owner
> **I want** to edit or delete a place
> **so that** I can correct errors or stop monitoring a location

```gherkin
Scenario: edit place
  Given the user is the owner of the place
  When they update the name, address, or other details
  Then the system saves the changes

Scenario: delete place
  Given the user confirms the deletion explicitly
  When the request is processed
  Then the system warns that history will be lost
  And the deletion is logged in the activity log

Scenario: cannot delete with active device
  Given the place has a linked active device
  When the user tries to delete it
  Then the system rejects and requires unlinking the device first
```

| Field | Value |
|-------|-------|
| Story Points | 2 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | place-service, device-service |
| Dependencies | HU-009 |

---

## E3 — Vinculacion del dispositivo IoT (14 SP)

### HU-012 — Vincular dispositivo a un lugar

> **As** an Owner
> **I want** to link my ESP32 device to a place using its serial number and pairing code (QR reading in Version 2)
> **so that** the platform starts receiving water consumption readings from that location

```gherkin
Scenario: successful linking
  Given the user enters a valid, unlinked serial number and the pairing code printed on its label
  And the device is powered on and online
  When they confirm the linking
  Then the device authenticates and registers to the place
  And the device begins sending readings via MQTT, authenticated with its factory token
  And the process completes within 60 seconds under normal network conditions

Scenario: device already linked
  Given the serial number is already linked to another place
  When the user attempts to link it
  Then the system rejects with "device already linked to another place"

Scenario: reading validation
  Given the device is linked and sending readings
  Then each reading is associated with the device, place, and timestamp
  And duplicate readings (same device and timestamp) are rejected
  And readings with negative or physically implausible values are rejected
  And the device retries if the backend is unavailable
```

| Field | Value |
|-------|-------|
| Story Points | 8 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | device-service |
| Dependencies | HU-009 |

---

### HU-013 — Ver estado del dispositivo

> **As** an Owner
> **I want** to see the current status of my device
> **so that** I know if my data is reliable

```gherkin
Scenario: device online
  Given the device has sent a reading within the configurable threshold (default 10 minutes)
  When the user views the device status
  Then the system shows "Conectado" with the last reading timestamp

Scenario: device offline
  Given no reading has been received within the threshold
  When the user views the device status
  Then the system shows "Desconectado" with the last known reading timestamp

Scenario: never reported
  Given the device has never sent a reading
  When the user views the device status
  Then the system shows "Nunca ha reportado"
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | device-service |
| Dependencies | HU-012 |

---

### HU-014 — Desvincular dispositivo

> **As** an Owner
> **I want** to unlink a device from a place
> **so that** I can move it to another location or replace it

```gherkin
Scenario: unlink device
  Given the device is linked to the user's place
  When the user confirms the unlink
  Then the device stops sending data to that place immediately
  And the consumption history for the place is preserved
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | device-service |
| Dependencies | HU-012 |

---

## E4 — Monitoreo de consumo en tiempo real (23 SP)

### HU-015 — Ver consumo en tiempo real

> **As** a user
> **I want** to see how much water I am consuming right now and how much I have accumulated
> **so that** I can immediately notice abnormal usage

```gherkin
Scenario: consumption today with auto-refresh
  Given the device is linked and online
  When the user opens the dashboard
  Then the system shows accumulated consumption for the selected period (default: today)
  And the data shown is no more than 1 minute old
  And the panel auto-refreshes at least once per minute
  And a manual refresh button is visible
  And the last update timestamp is displayed

Scenario: no data available
  Given there is no data for the selected period
  When the user views the dashboard
  Then the system shows a clear message instead of a misleading zero value
```

| Field | Value |
|-------|-------|
| Story Points | 8 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | consumption-service, device-service |
| Dependencies | HU-012 |

---

### HU-016 — Selector de periodo (dia / semana / mes)

> **As** a user
> **I want** to switch between viewing my consumption by day, week, or month
> **so that** I can analyze my usage at different time scales

```gherkin
Scenario: period selector
  Given the user opens the dashboard
  When they select Day, Week, or Month
  Then the chart and totals update to reflect the selected period
  And "Day" is preselected by default
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | consumption-service |
| Dependencies | HU-015 |

---

### HU-017 — Grafica de consumo

> **As** a user
> **I want** to see my consumption represented in a chart
> **so that** I can understand at a glance how my usage is distributed

```gherkin
Scenario: chart display
  Given the user has consumption data for the selected period
  When the chart renders
  Then it adjusts to the selected period (by hours for day, by days for week and month)
  And hovering or tapping a point shows the exact value for that interval
  And the chart renders correctly on both mobile and desktop screens
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | consumption-service |
| Dependencies | HU-015 |

---

### HU-018 — Ver caudal en tiempo real

> **As** a user
> **I want** to see the current flow rate in liters per minute
> **so that** I can tell if water is running right now

```gherkin
Scenario: active flow
  Given the device is reporting a non-zero flow rate
  When the user views the flow rate indicator
  Then the system shows the current liters/minute from the latest reading

Scenario: no flow
  Given the flow rate is zero
  When the user views the indicator
  Then the system clearly indicates there is no flow

Scenario: update frequency
  Given the device sends a new reading
  Then the flow rate updates with each reading
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | consumption-service, device-service |
| Dependencies | HU-012 |

---

# Entrega 2

> **Total: 20 HUs, 88 SP**

---

## E5 — Control manual de la valvula (18 SP)

### HU-019 — Ver estado de la valvula

> **As** a user
> **I want** to see whether the water valve is currently open or closed
> **so that** I know the state of my water supply

```gherkin
Scenario: valve status
  Given the user has a device with a valve
  When they view the valve widget in the main panel
  Then the system shows the status (Abierta / Cerrada / Sin comunicacion)
  And the status reflects the last confirmation received from the device, not the last command sent
```

| Field | Value |
|-------|-------|
| Story Points | 2 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | valve-service |
| Dependencies | HU-012 |

---

### HU-020 — Cerrar la valvula manualmente

> **As** an Owner
> **I want** to close the water valve from my phone
> **so that** I can shut off the water supply in an emergency or when leaving home

```gherkin
Scenario: request confirmation code
  Given the device is online and the valve is open
  When the Owner initiates a close command
  Then the system sends a confirmation code to the user
  And displays a prompt for the user to enter the code

Scenario: close valve with confirmation
  Given the user has received and enters a valid confirmation code
  When they confirm the close action
  Then the system sends a command to the device
  And the valve closes only when the device reports the state change

Scenario: show water shutoff warning
  Given the user initiates a valve closure
  When the confirmation prompt appears
  Then the system displays an explicit warning: "Este lugar quedara sin agua"

Scenario: device offline or timeout
  Given the device is offline or does not respond within 30 seconds
  When the user sends a close command
  Then the system shows the failure and the status does not change

Scenario: action logging
  Given any valve close action
  Then the action is logged with user, date, and time
```

| Field | Value |
|-------|-------|
| Story Points | 8 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | valve-service, device-service, notification-service |
| Dependencies | HU-012 |

---

### HU-021 — Abrir la valvula

> **As** an Owner
> **I want** to open the water valve from my phone
> **so that** I can restore water supply after a shutoff

```gherkin
Scenario: open valve
  Given the device is online and the valve is closed
  When the Owner sends an open command
  Then the valve opens and the system confirms based on the device report

Scenario: reopen after auto-shutoff
  Given the valve was closed by an automatic rule
  When the Owner sends a manual open command
  Then the valve reopens and the action is logged

Scenario: action logging
  Given any valve open action
  Then the action is logged with user, date, and time
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | valve-service, device-service |
| Dependencies | HU-020 |

---

### HU-022 — Historial de acciones sobre la valvula

> **As** an Owner
> **I want** to see a log of all valve open/close actions
> **so that** I can audit who controlled the valve and when

```gherkin
Scenario: action history
  Given there have been valve actions
  When the user opens the valve history
  Then the system shows a list with: user, action, date/time, and origin (manual or automatic)
  And the history is filterable by date range
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | valve-service |
| Dependencies | HU-020 |

---

## E6 — Metas de consumo (6 SP)

### HU-023 — Definir meta mensual

> **Version 2 (improvement).** Goals are a layer on top of measurement; this release completes measurement per place first.

> **As** a user
> **I want** to set a monthly water consumption goal
> **so that** I have a savings target

```gherkin
Scenario: set goal
  Given the user selects a place
  When they set a monthly goal in the configured unit of measurement
  Then the system saves the goal for the current billing period

Scenario: edit goal
  Given the user has an active goal for the current month
  When they modify it
  Then the system updates the goal
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service |
| Dependencies | HU-015 |

---

### HU-024 — Ver avance frente a la meta

> **Version 2 (improvement).** Depends on HU-023.

> **As** a user
> **I want** to see how much of my monthly goal I have consumed
> **so that** I know if I need to reduce my usage

```gherkin
Scenario: goal progress
  Given the user has an active goal
  When they view the goal widget
  Then the system shows the percentage consumed vs. goal and remaining days in the month
  And a visual indicator changes color at 80% and 100%

Scenario: no goal defined
  Given the user has no active goal
  When they view the goal area
  Then the system invites the user to create one
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service |
| Dependencies | HU-023 |

---

## E7 — Alertas y notificaciones (48 SP)

### Matriz de canales por nivel de urgencia

| Nivel | Alertas | Canales |
| --- | --- | --- |
| **Critica** | Fuga sospechosa - Valvula cerrada | App/Web + Push + Correo + WhatsApp/SMS |
| **Importante** | Meta superada - Sensor desconectado - Recibo desalineado - Recordatorio de pago | App/Web + Push |
| **Informativa** | Cerca de la meta (80%) | Solo App/Web |

---

### HU-025 — Centro de notificaciones

> **As** a user
> **I want** to see all my alerts within the app and web
> **so that** I can review what has happened even if I missed the notification

```gherkin
Scenario: notification center
  Given the user has received alerts
  When they open the notification center
  Then the system shows an icon with a count of unread alerts
  And alerts are listed from most recent to oldest with date, place, and type
  And the user can mark one or all of them as read (deleting alerts: Version 2)
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | notification-service |
| Dependencies | HU-003 |

---

### HU-026 — Notificacion push al celular

> **As** a user
> **I want** to receive a push notification on my phone
> **so that** I am alerted even when the app is not open

```gherkin
Scenario: push notification setup
  Given the user installs the app
  When prompted
  Then the system requests notification permissions

Scenario: push notification received
  Given a relevant alert is triggered
  When a push notification is sent
  Then tapping the notification opens the notification center

Scenario: cross-platform
  Given the push notification system
  Then it works on both Android and iOS (development or published build; Expo Go does not receive remote push on Android)
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | notification-service |
| Dependencies | HU-025 |

---

### HU-027 — Notificacion por correo

> **As** a user
> **I want** to receive critical alerts via email
> **so that** I have a backup if I missed the push notification

```gherkin
Scenario: email notification
  Given a critical alert is triggered
  When the email is sent
  Then the email indicates the place, alert type, date/time, and what to do
  And includes a direct link to the app or web
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | notification-service |
| Dependencies | HU-025 |

---

### HU-028 — Notificacion por WhatsApp / SMS

> **Version 2 (improvement).** Needs an SMS/WhatsApp provider and phone verification. Critical alerts already reach the app, push and e-mail.

> **As** a user
> **I want** to receive a message on my phone during an emergency
> **so that** I am alerted even without internet

```gherkin
Scenario: WhatsApp/SMS notification
  Given a Critical-level alert is triggered
  When the system sends the message
  Then it is only used for Critical alerts
  And requires the user to have a verified phone number
  And the message is short, indicating the place and the type of emergency
```

| Field | Value |
|-------|-------|
| Story Points | 8 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | notification-service |
| Dependencies | HU-025 |

---

### HU-029 — Alerta de fuga sospechosa

> **Version 2 (improvement).** First improvement of version 2: it uses the readings already stored per place, but needs the continuous-flow detector and a new alert type.

> **As** a user
> **I want** to be alerted when water has been running continuously for too long
> **so that** I can detect a leak before it costs me significantly

```gherkin
Scenario: leak suspected
  Given continuous flow exceeds a threshold for a configurable time (default: sustained flow for more than 30 minutes)
  When the leak detector evaluates the readings
  Then the system creates a LeakSuspected alert and notifies the user
  And gives greater weight to nighttime consumption

Scenario: alert content
  Given a LeakSuspected alert is created
  Then the alert indicates from what time the leak was detected and how much water has been consumed
  And offers direct access to the close-valve action

Scenario: rate limiting
  Given a LeakSuspected alert was already sent
  Then no repeat alert is sent for the same event within 1 hour
```

| Field | Value |
|-------|-------|
| Story Points | 8 |
| Priority | Must Have |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service, notification-service |
| Dependencies | HU-015 |

---

### HU-030 — Alerta de meta superada

> **Version 2 (improvement).** Depends on goals (HU-023).

> **As** a user
> **I want** to be notified when I exceed my monthly goal
> **so that** I can take immediate measures

```gherkin
Scenario: goal exceeded
  Given the user has an active goal
  When consumption reaches 100% of the goal
  Then the system sends a GoalExceeded alert
  And the alert indicates how much was exceeded and how many days remain in the month
  And it is sent only once per month
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service, notification-service |
| Dependencies | HU-023 |

---

### HU-031 — Alerta preventiva de meta

> **Version 2 (improvement).** Depends on goals (HU-023); in version 2 it can become a criterion of HU-030.

> **As** a user
> **I want** to be warned before I exceed my goal
> **so that** I can still correct my usage

```gherkin
Scenario: threshold alert
  Given the user has a goal
  When consumption reaches 80% of the goal
  Then the system sends a GoalThresholdReached alert
  And it is shown only within the app/web (informational level)
  And it is sent only once per month
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Could Have |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service, notification-service |
| Dependencies | HU-023 |

---

### HU-032 — Alerta de sensor desconectado

> **As** a user
> **I want** to know if my device stopped reporting
> **so that** I can check it and not be left without data

```gherkin
Scenario: sensor disconnected
  Given the device exceeds the inactivity threshold (default 10 minutes)
  When the system evaluates device status
  Then it sends a SensorDisconnected alert
  And indicates when the last report was received
  And sends only one alert until the device reconnects
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | device-service, notification-service |
| Dependencies | HU-013 |

---

### HU-033 — Alerta de valvula cerrada

> **As** a user
> **I want** to be notified when the water valve is closed
> **so that** I don't discover it when I turn on the faucet

```gherkin
Scenario: valve closed alert
  Given the valve is closed (manually or automatically)
  When the closure is confirmed
  Then the system sends a ValveClosed alert
  And indicates the origin (user who executed it, or automatic rule that triggered it)
  And the alert is classified as Critical and uses all four channels
```

| Field | Value |
|-------|-------|
| Story Points | 2 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | valve-service, notification-service |
| Dependencies | HU-020 |

---

### HU-034 — Preferencias de notificacion

> **As** a user
> **I want** to choose which channels I receive each type of alert through
> **so that** I don't receive messages I'm not interested in

```gherkin
Scenario: configure preferences
  Given the user opens notification settings
  When they toggle channels per urgency level
  Then the system saves the preferences and applies them to future alerts

Scenario: critical alerts minimum
  Given the user tries to disable all channels for critical alerts
  Then the system prevents it: at least the in-app notification remains active

Scenario: immediate effect
  Given the user changes preferences
  Then the changes apply immediately
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | notification-service |
| Dependencies | HU-025 |

---

## E8 — Cierre automatico de la valvula (16 SP)

### HU-035 — Configurar reglas de cierre automatico

> **Version 2 (improvement).** Depends on the leak alert (HU-029).

> **As** an Owner
> **I want** to define when the valve should close automatically
> **so that** I am protected even when I am not paying attention

```gherkin
Scenario: create rule for leak detection
  Given the user navigates to valve settings
  When they enable automatic shutoff for LeakSuspected
  Then the system saves the shutoff rule for the place

Scenario: rule disabled by default
  Given a new place
  Then the automatic shutoff rule is disabled by default

Scenario: owner-only
  Given a non-Owner user
  When they try to configure the rule
  Then the system denies access
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | valve-service |
| Dependencies | HU-029 |

---

### HU-036 — Cierre automatico por fuga con confirmacion

> **Version 2 (improvement).** Depends on HU-029 and HU-035.

> **As** an Owner
> **I want** the system to ask me to confirm valve closure when a leak is detected
> **so that** I am not left without water due to a false alarm

```gherkin
Scenario: confirmation request sent
  Given a shutoff rule is active for LeakSuspected
  When a LeakSuspected event is published
  Then the system sends a confirmation request via critical notification channels
  And waits for the user to confirm within a configurable timeout window

Scenario: auto-shutoff confirmed
  Given the user receives and confirms the shutoff request
  When the confirmation is received
  Then the system closes the valve, sends an alert, and logs the action

Scenario: auto-shutoff not confirmed (timeout)
  Given a confirmation request was sent
  When the configurable timeout window expires without user confirmation
  Then the system does NOT close the valve
  And keeps the alert active to remind the user
  And logs the timeout event

Scenario: full audit
  Given any shutoff confirmation flow
  Then the entire process is recorded in the log
```

| Field | Value |
|-------|-------|
| Story Points | 8 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | valve-service, notification-service |
| Dependencies | HU-035 |

---

### HU-037 — Reapertura tras cierre automatico

> **Version 2 (improvement).** Manual reopening already exists (HU-021); the warning before reopening is added with HU-036.

> **As** an Owner
> **I want** to reopen the valve after an automatic shutoff
> **so that** I can restore the water supply once I have resolved the issue

```gherkin
Scenario: manual reopen after auto-shutoff
  Given the valve was closed by an automatic rule
  When the Owner sends a manual open command
  Then the valve reopens and the action is logged

Scenario: warning if condition persists
  Given the condition that caused the closure is still active
  When the Owner tries to reopen
  Then the system warns before reopening

Scenario: no auto-reopen
  Given the valve was closed by an automatic rule
  Then the valve remains closed until a manual open command is issued
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | valve-service |
| Dependencies | HU-036 |

---

### HU-038 — Exportar datos a Excel / CSV

> **Version 2 (improvement).** Belongs to downloadable reports (E13); delivered with HU-058 as an extra export format.

> **As** a user
> **I want** to download my consumption data as a spreadsheet
> **so that** I can do my own analysis

```gherkin
Scenario: export CSV
  Given the user selects a date range
  When they click "Export"
  Then the system generates a file with one row per interval including date, time, consumption, and unit
  And numeric and date formats are compatible with Excel
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Could Have |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service |
| Dependencies | HU-015 |

> *Note: this story belongs to E13 (Reportes descargables) but is delivered early in Entrega 2.*

---

# Entrega 3

> **Total: 18 HUs, 70 SP**

---

## E9 — Invitado anonimo (13 SP)

### HU-039 — Generar enlace de acceso para invitados

> **Version 2 (improvement).** Shared access (E9, E10) is a full module; this release focuses on the owner of each place.

> **As** an Owner
> **I want** to generate a shareable link for my place
> **so that** anyone can view my consumption without needing to register

```gherkin
Scenario: generate guest link
  Given the Owner opens the place settings
  When they generate a unique guest link
  Then the system creates a shareable URL

Scenario: deactivate link
  Given the Owner deactivates the guest link
  When any anonymous session tries to use it
  Then the system denies access immediately
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | place-service |
| Dependencies | HU-009 |

---

### HU-040 — Ver consumo como invitado anonimo

> **Version 2 (improvement).** Depends on HU-039.

> **As** a person with a place's guest link
> **I want** to view consumption, charts, and history
> **so that** I can stay informed without creating an account

```gherkin
Scenario: guest view
  Given the guest accesses via the link without registration or login
  When they open the dashboard
  Then they see real-time consumption, charts, and history
  And they cannot see control actions (valve, goals, configuration)
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service, place-service |
| Dependencies | HU-039 |

---

### HU-041 — Restriccion de solo lectura del invitado

> **Version 2 (improvement).** A system rule rather than a user story; in version 2 it is an acceptance criterion of HU-040.

> **As** the system
> **I need** to restrict what the anonymous guest can do
> **so that** only the owner has control over the valve and configuration

```gherkin
Scenario: read-only enforcement
  Given the guest is viewing the dashboard
  Then actions not permitted are not shown in the interface
  And restrictions are validated in the backend, not just the UI
  And the guest cannot access sensitive data (bills, tariffs, alert configuration)
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Must Have |
| Scope | Version 2 (improvement) |
| Service(s) | place-service |
| Dependencies | HU-039 |

---

## E10 — Colaborador (24 SP)

### HU-042 — Invitar colaborador por correo

> **Version 2 (improvement).** Shared access (E9, E10) is a full module; this release focuses on the owner of each place.

> **As** an Owner
> **I want** to invite someone as a collaborator for my place
> **so that** they can help manage water consumption according to the permissions I define

```gherkin
Scenario: send invitation
  Given the Owner provides a valid email
  When they send the invitation with selected permissions
  Then the system sends an email invitation

Scenario: invitation expiration
  Given the invitation was sent
  When 7 days pass without acceptance
  Then the invitation expires

Scenario: invitation status
  Given invitations have been sent
  When the Owner views the invitation list
  Then the system shows the status of each invitation (pending / accepted / expired)
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | place-service, notification-service |
| Dependencies | HU-009 |

---

### HU-043 — Aceptar invitacion como colaborador

> **Version 2 (improvement).** Depends on HU-042.

> **As** a person invited as a collaborator
> **I want** to accept the invitation and access the place
> **so that** I can help manage water consumption

```gherkin
Scenario: accept invitation (existing user)
  Given the invited person has an account
  When they accept the invitation
  Then the place appears in their list with the label "Colaborador"

Scenario: accept invitation (new user)
  Given the invited person does not have an account
  When they click the invitation link
  Then the flow takes them to registration first, then to acceptance

Scenario: reject invitation
  Given the person received an invitation
  When they reject it
  Then the invitation is closed and the Owner is notified
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | place-service, auth-service |
| Dependencies | HU-042 |

---

### HU-044 — Definir permisos del colaborador

> **Version 2 (improvement).** Depends on HU-042.

> **As** an Owner
> **I want** to choose what actions each collaborator can perform
> **so that** I maintain control of my place without granting full access

```gherkin
Scenario: define permissions
  Given the Owner manages a collaborator's permissions
  When they set permissions per collaborator and per place
  Then the system saves the permissions

Scenario: edit permissions after invitation
  Given the collaborator already has access
  When the Owner modifies permissions
  Then the changes apply immediately
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | place-service |
| Dependencies | HU-042 |

---

### HU-045 — Colaborador ejecuta acciones segun permisos

> **Version 2 (improvement).** A system rule rather than a user story; in version 2 it is an acceptance criterion of HU-044.

> **As** the system
> **I need** to validate the collaborator's permissions on each action
> **so that** they can only do what the owner authorized

```gherkin
Scenario: permitted actions only
  Given the collaborator views the dashboard
  Then actions not permitted are not shown in the interface

Scenario: backend validation
  Given the collaborator attempts an action
  Then the restriction is validated in the backend, not just the UI

Scenario: permission revoked with active session
  Given a permission is revoked while the collaborator has an active session
  When they attempt the action next
  Then the action is blocked
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Must Have |
| Scope | Version 2 (improvement) |
| Service(s) | place-service |
| Dependencies | HU-044 |

---

### HU-046 — Revocar acceso de colaborador

> **Version 2 (improvement).** Depends on HU-042.

> **As** an Owner
> **I want** to revoke a collaborator's access
> **so that** I maintain control over who manages my place

```gherkin
Scenario: revoke access
  Given the Owner views the list of collaborators with access to the place
  When they revoke a collaborator's access
  Then the access is cut immediately, even with an active session
  And the action is logged
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | place-service |
| Dependencies | HU-042 |

---

### HU-047 — Colaborador recibe alertas del lugar

> **Version 2 (improvement).** Defined as part of the collaborator permissions (HU-044) in version 2.

> **As** a Collaborator
> **I want** to receive alerts for the place I have access to
> **so that** I am aware of leaks or anomalies even though I am not the owner

```gherkin
Scenario: collaborator alerts
  Given the collaborator has active access
  When an alert is triggered for the place
  Then the collaborator receives alerts according to the permissions the owner assigned
  And the alert types depend on the owner's configuration
  And alerts are delivered through the collaborator's preferred channel
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Could Have |
| Scope | Version 2 (improvement) |
| Service(s) | notification-service |
| Dependencies | HU-044 |

---

## E11 — Recibos del acueducto (19 SP)

### HU-048 — Registrar un recibo manualmente

> **Version 2 (improvement).** Bills are the contrast of the measurement; this release covers the sensor measurement and its estimated cost (HU-056).

> **As** a user
> **I want** to enter my water bill data
> **so that** I can compare what the utility charged vs. what my sensor measured

```gherkin
Scenario: register bill
  Given the user enters billed consumption, total amount, period start/end dates, and payment due date
  When they submit the bill
  Then the system stores the bill record linked to the place

Scenario: date validation
  Given the start date is after the end date
  When they submit the form
  Then the system rejects with a validation error

Scenario: overlapping period
  Given the period overlaps with an already registered bill
  When they submit the form
  Then the system warns the user
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service |
| Dependencies | HU-009 |

---

### HU-049 — Ver historial de recibos

> **Version 2 (improvement).** Depends on HU-048.

> **As** a user
> **I want** to see all the bills I have registered
> **so that** I can review how my billing has changed

```gherkin
Scenario: bill list
  Given the user has registered bills
  When they open the bill history
  Then the system shows bills by period from most recent to oldest
  And each bill can be edited or deleted
  And the evolution of the paid amount is shown over time
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service |
| Dependencies | HU-048 |

---

### HU-050 — Comparar recibo contra sensor

> **Version 2 (improvement).** Depends on HU-048.

> **As** a user
> **I want** to see the difference between what my device measured and what I was billed
> **so that** I know if I am being overcharged

```gherkin
Scenario: bill comparison
  Given the user has a bill and sensor data for the same period
  When they open the comparison view
  Then the system shows side-by-side: sensor consumption vs. billed consumption
  And calculates the absolute and percentage difference

Scenario: insufficient data
  Given the device did not have sufficient data coverage in the period
  When the comparison is shown
  Then the system warns that the comparison may not be reliable
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service |
| Dependencies | HU-048, HU-015 |

---

### HU-051 — Alerta de recibo desalineado

> **Version 2 (improvement).** Depends on HU-050.

> **As** a user
> **I want** to be alerted when there is a significant difference between my bill and sensor
> **so that** I can file a complaint with the utility company

```gherkin
Scenario: discrepancy alert
  Given the difference between billed and measured consumption exceeds a configurable threshold (default 15%)
  When the comparison is calculated
  Then the system creates a discrepancy alert and notifies the user
  And the alert links directly to the comparison view
  And it is classified as an Important alert
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service, notification-service |
| Dependencies | HU-050 |

---

### HU-052 — Recordatorio de pago del recibo

> **Version 2 (improvement).** Depends on HU-048.

> **As** a user
> **I want** to be reminded to pay before the due date
> **so that** I don't incur late fees or service suspension

```gherkin
Scenario: payment reminder
  Given the bill has a due date
  When the configurable advance period is reached (default 3 days before)
  Then the system sends a payment reminder (Important level: app/web + push)

Scenario: mark as paid
  Given the user marks the bill as paid
  Then the reminder stops
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Version 2 (improvement) |
| Service(s) | notification-service |
| Dependencies | HU-048 |

---

## E12 — Tarifas y unidades de medida (14 SP)

### HU-053 — Seleccionar pais y moneda

> **As** a user
> **I want** to indicate what country I am in
> **so that** the app uses my currency and usual units

```gherkin
Scenario: select country
  Given the user configures a place
  When they select a country (Colombia or Ecuador)
  Then the system sets the default currency (COP, USD) and suggested unit (liters)

Scenario: per-place setting
  Given the user has places in different countries
  Then the country is defined per place, not per user
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | consumption-service |
| Dependencies | HU-009 |

---

### HU-054 — Ingresar la tarifa manualmente

> **As** a user
> **I want** to enter my water rate per unit
> **so that** the app can calculate how much money I have spent

```gherkin
Scenario: set tariff
  Given the user enters the rate per unit from their bill
  When they save the tariff
  Then the system stores it linked to the place

Scenario: optional fixed charge
  Given the user wants to include a monthly fixed charge
  When they enter it
  Then it is included in cost calculations

Scenario: tariff history
  Given the user updates the tariff
  When they save the new rate
  Then historical tariffs are preserved so past calculations are not altered
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | consumption-service |
| Dependencies | HU-009 |

---

### HU-055 — Seleccionar unidad de medida

> **As** a user
> **I want** to choose if I see my consumption in liters, cubic meters, or gallons
> **so that** I understand it in the unit familiar to me

```gherkin
Scenario: select unit
  Given the user opens settings
  When they choose liters, cubic meters, or gallons
  Then the display changes across all screens and reports
  And the stored data is not affected (conversion only at display)
  And conversions are exact and consistent
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | consumption-service |
| Dependencies | HU-009 |

---

### HU-056 — Ver el consumo en dinero

> **As** a user
> **I want** to see how much money I have spent, not just how much water
> **so that** I can understand the real impact on my wallet

```gherkin
Scenario: cost estimate
  Given the place has a configured tariff and consumption data
  When the user views the dashboard
  Then the system shows the estimated cost alongside consumption for the selected period
  And clearly indicates it is an estimate based on the user's configured tariff

Scenario: no tariff configured
  Given no tariff is configured for the place
  When the user views the cost area
  Then the system invites them to configure a tariff instead of showing zero
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | consumption-service |
| Dependencies | HU-054 |

---

# Entrega 4

> **Total: 13 HUs, 48 SP**

---

## E13 — Reportes descargables (10 SP)

### HU-057 — Generar reporte por periodo

> **Version 2 (improvement).** Could priority; the consumption panel already shows total, chart and cost of the period.

> **As** a user
> **I want** to generate a consumption report for a date range
> **so that** I can review it or present it to the water utility

```gherkin
Scenario: generate report
  Given the user selects a place and date range
  When the report is generated
  Then it includes: total consumption, daily average, chart, goal for the period, and bill comparison if available
  And warns if there are data gaps in the selected period
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Could Have |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service |
| Dependencies | HU-015 |

---

### HU-058 — Exportar reporte a PDF

> **Version 2 (improvement).** Depends on HU-057.

> **As** a user
> **I want** to download the report as a PDF
> **so that** I can print it or send it by email

```gherkin
Scenario: PDF export
  Given the user is viewing a report
  When they click "Download PDF"
  Then the system generates a formatted PDF
  And the PDF includes the place name, date range, and generation date
  And charts are included as legible images
  And the file downloads with a descriptive name
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Could Have |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service |
| Dependencies | HU-057 |

> *Note: HU-038 (CSV export) belongs to this epic but is delivered in Entrega 2.*

---

## E14 — Admin Panel (incluye consejos de ahorro) (25 SP)

### HU-059 — Admin ve lista de usuarios registrados

> **As** an Admin
> **I want** to see a list of all registered users
> **so that** I can monitor platform usage and manage accounts

```gherkin
Scenario: user list
  Given the admin is authenticated with admin role
  When they open the user management section
  Then the system shows a paginated list with search by name or email
  And shows account status (active, blocked, unverified)
  And does not show sensitive data (passwords, documents)
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | auth-service |
| Dependencies | HU-003 |

---

### HU-060 — Admin bloquea/desbloquea usuario

> **As** an Admin
> **I want** to block or unblock a user account
> **so that** I can protect the platform from abuse

```gherkin
Scenario: block user
  Given the admin selects an active user
  When they block the account
  Then all user sessions are revoked and the account status becomes "blocked"
  And the blocked user cannot log in and sees an explanatory message

Scenario: unblock user
  Given the admin selects a blocked user
  When they unblock the account
  Then the user can log in again

Scenario: audit
  Given any block/unblock action
  Then it is logged with date, time, and admin who executed it
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Must Have |
| Scope | Current release (implemented) |
| Service(s) | auth-service |
| Dependencies | HU-059 |

---

### HU-061 — Admin ve lista de dispositivos

> **As** an Admin
> **I want** to see a list of all devices on the platform
> **so that** I can monitor device health and validate failures

```gherkin
Scenario: device list
  Given the admin opens the device management section
  When the page loads
  Then the system shows all devices with serial, status (connected/disconnected/never reported/decommissioned), linked place, and last report
  And allows searching by serial and filtering by status and link

Scenario: factory registration and maintenance
  Given the admin is in the device management section
  Then the admin can register new factory devices, regenerate their credentials and decommission a device
  And every action is logged
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | device-service |
| Dependencies | HU-012 |

---

### HU-062 — Admin ve metricas de plataforma

> **As** an Admin
> **I want** to see platform-wide metrics
> **so that** I can understand adoption and system health

```gherkin
Scenario: metrics dashboard
  Given the admin opens the metrics section
  When the page loads
  Then the system shows: total users, total devices, total places, active/inactive counts
  And data refreshes on page load
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | auth-service, device-service, place-service |
| Dependencies | HU-059, HU-061 |

---

### HU-063 — Admin crea recomendacion de ahorro

> **As** an Admin
> **I want** to create water-saving recommendations
> **so that** users receive useful tips within the app

```gherkin
Scenario: create recommendation
  Given the admin enters a title, body, and category (residential / commercial)
  When they submit the recommendation
  Then the system saves it and makes it visible to all users upon publishing

Scenario: minimum display
  Given recommendations exist
  Then at least 3 different tips are shown per user session

Scenario: invalid recommendation
  Given the admin leaves the title or body empty
  When they try to submit
  Then the system rejects with validation errors
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Should Have |
| Scope | Current release (implemented) |
| Service(s) | notification-service |
| Dependencies | None |

---

### HU-064 — Admin edita o desactiva recomendacion

> **As** an Admin
> **I want** to edit or deactivate an existing recommendation
> **so that** outdated tips are no longer shown without losing the history

```gherkin
Scenario: edit recommendation
  Given the admin modifies the title, body, or category
  When they save the changes
  Then the system updates the recommendation

Scenario: deactivate recommendation
  Given the admin deactivates a recommendation
  When they confirm the action
  Then the recommendation is hidden from users but not deleted
  And changes apply immediately
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Could Have |
| Scope | Current release (implemented) |
| Service(s) | notification-service |
| Dependencies | HU-063 |

---

### HU-065 — Ver consejos de ahorro de agua

> **As** a user
> **I want** to receive water-saving recommendations
> **so that** I can reduce my consumption and my bill

```gherkin
Scenario: view tips
  Given the user opens the tips section
  Then at least 3 different tips are shown per session
  And tips are generic (good practices for domestic and commercial savings), not personalized by point of use
  And tips are differentiated by place type (residential / commercial)

Scenario: favorites
  Given the user finds a useful tip
  When they mark it as a favorite
  Then it is saved for future reference
```

| Field | Value |
|-------|-------|
| Story Points | 3 |
| Priority | Could Have |
| Scope | Current release (implemented) |
| Service(s) | notification-service |
| Dependencies | HU-063 |

---

## E15 — Tarifas precargadas (26 SP, 13 activos)

### HU-066 — Catalogo de tarifas de Colombia

> **As** a user in Colombia
> **I want** to select my city and stratum
> **so that** the app calculates the cost without me having to type the rate

```gherkin
Scenario: select city and stratum
  Given the user is in Colombia
  When they select their city and stratum (1 to 6)
  Then the system loads the corresponding tariff

Scenario: tiered calculation
  Given the tariff includes tiers
  Then the system calculates using consumption ranges (basic, complementary, suntuario) when applicable

Scenario: tariff metadata
  Given a preloaded tariff is selected
  Then the system shows the date of the last tariff update

Scenario: override
  Given the user wants to use their own tariff
  When they enter a custom rate
  Then it overrides the preloaded one
```

| Field | Value |
|-------|-------|
| Story Points | 8 |
| Priority | Could Have |
| Scope | Current release (implemented) |
| Service(s) | consumption-service |
| Dependencies | HU-054 |

---

### HU-067 — Catalogo de tarifas de Ecuador

> **Version 2 (improvement).** Won't for now: the pre-loaded catalog is Colombia only; Ecuador uses the manual tariff (HU-054).

> **As** a user in Ecuador
> **I want** to select my city and service category
> **so that** the app calculates the cost automatically

```gherkin
Scenario: select city and category
  Given the user is in Ecuador
  When they select their city and category (residential, commercial, industrial)
  Then the system loads the corresponding tariff
  And shows the date of the last tariff update
  And the user can override with their own rate
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Won't (for now) |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service |
| Dependencies | HU-054 |

---

### HU-068 — Catalogo de tarifas de Estados Unidos

> **Version 2 (improvement).** The United States is not in the place scope of this version (Colombia and Ecuador only).

> **Out of market scope:** ADR-004 limits the product to Colombia and Ecuador. This story stays *Won't* until that ADR is revisited.

> **As** a user in the United States
> **I want** to select my water district
> **so that** the app calculates the cost in dollars per gallon

```gherkin
Scenario: select state and provider
  Given the user is in the United States
  When they select their state and water provider/district
  Then the system loads the corresponding tariff in the local unit (gallons or CCF)

Scenario: partial coverage
  Given the district is not in the catalog
  When the user searches for it
  Then the system offers manual tariff entry (HU-054) as fallback
```

| Field | Value |
|-------|-------|
| Story Points | 8 |
| Priority | Won't (for now) |
| Scope | Version 2 (improvement) |
| Service(s) | consumption-service |
| Dependencies | HU-054 |

---

### HU-069 — Calculo automatico con tarifa precargada

> **As** a user
> **I want** the cost to be calculated automatically with the tariff from my area
> **so that** I don't have to check the bill every time prices change

```gherkin
Scenario: automatic calculation
  Given a preloaded tariff is available for the place
  Then it is used by default for cost calculations
  And the source of the tariff (preloaded or user-entered) is clearly indicated

Scenario: tariff update
  Given an official tariff is updated
  When new calculations are made
  Then historical calculations are not modified
```

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | Could Have |
| Scope | Current release (implemented) |
| Service(s) | consumption-service |
| Dependencies | HU-066 |

---

## Rules for writing user stories

### 1. The role must be precise

```
OK  As an Owner
OK  As a Collaborator
OK  As an Invitado (Guest)
OK  As an Admin
NO  As a user (use "usuario" for shared Owner/Collaborator contexts)
NO  As a person
```

### 2. The benefit must be business value

```
OK  so that I can detect leaks before they cost me money
NO  so that I can see the data (restates the feature)
```

### 3. Acceptance criteria must be verifiable

```
OK  Then the system shows total liters consumed today
OK  Then the valve closes and the status updates to "closed"
NO  Then the experience is good
```

### 4. One story = one sprint (or less)

If a story covers too many flows, split it before sprint planning.

---

## Correlations

- Delivery roadmap -> `03-product/roadmap.md`
- Non-functional requirements -> `04-requirements/non-functional.md`
- Traceability matrix -> `04-requirements/traceability-matrix.md`
- Domain events triggered by these stories -> `02-domain/domain-events.md`
- API contracts derived from these stories -> `07-api/contracts/`
- Database tables -> `06-data/models.md`
