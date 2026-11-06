# HU-HIST-001: Attendance and absence query by parameters

## Story

**As** an administrator, supervisor, instructor, or student  
**I want** to query attendance and absence records using filters such as date, cohort, environment, instructor, or person  
**So that** I can obtain accurate attendance information according to my role and make timely academic decisions.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

### Parameter validation

- [ ] **AC1:** Given that an authenticated user accesses the attendance history module, when the user selects one or more query parameters (date, cohort, environment, instructor, or person), then the system validates each parameter before processing the query.

- [ ] **AC2:** Given that the user selects a date parameter, when the query is executed, then the system validates that the date is valid and applicable to the attendance control context before returning results.

- [ ] **AC3:** Given that the user selects a cohort/ficha as a filter, when the query is executed, then the system validates that the cohort is registered in the system before returning results.

- [ ] **AC4:** Given that the user selects an environment as a filter, when the query is executed, then the system validates that the environment is registered in the system before returning results.

- [ ] **AC5:** Given that the user selects an instructor as a filter, when the query is executed, then the system validates that the instructor/teacher is registered in the system before returning results.

- [ ] **AC6:** Given that the user selects a person as a filter, when the query is executed, then the system validates that the person is registered in the system before returning results.

### Query execution

- [ ] **AC7:** Given that all selected parameters are valid, when the query is executed, then the system returns the attendance and absence records matching the requested filters.

- [ ] **AC8:** Given that the selected parameters are valid but produce no matching records, when the query is executed, then the system shows a clear message indicating that no attendance records were found for the specified filters.

- [ ] **AC9:** Given that the user combines multiple filters, when the query is executed, then the system applies all filters simultaneously and returns only records matching all criteria.

### Role-based access

- [ ] **AC10:** Given that the user is a student, when the user queries attendance records, then the system only returns the records belonging to that student, unless a broader permission is explicitly granted.

- [ ] **AC11:** Given that the user is an instructor, when the user queries attendance records, then the system only returns records associated with the cohorts, environments, or training blocks assigned to that instructor.

- [ ] **AC12:** Given that the user is an administrator or supervisor, when the user queries attendance records, then the system returns records according to the administrative scope assigned to that role.

- [ ] **AC13:** Given that an unauthorized user attempts to query attendance records outside their role scope, when the request is processed, then the system denies access and records an unauthorized access attempt.

### Results display

- [ ] **AC14:** Given that a query returns results, when the system displays them, then the records include at minimum the following information: user, date, entry time, exit time, attendance status (present, absent, late, justified), cohort, environment, and instructor, where applicable.

- [ ] **AC15:** Given that a query returns a large number of results, when the system displays them, then the results are paginated to maintain performance and usability.

### Performance and audit

- [ ] **AC16:** Given normal network conditions, when the user executes an attendance query, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC17:** Given that the query is executed, when the operation is completed, then the system records an audit event containing the user, applied parameters, date, and time.

---

## Technical notes

- RF 5.1 indicates that the system must validate the following parameters before returning results:
  - User is registered in the system.
  - Cohort/ficha is registered in the system.
  - Environment is registered in the system.
  - Instructor/teacher is registered in the system.
  - Date is valid and applicable.
- The query parameters defined by the SRS are: date, cohort/ficha, environment, instructor, and person.
- Role-based filtering must be enforced on the server side:
  - Students: only their own records.
  - Instructors: records from assigned cohorts/environments/training blocks.
  - Administrators/Supervisors: broader administrative scope.
- The attendance status values should align with the domain model defined in `entities-and-rules.md`:
  - Present.
  - Absent.
  - Late.
  - Justified.
- Pagination should be considered for large result sets.
- The system should consider caching strategies for frequently accessed reports, if applicable.
- This story does not cover report export. That is handled in [HU-REP-001](../HU-REP/user-stories.md#hu-rep-001-report-export).
- This story does not cover dashboards. That is handled in [HU-REP-002](../HU-REP/user-stories.md#hu-rep-002-role-based-analytical-dashboard).
- This story does not cover delay/absence-specific queries by parameters. That is handled in [HU-REP-004](../HU-REP/user-stories.md#hu-rep-004-delay-and-absence-query-by-parameters).
- Attendance data must be protected according to RNF 4, and access must be restricted according to the user's role.

**Responsible service(s):** History service / Attendance query service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/attendance/history` — proposed, with query parameters for date, cohort, environment, instructor, and person.  
- `GET /api/v1/attendance/history/me` — proposed, for students querying their own records.  

**Events generated:**  
- `AttendanceHistoryQueried` — recommended for audit purposes.  
- `UnauthorizedAttendanceQueryAttempt`  

**Required permissions:**  
- Authenticated user.  
- Role-based scope enforcement:
  - Student: own records only.
  - Instructor: assigned cohorts, environments, or training blocks.
  - Administrator/Supervisor: administrative scope.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Query by date tested.
- [ ] Query by cohort tested.
- [ ] Query by environment tested.
- [ ] Query by instructor tested.
- [ ] Query by person tested.
- [ ] Combined filters tested.
- [ ] Empty result scenario tested.
- [ ] Invalid date scenario tested.
- [ ] Non-existent entity scenario tested.
- [ ] Role-based filtering tested for student, instructor, administrator, and supervisor.
- [ ] Unauthorized access attempt tested.
- [ ] Returned records include required fields.
- [ ] Pagination tested for large result sets.
- [ ] Audit event is generated.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 5.1 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 8 |
| Priority | High |
| Target sprint | Sprint 2 |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-IAM-003](../HU-IAM/user-stories.md#hu-iam-003-role-based-permission-assignment): Role-based permission assignment; [HU-ATT-001](../HU-ATT/user-stories.md#hu-att-001-facial-attendance-registration): Facial attendance registration; [HU-ACAD-005](../HU-ACAD/user-stories.md#hu-acad-005-assign-conferencetime-blocks): Assign conference/time blocks |

---

# HU-HIST-002: User change history

## Story

**As** an administrator or supervisor  
**I want** to consult the change history of a specific user  
**So that** I can audit the modifications made to that user's data over time and maintain traceability of administrative actions.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

### Query validation

- [ ] **AC1:** Given that an authenticated administrator or supervisor accesses the user change history module, when the user provides the parameters to identify the target user, then the system validates that the target user exists and is registered in the application.

- [ ] **AC2:** Given that the target user does not exist in the system, when the administrator queries the change history, then the system rejects the operation and shows a clear validation message.

- [ ] **AC3:** Given that the target user exists but has no registered changes, when the administrator queries the change history, then the system shows a clear message indicating that no changes have been recorded for that user.

### History display

- [ ] **AC4:** Given that the target user has registered changes, when the history is displayed, then the system shows each change with at least the following information: date and time of the change, type of change, the user who made the change, previous value (if applicable), and new value (if applicable).

- [ ] **AC5:** Given that the change history contains sensitive data, when the history is displayed, then the system protects sensitive fields (such as passwords or biometric parameters) and does not expose their actual values in the interface or logs.

- [ ] **AC6:** Given that the change history is displayed, when the administrator interacts with the results, then the system supports filtering by target user, date range, type of change, and user who made the change.

### Access control

- [ ] **AC7:** Given that the querying user does not have administrator or supervisor permissions, when the user attempts to access the user change history module, then the system denies access and records an unauthorized access attempt.

- [ ] **AC8:** Given that a regular user, instructor, or student attempts to access another user's change history, when the request is processed, then the system denies access and enforces role-based restrictions.

### Audit integration

- [ ] **AC9:** Given that a user's data is modified in any other module (user registration, role change, activation/deactivation, facial parameter update, etc.), when the modification is saved, then the system records the change in the user change history automatically.

- [ ] **AC10:** Given that the change history is consulted, when the operation is completed, then the system records an audit event containing the querying user, target user, date, and time.

### Performance

- [ ] **AC11:** Given normal network conditions, when the administrator queries the user change history, then the system responds in less than 5 seconds, according to RNF 1.

---

## Technical notes

- RF 5.2 indicates that the system must validate:
  - The querying user is registered in the system.
  - The filtered/target user exists.
- This story depends on audit events being generated by other modules:
  - [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): Login events.
  - [HU-IAM-002](../HU-IAM/user-stories.md#hu-iam-002-user-registration-with-role-assignment): User registration and role assignment events.
  - [HU-IAM-007](../Index_HU.md#planned-stories-pending-documentation): User activation/deactivation events.
  - [HU-IAM-010](../Index_HU.md#planned-stories-pending-documentation): Role change events.
  - [HU-ATT-002](../HU-ATT/user-stories.md#hu-att-002-initial-facial-parameter-enrollment): Facial parameter enrollment events.
  - [HU-CONF-006](../HU-CONF/user-stories.md#hu-conf-006-update-user-facial-parameters): Facial parameter update events.
- The change history must be immutable: once recorded, change entries should not be modified or deleted.
- Sensitive fields such as passwords or biometric parameters must not expose their actual values in the change history. Only the fact that a change occurred should be recorded.
- The change history should support filtering by:
  - Target user.
  - Date range.
  - Type of change.
  - User who made the change.
- Pagination should be considered for users with many changes.
- This story does not cover attendance history. That is handled in [HU-HIST-001](#hu-hist-001-attendance-and-absence-query-by-parameters).
- This story does not cover general audit of all system actions. It focuses specifically on user data changes.
- The team must define the retention period for change history records, considering institutional policies and data protection regulations.

**Responsible service(s):** attendance-service (HIST-001) / identity-service via platform logging (HIST-002).  
**Endpoint(s) implemented:**  
- `GET /api/v1/users/{userId}/change-history` — proposed.  
- `GET /api/v1/users/{userId}/change-history` with query parameters for date range, type of change, and author — proposed.  

**Events generated:**  
- `UserChangeHistoryQueried` — recommended for audit purposes.  
- `UnauthorizedChangeHistoryAccessAttempt`  

**Required permissions:**  
- Administrator or Supervisor.  
- Regular users must not be able to access other users' change history.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Successful change history query tested.
- [ ] Target user does not exist scenario tested.
- [ ] Target user with no changes scenario tested.
- [ ] Change history includes required fields: date, time, type, author, previous value, new value.
- [ ] Change history is automatically recorded when user data is modified in other modules.
- [ ] Unauthorized access attempt tested.
- [ ] Sensitive fields are protected and not exposed.
- [ ] Change history is immutable.
- [ ] Filtering by date range, type of change, and author tested.
- [ ] Audit event is generated for history queries.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 5.2 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 5 |
| Priority | Medium |
| Target sprint | Sprint 3 |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-IAM-003](../HU-IAM/user-stories.md#hu-iam-003-role-based-permission-assignment): Role-based permission assignment; [HU-IAM-002](../HU-IAM/user-stories.md#hu-iam-002-user-registration-with-role-assignment): User registration with role assignment; [HU-IAM-007](../Index_HU.md#planned-stories-pending-documentation): Activate/deactivate users; [HU-IAM-010](../Index_HU.md#planned-stories-pending-documentation): Role change |

---

