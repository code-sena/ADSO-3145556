# Resumen de Vitest (aplicación web)

Ejecución del 14 de octubre de 2026 con `npm test` (Vitest 5.0.3, jsdom). Rama `dev`, commit `e6edc12`. Node.js 22.22.2.

**Total:** 20 archivos, 141 pruebas, 141 aprobadas, 0 fallidas.

| Archivo | Pruebas | Aprobadas | Fallidas | Tiempo (s) |
|---|---|---|---|---|
| components/layout/MainLayout/Sidebar/Sidebar.test.jsx | 2 | 2 | 0 | 0.4 |
| context/HomeContext.test.jsx | 11 | 11 | 0 | 0.7 |
| features/Account/Account.test.jsx | 6 | 6 | 0 | 3.5 |
| features/Account/AccountDeleteError.test.jsx | 1 | 1 | 0 | 1.3 |
| features/Admin/AdminNotifications.test.jsx | 5 | 5 | 0 | 1.0 |
| features/Admin/AdminUsers.test.jsx | 9 | 9 | 0 | 2.5 |
| features/DetailHome/Home/DeleteHome/DeleteHome.test.jsx | 5 | 5 | 0 | 1.6 |
| features/DetailHome/Users/Users.test.jsx | 12 | 12 | 0 | 1.7 |
| features/auth/components/RPasswordForm/recover.test.jsx | 4 | 4 | 0 | 4.4 |
| features/auth/components/RegisterForm/verify.test.jsx | 4 | 4 | 0 | 2.1 |
| features/dashboard/pages/DashboardPage.test.jsx | 5 | 5 | 0 | 1.4 |
| features/shared/homeTypes.test.js | 6 | 6 | 0 | 0.0 |
| routes/RequirePermission.test.jsx | 3 | 3 | 0 | 0.2 |
| routes/UserOnlyRoute.test.jsx | 4 | 4 | 0 | 0.2 |
| services/admin/adminApi.test.js | 4 | 4 | 0 | 0.0 |
| services/auth/authApi.test.js | 4 | 4 | 0 | 0.1 |
| services/home/homeApi.test.js | 15 | 15 | 0 | 0.0 |
| services/http/errorMessages.test.js | 27 | 27 | 0 | 0.0 |
| services/http/httpClient.refresh.test.js | 3 | 3 | 0 | 0.1 |
| services/http/httpClient.test.js | 11 | 11 | 0 | 0.1 |

## Ejecución de la versión 1.1

Ejecución del 20 de octubre de 2026 con `npx vitest run` (Vitest 5.0.3, jsdom). Rama `main`, commit `2b41cb3`. Node.js 22.22.2.

**Total:** 20 archivos, 141 pruebas, 141 aprobadas, 0 fallidas (38,6 s). Mismos archivos y pruebas que la ejecución anterior. El *check* `Tests and build` de GitHub Actions ejecuta estas pruebas y el build de producción en cada *pull request*.

**Cobertura:** el `package.json` de la web no define un script de cobertura; no hay reporte.
