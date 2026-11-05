# Plan y Reporte de Pruebas de EnergyMonitor

Juan Esteban Cortés Parra y Miguel Ángel García Artunduaga

Servicio Nacional de Aprendizaje (SENA)

Tecnólogo en Análisis y Desarrollo de Software (ADSO), ficha 3145556

14 de octubre de 2026 (actualizado el 28 de octubre de 2026)

## Control de versiones

**Tabla 1**

*Control de versiones del documento*

| Versión | Fecha      | Descripción del cambio                                                                    | Responsables                                  |
|---------|------------|-------------------------------------------------------------------------------------------|-----------------------------------------------|
| 1.0     | 14/10/2026 | Primera versión: inventario, trazabilidad y resultados de la ejecución sin base de datos. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.1     | 20/10/2026 | Ejecución de la suite completa contra MySQL (1131 pruebas, 0 fallos) en local y en GitHub Actions; cierre de DEF-01 y DEF-02; 3 clases de prueba nuevas de notificaciones push; trazabilidad, riesgos y evidencias actualizados. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.2     | 24/10/2026 | Arquitectura de servicios: elementos bajo prueba por repositorio (gateway, servicios de seguridad, dispositivos y mediciones, backend) con los commits de `main` en producción, totales de cada suite, pruebas de punta a punta de `energy-monitor-infra` y pipelines por repositorio. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.3     | 28/10/2026 | Migración completa: ocho suites Java por repositorio (backend retirado), web con 228 pruebas, cinco pruebas de punta a punta de `energy-monitor-infra`, verificación estática y protocolo de pruebas manuales de la aplicación móvil, trazabilidad de la aceptación de términos (RF-SEC-13) y de las reglas de las cuentas de Google, y riesgos actualizados. | Cortés Parra, J. E.; García Artunduaga, M. A. |
| 1.4     | 28/10/2026 | Nuevas pruebas de la alerta de consumo inusual, del visor de fotos, de las fotos de los miembros, de los modales de ayuda de Ajustes y del push en Brave; commits de `main` actualizados; trazabilidad con RF-ALE-07, RF-HOM-12, RF-CLI-07 y RF-CLI-08 (112 requisitos, 75 con prueba automatizada). | Cortés Parra, J. E.; García Artunduaga, M. A. |

## Resumen

Este documento es el plan y reporte de pruebas de EnergyMonitor. Inventaría las 129 clases de prueba automatizadas del servicio central (1097 métodos de prueba declarados) y las 20 suites de la aplicación web (141 pruebas), traza los 107 requisitos funcionales y no funcionales de la especificación contra esas pruebas, y registra la ejecución del 14 de octubre de 2026. La suite del servicio central sin base de datos ejecutó 835 pruebas, con 833 aprobadas y 2 fallidas; la web ejecutó 141 pruebas, todas aprobadas. Las 37 clases que necesitan MySQL no se ejecutaron porque falta la variable SPRING_DATASOURCE_PASSWORD. La aplicación móvil no tiene pruebas automatizadas. Se escribieron 4 clases de prueba nuevas y se registraron 2 defectos y 1 observación documental.

La versión 1.1 (20 de octubre de 2026) registra la ejecución de la suite completa del servicio central contra MySQL 8.4: 1131 pruebas en 132 clases, 0 fallos, tanto en local como en el *check* obligatorio `Tests` de GitHub Actions. Las 37 clases pendientes ya se ejecutan, los defectos DEF-01 y DEF-02 están cerrados y la web vuelve a aprobar sus 141 pruebas. La trazabilidad queda en 68 requisitos con prueba automatizada ejecutada y 39 sin cobertura.

La versión 1.2 (24 de octubre de 2026) corresponde a la arquitectura de servicios en producción: los módulos security, device y measurement salieron del servicio central a sus propios repositorios (`en-monitor-security-service`, `en-monitor-device-service` y `en-monitor-measurement-service`), detrás de un API gateway (`en-monitor-api-gateway`), y el servicio central conserva home, alert, recommendation y notification. Cada repositorio ejecuta su suite en su pipeline: backend 481 pruebas, servicio de mediciones 151, servicio de dispositivos 130 y gateway 57 (24 de octubre), y servicio de seguridad 660 (22 de octubre), todas en verde, con `ModularityTests` en verde. Dos pruebas de punta a punta sobre la pila local (`infra/e2e/device-flow.sh`, 21/21, y `infra/e2e/measurement-flow.sh`, 25/25) verifican lo que cruza los servicios. El inventario por clase y la trazabilidad de este documento siguen siendo los de la versión 1.1.

La versión 1.3 (28 de octubre de 2026) corresponde a la migración completa: home, notification, alert y recommendation también son servicios independientes (`en-monitor-home-service`, `en-monitor-notification-service`, `en-monitor-alert-service` y `en-monitor-recommendation-service`) y el servicio central se retiró. Ocho suites Java en verde (seguridad 773, hogares 260, notificaciones 234, mediciones 151, dispositivos 129, alertas 119, recomendaciones 71 y gateway 69), la web con 38 archivos y 228 pruebas, y cinco pruebas de punta a punta de `energy-monitor-infra` (`device-flow` 24, `measurement-flow` 28, `alert-flow` 36, `activity-flow` 60 y `account-deletion-flow` 12). La trazabilidad pasa a 108 requisitos con la aceptación de términos (RF-SEC-13): 71 con prueba automatizada (con los dos de idiomas de la aplicación móvil) y 37 sin cobertura. De la aplicación móvil se agregó una suite unitaria de 78 pruebas en 8 suites, todas aprobadas, y se ejecutaron la verificación de tipos y `expo lint` sin errores; además se definió un protocolo de 12 pruebas manuales, que sigue por ejecutar en un dispositivo.

La versión 1.4 (28 de octubre de 2026) agrega pruebas de lo último que se construyó: la regla de consumo inusual de un dispositivo (`RegisterAnomalyAlertServiceTest`, 8 casos, y su lugar en el consumidor de lecturas), el aviso de esa alerta en correo y push, el perfil con foto (`UserProfileQueryServiceTest`, el adaptador de hogares y el endpoint de miembros), el visor de fotos y las fotos de los miembros, los modales de ayuda de Ajustes (documentación por rol, soporte solo para usuarios y novedades) y la explicación del push en Brave. Totales: 1.817 pruebas en los ocho servicios Java (seguridad 774, hogares 260, notificaciones 235, mediciones 151, dispositivos 129, alertas 128, recomendaciones 71 y gateway 69), 253 en la web (44 archivos) y 78 en la móvil. La trazabilidad pasa a 112 requisitos: 75 con prueba automatizada y 37 sin cobertura.

*Palabras clave:* pruebas de software, trazabilidad, JUnit, Vitest, Spring Modulith, EnergyMonitor

## Introducción

### Propósito

Documentar las pruebas que ya existen y se ejecutan en EnergyMonitor, y mostrar qué requisitos cubren y cuáles no antes de la entrega del 28 de octubre de 2026. La estructura se adapta de IEEE 829-2008 e ISO/IEC/IEEE 29119-3.

### Alcance

Se prueban el API gateway y los siete servicios (seguridad, dispositivos, mediciones, hogares, notificaciones, alertas y recomendaciones), la aplicación web, la ingesta MQTT y la autorización del broker. Hasta la versión 1.1 esos contextos eran los siete módulos del servicio central; desde la 1.2 se repartieron en servicios, el servicio central se retiró el 28 de octubre de 2026 y cada servicio se prueba en su repositorio, y las pruebas de punta a punta de `energy-monitor-infra` verifican la comunicación entre ellos (rutas del gateway, API interna, broker y eventos en RabbitMQ). El firmware del dispositivo, el broker Mosquitto, Bluetooth, las notificaciones push y la aplicación móvil Android quedan fuera de las pruebas automatizadas y se declaran como vacíos conocidos; de la aplicación móvil se ejecutan además la verificación de tipos y el análisis estático, y se define un protocolo de pruebas manuales (versión 1.3). No se prueban el rendimiento, la carga ni la accesibilidad (WCAG), para los que la especificación no fija meta.

### Referencias del documento

La trazabilidad usa los códigos de la especificación de requisitos (RF-SEC, RF-HOM, RF-DEV, RF-MED, RF-ALE, RF-REC, RF-NOT, RF-CLI y RNF) y las historias HU-01 a HU-15 (Cortés Parra y García Artunduaga, 2026). Los IDs, los nombres de clases y los códigos son idénticos en las versiones en español e inglés.

## Elementos bajo prueba

La Tabla 2 lista los elementos bajo prueba en la versión 1.4, con la rama y el commit de `main` al cierre del 28 de octubre de 2026; la versión 1.3 usó los de ese día antes de los últimos cambios (seguridad `f57c76b`, hogares `f424083`, notificaciones `7b10398`, alertas `f050b56` y web `698d4c9`). La versión 1.2 usó `main` del 24 de octubre de 2026 (gateway `c070cec`, seguridad `334691a`, dispositivos `f8b8526`, mediciones `ee2cb81`, backend `aa9a713`, web `e59bece`, infra `eaf961b`). La versión 1.1 usó `main` (backend `90ae5ef`, web `2b41cb3`, infra `7324110`, móvil `dev` `4ebccfb`) y la 1.0, `dev` (backend `d03bbd3`, web `e6edc12`).

**Tabla 2**

*Elementos bajo prueba, versión, rama y commit*

| Elemento                  | Versión                         | Rama | Commit  | Observación                                                              |
|---------------------------|---------------------------------|------|---------|--------------------------------------------------------------------------|
| en-monitor-api-gateway    | Java 21 · Spring Boot 4.0.8 · Spring Cloud Gateway | main | 0b2c164 | API gateway; 69 pruebas                                  |
| en-monitor-security-service | Java 21 · Spring Boot 4.1.1   | main | de52c95 | Servicio de seguridad; 774 pruebas                                       |
| en-monitor-device-service | Java 21 · Spring Boot 4.1.1     | main | 06c1bdc | Servicio de dispositivos; 129 pruebas                                    |
| en-monitor-measurement-service | Java 21 · Spring Boot 4.1.1 | main | 8a9b544 | Servicio de mediciones; 151 pruebas                                     |
| en-monitor-home-service   | Java 21 · Spring Boot 4.1.1     | main | 6a4e396 | Servicio de hogares; 260 pruebas                                         |
| en-monitor-notification-service | Java 21 · Spring Boot 4.1.1 | main | ecdbc0a | Servicio de notificaciones; 235 pruebas                                  |
| en-monitor-alert-service  | Java 21 · Spring Boot 4.1.1     | main | 0e65430 | Servicio de alertas; 128 pruebas                                         |
| en-monitor-recommendation-service | Java 21 · Spring Boot 4.1.1 | main | aeeeec5 | Servicio de recomendaciones; 71 pruebas                              |
| energy-monitor-backend    | Java 21 · Spring Boot 4.1.1 · Spring Modulith | main | c3c6df8 | Retirado el 28/10/2026: sus módulos son los cuatro servicios anteriores; conserva el firmware (`firmware/`, versión 1.3.8) |
| energy-monitor-web        | React 19 · Vite 8               | main | 27ad9f0 | Aplicación web; 44 archivos y 253 pruebas                                |
| energy-monitor-mobile     | Expo SDK 57 · React Native 0.86 | dev  | 8b5edc0 | 78 pruebas unitarias en 8 suites; `tsc` y `expo lint` sin errores. `main` sigue en `894b65a`, 193 commits por detrás de `dev` |
| energy-monitor-infra      | Docker Compose · Mosquitto 2 · RabbitMQ 4.1 | main | ceb1543 | Compose, broker y scripts validados en CI; pruebas de punta a punta `e2e/`; reemplaza a `devices`, retirado |
| energy-monitor-docs-final | Markdown ES/EN                  | dev  | —       | Documentación final (este documento)                                     |

## Estrategia de pruebas

### Niveles de prueba

**Tabla 3**

*Niveles de prueba y su estado en el proyecto*

| N.º | Nivel                                   | Clases/suites | Métodos de prueba | Clases ejecutadas |
|-----|-----------------------------------------|---------------|-------------------|-------------------|
| 1   | Dominio                                 | 21            | 342               | 21                |
| 2   | Casos de uso y adaptadores sin contexto | 51            | 285               | 51                |
| 3   | Web (controladores)                     | 10            | 144               | 10                |
| 4   | Seguridad                               | 18            | 195               | 18                |
| 5   | Integración con MySQL real              | 28            | 133               | 28                |
| 6   | Arquitectura                            | 1             | 2                 | 1                 |
| 7   | Eventos entre módulos                   | 1             | 1                 | 1                 |
| 8   | MQTT y broker                           | 2             | 16                | 2                 |
| 9   | Frontend web (Vitest)                   | 20            | 141               | 20                |

Todos los niveles de la lista existen en el proyecto. El nivel 9 corresponde a las suites de la web. La aplicación móvil no tiene pruebas automatizadas. En la versión 1.1 se ejecutan todas las clases del servicio central; los conteos incluyen las 3 clases nuevas de notificaciones push (PushDeviceTest, ExpoPushSenderTest y PushDevicePersistenceTest).

### Técnicas y herramientas

Se combinan pruebas de caja blanca (dominio y casos de uso con puertos simulados) y de caja negra (contratos HTTP), partición de equivalencia y valores límite (por ejemplo los niveles de potencia en 500, 1500 y 3000 W, los umbrales y los 150 segundos de silencio). Las pruebas nuevas siguen el estilo AAA y las convenciones del proyecto (nombres descriptivos con @DisplayName).

**Tabla 4**

*Herramientas de prueba y versiones verificadas*

| Herramienta                                    | Versión                    | Uso                                                                  |
|------------------------------------------------|----------------------------|----------------------------------------------------------------------|
| Spring Boot / spring-boot-starter-test         | 4.1.1                      | Pruebas de contexto, MockMvc y @MockitoBean (nunca @MockBean)        |
| JUnit Jupiter                                  | 6.0.3                      | Ejecución y pruebas parametrizadas                                   |
| Mockito                                        | 5.x        | Puertos simulados (5.20.0 y 5.23.0 en el repositorio local de Maven) |
| AssertJ                                        | 3.27.7                     | Aserciones                                                           |
| Spring Modulith (spring-modulith-starter-test) | 2.1.1                      | ModularityTests: límites de módulo                                   |
| Vitest / Testing Library (React)               | 5.0.3 / 16.3.3             | Pruebas de la aplicación web (jsdom)                                 |
| Java / Node.js / Docker                        | 21.0.12 / 22.22.2 / 29.8.2 | Entorno de ejecución                                                 |
| JaCoCo                                         | No está en el pom.xml      | Sin reporte de cobertura (ver recomendaciones)                       |
| GitHub Actions                                 | runner ubuntu-latest       | Integración continua: *checks* `Tests` (gateway, servicios y backend), `Tests and build` (web) y `Validate compose and scripts` (infra) |
| Scripts de punta a punta (`infra/e2e/`)         | bash, `jq`, `openssl`, `mosquitto_pub` | Recorridos entre servicios sobre la pila local completa (versión 1.2) |

## Entorno de pruebas

Las pruebas de persistencia y las de contexto completo corren contra MySQL 8.4 real (contenedor energy-monitor-mysql), no contra H2. Liquibase es el dueño del esquema e Hibernate está en ddl-auto: validate; una base H2 o un @AutoConfigureTestDatabase(replace = ANY) cambiarían el dialecto y anularían la validación del esquema real. El costo es que esas pruebas no son herméticas: necesitan MySQL activo, el perfil test y la variable SPRING_DATASOURCE_PASSWORD; el usuario por defecto es energy_app.

**Tabla 5**

*Componentes del entorno*

| Componente       | Detalle                                                                        |
|------------------|--------------------------------------------------------------------------------|
| MySQL 8.4        | Docker, energy-monitor-mysql (healthy)                                         |
| Perfil de Spring | test (src/test/resources/application-test.yaml); recommendation.enabled: false |
| CI (GitHub Actions) | Contenedor de servicio `mysql:8.4` (usuario energy_app), `application-local.yaml` generado desde el ejemplo y par RSA desechable; el job `Tests` tarda unos 23 minutos |

## Criterios de entrada, salida, suspensión y reanudación

**Tabla 6**

*Criterios de las pruebas*

| Criterio    | Condición                                                                                                                                    |
|-------------|----------------------------------------------------------------------------------------------------------------------------------------------|
| Entrada     | Código compilable en dev; MySQL activo y SPRING_DATASOURCE_PASSWORD definida para las pruebas con contexto; npm install ejecutado en la web. |
| Salida      | Todas las pruebas automatizadas aprobadas, ningún defecto de severidad alta abierto y las clases con MySQL ejecutadas.                       |
| Suspensión  | MySQL no disponible, falta de credenciales o fallo de compilación.                                                                           |
| Reanudación | Entorno restablecido; se repite desde la clase que se detuvo.                                                                                |

## Inventario de pruebas automatizadas

Cada tabla corresponde a un módulo del servicio central tal como estaba en la versión 1.1, cuando los siete módulos vivían en `energy-monitor-backend`. Desde la versión 1.2 el código de security, device y measurement está en sus propios repositorios, que tienen sus propias suites (Tabla 2); este inventario no se rehízo por clase para la versión 1.2. «Estado» es Aprobada, Falla (con su defecto) o «Por ejecutar (MySQL)» para las clases que levantan contexto con base de datos. Las clases nuevas están marcadas con NUEVA en su descripción. En la versión 1.1 todas las clases se ejecutaron contra MySQL y aprobaron, por lo que el estado de todas es «Aprobada».

**Tabla 7**

*Pruebas del módulo security*

| Nivel                  | Clase de prueba                       | Qué verifica                                                                                  | Métodos | Requisitos que cubre                                                                    | Estado               |
|------------------------|---------------------------------------|-----------------------------------------------------------------------------------------------|---------|-----------------------------------------------------------------------------------------|----------------------|
| Dominio                | AuditLogTest                          | Reglas e invariantes de AuditLog                                                              | 9       | RF-SEC-12                                                                               | Aprobada             |
| Dominio                | PasswordResetTokenTest                | Reglas e invariantes de PasswordResetToken                                                    | 19      | RF-SEC-06                                                                               | Aprobada             |
| Dominio                | RbacTest                              | Reglas e invariantes de Rbac                                                                  | 14      | RF-SEC-09                                                                               | Aprobada             |
| Dominio                | RefreshTokenTest                      | Reglas e invariantes de RefreshToken                                                          | 41      | RF-SEC-04, RNF-SEG-02                                                                   | Aprobada             |
| Dominio                | SecuritySupportModelTest              | Reglas e invariantes de SecuritySupportModel                                                  | 28      | RF-SEC-12                                                                               | Aprobada             |
| Dominio                | UserSessionTest                       | Reglas e invariantes de UserSession                                                           | 45      | RF-SEC-04                                                                               | Aprobada             |
| Dominio                | UserTest                              | Reglas e invariantes de User                                                                  | 21      | RF-SEC-01                                                                               | Aprobada             |
| Dominio                | ValueObjectTest                       | Reglas e invariantes de ValueObject                                                           | 14      | RF-SEC-01                                                                               | Aprobada             |
| Casos de uso y adaptad | AccountLifecycleServiceTest           | Consulta de cuenta, edición de perfil y cambio de estado de la cuenta                         | 9       | RF-SEC-08                                                                               | Aprobada             |
| Casos de uso y adaptad | AuthenticateUserServiceTest           | Estado de cuenta y credenciales clasificados; todo fallo queda en el registro                 | 6       | RF-SEC-03                                                                               | Aprobada             |
| Casos de uso y adaptad | ChangePasswordServiceTest             | Cambio de contraseña con verificación de la actual y política                                 | 3       | RF-SEC-07                                                                               | Aprobada             |
| Casos de uso y adaptad | DeleteAccountServiceTest              | Eliminar la cuenta valida la contraseña y anuncia AccountDeleted                              | 2       | RF-SEC-11                                                                               | Aprobada             |
| Casos de uso y adaptad | EmailVerificationServiceTest          | Código de verificación de seis dígitos válido al menos 15 minutos                             | 6       | RF-SEC-02                                                                               | Aprobada             |
| Casos de uso y adaptad | PasswordResetServiceTest              | Recuperación: código de un solo uso, ventana de validez y presupuesto de intentos             | 16      | RF-SEC-06                                                                               | Aprobada             |
| Casos de uso y adaptad | RefreshSessionServiceTest             | Renovación: uso único, linaje, identidad de sesión y escalada de reutilización                | 20      | RF-SEC-04, RNF-SEG-01, RNF-SEG-02                                                       | Aprobada             |
| Casos de uso y adaptad | RegisterUserServiceTest               | Registro: hash de contraseña, persona y usuario, rechazo de duplicados y política             | 7       | RF-SEC-01, RF-SEC-12                                                                    | Aprobada             |
| Casos de uso y adaptad | SignInWithGoogleServiceTest           | Inicio de sesión con Google y creación de la cuenta                                           | 7       | RF-SEC-05, RNF-INT-03                                                                   | Aprobada             |
| Casos de uso y adaptad | UserAdministrationServiceTest         | Administración de cuentas: un administrador nunca actúa sobre su propia cuenta                | 18      | —                                                                                       | Aprobada             |
| Casos de uso y adaptad | UserProfileQueryServiceTest           | Nombre y correo de las cuentas conocidas                                                      | 1       | RF-SEC-08                                                                               | Aprobada             |
| Casos de uso y adaptad | UserSessionServiceTest                | Apertura de sesión con refresh token opaco y cierre                                           | 6       | RF-SEC-04                                                                               | Aprobada             |
| Web (controladores)    | AdminControllerWebTest                | Endpoints de administración /api/v1/admin con permisos RBAC y 409 al administrarse a sí mismo | 15      | —                                                                                       | Aprobada             |
| Web (controladores)    | AuthControllerWebTest                 | Contrato HTTP de autenticación, cuenta, contraseña, perfil, Google y permisos (401, 403, 429) | 66      | RF-SEC-01, RF-SEC-02, RF-SEC-03, RF-SEC-05, RF-SEC-07, RF-SEC-08, RF-SEC-09, RNF-SEG-06 | Aprobada             |
| Seguridad              | AuthRefreshEndpointWebTest            | Contrato HTTP de POST /api/v1/auth/refresh                                                    | 18      | RF-SEC-04                                                                               | Aprobada             |
| Seguridad              | BCryptPasswordHasherAdapterTest       | BCryptPasswordHasherAdapter                                                                   | 10      | RF-SEC-03                                                                               | Aprobada             |
| Seguridad              | InMemoryResetAttemptLimiterTest       | Límite de 10 intentos por IP cada 15 minutos                                                  | 8       | RNF-SEG-06                                                                              | Aprobada             |
| Seguridad              | JwtTokenIssuerTest                    | Emisión y validación de JWT RS256 (15 minutos)                                                | 18      | RF-SEC-03, RNF-SEG-01                                                                   | Aprobada             |
| Seguridad              | PemRsaKeysTest                        | Qué claves PEM se aceptan y qué mensaje se da al rechazarlas                                  | 19      | —                                                                                       | Aprobada             |
| Seguridad              | PepperedHmacResetCodeHasherTest       | PepperedHmacResetCodeHasher                                                                   | 15      | RF-SEC-06, RNF-SEG-05                                                                   | Aprobada             |
| Seguridad              | PoolSaturatedLimiterTest              | El límite por IP se aplica sin esperar una conexión del pool (429)                            | 2       | RNF-SEG-06                                                                              | Aprobada             |
| Seguridad              | RbacServiceTest                       | Asignar y revocar roles con auditoría y verificación de permisos                              | 17      | RF-SEC-09                                                                               | Aprobada             |
| Seguridad              | ResetAttemptLimiterBeforeDatabaseTest | Un canje rechazado por el límite no abre transacción                                          | 3       | RNF-SEG-06                                                                              | Aprobada             |
| Seguridad              | SecureIdentifierGeneratorAdapterTest  | SecureIdentifierGeneratorAdapter                                                              | 5       | —                                                                                       | Aprobada             |
| Seguridad              | SecureTokenGeneratorAdapterTest       | SecureTokenGeneratorAdapter                                                                   | 13      | —                                                                                       | Aprobada             |
| Seguridad              | SecurityCorsConfigurationTest         | Guardas de la política CORS sin levantar contexto                                             | 11      | RNF-SEG-03                                                                              | Aprobada             |
| Seguridad              | SecurityCorsWebTest                   | Contrato CORS: orígenes respondidos y respuesta del preflight                                 | 23      | RNF-SEG-03                                                                              | Aprobada             |
| Seguridad              | SecurityLoginFlowTest                 | Orden del intercambio de inicio de sesión y lo que rechaza                                    | 11      | RF-SEC-03                                                                               | Aprobada             |
| Seguridad              | Sha256TokenHasherTest                 | Sha256TokenHasher                                                                             | 9       | RF-SEC-04                                                                               | Aprobada             |
| Integración con MySQL  | ConfigurationPolicyPersistenceTest    | Ida y vuelta de ConfigurationPolicy contra MySQL real                                         | 6       | RF-SEC-12                                                                               | Aprobada             |
| Integración con MySQL  | LogPersistenceTest                    | Ida y vuelta de Log contra MySQL real                                                         | 5       | RF-SEC-12                                                                               | Aprobada             |
| Integración con MySQL  | PasswordPolicySeedTest                | Política de contraseñas sembrada por el changeset security-019                                | 5       | RF-SEC-12                                                                               | Aprobada             |
| Integración con MySQL  | PasswordResetTokenReservationTest     | Reserva de intentos de canje contra MySQL                                                     | 6       | RF-SEC-06                                                                               | Aprobada             |
| Integración con MySQL  | RbacAssignmentPersistenceTest         | Ida y vuelta de RbacAssignment contra MySQL real                                              | 2       | RF-SEC-09                                                                               | Aprobada             |
| Integración con MySQL  | RefreshLifecycleIntegrationTest       | Ciclo completo: abrir sesión, rotar, detectar reutilización y revocar la familia              | 15      | RF-SEC-04, RNF-SEG-02                                                                   | Aprobada             |
| Integración con MySQL  | RefreshSessionTransactionTest         | La rotación de refresh token es atómica contra la BD real                                     | 6       | RF-SEC-04                                                                               | Aprobada             |
| Integración con MySQL  | RefreshTokenPersistenceTest           | Ida y vuelta de RefreshToken contra MySQL real                                                | 10      | RF-SEC-04                                                                               | Aprobada             |
| Integración con MySQL  | SecurityTransactionBoundaryTest       | Dónde queda el límite transaccional y que Spring lo respeta                                   | 5       | —                                                                                       | Aprobada             |
| Integración con MySQL  | SecurityTransactionRollbackTest       | La transacción hace rollback de verdad                                                        | 3       | —                                                                                       | Aprobada             |
| Integración con MySQL  | TokenSessionPersistenceTest           | Ida y vuelta de TokenSession contra MySQL real                                                | 6       | RF-SEC-04                                                                               | Aprobada             |
| Integración con MySQL  | UserPersonPersistenceTest             | Ida y vuelta de UserPerson contra MySQL real                                                  | 5       | RF-SEC-01, RNF-MAN-03                                                                   | Aprobada             |

**Tabla 8**

*Pruebas del módulo home*

| Nivel                  | Clase de prueba                  | Qué verifica                                                                                  | Métodos | Requisitos que cubre                                                                      | Estado               |
|------------------------|----------------------------------|-----------------------------------------------------------------------------------------------|---------|-------------------------------------------------------------------------------------------|----------------------|
| Dominio                | HomeTest                         | Reglas e invariantes de Home                                                                  | 16      | RF-HOM-01                                                                                 | Aprobada             |
| Dominio                | HomeThresholdsTest               | Reglas e invariantes de HomeThresholds                                                        | 17      | RF-HOM-08                                                                                 | Aprobada             |
| Dominio                | HomeTypeTest                     | Reglas e invariantes de HomeType                                                              | 8       | —                                                                                         | Aprobada             |
| Dominio                | MemberPermissionsTest            | Reglas e invariantes de MemberPermissions                                                     | 3       | RF-HOM-05                                                                                 | Aprobada             |
| Dominio                | OwnerPermissionsTest             | Reglas e invariantes de OwnerPermissions                                                      | 3       | RF-HOM-05                                                                                 | Aprobada             |
| Dominio                | RoleTest                         | Reglas e invariantes de Role                                                                  | 3       | RF-HOM-05                                                                                 | Aprobada             |
| Dominio                | UserHomeTest                     | Reglas e invariantes de UserHome                                                              | 26      | RF-HOM-05                                                                                 | Aprobada             |
| Casos de uso y adaptad | CreateHomeServiceTest            | Caso de uso CreateHome: reglas de negocio y errores con puertos simulados                     | 6       | RF-HOM-01                                                                                 | Aprobada             |
| Casos de uso y adaptad | DeleteHomeServiceTest            | Solo el propietario elimina el hogar; anuncia HomeDeleted; miembro recibe 403                 | 4       | RF-HOM-09, RNF-MAN-03                                                                     | Aprobada             |
| Casos de uso y adaptad | GetHomeThresholdsServiceTest     | Caso de uso GetHomeThresholds: reglas de negocio y errores con puertos simulados              | 2       | RF-HOM-08                                                                                 | Aprobada             |
| Casos de uso y adaptad | GetHomeTypeServiceTest           | Caso de uso GetHomeType: reglas de negocio y errores con puertos simulados                    | 2       | —                                                                                         | Aprobada             |
| Casos de uso y adaptad | JoinHomeServiceTest              | Caso de uso JoinHome: reglas de negocio y errores con puertos simulados                       | 4       | RF-HOM-03                                                                                 | Aprobada             |
| Casos de uso y adaptad | LeaveHomeServiceTest             | Caso de uso LeaveHome: reglas de negocio y errores con puertos simulados                      | 4       | RF-HOM-06                                                                                 | Aprobada             |
| Casos de uso y adaptad | ListHomeTypesServiceTest         | Caso de uso ListHomeTypes: reglas de negocio y errores con puertos simulados                  | 2       | —                                                                                         | Aprobada             |
| Casos de uso y adaptad | ListHomesServiceTest             | Caso de uso ListHomes: reglas de negocio y errores con puertos simulados                      | 5       | RF-HOM-02                                                                                 | Aprobada             |
| Casos de uso y adaptad | ListMembersServiceTest           | Caso de uso ListMembers: reglas de negocio y errores con puertos simulados                    | 4       | RF-HOM-04                                                                                 | Aprobada             |
| Casos de uso y adaptad | RemoveDeletedAccountServiceTest  | Cuenta eliminada: borra los hogares propios y rechaza si es único propietario con miembros    | 2       | RF-SEC-11                                                                                 | Aprobada             |
| Casos de uso y adaptad | RemoveUserServiceTest            | Caso de uso RemoveUser: reglas de negocio y errores con puertos simulados                     | 5       | RF-HOM-06                                                                                 | Aprobada             |
| Casos de uso y adaptad | ToggleFavoriteServiceTest        | Caso de uso ToggleFavorite: reglas de negocio y errores con puertos simulados                 | 2       | RF-HOM-07                                                                                 | Aprobada             |
| Casos de uso y adaptad | TransferHomeOwnershipServiceTest | Transferencia de propiedad: roles intercambiados, un solo propietario, destinos inválidos     | 6       | RF-HOM-10                                                                                 | Aprobada             |
| Casos de uso y adaptad | UpdateHomeThresholdsServiceTest  | Caso de uso UpdateHomeThresholds: reglas de negocio y errores con puertos simulados           | 7       | RF-HOM-08                                                                                 | Aprobada             |
| Web (controladores)    | HomeControllerTest               | Endpoints de hogares: crear, listar, unirse, favorito, expulsar, salir, eliminar y transferir | 26      | RF-HOM-01, RF-HOM-02, RF-HOM-03, RF-HOM-04, RF-HOM-06, RF-HOM-07, RF-HOM-08, RF-HOM-09, … | Aprobada             |
| Web (controladores)    | HomeExceptionHandlerTest         | Respuesta del módulo cuando la solicitud no puede atribuirse a un usuario                     | 2       | RF-SEC-10                                                                                 | Aprobada             |
| Integración con MySQL  | HomePersistenceTest              | Ida y vuelta de Home contra MySQL real                                                        | 6       | RF-HOM-01, RNF-MAN-03                                                                     | Aprobada             |
| Integración con MySQL  | HomeThresholdsPersistenceTest    | Ida y vuelta de HomeThresholds contra MySQL real                                              | 3       | RF-HOM-08                                                                                 | Aprobada             |
| Integración con MySQL  | HomeTypePersistenceTest          | Ida y vuelta de HomeType contra MySQL real                                                    | 3       | —                                                                                         | Aprobada             |
| Integración con MySQL  | HomeTypeSeedTest                 | El catálogo contiene exactamente los seis tipos de hogar sembrados                            | 1       | RF-HOM-01                                                                                 | Aprobada             |
| Integración con MySQL  | UserHomePersistenceTest          | Ida y vuelta de UserHome contra MySQL real                                                    | 7       | RF-HOM-05                                                                                 | Aprobada             |

**Tabla 9**

*Pruebas del módulo device*

| Nivel                  | Clase de prueba               | Qué verifica                                                                                                      | Métodos | Requisitos que cubre                                  | Estado   |
|------------------------|-------------------------------|-------------------------------------------------------------------------------------------------------------------|---------|-------------------------------------------------------|----------|
| Casos de uso y adaptad | BrokerAccessServiceTest       | Autenticación del broker con la clave en claro contra el hash y autorización de tópicos                           | 8       | RF-DEV-07, RNF-SEG-09                                 | Aprobada |
| Casos de uso y adaptad | DeviceConnectivityServiceTest | Estado en línea y sin conexión, bitácora de cambios y 150 s de silencio                                           | 6       | RF-ALE-02, RF-MED-05, RF-MED-06, RNF-DIS-02           | Aprobada |
| Casos de uso y adaptad | HomeDeviceServiceTest         | Vincular, listar, editar, reemitir credenciales y desvincular con reglas de propietario                           | 9       | RF-DEV-02, RF-DEV-03, RF-DEV-04, RF-DEV-05, RF-DEV-06 | Aprobada |
| Web (controladores)    | HomeDeviceControllerTest      | NUEVA. Endpoints de dispositivos del hogar: vincular, editar, credenciales, desvincular y errores 400/403/404/409 | 10      | RF-DEV-02, RF-DEV-03, RF-DEV-04, RF-DEV-05, RF-DEV-06 | Aprobada |
| Seguridad              | ApiKeyHashTest                | Hash SHA-256 de la api_key del dispositivo                                                                        | 3       | RF-DEV-07, RNF-SEG-09                                 | Aprobada |
| MQTT y broker          | BrokerAuthControllerTest      | NUEVA. /internal/mqtt/auth/user y /acl: 200 concede, 403 deniega, códigos de acceso 1 a 4                         | 8       | RF-DEV-07                                             | Aprobada |

**Tabla 10**

*Pruebas del módulo measurement*

| Nivel                  | Clase de prueba                     | Qué verifica                                                                                        | Métodos | Requisitos que cubre            | Estado               |
|------------------------|-------------------------------------|-----------------------------------------------------------------------------------------------------|---------|---------------------------------|----------------------|
| Dominio                | ConsumptionLevelTest                | Niveles LOW, MEDIUM, HIGH y CRITICAL y sus límites                                                  | 14      | RF-MED-09                       | Aprobada             |
| Dominio                | MeasurementTest                     | Reglas e invariantes de Measurement                                                                 | 12      | RF-MED-02                       | Aprobada             |
| Casos de uso y adaptad | GetConsumptionStatisticsServiceTest | Caso de uso GetConsumptionStatistics: reglas de negocio y errores con puertos simulados             | 5       | RF-MED-07                       | Aprobada             |
| Casos de uso y adaptad | GetLatestMeasurementServiceTest     | Caso de uso GetLatestMeasurement: reglas de negocio y errores con puertos simulados                 | 2       | RF-MED-07                       | Aprobada             |
| Casos de uso y adaptad | HomeConsumptionUseCaseTest          | Resumen e historial de consumo del hogar                                                            | 8       | RF-MED-08                       | Aprobada             |
| Casos de uso y adaptad | ListConsumptionLevelsServiceTest    | Caso de uso ListConsumptionLevels: reglas de negocio y errores con puertos simulados                | 2       | RF-MED-09                       | Aprobada             |
| Casos de uso y adaptad | ListMeasurementsServiceTest         | Caso de uso ListMeasurements: reglas de negocio y errores con puertos simulados                     | 4       | RF-MED-07                       | Aprobada             |
| Casos de uso y adaptad | RegisterMeasurementServiceTest      | Caso de uso RegisterMeasurement: reglas de negocio y errores con puertos simulados                  | 4       | RF-MED-02                       | Aprobada             |
| Web (controladores)    | ConsumptionLevelControllerTest      | Códigos HTTP y cuerpo de ConsumptionLevelController                                                 | 1       | RF-MED-09                       | Aprobada             |
| Web (controladores)    | MeasurementControllerTest           | Códigos HTTP y cuerpo de MeasurementController                                                      | 5       | RF-MED-07                       | Aprobada             |
| Integración con MySQL  | ConsumptionLevelPersistenceTest     | Ida y vuelta de ConsumptionLevel contra MySQL real                                                  | 4       | RF-MED-09                       | Aprobada             |
| Integración con MySQL  | MeasurementPersistenceTest          | Ida y vuelta de Measurement contra MySQL real                                                       | 4       | RF-MED-02                       | Aprobada             |
| MQTT y broker          | TelemetryMqttConsumerTest           | NUEVA. Ingesta de telemetría: el tópico identifica al dispositivo y se descartan mensajes inválidos | 8       | RF-DEV-07, RF-MED-02, RF-MED-03 | Aprobada             |

**Tabla 11**

*Pruebas del módulo alert*

| Nivel                  | Clase de prueba                      | Qué verifica                                                                       | Métodos | Requisitos que cubre | Estado               |
|------------------------|--------------------------------------|------------------------------------------------------------------------------------|---------|----------------------|----------------------|
| Dominio                | AlertTest                            | Reglas e invariantes de Alert                                                      | 16      | RF-ALE-03, RF-ALE-04 | Aprobada             |
| Casos de uso y adaptad | GetAlertServiceTest                  | Caso de uso GetAlert: reglas de negocio y errores con puertos simulados            | 2       | RF-ALE-05            | Aprobada             |
| Casos de uso y adaptad | ListAlertsServiceTest                | Caso de uso ListAlerts: reglas de negocio y errores con puertos simulados          | 3       | RF-ALE-05            | Aprobada             |
| Casos de uso y adaptad | RegisterDeviceLinkedAlertServiceTest | Aviso de dispositivo vinculado con la primera lectura                              | 4       | RF-ALE-01            | Aprobada             |
| Casos de uso y adaptad | RegisterLimitAlertServiceTest        | Alertas de límite diario o mensual                                                 | 4       | RF-ALE-04            | Aprobada             |
| Casos de uso y adaptad | RegisterThresholdAlertServiceTest    | Alertas de consumo alto o crítico por nivel de potencia                            | 7       | RF-ALE-03            | Aprobada             |
| Casos de uso y adaptad | ResolveAlertServiceTest              | Caso de uso ResolveAlert: reglas de negocio y errores con puertos simulados        | 4       | RF-ALE-05            | Aprobada             |
| Web (controladores)    | AlertControllerTest                  | Endpoints de alertas: listar, consultar, leer, eliminar y 404                      | 7       | RF-ALE-05            | Aprobada             |
| Integración con MySQL  | AlertPersistenceTest                 | Ida y vuelta de Alert contra MySQL real                                            | 4       | RF-ALE-05            | Aprobada             |
| Eventos entre módulos  | MeasurementRecordedListenerTest      | El evento MeasurementRecorded dispara el registro de alertas de umbral y de límite | 1       | RF-ALE-03, RF-ALE-06 | Aprobada             |

**Tabla 12**

*Pruebas del módulo recommendation*

| Nivel                  | Clase de prueba                    | Qué verifica                                                                                          | Métodos | Requisitos que cubre                                                                   | Estado               |
|------------------------|------------------------------------|-------------------------------------------------------------------------------------------------------|---------|----------------------------------------------------------------------------------------|----------------------|
| Dominio                | RecommendationRulesTest            | Cinco reglas de ahorro: hora punta, reposo, comparación semanal, proyección y aumento por dispositivo | 8       | RF-REC-01, RF-REC-02, RF-REC-03, RF-REC-04, RF-REC-05, RF-REC-06, RF-REC-07, RF-REC-09 | Aprobada             |
| Casos de uso y adaptad | EvaluateRecommendationsServiceTest | Evaluación de reglas por hogar y no repetir en 7 días                                                 | 4       | RF-REC-01, RF-REC-07                                                                   | Aprobada             |
| Casos de uso y adaptad | RecommendationInboxServiceTest     | Caso de uso RecommendationInbox: reglas de negocio y errores con puertos simulados                    | 4       | RF-REC-08                                                                              | Aprobada             |
| Web (controladores)    | RecommendationControllerTest       | NUEVA. Bandeja de recomendaciones: listar, leer, eliminar, 404 y 409                                  | 7       | RF-REC-08                                                                              | Aprobada             |
| Integración con MySQL  | RecommendationPersistenceTest      | Ida y vuelta de Recommendation contra MySQL real                                                      | 3       | RF-REC-08                                                                              | Aprobada             |

**Tabla 13**

*Pruebas del módulo notification*

| Nivel                  | Clase de prueba                       | Qué verifica                                                                              | Métodos | Requisitos que cubre                                   | Estado               |
|------------------------|---------------------------------------|-------------------------------------------------------------------------------------------|---------|--------------------------------------------------------|----------------------|
| Dominio                | NotificationTest                      | Reglas e invariantes de Notification                                                      | 20      | RF-NOT-01, RF-NOT-05                                   | Aprobada             |
| Casos de uso y adaptad | AbandonedDeliverySweeperTest          | Barrido de entregas abandonadas al iniciar                                                | 4       | RF-NOT-05                                              | Aprobada             |
| Casos de uso y adaptad | AdminNotificationsTest                | Notificaciones de administrador: quién recibe cada evento y lecturas acotadas al llamante | 5       | —                                                      | Aprobada             |
| Casos de uso y adaptad | EmailPasswordResetDeliveryAdapterTest | Envío del código de recuperación; nada sale si la transacción hace rollback               | 5       | RF-NOT-06, RF-SEC-06                                   | Aprobada             |
| Casos de uso y adaptad | EmailSenderTest                       | Registro del intento antes del envío y envío del correo                                   | 9       | RF-NOT-01, RF-NOT-05, RF-NOT-06, RF-SEC-02, RNF-INT-02 | Aprobada             |
| Casos de uso y adaptad | NotifyAlertTest                       | Entrega de alertas y recomendaciones a los miembros por correo y push según preferencias  | 8       | RF-NOT-01, RF-NOT-02, RF-NOT-07                        | Aprobada             |
| Casos de uso y adaptad | SendPasswordResetEmailTest            | Mensaje de recuperación de contraseña y su registro                                       | 11      | RF-NOT-06, RF-SEC-06                                   | Aprobada             |
| Casos de uso y adaptad | WebPushSenderTest                     | Cifrado del mensaje push (RFC 8291) como lo descifra un navegador                         | 2       | RF-NOT-01, RF-NOT-04                                   | Aprobada             |
| Dominio                | PushDeviceTest                        | NUEVA (1.1). Invariantes del dispositivo móvil registrado para push                       | 5       | RF-NOT-01                                              | Aprobada             |
| Casos de uso y adaptad | ExpoPushSenderTest                    | NUEVA (1.1). Interpretación de la respuesta del relé de push de Expo                      | 9       | RF-NOT-01                                              | Aprobada             |
| Integración con MySQL  | PushDevicePersistenceTest             | NUEVA (1.1). Registro y baja de instalaciones móviles contra el esquema real              | 7       | RF-NOT-01                                              | Aprobada             |
| Web (controladores)    | AdminNotificationsControllerWebTest   | Endpoints de notificaciones de administrador                                              | 5       | —                                                      | Aprobada             |
| Integración con MySQL  | NotificationPersistenceTest           | Bitácora de entregas contra el esquema real                                               | 4       | RF-NOT-05                                              | Aprobada             |

**Tabla 14**

*Pruebas del módulo transversales*

| Nivel                  | Clase de prueba                      | Qué verifica                                                                                 | Métodos | Requisitos que cubre  | Estado               |
|------------------------|--------------------------------------|----------------------------------------------------------------------------------------------|---------|-----------------------|----------------------|
| Seguridad              | AdminRbacIntegrationTest             | Rol ADMIN sembrado, códigos USER.READ, USER.MANAGE y ROLE.MANAGE y búsquedas del directorio  | 6       | RF-SEC-09             | Aprobada             |
| Seguridad              | HomeCallerIdentityIntegrationTest    | Identidad de quien llama a /homes por el filtro real y un JWT RS256; X-User-Id se ignora     | 4       | RF-SEC-10             | Aprobada             |
| Integración con MySQL  | AccountDeletionIntegrationTest       | Borrado de cuenta entre security y home contra el esquema real                               | 3       | RF-SEC-11             | Aprobada             |
| Integración con MySQL  | DeviceApiKeyStorageIntegrationTest   | Solo el hash SHA-256 de la api_key llega a la tabla; el acceso al broker sigue funcionando   | 3       | RF-DEV-02, RNF-SEG-09 | Aprobada             |
| Integración con MySQL  | EnergyMonitorBackendApplicationTests | Arranque del contexto completo de la aplicación                                              | 1       | —                     | Aprobada             |
| Integración con MySQL  | HomeDeletionIntegrationTest          | Eliminar un hogar borra membresías y umbrales y desvincula dispositivos; un miembro no puede | 2       | RF-DEV-06, RF-HOM-09  | Aprobada             |
| Integración con MySQL  | HomeOwnershipTransferIntegrationTest | Intercambio atómico de roles OWNER y MEMBER contra el esquema real                           | 4       | RF-HOM-05, RF-HOM-10  | Aprobada             |
| Arquitectura           | ModularityTests                      | ApplicationModules.verify(): los límites de los siete módulos se respetan                    | 2       | RNF-MAN-01            | Aprobada             |

**Tabla 15**

*Pruebas de la aplicación web (Vitest)*

| Suite                                                   | Pruebas | Requisitos que cubre                       | Estado   |
|---------------------------------------------------------|---------|--------------------------------------------|----------|
| components/layout/MainLayout/Sidebar/Sidebar.test.jsx   | 2       | RF-CLI-01                                  | Aprobada |
| context/HomeContext.test.jsx                            | 11      | —                                          | Aprobada |
| features/Account/Account.test.jsx                       | 6       | RF-CLI-01, RF-SEC-08, RF-SEC-11            | Aprobada |
| features/Account/AccountDeleteError.test.jsx            | 1       | RF-SEC-11                                  | Aprobada |
| features/Admin/AdminNotifications.test.jsx              | 5       | —                                          | Aprobada |
| features/Admin/AdminUsers.test.jsx                      | 9       | —                                          | Aprobada |
| features/DetailHome/Home/DeleteHome/DeleteHome.test.jsx | 5       | RF-CLI-01, RF-HOM-09                       | Aprobada |
| features/DetailHome/Users/Users.test.jsx                | 12      | RF-CLI-01, RF-HOM-04, RF-HOM-06, RF-HOM-10 | Aprobada |
| features/auth/components/RPasswordForm/recover.test.jsx | 4       | RF-CLI-01                                  | Aprobada |
| features/auth/components/RegisterForm/verify.test.jsx   | 4       | RF-CLI-01                                  | Aprobada |
| features/dashboard/pages/DashboardPage.test.jsx         | 5       | RF-CLI-01, RF-HOM-02                       | Aprobada |
| features/shared/homeTypes.test.js                       | 6       | —                                          | Aprobada |
| routes/RequirePermission.test.jsx                       | 3       | —                                          | Aprobada |
| routes/UserOnlyRoute.test.jsx                           | 4       | RF-CLI-01                                  | Aprobada |
| services/admin/adminApi.test.js                         | 4       | —                                          | Aprobada |
| services/auth/authApi.test.js                           | 4       | —                                          | Aprobada |
| services/home/homeApi.test.js                           | 15      | —                                          | Aprobada |
| services/http/errorMessages.test.js                     | 27      | —                                          | Aprobada |
| services/http/httpClient.refresh.test.js                | 3       | —                                          | Aprobada |
| services/http/httpClient.test.js                        | 11      | —                                          | Aprobada |

Las pruebas automatizadas de la aplicación móvil no existen: el repositorio no tiene archivos de prueba ni script de pruebas (solo expo lint). Es una brecha declarada. Desde la versión 1.3 se ejecutan la verificación de tipos (`tsc`) y `expo lint`, y se define un protocolo de pruebas manuales (Tabla 38).

## Matriz de trazabilidad requisito → prueba

Los 107 requisitos de la especificación se cruzan con las clases de prueba. «Cobertura» distingue lo que se ejecutó de lo que depende de MySQL. Resultado en la versión 1.0: 66 con prueba automatizada ejecutada, 1 con pruebas automatizadas que dependen de MySQL y 40 sin cobertura. Resultado en la versión 1.1: 68 con prueba automatizada ejecutada (RNF-MAN-01 ya se ejecuta y RF-NOT-02 queda cubierto por NotifyAlertTest) y 39 sin cobertura.

**Tabla 16**

*Trazabilidad de los requisitos RF-SEC*

| Requisito | Descripción                                                                                               | Pruebas automatizadas                                                                                                                                                                      | Cobertura    |
|-----------|-----------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------|
| RF-SEC-01 | El sistema debe permitir solicitar el registro de una cuenta con datos personales, correo y contraseña…   | RegisterUserServiceTest, MistypedEmailDomainsTest, AuthControllerWebTest, UserPersonPersistenceTest, UserTest, ValueObjectTest                                                            | Automatizada |
| RF-SEC-02 | El sistema debe enviar al correo indicado un código de verificación de seis dígitos y crear la cuenta…    | EmailVerificationServiceTest, EmailSenderTest, AuthControllerWebTest                                                                                                                       | Automatizada |
| RF-SEC-03 | El sistema debe permitir iniciar sesión de forma segura con correo y contraseña                           | AuthenticateUserServiceTest, SecurityLoginFlowTest, AuthControllerWebTest, JwtTokenIssuerTest, BCryptPasswordHasherAdapterTest                                                             | Automatizada |
| RF-SEC-04 | El sistema debe mantener la sesión del usuario sin pedirle sus credenciales continuamente, permitirle…    | RefreshSessionServiceTest, UserSessionServiceTest, UserSessionTest, RefreshTokenTest, AuthRefreshEndpointWebTest, RefreshLifecycleIntegrationTest, …                                       | Automatizada |
| RF-SEC-05 | El sistema debe permitir registrarse e iniciar sesión con una cuenta de Google, con aceptación de términos | SignInWithGoogleServiceTest, AuthControllerWebTest                                                                                                                                         | Automatizada |
| RF-SEC-06 | El sistema debe permitir recuperar la contraseña con un código de seis dígitos enviado al correo, válido… | PasswordResetServiceTest, PasswordResetTokenTest, PepperedHmacResetCodeHasherTest, SendPasswordResetEmailTest, EmailPasswordResetDeliveryAdapterTest, PasswordResetTokenReservationTest, … | Automatizada |
| RF-SEC-07 | El sistema debe permitir al usuario con sesión iniciada cambiar su contraseña (no a las cuentas de Google) | ChangePasswordServiceTest, AuthControllerWebTest, account-deletion-flow.sh                                                                                                                 | Automatizada |
| RF-SEC-08 | El sistema debe permitir consultar los datos de la cuenta y actualizar el perfil (nombre y apellido por separado) | AccountLifecycleServiceTest, UserProfileQueryServiceTest, AuthControllerWebTest, Account.test.jsx                                                                          | Automatizada |
| RF-SEC-09 | El sistema debe permitir conocer los permisos del usuario                                                 | RbacServiceTest, RbacTest, RbacAssignmentPersistenceTest, AdminRbacIntegrationTest, AuthControllerWebTest                                                                                  | Automatizada |
| RF-SEC-10 | El sistema debe identificar al usuario únicamente a partir de su sesión y no debe aceptar identidades…    | HomeCallerIdentityIntegrationTest, HomeExceptionHandlerTest                                                                                                                                | Automatizada |
| RF-SEC-11 | El sistema debe permitir al usuario eliminar su cuenta confirmando su contraseña o, con Google, un código  | DeleteAccountServiceTest, RemoveDeletedAccountServiceTest, AccountDeletionIntegrationTest, AccountDeleteError.test.jsx, Account.test.jsx, account-deletion-flow.sh                         | Automatizada |
| RF-SEC-12 | El sistema debe aplicar una política de contraseñas (longitud mínima de 8 y máxima de 64 caracteres) y…   | PasswordPolicySeedTest, ConfigurationPolicyPersistenceTest, LogPersistenceTest, AuditLogTest, SecuritySupportModelTest, RegisterUserServiceTest                                            | Automatizada |
| RF-SEC-13 | El sistema no debe permitir el acceso a una cuenta que no haya aceptado los términos y condiciones…       | RegisterUserServiceTest, SignInWithGoogleServiceTest, AuthControllerWebTest, SecurityLoginFlowTest, UserPersonPersistenceTest, registerSchema.test.ts, registerPayload.test.ts, termsConsent.test.ts (móvil)                                                              | Automatizada |

**Tabla 17**

*Trazabilidad de los requisitos RF-HOM*

| Requisito | Descripción                                                                                                | Pruebas automatizadas                                                                                                                | Cobertura     |
|-----------|------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|---------------|
| RF-HOM-01 | El sistema debe permitir crear un hogar eligiendo un tipo de hogar de un catálogo                          | CreateHomeServiceTest, HomeControllerTest, HomeTest, HomePersistenceTest, HomeTypeSeedTest                                           | Automatizada  |
| RF-HOM-02 | El sistema debe mostrar al usuario los hogares a los que pertenece                                         | ListHomesServiceTest, HomeControllerTest, DashboardPage.test.jsx                                                                     | Automatizada  |
| RF-HOM-03 | El sistema debe permitir unirse a un hogar con su código de acceso, con el rol de miembro                  | JoinHomeServiceTest, HomeControllerTest                                                                                              | Automatizada  |
| RF-HOM-04 | El sistema debe mostrar los miembros de un hogar                                                           | ListMembersServiceTest, HomeControllerTest, Users.test.jsx                                                                           | Automatizada  |
| RF-HOM-05 | El sistema debe distinguir los permisos generales de los usuarios de los roles de propietario y miembro…   | UserHomeTest, OwnerPermissionsTest, MemberPermissionsTest, RoleTest, UserHomePersistenceTest, HomeOwnershipTransferIntegrationTest   | Automatizada  |
| RF-HOM-06 | El propietario debe poder retirar a un miembro y cualquier miembro debe poder abandonar el hogar           | RemoveUserServiceTest, LeaveHomeServiceTest, HomeControllerTest, Users.test.jsx                                                      | Automatizada  |
| RF-HOM-07 | El usuario debe poder marcar un hogar como favorito                                                        | ToggleFavoriteServiceTest, HomeControllerTest                                                                                        | Automatizada  |
| RF-HOM-08 | El sistema debe permitir consultar, actualizar y restablecer a los valores del sistema los umbrales de un… | GetHomeThresholdsServiceTest, UpdateHomeThresholdsServiceTest, HomeThresholdsTest, HomeThresholdsPersistenceTest, HomeControllerTest | Automatizada  |
| RF-HOM-09 | El propietario debe poder eliminar el hogar mediante eliminación lógica                                    | DeleteHomeServiceTest, HomeDeletionIntegrationTest, HomeControllerTest, DeleteHome.test.jsx                                          | Automatizada  |
| RF-HOM-10 | El propietario debe poder transferir la propiedad del hogar a otro miembro                                 | TransferHomeOwnershipServiceTest, HomeOwnershipTransferIntegrationTest, HomeControllerTest, Users.test.jsx                           | Automatizada  |
| RF-HOM-11 | Pendiente: limitar el historial de un dispositivo al periodo de vinculación                                | —                                                                                                                                    | Sin cobertura |
| RF-HOM-12 | El sistema debe mostrar a los miembros de un hogar la foto de perfil de cada uno y permitir ampliarla | UserProfileQueryServiceTest, HttpUserDirectoryAdapterTest, HomeControllerTest, Users.test.jsx | Automatizada |

**Tabla 18**

*Trazabilidad de los requisitos RF-DEV*

| Requisito | Descripción                                                                                               | Pruebas automatizadas                                                                        | Cobertura     |
|-----------|-----------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|---------------|
| RF-DEV-01 | El sistema debe ofrecer un catálogo de tipos de electrodoméstico                                          | —                                                                                            | Sin cobertura |
| RF-DEV-02 | El propietario debe poder vincular un dispositivo de medición a su hogar                                  | HomeDeviceServiceTest, HomeDeviceControllerTest, DeviceApiKeyStorageIntegrationTest          | Automatizada  |
| RF-DEV-03 | Los miembros deben poder ver los dispositivos de un hogar                                                 | HomeDeviceServiceTest, HomeDeviceControllerTest                                              | Automatizada  |
| RF-DEV-04 | El propietario debe poder editar el electrodoméstico, el nombre y la habitación de un dispositivo sin…    | HomeDeviceServiceTest, HomeDeviceControllerTest                                              | Automatizada  |
| RF-DEV-05 | El propietario debe poder cambiar la red Wi-Fi de un dispositivo ya vinculado                             | HomeDeviceServiceTest, HomeDeviceControllerTest                                              | Automatizada  |
| RF-DEV-06 | El propietario debe poder desvincular un dispositivo                                                      | HomeDeviceServiceTest, HomeDeviceControllerTest, HomeDeletionIntegrationTest                 | Automatizada  |
| RF-DEV-07 | El sistema solo debe aceptar datos de dispositivos vinculados, y cada dispositivo solo puede enviar sus…  | BrokerAccessServiceTest, BrokerAuthControllerTest, ApiKeyHashTest, TelemetryMqttConsumerTest | Automatizada  |
| RF-DEV-08 | La vinculación debe poder hacerse desde la aplicación web por Bluetooth: la aplicación muestra las redes… | —                                                                                            | Sin cobertura |
| RF-DEV-09 | El dispositivo debe reconectarse sin intervención del usuario ante la pérdida de Wi-Fi o una falla…       | —                                                                                            | Sin cobertura |

**Tabla 19**

*Trazabilidad de los requisitos RF-MED*

| Requisito | Descripción                                                                                                   | Pruebas automatizadas                                                                                                        | Cobertura     |
|-----------|---------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|---------------|
| RF-MED-01 | El dispositivo debe enviar cada 60 segundos el voltaje (V), la corriente (A), la potencia activa (W) y la…    | —                                                                                                                            | Sin cobertura |
| RF-MED-02 | El sistema debe guardar cada lectura con la fecha y hora en que se tomó. Las muestras acumuladas sin…         | RegisterMeasurementServiceTest, MeasurementTest, MeasurementPersistenceTest, TelemetryMqttConsumerTest                       | Automatizada  |
| RF-MED-03 | El sistema debe descartar los mensajes que declaren pertenecer a un dispositivo distinto del que los envía    | TelemetryMqttConsumerTest                                                                                                    | Automatizada  |
| RF-MED-04 | El dispositivo debe informar su estado cada cinco minutos y al conectarse: si está en línea o sin…            | —                                                                                                                            | Sin cobertura |
| RF-MED-05 | El sistema debe conservar los cambios de estado de cada dispositivo                                           | DeviceConnectivityServiceTest                                                                                                | Automatizada  |
| RF-MED-06 | El sistema debe marcar un dispositivo como sin conexión cuando el dispositivo avisa que se desconecta o…      | DeviceConnectivityServiceTest                                                                                                | Automatizada  |
| RF-MED-07 | El sistema debe permitir consultar las mediciones, la última medición y estadísticas de consumo               | ListMeasurementsServiceTest, GetLatestMeasurementServiceTest, GetConsumptionStatisticsServiceTest, MeasurementControllerTest | Automatizada  |
| RF-MED-08 | El sistema debe ofrecer el resumen y el historial de consumo de un hogar, con selección de un rango de fechas | HomeConsumptionUseCaseTest                                                                                                   | Automatizada  |
| RF-MED-09 | El sistema debe clasificar la potencia en los niveles LOW (0 a 500 W), MEDIUM (500 a 1500 W), HIGH (1500 a…   | ConsumptionLevelTest, ListConsumptionLevelsServiceTest, ConsumptionLevelControllerTest, ConsumptionLevelPersistenceTest      | Automatizada  |

**Tabla 20**

*Trazabilidad de los requisitos RF-ALE*

| Requisito | Descripción                                                                                                | Pruebas automatizadas                                                                                          | Cobertura    |
|-----------|------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------|--------------|
| RF-ALE-01 | El sistema debe generar un aviso de dispositivo vinculado con la primera lectura que envía un dispositivo… | RegisterDeviceLinkedAlertServiceTest                                                                           | Automatizada |
| RF-ALE-02 | El sistema debe generar una alerta de desconexión cuando un dispositivo avisa que se desconecta o pasan…   | DeviceConnectivityServiceTest                                                                                  | Automatizada |
| RF-ALE-03 | El sistema debe generar una alerta de consumo alto o crítico cuando una lectura cae en el nivel HIGH o…    | RegisterThresholdAlertServiceTest, AlertTest, MeasurementRecordedListenerTest                                  | Automatizada |
| RF-ALE-04 | El sistema debe generar una alerta de límite diario o mensual cuando la energía del hogar desde el inicio… | RegisterLimitAlertServiceTest, AlertTest                                                                       | Automatizada |
| RF-ALE-05 | El sistema debe permitir consultar las alertas, ver el detalle de una, marcarla como leída y eliminarla    | ListAlertsServiceTest, GetAlertServiceTest, ResolveAlertServiceTest, AlertControllerTest, AlertPersistenceTest | Automatizada |
| RF-ALE-06 | El sistema debe generar las alertas de consumo a partir de cada nueva lectura y las de desconexión a…      | MeasurementRecordedListenerTest                                                                                | Automatizada |
| RF-ALE-07 | El sistema debe generar una alerta de consumo inusual cuando una lectura es de al menos 200 W y 2,5 veces la potencia habitual del dispositivo… | RegisterAnomalyAlertServiceTest, MeasurementRecordedConsumerTest, AlertMessagesAnomalyTest | Automatizada |

**Tabla 21**

*Trazabilidad de los requisitos RF-REC*

| Requisito | Descripción                                                                                                 | Pruebas automatizadas                                                                       | Cobertura    |
|-----------|-------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|--------------|
| RF-REC-01 | El sistema debe evaluar cada hora la energía por hora de los hogares cuyos dispositivos reportaron en la…   | EvaluateRecommendationsServiceTest, RecommendationRulesTest                                 | Automatizada |
| RF-REC-02 | Regla de hora punta: al menos el 40 % de la energía de los últimos 7 días (mínimo 2 kWh) se consumió entre… | RecommendationRulesTest                                                                     | Automatizada |
| RF-REC-03 | Regla de consumo en reposo: un dispositivo que no sea un refrigerador consumió en promedio 15 W o más…      | RecommendationRulesTest                                                                     | Automatizada |
| RF-REC-04 | Regla de comparación semanal: los últimos 7 días superan en 25 % o más a los 7 anteriores (mínimo 1 kWh)    | RecommendationRulesTest                                                                     | Automatizada |
| RF-REC-05 | Regla de proyección de límite: al ritmo actual, el hogar superará su límite diario o mensual, el que haya…  | RecommendationRulesTest                                                                     | Automatizada |
| RF-REC-06 | Regla de aumento por dispositivo: la potencia media de un dispositivo en los últimos 7 días es 30 % o más…  | RecommendationRulesTest                                                                     | Automatizada |
| RF-REC-07 | La misma recomendación (hogar, tipo y dispositivo) no debe repetirse dentro de 7 días, aunque se haya…      | EvaluateRecommendationsServiceTest, RecommendationRulesTest                                 | Automatizada |
| RF-REC-08 | El sistema debe permitir consultar las recomendaciones de un hogar, marcarlas como leídas y eliminar las…   | RecommendationInboxServiceTest, RecommendationControllerTest, RecommendationPersistenceTest | Automatizada |
| RF-REC-09 | Las reglas requieren historial: la hora punta y el consumo en reposo funcionan tras unos días de lecturas,… | RecommendationRulesTest                                                                     | Automatizada |

**Tabla 22**

*Trazabilidad de los requisitos RF-NOT*

| Requisito | Descripción                                                                                                 | Pruebas automatizadas                                                                        | Cobertura     |
|-----------|-------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|---------------|
| RF-NOT-01 | El sistema debe entregar cada alerta a todos los miembros del hogar por correo y por notificación del…      | NotifyAlertTest, NotificationTest, WebPushSenderTest, EmailSenderTest, ExpoPushSenderTest, PushDeviceTest | Automatizada  |
| RF-NOT-02 | El sistema debe entregar cada recomendación nueva por los mismos canales                                    | NotifyAlertTest                                                                              | Automatizada  |
| RF-NOT-03 | El usuario debe poder consultar y modificar sus preferencias de notificación                                | —                                                                                            | Sin cobertura |
| RF-NOT-04 | El usuario debe poder activar y desactivar las notificaciones del navegador                                 | WebPushSenderTest                                                                            | Automatizada  |
| RF-NOT-05 | Cada intento de entrega debe quedar registrado como enviado o fallido, sin guardar el contenido del mensaje | NotificationTest, NotificationPersistenceTest, AbandonedDeliverySweeperTest, EmailSenderTest | Automatizada  |
| RF-NOT-06 | El sistema debe enviar por correo el código de verificación y el de recuperación de contraseña              | SendPasswordResetEmailTest, EmailPasswordResetDeliveryAdapterTest, EmailSenderTest           | Automatizada  |
| RF-NOT-07 | Cada correo de alerta debe incluir un enlace a la aplicación web                                            | NotifyAlertTest                                                                              | Automatizada  |

**Tabla 23**

*Trazabilidad de los requisitos RF-CLI*

| Requisito | Descripción                                                                                                 | Pruebas automatizadas                                                                                               | Cobertura     |
|-----------|-------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|---------------|
| RF-CLI-01 | La aplicación web debe ofrecer registro, inicio de sesión (incluido Google), recuperación de contraseña,…   | Account.test.jsx, DashboardPage.test.jsx, DeleteHome.test.jsx, Users.test.jsx, verify.test.jsx, recover.test.jsx, … | Automatizada  |
| RF-CLI-02 | La aplicación web debe mostrar una insignia con las alertas pendientes, un contador en el título de la…     | —                                                                                                                   | Sin cobertura |
| RF-CLI-03 | La aplicación web debe vincular dispositivos por Bluetooth y permitir notificaciones del navegador, que…    | —                                                                                                                   | Sin cobertura |
| RF-CLI-04 | La aplicación móvil debe estar disponible solo para Android y permitir el acceso a la cuenta, los hogares,… | —                                                                                                                   | Sin cobertura |
| RF-CLI-05 | Ambas aplicaciones deben permitir elegir el idioma entre español, inglés, francés y portugués | locales.test.ts (móvil) | Automatizada |
| RF-CLI-06 | Las aplicaciones deben permitir elegir una paleta de colores con modo oscuro                                | —                                                                                                                   | Sin cobertura |
| RF-CLI-07 | La aplicación web debe ofrecer en Ajustes la ayuda en modales: documentación por rol, soporte solo para usuarios y novedades | HelpCenter.test.jsx, DocsModal.test.jsx, NewsModal.test.jsx, Support.test.jsx | Automatizada |
| RF-CLI-08 | La aplicación web debe permitir ver la foto de perfil ampliada dentro de la propia página | PhotoViewer.test.jsx, Account.test.jsx | Automatizada |

**Tabla 24**

*Trazabilidad de los requisitos RNF-SEG*

| Requisito  | Descripción                                                                                                | Pruebas automatizadas                                                                                                   | Cobertura     |
|------------|------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|---------------|
| RNF-SEG-01 | Cada acceso concedido a un usuario debe durar 15 minutos y renovarse automáticamente mientras la sesión…   | JwtTokenIssuerTest, RefreshSessionServiceTest                                                                           | Automatizada  |
| RNF-SEG-02 | Si se intenta reutilizar una credencial de renovación ya usada, el sistema debe invalidar la sesión        | RefreshTokenTest, RefreshSessionServiceTest, RefreshLifecycleIntegrationTest                                            | Automatizada  |
| RNF-SEG-03 | El sistema solo debe atender las solicitudes de las aplicaciones web autorizadas                           | SecurityCorsConfigurationTest, SecurityCorsWebTest                                                                      | Automatizada  |
| RNF-SEG-04 | Los datos que envían los dispositivos de medición deben viajar cifrados                                    | —                                                                                                                       | Sin cobertura |
| RNF-SEG-05 | Los códigos de recuperación deben almacenarse de forma protegida, de modo que una filtración de los datos… | PepperedHmacResetCodeHasherTest                                                                                         | Automatizada  |
| RNF-SEG-06 | El canje de códigos de recuperación debe limitarse a 10 intentos por dirección y por ventana de 15…        | InMemoryResetAttemptLimiterTest, ResetAttemptLimiterBeforeDatabaseTest, PoolSaturatedLimiterTest, DeleteAccountServiceTest, AuthControllerWebTest | Automatizada  |
| RNF-SEG-07 | Las funciones internas con las que se autentica a los dispositivos solo deben ser accesibles desde la red… | —                                                                                                                       | Sin cobertura |
| RNF-SEG-08 | El sistema debe proteger la información de los usuarios conforme a la Ley 1581 de 2012 (Congreso de la…    | —                                                                                                                       | Sin cobertura |
| RNF-SEG-09 | Las credenciales de los dispositivos deben comprobarse de forma segura                                     | ApiKeyHashTest, DeviceApiKeyStorageIntegrationTest, BrokerAccessServiceTest                                             | Automatizada  |
| RNF-SEG-10 | Las contraseñas y claves del sistema no deben quedar expuestas en los archivos compartidos del proyecto    | —                                                                                                                       | Sin cobertura |

**Tabla 25**

*Trazabilidad de los requisitos RNF-REN*

| Requisito  | Descripción                                                                                                  | Pruebas automatizadas | Cobertura     |
|------------|--------------------------------------------------------------------------------------------------------------|-----------------------|---------------|
| RNF-REN-01 | Las consultas de datos y las gráficas deben responder en menos de 2 segundos                                 | —                     | Sin cobertura |
| RNF-REN-02 | El sistema debe procesar la información de cada dispositivo con una cadencia de una lectura cada 60 segundos | —                     | Sin cobertura |
| RNF-REN-03 | El envío de correos no debe dejar esperando al usuario más de 5 segundos                                     | —                     | Sin cobertura |

**Tabla 26**

*Trazabilidad de los requisitos RNF-DIS*

| Requisito  | Descripción                                                                                              | Pruebas automatizadas         | Cobertura     |
|------------|----------------------------------------------------------------------------------------------------------|-------------------------------|---------------|
| RNF-DIS-01 | No se fija un porcentaje de disponibilidad objetivo para esta versión                                                    | —                             | Sin cobertura |
| RNF-DIS-02 | El sistema debe tolerar la pérdida ocasional de lecturas                                                 | DeviceConnectivityServiceTest | Automatizada  |
| RNF-DIS-03 | Si el sistema no está disponible, el dispositivo debe seguir midiendo y conservar hasta 30 muestras (30… | —                             | Sin cobertura |
| RNF-DIS-04 | El servicio que recibe los datos de los dispositivos debe reintentar conectarse con el sistema cada 10…  | —                             | Sin cobertura |
| RNF-DIS-05 | Si no hay Wi-Fi durante 10 minutos, el dispositivo debe reiniciarse                                      | —                             | Sin cobertura |
| RNF-DIS-06 | El sistema debe permitir consultar su estado de salud                                                    | —                             | Sin cobertura |

**Tabla 27**

*Trazabilidad de los requisitos RNF-COM*

| Requisito  | Descripción                                                                                                | Pruebas automatizadas | Cobertura     |
|------------|------------------------------------------------------------------------------------------------------------|-----------------------|---------------|
| RNF-COM-01 | La aplicación web debe funcionar en los navegadores actuales (Chrome, Firefox, Safari y Edge)              | —                     | Sin cobertura |
| RNF-COM-02 | La vinculación por Bluetooth desde la web solo está disponible en Chrome y Edge de escritorio y en Chrome… | —                     | Sin cobertura |
| RNF-COM-03 | La aplicación móvil debe ejecutarse en dispositivos Android                                                | —                     | Sin cobertura |
| RNF-COM-04 | El dispositivo de medición debe incluir un sensor de medición eléctrica dedicado, con un solo sensor por…  | —                     | Sin cobertura |

**Tabla 28**

*Trazabilidad de los requisitos RNF-MAN*

| Requisito  | Descripción                                                                                              | Pruebas automatizadas                                                 | Cobertura                                   |
|------------|----------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------|---------------------------------------------|
| RNF-MAN-01 | El sistema debe organizarse en módulos funcionales (Gestión de Usuarios y Perfiles, Gestión de Hogares,… | ModularityTests                                                       | Automatizada                                |
| RNF-MAN-02 | Todo cambio en la estructura de los datos debe poder revertirse                                          | —                                                                     | Sin cobertura                               |
| RNF-MAN-03 | Los datos eliminados deben conservarse marcados como eliminados y no borrarse físicamente                | HomePersistenceTest, UserPersonPersistenceTest, DeleteHomeServiceTest | Automatizada                                |
| RNF-MAN-04 | El sistema debe contar con pruebas automáticas que verifiquen sus funciones principales                  | —                                                                     | Sin cobertura                               |

**Tabla 29**

*Trazabilidad de los requisitos RNF-I18*

| Requisito  | Descripción                                                                                  | Pruebas automatizadas | Cobertura     |
|------------|----------------------------------------------------------------------------------------------|-----------------------|---------------|
| RNF-I18-01 | La aplicación web debe estar disponible en español, inglés, francés y portugués              | —                     | Sin cobertura |
| RNF-I18-02 | La aplicación móvil debe estar disponible en español, inglés, francés y portugués | locales.test.ts (móvil) | Automatizada |
| RNF-I18-03 | Las alertas y las recomendaciones deben mostrarse en el idioma que haya elegido cada usuario | —                     | Sin cobertura |

**Tabla 30**

*Trazabilidad de los requisitos RNF-ESC*

| Requisito  | Descripción                                                                                                 | Pruebas automatizadas | Cobertura     |
|------------|-------------------------------------------------------------------------------------------------------------|-----------------------|---------------|
| RNF-ESC-01 | El sistema debe poder crecer en número de usuarios y de dispositivos sin afectar la experiencia del usuario | —                     | Sin cobertura |
| RNF-ESC-02 | No se fijan cantidades objetivo de usuarios y dispositivos concurrentes para esta versión                         | —                     | Sin cobertura |

**Tabla 31**

*Trazabilidad de los requisitos RNF-USA*

| Requisito  | Descripción                                                                                               | Pruebas automatizadas | Cobertura     |
|------------|-----------------------------------------------------------------------------------------------------------|-----------------------|---------------|
| RNF-USA-01 | La interfaz debe ser clara, adaptable a distintos tamaños de pantalla y coherente, con retroalimentación… | —                     | Sin cobertura |

**Tabla 32**

*Trazabilidad de los requisitos RNF-INT*

| Requisito  | Descripción                                                                                                 | Pruebas automatizadas       | Cobertura     |
|------------|-------------------------------------------------------------------------------------------------------------|-----------------------------|---------------|
| RNF-INT-01 | Los dispositivos de medición deben comunicarse con el sistema mediante conexiones iniciadas desde el…       | —                           | Sin cobertura |
| RNF-INT-02 | El sistema debe enviar correos mediante una cuenta de correo configurada para ese fin                       | EmailSenderTest             | Automatizada  |
| RNF-INT-03 | El sistema debe permitir el acceso con cuentas de Google solicitando únicamente el nombre, el correo y la…  | SignInWithGoogleServiceTest | Automatizada  |
| RNF-INT-04 | Las notificaciones del navegador solo deben funcionar en una conexión segura y requieren que el sistema…    | —                           | Sin cobertura |
| RNF-INT-05 | La configuración del dispositivo por Bluetooth debe estar disponible hasta 10 minutos, no debe interrumpir… | —                           | Sin cobertura |

Los requisitos sin cobertura son vacíos conocidos (ver riesgos y limitaciones). La administración de cuentas no tiene requisito en la especificación (DOC-01); la especificación ya la cubre con los requisitos RF-ADM.

## Resultados

### Suite completa del backend con MySQL (versión 1.1)

El 20 de octubre de 2026 la suite completa del servicio central (`./mvnw test`, perfil test, MySQL 8.4) terminó con **1131 pruebas, 0 fallos y 0 errores**. El mismo resultado lo da el *check* obligatorio `Tests` de GitHub Actions sobre `main` (commit `90ae5ef`, 23 min 9 s), que se ejecuta en cada *pull request* hacia `dev` y `main`. La distribución por módulo del reporte Surefire local más reciente (16 de octubre de 2026: 132 clases, 1124 pruebas, 0 fallos) está en [resumen-suite-completa.md](evidence/backend-full-suite-summary.md); entre ese reporte y la ejecución final se agregaron 7 pruebas.

### Suites por repositorio (versión 1.2, 24/10/2026)

Con la arquitectura de servicios, cada repositorio ejecuta su propia suite con `./mvnw test` (contra MySQL 8.4 los que lo necesitan) en local y en el *check* `Tests` de su pipeline. Resultados, todos en verde:

- `energy-monitor-backend` (home, alert, recommendation, notification e intermediarios): 481 pruebas; `ModularityTests` en verde.
- `en-monitor-measurement-service`: 151 pruebas.
- `en-monitor-device-service`: 130 pruebas.
- `en-monitor-api-gateway`: 57 pruebas.
- `en-monitor-security-service`: 660 pruebas (ejecución del 22 de octubre de 2026).

Pruebas de punta a punta sobre la pila local completa (`energy-monitor-infra`, con los cinco servicios `healthy`):

- `e2e/device-flow.sh` (21/21): rutas del gateway, vinculación de un módulo (el servicio de dispositivos consulta al backend), autenticación del broker contra el servicio de dispositivos con clave correcta e incorrecta, telemetría (`ONLINE` y lectura guardada por el servicio de mediciones), estado `offline` (evento `device.connectivity-lost` y alerta `CONNECTIVITY` en el backend) y borrado del hogar (`home.deleted`, el módulo queda desvinculado y recibe `unlink`).
- `e2e/measurement-flow.sh` (25/25): dos lecturas publicadas por el broker que el servicio de mediciones guarda una sola vez y anuncia por su *outbox* (`measurement.recorded`), alerta `THRESHOLD` levantada y resuelta en el backend, consumo, historial, estadísticas y niveles por el gateway, y la cola `energy-monitor-backend.measurement.recorded` y su `.dlq` vacías.

No se adjuntan reportes de Surefire de la versión 1.2 en las evidencias.

### Backend sin base de datos (ejecución de la versión 1.0, 14/10/2026)

Se ejecutó `./mvnw -o test` limitado a las clases que no levantan contexto con base de datos (88 clases existentes) más las 4 clases nuevas. Los totales por módulo salen de target/surefire-reports y se detallan en [resumen-surefire.md](evidence/backend-surefire-summary.md).

**Tabla 33**

*Resultados del backend por módulo*

| Módulo         | Clases | Ejecutadas | Aprobadas | Fallidas | Omitidas | Tiempo (s) |
|----------------|--------|------------|-----------|----------|----------|------------|
| alert          | 9      | 48         | 48        | 0        | 0        | 1.2        |
| device         | 6      | 48         | 48        | 0        | 0        | 2.0        |
| home           | 23     | 159        | 159       | 0        | 0        | 1.5        |
| measurement    | 11     | 67         | 67        | 0        | 0        | 0.8        |
| notification   | 8      | 60         | 60        | 0        | 0        | 1.4        |
| recommendation | 4      | 23         | 22        | 1        | 0        | 0.3        |
| security       | 31     | 430        | 429       | 1        | 0        | 18.8       |
| Total          | 92     | 835        | 833       | 2        | 0        | 26.0       |

No se ejecutaron 37 clases que necesitan MySQL (270 métodos declarados) porque la variable SPRING_DATASOURCE_PASSWORD no estaba definida en el entorno de ejecución. Son las pruebas de persistencia, las de integración, las web con contexto completo, ModularityTests y las de seguridad con contexto; su estado es «Por ejecutar (MySQL)».

### Aplicación web

Se ejecutó `npm test`: 20 archivos y 141 pruebas, todas aprobadas ([resumen-vitest.md](evidence/web-vitest-summary.md)). En la versión 1.1 (20 de octubre de 2026, rama `main`, commit `2b41cb3`) `npx vitest run` repitió el resultado: 20 archivos y 141 pruebas aprobadas. El *check* `Tests and build` de GitHub Actions ejecuta estas pruebas y el build de producción en cada *pull request*.

### Pruebas nuevas

Se escribieron 4 clases para requisitos críticos sin ninguna cobertura automatizada. Siguen el estilo de las pruebas existentes (@WebMvcTest con @MockitoBean, o Mockito puro) y la estructura AAA.

**Tabla 34**

*Pruebas nuevas y por qué se escribieron*

| Clase                        | Pruebas | Aprobadas | Fallidas | Motivo                                                                                                   | Requisitos            |
|------------------------------|---------|-----------|----------|----------------------------------------------------------------------------------------------------------|-----------------------|
| BrokerAuthControllerTest     | 12      | 12        | 0        | Autenticación y autorización de dispositivos ante el broker (seguridad); no había prueba del controlador | RF-DEV-07, RNF-SEG-09 |
| HomeDeviceControllerTest     | 10      | 10        | 0        | Vinculación y credenciales de dispositivos; solo el caso de uso tenía prueba                             | RF-DEV-02 a RF-DEV-06 |
| RecommendationControllerTest | 7       | 6         | 1        | Bandeja de recomendaciones; el controlador no tenía pruebas. En la versión 1.1, con DEF-02 cerrado, aprueban las 7 | RF-REC-08             |
| TelemetryMqttConsumerTest    | 10      | 10        | 0        | Ingesta de lecturas y descarte de mensajes ajenos; el consumidor MQTT no tenía pruebas                   | RF-MED-02, RF-MED-03  |

### Defectos y observaciones

**Tabla 35**

*Defectos y observaciones encontrados*

| ID     | Severidad | Elemento                                                       | Descripción                                                                                                                                                                                                                                                                                                            | Estado  |
|--------|-----------|----------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| DEF-01 | Baja      | SecuritySupportModelTest.refusesMissingNames                   | La prueba exige que Person rechace un apellido nulo, pero el código lo hizo opcional a propósito (changeset security-023: una cuenta de Google puede no tener apellido). La prueba quedó desactualizada; el código es correcto. No se modificó. Versión 1.1: la prueba se actualizó; exige el nombre y acepta el apellido ausente, y aprueba. | Cerrado |
| DEF-02 | Media     | RecommendationControllerTest.listRequiresHomeId (prueba nueva) | Un parámetro obligatorio ausente (por ejemplo homeId en GET /api/v1/recommendations o /api/v1/alerts) responde 500 en lugar de 400: SecurityExceptionHandler declara @ExceptionHandler(Exception.class) con alcance global y atrapa MissingServletRequestParameterException. La prueba se dejó fallando; no se ajustó. Versión 1.1: `handleUnexpected` conserva el estado 4xx de las excepciones del framework (`ErrorResponse`) y `TypeMismatchException` responde 400; la prueba aprueba. | Cerrado |
| DOC-01 | Baja      | Documentación                                                  | La administración de cuentas (rutas /api/v1/admin, permisos USER.READ, USER.MANAGE y ROLE.MANAGE, pantalla /admin/users y notificaciones de administrador) existe en el código pero no tiene requisito en la especificación de requisitos ni está descrita en los manuales técnico y de administrador. Versión 1.1: los manuales técnico y de administrador ya la describen; la especificación ya incluye el requisito (RF-ADM). | Resuelto |

### Versión 1.3 (28/10/2026)

Con la migración completa cada repositorio ejecuta su suite con `./mvnw test` (contra MySQL 8.4 los que lo necesitan) y la web con `npx vitest run`. La Tabla 36 recoge los resultados, todos en verde.

**Tabla 36**

*Suites por repositorio, versión 1.3*

| Repositorio                      | Commit  | Pruebas | Resultado |
|----------------------------------|---------|---------|-----------|
| en-monitor-security-service      | f57c76b | 773     | 0 fallos  |
| en-monitor-home-service          | f424083 | 260     | 0 fallos  |
| en-monitor-notification-service  | 7b10398 | 234     | 0 fallos  |
| en-monitor-measurement-service   | 8a9b544 | 151     | 0 fallos  |
| en-monitor-device-service        | 06c1bdc | 129     | 0 fallos  |
| en-monitor-alert-service         | f050b56 | 119     | 0 fallos  |
| en-monitor-recommendation-service | aeeeec5 | 71     | 0 fallos  |
| en-monitor-api-gateway           | 0b2c164 | 69      | 0 fallos  |
| energy-monitor-web               | 698d4c9 | 228 (38 archivos) | 0 fallos |

Pruebas de punta a punta sobre la pila local completa (`energy-monitor-infra`, con los ocho servicios `healthy`). La Tabla 37 detalla lo que verifica cada una.

**Tabla 37**

*Pruebas de punta a punta de energy-monitor-infra*

| Script                        | Comprobaciones | Qué recorre |
|-------------------------------|----------------|-------------|
| `e2e/device-flow.sh`          | 24             | Rutas del gateway, vinculación de un módulo, autenticación del broker con clave correcta e incorrecta, telemetría, estado `offline` y alerta `CONNECTIVITY`, y borrado del hogar con el comando `unlink` |
| `e2e/measurement-flow.sh`     | 28             | Lecturas guardadas una sola vez y anunciadas por el *outbox*, alerta `THRESHOLD` levantada y resuelta, consumo, historial, estadísticas y niveles por el gateway, y colas sin mensajes pendientes |
| `e2e/alert-flow.sh`           | 36             | Alertas de consumo y de conectividad de punta a punta entre los servicios de mediciones, dispositivos, alertas y notificaciones |
| `e2e/activity-flow.sh`        | 60             | Actividad de hogares y dispositivos por la cola única de notificaciones: bienvenida, miembros, umbrales, propiedad y borrado, con su texto por destinatario |
| `e2e/account-deletion-flow.sh` | 12            | Cuenta creada con Google: no puede cambiar la contraseña (409), pide el código por correo (seguridad, *outbox*, notificaciones), un código equivocado no borra y el correcto sí |

El script `e2e/recommendation-flow.sh` también existe y no se volvió a ejecutar en esta versión. Los límites del gateway (20 POST por minuto por IP y ruta) hacen que ejecuciones consecutivas del mismo script puedan responder 429.

#### Aplicación móvil

La aplicación móvil no tenía pruebas automatizadas. En la versión 1.3 se agregó una suite unitaria y se ejecutó, junto con dos verificaciones estáticas, sobre `dev` (commit `1e4109b` más los archivos de prueba, 28 de octubre de 2026), y se definió un protocolo manual:

- Pruebas unitarias con Jest y `jest-expo` (`npm test`): **8 suites y 78 pruebas, todas aprobadas** (28 de octubre de 2026). Cubren las reglas de contraseña, el esquema de registro, el código de verificación, el cuerpo del registro (recortado y con `acceptTerms`), el aviso de términos (aceptar, cancelar y descartar), la normalización de errores de red y del 429 con `Retry-After`, el mapeo de cada operación y estado a su mensaje (428, 409, 429, 503), y la paridad de los textos de los cuatro idiomas, incluida la existencia en todos de cada clave de error que la API puede producir.
- `npx tsc --noEmit`: 0 errores.
- `npx expo lint`: sin errores (código de salida 0).

Las pruebas unitarias no sustituyen la prueba en un dispositivo: no recorren pantallas ni la llamada real a la API.

La Tabla 38 es el protocolo de pruebas manuales para un dispositivo Android. Su estado es **por ejecutar**: ninguna se ha realizado sobre un dispositivo; los requisitos de la aplicación móvil que pasan a «Automatizada» son solo los que cubre la suite unitaria (RF-CLI-05 y RNF-I18-02). Antes de ejecutarlas hay que construir un APK nuevo, porque el anterior no envía `acceptTerms` y el registro respondería 428.

**Tabla 38**

*Protocolo de pruebas manuales de la aplicación móvil*

| ID     | Prueba | Resultado esperado | Requisitos | Estado |
|--------|--------|--------------------|------------|--------|
| MOV-01 | Registrarse sin marcar los términos y luego marcándolos | Sin marcar no se envía; marcado se pide el código y se abre la pantalla de verificación | RF-SEC-01, RF-SEC-13 | Por ejecutar |
| MOV-02 | Registrarse con un correo de dominio mal escrito (`nombre@meil.com`) | El registro se rechaza con un mensaje legible | RF-SEC-01 | Por ejecutar |
| MOV-03 | Verificar el correo con el código recibido e iniciar sesión | La cuenta se crea al verificar y se entra al panel | RF-SEC-02, RF-SEC-03 | Por ejecutar |
| MOV-04 | Iniciar y cerrar sesión; reabrir la aplicación | La sesión se mantiene al reabrir y se cierra al salir | RF-SEC-03, RF-SEC-04 | Por ejecutar |
| MOV-05 | Entrar con Google por primera vez sin aceptar términos | Se piden los términos y, aceptados, se crea la cuenta | RF-SEC-05, RF-SEC-13 | Por ejecutar |
| MOV-06 | Recuperar la contraseña (correo, código y contraseña nueva) | Se puede entrar con la contraseña nueva | RF-SEC-06 | Por ejecutar |
| MOV-07 | Crear un hogar, marcarlo como favorito y cambiar sus umbrales | El hogar aparece en la lista y en favoritos con los umbrales nuevos | RF-HOM-01, RF-CLI-04 | Por ejecutar |
| MOV-08 | Consultar el panel de un hogar con un medidor enviando datos | Se ven el consumo y la última lectura | RF-MED-07, RF-MED-08, RF-CLI-04 | Por ejecutar |
| MOV-09 | Recibir una alerta y abrir la bandeja de notificaciones; cambiar las preferencias | La alerta se lista, se marca como leída y las preferencias se guardan | RF-ALE-04, RF-NOT-01, RF-CLI-04 | Por ejecutar |
| MOV-10 | Editar el nombre y el apellido por separado; cambiar la contraseña | Cada campo se guarda solo; una cuenta de Google no puede cambiar la contraseña | RF-SEC-07, RF-SEC-08 | Por ejecutar |
| MOV-11 | Eliminar la cuenta (con contraseña; con Google, con el código del correo) | La cuenta se elimina y el correo puede volver a registrarse | RF-SEC-11 | Por ejecutar |
| MOV-12 | Cambiar el idioma entre los cuatro disponibles y activar el modo oscuro | Los textos y los colores cambian sin reiniciar | RF-CLI-05, RF-CLI-06 | Por ejecutar |

### Versión 1.4 (28/10/2026)

Con los últimos cambios cambian los totales de cinco repositorios. La Tabla 39 muestra qué pruebas se agregaron y el commit de `main`.

**Tabla 39**

*Cambios en las suites, versión 1.4*

| Repositorio | Commit | Pruebas | Qué se agregó |
|---|---|---|---|
| en-monitor-security-service | de52c95 | 774 | El perfil interno incluye el avatar (`UserProfileQueryServiceTest`) |
| en-monitor-home-service | 6a4e396 | 260 | Los miembros devuelven `profileImage` (adaptador y endpoint, sin pruebas nuevas: se ampliaron las existentes) |
| en-monitor-notification-service | ecdbc0a | 235 | Texto de la alerta de consumo inusual (`AlertMessagesAnomalyTest`) |
| en-monitor-alert-service | 0e65430 | 128 | Regla de consumo inusual (`RegisterAnomalyAlertServiceTest`, 8) y su lugar en el consumidor (1) |
| energy-monitor-web | 27ad9f0 | 253 (44 archivos) | Visor de fotos, fotos de los miembros, modales de ayuda de Ajustes, lista de hogares que se cierra al hacer clic fuera y push en Brave |
| energy-monitor-mobile | 8b5edc0 | 78 | Sin cambios en las pruebas |

Las pruebas que necesitan MySQL de los servicios de alertas y de seguridad no se ejecutaron en el equipo de trabajo; las ejecuta el *check* `Tests` de cada pipeline.

### Evidencias

Las evidencias están en [evidencias](evidence/evidence-overview.md): resumen de Surefire, log recortado de la ejecución y resumen de Vitest de la versión 1.0, resumen de la suite completa y nueva ejecución de Vitest de la versión 1.1, y el estado de la versión 1.2 (totales por repositorio y pruebas de punta a punta). No contienen secretos.

## Riesgos, limitaciones y vacíos conocidos

**Tabla 40**

*Riesgos y limitaciones de las pruebas*

| ID   | Riesgo o limitación                                                                                                                                                                                                                                                                                                                                                 | Efecto                                                                      | Recomendación                                                           |
|------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------|-------------------------------------------------------------------------|
| R-01 | Pruebas no herméticas: persistencia y contexto completo necesitan MySQL real y SPRING_DATASOURCE_PASSWORD                                                                                                                                                                                                                                                           | Mitigado en la versión 1.1: la suite completa corre con MySQL en local y en CI; siguen sin ser herméticas | Evaluar Testcontainers |
| R-02 | La aplicación móvil no tiene pruebas automatizadas                                                                                                                                                                                                                                                                                                                  | Mitigado en la versión 1.3 con 78 pruebas unitarias, `tsc`, `expo lint` y un protocolo manual aún por ejecutar | Ejecutar el protocolo (Tabla 38) y agregar pruebas de componentes y de pantallas |
| R-03 | MQTT con QoS 0                                                                                                                                                                                                                                                                                                                                                      | Una lectura puede perderse; no se prueba la pérdida                         | Evaluar QoS 1 y agregar una prueba de pérdida                           |
| R-04 | Sin actualización por aire (OTA)                                                                                                                                                                                                                                                                                                                                    | El firmware se actualiza solo por USB                                       | Documentar el procedimiento de carga por USB                            |
| R-05 | Web Bluetooth limitado a Chrome, Edge y Chrome para Android, y a HTTPS o localhost                                                                                                                                                                                                                                                                                  | No se verifica de forma automatizada, tampoco el portal EnergyMonitor-Setup | Evaluar pruebas de extremo a extremo con navegador                      |
| R-06 | El contador de intentos de recuperación vive en memoria                                                                                                                                                                                                                                                                                                             | Se reinicia con el servicio y no se comparte entre instancias               | Limitador compartido si se escala                                       |
| R-07 | Sin pruebas de rendimiento ni de accesibilidad                                                                                                                                                                                                                                                                                                                      | RNF-REN y RNF-USA-01 sin cobertura                                          | Definir las metas y medirlas                                            |
| R-08 | Sin informe de cobertura (JaCoCo no está en el pom.xml)                                                                                                                                                                                                                                                                                                             | No se conoce el porcentaje de cobertura                                     | Agregar JaCoCo (no se agregó sin consultar)                             |
| R-09 | Vacíos sin prueba automatizada: StatusMqttConsumer, DeviceInactivityScheduler, HomeDeletedListener, DeviceCommandListener, MqttDeviceCommandAdapter, RecommendationScheduler, NotificationChannelsController, HomeConsumptionController, ApplianceTypeController, RegisterConnectivityAlertService, DeviceConnectivityLostListener, BrokerAuthSecurityConfiguration | Sin cobertura automatizada directa                                          | Priorizar conectividad y alertas de desconexión                         |

| R-10 | La suite completa del backend tarda unos 23 minutos en GitHub Actions | Los *pull requests* esperan ese tiempo para fusionarse | Paralelizar la suite o separar las pruebas de integración más lentas |

## Conclusiones y recomendaciones

El servicio central cuenta con una suite amplia y rápida: 835 pruebas ejecutadas sin base de datos en 26 segundos, con 2 fallas que no son del código de producción (DEF-01) o que apuntan a un defecto de manejo de errores (DEF-02). La web aprueba sus 141 pruebas. Falta ejecutar las 37 clases con MySQL para cerrar la verificación. Recomendaciones: (1) definir SPRING_DATASOURCE_PASSWORD y ejecutar `./mvnw clean test` completo; (2) corregir DEF-02 con un manejador de MissingServletRequestParameterException; (3) actualizar la prueba de DEF-01; (4) incorporar Testcontainers para hacer herméticas las pruebas de persistencia; (5) agregar pruebas a la aplicación móvil; (6) incorporar JaCoCo y una integración continua; (7) escribir los requisitos de administración y sus pruebas.

Versión 1.1: la verificación del servicio central quedó cerrada con la suite completa (1131 pruebas, 0 fallos) contra MySQL, en local y en GitHub Actions. Se cumplieron las recomendaciones (1), (2) y (3), y la integración continua de la (6) ya existe con *checks* obligatorios en `dev` y `main`. Siguen pendientes Testcontainers (4), las pruebas de la aplicación móvil (5), JaCoCo (6) y el requisito de administración en la especificación (7).

Versión 1.2: con la arquitectura de servicios, la verificación se reparte entre los repositorios y todas las suites están en verde (backend 481, mediciones 151, dispositivos 130, gateway 57 y seguridad 660), con `ModularityTests` en verde y las dos pruebas de punta a punta de `energy-monitor-infra` aprobadas (21/21 y 25/25) sobre la pila local. Siguen pendientes las mismas recomendaciones (4) a (7), ahora en cada servicio Java; JaCoCo no está en ninguno de los `pom.xml`. Rehacer el inventario por clase y la trazabilidad por repositorio queda pendiente.

Versión 1.3: con la migración completa, las ocho suites Java y la web están en verde (773, 260, 234, 151, 129, 119, 71, 69 y 228 pruebas) y las cinco pruebas de punta a punta aprobadas. La trazabilidad incorpora la aceptación de términos (RF-SEC-13) y las reglas de las cuentas de Google. La brecha mayor sigue siendo la aplicación móvil: tiene 78 pruebas unitarias, pero ninguna de componentes o pantallas, y su protocolo manual está sin ejecutar. Recomendaciones: ejecutar ese protocolo en un dispositivo, agregar pruebas de componentes a la aplicación móvil (React Native Testing Library), rehacer el inventario por clase de los servicios y volver a ejecutar `recommendation-flow.sh`.

## Referencias

Cortés Parra, J. E., & García Artunduaga, M. A. (2026). *Especificación de requisitos de software de EnergyMonitor* (versión 2.0). SENA – CIES Regional Huila.

Institute of Electrical and Electronics Engineers. (2008). *IEEE standard for software and system test documentation* (IEEE Std 829-2008). https://doi.org/10.1109/IEEESTD.2008.4578383

ISO/IEC/IEEE. (2021). *Software and systems engineering — Software testing — Part 3: Test documentation* (ISO/IEC/IEEE 29119-3:2021). International Organization for Standardization.
