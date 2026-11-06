# HU-ATT-001: Facial attendance registration

## Story

**As** a student/learner  
**I want** to register my entry using facial recognition  
**So that** my attendance is recorded quickly, securely, and without depending on manual roll call by the instructor.

---

## Acceptance criteria

> Format: “Given [context/initial state], when [user action], then [expected and verifiable result].”

- [ ] **AC1:** Given that the student is registered, active, has an active facial parameter, and has an assigned training block, when the student scans their face during the allowed entry period, then the system registers the entry, shows a confirmation message, and stores the date, time, training block, and registration method as “facial”.

- [ ] **AC2:** Given that the facial scan matches a registered user, when the system evaluates the entry time against the assigned training block, then the attendance record is marked as present or late according to the attendance rules configured in the system.

- [ ] **AC3:** Given that the student already has an entry record for the same training block and date, when the student attempts to register entry again, then the system prevents duplicate registration and shows a clear message indicating that the entry was already registered.

- [ ] **AC4:** Given that the student is not registered, is inactive, or does not have an active facial parameter, when the student attempts facial attendance registration, then the system denies the registration and shows a clear error message.

- [ ] **AC5:** Given that the face is not detected, does not match a registered user, or the camera fails, when the facial scan fails, then the system shows an error message and offers the corresponding alternative flow according to RF 3.1.2 or RF 3.2.1.

- [ ] **AC6:** Given normal network, camera, and lighting conditions, when the student completes facial attendance registration, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC7:** Given that biometric data is processed, when the registration is completed, then the system records an audit event and does not expose sensitive biometric data in logs, alerts, or the user interface.

- [ ] **AC8:** Given that the student exits the training session, when the student scans their face during the allowed exit period, then the system registers the exit, shows a confirmation message, and stores the exit time without duplicating the entry record.

---

## Technical notes

- Facial recognition may depend on an external cloud service, according to SRS assumption 2.4.5. If the external service fails, the system must show a clear error and allow the corresponding alternative flow.
- The system requires internet access, functional camera, and adequate lighting and face positioning, according to SRS constraints and assumptions.
- The system must validate:
  - The user exists and is active.
  - The user has an active facial parameter.
  - The user has an assigned training block or schedule.
  - The entry or exit time is appropriate for the assigned block.
  - The user does not already have a duplicate entry or exit record for the same block and date.
- Alternative registration for registered users whose facial scan fails is covered by RF 3.1.2 and should be linked from the failure state.
- Unregistered or casual users are covered by RF 3.2 and RF 3.2.1, but they should not be mixed with regular student attendance records without explicit classification.
- Biometric data must be treated as sensitive data and must comply with applicable data-protection regulations, such as Colombian Law 1581 of 2012.
- The system should prefer facial templates or embeddings over raw facial images, unless raw images are explicitly required and approved.
- Sensitive data must not be written to logs or shown in the interface.
- The attendance registration flow must generate audit information for traceability.
- Performance requirement: response time under 5 seconds, according to RNF 1.

**Responsible service(s):** Attendance service / Facial verification service, proposed.  
**Endpoint(s) implemented:**  
- `POST /api/v1/attendance/entry/facial` — proposed.  
- `POST /api/v1/attendance/exit/facial` — proposed.  
- `POST /api/v1/attendance/entry/alternative` — fallback, may belong to [HU-ATT-002](#hu-att-002-initial-facial-parameter-enrollment).  

**Events generated:**  
- `AttendanceEntryRegistered`  
- `AttendanceExitRegistered`  
- `LateDetected`  
- `FacialScanFailed`  
- `AlternativeRegistrationRequested`  

**Required permissions:**  
- Authenticated student/learner with active account.  
- The system must additionally validate active facial parameter and assigned training block.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team’s full DoD.  
> See: [`00-governance/definition-of-done.md`](https://github.com/code-sena/fae-docs/blob/main/00-governance/definition-of-done.md) (`fae-docs` repository).

**Additional checks specific to this HU:**

- [ ] Facial scan success flow tested with a registered and active user.
- [ ] Facial scan failure flow tested when the face is not detected.
- [ ] Facial scan failure flow tested when the face does not match a registered user.
- [ ] Camera unavailable or permission denied scenario tested.
- [ ] Duplicate entry prevention tested.
- [ ] Duplicate exit prevention tested.
- [ ] Late detection tested when entry occurs after the allowed time.
- [ ] Alternative registration flow is reachable when facial scan fails.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Sensitive biometric data is not exposed in logs, responses, or UI.
- [ ] Audit event is generated for successful and failed registration attempts.
- [ ] Confirmation and error messages are clear, visible, and understandable.
- [ ] Traceability to RF 3.1, RF 3.1.1, CU 1, and CU 2 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 8 |
| Priority | High |
| Target sprint | Sprint 1 / MVP |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-IAM-002](../HU-IAM/user-stories.md#hu-iam-002-user-registration-with-role-assignment): User registration with role assignment; [HU-ATT-002](#hu-att-002-initial-facial-parameter-enrollment): Initial facial parameter enrollment; [HU-ACAD-005](../HU-ACAD/user-stories.md#hu-acad-005-assign-conferencetime-blocks): Assign conference/time blocks |

---

# HU-ATT-002: Initial facial parameter enrollment

## Story

**As** a registered student/learner  
**I want** to register my facial parameter in FaceAttend EDU  
**So that** I can later register my attendance using facial recognition without depending on manual roll call.

---

## Acceptance criteria

> Format: “Given [context/initial state], when [user action], then [expected and verifiable result].”

- [ ] **AC1:** Given that the user is registered and has started a session in FaceAttend EDU, when the user initiates the facial enrollment process, then the system activates the camera and prepares for face capture, according to Use Case 4.

- [ ] **AC2:** Given that the camera is active and the user positions their face correctly, when the system captures the facial image under adequate lighting and positioning conditions, then the system processes the capture and associates the resulting facial parameter with the authenticated user.

- [ ] **AC3:** Given that the facial capture is successful, when the enrollment process finishes, then the system stores the facial parameter securely, shows a confirmation message, and records an audit event.

- [ ] **AC4:** Given that the face is not detected, the image quality is insufficient, or the camera fails, when the enrollment attempt fails, then the system shows a clear error message and allows the user to retry.

- [ ] **AC5:** Given that the user already has an active facial parameter, when the user attempts initial enrollment again, then the system informs that a facial parameter already exists and redirects the user to the update flow, unless replacement is explicitly allowed and confirmed.

- [ ] **AC6:** Given that biometric data is processed, when the facial parameter is stored, then the system protects sensitive data according to RNF 4 and does not expose raw biometric information in logs or user interface messages.

- [ ] **AC7:** Given normal device, camera, and network conditions, when the user completes facial enrollment, then the system responds in less than 5 seconds, according to RNF 1.

---

## Technical notes

- This story corresponds to the initial facial enrollment required before using facial attendance registration in [HU-ATT-001](#hu-att-001-facial-attendance-registration).
- The SRS indicates that facial recognition may fail due to lighting or user movement, so the interface must provide clear guidance.
- Facial enrollment may depend on an external cloud facial recognition service, according to SRS assumption 2.4.5. If the external service fails, the system must show a clear error and allow retry.
- The system should store facial templates or parameters instead of raw facial images unless raw images are explicitly required and approved.
- Biometric data must comply with applicable data-protection regulations, such as Colombian Law 1581 of 2012.
- The user must be authenticated before associating a facial parameter with their account.
- Camera permissions must be requested and handled gracefully if denied.
- Facial parameter updates are covered separately in [HU-CONF-006](../HU-CONF/user-stories.md#hu-conf-006-update-user-facial-parameters).
- Sensitive biometric data must not be written to logs or shown in the interface.

**Responsible service(s):** Attendance service / Biometric enrollment service, proposed.  
**Endpoint(s) implemented:**  
- `POST /api/v1/biometrics/facial-enrollment` — proposed.  
- `GET /api/v1/users/me/facial-status` — proposed.  

**Events generated:**  
- `FacialParameterRegistered`  
- `FacialRegistrationFailed`  
- `FacialParameterAlreadyExists`  

**Required permissions:**  
- Authenticated student/learner.  
- Administrator or Supervisor may assist with enrollment only if allowed by institutional policy.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Successful facial enrollment tested.
- [ ] Face not detected scenario tested.
- [ ] Poor lighting or movement scenario tested.
- [ ] Camera unavailable or permission denied scenario tested.
- [ ] Existing facial parameter scenario tested.
- [ ] Facial parameter is correctly associated with the authenticated user.
- [ ] Sensitive biometric data is not exposed in logs, responses, or UI.
- [ ] Audit event is generated.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to Use Case 4 and related RFs is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 8 |
| Priority | High |
| Target sprint | Sprint 1 / MVP |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-IAM-002](../HU-IAM/user-stories.md#hu-iam-002-user-registration-with-role-assignment): User registration with role assignment |

---

# HU-ATT-003: Alternative registration for registered users with facial scan failure

## Story

**As** a registered student/learner whose facial scan fails  
**I want** to complete a time-limited alternative registration process  
**So that** my attendance can still be recorded even when facial recognition cannot validate me.

---

## Acceptance criteria

> Format: “Given [context/initial state], when [user action], then [expected and verifiable result].”

- [ ] **AC1:** Given that a registered user attempted facial attendance registration and the facial scan failed, when the user selects the alternative registration option, then the system presents a time-limited survey requesting the personal data required for validation, according to RF 3.1.2.

- [ ] **AC2:** Given that the user provides personal data within the configured time limit, when the alternative registration is submitted, then the system compares the provided data with the data registered in the system.

- [ ] **AC3:** Given that the provided data matches the registered data, when the comparison is completed, then the system registers attendance, shows a confirmation message, stores the record with the method marked as “alternative”, and records an audit event.

- [ ] **AC4:** Given that the provided data does not match the registered data, when the comparison is completed, then the system rejects the alternative registration and shows a clear error message.

- [ ] **AC5:** Given that the configured time limit expires before the user submits the survey, when the user attempts to continue, then the system cancels the alternative registration attempt and shows a timeout message.

- [ ] **AC6:** Given that the person attempting alternative registration is not a registered user, when the alternative registration data is validated, then the system rejects the process and directs the person to the unregistered-user flow, if applicable.

- [ ] **AC7:** Given normal system conditions, when the alternative registration request is processed, then the system responds in less than 5 seconds, according to RNF 1.

---

## Technical notes

- The SRS describes the alternative registration as a survey with a time limit, where the user enters personal data to be compared with registered data.
- The time limit value must be defined by the team. A proposed initial value is 60 seconds, but it must be validated.
- The personal data fields used for validation must be defined by the team and should follow data-minimization principles.
- This story is a fallback for [HU-ATT-001](#hu-att-001-facial-attendance-registration) when facial recognition fails due to lighting, movement, camera failure, or non-matching facial parameters.
- The system must validate that the user is registered in the system, according to RF 3.1.2.
- The alternative registration must be linked to the corresponding attendance block, date, and user.
- The system should prevent abuse of the alternative registration flow using rate limiting or attempt controls.
- Personal data used for validation must not be written to logs or shown in the interface.
- If the alternative flow is used in a kiosk or shared device, the system must not expose unrelated user information.
- This story does not cover unregistered or NN users. That case is handled by [HU-ATT-004](#hu-att-004-entry-control-for-unregistered-or-nn-users) and [HU-ATT-005](../Index_HU.md#planned-stories-pending-documentation).

**Responsible service(s):** Attendance service / Alternative validation service, proposed.  
**Endpoint(s) implemented:**  
- `POST /api/v1/attendance/alternative/validate` — proposed.  
- `POST /api/v1/attendance/alternative/register` — proposed.  

**Events generated:**  
- `AlternativeAttendanceRegistered`  
- `AlternativeRegistrationFailed`  
- `AlternativeRegistrationTimedOut`  
- `UnregisteredUserDetected`  

**Required permissions:**  
- Registered user whose data can be validated.  
- If used in a kiosk or attendance terminal, the endpoint should be restricted to the attendance context and protected with rate limiting.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Alternative registration flow is reachable after facial scan failure.
- [ ] Successful alternative registration tested.
- [ ] Data mismatch scenario tested.
- [ ] Time limit expiration scenario tested.
- [ ] Unregistered user scenario tested.
- [ ] Attendance record is stored with method marked as alternative.
- [ ] Personal data used for validation is not exposed in logs or UI.
- [ ] Audit event is generated.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 3.1.2 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 5 |
| Priority | High |
| Target sprint | Sprint 1 / MVP |
| Dependencies | [HU-IAM-002](../HU-IAM/user-stories.md#hu-iam-002-user-registration-with-role-assignment): User registration with role assignment; [HU-ATT-001](#hu-att-001-facial-attendance-registration): Facial attendance registration; [HU-ACAD-005](../HU-ACAD/user-stories.md#hu-acad-005-assign-conferencetime-blocks): Assign conference/time blocks |

---

# HU-ATT-004: Entry control for unregistered or NN users

## Story

**As** an administrator, supervisor, or attendance-control user  
**I want** the system to classify people who are not part of the official attendance control as NN or casual users  
**So that** the system can distinguish official registered users from visitors or unregistered people.

---

## Acceptance criteria

> Format: “Given [context/initial state], when [user action], then [expected and verifiable result].”

- [ ] **AC1:** Given that a person attempts to be registered in the attendance control process, when the system checks the person’s identification data, then the system determines whether the person is a registered user or an unregistered/NN user.

- [ ] **AC2:** Given that the person is registered in FaceAttend EDU, when the classification process is executed, then the system assigns the person the corresponding registered-user type and directs the process to the normal or alternative registered-user flow.

- [ ] **AC3:** Given that the person is not registered in FaceAttend EDU, when the classification process is executed, then the system assigns the person the NN or casual-user type and enables the corresponding unregistered-user flow.

- [ ] **AC4:** Given that the identification data is incomplete, invalid, or cannot be processed, when the classification request is submitted, then the system rejects the classification and shows a clear validation message.

- [ ] **AC5:** Given that a person is classified as NN or casual user, when the classification is completed, then the system stores the classification separately from official student attendance records and records an audit event.

- [ ] **AC6:** Given normal system conditions, when the classification request is processed, then the system responds in less than 5 seconds, according to RNF 1.

---

## Technical notes

- The SRS RF 3.2 describes this requirement as determining which users are target users of attendance control and which are casual users.
- The output defined by the SRS is the assignment of user type: NN/user.
- The SRS acceptance criterion says the system must validate whether the user is registered. For this story, that validation is interpreted as checking whether the person exists in the official user registry.
- This story covers classification and control. The actual alternative registration for NN users is handled in [HU-ATT-005](../Index_HU.md#planned-stories-pending-documentation).
- The system must avoid mixing official attendance records with casual or NN records unless the institution explicitly requires consolidated reporting.
- Personal data used to classify a person must be minimized and protected.
- The classification process may be used in kiosk, reception, or attendance-terminal scenarios.
- Rate limiting and auditability are recommended to prevent abuse or unauthorized queries.
- The team must define which identification fields are required to classify a person.

**Responsible service(s):** Attendance service / Person classification service, proposed.  
**Endpoint(s) implemented:**  
- `POST /api/v1/attendance/person-classification` — proposed.  
- `GET /api/v1/attendance/classifications` — proposed for administrative review.  

**Events generated:**  
- `PersonClassifiedAsRegisteredUser`  
- `PersonClassifiedAsNN`  
- `PersonClassificationFailed`  

**Required permissions:**  
- System process for automatic classification.  
- Administrator or Supervisor for reviewing classifications.  
- Kiosk or attendance-terminal access should be restricted and rate-limited.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Registered user classification tested.
- [ ] Unregistered/NN user classification tested.
- [ ] Invalid or incomplete identification data tested.
- [ ] Classification is stored separately from official attendance records.
- [ ] NN classification enables the corresponding unregistered-user flow.
- [ ] Registered user classification redirects to normal or alternative registered flow.
- [ ] Audit event is generated.
- [ ] Personal data is not exposed in logs or UI.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 3.2 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 5 |
| Priority | High |
| Target sprint | Sprint 2 |
| Dependencies | [HU-IAM-002](../HU-IAM/user-stories.md#hu-iam-002-user-registration-with-role-assignment): User registration with role assignment; [HU-ATT-001](#hu-att-001-facial-attendance-registration): Facial attendance registration; related to [HU-ATT-005](../Index_HU.md#planned-stories-pending-documentation) |

---

