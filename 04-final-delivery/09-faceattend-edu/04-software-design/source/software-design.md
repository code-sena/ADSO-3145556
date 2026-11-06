---
title: "Architectural Design Documentation"
authors: Jonattan Steven Rizo Solano, Diego Andres Gutierrez Nuñez, Juan David Arboleda Perdomo
abstract: |
  This document details the technical architecture of the FaceAttendEDU system, describing the architectural model, the implemented design patterns, the data model, and the technological stack used. It explains the application of Hexagonal Architecture and Domain-Driven Design (DDD) principles to ensure the scalability, maintainability, and security of the system. The document serves as a technical guide for developers, auditors, and software architects, ensuring that the technical implementation is aligned with the strategic and functional objectives of the project.
keywords:
  - architectural design
  - hexagonal architecture
  - design patterns
  - data model
  - microservices
  - DDD
---

# Introduction

The architectural design of FaceAttendEDU represents the translation of functional and non-functional requirements into a robust technical structure. Unlike the software analysis, which defines the "what" and "why", this document focuses on the "how": code organization, data management, communication between components, and technology selection.

The system has been conceived under a **Polyglot Modular Monolith** approach (organized in a monorepo), where each module operates as an independent microservice with its own bounded context, allowing the coexistence of multiple programming languages based on technical needs (Java, TypeScript, Python, and Go).

## Document Objective
To provide a detailed and formal description of the system architecture, the applied design patterns, and the data model, establishing the technical base for implementation, deployment, and software evolution.

## Document Scope
This document covers the back-end services architecture, the front-end structure, the data persistence strategy, API management via Gateway, and the information flow between layers.

# Architectural Model

## Hexagonal Architecture (Ports and Adapters)
FaceAttendEDU implements **Hexagonal Architecture**, whose primary goal is to isolate the business logic (the core) from external technologies (frameworks, databases, APIs). This separation ensures that the system is testable and that changes in the infrastructure do not affect the business rules.

The internal structure of each service is divided into three main layers:

### 1. Domain Layer (The Core)
The center of the hexagon and the most stable part of the system.
- **Entities and Value Objects**: Represent business concepts (e.g., `User`, `AttendanceRecord`).
- **Ports**: Interfaces that define how the domain interacts with the outside. There are input ports (for use cases) and output ports (for persistence or external services).
- **Business Rules**: Pure logic that does not depend on any external library.

### 2. Application Layer
Acts as the system orchestrator.
- **Use Cases**: Implement the application logic. They receive data from input adapters, coordinate the domain, and return a response.
- **Orchestration**: They do not contain complex business rules but instead direct the data flow between the domain and infrastructure.

### 3. Infrastructure Layer (Adapters)
Contains the concrete implementations of the ports.
- **Input Adapters (Driving Adapters)**: REST Controllers (Spring Boot, FastAPI, etc.) that receive HTTP requests and translate them into use case calls.
- **Output Adapters (Driven Adapters)**: Persistence implementations (JPA, MongoDB, pgx) that translate domain needs into database queries.

## Information Flow
A typical request flow follows this route:
`Client` → `Kong Gateway` → `Controller (Infrastructure)` → `Use Case (Application)` → `Domain Entity/Port (Domain)` → `Persistence Adapter (Infrastructure)` → `Database`.

# Design Patterns

To ensure code quality and ease of maintenance, the following patterns have been applied:

## Structural and Behavioral Patterns
- **Repository Pattern**: Used to abstract the data layer. The domain defines an interface (port) and the infrastructure implements it, allowing the database to be changed without affecting the business logic.
- **Use Case / Interactor**: Each system functionality is encapsulated in a unique use case class, facilitating traceability and unit testing.
- **Data Transfer Object (DTO)**: Specific objects are used for data transport between the API and the application, avoiding exposing domain entities directly to the client.
- **Mapper Pattern**: Components responsible for transforming domain entities into DTOs and vice versa, maintaining the purity of the domain layer.
- **API Gateway**: Implemented using **Kong 3.6**, centralizing routing, rate limiting, CORS and edge authentication: a Lua *pre-function* requires `Authorization: Bearer <sessionId>` (opaque UUID, not a JWT) and validates it against ms-identity (`GET /api/v1/sessions/{id}`, 10 s cache). Permission checks are evaluated in each microservice.

## Data and Messaging Patterns
- **Database-per-Service**: Each microservice has its own PostgreSQL schema, eliminating physical dependencies (FKs) between contexts and ensuring loose coupling.
- **Event-Driven Architecture (EDA)**: Use of **Apache Kafka** for asynchronous communication. Services publish domain events (e.g., `attendance-events`) that can be consumed by other modules in the future.
- **Polyglot Persistence**: Selection of the database based on the data type:
    - **PostgreSQL**: For structured and relational data.
    - **MongoDB**: For storing biometric embeddings (facial and fingerprint vectors).
    - **Redis**: For caching and traffic control in the Gateway.

# Data Model

The system uses a distributed data architecture based on logical schemas linked by UUIDs.

## Main Entities and Relationships
### Identity and Authorization Context
- **User**: Central entity linked to credentials and sessions.
- **Role & Permission**: Implementation of **RBAC (Role-Based Access Control)**. A role groups multiple permissions, and a user can have one or more roles.

### Academic and Attendance Context
- **Academic Actor**: Represents the relationship between a person and their role in the school (student, instructor).
- **Course (Ficha) & Program**: Hierarchical structure of the academic offer.
- **Attendance Record**: Registers entry/exit, linking the user, the IoT device, and the timestamp.
- **Justification**: Entity linked to an attendance record, allowing the upload of digital supporting documents.

## Persistence Strategy
| Data Type | Technology | Justification |
|---|---|---|
| Relational Data | PostgreSQL 17 | ACID consistency and support for complex schemas. |
| Biometric Embeddings | MongoDB 7 | High efficiency in searching and storing high-dimensional vectors. |
| Sessions and Rate Limit | Redis 7 | Low latency for real-time validations. |

# Technical Information and Technological Stack

The success of the FaceAttendEDU project is based on a technological selection oriented towards performance and specialization.

## Technological Stack
| Component | Technology | Version | Purpose |
|---|---|---|---|
| **Backend (Core)** | Java / Spring Boot | 21 / 4.1.1 | Identity and authorization services. |
| **Backend (Biometrics)** | Python / FastAPI | 3.12 / 0.110 | Biometric vector processing. |
| **Backend (Notifications)** | TypeScript / Fastify (Node.js) | 5.4 / 4.26 (Node 20) | High concurrency in alerts (async I/O). |
| **Backend (TS)** | TypeScript / Fastify | 5.4 / 4.26.0 | Lightweight and fast services. |
| **Frontend** | React Native / Expo | RN 0.86 / Expo 57 | Multiplatform mobile app. |
| **Gateway** | Kong | 3.6 | API Orchestration and Security. |
| **Messaging** | Apache Kafka | 3.8.0 | Asynchronous communication. |
| **Migrations** | Liquibase | 4.29.0 | Database version control. |

## Technical Success Factors
1. **Total Decoupling**: Thanks to Hexagonal Architecture, the system can evolve its frameworks without rewriting the business logic.
2. **Robust Security**: Opaque sessions (`sessionId` UUID persisted in `identity.user_session`) are validated twice: at Kong and again in each microservice (session against ms-identity and permissions against ms-authorization), so no request reaches business logic without being validated.
3. **Polyglot Persistence**: The use of MongoDB for biometrics and PostgreSQL for administration optimizes performance according to the nature of the data.
4. **Modular Scalability**: The microservices structure allows independent scaling of the attendance module (high load) and the configuration module (low load).

# Conclusions

The architectural design of FaceAttendEDU is not just a technical choice, but a strategy to mitigate the risks identified in the analysis phase. The adoption of Hexagonal Architecture and DDD allows the system to be resilient to change and easy to audit. The combination of specialized languages (Java for robustness, Python for AI/Biometrics, Go for speed) ensures that each component is implemented with the most efficient tool for its purpose.

**Author's Note.** Jonas is the author and technical lead of the architectural design. Technical correspondence can be directed to jonas@consultoria.example.

# References

American Psychological Association. (2020). *Publication manual of the American Psychological Association* (7th ed.). https://doi.org/10.1037/0000165-000

International Organization for Standardization & International Electrotechnical Commission. (2011). *Systems and software engineering — Systems and software quality requirements and evaluation (SQuaRE) — System and software quality models* (ISO/IEC 25010:2011). https://www.iso.org/standard/35733.html

Evans, E. (2003). *Domain-Driven Design: Tackling Complexity in the Heart of Software*. Addison-Wesley.

Alistair Cockburn. (2005). *Hexagonal Architecture (Ports and Adapters)*.
