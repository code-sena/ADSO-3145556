# HU-JUS-001: Justification support upload

## Story

**As** a student/learner  
**I want** to upload a justification with its corresponding support document for an absence or late arrival  
**So that** my case can be formally reviewed and evaluated by a superior, instead of remaining as an unjustified absence or late arrival.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that the student is authenticated and has at least one recorded absence or late arrival pending justification, when the student accesses the justification module, then the system displays the list of absences or late arrivals eligible for justification.

- [ ] **AC2:** Given that the student selects an eligible absence or late arrival, when the student fills in the justification reason and uploads a valid support document, then the system validates the data, stores the justification with status "Pending", associates it with the corresponding attendance record, and shows a confirmation message.

- [ ] **AC3:** Given that the student attempts to submit a justification without uploading a support document, when the submission is processed, then the system rejects the operation and shows a clear validation message indicating that a support document is required.

- [ ] **AC4:** Given that the uploaded support document exceeds the maximum allowed file size or has an unsupported format, when the upload is attempted, then the system rejects the file and shows a clear error message with the allowed formats and maximum size.

- [ ] **AC5:** Given that a justification already exists for the selected absence or late arrival, when the student attempts to submit a new justification for the same record, then the system prevents duplicate justification and shows a clear message indicating that the record already has a justification in progress or resolved.

- [ ] **AC6:** Given that the student does not have any recorded absences or late arrivals eligible for justification, when the student accesses the justification module, then the system shows a clear message indicating that there are no records pending justification.

- [ ] **AC7:** Given that the justification is successfully submitted, when the operation is completed, then the system records an audit event containing the student identifier, the attendance record associated, the justification identifier, the support document reference, the submission date and time, and the initial status as "Pending".

- [ ] **AC8:** Given normal network conditions, when the student submits a justification with its support document, then the system responds in less than 10 seconds, including file upload time.

- [ ] **AC9:** Given that the justification is successfully submitted, when the system processes the submission, then the support document is stored securely and its content is not exposed in logs or user interface messages beyond the file name or reference.

---

## Technical notes

- The SRS Use Case 3 describes the justification flow as follows:
  1. The user registers attendance.
  2. The system informs the user of recorded absences during the established period.
  3. The system asks the user to upload a valid justification for the absence.
  4. If the user does not upload a justification, the absence remains marked as unjustified.
- RF 6.1 indicates that the support document must be uploaded successfully before continuing with the evaluation process.
- The system must validate:
  - The student is authenticated and active.
  - The attendance record exists and corresponds to an absence or late arrival.
  - The attendance record does not already have an active or resolved justification.
  - The support document is present, within the allowed file size, and in a supported format.
  - The justification reason is not empty.
- Supported file formats and maximum file size must be defined by the team. Proposed initial values:
  - Formats: PDF, JPG, JPEG, PNG.
  - Maximum size: 5 MB.
- The justification must be linked to a single attendance record, according to the domain rule RN-32 defined in `entities-and-rules.md`.
- A justification cannot be approved without at least one valid support document, according to domain rule RN-33.
- The support document must be stored securely and must not expose its content in logs or the interface.
- Justification parametrization, such as valid justification types, is managed in [HU-CONF-008](../HU-CONF/user-stories.md#hu-conf-008-delay-and-justification-parametrization) and RF 4.7.
- This story does not cover the approval/rejection workflow. That is handled in [HU-JUS-002](#hu-jus-002-justification-approval-and-rejection).
- This story does not cover the automatic notification of the result. That is handled in [HU-JUS-003](#hu-jus-003-automatic-notification-of-justification-result).

**Responsible service(s):** Justification service / File storage service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/attendance/unjustified` — proposed, returns absences and late arrivals eligible for justification.  
- `POST /api/v1/justifications` — proposed, creates the justification with the support document.  
- `GET /api/v1/justifications/me` — proposed, returns the student's own justifications.  

**Events generated:**  
- `JustificationSubmitted`  
- `JustificationSubmissionFailed`  
- `SupportDocumentUploaded`  

**Required permissions:**  
- Authenticated student/learner with at least one unjustified absence or late arrival.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Eligible absences and late arrivals list is displayed correctly.
- [ ] Successful justification submission with valid support document tested.
- [ ] Missing support document scenario tested.
- [ ] Invalid file format scenario tested.
- [ ] Oversized file scenario tested.
- [ ] Duplicate justification prevention tested.
- [ ] No eligible records scenario tested.
- [ ] Justification is correctly linked to the attendance record.
- [ ] Justification is stored with initial status "Pending".
- [ ] Support document is stored securely.
- [ ] Sensitive document content is not exposed in logs or UI.
- [ ] Audit event is generated.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions.
- [ ] Traceability to RF 6.1 and Use Case 3 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 8 |
| Priority | High |
| Target sprint | Sprint 2 |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-ATT-001](../HU-ATT/user-stories.md#hu-att-001-facial-attendance-registration): Facial attendance registration; [HU-HIST-001](../HU-HIST/user-stories.md#hu-hist-001-attendance-and-absence-query-by-parameters): Attendance and absence query by parameters; [HU-CONF-008](../HU-CONF/user-stories.md#hu-conf-008-delay-and-justification-parametrization): Delay and justification parametrization |

---

# HU-JUS-002: Justification approval and rejection

## Story

**As** an instructor, administrator, or supervisor  
**I want** to review a student's justification, evaluate the uploaded support document, and approve or reject the justification  
**So that** the student's attendance record is updated fairly and transparently according to institutional rules.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that an authorized user with a superior role is authenticated, when the user accesses the justification review module, then the system displays the list of justifications with status "Pending" that correspond to their assigned cohorts, environments, or institutional scope according to their role.

- [ ] **AC2:** Given that the reviewer selects a pending justification, when the system displays the justification detail, then the reviewer can see the student's name, the attendance record date, the absence or late arrival type, the justification reason, and the uploaded support document.

- [ ] **AC3:** Given that the reviewer reviews the support document and determines it is valid according to the configured justification parameters, when the reviewer approves the justification, then the system updates the justification status to "Approved", updates the corresponding attendance record status to "Justified", stores the reviewer's identifier and evaluation date, and shows a confirmation message.

- [ ] **AC4:** Given that the reviewer reviews the support document and determines it is not valid, when the reviewer rejects the justification, then the system updates the justification status to "Rejected", keeps the attendance record in its original status, stores the reviewer's identifier and evaluation date, and shows a confirmation message.

- [ ] **AC5:** Given that the support document does not exist or cannot be retrieved at the time of review, when the reviewer attempts to evaluate the justification, then the system prevents the evaluation and shows a clear error message indicating that the support document is not available.

- [ ] **AC6:** Given that a justification has already been approved or rejected, when another reviewer or the same reviewer attempts to evaluate it again, then the system prevents re-evaluation and shows a clear message indicating that the justification has already been resolved.

- [ ] **AC7:** Given that the user attempting to evaluate the justification does not have a superior role or does not have permission to review justifications for that cohort or student, when the user attempts the evaluation, then the system denies access and records an unauthorized access attempt.

- [ ] **AC8:** Given that the justification is approved or rejected, when the operation is completed, then the system records an audit event containing the justification identifier, the attendance record, the reviewer identifier, the decision (approved or rejected), and the evaluation date and time.

- [ ] **AC9:** Given normal network conditions, when the reviewer submits the approval or rejection, then the system responds in less than 5 seconds, according to RNF 1.

---

## Technical notes

- RF 6.2 indicates that the support must be validated according to what is defined by the superior.
- RF 4.7 defines the parametrization of valid justification types. The evaluation should consider whether the submitted justification matches the configured valid types.
- Domain rule RN-35 states that a justification can only be evaluated by a user with a role superior to the student's role.
- Domain rule RN-34 states that a justification can only be approved if it meets the criteria parametrized by the administrator.
- Domain rule RN-33 states that a justification cannot be approved if it does not have at least one valid support document.
- The system must validate:
  - The reviewer is authenticated and has a superior role.
  - The reviewer has visibility of the justification according to their assigned cohorts or scope.
  - The justification exists and is in status "Pending".
  - The support document exists and is accessible.
  - The justification has not been previously resolved.
- When a justification is approved, the corresponding attendance record should be updated to "Justified".
- When a justification is rejected, the attendance record should remain in its original status ("Absent" or "Late").
- The reviewer's identity and decision must be stored for traceability.
- The review process must not allow the same justification to be evaluated more than once.
- This story does not cover automatic notification of the result. That is handled in [HU-JUS-003](#hu-jus-003-automatic-notification-of-justification-result).

**Responsible service(s):** Justification service / Attendance service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/justifications?status=pending` — proposed, returns pending justifications filtered by reviewer scope.  
- `GET /api/v1/justifications/{justificationId}` — proposed, returns justification detail with support document.  
- `POST /api/v1/justifications/{justificationId}/approve` — proposed.  
- `POST /api/v1/justifications/{justificationId}/reject` — proposed.  

**Events generated:**  
- `JustificationApproved`  
- `JustificationRejected`  
- `AttendanceRecordUpdatedToJustified`  
- `JustificationEvaluationFailed`  
- `UnauthorizedJustificationAccessAttempt`  

**Required permissions:**  
- Instructor, Administrator, or Supervisor, depending on institutional policy.  
- The reviewer must have visibility of the student's cohort or scope.  
- Students must not be able to evaluate their own justifications.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Pending justification list is displayed correctly for the reviewer's scope.
- [ ] Justification detail with support document is displayed correctly.
- [ ] Successful approval tested.
- [ ] Successful rejection tested.
- [ ] Attendance record is updated to "Justified" after approval.
- [ ] Attendance record remains unchanged after rejection.
- [ ] Missing or inaccessible support document scenario tested.
- [ ] Already resolved justification scenario tested.
- [ ] Unauthorized reviewer scenario tested.
- [ ] Student self-evaluation prevention tested.
- [ ] Reviewer identity and decision date are stored.
- [ ] Audit event is generated for approval and rejection.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 6.2, RF 4.7, and Use Case 3 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 8 |
| Priority | High |
| Target sprint | Sprint 2 |
| Dependencies | [HU-JUS-001](#hu-jus-001-justification-support-upload): Justification support upload; [HU-CONF-008](../HU-CONF/user-stories.md#hu-conf-008-delay-and-justification-parametrization): Delay and justification parametrization; [HU-IAM-003](../HU-IAM/user-stories.md#hu-iam-003-role-based-permission-assignment): Role-based permission assignment |

---

# HU-JUS-003: Automatic notification of justification result

## Story

**As** a student/learner who submitted a justification  
**I want** to receive an automatic notification with the result of my justification (approved or rejected)  
**So that** I know the outcome without having to check the system manually and I can take further action if needed.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that a reviewer has approved a justification, when the approval is processed, then the system automatically sends a notification to the student who submitted the justification, including the justification identifier, the attendance record date, and the result as "Approved".

- [ ] **AC2:** Given that a reviewer has rejected a justification, when the rejection is processed, then the system automatically sends a notification to the student who submitted the justification, including the justification identifier, the attendance record date, and the result as "Rejected".

- [ ] **AC3:** Given that the notification is sent, when the student accesses the notification module or their notification inbox, then the student can see the notification with the justification result, the date of the evaluation, and the associated attendance record.

- [ ] **AC4:** Given that the notification fails to be delivered due to a system error, when the delivery fails, then the system records the failure in the platform log and retries delivery according to the configured retry policy, or marks the notification as failed for administrative review.

- [ ] **AC5:** Given that a justification is evaluated, when the result notification is generated, then the notification does not include sensitive details such as the support document content, the reviewer's personal data, or any biometric information. Only the result, justification identifier, and attendance date are included.

- [ ] **AC6:** Given that the student has notifications enabled, when multiple justifications are evaluated, then the system sends individual notifications for each justification result.

- [ ] **AC7:** Given that the notification is generated, when the operation is completed, then the system emits a platform audit event containing the notification type, the student identifier, the justification identifier, the result, and the notification delivery status.

- [ ] **AC8:** Given normal system conditions, when the justification result is processed, then the notification is generated and queued for delivery within 60 seconds of the evaluation.

---

## Technical notes

- RF 6.3 indicates that the superior provides the justification response automatically and that the support must exist at the time of the response.
- The notification mechanism must be defined by the team. Possible channels include:
  - In-app notification, as the primary channel.
  - Email notification, if configured and approved.
  - Push notification, if the mobile app supports it.
  - SMS or external messaging channels are out of scope unless explicitly approved.
- The notification should be generated as a reaction to the `JustificationApproved` or `JustificationRejected` events from [HU-JUS-002](#hu-jus-002-justification-approval-and-rejection).
- The notification must not include:
  - The support document content.
  - The reviewer's personal data beyond what is institutionally allowed.
  - Biometric or sensitive information.
- If the notification delivery fails, the system should:
  - Log the failure.
  - Retry according to a configured policy, or mark it for administrative review.
  - Not block the justification evaluation process.
- The student must be able to consult their notifications even if they missed the initial delivery.
- Notification preferences may be managed by the student through [HU-CONF-001](../HU-CONF/user-stories.md#hu-conf-001-configure-system-alerts) and [HU-CONF-002](../HU-CONF/user-stories.md#hu-conf-002-enable-or-disable-alerts), if those stories are implemented.
- Domain rule RN-36 states that the result of a justification must be notified automatically to the requesting user.
- The team must define the maximum acceptable delay between evaluation and notification delivery. Proposed initial value: 60 seconds.

**Responsible service(s):** Justification service / Notification service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/notifications/me` — proposed, returns notifications for the authenticated user.  
- `GET /api/v1/notifications/{notificationId}` — proposed, returns notification detail.  
- `PATCH /api/v1/notifications/{notificationId}/read` — proposed, marks a notification as read.  

**Events generated:**  
- `JustificationResultNotificationSent`  
- `JustificationResultNotificationFailed`  
- `NotificationRead`  

**Required permissions:**  
- System-generated notification, not user-triggered.  
- Authenticated student/learner may consult and read their own notifications.  
- Administrators or Supervisors may review notification delivery failures.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Automatic notification is generated after justification approval.
- [ ] Automatic notification is generated after justification rejection.
- [ ] Notification content includes justification identifier, attendance date, and result.
- [ ] Notification does not include sensitive data such as support content or reviewer personal data.
- [ ] Student can consult notifications in the notification inbox.
- [ ] Notification delivery failure is logged.
- [ ] Retry or administrative review mechanism is tested.
- [ ] Multiple justification evaluations generate individual notifications.
- [ ] Notification is generated within the acceptable delay after evaluation.
- [ ] Audit event is generated for notification delivery.
- [ ] Confirmation and error messages are clear.
- [ ] Traceability to RF 6.3 and Use Case 3 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 5 |
| Priority | High |
| Target sprint | Sprint 2 |
| Dependencies | [HU-JUS-002](#hu-jus-002-justification-approval-and-rejection): Justification approval and rejection; [HU-CONF-001](../HU-CONF/user-stories.md#hu-conf-001-configure-system-alerts): Configure system alerts, if applicable; [HU-CONF-002](../HU-CONF/user-stories.md#hu-conf-002-enable-or-disable-alerts): Enable or disable alerts, if applicable |