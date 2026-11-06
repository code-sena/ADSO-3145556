# EduAirControl: Plataforma de Microservicios con IoT para el Monitoreo y Análisis Ambiental de Espacios Educativos

**Jhon Jairo Cortez Vargas · María de los Ángeles Olaya García**

Tecnólogo en Análisis y Desarrollo de Software, Ficha 3145556  
Servicio Nacional de Aprendizaje (SENA) Industria, Neiva, Huila, Colombia  
  

## Resumen

Las condiciones ambientales de los espacios educativos pueden influir en el confort y las actividades de estudiantes y docentes. Sin embargo, la información sobre temperatura, humedad, calidad del aire y ruido puede no ser de mucha importancia para la gentede entornos educativos. Este artículo presenta EduAirControl, una plataforma web y móvil dirigida a mejorar la gestión y el análisis de información ambiental asociada con campus, ambientes educativos y sensores. El proyecto utiliza una arquitectura basada en un API Gateway y seis microservicios desarrollados con Java y Spring Boot, junto con tecnologías de persistencia, mensajería, caché y seguridad. La solución incluye una interfaz web desarrollada con React, una aplicación móvil basada en React Native y Expo, y un prototipo de Internet de las cosas (IoT) compuesto por un ESP32 y sensores ambientales (DHT11, MQ-135, KY-038). La infraestructura se despliega en un servidor virtual privado mediante Docker y Docker Compose, y contempla herramientas para la integración continua, la administración y la observabilidad. El trabajo comprende documentación técnica, contratos de API, decisiones de arquitectura y un plan de pruebas que permite diferenciar los casos diseñados de aquellos efectivamente ejecutados. El despliegue público constituye un avance verificable de la implementación; sin embargo, la validación integral de la seguridad, el rendimiento y el flujo de mediciones desde el prototipo físico hasta la plataforma requiere evidencias específicas y actualizadas. Se concluye que EduAirControl constituye una propuesta tecnológica aplicada al monitoreo ambiental educativo, cuyo grado de madurez debe evaluarse a partir de pruebas reproducibles y del estado real de cada componente.

*Palabras clave:* monitoreo ambiental, Internet de las cosas, microservicios, arquitectura hexagonal, Spring Boot, ESP32, espacios educativos.

---

## Introducción

### El Problema

Las condiciones ambientales de un aula influyen en el confort y el desempeño de quienes la usan. La revisión de Mendell y Heath (2005) relaciona los contaminantes del aire interior y las condiciones térmicas de las escuelas con el rendimiento estudiantil, y Shield y Dockrell (2003) documentan los efectos del ruido sobre los niños en el contexto escolar. 
Estos antecedentes permiten comprender la importancia de estudiar las condiciones ambientales de los espacios educativos, aunque no demuestran por sí mismos los efectos de EduAirControl sobre sus usuarios.

Teniendo esto en cuenta, surge la necesidad de disponer de herramientas que permitan consultar y organizar mediciones ambientales, relacionarlas con el lugar donde se obtuvieron y analizar sus variaciones a lo largo del tiempo. 
El problema abordado por EduAirControl se organiza en cinco dimensiones: acceso a la información, centralización de las mediciones, comprensión de los cambios ambientales, trazabilidad del origen y momento de captura, y control de los permisos de consulta y administración. La plataforma se propone atender estas necesidades mediante una solución que integra interfaces de usuario, servicios de backend y un prototipo de adquisición de datos ambientales.

### Motivación de la Investigación

La motivación fue construir un sistema que no solo funcionara, sino que fuera comprensible, medible y mantenible, y que sirviera como caso de estudio de un proyecto formativo del SENA en el que la documentación, las decisiones de diseño y las pruebas son tan importantes como el código. Además, se quiso comprobar que un prototipo IoT de bajo costo puede integrarse con una arquitectura de microservicios de calidad profesional. 
El despliegue de la plataforma en un servidor virtual privado representa una etapa adicional de implementación, ya que esto permite evaluar las configuraciones de los componentes fuera del entorno local. Este avance también evidencia la importancia de mantener coherentes el código fuente, la infraestructura, la seguridad y la documentación técnica.

### Objetivos de la Investigación

*El objetivo general* fue desarrollar una plataforma web y móvil para gestionar, consultar y analizar información ambiental de los espacios educativos mediante una arquitectura de microservicios y su respectiva integracion progresiva del IoT.

*Objetivos específicos:*

- Implementar mecanismos de autenticación y autorización para diferenciar los permisos de los usuarios.
- Gestionar la información de campus, ambientes educativos, sensores y dispositivos.
- Diseñar servicios para recibir, validar, almacenar y consultar mediciones ambientales.
- Desarrollar interfaces web y móvil para la visualización de información y la gestión de preferencias.
- Construir un prototipo basado en ESP32 para la adquisición de variables ambientales.
- Documentar la arquitectura, los contratos de API, la infraestructura y los procedimientos de instalación.
- Definir y ejecutar pruebas que permitan evaluar la funcionalidad, la seguridad y la integración de los componentes.

La ejecución de estos objetivos debe determinarse mediante los resultados obtenidos en cada componente, diferenciando las funcionalidades implementadas de aquellas que su validación permanece pendiente.

### Importancia del Tema

El Internet de las cosas permite conectar objetos físicos con servicios de software para observar su entorno de manera continua (Atzori et al., 2010; Gubbi et al., 2013). En los espacios educativos, esa capacidad habilita decisiones basadas en datos sobre ventilación, ruido o uso de los ambientes. En el ámbito educativo, el monitoreo ambiental puede facilitar el seguimiento de variables relacionadas con las condiciones de los espacios y proporcionar información útil para su administración. EduAirControl aplica estos principios mediante la integración de sensores, servicios backend y aplicaciones de consulta.

El alcance actual del proyecto se centra en la gestión y el análisis de información ambiental. El control físico automático, la predicción de condiciones futuras y la evaluación de impactos sobre el rendimiento académico no se consideran resultados demostrados y requieren desarrollos o estudios adicionales.


## Trabajos Relacionados

Esta sección reúne antecedentes que fundamentan las principales decisiones técnicas de EduAirControl. No constituye una revisión sistemática de la literatura.

### Internet de las Cosas y Monitoreo Ambiental

Atzori et al. (2010) examinan las diferentes perspectivas del Internet de las cosas, mientras que Gubbi et al. (2013) presentan una arquitectura de referencia que integra sensores, almacenamiento, procesamiento y servicios de aplicación.
EduAirControl adopta este enfoque al organizar la obtención de información ambiental, su procesamiento en el backend y su consulta mediante interfaces web y móvil. La integración efectiva de estos componentes debe verificarse mediante pruebas de extremo a extremo.

### Microservicios y Arquitectura Hexagonal

Fowler y Lewis (2014) definen los microservicios como pequeños servicios independientes que se comunican mediante mecanismos ligeros y se despliegan por separado. Newman (2015) y Richardson (2018) describen sus compromisos, entre ellos la consistencia de datos y la comunicación asíncrona. Para organizar internamente los servicios, el proyecto utiliza como referencia la arquitectura hexagonal de Cockburn (2005), que propone separar el dominio de los mecanismos de entrada y salida. Esta organización es compatible con los principios del diseño guiado por el dominio presentados por Evans (2003).

La separación de responsabilidades facilita la evolución de los módulos, pero también introduce necesidades adicionales de configuración, seguridad, observabilidad y pruebas de integración.

### Seguridad en APIs

OAuth 2.0, descrito por Hardt (2012), y los JSON Web Tokens (JWT), especificados por Jones et al. (2015), proporcionan referencias para implementar mecanismos de autorización y transmisión de información de identidad.
En EduAirControl, la seguridad comprende la autenticación de usuarios, la autorización de operaciones y la protección de las comunicaciones entre componentes. También debe contemplar la autenticación de dispositivos y la prevención del acceso directo no autorizado a los microservicios.
OWASP Foundation (2021) ofrece una referencia para identificar riesgos frecuentes en aplicaciones web y API. Su utilización como marco de revisión no sustituye las pruebas de seguridad ni demuestra automáticamente que la plataforma esté libre de vulnerabilidades.

### Calidad del Software y Pruebas

La norma ISO/IEC 25010 (International Organization for Standardization y International Electrotechnical Commission [ISO/IEC], 2011) define características de calidad como fiabilidad, seguridad y mantenibilidad. Martin (2008) sostiene que el código limpio reduce el costo de mantenimiento, y Forsgren et al. (2018) presenta el patrón de cortacircuitos como un mecanismo para limitar la propagación de fallos en sistemas distribuidos. En EduAirControl, los patrones de resiliencia deben distinguirse entre los documentados, los implementados y los que han sido comprobados mediante pruebas.

## Metodología

### Desarrollo Guiado por Especificación

El proyecto se desarrolló mediante un enfoque de documentación y especificación progresiva. Se organizaron artefactos relacionados con el contexto, el dominio, los requisitos, la arquitectura, los datos, los contratos de API, los microservicios, DevOps, la calidad y la operación.
Las decisiones de diseño se registraron mediante registros de decisión de arquitectura (ADR), mientras que los contratos de las interfaces se documentaron utilizando OpenAPI. El número de decisiones y rutas documentadas debe asociarse con la versión específica de la documentación evaluada.

Este enfoque busca mantener la trazabilidad entre las necesidades identificadas, las decisiones de diseño, la implementación y las pruebas.

### Arquitectura del Sistema

EduAirControl sigue una arquitectura cliente-servidor basada en servicios independientes. La aplicación web y la aplicación móvil consumen interfaces HTTP/JSON expuestas por un API Gateway, que centraliza el enrutamiento de las solicitudes y puede aplicar controles de acceso y otras políticas de comunicación.

El backend está organizado en seis microservicios funcionales y un API Gateway. Estos componentes gestionan la seguridad, los ambientes educativos, las mediciones ambientales, los sensores, la información de usuario y las preferencias de experiencia.

La persistencia se implementa con PostgreSQL y la gestión de cambios de esquema utiliza Liquibase. RabbitMQ se emplea como tecnología de mensajería y Redis como mecanismo de almacenamiento en memoria para las funciones que lo requieren.

La topología de bases de datos debe describirse de acuerdo con la configuración de despliegue de la versión evaluada, distinguiendo entre servidores PostgreSQL, bases de datos y esquemas. La existencia de varios microservicios no implica necesariamente que cada uno disponga de un servidor físico o contenedor independiente.

### Selección de Tecnologías

La selección tecnológica responde a la necesidad de construir una plataforma distribuida con interfaces web y movíl, persistencia y los mecanismos de comunicación entre servicios.

**Tabla 1**

*Pila Tecnológica de EduAirControl*

| Capa | Tecnología |
|---|---|
| Backend | Java 17, Spring Boot 4.0.5, Spring Data JPA |
| Seguridad | Spring Authorization Server, JWT RS256, inicio de sesión con Google y Facebook |
| Datos | PostgreSQL 15, Liquibase |
| Mensajería y caché | RabbitMQ 3.13, Redis 7 |
| Web | React 19, Vite 8, React Router, i18next, Recharts |
| Móvil | Expo 54, React Native 0.81, AsyncStorage |
| Comunicación con dispositivos | ESP32 y Bluetooh Low Energy (BLE) |
| Contenedores | Docker, Docker Compose |
| Integración y despliegue | Jenkins y mecanismos de automatización configurados para los repos |
| Administración y observabilidad |  Componentes de administración de contenedores, métricas, visualización y telemetría, según su estado real de funcionamiento |

### Módulos Funcionales

El backend esta organizado en los siguientes comoponentes:

*Módulos de Backend*

| Servicio | Responsabilidad | Puerto | 
|---|---|---|
| API Gateway | Enrutamiento, rate limiting (Redis), validacion JWT RS256 y BFF/OAuth2 | 8080 |
| ms-security | Registro, login, OAuth2/OIDC, JWKS, roles, instituciones, tokens de dispositivo, recuperación de contraseña | 8081 |
| ms-classroom-management | Campus, tipos de ambiente y ambientes educativos | 3002 |
| ms-environment-monitoring | Mediciones, panel, análisis, umbrales y alertas | 3003 |
| ms-sensor-management | Catálogo (modelos/estados/variables/unidades), sensores  e instalaciones | 3004 |
| ms-user-experience | Favoritos, preferencias, calificaciones, busquedas y estado de notificaciones | 3006 |
| ms-user-management | Perfil de usuario (nombre, telefono, institucion, avatar) | 3007 |


### Seguridad

El acceso de usuarios se basa en JWT firmados con RS256, con expiración y revocación: al cerrar sesión el token se registra en Redis y su reutilización se rechaza con un código 401. Los servicios validan la firma contra el conjunto de llaves públicas (JWKS) de ms-security. Los cuatro roles separan las operaciones de administración de las de consulta. Los dispositivos usan una clave X-API-Key que se muestra una sola vez al crearse o rotarse. Los secretos se configuran por variables de entorno y CORS es configurable.
Los hallazgos de seguridad identificados en auditorías anteriores deben tratarse como antecedentes hasta realizar una revisión de la versión actual. La corrección de una vulnerabilidad solo debe considerarse verificada cuando existan evidencias de que el control correspondiente funciona y el escenario de ataque deja de ser reproducible.

### Prototipo IoT

El prototipo físico utiliza un ESP32 como controlador y sensores destinados a la adquisición de variables ambientales. El DHT11 permite obtener lecturas de temperatura y humedad relativa; el KY-038 proporciona una señal relacionada con el sonido, y el MQ-135 responde a determinados gases y compuestos presentes en el ambiente.
Las lecturas del KY-038 no equivalen por sí solas a mediciones acústicas calibradas en decibelios. De igual forma, el MQ-135 no debe presentarse como un medidor selectivo y calibrado de CO₂ en partes por millón sin procedimientos de calibración y validación apropiados.
La integración contempla la comunicación BLE para las funciones de configuración que estén implementadas en la aplicación móvil. Debe diferenciarse esta comunicación de la transmisión de mediciones al backend, ya que son procesos distintos.
La validación del prototipo debe establecer qué variables se han leído físicamente, bajo qué condiciones se realizaron las pruebas y si los datos llegaron correctamente al servicio de ingesta y quedaron almacenados. Sin estas evidencias no es posible afirmar que el flujo completo se encuentra operativo.

## Implementación

### Estructura de un Microservicio

Los microservicios se organizan como componentes de backend con responsabilidades funcionales diferenciadas. La arquitectura hexagonal sirve como referencia para separar las reglas del dominio, la coordinación de casos de uso y los mecanismos de entrada y salida.
En esta organización, el dominio contiene las reglas propias del servicio; la capa de aplicación coordina los casos de uso, y la infraestructura implementa los controladores, adaptadores de persistencia y mecanismos de comunicación.

### Persistencia y Migraciones

PostgreSQL proporciona la persistencia de la información gestionada por los servicios. Liquibase permite versionar y ejecutar cambios en la estructura de los datos, facilitando el seguimiento de las modificaciones del esquema.
Cuando Hibernate se configura con ddl-auto=validate, la aplicación comprueba la correspondencia entre las entidades y el esquema existente en lugar de utilizar esa configuración para generar automáticamente todos los cambios de estructura.

### Mensajería y Consistencia

RabbitMQ permite implementar comunicación asíncrona entre servicios mediante eventos. Este mecanismo puede reducir el acoplamiento entre productores y consumidores, aunque requiere definir el tratamiento de fallos, los reintentos y los eventos duplicados.
El patrón outbox, descrito por Richardson (2018), permite registrar un evento en la misma transacción que los cambios de datos relacionados y publicarlo posteriormente. Su utilización en un servicio debe confirmarse revisando la implementación del registro, el proceso de publicación y el comportamiento del consumidor.

### Flujo de una Medición

El flujo de información ambiental tiene cuatro pasos:

- Los sensores capturan las variables del entorno, de acuerdo con sus capacidades técnicas.
- El ESP32 obtiene y prepara las lecturas del prototipo.
- La comunicación con la aplicación móvil permite ejecutar las funciones BLE implementadas, sin asumir que este mecanismo sustituye el envío de datos al servidor.
- El dispositivo transmite las mediciones al punto de ingesta cuando la conexión y el firmware correspondiente están disponibles.
- El backend valida la solicitud y la autenticación del dispositivo, procesa la información y la almacena cuando la operación resulta válida.
- Las aplicaciones web y móvil consultan los servicios para presentar la información disponible.

### Despliegue

El entorno de desarrollo local utiliza Docker Compose para organizar los contenedores de la aplicación y sus dependencias. La configuración de producción utiliza un servidor virtual privado de Hostinger, donde Docker y Docker Compose permiten ejecutar los componentes desplegados.
La infraestructura contempla un punto de entrada web, servicios de backend, almacenamiento de datos y herramientas de administración. También incluye trabajo de automatización de CI/CD con Jenkins y componentes destinados a la administración y observabilidad, como Portainer, Prometheus, Grafana y OpenTelemetry.

La presencia de estas herramientas debe diferenciarse de su grado de integración. Para describir una función como operativa se requiere comprobar, según corresponda, la ejecución de los pipelines, la recolección de métricas, la recepción de telemetría y la visualización de datos.

## Pruebas y Validación

### Estrategia de Pruebas

El proyecto contempla pruebas unitarias, de integración, de contrato, funcionales, de sistema, de IoT, de regresión y no funcionales. El plan establece técnicas como partición de equivalencia, análisis de valores límite, pruebas negativas, verificación por roles y pruebas de caja negra.
Cada caso debe registrar su resultado mediante un estado definido, como aprobado, fallido, bloqueado, no ejecutado o no aplicable. Esta clasificación evita confundir un caso diseñado con uno efectivamente probado.

### Pruebas Automatizadas

El inventario histórico del código registró 333 métodos de prueba distribuidos en 48 clases en la versión analizada en octubre de 2026. Esta cifra representa un conteo del código fuente y no debe interpretarse como el número de pruebas aprobadas.

Para actualizar este resultado se debe identificar el commit examinado, ejecutar las pruebas aplicables y registrar los resultados por servicio. También debe distinguirse entre pruebas unitarias, pruebas que requieren el contexto de Spring y pruebas que dependen de contenedores o otros servicios.

*Pruebas Automatizadas por Servicio*

| Servicio | Clases | Métodos |
|---|---|---|
| ms-environment-monitoring | 15 | 107 |
| ms-security | 11 | 77 |
| ms-sensor-management | 7 | 55 |
| ms-classroom-management | 4 | 33 |
| API Gateway | 6 | 29 |
| ms-user-management | 2 | 20 |
| ms-user-experience | 3 | 12 |
| **Total** | **48** | **333** |

### Pruebas Manuales

Las pruebas manuales deben verificar las funciones de las interfaces web y móvil, el comportamiento según los roles, la navegación, las preferencias y las operaciones de gestión de ambientes y sensores.

En el componente IoT deben contemplarse escenarios como la desconexión del sensor, la pérdida de conectividad, la denegación de permisos BLE y los fallos de transmisión a la API.
La cantidad de casos manuales y su estado deben obtenerse del registro de ejecución vigente. Los conteos de versiones anteriores no deben reutilizarse como resultados actuales sin una nueva comprobación.

### Criterios de Aceptación

*Criterios de Aceptación y estado de medición*

| Indicador | Criterio | Medición |
|---|---|---|
| Verificación de salud | Menos de 1 s | Pendiente |
| Inicio de sesión | Menos de 2 s | Pendiente |
| Panel con datos de prueba | Menos de 3 s | Pendiente |
| Errores HTTP 5xx | 0 en el flujo crítico | Pendiente |
| Disponibilidad | 100 % del tiempo planificado | Pendiente |

*Nota.* Pendiente: aún no se ejecutó la medición.

## Resultados

Los resultados se presentan tal como existen al 10 de octubre de 2026. No se reportan métricas de desempeño porque todavía no se midieron.

### Entregables

El proyecto ha avanzado desde el entorno local de desarrollo hasta una infraestructura desplegada en un servidor virtual privado. La plataforma cuenta con una dirección web pública y con componentes ejecutados mediante Docker.
En las comprobaciones realizadas durante octubre de 2026 se verificó que el frontend respondía satisfactoriamente a una solicitud HTTP. Este resultado permite documentar la publicación de la interfaz web, pero no sustituye las pruebas funcionales del inicio de sesión, las API, los microservicios ni la integración con el prototipo IoT.

La evaluación debe registrar la fecha, el commit y las pruebas utilizadas para comprobar el estado de cada componente.

### Implementacion de la arquitectura

La organización del backend en un API Gateway y seis microservicios permite distribuir las responsabilidades funcionales de la plataforma. La documentación técnica y los contratos de API proporcionan una base para analizar las interfaces y la interacción entre componentes.
La valoración de la arquitectura debe considerar tanto la separación de responsabilidades como las necesidades adicionales de configuración, comunicación, seguridad y mantenimiento. La independencia efectiva de los servicios y la consistencia de sus mecanismos de integración requieren verificaciones específicas.

### Estado de las Pruebas

El proyecto dispone de un plan de pruebas y de un inventario histórico de pruebas automatizadas. Sin embargo, el conteo de métodos no constituye evidencia de que todas las pruebas pasen en la versión desplegada.
Los reportes anteriores identificaron incidencias en determinadas ejecuciones de pruebas de integración. Esas incidencias deben considerarse antecedentes hasta comprobar su estado mediante una nueva ejecución.
Para presentar resultados actualizados se deben incluir, como mínimo, la versión analizada, los casos ejecutados, los resultados obtenidos, los defectos detectados y los casos pendientes. Las métricas de rendimiento y disponibilidad solo deben reportarse cuando existan mediciones reproducibles.

### Estado de la integracion IoT

El prototipo físico constituye el componente destinado a adquirir información ambiental. La validación de este componente debe diferenciar las lecturas obtenidas localmente de la transmisión al backend y de la visualización en las aplicaciones.

La integración se considerará comprobada de extremo a extremo únicamente cuando exista evidencia de que una medición identificable se captura, transmite, valida, almacena y consulta correctamente. Hasta contar con esa evidencia, el resultado debe describirse de acuerdo con la etapa efectivamente verificada.

### Registro de Riesgos

Las auditorías anteriores identificaron riesgos relacionados con el control de acceso, la protección de secretos, la configuración de seguridad, la cobertura de pruebas y la observabilidad.
Estos hallazgos proporcionan una referencia para orientar la revisión técnica, pero no deben presentarse como un inventario vigente sin comprobar el código y la configuración actuales.

El registro de riesgos debe indicar para cada hallazgo su descripción, severidad, componente afectado, acción correctiva, evidencia de verificación y estado. Una vulnerabilidad solo debe declararse cerrada cuando exista evidencia de que el escenario identificado ha sido corregido y validado.

## Discusión

### Interpretación

EduAirControl integra una aplicación web, una aplicación móvil, servicios de backend y un prototipo de adquisición de información ambiental. Esta combinación permite estudiar la aplicación de una arquitectura de microservicios a una necesidad concreta del entorno educativo.

La publicación de la plataforma en una infraestructura remota representa un avance respecto de una implementación limitada al entorno local. Sin embargo, la disponibilidad de la interfaz no demuestra por sí sola la calidad integral del sistema ni la operación correcta de todos los servicios.
La experiencia también muestra que la arquitectura documentada y la infraestructura desplegada deben mantenerse sincronizadas. Las diferencias entre configuraciones, versiones de código y dependencias pueden afectar la reproducibilidad de las pruebas y el comportamiento de los servicios. Por otra parte, la separación en microservicios facilita la distribución de responsabilidades, pero incrementa la dificultad de las comunicaciones, la gestión de credenciales y la observabilidad. Por ello, la calidad debe evaluarse considerando tanto las características individuales de cada componente como sus interacciones.

### Lecciones Aprendidas

Una de las principales lecciones del proyecto consiste en la importancia de diferenciar entre diseño, implementación y validación. Una funcionalidad descrita en los requisitos o presente parcialmente en el código no equivale necesariamente a una función probada en condiciones de uso.
También se evidencia la necesidad de registrar las versiones evaluadas, mantener actualizada la documentación y ejecutar pruebas después de los cambios de infraestructura. Este procedimiento permite relacionar los resultados con una configuración concreta y facilita la identificación de regresiones.

Por ultimo, la integración de hardware y software requiere validar cada etapa del flujo de datos. Las lecturas de un sensor, la comunicación con el dispositivo móvil y el envío al backend deben analizarse de forma diferenciada antes de afirmar que el sistema opera integralmente.

### Amenazas a la Validez

El trabajo tiene tres amenazas principales. Primero, las conclusiones sobre rendimiento y disponibilidad dependen de la existencia de mediciones reproducibles. Si estas no se han realizado, no es posible afirmar que se cumplen los criterios establecidos. Segundo, la validación del prototipo no equivale a una evaluación prolongada en aulas reales. Las condiciones de instalación, la variabilidad ambiental y las características de los sensores pueden afectar las lecturas. Tercero, el inventario estático de pruebas no garantiza su aprobación. Es necesario ejecutar los casos sobre la versión evaluada y registrar los resultados.

Finalmente, la existencia de mecanismos de seguridad no sustituye una evaluación específica de vulnerabilidades ni demuestra por sí sola la ausencia de riesgos.

## Conclusiones

### Resumen de Hallazgos

EduAirControl constituye una plataforma tecnológica orientada a la gestión y el análisis de información ambiental en espacios educativos. Su arquitectura integra interfaces web y móvil, servicios backend y un prototipo IoT, con una infraestructura basada en contenedores.

El despliegue remoto representa un avance en la implementación del proyecto. La evaluación de su funcionamiento integral requiere, además, evidencias actualizadas de las API, la persistencia, la seguridad y la transmisión de mediciones desde los dispositivos físicos.

### Contribuciones

La contribución práctica del proyecto consiste en la construcción de una plataforma para gestionar información ambiental de espacios educativos y en la implementación de un prototipo IoT asociado con esta finalidad.

La contribución técnica comprende la organización del backend en servicios funcionales, el uso de tecnologías de persistencia y mensajería, y la documentación de las interfaces y decisiones de arquitectura.
La contribución metodológica consiste en establecer un proceso de pruebas que permite diferenciar los casos definidos de los ejecutados y aprobados, así como registrar los riesgos y las limitaciones identificadas durante el desarrollo.

### Limitaciones

La evaluación debe precisar el estado de la integración completa entre los sensores, el ESP32, la comunicación BLE, el servicio de ingesta y las interfaces de consulta. También debe establecer el grado de calibración de los sensores utilizados y la precisión de las variables obtenidas.

Las conclusiones sobre rendimiento, escalabilidad, disponibilidad y seguridad deben limitarse a las propiedades que hayan sido verificadas. La evaluación en mas aulas reales, las pruebas de carga y la comprobación integral de los mecanismos de recuperación requieren evidencia adicional cuando no se hayan realizado.

### Trabajos Futuros

A corto plazo, se propone completar las verificaciones pendientes de la integración IoT, ejecutar los casos de prueba sobre una versión identificada y revisar los controles de seguridad de los servicios y dispositivos.

A mediano plazo, se plantea ampliar las pruebas automatizadas de las aplicaciones web y móvil, medir la cobertura del código y comprobar la integración de los mecanismos de observabilidad y de los procesos de CI/CD.
También se propone evaluar la idoneidad de los sensores utilizados, calibrar o sustituir el MQ-135 cuando se requieran mediciones de CO₂ con precisión conocida y estudiar mecanismos de resiliencia para los servicios distribuidos.

A largo plazo, se plantea realizar una evaluación controlada en aulas reales durante un periodo suficiente para analizar las variaciones ambientales, la estabilidad del prototipo y la utilidad de los datos para la administración de los espacios educativos.

### Reflexión Final

El desarrollo de EduAirControl evidencia la importancia de integrar diseño, implementación, documentación y validación en un proyecto de software. La publicación de una plataforma constituye un avance relevante, pero las afirmaciones sobre su calidad deben estar respaldadas por pruebas y mediciones verificables.

Distinguir entre las capacidades implementadas y las que todavía requieren validación permite describir el proyecto con rigor y establecer una ruta de mejora basada en evidencia.

## Referencias



Referencias

Atzori, L., Iera, A., & Morabito, G. (2010). The Internet of Things: A survey. Computer Networks, 54(15), 2787–2805. https://doi.org/10.1016/j.comnet.2010.05.010

Cockburn, A. (2005). Hexagonal architecture. https://alistair.cockburn.us/hexagonal-architecture/

Equipo EduAirControl. (2026a). Manual técnico de EduAirControl (versión 1.1) [Documento técnico]. SENA Industria.

Equipo EduAirControl. (2026b). Plan de pruebas de EduAirControl (versión 1.0) [Documento técnico]. SENA Industria.

Equipo EduAirControl. (2026c). Registro de riesgos del proyecto [Documento técnico]. SENA Industria.

Espressif Systems. (s. f.). ESP32 series datasheet. https://www.espressif.com/

Evans, E. (2003). Domain-driven design: Tackling complexity in the heart of software. Addison-Wesley.

Forsgren, N., Humble, J., & Kim, G. (2018). Accelerate: The science of lean software and DevOps. IT Revolution Press.

Fowler, M., & Lewis, J. (2014). Microservices: A definition of this new architectural term. https://martinfowler.com/articles/microservices.html

Gubbi, J., Buyya, R., Marusic, S., & Palaniswami, M. (2013). Internet of Things (IoT): A vision, architectural elements, and future directions. Future Generation Computer Systems, 29(7), 1645–1660. https://doi.org/10.1016/j.future.2013.01.010

Hardt, D. (Ed.). (2012). The OAuth 2.0 authorization framework (RFC 6749). Internet Engineering Task Force. https://doi.org/10.17487/RFC6749

International Organization for Standardization, & International Electrotechnical Commission. (2011). Systems and software engineering—Systems and software quality requirements and evaluation (SQuaRE)—System and software quality models (ISO/IEC 25010:2011).

Jones, M., Bradley, J., & Sakimura, N. (2015). JSON Web Token (JWT) (RFC 7519). Internet Engineering Task Force. https://doi.org/10.17487/RFC7519

Martin, R. C. (2008). Clean code: A handbook of agile software craftsmanship. Prentice Hall.

Mendell, M. J., & Heath, G. A. (2005). Do indoor pollutants and thermal conditions in schools influence student performance? A critical review of the literature. Indoor Air, 15(1), 27–52. https://doi.org/10.1111/j.1600-0668.2004.00320.x

Newman, S. (2015). Building microservices: Designing fine-grained systems. O’Reilly Media.

Nygard, M. T. (2018). Release it! Design and deploy production-ready software (2nd ed.). Pragmatic Bookshelf.

OpenAPI Initiative. (2021). OpenAPI specification (versión 3.1.0). https://spec.openapis.org/oas/v3.1.0

OWASP Foundation. (2021). OWASP Top 10: 2021. https://owasp.org/Top10/

Richardson, C. (2018). Microservices patterns: With examples in Java. Manning Publications.

Shield, B. M., & Dockrell, J. E. (2003). The effects of noise on children at school: A review. Building Acoustics, 10(2), 97–116.

## Apéndice

Datos: las cifras provienen del código fuente del proyecto (inventario de pruebas), de los reportes de ejecución del 7 de octubre de 2026 y del registro de riesgos. No se usaron datos personales reales.

Código: repositorio del proyecto, en el archivo 15_Enlace_Repositorio_GitHub.txt de la entrega; versión (commit) por registrar.

Entorno: Windows; los reportes previos muestran JDK 25.0.2, mientras que los POM declaran Java 17. Versión de Docker y Node por registrar.

Anexos: contratos OpenAPI, ADR, archivos de infraestructura, registro de riesgos, capturas e inventario de pruebas (carpeta 14_Anexos).
