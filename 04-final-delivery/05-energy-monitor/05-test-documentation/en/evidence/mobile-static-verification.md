# Tests and verification of the mobile application

Run date: October 28, 2026. Repository `energy-monitor-mobile`, branch `dev`, commit `1e4109b` plus the test files added that day. Node with dependencies installed with `npm ci`.

Up to version 1.2 the mobile application had no automated tests. In version 1.3 Jest was added with the `jest-expo` preset (`npm test`).

## Unit tests: 8 suites, 78 tests, all passed

| Suite | Tests | What it verifies |
|---|---|---|
| `i18n/__tests__/locales.test.ts` | 32 | The four languages (es, en, pt, fr), the same keys in every text file, no empty text, and that every error key the API can return exists in the four languages |
| `services/http/__tests__/errorMessages.test.ts` | 12 | Message key per operation and status (428 asks for the terms, 409, 429 with minutes to wait, 503) and fallback to the default |
| `features/auth/__tests__/passwordRules.test.ts` | 7 | The password policy: length, uppercase, three lowercase, three digits and special character |
| `features/auth/__tests__/registerSchema.test.ts` | 7 | Registration form schema: first and last name, email, matching passwords and accepted terms |
| `features/auth/__tests__/verifyCodeSchema.test.ts` | 7 | The six-digit verification code |
| `services/http/__tests__/errors.test.ts` | 6 | Normalization of axios and network errors and of the 429 with `Retry-After` |
| `features/auth/__tests__/termsConsent.test.ts` | 4 | The terms prompt: accept, cancel and dismiss |
| `features/auth/__tests__/registerPayload.test.ts` | 3 | The registration body: trimmed, with `acceptTerms` and without the form controls |

Unit tests do not walk through screens nor call the real API.

## Static checks

| Command | What it checks | Result |
|---|---|---|
| `npx tsc --noEmit` | TypeScript types of all the code, including the tests, without generating files | Exit code 0, no errors |
| `npx expo lint` | Expo ESLint rules over `src/` | Exit code 0, no errors |

The manual test protocol (MOV-01 to MOV-12) is in the test plan, Table 38. Its status is to be run on an Android device with an APK built from `dev`.

Spanish version: [../../ES/mobile/verificacion-estatica.md](mobile-static-verification.md)
