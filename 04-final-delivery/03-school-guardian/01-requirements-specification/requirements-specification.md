# Informe de Especificación de Requisitos de Software

**Guardian Escolar**

*Especificación de requisitos del sistema Guardian Escolar — plataforma de transporte escolar seguro*

**Integrantes del proyecto:**

- Johan Smith Santamaria Fernández
- Sharik Dayanna Rojas Ibarra
- Juan Pablo Chala Ramírez

**Programa:** Tecnólogo en Análisis y Desarrollo de Software
**Ficha:** 3145556
**Centro de formación:** Sede La Industria, La Empresa y Los Servicios - SENA
**Lugar:** Neiva, Huila, Colombia
**Fecha:** 6 de octubre de 2026

---

## Tabla de contenido

1. [Introducción](#1-introducción)
2. [Descripción general](#2-descripción-general)
3. [Requisitos funcionales](#3-requisitos-funcionales)
4. [Requisitos no funcionales](#4-requisitos-no-funcionales)
5. [Requisitos de interfaz](#5-requisitos-de-interfaz)
6. [Requisitos de datos](#6-requisitos-de-datos)
7. [Historias de usuario](#7-historias-de-usuario)
8. [Criterios de aceptación generales y estrategia de verificación](#8-criterios-de-aceptación-generales-y-estrategia-de-verificación)
9. [Glosario](#9-glosario)
10. [Anexo: matriz de trazabilidad HU↔RF](#10-anexo-matriz-de-trazabilidad-hurf)
11. [Referencias](#11-referencias)

---

# 1. Introducción

## 1.1 Propósito

El presente informe especifica los requisitos del software **Guardian Escolar**, plataforma web y móvil para el monitoreo y la seguridad del transporte escolar. La especificación establece, de manera verificable, qué debe hacer el sistema, con qué cualidades debe operar, cómo se comunica con sus componentes y con sus usuarios, y qué datos administra.

Esta especificación se redacta conforme a la estructura recomendada por la norma **IEEE 830** para especificaciones de requisitos de software (SRS) y toma como modelo de calidad de producto el modelo de características de uso de **ISO/IEC 25010**. Su audiencia es el equipo de diseño, construcción y verificación del software, el instructor responsable del proyecto y la institución educativa que reciba la solución.

Cada requisito de este documento es **necesario y suficiente** para orientar la implementación y la verificación posteriores: los requisitos funcionales describen funciones observables y sus reglas de negocio; los requisitos no funcionales fijan métricas medibles; y el anexo de trazabilidad vincula cada historia de usuario con los requisitos que implementa.

## 1.2 Alcance del producto

**Guardian Escolar** es una plataforma centralizada que integra seguimiento GPS, control de bordadas mediante escaneo de códigos QR, notificaciones y alertas en tiempo real, panel administrativo web y aplicaciones móviles para conductor y acudiente. El software comprende:

- Una **aplicación web** construida con Angular 21 y Angular Material 3, dirigida a los perfiles administrativo y de operación de plataforma.
- Una **aplicación móvil** construida con React y Expo, dirigida al conductor, al acudiente y al estudiante.
- **Nueve microservicios de dominio**: identificación y acceso (IAM), gestión de usuarios, gestión escolar, flota, rutas, notificaciones, usos excepcionales, auditoría y configuración.
- Un **API Gateway (Kong OSS)** como único punto de entrada, que termina TLS, aplica límite de tasa y CORS y enruta las rutas versionadas /api/v1.
- Un **bus de eventos** con Apache Kafka en modo KRaft y un **canal gRPC persistente** de baja latencia para la telemetría de geolocalización.
- Una **base de datos relacional única** (SQL Server 2022) con esquema por servicio y migraciones versionadas con Liquibase.

No forman parte del alcance de esta especificación los siguientes elementos, deliberadamente excluidos:

**Tabla 2**. _Elementos excluidos del alcance del producto_

| **Elemento fuera de alcance**                                                               | **Motivo**                                                             |
| ------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------- |
| Cobros, facturación o pago del servicio de transporte escolar                               | Ajeno al propósito de seguridad y monitoreo                            |
| Mantenimiento mecánico, combustible o taller de la flota                                    | No constituye un flujo de usuario confirmado                           |
| Gestión académica (calificaciones, asistencia, aprendizaje)                                 | La plataforma gestiona transporte, no la vida académica                |
| Transporte público y rutas no escolares                                                     | El dominio se limita al transporte escolar                             |
| Compra, administración o instalación física de dispositivos GPS                             | Responsabilidad externa al software                                    |
| Respuesta de emergencias autónoma o despacho automático                                     | La respuesta humana e institucional es externa                         |
| Identificación biométrica o reconocimiento facial                                           | No requerida para el alcance actual y eleva obligaciones de privacidad |
| Localización regulatoria multinacional                                                      | El contexto inicial es Neiva (Huila), Colombia                         |
| Navegación autónoma del conductor y generación de rutas óptimas por inteligencia artificial | Fuera del alcance del producto actual                                  |

El sistema **no ofrece autorregistro**: todas las cuentas de usuario las crea el administrador de la institución. No existe aplicación de escritorio; el acceso se realiza mediante el navegador web y la aplicación móvil.

## 1.3 Definiciones y acrónimos

**Tabla 3**. _Definiciones y acrónimos_

| **Término**   | **Definición**                                                                                      |
| ------------- | --------------------------------------------------------------------------------------------------- |
| SRS           | _Software Requirements Specification_: especificación de requisitos de software.                    |
| RF            | Requisito funcional: función o comportamiento que el sistema debe observar.                         |
| RNF           | Requisito no funcional: cualidad o restricción medible del sistema.                                 |
| HU            | Historia de usuario: descripción breve de una necesidad desde el punto de vista del rol.            |
| Épica         | Agrupación de historias de usuario relacionadas por un mismo valor de negocio.                      |
| API           | Interfaz de programación de aplicaciones.                                                           |
| REST          | Estilo arquitectónico de intercambio de recursos mediante HTTP.                                     |
| gRPC          | Protocolo de comunicación remota de procedimientos de alta eficiencia.                              |
| JWT           | _JSON Web Token_: token firmado usado para autenticación y autorización.                            |
| RBAC          | Control de acceso basado en roles.                                                                  |
| QR            | Código de respuesta rápida bidimensional.                                                           |
| Bordada       | Evento de subida o bajada de un estudiante registrado en el bus.                                    |
| GPS           | Sistema de posicionamiento global.                                                                  |
| Acudiente     | Adulto responsable de uno o más estudiantes autorizado para consultar su información de transporte. |
| SLO           | Objetivo de nivel de servicio.                                                                      |
| P95 / P99     | Percentil 95 o 99 de una distribución de medidas.                                                   |
| RTO / RPO     | Objetivo de tiempo de recuperación / objetivo de punto de recuperación.                             |
| PII           | Información de identificación personal.                                                             |
| Tasa de error | Porcentaje de solicitudes que terminan en respuesta del servidor.                                   |

## 1.4 Referencias normativas

Las siguientes normas y marcos regulatorios son de aplicación obligatoria o de referencia en esta especificación:

**Tabla 4**. _Normas y marcos regulatorios de referencia_

| **Referencia**                                     | **Objeto**                                                                                    |
| -------------------------------------------------- | --------------------------------------------------------------------------------------------- |
| IEEE 830-1998                                      | Guía para la elaboración de especificaciones de requisitos de software.                       |
| ISO/IEC 25010:2011                                 | Modelo de características de calidad de producto de software.                                 |
| ISO/IEC/IEEE 12207                                 | Procesos del ciclo de vida del software.                                                      |
| ISO/IEC 29119                                      | Normas de pruebas de software.                                                                |
| OWASP Top 10                                       | Riesgos críticos de seguridad en aplicaciones web.                                            |
| Ley 1581 de 2012 y Decreto 1377 de 2013 (Colombia) | Protección de datos personales y habeas data, con tratamiento especial para datos de menores. |
| Reglamento General de Protección de Datos (GDPR)   | Aplicable si el producto se ofrece en la Unión Europea.                                       |

# 2. Descripción general

## 2.1 Perspectiva y características del producto

Guardian Escolar resuelve la falta de una visión confiable y oportuna de la ruta del bus escolar: los acudientes no saben si el estudiante subió o bajó, los conductores registran la información de forma manual y, ante incidentes, no existe trazabilidad ni alertas oportunas. La solución sustituye esos procesos manuales por una plataforma única que conecta a familias, estudiantes, conductores, administradores y operadores de plataforma en torno a la operación de transporte escolar.

El producto se apoya en las siguientes decisiones estructurales:

- Arquitectura de **microservicios orientada a eventos** detrás de un API Gateway (Kong OSS) en el puerto **8000**; los servicios de negocio atienden en el puerto **8080**.
- **Base de datos relacional única** (SQL Server 2022, puerto **1433**) con esquema por servicio y migraciones Liquibase; ningún servicio accede directamente al esquema de otro servicio.
- **Apache Kafka 4.1.0** en modo KRaft (puerto **9092**) para los eventos de alto nivel del dominio y **gRPC** en el puerto **5001** para el flujo de telemetría de geolocalización de baja latencia.
- Autenticación con **JWT RS256**: token de acceso de **15 minutos** y token de actualización de **30 días**; solo el servicio de identificación y acceso posee la llave privada.
- Frontales: **aplicación web Angular 21 + Angular Material 3** y **aplicación móvil React + Expo** (notificaciones push Expo y cámara para escaneo QR).

```
  Acudiente / Estudiante / Conductor            Administrador / Operador
        (aplicación móvil React + Expo)              (aplicación web Angular)
                      │                                        │
                      └───────────────┬────────────────────────┘
                                      │ HTTPS · /api/v1
                                      ▼
                        ┌─────────────────────────────┐
                        │  API Gateway (Kong) :8000   │
                        │  TLS · CORS · límite de tasa│
                        │  JWT RS256 · ruteo          │
                        └──────────────┬──────────────┘
                                       │ REST/JSON :8080
        ┌──────────┬──────────┬────────┼────────┬──────────┬──────────┐
        ▼          ▼          ▼        ▼        ▼          ▼          ▼
      IAM     Gestión de  Gestión   Flota    Rutas   Notificaciones  …
                        usuarios  escolar
        └──────────┴──────────┴────────┬────────┴──────────┴──────────┘
                                       │
              ┌────────────────────────┼────────────────────────┐
              ▼                        ▼                        ▼
      SQL Server :1433          Kafka :9092              gRPC :5001
     (esquema por servicio)  (eventos de dominio)   (posiciones GPS)
```

**Figura 1**. _Vista general de la arquitectura de la plataforma_

La tabla siguiente resume las características del producto y su estado de satisfacción previsto, conforme al modelo de características de uso de ISO/IEC 25010:

**Tabla 5**. _Características de calidad del producto y su manifestación_

| **Característica**   | **Manifestación en el producto**                                                             |
| -------------------- | -------------------------------------------------------------------------------------------- |
| Adecuación funcional | Cobertura de los 30 requisitos funcionales de la sección 3.                                  |
| Rendimiento          | Latencia y tiempos de entrega fijados en la sección 4.1.                                     |
| Compatibilidad       | Integración mediante REST, gRPC, eventos y notificación móvil push.                          |
| Usabilidad           | Interfaces por rol, navegación lateral, formularios validados y retroalimentación inmediata. |
| Fiabilidad           | Disponibilidad SLO del 99,9 %, recuperación ante fallas y alertas de pérdida de señal GPS.   |
| Seguridad            | Autenticación JWT RS256, autorización por rol, auditoría y protección de datos personales.   |
| Mantenibilidad       | Cobertura de pruebas, complejidad controlada y migraciones versionadas.                      |
| Portabilidad         | Despliegue contenedorizado independiente del sistema operativo anfitrión.                    |

El producto se orienta a tres objetivos: (1) garantizar la trazabilidad en tiempo real de cada trayecto escolar; (2) notificar a los acudientes el estado de subida y bajada de sus estudiantes; (3) ofrecer a la institución un panel de gestión de flota, rutas y usuarios con roles.

## 2.2 Clases de usuario

El sistema define cinco roles, codificados como valores de enumeración. Cada rol se autentica y solo accede a las funciones y recursos autorizados por su rol y permisos:

**Tabla 6**. _Clases de usuario y funciones por rol_

| **Rol** | **Denominación** | **Perfil y funciones principales** | **Interfaz** |
| --- | --- | --- | --- |
| `SUPER_ADMIN` | Operador de plataforma | Gestiona instituciones, configuración global y auditoría de la plataforma. | Web |
| `ADMIN` | Administrador escolar | Gestiona sedes, cursos, personas, flota, rutas y reportes de su institución. | Web |
| `DRIVER` | Conductor | Ejecuta rutas, confirma bordadas mediante escaneo QR y recibe alertas en la aplicación móvil. | Móvil |
| `PARENT` | Acudiente | Sigue la ruta, recibe notificaciones de subida y bajada y consulta el historial de bordadas. | Móvil |
| `STUDENT` | Estudiante | Consulta su identificación QR vinculada al perfil para bordar. | Móvil |

Los actores externos que interactúan con el sistema, sin ser usuarios autenticados, son el dispositivo GPS embarcado que emite posiciones y el proveedor de notificaciones push. La institución educativa, las familias y los servicios de emergencia permanecen fuera del sistema como partes que actúan sobre la información que este le entrega.

## 2.3 Restricciones

**Tabla 7**. _Restricciones del proyecto_

| **Tipo**          | **Restricción**                                                                                                                                                                                                                         |
| ----------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Académica         | El proyecto se desarrolla en el programa de Análisis y Desarrollo de Software, ficha de programación 3145556, del Centro de formación de Neiva (Huila), Servicio Nacional de Aprendizaje.                                               |
| Geográfica        | El contexto operativo inicial es Neiva (Huila), Colombia.                                                                                                                                                                               |
| Tecnológica       | Frontal web en Angular 21 con Angular Material 3; aplicación móvil en React con Expo; servicios en Java 21 con Spring Boot (identificación y acceso) y en C# con .NET 10 (resto de servicios de dominio).                               |
| Arquitectónica    | Estilo microservicios con orientación a eventos detrás de un API Gateway; base de datos relacional única con esquema por servicio; migraciones obligatorias con Liquibase en cada arranque.                                             |
| De comunicaciones | Interfaz sincrónica versionada /api/v1 sobre REST/JSON; gRPC en el puerto 5001 para telemetría; eventos en Apache Kafka en modo KRaft (Kafka sin ZooKeeper).                                                                            |
| De seguridad      | Autenticación JWT RS256 con token de acceso de 15 minutos y token de actualización de 30 días; llave privada exclusiva del servicio de identificación y acceso; mínimo privilegio por rol; secretos únicamente en variables de entorno. |
| Regulatoria       | Cumplimiento de la Ley 1581 de 2012 y el Decreto 1377 de 2013, con tratamiento especial y consentimiento para datos de menores; GDPR si se ofrece el producto en la Unión Europea.                                                      |
| De explotación    | Despliegue en contenedores Docker y orquestación mediante Docker Compose; entornos de desarrollo local, pruebas y producción.                                                                                                           |
| De idioma         | Interfaz con al menos cuatro idiomas (español, inglés, francés y portugués) y español como idioma principal de la primera versión.                                                                                                      |
| De proceso        | Especificación, diseño, construcción, pruebas y despliegue conducidos con metodología ágil; toda modificación de alcance se documenta y aprueba antes de su construcción.                                                               |

## 2.4 Supuestos y dependencias

**Tabla 8**. _Supuestos y dependencias del proyecto_

| **#** | **Supuesto o dependencia**                                                                                                | **Tipo**    | **Consecuencia si no se cumple**                                               |
| ----- | ------------------------------------------------------------------------------------------------------------------------- | ----------- | ------------------------------------------------------------------------------ |
| 1     | Las instituciones y familias proveen datos correctos de estudiantes, acudientes, conductores, vehículos, rutas y paradas. | Supuesto    | Las asignaciones de ruta y las notificaciones de seguridad serían incorrectas. |
| 2     | Los usuarios disponen de un navegador web compatible o de un dispositivo móvil con cámara.                                | Supuesto    | Se requeriría un canal asistido o sin conexión.                                |
| 3     | SQL Server está disponible para la persistencia del proyecto.                                                             | Supuesto    | Cambiarían la arquitectura de persistencia y el plan de despliegue.            |
| 4     | La interfaz de programación expone los datos requeridos por los frontales web y móvil.                                    | Supuesto    | Cambiarían los contratos, los flujos y las fechas de entrega de los frontales. |
| 5     | Un proveedor de posicionamiento expone la ubicación y el estado del vehículo cuando el monitoreo está activo.             | Supuesto    | Permanecerían indisponibles las funciones de ubicación en tiempo real.         |
| 6     | El envío de notificaciones está disponible mediante el proveedor o servicio configurado.                                  | Supuesto    | Las notificaciones quedarían limitadas a la aplicación o a datos simulados.    |
| 7     | Los roles y permisos están definidos de forma coherente entre frontales y servicios.                                      | Supuesto    | Podrían ocurrir accesos no autorizados y paneles inconsistentes.               |
| 8     | El despliegue inicial se orienta a Neiva (Huila), Colombia.                                                               | Supuesto    | Deberían revisarse los supuestos de datos, idioma, regulación y operación.     |
| 9     | Entorno SQL Server disponible antes de las pruebas integradas.                                                            | Dependencia | No sería posible la verificación integrada.                                    |
| 10    | Definición del proveedor de GPS y de su contrato antes del monitoreo en vivo.                                             | Dependencia | Permanecería inactiva la visualización de ubicación en tiempo real.            |
| 11    | Definición del servicio de notificación push antes de la producción.                                                      | Dependencia | Las notificaciones no alcanzarían a los dispositivos.                          |
| 12    | Datos escolares y de transporte válidos antes de las pruebas de aceptación.                                               | Dependencia | No sería posible validar los flujos de negocio reales.                         |
| 13    | Contrato de roles y autorización alineado entre los equipos de servicios y de frontales.                                  | Dependencia | Se bloquearía la publicación con control de acceso.                            |

# 3. Requisitos funcionales

Esta sección especifica **30 requisitos funcionales** organizados en **10 grupos funcionales**. Cada requisito indica su identificador, su denominación, una descripción detallada, sus entradas y salidas, sus precondiciones y postcondiciones y las reglas de negocio que lo gobiernan. La prioridad utiliza la escala Alta, Media y Baja según el valor de negocio y la dependencia operativa.

**Tabla 9**. _Resumen de los grupos de requisitos funcionales_

| **Grupo** | **Denominación**                                   | **Responsable funcional**                                        | **RF** | **Prioridad dominante** |
| --------- | -------------------------------------------------- | ---------------------------------------------------------------- | ------ | ----------------------- |
| RF-01     | Gestión de usuarios                                | Servicio de gestión de usuarios e identificación y acceso        | 4      | Alta                    |
| RF-02     | Autenticación                                      | Servicio de identificación y acceso (IAM)                        | 2      | Alta                    |
| RF-03     | Gestión de rutas escolares, flota y asignaciones   | Servicio de rutas y servicio de flota                            | 8      | Alta                    |
| RF-04     | Gestión del escaneo QR: control de subida y bajada | Servicio de identificación y acceso y servicio de notificaciones | 3      | Alta                    |
| RF-05     | Notificaciones de bordada                          | Servicio de notificaciones                                       | 4      | Alta                    |
| RF-06     | Rastreo en tiempo real del bus                     | Módulo de geolocalización y canal gRPC                           | 3      | Media                   |
| RF-07     | Gestión de viajes                                  | Servicio de rutas                                                | 1      | Media                   |
| RF-08     | Incidentes                                         | Servicio de notificaciones                                       | 1      | Alta                    |
| RF-09     | Consulta de rutas                                  | Servicio de rutas                                                | 2      | Media                   |
| RF-10     | Personalización                                    | Aplicaciones cliente web y móvil                                 | 2      | Baja                    |

## 3.1 Grupo RF-01 — Gestión de usuarios (User Management)

Este grupo agrupa el ciclo de vida de las cuentas del ecosistema escolar: creación, actualización, activación y desactivación, y vínculo de estudiantes con su acudiente. La cuenta se divide en dos responsabilidades: los datos de la persona pertenecen al servicio de gestión de usuarios; el perfil de autenticación, con su rol y su hash de contraseña, pertenece al servicio de identificación y acceso.

### 3.1.1 RF-01.1 — Creación de cuentas con rol asignado

El administrador crea cuentas de estudiantes, conductores, acudientes y administradores asignando en el mismo acto el rol que definirá sus permisos. La contraseña inicial la genera el sistema y se entrega al usuario por un canal seguro; el rol se guarda junto con el perfil y habilita las funciones correspondientes desde el primer inicio de sesión.

**Tabla 10**. _Especificación del requisito RF-01.1: creación de cuentas con rol asignado_

| **Campo**       | **Detalle**                                                                                                                                                                                                                               |
| --------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Entradas        | Tipo y número de documento, nombre completo, correo electrónico, teléfono, dirección, fecha de nacimiento y rol a asignar (ADMIN, DRIVER, PARENT, STUDENT); datos académicos del estudiante y vínculo estudiante–acudiente cuando aplica. |
| Salidas         | Cuenta creada y activa con el rol asignado; mensaje de error si el correo o el documento ya existen; registro de auditoría de la creación.                                                                                                |
| Precondiciones  | El solicitante está autenticado con el rol ADMIN o SUPER_ADMIN y cuenta con el permiso de gestión de usuarios.                                                                                                                            |
| Postcondiciones | La cuenta existe, está activa y puede iniciar sesión con la contraseña inicial; el evento de alta del usuario queda publicado para su difusión en el sistema.                                                                             |

**Reglas de negocio**

- No existe autorregistro: todas las cuentas las crea el administrador de la institución; la cuenta de administrador se crea directamente por el equipo de implantación.
- El correo electrónico y el número de documento son únicos en el sistema; si ya existen, se informa que la cuenta está registrada y no se duplica el registro.
- Un estudiante queda vinculado a una sola cuenta de acudiente activa a la vez.
- Todo campo obligatorio ausente impide la creación y el sistema conserva los datos digitados para su corrección.

### 3.1.2 RF-01.2 — Actualización de datos de la cuenta

El administrador actualiza los datos personales de la cuenta (nombre, documento, correo, teléfono, dirección, fecha de nacimiento) y los datos académicos del estudiante, sin alterar el historial de bordadas ni las relaciones ya establecidas.

**Tabla 11**. _Especificación del requisito RF-01.2: actualización de datos de la cuenta_

| **Campo**       | **Detalle**                                                                                       |
| --------------- | ------------------------------------------------------------------------------------------------- |
| Entradas        | Identificador de la cuenta y conjunto de campos a modificar.                                      |
| Salidas         | Cuenta actualizada con fecha de última modificación; mensaje de error ante conflicto de unicidad. |
| Precondiciones  | Cuenta existente y solicitante autenticado con permiso de gestión de usuarios.                    |
| Postcondiciones | Los datos vigentes reflejan la última modificación; el evento de actualización queda publicado.   |

**Reglas de negocio**

- La fecha de creación es inmutable; la fecha de última modificación se actualiza automáticamente.
- Si el correo o el documento modificado ya pertenecen a otra cuenta, la operación se rechaza sin alterar el registro.
- Los cambios sobre personas se registran en la auditoría de acciones críticas.

### 3.1.3 RF-01.3 — Activación y desactivación de cuentas

El administrador activa o desactiva cuentas. La desactivación impide el inicio de sesión y bloquea cualquier asignación posterior del usuario a rutas, buses o viajes, conservando su historial.

**Tabla 12**. _Especificación del requisito RF-01.3: activación y desactivación de cuentas_

| **Campo**       | **Detalle**                                                                                    |
| --------------- | ---------------------------------------------------------------------------------------------- |
| Entradas        | Identificador de la cuenta y estado objetivo (ACTIVE / INACTIVE).                              |
| Salidas         | Estado de la cuenta actualizado; rechazo del inicio de sesión si la cuenta está desactivada.   |
| Precondiciones  | Cuenta existente; solicitante autenticado con permiso correspondiente.                         |
| Postcondiciones | Una cuenta INACTIVE no puede autenticarse ni ser asignada; su historial permanece consultable. |

**Reglas de negocio**

- El ciclo de vida de las entidades se rige por los estados ACTIVE e INACTIVE, es decir, baja lógica y no eliminación física.
- La desactivación bloquea el acceso sin eliminar el historial del usuario.
- Un perfil INACTIVE no puede iniciar una sesión autenticada nueva.

### 3.1.4 RF-01.4 — Vinculación de estudiantes a la cuenta de un acudiente

El administrador vincula uno o más estudiantes a la cuenta de un acudiente para que este pueda seguir a todos sus hijos desde una sola cuenta.

**Tabla 13**. _Especificación del requisito RF-01.4: vinculación de estudiantes a la cuenta de un acudiente_

| **Campo**       | **Detalle**                                                                                                |
| --------------- | ---------------------------------------------------------------------------------------------------------- |
| Entradas        | Identificador de la cuenta de acudiente y identificadores de los estudiantes a vincular.                   |
| Salidas         | Relación acudiente–estudiante registrada; mensaje de error si el estudiante ya está asociado.              |
| Precondiciones  | Cuentas de acudiente y de estudiante existentes y activas; solicitante con permiso de gestión de usuarios. |
| Postcondiciones | El acudiente visualiza la información y las notificaciones del estudiante vinculado.                       |

**Reglas de negocio**

- Un estudiante solo puede estar vinculado a una cuenta de acudiente activa a la vez.
- La cuenta de acudiente debe existir previamente para recibir vínculos.
- El vínculo habilita la consulta de la ruta del estudiante y la recepción de sus notificaciones.

## 3.2 Grupo RF-02 — Autenticación (Authentication)

Este grupo especifica el acceso al sistema. El servicio de identificación y acceso emite tokens JWT firmados con el algoritmo RS256 y aplica el control de acceso por rol; el token de acceso vence a los 15 minutos y el token de actualización a los 30 días.

### 3.2.1 RF-02.1 — Autenticación con credenciales y emisión de token

El sistema autentica al usuario con sus credenciales conforme a la política de contraseñas vigente y, si son válidas y la cuenta está activa, emite un token de acceso firmado que limita la sesión a las funciones permitidas por su rol.

**Tabla 14**. _Especificación del requisito RF-02.1: autenticación con credenciales y emisión de token_

| **Campo**       | **Detalle**                                                                                                               |
| --------------- | ------------------------------------------------------------------------------------------------------------------------- |
| Entradas        | Correo electrónico y contraseña según la política (longitud, mayúsculas, números, símbolos y expiración en días).         |
| Salidas         | Token de acceso JWT RS256 con vigencia de 15 minutos y claim de rol; error «credenciales inválidas» sin emisión de token. |
| Precondiciones  | Cuenta creada por el administrador y en estado ACTIVE.                                                                    |
| Postcondiciones | Sesión iniciada y registrada con dirección IP, fechas y aplicación; eventos de alta de perfil y sesión quedan publicados. |

**Reglas de negocio**

- Las credenciales incorrectas o la cuenta desactivada producen rechazo y no emiten token.
- El algoritmo de firma es RS256 y solo el servicio de identificación y acceso posee la llave privada.
- El control de acceso por rol se aplica de forma predeterminada: sin rol y permiso explícito, no hay acceso.

### 3.2.2 RF-02.2 — Cierre de sesión y renovación del token

El usuario cierra la sesión y, mientras mantenga vigente su token de actualización, renueva el token de acceso sin volver a digitar sus credenciales.

**Tabla 15**. _Especificación del requisito RF-02.2: cierre de sesión y renovación del token_

| **Campo**       | **Detalle**                                                                                                            |
| --------------- | ---------------------------------------------------------------------------------------------------------------------- |
| Entradas        | Token de actualización vigente (30 días) para renovación; token de acceso para el cierre de sesión.                    |
| Salidas         | Nuevo token de acceso de 15 minutos; sesión cerrada y su registro finalizado.                                          |
| Precondiciones  | Sesión iniciada previamente y cuenta aún activa.                                                                       |
| Postcondiciones | El token de acceso anterior queda invalidado tras el cierre; la sesión registrada almacena sus fechas de inicio y fin. |

**Reglas de negocio**

- La renovación se rechaza si el token de actualización está vencido, fue revocado o la cuenta está INACTIVE.
- Las sesiones se registran con dirección IP y fechas para su consulta y auditoría.
- Las rutas de autenticación y de renovación son las únicas públicas; el resto de la interfaz exige token Bearer válido.

## 3.3 Grupo RF-03 — Gestión de rutas escolares, flota y asignaciones (School Route Management)

Este grupo concentra la configuración operativa del transporte: definición de rutas con paradas y horarios, administración de la flota de buses y establecimiento de las asignaciones de bus, conductor y estudiantes. Corresponde al servicio de rutas la configuración de rutas y sus asignaciones, y al servicio de flota la administración de buses y de la responsabilidad del conductor sobre cada bus.

### 3.3.1 RF-03.1 — Creación de una ruta con datos, paradas ordenadas y horarios

El administrador crea una ruta escolar ingresando sus datos, sus paradas en orden de itinerario y sus horarios, de modo que conductores, estudiantes y familias dispongan de un itinerario definido y consistente.

**Tabla 16**. _Especificación del requisito RF-03.1: creación de una ruta con datos, paradas ordenadas y horarios_

| **Campo**       | **Detalle**                                                                                                                                                      |
| --------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Entradas        | Nombre y sector de la ruta, sede de pertenencia, paradas con dirección y coordenadas, orden de paradas, día de semana, dirección del trayecto y hora programada. |
| Salidas         | Ruta registrada con sus paradas ordenadas y sus horarios; ruta visible en el listado.                                                                            |
| Precondiciones  | Sede y datos maestros de la institución disponibles; solicitante autenticado como administrador.                                                                 |
| Postcondiciones | La ruta existe en estado utilizable y sus paradas conservan el orden de itinerario.                                                                              |

**Reglas de negocio**

- Una ruta debe pertenecer a una sede de una institución.
- Las paradas se ordenan por itinerario, nunca por orden de creación, y no pueden repetirse en la misma posición.
- No se puede activar una ruta que no tenga su configuración requerida completa.

### 3.3.2 RF-03.2 — Validación de unicidad de la ruta

Antes de persistir una ruta, el sistema valida que no exista otra ruta con el mismo nombre en el ámbito correspondiente.

**Tabla 17**. _Especificación del requisito RF-03.2: validación de unicidad de la ruta_

| **Campo**       | **Detalle**                                                            |
| --------------- | ---------------------------------------------------------------------- |
| Entradas        | Nombre propuesto para la ruta.                                         |
| Salidas         | Ruta persistida, o mensaje «la ruta ya existe» sin registro duplicado. |
| Precondiciones  | Solicitud de creación de ruta recibida.                                |
| Postcondiciones | No existen dos rutas con el mismo nombre.                              |

**Reglas de negocio**

- La unicidad se evalúa por nombre de ruta antes de cualquier escritura.
- El intento de duplicado se informa al usuario y conserva los datos digitados.

### 3.3.3 RF-03.3 — Actualización de datos de la ruta

El administrador modifica los datos de una ruta existente, sus paradas y sus horarios, manteniendo la coherencia de las asignaciones vigentes.

**Tabla 18**. _Especificación del requisito RF-03.3: actualización de datos de la ruta_

| **Campo**       | **Detalle**                                                                          |
| --------------- | ------------------------------------------------------------------------------------ |
| Entradas        | Identificador de la ruta y campos a modificar.                                       |
| Salidas         | Ruta actualizada; rechazo si la modificación vulnera unicidad o integridad.          |
| Precondiciones  | Ruta existente y solicitante con permiso.                                            |
| Postcondiciones | Los cambios quedan vigentes y se reflejan en las consultas de conductor y acudiente. |

**Reglas de negocio**

- La actualización no puede introducir paradas duplicadas en la misma posición ni ruptura del orden de itinerario.
- Toda modificación de configuración de ruta queda registrada en auditoría.

### 3.3.4 RF-03.4 — Eliminación de datos de la ruta

El administrador elimina una ruta cuando ya no opera, salvo que exista un viaje activo que dependa de ella.

**Tabla 19**. _Especificación del requisito RF-03.4: eliminación de datos de la ruta_

| **Campo**       | **Detalle**                                                              |
| --------------- | ------------------------------------------------------------------------ |
| Entradas        | Identificador de la ruta a eliminar.                                     |
| Salidas         | Ruta eliminada, o mensaje de rechazo si hay un viaje activo dependiente. |
| Precondiciones  | Ruta existente; solicitante con permiso.                                 |
| Postcondiciones | La ruta desaparece de la operación sin dejar referencias huérfanas.      |

**Reglas de negocio**

- No se permite eliminar una ruta con viaje activo.
- Las asignaciones asociadas se retiran junto con la ruta para conservar la integridad.

### 3.3.5 RF-03.5 — Asignación y desasignación de un bus a una ruta

El administrador asigna un bus a una ruta y puede desasignarlo, de modo que cada ruta opere con un vehículo disponible.

**Tabla 20**. _Especificación del requisito RF-03.5: asignación y desasignación de un bus a una ruta_

| **Campo**       | **Detalle**                                                                                         |
| --------------- | --------------------------------------------------------------------------------------------------- |
| Entradas        | Identificadores de la ruta y del bus; acción de asignación o desasignación.                         |
| Salidas         | Relación ruta–bus registrada o retirada; estado del bus actualizado a «en ruta» cuando corresponde. |
| Precondiciones  | Ruta activa y bus activo sin asignación vigente incompatible.                                       |
| Postcondiciones | La ruta tiene como máximo un bus asignado y el bus atiende como máximo una ruta.                    |

**Reglas de negocio**

- Un bus se asigna a una sola ruta a la vez y una ruta recibe un solo bus a la vez.
- No se asignan buses en estado INACTIVE.
- La cobertura de rutas del conductor se deriva del bus asignado a la ruta.

### 3.3.6 RF-03.6 — Creación, edición y activación o desactivación de buses

El administrador crea, edita y activa o desactiva buses de la flota, manteniendo la identidad física del vehículo al día para la asignación a rutas.

**Tabla 21**. _Especificación del requisito RF-03.6: creación, edición y activación o desactivación de buses_

| **Campo**       | **Detalle**                                                                                  |
| --------------- | -------------------------------------------------------------------------------------------- |
| Entradas        | Placa, capacidad, modelo y marca, datos del SOAT, dispositivo GPS asociado y estado inicial. |
| Salidas         | Bus registrado con estado disponible; mensaje de error si la placa ya existe.                |
| Precondiciones  | Solicitante autenticado con permiso de gestión de flota.                                     |
| Postcondiciones | El bus aparece en el listado y es elegible para asignación cuando está activo.               |

**Reglas de negocio**

- La placa es única en el sistema y la capacidad debe ser mayor que cero.
- Un bus pertenece a una institución y un dispositivo GPS no puede estar asignado a varios buses simultáneamente.
- Un bus INACTIVE no puede ser asignado a una ruta.

### 3.3.7 RF-03.7 — Asignación y desasignación de un conductor a un bus

El administrador asigna un conductor como responsable de un bus y puede reasignarlo o desasignarlo; las rutas cubiertas por ese bus quedan cubiertas por ese conductor.

**Tabla 22**. _Especificación del requisito RF-03.7: asignación y desasignación de un conductor a un bus_

| **Campo**       | **Detalle**                                                                                       |
| --------------- | ------------------------------------------------------------------------------------------------- |
| Entradas        | Identificador del bus y del conductor; acción de asignación o desasignación.                      |
| Salidas         | Relación conductor–bus registrada con su vigencia; evento de asignación publicado.                |
| Precondiciones  | Conductor con cuenta activa y bus existente; solicitante con permiso.                             |
| Postcondiciones | El conductor responsable del bus puede consultar en la aplicación las rutas cubiertas por su bus. |

**Reglas de negocio**

- Un conductor es responsable de un solo bus a la vez hasta que el administrador lo reasigne o desasigne.
- No existen conductores en estado INACTIVE elegibles para la asignación.
- El conductor llega a sus rutas de forma transversal, a través del bus asignado a cada ruta; no existe una asignación directa conductor–ruta.

### 3.3.8 RF-03.8 — Asignación de estudiantes a una ruta con parada de subida y bajada

El administrador asigna estudiantes a una ruta indicando la parada de subida y la parada de bajada, de modo que el sistema sepa quién espera, en qué parada y a qué hora.

**Tabla 23**. _Especificación del requisito RF-03.8: asignación de estudiantes a una ruta con parada de subida y bajada_

| **Campo**       | **Detalle**                                                                                          |
| --------------- | ---------------------------------------------------------------------------------------------------- |
| Entradas        | Identificador de la ruta, identificador del estudiante y paradas de subida y de bajada.              |
| Salidas         | Relación ruta–estudiante registrada; evento de asignación publicado.                                 |
| Precondiciones  | Ruta activa, estudiante con cuenta activa y paradas seleccionadas pertenecientes a la ruta.          |
| Postcondiciones | El acudiente puede consultar la ruta de su estudiante y el flujo de bordada por QR queda habilitado. |

**Reglas de negocio**

- Las paradas seleccionadas deben pertenecer a la ruta; en caso contrario se rechaza con el mensaje «la parada no pertenece a esta ruta».
- Un estudiante mantiene una sola ruta activa a la vez.
- El estudiante asignado debe pertenecer al contexto escolar de la ruta.

## 3.4 Grupo RF-04 — Gestión del escaneo QR: control de subida y bajada (QR Scanning Management)

Este grupo especifica la identificación del estudiante mediante código QR y el registro de sus bordadas. El código individual lo genera el servicio de identificación y acceso; el registro de subida y bajada lo procesa el servicio de notificaciones, que valida las condiciones operativas del viaje.

### 3.4.1 RF-04.1 — Generación del código QR individual del estudiante

El sistema genera para cada estudiante un código QR individual con identificador único, sujeto a una política de rotación y expiración, y lo muestra en la aplicación móvil para su verificación visual.

**Tabla 24**. _Especificación del requisito RF-04.1: generación del código QR individual del estudiante_

| **Campo**       | **Detalle**                                                                                                                     |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------- |
| Entradas        | Identificador del estudiante con cuenta activa; política de rotación y expiración vigente.                                      |
| Salidas         | Código QR individual con identificador único y nombre del estudiante para verificación.                                         |
| Precondiciones  | Estudiante con perfil ACTIVE y sesión autenticada propia.                                                                       |
| Postcondiciones | El código es consultable mientras esté vigente; al vencer o desactivarse el perfil se informa que el código no está disponible. |

**Reglas de negocio**

- Cada estudiante posee un código individual e identificador único.
- El código se rota o regenera conforme a su política de expiración y cuando se reemplazan las credenciales.
- Solo el propio estudiante autenticado puede consultar su código.

### 3.4.2 RF-04.2 — Lectura del código QR para registrar la subida

Al escanear el código del estudiante, el sistema registra su subida con la hora y la parada del evento, validando que el estudiante esté asignado a la ruta que se opera.

**Tabla 25**. _Especificación del requisito RF-04.2: lectura del código QR para registrar la subida_

| **Campo**       | **Detalle**                                                                                                                                    |
| --------------- | ---------------------------------------------------------------------------------------------------------------------------------------------- |
| Entradas        | Código QR escaneado desde la aplicación del conductor, viaje activo y contexto de la ruta.                                                     |
| Salidas         | Bordada de subida registrada con fecha y hora y parada; evento de escaneo publicado; rechazo con mensaje «estudiante no asignado a esta ruta». |
| Precondiciones  | Conductor autenticado, viaje activo y estudiante asignado a la ruta.                                                                           |
| Postcondiciones | Existe una bordada de subida vigente para el estudiante y queda publicado el evento que dispara la notificación al acudiente.                  |

**Reglas de negocio**

- La subida solo procede si el estudiante está asignado a la ruta en curso.
- La bordada se asocia obligatoriamente a estudiante, bus y parada.
- El escaneo debe ser legible con poca iluminación y soporta breves pérdidas de conectividad mediante cola local con reintento.

### 3.4.3 RF-04.3 — Lectura del código QR para registrar la bajada

Al escanear el código del estudiante en la parada de destino, el sistema registra su bajada con la hora y la ubicación del evento, siempre que exista una bordada de subida activa.

**Tabla 26**. _Especificación del requisito RF-04.3: lectura del código QR para registrar la bajada_

| **Campo**       | **Detalle**                                                                                                                                           |
| --------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------- |
| Entradas        | Código QR escaneado, viaje activo y bordada de subida vigente del estudiante.                                                                         |
| Salidas         | Bordada de bajada registrada con fecha, hora y ubicación; evento de bajada publicado; rechazo con mensaje «no hay bordada activa para el estudiante». |
| Precondiciones  | Bordada de subida activa del estudiante en el viaje en curso.                                                                                         |
| Postcondiciones | La bordada del estudiante queda cerrada y se publica el evento que notifica al acudiente.                                                             |

**Reglas de negocio**

- No se registra una bajada sin una subida activa en el mismo viaje.
- El tipo de bordada solo admite los valores de subida y bajada.
- El registro de la bajada conserva la fecha y hora del evento, distintas de la fecha de creación del registro.

## 3.5 Grupo RF-05 — Notificaciones de bordada (Boarding Notifications)

Este grupo especifica la comunicación automática al acudiente. El servicio de notificaciones consume los eventos de bordada y entrega avisos en tiempo real, además de alertar situaciones anormales del trayecto.

### 3.5.1 RF-05.1 — Detección del evento de subida del estudiante

El sistema detecta y registra el evento de subida del estudiante a partir del escaneo del código QR, como hecho de negocio que dispara las comunicaciones asociadas.

**Tabla 27**. _Especificación del requisito RF-05.1: detección del evento de subida del estudiante_

| **Campo**       | **Detalle**                                                                               |
| --------------- | ----------------------------------------------------------------------------------------- |
| Entradas        | Evento de escaneo proveniente del registro de bordada.                                    |
| Salidas         | Evento de subida detectado y publicado para su tratamiento asincrónico.                   |
| Precondiciones  | Registro de bordada de subida aceptado.                                                   |
| Postcondiciones | El evento queda disponible para el envío de notificación y para el historial de bordadas. |

**Reglas de negocio**

- El evento se deriva exclusivamente de un registro de bordada válido, nunca de una captura manual.
- La detección es idempotente: el mismo evento no genera dos notificaciones.

### 3.5.2 RF-05.2 — Notificación automática de subida al acudiente

El sistema envía automáticamente, en tiempo real, una notificación al acudiente con la hora de subida y la parada correspondiente.

**Tabla 28**. _Especificación del requisito RF-05.2: notificación automática de subida al acudiente_

| **Campo**       | **Detalle**                                                                                     |
| --------------- | ----------------------------------------------------------------------------------------------- |
| Entradas        | Evento de subida, acudientes vinculados al estudiante, plantilla y canal configurado.           |
| Salidas         | Notificación entregada al acudiente con hora de subida y parada; registro de estado de entrega. |
| Precondiciones  | Acudiente vinculado al estudiante y con dispositivo habilitado para recibir notificaciones.     |
| Postcondiciones | La notificación queda registrada con su estado de entrega y su fecha.                           |

**Reglas de negocio**

- Solo se notifica a los acudientes vinculados al estudiante que protagoniza el evento.
- El envío es automático: no requiere intervención del conductor ni del administrador.
- Los reintentos no duplican la notificación original.

### 3.5.3 RF-05.3 — Notificación automática de bajada al acudiente

El sistema envía automáticamente, en tiempo real, una notificación al acudiente con la hora de bajada y la ubicación del evento.

**Tabla 29**. _Especificación del requisito RF-05.3: notificación automática de bajada al acudiente_

| **Campo**       | **Detalle**                                                                           |
| --------------- | ------------------------------------------------------------------------------------- |
| Entradas        | Evento de bajada, acudientes vinculados, plantilla y canal configurado.               |
| Salidas         | Notificación entregada con hora de bajada y ubicación; registro de estado de entrega. |
| Precondiciones  | Bajada registrada con éxito y acudiente vinculado.                                    |
| Postcondiciones | El acudiente conoce la hora y el lugar en que su estudiante bajó del bus.             |

**Reglas de negocio**

- La ubicación comunicada corresponde a la parada o punto registrado en la bordada.
- El mensaje incluye el dato de forma legible, sin exponer información de otros estudiantes.

### 3.5.4 RF-05.4 — Alerta por detención prolongada del bus

Cuando el bus permanece detenido más allá del umbral de tiempo configurado, el sistema alerta al acudiente e incluye la ubicación del vehículo.

**Tabla 30**. _Especificación del requisito RF-05.4: alerta por detención prolongada del bus_

| **Campo**       | **Detalle**                                                                                                              |
| --------------- | ------------------------------------------------------------------------------------------------------------------------ |
| Entradas        | Flujo de posiciones GPS del bus y umbral de detención configurable por ruta.                                             |
| Salidas         | Alerta de detención prolongada enviada al acudiente con la ubicación del bus; registro de alerta y de sus destinatarios. |
| Precondiciones  | Ruta en operación con flujo de posiciones activo y umbral configurado.                                                   |
| Postcondiciones | El acudiente recibe la alerta con la ubicación; si el bus reanuda la marcha antes del umbral, no se envía alerta.        |

**Reglas de negocio**

- El umbral de detención es configurable por ruta y nunca está fijado en el código.
- La alerta solo se emite al exceder el umbral; la reanudación oportuna del viaje suprime la alerta.
- Cada alerta declara su tipo y su nivel de urgencia, y registra la lectura realizada por cada destinatario.

## 3.6 Grupo RF-06 — Rastreo en tiempo real del bus (Real-Time Bus Tracking)

Este grupo especifica la captura y la exposición de la ubicación del bus. La telemetría se recibe por el canal gRPC persistente del puerto 5001 y las posiciones se almacenan asociadas al dispositivo GPS registrado.

### 3.6.1 RF-06.1 — Captura continua de la ubicación GPS del bus

El sistema captura de forma continua la posición geográfica del bus durante su operación, con su fecha y hora, velocidad y rumbo.

**Tabla 31**. _Especificación del requisito RF-06.1: captura continua de la ubicación GPS del bus_

| **Campo**       | **Detalle**                                                                                                               |
| --------------- | ------------------------------------------------------------------------------------------------------------------------- |
| Entradas        | Posiciones emitidas por el dispositivo GPS embarcado (latitud, longitud, velocidad, rumbo, fecha y hora del dispositivo). |
| Salidas         | Posición persistida con su marca de recepción en el servidor; posición disponible para el seguimiento en vivo.            |
| Precondiciones  | Dispositivo GPS registrado, con identificador único, y asociado al bus.                                                   |
| Postcondiciones | La última posición conocida del bus está disponible para su consulta.                                                     |

**Reglas de negocio**

- Una posición pertenece siempre a un dispositivo GPS registrado; el identificador del dispositivo es único.
- La latitud debe estar entre −90 y 90 y la longitud entre −180 y 180; la velocidad no puede ser negativa.
- Solo los dispositivos registrados pueden aportar datos de posición válidos.

### 3.6.2 RF-06.2 — Visualización de la ubicación en mapa interactivo

El sistema muestra la ubicación del bus en un mapa interactivo que se actualiza sin necesidad de refresco manual.

**Tabla 32**. _Especificación del requisito RF-06.2: visualización de la ubicación en mapa interactivo_

| **Campo**       | **Detalle**                                                                              |
| --------------- | ---------------------------------------------------------------------------------------- |
| Entradas        | Posiciones más recientes del bus de la ruta consultada.                                  |
| Salidas         | Marcador del bus sobre el mapa con actualización continua.                               |
| Precondiciones  | Acudiente autenticado con estudiante asignado a una ruta, o conductor con ruta asignada. |
| Postcondiciones | La ubicación se percibe actualizada sin intervención del usuario.                        |

**Reglas de negocio**

- La actualización llega por flujo continuo, sin recarga manual de la vista.
- El acudiente solo consulta la ubicación de las rutas de sus estudiantes vinculados.

### 3.6.3 RF-06.3 — Alerta por pérdida de señal GPS

Ante la pérdida de la señal GPS, el sistema alerta y muestra la última posición conocida con un marcador de «sin señal».

**Tabla 33**. _Especificación del requisito RF-06.3: alerta por pérdida de señal GPS_

| **Campo**       | **Detalle**                                                                                                              |
| --------------- | ------------------------------------------------------------------------------------------------------------------------ |
| Entradas        | Comparación entre la última posición recibida y el umbral de vigencia de señal.                                          |
| Salidas         | Estado «sin señal» con la última posición conocida; alerta de pérdida de señal dirigida al destinatario correspondiente. |
| Precondiciones  | Historial de posiciones del dispositivo en operación.                                                                    |
| Postcondiciones | El usuario distingue entre ubicación vigente y ubicación sin señal.                                                      |

**Reglas de negocio**

- La detención de recepción durante más del umbral configurado se interpreta como pérdida de señal.
- La alerta conserva la última posición válida y no presenta datos inventados.
- El nivel de urgencia de la alerta proviene del catálogo de tipos de alerta.

## 3.7 Grupo RF-07 — Gestión de viajes (Trip Management)

Este grupo especifica el ciclo de vida del viaje, la unidad operativa que habilita las bordadas y las notificaciones con significado.

### 3.7.1 RF-07.1 — Ciclo de viaje: inicio y finalización

El conductor inicia y finaliza el viaje de su ruta desde la aplicación móvil. El inicio marca el viaje como activo; el fin lo cierra validando que no existan bajadas pendientes.

**Tabla 34**. _Especificación del requisito RF-07.1: ciclo de viaje: inicio y finalización_

| **Campo**       | **Detalle**                                                                                                                                              |
| --------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Entradas        | Ruta asignada vigente para el conductor; acción «iniciar viaje» o «finalizar viaje»; hora de cada acción.                                                |
| Salidas         | Viaje iniciado o finalizado con sus marcas de tiempo; eventos de inicio y fin publicados; rechazo con mensaje «ya existe un viaje activo para este bus». |
| Precondiciones  | Conductor autenticado con ruta asignada; para finalizar, viaje activo y estado de bordadas conocido.                                                     |
| Postcondiciones | Existe como máximo un viaje activo por bus; el viaje finalizado deja trazabilidad de su ejecución con bus, conductor e inicio y fin.                     |

**Reglas de negocio**

- Un bus solo puede tener un viaje activo a la vez; un segundo intento de inicio se rechaza.
- No se finaliza un viaje si quedan bordadas de subida sin su correspondiente bajada.
- Los eventos de inicio y fin del viaje son los que habilitan el registro de bordadas con sentido operativo.
- Cada ejecución de ruta conserva el bus y el conductor que la atendieron.

## 3.8 Grupo RF-08 — Incidentes (Incidents)

Este grupo especifica la comunicación de situaciones anormales durante el trayecto.

### 3.8.1 RF-08.1 — Reporte de incidente y notificación automática

El conductor reporta un incidente o emergencia durante el viaje indicando tipo, descripción opcional y ubicación; el sistema registra el reporte y notifica automáticamente a la institución y a los acudientes de los estudiantes que viajan a bordo.

**Tabla 35**. _Especificación del requisito RF-08.1: reporte de incidente y notificación automática_

| **Campo**       | **Detalle**                                                                                                                                                      |
| --------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Entradas        | Tipo de incidente del catálogo configurable, descripción opcional, viaje activo y ubicación GPS del momento.                                                     |
| Salidas         | Incidente registrado y vinculado al viaje; alerta generada y notificada a la institución y a los acudientes de bordadas activas; rechazo si no hay viaje activo. |
| Precondiciones  | Conductor autenticado con viaje activo; catálogo de tipos de incidente disponible.                                                                               |
| Postcondiciones | Existe trazabilidad del incidente con su tipo, su momento, su ubicación y sus destinatarios notificados.                                                         |

**Reglas de negocio**

- El viaje es campo obligatorio: sin viaje activo el reporte se rechaza.
- La ubicación GPS se adjunta al reporte en el momento del evento.
- El listado de tipos de incidente es configurable por la institución, no está fijado en el código.
- La notificación se dirige a la institución y a los acudientes de los estudiantes con bordada activa en ese viaje.
- El reporte de incidente se modela como una alerta con su tipo, su nivel de urgencia y su registro de destinatarios.

## 3.9 Grupo RF-09 — Consulta de rutas (Route Inquiry)

Este grupo especifica la consulta de la configuración de ruta por parte del acudiente y del conductor.

### 3.9.1 RF-09.1 — Consulta por el acudiente de la ruta de su estudiante

El acudiente consulta los detalles de la ruta de su estudiante: bus, conductor, paradas en orden y horarios programados.

**Tabla 36**. _Especificación del requisito RF-09.1: consulta por el acudiente de la ruta de su estudiante_

| **Campo**       | **Detalle**                                                                                                                         |
| --------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| Entradas        | Identificador del estudiante vinculado a la cuenta del acudiente.                                                                   |
| Salidas         | Detalle de la ruta: bus, conductor, paradas ordenadas y horarios; mensaje «aún no se ha asignado ruta» cuando no existe asignación. |
| Precondiciones  | Acudiente autenticado y estudiante vinculado a su cuenta.                                                                           |
| Postcondiciones | El acudiente conoce el itinerario y el vehículo asignados a su estudiante.                                                          |

**Reglas de negocio**

- El acudiente solo accede a las rutas de los estudiantes vinculados a su cuenta, verificación realizada en cada consulta.
- La consulta entrega la configuración de la ruta, no la posición en vivo, que corresponde al rastreo en tiempo real.

### 3.9.2 RF-09.2 — Consulta por el conductor de su ruta asignada

El conductor visualiza en la aplicación móvil su ruta asignada con las paradas en orden, los horarios programados y el bus correspondiente.

**Tabla 37**. _Especificación del requisito RF-09.2: consulta por el conductor de su ruta asignada_

| **Campo**       | **Detalle**                                                                                            |
| --------------- | ------------------------------------------------------------------------------------------------------ |
| Entradas        | Identificador del conductor autenticado.                                                               |
| Salidas         | Ruta vigente con paradas ordenadas, horarios y bus; mensaje «sin ruta asignada» cuando no corresponde. |
| Precondiciones  | Conductor autenticado con asignación vigente de bus y ruta.                                            |
| Postcondiciones | El conductor conoce su itinerario antes de iniciar el viaje.                                           |

**Reglas de negocio**

- La ruta mostrada se deriva del bus asignado al conductor y de la ruta asignada a ese bus.
- Las paradas se presentan en orden de itinerario.
- La consulta no exige un viaje activo para su visualización.

## 3.10 Grupo RF-10 — Personalización (Personalization)

Este grupo especifica la adaptación de la interfaz a las preferencias del usuario y a la identidad de la institución. Su alcance es exclusivamente de cliente: no incumbe a ningún servicio de dominio.

### 3.10.1 RF-10.1 — Personalización de los colores del sistema (tema)

El usuario autenticado selecciona un color de tema que se aplica a todas las pantallas de la aplicación y se conserva entre sesiones.

**Tabla 38**. _Especificación del requisito RF-10.1: personalización de los colores del sistema (tema)_

| **Campo**       | **Detalle**                                                                            |
| --------------- | -------------------------------------------------------------------------------------- |
| Entradas        | Color de tema elegido por el usuario con cualquier rol.                                |
| Salidas         | Tema aplicado en todas las vistas; preferencia persistida en el dispositivo.           |
| Precondiciones  | Usuario autenticado con cuenta activa.                                                 |
| Postcondiciones | El cambio es visible de inmediato y sobrevive al cierre y reapertura de la aplicación. |

**Reglas de negocio**

- La preferencia se guarda en el lado cliente, en la configuración local del dispositivo.
- El contraste del tema debe conservar la accesibilidad exigida por la interfaz.
- El cambio no afecta a otros usuarios ni requiere escritura en servicios de dominio.

### 3.10.2 RF-10.2 — Configuración de la interfaz en al menos cuatro idiomas

El usuario selecciona el idioma de la interfaz entre al menos cuatro idiomas soportados, y el sistema muestra el idioma predeterminado en el primer uso.

**Tabla 39**. _Especificación del requisito RF-10.2: configuración de la interfaz en al menos cuatro idiomas_

| **Campo**       | **Detalle**                                                                                                                  |
| --------------- | ---------------------------------------------------------------------------------------------------------------------------- |
| Entradas        | Identificador del idioma elegido entre los soportados (español, inglés, francés y portugués).                                |
| Salidas         | Etiquetas de la interfaz cargadas desde el diccionario del idioma elegido; idioma predeterminado en ausencia de preferencia. |
| Precondiciones  | Usuario autenticado; diccionarios de idioma disponibles en la aplicación.                                                    |
| Postcondiciones | La preferencia de idioma persiste por dispositivo.                                                                           |

**Reglas de negocio**

- El sistema admite al menos cuatro idiomas; el español es el idioma predeterminado de la primera versión.
- Los diccionarios se resuelven en el cliente y la preferencia se almacena por dispositivo.
- El cambio de idioma no altera los datos almacenados ni los mensajes de negocio ya registrados.

# 4. Requisitos no funcionales

Los ocho requisitos no funcionales fijan métricas verificables. Toda afirmación de calidad no medida se considera insuficiente: «el sistema debe ser rápido» no es un requisito; «la latencia P95 de los endpoints críticos debe ser inferior a 300 ms bajo 50 solicitudes por segundo» sí lo es.

**Tabla 40**. _Resumen de los requisitos no funcionales_

| **ID** | **Requisito**               | **Prioridad** | **Validación en integración continua**            |
| ------ | --------------------------- | ------------- | ------------------------------------------------- |
| RNF-01 | Rendimiento                 | P1            | Sí, con pruebas de carga en el entorno de pruebas |
| RNF-02 | Disponibilidad              | P1            | Sí, comprobaciones de salud                       |
| RNF-03 | Escalabilidad               | P2            | Manual, de forma trimestral                       |
| RNF-04 | Seguridad                   | P1            | Sí, análisis estático y revisión OWASP            |
| RNF-05 | Observabilidad              | P1            | Sí, prueba de humo                                |
| RNF-06 | Mantenibilidad              | P2            | Sí, cobertura de pruebas                          |
| RNF-07 | Portabilidad                | P2            | Sí, construcción de imagen                        |
| RNF-08 | Recuperación ante desastres | P1            | Manual, con simulacros                            |

## 4.1 RNF-01 Rendimiento

**Tabla 41**. _Métricas de rendimiento y condiciones de verificación_

| **Métrica**                                            | **Valor objetivo**                      | **Condición de verificación**                                                                |
| ------------------------------------------------------ | --------------------------------------- | -------------------------------------------------------------------------------------------- |
| Latencia P95 de endpoints críticos                     | < 300 ms                                | 50 solicitudes por segundo por servicio, en la ventana pico de bordadas de 6:30 a 8:00 a. m. |
| Latencia P99 de endpoints críticos                     | < 500 ms                                | Condiciones de ventana pico                                                                  |
| Latencia P95 de endpoints no críticos                  | < 1000 ms                               | Carga normal                                                                                 |
| Rendimiento mínimo sostenido                           | 50 solicitudes por segundo por servicio | Sin degradación de respuesta                                                                 |
| Evento de bordada o bajada → notificación al acudiente | P95 < 5 segundos                        | Extremo a extremo, durante la ventana pico                                                   |
| Posición GPS → mapa en vivo                            | P95 < 5 segundos                        | Seguimiento continuo                                                                         |
| Tiempo de arranque de un servicio                      | < 30 segundos                           | Arranque en frío                                                                             |

Endpoints críticos sujetos a la métrica: el registro de bordada por escaneo QR (POST /api/v1/boarding), la consulta de ubicación del bus (GET /api/v1/buses/{busId}/location) y el inicio de sesión (POST /api/v1/auth/login). Las pruebas de carga se ejecutan con herramientas del ecosistema k6, Apache JMeter, Locust o Gatling y se validan en la canalización de integración y despliegue antes de producción.

## 4.2 RNF-02 Disponibilidad

**Tabla 42**. _Objetivos de nivel de servicio por entorno_

| **Entorno** | **SLO** | **Ventana de mantenimiento**  | **Caída máxima mensual** |
| ----------- | ------- | ----------------------------- | ------------------------ |
| Producción  | 99,9 %  | Domingos de 2:00 a 4:00 a. m. | 44 minutos               |
| Pruebas     | 95 %    | Sin restricción               | 36 horas                 |

El presupuesto de error mensual en producción es de 44 minutos. Si se consume más del 50 % del presupuesto en la primera mitad del mes, se congelan los despliegues de funcionalidades hasta el mes siguiente y se prioriza la estabilidad. Cada servicio expone comprobaciones de salud: verificación de vida (GET /health) que responde mientras el proceso esté activo, y verificación de disponibilidad (GET /health/ready) que responde solo si el servicio puede procesar tráfico con sus dependencias conectadas; en los servicios Java se publica además el punto de salud del actuador (/actuator/health).

## 4.3 RNF-03 Escalabilidad

**Tabla 43**. _Escenarios de escalabilidad y comportamiento esperado_

| **Escenario**                   | **Comportamiento esperado**                                                        |
| ------------------------------- | ---------------------------------------------------------------------------------- |
| Crecimiento gradual de la carga | Activación del autoescalado horizontal cuando la utilización de CPU supera el 70 % |
| Pico súbito de demanda          | Escalado en menos de 2 minutos                                                     |
| Descenso de la carga            | Reducción de instancias sin interrumpir el tráfico activo                          |
| Límite de escalado horizontal   | Hasta 10 instancias por servicio                                                   |

La estrategia es el escalado horizontal de instancias sin estado: ninguna instancia guarda estado en memoria; los datos persistentes residen en SQL Server. La arquitectura soporta hasta 5.000 usuarios concurrentes sin degradación.

## 4.4 RNF-04 Seguridad

**Tabla 44**. _Requisitos medibles de seguridad por área_

| **Área**              | **Requisito medible**                                                                                                                                                                           |
| --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Autenticación         | Todo endpoint privado exige token Bearer válido en el encabezado Authorization; token de acceso JWT RS256 con vigencia de 15 minutos y token de actualización de 30 días.                       |
| Autorización          | Control de acceso por rol con permisos explícitos y denegación predeterminada; mínimo privilegio por rol.                                                                                       |
| Transmisión           | HTTPS obligatorio en producción con TLS 1.2 o superior; HTTP únicamente en desarrollo local.                                                                                                    |
| Contraseñas           | Almacenamiento con hash irreversible (bcrypt con coste ≥ 12 o Argon2id); la política fija longitud, mayúsculas, números, símbolos y expiración en días; el hash nunca se registra en bitácoras. |
| Datos personales      | Los datos de identificación personal se cifran en reposo y se minimiza su registro.                                                                                                             |
| Secretos              | Credenciales y llaves únicamente en variables de entorno o gestor de secretos, nunca en el código fuente.                                                                                       |
| Revisión de seguridad | Revisión contra OWASP Top 10 en cada liberación, con análisis estático, escaneo de dependencias y análisis dinámico en pruebas.                                                                 |
| Cumplimiento          | Ley 1581 de 2012 y Decreto 1377 de 2013, con consentimiento y autorización para el tratamiento de datos de menores; GDPR si el producto se ofrece en la Unión Europea.                          |

La superficie de confianza se organiza en cuatro zonas: pública (clientes hacia el gateway), de servicios (comunicación interna), de datos (base y caché, sin acceso público) y de administración (restringida por red). Entre servicios, la comunicación gRPC se protege con confianza de red o mutua y con circuito de interrupción; los temas del bus de eventos aplican control de acceso a nivel de tema.

## 4.5 RNF-05 Observabilidad

**Tabla 45**. _Requisitos de observabilidad y herramientas_

| **Pilar** | **Requisito**                                                                                              | **Herramienta**                                      |
| --------- | ---------------------------------------------------------------------------------------------------------- | ---------------------------------------------------- |
| Bitácoras | Formato JSON estructurado con identificador de correlación propagado en todas las trazas de la transacción | Serilog en servicios .NET; Logback en servicios Java |
| Métricas  | Indicadores de tasa, errores y duración por endpoint                                                       | Prometheus + Grafana                                 |
| Trazas    | Traza distribuida de extremo a extremo                                                                     | OpenTelemetry + Jaeger                               |
| Alertas   | Alerta operativa en menos de 5 minutos cuando se viola el objetivo de nivel de servicio                    | Alertmanager o servicio de alerta externo            |

Cada solicitud externa genera un identificador de correlación único que se propaga en sus bitácoras y trazas, lo que permite reconstruir una transacción completa entre cliente, gateway y servicios.

## 4.6 RNF-06 Mantenibilidad

**Tabla 46**. _Objetivos de mantenibilidad_

| **Métrica**                  | **Objetivo**                                                                                              |
| ---------------------------- | --------------------------------------------------------------------------------------------------------- |
| Cobertura de pruebas         | ≥ 80 % de líneas; ≥ 90 % en la capa de dominio                                                            |
| Complejidad ciclomática      | ≤ 10 por función                                                                                          |
| Deuda técnica                | Resolución en menos de un ciclo de desarrollo desde su registro                                           |
| Tiempo de incorporación      | Un desarrollador nuevo despliega en local en menos de 1 hora siguiendo la guía de preparación del entorno |
| Tiempo medio de construcción | < 5 minutos en la canalización de integración                                                             |

## 4.7 RNF-07 Portabilidad

**Tabla 47**. _Requisitos de portabilidad_

| **Requisito**     | **Detalle**                                                                              |
| ----------------- | ---------------------------------------------------------------------------------------- |
| Contenedores      | Todos los servicios se despliegan como imágenes de contenedor                            |
| Orquestación      | Las imágenes operan en cualquier entorno con Kubernetes 1.28 o superior                  |
| Sistema operativo | Ningún servicio depende del sistema operativo del anfitrión                              |
| Configuración     | Las variables de entorno son la única fuente de configuración específica de cada entorno |

## 4.8 RNF-08 Recuperación ante desastres

**Tabla 48**. _Objetivos de recuperación ante desastres por escenario_

| **Escenario**                         | **RTO**                                       | **RPO**                        |
| ------------------------------------- | --------------------------------------------- | ------------------------------ |
| Falla de un servicio                  | < 2 minutos (reinicio automatizado)           | 0 (servicio sin estado)        |
| Falla de la base de datos primaria    | < 5 minutos (conmutación a réplica)           | < 1 segundo (réplica síncrona) |
| Pérdida de una zona de disponibilidad | < 15 minutos                                  | < 5 minutos                    |
| Desastre total de la región           | < 4 horas (recuperación en región secundaria) | < 1 hora                       |

# 5. Requisitos de interfaz

## 5.1 Interfaz de usuario

**Aplicación web (Angular 21 + Angular Material 3).** Presenta una barra superior, un menú lateral de navegación según el rol, tablas con paginación, formularios con validación en línea, mensajes de retroalimentación tipo _snackbar_ y tarjetas de indicadores en el panel principal. Las vistas se organizan en panel de administración, panel de operación de plataforma, gestión de usuarios, sedes y cursos, flota, rutas y perfil. El diseño responde a escritorio, tableta y móvil, y respeta los tokens de diseño (color, tipografía y densidad) definidos para la interfaz.

**Aplicación móvil (React + Expo).** La navegación es distinta por rol: el conductor encuentra su ruta del día, el escáner QR, el reporte de incidentes y el estado de conexión; el acudiente encuentra el seguimiento de la ruta, las notificaciones y el historial de bordadas; el estudiante encuentra su código QR personal. La interfaz informa de forma explícita los estados de conexión y desconexión.

**Interacciones comunes.** Confirmación explícita para acciones aceptadas o irreversibles; listados consultables con filtros; mensajes de error consistentes y en el idioma elegido; contraste de color verificable frente a los criterios de accesibilidad; al menos cuatro idiomas de interfaz y personalización de tema por usuario.

## 5.2 Interfaz de hardware

**Tabla 49**. _Interfaz de hardware requerida_

| **Componente**            | **Interfaz requerida**                                                                                                                               |
| ------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- |
| Equipo de escritorio      | Navegador web con soporte de HTML5, CSS y ejecución de scripts; impresora no requerida.                                                              |
| Dispositivo móvil         | Android o iOS con cámara para el escaneo de códigos QR y soporte de notificaciones push.                                                             |
| Dispositivo GPS embarcado | Emisor de posiciones geográficas por conexión TCP persistente hacia el componente de geolocalización, con identificador de dispositivo único (IMEI). |
| Servidor                  | Instancia de SQL Server accesible por el puerto 1433 dentro de la red de servicios; no expuesta públicamente.                                        |
| Red                       | Conexión a internet estable para clientes; conectividad de datos para la recepción de posiciones y el envío de notificaciones.                       |

## 5.3 Interfaz de software

**Tabla 50**. _Interacciones de software requeridas_

| **Software**                     | **Interacción requerida**                                                                         |
| -------------------------------- | ------------------------------------------------------------------------------------------------- |
| SQL Server 2022                  | Persistencia relacional con esquema por servicio y migraciones versionadas.                       |
| Apache Kafka 4.1.0 (KRaft)       | Publicación y consumo de eventos de dominio en el puerto 9092.                                    |
| API Gateway (Kong OSS)           | Terminación TLS, límite de tasa, CORS y ruteo hacia los servicios.                                |
| Liquibase                        | Aplicación determinista de cambios de esquema en cada arranque de servicio.                       |
| Aplicación web Angular 21        | Consumo exclusivo de la interfaz REST versionada a través del gateway.                            |
| Aplicación móvil React + Expo    | Consumo de la interfaz REST, registro de token de notificación y acceso a la cámara.              |
| Servicio de mapas                | Representación del mapa interactivo y de la ubicación del bus en tiempo real.                     |
| Proveedor de notificaciones push | Entrega de avisos en los dispositivos móviles, identificados por token de dispositivo por perfil. |

## 5.4 Interfaz de comunicaciones

**REST/JSON.** Todas las interacciones cliente–servicio se realizan mediante HTTP con prefijo /api/v1, recursos en plural, verbos HTTP estándar, filtrado, orden y paginación por página y tamaño. El token Bearer es obligatorio salvo en las rutas de inicio de sesión y renovación. Las respuestas de error siguen una estructura consistente con marca de tiempo, mensaje y detalles o ruta solicitada. El gateway aplica límite de tasa y restringe el origen CORS al dominio web.

**gRPC.** Canal persistente en el puerto 5001 para el flujo de telemetría y geolocalización de baja latencia entre el componente de recepción de posiciones y los consumidores de ubicación; la comunicación admite interruptor de circuito ante fallas.

**Eventos (Apache Kafka, puerto 9092).** El bus de eventos transporta los seis temas de alta y perfil de usuarios y de escaneo QR:

**Tabla 51**. _Temas del bus de eventos y sus participantes_

| **Tema** | **Productor** | **Consumidor** | **Uso** |
| --- | --- | --- | --- |
| `student.created` | Servicio de gestión de usuarios | Servicios suscritos | Alta de estudiante en el ecosistema |
| `driver.created` | Servicio de gestión de usuarios | Servicios suscritos | Alta de conductor |
| `admin.created` | Servicio de gestión de usuarios | Servicios suscritos | Alta de administrador |
| `parent.created` | Servicio de gestión de usuarios | Servicios suscritos | Alta de acudiente |
| `profile.created` | Servicio de identificación y acceso | Servicios suscritos | Alta de perfil de autenticación |
| `student.scanned` | Servicio de identificación y acceso | Servicio de notificaciones | Escaneo QR que dispara la notificación y el registro de bordada |

Adicionalmente, los servicios emiten eventos de dominio internos —alta y actualización de rutas, asignación de bus y conductor, inicio y fin de viaje, subida y bajada, creación de alertas— que se tratan de forma asincrónica e idempotente dentro del modelo orientado a eventos.

**Notificaciones push.** El servicio de notificaciones entrega avisos a los dispositivos mediante el token de notificación registrado por perfil; cada entrega conserva su estado (en cola, enviada, fallida, leída) y su fecha.

**Tabla 52**. _Recursos de comunicación, puertos y propósito_

| **Recurso de comunicación** | **Puerto** | **Propósito**                        |
| --------------------------- | ---------- | ------------------------------------ |
| API Gateway (Kong)          | 8000       | Punto de entrada público de clientes |
| Servicios de negocio        | 8080       | Interfaz REST interna de servicios   |
| SQL Server                  | 1433       | Persistencia relacional              |
| Apache Kafka                | 9092       | Bus de eventos                       |
| gRPC                        | 5001       | Telemetría de geolocalización        |

# 6. Requisitos de datos

## 6.1 Modelo lógico de datos (resumen)

El software administra **38 tablas** distribuidas en **11 esquemas**, uno por servicio. Una única instancia relacional concentra la información con aislamiento lógico por esquema; ningún servicio accede directamente al esquema de otro y, cuando necesita sus datos, los obtiene mediante interfaz o evento.

**Tabla 53**. _Esquemas de base de datos y su contenido clave_

| **Esquema** | **Tablas y contenido clave** |
| --- | --- |
| `Iam` | Profile (persona, sede, hash de contraseña y rol), Role, Permissions, PermissionsRole, SessionProfile |
| `UserManagement` | Person (nombre, identificación tipo y número, correo, teléfono, dirección, fecha de nacimiento), Family, FamilyMember (relación acudiente–estudiante), DriverLicense (número y vencimiento) |
| `School` | School (ciudad, logotipo, datos de contacto, tema), SchoolCampus, Course, SchoolAdmin |
| `Geographic` | City (nombre y país) |
| `Fleet` | Bus (placa, capacidad, SOAT, dispositivo GPS, modelo), Brand, Model, DriverAssignemts (conductor–bus con vigencia) |
| `Route` | Route (sede, sector, horario), Stop (coordenadas con precisión de siete decimales), RouteStop (orden), RouteBusAssignments, RouteStudentAssignments, RouteSchedule (día de semana, dirección y hora), RouteExecution (ejecución real: bus, conductor, inicio y fin) |
| `Gps` | GpsDevice (IMEI, última conexión, estado), GpsLocation (latitud, longitud, velocidad, rumbo, fecha y hora) |
| `Notification` | Boarding (bordada con tipo y fecha y hora), Alert, AlertType (nivel de urgencia), AlertRecipient (lectura), DeviceToken (token de notificación por perfil) |
| `Exceptional` | ExceptionalDriverUsage (uso de bus por terceros con motivo), ExceptionalRouteUsage (desvío de ruta con motivo y ventana) |
| `Settings` | PasswordPolicies, SecuritySettings (parámetro y valor) |
| `Audit` | Audit (acción, fecha, descripción, IP y aplicación), LogErrors (tipo, descripción, IP y fecha) |

## 6.2 Reglas de integridad y validación

**Tabla 54**. _Reglas de integridad y validación_

| **#** | **Regla**                                                                                                                                                  |
| ----- | ---------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 1     | Las llaves primarias se implementan como identificadores únicos, salvo los catálogos de tipo pequeño y los contadores de auditoría.                        |
| 2     | Toda columna de referencia externa sigue la convención de nombre de la entidad referenciada más el sufijo de identificación.                               |
| 3     | Las fechas se almacenan con precisión de fecha y hora y en horario coordinado universal; no se almacena hora local sin desplazamiento.                     |
| 4     | El número de documento de una persona es único; la placa de un bus es única; el identificador IMEI de un dispositivo GPS es único.                         |
| 5     | Una ruta pertenece a una sede; un bus pertenece a una institución; una parada pertenece a una ruta o a su institución de referencia.                       |
| 6     | Un bus se asigna a una sola ruta a la vez; un conductor es responsable de un solo bus a la vez; un estudiante mantiene una sola ruta activa.               |
| 7     | Una bordada referencia obligatoriamente estudiante, bus y parada, y su tipo solo admite subida o bajada.                                                   |
| 8     | Una posición geográfica referencia un dispositivo registrado; la latitud está entre −90 y 90, la longitud entre −180 y 180 y la velocidad no es negativa.  |
| 9     | No existen referencias físicas cruzadas entre esquemas: los cruces se resuelven por identificador o código en la aplicación y su consistencia es eventual. |
| 10    | Las columnas de filtro y de unión disponen de índice; las series temporales se indexan por dispositivo y fecha descendente.                                |
| 11    | La unicidad de negocio se refuerza con restricciones únicas y las reglas de rango con restricciones de verificación.                                       |

## 6.3 Ciclo de vida y retención de datos

- **Baja lógica.** Las entidades de uso administrativo se desactivan mediante estado ACTIVE/INACTIVE y marca de borrado lógico, sin eliminación física, de modo que el historial permanezca consultable.
- **Auditoría.** El historial de auditoría es de solo inserción: no admite actualización ni eliminación desde la aplicación y se conserva con su marca de tiempo en horario coordinado universal.
- **Telemetría GPS.** Las posiciones de alta frecuencia no usan borrado lógico: su histórico se administra mediante trabajos de retención configurables, sin que la presente fije un número de días de conservación.
- **Eventos y notificaciones.** Cada bordada, alerta y entrega de notificación conserva sus marcas de tiempo de ocurrencia y de creación, que pueden diferir cuando el dato proviene de un dispositivo sin conexión.
- **Cambios de esquema.** Todo cambio de estructura se versiona como migración aplicada por la canalización; nunca se ejecutan modificaciones manuales sobre el entorno de producción y los cambios incompatibles siguen la secuencia de ampliación, migración de datos y contracción.
- **Copias de seguridad.** La base de datos se respalda conforme al plan operativo del entorno y su restauración se ensaya dentro de los objetivos de recuperación de la sección 4.8.

## 6.4 Protección de datos personales

**Tabla 55**. _Requisitos de protección de datos personales_

| **Requisito**     | **Detalle**                                                                                                                                                        |
| ----------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Clasificación     | Correo, nombre, número de documento, vínculos familiares y ubicación en vivo se tratan como información sensible.                                                  |
| Minimización      | La información personal se registra en bitácoras solo en la medida necesaria para la operación y la auditoría.                                                     |
| Cifrado           | Los datos de identificación personal se cifran en reposo y viajan cifrados mediante TLS.                                                                           |
| Control de acceso | La consulta se restringe por rol y por ámbito institucional: el acudiente solo accede a sus estudiantes vinculados y el administrador al ámbito de su institución. |
| Consentimiento    | El tratamiento de datos de menores exige consentimiento y autorización expresa del acudiente.                                                                      |
| Trazabilidad      | Toda acción crítica sobre datos personales queda registrada con quién, qué, cuándo, dirección IP y aplicación.                                                     |

# 7. Historias de usuario

## 7.1 Épicas

El backlog del producto se organiza en **7 épicas** que agrupan **20 historias de usuario**.

**Tabla 56**. _Épicas del backlog del producto_

| **Épica** | **Denominación**              | **Descripción**                                                                   | **HU** |
| --------- | ----------------------------- | --------------------------------------------------------------------------------- | ------ |
| EP-001    | Subida y bajada mediante QR   | Registra la subida y la bajada del estudiante mediante el escaneo de su código QR | 3      |
| EP-002    | Notificaciones                | Notifica al acudiente la subida y la bajada y las alertas de la ruta              | 2      |
| EP-003    | Gestión de rutas y flota      | Administra rutas, buses, paradas y asignaciones de conductor y estudiantes        | 8      |
| EP-004    | Gestión de usuarios y familia | Administra usuarios y vincula estudiantes a la cuenta del acudiente               | 3      |
| EP-005    | Seguimiento en vivo           | Muestra la ubicación del bus en el mapa en tiempo real                            | 1      |
| EP-006    | Incidentes                    | Reporta y notifica los incidentes ocurridos durante el viaje                      | 1      |
| EP-007    | Interfaz y personalización    | Personaliza el tema de colores y el idioma de la interfaz                         | 2      |

## 7.2 Historias de usuario por épica

### 7.2.1 Épica EP-001 — Subida y bajada mediante QR

**Tabla 57**. _Historias de usuario de la épica EP-001_

| **HU** | **Título** | **Rol** | **Plataforma** | **Prioridad** | **Criterios de aceptación (resumidos)** |
| --- | --- | --- | --- | --- | --- |
| HU-QR-001 | El conductor escanea el código QR del estudiante para registrar su subida | `DRIVER` | Móvil | Alta | Con estudiante asignado a la ruta y con parada registrada, el escaneo registra la subida con hora y parada y publica el evento de subida; si el estudiante pertenece a otra ruta, se rechaza con mensaje «estudiante no asignado a esta ruta». |
| HU-QR-002 | El conductor escanea el código QR del estudiante para registrar su bajada | `DRIVER` | Móvil | Alta | Con bordada de subida activa en el viaje, el escaneo registra la bajada con hora y ubicación y publica el evento de bajada; sin bordada activa, se rechaza con mensaje «no hay bordada activa para el estudiante». |
| HU-QR-004 | El estudiante consulta su código QR personal en la aplicación móvil | `STUDENT` | Móvil | Media | Con cuenta activa, la sección muestra el código con identificador único y el nombre del estudiante para verificación; si el código está vencido o la cuenta está desactivada, informa que el código no está disponible. |

### 7.2.2 Épica EP-002 — Notificaciones

**Tabla 58**. _Historias de usuario de la épica EP-002_

| **HU** | **Título** | **Rol** | **Plataforma** | **Prioridad** | **Criterios de aceptación (resumidos)** |
| --- | --- | --- | --- | --- | --- |
| HU-NOTIF-001 | Notificaciones en tiempo real de subida y bajada al acudiente | `PARENT` | Móvil | Alta | Al escanear la subida, el acudiente recibe de inmediato la hora y la parada; al escanear la bajada, recibe la hora y la ubicación del descenso. |
| HU-NOTIF-005 | Alerta cuando el bus permanece detenido más de lo permitido | `PARENT` | Móvil | Media | Al superar el umbral de detención configurado se envía la alerta con la ubicación del bus; si el bus reanuda la marcha dentro del umbral, no se envía ninguna alerta. |

### 7.2.3 Épica EP-003 — Gestión de rutas y flota

**Tabla 59**. _Historias de usuario de la épica EP-003_

| **HU** | **Título** | **Rol** | **Plataforma** | **Prioridad** | **Criterios de aceptación (resumidos)** |
| --- | --- | --- | --- | --- | --- |
| HU-ROUTE-001 | El administrador crea y gestiona rutas escolares con paradas y horarios | `ADMIN` | Web | Alta | La ruta se registra con paradas ordenadas y horarios y aparece en el listado; un nombre duplicado se rechaza con mensaje «la ruta ya existe». |
| HU-ROUTE-002 | El conductor consulta su ruta asignada con paradas y horarios | `DRIVER` | Móvil | Alta | Con ruta asignada y activa, la vista muestra paradas ordenadas, horarios y el bus; sin ruta asignada, informa «sin ruta asignada». |
| HU-ROUTE-003 | El conductor inicia y finaliza el viaje de su ruta | `DRIVER` | Móvil | Media | El inicio marca el viaje activo y publica el evento correspondiente; el fin valida que no haya bajadas pendientes; un segundo inicio concurrente se rechaza con mensaje «ya existe un viaje activo para este bus». |
| HU-ROUTE-004 | El acudiente consulta los detalles de la ruta de su hijo | `PARENT` | Móvil | Media | Con estudiante vinculado y con ruta asignada, la vista muestra bus, conductor, paradas y horarios; sin ruta asignada, informa «aún no se ha asignado ruta». |
| HU-ROUTE-005 | El administrador asigna un bus a una ruta | `ADMIN` | Web | Media | El bus se vincula a la ruta y el estado del bus pasa a «en ruta»; si la ruta ya tiene bus activo, se rechaza con mensaje «la ruta ya tiene un bus asignado». |
| HU-ROUTE-008 | El administrador asigna estudiantes a una ruta con su parada de subida y bajada | `ADMIN` | Web | Alta | El estudiante queda vinculado a la ruta y el acudiente puede consultarla; una parada ajena a la ruta se rechaza con mensaje «la parada no pertenece a esta ruta». |
| HU-FLEET-001 | El administrador gestiona la flota de buses | `ADMIN` | Web | Media | El bus se registra con estado disponible y aparece en el listado; una placa repetida se rechaza con mensaje «la placa ya existe». |
| HU-BUS-002 | El administrador asigna y desasigna un conductor a un bus | `ADMIN` | Web | Alta | El conductor queda registrado como responsable del bus y ve las rutas de su bus en la aplicación; si ya es responsable de otro bus, se rechaza con mensaje «conductor ya asignado a un bus». |

### 7.2.4 Épica EP-004 — Gestión de usuarios y familia

**Tabla 60**. _Historias de usuario de la épica EP-004_

| **HU** | **Título** | **Rol** | **Plataforma** | **Prioridad** | **Criterios de aceptación (resumidos)** |
| --- | --- | --- | --- | --- | --- |
| HU-IAM-001 | Inicio de sesión, cuentas y roles (control de acceso por rol) | Todos los roles | Web y móvil | Alta | Con credenciales válidas y cuenta activa, el sistema emite el token y concede acceso limitado a las funciones del rol; con credenciales incorrectas o cuenta desactivada, rechaza con mensaje «credenciales inválidas» y no emite token. |
| HU-USER-003 | El administrador vincula estudiantes a la cuenta de un acudiente | `ADMIN` | Web | Alta | El estudiante queda vinculado y el acudiente consulta su información y notificaciones; un estudiante ya vinculado a otro acudiente se rechaza con mensaje «estudiante ya asociado». |
| HU-USER-004 | El administrador gestiona las cuentas del ecosistema escolar | `ADMIN` | Web | Alta | La cuenta se crea con el rol correspondiente y aparece en el listado; al desactivar una cuenta, esta deja de acceder al sistema y el usuario queda indisponible para asignaciones. |

### 7.2.5 Épica EP-005 — Seguimiento en vivo

**Tabla 61**. _Historias de usuario de la épica EP-005_

| **HU** | **Título** | **Rol** | **Plataforma** | **Prioridad** | **Criterios de aceptación (resumidos)** |
| --- | --- | --- | --- | --- | --- |
| HU-GPS-002 | Ubicación del bus en el mapa en tiempo real | `PARENT` | Móvil | Media | Con estudiante asignado a una ruta, la vista muestra la ubicación actualizada sin refresco manual; ante pérdida de señal GPS, muestra la última posición conocida con marcador de «sin señal». |

### 7.2.6 Épica EP-006 — Incidentes

**Tabla 62**. _Historias de usuario de la épica EP-006_

| **HU** | **Título** | **Rol** | **Plataforma** | **Prioridad** | **Criterios de aceptación (resumidos)** |
| --- | --- | --- | --- | --- | --- |
| HU-INCIDENT-001 | El conductor reporta un incidente o emergencia durante el viaje | `DRIVER` | Móvil | Media | Con viaje activo, el reporte registra tipo, descripción opcional, viaje y ubicación, y notifica a la institución y a los acudientes de los estudiantes a bordo; sin viaje activo, el campo viaje es obligatorio y se rechaza el reporte. |

### 7.2.7 Épica EP-007 — Interfaz y personalización

**Tabla 63**. _Historias de usuario de la épica EP-007_

| **HU**    | **Título**                                                      | **Rol**         | **Plataforma** | **Prioridad** | **Criterios de aceptación (resumidos)**                                                                                                          |
| --------- | --------------------------------------------------------------- | --------------- | -------------- | ------------- | ------------------------------------------------------------------------------------------------------------------------------------------------ |
| HU-UI-001 | El usuario personaliza los colores del tema de la aplicación    | Todos los roles | Web y móvil    | Baja          | El cambio de color se aplica a todas las pantallas y se conserva al cerrar y reabrir la aplicación.                                              |
| HU-UI-002 | El usuario elige el idioma de la interfaz entre al menos cuatro | Todos los roles | Web y móvil    | Baja          | Al elegir un idioma soportado, las etiquetas se cargan desde su diccionario; sin preferencia guardada, la interfaz usa el idioma predeterminado. |

# 8. Criterios de aceptación generales y estrategia de verificación

## 8.1 Criterios de aceptación generales

Un incremento del software se considera aceptado cuando cumple, de forma conjunta, los criterios siguientes:

**Tabla 64**. _Criterios generales de aceptación_

| **#** | **Criterio**                                                                                                                                           |
| ----- | ------------------------------------------------------------------------------------------------------------------------------------------------------ |
| 1     | Toda función entregada está trazada al menos a un requisito funcional de la sección 3 y a una historia de usuario del anexo.                           |
| 2     | Los criterios de aceptación de la historia de usuario se verifican en formato Given/When/Then, con resultado observable y reproducible.                |
| 3     | Las reglas de negocio de cada requisito se cumplen también en los escenarios de error, no solo en el camino feliz.                                     |
| 4     | Las validaciones de entrada rechazan datos inválidos con mensaje claro y sin perder la información digitada.                                           |
| 5     | Todo endpoint privado exige token válido y aplica el control de acceso por rol; sin permiso explícito, la operación se deniega.                        |
| 6     | Las acciones críticas quedan registradas en auditoría con quién, qué, cuándo, dirección IP y aplicación.                                               |
| 7     | La interfaz conserva el contraste, la navegación por teclado y la presentación responsive exigidos por el diseño.                                      |
| 8     | Las pruebas no utilizan datos personales reales: los registros de prueba usan datos ficticios y credenciales marcadas como &lt;PASSWORD_DE_PRUEBA&gt;. |
| 9     | No se reduce la cobertura de pruebas existente: el código añadido incrementa o conserva la cobertura global.                                           |
| 10    | La documentación de interfaz y de datos se actualiza en la misma entrega cuando cambia el contrato o el significado de un campo.                       |

## 8.2 Estrategia de verificación

La verificación se organiza en niveles complementarios, con mayor volumen en la base de la pirámide, donde las pruebas son más rápidas y económicas:

**Tabla 65**. _Niveles de la estrategia de verificación_

| **Nivel**            | **Objetivo**                                                 | **Alcance típico**                                                               | **Herramientas**                                                                                                     |
| -------------------- | ------------------------------------------------------------ | -------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------- |
| Unitaria             | Probar la lógica de negocio de forma aislada                 | Agregados, objetos de valor, servicios de dominio y casos de uso                 | JUnit 5 con Mockito en Java; xUnit en .NET; Jest y Vitest en Angular; Jest con React Native Testing Library en móvil |
| De integración       | Verificar adaptadores contra sistemas reales                 | Capa de persistencia contra base de datos, publicadores contra el bus de eventos | Entornos de prueba contenedorizados con pruebas de integración de base y de API                                      |
| De contrato          | Confirmar que la implementación cumple la interfaz publicada | Interfaz HTTP frente al contrato de servicio                                     | Validadores de contrato y verificación del esquema publicado                                                         |
| De extremo a extremo | Validar flujos completos como los experimenta el usuario     | Inicio de sesión, gestión de rutas, bordada y consulta del acudiente             | Playwright y Cypress para interfaz web; k6 para pruebas de extremo a extremo de interfaz                             |
| Rendimiento          | Confirmar los objetivos de la sección 4.1                    | Endpoints críticos bajo carga sostenida                                          | k6, con fallo si la latencia P95 supera 300 ms o la tasa de error supera el 1 %                                      |
| Regresión y humo     | Proteger lo ya funcionando tras cada cambio                  | Flujos críticos y comprobación de salud de los servicios                         | Conjunto de regresión en cada entrega y prueba de humo en el entorno de pruebas                                      |

Las pruebas unitarias se ejecutan en cada entrega de código; las de integración y contrato en la canalización; las de extremo a extremo y de rendimiento en el entorno de pruebas, antes de la publicación en producción. Los defectos se registran con severidad y trazabilidad hacia el requisito funcional que los origina, y su cierre exige la prueba que lo reproduce.

## 8.3 Umbrales de calidad y métricas

**Tabla 66**. _Umbrales de calidad y momentos de medición_

| **Métrica**                        | **Umbral**                                  | **Momento de medición**                                          |
| ---------------------------------- | ------------------------------------------- | ---------------------------------------------------------------- |
| Cobertura de líneas de prueba      | ≥ 80 % global; ≥ 90 % en la capa de dominio | En cada entrega; una entrega que reduzca la cobertura se rechaza |
| Cobertura de ramas                 | ≥ 75 % global; ≥ 85 % en la capa de dominio | En cada entrega                                                  |
| Latencia P95 de endpoints críticos | < 300 ms                                    | Prueba de carga en el entorno de pruebas                         |
| Tasa de error bajo carga           | < 1 %                                       | Prueba de carga en el entorno de pruebas                         |
| Tiempo medio de construcción       | < 5 minutos                                 | Canalización de integración                                      |
| Complejidad ciclomática            | ≤ 10 por función                            | Análisis estático                                                |
| Revisión OWASP Top 10              | Sin hallazgos críticos ni altos pendientes  | Cada liberación                                                  |
| Disponibilidad                     | SLO 99,9 % mensual                          | Monitoreo del entorno de producción                              |

# 9. Glosario

## 9.1 Glosario del dominio

**Tabla 67**. _Glosario del dominio_

| **Término**            | **Definición**                                                                                                            |
| ---------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| Acudiente              | Adulto responsable de uno o más estudiantes, autorizado para consultar su información de transporte.                      |
| Alerta                 | Comunicación de alta prioridad generada por un evento o condición operativa, con tipo, nivel de urgencia y destinatarios. |
| Asignación de ruta     | Relación que vincula una ruta con un estudiante, un bus, un conductor o una parada.                                       |
| Bordada                | Registro del evento de subida o de bajada de un estudiante en el bus, con su fecha y hora.                                |
| Bus                    | Vehículo autorizado para el transporte escolar, identificado por su placa y su capacidad.                                 |
| Conducto de desvío     | Condición en la que el vehículo se aparta del trayecto previsto; evento de seguridad sujeto a monitoreo.                  |
| Conductor              | Persona responsable de operar un bus y la ruta asignados.                                                                 |
| Desfase                | Condición en la que el vehículo circula con retraso frente a la programación.                                             |
| Escuela                | Institución educativa titular del servicio de transporte, con sedes y cursos.                                             |
| Estudiante             | Menor transportado, asociado a una familia, una ruta y una parada; sus datos requieren protección reforzada.              |
| Familia                | Estructura de vínculo entre los estudiantes y su acudiente o adultos responsables.                                        |
| Incidente              | Evento operativo o de seguridad que afecta vehículo, ruta, conductor, estudiante o parada.                                |
| Monitoreo              | Observación del progreso de la ruta, el estado del vehículo, su ubicación y los eventos operativos.                       |
| Notificación           | Mensaje dirigido al usuario sobre un evento relevante del trayecto.                                                       |
| Parada                 | Ubicación física donde los estudiantes suben o bajan del bus.                                                             |
| Ruta                   | Trayecto escolar planificado que une la institución con sus paradas, con vehículos y estudiantes asignados.               |
| Sede                   | Locación física de una institución, base de la pertenencia de rutas, buses y perfiles.                                    |
| Trazabilidad           | Capacidad de saber qué ocurrió, cuándo y en relación con qué usuario, vehículo, ruta o evento.                            |
| Ubicación del vehículo | Posición geográfica actual o última conocida de un bus.                                                                   |
| Viaje                  | Ejecución real de una ruta, con bus, conductor, hora de inicio y hora de fin.                                             |

## 9.2 Glosario técnico

**Tabla 68**. _Glosario técnico_

| **Término**             | **Definición**                                                                                            |
| ----------------------- | --------------------------------------------------------------------------------------------------------- |
| API Gateway             | Punto único de entrada que aplica TLS, límite de tasa, CORS, autenticación y ruteo hacia los servicios.   |
| Bearer token            | Credencial transportada en el encabezado Authorization de cada solicitud privada.                         |
| Baja lógica             | Desactivación de un registro mediante estado, sin eliminación física.                                     |
| Identificador de correlación  | Dato técnico: identificador único que se propaga por bitácoras y trazas de una misma transacción.         |
| Esquema por servicio    | Patrón en el que cada servicio posee su propio esquema dentro de una base de datos compartida.            |
| Evento de dominio       | Hecho de negocio relevante publicado para su tratamiento por otros componentes.                           |
| gRPC                    | Protocolo de comunicación sincrónica de baja latencia usado para la telemetría de posicionamiento.        |
| Hash de contraseña      | Representación irreversible de la contraseña; nunca se almacena ni se registra el valor original.         |
| Interruptor de circuito | Mecanismo que aísla un dependiente fallido y evita su llamada continuada.                                 |
| Kafka (KRaft)           | Bus de eventos en modo de operación nativo, sin servicio de coordinación externo.                         |
| Liquibase               | Herramienta de migración de base de datos con cambios versionados y orden determinista.                   |
| Mínimo privilegio       | Principio según el cual cada rol recibe únicamente los permisos que necesita.                             |
| Payload                 | Conjunto de datos transportado por una solicitud o un mensaje.                                            |
| Punto de salud          | Endpoint que informa si el servicio está vivo y si puede recibir tráfico.                                 |
| Rate limit              | Límite de solicitudes por intervalo aplicado en el punto de entrada.                                      |
| RS256                   | Algoritmo de firma de token con par de llaves; solo el servicio de identificación posee la llave privada. |
| SLO                     | Objetivo de nivel de servicio, como la disponibilidad mensual del 99,9 %.                                 |
| Token de actualización  | Credencial de larga vigencia que permite renovar el token de acceso sin repetir credenciales.             |
| Tópico                  | Canal lógico del bus de eventos agrupador de mensajes de un mismo tipo.                                   |
| Umbral de detención     | Tiempo máximo configurado de inmovilización del bus antes de generar una alerta.                          |

# 10. Anexo: matriz de trazabilidad HU↔RF

## 10.1 Matriz de historias de usuario hacia requisitos funcionales

**Tabla 69**. _Matriz de trazabilidad de historias de usuario hacia requisitos funcionales_

| **#** | **HU**          | **Épica** | **RF implementadas**               | **Grupo funcional** |
| ----- | --------------- | --------- | ---------------------------------- | ------------------- |
| 1     | HU-IAM-001      | EP-004    | RF-02.1, RF-02.2                   | RF-02               |
| 2     | HU-USER-004     | EP-004    | RF-01.1, RF-01.2, RF-01.3          | RF-01               |
| 3     | HU-USER-003     | EP-004    | RF-01.4                            | RF-01               |
| 4     | HU-ROUTE-001    | EP-003    | RF-03.1, RF-03.2, RF-03.3, RF-03.4 | RF-03               |
| 5     | HU-FLEET-001    | EP-003    | RF-03.6                            | RF-03               |
| 6     | HU-ROUTE-005    | EP-003    | RF-03.5                            | RF-03               |
| 7     | HU-BUS-002      | EP-003    | RF-03.7                            | RF-03               |
| 8     | HU-ROUTE-008    | EP-003    | RF-03.8                            | RF-03               |
| 9     | HU-QR-004       | EP-001    | RF-04.1                            | RF-04               |
| 10    | HU-QR-001       | EP-001    | RF-04.2, RF-05.1                   | RF-04, RF-05        |
| 11    | HU-QR-002       | EP-001    | RF-04.3                            | RF-04               |
| 12    | HU-NOTIF-001    | EP-002    | RF-05.2, RF-05.3                   | RF-05               |
| 13    | HU-NOTIF-005    | EP-002    | RF-05.4                            | RF-05               |
| 14    | HU-GPS-002      | EP-005    | RF-06.1, RF-06.2, RF-06.3          | RF-06               |
| 15    | HU-ROUTE-003    | EP-003    | RF-07.1                            | RF-07               |
| 16    | HU-INCIDENT-001 | EP-006    | RF-08.1                            | RF-08               |
| 17    | HU-ROUTE-004    | EP-003    | RF-09.1                            | RF-09               |
| 18    | HU-ROUTE-002    | EP-003    | RF-09.2                            | RF-09               |
| 19    | HU-UI-001       | EP-007    | RF-10.1                            | RF-10               |
| 20    | HU-UI-002       | EP-007    | RF-10.2                            | RF-10               |

## 10.2 Cobertura de requisitos funcionales

**Tabla 70**. _Cobertura de requisitos funcionales por grupo_

| **Grupo**                         | **RF del grupo** | **RF trazadas** | **Historias que las cubren**                                       |
| --------------------------------- | ---------------- | --------------- | ------------------------------------------------------------------ |
| RF-01 Gestión de usuarios         | 4                | 4               | HU-USER-004, HU-USER-003                                           |
| RF-02 Autenticación               | 2                | 2               | HU-IAM-001                                                         |
| RF-03 Rutas, flota y asignaciones | 8                | 8               | HU-ROUTE-001, HU-ROUTE-005, HU-BUS-002, HU-ROUTE-008, HU-FLEET-001 |
| RF-04 Escaneo QR                  | 3                | 3               | HU-QR-004, HU-QR-001, HU-QR-002                                    |
| RF-05 Notificaciones de bordada   | 4                | 4               | HU-QR-001, HU-NOTIF-001, HU-NOTIF-005                              |
| RF-06 Rastreo en tiempo real      | 3                | 3               | HU-GPS-002                                                         |
| RF-07 Gestión de viajes           | 1                | 1               | HU-ROUTE-003                                                       |
| RF-08 Incidentes                  | 1                | 1               | HU-INCIDENT-001                                                    |
| RF-09 Consulta de rutas           | 2                | 2               | HU-ROUTE-004, HU-ROUTE-002                                         |
| RF-10 Personalización             | 2                | 2               | HU-UI-001, HU-UI-002                                               |
| **Total**                         | **30**           | **30**          | **20 historias de usuario en 7 épicas**                            |

Cada una de las 20 historias de usuario se verifica mediante criterios de aceptación en formato Given/When/Then y al menos un caso de prueba asociado; todo requisito funcional sin historia o sin caso de prueba correspondiente se considera una brecha de trazabilidad y se gestiona antes de su cierre.

# 11. Referencias

- Congreso de la República de Colombia. (2012). *Ley 1581 de 2012: Por la cual se dictan disposiciones generales para la protección de datos personales*. Diario Oficial.
- European Parliament and Council of the European Union. (2016). *Regulation (EU) 2016/679 on the protection of natural persons with regard to the processing of personal data and on the free movement of such data (General Data Protection Regulation)*. Official Journal of the European Union.
- IEEE. (1998). *IEEE recommended practice for software requirements specifications* (IEEE Std 830-1998). Institute of Electrical and Electronics Engineers.
- International Organization for Standardization. (2011). *Systems and software engineering — Systems and software quality requirements and evaluation (SQuaRE) — System and software quality models* (ISO/IEC 25010:2011). ISO.
- International Organization for Standardization. (2017). *Systems and software engineering — Software life cycle processes* (ISO/IEC/IEEE 12207:2017). ISO.
- International Organization for Standardization. (2022). *Software and systems engineering — Software testing* (ISO/IEC/IEEE 29119:2022). ISO.
- OWASP Foundation. (2021). *OWASP Top 10: 2021 — The ten most critical web application security risks*. https://owasp.org/Top10/
- Presidencia de la República de Colombia. (2013). *Decreto 1377 de 2013, por el cual se reglamenta la Ley 1581 de 2012*. Diario Oficial.
