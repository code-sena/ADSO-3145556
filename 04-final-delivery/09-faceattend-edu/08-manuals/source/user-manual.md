# User Manual

**FaceAttend EDU: Attendance Recording System Based on Facial Recognition**

Jonattan Steven Rizo Solano
Diego Andrés Gutiérrez Núñez
Juan David Arboleda Perdomo

> **Note.** English translation of *Manual de Usuario*. The original structure, section order and wording have been preserved.

---

# Modules

Each functional module of FaceAttend EDU is documented below, following a consistent format that includes name, description, access role, navigation path, usage steps, screenshot, expected result, and remarks or restrictions.

## Module 1: User Management (FR1)

Allows searching for existing users, linking them, and editing their basic data from the mobile application.

### Description

User lookup and linking module. The mobile application does not register new users and does not upload CSV files: it only searches for users that already exist in the system and allows them to be linked. The edit form allows the user's personal data to be modified, but it does not include fields for assigning the role or for activating or deactivating the account.

### Role That Can Use It

Administrator.

### Path or Menu Where It Is Located

Login > Main menu > Users

### Steps to Use It

1. Enter the "Users" module from the main menu.

2. Use the search box to locate the existing user.

3. Select the user from the list in order to link them.

4. If their data needs to be corrected, open the edit form and modify the available fields.

5. Save the changes.

### Screenshot

<img src="media/image8.png" style="width:1.45833in;height:3in" /> <img src="media/image1.png" style="width:1.44792in;height:3in" />

### Expected Result

The existing user is located and linked, and their personal data is updated when the form is edited.

### Remarks or Restrictions

Registration of new users, bulk CSV upload, role assignment, and account activation or deactivation are not performed from the mobile application.

## Module 2: Environment Management (FR2)

Allows querying and registering the environments or classrooms where attendance control takes place.

### Description

Module for managing environments/classrooms. The screen handles only the environment information; management of cohorts and courses is not performed in the mobile application but on the web platform. In addition to the basic environment data, the form includes the "Type" field, which indicates the kind of environment being registered.

### Role That Can Use It

Administrator.

### Path or Menu Where It Is Located

Login > Main menu > Environments

### Steps to Use It

1. Enter the "Environments" module from the main menu.

2. Select the option to register an environment.

3. Enter the environment data: name, location, and capacity.

4. Save the changes.

> **Note.** The screen shows a "Type" field, but the `scheduling.environment` table stores only `code`, `name`, `capacity` and `status`: it has no column for the type of environment. The value selected on this screen is therefore not persisted in version 1.0.0. Either the column is added to the model or the field is removed from the screen; until then, the field should be ignored.

### Screenshot

<img src="media/image13.png" style="width:1.44792in;height:3in" /> <img src="media/image9.png" style="width:1.45833in;height:3in" />

### Expected Result

The environment is registered, with its type defined, and becomes available for attendance control.

### Remarks or Restrictions

Management of cohorts or courses and the assignment of responsible staff and shifts are not available on this screen; they are administered from the web platform.

## Module 3: Check-In and Check-Out Recording (FR3)

Allows capturing and recognizing users' faces through the camera, and updating the photo and the facial parameters.

### Description

Main module of the mobile application. It has two camera screens with different purposes: the facial enrollment screen (RegisterFace), which activates the camera to scan the face and compare it against the registered facial data, and the photo update screen (UpdatePhotoScreen), which allows the user's photograph to be changed. Updating the facial parameters is a function exclusive to the Student role.

### Role That Can Use It

Student and Teacher/Instructor (facial recording). Student (facial parameter update).

### Path or Menu Where It Is Located

Mobile application > Facial scan

### Steps to Use It

**Flow A: Facial recording (RegisterFace)**

1. The user opens the facial scan module in the mobile app.

2. The system activates the camera to detect the face.

3. The user positions themselves in front of the camera with good lighting.

4. The system compares the image against the previously registered facial data.

5. If the face matches, attendance is recorded as "present".

6. If the face does not match or the scan fails, the alternate recording method is enabled.

**Flow B: Photo update (UpdatePhotoScreen)**

1. Enter the photo update option.

2. Fill in the mandatory fields: name, identification document, and phone number. Without this data the camera is not enabled.

3. Open the camera and capture the new photograph.

4. Confirm and save the updated photo.

**Flow C: Facial parameter update (Student only)**

The student can update their facial parameters from their user profile. This option is not available to the other roles.

### Screenshot

<img src="media/image13.png" style="width:1.44792in;height:3in" /><img src="media/image12.png" style="width:1.38445in;height:3.01063in" />

### Expected Result

The user's attendance or absence information is stored, or else the photo and the facial parameters are updated, depending on the flow used.

### Remarks or Restrictions

The scan may fail due to poor lighting or movement of the user. It requires the user to already be registered in the system with their face and credentials, and the database to be active. The photo update camera only opens once name, identification document, and phone number have been filled in.

## Module 4: System Configuration (FR4)

Allows adjusting the alerts, the language, and the appearance of the application, as well as the user parameters.

### Description

Module where the user adjusts their preferences. Appearance and language are separate screens. The appearance screen offers HSL sliders (hue, saturation, and lightness) to customize the colors, vision modes for people with color blindness, and a WCAG accessibility evaluator that checks the contrast of the chosen palette. Lateness parameterization is not performed in this module; it is described in Module 8.

### Role That Can Use It

User (personal preferences, mobile app); Administrator (general system parameters).

### Path or Menu Where It Is Located

Mobile application > Settings / Main menu > Settings

### Steps to Use It

1. Enter the "Settings" module.

2. For alerts: enable or disable notifications and choose the tone.

3. For language: open the "Language" screen and select the preferred application language.

4. For appearance: open the "Appearance" screen and adjust the color preferences to taste.

5. Select a color-blindness vision mode if required.

6. For user parameters: update the personal data.

7. The administrator can additionally manage IoT device alerts and change a user's role.

### Screenshot

<img src="media/image13.png" style="width:1.44792in;height:3in" /> <img src="media/image14.png" style="width:1.45833in;height:3in" />

<img src="media/image4.png" style="width:1.9375in;height:3in" /> <img src="media/image7.png" style="width:2.07292in;height:3in" />

<img src="media/image18.png" style="width:1.4375in;height:3in" />

### Expected Result

The configuration changes are saved and applied immediately to the user experience.

### Remarks or Restrictions

Role changes are available only to the Administrator role. Lateness parameterization is located in the School Management module (FR8).

## Module 5: Generate History Records (FR5)

Allows querying the history of attendance and absences, filtering by name and date.

### Description

Query module that allows reviewing students' attendance and absences. The filters available in the mobile application are only the person's name and the date; filtering by cohort, environment, or instructor is not possible.

### Role That Can Use It

Teacher/Instructor and Administrator.

### Path or Menu Where It Is Located

Main menu > History

### Steps to Use It

1. Enter the "History" module.

2. Type the name of the person to be queried.

3. Select the date or date range of interest.

4. Review on screen the results that match the filters.

### Screenshot

<img src="media/image16.png" style="width:1.45833in;height:3in" /><img src="media/image10.png" style="width:1.38125in;height:2.99479in" />

### Expected Result

The system displays the list of attendance records and absences matching the name and date entered.

### Remarks or Restrictions

The mobile application does not include a user change history. Queries by cohort, environment, or instructor are not available on this screen.

## Module 6: Absence Justification Management (FR6)

Allows users to submit absence justifications and allows the responsible staff to approve or reject them.

### Description

Module that allows an absence justification to be submitted. The process does not start with a system notice: it is the user who manually enters the justification type, the date, and the requested data. Before the form there is an intermediate justifications menu from which the action to be performed is chosen. Afterwards a responsible party approves or rejects it, and the outcome is notified automatically.

### Role That Can Use It

Student (submitting justifications); Teacher/Instructor or Administrator (approval or rejection).

### Path or Menu Where It Is Located

Main menu > Justifications > Justifications menu

### Steps to Use It

1. Enter the "Justifications" module from the main menu.

2. In the intermediate justifications menu, select the option to submit a new justification.

3. Select the justification type.

4. Enter the date of the absence.

5. Manually complete the remaining requested data and upload the supporting document (document or image), if applicable or if required.

6. If no supporting document is uploaded, the absence is marked as unjustified where such a justification applies.

7. The responsible party reviews the justification and approves or rejects it.

8. The system automatically notifies the user of the outcome.

### Screenshot

<img src="media/image13.png" style="width:1.44792in;height:3in" /> <img src="media/image14.png" style="width:1.45833in;height:3in" /> <img src="media/image19.png" style="width:1.47917in;height:3in" />

### Expected Result

The absence is recorded as justified or unjustified, according to the decision taken, and the user receives the corresponding notification.

### Remarks or Restrictions

The system treats as valid only those justifications approved by the assigned responsible party.

## Module 7: Attendance Reports and Alerts (FR7)

Allows querying alerts for excessive absences or lateness and notifying guardians.

### Description

In the mobile application this screen works as an alert system: it identifies the students who exceed the absence or lateness limit and allows their guardians to be notified. It does not include report export or a standalone analytical dashboard.

### Role That Can Use It

Teacher/Instructor, Administrator.

### Path or Menu Where It Is Located

Main menu > Reports

### Steps to Use It

1. Enter the "Reports" module.

2. Review the alerts generated by excessive absences or lateness.

3. Select the student or the alert to be handled.

4. Send the notification to the corresponding guardian.

### Screenshot

<img src="media/image13.png" style="width:1.44792in;height:3in" /> <img src="media/image17.png" style="width:1.44792in;height:3in" />

### Expected Result

The alert is handled and the student's guardian receives the notification about the accumulated absences or lateness.

### Remarks or Restrictions

Report export and the analytical dashboard are not available in the mobile application. Alerts depend on the absence and lateness limits configured in the system.

## Module 8: School Management (FR8)

Allows querying and editing the school's general, contact, academic, and attendance information.

### Description

Administrative module organized into four tabs: General, Contact, Academic, and Attendance. The Attendance tab contains the lateness parameterization. The mobile application does not manage courses or the curriculum.

### Role That Can Use It

Administrator.

### Path or Menu Where It Is Located

Main menu > Settings > School Settings

### Steps to Use It

1. Enter the "School Settings" module.

2. On the "General" tab, review or update the school's general data.

3. On the "Contact" tab, review or update the contact data.

4. On the "Academic" tab, review or update the academic information.

5. On the "Attendance" tab, review or update the attendance parameters, including the lateness parameterization.

6. Save the changes made.

### Screenshot

<img src="media/image13.png" style="width:1.44792in;height:3in" /> <img src="media/image14.png" style="width:1.45833in;height:3in" /> <img src="media/image20.png" style="width:1.44792in;height:3in" />

### Expected Result

The school information and the attendance parameters are updated.

### Remarks or Restrictions

Only the administrator can modify this information. Management of courses and of the curriculum is not available in the mobile application.

## Module 9: Password Recovery (Cross-Cutting)

Allows account access to be restored when the user forgets their password.

### Description

Publicly accessible screen available before logging in, which guides the user through the password recovery process.

### Role That Can Use It

All roles.

### Path or Menu Where It Is Located

Login > Recover password

### Steps to Use It

1. On the login screen, select the password recovery option.

2. Enter the requested data to identify the account.

3. Follow the system instructions to define a new password.

4. Log in with the new password.

### Screenshot

<img src="media/image15.png" style="width:1.82292in;height:3.88069in" /><img src="media/image11.png" style="width:1.79688in;height:3.87747in" />

### Expected Result

The user resets their password and can log back into the system.

### Remarks or Restrictions

After completing the steps required for the password change, the user must log in again from the beginning in the application; it does not go directly to the dashboard, for the user's own security.

## Module 10: Profile (Cross-Cutting)

Allows the user to view their account information.

### Description

Screen where the user views their profile data within the mobile application.

### Role That Can Use It

All roles.

### Path or Menu Where It Is Located

Bottom navigation bar > Profile

### Steps to Use It

1. Select "Profile" on the bottom navigation bar.

2. Review the personal information displayed.

3. Use the options available on the screen to update the permitted data.

### Screenshot

<img src="media/image6.png" style="width:1.44792in;height:0.40491in" />

<img src="media/image5.png" style="width:1.9324in;height:5.00735in" /><img src="media/image2.png" style="width:2.36458in;height:4.72917in" /><img src="media/image3.png" style="width:2.31436in;height:4.68229in" />

### Expected Result

The user views and, where applicable, updates their profile information.

### Remarks or Restrictions

Edit this information only if strictly required or if an instructor/administrator requests it, so as to avoid problems after changing it.

## Module 11: News (Cross-Cutting)

Allows querying the institution's announcements and updates.

### Description

Informational screen where the user reviews the news published within the application.

### Role That Can Use It

All roles.

### Path or Menu Where It Is Located

Main screen of the application

### Steps to Use It

1. Select "News" on the bottom navigation bar.

2. Scroll through the news list.

3. Open the item of interest to read its content.

### Screenshot

<img src="media/image21.png" style="width:1.84375in;height:4.09805in" />

### Expected Result

The user views the institution's current news.

### Remarks or Restrictions

This view is only for seeing what changes or information is added/changed/removed regarding the school.

## Module 12: Bottom Navigation Bar (Cross-Cutting)

Allows moving between the main sections of the mobile application.

### Description

Fixed bar at the bottom of the screen that provides quick access to the main sections of the application, among them Profile and News.

### Role That Can Use It

All roles (the visible options may vary by role).

### Path or Menu Where It Is Located

Mobile application > Bottom bar

### Steps to Use It

1. Log in to the application.

2. Tap the icon of the desired section on the bottom bar.

3. To go back, tap another option on the bar.

### Screenshot

<img src="media/image6.png" style="width:1.44792in;height:0.40491in" />

### Expected Result

The user switches sections quickly from any main screen.

### Remarks or Restrictions

This is intended to provide a better experience and faster, easier use for the users of the mobile application.
