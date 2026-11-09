# Administrator Manual

**FaceAttend EDU**

Team members:

Juan David Arboleda Perdomo

Diego Andrés Gutiérrez Nuñez

Jonathan Steven Rizo Solano

Centro de la Industria, la Empresa y los Servicios — SENA

Technologist in Software Analysis and Development — Cohort 3145556

Karol Daniela Correa

October 6, 2026

Document version 1.0 — Documented software version: 1.0.0

> *Note.* English translation of *Manual de Administrador*. The original structure, section order, tables and figures have been preserved. Placeholders marked `[TO BE COMPLETED]` correspond to the `[COMPLETAR]` markers of the source document.

---

# **Version Control**

Every modification to this manual must be recorded before the document is submitted again. The manual version must always correspond to the version of the software it describes.

**Table 1**

*Document version history*

| **Version** | **Date**   | **Description of change**                                            | **Responsible**   |
|-------------|------------|----------------------------------------------------------------------|-------------------|
| 1.0         | 06/10/2026 | Initial creation of the administrator manual.                        | Development team  |
| 1.1         | 06/10/2026 | Application of APA 7th edition standards and removal of color usage. | Development team  |
| [ ]         | [ ]        | [Record the next change here.]                                       | [ ]               |

*Note.* Minor changes, such as wording adjustments or the addition of screenshots, increment the second digit. Functional changes, such as the addition of an administrative module or the modification of permissions, increment the first digit.

## **Update Rule**

- The manual must be reviewed after every deployment that modifies screens, roles, or reports.

- Every delivered version must be recorded in the table above together with its responsible party.

# **Table of Contents**

- [Version Control](#version-control)
  - [Update Rule](#update-rule)
- [Table of Contents](#table-of-contents)
- [Introduction](#introduction)
- [Objective](#objective)
  - [Specific Objectives](#specific-objectives)
- [Scope](#scope)
  - [Content Covered by the Manual](#content-covered-by-the-manual)
  - [Content Not Covered by the Manual](#content-not-covered-by-the-manual)
- [Administrator Profile](#administrator-profile)
  - [Administrator Responsibilities](#administrator-responsibilities)
  - [Daily Responsibilities](#daily-responsibilities)
  - [Weekly Responsibilities](#weekly-responsibilities)
  - [Monthly Responsibilities](#monthly-responsibilities)
  - [Event-Driven Responsibilities](#event-driven-responsibilities)
  - [Limits of the Role](#limits-of-the-role)
- [Access to the Administration Panel](#access-to-the-administration-panel)
  - [Login Procedure](#login-procedure)
- [User Management](#user-management)
  - [Create a User](#create-a-user)
  - [Edit a User](#edit-a-user)
  - [Activate or Deactivate a User](#activate-or-deactivate-a-user)
  - [Reset a User's Password](#reset-a-users-password)
  - [Query and Filter Users](#query-and-filter-users)
- [Role and Permission Management](#role-and-permission-management)
  - [Assign or Change a Role](#assign-or-change-a-role)
  - [Permission Matrix by Operation](#permission-matrix-by-operation)
- [System Configuration](#system-configuration)
  - [Procedure for Modifying a Parameter](#procedure-for-modifying-a-parameter)
  - [Parameters External to the Panel](#parameters-external-to-the-panel)
- [Management of Core Information](#management-of-core-information)
  - [Manage Cohorts](#manage-cohorts)
  - [Create a Cohort](#create-a-cohort)
  - [Cohort Administration Rules](#cohort-administration-rules)
  - [Manage Courses](#manage-courses)
  - [Manage Schedules](#manage-schedules)
  - [Attendance Issue Management](#attendance-issue-management)
  - [Modules Pending Documentation](#modules-pending-documentation)
- [Administrative Reports](#administrative-reports)
  - [Generate and Export a Report](#generate-and-export-a-report)
  - [Monitoring Metrics](#monitoring-metrics)
  - [Good Practices in Report Handling](#good-practices-in-report-handling)
- [Backups](#backups)
  - [Procedure from the Panel](#procedure-from-the-panel)
  - [Alternate Command-Line Procedure](#alternate-command-line-procedure)
  - [Backup Storage](#backup-storage)
- [Information Restoration](#information-restoration)
  - [Restoration Procedure](#restoration-procedure)
  - [Post-Restoration Verification](#post-restoration-verification)
  - [Alternate Command-Line Procedure](#alternate-command-line-procedure-1)
- [Audit and Action History](#audit-and-action-history)
  - [Query the History](#query-the-history)
  - [Periodic Review](#periodic-review)
  - [Actions Not Logged by the System](#actions-not-logged-by-the-system)
- [Security Recommendations and Support Procedures](#security-recommendations-and-support-procedures)
  - [Account Security](#account-security)
  - [Information Security](#information-security)
  - [Communication with Users](#communication-with-users)
  - [Incident Management and Escalation](#incident-management-and-escalation)
  - [Change Management](#change-management)
  - [Known Risks and Contingency Plan](#known-risks-and-contingency-plan)
- [Glossary](#glossary)
- [References](#references)
- [Appendix A](#appendix-a)
- [Appendix B](#appendix-b)
- [Appendix C](#appendix-c)
- [Appendix D](#appendix-d)
- [Appendix E](#appendix-e)
- [Appendix F](#appendix-f)

# **Introduction**

FaceAttend EDU is a system built to record and control attendance within a training environment. The system replaces manual attendance taking with a digital workflow, so that the information is centralized, queryable, and exportable.

This manual explains how to administer the system. It does not explain how to use it as a student, nor how the source code is written; that information is found in the user manual and in the technical manual, respectively. The structure of the document follows the institutional guide for producing software manuals (Servicio Nacional de Aprendizaje, 2026).

The document is written so that a person who did not take part in the development can take over administration of the system by reading it in full. Each procedure indicates where the option is located, what steps to follow, and what result should be produced. The manual contains no passwords, private keys, tokens, or production credentials.

**Table 2**

*Intended readers of the manual and applicable sections*

| **Reader**                             | **What they will find in this manual**                                                     |
|----------------------------------------|--------------------------------------------------------------------------------------------|
| Super Administrator                    | The entire document, including the configuration, backup, and restoration sections.        |
| Administrator                          | Management of users, cohorts, courses, schedules, reports, and audit.                      |
| Instructor with delegated functions    | Sections 9, 12, and 13 only, and only for the groups assigned to them.                     |
| Person taking over the project handover| The complete document, starting with sections 7 and 8.                                     |

**Table 3**

*Complementary project documents*

| **Document**           | **Question it answers**            | **When to consult it**                                                |
|------------------------|------------------------------------|-----------------------------------------------------------------------|
| User manual            | How do I use the system?           | When an end user asks about a screen or a message.                    |
| Technical manual       | How is the system built?           | When services, the database, or endpoints need to be understood.      |
| Installation manual    | How do I install and run the system? | When the system is being brought up on a new machine or server.     |
| Administrator manual   | How do I administer the system?    | This document. Operation of the already-installed system.             |

# **Objective**

To guide the administrator in the management, configuration, and supervision of FaceAttend EDU, ensuring secure and organized use of the platform.

## **Specific Objectives**

1. Describe the profile, responsibilities, and limits of the administrator role within the system.

2. Document the procedure for accessing the administration panel.

3. Establish the procedures for creating, modifying, deactivating, and resetting user accounts.

4. Define the existing roles and the permissions associated with each one.

5. Explain the administration of cohorts, courses, and schedules.

6. Document the generation, querying, and export of attendance reports.

7. Establish the backup and restoration procedure for the system's two databases.

8. Define the security practices the administrator must comply with.

# **Scope**

## **Content Covered by the Manual**

- Administration of user accounts and role assignment.

- Administration of cohorts, courses, and schedules.

- Querying, filtering, and exporting attendance reports.

- General system configuration parameters.

- Backup and restoration of PostgreSQL and MongoDB.

- Querying the history of administrative actions.

- Incident handling and escalation procedures.

## **Content Not Covered by the Manual**

- Installation, deployment, and initial configuration of the microservices, described in the installation manual.

- Internal architecture, detailed data model, and endpoints, described in the technical manual.

- Day-to-day use of the system by students and instructors, described in the user manual.

- Administration of the server or container infrastructure where the system runs.

- Modification of the source code.

Every procedure in this manual is carried out on the already-installed and running system. If the system is not installed, the installation manual must be applied first.

# **Administrator Profile**

The administrator is the person responsible for keeping the system available, for ensuring that each user has the access that corresponds to them, and for ensuring that the attendance information is reliable.

**Table 4**

*Profile required for the administrator role*

| **Aspect**                  | **Description**                                                                                                                     |
|-----------------------------|-------------------------------------------------------------------------------------------------------------------------------------|
| Knowledge of the system     | Must be familiar with the users, cohorts, courses, schedules, and reports modules.                                                  |
| Minimum technical knowledge | Use of a web browser, downloading and storing files, and basic notions of databases in order to interpret a backup.                 |
| Institutional judgment      | Must know the structure of cohorts, programs, and shifts of the training center.                                                    |
| Responsibility over data    | Handles personal data of students and instructors, and is therefore accountable for its confidentiality.                            |

## **Administrator Responsibilities**

### ***Daily Responsibilities***

- Verify that the system is responding and that attendance recording is operating.

- Handle access requests: account creation, password reset, and unlocking.

- Review the issues reported by instructors regarding unrecorded attendance.

### ***Weekly Responsibilities***

- Review the list of active users and deactivate accounts that no longer correspond to people involved in the process.

- Generate and archive the attendance report for the period.

- Verify that the week's backup was generated and stored correctly.

### ***Monthly Responsibilities***

- Review the history of administrative actions, looking for unjustified operations.

- Validate that the system configuration still corresponds to the reality of the training center.

- Verify that this manual still matches the actual screens and procedures of the system.

### ***Event-Driven Responsibilities***

- Run a backup before any major change or deployment.

- Coordinate the restoration of information when data loss occurs.

- Escalate to the technical team those incidents that cannot be resolved from the administration panel.

## **Limits of the Role**

The administrator must not perform the following actions:

- Directly modify attendance records in order to alter an academic result.

- Share their account with another person or use a generic shared account.

- Delete users who have attendance history. Instead, the account is deactivated, so that the history is preserved.

- Modify the source code or the database directly without authorization from the technical lead.

# **Access to the Administration Panel**

The administration panel is the interface from which all the procedures in this manual are carried out. Access depends on the role assigned to the account.

**Table 5**

*Prerequisites for accessing the administration panel*

| **Requirement**      | **Detail**                                                                                     |
|----------------------|------------------------------------------------------------------------------------------------|
| Browser              | Google Chrome, Microsoft Edge, or Mozilla Firefox, in an up-to-date version.                    |
| Connection           | Access to the network where the system is published.                                            |
| Account              | User with the Super Administrator or Administrator role, in Active status.                      |
| System address       | http://localhost:8090                                                     |
| Services running     | The system microservices and the PostgreSQL and MongoDB databases must be available.            |

## **Login Procedure**

1. Open the browser and enter the system address.

2. On the login screen, type the institutional email address and the password.

3. Click the Log in button.

4. Verify that the system displays the panel corresponding to the administrator role.

5. Confirm that the administration menu is visible.

*Expected result:* the system presents the administration panel with the user's name and role at the top.

The screenshots corresponding to this procedure are included in Appendix E.

**Table 6**

*Frequent access problems and corrective actions*

| **Situation**                                        | **Probable cause**                              | **Administrator action**                                                                      |
|------------------------------------------------------|-------------------------------------------------|-----------------------------------------------------------------------------------------------|
| The system reports invalid credentials               | Incorrect email or password.                    | Verify the email address. If it persists, reset the password as described in section 9.       |
| The system reports that the account is inactive      | The account was deactivated.                    | Reactivate the account if appropriate, as described in section 9.                             |
| The user logs in but does not see the admin menu     | The assigned role is not an administrative one. | Check the account's role in section 10 and correct it if appropriate.                         |
| The page does not load                               | Service stopped or incorrect address.           | Verify the availability of the services and escalate to the technical team as per section 17. |
| The session closes by itself                         | The configured inactivity timeout elapsed.      | Log in again and review the session timeout parameter in section 11.                          |

# **User Management**

The users module allows accounts in the system to be created, queried, edited, activated, deactivated, and have their password reset. It is the module most frequently used by the administrator.

**Table 7**

*Fields that make up a user account*

| **Field**                 | **Mandatory** | **Remark**                                                        |
|---------------------------|---------------|-------------------------------------------------------------------|
| First and last names      | Yes           | Must match the person's identification document.                  |
| Identification number     | Yes           | Unique in the system. It cannot be registered twice.              |
| Email                     | Yes           | This is the credential the person logs in with. It must be unique.|
| Phone                     | No            | Used for contact in the event of an issue.                        |
| Role                      | Yes           | Defines what the person can do. See section 10.                   |
| Status                    | Yes           | Active or Inactive. Determines whether the person can log in.     |
| Associated cohort or group| Depends on role | Applies to students and instructors.                            |

## **Create a User**

1. Log in with an administrator account.

2. Select the Administration menu and then the Users option.

3. Click the New user button.

4. Fill in first names, last names, identification number, email, and phone.

5. Select the role that corresponds to the person's actual function.

6. Associate the cohort or group when the role requires it.

7. Set the status to Active.

8. Click Save.

9. Verify that the user appears in the list and that the system displays the successful registration message.

*Expected result:* the user is created and can access the system according to the assigned role.

Before creating an account, the identification number must be searched for in the list. If the person already exists with Inactive status, the existing account must be reactivated instead of creating a new one, since a duplicate record splits that person's attendance history.

## **Edit a User**

1. Go to the Administration menu and then to Users.

2. Search for the person by name, identification number, or email.

3. Click the Edit option on the corresponding row.

4. Modify only the fields that need correction.

5. Click Save and confirm the successful update message.

Role changes and status changes are recorded in the action history described in section 16.

## **Activate or Deactivate a User**

Deactivation is the standard procedure when a person stops being part of the training process, since it preserves the attendance history and blocks access.

1. Go to the user list.

2. Search for the person.

3. Change the status to Inactive.

4. Save the change.

5. Verify that the account no longer allows logging in.

**Table 8**

*Effects of activating, reactivating, and deleting accounts*

| **Action**  | **When it applies**                                               | **Effect on the history**                                             |
|-------------|-------------------------------------------------------------------|-----------------------------------------------------------------------|
| Deactivate  | The person completed their process, withdrew, or changed role.    | The attendance history is preserved intact.                           |
| Reactivate  | The person returns to the training process.                       | The previous history of the same account is recovered.                |
| Delete      | Only for records created in error and with no associated attendance. | The record is lost. Requires Super Administrator authorization.    |

## **Reset a User's Password**

1. Go to the user list and search for the person.

2. Select the Reset password option.

3. Confirm the action.

4. Inform the person, through a verifiable channel, that they must set a new password on their first login.

The administrator must never know, write down, or store another user's final password. The reset forces the user to define their own password.

## **Query and Filter Users**

- Filter by role to review how many administrative accounts exist.

- Filter by status to identify active accounts that should no longer be active.

- Filter by cohort or group to validate the composition of a group before a period starts.

- Search by identification number or email to handle a specific support request.

# **Role and Permission Management**

FaceAttend EDU handles four roles. Each role determines which modules the person sees and which actions they can carry out. The assignment principle is to grant only the permissions that the person's actual function requires.

**Table 9**

*System roles, main permissions, and restrictions*

| **Role**       | **Main permissions**                                                                                                                                       | **Restrictions**                                                                                                 |
|----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|
| Super Admin    | Full control of the system. Manages administrators, general configuration, backups, and restoration. Accesses the complete action history.                  | A minimum number of accounts with this role must exist. It is not shared between people.                         |
| Administrator  | Manages users, cohorts, courses, schedules, and reports. Queries the action history.                                                                       | Does not perform database restorations or modify critical parameters without Super Admin authorization.          |
| Instructor     | Queries and manages attendance for the cohorts and courses assigned to them. Generates reports for their groups.                                           | Does not create or delete users. Does not access information for groups not assigned to them.                    |
| Student        | Queries their own attendance and personal information.                                                                                                     | Does not access other users' information. Does not generate administrative reports.                              |

## **Assign or Change a Role**

1. Go to the Administration menu and then to Users.

2. Search for the person and select Edit.

3. Change the Role field to the appropriate value.

4. Save the change.

5. Ask the person to log out and log back in so that the permissions take effect.

6. Confirm with the person that they see the expected modules.

A role change immediately modifies what the person can see and do. Before elevating a role to Administrator or Super Admin, an explicit request from the project owner must exist. This change is recorded in the action history.

## **Permission Matrix by Operation**

The following matrix summarizes who can carry out each operation. It must be validated against the actual behavior of the system and adjusted if that behavior changes.

**Table 10**

*Permission matrix by operation and role*

| **Operation**                     | **Super Admin** | **Administrator**        | **Instructor**      | **Student**  |
|-----------------------------------|-----------------|--------------------------|---------------------|--------------|
| Create user                       | Yes             | Yes                      | No                  | No           |
| Change a user's role              | Yes             | Yes, except Super Admin  | No                  | No           |
| Deactivate user                   | Yes             | Yes                      | No                  | No           |
| Create cohort or course           | Yes             | Yes                      | No                  | No           |
| Assign schedule                   | Yes             | Yes                      | No                  | No           |
| Record or adjust attendance       | Yes             | Yes                      | Own groups only     | No           |
| Generate attendance report        | Yes             | Yes                      | Own groups only     | Own only     |
| Export report                     | Yes             | Yes                      | Own groups only     | No           |
| Modify system configuration       | Yes             | Partial                  | No                  | No           |
| Generate backup                   | Yes             | Subject to authorization | No                  | No           |
| Restore information               | Yes             | No                       | No                  | No           |
| Query action history              | Yes             | Yes                      | No                  | No           |

*Note.* The value Partial indicates that the role has access to some parameters and not to others. The detail is specified in section 11.

# **System Configuration**

The configuration defines the general parameters the system operates with. A change in this section affects all users, so every modification must be recorded and communicated.

**Table 11**

*Administrable system parameters*

| **Parameter**              | **Description**                                                                              | **Reference value**                            |
|----------------------------|----------------------------------------------------------------------------------------------|------------------------------------------------|
| Institution name           | Data that appears in headers and reports.                                                    | Centro de la Industria, la Empresa y los Servicios — SENA        |
| Session timeout            | Minutes of inactivity before the session is closed automatically.                            | 30 minutes                                     |
| Attendance statuses        | Values allowed when recording attendance.                                                    | Present, Absent, Late, Excused                 |
| Check-in tolerance         | Minutes after the start time within which the record is still considered on time.             | Defined by the training center |
| Shifts                     | Time bands in which training operates.                                                       | Morning, Afternoon, Evening                    |
| Export formats             | Formats available for downloading reports.                                                   | PDF and Excel                                  |
| Current academic period    | Date range over which reports are consolidated.                                              | Defined by the training center                |

## **Procedure for Modifying a Parameter**

1. Generate a backup before the change, as described in section 14.

2. Go to the Administration menu and then to Configuration.

3. Locate the parameter that needs to be modified.

4. Record the previous value in a note, in case it has to be reverted.

5. Apply the new value and save.

6. Verify the effect of the change with a concrete test.

7. Inform the affected users as described in section 17.

## **Parameters External to the Panel**

The following elements belong to the deployment configuration and are not modified from the administration panel. Any change to them is the responsibility of the technical team and is documented in the installation manual and in the technical manual.

**Figure 1**

*Structure of the system environment variables*

> POSTGRES_HOST=...
>
> POSTGRES_PORT=...
>
> POSTGRES_DB=...
>
> POSTGRES_USER=...
>
> POSTGRES_PASSWORD=YOUR_VALUE
>
> MONGO_URI=...
>
> MONGO_DB=...
>
> EMAIL_INTERNAL_TOKEN=YOUR_VALUE
>
> SESSION_TIMEOUT=...

*Note.* The figure presents only the structure of the variables. The manual must not publish passwords, private keys, tokens, or real connection strings. There is no JWT secret: sessions are opaque UUID identifiers stored in the identity database, so there are no signing keys to configure or rotate.

# **Management of Core Information**

The core information of FaceAttend EDU is made up of cohorts, courses, and schedules. Correct configuration of these determines whether attendance recording is associated with the right group and the right session.

**Figure 2**

*Configuration order of the core information*

> 1\. Cohort (training group)
>
> |
>
> 2\. Course (associated with a cohort)
>
> |
>
> 3\. Schedule (associated with a course and an instructor)
>
> |
>
> 4\. Students (associated with the cohort)
>
> |
>
> 5\. Attendance recording (generated against the schedule)

*Note.* The elements have dependencies among themselves. Configuring them in a different order produces assignment errors.

## **Manage Cohorts**

### ***Create a Cohort***

1. Go to the Administration menu and then to Cohorts.

2. Click New cohort.

3. Fill in the cohort number, program name, shift, and start and end dates.

4. Save and verify that the cohort appears in the list.

### ***Cohort Administration Rules***

- The cohort number is unique and must not be registered twice.

- A cohort with recorded attendance is not deleted; it is closed or marked as finished.

- When a cohort is closed, verify that the reports for the period have already been generated and archived.

## **Manage Courses**

1. Go to the Administration menu and then to Courses.

2. Click New course.

3. Fill in the name of the course or competency and the description.

4. Associate the course with the corresponding cohort.

5. Assign the responsible instructor.

6. Save and verify the record in the list.

*Expected result:* the course becomes available for schedule assignment and the assigned instructor sees it on their panel.

## **Manage Schedules**

1. Go to the Administration menu and then to Schedules.

2. Click New schedule.

3. Select the course.

4. Define the days of the week, the start time, and the end time.

5. Confirm the environment or classroom when the system requests it.

6. Save and verify that there is no overlap with another schedule of the same instructor or the same group.

**Table 12**

*Validations applicable to schedule assignment*

| **Validation**                  | **What to check**                                               | **Action if it fails**                       |
|---------------------------------|-----------------------------------------------------------------|----------------------------------------------|
| Group schedule overlap          | That the cohort does not have two simultaneous sessions.        | Adjust the time band of one of the sessions. |
| Instructor schedule overlap     | That the instructor is not assigned to two groups at the same time. | Reassign the instructor or change the time band. |
| Consistency with the shift      | That the time band corresponds to the cohort's shift.           | Correct the shift or the time band.          |
| Validity period                 | That the schedule falls within the cohort's dates.              | Adjust the dates before saving.              |

## **Attendance Issue Management**

When an instructor or a student reports that an attendance record was not registered or was registered incorrectly, the administrator applies the following procedure.

1. Receive the request through the official channel, including the date, group, student name, and description of the issue.

2. Look up the record in the corresponding attendance report.

3. Verify the issue with the instructor responsible for the session.

4. Apply the adjustment in the system only if the issue is confirmed.

5. Record in the issue log the date, the responsible party, and the justification for the adjustment.

6. Inform the requester that the issue has been handled.

Every manual attendance adjustment must be justified and recorded. An unsupported adjustment compromises the reliability of the system and of the administrator.

## **Modules Pending Documentation**

If the system implements face enrollment, attendance recording by facial recognition, or other additional administrative modules, the team must document them in this section using the same format employed in the previous sections: numbered steps, expected result, validations, and a real screenshot. Modules that are not implemented must not be documented.

# **Administrative Reports**

Reports are the final product of the system. They make it possible to verify attendance by group, by person, and by period, and they support academic decisions.

**Table 13**

*Reports available in the system*

| **Report**                | **Content**                                                  | **Filters**                      | **Frequency** |
|---------------------------|--------------------------------------------------------------|----------------------------------|---------------|
| Attendance by cohort      | Consolidated attendance of all the students in a group.      | Cohort and date range.           | Weekly        |
| Attendance by student     | Individual attendance history.                               | Student and date range.          | On request    |
| Attendance by course      | Attendance grouped by course or competency.                  | Course and date range.           | Weekly        |
| Attendance by instructor  | Sessions and records associated with an instructor.          | Instructor and date range.       | Monthly       |
| Registered users          | System accounts by status and by role.                       | Role and status.                 | Monthly       |
| Accumulated absences      | Students who exceed the absence threshold.                   | Cohort and period.               | Weekly        |

## **Generate and Export a Report**

1. Go to the Reports menu.

2. Select the report type.

3. Apply the cohort, course, student, or instructor filters and the date range.

4. Click Generate or Query.

5. Check on screen that the data corresponds to the applied filter.

6. Select the export format, PDF or Excel.

7. Download the file and save it using the defined naming convention.

*Expected result:* the system generates the file with the filtered data and downloads it to the machine.

**Figure 3**

*Naming convention for report files*

> Report\_\<type\>\_\<identifier\>\_\<YYYY-MM-DD\>.pdf
>
> Examples:
>
> Report_attendance_cohort\_\<number\>\_2026-10-06.pdf
>
> Report_active_users_2026-10-06.xlsx

## **Monitoring Metrics**

The administrator monitors the indicators in the following table. The target values must be set by the team according to the agreements of the training center.

**Table 14**

*Administrative monitoring indicators*

| **Indicator**                       | **Source**                                                     | **Periodicity** | **Alert threshold**              |
|-------------------------------------|----------------------------------------------------------------|-----------------|----------------------------------|
| Attendance percentage by cohort     | Attendance by cohort report.                                   | Weekly          | Defined by the training center                |
| Students with critical absence      | Accumulated absences report.                                   | Weekly          | Defined by the training center                |
| Sessions with no attendance record  | Comparison between scheduled sessions and generated records.   | Weekly          | Any session without a record     |
| Active accounts not in use          | User report cross-referenced with the last login.              | Monthly         | Defined by the training center                |
| Attendance issues handled           | Issue log.                                                     | Monthly         | More than 48 hours unattended    |
| Backups generated                   | Backup folder.                                                 | Weekly          | Any week without a backup        |

## **Good Practices in Report Handling**

- Always verify the date range before delivering a report, since an incorrect filter produces incorrect conclusions.

- Archive the period's reports in a controlled folder and not on the desktop of a personal machine.

- Deliver reports only to those whose function justifies it, as they contain students' personal data.

- Do not manually modify the exported file. If a data point is wrong, it is corrected in the system and the report is generated again.

# **Backups**

FaceAttend EDU uses two databases. A complete backup must include both, since backing up only one leaves the system in an inconsistent state at the time of restoration.

**Table 15**

*Elements included in the backup*

| **Element**             | **Engine**            | **Content**                                                                 | **Criticality** |
|-------------------------|-----------------------|-----------------------------------------------------------------------------|-----------------|
| Relational database     | PostgreSQL            | Users, roles, cohorts, courses, schedules, and attendance records.          | High            |
| Document database       | MongoDB               | Unstructured system information, according to the service that manages it.  | High            |
| Configuration files     | Deployment files      | Microservice parameters, without real credentials.                          | Medium          |
| Archived reports        | File system           | Exported reports from closed periods.                                       | Medium          |

**Table 16**

*Backup frequency*

| **Moment**                                   | **Scope**                                            | **Responsible**                |
|----------------------------------------------|------------------------------------------------------|--------------------------------|
| Weekly                                       | Complete backup of PostgreSQL and MongoDB.           | Administrator                  |
| Before a deployment or update                | Complete backup.                                     | Super Admin or technical lead  |
| Before a major configuration change          | Complete backup.                                     | Administrator                  |
| At the close of an academic period            | Complete backup plus the period's report archive.   | Administrator                  |

## **Procedure from the Panel**

1. Log in to the administration panel with an authorized account.

2. Select the Backups option.

3. Click Generate backup.

4. Wait for the system to confirm generation.

5. Download the generated file.

6. Save it in the backup folder with the date in the file name.

7. Record the backup in the backup log.

**Figure 4**

*Naming convention for backup files*

> backup_faceattend_postgres_2026-10-06.sql
>
> backup_faceattend_mongo_2026-10-06.archive

## **Alternate Command-Line Procedure**

When the panel does not offer the option or is unavailable, the backup is run by the technical team using the native tools of each engine. The connection values are taken from the deployment environment variables.

**Figure 5**

*Database backup commands*

> \# PostgreSQL
>
> pg_dump -h \<host\> -p \<port\> -U \<user\> -d \<database\> \\
>
> -F c -f backup_faceattend_postgres\_\<YYYY-MM-DD\>.dump
>
> \# MongoDB
>
> mongodump --uri="\<connection_string\>" \\
>
> --archive=backup_faceattend_mongo\_\<YYYY-MM-DD\>.archive --gzip

*Note.* The commands are run with credentials that must not be written in this manual nor shared over open channels. They must be requested from the technical lead at the time of use.

## **Backup Storage**

- Store each backup in at least two different locations.

- Keep one of the locations off the machine where the system runs.

- Retain at minimum the four most recent weekly backups and the closing backup of each period.

- Restrict access to the backup folder, since it contains complete personal data.

- Verify that the downloaded file has a plausible size, as a file of a few kilobytes indicates a failed backup.

**Table 17**

*Backup log*

| **Date** | **Type**                                | **Files generated** | **Location** | **Responsible** | **Verified** |
|----------|-----------------------------------------|---------------------|--------------|-----------------|--------------|
| [ ]      | [Weekly, pre-deployment, or closing]    | [ ]                 | [ ]          | [ ]             | [Yes or No]  |
| [ ]      | [ ]                                     | [ ]                 | [ ]          | [ ]             | [ ]          |
| [ ]      | [ ]                                     | [ ]                 | [ ]          | [ ]             | [ ]          |

# **Information Restoration**

Restoration returns the system to the state of the selected backup. It is a procedure that overwrites information, so it is carried out only by the Super Administrator or the technical lead, and always with prior authorization.

All information recorded between the backup date and the moment of restoration is lost. Before restoring, a backup of the current state must be generated, so that it is possible to roll back if the procedure fails.

**Table 18**

*Criteria for determining the need for restoration*

| **Situation**                                | **Does it require restoration?** | **Prior action**                                                 |
|----------------------------------------------|----------------------------------|------------------------------------------------------------------|
| Massive loss of attendance records           | Yes                              | Confirm the scope with the technical team.                       |
| A user deleted by mistake                    | Not necessarily                  | Recreate the user and restore only if their history was lost.    |
| Error in a single attendance record          | No                               | Correct it from the corresponding module, as per section 12.      |
| Corrupt or inaccessible database             | Yes                              | Escalate to the technical lead before taking any action.         |
| Failed deployment that altered the data      | Yes                              | Restore the backup taken before the deployment.                  |

## **Restoration Procedure**

1. Obtain authorization from the project owner and have it in writing.

2. Inform users that the system will be unavailable during the procedure.

3. Generate a backup of the current state of both databases.

4. Stop the services that write to the databases.

5. Select the backup file corresponding to the required date.

6. Restore PostgreSQL and MongoDB to the same point in time.

7. Restart the services.

8. Verify the system according to the checklist in the following section.

9. Inform users that the system has been restored and state the date up to which the information is valid.

10. Record the event in the incident log.

## **Post-Restoration Verification**

- Login works with an administrator account.

- The user list shows the expected number of records.

- Roles and permissions are correctly preserved.

- Cohorts, courses, and schedules appear complete.

- An attendance report for a known period yields the expected data.

- The most recent records correspond to the date of the restored backup.

## **Alternate Command-Line Procedure**

**Figure 6**

*Database restoration commands*

> \# PostgreSQL
>
> pg_restore -h \<host\> -p \<port\> -U \<user\> -d \<database\> \\
>
> --clean --if-exists backup_faceattend_postgres\_\<YYYY-MM-DD\>.dump
>
> \# MongoDB
>
> mongorestore --uri="\<connection_string\>" \\
>
> --archive=backup_faceattend_mongo\_\<YYYY-MM-DD\>.archive --gzip --drop

# **Audit and Action History**

The action history makes it possible to establish who carried out each operation and when. It is the tool with which the administrator substantiates any change to users, permissions, or attendance records.

**Table 19**

*Actions that must be recorded in the history*

| **Action**                          | **Minimum expected data**                                      | **Who reviews it** |
|-------------------------------------|----------------------------------------------------------------|--------------------|
| User creation                       | User created, role assigned, responsible party, date and time. | Administrator      |
| Role change                         | Previous role, new role, responsible party, date and time.     | Super Admin        |
| Account activation or deactivation  | Previous status, new status, responsible party, and date.      | Administrator      |
| Password reset                      | Account affected, responsible party, and date.                 | Super Admin        |
| Adjustment of an attendance record  | Record affected, previous value, new value, and responsible party. | Super Admin    |
| Configuration change                | Parameter, previous value, new value, and responsible party.   | Super Admin        |
| Backup generation or restoration    | Type of operation, responsible party, and date.                | Super Admin        |

## **Query the History**

1. Go to the Administration menu and then to Audit or Action history.

2. Apply the date range filter.

3. Filter by responsible user or by action type when investigating a specific case.

4. Review the resulting records.

5. Export the result when the review must be kept as evidence.

## **Periodic Review**

Once a month the administrator reviews the history, looking specifically for the following items:

- Role changes to Administrator or Super Admin that do not correspond to a recorded request.

- Attendance adjustments with no justification in the issue log.

- Administrative access at unusual hours.

- Repeated password resets on the same account.

- Deletion of records.

Any finding is documented and escalated according to the procedure in section 17.

## **Actions Not Logged by the System**

If the current system does not log one of the indicated actions, this must be noted in this section and reported to the technical team as a pending improvement. In the meantime, that action is recorded manually in the administrative change log in Appendix C.

# **Security Recommendations and Support Procedures**

## **Account Security**

- Do not share the administrator password with anyone, through any channel.

- Use individual accounts, with no generic accounts shared among several people.

- Assign permissions according to each person's actual function and not out of convenience.

- Immediately deactivate the accounts of people who no longer belong to the process.

- Log out when finished using the system, especially on shared machines.

- Do not leave the administrator session open on an unattended machine.

- Change your own password at any suspicion that it has become known to another person.

## **Information Security**

- Take a backup before any major change.

- Do not publish credentials, private keys, tokens, or connection strings in documents, repositories, or messages.

- Treat reports as personal information and deliver them only to those whose function justifies it.

- Do not store backups or reports on unauthorized personal services.

- Review the action history periodically.

## **Communication with Users**

**Table 20**

*User communication matrix*

| **Situation**                   | **Recipient**                        | **Timing**                        | **Channel**                      |
|---------------------------------|--------------------------------------|-----------------------------------|----------------------------------|
| Scheduled maintenance           | All users                            | 48 hours in advance               | Institutional email |
| Unplanned system outage         | Instructors and coordination         | As soon as it is detected         | Institutional email                |
| Change in roles or permissions  | Affected user and their supervisor   | Before applying the change        | Institutional email                |
| Information restoration         | All users                            | Before and after the procedure    | Institutional email                |
| New system version              | All users                            | On the day of deployment          | Institutional email                |
| Attendance issue handled        | Whoever reported the issue           | On closing the case               | Institutional email                |

## **Incident Management and Escalation**

An incident is any situation that prevents normal use of the system or that compromises the information. The procedure is as follows.

1. Record the incident with date, time, reporting user, description, and a screenshot of the error.

2. Classify the level according to the following table.

3. Attempt resolution from the administration panel when the level allows it.

4. Escalate to the corresponding level if it is not resolved within the defined time.

5. Inform the reporting user, both on receiving the case and on closing it.

6. Record the applied solution in the incident log in Appendix C.

**Table 21**

*Incident escalation levels*

| **Level**        | **Examples**                                                                                | **Responsible**                                | **Response time**                                      |
|------------------|---------------------------------------------------------------------------------------------|------------------------------------------------|--------------------------------------------------------|
| 1. Operational   | Forgotten password, inactive account, or a question about a report.                         | Administrator                                  | Same day                                               |
| 2. Functional    | A module does not respond, a report yields incorrect data, or a schedule overlap occurs.    | Administrator with support from the development team | 1 week                                 |
| 3. Technical     | A microservice down, a connection error to PostgreSQL or MongoDB, or an inaccessible system.| Technical lead                                 | Immediate                                              |
| 4. Critical      | Information loss, unauthorized access, or database corruption.                               | Super Admin and technical lead                 | Immediate, with notification to the project owner      |

## **Change Management**

A change is any planned modification to the system, such as a new version, a configuration change, the modification of permissions, or the adjustment of parameters. The procedure is as follows.

1. Request: whoever requires the change submits it in writing, stating the reason.

2. Assessment: the administrator and the technical lead determine the impact and the affected users.

3. Authorization: the project owner approves or rejects the change.

4. Backup: a backup is generated before applying the change.

5. Application: the change is carried out in the agreed window.

6. Verification: it is checked that the system works and that the change produced the expected effect.

7. Communication: the affected users are informed.

8. Documentation: this manual and its version control are updated when the change modifies a procedure.

If an applied change produces unexpected behavior, it is rolled back using the previous backup before attempting successive corrections, since accumulating corrections on an unstable system makes it harder to identify the cause.

## **Known Risks and Contingency Plan**

**Table 22**

*Known risks and contingency plan*

| **Risk**                                       | **Impact**                                                      | **Prevention**                                                                    | **Contingency**                                                                            |
|------------------------------------------------|-----------------------------------------------------------------|-----------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| One of the microservices goes down             | One module stops working while the rest operates.               | Service monitoring and daily review.                                              | Escalate to level 3 and record attendance on paper until service is restored.               |
| Loss of connection to PostgreSQL               | The system does not allow information to be queried or recorded.| Weekly backup and availability checks.                                            | Escalate to level 3 and restore from the latest backup if there is corruption.              |
| Loss of connection to MongoDB                  | The information managed by the document service fails.          | Weekly backup at the same point in time as PostgreSQL.                            | Escalate to level 3 and restore both databases to the same point.                           |
| Unverified backup that fails on restore        | Permanent loss of information.                                  | Verify the file size and test a trial restoration once per period.                | Use the immediately preceding backup and document the extent of the loss.                   |
| Compromised administrative account             | Unauthorized access to personal data.                           | Individual accounts, logging out, and review of the history.                       | Deactivate the account, reset credentials, review the history, and escalate to level 4.     |
| Loss of the only active administrator          | Nobody can manage the system.                                   | Keep at least two active administrative accounts.                                 | The Super Admin creates a new administrative account.                                       |
| Drift between the manual and the system        | Procedures that do not match reality.                           | Review of the manual after every deployment.                                      | Update the manual and record the new version in section 2.                                  |

# **Glossary**

**Table 23**

*Glossary of system terms*

| **Term**              | **Definition**                                                                                              |
|-----------------------|-------------------------------------------------------------------------------------------------------------|
| Administrator         | Role with permissions to manage users, cohorts, courses, schedules, and system reports.                     |
| Student               | Person in a training process whose attendance the system records.                                           |
| Audit                 | Log of the actions carried out in the system, with responsible party, date, and detail of the change.       |
| Backup                | File containing a copy of the system's information at a given point in time.                                |
| Course                | Training unit associated with a cohort, against which schedules are planned.                                |
| Escalation            | Procedure for transferring an incident to a higher level of support.                                        |
| Account status        | Condition that determines whether a user can log in: Active or Inactive.                                    |
| Export                | Download of a report's information in PDF or Excel format.                                                  |
| Cohort                | Training group identified by a unique number, to which the students belong.                                 |
| Schedule              | Band of days and hours in which a course takes place and against which attendance is recorded.              |
| Incident              | Situation that prevents normal use of the system or compromises the information.                             |
| Instructor            | Role that manages attendance for the cohorts and courses assigned to them.                                  |
| Microservice          | Independent system component that provides a specific function and communicates with the others.            |
| MongoDB               | Document database engine used by the system.                                                                |
| Attendance issue      | Request for correction of an attendance record.                                                             |
| Permission            | Authorization to carry out a given action within the system.                                                |
| PostgreSQL            | Relational database engine used by the system.                                                              |
| Restoration           | Procedure that returns the system to the state contained in a backup.                                       |
| Role                  | Set of permissions that defines what a user can do within the system.                                       |
| Super Admin           | Role with full control of the system, including general configuration and information restoration.          |
| Environment variable  | Configuration parameter external to the code with which the services are run.                               |

# **References**

Servicio Nacional de Aprendizaje. (2026). *Guía para elaborar manuales de software: Manual de usuario, manual técnico, manual de instalación, manual de administrador* [Support material for students]. Centro de la Industria, la Empresa y los Servicios.

# **Appendix A**

**Administrator Activity Checklist**

**Table 24**

*Administrator activity checklist*

| **Activity**                                   | **Periodicity** | **Complies**  | **Date** | **Remark** |
|------------------------------------------------|-----------------|---------------|----------|------------|
| The system responds and allows login           | Daily           | [Yes or No]   | [ ]      | [ ]        |
| Access requests handled                        | Daily           | [Yes or No]   | [ ]      | [ ]        |
| Attendance issues handled                      | Daily           | [Yes or No]   | [ ]      | [ ]        |
| Active users reviewed                          | Weekly          | [Yes or No]   | [ ]      | [ ]        |
| Attendance report generated and archived       | Weekly          | [Yes or No]   | [ ]      | [ ]        |
| Backup generated and verified                  | Weekly          | [Yes or No]   | [ ]      | [ ]        |
| Action history reviewed                        | Monthly         | [Yes or No]   | [ ]      | [ ]        |
| System configuration validated                 | Monthly         | [Yes or No]   | [ ]      | [ ]        |
| Manual checked against the real system         | Monthly         | [Yes or No]   | [ ]      | [ ]        |

# **Appendix B**

**Contact Directory**

The team must fill in this directory before delivering the manual. Without it, the escalation procedure in section 17 is not applicable.

**Table 25**

*Project contact directory*

| **Function**                    | **Name**              | **Email**             | **Reason for contact**                                                     |
|---------------------------------|-----------------------|-----------------------|----------------------------------------------------------------------------|
| Super Administrator             | [TO BE COMPLETED]     | [TO BE COMPLETED]     | Restorations, critical configuration changes, and level 4 incidents.       |
| Principal administrator         | [TO BE COMPLETED]     | [TO BE COMPLETED]     | Daily management of users, cohorts, schedules, and reports.                |
| Backup administrator            | [TO BE COMPLETED]     | [TO BE COMPLETED]     | Cover for the principal administrator.                                     |
| Technical lead                  | [TO BE COMPLETED]     | [TO BE COMPLETED]     | Level 3 incidents, deployments, and service or database failures.          |
| Development team                | [TO BE COMPLETED]     | [TO BE COMPLETED]     | Functional errors and improvement requests.                                |
| Project instructor              | [TO BE COMPLETED]     | [TO BE COMPLETED]     | Academic decisions and approval of changes.                                |
| Project owner                   | [TO BE COMPLETED]     | [TO BE COMPLETED]     | Authorization of changes and of restorations.                              |

# **Appendix C**

**Administrative Change and Incident Log**

**Table 26**

*Administrative change and incident log*

| **Date** | **Type**                       | **Description** | **Responsible** | **Outcome** |
|----------|--------------------------------|-----------------|-----------------|-------------|
| [ ]      | [Change, incident, or issue]   | [ ]             | [ ]             | [ ]         |
| [ ]      | [ ]                            | [ ]             | [ ]             | [ ]         |
| [ ]      | [ ]                            | [ ]             | [ ]             | [ ]         |
| [ ]      | [ ]                            | [ ]             | [ ]             | [ ]         |

# **Appendix D**

**Administrative Activity Calendar**

**Table 27**

*Administrative activity calendar*

| **Activity**                           | **Frequency**     | **Suggested timing**              | **Responsible**   |
|----------------------------------------|-------------------|-----------------------------------|-------------------|
| System availability check              | Daily             | Start of the working day          | Administrator     |
| Handling of access requests            | Daily             | During the working day            | Administrator     |
| Generation of the attendance report    | Weekly            | Friday                            | Administrator     |
| Backup                                 | Weekly            | Friday, at close of day           | Administrator     |
| Cleanup of inactive users              | Weekly            | Friday                            | Administrator     |
| Review of the action history           | Monthly           | Last business day of the month    | Administrator     |
| Validation of the configuration        | Monthly           | Last business day of the month    | Super Admin       |
| Restoration test                       | Per period        | Before the close of the period    | Technical lead    |
| Manual update                          | Per deployment    | After every version               | Development team  |
| Academic period closing                | Per period        | Defined by the training center   | Administrator     |

# **Appendix E**

**System Screenshots**

This appendix must contain the real screenshots of the system, at good resolution and ordered according to the administration flow: login, administration panel, user list, new user form, role management, cohorts, courses, schedules, reports screen, and backups screen.

Each screenshot must be inserted using the figure format established by the APA standards: the label Figure followed by the corresponding number in bold, the title in italics on the following line, the image, and an explanatory note where necessary. The screenshots must not show real personal data or credentials.

# **Appendix F**

**Items Pending Completion**

Before the final submission, the team must replace all fields marked `[TO BE COMPLETED]` and verify the following points:

- Cover page data: team members, training center, cohort, and instructor.

- URL of the administration panel.

- Real names of the menus and buttons, if they differ from those used in this manual.

- Values of the configuration parameters and thresholds of the metrics.

- Official communication channels and response times.

- Contact directory in Appendix B.

- Real screenshots in Appendix E.

- Authorship details of the institutional guide in the reference list.

- Documentation of the additional implemented modules, as per section 12.
