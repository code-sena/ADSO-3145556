# HU-REP-001: Report export

## Story

**As** an administrator, supervisor, instructor, or authorized user  
**I want** to export attendance reports including assistances, absences, delays, cohorts/groups, instructors, and environments  
**So that** I can obtain structured files for institutional analysis, academic follow-up, and administrative record-keeping according to my role.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that an authenticated user accesses the report export module, when the system displays the available report types, then the user can see the following options: assistances, absences, delays, cohorts/groups, instructors/teachers, and environments/classrooms.

- [ ] **AC2:** Given that the user selects a report type, when the export request is submitted, then the system validates that the user's role is authorized to export that specific report type before processing the request.

- [ ] **AC3:** Given that the user's role is authorized for the selected report type, when the export is processed, then the system generates the report file and provides it to the user for download.

- [ ] **AC4:** Given that the user's role is not authorized for the selected report type, when the export is requested, then the system denies the request, shows a clear permission error message, and records an unauthorized access attempt.

- [ ] **AC5:** Given that the report generation completes successfully, when the file is downloaded, then the exported data matches the current authorized scope of the user and includes the report type, generation date, and user who requested the export.

- [ ] **AC6:** Given that the report contains no data matching the user's scope, when the export is requested, then the system shows a clear message indicating that no data is available for export under the current criteria.

- [ ] **AC7:** Given that a student attempts to export reports outside their personal scope, when the request is processed, then the system denies the request and enforces role-based restrictions.

- [ ] **AC8:** Given normal network conditions, when the user submits a standard export request, then the system responds in less than 10 seconds for typical data volumes.

- [ ] **AC9:** Given that the export request is processed, when the operation is completed, then the system records an audit event containing the user, report type, date, time, and result.

---

## Technical notes

- RF 7.1 indicates that the system must validate the user's role and associate it with the export types they are allowed to request.
- The report types defined by the SRS are:
  - Attendances (orig. ES "Asistencias").
  - Absences (orig. ES "Faltas").
  - Delays (orig. ES "Retardos").
  - Cohorts/groups (orig. ES "Fichas/Grupos").
  - Instructors/teachers (orig. ES "Instructores/Profesores").
  - Environments/classrooms (orig. ES "Ambientes/Salón").
- Role-based access matrix for reports should be defined by the team. Proposed initial matrix:
  - Student: personal attendance history only.
  - Instructor: assistances, delays, and absences for assigned cohorts.
  - Administrator/Supervisor: all report types.
- Export formats must be defined by the team. Proposed initial formats:
  - CSV for data analysis.
  - PDF for institutional documents.
- For large datasets, the system should consider:
  - Pagination or streaming generation.
  - Asynchronous export with notification when ready.
  - A maximum export size limit.
- The exported file should include:
  - Report type.
  - Generation date and time.
  - User who requested the export.
  - Applied filters, if any.
- Exported data must respect the user's role scope and must not include unauthorized records.
- This story does not cover date-range filters. That is handled in [HU-REP-003](#hu-rep-003-date-range-reports).
- This story does not cover the analytical dashboard. That is handled in [HU-REP-002](#hu-rep-002-role-based-analytical-dashboard).
- This story does not cover delay/absence-specific queries by parameters. That is handled in [HU-REP-004](#hu-rep-004-delay-and-absence-query-by-parameters).
- Performance for large exports may exceed the standard 5-second response time. The team should define whether asynchronous export is required.
- Sensitive data included in exports must be protected according to RNF 4.

**Responsible service(s):** Report service / Export service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/reports/types` — proposed, returns report types available for the user's role.  
- `POST /api/v1/reports/export` — proposed, generates and returns the report file.  
- `GET /api/v1/reports/export/{exportId}` — proposed, retrieves an asynchronous export result.  

**Events generated:**  
- `ReportExportRequested`  
- `ReportExportCompleted`  
- `ReportExportFailed`  
- `UnauthorizedReportAccessAttempt`  

**Required permissions:**  
- Authenticated user.  
- Role-based scope enforcement:
  - Student: personal records only.
  - Instructor: assigned cohorts/environments.
  - Administrator/Supervisor: full institutional scope.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] All six report types are available according to role permissions.
- [ ] Role-based validation tested for each report type.
- [ ] Successful export tested for authorized roles.
- [ ] Unauthorized export attempt tested and denied.
- [ ] Empty result scenario tested.
- [ ] Student scope restriction tested.
- [ ] Exported file includes report type, generation date, and requesting user.
- [ ] Audit event is generated for export requests.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated for typical data volumes.
- [ ] Large dataset behavior is defined and tested.
- [ ] Traceability to RF 7.1 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 8 |
| Priority | Medium |
| Target sprint | Sprint 3 |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-IAM-003](../HU-IAM/user-stories.md#hu-iam-003-role-based-permission-assignment): Role-based permission assignment; [HU-HIST-001](../HU-HIST/user-stories.md#hu-hist-001-attendance-and-absence-query-by-parameters): Attendance and absence query by parameters |

---

# HU-REP-002: Role-based analytical dashboard

## Story

**As** an administrator, supervisor, instructor, or student  
**I want** to access an analytical dashboard corresponding to my role  
**So that** I can visualize relevant attendance indicators and make timely decisions according to my responsibilities.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that an authenticated user accesses the dashboard module, when the system processes the request, then the system validates the user's role and displays the dashboard corresponding to that role.

- [ ] **AC2:** Given that the user is a student, when the dashboard is displayed, then the system shows personal attendance information such as attendance percentage, absences, delays, and justification status.

- [ ] **AC3:** Given that the user is an instructor, when the dashboard is displayed, then the system shows attendance information for the cohorts, environments, or training blocks assigned to that instructor.

- [ ] **AC4:** Given that the user is an administrator or supervisor, when the dashboard is displayed, then the system shows institutional or administrative-scope attendance indicators.

- [ ] **AC5:** Given that the user's role does not have an associated dashboard, when the user accesses the dashboard module, then the system shows a clear message indicating that no dashboard is available for their role.

- [ ] **AC6:** Given that the dashboard is displayed, when the user interacts with the visual components, then the information is updated according to the user's scope without exposing unauthorized data.

- [ ] **AC7:** Given that an unauthorized user attempts to access a dashboard outside their role, when the request is processed, then the system denies access and records an unauthorized access attempt.

- [ ] **AC8:** Given normal network conditions, when the user accesses the dashboard, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC9:** Given that the dashboard is accessed, when the operation is completed, then the system records an audit event containing the user, role, date, and time.

---

## Technical notes

- RF 7.2 indicates that the system must validate the user's role and associate it with the corresponding dashboard.
- The dashboard content must be defined per role. Proposed initial content:
  - **Student dashboard:**
    - Attendance percentage.
    - Total absences.
    - Total delays.
    - Justification status.
    - Recent attendance records.
  - **Instructor dashboard:**
    - Attendance summary for assigned cohorts.
    - Students with most absences or delays.
    - Recent attendance records.
    - Pending justifications, if applicable.
  - **Administrator/Supervisor dashboard:**
    - Institutional attendance summary.
    - Attendance by cohort.
    - Attendance by environment.
    - Users with most absences or delays.
    - System alerts, if applicable.
- The dashboard should be responsive and compatible with web and mobile views, according to RNF 3.
- The dashboard should consider:
  - Real-time or near-real-time updates, if feasible.
  - Clear visual indicators (charts, counters, status badges).
  - Accessibility best practices, according to RNF 2.
- Role-based filtering must be enforced on the server side. Client-side filtering alone is not sufficient.
- The dashboard should use cached or aggregated data where possible to improve performance.
- This story does not cover report export. That is handled in [HU-REP-001](#hu-rep-001-report-export).
- This story does not cover date-range reports. That is handled in [HU-REP-003](#hu-rep-003-date-range-reports).
- This story does not cover delay/absence-specific queries. That is handled in [HU-REP-004](#hu-rep-004-delay-and-absence-query-by-parameters).
- The team must define the specific KPIs and visual components for each role.

**Responsible service(s):** Report service / Dashboard service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/dashboard` — proposed, returns dashboard data based on user role.  
- `GET /api/v1/dashboard/student` — proposed.  
- `GET /api/v1/dashboard/instructor` — proposed.  
- `GET /api/v1/dashboard/admin` — proposed.  

**Events generated:**  
- `DashboardAccessed`  
- `UnauthorizedDashboardAccessAttempt`  

**Required permissions:**  
- Authenticated user.  
- Role-based scope enforcement:
  - Student: personal dashboard.
  - Instructor: assigned cohorts/environments dashboard.
  - Administrator/Supervisor: institutional dashboard.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Dashboard is displayed correctly for each role.
- [ ] Student dashboard shows personal attendance information.
- [ ] Instructor dashboard shows assigned cohorts information.
- [ ] Administrator/Supervisor dashboard shows institutional indicators.
- [ ] Role without dashboard scenario tested.
- [ ] Unauthorized dashboard access attempt tested.
- [ ] Dashboard data respects user scope.
- [ ] Dashboard is responsive on web and mobile views.
- [ ] Audit event is generated for dashboard access.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 7.2 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 8 |
| Priority | High |
| Target sprint | Sprint 3 |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-IAM-003](../HU-IAM/user-stories.md#hu-iam-003-role-based-permission-assignment): Role-based permission assignment; [HU-HIST-001](../HU-HIST/user-stories.md#hu-hist-001-attendance-and-absence-query-by-parameters): Attendance and absence query by parameters; [HU-ATT-001](../HU-ATT/user-stories.md#hu-att-001-facial-attendance-registration): Facial attendance registration |

---

# HU-REP-003: Date-range reports

## Story

**As** an administrator, supervisor, instructor, or authorized user  
**I want** to export reports filtered by a date range  
**So that** I can analyze attendance information for specific periods such as weeks, months, or academic terms according to my role.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that an authenticated user accesses the date-range report module, when the user selects start date, end date, and report type, then the system validates the input parameters before processing the request.

- [ ] **AC2:** Given that the selected date range is valid and the user's role is authorized, when the export request is submitted, then the system generates the report filtered by the specified date range and provides it for download.

- [ ] **AC3:** Given that the start date is later than the end date, when the request is submitted, then the system rejects the operation and shows a clear validation message.

- [ ] **AC4:** Given that the selected date range exceeds the maximum allowed period, when the request is submitted, then the system rejects the operation and shows a clear message indicating the maximum allowed period.

- [ ] **AC5:** Given that the user's role is not authorized for the selected report type, when the export is requested, then the system denies the request, shows a permission error, and records an unauthorized access attempt.

- [ ] **AC6:** Given that no data exists within the selected date range, when the export is requested, then the system shows a clear message indicating that no data is available for the specified period.

- [ ] **AC7:** Given that the date-range report is generated, when the file is downloaded, then the exported data respects the user's role scope and includes the applied date range, report type, generation date, and requesting user.

- [ ] **AC8:** Given normal network conditions, when the user submits a standard date-range export request, then the system responds in less than 10 seconds for typical data volumes.

- [ ] **AC9:** Given that the date-range export is processed, when the operation is completed, then the system records an audit event containing the user, report type, date range, date, time, and result.

---

## Technical notes

- RF 7.3 indicates that the system must validate the user's role and associate it with the export types they are allowed to request, similar to RF 7.1 but with date-range filtering.
- The report types available for date-range export should align with [HU-REP-001](#hu-rep-001-report-export):
  - Attendances (orig. ES "Asistencias").
  - Absences (orig. ES "Faltas").
  - Delays (orig. ES "Retardos").
  - Cohorts/groups (orig. ES "Fichas/Grupos").
  - Instructors/teachers (orig. ES "Instructores/Profesores").
  - Environments/classrooms (orig. ES "Ambientes/Salón").
- The team must define:
  - Maximum allowed date range (for example, 1 year).
  - Whether date-range export supports combined filters (cohort, environment, instructor).
  - Whether asynchronous export is required for large date ranges.
- The system should validate:
  - Start date is not later than end date.
  - Date range does not exceed the maximum allowed period.
  - Dates are valid and applicable to the attendance control context.
- The exported file should include:
  - Applied date range.
  - Report type.
  - Generation date and time.
  - User who requested the export.
- This story builds on [HU-REP-001](#hu-rep-001-report-export). The team should consider reusing the export generation logic.
- This story does not cover the analytical dashboard. That is handled in [HU-REP-002](#hu-rep-002-role-based-analytical-dashboard).
- This story does not cover delay/absence-specific queries by parameters. That is handled in [HU-REP-004](#hu-rep-004-delay-and-absence-query-by-parameters).
- Performance for large date ranges may require asynchronous export with notification when ready.
- Sensitive data included in exports must be protected according to RNF 4.

**Responsible service(s):** Report service / Export service, proposed.  
**Endpoint(s) implemented:**  
- `POST /api/v1/reports/export/by-date-range` — proposed.  
- `GET /api/v1/reports/export/{exportId}` — proposed, retrieves an asynchronous export result.  

**Events generated:**  
- `DateRangeReportRequested`  
- `DateRangeReportCompleted`  
- `DateRangeReportFailed`  
- `UnauthorizedDateRangeReportAccessAttempt`  

**Required permissions:**  
- Authenticated user.  
- Role-based scope enforcement:
  - Student: personal records only.
  - Instructor: assigned cohorts/environments.
  - Administrator/Supervisor: full institutional scope.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Successful date-range export tested.
- [ ] Start date later than end date scenario tested.
- [ ] Maximum date range exceeded scenario tested.
- [ ] Unauthorized report type scenario tested.
- [ ] Empty date range result scenario tested.
- [ ] Role-based scope enforcement tested.
- [ ] Exported file includes applied date range, report type, and requesting user.
- [ ] Audit event is generated for date-range export requests.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated for typical data volumes.
- [ ] Large date range behavior is defined and tested.
- [ ] Traceability to RF 7.3 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 5 |
| Priority | Medium |
| Target sprint | Sprint 3 |
| Dependencies | [HU-REP-001](#hu-rep-001-report-export): Report export; [HU-IAM-003](../HU-IAM/user-stories.md#hu-iam-003-role-based-permission-assignment): Role-based permission assignment; [HU-HIST-001](../HU-HIST/user-stories.md#hu-hist-001-attendance-and-absence-query-by-parameters): Attendance and absence query by parameters |

---

# HU-REP-004: Delay and absence query by parameters

## Story

**As** an administrator, supervisor, instructor, or authorized user  
**I want** to query delays and absences using parameters such as user, date, cohort/group, environment/classroom, and instructor  
**So that** I can identify attendance issues and take timely corrective actions according to my role.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that an authenticated user accesses the delay/absence query module, when the user selects one or more parameters (user, date, cohort/group, environment/classroom, instructor), then the system validates the parameters before processing the query.

- [ ] **AC2:** Given that the selected parameters are valid and the user's role is authorized, when the query is executed, then the system returns the delays and absences matching the requested filters.

- [ ] **AC3:** Given that the user's role is not authorized for the requested query scope, when the query is executed, then the system denies access, shows a permission error, and records an unauthorized access attempt.

- [ ] **AC4:** Given that the selected parameters produce no matching records, when the query is executed, then the system shows a clear message indicating that no delays or absences were found for the specified filters.

- [ ] **AC5:** Given that a student queries delays and absences, when the request is processed, then the system only returns records belonging to that student.

- [ ] **AC6:** Given that an instructor queries delays and absences, when the request is processed, then the system only returns records associated with the cohorts, environments, or training blocks assigned to that instructor.

- [ ] **AC7:** Given that the query returns results, when the system displays them, then each record includes at minimum the following information: user, date, type (delay or absence), cohort, environment, instructor, and justification status where applicable.

- [ ] **AC8:** Given normal network conditions, when the user executes a delay/absence query, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC9:** Given that the query is executed, when the operation is completed, then the system records an audit event containing the user, applied parameters, date, and time.

---

## Technical notes

- RF 7.4 indicates that the system must validate the user's role and associate it with the delay/absence queries they are allowed to request.
- The query parameters defined by the SRS are:
  - User references.
  - Dates.
  - Cohorts/groups.
  - Environments/classrooms.
  - Instructors/teachers.
- The query should support combined filters for more precise results.
- Role-based filtering must be enforced on the server side:
  - Students: only their own records.
  - Instructors: records from assigned cohorts/environments/training blocks.
  - Administrators/Supervisors: broader administrative scope.
- The query results should include justification status to help users identify which delays/absences have been justified.
- Pagination should be considered for large result sets.
- The team should consider:
  - Sorting options (by date, user, type).
  - Export option for query results, if applicable.
  - Integration with [HU-REP-001](#hu-rep-001-report-export) for exporting query results.
- This story does not cover the analytical dashboard. That is handled in [HU-REP-002](#hu-rep-002-role-based-analytical-dashboard).
- This story does not cover general attendance/absence history queries. That is handled in [HU-HIST-001](../HU-HIST/user-stories.md#hu-hist-001-attendance-and-absence-query-by-parameters).
- This story focuses specifically on delays and absences, not on all attendance records.
- Attendance data must be protected according to RNF 4, and access must be restricted according to the user's role.

**Responsible service(s):** Report service / Delay-Absence query service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/reports/delays-absences` — proposed, with query parameters for user, date, cohort, environment, and instructor.  
- `GET /api/v1/reports/delays-absences/me` — proposed, for students querying their own records.  

**Events generated:**  
- `DelayAbsenceQueryExecuted` — recommended for audit purposes.  
- `UnauthorizedDelayAbsenceQueryAttempt`  

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

- [ ] Query by user tested.
- [ ] Query by date tested.
- [ ] Query by cohort/group tested.
- [ ] Query by environment/classroom tested.
- [ ] Query by instructor tested.
- [ ] Combined filters tested.
- [ ] Empty result scenario tested.
- [ ] Role-based filtering tested for student, instructor, administrator, and supervisor.
- [ ] Unauthorized access attempt tested.
- [ ] Returned records include required fields: user, date, type, cohort, environment, instructor, justification status.
- [ ] Pagination tested for large result sets.
- [ ] Audit event is generated.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 7.4 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 5 |
| Priority | High |
| Target sprint | Sprint 3 |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-IAM-003](../HU-IAM/user-stories.md#hu-iam-003-role-based-permission-assignment): Role-based permission assignment; [HU-HIST-001](../HU-HIST/user-stories.md#hu-hist-001-attendance-and-absence-query-by-parameters): Attendance and absence query by parameters; [HU-CONF-008](../HU-CONF/user-stories.md#hu-conf-008-delay-and-justification-parametrization): Delay and justification parametrization |