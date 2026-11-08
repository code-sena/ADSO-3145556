---
title: "Role Flows Within the Application — FaceAttend EDU"
authors: Jonattan Steven Rizo Solano, Diego Andres Gutierrez Nuñez, Juan David Arboleda Perdomo
date: "October 13, 2026"
abstract: |
  This document describes the flow that each role performs within the FaceAttend EDU application: SUPER_ADMIN, SCHOOL_ADMIN, INSTRUCTOR and STUDENT. For each role it presents the objective, the screens and functions it accesses, the typical step sequence and the key permissions, based on the navigation map validated against the frontend code, the user stories and the authorization seeds in FULL/database.
keywords:
  - user roles
  - role based access control
  - user flows
  - attendance management
  - APA 7th Edition
---

# Introduction

## Document Objective

To describe the end-to-end flow of each role within the application, so that the analysis unambiguously establishes who does what, in which order and with which permissions.

## Document Scope

This document covers the four catalog roles (SUPER_ADMIN, SCHOOL_ADMIN, INSTRUCTOR, STUDENT) across the web and mobile clients. It does not define the permission matrix implementation, which belongs to the design and code artifacts.

## Method and Sources

Flows were reconstructed from the navigation map validated against the frontend (`useRolePermissions.js`, visible tabs, per-role action menus), the user stories (`04-requirements`), the domain rules (`02-domain`) and the authorization seeds (`FULL/database/02-ms-authorization-db`). The seed catalog defines 12 permissions in `context.resource:action` format; the mobile build unifies `SCHOOL_ADMIN` into `Administrador` and reintroduces `Super Administrador` as the global role.

## Transversal Flow: Authentication

All roles log in with an identifier (email or username) plus password (BCrypt, cost factor 12), receive a generic 401 that avoids user enumeration on failure, and obtain an opaque session validated both at Kong and in each microservice. Account lockout after five failed attempts (30-minute lockout) is specified but pending implementation. Every table applies soft delete and optimistic locking.

# Role Flows

## 1. SUPER_ADMIN — Global Administrator

*Objective.* Total control of the system; the first account is created with `database/scripts/bootstrap-admin.sh`; the only role with multi-school scope.

*Screens and functions.* Web dashboard with the six tabs: institutional dashboard (KPIs, at-risk students), Users (CRUD, CSV import, enrolling faces on behalf of third parties), Courses, Environments (exclusive management), full-scope Reports with export and notify-all, and Settings (security, palettes, justification parameterization, language and theme). Mobile: user management, environment management, school configuration and report screens.

**Typical sequence.**

1. Log in and land on the institutional dashboard.
2. Create the school, program, cohort, course and environment structure.
3. Create persons, credentials and assign roles (`POST /persons`, `POST /users`, `POST /users/{id}/roles`).
4. Define the role-by-permission matrix and verify it through permission evaluation.
5. Parameterize justification types, tardiness tolerance, alerts and security policies.
6. Audit the change history of any user and export any report.

*Key permissions.* All 12 of the catalog (full assignment).

## 2. SCHOOL_ADMIN — School Administrator

*Objective.* Operate one school: its users, academic structure, schedules, justifications and reports.

*Screens and functions.* The same six web tabs as the global administrator but scoped to the school; equivalent mobile screens except global functions. Manages the school users, creates environments and blocks, approves justifications, reviews biometric cases, configures the school and exports its reports.

**Typical sequence.**

1. Log in and land on the school dashboard.
2. Register environments, cohorts and courses.
3. Assign the responsible instructor to each cohort and block, validating room–slot and instructor–slot overlaps.
4. Import or create students, enroll them and register their faces.
5. Resolve the school pending justifications and biometric update cases (`Pending → In_Review → Approved | Rejected`).
6. Generate and export reports by cohort and date, and notify the school population.

*Key permissions.* All 12 within the school scope (person write, school management, enrollment management, session management, attendance write, justification approval, biometric review, configuration management, plus read and alert permissions).

## 3. INSTRUCTOR — Teacher

*Objective.* Move from manual roll call to supervision: open and close sessions, record attendance manually when facial capture fails, validate justifications and follow up cohorts.

*Screens and functions.* Web with five tabs (dashboard, Students read-only with face registration, Courses management, own Reports with export, teacher Settings; Environments visible but not manageable). Mobile: teacher dashboard, pending-justification queue with approve/reject, attendance display, photo update, justification consultation and news.

**Typical sequence.**

1. Log in and see the assigned cohorts and schedule.
2. Open the class session (unique per block and date, `Open` status).
3. Supervise automatic facial capture (client matching against the biometric service, then record creation with `FACIAL` method; `Present` or `Late` according to the tardiness tolerance; exactly one record per actor and session, only in `Open` sessions).
4. On facial failure, record manually (`MANUAL`/`IMPORT`) or route to justification.
5. Close the session (`Closed` with author and timestamp, or `Cancelled`).
6. Validate justifications: review the pending queue with reason and attachments, approve (record becomes `Justified`) or reject (keeps `Absent`/`Late`), triggering automatic notification to the student.
7. Consult cohort history and reports, and monitor absenteeism, tardiness, low-attendance and pending-justification alerts.

*Key permissions (8).* Person read, session management, attendance read and write, justification approval, biometric case request and review, alert read. Explicitly excluded: configuration management, school management, person write and notify-all.

## 4. STUDENT — Learner

*Objective.* Register attendance quickly without depending on roll call, justify absences and consult personal history.

*Screens and functions.* Web with three tabs (personal dashboard, read-only courses, student settings; classmates read-only; no reports or environments). Mobile carries the main flow: facial registration with success confirmation (single active embedding), personal attendance display, justification menu (date, instructor, environment, reason, PDF/JPG/PNG attachments up to 5 MB), justification status consultation, photo update (verify, scan, validate), news, profile and language/appearance settings.

**Typical sequence.**

1. Log in and complete the initial facial enrollment.
2. Day to day: facial scan within the block window, `FACIAL` record with confirmation; anti-duplication enforced by the unique session–actor constraint; a second scan records exit without duplicating entry.
3. On recognition failure, retry or fall back to manual justification.
4. On `Absent`/`Late`, upload a justification with attachments (1:1 with the attendance record, starting in `Pending`; mandatory attachment when the type requires it).
5. Receive the outcome notification (in-app, email or push) and review personal history and reports in personal scope.
6. Adjust personal settings (alerts, tone, language); request facial updates, which require administrator approval.

*Key permissions (4).* Person read, attendance read, biometric case request, alert read. Explicitly excluded: exporting third-party reports, approving justifications, managing users, courses or environments, and configuration management.

# Discussion and Conclusion

The four flows form a coherent chain: administrators prepare the structure and people, instructors operate the sessions and validate exceptions, and students produce attendance evidence and justify absences. The analysis confirms segregation of duties through the 12-permission catalog, with the global administrator as the only multi-school role and the student as the most restricted one.
