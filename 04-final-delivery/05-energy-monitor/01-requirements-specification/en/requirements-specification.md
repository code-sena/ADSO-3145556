# EnergyMonitor Software Requirements Specification

Juan Esteban Cortés Parra and Miguel Ángel García Artunduaga

Servicio Nacional de Aprendizaje (SENA)

Technologist in Software Analysis and Development (ADSO), group (ficha) 3145556

Version 2.3, October 28, 2026

October 16, 2026 (updated October 28, 2026)

## Version Control

- Version 2.0 (10/10/2026): functional requirements per module, non-functional requirements, 15 user stories and 2 use cases.
- Version 2.1 (10/20/2026): Platform Administration Module (RF-ADM-01 to RF-ADM-10), security requirement for the administration functions (RNF-SEG-11), continuous integration and deployment (RNF-MAN-05), push notifications in the mobile application, user story HU-16 and renumbering of tables 12 to 23.
- Version 2.2 (10/28/2026): registration with prior email verification (the account does not exist until it is verified) and rejection of domains with a typing error (RF-SEC-01, RF-SEC-02), mandatory acceptance of the terms (RF-SEC-13), rules for accounts created with Google (RF-SEC-05, RF-SEC-07, RF-SEC-11), independent editing of the first and last name (RF-SEC-08), attempt limit for the deletion code (RNF-SEG-06) and acceptance criteria CA-39 to CA-42.
- Version 2.3 (10/28/2026): unusual consumption alert of a device (RF-ALE-07), photos of the members of a home (RF-HOM-12), help in Settings with documentation by role, support only for users and automatic news (RF-CLI-07), profile photo that can be enlarged in the page (RF-CLI-08), user story HU-17 and acceptance criteria CA-43 to CA-47.

## Abstract

This document specifies what EnergyMonitor must do, a system that lets households see the electrical consumption of each appliance, receive alerts and get saving recommendations. It is written from the perspective of the user and the business, without defining how the solution will be built. It presents the functional requirements, grouped by system module, the non-functional requirements (security, performance, availability, compatibility, maintainability, internationalization, scalability, usability and interoperability) and 17 user stories and 2 use cases with verifiable acceptance criteria. Requirements are identified with stable codes that are identical in the Spanish and English versions.

*Keywords:* requirements specification, electrical consumption, alerts, saving recommendations, households

## Introduction

### Purpose

This document specifies the requirements of EnergyMonitor: what the system must do (functional requirements), with what quality it must do it (non-functional requirements) and how it is verified (user stories and use cases with their acceptance criteria). The structure follows the recommended practice for software requirements specifications (Institute of Electrical and Electronics Engineers [IEEE], 1998). The document describes user and business needs and does not constrain the technology used to build the system.

### Scope

EnergyMonitor measures the electrical consumption of each appliance in a home with a measuring device attached to the appliance, stores the readings and presents them in a web application and an Android mobile application. It generates alerts, saving recommendations and notifications. The full scope and what is excluded are in the project charter (General Scope section).

### Definitions and Acronyms

Table 1 defines the terms and acronyms used in the document.

**Table 1**

*Definitions and Acronyms*

| Term | Description |
|---|---|
| ADSO | Software Analysis and Development |
| RF | Functional requirement |
| RNF | Non-functional requirement |
| HU | User story |
| CU | Use case |
| CA | Acceptance criterion |
| Home | Space (house, apartment or other) that groups its members and its measuring devices |
| Owner | Person who manages a home, its devices, its members and its consumption limits |
| Member | Person who belongs to a home and views its consumption, alerts and recommendations |
| Measuring device | Physical device that attaches to an appliance, measures its electrical consumption and sends the readings |
| Threshold | Daily or monthly consumption limit set by the owner of the home |
| Alert | Notice the system raises for excessive consumption, a limit reached or a device disconnection |
| Recommendation | Saving suggestion the system builds from the home's measured consumption |
| Global role | Permission over the whole platform, separate from the owner and member roles of each home. Today there is one: administrator (ADMIN) |
| Administration notice | Notification the system shows inside the web application to administrators; it is not sent by email or push |

### Code Convention

Every requirement has a stable code that is identical in the Spanish and English versions. Table 2 explains the prefixes.

**Table 2**

*Code Prefixes*

| Prefix | Meaning |
|---|---|
| `RF-SEC` | Functional requirements of the User and Profile Management Module |
| `RF-HOM` | Functional requirements of the Home Management Module |
| `RF-DEV` | Functional requirements of the Device Management Module |
| `RF-MED` | Functional requirements of the Measurements and Consumption Module |
| `RF-ALE` | Functional requirements of the Alerts Module |
| `RF-REC` | Functional requirements of the Savings Recommendations Module |
| `RF-NOT` | Functional requirements of the Notifications Module |
| `RF-CLI` | Functional requirements for the web and mobile applications |
| `RF-ADM` | Functional requirements of the Platform Administration Module |
| `RNF-SEG`, `RNF-REN`, `RNF-DIS`, `RNF-COM`, `RNF-MAN`, `RNF-I18`, `RNF-ESC`, `RNF-USA`, `RNF-INT` | Non-functional requirements: security, performance, availability, compatibility, maintainability, internationalization, scalability, usability and interoperability |
| `HU` | User stories |
| `CU` | Use cases |
| `CA` | Acceptance criteria |

The prefixes keep their Spanish form in both versions so that codes match exactly.

### User Types

Table 3 describes the user types of the system. A person can belong to several homes with different roles.

**Table 3**

*User Types*

| Type | Description |
|---|---|
| System administrator | Holds the global administrator role. Views the platform accounts, changes their status, grants or revokes the administrator role and receives administration notices from a dedicated screen of the web application. The role gives no access to any home |
| Home owner | Manages the home, its devices, its members and its consumption limits |
| Home member | Views the home's consumption, alerts and recommendations |
| Measuring device | Technical actor that sends consumption readings and its own status |

### Assumptions and Dependencies

1. Measuring devices have continuous Wi-Fi at the installation site.
2. Google sign-in and email sending depend on accounts of those services configured for the system.
3. Browser notifications depend on the system having that service enabled.

## Functional Requirements

### User and Profile Management Module

Table 4 lists the functional requirements of the User and Profile Management Module.

**Table 4**

*Functional Requirements of the User and Profile Management Module*

| Code | Requirement |
|---|---|
| RF-SEC-01 | The system must allow requesting the registration of an account with personal data, email and password. The account does not exist until the user verifies their email: the request stays pending for 24 hours and, if it is not verified, it is discarded. It must reject the registration if the email already has an account, if the domain does not exist or does not receive mail, or if the domain looks like a typing error of a well-known provider (for example `meil.com`), and inform the user. |
| RF-SEC-02 | The system must send a six-digit verification code to the given email and create the account, with the email already verified, when the user enters it. The user must be able to ask for the code to be sent again, and registering again with the same email replaces the pending request. Without verification it is not possible to sign in. The code lasts between 15 and 30 minutes and changing the email of an account invalidates it. |
| RF-SEC-03 | The system must allow signing in securely with email and password. |
| RF-SEC-04 | The system must keep the user's session without asking for credentials continuously, let the user sign out and close the session if it detects an attempt to reuse a session credential that was already used. |
| RF-SEC-05 | The system must allow registering and signing in with a Google account. The first time it creates the account with the Google first name, last name (if any), photo and email, the email is verified and acceptance of the terms is required. If the email belongs to an account registered with a password, it must reject the access and report the conflict. If the Google service is not available, it must notify the user. |
| RF-SEC-06 | The system must allow password recovery with a six-digit code sent to the email, valid for 15 minutes and single use. It must answer the same whether or not the account exists, and requesting a new code cancels the previous ones. |
| RF-SEC-07 | The system must allow a signed-in user to change their password. An account created with Google has no password of its own and cannot change it: the system must reject the request with a readable message. |
| RF-SEC-08 | The system must allow viewing the account data and updating the profile, editing the first name and the last name separately, and the photo. |
| RF-SEC-09 | The system must allow knowing the user's permissions. General permissions are evaluated on every query and are not fixed in the session. |
| RF-SEC-10 | The system must identify the user only from their session and must not accept identities indicated by the application making the request. |
| RF-SEC-11 | The system must allow the user to delete their account by confirming their password. With a wrong password it must reject the request. An account created with Google has no password: it confirms with a six-digit code that the system sends to its email, and a wrong or expired code rejects the request. Deletion is soft and all-or-nothing: it closes sessions, marks personal data as deleted, releases the email and the Google identity for a new registration and handles memberships in homes. A home of which the account is the only member is deleted. If the account is the only owner of a home with other members, the system rejects the deletion with a readable message until ownership is transferred or the members are removed. Access already granted before deletion may remain valid for at most 15 minutes and cannot be renewed. |
| RF-SEC-12 | The system must apply a password policy (minimum length of 8 and maximum of 64 characters) and record failed access attempts and security-relevant actions. Failed attempts are counted and logged, and an administrator can block an account, but there is no automatic lockout after failed attempts and the 90-day password expiration is not enforced in this version. |
| RF-SEC-13 | The system must not allow access to an account that has not accepted the terms and conditions, whatever the method: registration with a password, the first sign-in with Google and the sign-in of an account without acceptance require the user's express acceptance, and without it the system answers asking for it and neither creates nor changes anything. The moment of acceptance is recorded. |

### Home Management Module

Table 5 lists the functional requirements of the Home Management Module.

**Table 5**

*Functional Requirements of the Home Management Module*

| Code | Requirement |
|---|---|
| RF-HOM-01 | The system must allow creating a home by choosing a home type from a catalog. The creator becomes the owner and the home receives an access code. |
| RF-HOM-02 | The system must show the user the homes they belong to. |
| RF-HOM-03 | The system must allow joining a home with its access code, with the member role. |
| RF-HOM-04 | The system must show the members of a home. |
| RF-HOM-05 | The system must distinguish users' general permissions from the owner and member roles within each home. Every home has exactly one owner. A user can have different roles in different homes. |
| RF-HOM-06 | The owner must be able to remove a member and any member must be able to leave the home. |
| RF-HOM-07 | The user must be able to mark a home as a favorite. |
| RF-HOM-08 | The system must allow viewing, updating and resetting to system values the thresholds of a home. The owner sets the daily or the monthly limit. Default values are 6 kWh daily and 180 kWh monthly. |
| RF-HOM-09 | The owner must be able to delete the home by soft delete. The home's devices are unlinked and become free, the history of measurements, alerts and recommendations is kept, and members stop seeing the home. Only the owner can do it. |
| RF-HOM-10 | The owner must be able to transfer home ownership to another member. Roles are swapped in a single step and the home always has a single owner. The transfer does not change thresholds, devices or measurements. |
| RF-HOM-11 | Pending: limit a device's history to the linking period. |
| RF-HOM-12 | The system must show the members of a home the profile photo of each one, when they have it, and allow enlarging it. |

### Device Management Module

Table 6 lists the functional requirements of the Device Management Module.

**Table 6**

*Functional Requirements of the Device Management Module*

| Code | Requirement |
|---|---|
| RF-DEV-01 | The system must provide a catalog of appliance types. |
| RF-DEV-02 | The owner must be able to link a measuring device to their home. The system registers the device and gives it its credentials and the address of the service it must send its data to. A device linked to another home must be rejected. Linking the same device again keeps its identity and history and renews its credentials. |
| RF-DEV-03 | Members must be able to see the devices of a home. |
| RF-DEV-04 | The owner must be able to edit the appliance, name and room of a device without intervening on the physical unit. |
| RF-DEV-05 | The owner must be able to change the Wi-Fi network of an already linked device. The system renews the credentials and invalidates the old ones. It is not a new link and does not raise the device linked notice. |
| RF-DEV-06 | The owner must be able to unlink a device. When doing so, the credentials stop being valid and the device receives the order to forget them, keeps its Wi-Fi network and turns Bluetooth on at once to be linked again. If it was off, it receives the order when it reconnects. The old credentials may still be accepted for up to five minutes. When it is linked again, the pending order is cleared. |
| RF-DEV-07 | The system must only accept data from linked devices, and each device can only send its own data. |
| RF-DEV-08 | Linking must be possible from the web application over Bluetooth: the application shows the Wi-Fi networks the device sees, registers the device and sends it the configuration. For browsers without Bluetooth, the device offers its own configuration portal, reached by connecting to its `EnergyMonitor-Setup` network. |
| RF-DEV-09 | The device must reconnect without user intervention after a Wi-Fi loss or a temporary system failure, keep up to 30 samples while it has no connection and never send zero readings when the sensor does not answer. |

### Measurements and Consumption Module

Table 7 lists the functional requirements of the Measurements and Consumption Module.

**Table 7**

*Functional Requirements of the Measurements and Consumption Module*

| Code | Requirement |
|---|---|
| RF-MED-01 | The device must send every 60 seconds the voltage (V), current (A), active power (W) and accumulated energy (kWh) of the appliance. The system ignores any additional data that is not part of those quantities. |
| RF-MED-02 | The system must store each reading with the date and time it was taken. Samples accumulated while offline arrive with their real time. If the device has no time, the system uses the reception time. |
| RF-MED-03 | The system must drop messages that claim to belong to a device other than the one sending them. |
| RF-MED-04 | The device must report its status every five minutes and on connect: whether it is online or offline, its signal strength, its software version and how long it has been on. |
| RF-MED-05 | The system must keep each device's state changes. While it stays online, each report only updates the last recorded state. |
| RF-MED-06 | The system must mark a device as offline when the device announces it is disconnecting or when 150 seconds pass without news from it. |
| RF-MED-07 | The system must allow querying measurements, the latest measurement and consumption statistics. |
| RF-MED-08 | The system must provide the consumption summary and history of a home, with date range selection. |
| RF-MED-09 | The system must classify power into the levels LOW (0 to 500 W), MEDIUM (500 to 1500 W), HIGH (1500 to 3000 W) and CRITICAL (from 3000 W), with ranges that include the lower limit and exclude the upper one, and allow them to be queried. |

### Alerts Module

Table 8 lists the functional requirements of the Alerts Module.

**Table 8**

*Functional Requirements of the Alerts Module*

| Code | Requirement |
|---|---|
| RF-ALE-01 | The system must raise a device linked notice with the first reading a device sends after being linked, once per link. The user closes it by marking it as read. |
| RF-ALE-02 | The system must raise a disconnection alert when a device announces it is disconnecting or 150 seconds pass without data. It resolves itself when the device reports again. |
| RF-ALE-03 | The system must raise a high or critical consumption alert when a reading falls in the HIGH or CRITICAL level. It resolves itself with the first reading in the LOW or MEDIUM level. |
| RF-ALE-04 | The system must raise a daily or monthly limit alert when the home's energy since the start of the day or month reaches the limit in its thresholds. Day and month are computed in the Bogotá time zone. It resolves itself when a new period starts or when the limit is raised above the consumption. |
| RF-ALE-05 | The system must allow listing alerts, viewing the detail of one, marking it as read and deleting it. Only resolved or read alerts can be deleted. |
| RF-ALE-06 | The system must raise consumption alerts from each new reading and disconnection alerts from the loss of communication with the device, and send each alert for notification. |
| RF-ALE-07 | The system must raise an unusual consumption alert when a reading of a device is at least 200 W and at least 2.5 times its usual power (the hourly average of the previous 7 days, leaving out the last hour), as long as there are at least 24 hours of history and an average of 5 W or more. There will be one pending alert per device and it resolves itself when the reading is back to normal. |

### Savings Recommendations Module

Table 9 lists the functional requirements of the Savings Recommendations Module.

**Table 9**

*Functional Requirements of the Savings Recommendations Module*

| Code | Requirement |
|---|---|
| RF-REC-01 | The system must evaluate every hour the hourly energy of homes whose devices reported in the last week, and store a recommendation when a rule matches. The first evaluation runs 2 minutes after the system starts. |
| RF-REC-02 | Peak hours rule: at least 40% of the last 7 days' energy (minimum 2 kWh) was used between 18:00 and 22:00. |
| RF-REC-03 | Standby consumption rule: a device that is not a refrigerator drew an average of 15 W or more between 01:00 and 05:00 on each of the last 3 nights. |
| RF-REC-04 | Weekly comparison rule: the last 7 days are 25% or more above the previous 7 (minimum 1 kWh). |
| RF-REC-05 | Limit projection rule: at the current pace, the home will exceed its daily or monthly limit, whichever the owner set. |
| RF-REC-06 | Device increase rule: a device's mean power in the last 7 days is 30% or more above that of the previous 21 days. |
| RF-REC-07 | The same recommendation (home, type and device) must not repeat within 7 days, even if it was deleted. Hours are interpreted in the Bogotá time zone. |
| RF-REC-08 | The system must allow listing a home's recommendations, marking them as read and deleting the read ones, one by one or all at once. Someone who is not a member of the home cannot see its recommendations. |
| RF-REC-09 | The rules need history: peak hours and standby consumption work after a few days of readings, the weekly comparison after two weeks and the per-device comparison after four. |

### Notifications Module

Table 10 lists the functional requirements of the Notifications Module.

**Table 10**

*Functional Requirements of the Notifications Module*

| Code | Requirement |
|---|---|
| RF-NOT-01 | The system must deliver every alert to all members of the home by email and push notification (in the browser or in the Android mobile application where the user turned it on), according to each user's preferences. Both channels are on by default. |
| RF-NOT-02 | The system must deliver every new recommendation through the same channels. |
| RF-NOT-03 | The user must be able to view and change their notification preferences. |
| RF-NOT-04 | The user must be able to turn push notifications on and off in the browser and in the mobile application. If the service is not enabled in the system, email keeps working. |
| RF-NOT-05 | Every delivery attempt must be recorded as sent or failed, without storing the message content. Attempts still not closed after 30 minutes are marked as failed. |
| RF-NOT-06 | The system must send the verification code and the password recovery code by email. An email failure must not change what the user sees on screen. |
| RF-NOT-07 | Every alert email must include a link to the web application. |

### Web and Mobile Applications

Table 11 gathers the functional requirements of the web and mobile applications.

**Table 11**

*Functional Requirements for the Web and Mobile Applications*

| Code | Requirement |
|---|---|
| RF-CLI-01 | The web application must offer registration, sign-in (including Google), password recovery, account, homes, favorites, devices, thresholds, consumption, history and notifications. To administrator accounts it offers the platform administration instead (RF-ADM-06). |
| RF-CLI-02 | The web application must show a badge with pending alerts, a counter in the tab title and a pop-up notice when a new alert arrives, and refresh every 30 seconds. |
| RF-CLI-03 | The web application must link devices over Bluetooth and allow browser notifications, which arrive even when the tab is closed. |
| RF-CLI-04 | The mobile application must be available for Android only and allow access to the account, homes, alerts, recommendations and notification preferences, and receive push notifications on the phone. Home deletion and ownership transfer are not yet available in the mobile application. |
| RF-CLI-05 | Both applications must allow choosing the language among Spanish, English, French and Portuguese. |
| RF-CLI-06 | The applications must allow choosing a color palette with dark mode.  |
| RF-CLI-07 | The web application must offer help in Settings in modal windows: the documentation according to the role (user or administrator), technical support —only for users, who create the ticket as an email to an administrator— and the news, which update themselves with every version.  |
| RF-CLI-08 | The web application must allow viewing the profile photo enlarged inside the page itself, without opening another tab. |

### Platform Administration Module

Table 12 lists the functional requirements of the Platform Administration Module.

**Table 12**

*Functional Requirements of the Platform Administration Module*

| Code | Requirement |
|---|---|
| RF-ADM-01 | The system must have a global administrator role with three permissions: view the accounts, change the status of accounts and manage global roles. No account is an administrator when the system is installed: the first administrator is designated from the server on an already registered account, and the next ones from the web application. |
| RF-ADM-02 | The administrator must be able to view the directory of active accounts, from newest to oldest, search by email, first name or last name and browse it in pages of up to 50 accounts. For each account it shows the email, the name, the status, whether the email is verified, the registration date, the last sign-in and the global roles. |
| RF-ADM-03 | The administrator must be able to change the status of another account to active, inactive or blocked. An account that stops being active cannot sign in and its open sessions are closed. The web application asks for confirmation before applying the change. |
| RF-ADM-04 | The administrator must be able to grant and revoke the administrator role to another account. Repeating a grant or a revocation changes nothing. The web application asks for confirmation before revoking the role. |
| RF-ADM-05 | The system must prevent an administrator from changing the status or the roles of their own account, and reject the request with a readable message. |
| RF-ADM-06 | The administrator role must give no access to any home. In the web application, an administrator account goes straight to the administration and its menu only shows Administration, Notifications and Settings; the home and favorites screens are not available to it and Settings does not show the homes' notification preferences. |
| RF-ADM-07 | The system must notify every active administrator inside the web application when a new account is registered (also on the first Google sign-in) and when another administrator changes the status or the role of an account. Whoever made the change does not receive the notice, and an action that changes nothing produces no notices. |
| RF-ADM-08 | The administrator must be able to view their 50 most recent notices, see which ones are unread, mark them as read one by one or all at once and delete them. They can only view and change their own notices. The application shows the number of unread notices. |
| RF-ADM-09 | Every status or role change made by an administrator must be recorded in the audit log, with the source address. |
| RF-ADM-10 | Pending: management of support requests and editing of the documentation from Settings, and delivery of the administration notices by email or push notification. |

## Non-functional Requirements

### Security

Table 13 sets the security requirements.

**Table 13**

*Non-functional Security Requirements*

| Code | Requirement |
|---|---|
| RNF-SEG-01 | Each access granted to a user must last 15 minutes and renew automatically while the session remains valid. A session must not exceed 7 days without the user signing in again. |
| RNF-SEG-02 | If an attempt is made to reuse a renewal credential that was already used, the system must invalidate the session. |
| RNF-SEG-03 | The system must only serve requests from authorized web applications. Others must be rejected. |
| RNF-SEG-04 | The data that measuring devices send must travel encrypted. |
| RNF-SEG-05 | Recovery codes must be stored in a protected way, so that a data leak does not allow them to be used. |
| RNF-SEG-06 | Redeeming recovery codes must be limited to 10 attempts per address per 15-minute window, and tell the user how long to wait. Email verification and the confirmation of account deletion with a code must additionally be limited to 5 attempts per address per window. |
| RNF-SEG-07 | The internal functions used to authenticate devices must only be reachable from the system's own network and never from the internet. |
| RNF-SEG-08 | The system must protect user information in accordance with Law 1581 of 2012 (Congreso de la República de Colombia, 2012). |
| RNF-SEG-09 | Device credentials must be checked securely. Only a protected hash of each key is kept. |
| RNF-SEG-10 | The system's passwords and keys must not be exposed in the project's shared files. |
| RNF-SEG-11 | The administration functions must require, on every request, the global permission that matches the action. Without a session they are rejected as unauthenticated, without the permission as unauthorized, and any administration function that has no permission assigned is always rejected. |

### Performance

Table 14 sets the performance requirements.

**Table 14**

*Non-functional Performance Requirements*

| Code | Requirement |
|---|---|
| RNF-REN-01 | Data queries and charts must respond in under 2 seconds. This value has not been measured under load. |
| RNF-REN-02 | The system must process each device's information at a cadence of one reading every 60 seconds. |
| RNF-REN-03 | Sending emails must not keep the user waiting more than 5 seconds. |

### Availability and Fault Tolerance

Table 15 sets the availability and fault tolerance requirements.

**Table 15**

*Non-functional Availability and Fault Tolerance Requirements*

| Code | Requirement |
|---|---|
| RNF-DIS-01 | No target availability percentage is set for this version. |
| RNF-DIS-02 | The system must tolerate the occasional loss of readings. One lost reading plus network delay must not cause the offline state: the threshold is 150 seconds. |
| RNF-DIS-03 | If the system is unavailable, the device must keep measuring and hold up to 30 samples (30 minutes). If full, it drops the oldest. |
| RNF-DIS-04 | The service that receives the devices' data must retry its connection to the system every 10 seconds while the system is unavailable. |
| RNF-DIS-05 | If there is no Wi-Fi for 10 minutes, the device must restart. After 5 minutes without a known network, it must offer Bluetooth configuration. |
| RNF-DIS-06 | The system must allow its health status to be checked. |

### Compatibility

Table 16 sets the compatibility requirements.

**Table 16**

*Non-functional Compatibility Requirements*

| Code | Requirement |
|---|---|
| RNF-COM-01 | The web application must work in current browsers (Chrome, Firefox, Safari and Edge). |
| RNF-COM-02 | Linking over Bluetooth from the web is only available in desktop Chrome and Edge and Chrome on Android, and only on a secure connection. In other browsers the device's configuration portal is used. |
| RNF-COM-03 | The mobile application must run on Android devices. |
| RNF-COM-04 | The measuring device must include a dedicated electrical measurement sensor, with one sensor per board, and a board with Wi-Fi and Bluetooth. |

### Maintainability

Table 17 sets the maintainability requirements.

**Table 17**

*Non-functional Maintainability Requirements*

| Code | Requirement |
|---|---|
| RNF-MAN-01 | The system must be organized in functional modules (User and Profile Management, Home Management, Device Management, Measurements and Consumption, Alerts, Savings Recommendations, Notifications and Platform Administration), so that each one can be modified without affecting the others. |
| RNF-MAN-02 | Every change to the data structure must be reversible. |
| RNF-MAN-03 | Deleted data must be kept marked as deleted and not physically erased. |
| RNF-MAN-04 | The system must have automated tests that verify its main functions. |
| RNF-MAN-05 | Every change must pass the automated tests before it is integrated, and its release to production must require an approval, check the health of the system and go back to the previous version if the check fails. |

### Internationalization

Table 18 sets the internationalization requirements.

**Table 18**

*Non-functional Internationalization Requirements*

| Code | Requirement |
|---|---|
| RNF-I18-01 | The web application must be available in Spanish, English, French and Portuguese. |
| RNF-I18-02 | The mobile application must be available in Spanish, English, French and Portuguese. |
| RNF-I18-03 | Alerts and recommendations must be shown in the language each user has chosen. |

### Scalability

Table 19 sets the scalability requirements.

**Table 19**

*Non-functional Scalability Requirements*

| Code | Requirement |
|---|---|
| RNF-ESC-01 | The system must be able to grow in number of users and devices without affecting the user experience. |
| RNF-ESC-02 | No target numbers of concurrent users and devices are set for this version. |

### Usability

Table 20 sets the usability requirements.

**Table 20**

*Non-functional Usability Requirements*

| Code | Requirement |
|---|---|
| RNF-USA-01 | The interface must be clear, adaptable to different screen sizes and consistent, with immediate feedback. No formal WCAG conformance level is declared. |

### Interoperability

Table 21 sets the interoperability requirements with external services and with the measuring device.

**Table 21**

*Non-functional Interoperability Requirements*

| Code | Requirement |
|---|---|
| RNF-INT-01 | Measuring devices must communicate with the system through connections started from the home, so that they work behind any home router without changing the router configuration. |
| RNF-INT-02 | The system must send emails through an email account configured for that purpose. |
| RNF-INT-03 | The system must allow access with Google accounts, requesting only the profile name, email and photo. |
| RNF-INT-04 | Browser notifications must only work on a secure connection and require the system to have that service enabled. |
| RNF-INT-05 | Device configuration over Bluetooth must be available for up to 10 minutes, must not interrupt measuring and must not require prior pairing. |

## User Stories and Use Cases

User stories describe the interactions from the perspective of whoever uses them. Each one states the requirements it covers and its acceptance criteria in Given, When, Then format. The use cases detail the two most critical flows of the system.

### User Stories

#### HU-01 Register an Account

As a person who wants to monitor their consumption, I want to register and verify my email, so that I can access the system.

Related requirements: RF-SEC-01, RF-SEC-02, RF-SEC-13.

- CA-01. Given an email that is already registered, when I try to register with it, then the system shows an error and does not create a second account.
- CA-02. Given a successful registration, when I enter the six-digit code that arrived in my email within its validity window, then my account is created with the email already verified. If I do not verify within 24 hours, the request is discarded and no account is left.
- CA-39. Given a registration without accepting the terms and conditions, when I submit it, then the system rejects it asking for the acceptance and stores nothing. The same happens with the first sign-in with Google.
- CA-40. Given an email with a domain that looks like a typing error of a well-known provider (`meil.com`) or that does not exist, when I try to register, then the system rejects it and, where applicable, names the provider I probably meant to type.

#### HU-02 Sign In and Keep the Session

As a registered user, I want to sign in and keep my session securely, so that I can check my data without repeating my credentials all the time.

Related requirements: RF-SEC-03, RF-SEC-04, RF-SEC-10, RNF-SEG-01, RNF-SEG-02.

- CA-03. Given a successful sign-in, when the system grants access, then it lasts 15 minutes and renews while the session remains valid.
- CA-04. Given a renewal credential that was already used, when I try to use it a second time, then the system invalidates the session.
- CA-05. Given a request that indicates the identity of another user, when the system serves it, then it uses the identity of my session.

#### HU-03 Recover the Password

As a user who forgot their password, I want to recover it with a code sent to my email, so that I can get back into my account.

Related requirements: RF-SEC-06, RF-NOT-06, RNF-SEG-05, RNF-SEG-06.

- CA-06. Given an email that does not exist, when I request the code, then the system answers the same as if it existed. Attempt number 11 within 15 minutes from the same address is rejected and says how long to wait.
- CA-07. Given that email sending is not configured, when I request the code, then the system answers the same and the delivery is recorded as failed.

#### HU-04 Delete My Account

As a user, I want to delete my account, so that I stop using the service and release my email.

Related requirements: RF-SEC-11.

- CA-08. Given a wrong password, when I request to delete the account, then the system rejects it. With the correct password it deletes it, the email can register again and the sessions are closed.
- CA-09. Given that I am the only owner of a home with other members, when I request to delete the account, then the system rejects it with a readable message and changes no data.
- CA-41. Given an account created with Google, when I request to delete it, then the system sends a code to my email and deletes it only with the correct code; with a wrong or expired one it rejects it, and after five attempts in 15 minutes it asks me to wait. Once deleted, I can register again with the same Google account.
- CA-42. Given an account created with Google, when I try to change the password, then the system rejects it with a readable message.

#### HU-05 Create a Home and Set Its Thresholds

As an owner, I want to create a home, invite its members and set its consumption limits, so that the system warns me when they are exceeded.

Related requirements: RF-HOM-01 to RF-HOM-08.

- CA-10. Given a new home, when I reset its thresholds to the system values, then it shows 6 kWh daily and 180 kWh monthly.
- CA-11. Given a user who belongs to two homes, when they check their roles, then they can be owner in one and member in the other at the same time.

#### HU-06 Link a Measuring Device

As an owner, I want to link a measuring device to my home from the web, so that it starts sending readings from an appliance.

Related requirements: RF-DEV-02, RF-DEV-07, RF-DEV-08, RNF-SEG-04.

- CA-12. Given a device already linked to another home, when I try to link it, then the system rejects it. If I link it again to the same home, it keeps its history and receives new credentials.
- CA-13. Given a device with valid credentials, when it connects, then the system accepts its data. With wrong credentials it rejects it.
- CA-14. Given a linked device, when it tries to send data as if it were another device, then the system rejects it.

#### HU-07 Unlink a Device

As an owner, I want to remove a device from my home, so that the device is immediately available to link again.

Related requirements: RF-DEV-06.

- CA-15. Given a connected device, when I delete the device, then the device forgets its credentials and offers Bluetooth within a few seconds.

#### HU-08 Check My Home's Consumption

As a home member, I want to see the current consumption and the history of each appliance, so that I can identify the periods of highest demand.

Related requirements: RF-MED-01 to RF-MED-09.

- CA-16. Given an online device, when I check its consumption, then a new reading appears every 60 seconds with voltage, current, power and energy.
- CA-17. Given a message that claims to belong to a device other than the one sending it, when the system receives it, then it does not store it.
- CA-18. Given readings of 1499 W, 1500 W and 3000 W, when the system classifies them, then they are MEDIUM, HIGH and CRITICAL respectively.
- CA-19. Given that the system was unavailable, when the connection is restored, then the device sends up to 30 accumulated samples with their real time.

#### HU-09 Receive Alerts

As a home member, I want the system to alert me when consumption is excessive or a device disconnects, so that I can react in time.

Related requirements: RF-ALE-01 to RF-ALE-06, RF-MED-06.

- CA-20. Given an online device, when I disconnect it, then the system marks it offline in about 22 seconds if the device announces it, or at most at 150 seconds, and raises the disconnection alert. When it reconnects, the alert resolves itself.
- CA-21. Given a reading in the HIGH level, when the system processes it, then it raises a high consumption alert. The first LOW or MEDIUM reading resolves it.
- CA-22. Given the day's energy reaching the daily limit, when the system evaluates it, then it raises the daily limit alert. If I raise the limit above the consumption, the alert resolves.

#### HU-10 Receive Saving Recommendations

As a home member, I want to receive recommendations based on my own consumption, so that I can save energy.

Related requirements: RF-REC-01 to RF-REC-09.

- CA-23. Given that 40% or more of the last 7 days' energy (minimum 2 kWh) was used between 18:00 and 22:00, when the system evaluates the rules, then it creates a peak hours recommendation.
- CA-24. Given a recommendation that I deleted, when the system evaluates the rules again, then the same one does not reappear before 7 days.
- CA-25. Given a user who is not a member of the home, when they try to see its recommendations, then the system does not show them.

#### HU-11 Choose How to Receive Notifications

As a user, I want to choose whether I receive alerts and recommendations by email, by browser notification or both, so that I find out through the channel I prefer.

Related requirements: RF-NOT-01 to RF-NOT-05.

- CA-26. Given both channels on, when an alert is raised, then each member receives one email and one browser notification. With one channel off, they receive only the other.
- CA-27. Given any delivery attempt, when it ends, then it is recorded as sent or failed, without the message content.

#### HU-12 Use the System in My Language

As a user, I want to change the application language, so that I understand the information.

Related requirements: RF-CLI-05, RNF-I18-01, RNF-I18-02.

- CA-28. Given a language chosen among Spanish, English, French and Portuguese, when I change it in the web or the mobile application, then all texts switch to that language.

#### HU-13 Protect Access

As a system administrator, I want the system to reject unauthorized origins and networks, so that user data is protected.

Related requirements: RNF-SEG-03, RNF-SEG-07.

- CA-29. Given a request from an unauthorized web application, when the system responds, then it rejects it.
- CA-30. Given a request to the internal device authentication functions from a non-allowed network, when the system receives it, then it rejects it.

#### HU-14 Evolve the Data Without Losing Information

As a developer, I want every change to the data structure to be reversible, so that I can undo a failed change.

Related requirements: RNF-MAN-02.

- CA-31. Given any change to the data structure, when it is reviewed, then a way to revert it exists.

#### HU-15 Delete a Home or Transfer Its Ownership

As an owner, I want to delete my home or hand its ownership to another member, so that I can stop managing it without losing control of its data.

Related requirements: RF-HOM-05, RF-HOM-09, RF-HOM-10, RF-DEV-06.

- CA-32. Given a home with linked devices, when the owner deletes it, then the devices are unlinked and free, and members no longer see the home.
- CA-33. Given a home where I am a member, when I try to delete it or transfer its ownership, then the system rejects it for lack of permission.
- CA-34. Given a home with an owner and members, when the owner transfers ownership to a member, then the new owner takes the role, the previous one becomes a member and the home still has a single owner.

#### HU-16 Administer the Platform Accounts

As a system administrator, I want to view the accounts, change their status and manage the administrator role, so that I keep the platform under control without going into the database.

Related requirements: RF-ADM-01 to RF-ADM-09, RNF-SEG-11.

- CA-35. Given an account without the administrator role, when it tries to open the administration, then the system rejects it for lack of permission.
- CA-36. Given an active account with open sessions, when an administrator blocks it, then the account can no longer sign in and its sessions are closed.
- CA-37. Given an administrator, when they try to change the status or the roles of their own account, then the system rejects it with a readable message and changes nothing.
- CA-38. Given two administrators, when one of them grants the administrator role to another account, then the other administrator receives a notice inside the application and whoever made the change does not.

#### HU-17 Consult the Help from Settings

As a person who uses EnergyMonitor, I want to read the documentation, ask for support and see the news without leaving the application, so that I can solve my doubts quickly.

Related requirements: RF-CLI-07, RF-CLI-08, RF-HOM-12, RF-ALE-07.

- CA-43. Given a 300 W reading of an appliance that usually uses 100 W, when the system processes it, then it raises an unusual consumption alert; with a 240 W reading or with less than 24 hours of history it raises nothing, and the pending alert resolves itself when it is back to normal.
- CA-44. Given a user or an administrator, when they open the documentation in Settings, then they see only the guide of their role.
- CA-45. Given a user, when they open technical support in Settings, then they see a short explanation and can create the ticket as an email to an administrator; an administrator does not see the support, because they are the one who attends it.
- CA-46. Given a new version of the web application deployed, when I open the news, then I see the changes of that version without anybody having written them by hand.
- CA-47. Given an account with a photo, when I choose "View photo", then it is enlarged inside the page, and in a home I see the photo of every member that has one.

### Use Cases

#### CU-01 Link a Measuring Device

Table 22 summarizes the device linking use case.

**Table 22**

*Use Case CU-01 Link a Measuring Device*

| Field | Detail |
|---|---|
| Actors | Home owner, measuring device |
| Preconditions | The user is the owner of the home. The device is in setup mode (freshly set up, never linked, or with the device button held for 3 seconds). The browser is Chrome or Edge on a secure connection |
| Postconditions | The device is registered with its credentials, connects to the system and leaves setup mode |
| Related requirements | RF-DEV-02, RF-DEV-07, RF-DEV-08, RNF-INT-05 |

Normal flow:

1. The owner opens Home, Devices, Link device.
2. The application searches for the device over Bluetooth and the owner picks `EnergyMonitor-<code>`.
3. The owner chooses the appliance, the name and the room.
4. The application lists the Wi-Fi networks the device sees and the owner picks their home network and types its password.
5. The application registers the device in the system, which gives it its credentials.
6. The application sends the device, over Bluetooth, the network, the password, the service address and the credentials, and shows the device's real progress.
7. The device connects to the system, which verifies its credentials, and leaves setup mode.

Alternate flows:

- A1. The browser has no Bluetooth: The user connects to the `EnergyMonitor-Setup` network, opens the Energy Monitor page, and completes the setup on the portal.
- A2. The device is already linked to another home: the system rejects the linking.
- A3. The Wi-Fi password is wrong: the device reports it and the owner corrects the password.

#### CU-02 Handle a Disconnection Alert

Table 23 summarizes the use case of the alert for a device disconnection.

**Table 23**

*Use Case CU-02 Handle a Disconnection Alert*

| Field | Detail |
|---|---|
| Actors | Measuring device, system, home members |
| Preconditions | The device is linked and online. Members have at least one notification channel active |
| Postconditions | The alert is resolved when the device reports again. The device state is recorded |
| Related requirements | RF-MED-04 to RF-MED-06, RF-ALE-02, RF-NOT-01 |

Normal flow:

1. The device loses power or connection.
2. The system receives the device's disconnection notice about 22 seconds later.
3. The system marks the device as offline and raises the disconnection alert.
4. The system delivers the alert to all members of the home by email and browser notification, according to their preferences.
5. The device reconnects automatically.
6. The system records the online state and resolves the alert.

Alternate flows:

- A1. The disconnection notice does not arrive: the system marks offline after 150 seconds without data.
- A2. A member's channels are off: that member does not receive the notification, but the alert remains visible in the application.

## References

Congreso de la República de Colombia. (2012, October 17). *Ley 1581 de 2012: Por la cual se dictan disposiciones generales para la protección de datos personales* [Law 1581 of 2012: By which general provisions for the protection of personal data are issued]. Diario Oficial No. 48,587.

Institute of Electrical and Electronics Engineers. (1998). *IEEE recommended practice for software requirements specifications* (IEEE Std 830-1998). https://doi.org/10.1109/IEEESTD.1998.88286
