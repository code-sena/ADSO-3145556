# Pruebas y verificación de la aplicación móvil

Fecha de ejecución: 28 de octubre de 2026. Repositorio `energy-monitor-mobile`, rama `dev`, commit `1e4109b` más los archivos de prueba agregados ese día. Node con dependencias instaladas con `npm ci`.

Hasta la versión 1.2 la aplicación móvil no tenía pruebas automatizadas. En la versión 1.3 se agregó Jest con el preset `jest-expo` (`npm test`).

## Pruebas unitarias: 8 suites, 78 pruebas, todas aprobadas

| Suite | Pruebas | Qué verifica |
|---|---|---|
| `i18n/__tests__/locales.test.ts` | 32 | Los cuatro idiomas (es, en, pt, fr), las mismas claves en cada archivo de textos, ningún texto vacío y que cada clave de error que la API puede devolver existe en los cuatro idiomas |
| `services/http/__tests__/errorMessages.test.ts` | 12 | Clave de mensaje por operación y estado (428 pide los términos, 409, 429 con minutos de espera, 503) y caída al valor por defecto |
| `features/auth/__tests__/passwordRules.test.ts` | 7 | La política de contraseñas: longitud, mayúscula, tres minúsculas, tres números y carácter especial |
| `features/auth/__tests__/registerSchema.test.ts` | 7 | Esquema del formulario de registro: nombre y apellido, correo, contraseñas iguales y términos aceptados |
| `features/auth/__tests__/verifyCodeSchema.test.ts` | 7 | El código de verificación de seis dígitos |
| `services/http/__tests__/errors.test.ts` | 6 | Normalización de errores de axios, de red y del 429 con `Retry-After` |
| `features/auth/__tests__/termsConsent.test.ts` | 4 | El aviso de términos: aceptar, cancelar y descartar |
| `features/auth/__tests__/registerPayload.test.ts` | 3 | El cuerpo del registro: recortado, con `acceptTerms` y sin los controles del formulario |

Las pruebas unitarias no recorren pantallas ni llaman a la API real.

## Verificaciones estáticas

| Comando | Qué comprueba | Resultado |
|---|---|---|
| `npx tsc --noEmit` | Tipos de TypeScript de todo el código, incluidas las pruebas, sin generar archivos | Código de salida 0, sin errores |
| `npx expo lint` | Reglas de ESLint de Expo sobre `src/` | Código de salida 0, sin errores |

El protocolo de pruebas manuales (MOV-01 a MOV-12) está en el plan de pruebas, Tabla 38. Su estado es por ejecutar en un dispositivo Android con un APK construido desde `dev`.

Versión en inglés: [../../EN/mobile/static-verification.md](mobile-static-verification.md)
