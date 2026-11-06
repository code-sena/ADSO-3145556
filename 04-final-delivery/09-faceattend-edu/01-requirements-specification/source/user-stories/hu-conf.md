# HU-CONF-001: Configure system alerts

## Story

**As** an authenticated user  
**I want** to configure the alerts and notifications of the application  
**So that** I can customize how the system informs me about relevant events such as attendance records, justifications, and system alerts.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that the user is registered and has logged into the system, when the user accesses the alert configuration module, then the system displays the available alert configuration options.

- [ ] **AC2:** Given that the user modifies one or more alert configuration options, when the changes are saved, then the system stores the new configuration and reflects the changes in the system's behavior.

- [ ] **AC3:** Given that the user is not registered or has not logged into the system, when the user attempts to access the alert configuration module, then the system denies access and shows a clear authentication error message.

- [ ] **AC4:** Given that the alert configuration is saved successfully, when the system processes the changes, then the system shows a confirmation message indicating that the configuration was updated.

- [ ] **AC5:** Given that the alert configuration fails to save due to a system error, when the operation is processed, then the system shows a clear error message and does not apply the changes.

- [ ] **AC6:** Given that the user's alert configuration is updated, when a relevant event occurs in the system, then the system sends notifications according to the user's configured preferences.

- [ ] **AC7:** Given normal network conditions, when the user saves the alert configuration, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC8:** Given that the alert configuration is updated, when the operation is completed, then the system records an audit event containing the user, date, and time.

---

## Technical notes

- RF 4.1 indicates that the system must validate that the user is registered and has logged into the system before allowing alert configuration.
- This story covers the general alert configuration module. Specific sub-options such as enabling/disabling alerts and changing notification tones are handled in separate stories ([HU-CONF-002](#hu-conf-002-enable-or-disable-alerts) and [HU-CONF-003](#hu-conf-003-change-notification-tone)).
- The alert configuration should be stored per user to allow personalization.
- The system should consider the following types of alerts:
  - Attendance records (entry/exit).
  - Justification results (approved/rejected).
  - System alerts (device failures, maintenance).
  - Academic alerts (delays, absences).
- The alert configuration should be compatible with the notification delivery mechanism defined in [HU-JUS-003](../HU-JUS/user-stories.md#hu-jus-003-automatic-notification-of-justification-result).
- The system must validate that the user is authenticated before processing any configuration change.
- Configuration changes must be auditable for traceability.
- This story does not cover IoT device failure alerts. That is handled in [HU-CONF-007](#hu-conf-007-iot-device-failure-alerts-management).
- The team must define the default alert configuration for new users.

**Responsible service(s):** Configuration service / Notification service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/config/alerts` — proposed, returns the user's current alert configuration.  
- `PUT /api/v1/config/alerts` — proposed, updates the user's alert configuration.  

**Events generated:**  
- `AlertConfigurationUpdated`  
- `AlertConfigurationFailed`  

**Required permissions:**  
- Authenticated user (any role).  
- Configuration is personal and applies only to the authenticated user's notifications.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Alert configuration module is accessible to authenticated users.
- [ ] Successful configuration update tested.
- [ ] Unauthenticated access attempt tested.
- [ ] Confirmation and error messages are clear.
- [ ] Configuration is stored per user.
- [ ] Notifications are sent according to configured preferences.
- [ ] Audit event is generated.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 4.1 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 3 |
| Priority | Medium |
| Target sprint | Sprint 3 |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login |

---

# HU-CONF-002: Enable or disable alerts

## Story

**As** an authenticated user  
**I want** to enable or disable specific alerts and notifications  
**So that** I can control which notifications I receive and reduce unnecessary interruptions.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that the user is registered and has logged into the system, when the user accesses the alert enable/disable module, then the system displays the list of available alerts with their current enabled/disabled status.

- [ ] **AC2:** Given that the user selects an alert to enable, when the change is saved, then the system enables the selected alert and reflects the change in the system's behavior.

- [ ] **AC3:** Given that the user selects an alert to disable, when the change is saved, then the system disables the selected alert and limits the notifications the user receives for that alert type.

- [ ] **AC4:** Given that the user is not registered or has not logged into the system, when the user attempts to enable or disable alerts, then the system denies access and shows a clear authentication error message.

- [ ] **AC5:** Given that the alert status is changed successfully, when the system processes the change, then the system shows a confirmation message indicating that the alert was enabled or disabled.

- [ ] **AC6:** Given that an alert is disabled, when the corresponding event occurs in the system, then the system does not send notifications for that alert type to the user.

- [ ] **AC7:** Given that an alert is enabled, when the corresponding event occurs in the system, then the system sends notifications for that alert type to the user according to their configured preferences.

- [ ] **AC8:** Given normal network conditions, when the user enables or disables an alert, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC9:** Given that the alert status is changed, when the operation is completed, then the system records an audit event containing the user, alert type, new status, date, and time.

---

## Technical notes

- RF 4.1.1 indicates that the system must validate that the user is registered and has logged into the system before allowing alert enable/disable operations.
- This story specifically covers the enable/disable toggle for individual alerts, complementing the general configuration module in [HU-CONF-001](#hu-conf-001-configure-system-alerts).
- The alert types that can be enabled or disabled should align with the alert categories defined in [HU-CONF-001](#hu-conf-001-configure-system-alerts):
  - Attendance records (entry/exit).
  - Justification results (approved/rejected).
  - System alerts (device failures, maintenance).
  - Academic alerts (delays, absences).
- The system should consider a master toggle to enable or disable all alerts at once, as well as individual toggles for each alert type.
- When an alert is disabled, the system must not send notifications for that alert type, but should still log the event for audit purposes.
- The alert status should be stored per user to allow personalization.
- The team must define whether critical alerts (such as security incidents) can be disabled by users or should always remain enabled.
- This story does not cover changing notification tones. That is handled in [HU-CONF-003](#hu-conf-003-change-notification-tone).

**Responsible service(s):** Configuration service / Notification service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/config/alerts/status` — proposed, returns the enabled/disabled status of each alert.  
- `PATCH /api/v1/config/alerts/{alertType}/status` — proposed, enables or disables a specific alert.  

**Events generated:**  
- `AlertEnabled`  
- `AlertDisabled`  
- `AlertStatusChangeFailed`  

**Required permissions:**  
- Authenticated user (any role).  
- Configuration is personal and applies only to the authenticated user's notifications.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Alert enable/disable module is accessible to authenticated users.
- [ ] Successful alert enable tested.
- [ ] Successful alert disable tested.
- [ ] Unauthenticated access attempt tested.
- [ ] Notifications are not sent for disabled alerts.
- [ ] Notifications are sent for enabled alerts.
- [ ] Master toggle (if implemented) tested.
- [ ] Audit event is generated.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 4.1.1 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 2 |
| Priority | Low |
| Target sprint | Sprint 3 |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-CONF-001](#hu-conf-001-configure-system-alerts): Configure system alerts |

---

# HU-CONF-003: Change notification tone

## Story

**As** an authenticated user  
**I want** to change the tone or sound of the application's notifications  
**So that** I can customize the audio feedback I receive when the system alerts me about relevant events.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that the user is registered and has logged into the system, when the user accesses the notification tone configuration module, then the system displays the list of available notification tones.

- [ ] **AC2:** Given that the user selects a notification tone from the available options, when the change is saved, then the system stores the selected tone and applies it to the user's notifications.

- [ ] **AC3:** Given that the user selects a notification tone, when the user wants to preview the sound, then the system allows playing a preview of the selected tone before saving.

- [ ] **AC4:** Given that the user is not registered or has not logged into the system, when the user attempts to change the notification tone, then the system denies access and shows a clear authentication error message.

- [ ] **AC5:** Given that the notification tone is changed successfully, when the system processes the change, then the system shows a confirmation message indicating that the tone was updated.

- [ ] **AC6:** Given that a notification is triggered after the tone change, when the notification is delivered, then the system plays the newly selected tone.

- [ ] **AC7:** Given that the user does not select a tone or cancels the operation, when the module is closed, then the system retains the previous tone without applying changes.

- [ ] **AC8:** Given normal network conditions, when the user changes the notification tone, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC9:** Given that the notification tone is changed, when the operation is completed, then the system records an audit event containing the user, selected tone, date, and time.

---

## Technical notes

- RF 4.1.2 indicates that the system must validate that the user is registered and has logged into the system before allowing notification tone changes.
- This story specifically covers the audio customization of notifications, complementing the general configuration module in [HU-CONF-001](#hu-conf-001-configure-system-alerts).
- The system should provide a predefined set of notification tones for the user to choose from. The team must define the available tones.
- The system should consider:
  - A default tone for new users.
  - A "silent" or "vibration only" option, if applicable.
  - Preview functionality before saving.
- The notification tone setting should be stored per user to allow personalization.
- On mobile devices, the application must request appropriate permissions to play sounds.
- On web browsers, the system should handle browser autoplay policies that may restrict audio playback.
- The tone change should apply to all notification types unless the team decides to allow per-alert-type tone configuration.
- This story does not cover enabling/disabling alerts. That is handled in [HU-CONF-002](#hu-conf-002-enable-or-disable-alerts).

**Responsible service(s):** Configuration service / Notification service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/config/notification-tone` — proposed, returns the user's current notification tone.  
- `PUT /api/v1/config/notification-tone` — proposed, updates the user's notification tone.  

**Events generated:**  
- `NotificationToneUpdated`  
- `NotificationToneChangeFailed`  

**Required permissions:**  
- Authenticated user (any role).  
- Configuration is personal and applies only to the authenticated user's notifications.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Notification tone module is accessible to authenticated users.
- [ ] Available tones are displayed correctly.
- [ ] Successful tone change tested.
- [ ] Preview functionality tested.
- [ ] Unauthenticated access attempt tested.
- [ ] New tone is played on subsequent notifications.
- [ ] Cancel operation retains previous tone.
- [ ] Mobile permissions handling tested.
- [ ] Web browser autoplay policy handling tested.
- [ ] Audit event is generated.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 4.1.2 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 2 |
| Priority | Low |
| Target sprint | Sprint 3 |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-CONF-001](#hu-conf-001-configure-system-alerts): Configure system alerts |

---

# HU-CONF-004: Configure application language

## Story

**As** an authenticated user  
**I want** to configure the language of the application  
**So that** I can interact with the system in my preferred language and improve my user experience.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that the user is registered and has logged into the system, when the user accesses the language configuration module, then the system displays the list of available languages.

- [ ] **AC2:** Given that the user selects a language from the available options, when the change is saved, then the system stores the selected language and applies it to the user's interface.

- [ ] **AC3:** Given that the language is changed successfully, when the system processes the change, then the system shows a confirmation message in the newly selected language.

- [ ] **AC4:** Given that the user is not registered or has not logged into the system, when the user attempts to change the language, then the system denies access and shows a clear authentication error message.

- [ ] **AC5:** Given that the user selects a language, when subsequent screens and messages are displayed, then all interface texts, labels, and messages are shown in the selected language.

- [ ] **AC6:** Given that the user does not select a language or cancels the operation, when the module is closed, then the system retains the previous language without applying changes.

- [ ] **AC7:** Given that a new user registers in the system, when no language preference has been set, then the system uses the default language defined by the team.

- [ ] **AC8:** Given normal network conditions, when the user changes the language, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC9:** Given that the language is changed, when the operation is completed, then the system records an audit event containing the user, selected language, date, and time.

---

## Technical notes

- RF 4.2 indicates that the system must validate that the user is registered and has logged into the system before allowing language configuration.
- This story supports the non-functional requirement RNF 2 (Disponibilidad), which states that the interface should support different languages.
- The team must define the available languages. Proposed initial languages:
  - Spanish (default).
  - English.
- The system should consider implementing internationalization (i18n) best practices:
  - Externalize all user-facing strings into translation files.
  - Use locale-based formatting for dates, times, and numbers.
  - Support right-to-left (RTL) languages if applicable in the future.
- The language preference should be stored per user to allow personalization.
- The system should apply the language change immediately without requiring a logout/login cycle, if feasible.
- Error messages, validation messages, and notification texts should also be translated.
- The system should consider a fallback language (default) in case a translation is missing.
- This story does not cover color palette configuration. That is handled in [HU-CONF-005](#hu-conf-005-update-color-palettes).
- The team must define whether the language configuration applies only to the web interface or also to mobile notifications and emails.

**Responsible service(s):** Configuration service / Internationalization service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/config/language` — proposed, returns the user's current language.  
- `PUT /api/v1/config/language` — proposed, updates the user's language.  
- `GET /api/v1/config/available-languages` — proposed, returns the list of available languages.  

**Events generated:**  
- `LanguageUpdated`  
- `LanguageChangeFailed`  

**Required permissions:**  
- Authenticated user (any role).  
- Configuration is personal and applies only to the authenticated user's interface.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Language configuration module is accessible to authenticated users.
- [ ] Available languages are displayed correctly.
- [ ] Successful language change tested.
- [ ] Interface texts are displayed in the selected language.
- [ ] Confirmation message is shown in the new language.
- [ ] Unauthenticated access attempt tested.
- [ ] Cancel operation retains previous language.
- [ ] Default language is applied for new users.
- [ ] Error and validation messages are translated.
- [ ] Date and time formatting adapts to the selected locale.
- [ ] Audit event is generated.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 4.2 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 3 |
| Priority | Medium |
| Target sprint | Sprint 3 |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login |

---

# HU-CONF-005: Update color palettes

## Story

**As** an administrator  
**I want** to configure and add color palettes for the application interface  
**So that** the institution's visual identity is reflected in the system and the user experience is customized according to institutional criteria.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that an authenticated administrator accesses the color palette configuration module, when the system loads the module, then it displays the current color palettes and the option to add or modify palettes.

- [ ] **AC2:** Given that the administrator selects or creates a new color palette, when the changes are saved, then the system validates the color values, stores the new palette, and reflects the changes in the application's visual behavior.

- [ ] **AC3:** Given that the administrator enters invalid color values (malformed codes, out-of-range values), when the save operation is submitted, then the system rejects the operation and shows specific validation messages.

- [ ] **AC4:** Given that the user attempting to configure color palettes does not have administrator permissions, when the request is processed, then the system denies access and records an unauthorized access attempt.

- [ ] **AC5:** Given that the color palette is updated successfully, when the system applies the changes, then subsequent screens and interface components reflect the new color scheme.

- [ ] **AC6:** Given that the administrator cancels the operation without saving, when the module is closed, then the system retains the previous color palette without applying changes.

- [ ] **AC7:** Given normal network conditions, when the administrator saves the color palette configuration, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC8:** Given that the color palette is updated, when the operation is completed, then the system records an audit event containing the administrator, date, and time.

---

## Technical notes

- RF 4.3 indicates that the system must validate that the user is registered and has logged into the system before allowing color palette configuration.
- RF 4.3 specifies that this is an **administrator-only** operation. Regular users, instructors, and students must not be able to modify color palettes.
- The color palette configuration should support standard color formats such as hexadecimal codes (e.g., `#RRGGBB`) or RGB values.
- The system should consider:
  - A preview functionality before saving changes.
  - Contrast validation to ensure accessibility compliance (WCAG guidelines).
  - A default palette that is applied if no custom palette is configured.
- The color palette changes should apply globally across the application or per institutional scope, depending on the team's decision.
- The team must define whether multiple palettes can coexist and how they are applied (by institution, by role, or globally).
- This story does not cover language configuration. That is handled in [HU-CONF-004](#hu-conf-004-configure-application-language).
- This story does not cover alert configuration. That is handled in [HU-CONF-001](#hu-conf-001-configure-system-alerts).

**Responsible service(s):** Configuration service / UI theme service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/config/color-palettes` — proposed, returns current color palettes.  
- `POST /api/v1/config/color-palettes` — proposed, adds a new color palette.  
- `PUT /api/v1/config/color-palettes/{paletteId}` — proposed, updates an existing palette.  

**Events generated:**  
- `ColorPaletteUpdated`  
- `ColorPaletteChangeFailed`  
- `UnauthorizedPaletteAccessAttempt`  

**Required permissions:**  
- Administrator only.  
- Regular users, instructors, and students must not access this configuration.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Color palette module is accessible only to administrators.
- [ ] Successful palette creation tested.
- [ ] Successful palette update tested.
- [ ] Invalid color values scenario tested.
- [ ] Unauthorized access attempt tested.
- [ ] Preview functionality tested (if implemented).
- [ ] Interface reflects new color scheme after saving.
- [ ] Cancel operation retains previous palette.
- [ ] Audit event is generated.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 4.3 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 3 |
| Priority | Medium |
| Target sprint | Sprint 3 |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-IAM-003](../HU-IAM/user-stories.md#hu-iam-003-role-based-permission-assignment): Role-based permission assignment |

---

# HU-CONF-006: Update user facial parameters

## Story

**As** an authenticated user or administrator  
**I want** to update my facial parameters in the system  
**So that** my facial recognition data remains accurate and up-to-date for reliable attendance registration.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that the user is registered and has logged into the system, when the user requests a facial parameter update, then the system activates the camera and initiates the facial capture process.

- [ ] **AC2:** Given that the camera is active and the user positions their face correctly, when the system captures the new facial image, then the system processes the capture and validates the new facial parameters.

- [ ] **AC3:** Given that the new facial parameters are valid, when the update is saved, then the system replaces the previous facial parameters with the new ones and reflects the changes in the system's behavior.

- [ ] **AC4:** Given that the facial capture fails due to lighting, movement, or camera issues, when the update attempt is processed, then the system shows a clear error message and allows the user to retry.

- [ ] **AC5:** Given that the user is not registered or has not logged into the system, when the user attempts to update facial parameters, then the system denies access and shows a clear authentication error message.

- [ ] **AC6:** Given that the facial parameter update is successful, when the system processes the change, then the system shows a confirmation message and records an audit event containing the user, date, and time.

- [ ] **AC7:** Given that the facial parameters are updated, when the user performs subsequent attendance registration, then the system uses the new facial parameters for recognition.

- [ ] **AC8:** Given that an administrator requests a facial parameter update on behalf of another user, when the administrator has appropriate permissions, then the system allows the update and records the administrator's identity in the audit event.

- [ ] **AC9:** Given normal device, camera, and network conditions, when the user completes the facial parameter update, then the system responds in less than 5 seconds, according to RNF 1.

---

## Technical notes

- RF 4.4 indicates that the system must validate that the user is registered and has logged into the system before allowing facial parameter updates.
- RF 4.4 specifies that this operation can be requested by the user themselves or by an administrator.
- This story complements [HU-ATT-002](../HU-ATT/user-stories.md#hu-att-002-initial-facial-parameter-enrollment) (initial facial parameter enrollment). The enrollment story covers the first-time registration, while this story covers updates to existing parameters.
- The facial parameter update should follow the same security and data-protection requirements as the initial enrollment:
  - Biometric data must be stored securely.
  - Facial templates or embeddings should be preferred over raw images.
  - Compliance with Colombian Law 1581 of 2012 (Habeas Data).
- The system should consider:
  - Requiring explicit user confirmation before replacing existing parameters.
  - Maintaining a backup of the previous parameters for a defined retention period.
  - Invalidating the previous parameters immediately after the update.
- The facial parameter update may require re-authentication to confirm the user's identity before proceeding.
- The system must not expose facial parameters or biometric data in logs or interface messages.
- This story does not cover initial facial enrollment. That is handled in [HU-ATT-002](../HU-ATT/user-stories.md#hu-att-002-initial-facial-parameter-enrollment).
- This story does not cover user activation/deactivation. That is handled in [HU-IAM-007](../Index_HU.md#planned-stories-pending-documentation) or [HU-CONF-007](#hu-conf-007-iot-device-failure-alerts-management) depending on the SRS interpretation.

**Responsible service(s):** Configuration service / Biometric service, proposed.  
**Endpoint(s) implemented:**  
- `POST /api/v1/users/me/facial-parameters/update` — proposed, for self-service updates.  
- `POST /api/v1/users/{userId}/facial-parameters/update` — proposed, for administrator-initiated updates.  

**Events generated:**  
- `FacialParameterUpdated`  
- `FacialParameterUpdateFailed`  
- `UnauthorizedFacialParameterUpdateAttempt`  

**Required permissions:**  
- Authenticated user for self-service updates.  
- Administrator or Supervisor for updates on behalf of other users.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Successful self-service facial parameter update tested.
- [ ] Successful administrator-initiated update tested.
- [ ] Failed facial capture scenario tested.
- [ ] Unauthenticated access attempt tested.
- [ ] New parameters are used for subsequent attendance registration.
- [ ] Previous parameters are invalidated after update.
- [ ] User confirmation is required before replacing parameters.
- [ ] Biometric data is not exposed in logs or UI.
- [ ] Audit event is generated.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 4.4 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 5 |
| Priority | Medium |
| Target sprint | Sprint 2 |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-ATT-002](../HU-ATT/user-stories.md#hu-att-002-initial-facial-parameter-enrollment): Initial facial parameter enrollment |

---

# HU-CONF-007: IoT device failure alerts management

## Story

**As** an administrator or supervisor  
**I want** to receive alerts when IoT devices used for attendance registration experience failures  
**So that** I can take timely corrective actions and maintain the availability of the attendance registration system.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that an IoT device registered in the system detects a failure (camera malfunction, connectivity loss, hardware error), when the failure is detected, then the system generates an alert and notifies the configured administrators or supervisors.

- [ ] **AC2:** Given that an IoT device failure alert is generated, when the administrator accesses the alert management module, then the system displays the alert with device information, failure type, date, and time.

- [ ] **AC3:** Given that the administrator acknowledges the alert, when the acknowledgment is processed, then the system marks the alert as acknowledged and records the administrator's identity and timestamp.

- [ ] **AC4:** Given that the administrator resolves the device failure, when the resolution is recorded, then the system marks the alert as resolved and updates the device status accordingly.

- [ ] **AC5:** Given that an IoT device reconnects or recovers automatically, when the system detects the recovery, then the system updates the device status and closes any open alerts associated with that device.

- [ ] **AC6:** Given that the user attempting to access the alert management module does not have administrator or supervisor permissions, when the request is processed, then the system denies access and records an unauthorized access attempt.

- [ ] **AC7:** Given normal network conditions, when the administrator accesses or manages IoT device alerts, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC8:** Given that an IoT device failure alert is generated, when the operation is completed, then the system records an audit event containing the device, failure type, date, and time.

---

## Technical notes

- ⚠️ **SRS Inconsistency Note:** The SRS index lists RF 4.5 as "Gestión de alertas de fallos de dispositivos IoT" [IoT device failure alert management], but the detailed RF 4.5 section describes "Desactivar y reactivar usuarios" [Deactivate and reactivate users]. This story is documented based on the **index description** (IoT device failure alerts). The user activation/deactivation functionality is covered in [HU-IAM-007](../Index_HU.md#planned-stories-pending-documentation) (RF 1.6). This inconsistency should be validated with the instructor.
- The IoT device failure alerts management should consider:
  - Device types: cameras, sensors, or other hardware used for attendance registration.
  - Failure types: connectivity loss, hardware malfunction, camera failure, low battery (if applicable).
  - Alert severity levels: informational, warning, critical.
- The alert notification mechanism should integrate with the notification service defined in [HU-CONF-001](#hu-conf-001-configure-system-alerts) and [HU-JUS-003](../HU-JUS/user-stories.md#hu-jus-003-automatic-notification-of-justification-result).
- The system should consider:
  - Automatic health checks for registered IoT devices.
  - Configurable alert thresholds (e.g., alert after N consecutive failures).
  - Alert escalation for unresolved critical failures.
- The device status should be updated in real-time or near-real-time.
- The system should maintain a device registry with information such as device ID, location/environment, status, and last activity timestamp.
- This story does not cover user activation/deactivation. That is handled in [HU-IAM-007](../Index_HU.md#planned-stories-pending-documentation).
- The team must define the specific IoT device types and failure detection mechanisms.

**Responsible service(s):** Configuration service / IoT monitoring service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/iot-devices/alerts` — proposed, returns IoT device failure alerts.  
- `PATCH /api/v1/iot-devices/alerts/{alertId}/acknowledge` — proposed, acknowledges an alert.  
- `PATCH /api/v1/iot-devices/alerts/{alertId}/resolve` — proposed, resolves an alert.  
- `GET /api/v1/iot-devices` — proposed, returns device registry with status.  

**Events generated:**  
- `IoTDeviceFailureDetected`  
- `IoTDeviceAlertAcknowledged`  
- `IoTDeviceAlertResolved`  
- `IoTDeviceRecovered`  
- `UnauthorizedIoTAlertAccessAttempt`  

**Required permissions:**  
- Administrator or Supervisor for alert management.  
- Regular users, instructors, and students must not access IoT device alerts.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] IoT device failure detection tested.
- [ ] Alert generation tested for different failure types.
- [ ] Alert acknowledgment tested.
- [ ] Alert resolution tested.
- [ ] Device automatic recovery scenario tested.
- [ ] Unauthorized access attempt tested.
- [ ] Device registry displays correct status information.
- [ ] Audit event is generated for all alert operations.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] SRS inconsistency is documented and validated with instructor.
- [ ] Traceability to RF 4.5 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 5 |
| Priority | High |
| Target sprint | Sprint 3 |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-IAM-003](../HU-IAM/user-stories.md#hu-iam-003-role-based-permission-assignment): Role-based permission assignment; [HU-CONF-001](#hu-conf-001-configure-system-alerts): Configure system alerts |

---

# HU-CONF-008: Delay and justification parametrization

## Story

**As** an administrator  
**I want** to configure which excuses or justifications are valid for supporting absences or delays  
**So that** the justification evaluation process follows institutional criteria and ensures consistent treatment of all student cases.

---

## Acceptance criteria

> Format: "Given [context/initial state], when [user action], then [expected and verifiable result]."

- [ ] **AC1:** Given that an authenticated administrator accesses the delay and justification parametrization module, when the system loads the module, then it displays the list of currently configured valid justifications and the option to add, modify, or remove parameters.

- [ ] **AC2:** Given that the administrator adds a new valid justification parameter, when the save operation is submitted, then the system validates that the parameter does not already exist in the system and stores the new justification type.

- [ ] **AC3:** Given that the justification parameter already exists in the system, when the administrator attempts to add it again, then the system rejects the operation and shows a clear error message indicating that the parameter is already defined.

- [ ] **AC4:** Given that the administrator modifies an existing justification parameter, when the changes are saved, then the system updates the parameter and reflects the changes in subsequent justification evaluations.

- [ ] **AC5:** Given that the administrator removes a justification parameter, when the removal is confirmed, then the system deactivates the parameter and it is no longer considered valid for new justification evaluations.

- [ ] **AC6:** Given that the user attempting to access the parametrization module does not have administrator permissions, when the request is processed, then the system denies access and records an unauthorized access attempt.

- [ ] **AC7:** Given that a justification parameter is configured, when a student submits a justification in [HU-JUS-001](../HU-JUS/user-stories.md#hu-jus-001-justification-support-upload), then the system uses the configured parameters to determine if the justification is valid during evaluation in [HU-JUS-002](../HU-JUS/user-stories.md#hu-jus-002-justification-approval-and-rejection).

- [ ] **AC8:** Given normal network conditions, when the administrator saves the parametrization changes, then the system responds in less than 5 seconds, according to RNF 1.

- [ ] **AC9:** Given that a justification parameter is added, modified, or removed, when the operation is completed, then the system records an audit event containing the administrator, action type, parameter details, date, and time.

---

## Technical notes

- RF 4.7 indicates that the system must validate that the configured parameter is not already defined in the system.
- RF 4.7 specifies that this is an **administrator-only** operation for defining valid justifications.
- The justification parametrization should consider:
  - Justification type name (e.g., medical certificate, family emergency, institutional activity).
  - Whether a supporting document is required.
  - Whether the justification applies to absences, delays, or both.
  - Maximum validity period (e.g., justification must be submitted within N days).
  - Whether the justification requires approval from a specific role.
- The configured parameters are used by:
  - [HU-JUS-001](../HU-JUS/user-stories.md#hu-jus-001-justification-support-upload): To validate that the submitted justification matches a valid type.
  - [HU-JUS-002](../HU-JUS/user-stories.md#hu-jus-002-justification-approval-and-rejection): To determine if the justification is valid during evaluation.
  - [HU-REP-004](../HU-REP/user-stories.md#hu-rep-004-delay-and-absence-query-by-parameters): To filter and report delays and absences by justification status.
- The system should consider:
  - Preventing removal of justification parameters that are actively used in pending justifications.
  - Maintaining a historical record of parameter changes for audit purposes.
  - Providing default justification types for initial setup.
- The team must define whether justification parameters are institution-specific or global.
- This story does not cover the justification submission process. That is handled in [HU-JUS-001](../HU-JUS/user-stories.md#hu-jus-001-justification-support-upload).
- This story does not cover the justification evaluation process. That is handled in [HU-JUS-002](../HU-JUS/user-stories.md#hu-jus-002-justification-approval-and-rejection).
- This story does not cover delay detection. That is handled in [HU-ATT-001](../HU-ATT/user-stories.md#hu-att-001-facial-attendance-registration).

**Responsible service(s):** Configuration service / Justification parametrization service, proposed.  
**Endpoint(s) implemented:**  
- `GET /api/v1/config/justifications` — proposed, returns configured justification parameters.  
- `POST /api/v1/config/justifications` — proposed, adds a new justification parameter.  
- `PUT /api/v1/config/justifications/{paramId}` — proposed, modifies an existing parameter.  
- `DELETE /api/v1/config/justifications/{paramId}` — proposed, deactivates a parameter.  

**Events generated:**  
- `JustificationParameterAdded`  
- `JustificationParameterModified`  
- `JustificationParameterDeactivated`  
- `JustificationParametrizationFailed`  
- `UnauthorizedParametrizationAccessAttempt`  

**Required permissions:**  
- Administrator only.  
- Regular users, instructors, and students must not access justification parametrization.

---

## Definition of Done (DoD)

> This HU can only be closed when it meets the team's full DoD.

**Additional checks specific to this HU:**

- [ ] Justification parametrization module is accessible only to administrators.
- [ ] Successful parameter addition tested.
- [ ] Duplicate parameter scenario tested.
- [ ] Successful parameter modification tested.
- [ ] Successful parameter deactivation tested.
- [ ] Unauthorized access attempt tested.
- [ ] Configured parameters are used in justification evaluation flow.
- [ ] Audit event is generated for all parametrization operations.
- [ ] Confirmation and error messages are clear.
- [ ] Response time is validated under normal conditions and remains under 5 seconds.
- [ ] Traceability to RF 4.7 is documented.

---

## Estimation and priority

| Field | Value |
|---|---|
| Story Points | 5 |
| Priority | High |
| Target sprint | Sprint 2 |
| Dependencies | [HU-IAM-001](../HU-IAM/user-stories.md#hu-iam-001-user-login): User login; [HU-IAM-003](../HU-IAM/user-stories.md#hu-iam-003-role-based-permission-assignment): Role-based permission assignment |