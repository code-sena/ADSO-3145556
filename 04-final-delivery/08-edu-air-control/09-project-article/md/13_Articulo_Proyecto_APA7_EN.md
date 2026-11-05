# EduAirControl: An IoT-Enabled Microservices Platform for Environmental Monitoring and Analysis of Educational Spaces

**Jhon Jairo Cortez Vargas · María de los Ángeles Olaya García**

Technologist in Software Analysis and Development, Group (Ficha) 3145556  
Servicio Nacional de Aprendizaje (SENA) Industria, Neiva, Huila, Colombia  
Instructor: Karol Daniela Correa  

## Abstract

Environmental conditions in educational spaces can influence the comfort and activities of students and teachers. However, information about temperature, humidity, air quality, and noise may not always receive sufficient attention in educational environments. This paper presents EduAirControl, a web and mobile platform designed to improve the management and analysis of environmental information associated with campuses, educational spaces, and sensors. The project uses an architecture based on an API Gateway and six microservices developed with Java and Spring Boot, together with persistence, messaging, caching, and security technologies. The solution includes a web interface developed with React, a mobile application based on React Native and Expo, and an Internet of Things (IoT) prototype composed of an ESP32 and environmental sensors (DHT11, MQ-135, and KY-038). The infrastructure is deployed on a virtual private server using Docker and Docker Compose, and incorporates tools for continuous integration, administration, and observability. The work includes technical documentation, API contracts, architectural decisions, and a test plan that distinguishes designed test cases from those actually executed. Public deployment represents a verifiable implementation milestone; however, comprehensive validation of security, performance, and the measurement flow from the physical prototype to the platform requires specific and up-to-date evidence. It is concluded that EduAirControl constitutes a technology-based proposal for environmental monitoring in educational settings, whose level of maturity must be assessed based on reproducible tests and the actual status of each component.

*Keywords:* environmental monitoring, Internet of Things, microservices, hexagonal architecture, Spring Boot, ESP32, educational spaces.

---

## Introduction

### The Problem

Classroom environmental conditions influence the comfort and performance of the people who use them. The review by Mendell and Heath (2005) relates indoor air pollutants and thermal conditions in schools to student performance, while Shield and Dockrell (2003) document the effects of noise on children in school settings. These studies provide a basis for understanding the importance of examining environmental conditions in educational spaces, although they do not, by themselves, demonstrate the effects of EduAirControl on its users.

Considering this context, there is a need for tools that make it possible to access and organize environmental measurements, associate them with the locations where they were obtained, and analyze their variations over time.

The problem addressed by EduAirControl is organized around five dimensions: access to information, centralization of measurements, understanding of environmental changes, traceability of the origin and capture time of measurements, and control of consultation and administration permissions. The platform aims to address these needs through a solution that integrates user interfaces, backend services, and an environmental data acquisition prototype.

### Research Motivation

The motivation was to build a system that would not only function but also be understandable, measurable, and maintainable. The project also serves as a case study within the SENA training program, where documentation, design decisions, and testing are as important as the code itself. In addition, the project sought to explore whether a low-cost IoT prototype could be integrated into a microservices architecture following professional software engineering practices.

Deploying the platform on a virtual private server represents an additional implementation stage because it makes it possible to evaluate component configurations outside the local development environment. This milestone also highlights the importance of maintaining consistency among source code, infrastructure, security, and technical documentation.

### Research Objectives

The **general objective** was to develop a web and mobile platform for managing, accessing, and analyzing environmental information from educational spaces through a microservices architecture and the progressive integration of IoT technology.

The **specific objectives** were:

- To implement authentication and authorization mechanisms that distinguish user permissions.
- To manage information about campuses, educational spaces, sensors, and devices.
- To design services for receiving, validating, storing, and retrieving environmental measurements.
- To develop web and mobile interfaces for displaying information and managing user preferences.
- To build an ESP32-based prototype for acquiring environmental variables.
- To document the architecture, API contracts, infrastructure, and installation procedures.
- To define and execute tests to evaluate the functionality, security, and integration of the components.

The achievement of these objectives must be determined through the results obtained for each component, distinguishing implemented functionality from functionality whose validation remains pending.

### Significance of the Topic

The Internet of Things enables physical objects to connect to software services in order to observe their surroundings (Atzori et al., 2010; Gubbi et al., 2013). In educational spaces, this capability can support data-informed decisions concerning ventilation, noise, and the use of facilities. Environmental monitoring in educational settings can facilitate the tracking of variables associated with indoor conditions and provide useful information for facility management. EduAirControl applies these principles by integrating sensors, backend services, and user-facing applications.

The current scope of the project focuses on managing and analyzing environmental information. Automatic physical control, prediction of future environmental conditions, and evaluation of impacts on academic performance are not considered demonstrated results and require additional development or research.

## Related Work

This section presents background literature supporting the main technical decisions made in EduAirControl. It is not intended to be a systematic literature review.

### Internet of Things and Environmental Monitoring

Atzori et al. (2010) examine different perspectives on the Internet of Things, while Gubbi et al. (2013) present a reference architecture integrating sensors, storage, processing, and application services.

EduAirControl adopts this approach by organizing environmental data acquisition, backend processing, and data access through web and mobile interfaces. The effective integration of these components must be verified through end-to-end testing.

### Microservices and Hexagonal Architecture

Fowler and Lewis (2014) describe microservices as small, independent services that communicate through lightweight mechanisms and can be deployed separately. Newman (2015) and Richardson (2018) discuss the trade-offs involved, including data consistency and asynchronous communication. To organize services internally, the project uses Cockburn's (2005) hexagonal architecture as a reference. This approach proposes separating the domain from input and output mechanisms. This organization is compatible with the principles of domain-driven design presented by Evans (2003).

Separation of responsibilities facilitates module evolution, but it also introduces additional requirements for configuration, security, observability, and integration testing.

### API Security

OAuth 2.0, described by Hardt (2012), and JSON Web Tokens (JWTs), specified by Jones et al. (2015), provide references for implementing authorization mechanisms and transmitting identity-related information.

In EduAirControl, security encompasses user authentication, operation authorization, and the protection of communications between components. It must also address device authentication and prevent unauthorized direct access to microservices.

The OWASP Foundation (2021) provides a reference for identifying common risks in web applications and APIs. Using it as a review framework does not replace security testing or automatically demonstrate that a platform is free of vulnerabilities.

### Software Quality and Testing

ISO/IEC 25010 (International Organization for Standardization and International Electrotechnical Commission [ISO/IEC], 2011) defines quality characteristics such as reliability, security, and maintainability. Martin (2008) argues that clean code reduces maintenance costs, while Forsgren et al. (2018) discuss the circuit breaker pattern as a mechanism for limiting the propagation of failures in distributed systems. In EduAirControl, resilience patterns must be distinguished according to whether they are documented, implemented, or verified through testing.

## Methodology

### Specification-Driven Development

The project was developed using an approach based on progressive documentation and specification. Artifacts were organized around context, domain, requirements, architecture, data, API contracts, microservices, DevOps, quality, and operations.

Design decisions were recorded through architecture decision records (ADRs), while interface contracts were documented using OpenAPI. The number of documented decisions and routes must be associated with the specific version of the documentation being evaluated.

This approach aims to maintain traceability among identified needs, design decisions, implementation, and testing.

### System Architecture

EduAirControl follows a client-server architecture based on independent services. The web and mobile applications consume HTTP/JSON interfaces exposed through an API Gateway, which centralizes request routing and can enforce access controls and other communication policies.

The backend is organized into six functional microservices and one API Gateway. These components manage security, educational spaces, environmental measurements, sensors, user information, and user experience preferences.

Data persistence is implemented using PostgreSQL, while Liquibase manages schema changes. RabbitMQ is used as a messaging technology, and Redis serves as an in-memory storage mechanism for functions that require it.

The database topology must be described according to the deployment configuration of the version being evaluated, distinguishing among PostgreSQL servers, databases, and schemas. The existence of multiple microservices does not necessarily imply that each one has a separate physical server or container.

### Technology Selection

The technology stack was selected to meet the needs of a distributed platform with web and mobile interfaces, persistence, and communication mechanisms between services.

**Table 1. EduAirControl Technology Stack**

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 4.0.5, Spring Data JPA |
| Security | Spring Authorization Server, JWT RS256, Google and Facebook sign-in |
| Data | PostgreSQL 15, Liquibase |
| Messaging and caching | RabbitMQ 3.13, Redis 7 |
| Web | React 19, Vite 8, React Router, i18next, Recharts |
| Mobile | Expo 54, React Native 0.81, AsyncStorage |
| Device communication | ESP32 and Bluetooth Low Energy (BLE) |
| Containers | Docker, Docker Compose |
| Integration and deployment | Jenkins and automation mechanisms configured for the repositories |
| Administration and observability | Container administration, metrics, visualization, and telemetry components, according to their actual operational status |

### Functional Modules

The backend is organized into the following components.

**Table 2. Backend Modules**

| Service | Responsibility | Port |
|---|---|---:|
| API Gateway | Routing, rate limiting through Redis, JWT RS256 validation, and BFF/OAuth2 functionality | 8080 |
| ms-security | Registration, login, OAuth2/OIDC, JWKS, roles, institutions, device tokens, and password recovery | 8081 |
| ms-classroom-management | Campuses, educational space types, and educational spaces | 3002 |
| ms-environment-monitoring | Measurements, dashboard, analysis, thresholds, and alerts | 3003 |
| ms-sensor-management | Catalogs of models, states, variables, and units; sensors and installations | 3004 |
| ms-user-experience | Favorites, preferences, ratings, searches, and notification status | 3006 |
| ms-user-management | User profiles, including name, phone number, institution, and avatar | 3007 |

### Security

User access is based on JWTs signed with RS256, with expiration and revocation mechanisms. When a user logs out, the token is registered in Redis, and subsequent reuse is rejected with an HTTP 401 response. Services validate signatures against the public key set (JWKS) provided by `ms-security`. Four roles separate administrative operations from consultation operations.

Devices use an `X-API-Key` credential that is displayed only once when it is created or rotated. Secrets are configured through environment variables, and CORS is configurable.

Security findings identified in previous audits must be treated as historical evidence until the current version has been reviewed. A vulnerability should only be considered verified as fixed when evidence demonstrates that the corresponding control works and the attack scenario can no longer be reproduced.

### IoT Prototype

The physical prototype uses an ESP32 as its controller and sensors intended to acquire environmental variables. The DHT11 measures temperature and relative humidity; the KY-038 provides a signal related to sound, and the MQ-135 responds to certain gases and compounds present in the environment.

KY-038 readings are not, by themselves, equivalent to calibrated acoustic measurements in decibels. Similarly, the MQ-135 should not be presented as a selective, calibrated CO₂ meter reporting parts per million without appropriate calibration and validation procedures.

The integration includes BLE communication for configuration functions that have been implemented in the mobile application. This communication must be distinguished from the transmission of measurements to the backend, since these are separate processes.

Prototype validation must establish which variables have been physically measured, under what test conditions, and whether the data reached the ingestion service and were stored correctly. Without this evidence, it is not possible to claim that the entire data flow is operational.

## Implementation

### Microservice Structure

The microservices are organized as backend components with distinct functional responsibilities. Hexagonal architecture serves as a reference for separating domain rules, use-case coordination, and input/output mechanisms.

Within this organization, the domain contains the service's business rules; the application layer coordinates use cases; and the infrastructure layer implements controllers, persistence adapters, and communication mechanisms.

### Persistence and Migrations

PostgreSQL provides persistence for the information managed by the services. Liquibase enables database structure changes to be versioned and executed, facilitating the tracking of schema modifications.

When Hibernate is configured with `ddl-auto=validate`, the application checks the correspondence between entities and the existing schema rather than using this setting to automatically generate all structural changes.

### Messaging and Consistency

RabbitMQ enables asynchronous communication between services through events. This mechanism can reduce coupling between producers and consumers, although it requires defined procedures for failure handling, retries, and duplicate events.

The outbox pattern, described by Richardson (2018), makes it possible to record an event in the same transaction as the related data changes and publish it later. Its use in a service must be confirmed by reviewing the event-recording implementation, the publishing process, and consumer behavior.

### Measurement Flow

The environmental information flow consists of the following steps:

1. Sensors capture environmental variables according to their technical capabilities.
2. The ESP32 obtains and prepares readings from the prototype.
3. Communication with the mobile application enables the BLE functions that have been implemented, without assuming that this mechanism replaces data transmission to the server.
4. The device transmits measurements to the ingestion endpoint when the connection and corresponding firmware are available.
5. The backend validates the request and device authentication, processes the information, and stores it when the operation is valid.
6. The web and mobile applications query the services to display the available information.

### Deployment

The local development environment uses Docker Compose to organize application containers and their dependencies. The production configuration uses a Hostinger virtual private server, where Docker and Docker Compose run the deployed components.

The infrastructure includes a web entry point, backend services, data storage, and administration tools. It also includes CI/CD automation work using Jenkins and components intended for administration and observability, such as Portainer, Prometheus, Grafana, and OpenTelemetry.

The presence of these tools must be distinguished from their degree of integration. To describe a function as operational, it is necessary to verify, as applicable, pipeline execution, metric collection, telemetry reception, and data visualization.

## Testing and Validation

### Testing Strategy

The project includes a plan for unit, integration, contract, functional, system, IoT, regression, and non-functional testing. The plan establishes techniques such as equivalence partitioning, boundary value analysis, negative testing, role-based verification, and black-box testing.

Each test case must record its result using a defined status, such as passed, failed, blocked, not executed, or not applicable. This classification prevents designed test cases from being confused with tests that have actually been performed.

### Automated Testing

The historical source-code inventory recorded 333 test methods distributed across 48 classes in the version analyzed in October 2026. This figure represents a count of source-code test methods and must not be interpreted as the number of tests that passed.

To update this result, the evaluated commit must be identified, applicable tests must be executed, and results must be recorded for each service. Unit tests, tests requiring the Spring application context, and tests dependent on containers or other services must also be distinguished.

**Table 3. Automated Tests by Service**

| Service | Classes | Methods |
|---|---:|---:|
| ms-environment-monitoring | 15 | 107 |
| ms-security | 11 | 77 |
| ms-sensor-management | 7 | 55 |
| ms-classroom-management | 4 | 33 |
| API Gateway | 6 | 29 |
| ms-user-management | 2 | 20 |
| ms-user-experience | 3 | 12 |
| **Total** | **48** | **333** |

### Manual Testing

Manual tests must verify web and mobile interface functions, role-based behavior, navigation, preferences, and operations for managing educational spaces and sensors.

For the IoT component, scenarios should include sensor disconnection, connectivity loss, denied BLE permissions, and failures when transmitting data to the API.

The number of manual test cases and their status must be obtained from the current execution record. Counts from previous versions must not be reused as current results without a new verification.

### Acceptance Criteria

**Table 4. Acceptance Criteria and Measurement Status**

| Indicator | Criterion | Measurement status |
|---|---|---|
| Health check | Less than 1 second | Pending |
| Login | Less than 2 seconds | Pending |
| Dashboard with test data | Less than 3 seconds | Pending |
| HTTP 5xx errors | Zero in the critical workflow | Pending |
| Availability | 100% of the planned period | Pending |

*Note.* Pending means that the measurement has not yet been performed.

## Results

The results are presented as of October 10, 2026. No performance metrics are reported because they have not yet been measured.

### Deliverables

The project has progressed from a local development environment to infrastructure deployed on a virtual private server. The platform has a public web address and components running through Docker.

During checks performed in October 2026, the frontend was verified to respond successfully to an HTTP request. This result provides evidence that the web interface was published, but it does not replace functional testing of login, APIs, microservices, or integration with the IoT prototype.

The evaluation should record the date, commit, and tests used to verify the status of each component.

### Architecture Implementation

Organizing the backend into an API Gateway and six microservices makes it possible to distribute the platform's functional responsibilities. Technical documentation and API contracts provide a basis for analyzing interfaces and interactions among components.

The architecture should be evaluated by considering both the separation of responsibilities and the additional requirements for configuration, communication, security, and maintenance. The effective independence of services and the consistency of their integration mechanisms require specific verification.

### Testing Status

The project has a test plan and a historical inventory of automated tests. However, the number of test methods does not constitute evidence that all tests pass in the deployed version.

Previous reports identified issues in certain integration test executions. These issues must be treated as historical findings until their status is verified through a new test run.

To present updated results, the report should include at least the version evaluated, the test cases executed, the results obtained, the defects identified, and the pending cases. Performance and availability metrics should only be reported when reproducible measurements are available.

### IoT Integration Status

The physical prototype is the component intended to acquire environmental information. Validation of this component must distinguish between readings obtained locally, transmission to the backend, and visualization in the applications.

End-to-end integration will be considered verified only when evidence shows that an identifiable measurement is captured, transmitted, validated, stored, and successfully retrieved. Until such evidence is available, the result must be described according to the stage that has actually been verified.

### Risk Register

Previous audits identified risks related to access control, secret protection, security configuration, test coverage, and observability.

These findings provide a reference for guiding the technical review, but they must not be presented as a current inventory without checking the present code and configuration.

For each finding, the risk register should indicate its description, severity, affected component, corrective action, verification evidence, and status. A vulnerability should only be declared closed when evidence shows that the identified scenario has been corrected and validated.

## Discussion

### Interpretation

EduAirControl integrates a web application, a mobile application, backend services, and a prototype for acquiring environmental information. This combination makes it possible to study the application of a microservices architecture to a specific need in educational environments.

Deploying the platform on remote infrastructure represents progress beyond an implementation limited to the local environment. However, the availability of the interface alone does not demonstrate the overall quality of the system or the correct operation of all services.

The project also demonstrates that the documented architecture and deployed infrastructure must remain synchronized. Differences among configurations, code versions, and dependencies can affect test reproducibility and service behavior. Furthermore, separating the system into microservices facilitates the distribution of responsibilities but increases the complexity of communication, credential management, and observability. Therefore, quality must be evaluated by considering both individual component characteristics and their interactions.

### Lessons Learned

One of the main lessons from the project is the importance of distinguishing between design, implementation, and validation. A function described in the requirements or partially present in the code is not necessarily a function that has been tested under real usage conditions.

The project also highlights the need to record evaluated versions, keep documentation up to date, and run tests after infrastructure changes. This procedure makes it possible to associate results with a specific configuration and facilitates the identification of regressions.

Finally, hardware and software integration requires validation of every stage of the data flow. Sensor readings, communication with the mobile device, and transmission to the backend must be analyzed separately before claiming that the entire system is operational.

### Threats to Validity

This work has three main threats to validity. First, conclusions about performance and availability depend on reproducible measurements. If these measurements have not been conducted, it is not possible to claim that the established criteria have been met. Second, prototype validation is not equivalent to a long-term evaluation in real classrooms. Installation conditions, environmental variability, and sensor characteristics can affect readings. Third, a static test inventory does not guarantee that the tests pass. Test cases must be executed against the evaluated version, and their results must be recorded.

Finally, the existence of security mechanisms does not replace a specific vulnerability assessment or, by itself, demonstrate the absence of risks.

## Conclusions

### Summary of Findings

EduAirControl is a technology platform aimed at managing and analyzing environmental information in educational spaces. Its architecture integrates web and mobile interfaces, backend services, and an IoT prototype, supported by container-based infrastructure.

Remote deployment represents progress in the project's implementation. Evaluating its overall operation also requires up-to-date evidence concerning APIs, persistence, security, and the transmission of measurements from physical devices.

### Contributions

The practical contribution of the project consists of building a platform for managing environmental information in educational spaces and implementing an IoT prototype associated with this purpose.

The technical contribution includes organizing the backend into functional services, using persistence and messaging technologies, and documenting interfaces and architectural decisions.

The methodological contribution consists of establishing a testing process that distinguishes defined test cases from executed and passed tests, while also recording risks and limitations identified during development.

### Limitations

The evaluation must establish the status of the complete integration among the sensors, ESP32, BLE communication, ingestion service, and consultation interfaces. It must also determine the degree of calibration of the sensors used and the accuracy of the variables obtained.

Conclusions about performance, scalability, availability, and security must be limited to properties that have actually been verified. Evaluation in additional real classrooms, load testing, and comprehensive verification of recovery mechanisms require further evidence whenever these activities have not yet been conducted.

### Future Work

In the short term, the project proposes completing the pending IoT integration checks, executing test cases against an identified version, and reviewing the security controls of services and devices.

In the medium term, the project proposes expanding automated testing for the web and mobile applications, measuring code coverage, and verifying the integration of observability mechanisms and CI/CD processes. It also proposes evaluating the suitability of the sensors used, calibrating or replacing the MQ-135 when measurements of CO₂ with known accuracy are required, and studying resilience mechanisms for distributed services.

In the long term, the project proposes conducting a controlled evaluation in real classrooms over a sufficient period to analyze environmental variations, prototype stability, and the usefulness of the data for educational facility management.

### Final Reflection

The development of EduAirControl demonstrates the importance of integrating design, implementation, documentation, and validation in a software project. Publishing a platform is a significant milestone, but claims about its quality must be supported by verifiable tests and measurements.

Distinguishing implemented capabilities from those that still require validation makes it possible to describe the project rigorously and establish an evidence-based improvement roadmap.

## References

Atzori, L., Iera, A., & Morabito, G. (2010). The Internet of Things: A survey. *Computer Networks, 54*(15), 2787–2805. https://doi.org/10.1016/j.comnet.2010.05.010

Cockburn, A. (2005). *Hexagonal architecture*. https://alistair.cockburn.us/hexagonal-architecture/

EduAirControl Team. (2026a). *EduAirControl technical manual* (Version 1.1) [Technical document]. SENA Industria.

EduAirControl Team. (2026b). *EduAirControl test plan* (Version 1.0) [Technical document]. SENA Industria.

EduAirControl Team. (2026c). *Project risk register* [Technical document]. SENA Industria.

Espressif Systems. (n.d.). *ESP32 series datasheet*. https://www.espressif.com/

Evans, E. (2003). *Domain-driven design: Tackling complexity in the heart of software*. Addison-Wesley.

Forsgren, N., Humble, J., & Kim, G. (2018). *Accelerate: The science of lean software and DevOps*. IT Revolution Press.

Fowler, M., & Lewis, J. (2014). *Microservices: A definition of this new architectural term*. https://martinfowler.com/articles/microservices.html

Gubbi, J., Buyya, R., Marusic, S., & Palaniswami, M. (2013). Internet of Things (IoT): A vision, architectural elements, and future directions. *Future Generation Computer Systems, 29*(7), 1645–1660. https://doi.org/10.1016/j.future.2013.01.010

Hardt, D. (Ed.). (2012). *The OAuth 2.0 authorization framework* (RFC 6749). Internet Engineering Task Force. https://doi.org/10.17487/RFC6749

International Organization for Standardization, & International Electrotechnical Commission. (2011). *Systems and software engineering—Systems and software quality requirements and evaluation (SQuaRE)—System and software quality models* (ISO/IEC 25010:2011).

Jones, M., Bradley, J., & Sakimura, N. (2015). *JSON Web Token (JWT)* (RFC 7519). Internet Engineering Task Force. https://doi.org/10.17487/RFC7519

Martin, R. C. (2008). *Clean code: A handbook of agile software craftsmanship*. Prentice Hall.

Mendell, M. J., & Heath, G. A. (2005). Do indoor pollutants and thermal conditions in schools influence student performance? A critical review of the literature. *Indoor Air, 15*(1), 27–52. https://doi.org/10.1111/j.1600-0668.2004.00320.x

Newman, S. (2015). *Building microservices: Designing fine-grained systems*. O’Reilly Media.

Nygard, M. T. (2018). *Release it! Design and deploy production-ready software* (2nd ed.). Pragmatic Bookshelf.

OpenAPI Initiative. (2021). *OpenAPI specification* (Version 3.1.0). https://spec.openapis.org/oas/v3.1.0

OWASP Foundation. (2021). *OWASP Top 10: 2021*. https://owasp.org/Top10/

Richardson, C. (2018). *Microservices patterns: With examples in Java*. Manning Publications.

Shield, B. M., & Dockrell, J. E. (2003). The effects of noise on children at school: A review. *Building Acoustics, 10*(2), 97–116.

## Appendix

**Data:** The figures come from the project's source code (test inventory), test execution reports dated October 7, 2026, and the risk register. No real personal data were used.

**Code:** Project repository, referenced in the `15_Enlace_Repositorio_GitHub.txt` file included with the submission; version (commit) to be recorded.

**Environment:** Windows. Previous reports indicate JDK 25.0.2, while the POM files declare Java 17. Docker and Node versions must be recorded.

**Appendices:** OpenAPI contracts, ADRs, infrastructure files, risk register, screenshots, and test inventory (folder `14_Anexos`).