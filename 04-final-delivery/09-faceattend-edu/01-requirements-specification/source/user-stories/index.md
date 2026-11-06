# User Story Index — FaceAttendEDU

This document serves as a centralized entry point for all user stories in the system. Each link directs you directly to the file where the detailed specification of the functionality is located.

## User Story List by Module

### Academic Management (HU-ACAD)
- [Academic User Stories](HU-ACAD/user-stories.md)
  - [HU-ACAD-001: Register environments/classrooms](HU-ACAD/user-stories.md#hu-acad-001-register-environmentsclassrooms)
  - [HU-ACAD-002: Register cohorts/courses](HU-ACAD/user-stories.md#hu-acad-002-register-cohortscourses)
  - [HU-ACAD-003: Assign responsible instructors](HU-ACAD/user-stories.md#hu-acad-003-assign-responsible-instructors)
  - [HU-ACAD-004: Assign cohorts to responsible users](HU-ACAD/user-stories.md#hu-acad-004-assign-cohorts-to-responsible-users)
  - [HU-ACAD-005: Assign conference/time blocks](HU-ACAD/user-stories.md#hu-acad-005-assign-conferencetime-blocks)

### Identity and Access (HU-IAM)
- [Identity and Access User Stories](HU-IAM/user-stories.md)
  - [HU-IAM-001: User login](HU-IAM/user-stories.md#hu-iam-001-user-login)
  - [HU-IAM-002: User registration with role assignment](HU-IAM/user-stories.md#hu-iam-002-user-registration-with-role-assignment)
  - [HU-IAM-003: Role-based permission assignment](HU-IAM/user-stories.md#hu-iam-003-role-based-permission-assignment)

### Attendance (HU-ATT)
- [Attendance User Stories](HU-ATT/user-stories.md)
  - [HU-ATT-001: Facial attendance registration](HU-ATT/user-stories.md#hu-att-001-facial-attendance-registration)
  - [HU-ATT-002: Initial facial parameter enrollment](HU-ATT/user-stories.md#hu-att-002-initial-facial-parameter-enrollment)
  - [HU-ATT-003: Alternative registration for registered users with facial scan failure](HU-ATT/user-stories.md#hu-att-003-alternative-registration-for-registered-users-with-facial-scan-failure)
  - [HU-ATT-004: Entry control for unregistered or NN users](HU-ATT/user-stories.md#hu-att-004-entry-control-for-unregistered-or-nn-users)

### Justifications (HU-JUS)
- [Justification User Stories](HU-JUS/user-stories.md)
  - [HU-JUS-001: Justification support upload](HU-JUS/user-stories.md#hu-jus-001-justification-support-upload)
  - [HU-JUS-002: Justification approval and rejection](HU-JUS/user-stories.md#hu-jus-002-justification-approval-and-rejection)
  - [HU-JUS-003: Automatic notification of justification result](HU-JUS/user-stories.md#hu-jus-003-automatic-notification-of-justification-result)

### Configuration (HU-CONF)
- [Configuration User Stories](HU-CONF/user-stories.md)
  - [HU-CONF-001: Configure system alerts](HU-CONF/user-stories.md#hu-conf-001-configure-system-alerts)
  - [HU-CONF-002: Enable or disable alerts](HU-CONF/user-stories.md#hu-conf-002-enable-or-disable-alerts)
  - [HU-CONF-003: Change notification tone](HU-CONF/user-stories.md#hu-conf-003-change-notification-tone)
  - [HU-CONF-004: Configure application language](HU-CONF/user-stories.md#hu-conf-004-configure-application-language)
  - [HU-CONF-005: Update color palettes](HU-CONF/user-stories.md#hu-conf-005-update-color-palettes)
  - [HU-CONF-006: Update user facial parameters](HU-CONF/user-stories.md#hu-conf-006-update-user-facial-parameters)
  - [HU-CONF-007: IoT device failure alerts management](HU-CONF/user-stories.md#hu-conf-007-iot-device-failure-alerts-management)
  - [HU-CONF-008: Delay and justification parametrization](HU-CONF/user-stories.md#hu-conf-008-delay-and-justification-parametrization)

### Histories (HU-HIST)
- [History User Stories](HU-HIST/user-stories.md)
  - [HU-HIST-001: Attendance and absence query by parameters](HU-HIST/user-stories.md#hu-hist-001-attendance-and-absence-query-by-parameters)
  - [HU-HIST-002: User change history](HU-HIST/user-stories.md#hu-hist-002-user-change-history)

### Reports (HU-REP)
- [Report User Stories](HU-REP/user-stories.md)
  - [HU-REP-001: Report export](HU-REP/user-stories.md#hu-rep-001-report-export)
  - [HU-REP-002: Role-based analytical dashboard](HU-REP/user-stories.md#hu-rep-002-role-based-analytical-dashboard)
  - [HU-REP-003: Date-range reports](HU-REP/user-stories.md#hu-rep-003-date-range-reports)
  - [HU-REP-004: Delay and absence query by parameters](HU-REP/user-stories.md#hu-rep-004-delay-and-absence-query-by-parameters)

## Planned stories (pending documentation)

These stories are cited by other user stories and tracked in the traceability matrix (fae-docs), but their detailed specification is not yet part of this annex.

| ID | Title | Requirement | Sprint | Cited in |
|---|---|---|---|---|
| HU-ATT-005 | Alternative registration for NN users | RF 3.2.1 | Sprint 2 | [HU-ATT-003](HU-ATT/user-stories.md#hu-att-003-alternative-registration-for-registered-users-with-facial-scan-failure), [HU-ATT-004](HU-ATT/user-stories.md#hu-att-004-entry-control-for-unregistered-or-nn-users) |
| HU-IAM-004 | Massive user import via CSV | RF 1.2 | Sprint 2 | [HU-IAM-002](HU-IAM/user-stories.md#hu-iam-002-user-registration-with-role-assignment) |
| HU-IAM-007 | Activate/deactivate users | RF 1.6 | Sprint 2 | [HU-CONF-006](HU-CONF/user-stories.md#hu-conf-006-update-user-facial-parameters), [HU-CONF-007](HU-CONF/user-stories.md#hu-conf-007-iot-device-failure-alerts-management), [HU-HIST-002](HU-HIST/user-stories.md#hu-hist-002-user-change-history) |
| HU-IAM-010 | Change user role | RF 4.6 | Sprint 2 | [HU-HIST-002](HU-HIST/user-stories.md#hu-hist-002-user-change-history) |

---
*Note: To navigate to a specific story, click the link for the corresponding module or story.*
