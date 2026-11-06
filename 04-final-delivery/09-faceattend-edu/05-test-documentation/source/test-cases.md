# End-to-end cases

Each case runs against the full system. The detail is in `tools/e2e/mvp/flujos.mjs`.

| ID | Flow | Verified step | Expected result |
|---|---|---|---|
| E2E-01 | Sign-in | First sign-in with the initial password | The gateway blocks writes with 403 `PASSWORD_CHANGE_REQUIRED` |
| E2E-02 | Sign-in | Initial password change | The old password returns 401, the new one signs in and can write |
| E2E-03 | Sign-in | New account with a wrong password | 401, and the password policy is applied |
| E2E-04 | School onboarding | Public sign-up of school and administrator | Stays pending activation |
| E2E-05 | School onboarding | The Super Administrator activates the school | The school administrator can sign in |
| E2E-06 | Academic structure | Create program, period, group, course and room | The records are created |
| E2E-07 | Academic structure | Create instructor and students | Each one signs in and accepts the terms |
| E2E-08 | Academic structure | Enroll students in the group | Both students are enrolled |
| E2E-09 | Scheduling | Second block in the same room and time | Rejected |
| E2E-10 | Scheduling | Create a class session | Starts as Open, unique per block and date |
| E2E-11 | Attendance | The instructor marks Present manually | Recorded, and a duplicate is rejected |
| E2E-12 | Attendance | The instructor closes the session | `closedBy` and `closedAt` are recorded |
| E2E-13 | Attendance | Closing with a student without a record | That student becomes Absent and the present one does not change |
| E2E-14 | Justification | Approve without the required attachment | Rejected |
| E2E-15 | Justification | The student uploads the PDF and the administrator approves | `reviewedBy` and `reviewedAt` are recorded |
| E2E-16 | Web session | Web sign-in | HttpOnly cookie and no session id in the body |
| E2E-17 | Web session | Write without the anti-CSRF header | Returns 403 |
| E2E-18 | Web session | Sign-out | The session stops working across the gateway |

# Unit cases

The catalog of documented unit cases, with its traceability to requirements, is in the annex of this document.