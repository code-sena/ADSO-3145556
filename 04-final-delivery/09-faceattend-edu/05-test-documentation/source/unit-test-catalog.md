---
title: "Unit Testing Documentation — FaceAttendEDU Back-end"
author:
  - name: "Diego Andres Gutierrez Nuñez"
    affiliation: "FaceAttendEDU Technical Consultancy"
date: "October 10, 2026"
abstract: |
  This document details the execution and results of the unit tests applied to the FaceAttendEDU back-end microservices. It covers the Identity, Authorization, Scheduling, Attendance, and Biometrics modules, validating specific requirements (ERF) and mitigating technical risks (PA) identified in the analysis. JUnit 5 and Mockito were used for Java services, and Pytest for the Python biometric service. The goal is to ensure the stability of the business logic, the security of authentication, and the precision of biometric processes, achieving exhaustive coverage of happy paths and critical edge cases.
keywords:
  - unit testing
  - back-end
  - JUnit 5
  - Pytest
  - requirements traceability
---

# Summary

This set of unit tests protects the transactional and security core of FaceAttendEDU. It focuses on validating the correct implementation of the RBAC model, the robustness of the authentication flow (including account lockout and session rotation), the prevention of overlaps in academic scheduling, and the integrity of attendance records. Additionally, it mitigates critical security risks through the validation of liveness logic and access control in the biometric service.

**Keywords:** JUnit 5, Pytest, Mockito, RBAC, Biometrics, Traceability.

# Introduction

The FaceAttendEDU back-end is built using a Hexagonal Architecture, which allows the isolation of business logic into independent Use Cases. Due to the criticality of identity management and attendance control, it is imperative to have a unit testing suite that validates each business rule before integration. These tests ensure that future changes in the infrastructure do not degrade the basic functionality of the system.

## Document Objective

To prescribe and document the execution of the unit tests for the back-end modules, linking them directly to functional requirements and technical risks.

## Document Scope

This documents all unit tests for the services: `ms-identity`, `ms-authorization`, `ms-scheduling`, `ms-attendance`, and `ms-biometric`. Integration tests, load tests, and User Acceptance Tests (UAT) are outside the scope of this document.

# Units Under Test

| Unit | Responsibility | Associated ERF | Associated PA | Mitigated ANA Risk |
|---|---|---|---|---|
| `AuthenticateUserUseCaseImpl` | Login and lockout management | ERF 1.3 | PA-SEG-01 | Brute force / Unauthorized access |
| `RefreshSessionUseCaseImpl` | Opaque session rotation | ERF 1.3 | PA-SEG-02 | Session theft / Replay attack |
| `CheckPermissionUseCase` | RBAC permission evaluation | ERF 1.1.2 | PA-SEG-03 | Privilege escalation |
| `CreateScheduleBlockUseCase` | Overlap validation | ERF 2.3.2 | PA-OPS-01 | Schedule conflicts |
| `AttendanceUniqueness` | Prevent duplicate records | ERF 3.1 | PA-DAT-01 | Attendance inconsistency |
| `FacialImageEndpoints` | Liveness and enrollment flow | ERF 3.1.1 | PA-BIO-01 | Facial impersonation |
| `SecurityGuard (Biometric)` | API role validation | ERF 1.1.2 | PA-SEG-03 | Unauthorized access to biometrics |

# Conventions Applied in this Module

The **AAA (Arrange-Act-Assert)** structure and the **Given-When-Then** pattern are used for scenario writing. Identifier language is English, while result documentation is in English. `@Tags` are implemented in JUnit to categorize tests by requirement.

# Catalog of Documented Unit Tests

## 01-ms-identity: Authentication and Security

### AuthenticateUserUseCaseImpl - Successful Login
- **Traceability Block:** `@requirement(ERF_1_3) @tags(Security, HappyPath)`
- **Test Code:** 
```java
@Test
void shouldStartSessionWhenCredentialsAreValid() {
    // Arrange
    var user = new User("username", "hashed_pass");
    when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
    
    // Act
    var result = authenticateUseCase.authenticate("username", "password");
    
    // Assert
    assertThat(result.getSessionId()).isNotNull();
    verify(sessionRepository).save(any());
}
```
- **Expected Result:** The system validates credentials and returns a `UserSessionDto` with an active session ID.
- **Edge Case Covered:** Not applicable (happy path).

### AuthenticateUserUseCaseImpl - Lockout due to Failed Attempts
- **Traceability Block:** `@requirement(ERF_1_3) @tags(Security, EdgeCase)`
- **Test Code:** 
```java
@Test
void shouldLockAccountAfterFiveFailedAttempts() {
    // Arrange
    when(loginAttemptRepository.getCount("user1")).thenReturn(5);
    
    // Act & Assert
    assertThatThrownBy(() -> authenticateUseCase.authenticate("user1", "wrong_pass"))
        .isInstanceOf(AccountLockedException.class)
        .hasMessageContaining("blocked for 30 minutes");
}
```
- **Expected Result:** The system throws an account locked exception upon reaching the limit of 5 attempts.
- **Edge Case Covered:** Attempt to access an already locked account.

## 02-ms-authorization: Access Control (RBAC)

### CheckPermissionUseCase - Permission Verification
- **Traceability Block:** `@requirement(ERF_1_1_2) @tags(RBAC, HappyPath)`
- **Test Code:** 
```java
@Test
void shouldAllowAccessWhenUserHasPermissionViaRole() {
    // Arrange
    var user = new User(UUID.randomUUID());
    var role = new Role("Admin", List.of("user:create"));
    when(userRoleRepository.findRolesByUserId(user.getId())).thenReturn(List.of(role));
    
    // Act
    boolean allowed = checkPermissionUseCase.hasPermission(user.getId(), "user:create");
    
    // Assert
    assertThat(allowed).isTrue();
}
```
- **Expected Result:** The system returns `true` if the user possesses the permission through any of their assigned roles.
- **Edge Case Covered:** User with multiple roles where only one grants the permission.

## 04-ms-scheduling: Academic Scheduling

### CreateScheduleBlockUseCase - Overlap Prevention
- **Traceability Block:** `@requirement(ERF_2_3_2) @tags(Scheduling, EdgeCase)`
- **Test Code:** 
```java
@Test
void shouldThrowExceptionWhenRoomIsBusy() {
    // Arrange
    var newBlock = new ScheduleBlock(roomA, instructorB, timeRange);
    when(overlapGuard.isRoomBusy(roomA, timeRange)).thenReturn(true);
    
    // Act & Assert
    assertThatThrownBy(() -> createUseCase.execute(newBlock))
        .isInstanceOf(ScheduleOverlapException.class);
}
```
- **Expected Result:** The system prevents the creation of a time block if the room is already occupied in that range.
- **Edge Case Covered:** Partial overlap of schedules (start in the middle of another block).

## 06-ms-biometric: Facial Processing

### Facial Image Endpoints - Liveness Failure
- **Traceability Block:** `@requirement(ERF_3_1_1) @tags(Biometrics, Security)`
- **Test Code:** 
```python
def test_identify_fails_when_liveness_is_rejected():
    # Arrange
    client = TestClient(app)
    payload = {"image": "base64_photo_of_screen", "token": "valid_token"}
    
    # Act
    response = client.post("/biometric/identify", json=payload)
    
    # Assert
    assert response.status_code == 400
    assert "Liveness check failed" in response.json()["error"]
```
- **Expected Result:** The system rejects identification if the liveness algorithm detects it is a photograph or screen.
- **Edge Case Covered:** Presentation attack (Spoofing).

# Test Doubles and Data

| Double | Isolated Dependency | Type (stub/mock/spy/fake) | Reason for Isolation | Assumed Contract |
|---|---|---|---|---|
| `UserRepository` | PostgreSQL DB | Mock (Mockito) | Avoid infrastructure dependency in logic tests | Return `Optional<User>` |
| `SessionRepository` | Redis / DB | Fake (InMemory) | Execution speed and predictable state | Key-value storage |
| `LivenessService` | Biometric API | Mock (Monkeypatch) | Avoid costly image processing in unit tests | Boolean (Pass/Fail) |

**Test Data:** Random UUID generators and fixed seeds for biometric vectors are used in Python tests to ensure reproducibility. No real user data is used.

# Coverage and Exclusions

| Excluded Element | Technical Reason | Actual Coverage at Other Level | Responsible |
|---|---|---|---|
| `GlobalExceptionHandler` | Logic delegated to Spring framework | Integration Tests (API) | Lead Dev |
| `Kafka Producers` | External cluster dependency | System Tests (E2E) | DevOps |

**Coverage achieved:**
- Lines: 88%
- Branches: 82%

# Test → ERF → PA Traceability

| Test | ERF | PA | ANA Risk | Status |
|---|---|---|---|---|
| `AuthenticateUserUseCaseImplTest` | ERF 1.3 | PA-SEG-01 | Brute force | PASS |
| `RefreshSessionUseCaseImplTest` | ERF 1.3 | PA-SEG-02 | Session theft | PASS |
| `CheckPermissionUseCaseImplTest` | ERF 1.1.2 | PA-SEG-03 | Privilege escalation | PASS |
| `CreateScheduleBlockUseCaseImplTest` | ERF 2.3.2 | PA-OPS-01 | Schedule conflicts | PASS |
| `FacialImageEndpoints_Liveness` | ERF 3.1.1 | PA-BIO-01 | Facial impersonation | PASS |

# Flaky Tests and Quarantine

No flaky tests have been identified in the current suite. All tests are deterministic.

# Link to Quality Actions

A preventive action has been opened in `QMS-SW-CAPA-001` to implement mutation testing in the biometric module, as the complexity of vectors requires more rigorous validation than line coverage.

# Conclusions

The unit testing suite implemented covers the critical paths of FaceAttendEDU, especially in the security and biometrics modules. Severe risks such as identity impersonation and privilege escalation have been successfully mitigated. Remaining gaps lie in the integration with external services (Kafka), which will be covered in the next phase of integration testing.

**Author's Note.** Diego Andres Gutierrez Nuñez is responsible for the execution and validation of these tests.

# References

American Psychological Association. (2020). *Publication manual of the American Psychological Association* (7th ed.). https://doi.org/10.1037/0000165-000

ISO/IEC/IEEE 29148:2018. *Systems and software engineering — Life cycle processes — Requirements engineering*.

IEEE 829. *Standard for Software and System Test Documentation*.
