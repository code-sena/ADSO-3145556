<!-- running-head: USER MANUAL: VEHICLE WASHING SYSTEM -->

# User Manual

Vehicle Washing Management System (Vehicle Washing)

Web Platform for Booking Washes, Paying by QR Code, and Managing the Operator's Daily Work

Brayan Estiven Patiño Cabrera (Project Leader), Juan Camilo Guilombo Osorio (Frontend Leader), and Merari Urbano (Backend Leader)

Technologist in Software Analysis and Development (ADSO), Servicio Nacional de Aprendizaje (SENA), Training Center: Industria

Ficha 3145556

Instructor: Karol Daniela Correa

October 27, 2026

Manual Type: User Manual (Customer and Operator Roles, on the web and in the mobile application) · Version 4.13

## Version Control

Table 1 records the change history of this document. Each time the development team modifies the manual, a new row must be added with the version, the date, the description of the change, and the person responsible.

**Table 1**

*Version History of the User Manual*

| Version | Date | Description of the change | Responsible |
|---|---|---|---|
| 1.0 | 2026-10-03 | Initial creation of the user manual (in Spanish) for the Customer and Operator roles. | Development team |
| 2.0 | 2026-10-05 | Cover data completed (ficha, instructor, and training center) and the system name adjusted to Vehicle Washing. | Development team |
| 3.0 | 2026-10-07 | Translation into English, formatting according to the APA Style (7th ed.), restructuring according to the SENA guide, and verification against the source code of the web application. Corrected: screen routes, registration fields (ID number, first and last names, acceptance of the legal documents), password rules, absence of account lockout, booking flow (no operator choice, no rescheduling from the web), payment flow (no promotion code, reference rules), history filters, notifications by email, and operator statuses. | Development team |
| 3.1 | 2026-10-09 | Update against the code of October 9, 2026, and creation of the Spanish version (Manual_usuario_LavadoVehicular_3145556_ES.md). Added: loyalty points per service and in the booking summary, loyalty progress and unlocked coupons in the dashboard, coupon redemption when paying, list of missing data under the confirm button, payment status in the history and Pay now only when the booking can be paid, email notices of approved, rejected, and refunded payments, points earned, unlocked coupons, and inspection reports, notification switches saved in the account, staff notices for the operator, and new messages. Removed: WhatsApp from the help center. | Development team |
| 4.0 | 2026-10-16 | Review against the code of October 16, 2026. The figures changed from placeholders to real screenshots of the running system with test data. Added: optional operator choice when booking and automatic assignment of a free one, account lockout for 15 minutes after five failed attempts, messages that explain why a time cannot be booked, starting the service from 15 minutes before the booked time, downloading the Android application from the web, the profile address saved in the account, and the Deactivate my account option, formerly called Delete account. Removed: the Report progress button, which no longer exists on the operator screen. | Development team |
| 4.1 | 2026-10-17 | Review against the code of October 17, 2026. Updated the welcome screen ("About us" section without figures, new frequently asked questions section, footer without social networks or contact links) and described how it adapts to phones and tablets. Clarified that the progress of the next service stays at 0% until the wash starts and that the dashboard greeting names the car wash Lavado Vehicular. | Development team |
| 4.2 | 2026-10-19 | Administrator accounts do not have the Deactivate my account option (frequently asked questions). | Development team |
| 4.3 | 2026-10-19 | Screenshots taken again on October 19, 2026 (date on the dashboards, calendars without past days, and booking summary). | Development team |
| 4.4 | 2026-10-20 | The customer's and the operator's history go from the newest booking to the oldest and open the detail on click, with the **Go to payment** and **Go to ratings** buttons. Notifications open their detail on click, with a button to go to the section the notice comes from. Once the payment is approved, the payment screen only shows the summary. | Development team |
| 4.5 | 2026-10-21 | Review against the code of October 21, 2026: the receipt is reduced below 2 MB before it is sent and the QR can be enlarged with **Enlarge QR** (section 10.1.5); Cash only appears if the business keeps that account active; bookings nobody starts close on their own one hour after their end time (Table 7, section 10.2.3, and frequently asked questions); when signing out, the other tabs sign out too (section 12); the mobile app asks to install the new APK when it is published (section 6.2). The date and the version of the cover were corrected. | Development team |
| 4.6 | 2026-10-21 | Change of the rule for bookings that end without being recorded, reviewed by the team on the night of October 21: only a confirmed booking that never had an operator becomes No show on its own; if it had an operator, the operator receives the notice "¿Atendiste este servicio?" (did you serve this booking?) and the administrator closes it (Table 7, sections 10.2.3 and 10.2.4, and frequently asked questions). The schedule and the assigned services no longer show cancelled or No show bookings. | Development team |
| 4.7 | 2026-10-23 | Review against *develop* of October 23, 2026: new chapter on the Android mobile application for the three roles (section 10.3), written from the app's 31 screens; the operator can start from 15 minutes before the booked time and there is no later limit (sections 10.2.2 and 13); the applied coupon is removed with the **X** and the system cancels the redemption, and only one coupon is accepted per booking (section 10.1.5). | Development team |
| 4.8 | 2026-10-24 | Review against *develop* of October 24, 2026, which runs on the public server since that day: the screens refresh on their own, without reloading (4.1 and 10.3.4); sign-in accepts any email domain and sign-up still requires @gmail.com (8.1, 8.2, and 10.3.2); the server requires the consent, limits repeated attempts, and voids the recovery code at the 5th mistake (8.1 to 8.3); the amount to pay and the coupon come from the server and are kept after reloading (10.1.5); a vehicle with active bookings is not removed (10.1.2); the operator sees the administrator's notes and can start up to 4 hours after the end time (10.2.2, 10.2.3, and 13); payment notices for a cancelled booking and read-only permissions for the administrator in the app (10.3.6); new error messages (Tables 11 and 12). | Development team |
| 4.9 | 2026-10-26 | Payment at booking, reviewed against *develop* of October 26, 2026 and deployed to the public server that day: "How will you pay?" step when booking, 30-minute deadline to upload the digital payment receipt, and at most one active cash booking (10.1.3); booking cancelled for lack of payment (10.1.4); payment screen according to the chosen method and cash label (10.1.5 and 10.3.4); new messages (Table 12) and a frequently asked question about the payment deadline (13). | Development team |
| 4.10 | 2026-10-26 | The Customer User Manual and the Operator User Manual (version 1.0) are created, with the content of this manual separated by role as the SENA guide recommends; this manual is kept as a joint reference for both roles. Corrections: the operator also has the notification switches and **Deactivate my account** (10.2.7), and mobile app *v1.1.0* is already published (10.3.4). | Development team |
| 4.11 | 2026-10-26 | Mobile application 1.1.1: APK *versionCode* 6 and over-the-air update published on October 26, 2026 (10.3 and 10.3.4). | Development team |
| 4.12 | 2026-10-27 | The alternative times of "That time was just taken" respect the other bookings of the same vehicle (10.1.3 and 13). | Development team |
| 4.13 | 2026-10-27 | Section 10.3 points to the mobile application screenshots of the role manuals. | Development team |

*Note.* Dates are written in the ISO 8601 format (year-month-day). Prepared by the development team.

## Table of Contents

<!-- toc -->

- [Version Control](#version-control)
- [4. Introduction](#4-introduction)
  - [4.1 Sources and Conventions of This Manual](#41-sources-and-conventions-of-this-manual)
- [5. Objective](#5-objective)
  - [5.1 General Objective](#51-general-objective)
  - [5.2 Specific Objectives](#52-specific-objectives)
- [6. Scope](#6-scope)
  - [6.1 What This Manual Covers](#61-what-this-manual-covers)
  - [6.2 What This Manual Does Not Cover](#62-what-this-manual-does-not-cover)
  - [6.3 About the Screenshots](#63-about-the-screenshots)
- [7. Requirements to Use the System](#7-requirements-to-use-the-system)
  - [7.1 Device and Browser Requirements](#71-device-and-browser-requirements)
  - [7.2 System Roles](#72-system-roles)
  - [7.3 System Overview](#73-system-overview)
- [8. Logging In to the System](#8-logging-in-to-the-system)
  - [8.1 Registering a New Customer](#81-registering-a-new-customer)
  - [8.2 Logging In](#82-logging-in)
  - [8.3 Password Recovery](#83-password-recovery)
- [9. Main Menu](#9-main-menu)
  - [9.1 Customer Menu](#91-customer-menu)
  - [9.2 Operator Menu](#92-operator-menu)
- [10. System Modules](#10-system-modules)
  - [10.1 Customer Role Modules](#101-customer-role-modules)
  - [10.2 Operator Role Modules](#102-operator-role-modules)
  - [10.3 Android Mobile Application](#103-android-mobile-application)
- [11. Messages and Validations](#11-messages-and-validations)
- [12. Logging Out](#12-logging-out)
- [13. Frequently Asked Questions](#13-frequently-asked-questions)
- [14. Glossary](#14-glossary)
- [References](#references)
- [15. Annexes](#15-annexes)
  - [Annex A. Source of the Screenshots](#annex-a-source-of-the-screenshots)
  - [Annex B. Delivery Checklist](#annex-b-delivery-checklist)

<!-- /toc -->

*Note.* Numbering starts at 4 because sections 1 to 3 are the cover, the version control, and the table of contents, which have no number in their title. It is kept this way so that the cross-references between documents by section number (for example, "Installation Manual, section 10") remain valid.

## 4. Introduction

The Vehicle Washing Management System (Vehicle Washing) is a web platform that lets the customers of a vehicle washing business book a slot from a phone or a computer, and that organizes in a single place the work of the operators, the verification of payments, and the follow-up of each service.

Before the system, customers had to call or travel to the premises without knowing whether a slot was free. Inside the business, bookings were written down by hand, slots could overlap, work was distributed at the manager's discretion, and cash payments left no record of the customer. The system replaces that procedure with a digital flow: the customer checks the real availability of the business, books in a few minutes, pays by scanning the business's QR code, and keeps the history and the rating of each service.

This manual explains, in simple language and step by step, how the two people who use the system every day work with it: the **customer**, who is the owner of the vehicle, and the **operator**, who is the employee who performs the wash. No knowledge of programming, databases, or technical configuration is needed to follow the instructions.

The document follows the structure of the guide for writing software manuals of the Technologist in Software Analysis and Development program (Servicio Nacional de Aprendizaje [SENA], 2026) and applies the APA Style, seventh edition, to headings, tables, figures, citations, and references. Its content was verified against the source code of the web application (Vehicle Washing Development Team, 2026b) and the project documentation (Vehicle Washing Development Team, 2026a). Regarding the personal data of users, the system follows Statutory Law 1581 of 2012, which regulates the processing of personal data in Colombia (Congreso de la República de Colombia, 2012).

### 4.1 Sources and Conventions of This Manual

The screens, routes, buttons, and messages described here correspond to the *develop* branch of the web application as of October 17, 2026. In the interface the system appears under its trade name, **LavaRápido**. The names of buttons, menus, and messages are written in **bold** or between quotation marks exactly as they appear when the interface language is English; the interface is also available in Spanish, French, and Portuguese. A route such as */client/vehicles* is the part of the web address that follows the domain of the system (for example, *http://localhost:4200/client/vehicles* in the local environment).

**Data always up to date (since October 24, 2026).** Reloading the page is no longer needed to see a change made by someone else: the web refreshes the screen on entering, on returning to the tab or window, and whenever a new notification arrives, and the screens that wait for another person (for example, **Pay service** while the administrator reviews the payment) refresh every 45 seconds while the tab is visible. The mobile app refreshes on returning to the screen and when a push notification arrives. So when the administrator approves a payment or assigns an operator, the customer sees it in less than a minute.

## 5. Objective

### 5.1 General Objective

To explain step by step how the customer and the operator must log in, navigate, and use the main functions of the Vehicle Washing Management System, so that any person can use the software without the help of the development team.

### 5.2 Specific Objectives

- Guide the customer through account registration, login, and password recovery.
- Describe how the customer registers vehicles, books a wash, cancels a booking, pays for the service, and rates the attention received.
- Describe how the operator checks the schedule, reviews the services assigned, starts and finishes each service, and consults the ratings received.
- Explain the meaning of the messages and validations shown by the system and the action the person must take in each case.
- Answer the most frequent questions of the people who use the platform.

## 6. Scope

### 6.1 What This Manual Covers

- The web application of the system, used from a browser on a computer, tablet, or phone, and the Android mobile application, with its differences of use for the three roles (section 10.3).
- The **Customer** role: registration, login, password recovery, dashboard, vehicles, bookings, payments, loyalty points and coupons, history, ratings, notifications, profile, and settings.
- The **Operator** role: dashboard, schedule, assigned services, starting and finishing services, notifications, history, ratings received, profile, and settings.
- Appearance preferences (four color themes) and language (Spanish, English, French, and Portuguese).

### 6.2 What This Manual Does Not Cover

- The functions of the **Administrator** role (bookings, operator assignment, payment review, schedules and bays, users, services, reports, and configuration), which are explained in the Administrator Manual.
- The installation, configuration, and internal construction of the software, which are explained in the Installation Manual and the Technical Manual.
- The detail of the Administrator role rules in the mobile application: section 10.3.6 summarizes its screens, and the rules are those of the Administrator Manual.
- At-home washing: in the current version the customer brings the vehicle to the business on the booked date and time.
- Electronic invoicing with the DIAN (Colombian tax authority), payment gateways, and the management of supplies or inventory.

### 6.3 About the Screenshots

The figures in this manual are real screenshots of the system running in the local environment, taken on October 16, 2026, with test accounts and data. They are ordered according to the flow of use, and their source is explained in Annex A.

## 7. Requirements to Use the System

### 7.1 Device and Browser Requirements

The system works on the current versions of Google Chrome, Mozilla Firefox, and Microsoft Edge, and its interface adapts to the screen of a phone, a tablet, or a computer. Table 2 lists the requirements.

**Table 2**

*Minimum Requirements to Use the System*

| Resource | Requirement |
|---|---|
| Device | Computer, tablet, or smartphone. |
| Browser | Google Chrome, Mozilla Firefox, or Microsoft Edge, in an updated version. |
| Connection | Stable internet access throughout the use of the system. |
| Email | An email account ending in @gmail.com. The system only accepts this type of email. |
| Identity document | A Colombian ID number of 5 to 20 digits. |
| Phone | A 10-digit Colombian mobile number starting with 3. |
| Account | A registered and active user. Operators receive their account from the business administrator. |
| Digital payment (customers) | Nequi, Daviplata, or a banking app to scan the QR code and make the transfer. |

*Note.* Prepared by the authors from the validations of the registration form and the non-functional requirements of the system.

### 7.2 System Roles

The system recognizes three fixed roles. Each person sees only the options of the role and cannot open the screens of another role: if the person tries to do so, the system returns to the home screen of the role (Table 3).

**Table 3**

*System Roles and General Description*

| Role | Who they are | What they do in the system | Home route |
|---|---|---|---|
| Customer | Owner of the vehicle. | Registers vehicles, books slots, pays, rates the service, and consults the history. | /client |
| Operator | Employee who performs the wash. | Consults the schedule and the assigned services, starts and finishes each service, and consults the ratings. | /operator |
| Administrator | Owner or manager of the business. | Manages bookings, operators, payments, schedules, users, and reports. Documented in the Administrator Manual. | /admin |

*Note.* Prepared by the authors from the route configuration of the web application.

### 7.3 System Overview

**Welcome screen.** When the web address of the system is opened, the welcome screen appears. At the top there is a navigation bar with the links **Home**, **About**, and **Services**, a language selector, and the **Login** and **Register** buttons. The central block shows the **Get started** and **I already have an account** buttons; further down appear, in this order, the "About us" section, "Why choose us?", the washing services with their description and duration, the mobile app download, the contact details of the car wash, and the **Frequently asked questions** section, which answers in an accordion (one question open at a time) how to book, the payment methods, cancellation, choosing the operator, points, reminders, and the mobile app. The footer has the links **About us**, the three services, **Help center**, **Frequently asked questions** (which goes to that section), **Terms of service**, and **Privacy policy**.

**Use on phones and tablets.** Every screen adapts to the width of the device from 360 pixels, without horizontal scrolling. On a phone, the top bar shows only the language selector and **Login** (registration is still available with **Get started**), the buttons of the front block take the full width, and, once logged in, the side menu opens with the ☰ button in the upper left corner.

**Figure 1**

*System Welcome Screen*

![System Welcome Screen](capturas/user-01-system-welcome-screen.jpg)

*Note.* Capture in a desktop browser with the full window.

**Types of washing service.** The business offers three levels of cleaning. Each service has its own duration and price, and **the price depends on the type of vehicle**; for this reason, the final amount is shown in the booking summary once the person chooses the vehicle and the service (Table 4).

**Table 4**

*Washing Service Levels*

| Service | What it includes | Estimated duration |
|---|---|---|
| Basic | Full exterior wash, basic vacuuming, and window cleaning. | 45 minutes |
| Premium | Everything in Basic, plus engine wash and protective liquid wax. | 75 minutes |
| Complete | Everything in Premium, plus hand waxing and interior detailing. | 120 minutes |

*Note.* Durations correspond to the initial data of the system. Prices and durations are configured by the administrator for each vehicle type and may change.

**Languages and themes.** The complete interface is available in Spanish, English, French, and Portuguese, and in four color themes (green light, green dark, pink, and pink dark). The language can be changed from the welcome screen or, once logged in, from **Settings** (section 10.1.9).

## 8. Logging In to the System

Every person must have an account to use the functions of the system. Customers create their own account through registration; operators receive their account from the business administrator, with a temporary password that they should change after the first login.

### 8.1 Registering a New Customer

**Module name:** Customer registration.

**Description:** Allows a new person to create a customer account to book washing services.

**Role that can use it:** Person without an account (future customer).

**Route or menu where it is found:** Welcome screen > **Register** button (route */auth/register*).

**Steps to use it:**

1. Open the browser and enter the web address of the system.
2. Click **Register** on the top bar (or **Get started**).
3. Type the **ID Number**: only digits, between 5 and 20.
4. Type the **First Names** (minimum 3 characters) and the **Last Names** (minimum 2 characters).
5. Type the **Email Address**, which must end in @gmail.com (sign-up only accepts Gmail; the server checks it too). The field does not allow spaces.
6. Type the **Phone Number**: a 10-digit Colombian mobile number starting with 3. The field only accepts digits.
7. Type the **Password** and watch the list of requirements, which are checked as they are met: minimum 8 characters, at least one uppercase letter, at least one number, and at least one special character. **All four requirements are mandatory**; spaces are not allowed.
8. Repeat the password in **Confirm Password**. The eye icon shows or hides what was typed.
9. Open the **Terms and Conditions** and the **Personal Data Processing Policy** with their links and accept both. Clicking **Accept and continue** inside each document checks the corresponding box automatically.
10. Click **Register**. While the request is processed, the button shows "Registering...".

**Expected result:** The system shows the message "Registration successful!" and, after clicking **Go to login**, takes the person to the login screen.

**Notes or restrictions:** It is not possible to register two accounts with the same email address or the same ID number. In those cases, the system shows "An account with this email already exists" or "An account with this ID number already exists" and does not create the account. Since October 24, 2026 the server also requires both acceptance checkboxes: without them it answers "You must accept the personal data policy (Law 1581) to sign up."

**Figure 2**

*Customer Registration Form*

![Customer Registration Form](capturas/user-02-customer-registration-form.jpg)

*Note.* No real personal information may appear in the screenshot.

**Figure 3**

*Validation Messages in Registration*

![Validation Messages in Registration](capturas/user-03-validation-messages-in-registration.jpg)

*Note.* Supports Table 13 in section 11.

### 8.2 Logging In

**Module name:** Login.

**Description:** Allows the customer, the operator, or the administrator to enter the system with their credentials.

**Role that can use it:** Customer, Operator, and Administrator.

**Route or menu where it is found:** Welcome screen > **Login** button (route */auth*).

**Steps to use it:**

1. Open the browser and enter the web address of the system.
2. Click **Login**.
3. Type the registered **Email Address**. Since October 24, 2026 sign-in accepts an email of any domain (only the email format is checked), so accounts created earlier with another provider can get in; sign-up still requires @gmail.com.
4. Type the **Password**.
5. Click **Login**. While the request is processed, the button shows "Signing in...".

**Expected result:** The system opens the home screen of the person's role: */client* for the customer, */operator* for the operator, or */admin* for the administrator.

**Notes or restrictions:** If the email or the password is wrong, the system shows "Incorrect email or password" and does not reveal which of the two failed. After five failed attempts within 15 minutes, the account is locked for 15 minutes and the system shows "Too many failed attempts. Your account is locked for 15 minutes; wait or use "Forgot your password?" to get in now." If the person resets the password, they can log in right away. The session lasts one hour; when it expires, the system returns to the login screen with the message "Your session expired. Please sign in again." A deactivated account cannot log in and receives the message "Your account is disabled. Contact the administrator." Since October 24, 2026, if too many attempts are made in a row from the same device (more than 60 in a minute across sign-in, sign-up, and recovery), the system answers "Too many attempts in a row. Wait a moment and try again."

**Figure 4**

*Login Screen*

![Login Screen](capturas/user-04-login-screen.jpg)

*Note.* Use a test email; do not show real passwords.

### 8.3 Password Recovery

**Module name:** Password recovery.

**Description:** Allows the person to recover access to the account when the password has been forgotten, by means of a six-digit verification code sent by email.

**Role that can use it:** Customer, Operator, and Administrator.

**Route or menu where it is found:** Login screen > **Forgot your password?** link (route */auth/forgot-password*).

**Steps to use it:**

1. On the login screen, click **Forgot your password?**.
2. **Step 1 (Recover Password):** type the email address of the account and click **Send Code**.
3. Open the email received and copy the six-digit code. The code is valid for 15 minutes.
4. **Step 2 (Verification):** type the six digits, one in each box, and click **Verify Code**. If the email did not arrive, click **Didn't receive the code? Resend**.
5. **Step 3 (New Password):** type the new password, which must meet the four requirements, confirm it, and click **Update Password**.
6. When the message "Password updated!" appears, click **Go to login** and log in with the new password.

**Expected result:** The password is changed and the code used cannot be used again.

**Notes or restrictions:** For security, the system shows the same screen whether the email is registered or not, so it does not reveal which accounts exist. Each new code invalidates the previous one ("We sent you a new code. The previous one no longer works."). After five incorrect attempts the code stops working and the system shows "Too many failed attempts. Wait a few minutes or request another code to try again." (since October 24, 2026): request a new one with **Resend** (in the app, **Request another code**). A three-step indicator at the top shows the stage of the process.

**Figure 5**

*Password Recovery, Step 1: Email Address*

![Password Recovery, Step 1: Email Address](capturas/user-05-password-recovery-step-1-email-address.jpg)

**Figure 6**

*Password Recovery, Step 2: Verification Code*

![Password Recovery, Step 2: Verification Code](capturas/user-06-password-recovery-step-2-verification-code.jpg)

**Figure 7**

*Password Recovery, Step 3: New Password*

![Password Recovery, Step 3: New Password](capturas/user-07-password-recovery-step-3-new-password.jpg)

**Figure 8**

*Password Change Confirmation*

![Password Change Confirmation](capturas/user-08-password-change-confirmation.jpg)

## 9. Main Menu

After logging in, the system shows a side menu with the options of the person's role. At the top of the menu appear the initials, the name, and the email of the person with the open session; at the bottom is the **Log Out** option. On a phone, the menu is opened and closed with the menu icon.

### 9.1 Customer Menu

**Table 5**

*Main Menu Options of the Customer Role*

| Option (route) | What it is for |
|---|---|
| Dashboard (/client) | Shows a summary of bookings, vehicles, the next service, quick access links, and benefits. |
| Profile (/client/profile) | Allows viewing and editing personal data, changing the email and the password, and deactivating the account. |
| My vehicles (/client/vehicles) | Allows registering, editing, deleting, and viewing the customer's vehicles. |
| Book Wash (/client/reserve) | Allows scheduling a new washing service. |
| Pay service (/client/payment) | Allows paying a booking with a QR code or in cash, redeeming a coupon, and canceling it. |
| Notifications (/client/notifications) | Shows the notices about bookings, reminders, assignments, and the progress of the service. |
| History (/client/history) | Lists the bookings with their payment status, and allows paying, canceling, and rating them. |
| Settings (/client/configuration) | Allows choosing the theme, the language, and the notification preferences, and opening the help center and the legal documents. |
| Log Out | Ends the session securely. |

*Note.* The option names are those of the side menu when the interface is in English.

**Figure 9**

*Side Menu of the Customer Role*

![Side Menu of the Customer Role](capturas/user-09-side-menu-of-the-customer-role.jpg)

### 9.2 Operator Menu

**Table 6**

*Main Menu Options of the Operator Role*

| Option (route) | What it is for |
|---|---|
| Dashboard (/operator) | Shows the pending services, the progress of the day, and summary indicators. |
| Profile (/operator/profile) | Allows viewing and editing personal data and changing the password. |
| My Schedule (/operator/schedule) | Shows the services by day or by week and allows starting and finishing them. |
| Assigned services (/operator/assigned-services) | Lists the assigned services with their detail and allows starting and finishing them. |
| Notifications (/operator/notifications) | Shows the notices about assignments and status changes. |
| History (/operator/service-history) | Lists the services performed with their final status and rating. |
| Ratings (/operator/qualifications) | Shows the ratings left by customers. |
| Settings (/operator/settings) | Allows choosing the theme and the language and opening the help center. |
| Log Out | Ends the session securely. |

*Note.* The option names are those of the side menu when the interface is in English.

**Figure 10**

*Side Menu of the Operator Role*

![Side Menu of the Operator Role](capturas/user-10-side-menu-of-the-operator-role.jpg)

## 10. System Modules

Each module is documented with the same format: name, description, role that can use it, route or menu, steps, expected result, and notes. The modules are grouped by role.

### 10.1 Customer Role Modules

#### 10.1.1 Customer Dashboard

**Module name:** Customer dashboard.

**Description:** Home screen of the customer. It summarizes the activity and offers shortcuts to the most frequent actions.

**Role that can use it:** Customer.

**Route or menu where it is found:** Side menu > **Dashboard** (route */client*).

**Steps to use it:**

1. Log in with a customer account; the system opens the dashboard automatically.
2. Review the summary cards: **Active bookings**, **My vehicles**, and **Washes completed**.
3. Check the **Your next service** block, which shows the service, the vehicle, the date, the time, the **Assigned operator**, and the progress of the service. The progress stays at 0% while the booking is scheduled or confirmed, because the wash has not started yet; it rises when the operator starts the service and reaches 100% when it finishes. Click **View detail** to see all the information.
4. Use the **Quick access** links (Book a wash, Pay service, My vehicles, and Notifications) to go directly to each module.
5. Review the **Your benefits and promotions** block: the **Loyalty progress** bar shows the points accumulated ("N of M points") and, for each promotion, either "Unlocked! Redeem it when you pay." or how many points are still missing to unlock it.

**Expected result:** The customer sees the most important information and reaches any module with a single click.

**Notes or restrictions:** If there are no bookings, the block shows "No scheduled services" instead of an empty screen. The points are credited when the administrator approves the payment of a booking (or records a payment at the business), not when the service ends; until the first payment is approved, the benefits block shows "No loyalty data yet". Points are never spent: they unlock coupons, and redeeming a coupon does not reduce the balance.

**Figure 11**

*Customer Dashboard*

![Customer Dashboard](capturas/user-11-customer-dashboard.jpg)

#### 10.1.2 My Vehicles

**Module name:** My vehicles.

**Description:** Allows registering the customer's vehicles and viewing, for each one, the last wash and the total number of washes. A vehicle must be registered before a service can be booked.

**Role that can use it:** Customer.

**Route or menu where it is found:** Side menu > **My vehicles** (route */client/vehicles*).

**Steps to use it:**

1. Click **My vehicles** in the side menu.
2. Review the indicators at the top: **Registered vehicles**, **Accumulated washes**, and **Last wash performed**.
3. To add a vehicle, click **Register vehicle**.
4. In the "Register new vehicle" window, select the **Vehicle type** (Car, Sedan, SUV, Pickup, Truck, or Motorcycle) and type the **Brand**, the **Model**, the **Plate**, and the **Color**.
5. Click **Save vehicle** and verify that the vehicle appears in the list with the message "Vehicle registered!".
6. To correct the data of a vehicle, use the edit option of its card, change the fields, and click **Save changes**.
7. To remove a vehicle, use the delete option of its card and confirm in the "Delete vehicle?" window. Since October 24, 2026, if the vehicle has active or upcoming bookings, the system does not remove it and shows "This vehicle has active or upcoming bookings: cancel them before removing it."

**Expected result:** The vehicle is registered and available to be selected when booking a wash.

**Notes or restrictions:** The plate must be an official Colombian plate: three letters and three numbers for cars and other vehicles (for example, ABC123), or three letters, two numbers, and one letter for motorcycles (for example, ABC12D). Foreign or promotional plates are not accepted. The brand is mandatory (at least two characters) and the color only accepts letters. The same plate cannot be registered twice while the vehicle is active. The vehicle type matters because it determines the price of each service.

**Figure 12**

*Customer Vehicle List*

![Customer Vehicle List](capturas/user-12-customer-vehicle-list.jpg)

**Figure 13**

*Form to Register a Vehicle*

![Form to Register a Vehicle](capturas/user-13-form-to-register-a-vehicle.jpg)

#### 10.1.3 Book a Wash

**Module name:** Book a wash.

**Description:** Allows scheduling a washing slot by choosing the vehicle, the type of service, the date, the time, and the payment method. The system only offers times that are really free.

**Role that can use it:** Customer.

**Route or menu where it is found:** Side menu > **Book Wash** (route */client/reserve*), or the **Book a wash** quick access link.

**Steps to use it:**

1. Click **Book Wash**.
2. **Step 1 (Choose your vehicle):** select one of the registered vehicles. If the vehicle is not listed, use the **My Vehicles** link to register it first.
3. **Step 2 (Service type):** select Basic, Premium, or Complete. Each card shows what it includes, the duration, the price for the type of vehicle chosen, and the loyalty points it adds ("Earns N points").
4. **Step 3 (Date and time):** choose the date (the calendar does not allow past days) and then one of the free times shown in **Select a time**. The times are offered every 30 minutes, within the opening hours, outside the break, and only when a washing bay is free for the whole duration of the service.
5. Optionally, choose in **Operator (optional)** one of the operators who are free at that time. If it is left as "No preference: we assign a free one", the system assigns one automatically.
6. **Step 4 (How will you pay?):** choose one of the two options (since October 26, 2026):
   - **Digital payment now:** Nequi, Daviplata, or bank transfer. The QR code is scanned and the receipt is uploaded **within the next 30 minutes** (or before the service time, if that comes first).
   - **Cash at the car wash:** paid on arrival, and the administrator records the payment, without a receipt. It only appears if the business has the cash account active, and the customer can have **at most one active cash booking**.
7. Review **Your booking summary**: vehicle, plate, service, estimated duration, date, time, location, total, and **Points you earn**.
8. Click **Book Now**.

**Expected result:** The booking is created immediately with the status Confirmed and a washing bay, and the customer receives the confirmation in their notifications and by email. What follows depends on the payment method:

- **Digital payment:** the system opens the payment screen of that booking directly, with the notice "You have a deadline to pay: Upload the receipt before HH:mm or the booking will be released." If the receipt is not uploaded in time, the booking is cancelled automatically with the reason "The payment receipt was not uploaded in time" and the slot is freed for other customers.
- **Cash:** "Booking created!" appears with the summary and the message "Your wash is scheduled. You pay in cash at the car wash."; the **View my bookings** button opens the history. There is nothing to pay online.

**Notes or restrictions:**

- Choosing the operator is optional. If the chosen operator is no longer free when the booking is confirmed, the system assigns another one ("If the operator you chose is no longer free when we confirm, we assign another one."). The assigned operator is shown in the dashboard and in the notifications, and the administrator can change it later.
- Bookings can be made up to 60 days in advance.
- If the day is closed, the system shows "The car wash is closed that day"; if there are no free times, "No free times that day".
- If another person takes the time while the customer is booking, the system saves nothing, shows "That time was just taken: Here are the closest free times of that day.", and leaves in **Select a time** only the closest free times of that same day (up to five) so that the customer can choose another one. Since October 27, 2026 those times also respect the other bookings of the same vehicle: a time that overlaps another active booking of that vehicle is not offered, because a vehicle cannot be in two bays at the same time.
- If the chosen time cannot be booked for another reason, the system explains which one: "The car wash is closed that day. Choose another date.", "At that time the service does not fit in the opening hours. Choose another time.", or "That time has already passed. Choose a later time."
- A vehicle cannot have two bookings at the same time ("That vehicle already has a booking at that time.").
- The service is provided at the business: the customer must bring the vehicle on the booked date and time.
- If the customer already has an active cash booking (Scheduled, Confirmed, or In progress), they cannot make another one in cash: the system shows "You already have an active booking paid in cash. Pay it or cancel it, or choose digital payment." Cash becomes available again when that booking ends, is cancelled, or is marked No-show (it is not enough for the administrator to record the payment).
- While the booking with digital payment is waiting for the receipt, the History and the detail show the badge "Pay before HH:mm". Once the receipt is sent, the booking no longer expires, even if the administrator has not reviewed it yet.
- The payment method does not change if the administrator reschedules the booking. Bookings made before October 26, 2026 have no payment method and no deadline.

**Figure 14**

*Book a Wash, Steps 1 and 2: Vehicle and Service*

![Book a Wash, Steps 1 and 2: Vehicle and Service](capturas/user-14-book-a-wash-steps-1-and-2-vehicle-and-service.jpg)

**Figure 15**

*Book a Wash, Step 3 and Summary*

![Book a Wash, Step 3 and Summary](capturas/user-15-book-a-wash-step-3-and-summary.jpg)

**Figure 16**

*Booking Confirmation*

![Booking Confirmation](capturas/user-16-booking-confirmation.jpg)

#### 10.1.4 View or Cancel a Booking

**Module name:** Booking follow-up and cancellation.

**Description:** Allows checking the status of a booking and canceling it before the service starts.

**Role that can use it:** Customer.

**Route or menu where it is found:** Dashboard > **Your next service** > **View detail**; side menu > **History**; or side menu > **Pay service**.

**Steps to use it:**

1. Open the **Dashboard** or **History** and locate the booking.
2. On the Dashboard, click **View detail** to check the service, the vehicle, the date, the time, the assigned operator, and the status. In **History**, click the booking card to see its detail (section 10.1.6).
3. To cancel, click **Cancel booking** on the booking card in **History** (or on the **Pay service** screen).
4. Read the message "Cancel booking?" and click **Yes, cancel**; click **Back** to keep the booking.

**Expected result:** The booking changes to Cancelled, the message "Booking cancelled" appears, and the slot becomes free for other customers.

**Notes or restrictions:** A booking can only be canceled before its start time; afterwards the system shows "This booking can no longer be changed." The cancellation cannot be undone. The web application does not offer the option to change the date or the time of a booking: to move it, the customer cancels it and books again, or contacts the business so that the administrator reschedules it.

**Table 7**

*Booking Statuses*

| Status | Meaning | Can the customer cancel it? |
|---|---|---|
| Scheduled | The booking was registered and is pending confirmation. | Yes, before the start time. |
| Confirmed | The booking has a slot and a washing bay. Bookings made by the customer are created with this status. | Yes, before the start time. |
| In progress | The operator started the wash. | No. |
| Completed | The service is finished; from this status it can be rated. | No. |
| Cancelled | The booking was voided by the customer or by the business, or the system closed it because one hour passed after its end without being confirmed or, since October 26, 2026, because the digital payment receipt was not uploaded in time ("The payment receipt was not uploaded in time"). | Not applicable. |
| No show | The customer did not arrive at the booked slot and the business recorded it. If the confirmed booking never had an operator, the system marks it this way one hour after its end time. | Not applicable. |

*Note.* The names are those displayed by the interface in English (codes SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, and NO_SHOW in the database).

**Figure 17**

*Booking Cancellation Confirmation*

![Booking Cancellation Confirmation](capturas/user-17-booking-cancellation-confirmation.jpg)

#### 10.1.5 Pay for a Service

Payment is made by transfer to the business's account (scanning its QR code or using its account data) or in cash at the business. The system **does not request or store credit or debit card data** and does not connect to banks: an administrator verifies each digital payment manually (Table 8).

Since October 26, 2026 the payment method is chosen when booking (section 10.1.3), and the **Pay service** screen follows it: on a booking with **digital payment**, cash is not offered and the deadline is shown ("Upload the receipt before HH:mm or the booking will be released."); on a **cash** booking, the Cash method appears preselected with the notice "You pay in cash at the car wash: The administrator records the payment when you arrive; you don't need to upload a receipt.", and in the History that booking shows "You pay in cash at the car wash" instead of **Pay now**.

**Table 8**

*Available Payment Methods*

| Method | How it is paid | Is a receipt required? |
|---|---|---|
| Nequi | The QR code is scanned and the transfer is made from the app. | Yes |
| Daviplata | The QR code is scanned and the transfer is made from the app. | Yes |
| Transfer | The QR code is scanned or the account data shown is used. | Yes |
| Cash | Paid at the business when the service is received. No QR code is shown. It only appears if the business keeps the cash account active. | No; the administrator records the payment at the business. |

*Note.* The methods shown, Cash included, depend on the payment accounts that the administrator keeps active.

**Module name:** Pay service.

**Description:** Allows paying a booking by attaching the receipt of the digital payment, or announcing that it will be paid in cash.

**Role that can use it:** Customer.

**Route or menu where it is found:** Side menu > **Pay service** (route */client/payment*); also **Go to payment** after booking, or **Pay now** in History.

**Steps to use it:**

1. Open **Pay service** and review the **Booking summary**: service, vehicle, **Subtotal**, **Discount applied**, and **TOTAL TO PAY** (VAT of 19% included).
2. If you have an unlocked coupon, type it in **Coupon code** and click **Apply**. The message "Coupon applied: [name]" appears, and the discount is subtracted from the total to pay. To remove it before reporting the payment, click the **X** next to the coupon: the system cancels the redemption and the total goes back to the booking's. Since October 24, 2026 the **TOTAL TO PAY** is the one the server calculates: if the page is reloaded or opened again after applying the coupon, the screen still shows the coupon, the discount, and the **X**. If the system cannot get the total, it shows "—" and the notice "We couldn't get the amount to pay. Check your connection and come back in a few seconds."; in that case, do not pay until the amount appears.
3. In **Select your payment method**, choose Nequi, Daviplata, Transfer, or Cash.
4. For a digital method: scan the QR code with the banking or wallet app, or copy the number shown in **Authorized key / account** with **Copy number**, and transfer exactly the **Amount to pay**. If the code is hard to read, **Enlarge QR** shows it bigger, with **Zoom in** and **Zoom out** (also with the mouse wheel or a double click).
5. In **Confirm your payment**, drag the image of the receipt or click to browse for it (JPG or PNG of up to 10 MB). The page reduces it below 2 MB before sending it; meanwhile it shows "Preparing the receipt...".
6. Type the **Transaction reference** printed on the receipt (8 to 12 letters or numbers issued by the bank) and check the **Amount paid** (greater than 0), which must match the amount transferred.
7. Click **I've made the payment**. While data is missing, the button stays disabled and the list "To confirm the payment you still need to:" shows what is pending (receipt, reference, or amount).
8. For cash: on a booking made with cash, the **Cash** method is already selected; the payment is made at the business and recorded by the administrator, without a receipt.

**Expected result:** The system shows "Payment sent!" and the payment remains in review ("Payment under review") until an administrator approves or rejects it. The customer receives a notification and an email with the result.

**Notes or restrictions:**

- The amount due is always calculated by the system (booking total minus the coupon applied, if any); the customer cannot change it. The amount paid that the customer declares is compared by the administrator with the receipt and the bank.
- Only bookings in Confirmed, In progress, or Completed status can be paid.
- While a payment is in review, another one cannot be reported for the same booking ("There is already a payment under review for this booking."); a booking admits a single approved payment. A reference already used in another payment is rejected ("That reference was already used in another payment. Check your receipt number.").
- If the booking changes after the payment is reported (for example, the administrator changes its services), the reported payment can no longer be approved with the old amount: the administrator rejects it and the customer reports it again with the new amount shown on screen.
- If the administrator rejects the payment, the customer can report it again with a correct receipt.
- A booking with digital payment has a deadline to upload the receipt: 30 minutes from when it was made, or the service time if that comes first. If it passes, the booking is cancelled automatically and can no longer be paid ("This booking can no longer be paid (it is cancelled, paid or under review)."): the customer has to book again.
- If the image cannot be reduced below 2 MB, the message "We could not reduce the image below 2 MB. Try a screenshot of the receipt." appears.
- When the payment is approved, the customer receives the notice "Payment confirmed" (inbox and email), the loyalty points of the booking ("You earned N points", inbox only), and, for each coupon those points unlock, the notice "You have a redeemable coupon!" with its code (inbox and email). If the payment is rejected or refunded, the customer receives a notice and an email; a refund reverses the points earned by that booking.
- Once the payment is approved, the **Pay service** screen of that booking only shows the summary: the payment method, the QR code, the receipt upload, and the **I've made the payment** button disappear, and the coupon used (if any) can no longer be removed.
- A coupon is accepted only if it is active, is within its dates, and the customer has reached the points it requires; only one coupon is accepted per booking (since October 23, 2026; before, different coupons could be combined; if there is already one, the system answers "This booking already has a coupon. Remove it if you want to use another one."). A percentage coupon is recalculated if the booking total changes; a fixed discount is shown in pesos. The system answers "That coupon doesn't exist.", "That coupon isn't active, or you haven't reached the points it needs yet.", or "You already redeemed this coupon on this booking." A successful redemption sends a confirmation notice and email.

**Table 9**

*Payment Statuses*

| Status | Meaning | What the customer must do |
|---|---|---|
| Pending | The booking does not yet have an approved payment. | Make the payment. |
| In review | The receipt was submitted and awaits the administrator's verification. | Wait for the verification. |
| Approved | The administrator verified the payment. | No action. |
| Rejected | The payment could not be verified; the administrator records the reason. | Report the payment again with a correct receipt. |
| Refunded | The amount of the payment was returned to the customer. | No action. |

*Note.* Codes PENDING, IN_REVIEW, APPROVED, REJECTED, and REFUNDED in the database. On the History cards they are shown as Pending verification, Under review, Paid, Rejected, and Refunded.

**Figure 18**

*Payment Method Selection and Booking Summary*

![Payment Method Selection and Booking Summary](capturas/user-18-payment-method-selection-and-booking-summary.jpg)

**Figure 19**

*QR Code and Receipt Upload*

![QR Code and Receipt Upload](capturas/user-19-qr-code-and-receipt-upload.jpg)

*Note.* Use a test QR code; do not show real bank accounts.

#### 10.1.6 History and Rating

**Module name:** History.

**Description:** Lists the customer's bookings with their status and allows paying, canceling, and rating them.

**Role that can use it:** Customer.

**Route or menu where it is found:** Side menu > **History** (route */client/history*).

**Steps to use it:**

1. Click **History**. Bookings appear from the newest to the oldest.
2. Use the **All**, **Active**, **Completed**, and **Cancelled** filters, and the **From** and **To** dates, to narrow the list.
3. Use **Search by service, vehicle or code** to find a booking.
4. On each card, review the **Service type**, the **Vehicle**, the **Status**, and the **Payment** status, and use **Pay now** or **Cancel booking** when they are available. **Pay now** appears only when the booking can be paid (payable status, without an approved payment and without one pending or in review).
5. Click the card to open the **Booking details** (services, date and time, vehicle, total, payment status, and rating). The **Go to payment** button opens the payment screen of that booking; it does not appear on cancelled or no-show bookings.
6. On a completed service, click **Rate Service**.
7. Choose from one to five stars, write a comment if desired (maximum 500 characters), and click **Submit rating**.

**Expected result:** The message "Thanks for your rating!" appears, the card shows **Rated**, and the operator who performed the service can see the rating.

**Notes or restrictions:** Rating is optional, is allowed only once per booking, and only when the booking is Completed; it cannot be modified afterwards. If there are no bookings for the filters chosen, the system shows "No services found".

**Figure 20**

*History With Filters*

![History With Filters](capturas/user-20-history-with-filters.jpg)

**Figure 21**

*Rating of a Completed Service*

![Rating of a Completed Service](capturas/user-21-rating-of-a-completed-service.jpg)

#### 10.1.7 Notifications

**Module name:** Notifications.

**Description:** Gathers the notices of the system (booking created, confirmed, or cancelled, reminders, operator assigned, service started and completed, payment confirmed, rejected, or refunded, points earned, coupon unlocked or redeemed, and vehicle inspection report) and indicates which ones have been read.

**Role that can use it:** Customer and Operator.

**Route or menu where it is found:** Side menu > **Notifications**.

**Steps to use it:**

1. Click **Notifications**. The header shows how many notifications are unread.
2. Use the **All**, **Reminders**, **Promotions**, **Confirmations**, and **Others** tabs to filter by type.
3. Use the **Read status** filter and the dates to narrow the list.
4. Click a notification (or **View detail**) to read it completely; it is marked as read. If the notice belongs to a section, the detail has a button to go there: **Go to history** (bookings, payments, service, and inspection report) or **Go to home** (welcome, points, and coupons); for the operator, **Go to assigned services**.
5. Use **Mark as read** or **Mark as unread** on each notification, or **Mark all as read**.
6. To delete a notification, use the delete option and confirm "Delete this notification?".

**Expected result:** The person stays informed without checking each module constantly.

**Notes or restrictions:** The system sends reminders 24 hours and 1 hour before each booking. Notices that change something for the customer are also sent by email: welcome, booking created, confirmed, cancelled, and reminders, payment confirmed, rejected, and refunded, coupon redeemed or unlocked, and inspection report. The progress of the service (operator assigned, started, and completed) and the points earned arrive only in the inbox and as push notifications. The password recovery code always arrives by email. The switches of **Settings > Notifications** decide only the external channels; the inbox always receives every notice. A deleted notification cannot be recovered.

**Figure 22**

*Notification Center*

![Notification Center](capturas/user-22-notification-center.jpg)

#### 10.1.8 Profile

**Module name:** Profile.

**Description:** Allows viewing and updating personal data, changing the email and the password, and deactivating the account.

**Role that can use it:** Customer, Operator, and Administrator.

**Route or menu where it is found:** Side menu > **Profile**.

**Steps to use it:**

1. Click **Profile**. The "My Profile" screen opens in view mode with the first names, last names, email, phone, and the date since which the person is a member.
2. To modify the data, click **Edit profile**, change the fields, and click **Save**; the message "Profile updated!" appears.
3. To change the email, which is the login username, type the new one and confirm with the current password in the window "Confirm the email change"; from then on the new email is used to log in.
4. To change the password, go to **Password** and click **Change**. Type the **Current password**, the **New password**, and **Confirm new password**, and click **Save**.
5. To stop using the account, go to **Account**, click **Deactivate my account**, read the message, and confirm with the password.

**Expected result:** The data is updated. If the password was changed, the new one is used at the next login.

**Notes or restrictions:** If the current password is wrong, the change is rejected ("The current password is incorrect"); the new password must be different from the current one. The account is deactivated, not erased: the person can no longer log in and the sessions are closed, but the history of bookings and payments is kept. To reactivate it, the person must contact the administrator. The **Address** is saved in the account, so it appears the same on any device.

**Figure 23**

*User Profile*

![User Profile](capturas/user-23-user-profile.jpg)

#### 10.1.9 Settings

**Module name:** Settings.

**Description:** Allows customizing the appearance, the language, and the notification preferences, and opening the help center and the legal documents.

**Role that can use it:** Customer, Operator, and Administrator.

**Route or menu where it is found:** Side menu > **Settings** (route */client/configuration* for the customer).

**Steps to use it:**

1. Click **Settings**.
2. Under **Appearance**, click **Choose theme** and select one of the four color themes. The change is applied immediately.
3. Under **Languages**, select Español, English, Français, or Português. All the texts of the interface change to the chosen language.
4. Under **Notifications**, turn **Push Notifications**, **Email Reminders** (booking reminders by email, one day and one hour before), and **Promotions and Offers** (alerts for the coupons you unlock with your points, by email and push) on or off.
5. Under **Help**, click **Open** to see the help center: support line, email, opening hours (Monday to Saturday, 7:00 a.m. to 6:30 p.m.), and address, taken from the data of the business registered in the system.
6. Under **Legal and privacy**, open **View Terms and Conditions** or **View Data Policy**.

**Expected result:** The theme, the language, and the notification switches are applied immediately and are saved in the account, so they are kept at the next login and on any device.

**Notes or restrictions:** Turning off **Push Notifications** stops the notices to the phone; turning off **Email Reminders** stops only the reminder emails; turning off **Promotions and Offers** stops the email and push alerts of unlocked coupons. The inbox is not affected by any switch. If the change cannot be saved, the system shows "Your notification preferences could not be saved. Please try again." Dates are shown in the format of the selected language.

**Figure 24**

*Settings Screen*

![Settings Screen](capturas/user-24-settings-screen.jpg)

### 10.2 Operator Role Modules

The operator receives the account from the business administrator. The operator can only see and update the services assigned to them; the system rejects any attempt to change a service of another operator.

#### 10.2.1 Operator Dashboard

**Module name:** Operator dashboard.

**Description:** Home screen of the operator. It summarizes the pending work and the progress of the day.

**Role that can use it:** Operator.

**Route or menu where it is found:** Side menu > **Dashboard** (route */operator*).

**Steps to use it:**

1. Log in with an operator account; the system opens the dashboard automatically.
2. Read the greeting, which indicates how many pending services there are ("You have N pending services for today"), and click **View assigned services** to go to the list.
3. Review the indicators (assigned bookings, services in progress, notifications, and rating) and the progress bar **Services completed today**.
4. In **Today's services**, review the upcoming assigned services with their type, vehicle, time, customer, and status, and click **Start** when it is time to begin one.

**Expected result:** The operator knows at a glance which services must be performed.

**Notes or restrictions:** If there are no assigned services, the system shows "You have no scheduled services right now."

**Figure 25**

*Operator Dashboard*

![Operator Dashboard](capturas/user-25-operator-dashboard.jpg)

#### 10.2.2 My Schedule

**Module name:** My Schedule.

**Description:** Shows the operator's bookings in a calendar, by day or by week, and allows starting and finishing them.

**Role that can use it:** Operator.

**Route or menu where it is found:** Side menu > **My Schedule** (route */operator/schedule*).

**Steps to use it:**

1. Click **My Schedule**.
2. Select a day in the calendar, or switch between the **Day**, **Week**, and **Completed** tabs.
3. Review the totals: **Total**, **Pending**, **In progress**, and **Completed**.
4. Check the **Next service** block, which indicates how many minutes remain before it starts.
5. In the list of bookings, click **Start** or **Finish** according to the status of each service, or **View detail** to see the date, the duration, and the vehicle.

**Expected result:** The status of the service changes and the customer can follow its progress.

**Notes or restrictions:** A service can be started from 15 minutes before the booked time and up to 4 hours after its end time (a service that was actually done can be recorded late within that period; since October 24, 2026). After that, the **Start** button is disabled with the notice "It can no longer be started: more than 4 h have passed since the end time" and the administrator closes the service. Only one service can be in progress at a time. A service must be started before it can be finished ("You must start the service first."). When finishing, the system asks "Finish this service?" and moves it to the history.

**Figure 26**

*Operator Schedule*

![Operator Schedule](capturas/user-26-operator-schedule.jpg)

#### 10.2.3 Assigned Services

**Module name:** Assigned services.

**Description:** Lists all the services assigned to the operator and allows starting and finishing them.

**Role that can use it:** Operator.

**Route or menu where it is found:** Side menu > **Assigned services** (route */operator/assigned-services*).

**Steps to use it:**

1. Click **Assigned services**.
2. Review the cards: **Total Services**, **Pending**, **In Progress**, and **Completed Today**.
3. Use **Filter by date**, **Filter by service type**, or the search box, and click **Filter**.
4. In the table, read the **ID**, **Service type**, **Date & time**, **Vehicle**, **Client**, and **Status** of each service.
5. Click a service to open **Service information**: client, vehicle, service type, scheduled time, and payment method. Since October 24, 2026, if the administrator wrote instructions when assigning or reassigning the service, they appear in **Notes from the administrator** (on the web and in the app).
6. Click **Start Service** when beginning the wash and confirm with **Start**.
7. When the wash is finished, click **Finish Service** and confirm with **Finish**.
8. If it is necessary to communicate with the customer, use **Contact client** in **Additional options**.

**Expected result:** The status changes to In progress and then to Completed ("Service finished!"). The customer receives the notices and can rate the service.

**Notes or restrictions:** The service can be started from 15 minutes before the booked time. The operator can only update the services assigned to them. Every service must be recorded with **Start** and **Finish**, even if late: if the time of the service has already passed, it can still be started and finished up to 4 hours after its end time, and the real hours count; afterwards the system answers "This service can no longer be started: more than 4 hours have passed since its end time. Ask the administrator to close it." If an assigned service was not started when its time ended, or was started and not finished one hour after its end time, the operator receives the notice "¿Atendiste este servicio?" (did you serve this booking?) and the administrator sees the booking in the list of bookings to close; if it can no longer be recorded, the operator tells the administrator whether the vehicle was served, and the administrator closes it. The schedule and the assigned services list do not show cancelled bookings or those left as No show.

**Table 10**

*Service Statuses Shown to the Operator*

| Status | Meaning | Available action |
|---|---|---|
| Pending | The service was assigned and has not started yet. | Start Service (from 15 minutes before the booked time and up to 4 hours after its end time). |
| In progress | The operator started the wash. | Finish Service. |
| Completed | The wash is finished and the customer can rate it. | View only. |

*Note.* The database also defines the execution statuses PAUSED and WITH_ISSUE, which the current interface does not use.

**Figure 27**

*List of Assigned Services With Filters*

![List of Assigned Services With Filters](capturas/user-27-list-of-assigned-services-with-filters.jpg)

**Figure 28**

*Service Information and Action Buttons*

![Service Information and Action Buttons](capturas/user-28-service-information-and-action-buttons.jpg)

#### 10.2.4 Operator Notifications

The operator's notifications work like the customer's (section 10.1.7). The most frequent notices are a new service assigned, a service moved to another time, a service cancelled, and a service reassigned to another operator. Since October 21, 2026 the notice "¿Atendiste este servicio?" (did you serve this booking?) also arrives when a service's time ended and it was not started or not finished (only once per booking, section 10.2.3). These staff notices arrive in the inbox and as push notifications, never by email. The operator opens them with **Notifications** in the side menu and uses the tabs, the filters, and the **Mark as read** and **Mark all as read** buttons. Clicking one of these notices opens its detail with the **Go to assigned services** button.

**Figure 29**

*Operator Notifications*

![Operator Notifications](capturas/user-29-operator-notifications.jpg)

#### 10.2.5 Service History

**Module name:** Service History.

**Description:** Lists the services of the operator with their date, vehicle, payment, rating, and final status.

**Role that can use it:** Operator.

**Route or menu where it is found:** Side menu > **History** (route */operator/service-history*).

**Steps to use it:**

1. Click **History**.
2. Review the indicators, including the **Completion rate**.
3. Use the **All**, **Completed**, **Canceled**, and **Reassigned** tabs and the **All services** filter.
4. Use the search box to find a service by code, plate, client, or vehicle.
5. Click **Clear** to remove the filters.
6. Click a service to see its detail. If the customer rated it, the **Go to ratings** button opens **Ratings received** (section 10.2.6).

**Expected result:** The operator consults the completed work, from the newest to the oldest, and the final status of each service.

**Notes or restrictions:** A reassigned service is one that the administrator moved to another operator; it no longer counts as the operator's own service.

**Figure 30**

*Operator Service History*

![Operator Service History](capturas/user-30-operator-service-history.jpg)

#### 10.2.6 Ratings Received

**Module name:** Ratings received.

**Description:** Shows the ratings and comments that customers left about the operator's work.

**Role that can use it:** Operator.

**Route or menu where it is found:** Side menu > **Ratings** (route */operator/qualifications*).

**Steps to use it:**

1. Click **Ratings**.
2. Review the average rating and the total number of ratings received.
3. Use **Filter by date**, **Filter by service type** (Basic Wash, Premium Wash, or Complete Wash), and the number of stars, and click **Filter**.
4. Read each rating with the customer, the service, the date, and the comment.

**Expected result:** The operator knows the customers' opinion.

**Notes or restrictions:** Only the ratings that customers decided to leave appear, because rating is optional. If there are none, the system shows "No ratings found".

**Figure 31**

*Ratings Received by the Operator*

![Ratings Received by the Operator](capturas/user-31-ratings-received-by-the-operator.jpg)

#### 10.2.7 Operator Profile and Settings

The operator views and edits the personal data, changes the password, and can deactivate the account in **Profile**, following the steps of section 10.1.8. **Settings** (route */operator/settings*) has the theme, the language, the notification switches, and the help center, as explained in section 10.1.9; for the operator only **Push notifications on your phone** takes effect, because staff notices never arrive by email. If the administrator provided a temporary password, it should be changed at the first login. The full detail is in the Operator User Manual.

### 10.3 Android Mobile Application

The LavaRapidoVehicular application (version 1.1.1, APK *versionCode* 6 published on October 26, 2026, package *com.vehiclecarwash.app*) offers the three roles the same functions as the web, with the same rules and the same data: what is done in one is seen in the other. This section explains how to install it and how its use differs; the rules of each module are those of sections 10.1 and 10.2, and the administrator's are those of the Administrator Manual. It was written from the 31 screens in the app's code of October 23, 2026 (3 for sign-in, 8 for the customer, 8 for the operator, and 12 for the administrator); its screenshots are in the Customer User Manual (Figures 26 to 32) and in the Operator User Manual (Figures 15 to 18).

#### 10.3.1 Installation and Updates

**Requirements:** an Android phone with an internet connection and room to install an APK. The app is not on Google Play: it is installed from the business's website.

1. On the home page of the web, tap the app download button or scan the QR code (the file is */app/LavaRapidoVehicular.apk*).
2. Open the downloaded file and tap **Install**. The first time, Android asks to allow the browser to install applications.
3. Open **LavaRapidoVehicular**. If there was a version older than the package change of October 16, 2026, uninstall it once; the following ones install over it.

Updates arrive in two ways:

- **Mandatory update.** If the business publishes a new APK, when the app opens the screen **Update the app to continue** appears with the **Download** button. When the download finishes, open the file and install it over the current one: the account and data are kept. Until it is installed, the app cannot be used.
- **Improvements over the internet.** Screen changes arrive on their own: when the app opens, **Update ready** appears; **Restart now** shows them right away and, otherwise, they apply the next time the app is opened.

#### 10.3.2 Sign-in, Sign-up, and Sign-out

The sign-in screen asks for the email and the password, with the same rules as section 8: since October 24, 2026 it accepts an email of any domain (sign-up still requires @gmail.com), and five failed attempts lock the account for 15 minutes. If the recovery code is mistyped five times, the app offers **Request another code**. From it a customer account is created (the same data and the acceptance of the two legal documents of section 8.1) and the password is recovered with the 6-digit code sent by email (section 8.3). The session is stored encrypted on the phone and lasts until the token expires (one hour); then the app asks to sign in again. To leave, open **More** and tap **Log Out**, which asks for confirmation; after signing out, the phone stops receiving that account's notifications.

#### 10.3.3 Navigation

Each role sees only its screens. At the bottom there is a bar with three main options and **Alerts**; the **More** button opens a panel with the rest of the options and **Log Out**:

- **Customer:** bar with **Home**, **Book**, **History**, and **Alerts**; in **More**: **My vehicles**, **Profile**, **Pay service**, and **Settings**.
- **Operator:** bar with **Home**, **Schedule**, **Assigned**, and **Alerts**; in **More**: **History**, **Profile**, **Ratings**, and **Settings**.
- **Administrator:** bar with **Home**, **Bookings**, **Payments**, and **Alerts**; in **More**: **Operators**, **Profile**, **Management**, **Hours & Bays**, **Reports**, and **Settings**.

#### 10.3.4 Use by the Customer

- **Home:** greeting, cards for active bookings, vehicles, washes done, and notifications, the next service block (status, operator, and progress), the quick access buttons (book a wash, pay service, my vehicles, and notifications), and the benefits and promotions block, with the loyalty progress and the unlocked coupons (section 10.1.1).
- **Book:** steps on one screen: choose the vehicle, the type of service (with the estimated duration and the points it adds), the date and time (closed days and taken times are not offered), the operator (optional), with no preference by default, and, since October 26, 2026, **How will you pay?** (digital payment now or cash at the car wash, with the same rules as the web). With digital payment, **Book Now** goes to pay with the deadline; with cash, it shows the summary and that payment is made at the car wash (section 10.1.3). The change is in the app since *v1.1.0*; on October 26, 2026 APK *versionCode* 6 (1.1.1) and the over-the-air update were published.
- **Pay service:** choose the business's account (or cash, if the business has it active), enlarge the QR, apply a coupon (and remove it with the **X**), attach the receipt, which the app reduces to less than 2 MB, and type the transaction reference. The amount to pay is calculated by the server and, since October 24, 2026, the applied coupon is still shown after leaving and returning to the screen. After sending, the payment is under review; when the administrator approves it, the screen refreshes on its own with the push notification. The booking can also be cancelled from this screen (section 10.1.5).
- **History:** filters for all, active, completed, and cancelled bookings, search by service, vehicle, or code, and the detail of each booking with pay now, cancel booking, and rate service (section 10.1.6).
- **My vehicles**, **Alerts**, **Profile**, and **Settings** work as in sections 10.1.2, 10.1.7, 10.1.8, and 10.1.9.

#### 10.3.5 Use by the Operator

- **Home:** the number of services pending for today, the button to see the assigned services, and the indicators of assigned bookings, in progress, notifications, and rating (section 10.2.1).
- **Schedule:** **Day**, **Week**, and **Completed** views, with the next service and the **Start** and **Finish** buttons. Before time, the app says "You can start it from HH:MM (15 minutes before the booking)" and, since October 24, 2026, more than 4 hours after the end time the **Start** button is disabled with "it can no longer be started" (sections 10.2.2 and 10.2.3). The service detail shows the administrator's notes.
- **Assigned:** totals of services pending, in progress, and finished today, with filters by date, type of service, and search (section 10.2.3).
- **History**, **Ratings** (average, total received, and satisfaction level of the last 12 months, with the detail of each rating), **Alerts**, **Profile**, and **Settings** work as in sections 10.2.4 to 10.2.7. If the notice "Did you serve this booking?" arrives, follow section 10.2.4.

#### 10.3.6 Use by the Administrator

The rules are those of the Administrator Manual; the app groups its functions as follows:

- **Home** (general control panel): today's bookings, services in progress, payments to review, revenue of the day, and active bays, the revenue of the last 7 days, the status of the operators, and the cards for payments pending review (with **Review**), bookings without an operator (with the assignment), and **To close**, where the administrator confirms whether the booking was served or the customer did not show up.
- **Bookings** (booking management): **New Booking**, **Assign operator**, the detail of each booking with its vehicle inspection report, and **Export**, which produces a CSV file.
- **Payments** (payment management and verification): **Review** each receipt to approve or reject it (the queue is loaded by pages and the receipt image is requested when it is opened), **Register manual payment** when the customer paid at the counter (the cash account appears even if it is deactivated for customers), refund an approved payment, and **Export reconciliation**. Since October 24, 2026, if a payment's booking was cancelled, the payment shows a notice to **refund** it (if approved) or **reject** it (if in review); on the web the notice says "Booking cancelled: refund" or "Booking cancelled: reject".
- **Operators**: list, detail with skills, status of the day, and calendar; new operator accounts are created in **Management**.
- **Management**: **Users**, **Roles** (the 3 roles are fixed; since October 24, 2026 the permission matrix is read-only, on the web and in the app: "This table shows each role's reference permissions and is read-only."), **Services**, and **Promotions** tabs.
- **Hours & Bays**: weekly schedule, exceptions, and bays. **Reports**: services done, best-selling services, charts, and **Export report** with a preview.

#### 10.3.7 Push Notifications and Differences from the Web

- When signing in, the app asks for permission to send notifications. If it is granted, the phone receives by push the same notices as the inbox (reminders 24 hours and 1 hour before, operator assigned, service started and finished, payments, points, and coupons, among others), and tapping one opens the related screen. Android groups them in the channels **Reminders**, **Promotions and points**, **Confirmations**, **Cancellations**, **Service messages**, and **System**, which can be muted from the phone's settings.
- The data are the same as on the web: a booking made in the app appears on the web and vice versa.
- Since October 24, 2026 the app shows the exact reason of the server errors (in the four languages), the administrator's booking form only offers the allowed status changes, and the coupon is kept when returning to the payment screen.
- Known limitation as of October 24, 2026, recorded in the Corrective Actions document (ACO-39): if the session expires while the app is in the background, the phone may keep receiving that account's notifications until signing in again or logging out from **More**.

## 11. Messages and Validations

The system validates the information while the person types. When a piece of data is not correct, it highlights the field with a red border and shows a message below it. Tables 11 and 12 explain the meaning of the most frequent messages and what the person must do.

**Table 11**

*Messages of the Registration, Login, and Recovery Forms*

| System message | Meaning | User action |
|---|---|---|
| Email is required | The email field is empty. | Type the email address. |
| Please enter a valid email | On sign-in or recovery, the email does not have an email format (since October 24, 2026 any domain is accepted). | Correct the email. |
| Only @gmail.com emails are allowed | In registration, an email from another provider was typed. | Use a Gmail account. |
| ID number is required / Must have between 5 and 20 digits | The ID number is empty or has an invalid length. | Type the ID number with digits only. |
| Name is required / Must contain at least 3 characters | The first names are empty or too short. | Type the first names. |
| Last names are required / Must have at least 2 characters | The last names are empty or too short. | Type the last names. |
| Phone number is required | The phone field is empty. | Type the mobile number. |
| Must start with 3 (Colombian mobile number) | The number does not start with 3. | Type a Colombian mobile number. |
| The number must contain 10 digits | The phone has fewer than 10 digits. | Complete the 10 digits. |
| You must confirm the password / Passwords do not match | The confirmation is empty or different from the password. | Type the same password in both fields. |
| The password does not meet the security requirements | One of the four requirements is missing. | Follow the list of requirements. |
| You must accept the Terms and the Data Policy to continue | One of the two legal documents was not accepted. | Read and accept both documents. |
| You must accept the personal data policy (Law 1581) to sign up. | The server received the sign-up without both acceptances (since October 24, 2026). | Check both boxes and sign up again. |
| Too many attempts in a row. Wait a moment and try again. | More than 60 attempts were made in a minute from the same device (since October 24, 2026). | Wait a minute and try again. |
| Too many failed attempts. Wait a few minutes or request another code to try again. | The recovery code or the current password was mistyped five times (since October 24, 2026). | Request another code; if it was the current password, wait 15 minutes. |
| An account with this email already exists | The email is already registered. | Log in or recover the password. |
| An account with this ID number already exists | The ID number is already registered. | Verify the data or contact the business. |
| Incorrect email or password | The credentials do not match any account. | Check the data and try again, or recover the password. |
| Your account is disabled. Contact the administrator. | The account was deactivated. | Ask the administrator to reactivate it. |
| The code is wrong, already used or expired. Request a new one. | The recovery code is not valid. | Request a new code. |
| Password updated! | The password was changed successfully. | Log in with the new password. |

*Note.* Texts taken from the English translation file of the web application.

**Table 12**

*System Operation Messages*

| Message or situation | Meaning | User action |
|---|---|---|
| Your session expired. Please sign in again. | The one-hour session ended, or the session was closed (for example, because the administrator deactivated the account). | Log in again. |
| You do not have permission to perform this action | The action belongs to another role or to another person's data. | Use only the options of the role. |
| Your customer profile is not ready yet. Please try again in a few minutes. | The customer profile is being created after registration. | Wait a moment and try again. |
| A vehicle is already registered with this plate | The plate belongs to an active vehicle. | Verify the plate. |
| The plate format does not match the vehicle type | The plate does not follow the official format for the type chosen. | Correct the plate or the type. |
| That time was just taken | The slot was taken while booking. | Choose one of the times suggested. |
| The car wash is closed that day. Choose another date. | The chosen day is marked as closed. | Choose another date. |
| At that time the service does not fit in the opening hours. Choose another time. | The service would end after closing time or overlaps the break. | Choose another of the free times. |
| That time has already passed. Choose a later time. | The chosen time is already in the past. | Choose a later time. |
| Too many failed attempts. Your account is locked for 15 minutes... | The password was typed wrong five times within 15 minutes. | Wait 15 minutes or use Forgot your password?. |
| That vehicle already has a booking at that time. | The vehicle is already booked in that slot. | Choose another time. |
| This vehicle has active or upcoming bookings: cancel them before removing it. | An attempt was made to remove a vehicle with pending bookings (since October 24, 2026). | Cancel those bookings or wait for them to finish. |
| That date cannot be booked. | The date is in the past or beyond the booking window. | Choose a valid date. |
| This booking can no longer be changed. | The booking already started or was closed. | Contact the business. |
| Choose how you will pay: digital payment or cash at the car wash. | A booking was attempted without choosing the payment method (since October 26, 2026). | Choose an option in **How will you pay?**. |
| You already have an active booking paid in cash. Pay it or cancel it, or choose digital payment. | The customer already has an active cash booking (at most one). | Choose digital payment, or wait for that booking to end or cancel it. |
| Upload the receipt before HH:mm or the booking will be released. / Pay before HH:mm | The booking with digital payment has a deadline to upload the receipt (30 minutes or the service time). | Pay and upload the receipt before that time. |
| You pay in cash at the car wash | The booking is paid in cash on arrival; no receipt is needed. | Bring the money on the day of the service. |
| The payment receipt was not uploaded in time | Reason with which the system cancelled a booking with digital payment whose deadline passed. | Book again. |
| This booking can no longer be paid (it is cancelled, paid or under review). | Payment was attempted for a cancelled booking or one that already has a payment. | Check the status in the History. |
| Invalid file | The receipt is not a JPG or PNG image of up to 10 MB. | Attach a valid image. |
| Only a completed service can be rated. / This service was already rated. | The rating is not allowed. | Wait until the service is completed; a booking is rated only once. |
| You must start the service first. / This service was already started. | The operator tried an action out of order. | Start the service before finishing it. |
| This service can no longer be started: more than 4 hours have passed since its end time. Ask the administrator to close it. | The operator tried to start a service past the deadline (since October 24, 2026). | Tell the administrator whether the vehicle was served. |
| The data clashes with an existing record or someone changed it while you were editing. Open it again and retry. | Someone else changed the same thing at the same time. | Open the screen again and repeat the change. |
| The booking service is not responding. / The database is not responding right now. | A part of the system is stopped for a moment. | Try again in a few minutes. |
| We could not reduce the image below 2 MB. Try a screenshot of the receipt. | The photo of the receipt is too heavy even after reducing it. | Attach a screenshot of the receipt. |
| To confirm the payment you still need to: ... | Data of the payment is missing (receipt, reference, or amount). | Complete the items listed under the button. |
| That coupon doesn't exist. | The coupon code is wrong. | Check the code shown in the coupon notice. |
| That coupon isn't active, or you haven't reached the points it needs yet. | The coupon is paused, outside its dates, or locked. | Check the points in the dashboard. |
| You already redeemed this coupon on this booking. | The coupon was already used on that booking. | Pay without the coupon or use another one. |
| This booking already has a coupon. Remove it if you want to use another one. | Only one coupon is accepted per booking. | Remove the coupon with the **X** before applying another one. |
| We couldn't get the amount to pay. Check your connection and come back in a few seconds. | The system could not calculate the amount (since October 24, 2026 the total comes from the server). | Wait and open the screen again; do not pay while the total shows "—". |
| There is already a payment under review for this booking. / That reference was already used in another payment. Check your receipt number. | The payment was already reported or the reference exists in another payment. | Wait for the review or correct the reference. |
| Cannot reach the server. Please try again in a moment. | There is no connection with the system. | Check the internet connection and try again. |

*Note.* Texts taken from the English translation file of the web application.

## 12. Logging Out

Logging out when finished prevents another person from using the account, especially on shared computers or phones. Although the session expires automatically one hour after login, it is recommended to log out manually.

1. Locate the **Log Out** option at the bottom of the side menu.
2. Click **Log Out**.
3. In the "Log out" window, click **Yes, log out**.
4. Verify that the system returns to the login screen.

When signing out, the other tabs of the same browser also go back to the login screen, and if another account signs in on one tab, the others reload with that account. Since October 21, 2026 the closed session does not work on the server either, even if someone had copied its access.

**Figure 32**

*Log Out Confirmation*

![Log Out Confirmation](capturas/user-32-log-out-confirmation.jpg)

## 13. Frequently Asked Questions

##### What do I do if I forgot my password?

On the login screen, click "Forgot your password?" and follow the three steps of section 8.3. The code arrives at the registered email and is valid for 15 minutes.

##### Why won't the system let me register?

The system only accepts emails ending in @gmail.com, and each email and each ID number can have only one account. In addition, the two legal documents must be accepted.

##### Can my account be locked for typing the password wrong?

Yes. After five failed attempts within 15 minutes, the account is locked for 15 minutes. You can wait that time or use **Forgot your password?** to create a new password and log in right away.

##### How do I know if my booking was confirmed?

When the time is free, the booking is confirmed at once: the system shows "Booking created!" and the booking appears in the dashboard and in History with the status Confirmed.

##### What do I do if the time I want is already taken?

The screen only offers free times. If another person takes the time while you book, the system suggests the closest free times of the same day that your vehicle can take (not those that overlap another booking of yours); choose one and confirm.

##### Can I choose who washes my vehicle?

Yes, it is optional. When booking, **Operator (optional)** lists the operators who are free at that time. If you do not choose one, the system assigns a free operator automatically. You can see the assigned operator in the dashboard and receive a notification when the operator is assigned or changed.

##### How do I change the date of a booking?

The web application does not allow changing the date or the time. Cancel the booking before its start time and book again, or contact the business.

##### What happens if I do not arrive for my booking?

The business records the booking as No show. If the booking had no operator assigned, the system marks it this way one hour after the end time. If you had already paid, contact the business: the administrator decides on the refund.

##### How do I pay for my service?

In Pay service, choose the method. With Nequi, Daviplata, or Transfer, scan the QR code, transfer the exact amount, and attach the receipt with the transaction reference; with Cash (if the business has it active), pay at the business.

##### Does the system store my card data?

No. The system does not request or store card data.

##### Why was my booking cancelled on its own?

If you chose **Digital payment now** when booking, you had 30 minutes (or until the service time, if that came first) to upload the receipt. If you did not upload it in time, the system cancelled the booking with the reason "The payment receipt was not uploaded in time" and freed the slot. You can book again and pay within the deadline, or choose **Cash at the car wash** if you have no other active cash booking.

##### Why does my payment appear as "In review"?

Because an administrator must verify that the money arrived in the business's account. When the administrator approves or rejects it, you receive a notification and an email; you can also check the status in History.

##### Can I use a promotion code?

Yes. When your points unlock a coupon, you receive a notice with its code. In Pay service, type it in Coupon code and click Apply before confirming the payment.

##### How do I earn loyalty points?

Each service adds the points shown when booking ("Earns N points"). The points are credited when the administrator approves the payment of the booking, and you can see your progress in the dashboard. Points are not spent: they unlock coupons.

##### How do I rate a service?

When the booking is Completed, open History, click "Rate Service", choose from one to five stars, and, if desired, write a comment. Each booking can be rated only once.

##### Can I change the language or the colors?

Yes. In Settings you can choose Spanish, English, French, or Portuguese and one of four color themes.

##### Can I delete or deactivate my account?

The **Deactivate my account** option deactivates the account, without erasing it: you will not be able to log in, but your history is kept. To reactivate it, contact the administrator. Administrator accounts do not have this option.

##### I am an operator and cannot start a service. Why?

A service can be started from 15 minutes before the booked time (earlier, the system answers that it is still too early) and up to 4 hours after its end time (afterwards it answers that it can no longer be started), only if it is assigned to you, and only when you do not have another service in progress. If the service is yours and the problem continues, inform the administrator.

##### I served a vehicle and forgot to press Start. What do I do?

If no more than 4 hours have passed since the end time, open the service and press **Start** and then **Finish**; the system accepts it even if it is late. If you can no longer do it, tell the administrator that the vehicle was served, so the booking is closed as attended and not as No show.

## 14. Glossary

**Table 13**

*Glossary of System Terms*

| Term | Definition |
|---|---|
| Administrator | Role that manages bookings, operators, payments, schedules, users, and reports. |
| Assignment | Link between an operator and a booking, made by the administrator. |
| Bay (washing bay) | Physical spot of the business where a vehicle is washed. Two bookings cannot occupy the same bay at the same time. |
| Booking | Block of time reserved for washing a vehicle. |
| Coupon | Discount code that the customer unlocks by accumulating loyalty points and redeems when paying. |
| Customer | Person who registers vehicles and books slots. |
| Dashboard | Home screen that summarizes the most important information of each role. |
| Loyalty points | Points that each service adds when its payment is approved; they unlock coupons and are not spent. |
| Notification | Notice inside the system about a relevant event. |
| Operator | Employee who washes the vehicles. |
| QR code | Two-dimensional code that is scanned with the phone to pay. |
| Rating | Score from one to five stars, with an optional comment, that the customer gives to a completed service. |
| Receipt | Image that proves that a transfer was made. |
| Reminder | Notice that the system sends 24 hours and 1 hour before a booking. |
| Theme | Combination of interface colors that each person can choose. |
| Transaction reference | Number that the bank prints on the receipt to identify the transfer. |
| Washing service | Type of wash offered (Basic, Premium, or Complete), with its own duration and price. |

*Note.* Based on the glossary of the project documentation.

## References

Congreso de la República de Colombia. (2012). *Ley Estatutaria 1581 de 2012, por la cual se dictan disposiciones generales para la protección de datos personales* [Statutory Law 1581 of 2012, which issues general provisions for the protection of personal data]. http://www.secretariasenado.gov.co/senado/basedoc/ley_1581_2012.html

Servicio Nacional de Aprendizaje. (2026). *Guía para elaborar manuales de software: Manual de usuario, manual técnico, manual de instalación y manual de administrador* [Guide for writing software manuals: User manual, technical manual, installation manual, and administrator manual] [Unpublished instructional material]. Technologist in Software Analysis and Development program.

Vehicle Washing Development Team. (2026a). *Vehicle washing management system: Project documentation* (vehicle-w-docs) [Unpublished project documentation]. GitHub. https://github.com/code-sena/vehicle-w-docs

Vehicle Washing Development Team. (2026b). *Front-end-proyecto-web* (develop branch) [Computer software]. GitHub. https://github.com/BrayanCABRERA123/Front-end-proyecto-web

## 15. Annexes

### Annex A. Source of the Screenshots

The figures in this manual were taken on October 16, 2026, on the version of the *develop* branch, with the whole system running in Docker and the web at *http://localhost:4200*. Test accounts, vehicles, bookings, and payments created only for the screenshots were used, so no real emails, ID numbers, phone numbers, plates, or bank accounts appear. If a screen changes in a future version, the corresponding screenshot must be taken again with the same file name in the *capturas* folder.

### Annex B. Delivery Checklist

**Table 14**

*User Manual Checklist*

| Criterion | Met |
|---|---|
| Has a complete title page (ficha, instructor, and training center) and version | Yes / No |
| Includes a table of contents with updated pages | Yes / No |
| Explains objective and scope | Yes / No |
| The content matches the software of the final version | Yes / No |
| All figures are real screenshots of the final version | Yes / No |
| The steps are clear and ordered | Yes / No |
| Contains no sensitive information or real credentials | Yes / No |
| Spelling and APA formatting reviewed | Yes / No |
| Exported to PDF with the name Manual_usuario_LavadoVehicular_3145556.pdf | Yes / No |

*Note.* Adapted from the checklist of the guide for writing software manuals (SENA, 2026).
