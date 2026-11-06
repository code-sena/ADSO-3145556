---
title: "Software Analysis Documentation"
authors: Jonattan Steven Rizo Solano, Diego Andres Gutierrez Nuñez, Juan David Arboleda Perdomo
abstract: |
  This document constitutes the software analysis documentation for an academic, administrative, and attendance management system with web and mobile components. It describes the project objectives, scope, justification, expected utility, strategic purpose, the contribution of the analysis during software creation, and the main beneficiaries. Additionally, it incorporates elements of context analysis, stakeholders, feasibility, risks, data, software quality, success criteria, and traceability with other life cycle artifacts. The document aims to serve as a technical, managerial, and audit base for requirements specification, design, implementation, testing, and continuous improvement of the system.
keywords:
  - software analysis
  - requirements engineering
  - project objectives
  - functional scope
  - justification
  - beneficiaries
  - software quality
  - academic system
---

# Introduction

The software analysis documentation for the FaceAttendEDU system allows for an understanding of the problem, the context, and the conditions necessary for the technological solution to generate real value. In this project, the analysis is critical as it integrates human processes, regulatory constraints, information security, and attendance traceability.

The system focuses on the management of users, environments, courses, attendance records, and advanced reports, starting from a situation of fragmented processes and security risks. This document establishes the objectives, delimits the scope, and provides a verifiable base for requirements specification and architectural design, reducing ambiguity and aligning stakeholder expectations.

## Document Objective
Formally analyze the context, objectives, scope, and feasibility of the project to guide the requirements specification, design, implementation, and continuous improvement.

## Document Scope
This document covers the strategic analysis of the system (problem, objectives, risks, and success criteria). It does not replace the requirements specification report or the detailed architectural design.

# Project Context

## General System Description

The system is a platform composed of an administrative web application and an operational mobile application. Its purpose is to digitize and control processes related to users' digital identity, role and permission assignment, academic environment management, attendance recording, control of unregistered personnel, parameterization of lateness and justification rules, generation of histories, production of reports, and administration of the school's academic catalog.

The solution relies on authentication, biometrics, notifications, document management, auditing, analytical reports, and IoT device monitoring components. By its nature, it requires robust security, privacy, usability, traceability, and availability controls.

## Current Situation

Before the implementation of the system, or during its initial development phase, the following conditions are identified:

- Manual, semi-manual, or dispersed attendance recording processes in non-integrated tools.
- Difficulty in consulting histories immediately and reliably.
- Risk of human errors in typing, duplication, omission, or manipulation of records.
- Lack of comprehensive visibility over lateness, absences, and justifications.
- Inconsistencies between the mobile interface, approved mockups, and actual development progress.
- Presence of functional defects in mobile navigation and date selection.
- Insufficient management of roles, permissions, and segregation of duties.
- Need to strengthen security protocols to prevent forgery, impersonation, or unauthorized access.
- Absence or weakness in traceable audit mechanisms.
- Difficulty in generating reliable reports for decision-making.

## Central Problem

The central problem can be formulated as follows:

> The organization lacks an integrated, secure, traceable, and usable system to manage users, academic environments, attendance, justifications, reports, and operational configuration, which increases the risk of errors, inconsistencies, unauthorized access, administrative delays, and low quality in decision-making.

## Desired Situation

The desired situation consists of having a platform that allows:

- Registering and managing users with clearly defined roles and permissions.
- Secure authentication on web and mobile.
- Controlling environments, courses, supervisors, and shifts.
- Recording entries and exits with biometric support and controlled alternative methods.
- Handling unregistered personnel through supervised and temporary processes.
- Configuring alerts, language, palettes, and personal parameters on mobile.
- Monitoring IoT device failures.
- Parameterizing lateness and justification rules with versioning.
- Consulting histories and change audits.
- Uploading, approving, or rejecting justifications with automatic notification.
- Generating advanced reports, dashboards, and secure exports.
- Managing the school's courses and curricula.

## Analyzed Gap

| Dimension | Current State | Desired State | Main Gap |
|---|---|---|---|
| Identity Management | Incomplete or poorly visible users, roles, and permissions | Clear, audited RBAC model applied on the server | Lack of full roles and permissions implementation |
| Attendance | Dispersed, manual, or error-prone records | Biometric or alternative registration with traceability | Need for a reliable, secure, and verifiable flow |
| Mobile | Date errors, inconsistent navigation, and misaligned mockup | Fluid experience, consistent with approved design | Correction of defects and design-development alignment |
| Security | Risk of forgery, impersonation, or unauthorized access | Preventive controls, robust authentication, and auditing | Strengthen protocols and security questions |
| Reports | Information difficult to consolidate | Dashboards, exports, and parameter-based queries | Build a reliable and secure analytical layer |
| Governance | Decisions based on incomplete data | Traceability, indicators, and periodic review | Institutionalize analysis, CAPA, and continuous improvement |

# Project Objectives

## General Objective

Develop and implement a web and mobile platform to manage in an integrated, secure, traceable, and efficient manner the users, academic environments, attendance records, justifications, reports, operational configurations, and the school's educational catalog, in order to reduce operational errors, strengthen information security, and improve decision-making.

## Specific Objectives

1. Digitize user management, including individual registration, mass registration via CSV, login, password recovery and change, activation, deactivation, controlled deletion, and information consultation.
2. Implement a role and permission model based on RBAC, applied consistently across the web platform, API, and mobile application.
3. Manage environments, courses, supervisors, and shifts, ensuring traceable assignments and controlled validity.
4. Enable the registration of entries and exits using facial recognition, fingerprints, and supervised alternative methods.
5. Control the entry of unregistered personnel using temporary credentials, alternative registration, and stay limits.
6. Allow mobile configuration of alerts, languages, color palettes, user parameters, and notifications.
7. Manage alerts derived from failures or anomalies in IoT devices.
8. Parameterize rules for lateness, absences, and justifications with versioning and auditing.
9. Generate attendance, absence, and user change histories searchable by parameters.
10. Implement justification management with support uploads, approval, rejection, and automatic notification.
11. Provide advanced reports, an analytical dashboard, secure export, and date-range queries.
12. Manage the catalog of courses and the curricula of the subjects associated with the school.
13. Guarantee security, privacy, traceability, usability, and maintainability as transversal attributes of the system.

## Business Objectives

| Business Objective | Description | Associated Indicator |
|---|---|---|
| Reduce operational errors | Minimize incorrect, duplicated, or incomplete records | Manual correction rate |
| Improve traceability | Allow reconstructing who, when, how, and on what data an action was performed | Critical audit coverage |
| Strengthen security | Prevent unauthorized access, forgery, and impersonation | Avoided security incidents |
| Speed up decisions | Provide timely information through reports and dashboards | Report generation time |
| Elevate user experience | Reduce friction in mobile and administrative flows | User satisfaction and churn rate |
| Sustain continuous improvement | Feed corrective, preventive, and improvement actions | Verified CAPA efficacy |

# Scope

## Included Scope

The project includes the analysis, specification, development, testing, deployment, and production rollout of the following functional components:

- User management.
- Assignment of roles and permissions.
- Mass user registration via CSV files.
- Mobile login.
- Password recovery and change.
- Activation, deactivation, and controlled deletion of users.
- User information consultation.
- Management of environments or rooms.
- Management of courses.
- Assignment of supervisors, courses, and shifts.
- Registration of entries and exits.
- Mobile facial registration.
- Alternative registration by fingerprint.
- Control of unregistered personnel entries.
- Alternative registration for unregistered users.
- Mobile alert configuration.
- Activation and deactivation of alerts.
- Notification tone change.
- Mobile language configuration.
- Mobile color palette updates.
- User parameter updates on mobile.
- Management of IoT device failure alerts.
- Role change for a user.
- Parameterization of lateness and justifications.
- Attendance and absence histories by parameters.
- User change history.
- Justification support uploads.
- Approval and rejection of justifications.
- Automatic result notification.
- Report export.
- Analytical dashboard.
- Reports by date range.
- Lateness and absence queries by parameters.
- Adding and removing courses.
- Subject curricula.

It also includes transversal requirements for security, auditing, input validation, access control, protection of personal and biometric data, error handling, traceability, and verifiable acceptance criteria.

## Excluded Scope

In this analysis version, the following are not explicitly included:

- Purchase, installation, or physical certification of IoT devices, cameras, sensors, or mobile terminals.
- Contracting of connectivity, hosting, or undefined commercial licenses.
- Academic grading, financial enrollment, payroll, accounting, or treasury modules.
- Integrations with unspecified external systems, such as ERP, CRM, payment gateways, or government platforms.
- Mass migration of histories without approval of mapping, data quality, and backup plan.
- Final legal certification on biometric data processing, although privacy and security requirements are identified.
- 24/7 technical support, unless expressly contracted.
- Final brand graphic design, except for alignment with existing palettes and mockups.
- External penetration tests, unless approved as a complementary activity.

## Analysis Limits

This document is based on the functional information provided, operational findings detected, and software engineering best practices. It does not replace legal validation, forensic auditing, detailed architectural design, or formal budget approval. Some quantitative criteria, such as response times, maximum user volumes, or exact availability, must be concretized with real operational data.

# Project Justification and Utility

## Detected Need
The project arises from a critical need for control, security, and administrative efficiency. Dispersed management of attendance and reports generated operational risks and hindered auditing. Additionally, technical failures were detected during initial development:
- Date field and navigation errors in the mobile application.
- Absence of security questions and anti-forgery protocols.
- Misalignment between mockups and actual implementation.
- Limited role visibility.

## Utility and Purpose
The system provides an operational tool to register, consult, and audit critical school processes. Its purpose is to modernize academic processes, reduce dependence on manual controls, and protect biometric data.

| Domain | Utility | Expected Result |
|---|---|---|
| Operational | Digitize attendance and justifications | Fewer errors and greater speed |
| Administrative | Centralize users and roles | Better organizational control |
| Technical | Define requirements and risks | Predictable development |
| Security | Implement RBAC and anti-forgery | Lower risk exposure |
| Analytical | Provide dashboards and reports | Data-driven decisions |
| Institutional | Establish traceability and quality | Organizational maturity |

## Risks of Not Implementing the Solution
| Risk | Impact | Consequence |
|---|---|---|
| Manual processes | High | Loss of traceability and errors |
| Undefined roles | Critical | Unauthorized access and privilege escalation |
| Insecure biometrics | Critical | Identity impersonation |
| Deficient mobile navigation | Medium-High | Operational abandonment |
| Unreliable reports | High | Decisions based on incomplete data |

# Contribution of Analysis to Project Creation

Software analysis transforms vague needs into actionable and verifiable artifacts, adding value in the following dimensions:

- **Reduction of Ambiguity:** Breaks down general statements (e.g., "manage users") into specific requirements (registration, roles, authentication), avoiding divergent interpretations.
- **Prioritization and Criteria:** Allows distinguishing critical functionalities and associating them with objective compliance conditions, facilitating testing and acceptance.
- **Risk Identification:** Exposes technical and privacy risks early, reducing the cost of correction.
- **Design-Development Alignment:** Corrects deviations between mockups and the frontend before they become technical debt.
- **Security by Design:** Incorporates controls from conception (server-side RBAC, biometric encryption, auditing).
- **Traceability and Improvement:** Links findings with requirements and corrective actions (CAPA), fundamental for auditing.

| Activity | Analysis Contribution | Expected Result |
|---|---|---|
| Requirements Gathering | Clarifies problem and context | Precise requirements |
| Scope Definition | Delimits included and excluded | Avoids "scope creep" |
| Flow Design | Identifies actors and exceptions | Robust processes |
| Construction | Reduces rework due to ambiguity | Higher productivity |
| Security | Incorporates preventive controls | Lower risk exposure |
| Operation | Enables monitoring and auditing | Sustainability |

# Main Beneficiaries

## Primary Beneficiaries

| Beneficiary | Main Interest | Expected Benefit | Evidence of Benefit |
|---|---|---|---|
| School Direction | Governance, control, and decisions | Reliable information, risk reduction, operational visibility | Dashboards, reports, attendance and justification indicators |
| Academic Supervisors and Instructors | Management of courses, shifts, and attendance | Time savings, quick consultation, control of their scope | Mobile histories, approvals, parameter-based reports |
| Registered Personnel | Access, profile, attendance, and justifications | Clear experience, secure recovery, personal traceability | Login, biometric registration, notifications, own history |
| Administrative Area | Users, roles, courses, and parameters | Centralized and audited administration | Web management, CSV import, change audit |
| IT and Development Area | Technical base to build and maintain | Clear requirements, less ambiguity, traceability | SRS, traceability matrix, tickets, tests, deploys |
| Quality and Audit Area | Verification and compliance | Objective evidence for review | CAPA files, logs, validation reports |
| Security and Privacy | Protection of data and identities | Technical and procedural controls | RBAC, encryption, MFA, logs, threat model |

## Secondary Beneficiaries

| Beneficiary | Indirect Benefit |
|---|---|
| Visitors or unregistered personnel | Orderly, temporary, and supervised entry process |
| Families or guardians, if applicable | Greater confidence in attendance and security controls |
| Technology Providers | Clear specifications for integration or support |
| Educational Community | General improvement in efficiency, transparency, and institutional order |
| Future Organizational Projects | Reusable documentary base for scaling |

## Value Analysis by Beneficiary

| Beneficiary | Instrumental Value | Strategic Value | Trust Value |
|---|---|---|---|
| Direction | Reports and control | Data-driven decision making | Institutional transparency |
| Instructors | Operational time savings | Better academic tracking | Reduced administrative load |
| Registered Personnel | Profile and attendance self-management | Consistent digital experience | Credential security |
| IT and Development | Clear requirements and criteria | Less rework | Technical traceability |
| Quality | Verifiable evidence | Continuous improvement | Sustainable auditing |
| Security | Implemented controls | Incident prevention | Data protection |

# Stakeholders and Responsibilities

## Stakeholder Map

| Stakeholder | Role in Analysis | Expected Contribution | Influence Level |
|---|---|---|---|
| Project Sponsor | Approves vision and resources | Strategic prioritization | High |
| Product Owner or Business Leader | Defines needs and accepts deliverables | Business rules | High |
| Key Users | Validate operational flows | Usability feedback | Medium-High |
| Development Team | Analyzes technical feasibility | Estimates and design | High |
| QA | Derives test cases | Verification criteria | High |
| Security | Evaluates risks and controls | Threats and mitigations | High |
| Privacy or Compliance | Reviews personal and biometric data | Legal restrictions | High |
| DevOps or Infrastructure | Defines deployment and monitoring | Operability | Medium |
| Internal Audit | Reviews traceability | Conformity | Medium |

## Minimum RACI Model for Analysis

| Activity | Sponsor | Product Owner | Analyst | Development | QA | Security | Privacy |
|---|---:|---:|---:|---:|---:|---:|---:|
| Validate problem and objectives | A | R | R | C | C | C | C |
| Define scope | I | A | R | C | C | C | C |
| Identify stakeholders | I | A | R | C | I | C | C |
| Analyze risks | I | C | R | C | C | A | C |
| Review functional requirements | I | A | R | R | R | C | C |
| Validate privacy restrictions | I | C | C | I | I | C | A |
| Approve analysis document | A | R | R | C | C | C | C |

Notes:
- **R**: responsible for executing.
- **A**: approver or accountable.
- **C**: consulted.
- **I**: informed.

# Applied Analysis Methodology

## Information Gathering

Complementary techniques were used:
- Review of the provided functional requirements list.
- Analysis of operational and development findings.
- Review of mockups, mobile screens, and web progress.
- Identification of actors and critical processes.
- Application of software quality criteria and risk management.

## Process Analysis

The expected operational sequence was modeled narratively:
1. Register users and assign roles.
2. Configure environments, courses, supervisors, and shifts.
3. Authenticate users on web or mobile.
4. Register entries and exits by biometrics or alternative method.
5. Control the entry of unregistered personnel.
6. Parameterize rules for lateness and justifications.
7. Consult histories and audit.
8. Upload, approve, or reject justifications.
9. Generate reports and dashboards.
10. Manage courses and curricula.
11. Monitor IoT alerts and apply corrective or preventive actions.

## Requirements Analysis

Functional requirements were classified by module and linked with dependencies, platforms, risks, and acceptance criteria. This analysis directly feeds the Requirements Specification Report.

## Data Analysis

Main entities and their basic relationships were identified.

| Entity | Description | Relevant Relationships | Key Rule |
|---|---|---|---|
| User | Person with digital identity in the system | Has roles, permissions, attendance, justifications | Unique document or email according to policy |
| Role | Grouping of responsibilities | Contains permissions, assigned to users | Must be audited and versioned if applicable |
| Permission | Technical capacity to execute action | Belongs to roles, evaluated in API | Deny by default |
| Environment | Physical or logical space | Associated with courses, shifts, supervisors | Unique code |
| Course (Ficha) | Academic or operational group | Belongs to a curriculum, has supervisors and shifts | Unique per period |
| Shift (Jornada) | Time slot or shift | Associated with environment, course, and supervisor | Must validate time zone |
| Supervisor | Authorized person over courses or shifts | Linked to users and roles | Only active users |
| Attendance Record | Entry or exit event | Associated with user, method, device, date | Idempotent and audited |
| Justification | Irregularity support | Linked to attendance record | Requires approval or rejection |
| Support | File attached to justification | Belongs to justification | Validate type, size, and malware |
| Curriculum (Curso) | Academic catalog | Has courses (fichas) and curricula | Unique code |
| Study Plan | Subject structure | Belongs to a curriculum, associated with courses | Versioned |
| IoT Device | Connected element | Generates alerts and telemetry | Unique authentication |
| Audit | Record of changes | Applies to critical entities | Immutable or append-only |

## Feasibility Analysis

| Feasibility Type | Evaluation | Necessary Condition |
|---|---|---|
| Technical | Viable with modular architecture, secure API, compatible mobile, and controlled biometric services | Define biometric providers, iOS/Android support, and offline strategy |
| Operational | Viable if training, adoption, and support exist | Change management and clear roles |
| Economic | Positively justifiable if it reduces errors, administrative time, and risk | Budget for development, licenses, infrastructure, and security |
| Legal and Privacy | Conditioned on proper handling of personal and biometric data | Consent, retention, encryption, and legal review |
| Chronological | Viable through incremental deliveries | Prioritize critical modules first |
| Organizational | Viable if there is sponsorship and commitment from key users | Project committee and governance |

## Risk Analysis

| Risk | Probable Cause | Impact | Mitigation |
|---|---|---|---|
| Biometric false negative | Lighting, injuries, dirty sensor | Denial of service | Alternative method, operational support, configurable thresholds |
| Facial impersonation | Photo, screen, presentation attack | Unauthorized access | Liveness, auditing, MFA, monitoring |
| Privilege escalation | Misconfigured roles | Administrative compromise | Server-side RBAC, segregation of duties |
| CSV data exposure | Insecure import or export | Privacy violation | Sanitization, encryption, access control |
| Mobile navigation errors | Inconsistent design or defects | Operational abandonment | UX tests, navigation map, CAPA corrections |
| Outdated mockup | Lack of design governance | Rework | Approved baseline, design-development comparison |
| Undetected IoT failures | Insufficient heartbeat | Blind operation | Alerts, escalation, acknowledgment |
| Retroactive parameter changes | Poor versioning | Historical inconsistency | Validity periods, period closing, auditing |
| Mobile connection loss | Unstable networks | Loss of record | Secure local queue, idempotent synchronization |
| Alert fatigue | Too many false alarms | Ignoring critical events | Suppression, thresholds, severity classification |

## Software Quality Analysis

The system and software quality model (ISO/IEC 25010:2011) was used as a reference.

| Characteristic | Relevance in the Project | Derived Requirement |
|---|---|---|
| Functional Suitability | Must cover user management, attendance, justifications, and reports | Each ERF with acceptance criteria |
| Performance Efficiency | Mobile registration and queries must not degrade operation | Target times and pagination |
| Compatibility | Web, mobile, IoT, and biometric services | Stable and versioned interfaces |
| Usability | Critical in mobile and recovery flows | Fewer steps, clear messages, consistent navigation |
| Reliability | Attendance and auditing cannot fail silently | Idempotency, retries, monitoring |
| Security | Personal, biometric, and sensitive role data | RBAC, encryption, MFA, auditing |
| Maintainability | System intended to evolve | Modularity, documentation, testing |
| Portability | Support on different mobile devices | Minimum versions and platform abstraction |

# Analysis Results

## Key Findings

1. The project is necessary and strategically relevant because it addresses problems of control, security, traceability, and administrative efficiency.
2. Role and permission management must be treated as a critical requirement, not a secondary functionality.
3. Biometric registration requires privacy controls, liveness detection, alternative methods, and robust auditing.
4. The mobile application requires priority correction of navigation, date fields, and alignment with mockups.
5. Security must include recovery questions, anti-forgery protocols, session protection, and server-side validation.
6. Reports and dashboards must respect the scope per supervisor to avoid information leakage.
7. Parameterization of lateness and justifications must be versioned to avoid corrupting histories.
8. CSV import represents a security and privacy risk if not properly sanitized and controlled.
9. The analysis must connect with corrective, preventive, and improvement actions to close the quality cycle.
10. Analytical documentation reduces ambiguity and serves as a contractual, technical, and audit base.

## Preliminary Conclusions

- The system is technically and operationally viable, provided that security, privacy, mobile usability, and biometric integration risks are managed.
- The greatest value of the project is not just in automating, but in institutionalizing traceability, control, and continuous improvement.
- The main beneficiaries are direction, academic supervisors, registered personnel, administrative areas, IT, quality, and security.
- The analysis performed justifies proceeding to formal requirements specification, architectural design, and incremental construction.

## Recommendations

1. Approve this document as the analysis base before freezing the scope.
2. Prioritize the implementation of roles, permissions, secure authentication, and attendance registration.
3. Correct critical mobile defects: dates, navigation, and coherence with the mockup.
4. Implement security questions and anti-forgery protocols with a risk-based approach.
5. Define a biometric data policy with the legal or compliance area.
6. Establish a traceability matrix between RF, ERF, test cases, tickets, and deployment evidence.
7. Create a CAPA file for each relevant finding.
8. Perform validation with key users before production.
9. Design incremental deliveries with adoption and quality metrics.
10. Schedule periodic review of the analysis and derived requirements.

# Success Criteria

| Criterion | Suggested Goal | Measurement Method |
|---|---|---|
| Analysis Coverage | 100% of RF and ERF documented with purpose, rules, and criteria | Documentary review |
| Critical Risks Mitigated | 100% with approved mitigation plan | Risk register |
| Reduced Ambiguity | Zero requirements without verifiable acceptance criteria | QA and business review |
| Mobile Correction | Date and navigation defects closed with evidence | Tickets and tests |
| Security | RBAC, authentication, and anti-forgery controls implemented | Security tests |
| User Adoption | Minimum satisfaction 4 out of 5 in UAT | Survey or validation |
| Traceability | Each technical change linked to requirement and evidence | Repository and pipeline |
| Data Quality | Reports consistent with histories and auditing | Test comparison |
| Continuous Improvement | Effective CAPA actions verified | Efficacy validation |

# Limitations and Assumptions

## Assumptions

- The provided functional requirements represent a valid business base.
- Stakeholders are willing to validate flows and acceptance criteria.
- Mobile devices support cameras and, optionally, fingerprint sensors.
- Minimum infrastructure exists for web, API, database, and notifications.
- Biometric data can be treated according to an approved privacy policy.
- IoT devices can emit telemetry or failure signals.

## Limitations

- This analysis does not include detailed architectural design.
- It does not define a complete physical database model.
- It does not constitute a binding legal opinion on data protection.
- It does not estimate definitive costs or a contractual schedule.
- It does not technologically validate specific biometric providers.
- It does not replace external security tests or forensic auditing.

# Traceability with Other Artifacts

| Artifact | Relationship with this Document | Expected Use |
|---|---|---|
| Requirements Specification Report | Derives from the objectives, scope, and findings analyzed | Convert analysis into verifiable requirements |
| Corrective, Preventive, and Improvement Action Table | Receives identified findings and risks | Manage continuous improvement |
| Test Plan | Uses acceptance criteria and edge cases | Verify compliance |
| Risk Register | Feeds mitigations and tracking | Project governance |
| RBAC Matrix | Derives from the need for roles and permissions | Access control |
| Privacy Policy | Derives from the treatment of biometric and personal data | Compliance |
| Release Notes and Backlog | Convert analysis into prioritized deliverables | Agile execution |

# Expected Value Analysis

The system generates value in four main dimensions:

1. **Operational:** Reduces registration and information consolidation time, decreasing daily friction and manual errors.
2. **Risk:** Prevents unauthorized access, impersonation, and loss of traceability. The value is preventive and evidenced by the reduction of incidents.
3. **Decisional:** Allows moving from intuitive decisions to data-driven decisions through dashboards and analytical reports.
4. **Institutional:** Strengthens organizational maturity by institutionalizing software quality and traceability as recurrent practices.

# Recommended Approach for the Next Phase

The next phase should be the formal specification of requirements, already initiated in the Requirements Specification Report. To maintain coherence, it is recommended to:

1. Freeze the minimum viable scope based on critical risks.
2. Prioritize RF 1, RF 3, RF 4.7, RF 5, and RF 6 as the operational core.
3. Treat RF 7 as an analytical layer dependent on data quality.
4. Implement RF 8 as a master catalog necessary for courses and plans.
5. Link each ERF with test cases, responsible party, commitment date, and evidence.
6. Open a CAPA file for mobile, security, and design findings.
7. Validate with key users before mass deployment.

# Ethical, Privacy, and Responsibility Considerations

The use of biometrics, personal data, and access controls requires an ethical and legal approach. The system must not be implemented as a disproportionate surveillance mechanism or as an arbitrary sanction tool. It must respect principles of legitimacy, purpose, minimization, accuracy, security, transparency, and proactive responsibility.

In particular:
- Biometrics must be used only when there is a real need and a suitable legal basis.
- Users must be clearly informed about the treatment of their data.
- Reasonable alternatives must exist when biometrics fail or are not desirable.
- Records must be kept for defined periods and deleted or anonymized when appropriate.
- Access to sensitive information must be audited and limited by role.
- Alerts and reports must not be used for discrimination or abusive control.

# Operational Glossary

| Term | Definition |
|---|---|
| Software Analysis | Process of understanding the problem, context, requirements, risks, and success criteria before or during development |
| Scope | Delimitation of what is included, excluded, and limited in the project |
| Beneficiary | Person, area, or system that receives direct or indirect value from the project |
| Purpose | Long-term strategic goal that justifies the existence of the project |
| Justification | Reason for the project's existence in the face of a problem, risk, or opportunity |
| Utility | Practical and immediate application of the system to solve needs |
| RBAC | Role-Based Access Control |
| CAPA | Corrective, Preventive, and Improvement Actions |
| Stakeholder | Interested party with influence or affectation in the project |
| Traceability | Ability to link requirements, decisions, evidence, and results |

# Conclusions

The Software Analysis Documentation serves a structural function in the project life cycle: it transforms an operational need into a comprehensible, prioritized, verifiable, and governable framework. In this case, the analysis confirms that the system is pertinent, viable, and of high value, provided that security, privacy, mobile usability, traceability, and data quality are properly managed.

The project objectives are oriented towards digitizing, controlling, and auditing critical academic and administrative management processes. The scope is delimited by clear functional modules, explicit exclusions, and analytical limits. The project was undertaken because of the need to reduce errors, strengthen security, and improve decisions; it serves to automate processes, provide traceability, and enable reports; and it has the strategic purpose of institutionalizing quality, trust, and continuous improvement.

Likewise, the analysis helps during project creation by reducing ambiguity, prioritizing efforts, preventing risks, aligning stakeholders, grounding tests, and sustaining documentary traceability. The main beneficiaries are school direction, academic supervisors, registered personnel, administrative areas, IT, quality, security, and privacy, without neglecting indirect benefits for visitors, the educational community, and future system scalability.

This document should be considered a living base. Its value increases when connected to the requirements specification, the backlog, test cases, the risk register, and corrective, preventive, and improvement actions. In this way, the analysis is not a static artifact, but an engineering and governance mechanism for building robust, secure, and useful software.

**Author's Note.** Jonas is the author and technical lead of the document. Correspondence related to this analysis can be directed to jonas@consultoria.example. The author declares the absence of conflicts of interest and external funding for the preparation of this document.

# References and Additional Annexes

This section remains open for the incorporation of new standards, technical manuals, or supporting documents that emerge during the system evolution. and Additional Annexes

This section remains open for the incorporation of new standards, technical manuals, or supporting documents that emerge during the system evolution.

American Psychological Association. (2020). *Publication manual of the American Psychological Association* (7th ed.). https://doi.org/10.1037/0000165-000

International Organization for Standardization. (2015). *Quality management systems — Requirements* (ISO 9001:2015). https://www.iso.org/standard/62085.html

International Organization for Standardization & International Electrotechnical Commission. (2011). *Systems and software engineering — Systems and software quality requirements and evaluation (SQuaRE) — System and software quality models* (ISO/IEC 25010:2011). https://www.iso.org/standard/35733.html

International Organization for Standardization, International Electrotechnical Commission, & Institute of Electrical and Electronics Engineers. (2017). *Systems and software engineering — Software life cycle processes* (ISO/IEC/IEEE 12207:2017). https://www.iso.org/standard/63712.html

International Organization for Standardization, International Electrotechnical Commission, & Institute of Electrical and Electronics Engineers. (2018). *Systems and software engineering — Life cycle processes — Requirements engineering* (ISO/IEC/IEEE 29148:2018). https://www.iso.org/standard/72089.html

OWASP Foundation. (2021). *OWASP Application Security Verification Standard 4.0*. https://owasp.org/www-project-application-security-verification-standard/
