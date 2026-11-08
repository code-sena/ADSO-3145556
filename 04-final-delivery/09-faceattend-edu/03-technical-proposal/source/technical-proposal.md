# TECHNICAL REPORT

## PROJECT: FaceAttend EDU

---

**TEAM MEMBERS:**
- DIEGO ANDRÉS GUTIÉRREZ NUÑEZ
- JUAN DAVID ARBOLEDA PERDOMO
- JONATTAN STEVEN RIZO SOLANO

**INSTRUCTOR:**
- MOTTA VARGAS JOSÉ DE JESÚS

**INSTITUTION:**
- SERVICIO NACIONAL DE APRENDIZAJE – SENA
- ANÁLISIS Y DESARROLLO DE SOFTWARE – 3145556

*YEAR:* 2025

---

## TABLE OF CONTENTS

1. [Executive Summary](#1-executive-summary)
2. [Introduction](#2-introduction)
3. [Project Description](#3-project-description)
4. [Requirements](#4-requirements)
5. [Proposed Solution](#5-proposed-solution)
6. [System Architecture](#6-system-architecture)
7. [Data Model](#7-data-model)
8. [API and Microservices](#8-api-and-microservices)
9. [Security](#9-security)
10. [Infrastructure and DevOps](#10-infrastructure-and-devops)
11. [Work Plan](#11-work-plan)
12. [Work Team](#12-work-team)
13. [Budget](#13-budget)
14. [Quality Management](#14-quality-management)
15. [Risk Management](#15-risk-management)
16. [Annexes](#16-annexes)

---

## 1. EXECUTIVE SUMMARY

### 1.1 Project Description

FaceAttend EDU is a comprehensive attendance management platform using dual biometric recognition: facial recognition using the device camera (computer or mobile) and fingerprint recognition using the DigitalPersona 4500 SDK. Designed specifically for educational institutions, the system automates student check-in and check-out, centralizes academic histories, supports justification flows, and generates role-based reports with automatic alerts.

### 1.2 General Objective

Develop a comprehensive software system that uses the device camera for facial recognition and the DigitalPersona 4500 SDK for fingerprint biometrics, mobile and web applications, and AI tools implemented directly in code, to efficiently validate student attendance and effectively track their participation in classes, minimizing inconsistencies in records, improving academic performance, and facilitating instructor control.

### 1.3 Specific Objectives

1. Design and implement an attendance registration module that integrates dual biometric technologies: facial recognition using the device camera and fingerprint recognition using the DigitalPersona 4500 SDK, implementing directly in code all necessary functionality for real-time data capture, ensuring precise and automated validation in in-person, hybrid, or virtual environments.

2. Develop an intuitive user interface that allows instructors and administrators to view attendance records, monitor class participation through interaction tools, and receive automatic alerts about absences or low participation.

3. Integrate an automated notification system via email or mobile apps that informs instructors, parents, and students about attendance or participation anomalies, facilitating immediate corrective actions and promoting accountability.

4. Ensure software scalability and compatibility with various market technologies, allowing adaptation to different educational institutions and learning environments.

5. Implement security and privacy measures in the software, including data encryption and compliance with regulations such as Law 1581 on Personal Data Protection (Colombia), to protect sensitive student information and ensure ethical use of integrated technologies (Congress of Colombia, 2012).

### 1.4 Scope

**In scope (MVP - Horizon 1, 0-3 months):**
- User management with CSV and manual registration
- Login and authentication with opaque sessionId
- Password recovery and change
- Management of environments, cohorts, courses, and academic periods
- Check-in/check-out records FACIAL + MANUAL/IMPORT
- System and academic configuration
- Justification flow with supporting documents
- Histories and reports by date range/cohort
- Facial recognition with liveness detection
- Responsive frontend (Web + Mobile)

**Out of scope:**
- Teacher payroll, grades, payments
- Complete physical access control (doors/turnstiles)
- Integration with external systems (enrollments/grades/IdP)
- Advanced messaging (SMS/WhatsApp)
- Predictive dropout analytics
- IoT device failure alerts
- Separate native mobile apps (MVP is responsive web + mobile web)

### 1.5 Personnel Involved

| Name | Role | Responsibility |
|--------|-----|-----------------|
| Diego Andrés Gutiérrez Nuñez | Administrator, Developer, AI | Information analysis, design and programming |
| Juan David Arboleda Perdomo | Analyst, Database | Information analysis, design and programming |
| Jonattan Steven Rizo Solano | Hardware, Developer | Information analysis, design and programming |

### 1.6 Summary

The FaceAttend EDU project represents a complete technological solution that transforms attendance management in educational institutions through facial recognition, polyglot microservices architecture, and a unified frontend with React Native/Expo. The system is designed with professional quality standards, including IEEE 829, ISO 25010, ISO 29110, and ISTQB documentation (Institute of Electrical and Electronics Engineers [IEEE], 2008; International Organization for Standardization & International Electrotechnical Commission [ISO & IEC], 2011; International Organization for Standardization & International Electrotechnical Commission [ISO & IEC], 2016; International Software Testing Qualifications Board, 2018).

---

## 2. INTRODUCTION

### 2.1 Problem Statement

In the current educational environment, manual attendance registration presents multiple issues:

- *Inconsistencies in records:* Traditional methods (paper lists, roll calls) are prone to human error, forgetfulness, and manipulation.
- *Administrative overload:* Instructors spend valuable class time on administrative tasks instead of focusing on teaching.
- *Impersonation risk:* Without automated identity verification, it is easy for a student to register attendance for another.
- *Lack of traceability:* No centralized system exists to efficiently consult attendance histories.
- *Difficulty in report generation:* Manual data consolidation for reports is slow and error-prone.

### 2.2 Purpose

FaceAttend EDU emerges as a response to these issues, providing a platform that:

- Automates attendance registration through facial and biometric recognition
- Guarantees student identity through biometric verification with liveness detection
- Centralizes all attendance information in a structured database
- Generates automatic reports and real-time alerts
- Reduces instructor administrative burden
- Complies with data protection regulations (Law 1581)

### 2.3 Justification

The implementation of FaceAttend EDU is justified by:

1. *Operational efficiency:* Reduction of attendance registration time from minutes to seconds per student.
2. *Precision and security:* Elimination of human errors and identity impersonation through biometrics.
3. *Complete traceability:* Complete and auditable attendance histories.
4. *Academic improvement:* Early identification of absenteeism patterns affecting performance.
5. *Scalability:* Microservices architecture that allows growth with the institution.
6. *Regulatory compliance:* Alignment with Law 1581 on Personal Data Protection.

---

## 3. PROJECT DESCRIPTION

### 3.1 General Description

FaceAttend EDU is a web and mobile application designed for educational institutions seeking to optimize the attendance registration process through dual biometric technology: facial recognition using the device camera and fingerprint recognition using the DigitalPersona 4500 SDK. This system automates student presence verification and allows generating precise reports, improving academic management and reducing administrative burden.

The application integrates as a support tool for educational management, with functionalities adaptable to different academic levels and focused on usability, security, and efficiency. It is an autonomous solution, although it can be complemented with other institutional academic information systems.

### 3.2 Product Perspective

FaceAttend EDU is a system composed of:

- *Web and Mobile Frontend:* Responsive application built with React Native and Expo, compatible with modern browsers and iOS/Android mobile devices.
- *Backend:* 9 microservices specialized in different business domains.
- *API Gateway:* Kong OSS as the single entry point for all requests.
- *Databases:* PostgreSQL 17 (relational data) and MongoDB 7 (biometric data).
- *Messaging:* Apache Kafka for asynchronous communication between services.
- *Infrastructure:* Docker Compose for container orchestration.

### 3.3 User Characteristics

| User Type | Description | Main Needs |
|-----------------|-------------|------------------------|
| Learner/Student | Main user who registers attendance | Quick attendance registration, history consultation, absence justification |
| Instructor/Teacher | Responsible for managing attendance in their classes | Automatic attendance capture, report visualization, justification management |
| Administrator | Manages system configuration | User management, academic configuration, global report generation |
| Supervisor | Monitors academic compliance | Absenteeism alerts, consolidated reports, participation metrics |

### 3.4 Main Features

1. *Facial Attendance Registration:* Face capture with device camera (computer or mobile), liveness detection, and automatic registration with date/time.
2. *Fingerprint Attendance Registration:* Fingerprint capture via DigitalPersona 4500 SDK, in-code processing, and automatic registration with date/time.
3. *Manual/Import Registration:* Backup method for special cases or technical failures.
4. *User Management:* Complete CRUD of learners, instructors, and administrators.
5. *Academic Management:* Administration of campuses, programs, cohorts, courses, and periods.
6. *Justifications:* Complete flow of request, review, and approval/rejection of justifications.
7. *Reports and Dashboard:* Metrics visualization, Excel export, filters by date/cohort.
8. *Notifications and Alerts:* Automatic alert system for absences or anomalies.
9. *Configuration:* Academic, security, and biometric parameters.

---

## 4. REQUIREMENTS

### 4.1 Software Requirements

#### 4.1.1 Operating Systems

**Server:**
- *Linux:* Ubuntu Server 22.04 LTS, CentOS 8 or Debian 12 (recommended for production)
- *Windows:* Windows Server 2019/2022 (alternative)
- *Containers:* Docker 24+ and Docker Compose 2+

**Client (Web Browser):**
- Chrome 90+
- Firefox 88+
- Edge 90+
- Safari 14+
- Brave / Opera (any recent version)

**Mobile:**
- Android 8.0 (API 26) or higher
- iOS 14.0 or higher

#### 4.1.2 Backend Technologies

| Technology | Version | Use |
|------------|---------|-----|
| Java | 21 LTS | 4 main microservices |
| Spring Boot | 4.1.2 | Main Java framework |
| Spring Security | 7.x (managed by Spring Boot) | Authentication and authorization |
| Spring Data JPA | 4.x | Java persistence |
| TypeScript | 5.x (Node 24) | 4 microservices |
| Fastify | 5.x | TypeScript HTTP framework |
| pg (node-postgres) | 8.x | PostgreSQL driver for the TypeScript services |
| Python | 3.12 | Biometric microservice |
| FastAPI | 0.162 | Python framework |

#### 4.1.3 Frontend Technologies

| Technology | Version | Use |
|------------|---------|-----|
| Expo SDK | 57 | React Native framework |
| React | 19.2.3 | UI library |
| React Native | 0.86.3 | Mobile framework |
| React Native Web | 0.21 | Web support |
| React Navigation | 7.x | Navigation |
| @digitalpersona/fingerprint | 1.0.0 | DigitalPersona fingerprint reader |
| @digitalpersona/websdk | 1.1.0 | DigitalPersona Web SDK |
| axios | 1.16.1 | HTTP client |
| i18next | 26.0.4 | Internationalization |
| xlsx | 0.18.5 | Excel export |

#### 4.1.4 Databases

| Engine | Version | Use |
|-------|---------|-----|
| PostgreSQL | 17 | Main database (8 schemas) |
| MongoDB | 7 | Biometric vector storage |
| Redis | 7 | Cache and rate-limiting |

#### 4.1.5 Infrastructure and Tools

| Technology | Version | Use |
|------------|---------|-----|
| Docker | 24+ | Containers |
| Docker Compose | 2+ | Orchestration |
| Kong | 3.6 | API Gateway |
| Apache Kafka | 3.8 (KRaft) | Event messaging |
| Liquibase | 4.29.0 | Database migrations |
| OpenTelemetry | 1.24 | Observability |
| Jenkins | 2.x | CI/CD |

#### 4.1.6 Biometric Technology

| Component | Technology | Description |
|------------|------------|-------------|
| Facial Recognition | Device camera + face-api.js | Facial detection and recognition using computer or mobile camera |
| Fingerprint SDK | DigitalPersona 4500 SDK | Fingerprint capture and processing |
| Storage | MongoDB 7 | Facial embeddings and fingerprint templates with index |
| Facial Matching | Cosine similarity | 128-d vector comparison |
| Fingerprint Matching | DigitalPersona SDK | Fingerprint template comparison |

*Note:* The system uses two complementary biometric methods: facial recognition using the device camera (computer or mobile) and fingerprint recognition using the DigitalPersona 4500 SDK. All functionality will be implemented directly in code.

#### 4.1.7 Security and Validation

| Mechanism | Details |
|-----------|---------|
| Authentication | Opaque SessionId (UUID) - never JWT |
| Password Hashing | BCrypt with cost factor 12 |
| Authorization | RBAC (Role-Based Access Control) |
| Encryption | SSL/TLS for communications |
| Rate Limiting | Kong (5173, 8090, 3000 origins) |
| Biometric validation | Liveness detection (facial) + Fingerprint capture (DigitalPersona 4500) |
| Data protection | Law 1581 compliance |

### 4.2 Hardware Requirements

#### 4.2.1 Server

| Component | Minimum | Recommended |
|------------|--------|-------------|
| Processor | Intel i5 (8th gen) or AMD equivalent | Intel i7 (12th gen) or AMD Ryzen 7 |
| RAM | 8 GB | 16 GB |
| Storage | SSD 256 GB | SSD 512 GB |
| GPU | Not required | Not required |
| Network | Ethernet 100 Mbps | Ethernet 1 Gbps |

#### 4.2.2 Biometric Devices

| Type | Specification | Use |
|------|----------------|-----|
| Device camera | Webcam 720p+ (computer) or front camera (mobile) | Facial recognition |
| DigitalPersona 4500 | USB fingerprint reader with SDK | Fingerprint recognition |

*Note:* The system uses the device camera (computer or mobile) for facial recognition and DigitalPersona 4500 for fingerprint recognition. Both biometric methods work complementarily.

#### 4.2.3 Mobile Devices

| Platform | Requirements |
|------------|------------|
| Android | Android 8.0+, front camera 720p+, 2 GB RAM |
| iOS | iOS 14.0+, front camera 720p+, 2 GB RAM |

#### 4.2.4 IoT and Sensors (Future - Horizon 3)

| Device | Function |
|-------------|---------|
| Motion Sensors | Verify physical presence in the classroom |
| NFC/Proximity Sensors | Confirm student location |
| Raspberry Pi | Edge processing for IoT |

---

## 5. PROPOSED SOLUTION

### 5.1 Solution Overview

The proposed solution is a comprehensive software system based on a polyglot microservices architecture that uses market-available technologies to efficiently validate student attendance and effectively track their class participation.

### 5.2 Solution Pillars

1. *Automated Attendance Capture:* Dual biometric registration: facial (device camera) and fingerprint (DigitalPersona 4500 SDK).
2. *Reliable Academic Tracking:* Complete histories, automatic reports, and alerts.
3. *Transparent Justification Flow:* Complete process of request, review, and resolution.
4. *Security, Privacy, and Usability:* Regulatory compliance, robust authentication, and intuitive UX.
5. *Scalable and Compatible Operation:* Microservices architecture, Docker containers, and API Gateway.

### 5.3 Product Roadmap

| Horizon | Period | Deliverables |
|-----------|---------|-------------|
| Horizon 1 (MVP) | 0-3 months | Identity, Academic, Scheduling, Attendance core, Biometric facial enroll (camera) + fingerprint enroll (DigitalPersona 4500), Justification workflow, Notification in-app |
| Horizon 2 (Traceability) | 3-6 months | End-to-end justifications, dashboards, alerts, per-school configuration |
| Horizon 3 (Scale) | 6-12 months | Advanced analytics, multi-school tenancy, SDK optimization |

### 5.4 Design Principles

- *Precision over speed:* Recognition accuracy is paramount.
- *Privacy by design:* Biometric data treated as sensitive PII.
- *Offline-first (future):* Offline capture capability with later synchronization.
- *API-First:* OpenAPI contracts as source of truth.
- *Observability by Design:* Logs, metrics, and tracing from day one.

---

## 6. SYSTEM ARCHITECTURE

### 6.1 Architectural Style

The system implements a *Polyglot Microservices* architecture with *Hexagonal Architecture* (Ports and Adapters) and *Domain-Driven Design* (Bounded Contexts).

### 6.2 Container Diagram (C4)

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         CLIENTS                                        │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐      │
│  │   Web Browser    │  │  Mobile App      │  │  Tablet          │      │
│  │   (React Native  │  │  (Expo/React     │  │  (Expo/React     │      │
│  │    Web)          │  │   Native)        │  │   Native)        │      │
│  └────────┬─────────┘  └────────┬─────────┘  └────────┬─────────┘      │
└───────────┼─────────────────────┼─────────────────────┼────────────────┘
            │                     │                     │
            └─────────────────────┼─────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                      API GATEWAY                                        │
│  ┌──────────────────────────────────────────────────────────────────┐   │
│  │   Kong OSS 3.6 (DB-less)                                         │   │
│  │   Port: 8000 (proxy); admin API not published                 │   │
│  │   Plugins: CORS, Rate Limiting                                  │   │
│  └──────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                    MICROSERVICES                                       │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐       │
│  │ 01-Identity │ │02-Authoriz. │ │03-Academic  │ │04-Schedul.  │       │
│  │ Java 8081   │ │ Java 8082   │ │ TS 8083     │ │ Java 8084  │       │
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘       │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐       │
│  │05-Attendan. │ │06-Biometric │ │07-Config.   │ │08-Notif.    │       │
│  │ Java 8085   │ │ Python 8086 │ │ TS 8087     │ │ TS 8088     │       │
│  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘       │
│  ┌─────────────┐                                                      │
│  │ 09-Quality  │                                                      │
│  │ TS 8089     │                                                      │
│  └─────────────┘                                                      │
└─────────────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                      DATA                                              │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐         │
│  │ PostgreSQL 17   │  │ MongoDB 7       │  │ Redis 7         │         │
│  │ 8 schemas       │  │ Biometric       │  │ Kong cache      │         │
│  │ faceattend_db   │  │ embeddings      │  │                 │         │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘         │
│  ┌─────────────────┐                                                    │
│  │ Kafka 3.8       │                                                    │
│  │ Domain Events   │                                                    │
│  └─────────────────┘                                                    │
└─────────────────────────────────────────────────────────────────────────┘
```

### 6.3 Microservices Catalog

| # | Service | Port | Language | Framework | Database | Responsibility |
|---|----------|--------|----------|-----------|---------------|-----------------|
| 01 | ms-identity | 8081 | Java 21 | Spring Boot 4.1 | PostgreSQL | Persons, users, sessions, password policies |
| 02 | ms-authorization | 8082 | Java 21 | Spring Boot 4.1 | PostgreSQL | RBAC (roles, permissions, assignments) |
| 03 | ms-academic | 8083 | TypeScript | Fastify + pg | PostgreSQL | Campuses, programs, cohorts, courses, enrollments |
| 04 | ms-scheduling | 8084 | Java 21 | Spring Boot 4.1 | PostgreSQL | Environments, schedule blocks, class sessions |
| 05 | ms-attendance | 8085 | Java 21 | Spring Boot 4.1 | PostgreSQL | Attendance records, justifications, documents |
| 06 | ms-biometric | 8086 | Python 3.12 | FastAPI | MongoDB | Facial embeddings + fingerprint templates, matching |
| 07 | ms-configuration | 8087 | TypeScript | Fastify | PostgreSQL | Academic/security configuration, biometric cases |
| 08 | ms-notification | 8088 | TypeScript | Fastify | PostgreSQL | Alerts and notifications |
| 09 | ms-quality | 8089 | TypeScript | Fastify | PostgreSQL | ISO 25010/29110 quality evaluation, ISTQB |
| 99 | api-gateway | 8000 | - | Kong OSS 3.6 | Redis | Routing, auth, rate-limit, CORS |

### 6.4 Hexagonal Architecture

Each microservice follows the Ports and Adapters pattern:

```
┌─────────────────────────────────────────────────────────┐
│                   INFRASTRUCTURE                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │ HTTP        │  │ Repository  │  │ Event       │     │
│  │ Controllers │  │ Adapters    │  │ Publishers  │     │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘     │
│         │                │                │             │
│         └────────────────┼────────────────┘             │
│                          │                              │
│                          ▼                              │
│  ┌─────────────────────────────────────────────────┐   │
│  │                  APPLICATION                     │   │
│  │  ┌─────────────┐  ┌─────────────┐               │   │
│  │  │ Use Cases   │  │ DTOs        │               │   │
│  │  └─────────────┘  └─────────────┘               │   │
│  └────────────────────┬────────────────────────────┘   │
│                       │                                 │
│                       ▼                                 │
│  ┌─────────────────────────────────────────────────┐   │
│  │                    DOMAIN                        │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────┐  │   │
│  │  │ Aggregates  │  │ Value       │  │ Domain  │  │   │
│  │  │             │  │ Objects     │  │ Events  │  │   │
│  │  └─────────────┘  └─────────────┘  └─────────┘  │   │
│  │  ┌─────────────┐  ┌─────────────┐               │   │
│  │  │ Ports (In)  │  │ Ports (Out) │               │   │
│  │  └─────────────┘  └─────────────┘               │   │
│  └─────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

*Dependency rule:* `infrastructure → application → domain` (always inward).

### 6.5 Adopted Architectural Patterns

| Pattern | Status | Description |
|--------|--------|-------------|
| API Gateway (Kong OSS) | Implemented | Single entry point with CORS and rate-limiting |
| Database per Service (schema-per-context) | Implemented | Each bounded context has its own schema in PostgreSQL |
| Hexagonal Architecture | Implemented | Ports and adapters in all services |
| Message Broker (Kafka) | Implemented | Asynchronous communication between services |
| Circuit Breaker | Partial | Defined but not fully implemented |
| Saga (choreographed) | Partial | For distributed operations |
| Outbox Pattern | Partial | For reliable event publishing |
| Retry + Exponential Backoff + DLQ | Implemented | For communication resilience |
| CQRS | Not implemented | Future |
| Event Sourcing | Not implemented | Future |

### 6.6 Docker Networks

| Network | Members | Purpose |
|-----|----------|-----------|
| `faceattend-edge` | frontend-web, kong-gateway | External contact point |
| `faceattend-app` | kong-gateway, 9 ms-*, redis | Inter-service communication |
| `faceattend-data` | 9 ms-*, postgres, mongodb, kafka, liquibase | Data access |

---

## 7. DATA MODEL

### 7.1 Summary

- *29 SQL tables* in PostgreSQL 17 (8 schemas)
- *2 MongoDB collections* for biometric data (facial + fingerprint)
- *28 entities* + *4 Value Objects*
- *Normalization:* 3NF in the core with 0 intentional denormalizations

### 7.2 PostgreSQL Schemas

| Schema | Tables | Description |
|--------|--------|-------------|
| identity | person, app_user, user_session, password_policy | Identity and credentials |
| authorization | role, permission, role_permission, user_role | RBAC |
| academic | school, program, academic_period, cohort, course, academic_actor_type, academic_actor, enrollment | Academic structure |
| scheduling | environment, schedule_block, class_session | Schedules and sessions |
| attendance | attendance_record, justification_type, justification, supporting_document, attendance_report | Attendance |
| configuration | academic_configuration, security_configuration, biometric_update_case | Configuration |
| notification | alert_type, alert | Alerts |
| biometric | (empty - uses MongoDB) | Schema only |
| quality | quality_project, quality_evaluation, quality_evaluation_item, process_assessment, process_assessment_rating, istqb_assessment, istqb_assessment_item | Quality |

### 7.3 MongoDB Collections

| Collection | Key Fields |
|-----------|--------------|
| facial_embedding | person_id, template_version, encoding (128-d), model_version, enrolled_at, is_active |
| fingerprint_embedding | person_id, finger_number, template_version, encoding, model_version, enrolled_at, is_active |

### 7.4 Main Entities by Context

#### Identity
- *Person:* UUID, document_type, document_number (unique), primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, email, phone, birth_date, status
- *App_User:* UUID, username, password_hash, status, last_login_at, failed_attempts, locked_until
- *User_Session:* UUID, session_id (opaque), user_id, status, start_date, end_date, source_ip, user_agent
- *Password_Policy:* UUID, min_length, require_uppercase, require_lowercase, require_digit, require_special, max_age_days, history_count

#### Authorization
- *Role:* UUID, name, description, status
- *Permission:* UUID, name (format: context.resource:action), description
- *Role_Permission:* role_id, permission_id (composite PK)
- *User_Role:* user_id, role_id (composite PK)

#### Academic
- *School:* UUID, name, code, address, city_id, status
- *Program:* UUID, school_id, name, code, duration_hours, status
- *Academic_Period:* UUID, school_id, name, start_date, end_date, status
- *Cohort:* UUID, program_id, instructor_id, name, code, start_date, end_date, status
- *Course:* UUID, school_id, name, code, credits, status
- *Academic_Actor_Type:* UUID, name (INSTRUCTOR, LEARNER, etc.)
- *Academic_Actor:* UUID, person_id, actor_type_id, status
- *Enrollment:* UUID, cohort_id, learner_id, status, enrollment_date

#### Scheduling
- *Environment:* UUID, school_id, name, capacity, status
- *Schedule_Block:* UUID, environment_id, day_of_week, start_time, end_time
- *Class_Session:* UUID, cohort_id, course_id, environment_id, session_date, status (Open/Closed/Cancelled)

#### Attendance
- *Attendance_Record:* UUID, class_session_id, learner_id, status (Present/Absent/Late/Justified), recording_method (FACIAL/MANUAL/IOT/IMPORT), recorded_at, confidence_score
- *Justification_Type:* UUID, name, description, max_days, requires_document
- *Justification:* UUID, attendance_record_id, justification_type_id, reason, status (Pending/Approved/Rejected), reviewed_by, reviewed_at
- *Supporting_Document:* UUID, justification_id, file_name, file_path, file_size, mime_type, uploaded_at
- *Attendance_Report:* UUID, cohort_id, generated_at, filters (JSONB), result (JSONB)

#### Biometric
- *Facial_Embedding (MongoDB):* person_id, template_version, encoding (128-d array), model_version, enrolled_at, is_active
- *Fingerprint_Embedding (MongoDB):* person_id, finger_number, template_version, encoding, model_version, enrolled_at, is_active
- *Biometric_Update_Case (SQL):* UUID, person_id, update_type, status (Pending/In_Review/Approved/Rejected), requested_by, reviewed_by

#### Notification
- *Alert_Type:* UUID, name, severity (INFO/WARNING/CRITICAL), channel (DASHBOARD/EMAIL/PUSH)
- *Alert:* UUID, alert_type_id, user_id, title, message, status, created_at, resolved_at

#### Configuration
- *Academic_Configuration:* UUID, school_id, attendance_threshold, late_tolerance_minutes, justification_window_days
- *Security_Configuration:* UUID, session_timeout_minutes, max_failed_attempts, lockout_duration_minutes, password_policy_id

### 7.5 Design Rules

- *No FK between contexts* (only comments, references by UUID)
- *UUID* for cross-context entities
- *Soft delete* with `deleted_at`
- *Audit:* created_at, updated_at, deleted_at, created_by, updated_by, deleted_by, row_version
- *ENUMs* native to PostgreSQL
- *Naming:* snake_case, singular tables

### 7.6 Business Rules (Summary)

| ID | Rule |
|----|-------|
| RN-01 | App_User must have an associated Person |
| RN-07 | Every App_User must have at least one role |
| RN-14 | Cohort must have a valid instructor before registering attendance |
| RN-18 | Schedule_Block cannot overlap in the same environment |
| RN-22 | One attendance_record per actor per session |
| RN-23 | Attendance is only registered in sessions with Open status |
| RN-28 | Only one facial embedding and one active fingerprint template per person and finger |
| RN-32 | Justification is 1:1 with attendance_record |
| RN-36 | Approved justification → attendance_status = Justified |

---

## 8. API AND MICROSERVICES

### 8.1 API Gateway (Kong)

| Service | Routes | Description |
|----------|-------|-------------|
| identity-service | `/api/v1/persons`, `/api/v1/users`, `/api/v1/auth`, `/api/v1/sessions`, `/api/v1/password-policies` | Identity and authentication |
| authorization-service | `/api/v1/roles`, `/api/v1/permissions`, `/api/v1/user-roles` | RBAC |
| academic-service | `/api/v1/schools`, `/api/v1/programs`, `/api/v1/academic-periods`, `/api/v1/cohorts`, `/api/v1/courses`, `/api/v1/actor-types`, `/api/v1/academic-actors`, `/api/v1/enrollments` | Academic structure |
| scheduling-service | `/api/v1/environments`, `/api/v1/schedule-blocks`, `/api/v1/class-sessions` | Schedules |
| attendance-service | `/api/v1/attendance-records`, `/api/v1/attendance-reports`, `/api/v1/justifications`, `/api/v1/justification-types`, `/api/v1/supporting-documents` | Attendance |
| biometric-service | `/api/v1/biometric` | Dual biometrics: facial (camera) + fingerprint (DigitalPersona 4500) |
| configuration-service | `/api/v1/academic-configurations`, `/api/v1/security-configurations`, `/api/v1/biometric-update-cases` | Configuration |
| notification-service | `/api/v1/alert-types`, `/api/v1/alerts` | Alerts |
| quality-service | `/api/v1/quality` | Quality |

### 8.2 Detailed Biometric Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/biometric/facial/enroll` | Register face (device camera) |
| POST | `/api/v1/biometric/facial/verify` | Verify facial identity (1:1) |
| POST | `/api/v1/biometric/facial/identify` | Identify facial person (1:N) |
| GET | `/api/v1/biometric/facial/{person_id}` | Get active facial template |
| GET | `/api/v1/biometric/facial/{person_id}/history` | Facial template history |
| DELETE | `/api/v1/biometric/facial/{person_id}` | Delete facial template |
| POST | `/api/v1/biometric/fingerprint/enroll` | Register fingerprint (DigitalPersona 4500) |
| POST | `/api/v1/biometric/fingerprint/verify` | Verify fingerprint identity (1:1) |
| POST | `/api/v1/biometric/fingerprint/identify` | Identify fingerprint person (1:N) |
| GET | `/api/v1/biometric/fingerprint/{person_id}` | Get active fingerprint template |
| GET | `/api/v1/biometric/fingerprint/{person_id}/history` | Fingerprint template history |
| DELETE | `/api/v1/biometric/fingerprint/{person_id}` | Delete fingerprint template |
| WS | `/api/v1/biometric/ws` | WebSocket channel |

### 8.3 Authentication

*Mechanism:* `Authorization: Bearer <sessionId>` where sessionId is an opaque UUID (never JWT).

**Login Flow:**
1. `POST /api/v1/auth/login` with `{identifier (email|username), password}`
2. Server validates credentials with BCrypt
3. A `user_session` is created with UUID sessionId
4. Response: `{sessionId, userId, sessionStatus, startDate, endDate, sourceIp}`
5. Failures always return 401 "Invalid email or password" (no user enumeration)

**Session lifecycle:**
- 480-minute lifetime (`session_timeout_minutes`) from `start_date`; expiry is lazy: it is applied when the session is read.
- `POST /api/v1/auth/refresh` with `{sessionId}` rotates the session: it returns a new sessionId and closes the old one (single use; 401 if expired or already refreshed).
- `POST /api/v1/auth/logout?sessionId=<UUID>` closes the session (204).

**Validation:**
- Kong (edge): a global Lua *pre-function* requires `Bearer <UUID>` on every non-public route (public: login, login/verify, refresh, logout, forgot-password, verify-code, reset-password, `POST /api/v1/onboarding/schools` and health) and validates it with `GET /api/v1/sessions/{id}` against ms-identity, with a 10 s cache. No `jwt` plugin and no identity headers injected.
- Each service re-validates on its own: session against ms-identity (`/api/v1/sessions/{id}`) and permissions against ms-authorization (`/api/v1/users/{id}/roles` + `/api/v1/auth/evaluate?userId&permission`). Java (01/02/04/05): `AuthTokenFilter`; TypeScript (03/07/08/09): `authGuard.ts`; Python (06): `security.py`.

### 8.4 Domain Events

| Context | Events |
|----------|---------|
| Identity | UserRegistered, UserActivated, UserDeactivated, SessionStarted, PasswordChanged |
| Authorization | RoleAssigned, RoleRemoved, PermissionGranted, PermissionRevoked |
| Academic | SchoolRegistered, ProgramCreated, CohortRegistered, EnrollmentCreated, CourseAdded, CourseDeleted |
| Scheduling | EnvironmentRegistered, ScheduleBlockCreated, ScheduleBlockConflictDetected, ClassSessionOpened, ClassSessionClosed |
| Biometric | FacialEmbeddingEnrolled, FacialEmbeddingUpdated, FingerprintEmbeddingEnrolled, FingerprintEmbeddingUpdated, BiometricUpdateRequested, BiometricUpdateApproved, BiometricUpdateRejected |
| Attendance | AttendanceRecorded, LateArrivalDetected, AbsenceDetected, JustificationRequested, SupportingDocumentUploaded, JustificationApproved, JustificationRejected |
| Notification | AlertRaised, AlertResolved |

*Delivery guarantees:* At-least-once with idempotent handlers, Dead Letter Queue (DLQ) with 3-5 retries, exponential backoff, 7-day retention.

### 8.5 OpenAPI Contracts

All services have OpenAPI 3.0.3 specifications in `07-api/contracts/openapi/`:
- `01-identity-service.yaml`
- `02-authorization-service.yaml`
- `03-academic-service.yaml`
- `04-scheduling-service.yaml`
- `05-attendance-service.yaml`
- `06-biometric-service.yaml`
- `07-configuration-service.yaml`
- `08-notification-service.yaml`
- `09-quality-service.yaml`
- `api-gateway.yaml`

---

## 9. SECURITY

### 9.1 Authentication and Authorization

| Mechanism | Details |
|-----------|---------|
| Authentication | Opaque SessionId (UUID) - never JWT |
| Password Hashing | BCrypt with cost factor 12, automatic 16-byte salt |
| Authorization | RBAC with roles: Administrator, Instructor, Learner |
| Permission evaluation | `GET /api/v1/auth/evaluate?userId&permission` → `{allowed}` |
| Encryption | SSL/TLS for all communications |
| Rate Limiting | Kong with Redis (origins: localhost:3000, 5173, 8090) |

### 9.2 Data Protection

- *Biometric data as PII:* Facial embeddings and fingerprint templates are treated as sensitive personal information.
- *Law 1581:* Compliance with Colombian personal data protection regulations.
- *Consent:* Explicit consent required for biometric registration.
- *Encryption at rest:* Sensitive data encrypted in the database.
- *Encryption in transit:* All communications use HTTPS/TLS.

### 9.3 Biometric Validation

- *Liveness Detection (facial):* Liveness verification through blink and movement detection.
- *Fingerprint capture:* Via DigitalPersona 4500 SDK with fingerprint quality validation.
- *Biometric Rate Limiting:* Maximum 20 attempts per 60-second window.
- *Secure storage:* Facial embeddings and fingerprint templates encrypted in MongoDB.

### 9.4 Security Technical Debt

| ID | Description | Priority |
|----|-------------|-----------|
| AT-004 | ~~Bearer not validated in TS/Python services (03/06/07/08/09)~~ Resolved: `authGuard.ts` and `security.py` validate session and permissions | — |
| AT-005 | ~~Password recovery endpoints not implemented~~ Resolved: `forgot-password`, `verify-code` and `reset-password` in ms-identity | — |
| AT-007 | Kong does not use a JWT plugin: it validates the opaque session with a *pre-function* (current decision; a signed token is future evolution, ADR-007) | P2 |

---

## 10. INFRASTRUCTURE AND DEVOPS

### 10.1 Environments

| Environment | Description |
|---------|-------------|
| Development | Local Docker Compose with all dependencies |
| Staging | Production replica with test data |
| Production | Deployment with monitoring and alerts |

### 10.2 Ports

| Port | Service | Bind |
|--------|----------|------|
| 8000 | Kong Gateway (proxy) | 0.0.0.0 |
| 8090 | Web Frontend | 0.0.0.0 |
| 5432 | PostgreSQL | 127.0.0.1 |
| 27017 | MongoDB | 127.0.0.1 |
| 6379 | Redis | 127.0.0.1 |
| 9092 | Kafka | 127.0.0.1 |
| 8081-8089 | Microservices | 127.0.0.1 |

### 10.3 Main Environment Variables

```env
# PostgreSQL
POSTGRES_USER=postgres
POSTGRES_PASSWORD=YOUR_VALUE
POSTGRES_DB=faceattend_db
POSTGRES_PORT=5432

# MongoDB
MONGO_USER=mongoadmin
MONGO_PASSWORD=YOUR_VALUE
MONGO_DB=faceattend_biometric
MONGO_PORT=27017

# Redis
REDIS_PORT=6379

# Kafka
KAFKA_PORT=9092

# Kong
KONG_PROXY_PORT=8000
KONG_ADMIN_PORT=8001

# Frontend
FRONTEND_PORT=8090
EXPO_PUBLIC_API_URL=

# Biometrics
BIOMETRIC_FACIAL_MATCH_THRESHOLD=0.6
BIOMETRIC_FINGERPRINT_MATCH_THRESHOLD=8
BIOMETRIC_LIVENESS_TTL_SECONDS=120
BIOMETRIC_RATE_LIMIT_MAX_ATTEMPTS=20
BIOMETRIC_RATE_LIMIT_WINDOW_SECONDS=60
BIOMETRIC_LIVENESS_CHALLENGE_SECRET=YOUR_VALUE

# SMTP
SMTP_HOST=host.docker.internal
SMTP_PORT=1025
```

### 10.4 CI/CD

- *Jenkins* for continuous integration
- *GitHub Actions* for automated checks
- *Liquibase migrations* for database versioning
- *Docker* for consistent packaging

### 10.5 Observability

- *Logs:* Structured JSON (Pino for TS, structlog for Python)
- *Metrics:* RED (Rate, Errors, Duration)
- *Tracing:* OpenTelemetry for distributed tracing
- *Alerts:* Configured for response time < 5 min

---

## 11. WORK PLAN

### 11.1 Methodology

- *Framework:* Agile with 2-week sprints
- *Approach:* TDD (Test-Driven Development)
- *Documentation:* SDD (Software Design Documentation) - design before code

### 11.2 Project Phases

| Phase | Activities | Duration |
|------|-------------|----------|
| Phase 1: Discovery | Problem analysis, user research, benchmarking | Week 1 |
| Phase 2: Definition | Requirements definition, user stories, scope | Week 2 |
| Phase 3: Detailed Design | Architecture, data model, API contracts, ADRs | Weeks 2-3 |
| Phase 4: Implementation | TDD development of microservices and frontend | Weeks 3-10 |
| Phase 5: Testing | Unit, integration, E2E tests, IEEE 829 | Weeks 8-12 |
| Phase 6: Deployment | Production configuration, monitoring, go-live | Weeks 12-14 |

### 11.3 Gantt Chart

| Activity | Month 1 | Month 2 | Month 3 | Month 4 | Month 5 | Month 6 |
|-----------|-------|-------|-------|-------|-------|-------|
| Discovery and Analysis | ████ | | | | | |
| Design and Architecture | ████ | ████ | | | | |
| Backend Development | | ████ | ████ | ████ | | |
| Frontend Development | | ████ | ████ | ████ | | |
| Integration | | | | ████ | ████ | |
| Testing and QA | | | | ████ | ████ | ████ |
| Documentation | ████ | ████ | ████ | ████ | ████ | ████ |
| Deployment | | | | | | ████ |

### 11.4 Deliverables per Sprint

| Sprint | Deliverables |
|--------|-------------|
| Sprint 1 | Identity service, Authorization service, base data model |
| Sprint 2 | Academic service, Scheduling service, API Gateway |
| Sprint 3 | Attendance service, Biometric service (facial + fingerprint enroll) |
| Sprint 4 | Web Frontend (Login, Dashboard, user management) |
| Sprint 5 | Web Frontend (Attendance, reports), Biometric (identify/verify) |
| Sprint 6 | Justifications, Notifications, Mobile Frontend |
| Sprint 7 | Complete integration, E2E tests, documentation |
| Sprint 8 | Final adjustments, deployment, training |

---

## 12. WORK TEAM

### 12.1 Members

| Name | Role | Category | Responsibility | Contact |
|--------|-----|-----------|-----------------|----------|
| Diego Andrés Gutiérrez Nuñez | Administrator, Developer, AI | Software Analysis and Development Technologist Apprentice | Information analysis, design and programming | — |
| Juan David Arboleda Perdomo | Analyst, Database | Software Analysis and Development Technologist Apprentice | Information analysis, design and programming | — |
| Jonattan Steven Rizo Solano | Hardware, Developer | Software Analysis and Development Technologist Apprentice | Information analysis, design and programming | — |

### 12.2 Instructor

| Name | Role |
|--------|-----|
| Motta Vargas José de Jesús | SENA Instructor |

---

## 13. BUDGET

### 13.1 Human Resources

| Role | Monthly Cost (COP) | Time (months) | Total (COP) |
|-----|---------------------|----------------|-------------|
| Project Administrator | 8,000,000 | 6 | 48,000,000 |
| Software Developer (x2) | 4,000,000 | 6 | 48,000,000 |
| AI/Facial Recognition Specialist | 5,000,000 | 3 | 15,000,000 |
| Database Administrator | 10,000,000 | 3 | 30,000,000 |
| Hardware/IoT Engineer | 10,000,000 | 2 | 20,000,000 |
| TOTAL HUMAN RESOURCES | | | 161,000,000 |

### 13.2 Vendors and Technologies

#### Software

| Vendor | Detail | Cost (COP) |
|-----------|---------|-------------|
| OpenCV / face_recognition | Open Source Software | Free |
| Amazon Rekognition (alternative) | ~4,000 COP per 1,000 images | 40,000/month |
| Microsoft Azure Face API (alternative) | ~4,000 COP per 1,000 images | 40,000/month |

#### Hardware

| Vendor | Detail | Cost (COP) |
|-----------|---------|-------------|
| Logitech | 1080p cameras (x2) | 800,000 |
| Axis Communications | IP cameras (x2) | 2,400,000 |
| Dell/HP | Basic server | 6,000,000 |
| Raspberry Pi | IoT devices (x2) | 400,000 |

### 13.3 Budget Summary

| Item | Amount (COP) |
|----------|-------------|
| Human Resources | 161,000,000 |
| Hardware (economic option) | 7,200,000 |
| Software (Open Source) | 0 |
| ESTIMATED TOTAL | 168,200,000 |

*Estimated minimum budget:* $163,800,000 COP

---

## 14. QUALITY MANAGEMENT

### 14.1 Applied Standards

| Standard | Application |
|----------|------------|
| IEEE 829 | Software test documentation |
| ISO 25010 | Software product quality model |
| ISO 29110 | Processes for small software systems |
| ISTQB | Software testing certification |

### 14.2 Testing Strategy

| Test Type | Target Coverage | Tool |
|----------------|-------------------|-------------|
| Unit | ≥ 70% (≥ 80% domain) | JUnit, Jest, pytest |
| Integration | Critical endpoints | Supertest, TestContainers |
| E2E | Main flows | Detox, Manual |
| Performance | P95 < 2000ms | k6, JMeter |
| Security | OWASP Top 10 | Manual + tools |

### 14.3 Non-Functional Requirements

| ID | Category | Metric |
|----|-----------|---------|
| NFR-001 | Performance | P95 < 2000ms critical endpoints, P99 < 5000ms |
| NFR-002 | Availability | Health checks GET /health and /health/ready |
| NFR-003 | Scalability | Support 50% user increase in < 1 year |
| NFR-004 | Security | Opaque sessionId authentication, bcrypt cost 12, RBAC, HTTPS |
| NFR-005 | Compatibility | Chrome, Firefox, Edge, Safari, Brave, Opera |
| NFR-006 | Usability | Registration < 1 min, success rate ≥ 90%, WCAG 2.1 AA |
| NFR-007 | Observability | JSON logs, RED metrics, distributed tracing |
| NFR-008 | Maintainability | Coverage ≥ 70%, cyclomatic complexity ≤ 10 |
| NFR-009 | Portability | Docker images, environment variables |
| NFR-010 | Recovery | RTO < 5 min, RPO 0, daily backups |
| NFR-011 | Integrity | Unique record, immutable evidence, audit |

---

## 15. RISK MANAGEMENT

### 15.1 Identified Risks

| ID | Risk | Probability | Impact | Mitigation |
|----|--------|--------------|---------|------------|
| R-01 | Facial recognition failure under variable conditions | High | High | Liveness detection, multiple attempts, manual fallback |
| R-02 | User resistance to change | Medium | High | Training, intuitive interface, continuous support |
| R-03 | Connectivity issues in the classroom | Medium | High | Offline mode (future), manual backup registration |
| R-04 | Project scope growth | High | Medium | Clear MVP definition, roadmap by horizons |
| R-05 | Data regulation non-compliance | Low | High | Legal audit, encryption, explicit consent |
| R-06 | Accumulated technical debt | Medium | Medium | Continuous refactoring, code reviews |
| R-07 | Dependency on a single PostgreSQL | Medium | High | Migration plan to separate instances |

### 15.2 Registered Technical Debt

| ID | Description | Priority |
|----|-------------|-----------|
| AT-001 | Single PostgreSQL instance is SPOF | P1 |
| AT-002 | No import-boundary lint rule | P2 |
| AT-003 | ~~Go runtime adds 4th runtime for 2 tables~~ Resolved: ms-notification was rewritten in TypeScript | — |
| AT-004 | ~~Bearer not validated in TS/Python services~~ Resolved | — |
| AT-005 | ~~Password recovery endpoints not implemented~~ Resolved | — |
| AT-006 | No circuit breaker, transactional outbox, or Kafka DLQ | P1 |
| AT-007 | Kong does not use a JWT plugin: it validates the opaque session with a *pre-function* (see ADR-007) | P2 |

---

## 16. ANNEXES

### 16.1 Glossary of Terms

| Term | Definition |
|---------|------------|
| Bounded Context | Delimited context in DDD where a domain model is consistent |
| Liveness Detection | Verification that the captured face belongs to a living person |
| Embedding | 128-dimensional numerical vector representing facial characteristics |
| SessionId | Opaque identifier (UUID) of an `identity.user_session` row; used as the Bearer token and validated against ms-identity |
| RBAC | Role-based access control |
| Saga | Pattern for managing distributed transactions |
| DLQ | Dead Letter Queue - queue of messages that could not be processed |
| ADR | Architecture Decision Record - architectural decision document |
| TDD | Test-Driven Development - test-driven development |
| SDD | Software Design Documentation - software design documentation |

### 16.2 References

Congress of Colombia. (2012). *Statutory Law 1581 of 2012, which establishes general provisions for the protection of personal data* (Diario Oficial No. 48.587).

Institute of Electrical and Electronics Engineers. (2008). *IEEE standard for software and system test documentation* (IEEE Std 829-2008).

International Organization for Standardization & International Electrotechnical Commission. (2011). *Systems and software engineering — Systems and software quality requirements and evaluation (SQuaRE) — System and software quality models* (ISO/IEC 25010:2011). https://www.iso.org/standard/35733.html

International Organization for Standardization & International Electrotechnical Commission. (2016). *Systems and software engineering — Lifecycle profiles for very small entities (VSEs) — Part 1: Overview* (ISO/IEC TR 29110-1:2016).

International Software Testing Qualifications Board. (2018). *Certified Tester Foundation Level syllabus*. ISTQB.

### 16.3 Repository

- *Documentation:* `fae-docs/`
- *Source code:* `ProyectoFaceAttendEDU/`

---

**End of document**

*Document generated for the FaceAttend EDU project - SENA Software Analysis and Development 3145556*
