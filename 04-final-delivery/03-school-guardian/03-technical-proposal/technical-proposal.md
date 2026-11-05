
**Propuesta Técnica del Software**

Guardian Escolar

Software para la creación de la aplicación "Guardian Escolar"

Johan Smith Santamaria Fernández  
Sharick Dayanna Rojas Ibarra  
Juan Pablo Chala Ramírez

SENA — Servicio Nacional de Aprendizaje  
Ficha de programación 3145556  
Centro de formación de Neiva (Huila)

Neiva, 6 de octubre de 2026

# Control de versiones

**Tabla 1**. Control de versiones del documento

| Versión | Fecha      | Autor                   | Descripción del cambio        |
| ------- | ---------- | ----------------------- | ----------------------------- |
| 1.0     | 06/10/2026 | Equipo Guardian Escolar | Versión inicial del documento |

**Tabla de contenido**

Presione F9 (o clic derecho → Actualizar campo) para generar el índice.

# 1 Resumen ejecutivo

## 1.1 La propuesta en síntesis

**Guardian Escolar** es una plataforma web y móvil para el monitoreo y la seguridad del transporte escolar. Integra **geolocalización GPS** en tiempo real, **escaneo QR** de subida y bajada de estudiantes, **notificaciones y alertas automáticas**, un **panel administrativo web** y **aplicaciones móviles** para conductor, acudiente y estudiante. La presente propuesta técnica explica cómo se construye, se asegura y se pone en marcha la plataforma, con énfasis en el valor que recibe la institución educativa y las condiciones verificables de entrega.

El modelo propuesto separa el dominio en **nueve servicios de dominio** detrás de una **puerta de enlace única**, con **comunicación asíncrona por eventos** para la operación en tiempo real y una **base de datos relacional con esquema por servicio** para preservar la integridad de la información. Los clientes son una **aplicación web Angular 21** con Angular Material 3 y una **aplicación móvil React + Expo**. La entrega se organiza en **seis fases** y **cinco hitos verificables** a lo largo de **veintiocho semanas** (estimación preliminar).

## 1.2 Beneficios para la institución

**Tabla 2**. Beneficios de la plataforma para la institución

| Beneficio                           | Cómo se materializa                                                                                                                           |
| ----------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| Visibilidad permanente del trayecto | Seguimiento de la posición del bus en un mapa que se actualiza sin refresco manual, con marcador de "sin señal" cuando se pierde la conexión. |
| Certeza sobre la subida y la bajada | Registro de cada bordada mediante escaneo QR, con validación de que el estudiante pertenece a la ruta y notificación inmediata al acudiente.  |
| Comunicación oportuna               | Avisos automáticos de subida, bajada, parada prolongada, incidentes y pérdida de señal, dirigidos a los destinatarios correctos.              |
| Control operativo centralizado      | Administración de sedes, cursos, personas, flota, rutas, horarios y asignaciones desde un panel con roles y permisos.                         |
| Trazabilidad ante incidentes        | Historial de bordadas, ejecuciones de ruta, alertas y acciones administrativas con registro de quién, qué, cuándo, IP y aplicación.           |
| Adopción sin fricción               | Experiencia web para la administración y experiencia móvil para la operación en campo, con accesos diferenciados por rol.                     |

## 1.3 Cifras clave de la propuesta

**Tabla 3**. Cifras clave de la propuesta

| Elemento                   | Valor                                                                   |
| -------------------------- | ----------------------------------------------------------------------- |
| Requisitos funcionales     | 30 RF organizados en 10 grupos funcionales; 8 requisitos no funcionales |
| Historias y épicas         | 20 historias de usuario en 7 épicas                                     |
| Servicios de dominio       | 9 microservicios                                                        |
| Modelo de datos            | 38 tablas en 11 esquemas                                                |
| Roles del sistema          | 5                                                                       |
| Eventos de dominio         | 6 topics de Kafka                                                       |
| Aplicaciones cliente       | 1 web Angular 21 + Angular Material 3; 1 móvil React + Expo             |
| Objetivo de disponibilidad | SLO 99,9 % mensual                                                      |
| Objetivo de latencia       | Latencia de API P95 < 300 ms en los puntos críticos                     |
| Duración estimada          | 28 semanas (estimación preliminar)                                      |
| Esfuerzo estimado          | 107–156 personas·semana (estimación)                                    |

# 2 Diagnóstico y problema

## 2.1 Situación actual del transporte escolar

Las instituciones educativas gestionan hoy el transporte escolar con mecanismos mayoritariamente manuales y con información dispersa entre la institución, el conductor y los acudientes. No existe una fuente única y confiable que permita responder, en tiempo real, dónde está el bus, si un estudiante subió o bajó correctamente y qué ocurrió durante el trayecto. Esa ausencia de información centralizada produce incertidumbre en las familias, dificulta el control operativo de la institución y elimina la trazabilidad sobre rutas y bordadas.

La comunicación se resuelve por canales informales —llamadas, mensajes y avisos verbales—, lo que introduce demoras, depende de la disponibilidad de las personas y no deja registro alguno. Los sistemas de rastreo externos, cuando existen, muestran vehículos sobre un mapa, pero no integran estudiantes, acudientes, rutas y eventos de subida y bajada en un mismo entorno. El resultado es un proceso fragmentado: se conoce la posición del vehículo o se conoce la nómina del curso, pero no ambos elementos articulados con la persona correcta.

## 2.2 Hallazgos del diagnóstico

**Tabla 4**. Hallazgos del diagnóstico

| #   | Hallazgo                                                                       | Implicación                                                                            |
| --- | ------------------------------------------------------------------------------ | -------------------------------------------------------------------------------------- |
| 1   | No existe una fuente única de información del transporte escolar.              | Cada actor maneja su propia versión de los hechos; se dificulta coordinar y verificar. |
| 2   | No hay un mecanismo integrado para registrar subida y bajada de estudiantes.   | El control es manual, con riesgo de error humano y sin trazabilidad por estudiante.    |
| 3   | No se conoce en tiempo real la posición del bus ni el estado de la ruta.       | Los acudientes dependen de terceros para saber si el recorrido avanza con normalidad.  |
| 4   | Ante incidentes o retrasos no existen alertas oportunas ni canales formales.   | La respuesta a una eventualidad ocurre tarde y sin registro de lo sucedido.            |
| 5   | Los rastreadores externos no articulan vehículo, estudiante, acudiente y ruta. | Se obliga a consultar varias herramientas o a adaptar procesos de forma manual.        |
| 6   | No hay registro automatizado de responsabilidad por vehículo, ruta o acción.   | Se pierde la capacidad de auditar asignaciones y decisiones operativas.                |

## 2.3 Limitaciones de la solución actual

**Tabla 5**. Limitaciones de las soluciones vigentes

| Solución vigente                                               | Limitación                                                                       | Fricción que genera                                                |
| -------------------------------------------------------------- | -------------------------------------------------------------------------------- | ------------------------------------------------------------------ |
| Comunicación directa entre institución, conductor y acudientes | La información depende del contacto manual y no hay una fuente única de verdad.  | Tiempo adicional y demoras en la comunicación.                     |
| Verificación manual del estado del recorrido                   | No ofrece una vista centralizada de la posición actual del bus.                  | Incertidumbre y dependencia de terceros.                           |
| Control manual de estudiantes durante la subida y la bajada    | No existe un mecanismo automático que registre cada evento por estudiante.       | Riesgo de error humano y pérdida de trazabilidad.                  |
| Plataformas externas de rastreo GPS                            | Muestran vehículos, pero no integran estudiantes, acudientes, rutas ni bordadas. | Necesidad de consultar distintos medios o adaptar procesos a mano. |

## 2.4 Evidencia recogida

**Tabla 6**. Evidencia recogida en el diagnóstico

| Tipo de evidencia                                         | Hallazgo clave                                                                                                        |
| --------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------- |
| Entrevistas con usuarios vinculados al transporte escolar | Se identificó la necesidad de conocer la ubicación del bus y de mantener informados a los acudientes.                 |
| Entrevistas con responsables del servicio                 | Se identificó la necesidad de controlar quién sube y quién baja del vehículo.                                         |
| Observación directa del proceso actual                    | No existe un mecanismo integrado para registrar subida y bajada mediante código QR.                                   |
| Análisis comparativo de soluciones de rastreo             | La visualización de vehículos sobre mapa se confirma como referencia para el seguimiento de rutas.                    |
| Validación técnica con dispositivo GPS                    | Se confirmó la viabilidad de recibir datos de posicionamiento sobre canal TCP para obtener la ubicación del vehículo. |

## 2.5 Restricciones del contexto

**Tabla 7**. Restricciones del contexto

| Tipo          | Restricción                                                                              | Implicación para la propuesta                                           |
| ------------- | ---------------------------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| Académica     | Desarrollo en el marco del programa Análisis y Desarrollo de Software, ficha 3145556.    | El calendario de entrega se ajusta al periodo de formación.             |
| Geográfica    | Contexto operativo inicial en Neiva (Huila), Colombia.                                   | Idioma, datos y supuestos operativos se definen para ese contexto.      |
| Tecnológica   | Web Angular 21, móvil React + Expo, servicios Java y C#, base SQL Server.                | El diseño y las pruebas se alinean con ese stack.                       |
| De desarrollo | Docker y contenedores para estandarizar la ejecución.                                    | Los entornos reproducen la misma configuración en todo el ciclo.        |
| De seguridad  | Datos sensibles de estudiantes y familias.                                               | Se exigen control de acceso, cifrado en tránsito y mínimo privilegio.   |
| Regulatoria   | Normativa colombiana de protección de datos personales, con atención especial a menores. | Tratamiento, consentimiento y retención deben ser explícitos.           |
| De entrega    | Integraciones externas de GPS y notificaciones aún sin proveedor confirmado.             | No pueden considerarse listas para producción hasta cerrar el contrato. |
| De proceso    | Ramas develop, main y docs; mensajes de confirmación con convención.                     | Cambios y liberaciones siguen el flujo documentado.                     |

# 3 Objetivos de la propuesta

## 3.1 Objetivo general

Diseñar, construir y poner en marcha una plataforma centralizada que garantice la trazabilidad y la comunicación del transporte escolar, de modo que la institución, los conductores y los acudientes compartan una misma fuente de información confiable sobre cada trayecto.

## 3.2 Objetivos específicos

**Tabla 8**. Objetivos específicos y resultados verificables

| #   | Objetivo                                                                           | Resultado verificable                                                                                                                |
| --- | ---------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| O1  | Garantizar la trazabilidad en tiempo real de cada trayecto escolar.                | Posición del bus disponible en el mapa durante la ejecución de la ruta, con registro de la ejecución y sus eventos.                  |
| O2  | Notificar a los acudientes el estado de subida y bajada de sus estudiantes.        | Cada bordada registrada por QR genera la notificación correspondiente al acudiente del estudiante.                                   |
| O3  | Ofrecer a la institución un panel de gestión de flota, rutas y usuarios con roles. | Panel web operativo con gestión de sedes, cursos, personas, flota y rutas, restringido por rol y permisos.                           |
| O4  | Incorporar la seguridad y la protección de datos desde el diseño.                  | Autenticación con token firmado, autorización por rol, auditoría de acciones críticas y tratamiento responsable de datos de menores. |
| O5  | Entregar un sistema operable y observable.                                         | Entornos definidos, pipeline de integración y liberación, comprobaciones de salud, métricas y alertas.                               |
| O6  | Habilitar la adopción por parte de los cinco roles.                                | Interfaces diferenciadas por rol en web y móvil, con flujos verificados de extremo a extremo.                                        |

## 3.3 Supuestos y dependencias externas

**Tabla 9**. Supuestos y dependencias externas

| ID   | Dependencia                                       | Tipo            | Necesaria para                    | Estado              | Riesgo si se retrasa                         |
| ---- | ------------------------------------------------- | --------------- | --------------------------------- | ------------------- | -------------------------------------------- |
| D-01 | Instancia de SQL Server y credenciales            | Infraestructura | Todos los esquemas de datos       | Supuesta disponible | Bloqueo del modelo de datos                  |
| D-02 | Clúster de Apache Kafka                           | Infraestructura | Funciones asíncronas              | Supuesta disponible | Bloqueo de eventos y notificaciones          |
| D-03 | Definición de la taxonomía de rutas y excepciones | Producto        | Enumeraciones y reglas de negocio | Abierta             | Valores incorrectos en tipos y estados       |
| D-04 | Contratos de proveedor de notificaciones push     | Externo         | Despacho de notificaciones        | Abierta             | Notificaciones limitadas o simuladas         |
| D-05 | Carga inicial de datos de la institución          | Datos           | Demostración y puesta en marcha   | Abierta             | Imposibilidad de aceptación con datos reales |
| D-06 | Formato del flujo del dispositivo GPS             | Externo         | Ingesta de posicionamiento        | Abierta             | Seguimiento no disponible en vivo            |

# 4 Solución y alcance

## 4.1 Descripción de la solución

Guardian Escolar se compone de cuatro bloques construidos sobre una base común de identidad y datos:

- **Plataforma de servicios**: nueve servicios de dominio que exponen las capacidades de identidad, usuarios, escuela, flota, rutas, notificaciones, usos excepcionales, auditoría y configuración.
- **Puerta de enlace única**: punto de entrada que termina el cifrado en tránsito, aplica control de tasa y CORS, valida el token de acceso y enruta las peticiones versionadas /api/v1/... hacia los servicios.
- **Clientes**: aplicación web Angular 21 con Angular Material 3 para la administración, y aplicación móvil React + Expo para conductor, acudiente y estudiante, con cámara para el escaneo QR y notificaciones push.
- **Plano de datos**: una instancia relacional con **esquema por servicio** (38 tablas en 11 esquemas), un broker de eventos para el flujo asíncrono y un canal de baja latencia para la telemetría de posicionamiento.

## 4.2 Principios rectores

**Tabla 10**. Principios rectores de la solución

| Principio                    | Implicación en las decisiones                                                                                                                              |
| ---------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Seguridad primero            | Las funciones que mejoran la seguridad, la identificación, la trazabilidad o el control del transporte tienen prioridad sobre la funcionalidad secundaria. |
| Información cuando importa   | Los usuarios reciben información relevante en el momento útil, sin complejidad innecesaria ni notificaciones excesivas.                                    |
| Simple para cada usuario     | La plataforma debe ser comprensible para acudientes, conductores y administradores con distintos niveles técnicos.                                         |
| Trazabilidad por defecto     | Los eventos importantes del transporte dejan un registro confiable y consultable cuando se necesita.                                                       |
| Confiable antes que avanzado | El seguimiento GPS, el registro QR, las rutas y las notificaciones deben ser confiables antes de agregar funciones complejas.                              |

## 4.3 Alcance incluido

**Tabla 11**. Capacidades incluidas en el alcance

| #   | Capacidad incluida               | Descripción                                                                                                                                   |
| --- | -------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| 1   | Identidad y roles                | Inicio de sesión, tableros según rol y acceso diferenciado para administradores, superadministradores, conductores, acudientes y estudiantes. |
| 2   | Gestión de familias y acudientes | Administración de familias, acudientes, miembros y sus relaciones con los estudiantes.                                                        |
| 3   | Gestión de estudiantes           | Registro de estudiantes y asociación con familias, rutas, paradas y asignaciones de transporte.                                               |
| 4   | Gestión de conductores           | Administración de conductores y su información de licencia.                                                                                   |
| 5   | Gestión de vehículos             | Administración de buses y sus asignaciones operativas.                                                                                        |
| 6   | Gestión de rutas y paradas       | Creación y mantenimiento de rutas, paradas, orden de paradas y asignaciones.                                                                  |
| 7   | Gestión escolar                  | Administración de instituciones, sedes, cursos y grupos.                                                                                      |
| 8   | Consulta de rutas                | Consulta de estudiantes, rutas, paradas e información de transporte por parte de acudientes y conductores.                                    |
| 9   | Monitoreo y trazabilidad         | Registro de la actividad de la ruta, estado del vehículo, eventos de subida y bajada e historial operativo.                                   |
| 10  | Notificaciones y alertas         | Comunicación de retrasos, desvíos, incidentes, inicio y fin de ruta, y eventos de subida y bajada.                                            |
| 11  | Perfil y cambios de datos        | Consulta y actualización de correo, credenciales y datos de contacto por parte de administradores y superadministradores.                     |
| 12  | Acceso web y móvil               | Experiencia web Angular y experiencia móvil React + Expo.                                                                                     |

## 4.4 Alcance excluido

**Tabla 12**. Elementos excluidos del alcance

| #   | Elemento excluido                                                  | Motivo                                                                          | ¿Versión futura?              |
| --- | ------------------------------------------------------------------ | ------------------------------------------------------------------------------- | ----------------------------- |
| 1   | Cobros, facturación o liquidación del servicio                     | Fuera del propósito de seguridad, monitoreo y gestión.                          | Posible módulo futuro         |
| 2   | Mantenimiento de flota, combustible y taller mecánico              | No corresponde a un flujo confirmado del alcance actual.                        | Posible versión futura        |
| 3   | Gestión académica, notas o asistencia escolar                      | Guardian Escolar gestiona el transporte, no el expediente académico.            | Sin compromiso actual         |
| 4   | Transporte público y rutas no escolares                            | El dominio se limita al transporte escolar.                                     | No                            |
| 5   | Integración garantizada con proveedor GPS de producción            | El proveedor y su contrato no están confirmados.                                | Sí, tras definir el proveedor |
| 6   | Integración garantizada con proveedor de notificaciones push       | La integración de producción está pendiente de confirmación.                    | Sí                            |
| 7   | Respuesta o despacho automático de emergencias                     | Las alertas comunican incidentes; la respuesta humana es externa.               | Requiere acuerdo aparte       |
| 8   | Identificación biométrica o reconocimiento facial                  | No es necesario para el alcance actual e incrementa obligaciones de privacidad. | Por decidir                   |
| 9   | Localización regulatoria multi-país                                | La meta inicial es Neiva (Huila), Colombia.                                     | Posible expansión futura      |
| 10  | Generación automática de rutas óptimas con inteligencia artificial | No aporta al objetivo de trazabilidad del alcance inicial.                      | No en esta versión            |
| 11  | Navegación autónoma para el conductor                              | La conducción y la navegación están fuera del sistema.                          | No                            |
| 12  | Sustituir los procedimientos de emergencia de cada institución     | La respuesta institucional es ajena al software.                                | No                            |

## 4.5 Actores y roles

**Tabla 13**. Actores, perfil y canal principal

| Rol         | Perfil                                                                                                | Canal principal |
| ----------- | ----------------------------------------------------------------------------------------------------- | --------------- |
| SUPER_ADMIN | Operador de plataforma: gestiona instituciones, configuración global y auditoría.                     | Web             |
| ADMIN       | Administrador escolar: administra sedes, cursos, personas, flota, rutas y reportes de su institución. | Web             |
| DRIVER      | Conductor: ejecuta rutas, confirma bordadas con QR y recibe alertas en la aplicación móvil.           | Móvil           |
| PARENT      | Acudiente: sigue la ruta, recibe notificaciones de subida y bajada y consulta el historial.           | Móvil           |
| STUDENT     | Estudiante: identificación QR vinculada a su perfil para bordar el vehículo.                          | Móvil           |

# 5 Modelo arquitectónico y stack

## 5.1 Estilo arquitectónico y principios técnicos

Se adoptó un estilo de **microservicios con orientación a eventos** detrás de una **puerta de enlace única**. La decisión responde a que el dominio se divide de forma natural en contextos acotados (identidad, escuela, transporte, seguimiento, notificación e infraestructura), a que los flujos de tiempo real —bordadas, posicionamiento y alertas— se procesan mejor de forma asíncrona, y a que cada servicio puede evolucionar y desplegarse sin detener el resto. La base de datos es **relacional única con esquema por servicio**, lo que preserva la integridad referencial y reduce el costo operativo frente a una instancia por servicio.

**Tabla 14**. Principios técnicos del estilo arquitectónico

| Principio                            | Aplicación                                                                                                                                             |
| ------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Contratos primero                    | Los contratos de interfaz se definen antes de la implementación y se consumen desde un portal de documentación sobre el estándar OpenAPI.              |
| Esquema por servicio                 | Aislamiento lógico por servicio dentro de una misma instancia relacional; sin mezcla de responsabilidades de datos.                                    |
| Falla rápido, se recupera con gracia | Validación en el borde, comprobaciones de salud, cortacircuitos en las llamadas síncronas y consumidores idempotentes.                                 |
| Observabilidad por diseño            | Identificador de correlación de extremo a extremo, registros estructurados, métricas y trazas por servicio.                                            |
| Seguridad por defecto                | Token firmado en el borde, autorización por rol con denegación por defecto, secretos fuera del control de versiones y auditoría de acciones sensibles. |

## 5.2 Vista general del modelo propuesto

┌───────────────────────────────────────────────┐

│ CLIENTES DE ACCESO │

│ Web institucional Aplicación móvil │

│ (Angular 21 + (React + Expo: │

│ Angular Material 3) conductor, acudiente, │

│ estudiante con QR) │

└───────────────────────┬───────────────────────┘

│ HTTPS · REST/JSON

│ Bearer JWT RS256 · /api/v1/...

▼

┌───────────────────────────────────────────────┐

│ PUERTA DE ENLACE ÚNICA (Kong OSS) :8000 │

│ TLS · CORS · control de tasa · enrutamiento │

└───────────────────────┬───────────────────────┘

│ DNS de contenedores

▼

┌────────────────────────────────────────────────────────────────────────┐

│ MICROSERVICIOS DE DOMINIO (9) :8080 │

│ │

│ identificación y acceso (IAM) \[Java 21 + Spring Boot\] │

│ gestión de usuarios · gestión escolar · flota · rutas · │

│ notificaciones · usos excepcionales · auditoría · configuración │

│ \[C# / .NET 10\] │

└───────┬───────────────────────┬──────────────────────┬────────────────┘

│ │ │

▼ ▼ ▼

┌───────────────┐ ┌───────────────┐ ┌───────────────────┐

│ SQL Server │ │ Apache Kafka │ │ Canal gRPC :5001 │

│ 2022 :1433 │ │ 4.1.0 (KRaft) │ │ telemetría GPS │

│ base │ │ :9092 │ └───────────────────┘

│ school-guardian│ │ 6 topics │ ┌───────────────────┐

│ 11 esquemas │ └───────────────┘ │ Redis (caché │

│ 38 tablas │ │ efímera y tasa) │

└───────────────┘ └───────────────────┘

**Figura 1**. Vista general de la arquitectura propuesta

## 5.3 Componentes y puertos

**Tabla 15**. Componentes, tecnología y puertos

| Componente                                | Función principal                                                   | Tecnología                 | Puerto |
| ----------------------------------------- | ------------------------------------------------------------------- | -------------------------- | ------ |
| Puerta de enlace                          | Punto único de entrada: enrutamiento, TLS, CORS, control de tasa    | Kong OSS                   | 8000   |
| Servicio de identificación y acceso (IAM) | Autenticación, roles, permisos, perfiles y sesiones                 | Java 21 + Spring Boot      | 8080   |
| Servicio de gestión de usuarios           | Personas, familias, vínculos familiares y licencias de conducción   | C# / .NET 10               | 8080   |
| Servicio de gestión escolar               | Instituciones, sedes, cursos y ciudades                             | C# / .NET 10               | 8080   |
| Servicio de flota                         | Buses, marcas, modelos y asignaciones conductor–bus                 | C# / .NET 10               | 8080   |
| Servicio de rutas                         | Rutas, paradas, horarios, asignaciones y ejecuciones                | C# / .NET 10               | 8080   |
| Servicio de notificaciones                | Bordadas, alertas, destinatarios y despacho                         | C# / .NET 10               | 8080   |
| Servicio de usos excepcionales            | Uso de bus por terceros y desvíos de ruta con motivo                | C# / .NET 10               | 8080   |
| Servicio de auditoría                     | Registro de acciones críticas y de errores                          | C# / .NET 10               | 8080   |
| Servicio de configuración                 | Gestión de la política de contraseñas y de los ajustes de seguridad | C# / .NET 10               | 8080   |
| Broker de eventos                         | Desacoplamiento asíncrono entre servicios                           | Apache Kafka 4.1.0 (KRaft) | 9092   |
| Almacenamiento relacional                 | Persistencia con esquema por servicio                               | SQL Server 2022            | 1433   |
| Canal de telemetría                       | Posicionamiento de baja latencia                                    | gRPC                       | 5001   |
| Caché efímera                             | Caché de sesión y control de tasa                                   | Redis                      | 6379   |

## 5.4 Patrones de comunicación

La comunicación sincrónica cliente–plataforma usa **REST/JSON** sobre la puerta de enlace, con versionado /api/v1. Los servicios intercambian **eventos** a través del broker para los flujos de alta concurrencia y de naturaleza asíncrona, y usan **gRPC** en un canal persistente para la telemetría de posicionamiento de baja latencia. Las respuestas mantienen una estructura de error consistente (timestamp, message, details/path) y la paginación por página y tamaño.

### 5.4.1 Eventos de dominio

**Tabla 16**. Eventos de dominio y sus participantes

| Topic           | Propósito                     | Productor funcional                  | Consumidor funcional                            |
| --------------- | ----------------------------- | ------------------------------------ | ----------------------------------------------- |
| student.created | Alta de estudiante            | Servicio de gestión de usuarios      | Servicio de identificación y acceso             |
| driver.created  | Alta de conductor             | Servicio de gestión de usuarios      | Servicio de identificación y acceso             |
| admin.created   | Alta de administrador         | Servicio de gestión de usuarios      | Servicio de identificación y acceso             |
| parent.created  | Alta de acudiente             | Servicio de gestión de usuarios      | Servicio de identificación y acceso             |
| profile.created | Perfil creado en identidad    | Servicio de identificación y acceso  | Suscripción en preparación                      |
| student.scanned | Escaneo QR de subida o bajada | Servicio de notificaciones (escaneo) | Servicio de notificaciones (bordada y despacho) |

### 5.4.2 Flujo de una bordada

Conductor (móvil) Puerta de enlace Servicio de notificaciones

│ │ │

│ escanea QR del │ │

│ estudiante ──────────▶│ POST /api/v1/boarding │

│ │ ───────────────────────────▶

│ │ │ valida la asignación

│ │ │ a la ruta

│ │ │ registra la bordada

│ │ │ publica student.scanned

│ │ │ despacha push

│ │ │────────────▶ Acudiente

│◀── confirmación ──────│ │

**Figura 2**. Flujo de una bordada por escaneo QR

### 5.4.3 Recursos representativos

**Tabla 17**. Recursos representativos de la interfaz

| Método | Ruta                           | Rol           | Descripción                                    |
| ------ | ------------------------------ | ------------- | ---------------------------------------------- |
| POST   | /api/v1/auth/login             | Público       | Inicio de sesión y emisión del token de acceso |
| POST   | /api/v1/boarding               | DRIVER        | Registro de una bordada por escaneo QR         |
| GET    | /api/v1/buses/{busId}/location | PARENT, ADMIN | Posición del bus para el mapa de seguimiento   |

Las interfaces se organizan por dominio funcional: autenticación y sesiones; personas y perfiles; familias; instituciones y sedes; cursos; flota; rutas; geolocalización; notificaciones, alertas y bordadas; usos excepcionales; configuración; y auditoría.

## 5.5 Modelo de datos resumen

El modelo consta de **38 tablas distribuidas en 11 esquemas**, con llaves primarias uuid (salvo catálogos de autoincremento y tablas de auditoría), claves foráneas declaradas, fechas datetime2 y baja lógica mediante Status con estados ACTIVE/INACTIVE.

**Tabla 18**. Contenido de los esquemas de datos

| Esquema        | Contenido clave                                                                                     |
| -------------- | --------------------------------------------------------------------------------------------------- |
| Iam            | Profile (persona, sede, hash y rol), Role, Permissions, PermissionsRole, SessionProfile             |
| UserManagement | Person, Family, FamilyMember (relación acudiente/estudiante), DriverLicense                         |
| School         | School, SchoolCampus, Course, SchoolAdmin                                                           |
| Geographic     | City                                                                                                |
| Fleet          | Bus, Brand, Model, DriverAssignemts                                                                 |
| Route          | Route, Stop, RouteStop, RouteBusAssignments, RouteStudentAssignments, RouteSchedule, RouteExecution |
| Gps            | GpsDevice, GpsLocation                                                                              |
| Notification   | Boarding, Alert, AlertType, AlertRecipient, DeviceToken                                             |
| Exceptional    | ExceptionalDriverUsage, ExceptionalRouteUsage                                                       |
| Settings       | PasswordPolicies, SecuritySettings                                                                  |
| Audit          | Audit, LogErrors                                                                                    |

Las relaciones centrales del negocio son institución → sede → (bus, ruta, perfil), ruta → paradas ordenadas, programación → asignaciones de bus y ruta, ejecución → bordadas y alertas, y persona → perfil → (rol, vínculo familiar, licencia, sesión, auditoría).

## 5.6 Justificación del stack por capa

### 5.6.1 Capa de servicios

**Tabla 19**. Decisiones de la capa de servicios

| Decisión                                                    | Criterio de elección                                                                                                                                            | Alternativas consideradas                                                              |
| ----------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------- |
| Microservicios con orientación a eventos                    | El dominio se divide en contextos acotados; permite desplegar cada capacidad de forma independiente y procesar bordadas, posición y alertas de forma asíncrona. | Monolito modular; arquitectura en capas única                                          |
| Puerta de enlace Kong OSS                                   | Punto único de entrada con TLS, control de tasa, CORS y ruteo versionado, sin atar el sistema a un lenguaje.                                                    | Nginx con lógica propia; puerta de enlace incluida en el framework; producto comercial |
| Servicio de identificación en Java 21 + Spring Boot (Maven) | Ecosistema maduro para seguridad, arquitectura hexagonal y librerías de firma asimétrica.                                                                       | C# para todos los servicios; Node.js                                                   |
| Servicios de dominio en C# / .NET 10                        | Rendimiento, productividad y homogeneidad en la mayoría de los servicios, con despliegue por contenedor.                                                        | Java en todos; Go; Node.js                                                             |
| Comunicación síncrona REST/JSON                             | Contratos abiertos, versionados y fáciles de consumir desde web y móvil.                                                                                        | GraphQL; RPC sobre HTTP para todo el tráfico                                           |
| Comunicación de baja latencia por gRPC                      | Canal persistente y eficiente para el flujo de telemetría y geolocalización (puerto 5001).                                                                      | REST con sondeo; WebSocket                                                             |
| Mensajería con Apache Kafka 4.1.0 en modo KRaft             | Orden por clave, retención de eventos y desacoplamiento, sin componente de coordinación externo.                                                                | RabbitMQ; cola sobre base de datos                                                     |
| Contenedores con Docker / Docker Compose                    | Reproducibilidad de los entornos e imágenes propias por servicio.                                                                                               | Instalación directa sobre el anfitrión                                                 |

### 5.6.2 Capa de datos

**Tabla 20**. Decisiones de la capa de datos

| Decisión                                                  | Criterio de elección                                                                                                           | Alternativas consideradas                     |
| --------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------ | --------------------------------------------- |
| SQL Server 2022, instancia única con esquema por servicio | Integridad relacional, 38 tablas en 11 esquemas, menor costo operativo y camino de evolución documentado hacia el aislamiento. | Base de datos por servicio; almacén NoSQL     |
| Migraciones con Liquibase 4.29                            | Cambiosets versionados y de orden determinista, válidos tanto para servicios Java como .NET.                                   | Migraciones del ORM del framework; Flyway     |
| Caché efímera en Redis                                    | Estado volátil fuera de los servicios: caché de sesión y contadores de tasa en el borde.                                       | Memoria de la puerta de enlace; base de datos |
| Eventos versionados de forma aditiva                      | Evitar que la evolución del esquema rompa a los consumidores.                                                                  | Cambios de contrato en sitio                  |

### 5.6.3 Clientes y operación

**Tabla 21**. Decisiones de clientes y operación

| Decisión                                                       | Criterio de elección                                                                                                           | Alternativas consideradas                                  |
| -------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------ | ---------------------------------------------------------- |
| Web con Angular 21 + Angular Material 3                        | Componentes de administración, tablas con paginación, formularios validados, tableros con indicadores y navegación por rol.    | Vue; React para web; HTML y CSS propios                    |
| Móvil con React + Expo                                         | Una sola base de código para Android e iOS, cámara para el escaneo QR y notificaciones push.                                   | Desarrollo nativo por plataforma; React Native sin Expo    |
| Identificación con JWT RS256 (access 15 min / refresh 30 días) | Firma asimétrica: solo el servicio de identificación posee la llave privada y la puerta de enlace valida con la llave pública. | Sesiones de servidor; token firmado con secreto compartido |
| Despliegue por contenedores con reinicio y reversión           | Recuperación rápida de fallos y liberaciones sin interrumpir el tráfico.                                                       | Publicación directa sobre el anfitrión                     |
| Pipeline por servicio                                          | Detección temprana de regresiones y liberaciones repetibles.                                                                   | Publicación manual                                         |
| Registros correlacionados, salud y métricas                    | Diagnóstico entre servicios y verificación del objetivo de disponibilidad.                                                     | Registros en texto plano sin correlación                   |

# 6 Capacidades funcionales clave

## 6.1 Estructura de requisitos

El alcance funcional comprende **30 requisitos funcionales distribuidos en 10 grupos**, complementados por **8 requisitos no funcionales** que fijan calidad, seguridad y operación.

**Tabla 22**. Requisitos funcionales por grupo

| Grupo     | Denominación                                       | Nº de RF |
| --------- | -------------------------------------------------- | -------- |
| RF-01     | Gestión de usuarios                                | 4        |
| RF-02     | Autenticación                                      | 2        |
| RF-03     | Gestión de rutas escolares                         | 8        |
| RF-04     | Gestión de escaneo QR (control de subida y bajada) | 3        |
| RF-05     | Notificaciones de bordada                          | 4        |
| RF-06     | Rastreo de buses en tiempo real                    | 3        |
| RF-07     | Gestión de viajes                                  | 2        |
| RF-08     | Incidentes                                         | 2        |
| RF-09     | Consulta de rutas                                  | 2        |
| RF-10     | Personalización                                    | 2        |
| **Total** | **10 grupos**                                      | **30**   |

## 6.2 Capacidades por grupo

**Tabla 23**. Capacidades por grupo de requisitos

| Grupo | Capacidad principal                                                                                                                                                 | Roles              | Canal       |
| ----- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------ | ----------- |
| RF-01 | Alta, edición y baja lógica de estudiantes, conductores, acudientes y administradores; vínculo de estudiantes con la cuenta del acudiente.                          | ADMIN, SUPER_ADMIN | Web         |
| RF-02 | Inicio de sesión con credenciales y emisión del token, cierre de sesión y renovación del acceso.                                                                    | Todos              | Web y móvil |
| RF-03 | Rutas con paradas ordenadas y horarios, validación de unicidad, asignación de bus a ruta, conductor a bus y estudiantes a ruta con su parada.                       | ADMIN              | Web         |
| RF-04 | Generación del código QR individual del estudiante, lectura para registrar la subida (validando la asignación a la ruta) y la bajada (exigiendo una subida activa). | DRIVER, STUDENT    | Móvil       |
| RF-05 | Detección del evento de subida y bajada, notificación en tiempo real al acudiente con hora y ubicación, y alerta por parada prolongada.                             | PARENT             | Móvil       |
| RF-06 | Captura continua de la posición del bus, visualización en mapa sin refresco manual y alerta por pérdida de señal con la última posición conocida.                   | PARENT, ADMIN      | Web y móvil |
| RF-07 | Inicio del viaje (un solo viaje activo por bus) y cierre del viaje validando que no queden bajadas pendientes.                                                      | DRIVER             | Móvil       |
| RF-08 | Reporte de incidente con tipo, descripción opcional y posición, y notificación automática a la institución y a los acudientes de los estudiantes a bordo.           | DRIVER             | Móvil       |
| RF-09 | Consulta del detalle de la ruta del estudiante por el acudiente y de la ruta asignada por el conductor, con bus, conductor, paradas ordenadas y horarios.           | PARENT, DRIVER     | Web y móvil |
| RF-10 | Tema de colores configurable e interfaz en al menos cuatro idiomas.                                                                                                 | Todos              | Web y móvil |

## 6.3 Épicas e historias de usuario

**Tabla 24**. Épicas e historias de usuario

| Épica     | Denominación                       | Historias                                                                                                    | Nº     |
| --------- | ---------------------------------- | ------------------------------------------------------------------------------------------------------------ | ------ |
| EP-001    | Registro de subida y bajada por QR | HU-QR-001, HU-QR-002, HU-QR-004                                                                              | 3      |
| EP-002    | Notificaciones                     | HU-NOTIF-001, HU-NOTIF-005                                                                                   | 2      |
| EP-003    | Gestión de rutas y flota           | HU-ROUTE-001, HU-ROUTE-002, HU-ROUTE-003, HU-ROUTE-004, HU-ROUTE-005, HU-ROUTE-008, HU-FLEET-001, HU-BUS-002 | 8      |
| EP-004    | Gestión de usuarios y familia      | HU-IAM-001, HU-USER-003, HU-USER-004                                                                         | 3      |
| EP-005    | Seguimiento en vivo                | HU-GPS-002                                                                                                   | 1      |
| EP-006    | Incidentes                         | HU-INCIDENT-001                                                                                              | 1      |
| EP-007    | Interfaz y personalización         | HU-UI-001, HU-UI-002                                                                                         | 2      |
| **Total** | **7 épicas**                       |                                                                                                              | **20** |

Las historias se priorizan con un enfoque de seguridad primero: se entrega completa la cadena crítica —identidad, usuarios, rutas, flota, escaneo QR y notificaciones— antes de incorporar funciones de madurez como el reporte de incidentes y la personalización.

# 7 Seguridad y cumplimiento

## 7.1 Identificación, autorización y sesiones

**Tabla 25**. Identificación, autorización y sesiones

| Elemento                  | Definición                                                                                                                                                                                     |
| ------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Autenticación             | Token JWT firmado con RS256: acceso de **15 minutos** y renovación de **30 días**. Solo el servicio de identificación posee la llave privada; la puerta de enlace valida con la llave pública. |
| Endpoints públicos        | Únicamente el inicio de sesión y la renovación; el resto exige el encabezado Authorization: Bearer &lt;token&gt;.                                                                              |
| Autorización              | Control por rol y permisos mediante las tablas Permissions y PermissionsRole, con denegación por defecto y alcance por institución y sede.                                                     |
| Sesiones                  | Registro en SessionProfile con dirección IP y fechas, lo que permite auditar accesos y revocar sesiones.                                                                                       |
| Políticas de credenciales | Parámetros configurables de longitud, mayúsculas, números, símbolos y expiración en días, administrados desde PasswordPolicies y SecuritySettings.                                             |
| Alta de cuentas           | No existe autorregistro: las cuentas son creadas por el administrador, lo que reduce el riesgo de accesos no autorizados.                                                                      |

## 7.2 Controles técnicos

**Tabla 26**. Controles técnicos de seguridad

| Control                                 | Descripción                                                                                                                    | Capa                       |
| --------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------ | -------------------------- |
| Cifrado en tránsito y CORS              | Terminación de TLS y restricción de origen web en la puerta de enlace; HTTPS obligatorio en producción.                        | Puerta de enlace           |
| Control de tasa                         | Contadores en caché efímera para prevenir abuso y saturaciones.                                                                | Puerta de enlace           |
| Validación de entrada                   | Validación en la puerta de enlace y en cada servicio antes de procesar la solicitud.                                           | Borde y servicios          |
| Protección de credenciales              | Hashing de contraseñas conforme a la política de longitud, complejidad y expiración en días.                                   | Servicio de identificación |
| Mínimo privilegio                       | Permisos por rol y denegación por defecto; credenciales de base de datos por esquema.                                          | Servicios y datos          |
| Secretos fuera del control de versiones | Uso de variables de entorno con marcadores como &lt;JWT_PRIVATE_KEY&gt; y &lt;ADMIN_PASSWORD&gt;.                              | Todos los entornos         |
| Auditoría de acciones críticas          | Registro de quién, qué, cuándo, dirección IP y aplicación en la tabla Audit, sin actualización ni borrado desde la aplicación. | Servicio de auditoría      |
| Registro de errores                     | Persistencia de tipo, descripción, IP y fecha en LogErrors.                                                                    | Servicio de auditoría      |
| Comunicación interna protegida          | Canales de servicio con confianza de red y tokens; topics con control de acceso por tema.                                      | Servicios y broker         |
| Baja lógica                             | Estados ACTIVE/INACTIVE sin eliminación física, conservando la trazabilidad histórica.                                         | Datos                      |

## 7.3 Amenazas identificadas y mitigación

**Tabla 27**. Amenazas identificadas y su mitigación

| Categoría                  | Amenaza                                                                | Mitigación                                                                                               |
| -------------------------- | ---------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------- |
| Suplantación               | Uso de un token falsificado para invocar servicios internos.           | Firma RS256 en el borde, rechazo de algoritmos no firmados y rotación de llaves.                         |
| Alteración                 | Repetición o modificación de cargas en el canal gRPC o en los eventos. | Cifrado en tránsito, control de acceso por tema y esquema de eventos versionado.                         |
| Repudio                    | Falta de evidencia sobre quién cambió una asignación o una ruta.       | Registro de auditoría de solo inserción en el esquema de auditoría.                                      |
| Divulgación de información | Inyección de código en consultas o filtración de datos personales.     | Consultas parametrizadas, roles de base de datos con mínimo privilegio y migraciones controladas.        |
| Denegación de servicio     | Saturación de endpoints de salud o de autenticación.                   | Control de tasa en la puerta de enlace y retroceso progresivo en las llamadas internas.                  |
| Escalada de privilegio     | Acceso de un usuario a datos de otra institución.                      | Autorización en el borde y en el servicio, alcance por institución y reglas de frontera entre servicios. |

## 7.4 Protección de datos personales y cumplimiento

**Tabla 28**. Tratamiento de la protección de datos personales

| Aspecto                       | Tratamiento en la propuesta                                                                                                     |
| ----------------------------- | ------------------------------------------------------------------------------------------------------------------------------- |
| Marco aplicable               | Normativa colombiana de protección de datos personales, con atención especial a los datos de menores de edad.                   |
| Minimización                  | Los eventos y las cargas entre servicios se limitan a los datos necesarios y no incluyen secretos.                              |
| Consentimiento y autorización | El tratamiento de datos de estudiantes requiere autorización de los acudientes o representantes, gestionada por la institución. |
| Enmascaramiento               | Los datos personales se enmascaran en los registros y no se exponen en mensajes de error ni en trazas.                          |
| Retención                     | La permanencia de los datos de posicionamiento debe definirse con el área legal antes de la puesta en marcha.                   |
| Acceso                        | El acceso a información personal está restringido por rol y por alcance institucional, y queda registrado en auditoría.         |

## 7.5 Auditoría y trazabilidad

El servicio de auditoría concentra el registro de acciones críticas y de errores en un esquema de solo inserción. A esta evidencia se suman el historial de bordadas (Boarding), las ejecuciones de ruta (RouteExecution), las alertas y sus destinatarios (Alert, AlertRecipient) y las sesiones (SessionProfile). En conjunto, permiten reconstruir qué ocurrió en un trayecto, quién ejecutó una acción administrativa y qué usuarios fueron notificados.

# 8 Modelo de despliegue y entornos

## 8.1 Entornos

**Tabla 29**. Entornos, propósito y actualización

| Entorno              | Propósito                                  | Actualización                                             | Acceso                          | Datos                   |
| -------------------- | ------------------------------------------ | --------------------------------------------------------- | ------------------------------- | ----------------------- |
| Local                | Desarrollo en el equipo de cada integrante | Manual                                                    | Desarrollador                   | Sintéticos o de siembra |
| Desarrollo           | Integración continua                       | Automática al fusionar un cambio en la rama de desarrollo | Todo el equipo                  | Sintéticos              |
| Pruebas y aceptación | Preproducción, validación y demostraciones | Automática al fusionar en la rama principal               | Equipo e institución            | Anonimizados            |
| Producción           | Usuarios reales                            | Manual con aprobación                                     | Responsable técnico y operación | Reales                  |

## 8.2 Topología de despliegue

Navegador institucional Dispositivos móviles

(Angular 21) (React + Expo)

│ │

└────────────────────┬───────────────────┘

│ HTTPS · JWT RS256 · /api/v1

▼

┌──────────────────────────────────────────────┐

│ Puerta de enlace Kong OSS — puerto 8000 │

│ TLS · CORS · control de tasa · enrutamiento │

└───────────────────────┬──────────────────────┘

│ red interna de contenedores

┌───────────────────────┴──────────────────────┐

│ Servicios de negocio — puerto 8080 │

│ (9 servicios de dominio) │

└──┬──────────────┬───────────────┬────────────┘

│ │ │

▼ ▼ ▼

SQL Server 2022 Apache Kafka Canal gRPC :5001

:1433 · 11 esquemas 4.1.0 :9092 (telemetría GPS)

· 38 tablas (KRaft)

Redis :6379

(caché y tasa)

**Figura 3**. Topología de despliegue de la plataforma

## 8.3 Integración y liberación continua

rama de cambio ──▶ desarrollo ──▶ pruebas ──▶ producción

─────────────────────────────────────────────────────────────

formato, pruebas integración, batería canario

unitarias y imagen y completa, al 10 %,

compilación despliegue contrato, observación

(bloquea la con prueba carga (k6) y paso al

fusión) de humo y aviso a 100 % o

la institución reversión

**Figura 4**. Flujo de integración y liberación continua

**Tabla 30**. Etapas y duración de los pipelines

| Pipeline      | Disparador                      | Etapas                                                                                                                                                             | Duración objetivo                                        |
| ------------- | ------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ | -------------------------------------------------------- |
| De cambio     | Cada propuesta de fusión        | Formato y análisis estático; pruebas unitarias; compilación                                                                                                        | Menos de 10 minutos en total; su falla bloquea la fusión |
| De desarrollo | Fusión en la rama de desarrollo | Compilación, pruebas y análisis; pruebas de integración; construcción de imagen; despliegue; pruebas de humo                                                       | Menos de 38 minutos                                      |
| De pruebas    | Fusión en la rama principal     | Batería completa de pruebas; pruebas de contrato; construcción y etiquetado de imagen; despliegue; pruebas de humo; pruebas de carga con k6; aviso para aceptación | Menos de 55 minutos                                      |
| De producción | Aprobación manual               | Canario al 10 % del tráfico; observación de 15 minutos; paso al 100 % o reversión automática                                                                       | Según la ventana de liberación                           |

Los controles automáticos por tipo de cambio incluyen la validación de la documentación y de los enlaces, la validación y comparación de contratos de API, la compilación de los servicios, la validación de los cambiosets de migración y el despliegue a desarrollo con prueba de humo sobre la puerta de enlace.

## 8.4 Orden de arranque y verificación

1. Levantar el sistema de base de datos.
2. Levantar el broker de eventos.
3. Aplicar las migraciones versionadas con Liquibase.
4. Desplegar los servicios de dominio y esperar sus comprobaciones de salud.
5. Recargar las rutas de la puerta de enlace cuando cambien.
6. Publicar la aplicación web y distribuir la aplicación móvil.
7. Verificar la disponibilidad con la comprobación de salud pública.

curl -fsS <http://localhost:8000/health>

## 8.5 Estrategia de publicación y reversión

La publicación en producción utiliza despliegue canario con reversión automática. Antes de liberar se verifica la revisión y aprobación del cambio, el paso de la batería de pruebas en el entorno de pruebas —incluido el rendimiento con k6—, la aprobación de aceptación de la institución, la ejecución de las migraciones antes del código y la disponibilidad del procedimiento de reversión. Ante un comportamiento adverso, se revierte a la versión anterior y, si existiera una migración incompatible, se activa el procedimiento de contingencia con aprobación del responsable técnico.

# 9 Plan de implementación por fases

## 9.1 Enfoque de entrega

La ejecución adopta un enfoque híbrido con iteraciones cortas y control formal de hitos. El trabajo se organiza sobre un backlog de **20 historias de usuario en 7 épicas**, con tablero de seguimiento, definición de terminado por incremento y validación periódica con la institución. Cada incremento se apoya en el pipeline de integración y liberación descrito, de modo que la calidad y la seguridad se verifican de forma continua y no al final.

## 9.2 Fases y actividades

**Tabla 31**. Fases, actividades y resultados

| Fase                          | Duración (semanas) | Actividades principales                                                                                                                   | Resultado                                                   |
| ----------------------------- | ------------------ | ----------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------- |
| F1. Análisis y validación     | 1–2                | Confirmar alcance, cerrar preguntas abiertas, definir la taxonomía de rutas y excepciones, validar la carga inicial de datos.             | Alcance y plan base aprobados                               |
| F2. Diseño                    | 3–5                | Arquitectura, modelo de datos, contratos de interfaz, diseño de pantallas por rol, entornos y pipeline.                                   | Entorno base operativo con puerta de enlace y base de datos |
| F3. Construcción iterativa    | 6–17               | Identidad y usuarios; rutas, flota y asignaciones; operación de viaje con QR; seguimiento y notificaciones; incidentes y personalización. | Incrementos funcionales por épica                           |
| F4. Pruebas e integración     | 18–21              | Pruebas unitarias, de integración, de extremo a extremo, de carga y de seguridad; corrección de defectos; aceptación con la institución.  | Batería de pruebas superada y aceptación                    |
| F5. Despliegue e implantación | 22–23              | Migraciones, despliegue por entorno, carga inicial de datos, capacitación y distribución de la aplicación móvil.                          | Sistema en producción                                       |
| F6. Soporte y estabilización  | 24–28              | Monitoreo del objetivo de disponibilidad, ajustes, procedimientos de operación y respaldo, revisión de resultados.                        | Paquete de puesta en marcha y cierre                        |

Las duraciones corresponden a una estimación preliminar y se ajustan al cierre de las dependencias externas y a la retroalimentación de la institución.

## 9.3 Hitos y criterios de salida

**Tabla 32**. Hitos y criterios de salida

| Hito                           | Semana objetivo | Criterio de salida                                                                                                                                    |
| ------------------------------ | --------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------- |
| H1. Entorno base operativo     | 5               | Rutas configuradas en la puerta de enlace y prueba de humo satisfactoria sobre el puerto 8000.                                                        |
| H2. Cuentas y rutas operativas | 10              | Inicio de sesión, gestión de cuentas, rutas y flota con sus asignaciones disponibles y verificados.                                                   |
| H3. Operación de viaje con QR  | 14              | Registro de subida y bajada por escaneo QR e inicio y cierre de viaje en la aplicación móvil.                                                         |
| H4. Visibilidad del acudiente  | 17              | Mapa con la posición del bus y notificaciones de subida y bajada entregadas al acudiente.                                                             |
| H5. Puesta en marcha           | 23–28           | Aceptación de la institución, despliegue en producción, procedimientos de operación y respaldo, y resultados revisados frente a los resultados clave. |

## 9.4 Cronograma

Semanas 1 5 9 13 17 21 25 28

|----|----|----|----|----|----|----|

F1 Análisis ##

F2 Diseño ###

F3 Constr. ############

F4 Pruebas ####

F5 Despliegue ##

F6 Soporte #####

Hito H1 H2 H3 H4 H5

**Figura 5**. Cronograma de fases e hitos en semanas

Escala: una columna equivale a una semana. Las duraciones y las fechas son una estimación preliminar sujeta a la confirmación de dependencias externas.

# 10 Recursos

## 10.1 Equipo y roles

**Tabla 33**. Equipo, responsabilidades y dedicación

| Rol                                           | Responsabilidad principal                                                           | Dedicación semanal estimada |
| --------------------------------------------- | ----------------------------------------------------------------------------------- | --------------------------- |
| Líder técnico y arquitecto                    | Decisiones de arquitectura, revisión de contratos y calidad del código.             | 1,0 persona                 |
| Desarrollo de servicios (Java y .NET)         | Implementación de los servicios de dominio, eventos y contratos de API.             | 1,5–2,0 personas            |
| Desarrollo de aplicación web (Angular)        | Pantallas por rol, tablas, formularios y tableros.                                  | 0,7–1,0 personas            |
| Desarrollo de aplicación móvil (React + Expo) | Aplicaciones de conductor, acudiente y estudiante; cámara QR y notificaciones push. | 0,7–1,0 personas            |
| Calidad y pruebas                             | Estrategia de pruebas, casos por requisito, automatización y gestión de defectos.   | 0,5–0,8 personas            |
| Operación y despliegue                        | Entornos, pipeline, migraciones, monitoreo y respaldos.                             | 0,3–0,5 personas            |
| Diseño de interfaces y experiencia            | Pantallas por rol, sistema de diseño y validación con usuarios.                     | 0,2–0,3 personas            |
| Gestión de proyecto y aceptación              | Backlog, plan, riesgos y aceptación con la institución.                             | 0,2–0,3 personas            |

La dedicación corresponde a la fase de construcción, en la que el equipo alcanza su mayor tamaño. En las fases de análisis, diseño, pruebas, despliegue y soporte el equipo se reduce. El equipo núcleo del proyecto lo conforman tres desarrolladores del programa Análisis y Desarrollo de Software, de modo que algunas funciones se cubren con dedicación parcial.

## 10.2 Competencias requeridas

**Tabla 34**. Competencias requeridas por área

| Área      | Competencias                                                                                 |
| --------- | -------------------------------------------------------------------------------------------- |
| Servicios | Java 21 con Spring Boot, C# con .NET 10, diseño de API REST, eventos y RPC de baja latencia. |
| Datos     | SQL Server, modelado relacional, migraciones versionadas y consultas eficientes.             |
| Clientes  | Angular 21 con Angular Material 3, React con Expo, accesibilidad y diseño responsivo.        |
| Calidad   | Pruebas unitarias, de integración, de extremo a extremo, de carga y de seguridad.            |
| Operación | Contenedores, integración y liberación continua, monitoreo y respuesta a incidentes.         |
| Seguridad | Autenticación con firma asimétrica, autorización por rol y protección de datos personales.   |

## 10.3 Infraestructura y herramientas

**Tabla 35**. Infraestructura y herramientas

| Recurso                    | Especificación                                                                                        | Uso                                             |
| -------------------------- | ----------------------------------------------------------------------------------------------------- | ----------------------------------------------- |
| Plataforma de contenedores | Docker 24.0 o superior y Docker Compose 2.20 o superior                                               | Estandarizar la ejecución en todos los entornos |
| Control de versiones       | Git con ramas develop, main y docs, y mensajes de confirmación con convención                         | Trazabilidad de cambios y liberaciones          |
| Base de datos              | SQL Server 2022, base school-guardian, puerto 1433                                                    | Persistencia de los 11 esquemas                 |
| Broker de eventos          | Apache Kafka 4.1.0 en modo KRaft, puerto 9092                                                         | Flujo asíncrono de eventos de dominio           |
| Puerta de enlace           | Kong OSS, puerto 8000                                                                                 | Entrada única, seguridad y enrutamiento         |
| Canal de telemetría        | gRPC, puerto 5001                                                                                     | Posicionamiento de baja latencia                |
| Caché efímera              | Redis, puerto 6379                                                                                    | Caché de sesión y control de tasa               |
| Migraciones                | Liquibase 4.29                                                                                        | Cambiosets versionados por servicio             |
| Pruebas de rendimiento     | k6 en el entorno de pruebas                                                                           | Verificación de latencia antes de liberar       |
| Monitoreo                  | Comprobaciones de salud, registros estructurados con identificador de correlación, métricas y alertas | Observabilidad de pruebas y producción          |

# 11 Estimación de esfuerzo por fase

## 11.1 Base de la estimación

La estimación se expresa en **personas·semana** y se presenta como **rango**, no como compromiso. Se construyó descomponiendo cada fase por actividad y aplicando un margen de variación según la madurez de las dependencias externas. No incluye cifras monetarias, licencias de terceros ni costos de proveedores, que deben cotizarse por separado. Los valores suponen un alcance de 30 requisitos funcionales y 20 historias de usuario, y se ajustan si cambian el alcance, el número de instituciones o la confirmación de los proveedores externos.

## 11.2 Esfuerzo por fase

**Tabla 36**. Esfuerzo estimado por fase

| Fase                          | Duración (semanas) | Esfuerzo estimado (personas·semana) | Supuestos clave                                                                     |
| ----------------------------- | ------------------ | ----------------------------------- | ----------------------------------------------------------------------------------- |
| F1. Análisis y validación     | 2                  | 6–8                                 | Equipo reducido de 3 a 4 personas; cierre de preguntas abiertas con la institución. |
| F2. Diseño                    | 3                  | 9–15                                | Equipo de 3 a 5 personas; arquitectura, datos, contratos y pantallas.               |
| F3. Construcción iterativa    | 12                 | 60–84                               | Equipo de 5 a 7 personas en promedio; construcción por épicas.                      |
| F4. Pruebas e integración     | 4                  | 16–24                               | Equipo de 4 a 6 personas con apoyo del equipo de desarrollo.                        |
| F5. Despliegue e implantación | 2                  | 6–10                                | Equipo de 3 a 5 personas; migraciones, carga de datos y capacitación.               |
| F6. Soporte y estabilización  | 5                  | 10–15                               | Equipo de 2 a 3 personas con dedicación parcial.                                    |
| **Total**                     | **28**             | **107–156**                         | **Estimación preliminar**                                                           |

## 11.3 Distribución por rol

**Tabla 37**. Distribución del esfuerzo por rol

| Rol                                           | Personas·semana (estimación) | Mayor presencia              |
| --------------------------------------------- | ---------------------------- | ---------------------------- |
| Líder técnico y arquitecto                    | 12–20                        | Todas las fases              |
| Desarrollo de servicios (Java y .NET)         | 36–52                        | F3 y F4                      |
| Desarrollo de aplicación web (Angular)        | 14–20                        | F3 y F4                      |
| Desarrollo de aplicación móvil (React + Expo) | 14–20                        | F3 y F4                      |
| Calidad y pruebas                             | 12–20                        | F4, con apoyo continuo en F3 |
| Operación y despliegue                        | 8–10                         | F2, F5 y F6                  |
| Diseño de interfaces y experiencia            | 4–6                          | F2 y F3                      |
| Gestión de proyecto y aceptación              | 7–8                          | Todas las fases              |
| **Total**                                     | **107–156**                  |                              |

## 11.4 Escenarios de ajuste

**Tabla 38**. Escenarios de ajuste del esfuerzo

| Escenario                                                                           | Ajuste estimado del esfuerzo                        |
| ----------------------------------------------------------------------------------- | --------------------------------------------------- |
| Confirmación tardía del proveedor de GPS o de notificaciones                        | Incremento del 8 % al 15 % en integración y pruebas |
| Extensión del alcance a más de una institución en la primera entrega                | Incremento del 10 % al 20 %                         |
| Reducción del alcance a las épicas centrales (usuarios, rutas, QR y notificaciones) | Reducción del 20 % al 30 %                          |
| Requisito de escalado con orquestación completa en producción                       | Incremento del 5 % al 10 %                          |

Los porcentajes son referencias de planeación y no constituyen un compromiso contractual.

# 12 Análisis de riesgos

## 12.1 Criterios de valoración

IMPACTO

│

│ ALTO │ \[Mitigar\] │ \[Evitar\] │

│ │ Probabilidad │ Probabilidad │

│ │ baja │ alta │

│──────────────────────────────────────────────────

│ MEDIO │ \[Aceptar y │ \[Mitigar\] │

│ │ monitorear\] │ │

│──────────────────────────────────────────────────

│ BAJO │ \[Aceptar\] │ \[Aceptar\] │

└──────────────────────────────────────────────────

BAJA ALTA

PROBABILIDAD

**Figura 6**. Matriz de probabilidad e impacto

Las estrategias disponibles son mitigar (reducir probabilidad o impacto), evitar (cambiar el plan), transferir (trasladar el riesgo a un tercero) y aceptar (registrar y monitorear con plan de contingencia).

## 12.2 Registro de riesgos

**Tabla 39**. Registro de riesgos y mitigaciones

| ID   | Riesgo                                                                            | Categoría    | Probabilidad | Impacto | Estrategia           | Mitigación                                                                                                                                          |
| ---- | --------------------------------------------------------------------------------- | ------------ | ------------ | ------- | -------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------- |
| R-01 | El proveedor de GPS o su contrato no se confirma antes de la integración en vivo. | Externa      | Media        | Alto    | Mitigar              | Iniciar la gestión del contrato con antelación, desarrollar con datos simulados y habilitar el seguimiento en vivo cuando el proveedor se confirme. |
| R-02 | El proveedor de notificaciones push no se confirma para producción.               | Externa      | Media        | Alto    | Mitigar              | Registrar el token por dispositivo y mantener una cola de entrega reintentable; ofrecer la notificación dentro de la aplicación como contingencia.  |
| R-03 | Los acudientes no usan la aplicación con la frecuencia esperada.                  | Producto     | Media        | Alto    | Mitigar              | Ejecutar un piloto y medir la frecuencia de consulta; acompañar la adopción con comunicación institucional.                                         |
| R-04 | Los conductores no escanean el QR correctamente.                                  | Operación    | Media        | Alto    | Mitigar              | Probar la subida y la bajada en trayectos reales, capacitar al personal y ofrecer retroalimentación inmediata en la aplicación.                     |
| R-05 | La señal GPS es inestable durante los trayectos.                                  | Técnica      | Media        | Alto    | Mitigar              | Probar el dispositivo en distintos trayectos, mostrar la última posición conocida con marcador de "sin señal" y gestionar la reconexión.            |
| R-06 | Las notificaciones no son oportunas o resultan irrelevantes.                      | Producto     | Media        | Alto    | Mitigar              | Medir los tiempos de generación y entrega, parametrizar los umbrales y controlar la frecuencia de los avisos.                                       |
| R-07 | El proceso de transporte varía entre instituciones.                               | Producto     | Media        | Medio   | Mitigar              | Validar el flujo con cada institución antes de estandarizar y mantener parámetros configurables.                                                    |
| R-08 | La carga inicial de datos de la institución se retrasa o llega incompleta.        | Datos        | Media        | Alto    | Mitigar              | Ejecutar la carga de forma asistida y validar los datos antes de las pruebas de aceptación.                                                         |
| R-09 | Los usuarios no perciben valor en la consulta de ubicación.                       | Producto     | Media        | Medio   | Mitigar              | Medir el uso durante el piloto y ajustar la experiencia según la evidencia.                                                                         |
| R-10 | La retención de los datos de posicionamiento no se define antes del lanzamiento.  | Cumplimiento | Media        | Medio   | Aceptar y monitorear | Definir la política de retención con el área legal antes de la puesta en marcha.                                                                    |
| R-11 | El alcance crece con funciones ajenas al objetivo de trazabilidad.                | Gestión      | Media        | Medio   | Evitar               | Mantener el alcance acordado y canalizar las nuevas necesidades hacia una versión futura.                                                           |

## 12.3 Riesgos técnicos de la arquitectura

**Tabla 40**. Riesgos técnicos de la arquitectura

| Riesgo técnico                                | Probabilidad típica | Mitigación                                                                                                             |
| --------------------------------------------- | ------------------- | ---------------------------------------------------------------------------------------------------------------------- |
| Cascada de fallos por la caída de un servicio | Media               | Cortacircuitos, tiempos límite, respuestas de respaldo y respuesta 503 cuando una dependencia crítica está caída.      |
| Inconsistencia de datos entre servicios       | Alta                | Consumidores idempotentes, eventos con orden por clave y patrones de compensación en asignaciones de varios servicios. |
| Latencia en llamadas síncronas                | Alta                | Uso de gRPC para la telemetría, caché en el borde y procesamiento asíncrono donde es posible.                          |
| Pérdida de mensajes en el broker              | Media               | Entrega al menos una vez con consumidores idempotentes y reintentos controlados.                                       |
| Evolución del esquema que rompe consumidores  | Alta                | Versionado aditivo de eventos y pruebas de contrato antes de liberar.                                                  |
| Acumulación de deuda técnica                  | Alta                | Definición de terminado con cobertura mínima, revisiones periódicas y registro priorizado de la deuda.                 |
| Exposición de datos sensibles en registros    | Media               | Política de registro, enmascaramiento de datos personales y análisis estático de seguridad.                            |
| Deriva de configuración entre entornos        | Alta                | Infraestructura como código, variables de entorno y plantillas verificadas por el pipeline.                            |
| Sobreingeniería prematura                     | Media               | Construir simple, medir antes de optimizar y priorizar la confiabilidad del flujo central.                             |

## 12.4 Preguntas abiertas que condicionan decisiones

**Tabla 41**. Preguntas abiertas que condicionan decisiones

| ID   | Pregunta                                                                                 | Responsable           | Necesaria antes de   |
| ---- | ---------------------------------------------------------------------------------------- | --------------------- | -------------------- |
| Q-01 | ¿Cuántos días se retienen los datos de posicionamiento?                                  | Producto y área legal | Puesta en marcha     |
| Q-02 | ¿Qué reportes con datos personales pueden salir de los roles administrativos?            | Seguridad             | Puesta en marcha     |
| Q-03 | ¿Cuáles son los objetivos de punto y tiempo de recuperación por esquema?                 | Operación             | Puesta en marcha     |
| Q-04 | ¿Qué tolerancia fuera de línea debe soportar el cálculo de tiempos estimados de llegada? | Dominio               | Siguiente incremento |

# 13 Criterios de éxito

## 13.1 Resultados clave

**Tabla 42**. Resultados clave y formas de medición

| KR  | Indicador                             | Meta                                                                          | Forma de medición                                                 |
| --- | ------------------------------------- | ----------------------------------------------------------------------------- | ----------------------------------------------------------------- |
| KR1 | Disponibilidad de la plataforma       | SLO de 99,9 % mensual                                                         | Comprobaciones de salud y métricas de la puerta de enlace         |
| KR2 | Latencia de la API en puntos críticos | P95 < 300 ms                                                                  | Pruebas de carga con k6 en el entorno de pruebas antes de liberar |
| KR3 | Cobertura de rutas monitoreadas       | Al menos el 95 % de las rutas activas con posición disponible                 | Datos de posicionamiento recibidos durante la ejecución           |
| KR4 | Tasa de notificaciones entregadas     | Al menos el 95 % de los eventos relevantes generan y entregan su notificación | Registros de eventos y de entrega                                 |
| KR5 | Registro de bordadas sin incidencias  | Al menos el 95 % de las subidas y bajadas registradas por QR sin incidencias  | Registros de bordada generados por el sistema                     |
| KR6 | Adopción por rol                      | Usuarios activos mensuales por rol, con meta a fijar durante el piloto        | Métricas de autenticación y actividad por rol                     |

## 13.2 Indicadores complementarios

**Tabla 43**. Indicadores complementarios de éxito

| Indicador                                                               | Meta                                                                               | Observación                                                                         |
| ----------------------------------------------------------------------- | ---------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------- |
| Información de transporte gestionada en la plataforma                   | Al menos el 90 % de los registros operativos                                       | Sustituye procesos manuales y dispersos.                                            |
| Métrica norte: trayectos con eventos críticos registrados y disponibles | Cobertura creciente por trayecto                                                   | Integra posición del bus y bordadas disponibles para los usuarios correspondientes. |
| Oportunidad de extremo a extremo                                        | Del evento de bordada a la notificación del acudiente en menos de 5 segundos (P95) | Objetivo operativo del flujo de notificación.                                       |
| Reducción de incidentes de información no disponible                    | Reducción frente a la línea base del piloto                                        | Se mide con los registros de soporte y de incidencias.                              |

## 13.3 Criterios de aceptación de la implementación

El sistema se considera aceptado cuando la batería de pruebas en el entorno de pruebas se supera —unitarias, de integración, de extremo a extremo, de contrato, de carga y de seguridad—, el objetivo de disponibilidad y la latencia P95 se verifican, las pruebas de humo son satisfactorias en cada entorno, la revisión de seguridad no reporta hallazgos críticos pendientes y la institución aprueba formalmente el alcance entregado.

# 14 Conclusiones y próximos pasos

## 14.1 Conclusiones

La propuesta resuelve un problema concreto y verificable: la ausencia de una fuente única y oportuna de información sobre el transporte escolar. La combinación de seguimiento GPS, registro QR de subida y bajada, notificaciones automáticas, panel administrativo y aplicaciones móviles convierte un proceso manual y fragmentado en un proceso trazable, auditable y centrado en la seguridad del estudiante.

La arquitectura elegida —microservicios con orientación a eventos, puerta de enlace única, esquema por servicio y clientes web y móviles— ofrece el equilibrio adecuado entre velocidad de entrega, integridad de los datos y capacidad de crecimiento. Las decisiones de seguridad se incorporan desde el diseño: autenticación con firma asimétrica, autorización por rol con denegación por defecto, auditoría de acciones críticas y tratamiento responsable de los datos de menores.

El plan por fases y el cronograma propuesto permiten entregar valor de forma progresiva. Los hitos verificables —entorno base, cuentas y rutas, operación de viaje, visibilidad del acudiente y puesta en marcha— dan a la institución puntos de control claros. La estimación de esfuerzo se presenta como rango y se ajusta cuando se confirman las dependencias externas, en particular los proveedores de GPS y de notificaciones.

## 14.2 Próximos pasos

1. Aprobar el alcance, los objetivos y el cronograma propuestos, con la definición de la taxonomía de rutas y excepciones.
2. Iniciar la gestión de los contratos con el proveedor de GPS y el proveedor de notificaciones, con antelación a la fase de construcción.
3. Cerrar las preguntas abiertas sobre retención de datos de posicionamiento, reportes con datos personales y objetivos de recuperación.
4. Acordar con la institución el plan de carga inicial de datos y los criterios de aceptación.
5. Conformar el equipo con las dedicaciones estimadas y habilitar los entornos de desarrollo y de pruebas.
6. Iniciar la fase de análisis y validación, con revisión de avance al cierre de cada hito.