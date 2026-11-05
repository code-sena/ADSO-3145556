# EnergyMonitor Project Charter

Juan Esteban Cortés Parra and Miguel Ángel García Artunduaga

Servicio Nacional de Aprendizaje (SENA)

Technologist in Software Analysis and Development (ADSO), group (ficha) 3145556

October 16, 2026 (updated October 28, 2026)

## Version Control

| Version | Date       | Description of change                                                                                      | Authors                                       |
|---------|------------|------------------------------------------------------------------------------------------------------------|-----------------------------------------------|
| 1.0     | 10/16/2026 | First version of the project charter: justification, objectives, scope, budget, schedule and team.         | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.1     | 10/20/2026 | Alignment with the deployed system: account administration in scope, budget with the production server and the estimated total cost, go-live at `https://energy-monitor.tech` with continuous integration and deployment, and status of objectives and milestones as of October 20. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.2     | 10/24/2026 | Scope components according to the architecture in production: users, devices and measurements as independent services behind an API gateway, and migration of the remaining modules in progress. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.3     | 10/28/2026 | Migration completed: the API gateway and the seven services in production, and the monolithic backend retired. Registration with prior email verification and acceptance of the terms, rules for accounts created with Google (no password change and deletion with a code sent by email) and independent editing of the first and last name. Milestone status and final-phase plan as of October 28. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.4     | 10/28/2026 | In-app help (documentation by role, support for users and news that update themselves), profile photos visible in the page and in homes, unusual consumption alert for a device and an explanation of push in Brave. | Cortés Parra, J. E.; García Artunduaga, M. A. |

## Abstract

This document is the project charter of EnergyMonitor, a solution that lets households see the electrical consumption of each appliance, identify the periods of highest demand, receive alerts and get saving recommendations. It defines why the project is being done and the value it provides, its objectives with measurable indicators, the scope included and excluded, the budget, the phases, the milestones and the schedule until the delivery date, October 28, 2026, and the work team with its roles. The solution integrates a measuring device that attaches to each appliance, a web application and an Android mobile application. As of October 28, 2026 the system is deployed in production at `https://energy-monitor.tech`, split into an API gateway and seven independent services, and is updated through continuous integration and deployment. The detail of the functions is developed in the requirements specification.

*Keywords:* project charter, electrical consumption, energy saving, households, alerts

## Introduction and Justification

### Project Context

EnergyMonitor lets households see the electrical consumption of each appliance, identify the periods of highest demand, receive alerts and get recommendations to save energy. Measurement is done by a purpose-built device that attaches to the appliance and sends real readings of its consumption. Users consult the data from a web application and an Android mobile application.

The project is a formative project of the Technologist in Software Analysis and Development (ADSO) program at SENA, developed by two trainees and due on October 28, 2026.

### Problem Addressed

High energy consumption in Colombian households is largely caused by a lack of energy awareness. People do not know which appliances consume the most or how their consumption varies throughout the day, so they cannot correct habits such as leaving a device on unnecessarily or keeping an appliance that consumes more than it should. The result is high bills and a greater environmental impact.

### Justification

The project starts from the premise that most households have no tools that show their consumption in real time or identify which appliances weigh most on the bill. A system that measures the consumption of each appliance, warns when reasonable limits are exceeded and proposes saving actions based on measured data supports informed decisions and encourages responsible and sustainable use of electricity. Measuring with a real sensor, rather than estimating, is what allows alerts and recommendations to be based on the actual behavior of each household.

### Expected Benefits

- For households: knowing which appliances use the energy, reacting in time to excessive consumption and making saving decisions with their own data, which helps control the cost of the bill.
- For the environment: reducing unnecessary electricity consumption and, with it, its environmental impact.
- For the training program: a complete and demonstrable system, from measurement on a real appliance to the notice on the user's phone.

## Project Objectives

### General Objective

Develop an energy resource control system that monitors household electrical consumption in real time and identifies the appliances that generate the highest expense, in order to promote efficient and responsible use of electricity.

### Specific Objectives

Each specific objective has a measurable indicator and an achievable target within the project timeframe. Table 1 presents them together with their coverage as of October 20, 2026.

**Table 1**

*Specific Objectives, Indicators and Coverage*

| Code | Objective | Indicator and target | Coverage as of October 20, 2026 |
|---|---|---|---|
| OBJ-01 | Measure the electrical consumption of each appliance in the home | One reading every 60 seconds of voltage, current, active power and accumulated energy for each connected measuring device | Covered. Measuring device operational |
| OBJ-02 | Show consumption clearly in a web and a mobile application | Consumption summary and history per home on the web, and consultation of homes, alerts and recommendations in the Android application, in Spanish, English, French and Portuguese | Web application deployed in production. Mobile application under development, integrated with the API |
| OBJ-03 | Issue automatic alerts for unusual or excessive consumption | Four alert types (device linked, disconnection, high or critical consumption, daily or monthly limit). A disconnection is detected in at most 150 seconds | Covered |
| OBJ-04 | Encourage responsible habits through recommendations based on measured data | Five saving rules evaluated every hour. The same recommendation is not repeated within 7 days | Covered |
| OBJ-05 | Deliver the system working end to end | Live demonstration, from a physical device measurement to an alert or recommendation on a phone, with no manual intervention, on October 28, 2026 | System deployed in production. Live demonstration planned for the October 28 delivery; linking a meter in production is pending evidence |

## General Scope

### In Scope

Table 2 lists what the project builds and the system module responsible for each item.

**Table 2**

*Items in Scope*

| Item | Description | Module or component |
|---|---|---|
| Users and profiles | Registration with prior email verification and acceptance of the terms, sign-in, password recovery, Google sign-in, profile (first name, last name and photo) and account deletion | User and Profile Management Module |
| Administration | Account lookup, status changes, granting and revoking the administrator role, and notices for administrators | User and Profile Management Module |
| Homes | Homes, members, owner and member roles, favorites, consumption limits, home deletion and ownership transfer | Home Management Module |
| Devices | Registration, linking, editing and unlinking of measuring devices | Device Management Module |
| Measurements | Reception of readings, device status, consumption history and statistics | Measurements and Consumption Module |
| Alerts | Consumption, connection and unusual consumption alerts of the devices | Alerts Module |
| Recommendations | Saving suggestions based on measured consumption | Savings Recommendations Module |
| Notifications | Notices by email and in the browser, in-app notices for administrators, preferences and delivery log | Notifications Module |
| Web application | Full client for management, history and statistics, with help in Settings (documentation, support and news) | Web application |
| Mobile application | Android client for consultation and alerts | Mobile application |
| Measuring device | Physical device that measures the consumption of each appliance and sends the readings | Measuring device |
| Deployment | Publication of the system on a server with HTTPS, and updates with automated tests, approval and rollback | Infrastructure |

Since October 28, 2026, all the modules run in production as independent services (security, devices, measurements, homes, notifications, alerts and recommendations), behind a single entry point for the API. The core service that used to hold them all was retired. The technical detail is in the technical proposal.

### Out of Scope

Table 3 explicitly lists what the project does not build and the reason.

**Table 3**

*Items Out of Scope*

| Item | Reason |
|---|---|
| iPhone application (iOS) | The mobile application is developed for Android only. |
| Manual entry of consumption from the utility bill | The measuring device records consumption automatically, so manual capture is not provided. |
| Support and content management from the administration | The current administration covers accounts, statuses, roles and notices; the rest is left for a later version. |
| Cost estimation in local currency | There is no agreement with an energy provider that supplies tariffs. The system reports energy in kWh. |
| Remote update of the measuring device's software | The device is updated by connecting it with a cable. |
| Service for several organizations at once | Each installation serves a single organization. |

### Assumptions and Constraints

Table 4 gathers the assumptions and constraints that condition the project.

**Table 4**

*Project Assumptions and Constraints*

| Type | Description |
|---|---|
| Time | Delivery on October 28, 2026, the date set for the evaluation of group 3145556 in the ADSO program, per the ADSO program calendar for group 3145556. |
| Budget | Approximately COP 180,000 invested in the components of the measuring device and COP 48,000 in one month of server. The domain was obtained at no cost. |
| Technology | The tools defined for the formative project are used, detailed in the technical proposal. |
| Team | Two trainees of the ADSO program. |
| Regulation | Protection of user information, with reference to Law 1581 of 2012 (Congreso de la República de Colombia, 2012). |
| Connectivity | Continuous Wi-Fi is assumed where measuring devices are installed. |

### Budget

Table 5 summarizes the project budget.

**Table 5**

*Project Budget*

| Item | Amount |
|---|---|
| Measuring device components | Approximately COP 180,000 (COP 173,997 according to the technical proposal breakdown) |
| Hosting | COP 48,000: Hostinger KVM 2 VPS contracted for one month (COP 27,900 per month on a yearly contract) |
| Domain and certificates | No cost: free `energy-monitor.tech` domain and Let's Encrypt certificates |
| Estimated total project cost | COP 57,021,997, including the development team's equipment and the estimated human resources (details in the technical proposal) |

## Schedule and Milestones

### Main Phases

The project started on May 1, 2026 and was planned in four phases with one-week progress steps. Table 6 describes each phase with its period, goal and exit criterion.

**Table 6**

*Project Phases*

| Phase | Period | Goal | Exit criterion |
|---|---|---|---|
| 1. Foundation and core loop | May 1 to June 30 | A user registers, creates or joins a home, registers a device and sees its measurements arrive. | A registered user logs in, creates a home, links a device and sees at least one real measurement recorded. |
| 2. Consumption visibility and alerts | July 1 to August 31 | Consumption is visible and the system warns when something is abnormal. | A home exceeding its limit produces a visible alert on web and mobile and a notification is delivered. |
| 3. Recommendations and internationalization | September 1 to 30 | Close the product loop with proactive guidance and finish the cross-cutting interface work. | A home with one week of history receives at least one recommendation based on its own consumption, and the interface is verified in more than one language. |
| 4. Consolidation and delivery | October 1 to 28 | Leave the system ready for demonstration and defense. | The system is demonstrated live, from a physical device measurement to an alert or recommendation on a phone. |

**Status as of October 20, 2026:** the functions of phases 1 to 3 are built and, during phase 4, the system went live at `https://energy-monitor.tech`, with continuous integration and deployment. On October 24, 2026 the separation of the users, devices and measurements modules into independent services went into production, and on October 28, 2026 the separation was completed with the homes, notifications, alerts and recommendations modules and the retirement of the core service, with no changes for users.

### Milestones

Table 7 gathers the project milestones with their estimated date.

**Table 7**

*Project Milestones*

| Code | Milestone | Estimated date | Status as of October 20, 2026 |
|---|---|---|---|
| H-01 | Project start | May 1, 2026 | Done |
| H-02 | End of phase 1: a registered user links a device and sees a real measurement | June 30, 2026 | Done |
| H-03 | End of phase 2: an alert for exceeding a limit, visible on web and mobile with its notification | August 31, 2026 | Done |
| H-04 | End of phase 3: recommendation based on the home's own consumption and interface in more than one language | September 30, 2026 | Done |
| H-05 | Complete bilingual documentation (project charter and requirements specification) | October 12, 2026 | In progress |
| H-06 | End-to-end tests and user, technical and installation manuals | October 14, 2026 | Completed, except the end-to-end tests in production, which are pending evidence |
| H-07 | Technical and cost report, and rehearsal of the demonstration | October 15, 2026 | Completed |
| H-08 | Final delivery and live demonstration | October 28, 2026 | Pending |
| H-09 | Go-live at `https://energy-monitor.tech` with continuous integration and deployment | October 19, 2026 | Done |

### Final Phase Plan

Table 8 details the activities of the period between October 10 and 28, 2026, which corresponds to phase 4, with the go-live and the delivery.

**Table 8**

*Phase 4 Activity Plan*

| Dates | Activities | Deliverable |
|---|---|---|
| October 10 to 12 | Confirm and record the pending changes. Complete the project charter and the requirements specification (ES and EN) | Bilingual documents in APA 7 format |
| October 13 and 14 | End-to-end tests. User manuals (web and mobile), technical and installation manual | Test plan and evidence. Manuals |
| October 15 | Technical and cost report. Rehearsal of the live demonstration | Technical and cost report |
| October 16 to 20 | Deployment on the production server, GitHub Actions pipelines and implementation and deployment manual | System in production and implementation and deployment manual |
| Until October 24 | Separation of the users, devices and measurements modules into independent services, with a single entry point for the API | Services in production and updated documentation |
| October 25 to 28 | Separation of homes, notifications, alerts and recommendations and retirement of the core service. Registration with email verification and terms, rules for Google accounts, end-to-end tests and production deployment of the gateway and the seven services | The API gateway and the seven services in production, and updated final documentation |
| October 28 | Final delivery and live demonstration | Working system and complete documentation |

The security improvement to the devices' credentials has already been applied: the system stores only the hash of each device's key. The reliability improvement in the sending of readings has no date within this plan and is carried out only if time allows. The only pending item of home and device management, which is device history management by linking period, also has no assigned date. The separation into services of the Home, Alerts, Recommendations and Notifications modules, carried out by Miguel Ángel García Artunduaga, was completed and deployed on October 28, 2026.

## Work Team and Roles

Table 9 presents who does what in the project. The team is made up of two trainees of the ADSO program.

**Table 9**

*Work Team and Roles*

| Member | Role | Responsibilities |
|---|---|---|
| Juan Esteban Cortés Parra | Project lead (Project Manager), Scrum Master, system data manager and developer | Project planning and follow-up, facilitation of the methodology, design and administration of the system data, information analysis, design and programming |
| Miguel Ángel García Artunduaga | Analyst, QA and developer | Quality assurance and testing, information analysis, design and programming |

## References

Congreso de la República de Colombia. (2012, October 17). *Ley 1581 de 2012: Por la cual se dictan disposiciones generales para la protección de datos personales* [Law 1581 of 2012: By which general provisions for the protection of personal data are issued]. Diario Oficial No. 48,587.
