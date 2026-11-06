# HU-ACAD-001: Register environments/classrooms

## Story

**As** an administrator or supervisor  
**I want** to register environments or classrooms  
**So that** attendance records can be associated with the correct training space where students attend class.

---

## Acceptance criteria

> Format: “Given [context/initial state], when [user action], then [expected and verifiable result].”

- [ ] **AC1:** Given that an administrator or supervisor is authenticated in the system, when the user submits valid environment identifiers, then the system validates the data, registers the environment/classroom, stores the information securely, and shows a confirmation message.

- [ ] **AC2:** Given that the environment data is incomplete or invalid, when the registration is submitted, then the system rejects the operation and shows specific validation messages.

- [ ] **AC3:** Given that an environment with the same unique identifier already exists, when the administrator attempts to register it again, then the system prevents duplicate registration and shows a clear error message.

- [ ] **AC4:** Given that an environment is successfully registered, when the system requires an environment for attendance registration or reporting, then the registered environment is available for selection.

- [ ] **AC5:** Given normal network conditions, when the administrator registers an environment, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC6:** Given that an environment is registered, when the operation is completed, then the system records an audit event containing the action, user, date, and time.

---

## Technical notes

- The SRS refers to environments as “ambientes/salones”, which are the physical or virtual spaces where attendance is recorded.
- Environment data must be validated before persistence, according to RF 2.1 acceptance criteria.
- The information must be stored securely, according to RF 2.1 and RNF 4.
- This story does not include assignment of schedules, cohorts, or instructors. Those relationships are handled in other ACAD stories.
- The environment entity will be used later by attendance registration, reports, and dashboards.
- The user interface should allow consulting registered environments after creation.
- Auditability is recommended for administrative changes.

**Responsible service(s):** Academic Structure service, proposed.  
**Endpoint(s) implemented:**  
- `POST /api/v1/environments` — proposed.  
- `GET /api/v1/environments` — proposed.  

**Events generated:**  
- `EnvironmentRegistered`  
- `EnvironmentRegistrationFailed`  

**Required permissions:**  
- Administrator or Supervisor.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Successful environment registration tested.
- [ ] Invalid or incomplete environment data tested.
- [ ] Duplicate environment scenario tested.
- [ ] Registered environment is available for selection in academic flows.
- [ ] Confirmation and error messages are clear.
- [ ] Audit event is generated.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Data is stored securely.
- [ ] Traceability to RF 2.1 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 3 |
| Priority | High |
| Target sprint | Sprint 1 / MVP |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-IAM-003](../HU-IAM/user-stories.md#hu-iam-003-role-based-permission-assignment): Role-based permission assignment |

---

# HU-ACAD-002: Register cohorts/courses

## Story

**As** an administrator or supervisor  
**I want** to register cohorts or courses, known as “fichas” in the SENA context  
**So that** student groups can be associated with attendance control, responsible instructors, and training schedules.

---

## Acceptance criteria

> Format: “Given [context/initial state], when [user action], then [expected and verifiable result].”

- [ ] **AC1:** Given that an administrator or supervisor is authenticated in the system, when the user submits valid cohort/course identifiers, then the system validates the data, registers the cohort/course associated with attendance control, stores the information securely, and shows a confirmation message.

- [ ] **AC2:** Given that the cohort/course data is incomplete or invalid, when the registration is submitted, then the system rejects the operation and shows specific validation messages.

- [ ] **AC3:** Given that a cohort/course with the same unique identifier already exists, when the administrator attempts to register it again, then the system prevents duplicate registration and shows a clear error message.

- [ ] **AC4:** Given that a cohort/course is successfully registered, when attendance registration or academic assignment is performed, then the system can display or use the cohort/course associated with the corresponding user or training group, according to RF 2.2.

- [ ] **AC5:** Given that a cohort/course is successfully registered, when the administrator assigns responsible instructors or schedules, then the cohort/course is available for those relationships.

- [ ] **AC6:** Given normal network conditions, when the administrator registers a cohort/course, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC7:** Given that a cohort/course is registered, when the operation is completed, then the system records an audit event containing the action, user, date, and time.

---

## Technical notes

- The SRS uses the terms “fichas/cursos”. The team must define whether a “ficha” is treated as a cohort, group, or course in the final domain model.
- This story covers the registration of the group/cohort, not the assignment of students, instructors, or schedules.
- The registered cohort/course must be available for later assignment of responsible users, according to RF 2.3.1.
- The cohort/course entity will be used by attendance registration, history queries, justifications, and reports.
- Validation rules should include required identifiers and institutional context.
- Sensitive data is not expected in this entity, but administrative changes should be auditable.
- The system should store data securely, according to RF 2.2 and RNF 4.

**Responsible service(s):** Academic Structure service, proposed.  
**Endpoint(s) implemented:**  
- `POST /api/v1/cohorts` — proposed, representing SENA “fichas”.  
- `GET /api/v1/cohorts` — proposed.  

**Events generated:**  
- `CohortRegistered`  
- `CohortRegistrationFailed`  

**Required permissions:**  
- Administrator or Supervisor.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Successful cohort/course registration tested.
- [ ] Invalid or incomplete cohort/course data tested.
- [ ] Duplicate cohort/course scenario tested.
- [ ] Registered cohort/course is available for instructor assignment.
- [ ] Registered cohort/course is available for attendance-related flows.
- [ ] Confirmation and error messages are clear.
- [ ] Audit event is generated.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Data is stored securely.
- [ ] Traceability to RF 2.2 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 3 |
| Priority | High |
| Target sprint | Sprint 1 / MVP |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-IAM-003](../HU-IAM/user-stories.md#hu-iam-003-role-based-permission-assignment): Role-based permission assignment; [HU-ACAD-001](#hu-acad-001-register-environmentsclassrooms) recommended for environment context |

---

# HU-ACAD-003: Assign responsible instructors

## Story

**As** an administrator or supervisor  
**I want** to assign a teacher/instructor as the responsible user for a cohort or training block  
**So that** attendance records are linked to the correct instructor and instructors can view the groups assigned to them.

---

## Acceptance criteria

> Format: “Given [context/initial state], when [user action], then [expected and verifiable result].”

- [ ] **AC1:** Given that an administrator or supervisor has logged into FaceAttend EDU and users are registered in the system, when the user accesses the responsible assignment function, then the system shows the list of users associated with the administrator and the list of users of type teacher/instructor, according to RF 2.3.

- [ ] **AC2:** Given that a valid teacher/instructor and a registered cohort/course are selected, when the administrator assigns the responsible user, then the system associates the instructor with the cohort/course or training block and shows a confirmation message.

- [ ] **AC3:** Given that the selected user is not a valid user or is not of type teacher/instructor, when the administrator attempts the assignment, then the system rejects the operation and shows a validation error.

- [ ] **AC4:** Given that the cohort/course does not exist in the system, when the administrator attempts to assign a responsible user, then the system rejects the operation and shows a validation error.

- [ ] **AC5:** Given that a responsible instructor is successfully assigned, when attendance is registered for the corresponding cohort or training block, then the system can identify the instructor responsible for that attendance record.

- [ ] **AC6:** Given normal network conditions, when the administrator assigns a responsible instructor, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC7:** Given that a responsible instructor is assigned or updated, when the operation is completed, then the system records an audit event containing the action, administrator, instructor, cohort/course, date, and time.

---

## Technical notes

- The SRS describes this requirement as obtaining the teacher/instructor responsible for the training block corresponding to attendance registration.
- The assignment must require prior login and prior registration in FaceAttend EDU, according to RF 2.3 acceptance criteria.
- The system must show both:
  - Users associated with the administrator.
  - Users of type teacher/instructor.
- This story does not yet include assignment of time blocks or schedules. That is covered in [HU-ACAD-005](#hu-acad-005-assign-conferencetime-blocks).
- The responsible instructor relationship will be used by attendance, history, reports, and dashboards.
- The system should prevent assigning responsibility to users without an instructor/teacher role.
- Auditability is recommended because this assignment affects academic visibility and reporting permissions.
- The team should define whether a cohort/course can have one or multiple responsible instructors.

**Responsible service(s):** Academic Structure service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/users?role=instructor` — proposed.  
- `POST /api/v1/cohorts/{cohortId}/responsible` — proposed.  
- `GET /api/v1/cohorts/{cohortId}/responsible` — proposed.  

**Events generated:**  
- `ResponsibleAssigned`  
- `ResponsibleAssignmentFailed`  

**Required permissions:**  
- Administrator or Supervisor.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Administrator login tested before assignment.
- [ ] Instructor user list display tested.
- [ ] Users associated with the administrator display tested.
- [ ] Successful responsible instructor assignment tested.
- [ ] Invalid instructor scenario tested.
- [ ] Non-existent cohort/course scenario tested.
- [ ] Assigned instructor is correctly linked to the cohort/course or training block.
- [ ] Audit event is generated.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 2.3 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 5 |
| Priority | High |
| Target sprint | Sprint 1 / MVP |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-IAM-002](../HU-IAM/user-stories.md#hu-iam-002-user-registration-with-role-assignment): User registration with role assignment; [HU-ACAD-002](#hu-acad-002-register-cohortscourses): Register cohorts/courses |

---

# HU-ACAD-004: Assign cohorts to responsible users

## Story

**As** an administrator or supervisor  
**I want** to assign registered cohorts/courses, known as “fichas”, to a valid responsible user  
**So that** administrative users, instructors, and responsible users can view and manage the cohorts assigned to their academic responsibility.

---

## Acceptance criteria

> Format: “Given [context/initial state], when [user action], then [expected and verifiable result].”

- [ ] **AC1:** Given that an administrator or supervisor is authenticated in the system, when the user selects a valid responsible user and an existing cohort/course, then the system associates the cohort/course with the responsible user and shows a confirmation message.

- [ ] **AC2:** Given that the selected responsible user is not a valid registered user, when the assignment is submitted, then the system rejects the operation and shows a validation error.

- [ ] **AC3:** Given that the selected cohort/course does not exist in the system, when the assignment is submitted, then the system rejects the operation and shows a validation error.

- [ ] **AC4:** Given that a responsible user has assigned cohorts/courses, when the user requests the list of cohorts under their responsibility, then the system displays the cohorts corresponding to their assigned responsibility, according to RF 2.3.1.

- [ ] **AC5:** Given that an administrative user, supervisor, administrator, or teacher/instructor accesses the cohort assignment module, when the system loads the information, then the user sees only the cohorts allowed by their role.

- [ ] **AC6:** Given normal network conditions, when the administrator assigns or consults cohorts for a responsible user, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC7:** Given that a cohort/course is assigned to a responsible user, when the operation is completed, then the system records an audit event containing the action, user, responsible user, cohort/course, date, and time.

---

## Technical notes

- The SRS indicates that administrative users (Supervisor, Administrator, and Teacher/Instructor) must be able to obtain the list of cohorts corresponding to their work.
- The system must validate that the responsible user is valid and that the cohort/course exists, according to RF 2.3.1 acceptance criteria.
- This story complements [HU-ACAD-003](#hu-acad-003-assign-responsible-instructors) by formalizing the visibility and association of cohorts assigned to a responsible user.
- The responsible-cohort relationship will be used by attendance registration, history, reports, dashboards, and instructor permissions.
- The system should define whether a responsible user can have one or multiple cohorts assigned.
- The system should prevent assigning inactive users as responsible users.
- Role-based access must be enforced on the server side.
- Auditability is recommended because this assignment affects academic visibility and reporting permissions.

**Responsible service(s):** Academic Structure service, proposed.  
**Endpoint(s) implemented:**  
- `POST /api/v1/responsibles/{responsibleId}/cohorts` — proposed.  
- `GET /api/v1/responsibles/{responsibleId}/cohorts` — proposed.  
- `GET /api/v1/users/me/cohorts` — proposed.  

**Events generated:**  
- `CohortAssignedToResponsible`  
- `CohortAssignmentFailed`  

**Required permissions:**  
- Administrator or Supervisor for assigning cohorts to responsible users.  
- Teacher/Instructor may consult their own assigned cohorts.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Successful cohort assignment to a responsible user tested.
- [ ] Invalid responsible user scenario tested.
- [ ] Non-existent cohort/course scenario tested.
- [ ] Consultation of assigned cohorts tested.
- [ ] Role-based visibility tested for administrator, supervisor, and instructor.
- [ ] Audit event is generated.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 2.3.1 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 5 |
| Priority | High |
| Target sprint | Sprint 1 / MVP |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-ACAD-002](#hu-acad-002-register-cohortscourses): Register cohorts/courses; [HU-ACAD-003](#hu-acad-003-assign-responsible-instructors): Assign responsible instructors |

---
conference
# HU-ACAD-005: Assign conference/time blocks

## Story

**As** an administrator or supervisor  
**I want** to assign  or time blocks to a registered cohort with a valid responsible instructor  
**So that** the system knows the schedule in which students must attend and register their attendance.

---

## Acceptance criteria

> Format: “Given [context/initial state], when [user action], then [expected and verifiable result].”

- [ ] **AC1:** Given that an administrator or supervisor is authenticated in the system, when the user assigns a time block to a registered cohort with a valid responsible instructor, then the system associates the time block with the cohort and responsible user and shows a confirmation message.

- [ ] **AC2:** Given that the selected responsible user is not valid or is not registered, when the time block assignment is submitted, then the system rejects the operation and shows a validation error.

- [ ] **AC3:** Given that the proposed time block overlaps with another time block already assigned, when the assignment is submitted, then the system rejects the operation and shows a clear conflict message.

- [ ] **AC4:** Given that the cohort is not available for the proposed time block, when the assignment is submitted, then the system rejects the operation and shows a validation error.

- [ ] **AC5:** Given that a jornada/time block is successfully assigned, when attendance registration occurs for that cohort, then the system can identify the expected time in which students must register entry and exit.

- [ ] **AC6:** Given that users need to consult schedules, when an administrator, instructor, or student requests the corresponding schedule, then the system displays the schedules where users must attend and register, according to their role.

- [ ] **AC7:** Given normal network conditions, when the administrator assigns or consults conference/time blocks, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC8:** Given that a jornada/time block is assigned, updated, or rejected, when the operation is completed, then the system records an audit event containing the action, user, cohort, responsible user, time block, date, and time.

---

## Technical notes

- The SRS refers to “asignar conference” and “tiempo bloque de laburo”. For consistency, this story uses “jornada” or “time block” as the period during which a cohort must attend with a responsible instructor.
- The system must validate that:
  - The responsible user is valid.
  - The time block does not overlap with another already assigned block.
  - The cohort has availability for the assigned time block.
- This story is essential for attendance validation because [HU-ATT-001](../HU-ATT/user-stories.md#hu-att-001-facial-attendance-registration) depends on knowing whether entry and exit times are appropriate for the assigned block.
- The schedule/jornada relationship will be used by attendance registration, late detection, absence detection, history, reports, and dashboards.
- The system should prevent duplicate or conflicting schedules for the same cohort, responsible user, environment, or time period.
- The team must define whether overlap validation applies only to the same cohort, to the same responsible instructor, to the same environment, or to a combination of these.
- Role-based access must be enforced:
  - Administrators and supervisors can assign and consult schedules.
  - Instructors can consult schedules assigned to them.
  - Students can consult schedules where they are registered, if applicable.

**Responsible service(s):** Academic Structure service / Scheduling service, proposed.  
**Endpoint(s) implemented:**  
- `POST /api/v1/schedules` — proposed.  
- `GET /api/v1/schedules` — proposed.  
- `GET /api/v1/cohorts/{cohortId}/schedule` — proposed.  
- `GET /api/v1/users/me/schedule` — proposed.  

**Events generated:**  
- `ScheduleAssigned`  
- `ScheduleAssignmentFailed`  
- `ScheduleConflictDetected`  

**Required permissions:**  
- Administrator or Supervisor for assigning conference/time blocks.
- Teacher/Instructor may consult schedules assigned to them.  
- Student may consult allowed schedules if applicable.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Successful jornada/time block assignment tested.
- [ ] Invalid responsible user scenario tested.
- [ ] Time block overlap scenario tested.
- [ ] Cohort unavailable scenario tested.
- [ ] Assigned time block is correctly associated with cohort and responsible user.
- [ ] Schedule consultation tested for administrator, instructor, and student where applicable.
- [ ] Attendance module can use the assigned time block for entry/exit validation.
- [ ] Audit event is generated.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 2.3.2 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 8 |
| Priority | High |
| Target sprint | Sprint 1 / MVP |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-ACAD-003](#hu-acad-003-assign-responsible-instructors): Assign responsible instructors; [HU-ACAD-004](#hu-acad-004-assign-cohorts-to-responsible-users): Assign cohorts to responsible users |