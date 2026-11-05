```markdown
# HU-IAM-001: Registro de usuario

## Story

**As** administrator or new user

**I want** to register a new account in EduAirControl

**So that** I can access the platform and use its environmental monitoring and analysis functionalities according to my permissions.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]"

- [ ] **AC1:** Given that the user is on the registration form, when they enter valid required information and submit the form, then the system creates the user account successfully.

- [ ] **AC2:** Given that the user provides valid registration information, when the registration is completed, then the system stores the user information in the User Management context and the account can be used for authentication.

- [ ] **AC3:** Given that the user attempts to register with an email that is already associated with an existing account, when they submit the registration form, then the system rejects the operation and displays an error indicating that the email is already registered.

- [ ] **AC4:** Given that the user enters incomplete or invalid required information, when they submit the registration form, then the system validates the information and prevents the account from being created.

- [ ] **AC5:** Given that the user successfully registers, when the account is created, then the system assigns the default user role and the corresponding initial account status.

---

## Technical notes

> The registration functionality must be implemented following the architecture defined for EduAirControl. The User Management functionality is responsible for managing user accounts and authentication-related information. Input data must be validated before persistence, duplicate accounts must be prevented, and sensitive authentication information must not be stored in plain text.

**Responsible service(s):** `ms-security`

**Endpoint(s) implemented:** `POST /api/auth/register`

**Events generated:** `UserRegistered`

**Required permissions:** Public — no authentication required for registration.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

> See: [`00-governance/definition-of-done.md`](../00-governance/definition-of-done.md)

**Additional checks specific to this HU (if applicable):**

- [ ] Database migration for the user structure has been executed successfully.
- [ ] Passwords are encrypted/hashed before being stored in the database.
- [ ] Duplicate email validation has been implemented.
- [ ] Registration endpoint has been tested successfully.
- [ ] Validation and error responses have been verified.
- [ ] Registration has been tested from the frontend.
- [ ] The created user can subsequently authenticate using the registered credentials.

---

## Estimation and priority

| Field | Value |
|-------|-------|
| Story Points | 5 |
| Priority | High |
| Target sprint | Sprint 1 |
| Dependencies | None |
```
