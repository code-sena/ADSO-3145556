# Informe de Calidad y Mejoras de EnergyMonitor

Juan Esteban Cortés Parra y Miguel Ángel García Artunduaga

Servicio Nacional de Aprendizaje (SENA)

Tecnólogo en Análisis y Desarrollo de Software (ADSO), ficha 3145556

16 de octubre de 2026 (actualizado el 28 de octubre de 2026)

## Control de versiones

**Tabla 1**

*Control de versiones del documento*

| Versión | Fecha      | Descripción del cambio                                                                                          | Responsables                                  |
|---------|------------|-----------------------------------------------------------------------------------------------------------------|-----------------------------------------------|
| 1.0     | 16/10/2026 | Primera versión: auditoría de código limpio, registro de deuda técnica y hoja de ruta de la versión 2.0.        | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.1     | 20/10/2026 | Seguimiento de la deuda: estado de cada elemento, nuevo escaneo estático, integración continua con GitHub Actions, deuda de operación surgida del despliegue en producción (DT-19 a DT-22) y hoja de ruta actualizada. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.2     | 24/10/2026 | Arquitectura de servicios en producción: extracción de seguridad, dispositivos y mediciones a microservicios con API gateway resuelta (DT-23) y migración de los cuatro módulos restantes en curso (DT-24); sesión web en cookies HttpOnly (DT-04 resuelta); pipelines y conteos de pruebas por repositorio; estrategia de escalabilidad actualizada. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.3     | 28/10/2026 | Migración completa a microservicios (DT-24 resuelta) y retiro del servicio central; escaneos de ESLint de la web y de la móvil sin hallazgos (DT-16 parcial); pipeline del servicio de notificaciones, el único desplegable que no lo tenía (DT-12); conteos de pruebas por repositorio; pruebas unitarias, verificación estática y protocolo manual de la móvil (DT-13); nueva deuda de operación DT-25 (despliegue por SSH como `root`). | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.4     | 28/10/2026 | Nueva deuda del trabajo de la última jornada: novedades de la web en el idioma de los commits (DT-26) y umbrales fijos de la alerta de consumo inusual (DT-27); conteos de pruebas actualizados. | Cortés Parra, J. E.; García Artunduaga, M. A. |

## Resumen

Este documento es el informe de calidad y mejoras de EnergyMonitor y se redacta con un enfoque de auditoría técnica y escalabilidad. Presenta el resultado de los escaneos estáticos que el proyecto sí tiene configurados —ESLint en la aplicación web y `expo lint` en la aplicación móvil— y declara como hallazgo que el servicio central no cuenta con análisis estático ni con reporte de cobertura. Las versiones 1.0 a 1.2 registraron 5 hallazgos en la web (3 errores y 2 advertencias) y 12 advertencias en la móvil; en la versión 1.3 (28 de octubre de 2026) los dos escaneos terminan sin hallazgos. A partir de esos resultados, de los defectos y riesgos registrados en el plan de pruebas y en la propuesta técnica, y de la puesta en producción descrita en el manual de implementación y despliegue, se construye un registro de 27 elementos de deuda técnica con su evidencia, impacto, severidad, esfuerzo, prioridad y estado. Al 20 de octubre de 2026 tres elementos estaban resueltos —entre ellos la integración continua, que ahora ejecuta las pruebas en cada *pull request* y despliega en producción con aprobación— y dos estaban resueltos en parte. Al 24 de octubre de 2026 se resolvieron además la sesión web en `localStorage` (DT-04) y la extracción a microservicios de los contextos de seguridad, dispositivos y mediciones detrás de un API gateway (DT-23), y la migración de los cuatro módulos restantes se completó el 28 de octubre de 2026 con el retiro del servicio central (DT-24). Ese día también quedaron sin hallazgos los escaneos de la web y de la móvil (DT-16, ahora solo a falta del lint en CI). Sobre ese registro se define el plan de mejoras y la hoja de ruta de la versión 2.0, que prioriza el pago de la deuda de calidad, verificación y operación (cobertura, pruebas herméticas, respaldos externos y monitoreo) antes de las funcionalidades avanzadas (actualización por aire y medidores inteligentes).

*Palabras clave:* calidad de software, código limpio, análisis estático, deuda técnica, integración continua, escalabilidad, ESLint, EnergyMonitor

## Introducción

### Propósito

Documentar el estado de calidad del código de EnergyMonitor y planificar su mejoramiento antes y después de la entrega del 28 de octubre de 2026. El documento reúne el análisis de código limpio, la gestión de la deuda técnica y el plan de mejoras y hoja de ruta de la versión 2.0. La estructura del informe se inspira en el modelo de calidad de producto de ISO/IEC 25010 (mantenibilidad, fiabilidad, seguridad y eficiencia de desempeño), pero no aplica una evaluación certificada.

### Alcance

La auditoría cubre el código fuente de los componentes que están versionados en los repositorios del proyecto: los servicios Java (API gateway, servicios de seguridad, dispositivos y mediciones y servicio central, con Java 21 y Spring Boot 4.1.1; el gateway, con Spring Boot 4.0.8), la aplicación web (React 19 y Vite 8), la aplicación móvil (React Native 0.86 y Expo SDK 57) y, de forma parcial, el firmware del dispositivo, los archivos de infraestructura y los pipelines de integración continua. El análisis estático automatizado solo puede ejecutarse en los clientes, porque son los únicos que declaran herramientas de linting; los servicios Java se evalúan mediante revisión manual dirigida y mediante el registro de deuda ya documentado en el plan de pruebas (carpeta `07-pruebas`) y en la propuesta técnica (carpeta `04-propuesta-tecnica`). La operación en producción se evalúa con el manual de implementación y despliegue (carpeta `09-implementacion`).

### Enfoque del documento

El lenguaje es el de una auditoría técnica orientada a la escalabilidad. Por eso no se limita a listar defectos: cada hallazgo se traduce en un elemento de deuda técnica con acción propuesta y estado, y cada acción se ordena en una hoja de ruta que separa el pago de la deuda (versión 1.1) del crecimiento funcional (versión 2.0). Las decisiones de escalabilidad se justifican por lo que el sistema es hoy —un API gateway, tres microservicios y un servicio central que sigue siendo un monolito modular, todos en un solo servidor— y no suponen una arquitectura que el proyecto no haya adoptado.

### Referencias del documento

Los identificadores de deuda técnica (DT-01 a DT-27) y de mejoras (MP-01 a MP-22) son propios de este documento. Los defectos (DEF-01, DEF-02), las observaciones (DOC-01) y los riesgos (R-01 a R-09) provienen del plan y reporte de pruebas; los riesgos técnicos (RT-01 a RT-09) y las discrepancias documentales (DD-01 a DD-03) provienen de la propuesta técnica; los requisitos (RF-*, RNF-*) provienen de la especificación de requisitos. Los códigos y la numeración son idénticos en las versiones en español e inglés.

## Metodología de la auditoría

La auditoría combinó cuatro fuentes: la ejecución de los analizadores estáticos disponibles, la revisión manual dirigida del código con mayor riesgo, la consolidación de la deuda ya detectada en los demás documentos del proyecto y la revisión de los pipelines y de la operación en producción. La versión 1.1 repitió los escaneos el 20 de octubre de 2026 y revisó en el código el estado de cada elemento de deuda. La Tabla 2 resume las fuentes y su uso.

**Tabla 2**

*Fuentes de la auditoría y su uso*

| Fuente | Tipo | Uso en este informe |
|---|---|---|
| `eslint .` (ESLint 9.39.4) en `frontend/energy-monitor-web` | Ejecución real | Hallazgos de código limpio de la aplicación web (Tabla 4) |
| `eslint .` / `expo lint` (`eslint-config-expo`) en `frontend/energy-monitor-mobile` | Ejecución real | Hallazgos de código limpio de la aplicación móvil (Tabla 5) |
| `pom.xml` del gateway, de los servicios y del servicio central | Inspección | Confirma la ausencia de análisis estático y de cobertura (DT-11) |
| Pipelines `.github/workflows/ci-cd.yml` del gateway, los servicios, el backend, la web y la infraestructura | Inspección y ejecución real | Estado de la integración continua (DT-12, DT-16, DT-21) |
| Plan y reporte de pruebas (`07-pruebas`) | Consolidación | Defectos, riesgos y requisitos sin cobertura (DT-01, DT-07, DT-14, DT-18) |
| Propuesta técnica (`04-propuesta-tecnica`) | Consolidación | Riesgos técnicos, mantenimiento evolutivo y discrepancias (DT-03 a DT-10, DT-17) |
| Manual de implementación y despliegue (`09-implementacion`) | Consolidación | Deuda de operación en producción (DT-19 a DT-22) |
| Revisión manual de código | Inspección | Malas prácticas con evidencia `archivo:línea` (Tabla 6) y estado de la deuda (Tabla 7) |
| Repositorios de los servicios y `energy-monitor-infra` | Inspección | Estado de la extracción a microservicios (DT-23, DT-24) |

*Nota metodológica:* el proyecto no tiene configurado SonarQube ni ninguna otra plataforma de análisis continuo. No se reportan métricas de SonarQube porque no existen; su ausencia se registra como hallazgo (DT-11) y como mejora propuesta (MP-16).

## Análisis de Código Limpio

### Herramientas de análisis estático disponibles

La Tabla 3 muestra qué herramientas de análisis estático y de verificación automática existen realmente en cada componente y en qué estado se encuentran.

**Tabla 3**

*Herramientas de análisis estático y su estado por componente*

| Componente | Herramienta | Versión | Estado | Observación |
|---|---|---|---|---|
| Aplicación web | ESLint (flat config) con `eslint-plugin-react-hooks` y `eslint-plugin-react-refresh` | 9.39.4 | Configurada y ejecutable (`npm run lint`) | Sin hallazgos desde la versión 1.3 (Tabla 4 conserva los anteriores); no se ejecuta en CI |
| Aplicación móvil | ESLint con `eslint-config-expo` y verificación de tipos (`tsc --noEmit`) | vía Expo (`expo lint`) | Configurada y ejecutable | Sin hallazgos desde la versión 1.3 (Tabla 5 conserva los anteriores); no tiene CI |
| Servicios Java (gateway y siete servicios) | Checkstyle / SpotBugs / PMD / Sonar | — | No configurada | No hay plugin en ningún `pom.xml`; hallazgo DT-11 |
| Servicios Java | JaCoCo (cobertura) | — | No configurada | Sin reporte de cobertura; hallazgo DT-11 |
| Infraestructura | `shellcheck`, `bash -n`, `docker compose config`, `nginx -t` | — | Configuradas en CI | Validan scripts, Compose y la plantilla de Nginx en cada *pull request* |
| Gateway, siete servicios, web e infraestructura | GitHub Actions con *checks* obligatorios | — | Configurada | Pruebas y build obligatorios para fusionar; sin lint ni umbral de cobertura |
| Servicios Java | `ModularityTests` (Spring Modulith) | 2.1.1 | Fue configurada y en verde | Verificó los límites de los módulos mientras vivían en el servicio central; retirado con él el 28 de octubre de 2026 |
| Todos | SonarQube / SonarCloud | — | No configurada | Sin análisis continuo ni calidad de puerta |

### Resultados del escaneo estático de la aplicación web

Se ejecutó `eslint .` sobre `frontend/energy-monitor-web`. El resultado de las versiones 1.0 a 1.2 fue de 5 hallazgos: 3 errores y 2 advertencias en 4 archivos. **En la versión 1.3 (28 de octubre de 2026, commit `8457d50` de `dev`) el escaneo termina con código de salida 0 y sin hallazgos**: los 5 se corrigieron. La Tabla 4 conserva el detalle de los hallazgos corregidos como registro.

**Tabla 4**

*Hallazgos de ESLint en la aplicación web (versiones 1.0 a 1.2)*

| Archivo:línea | Severidad | Regla | Descripción | Acción propuesta |
|---|---|---|---|---|
| `src/components/layout/Navbar/NavList.jsx:5` | Error | `no-unused-vars` | La variable `setOpen` se declara y no se usa. | Eliminar la variable o usarla |
| `src/context/ThemeContext.jsx:3` | Error | `react-refresh/only-export-components` | El archivo exporta constantes además de componentes, lo que rompe la recarga rápida. | Mover constantes a un archivo aparte |
| `src/context/ThemeContext.jsx:182` | Error | `react-refresh/only-export-components` | Mismo caso en una segunda exportación. | Mover la función/constante a un archivo aparte |
| `src/features/DetailHome/Consumption/Consumption.jsx:114` | Advertencia | `react-hooks/exhaustive-deps` | El `useMemo` declara una dependencia innecesaria (`i18n.language`). | Revisar la lista de dependencias |
| `src/features/DetailHome/ConsumptionHistory/ComsumptionHistory.jsx:167` | Advertencia | `react-hooks/exhaustive-deps` | Mismo caso en el segundo `useMemo`. | Revisar la lista de dependencias |

### Resultados del escaneo estático de la aplicación móvil

Se ejecutó el lint de Expo sobre `frontend/energy-monitor-mobile`. El resultado de las versiones 1.0 a 1.2 fue de 12 advertencias en archivos de código fuente, sin errores. **En la versión 1.3 (28 de octubre de 2026, commit `1e4109b` de `dev`) `npx expo lint` y `npx tsc --noEmit` terminan con código de salida 0 y sin hallazgos**: las 12 advertencias se corrigieron y los errores `import/no-unresolved` de la versión 1.1 no aparecen con las dependencias instaladas con `npm ci`. La Tabla 5 conserva el detalle de los hallazgos corregidos como registro.



**Tabla 5**

*Hallazgos del lint en la aplicación móvil (versiones 1.0 a 1.2)*

| Archivo:línea | Severidad | Regla | Descripción | Acción propuesta |
|---|---|---|---|---|
| `src/components/layout/Sidebar/Sidebar.tsx:23` | Advertencia | `@typescript-eslint/no-empty-object-type` | Interfaz vacía que acepta cualquier valor no nulo. | Tipar la interfaz o usar `object`/`unknown` |
| `src/features/auth/screens/LoginScreen/LoginScreen.tsx:16` | Advertencia | `@typescript-eslint/no-empty-object-type` | Interfaz vacía. | Tipar la interfaz |
| `src/features/auth/screens/NewPasswordScreen/NewPasswordScreen.tsx:12` | Advertencia | `@typescript-eslint/no-empty-object-type` | Interfaz vacía. | Tipar la interfaz |
| `src/features/auth/screens/RecoverPasswordScreen/RecoverPasswordScreen.tsx:12` | Advertencia | `@typescript-eslint/no-empty-object-type` | Interfaz vacía. | Tipar la interfaz |
| `src/features/auth/screens/RegisterScreen/RegisterScreen.tsx:12` | Advertencia | `@typescript-eslint/no-empty-object-type` | Interfaz vacía. | Tipar la interfaz |
| `src/features/auth/screens/VerifyAccountScreen/VerifyAccountScreen.tsx:12` | Advertencia | `@typescript-eslint/no-empty-object-type` | Interfaz vacía. | Tipar la interfaz |
| `src/features/auth/screens/VerifyRecoverPasswordScreen/VerifyRecoverPasswordScreen.tsx:12` | Advertencia | `@typescript-eslint/no-empty-object-type` | Interfaz vacía. | Tipar la interfaz |
| `src/features/dashboard/screens/DashboardScreen/DashboardScreen.tsx:25` | Advertencia | `@typescript-eslint/no-empty-object-type` | Interfaz vacía. | Tipar la interfaz |
| `src/features/auth/components/VerifyAccountForm/VerifyAccountForm.tsx:44` | Advertencia | `react-hooks/incompatible-library` | Uso de una función incompatible con las reglas de hooks. | Revisar el uso del hook |
| `src/features/auth/components/VerifyRecoverPasswordForm/VerifyRecoverPasswordForm.tsx:45` | Advertencia | `react-hooks/incompatible-library` | Mismo caso. | Revisar el uso del hook |
| `src/components/OtpInput/OtpInput.tsx:23` | Advertencia | `@typescript-eslint/array-type` | Tipo de arreglo declarado de forma no preferida. | Usar la forma `Type[]` |
| `src/services/http/httpClient.ts:19` | Advertencia | `import/no-named-as-default-member` | Se usa `axios.create` como miembro del import por defecto. | Importar `create` de forma nombrada o ajustar la regla |

### Malas prácticas detectadas con revisión manual

Más allá de los analizadores, la revisión dirigida sobre los puntos de mayor riesgo encontró las prácticas de la Tabla 6. Todas tienen evidencia en el código o en los documentos de prueba, propuesta y despliegue.

**Tabla 6**

*Malas prácticas y riesgos detectados con evidencia*

| ID | Componente | Práctica observada | Evidencia | Impacto |
|---|---|---|---|---|
| BP-A | Servicio central (security; hoy servicio de seguridad) | Un manejador global captura `Exception.class`, por lo que una solicitud con un parámetro obligatorio ausente respondía 500 en lugar de 400. **Corregida en la versión 1.1** (DT-01). | `SecurityExceptionHandler.java` (`handleUnexpected`); defecto DEF-02 de `07-pruebas` | Contrato de error inconsistente para el cliente |
| BP-B | Aplicación web | Los tokens de sesión se almacenaban en `localStorage`. **Corregida en la versión 1.2** (DT-04): la sesión viaja en cookies HttpOnly. | Riesgo RT-03 de `04-propuesta-tecnica`; `src/services/auth/session.js` | Exposición ante un ataque XSS |
| BP-C | Servicio de seguridad (antes módulo security del servicio central) | El contador de intentos de recuperación vive en memoria y se reinicia con el servicio. | Riesgo RT-05; prueba `InMemoryResetAttemptLimiterTest` | El límite no se comparte entre instancias |
| BP-D | Servicio central | Las pruebas de persistencia e integración no son herméticas: necesitan MySQL real y la variable `SPRING_DATASOURCE_PASSWORD`. | Riesgo R-01 de `07-pruebas` | Mayor esfuerzo y fragilidad al ejecutar las pruebas |
| BP-E | Firmware | La telemetría se publica con MQTT en QoS 0. | Riesgo RT-02 de `04-propuesta-tecnica` | Posible pérdida de lecturas |
| BP-F | Infraestructura | Los respaldos de MySQL se guardan en el mismo servidor que la base de datos. | `deploy/backup-mysql.sh`; sección 8 de `09-implementacion` | Una falla del servidor pierde a la vez los datos y sus respaldos |

### Fortalezas de código limpio observadas

La auditoría también reconoce decisiones que favorecen la mantenibilidad y que conviene preservar en la versión 2.0:

- **Arquitectura hexagonal por módulo y por servicio.** Cada módulo y cada servicio separan dominio, aplicación y adaptadores; dentro del servicio central los módulos solo se comunican por sus paquetes `api`, y la prueba `ModularityTests` verifica esos límites.
- **Contratos explícitos.** Cada servicio expone DTOs y una API REST bajo `/api/v1`, con manejo de errores propio; entre servicios hay una API interna (`/internal/**`) y eventos con *exchanges* y *routing keys* propios en RabbitMQ.
- **Extracción sin romper a los clientes.** Los paquetes intermediarios del servicio central conservan los contratos que ya usaban los módulos y el gateway mantiene las rutas públicas, por lo que la web y la móvil siguieron llamando las mismas rutas al separar los servicios.
- **Concentración de acceso HTTP en el cliente.** La web reúne las llamadas en `src/services/`, con un cliente HTTP y un mapa de errores a claves de i18n.
- **Internacionalización centralizada.** Los textos de la web y de la móvil se gestionan con i18n en lugar de quedar embebidos.
- **Convenciones de desarrollo documentadas.** Commits en Conventional Commits, ramas por tarea y revisión por pull request (`00-governance/git-conventions.md`).
- **Sin marcas de trabajo pendiente en el código.** La búsqueda de `TODO`, `FIXME`, `HACK` y `XXX` no encontró coincidencias en el código fuente del servicio central ni de la aplicación web.
- **Integración y despliegue continuos (versión 1.1).** Cada *pull request* del backend, la web y la infraestructura ejecuta sus pruebas en GitHub Actions como *check* obligatorio; desde la versión 1.2, también los del gateway y de los servicios de seguridad, dispositivos y mediciones. Los cambios en `main` se despliegan con aprobación manual, respaldo previo, prueba de salud y reversión automática.

## Gestión de Deuda Técnica

### Registro de deuda técnica

La Tabla 7 es el registro de la deuda técnica del proyecto. Cada elemento tiene identificador propio (DT), componente, descripción, evidencia, tipo, severidad, esfuerzo, prioridad, acción propuesta y estado. El esfuerzo se estima cualitativamente (bajo, medio, alto) y la prioridad resulta de la matriz de la sección siguiente. Los valores de severidad y esfuerzo son una valoración del equipo y no una medición automática, por lo que quedan sujetos a revisión. El estado se revisó en el código el 20 de octubre de 2026 y de nuevo el 24 de octubre de 2026: **Resuelta**, **Parcial** (avanza, pero queda trabajo), **En curso** (el trabajo se está haciendo) o **Abierta**. Los elementos DT-19 a DT-22 surgieron del despliegue en producción, y DT-23 y DT-24, de la arquitectura del servicio central.

**Tabla 7**

*Registro de deuda técnica*

| ID | Componente | Descripción | Evidencia | Tipo | Severidad | Esfuerzo | Prioridad | Acción propuesta | Estado |
|----|------------|-------------|-----------|------|-----------|----------|-----------|------------------|--------|
| DT-01 | Backend · security | El manejador global captura `Exception.class`, y un parámetro obligatorio ausente responde 500 en lugar de 400. | `SecurityExceptionHandler.java`; DEF-02 | Correctiva | Alta | Bajo | P1 | Añadir un manejador para `MissingServletRequestParameterException` | **Resuelta**: los errores del framework conservan su estado 4xx y un tipo inválido responde 400 |
| DT-02 | Backend · measurement/home | El historial de consumo de un dispositivo no se limita al periodo en que estuvo vinculado al hogar. | RF-HOM-11 de `02-especificacion-requisitos`; Tabla 17 de `07-pruebas` | Funcional pendiente | Media | Medio | P2 | Filtrar el historial por el rango de vinculación | Abierta |
| DT-03 | Firmware · MQTT | La telemetría se publica con QoS 0. | RT-02 | Riesgo de diseño | Media | Medio | P2 | Evaluar QoS 1 y persistencia de mensajes | Abierta |
| DT-04 | Web · seguridad | Los tokens de sesión se guardan en `localStorage`. | RT-03 | Riesgo de seguridad | Alta | Medio | P1 | Migrar a cookies `httpOnly` o a memoria más renovación | **Resuelta**: el servicio de seguridad entrega los tokens en cookies HttpOnly y el gateway lee el token de la cookie; la web solo guarda datos no secretos de la cuenta |
| DT-05 | Firmware | No existe actualización de firmware por aire (OTA). | RT-04 | Funcional pendiente | Baja | Alto | P3 | Diseñar la actualización por aire | Abierta |
| DT-06 | Servicio de seguridad | El límite de intentos de recuperación vive en memoria. | RT-05 | Riesgo de seguridad | Media | Medio | P2 | Limitador compartido y persistente | Abierta |
| DT-07 | Backend · pruebas | Las pruebas de persistencia e integración requieren MySQL real; 37 clases quedaron sin ejecutar. | R-01; sección de resultados de `07-pruebas` | Deuda de verificación | Media | Medio | P1 | Adoptar Testcontainers | **Parcial**: en CI cada servicio ejecuta su suite contra un MySQL 8.4 de servicio (al 28 de octubre de 2026: 774 pruebas de seguridad, 260 de hogares, 235 de notificaciones, 151 de mediciones, 129 de dispositivos, 128 de alertas, 71 de recomendaciones y 69 del gateway); en local siguen sin ser herméticas |
| DT-08 | Servicio de seguridad | El access token sigue válido hasta 15 minutos tras eliminar la cuenta. | RT-07 | Riesgo de seguridad | Baja | Medio | P3 | Lista de revocación o acortar la vigencia | Abierta |
| DT-09 | Backend · alert | Algunos casos límite de alertas de conectividad requieren revisión manual. | RT-08 | Correctiva | Baja | Medio | P3 | Reglas de resolución automática | Abierta |
| DT-10 | Web · device | Web Bluetooth tiene limitaciones de compatibilidad y exige contexto seguro. | RT-09 | Limitación asumida | Baja | Bajo | P3 | Documentar y reforzar el portal de configuración | Abierta (en producción el contexto seguro lo da HTTPS) |
| DT-11 | Servicios Java · tooling | No hay análisis estático (Checkstyle, SpotBugs, PMD, Sonar) ni cobertura (JaCoCo) en el `pom.xml`. | `pom.xml` del gateway, de los servicios y del backend; Tabla 4 de `07-pruebas` | Deuda de calidad | Media | Medio | P1 | Integrar JaCoCo, un analizador estático y SonarQube | Abierta (tampoco en los servicios nuevos) |
| DT-12 | Todos · proceso | No existe integración continua ni comprobaciones obligatorias, aunque la convención exige CI verde antes del merge. | Ausencia de `.github/workflows`; `git-conventions.md` | Deuda de proceso | Media | Medio | P1 | Crear pipelines y completar la protección de ramas | **Resuelta**: pipelines con *checks* obligatorios en `dev` y `main` en el gateway, los siete servicios, la web y la infraestructura (el del servicio de notificaciones se incorporó el 28 de octubre de 2026, el último desplegable que no lo tenía); la aplicación móvil no tiene pipeline (DT-13) |
| DT-13 | Mobile · pruebas | La aplicación móvil no tiene pruebas automatizadas ni pipeline. | R-02 | Deuda de verificación | Media | Alto | P2 | Pruebas de componentes y de servicios, y un pipeline propio | **Parcial** (28 de octubre de 2026): 78 pruebas unitarias en 8 suites (`npm test`), `tsc` y `expo lint` sin errores y un protocolo de 12 pruebas manuales (`07-pruebas`, Tabla 38) por ejecutar; siguen sin pruebas de componentes ni pipeline |
| DT-14 | Backend · security (hoy servicio de seguridad) | La prueba `SecuritySupportModelTest.refusesMissingNames` quedó desactualizada respecto al código. | DEF-01 | Correctiva | Baja | Bajo | P2 | Actualizar la prueba | **Resuelta**: la prueba exige solo el nombre y el apellido ausente se acepta, como en el código |
| DT-15 | Documentación · admin | El módulo de administración existe en el código, pero no tiene requisitos ni manuales. | DOC-01 | Deuda documental | Baja | Medio | P2 | Documentar requisitos, API y manuales de administración | **Parcial**: el manual de administrador y el manual técnico ya lo describen; falta el requisito en la especificación |
| DT-16 | Web y Mobile · estilo | Hallazgos de ESLint: 5 en la web (3 errores) y 12 en la móvil (0 errores). | Tablas 4 y 5 | Correctiva | Baja | Bajo | P2 | Corregir los hallazgos y activar el lint en CI | **Parcial**: los hallazgos se corrigieron y los dos escaneos terminan en cero (28 de octubre de 2026); los pipelines todavía no ejecutan el lint |
| DT-17 | Documentación | Discrepancias documentales DD-01 a DD-03 entre documentos y código. | `04-propuesta-tecnica`, Tabla 14 | Deuda documental | Baja | Bajo | P3 | Corregir el README web y los documentos generales | Abierta (el README web aún dice que las recomendaciones son simuladas) |
| DT-18 | Todos · verificación | Cuarenta requisitos de la especificación no tienen cobertura de prueba automatizada. | Sección de trazabilidad de `07-pruebas` | Deuda de verificación | Media | Alto | P2 | Completar la cobertura por prioridad | Abierta |
| DT-19 | Infraestructura · respaldos | Los respaldos de MySQL se guardan en el mismo VPS que la base de datos y no se ha probado su restauración. | `deploy/backup-mysql.sh`; BP-F | Riesgo de operación | Media | Bajo | P1 | Copiar cada respaldo fuera del servidor y programar una prueba de restauración | Abierta |
| DT-20 | Infraestructura · monitoreo | No hay monitoreo de disponibilidad ni alertas en producción: la salud solo se comprueba durante un despliegue. | Sección 8.1 de `09-implementacion` | Riesgo de operación | Media | Bajo | P1 | Monitor externo de `https://energy-monitor.tech` y del broker con aviso por correo | Abierta |
| DT-21 | CI · rendimiento | La suite del backend tarda unos 23 minutos en GitHub Actions, lo que demora cada *pull request*. | Sección 9.9 de `09-implementacion` | Deuda de proceso | Baja | Medio | P3 | Paralelizar la suite o separar las pruebas de integración más lentas | Abierta (medido con las 1131 pruebas del backend único; no se ha vuelto a medir tras repartir las suites entre los servicios) |
| DT-22 | Infraestructura · entornos | No hay entorno de *staging* y las imágenes se compilan en el servidor de producción. | Sección 9.10 de `09-implementacion` | Deuda de proceso | Baja | Medio | P3 | Segundo stack para `dev` e imágenes publicadas en un registro | Abierta |
| DT-23 | Backend · arquitectura | Los siete contextos vivían en un solo desplegable: un cambio en seguridad, dispositivos o mediciones obligaba a reconstruir y desplegar todo el servicio central, y ninguno podía desplegarse ni escalarse por separado. | `docs/Architecture.md` de `energy-monitor-backend`; `docker-compose.prod.yml` de `energy-monitor-infra` | Deuda de arquitectura | Media | Alto | P2 | Extraer los contextos a microservicios detrás de un API gateway, con REST interno y eventos con *outbox* | **Resuelta**: seguridad (`en-monitor-security-service`), dispositivos (`en-monitor-device-service`) y mediciones (`en-monitor-measurement-service`) son microservicios en producción desde el 24 de octubre de 2026, detrás de `en-monitor-api-gateway`, sin cambios en la API pública |
| DT-24 | Backend · arquitectura | Cuatro contextos (`home`, `alert`, `recommendation` y `notification`) seguían en el monolito modular `energy-monitor-backend`. | `docs/Architecture.md` de `energy-monitor-backend`; `docker-compose.prod.yml` de `energy-monitor-infra` | Deuda de arquitectura | Media | Alto | P2 | Migrarlos a microservicios | **Resuelta**: hogares, notificaciones, alertas y recomendaciones son servicios en producción desde el 28 de octubre de 2026 y `energy-monitor-backend` se retiró; la migración la realizó Miguel Ángel García Artunduaga |
| DT-25 | Infraestructura · acceso | El despliegue continuo entra al servidor por SSH como el usuario `root`; la llave está restringida en `authorized_keys` a `deploy.sh`, pero el comando corre con ese usuario. | `authorized_keys` del servidor; sección 8.5 de `09-implementacion` | Riesgo de operación | Baja | Medio | P3 | Usuario sin privilegios con acceso solo a Docker y rotación periódica de la llave de despliegue | Abierta |
| DT-26 | Web · novedades | Las novedades se arman con los títulos de los commits, que están en inglés, y solo salen los que siguen el formato `feat`, `fix` o `perf`. | `vite.config.js`; modal de novedades | Deuda de producto | Baja | Bajo | P3 | Mantener un texto curado por versión en los cuatro idiomas | Abierta |
| DT-27 | Alertas · consumo inusual | Los umbrales de la alerta (2,5 veces, 200 W, 24 horas y 5 W) son constantes del servicio: no se pueden ajustar por hogar ni por aparato. | `RegisterAnomalyAlertService` | Deuda de producto | Baja | Medio | P3 | Llevar los umbrales a la configuración del hogar y medir los falsos positivos con datos reales | Abierta |

### Criterios de priorización

La prioridad combina la severidad del impacto con el esfuerzo de corrección. La Tabla 8 fija la regla: lo que afecta la seguridad o el contrato con el cliente y cuesta poco se atiende primero; lo que es costoso y de bajo impacto se planifica para el final.

**Tabla 8**

*Criterios de priorización de la deuda*

| Prioridad | Regla | Significado |
|---|---|---|
| P1 | Severidad alta, o severidad media con esfuerzo bajo | Corrección inmediata; bloquea la versión 1.1 |
| P2 | Severidad media con esfuerzo medio o alto, o severidad baja con esfuerzo bajo | Corrección a corto plazo |
| P3 | Severidad baja con esfuerzo medio o alto | Mejora planificada; no bloquea |

### Matriz prioridad y esfuerzo

La Tabla 9 ubica cada elemento de deuda por severidad y esfuerzo. La esquina de severidad alta y esfuerzo bajo (P1) es la que primero debe pagarse. Los elementos tachados ya están resueltos.

**Tabla 9**

*Matriz de severidad y esfuerzo*

| Severidad \ Esfuerzo | Bajo | Medio | Alto |
|---|---|---|---|
| Alta | ~~DT-01~~ | ~~DT-04~~ | — |
| Media | DT-19, DT-20 | DT-03, DT-06, DT-07, DT-11, ~~DT-12~~ | DT-13, DT-18, ~~DT-23~~, ~~DT-24~~ |
| Baja | DT-10, ~~DT-14~~, DT-16, DT-17, DT-26 | DT-02, DT-08, DT-09, DT-15, DT-21, DT-22, DT-25, DT-27 | DT-05 |

*Nota:* en la versión 1.0, DT-04 aparecía en la columna de esfuerzo bajo aunque el registro le asigna esfuerzo medio. La versión 1.1 la ubica según el registro; su prioridad sigue siendo P1 por su severidad alta.

## Plan de Mejoras y Roadmap

### Objetivos de la versión 2.0

La versión 2.0 persigue cuatro objetivos de calidad, en este orden: primero, hacer la calidad verificable (cobertura, análisis estático e integración continua); segundo, cerrar los riesgos de seguridad y de operación reconocidos; tercero, crecer en funcionalidad sin comprometer la estabilidad; y cuarto, preparar el sistema para más usuarios y dispositivos. El pago de la deuda de prioridad P1 que sigue abierta (DT-07, DT-11, DT-19 y DT-20) es condición previa al crecimiento funcional.

### Mejoras planificadas

La Tabla 10 relaciona cada mejora con la deuda que resuelve o el requisito que completa, el beneficio esperado, su prioridad y su estado al 28 de octubre de 2026.

**Tabla 10**

*Mejoras planificadas*

| ID | Mejora | Deuda o requisito asociado | Beneficio | Prioridad | Estado |
|----|--------|----------------------------|-----------|-----------|--------|
| MP-01 | Manejo correcto de parámetros obligatorios ausentes (400) | DT-01, DEF-02 | Contrato de error consistente | P1 | **Entregada** |
| MP-02 | Cobertura de pruebas con JaCoCo y umbral mínimo | DT-11 | Calidad medible | P1 | Pendiente |
| MP-03 | Integración continua con lint, pruebas y build | DT-12 | Detección temprana de fallos | P1 | **Entregada** (pruebas y build; el lint pasa a MP-06) |
| MP-04 | Pruebas herméticas con Testcontainers | DT-07 | Pruebas reproducibles sin MySQL externo | P1 | Parcial (MySQL de servicio en CI) |
| MP-05 | Cookies `httpOnly` para la sesión de la web | DT-04 | Reduce el riesgo de XSS | P1 | **Entregada** |
| MP-06 | Limpieza de los hallazgos de ESLint y lint en CI | DT-16 | Código limpio sostenido | P2 | Parcial (hallazgos corregidos; falta el lint en CI) |
| MP-07 | Pruebas de componentes y servicios en la móvil, con pipeline | DT-13 | Verificación del cliente móvil | P2 | Parcial (78 pruebas unitarias, `tsc`, `expo lint` y protocolo manual; faltan pruebas de componentes y pipeline) |
| MP-08 | Limitador de intentos compartido y persistente | DT-06 | Límite efectivo entre instancias | P2 | Pendiente |
| MP-09 | Telemetría con QoS 1 y persistencia | DT-03 | Menos pérdida de lecturas | P2 | Pendiente |
| MP-10 | Historial de consumo por periodo de vinculación | DT-02, RF-HOM-11 | Datos correctos por dispositivo | P2 | Pendiente |
| MP-11 | Notificaciones de recomendaciones (correo y push) | RF-NOT-02, RF-NOT-03 | Cierra requisitos sin implementar | P2 | **Entregada** (el módulo de notificaciones consume `RecommendationCreated`) |
| MP-12 | Completar la cobertura de los requisitos pendientes | DT-18 | Mayor confianza en los requisitos | P2 | Pendiente |
| MP-13 | Interfaz de administración y sus requisitos | DT-15, DOC-01 | Administración documentada y usable | P2 | Parcial (interfaz y manuales listos; falta el requisito) |
| MP-14 | Actualización de firmware por aire (OTA) | DT-05, RT-04 | Firmware actualizable sin cable | P3 | Pendiente |
| MP-15 | Medidores inteligentes y nuevas fuentes de datos | Mantenimiento evolutivo | Mayor alcance del producto | P3 | Pendiente |
| MP-16 | Análisis continuo con SonarQube | DT-11 | Calidad de puerta sostenida | P3 | Pendiente |
| MP-17 | Respaldos fuera del servidor y prueba de restauración periódica | DT-19 | Los datos sobreviven a una falla del VPS | P1 | Pendiente |
| MP-18 | Monitoreo de disponibilidad con alertas | DT-20 | Detección de caídas sin depender de un usuario | P1 | Pendiente |
| MP-19 | Entorno de *staging* e imágenes en un registro | DT-22 | Probar antes de producción y despliegues más rápidos | P3 | Pendiente |
| MP-20 | Suite del backend más rápida en CI | DT-21 | *Pull requests* revisables en menos tiempo | P3 | Pendiente |
| MP-21 | Microservicios de seguridad, dispositivos y mediciones con API gateway, REST interno y eventos en RabbitMQ con *outbox* | DT-23 | Despliegue y escalado por servicio sin cambiar la API pública | P2 | **Entregada** (24 de octubre de 2026) |
| MP-22 | Migración de `home`, `alert`, `recommendation` y `notification` a microservicios | DT-24 | Completa la separación del servicio central | P2 | **Entregada** (28 de octubre de 2026) |

### Módulos opcionales y funcionalidades avanzadas

Además del pago de la deuda, la versión 2.0 contempla módulos opcionales que hoy están fuera del alcance. La Tabla 11 los enumera con el criterio de escalabilidad que los justifica.

**Tabla 11**

*Módulos opcionales de la versión 2.0*

| Módulo o funcionalidad | Descripción | Justificación de escalabilidad |
|---|---|---|
| Interfaz de administración ampliada | Sobre la administración actual (usuarios, estados, roles y avisos), agregar gestión de soporte, auditoría y contenidos desde la web | Reduce el trabajo manual sobre la base de datos |
| Actualización por aire (OTA) | Entrega de firmware firmado desde el servidor | Permite crecer en número de dispositivos sin visitas físicas |
| Medidores inteligentes | Integración con medidores comerciales además del prototipo | Amplía el mercado sin cambiar el servicio central |
| Analítica y recomendaciones avanzadas | Reglas más finas y comparaciones por tipo de hogar | Aprovecha el historial acumulado |
| Notificaciones enriquecidas | Más canales y preferencias por hogar y por miembro | Aumenta la retención de usuarios |
| Registro de auditoría ampliado | Trazabilidad de acciones de administración | Requisito habitual al abrir el sistema a más organizaciones |

### Estrategia de escalabilidad

El sistema corre hoy en un solo servidor, un VPS Hostinger KVM 2 (2 vCPU y 8 GB de RAM): un API gateway y siete microservicios (seguridad, dispositivos, mediciones, hogares, notificaciones, alertas y recomendaciones), con una sola base MySQL compartida. La Tabla 12 ordena las medidas de escalabilidad de menor a mayor costo, para no sobredimensionar la solución.

**Tabla 12**

*Estrategia de escalabilidad*

| Nivel | Medida | Cuándo aplicarla | Efecto esperado |
|---|---|---|---|
| 1 | Escalado vertical del VPS (de KVM 2 a KVM 4: 4 vCPU y 16 GB de RAM) | Al aumentar usuarios o dispositivos | Aumenta la capacidad sin cambiar el código ni reinstalar |
| 2 | Redis para caché y para el limitador de intentos | Cuando el limitador en memoria no baste | Comparte estado entre instancias y descarga la base de datos |
| 3 | Índices y partición temporal de la tabla de mediciones | Al crecer el historial de lecturas | Consultas de historial más rápidas |
| 4 | Procesamiento asíncrono con patrón outbox | Si se separan módulos en servicios | Evita perder eventos al desacoplar. **Aplicado en la versión 1.2**: RabbitMQ 4.1 con *transactional outbox* entre los servicios |
| 5 | Telemetría con QoS 1 y persistencia | Junto con la mejora MP-09 | Reduce la pérdida de lecturas bajo carga |
| 6 | Monitoreo con Actuator y métricas | Antes del crecimiento, junto con MP-18 | Visibilidad para decidir el escalado |

La regla es no adoptar un nivel superior mientras el inferior no se agote. La separación en microservicios ya permite desplegar cada servicio por separado; para correr varias instancias de los servicios de dispositivos o de mediciones, cada una necesita su propio `MQTT_CLIENT_ID`. La base de datos sigue siendo una sola y compartida.

### Hoja de ruta

La Tabla 13 ordena el trabajo en dos versiones: la 1.1, dedicada a pagar la deuda de calidad, seguridad, verificación y operación, y la 2.0, dedicada al crecimiento funcional. MP-01, MP-03, MP-05, MP-11, MP-21 y MP-22 ya se entregaron en octubre de 2026. Las fechas restantes son tentativas.

**Tabla 13**

*Hoja de ruta de mejoras*

| Versión | Periodo | Entregables | Deuda que cierra |
|---|---|---|---|
| 1.1 | Cuarto trimestre de 2026 | Entregadas: MP-01 (manejo de errores), MP-03 (integración continua), MP-05 (cookies seguras) MP-21 (microservicios de seguridad, dispositivos y mediciones) y MP-22 (migración de los cuatro módulos restantes). Pendientes: MP-02 (cobertura), MP-04 (pruebas herméticas), MP-17 (respaldos externos) y MP-18 (monitoreo) | DT-01, DT-04, DT-12, DT-23 y DT-24 (cerradas); DT-07, DT-11, DT-19 y DT-20 |
| 2.0 | 2027 | MP-06 a MP-10, MP-12 a MP-16, MP-19 y MP-20: lint en CI, pruebas móviles, limitador compartido, QoS 1, historial por vinculación, cobertura de requisitos, requisitos de administración, OTA, medidores, análisis continuo, *staging* y CI más rápida. MP-11 ya se entregó | DT-02, DT-03, DT-05, DT-06, DT-13, DT-15 a DT-18, DT-21, DT-22 y DT-25 |

## Conclusiones

El proyecto tiene una base de código ordenada —arquitectura hexagonal por módulos y por servicio, contratos explícitos, acceso HTTP centralizado y sin marcas de trabajo pendiente— y suites de pruebas amplias en cada servicio y en la web. Los analizadores que existen ya no reportan hallazgos: tenían 5 en la web y 12 en la móvil, casi todos advertencias de estilo o de tipado, y se corrigieron en la versión 1.3, lo que confirma que el código no está degradado.

Entre la versión 1.0 y la 1.1 se cerraron tres elementos de deuda. El defecto de contrato de errores (DT-01) se corrigió y la prueba desactualizada (DT-14) se alineó con el código. Además se implantó la integración continua (DT-12): ahora ninguna rama se fusiona sin pasar sus pruebas, y producción se actualiza con aprobación, respaldo previo, prueba de salud y reversión automática. Con ella, las 1131 pruebas del servicio central se ejecutan contra MySQL en cada *pull request*, lo que reduce en parte la deuda de verificación (DT-07).

En la versión 1.2 (24 de octubre de 2026) se cerraron dos elementos más. La sesión de la web pasó a cookies HttpOnly (DT-04), y los contextos de seguridad, dispositivos y mediciones se extrajeron a microservicios detrás de un API gateway, con REST interno y eventos en RabbitMQ con *transactional outbox*, sin cambiar la API pública (DT-23). Cada servicio tiene su propio pipeline y su suite: 481 pruebas del backend, 151 del servicio de mediciones, 130 del de dispositivos y 57 del gateway al 24 de octubre, y 660 del servicio de seguridad al 22 de octubre, todas en verde. La migración de los cuatro módulos que siguen en el servicio central está en curso (DT-24).

En la versión 1.3 (28 de octubre de 2026) se completó la migración: hogares, notificaciones, alertas y recomendaciones son servicios independientes en producción y el servicio central se retiró (DT-24). Con ella, el gateway y los siete servicios tienen su pipeline, que incluye ahora el de notificaciones, y sus ocho suites suman 1.817 pruebas en verde (774, 260, 235, 151, 129, 128, 71 y 69), además de las 253 de la web. Los escaneos de ESLint de la web y de la móvil terminan sin hallazgos (DT-16), y la móvil suma 78 pruebas unitarias, verificación de tipos y un protocolo de pruebas manuales (DT-13). La puesta en producción dejó una deuda nueva de operación (DT-25): el despliegue entra por SSH como `root`, aunque con una llave restringida a `deploy.sh`.

Las carencias que siguen están en el gobierno de la calidad y en la operación: no hay análisis estático ni cobertura en ninguno de los servicios Java, el lint no corre en CI, la aplicación móvil no tiene pruebas, y en producción los respaldos viven en el mismo servidor y no hay monitoreo de disponibilidad. La deuda P1 abierta se concentra en cuatro elementos (DT-07, DT-11, DT-19 y DT-20), de corrección acotada y alto impacto. Atenderlos primero —en la versión 1.1— deja el sistema en condiciones de crecer hacia la versión 2.0 sin arrastrar fragilidad. La escalabilidad se resuelve por niveles, empezando por el escalado vertical del VPS actual; la separación en microservicios ya permite desplegar por separado los servicios extraídos.

## Referencias

Cortés Parra, J. E., & García Artunduaga, M. A. (2026). *Especificación de requisitos de software de EnergyMonitor* (versión 2.0). SENA – CIES Regional Huila.

Cortés Parra, J. E., & García Artunduaga, M. A. (2026). *Manual de implementación y despliegue de EnergyMonitor*. SENA – CIES Regional Huila.

Cortés Parra, J. E., & García Artunduaga, M. A. (2026). *Propuesta técnica de EnergyMonitor*. SENA – CIES Regional Huila.

Cortés Parra, J. E., & García Artunduaga, M. A. (2026). *Plan y reporte de pruebas de EnergyMonitor*. SENA – CIES Regional Huila.

ESLint. (2025). *ESLint: Find and fix problems in your JavaScript code* [Documentación]. https://eslint.org/docs/latest/

GitHub. (2026). *GitHub Actions documentation* [Documentación]. https://docs.github.com/actions

International Organization for Standardization. (2023). *Systems and software engineering — Systems and software Quality Requirements and Evaluation (SQuaRE) — Product quality model* (ISO/IEC 25010:2023). https://www.iso.org/standard/78176.html

SonarSource. (2025). *SonarQube documentation* [Documentación]. https://docs.sonarsource.com/sonarqube/

## Decisiones abiertas para las próximas versiones

- Responsable y fecha de cada elemento de deuda técnica (columnas no incluidas para no suponerlas).
- Umbral mínimo de cobertura de pruebas (MP-02).
- Periodos exactos de las versiones 1.1 y 2.0 de la hoja de ruta (Tabla 13).
- Decisión sobre SonarQube: instancia propia o SonarCloud (MP-16).
- Destino de los respaldos externos y frecuencia de la prueba de restauración (MP-17).
- Herramienta de monitoreo de disponibilidad (MP-18).
- Presupuesto y alcance de la jerarquía de escalabilidad de mayor costo (niveles 3 a 5 de la Tabla 12).
