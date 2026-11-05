# User Stories — Backlog

> **What to fill in here:** The product's User Story backlog.
>
> Each HU uses the standard format with Acceptance Criteria in Given/When/Then.
>
> Refined (Ready) HUs go to the sprint. Unrefined ones are epics or ideas.

---

## Backlog status

| Cut | Sprint | Total HUs | Refined | In progress | Completed |
|-----|--------|-----------|---------|-------------|-----------|
| Cut 1 | Sprint 1-2 | 7 | 7 | 0 | 0 |
| Cut 2 | Sprint 3-4 | 10 | 10 | 0 | 0 |

---

## Epics

| ID | Epic | Description |
|----|------|-------------|
| EP-001 | User and Access Management | Manage user registration, authentication, authorization, roles and access to EduAirControl. |
| EP-002 | Educational Environment Management | Manage the educational spaces or classrooms where environmental monitoring is performed. |
| EP-003 | Environmental Sensor Management | Register, configure and manage the environmental sensors used by EduAirControl. |
| EP-004 | Environmental Monitoring | Capture, store and consult environmental measurements such as temperature, humidity, CO₂ and noise levels. |
| EP-005 | Environmental Data Analysis | Analyze historical environmental information using daily, weekly, monthly and yearly views. |
| EP-006 | Environmental Dashboard | Present current and historical environmental information through a centralized visual dashboard. |
| EP-007 | System Architecture and Integration | Enable communication between the frontend, API Gateway and EduAirControl microservices. |

---

## User Stories

### HU-IAM-001 — User registration {#HU-IAM-001}

**Epic:** EP-001

> **As** a new user
>
> **I want** to register an account in EduAirControl
>
> **so that** I can access the platform's environmental monitoring and analysis functionalities according to my permissions.

**Acceptance Criteria:**

```gherkin
Scenario 1: Successful user registration

  Given the user is on the registration form
  When the user enters valid required information and submits the form
  Then the system creates the user account successfully
  And the account information is stored in the User Management context

Scenario 2: Registration with an existing email

  Given an account already exists with the entered email
  When the user attempts to register using the same email
  Then the system rejects the registration
  And a message indicating that the email is already registered is shown

Scenario 3: Registration with invalid information

  Given the user has entered incomplete or invalid required information
  When the user submits the registration form
  Then the system rejects the request
  And the corresponding validation messages are displayed
```

| Field               | Value            |
| ------------------- | ---------------- |
| Story Points        | 5                |
| Priority            | Must Have        |
| Target sprint       | Sprint 1         |
| Assigned to         | Development Team |
| Status              | In Progress      |
| Dependencies        | None             |
| Affected service(s) | ms-security      |

---

### HU-IAM-002 — User authentication / Login {#HU-IAM-002}

**Epic:** EP-001

> **As** a registered user
>
> **I want** to authenticate using my credentials
>
> **so that** I can securely access the EduAirControl platform.

**Acceptance Criteria:**

```gherkin
Scenario 1: Successful authentication

  Given the user has a valid registered account
  When the user enters valid credentials and submits the login form
  Then the system authenticates the user
  And a valid JWT access token is generated

Scenario 2: Invalid credentials

  Given the user has entered incorrect credentials
  When the user submits the login form
  Then the system rejects the authentication
  And an authentication error message is shown

Scenario 3: Empty required fields

  Given the login form contains empty required fields
  When the user attempts to authenticate
  Then the system prevents the request
  And validation messages are displayed
```

| Field               | Value            |
| ------------------- | ---------------- |
| Story Points        | 5                |
| Priority            | Must Have        |
| Target sprint       | Sprint 1         |
| Assigned to         | Development Team |
| Status              | In Progress      |
| Dependencies        | HU-IAM-001       |
| Affected service(s) | ms-security      |

---

### HU-IAM-003 — Role-based access control {#HU-IAM-003}
### HU-IAM-003 — Role-based access control {#HU-IAM-003}

**Epic:** EP-001

> **As** a system administrator
>
> **I want** to control access according to user roles
>
> **so that** only authorized users can access protected EduAirControl functionalities.

**Acceptance Criteria:**

```gherkin
Scenario 1: Authorized access

  Given the user has a valid JWT and the required role
  When the user accesses a protected endpoint
  Then the system allows access to the requested resource

Scenario 2: Unauthorized access

  Given the user has a valid JWT but does not have the required role
  When the user attempts to access a restricted endpoint
  Then the system denies access
  And an HTTP 403 Forbidden response is returned

Scenario 3: Unauthenticated access

  Given the user does not have a valid JWT
  When the user attempts to access a protected endpoint
  Then the system denies access
  And an HTTP 401 Unauthorized response is returned
```

| Field               | Value                    |
| ------------------- | ------------------------ |
| Story Points        | 5                        |
| Priority            | Must Have                |
| Target sprint       | Sprint 1                 |
| Assigned to         | Development Team         |
| Status              | Backlog                  |
| Dependencies        | HU-IAM-002               |
| Affected service(s) | ms-security, API Gateway |

---

### HU-USER-001 — User management {#HU-USER-001}

**Epic:** EP-001

> **As** a system administrator
>
> **I want** to consult and manage registered users
>
> **so that** I can maintain accurate user information and control platform accounts.

**Acceptance Criteria:**

```gherkin
Scenario 1: Consult registered users

  Given the administrator is authenticated and authorized
  When the administrator requests the list of users
  Then the system returns the registered users according to the defined permissions

Scenario 2: Consult a specific user

  Given the requested user exists
  When the administrator requests the user's information
  Then the system returns the corresponding user information

Scenario 3: User does not exist

  Given the requested user does not exist
  When the administrator requests the user's information
  Then the system returns an HTTP 404 Not Found response
```

| Field               | Value            |
| ------------------- | ---------------- |
| Story Points        | 5                |
| Priority            | Should Have      |
| Target sprint       | Sprint 2         |
| Assigned to         | Development Team |
| Status              | Backlog          |
| Dependencies        | HU-IAM-003       |
| Affected service(s) | ms-user-management      |

---

### HU-CLASS-001 — Educational classroom management {#HU-CLASS-001}

**Epic:** EP-002

> **As** a system administrator
>
> **I want** to register and manage educational classrooms or environments
>
> **so that** environmental monitoring can be associated with a specific educational space.

**Acceptance Criteria:**

```gherkin
Scenario 1: Create an educational classroom

  Given the administrator is authenticated and authorized
  When the administrator enters valid classroom information and submits the request
  Then the system creates the classroom successfully

Scenario 2: Consult classroom information

  Given the classroom exists
  When the administrator requests its information
  Then the system returns the classroom data

Scenario 3: Invalid classroom information

  Given the administrator enters incomplete or invalid classroom information
  When the administrator submits the request
  Then the system rejects the operation
  And the corresponding validation error is displayed
```

| Field               | Value            |
| ------------------- | ---------------- |
| Story Points        | 5                |
| Priority            | Must Have        |
| Target sprint       | Sprint 2         |
| Assigned to         | Development Team |
| Status              | Backlog          |
| Dependencies        | HU-IAM-003       |
| Affected service(s) | ms-classroom-management         |

---

### HU-SENS-001 — Environmental sensor management {#HU-SENS-001}

**Epic:** EP-003

> **As** a system administrator
>
> **I want** to register and manage environmental sensors
>
> **so that** each sensor can be identified and associated with the educational environment where it operates.

**Acceptance Criteria:**

```gherkin
Scenario 1: Register a sensor

  Given the administrator is authenticated and authorized
  When the administrator enters valid sensor information
  Then the system registers the sensor successfully
  And the sensor receives a unique identifier

Scenario 2: Associate sensor with classroom

  Given the sensor and classroom exist
  When the administrator associates the sensor with the classroom
  Then the system stores the relationship successfully

Scenario 3: Duplicate sensor

  Given a sensor with the same unique identifier already exists
  When the administrator attempts to register the sensor
  Then the system rejects the operation
  And a duplicate sensor error is displayed
```

| Field               | Value            |
| ------------------- | ---------------- |
| Story Points        | 5                |
| Priority            | Must Have        |
| Target sprint       | Sprint 2         |
| Assigned to         | Development Team |
| Status              | Backlog          |
| Dependencies        | HU-CLASS-001     |
| Affected service(s) | ms-sensor-management      |

---

### HU-SENS-002 — Sensor communication {#HU-SENS-002}

**Epic:** EP-003

> **As** the environmental monitoring system
>
> **I want** to receive data from registered environmental sensors
>
> **so that** EduAirControl can process current environmental conditions.

**Acceptance Criteria:**

```gherkin
Scenario 1: Receive valid sensor data

  Given a registered sensor is configured to send environmental measurements
  When the system receives a valid measurement
  Then the system accepts and processes the environmental data

Scenario 2: Invalid sensor data

  Given the received measurement contains invalid or incomplete values
  When the system processes the measurement
  Then the system rejects the invalid data
  And an appropriate validation error is recorded

Scenario 3: Unregistered sensor

  Given the measurement comes from an unregistered sensor
  When the system receives the measurement
  Then the system rejects the measurement
  And the event is recorded for traceability
```

| Field               | Value                     |
| ------------------- | ------------------------- |
| Story Points        | 8                         |
| Priority            | Must Have                 |
| Target sprint       | Sprint 3                  |
| Assigned to         | Development Team          |
| Status              | Backlog                   |
| Dependencies        | HU-SENS-001               |
| Affected service(s) | ms-sensor-management, ms-environment-monitoring |

---

### HU-MON-001 — Environmental data reception {#HU-MON-001}

**Epic:** EP-004

> **As** the environmental monitoring system
>
> **I want** to receive and process measurements from environmental sensors
>
> **so that** the platform can maintain updated information about educational environments.

**Acceptance Criteria:**

```gherkin
Scenario 1: Process environmental measurement

  Given a valid measurement has been received from a registered sensor
  When the monitoring service processes the measurement
  Then the system stores the environmental measurement
  And the measurement is associated with the corresponding sensor and environment

Scenario 2: Measurement outside valid format

  Given the measurement does not contain the required environmental variables
  When the monitoring service processes the measurement
  Then the system rejects the measurement
  And records the corresponding error
```

| Field               | Value            |
| ------------------- | ---------------- |
| Story Points        | 8                |
| Priority            | Must Have        |
| Target sprint       | Sprint 3         |
| Assigned to         | Development Team |
| Status              | Backlog          |
| Dependencies        | HU-SENS-002      |
| Affected service(s) | ms-environment-monitoring     |

---

### HU-MON-002 — Environmental data storage {#HU-MON-002}

**Epic:** EP-004

> **As** the environmental monitoring system
>
> **I want** to store environmental measurements with their date and associated environment
>
> **so that** historical environmental information can be consulted and analyzed later.

**Acceptance Criteria:**

```gherkin
Scenario 1: Store valid environmental data

  Given a valid environmental measurement has been received
  When the monitoring service processes it
  Then the measurement is persisted in the database
  And the record contains its environmental variables, timestamp, sensor and associated environment

Scenario 2: Database persistence failure

  Given the system cannot persist the environmental measurement
  When the monitoring service attempts to save the measurement
  Then the system reports the persistence error
  And the original measurement is not considered successfully stored
```

| Field               | Value            |
| ------------------- | ---------------- |
| Story Points        | 5                |
| Priority            | Must Have        |
| Target sprint       | Sprint 3         |
| Assigned to         | Development Team |
| Status              | Backlog          |
| Dependencies        | HU-MON-001       |
| Affected service(s) | ms-environment-monitoring     |

---

### HU-MON-003 — Current environmental monitoring {#HU-MON-003}

**Epic:** EP-004

> **As** a registered user
>
> **I want** to consult the current environmental conditions of an educational environment
>
> **so that** I can know the current temperature, humidity, CO₂ and noise levels.

**Acceptance Criteria:**

```gherkin
Scenario 1: Consult current environmental conditions

  Given the user is authenticated and the educational environment has current measurements
  When the user requests the current environmental information
  Then the system displays the latest available values for temperature, humidity, CO₂ and noise level

Scenario 2: No current data available

  Given the selected environment has no available environmental measurements
  When the user requests its current information
  Then the system informs the user that current environmental data is unavailable
```

| Field               | Value            |
| ------------------- | ---------------- |
| Story Points        | 5                |
| Priority            | Must Have        |
| Target sprint       | Sprint 3         |
| Assigned to         | Development Team |
| Status              | Backlog          |
| Dependencies        | HU-MON-002       |
| Affected service(s) | ms-environment-monitoring     |

---

### HU-MON-004 — Environmental variables consultation {#HU-MON-004}

**Epic:** EP-004

> **As** a registered user
>
> **I want** to consult temperature, humidity, CO₂ and noise measurements
>
> **so that** I can understand the environmental conditions of an educational space.

**Acceptance Criteria:**

```gherkin
Scenario 1: Display environmental variables

  Given environmental measurements are available
  When the user consults the monitoring information
  Then the system displays temperature, humidity, CO₂ and noise level
  And each value is displayed with its corresponding measurement unit

Scenario 2: Partial environmental data

  Given one or more environmental variables are unavailable
  When the user consults the monitoring information
  Then the system identifies the unavailable values
  And does not display them as valid measurements
```

| Field               | Value            |
| ------------------- | ---------------- |
| Story Points        | 3                |
| Priority            | Must Have        |
| Target sprint       | Sprint 3         |
| Assigned to         | Development Team |
| Status              | Backlog          |
| Dependencies        | HU-MON-003       |
| Affected service(s) | ms-environment-monitoring     |

---

### HU-ANA-001 — Daily environmental analysis {#HU-ANA-001}

**Epic:** EP-005

> **As** a registered user
>
> **I want** to analyze environmental data by day
>
> **so that** I can identify daily variations in the environmental conditions of an educational environment.

**Acceptance Criteria:**

```gherkin
Scenario 1: Consult daily analysis

  Given historical environmental data exists for the selected day
  When the user selects the daily analysis
  Then the system displays the environmental measurements for that day
  And the information can be interpreted according to the available variables

Scenario 2: No data for selected day

  Given there is no historical environmental data for the selected day
  When the user requests the daily analysis
  Then the system indicates that there is no available data for that date
```

| Field               | Value            |
| ------------------- | ---------------- |
| Story Points        | 5                |
| Priority            | Must Have        |
| Target sprint       | Sprint 4         |
| Assigned to         | Development Team |
| Status              | Backlog          |
| Dependencies        | HU-MON-002       |
| Affected service(s) | ms-environment-monitoring     |

---

### HU-ANA-002 — Weekly environmental analysis {#HU-ANA-002}

**Epic:** EP-005

> **As** a registered user
>
> **I want** to analyze environmental data by week
>
> **so that** I can identify environmental variations throughout a week.

**Acceptance Criteria:**

```gherkin
Scenario 1: Consult weekly analysis

  Given historical environmental data exists for the selected week
  When the user selects the weekly analysis
  Then the system displays the environmental information corresponding to the selected week

Scenario 2: No data for selected week

  Given there is no historical data for the selected week
  When the user requests the weekly analysis
  Then the system indicates that there is no available data
```

| Field               | Value            |
| ------------------- | ---------------- |
| Story Points        | 5                |
| Priority            | Must Have        |
| Target sprint       | Sprint 4         |
| Assigned to         | Development Team |
| Status              | Backlog          |
| Dependencies        | HU-MON-002       |
| Affected service(s) | ms-environment-monitoring     |

---

### HU-ANA-003 — Monthly environmental analysis {#HU-ANA-003}

**Epic:** EP-005

> **As** a registered user
>
> **I want** to analyze environmental data by month
>
> **so that** I can identify medium-term variations in environmental conditions.

**Acceptance Criteria:**

```gherkin
Scenario 1: Consult monthly analysis

  Given historical environmental data exists for the selected month
  When the user selects the monthly analysis
  Then the system displays the environmental information corresponding to the selected month

Scenario 2: No data for selected month

  Given there is no historical environmental data for the selected month
  When the user requests the monthly analysis
  Then the system indicates that there is no available data
```

| Field               | Value            |
| ------------------- | ---------------- |
| Story Points        | 5                |
| Priority            | Must Have        |
| Target sprint       | Sprint 4         |
| Assigned to         | Development Team |
| Status              | Backlog          |
| Dependencies        | HU-MON-002       |
| Affected service(s) | ms-environment-monitoring     |

---

### HU-ANA-004 — Yearly environmental analysis {#HU-ANA-004}

**Epic:** EP-005

> **As** a registered user
>
> **I want** to analyze environmental data by year
>
> **so that** I can identify long-term environmental variations in educational environments.

**Acceptance Criteria:**

```gherkin
Scenario 1: Consult yearly analysis

  Given historical environmental data exists for the selected year
  When the user selects the yearly analysis
  Then the system displays the environmental information corresponding to the selected year

Scenario 2: No data for selected year

  Given there is no historical environmental data for the selected year
  When the user requests the yearly analysis
  Then the system indicates that there is no available data
```

| Field               | Value            |
| ------------------- | ---------------- |
| Story Points        | 5                |
| Priority            | Should Have      |
| Target sprint       | Sprint 4         |
| Assigned to         | Development Team |
| Status              | Backlog          |
| Dependencies        | HU-MON-002       |
| Affected service(s) | ms-environment-monitoring     |

---

### HU-ANA-005 — Environmental dashboard {#HU-ANA-005}

**Epic:** EP-006

> **As** a registered user
>
> **I want** to view environmental information through a dashboard
>
> **so that** I can understand the conditions of educational environments quickly and visually.

**Acceptance Criteria:**

```gherkin
Scenario 1: Display environmental dashboard

  Given the user is authenticated and environmental information is available
  When the user accesses the environmental dashboard
  Then the system displays the available environmental information
  And the information is presented in a clear visual format

Scenario 2: Dashboard without available data

  Given there is no environmental information available
  When the user accesses the dashboard
  Then the system displays an appropriate message indicating that data is unavailable
```

| Field               | Value                  |
| ------------------- | ---------------------- |
| Story Points        | 8                      |
| Priority            | Must Have              |
| Target sprint       | Sprint 4               |
| Assigned to         | Development Team       |
| Status              | Backlog                |
| Dependencies        | HU-MON-003, HU-ANA-001 |
| Affected service(s) | ms-environment-monitoring, Frontend |

---

### HU-ARCH-001 — API Gateway communication {#HU-ARCH-001}

**Epic:** EP-007

> **As** an EduAirControl system
>
> **I want** the frontend requests to be routed through the API Gateway
>
> **so that** access to the microservices is centralized and controlled.

**Acceptance Criteria:**

```gherkin
Scenario 1: Successful request routing

  Given the API Gateway is running and the target microservice is available
  When the frontend sends a request to a valid API Gateway route
  Then the Gateway routes the request to the corresponding microservice
  And the response is returned to the frontend

Scenario 2: Invalid route

  Given the requested Gateway route does not exist
  When the frontend sends the request
  Then the Gateway returns an HTTP 404 Not Found response

Scenario 3: Protected route without authentication

  Given the requested route requires authentication
  When the frontend sends the request without a valid JWT
  Then the Gateway rejects the request
  And an HTTP 401 Unauthorized response is returned
```

| Field               | Value                                                                      |
| ------------------- | -------------------------------------------------------------------------- |
| Story Points        | 5                                                                          |
| Priority            | Must Have                                                                  |
| Target sprint       | Sprint 2                                                                   |
| Assigned to         | Development Team                                                           |
| Status              | Backlog                                                                    |
| Dependencies        | HU-IAM-002                                                                 |
| Affected service(s) | API Gateway, ms-security, ms-user-management, ms-classroom-management, ms-sensor-management, ms-environment-monitoring |

---

## Rules for writing HUs

### 1. The role matters

Do not write "As a user" — use a specific role or system actor:

```text
✓ As a registered user
✓ As a system administrator
✓ As an environmental monitoring system
✓ As a new user

✗ As a person
✗ As someone
```

### 2. The benefit justifies the work

The "so that" must describe a business benefit, not simply repeat the functionality:

```text
✓ so that I can understand the environmental conditions of an educational space

✗ so that I can see environmental data
```

### 3. ACs are verifiable

Each acceptance criterion must be verifiable manually or automatable as a test:

```text
✓ Then the system displays temperature, humidity, CO₂ and noise level

✓ Then the system returns HTTP 401 Unauthorized when the request has no valid JWT

✗ Then the system works well

✗ Then the user is satisfied
```

### 4. One HU = one unit of value

Each HU represents a specific unit of business value and should be small enough to be completed within a sprint.

---

## Ready-to-copy HU template

````markdown
### HU-00X — [Name] {#HU-00X}

**Epic:** EP-00X

> **As** [role]
>
> **I want** [action]
>
> **so that** [benefit]

**Acceptance Criteria:**

```gherkin
Scenario 1: [name]

  Given [context]

  When [action]

  Then [result]
````

| Field               | Value   |
| ------------------- | ------- |
| Story Points        |         |
| Priority            |         |
| Target sprint       |         |
| Status              | Backlog |
| Dependencies        |         |
| Affected service(s) |         |

```

---

## Correlations

- Full template with DoD checklist → `04-requirements/_template-hu.md`
- Non-functional requirements → `04-requirements/non-functional.md`
- Traceability matrix → `04-requirements/traceability-matrix.md`
- Functional requirements → `04-requirements/functional-requirements.md`
- API contracts derived from these HUs → `07-api/contracts/openapi/`
- Domain Map — Bounded Contexts → `02-domain/domain-map.md`
- Microservice catalog → `09-microservices/service-catalog.md`
