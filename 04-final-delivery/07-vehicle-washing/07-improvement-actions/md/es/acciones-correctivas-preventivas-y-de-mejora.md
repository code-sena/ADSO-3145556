<!-- running-head: ACCIONES CORRECTIVAS, PREVENTIVAS Y DE MEJORA: SISTEMA DE LAVADO VEHICULAR -->

# Documentación de Acciones Correctivas, Preventivas y de Mejoramiento del Software

Sistema de Gestión de Lavado Vehicular (Vehicle Washing)

Procedimiento de Gestión, Registro de Acciones, Análisis de Causa Raíz, Seguimiento y Lecciones Aprendidas

Brayan Estiven Patiño Cabrera (líder de proyecto), Juan Camilo Guilombo Osorio (líder de frontend) y Merari Urbano Quintero (líder de backend)

Tecnólogo en Análisis y Desarrollo de Software (ADSO), Servicio Nacional de Aprendizaje (SENA), Centro de Formación: Industria

Ficha 3145556

Instructora: Karol Daniela Correa

4 de noviembre de 2026

Tipo de documento: Registro de acciones correctivas, preventivas y de mejoramiento (equipo de desarrollo e instructora) · Versión 3.5

## Control de Versiones

La Tabla 1 registra el historial de cambios de este documento. El registro de acciones (secciones 9 a 12) es un documento vivo: cada vez que se abre, se actualiza o se cierra una acción, se modifica la fila de la acción y, al menos una vez por sprint, se agrega una fila a esta tabla.

**Tabla 1**

*Historial de Versiones del Documento de Acciones*

| Versión | Fecha | Descripción del cambio | Responsable |
|---|---|---|---|
| 1.0 | 2026-10-09 | Creación del documento (en español y en inglés). Se define el procedimiento de gestión y se registran 23 acciones correctivas cerradas, 13 acciones correctivas abiertas, 14 acciones preventivas y 13 acciones de mejoramiento, verificadas contra el código de los once repositorios (rama *develop* al 9 de octubre de 2026), el historial de Git, el registro de riesgos y el backlog técnico del proyecto. | Equipo de desarrollo |
| 2.0 | 2026-10-16 | Revisión contra el código del 16 de octubre de 2026. Se cerraron once acciones correctivas abiertas (ACO-01 a ACO-03 y ACO-05 a ACO-12, ahora AC-24 a AC-34) y cinco de mejoramiento (AM-01, AM-04, AM-08, AM-10 y AM-11); se registraron y cerraron cinco no conformidades encontradas en la revisión completa del sistema (AC-35 a AC-39); se actualizaron las acciones preventivas, los indicadores, las lecciones aprendidas y la trazabilidad. | Equipo de desarrollo |
| 2.1 | 2026-10-17 | Revisión contra el código del 17 de octubre de 2026. Se cerraron las correctivas AC-40 a AC-43 (aviso de sesión expirada al cerrar sesión, pantallas que se salían en el celular, campos de filtro aplastados en la app y avance del servicio antes de empezar), se agregó la preventiva AP-15 (revisión de diseño adaptable antes de fusionar) y se cerraron las mejoras AM-07 (informe de inspección en la app móvil) y AM-08 (preguntas frecuentes en la página de inicio). | Equipo de desarrollo |
| 2.2 | 2026-10-19 | Se cerró la acción preventiva AP-01 con la integración continua en GitHub Actions de los 10 repositorios. | Equipo de desarrollo |
| 2.3 | 2026-10-19 | Indicador de pruebas con la ejecución del 19 de octubre (655 automatizadas, 19 de punta a punta y 42 casos funcionales). | Equipo de desarrollo |
| 2.4 | 2026-10-20 | Revisión contra el código del 20 de octubre de 2026: el registro sigue vigente (AM-03, la reprogramación desde la web y la app, y AM-12, los estados de pausa y novedad, siguen abiertas); se completó el nombre de la líder de backend. | Equipo de desarrollo |
| 2.5 | 2026-10-21 | Revisión contra el código del 21 de octubre de 2026. Se registraron y cerraron las correctivas AC-44 a AC-52 (token que seguía sirviendo después de cerrar sesión, reservas que nadie cerraba, asignación doble de un operario, comprobantes sin límite de tamaño, efectivo visible aunque estuviera desactivado, reportes que contaban reservas no terminadas, texto engañoso de la pantalla de roles, pestañas con la sesión desfasada y registro de transacciones de la base sin límite); se cerró la preventiva AP-04 (respaldos diarios verificados), se abrió la correctiva ACO-14 (variables nuevas fuera de *.env.example* y del compose) y se agregaron y cerraron AP-16 a AP-18 (registros de contenedores, encabezados de seguridad y chequeos de salud) y la mejora AM-16 (actualización obligatoria del APK); las mejoras del 17 de octubre que repetían los identificadores AM-07 y AM-08 pasan a ser AM-14 (informe de inspección en la app) y AM-15 (preguntas frecuentes); indicadores con las pruebas del 21 de octubre. | Equipo de desarrollo |
| 2.6 | 2026-10-21 | Revisión contra el código de la noche del 21 de octubre de 2026. Se ajustó AC-45 y se registraron y cerraron las correctivas AC-53 (la primera versión del cierre automático podía registrar como "No asistió" un servicio que sí se hizo) y AC-54 (un campo `null` en un evento mandaba el mensaje a la cola de errores de notification-service); se agregaron la preventiva AP-19 y la lección L-16; ACO-14 incluye las variables *FOLLOW_UP_\** de operations-service; indicadores con la ejecución final del 21 de octubre (707 pruebas). | Equipo de desarrollo |
| 2.7 | 2026-10-23 | Revisión contra *develop* del 23 de octubre de 2026, la versión *v1.0.0* y la revisión del sistema de ese día. Se cerraron ACO-14 (AC-55) y las correctivas que Merari Urbano corrigió ese día en *develop* (AC-56 a AC-71, con su commit), y la documentación desactualizada (AC-72). Se abrieron ACO-15 a ACO-32 con los hallazgos que siguen abiertos y las salvedades de los arreglos del día (SEC-01, BIZ-01, BIZ-02, OPS-01, CD-01, entre otros); dos son P1, así que el criterio de entrada E6 deja de cumplirse. Se recalculó la Tabla 10, se agregaron las mejoras AM-17 (despliegue continuo) y AM-18 (pruebas de los clientes) y el Anexo D con la evaluación del instructor (H-01 a H-25) y su estado. | Equipo de desarrollo |
| 2.8 | 2026-10-24 | Revisión contra *develop* del 24 de octubre de 2026 (128 ramas fusionadas en los 10 repositorios, con el CI en verde) y el despliegue en Azure de ese día. Se cerraron ACO-15 a ACO-18, ACO-20 a ACO-23, ACO-27, ACO-29, ACO-31 y ACO-32 (AC-73 a AC-84, cada una con su repositorio y su commit), más la protección que impide bajar de versión el servidor (AC-85) y la web sin *root* en el puerto 8080 (AC-86). Se actualizaron ACO-13, ACO-19, ACO-24 a ACO-26 y ACO-28 (ACO-24 queda solo con la app móvil, porque payment-service tiene *v1.0.0* desde el 24 de octubre), y se abrieron ACO-33 a ACO-40 con los pendientes de la revisión cruzada final. No queda ninguna P0 ni P1 abierta, así que el criterio E6 vuelve a cumplirse. Se recalcularon la Tabla 10 (86 de 102) y el conteo de pruebas (1 192), se cerró la preventiva AP-11, se agregaron las lecciones L-17 y L-18 y se actualizó el Anexo D (9 corregidos, 8 parciales, 6 abiertos, 1 aceptado y 1 que no se puede corregir). | Equipo de desarrollo |
| 2.9 | 2026-10-24 | Se cerró AC-87: después del despliegue del 24 de octubre, notification-service no podía leer *pending_delivery* (columnas VARCHAR de la migración 047) y no enviaba push ni correos; la migración 048 las pasó a NVARCHAR y se desplegó ese día a las 17:12 UTC. Se recalculó la Tabla 10 (87 de 103) y se agregó la lección L-19 sobre las pruebas de integración contra SQL Server (RNF-003). | Equipo de desarrollo |
| 3.0 | 2026-10-26 | Pago al reservar. Se cerró AC-88, acción preventiva y de mejora que nació de la pregunta de la sustentación y de la recomendación del instructor: mitigación de reservas masivas con el modo de pago al reservar, el efectivo con límite y el vencimiento del pago digital. Se cerró AC-89: en la prueba en vivo se perdió un evento y el job canceló una reserva pagada, y ahora booking le pregunta a payment antes de cancelar. Se abrió ACO-41 (tolerancia por retraso en el efectivo). Se recalcularon la Tabla 10 (89 de 106) y el conteo de pruebas, y se agregaron la lección L-20 y las acciones nuevas en el Anexo C. | Equipo de desarrollo |
| 3.1 | 2026-10-26 | Versiones *v1.1.0* y *v1.1.1*. Se cerró ACO-19 como AC-90: el despliegue continuo corrió solo por primera vez la noche del 26 de octubre y la OTA del móvil se publicó tras comprobar la huella nativa. ACO-24 quedó solo con la protección de *develop*. Se actualizaron la Tabla 10 (90 de 106), AM-17, H-15 y el Anexo C. | Equipo de desarrollo |
| 3.2 | 2026-10-27 | Conteo de pruebas del 27 de octubre de 2026 (booking 137, customer-service 83: 1 311) y pruebas de persistencia de payment en el CI (Tabla 10). | Equipo de desarrollo |
| 3.3 | 2026-10-27 | Versiones *v1.1.1* y *v1.1.2* del 27 de octubre. Se cerraron AC-91 (400 en lugar de 500 con una lista de servicios o una semana del horario inválidas), AC-92 (`bookedBy` visible para el cliente) y AC-93 (horas alternativas que el vehículo no podía tomar). Se abrieron ACO-42 (contacto legal de ejemplo), ACO-43 (reservas sin paginar) y ACO-44 (pantalla de historial de pagos, RF-015). Avance de ACO-24 y ACO-26; H-01 y H-02 pasan a Corregido, H-15 a Parcial (la máquina se sigue encendiendo bajo demanda y falta el monitor externo) y H-21 a Parcial (fondos en WebP). Se recalcularon la Tabla 10 (93 de 112, 1 337 pruebas), AM-18, L-19 y el Anexo C. | Equipo de desarrollo |
| 3.4 | 2026-10-27 | ACO-25: avance del 27 de octubre en payment-service (contraseñas fuera del código, sin credenciales por defecto y revisión del historial). Sigue abierta hasta rotar las contraseñas. | Equipo de desarrollo |
| 3.5 | 2026-11-04 | Indicador de pruebas automatizadas con payment-service *v1.1.3*, fusionada en *main* el 27 de octubre (275 de payment y 1 348 en total); ACO-25 con el estado de *v1.1.3*, y H-23 con la rama *main* de *vehicle-w-docs*, al día con *docs*. | Equipo de desarrollo |

*Nota.* Las fechas se escriben en formato ISO 8601 (año-mes-día). Elaborado por el equipo de desarrollo.

## Tabla de Contenido

<!-- toc -->

- [Control de Versiones](#control-de-versiones)
- [4. Introducción](#4-introducción)
  - [4.1 Marco de Referencia](#41-marco-de-referencia)
  - [4.2 Fuentes de Este Documento](#42-fuentes-de-este-documento)
- [5. Objetivo](#5-objetivo)
  - [5.1 Objetivo General](#51-objetivo-general)
  - [5.2 Objetivos Específicos](#52-objetivos-específicos)
- [6. Alcance](#6-alcance)
- [7. Definiciones y Clasificación](#7-definiciones-y-clasificación)
  - [7.1 Tipos de Acción](#71-tipos-de-acción)
  - [7.2 Severidad y Prioridad](#72-severidad-y-prioridad)
  - [7.3 Estados de una Acción](#73-estados-de-una-acción)
- [8. Procedimiento de Gestión de Acciones](#8-procedimiento-de-gestión-de-acciones)
  - [8.1 Fuentes de Identificación](#81-fuentes-de-identificación)
  - [8.2 Etapas del Procedimiento](#82-etapas-del-procedimiento)
  - [8.3 Análisis de Causa Raíz](#83-análisis-de-causa-raíz)
  - [8.4 Verificación de la Eficacia](#84-verificación-de-la-eficacia)
  - [8.5 Roles](#85-roles)
- [9. Registro de Acciones Correctivas Cerradas](#9-registro-de-acciones-correctivas-cerradas)
- [10. Registro de Acciones Correctivas Abiertas](#10-registro-de-acciones-correctivas-abiertas)
- [11. Registro de Acciones Preventivas](#11-registro-de-acciones-preventivas)
- [12. Registro de Acciones de Mejoramiento](#12-registro-de-acciones-de-mejoramiento)
- [13. Indicadores de Seguimiento](#13-indicadores-de-seguimiento)
- [14. Lecciones Aprendidas](#14-lecciones-aprendidas)
- [15. Glosario](#15-glosario)
- [Referencias](#referencias)
- [16. Anexos](#16-anexos)
  - [Anexo A. Formato de Registro de una Acción](#anexo-a-formato-de-registro-de-una-acción)
  - [Anexo B. Ejemplo de Análisis de Causa Raíz](#anexo-b-ejemplo-de-análisis-de-causa-raíz)
  - [Anexo C. Trazabilidad Entre Acciones, Riesgos y Deuda Técnica](#anexo-c-trazabilidad-entre-acciones-riesgos-y-deuda-técnica)
  - [Anexo D. Evaluación del Instructor](#anexo-d-evaluación-del-instructor)

<!-- /toc -->

*Nota.* La numeración empieza en 4 porque las secciones 1 a 3 son la portada, el control de versiones y la tabla de contenido, que no llevan número en el título. Se conserva así para que sigan valiendo las referencias entre documentos por número de sección (por ejemplo, "Manual de Instalación, sección 10").

## 4. Introducción

El Sistema de Gestión de Lavado Vehicular (Vehicle Washing), llamado internamente *LavaRapido*, es una plataforma de reservas, asignación de operarios, ejecución y calificación de servicios, verificación de pagos por código QR y fidelización por puntos para un lavadero. Está formado por un gateway, seis microservicios (cinco en Java 21 con Spring Boot 4 y uno en .NET 8), una base de datos SQL Server 2022, RabbitMQ y dos clientes, web (Angular 21) y móvil (React Native con Expo) (Equipo de Desarrollo Vehicle Washing, 2026a).

Durante su construcción, sus revisiones con la instructora y la elaboración de sus manuales se han detectado defectos, riesgos y oportunidades de mejora. Este documento establece **cómo** se gestionan (identificación, registro, análisis de causa raíz, planificación, ejecución, verificación y cierre) y **registra** las acciones tomadas y pendientes, de modo que cualquier integrante del equipo, la instructora o un futuro equipo de mantenimiento sepan qué se corrigió, por qué, con qué evidencia y qué queda por hacer.

### 4.1 Marco de Referencia

- **ISO/IEC/IEEE 12207:2017.** El proceso de Mantenimiento (6.4.13) incluye corregir defectos, prevenir fallas y mejorar el software en operación, y el proceso de Aseguramiento de la Calidad (6.3.8) exige tratar los incidentes y los problemas detectados y verificar que las acciones sean eficaces (International Organization for Standardization [ISO] et al., 2017).
- **ISO/IEC/IEEE 14764:2022.** Desarrolla el proceso de mantenimiento de la ISO/IEC/IEEE 12207 y define los tipos de mantenimiento; este documento usa los tipos correctivo (corregir defectos descubiertos), preventivo (corregir defectos latentes antes de que se manifiesten), adaptativo (ajustar el software a cambios del entorno) y perfectivo (mejorar el software) (ISO et al., 2022).
- **ISO 9001:2015.** La cláusula 10.2 exige reaccionar ante una no conformidad, determinar sus causas, implementar la acción necesaria, revisar su eficacia y conservar información documentada; la cláusula 10.3 exige la mejora continua. La norma reemplazó la "acción preventiva" como requisito separado por el pensamiento basado en riesgos (cláusula 6.1), por lo que en este documento las acciones preventivas nacen del registro de riesgos (ISO, 2015).
- **Ciclo PHVA.** Cada acción recorre el ciclo planear, hacer, verificar y actuar, que es la base de la mejora continua de la ISO 9001.

### 4.2 Fuentes de Este Documento

Las acciones se verificaron contra el código de los once repositorios (rama *develop* al 24 de octubre de 2026, que es la que corre en producción desde ese día, y la versión *v1.0.0* del 23 de octubre) y su historial de Git, contra la revisión del sistema del 23 de octubre de 2026 (seguridad, negocio, operaciones, despliegue, web, móvil y documentación), contra la auditoría de cierre y la revisión cruzada final del 24 de octubre de 2026 y contra la evaluación extremo a extremo del instructor del 21 de octubre de 2026 (Anexo D), y contra el registro de riesgos, el backlog técnico, la gestión de incidentes, la estrategia de pruebas y los registros de decisiones de arquitectura (ADR) del proyecto (Equipo de Desarrollo Vehicle Washing, 2026a). También se incluyen los hallazgos de la revisión de los manuales del 7, del 9 y del 16 de octubre de 2026 y de la revisión completa del sistema del 16 de octubre (anexo B del Manual Técnico y anexos de los demás manuales).

## 5. Objetivo

### 5.1 Objetivo General

Documentar el procedimiento y el registro de las acciones correctivas, preventivas y de mejoramiento del Sistema de Gestión de Lavado Vehicular, para asegurar que cada defecto, riesgo u oportunidad de mejora se trate de forma trazable, verificable y con aprendizaje para el equipo.

### 5.2 Objetivos Específicos

- Definir los tipos de acción, su clasificación por severidad y prioridad y su ciclo de estados.
- Establecer un procedimiento único para identificar, registrar, analizar, ejecutar, verificar y cerrar acciones.
- Registrar las acciones correctivas ya ejecutadas con su causa raíz y su evidencia.
- Registrar las acciones correctivas, preventivas y de mejoramiento pendientes con su responsable, prioridad y criterio de cierre.
- Definir indicadores para medir la eficacia del procedimiento y consolidar las lecciones aprendidas.

## 6. Alcance

**Cubre:** los seis servicios de backend, el gateway, la base de datos, la infraestructura de Docker, la aplicación web, la aplicación móvil y la documentación del proyecto (manuales, ADR, contratos y modelos de datos), desde la construcción hasta la operación posterior a la implementación.

**No cubre:** la gestión de los requisitos nuevos del producto (historias de usuario del backlog de producto), salvo cuando una necesidad de mejora del software ya construido se registra como acción de mejoramiento; tampoco cubre la gestión académica del proyecto (notas, entregas del programa).

## 7. Definiciones y Clasificación

### 7.1 Tipos de Acción

**Tabla 2**

*Tipos de Acción y su Relación con los Tipos de Mantenimiento*

| Tipo | Definición | Disparador | Tipo de mantenimiento (ISO/IEC/IEEE 14764) | Prefijo |
|---|---|---|---|---|
| Acción correctiva | Elimina la causa de una no conformidad **ya ocurrida** (defecto, incumplimiento de un requisito, de un ADR o de la documentación) para que no se repita. | Incidente, prueba fallida, revisión, observación de la instructora o de la aceptación. | Correctivo | AC (cerradas) y ACO (abiertas) |
| Acción preventiva | Elimina la causa de una no conformidad **potencial** antes de que ocurra. | Riesgo del registro de riesgos, deuda técnica, análisis de tendencias. | Preventivo y adaptativo | AP |
| Acción de mejoramiento | Aumenta el valor, la calidad o la eficiencia del software sin que exista una falla. | Retrospectiva, sugerencia de usuarios, evolución recomendada de la arquitectura. | Perfectivo y adaptativo | AM |

*Nota.* Elaborado por los autores a partir de ISO (2015) e ISO et al. (2022).

Una **corrección** (por ejemplo, reiniciar un servicio o editar un dato) no es una acción correctiva: resuelve el síntoma, pero no la causa. En la gestión de incidentes del proyecto la corrección corresponde al paso "mitigar"; la acción correctiva es el cambio de código, configuración o proceso que impide que el incidente se repita.

### 7.2 Severidad y Prioridad

La severidad usa la misma escala de la gestión de incidentes del proyecto (Tabla 3). La prioridad combina la severidad con la probabilidad de ocurrencia y el esfuerzo, y se expresa como Alta, Media o Baja, igual que en el backlog técnico.

**Tabla 3**

*Escala de Severidad*

| Nivel | Definición | Ejemplo | Plazo de respuesta |
|---|---|---|---|
| P0 | Nada funciona. | SQL Server caído; ningún servicio arranca; imposible iniciar sesión. | Inmediato. |
| P1 | Un flujo principal está roto. | No se puede reservar, asignar, iniciar un servicio, reportar o aprobar un pago. | El mismo día. |
| P2 | Una función secundaria falla y existe alternativa. | Los correos no llegan, pero la bandeja interna sí funciona. | Dentro del sprint. |
| P3 | Cosmético o documental. | Texto sin traducir, ícono incorrecto, manual desactualizado. | Backlog. |

*Nota.* Tomado de la gestión de incidentes del proyecto (Equipo de Desarrollo Vehicle Washing, 2026a).

### 7.3 Estados de una Acción

Cada acción pasa por los estados **Abierta** (registrada), **En análisis** (se busca la causa raíz), **Planificada** (tiene responsable, solución y fecha), **En ejecución** (rama y pull request en curso), **En verificación** (desplegada; se comprueba su eficacia) y **Cerrada** (eficaz y con evidencia). Una acción cuya verificación falla vuelve a *En análisis*. Una acción que el equipo decide no ejecutar se marca **Aceptada como riesgo**, con la justificación y la aprobación del líder de proyecto.

## 8. Procedimiento de Gestión de Acciones

### 8.1 Fuentes de Identificación

- Incidentes de la gestión de incidentes del proyecto y sus *post-mortems* (P0 y P1).
- Pruebas automáticas fallidas (*./mvnw verify*, *dotnet test*, *ng build*) y pruebas manuales de punta a punta antes de cada revisión.
- Revisiones de código (pull requests) y revisiones de la instructora.
- Pruebas de humo y de aceptación del Procedimiento de Implementación.
- Contraste entre el código, los ADR y los manuales (anexos de discrepancias).
- Registro de riesgos y backlog técnico (fuente de acciones preventivas).
- Retrospectivas de cada sprint y comentarios de las personas usuarias (fuente de acciones de mejoramiento).

### 8.2 Etapas del Procedimiento

**Tabla 4**

*Etapas del Procedimiento de Gestión de Acciones*

| Etapa (PHVA) | Actividad | Responsable | Salida |
|---|---|---|---|
| 1. Identificar (Planear) | Detectar la no conformidad, el riesgo o la oportunidad y comunicarla en el chat del equipo. | Quien la detecta | Aviso con descripción y evidencia. |
| 2. Registrar (Planear) | Crear la fila en el registro correspondiente (secciones 9 a 12) con el formato del Anexo A; asignar ID, tipo, severidad y responsable. | Líder del área afectada | Acción en estado Abierta. |
| 3. Contener (Hacer) | Solo en correctivas P0 y P1: aplicar la corrección inmediata (reiniciar, ajustar *.env*, levantar el contenedor faltante). | Responsable del servicio | Flujo restablecido. |
| 4. Analizar la causa (Planear) | Aplicar los cinco porqués o el diagrama de causa y efecto (sección 8.3). | Responsable del servicio | Causa raíz documentada. |
| 5. Planificar (Planear) | Definir la solución, el criterio de cierre, el esfuerzo y la fecha; si cambia una decisión técnica, crear o modificar un ADR. | Responsable con el líder de proyecto | Acción en estado Planificada. |
| 6. Ejecutar (Hacer) | Rama *fix/\** o *feat/\**, cambio con pruebas que reproduzcan el defecto, pull request revisado por otra persona y documentación actualizada en el mismo pull request (definición de terminado). | Responsable | Pull request integrado en *develop*. |
| 7. Verificar la eficacia (Verificar) | Comprobar el criterio de cierre en el entorno implementado y que el problema no reaparece (sección 8.4). | Persona distinta del ejecutor | Evidencia de verificación. |
| 8. Cerrar y aprender (Actuar) | Marcar Cerrada, registrar la lección aprendida (sección 14) y actualizar el registro de riesgos y el backlog técnico. | Líder de proyecto | Acción en estado Cerrada. |

*Nota.* Elaborado por los autores a partir de la cláusula 10.2 de ISO (2015) y de la definición de terminado del proyecto.

### 8.3 Análisis de Causa Raíz

Para las acciones correctivas de severidad P0, P1 y P2 es obligatorio documentar la causa raíz. Se usan dos técnicas:

- **Cinco porqués:** se pregunta "¿por qué ocurrió?" sobre cada respuesta hasta llegar a una causa sobre la que el equipo puede actuar (proceso, diseño, configuración o conocimiento). El Anexo B contiene un ejemplo real del proyecto.
- **Diagrama de causa y efecto (Ishikawa):** cuando hay varias causas posibles, se clasifican en las categorías *código*, *configuración*, *datos*, *infraestructura*, *proceso* y *personas* antes de elegir la causa principal.

La causa raíz **nunca** se registra como "error humano": si una persona se equivocó, la pregunta es qué en el proceso, la herramienta o la documentación permitió el error (enfoque sin culpables de los *post-mortems* del proyecto).

### 8.4 Verificación de la Eficacia

Una acción solo se cierra cuando se cumple su criterio de cierre y existe evidencia verificable: commit o pull request, prueba automática que reproduce el defecto y ahora pasa, resultado de una prueba manual, consulta a la base de datos o captura. Para las correctivas P0 y P1 se exige además que el problema no reaparezca en la siguiente revisión o implementación. La verificación la hace una persona distinta de quien ejecutó la acción.

### 8.5 Roles

**Tabla 5**

*Roles en la Gestión de Acciones*

| Rol | Persona | Responsabilidad |
|---|---|---|
| Líder de proyecto | Brayan Estiven Patiño Cabrera | Aprueba prioridades, acepta riesgos, verifica el cierre y mantiene este documento. |
| Líder de backend | Merari Urbano Quintero | Responsable de las acciones de servicios, base de datos e infraestructura. |
| Líder de frontend | Juan Camilo Guilombo Osorio | Responsable de las acciones de las aplicaciones web y móvil. |
| Autor de cada pull request | Cualquier integrante | Actualiza la documentación afectada en el mismo pull request. |
| Instructora | Karol Daniela Correa | Fuente de observaciones; revisa el documento en las entregas. |

*Nota.* Elaborado por los autores. Cada servicio tiene además un dueño definido en el catálogo de servicios del proyecto.

## 9. Registro de Acciones Correctivas Cerradas

La Tabla 6 registra las no conformidades ya corregidas, con su causa raíz y la evidencia de cierre. La columna "Origen" remite al riesgo cerrado (C-), a la deuda técnica (TD-) o al ADR correspondiente.

**Tabla 6**

*Acciones Correctivas Cerradas*

| ID | No conformidad | Sev. | Causa raíz | Acción ejecutada | Evidencia de cierre | Origen | Cierre |
|---|---|---|---|---|---|---|---|
| AC-01 | Las horas de las reservas aparecían corridas cinco horas. | P1 | La JVM convertía las fechas con la zona del equipo y no con la del negocio. | La JVM de booking-service corre en UTC; las fechas se guardan en UTC y se convierten solo en los bordes con *BOOKING_ZONE=America/Bogota*. | Pruebas de *AvailabilityPolicyTest* y reservas correctas en la prueba de punta a punta. | C-001 | 2026-10-04 |
| AC-02 | Los servicios fallaban al arrancar después de *docker compose up*. | P0 | Los servicios intentaban conectarse antes de que SQL Server estuviera sano. | Chequeos de salud en *sqlserver* y *rabbitmq* y dependencias *service_healthy* y *service_completed_successfully* en el archivo de Compose. | *docker-compose.yml* con *depends_on* condicionados. | C-002 | 2026-10-04 |
| AC-03 | Existían dos implementaciones distintas del servicio de reservas. | P1 | Se empezó a programar sin un modelo de datos acordado. | Se reconstruyó booking-service a partir del modelo SQL acordado; regla del equipo: acordar el modelo antes de programar un servicio. | Un solo repositorio *lavarapido-booking-service* alineado con *06-data/models.md*. | C-003 | 2026-10-04 |
| AC-04 | Con *--profile app* todas las rutas de pagos fallaban. | P1 | payment-service no tenía Dockerfile ni servicio en Compose, y el contenedor del gateway no recibía *ROUTE_PAYMENT_URL*. | Dockerfile multietapa de .NET 8, servicio *payment-service* en Compose y *ROUTE_PAYMENT_URL* en el gateway. | Commit de *lavarapido-infra* del 2026-10-08 ("wire payment-service to booking-service and rabbitmq"). | TD-014 | 2026-10-08 |
| AC-05 | Faltaba *BOOKING_SERVICE_URL* para payment-service en Compose; la acreditación de puntos fallaba en contenedores. | P1 | La variable se omitió al crear el servicio (vacío previo descubierto en ADR-015). | Variable agregada con *http://booking-service:3003/api/v1*. | *docker-compose.yml* vigente. | ADR-015 | 2026-10-08 |
| AC-06 | El cliente no recibía aviso cuando su pago se aprobaba, rechazaba o reembolsaba. | P2 | payment-service levantaba eventos de dominio, pero no tenía publicador de RabbitMQ. | Publicador con *RabbitMQ.Client* y eventos *payment.confirmed*, *payment.rejected* y *payment.refunded*, con correo al cliente. | ADR-011 sección 9; tipos *PAYMENT_\** en notification-service (migración 028). | TD-015 | 2026-10-08 |
| AC-07 | Una misma referencia de transacción podía pagar dos reservas. | P1 | No se validaba la unicidad de la referencia al reportar un pago (control de ADR-008 y RF-016). | Validación en payment-service con el código de error *TRANSACTION_REFERENCE_REUSED*. | *ErrorCodes.cs* de payment-service. | TD-016 | 2026-10-08 |
| AC-08 | payment-service solo probaba su dominio; las reglas de rol y de conflicto (409) no estaban protegidas. | P2 | Las pruebas de aplicación y de API se aplazaron al construir el servicio. | Proyectos *PaymentService.Application.UnitTests* e *Infrastructure.IntegrationTests*. | Carpeta *tests/* con pruebas de comandos, consultas, promociones y persistencia. | TD-018 | 2026-10-08 |
| AC-09 | Los puntos ganados por una reserva no se revertían al reembolsar su pago, y dos movimientos simultáneos podían dejar un saldo incorrecto. | P1 | El ledger de puntos no tenía movimiento de reversión ni control de concurrencia. | Tipo de movimiento *REVERSED* y columna *sequence_no* única por cliente (migración *pay-020*); el segundo movimiento concurrente responde 409. | Migración *020-loyalty-reversal-and-sequence.sql*. | ADR-015 | 2026-10-08 |
| AC-10 | La aprobación de pagos comparaba el total de la reserva consigo mismo y aceptaba imágenes no válidas. | P1 | El monto declarado por el cliente no se enviaba ni se comparaba. | Envío del monto pagado, comparación con el monto adeudado en la ventana de revisión y validación de imágenes PNG o JPG. | Commits de payment-service y de la web del 2026-10-08. | Manuales v3.0 | 2026-10-08 |
| AC-11 | Existía un catálogo de "roles personalizados" sin efecto en la autorización, que contradecía ADR-010. | P2 | Se construyó una función sin contrastarla con las decisiones registradas. | Se eliminó *security.custom_role* (migración 026); los tres roles quedan fijos y se edita la matriz de permisos de *security.role_permission*. | ADR-015 sección 2; endpoint */api/v1/admin/roles-permissions*. | ADR-015 | 2026-10-08 |
| AC-12 | Se había construido una asignación fija de operario por bahía, contraria a ADR-010. | P2 | Igual que AC-11. | Se revirtió la columna *service_bay.operator_id* (migración 024); la bahía se elige por reserva y el operario por ejecución. | ADR-015 sección 1. | ADR-015 | 2026-10-08 |
| AC-13 | Las promociones mostraban un precio de paquete que nunca se aplicaba al cobro. | P2 | El modelo de promociones se usaba solo para presentación. | Cupones con descuento porcentual real, canjeables con puntos (*POST /api/v1/loyalty/redeem*), y formulario sin precio, duración ni estado. | ADR-015 secciones 3 y 5. | ADR-015 | 2026-10-09 |
| AC-14 | Al editar un servicio, sus puntos de fidelización volvían a 0. | P2 | booking-service enlaza el campo a un *int* primitivo y los clientes omitían el campo. | La web y la app envían siempre *loyaltyPoints*; la app conserva el precio de cada tipo de vehículo al editar. | ADR-015 sección 5; commits de la app móvil del 2026-10-09. | ADR-015 | 2026-10-09 |
| AC-15 | Se ofrecía "Pagar" en reservas ya pagadas o en revisión. | P2 | Cada pantalla repetía la regla de pago y no consultaba a payment-service. | Endpoint *GET /api/v1/payments/me/bookings* con el indicador *payable*; la web y la app solo muestran el botón con ese indicador. | ADR-015 sección 5. | ADR-015 | 2026-10-09 |
| AC-16 | Los interruptores de notificaciones se guardaban solo en el navegador o el teléfono y no tenían efecto. | P2 | No existían columnas para esas preferencias en la cuenta. | Columnas en *security.user_preference* (migración 030), actualización parcial de preferencias y aplicación de los interruptores en notification-service. | ADR-011 sección 12. | ADR-011 | 2026-10-09 |
| AC-17 | El tema y el idioma se guardaban por navegador y una segunda cuenta en el mismo equipo los heredaba. | P2 | Las preferencias se leían de *localStorage* sin consultar la cuenta. | Carga después del inicio de sesión y guardado con *GET/PUT /api/v1/users/me/preferences*; paletas aceptadas por security-service (migración 020). | *core/services/preferences.ts* de la web. | TD-002 | 2026-10-09 |
| AC-18 | El administrador podía enviar a mano cualquier tipo de notificación (por ejemplo, un falso "pago aprobado"). | P1 | El endpoint manual no restringía el tipo. | Solo se aceptan *SYSTEM_MESSAGE* e *INSPECTION_REPORT*; cualquier otro responde 400 *NOTIFICATION_TYPE_NOT_ALLOWED*. | ADR-011 sección 11. | Revisión de seguridad | 2026-10-09 |
| AC-19 | Al actualizar los permisos de un rol se aceptaban identificadores nulos o inexistentes. | P2 | Faltaba validación de entrada en el caso de uso. | Rechazo con 400 *UNKNOWN_PERMISSION* y mensaje traducido en la web. | Commit de security-service del 2026-10-08. | ADR-015 | 2026-10-08 |
| AC-20 | El informe de inspección se compartía por WhatsApp, fuera de los canales del sistema. | P3 | Decisión de producto anterior. | Botón "Notificar al cliente" que envía el resumen por notificación y correo (*INSPECTION_REPORT*); se retiró WhatsApp de los datos del negocio y del centro de ayuda. | ADR-011 sección 11; commits de la web del 2026-10-09. | ADR-011 | 2026-10-09 |
| AC-21 | La app abierta en el navegador con Expo (puerto 8081) era rechazada por CORS. | P2 | El origen 8081 no estaba en los valores por defecto. | *CORS_ALLOWED_ORIGINS* por defecto incluye *http://localhost:8081* en todos los servicios. | Commits de *lavarapido-infra* y del gateway. | — | 2026-10-09 |
| AC-22 | La ruta antigua de tareas del operario mostraba una página vacía. | P3 | Ruta sin componente después de una reorganización. | Redirección a "Servicios asignados" y eliminación de la página vacía. | Commit de la web del 2026-10-09. | — | 2026-10-09 |
| AC-23 | Los manuales v3.0 quedaron desactualizados por los cambios del 8 y 9 de octubre (puntos, cupones, reembolsos, roles, preferencias, pagos en Docker), solo existían en inglés y el Manual de Instalación indicaba clonar el repositorio de operaciones con un nombre que rompe la construcción. | P3 | La documentación no se actualizó en los mismos pull requests (riesgo R-010). | Manuales actualizados a la versión 3.1 en español y en inglés, y creación del Procedimiento de Implementación y de este documento. | Control de versiones de cada manual (2026-10-09). | R-010 | 2026-10-09 |
| AC-24 | La suite de pruebas web no compilaba (antes ACO-01). | P2 | Seis archivos *.spec.ts* de andamiaje apuntaban a clases que ya no existían. | Se corrigieron los archivos de prueba y se agregaron pruebas de los mensajes de error de la API. | *npx ng test --watch=false*: 43 pruebas en 37 archivos aprobadas (2026-10-16). | TD-001 | 2026-10-16 |
| AC-25 | No existía límite de intentos de inicio de sesión (antes ACO-02). | P1 | RF-004b no se había implementado. | *LoginLockout*: cinco intentos fallidos en 15 minutos bloquean la cuenta durante 15 minutos (423 ACCOUNT_LOCKED); un ingreso correcto o el restablecimiento de la contraseña lo limpian. | Changelog 034, *AuthenticationServiceTest* y *ApiSecurityWebTest*; mensaje visible en la web y el móvil. | TD-003, RF-004b | 2026-10-16 |
| AC-26 | La aceptación de los Términos y de la Política de Tratamiento de Datos no se guardaba en el servidor (antes ACO-03). | P1 | La tabla de consentimiento quedó pendiente en ADR-010. | *security.app_user* guarda la fecha y la hora de aceptación de cada documento (*terms_accepted_at*, *data_policy_accepted_at*); el registro web y móvil envía *acceptsTerms* y *acceptsDataPolicy*. | Changelog 039, 147 pruebas de security-service aprobadas, casillas en ambos clientes. | Ley 1581 de 2012 | 2026-10-16 |
| AC-27 | La salud de payment-service respondía *UP* con la base caída y el servicio no manejaba *X-Correlation-Id* (antes ACO-05). | P2 | El endpoint devolvía un valor fijo. | */health* y */actuator/health* consultan SQL Server con un límite de 3 segundos; *CorrelationIdMiddleware* toma o crea el identificador y lo reenvía a booking-service. | Con la base detenida el servicio responde 503 DOWN; 141 pruebas de payment-service aprobadas. | TD-017 | 2026-10-16 |
| AC-28 | Los textos del reembolso estaban fijos en español (antes ACO-06). | P3 | Se dejaron en un archivo temporal de constantes. | Textos movidos a los archivos de idioma en español, inglés, francés y portugués; se borró *payment-texts.ts*. | Pantalla de pagos del administrador en los cuatro idiomas. | Manual de Administrador | 2026-10-16 |
| AC-29 | El botón "Reportar avance" del operario decía que avisaba al cliente, pero no enviaba nada (antes ACO-07). | P3 | La función quedó solo con la confirmación visual. | Se retiró el botón en la web y en el móvil; el cliente recibe los avisos reales de inicio y fin del servicio. | Pantalla de servicios asignados sin el botón. | Manual de Usuario | 2026-10-16 |
| AC-30 | La página de bienvenida anunciaba un servicio "donde estés" y el historial del operario podía mostrar medios de pago que el sistema no admite (antes ACO-08). | P3 | Textos de maqueta. | La página de inicio describe el servicio en el lavadero con los datos reales del establecimiento; el historial del operario ya no muestra tarjeta ni PSE. | Página de inicio en los cuatro idiomas. | Manual Técnico, anexo B | 2026-10-16 |
| AC-31 | Una dirección desconocida mostraba una página vacía (antes ACO-09). | P3 | Faltaba la ruta comodín. | Página 404 (*features/not-found*, ruta *\*\**) con enlace al inicio. | Una dirección inexistente muestra la página 404. | TD-009 | 2026-10-16 |
| AC-32 | El informe de inspección y sus fotos se guardaban solo en el navegador (antes ACO-10). | P2 | No existía un agregado en el backend. | Agregado *InspectionReport* en operations-service con fases, hallazgos y fotos (changelog 033); el enlace público abre en cualquier equipo y el aviso al cliente lo incluye. | Prueba *inspection-flow.test.js* de *lavarapido-infra/e2e* aprobada. | RF-036 | 2026-10-16 |
| AC-33 | Los datos del negocio de Configuración se guardaban solo en el navegador (antes ACO-11). | P3 | La extensión de *booking.establishment* esperaba la definición de sus campos. | Changelog 032 y *GET/PUT /api/v1/admin/establishment*; la página de inicio y el centro de ayuda leen esos datos. | Los cambios se ven desde otra cuenta y en la página pública. | TD-013, RF-037 | 2026-10-16 |
| AC-34 | El README de *lavarapido-infra* y la guía del equipo describían como pendientes servicios y pantallas que ya existían (antes ACO-12). | P3 | Documentos no actualizados al integrar los servicios. | Ambos documentos actualizados. | Commits del 2026-10-16 en *lavarapido-infra*. | R-010 | 2026-10-16 |
| AC-35 | Dos solicitudes simultáneas podían intentar crear el mismo operario y una fallaba con error 500. | P2 | El alta del operario leía y luego insertaba sin manejar la carrera entre solicitudes. | La inserción corre en su propia transacción y, si la fila ya existe, se vuelve a leer en lugar de fallar. | *OperatorPersistenceAdapterTest*; 54 pruebas de operations-service aprobadas. | Revisión del 2026-10-16 | 2026-10-16 |
| AC-36 | Al pedir una hora fuera del horario o ya pasada, el sistema respondía "Esa hora ya se ocupó". | P3 | La respuesta 409 no distinguía el motivo. | La respuesta trae *reason* (CLOSED_DAY, OUTSIDE_HOURS, TIME_PASSED o NO_FREE_BAY) y la web y el móvil muestran un mensaje para cada caso. | Prueba *access-rules.test.js* (*reason: OUTSIDE_HOURS*) y *api-error.spec.ts*. | Revisión del 2026-10-16 | 2026-10-16 |
| AC-37 | La opción "Eliminar cuenta" decía que borraba la cuenta y todos los datos, pero solo la desactiva. | P2 | Texto heredado de la maqueta, contrario a lo que hace el servidor. | La opción se llama "Desactivar mi cuenta" y explica que el historial se conserva, en la web y el móvil y en los cuatro idiomas. | Perfil de la web y del móvil; Manual de Usuario 4.0. | Ley 1581 de 2012 | 2026-10-16 |
| AC-38 | Los manuales y documentos del proyecto quedaron desactualizados respecto del código del 16 de octubre (bloqueo, asignación automática, inspección, 57 tablas) y las figuras eran marcadores. | P3 | Cambios integrados sin actualizar la documentación en el mismo pull request (R-010). | Manuales 4.0 con capturas reales, Procedimiento de Implementación 2.0, este documento 2.0 y la documentación técnica del proyecto revisada contra el código. | Control de versiones de cada documento (2026-10-16). | R-010 | 2026-10-16 |
| AC-39 | Con dos cuentas del mismo medio (por ejemplo, dos Nequi), la pantalla de pagos de la web las marcaba a la vez y siempre mostraba el QR de la primera. | P3 | La opción elegida se guardaba por tipo de medio y no por cuenta. | La selección se guarda por cuenta y, si dos cuentas comparten medio, cada opción muestra su titular. | *payment.spec.ts* (dos cuentas Nequi); 43 pruebas web aprobadas. | Documentación de Pruebas, defecto 6 | 2026-10-16 |
| AC-40 | Al cerrar sesión, la web y la app mostraban "Tu sesión expiró". | P2 | Una respuesta 401 que llegaba después de borrar el token se tomaba como sesión vencida. | El 401 solo cierra la sesión con aviso si todavía hay una sesión abierta (*auth-interceptor* en la web y *AuthServiceContext* en la app). | Al cerrar sesión ya no aparece el aviso; cuando el token vence de verdad, sí. | Reporte de uso | 2026-10-16 |
| AC-41 | En el celular (360 px) había botones fuera de la pantalla, títulos encimados, el botón ☰ sobre los títulos y filtros fuera de su tarjeta. | P2 | Estilos pensados para escritorio, sin reglas para celular, e interlineado fijo de 20 px heredado del tema de Material. | Reglas de celular en la landing, autenticación, panel del cliente, servicios asignados, gestión y configuración; espacio global para el botón ☰ e interlineado relativo en los títulos. | 60 pantallas web (360 y 768 px) y 26 de la app sin desbordes (pruebas, Tabla 5b). | Revisión de diseño adaptable | 2026-10-17 |
| AC-42 | En la app, los campos de fecha y búsqueda se aplastaban cuando iban en columna. | P3 | La caja usaba *flex: 1*, que parte de alto 0 en una columna. | Crece solo a lo ancho y conserva un alto mínimo de 46 px (*FilterInputs.tsx*). | Historial y Calificaciones muestran los campos completos. | Revisión de diseño adaptable | 2026-10-17 |
| AC-43 | El avance del próximo servicio mostraba 10 % o 25 % sin que el lavado hubiera empezado. | P3 | La tabla de avance daba un porcentaje a los estados Programada y Confirmada. | Programada y Confirmada quedan en 0 % en la web y en la app. | El panel muestra 0 % hasta que el operario inicia el servicio. | Reporte de uso | 2026-10-16 |
| AC-44 | Un token seguía sirviendo hasta una hora después de cerrar sesión o de que el administrador inhabilitara la cuenta. | P2 | Cada servicio solo verificaba la firma y la vigencia del JWT (ADR-006); nadie preguntaba si la sesión seguía abierta. | security-service rechaza el token de una sesión cerrada (claim *sid*, *ActiveSessionValidator*) y el gateway lo confirma con *GET /api/v1/auth/session* antes de pasar a los demás servicios, con la respuesta guardada 30 segundos por token. | *SessionCheckFilterTest*, *ApiSecurityWebTest* (401 con el token de una sesión cerrada); 19 pruebas de punta a punta con el chequeo activo. | TD-024 | 2026-10-21 |
| AC-45 | Las reservas que nadie iniciaba quedaban Programadas o Confirmadas para siempre, y cada pantalla del administrador contaba "sin operario" a su manera. | P2 | No había un cierre para las reservas vencidas y la regla de "sin operario" estaba repetida en las pantallas. | *ExpiredBookingsJob* cancela cada 5 minutos, con el motivo *EXPIRED*, la reserva Programada que pasó una hora de su fin sin confirmarse; las Confirmadas se tratan como indica AC-53. *GET /api/v1/admin/assignments/unassigned* es el único conteo de reservas sin operario, en la web y en la app móvil. | *BookingTest*, *BookingServiceTest*, *OperationsServiceTest*, *reservations-store.spec.ts*. | TD-025 | 2026-10-21 |
| AC-46 | Una asignación manual y una automática al mismo tiempo podían darle a un operario dos servicios a la misma hora. | P2 | Las dos asignaciones leían la agenda del operario y guardaban sin coordinarse. | Las dos toman el mismo candado de SQL Server (*sp_getapplock* sobre *operations-assignment*) antes de leer y guardar. | *OperationsServiceTest* comprueba que el candado se toma antes de guardar. | TD-026 | 2026-10-21 |
| AC-47 | El servidor aceptaba comprobantes de pago de cualquier tamaño, y una foto del celular de varios MB se guardaba tal cual en la base. | P3 | El límite de tamaño no estaba en el dominio de pagos. | payment-service rechaza un comprobante de más de 2 MB (*RECEIPT_TOO_LARGE*) y la web y la app lo comprimen antes de enviarlo. | *PaymentTests* (más de 2 MB y 2 MB exactos). | Revisión del 2026-10-21 | 2026-10-21 |
| AC-48 | El cliente veía la opción Efectivo aunque el administrador hubiera desactivado esa cuenta. | P3 | *GET /api/v1/payment-accounts* no devolvía la cuenta de efectivo y la web y la app la agregaban siempre por su cuenta. | La API devuelve la cuenta de efectivo mientras está activa y los clientes muestran Efectivo solo si viene en la lista. | Pantalla de pagos con la cuenta de efectivo activa y desactivada. | Diseño del Software, hallazgo 8 | 2026-10-21 |
| AC-49 | Las tarjetas y gráficas de Reportes contaban como servicios realizados reservas que no se habían terminado. | P3 | El conteo tomaba las reservas por fecha sin mirar su estado. | Solo cuentan las reservas completadas. | *reports.spec.ts*. | Revisión del 2026-10-21 | 2026-10-21 |
| AC-50 | La pestaña de roles prometía que los permisos definen lo que cada usuario puede hacer. | P3 | La autorización real usa el rol del token y la matriz de permisos es de referencia (ADR-015), pero el texto no lo decía. | La pestaña y el diálogo explican que los roles son fijos y que el acceso lo decide el rol. | *management.spec.ts* (roles fijos). | ADR-015 | 2026-10-21 |
| AC-51 | Al cerrar sesión o entrar con otra cuenta en una pestaña, las otras pestañas del navegador seguían mostrando la sesión anterior. | P3 | Cada pestaña leía la sesión solo al abrirse. | Las pestañas escuchan el evento *storage*: si otra cierra sesión vuelven al inicio de sesión y si entra otra cuenta se recargan. | *auth.spec.ts* (pestañas). | Revisión del 2026-10-21 | 2026-10-21 |
| AC-52 | El registro de transacciones de la base del servidor crecía sin límite. | P2 | La base estaba en modelo de recuperación completo sin respaldos del registro de transacciones. | La base pasa al modelo de recuperación simple y se respalda completa todos los días (AP-04). | *RECOVERY SIMPLE* en el servidor; *lavarapido-infra*, commit del 2026-10-21. | R-008 | 2026-10-21 |
| AC-53 | La primera versión de AC-45 dejaba en No asistió toda reserva Confirmada que nadie inició una hora después de su fin. Si el operario lavó el carro y olvidó darle "Iniciar", el historial, los reportes y las métricas registraban que el cliente no vino, y No asistió es definitivo. | P1 | La regla decidía sola un estado definitivo sin saber si el servicio se hizo: el sistema no puede distinguir una inasistencia de un registro olvidado cuando la reserva tiene operario. | El sistema solo decide cuando está seguro: la Confirmada que nunca tuvo operario pasa a No asistió (operations-service la pide a booking por *POST /internal/v1/bookings/{id}/no-show*, que vuelve a comprobar que venció); la que tiene operario y nadie inició o finalizó pasa a la lista "Por cerrar" del administrador, en la web y en la app móvil, que la cierra como "Se atendió" (sin inventar horas y sin avisar al cliente que recoja el vehículo) o como "No asistió"; el operario y los administradores reciben un solo aviso (migraciones 040 y 041). | *FollowUpPolicyTest*, *FollowUpServiceTest*, *BookingServiceTest*, *BookingApiWebTest*, *reservations-store.spec.ts*; prueba en vivo del 21 de octubre en la web y en la app móvil. | Revisión del equipo del 2026-10-21 | 2026-10-21 |
| AC-54 | Un campo `null` en el payload de un evento mandaba el mensaje completo a la cola de errores (DLQ) de notification-service, y el aviso no se creaba. | P2 | La lectura del sobre del evento fallaba con un valor `null` en lugar de ignorarlo. | El campo `null` se ignora y el resto del mensaje se procesa. | *DomainEventEnvelopeTest*. | R-005 | 2026-10-21 |
| AC-55 | Las variables del 21 de octubre de 2026 (*SESSION_CHECK_\**, *BOOKING_EXPIRY_\** y *FOLLOW_UP_\**) no estaban en *.env.example* ni en el bloque *environment* de *docker-compose.yml*; en Docker solo valían sus valores por defecto (antes ACO-14). | P3 | Se agregaron en el *application.yml* de cada servicio sin actualizar *lavarapido-infra*. | Las nueve variables, incluida *FOLLOW_UP_LOOK_BACK_DAYS*, están comentadas en *.env.example* y *.env.production.example* y el compose las pasa a *api-gateway*, *booking-service* y *operations-service*. | *lavarapido-infra*, commit *70b990c*; *docker compose config* las muestra (verificado el 2026-10-23). | ACO-14 | 2026-10-22 |
| AC-56 | El gateway publicaba */api/v1/operator/bookings/\** y booking-service dejaba que un operario iniciara o terminara cualquier reserva sin revisar que estuviera asignado ni la regla de los 15 minutos, y sin publicar los eventos de la ejecución (E-01). | P1 | La ruta pensada para la llamada interna de operations-service quedó publicada, y booking trataba al personal como administrador. | El gateway deja de publicar la ruta; booking exige la llave interna en las rutas del operario y aplica los 15 minutos; operations-service envía la llave. | Commits *b9107aa* (gateway, prueba de 404), *a73467d* (booking), *144a0c6* (operations) y *f07802a* (infra); CI verde. | Revisión del 2026-10-22 (E-01, OPS-02, SEC-02, BIZ-03) | 2026-10-23 |
| AC-57 | Quitar el rol de administrador en la base no cortaba su token, que seguía sirviendo hasta una hora (E-02). | P2 | La sesión se revisaba por su identificador (*sid*), no por los roles del token. | *ActiveSessionValidator* compara los roles del token con *security.user_role* y, si el token trae un rol retirado, lo rechaza y revoca las sesiones. | security-service, commit *579480e* (*AuthenticationServiceTest*, *ApiSecurityWebTest*). Su efecto lo limitaba ACO-15 (cerrada como AC-73). | Revisión del 2026-10-22 (E-02, SEC-03) | 2026-10-23 |
| AC-58 | El perfil por defecto era *dev* y las contraseñas de las cuentas demo y de SQL Server estaban en el código, la guía del equipo, el compose y el README de payment-service (E-05, H-06, H-07 y H-08). | P2 | Comodidad del entorno local. | security-service ya no fija el perfil *dev*; las contraseñas demo se leen de *DEMO_ADMIN_PASSWORD*, *DEMO_OPERATOR_PASSWORD* y *DEMO_CLIENT_PASSWORD* (vacía: la cuenta no se crea); infra y payment sin contraseñas literales. | Commits *e952fe2* (security), *f0800ef* (infra) y *1c7df54* (payment). La contraseña antigua sigue en el historial y en *main* de payment, una contraseña demo sigue escrita en un ejemplo de *GUIA-EQUIPO.md* y *payment-migrate* recibe la contraseña como argumento: ACO-25. | Evaluación del instructor (H-06 a H-08) | 2026-10-23 |
| AC-59 | Un cupón del 100 % o un descuento fijo mayor o igual al total dejaban la reserva con monto 0, que el pago no admite: la reserva quedaba impagable (E-07). | P1 | El dominio aceptaba el 100 % y no ponía tope al descuento fijo. | El porcentaje va de 1 a 99 y el descuento fijo se limita al total menos 1 peso; la web y la app limitan el formulario a 99 %. | Commits *827746f* (payment), *1175eb0* (web) y *c0cd099* (móvil). BIZ-04 y BIZ-05 se cerraron después en AC-77. | Revisión del 2026-10-22 (E-07, RF-024) | 2026-10-23 |
| AC-60 | "Quitar cupón" solo lo quitaba de la pantalla: el servidor seguía restando el canje (E-08, B-05). | P2 | No existía una forma de anular un canje. | *DELETE /api/v1/loyalty/redemptions/{bookingId}/{promotionId}* (solo el cliente dueño; 409 si ya hay un pago) y la web y la app lo usan. | Commits *6e2df05* (payment), *2158196* (web) y *ce630a5* (móvil). Recuperar el canje al recargar se cerró en AC-75. | Revisión del 2026-10-22 (E-08) | 2026-10-23 |
| AC-61 | Una reserva podía acumular varios cupones (E-09). | P2 | Faltaba la regla y el índice en la base. | Una promoción por reserva y un índice único. | payment-service, commit *6e2df05* y migración *021-promotion-one-per-booking.sql* (con *onFail: CONTINUE*: revisar antes de desplegar que no haya reservas con dos cupones). | Revisión del 2026-10-22 (E-09) | 2026-10-23 |
| AC-62 | La regla de correo @gmail solo estaba en algunos formularios y el servidor aceptaba cualquier dominio (A-01). | P2 | La regla se puso en los clientes y no en el servidor. | security-service valida *ALLOWED_EMAIL_DOMAINS* (400 *EMAIL_DOMAIN_NOT_ALLOWED*) y la web y la app aplican la misma regla en todos los formularios de correo. | Commits *bef15d5* (security), *fed8465* (infra), *9f24ce8* (web) y *ee6e339* (móvil). Salvedad del 23 de octubre, resuelta en AC-84: el servidor no restringe el inicio de sesión, pero la web y la app sí, así que una cuenta ya creada con otro dominio no puede entrar. | Revisión del 2026-10-22 (A-01, SEC-08) | 2026-10-23 |
| AC-63 | La web no permitía reasignar el operario de una reserva (A-02). | P2 | El botón Asignar solo aparecía sin operario. | Asignar también aparece con operario, en la lista y en el detalle. | Web, commit *eb3e16d*. | Revisión del 2026-10-22 (A-02, RF-009) | 2026-10-23 |
| AC-64 | Las notas para el operario al asignar se descartaban (B-01). | P3 | El contrato de la asignación no tenía el campo. | operations-service guarda las notas y la web las envía. | Commits *cf28fbe* (operations, migración 042) y *dbe0a49* (web). Salvedad del 23 de octubre, resuelta en AC-84: el operario todavía no ve las notas. | Revisión del 2026-10-22 (B-01) | 2026-10-23 |
| AC-65 | La asignación manual no revisaba si la cuenta del operario estaba desactivada, y reprogramar conservaba al operario sin revalidarlo (A-13, E-12). | P2 | Solo la asignación automática consultaba la cuenta. | Se valida la cuenta activa al asignar y se revalida al operario al reprogramar; la web y la app muestran el mensaje de cuenta desactivada. | Commits *e94319c* y *c653b4e* (operations), *5f4377c* (web) y *437bd03* (móvil). | Revisión del 2026-10-22 (A-13, E-12) | 2026-10-23 |
| AC-66 | Los cambios de estado que hacía el administrador no avisaban al cliente (A-11). | P3 | booking-service no publicaba evento en ese cambio. | booking publica el evento y notification-service avisa al cliente. | Commits *369bd85* (booking) y *f9c9adf* (notification). Salvedad del 23 de octubre, resuelta en AC-84: ese cambio no cancela los recordatorios. | Revisión del 2026-10-22 (A-11) | 2026-10-23 |
| AC-67 | Los puntos de fidelización de una reserva cambiaban si el administrador cambiaba los del servicio (A-09). | P2 | La línea de la reserva no guardaba los puntos. | Los puntos quedan congelados en cada línea de la reserva. | booking-service, commit *8926287* (migración 043). | Revisión del 2026-10-22 (A-09) | 2026-10-23 |
| AC-68 | El motivo de cancelación que elegía el administrador se ignoraba (A-20, H-24). | P3 | El controlador llamaba a la cancelación sin el motivo. | booking usa el motivo elegido y la web lo pide. | Commits *5c23630* (booking) y *48e60e8* (web). | Evaluación del instructor (H-24) | 2026-10-23 |
| AC-69 | Los ingresos de la web se calculaban por la fecha del reporte y sumaban todos los años, y el estado "En servicio" de los operarios no se calculaba (A-17, A-19). | P3 | Cálculos hechos con el dato equivocado. | Ingresos por fecha de aprobación y estado calculado. | Web, commit *3c02a4d*. | Revisión del 2026-10-22 | 2026-10-23 |
| AC-70 | El historial y los servicios asignados del operario mostraban datos falsos o vacíos en la web (A-18). | P3 | Quedaban datos de prueba en el ViewModel. | Datos reales del servidor. | Web, commit *b20d3a9*. Salvedad del 23 de octubre, resuelta en AC-84: el método de pago y el estado del historial siguen fijos. | Revisión del 2026-10-22 | 2026-10-23 |
| AC-71 | Una placa de más de 6 caracteres se recortaba en lugar de rechazarse (E-16). | P3 | El objeto valor truncaba el texto. | Se rechaza con un error de validación. | customer-service, commit *645fa5c*. Salvedad del 23 de octubre, resuelta en AC-84: una placa larga que ya esté en la base rompe la lectura de ese vehículo. | Revisión del 2026-10-22 (E-16) | 2026-10-23 |
| AC-72 | La documentación quedó desactualizada después de *v1.0.0*: decía que no había etiquetas, clonaba y revertía con etiquetas inexistentes, usaba *COMPRESSION* en el respaldo, no documentaba la app móvil para el usuario ni el MVVM, y este registro no tenía las no conformidades reales (DOC-02 a DOC-16; A-03 a A-08, B-02, B-06, C-01 a C-03). | P3 | La documentación no se revisó después de los cambios del 22 y el 23 de octubre. | Revisión de los documentos 01, 02, 04 a 11, del README y del CHANGELOG, en español e inglés. | *lavarapido-docs*, rama *docs/update-october-23* (incluye las ramas *fix/\** de Merari Urbano). Los PDF y Word quedan por regenerar. | R-010 | 2026-10-23 |
| AC-73 | El gateway no revisaba la sesión si el encabezado decía *bearer* en minúscula: el token de una sesión cerrada seguía sirviendo hasta una hora en los servicios distintos de security (SEC-01, antes ACO-15). | P1 | *SessionCheckFilter* solo revisaba los encabezados que empezaban exactamente por *Bearer *. | Revisa toda petición con *Authorization*, con cualquier esquema y en cualquier ruta; la caché usa solo el token, así que el cierre de sesión la borra en todas sus variantes. | api-gateway, merge *2e20204* (*SessionCheckFilterTest*, 12 casos). En producción desde el 24 de octubre. | Revisión del 2026-10-22 (SEC-01) | 2026-10-24 |
| AC-74 | Una reserva pagada o con el pago en revisión se podía reprogramar con otros servicios y el pago aprobado no correspondía a lo que se hizo (BIZ-01, antes ACO-16). | P1 | booking aceptaba *serviceIds* al reprogramar y payment no recalculaba el monto al aprobar. | El cliente solo cambia la fecha y la hora (409 *BOOKING_SERVICES_LOCKED*); al aprobar, si el monto no coincide con lo que se debe hoy, 409 *AMOUNT_CHANGED*; el administrador ve un aviso al cambiar los servicios de una reserva con pago. | booking *2983d76*, payment *a898a98* y web *19138db*. | Revisión del 2026-10-22 (BIZ-01) | 2026-10-24 |
| AC-75 | El cliente no podía consultar el monto que cobra el servidor ni recuperar el cupón al volver a abrir el pago (BIZ-02, resto de E-08, antes ACO-17). | P2 | El monto solo lo exponía una ruta de administrador. | *GET /api/v1/loyalty/redemptions/{bookingId}*; la web y la app muestran *amountDue*, recuperan el cupón al recargar y "Quitar" usa el DELETE. | payment *fb90b0c*, web *0bdc8c1* y móvil *2ce105f*. | Revisión del 2026-10-22 (BIZ-02) | 2026-10-24 |
| AC-76 | Iniciar o terminar un servicio no era atómico con booking-service y el operario podía quedar bloqueado (OPS-01, antes ACO-18). | P2 | operations llamaba a booking antes de guardar su propio estado. | Guarda primero y, si booking responde 409, relee la reserva y da el paso por hecho; candados de fila en las ejecuciones (OPS-03) y llamadas HTTP fuera del candado de asignación (OPS-11). | operations *6d18916*, *16175fc* y *5e9d15b*. Salvedades en ACO-35 y ACO-36. | Revisión del 2026-10-22 (OPS-01) | 2026-10-24 |
| AC-77 | Quedaban reservas que no se podían pagar: precio 0 en el catálogo y un descuento congelado en pesos que superaba el total al reprogramar (BIZ-04, BIZ-05, antes ACO-20); además, dos cambios a la vez sobre la misma reserva se pisaban (BIZ-09). | P2 | El catálogo admitía precio 0, el canje guardaba el valor y no el porcentaje, y la reserva no tenía bloqueo optimista. | Precio mayor que 0 (400 *INVALID_PRICE*, migración 044); el canje guarda el porcentaje y el tope y el monto se recalcula con el total de hoy (migración 022); *@Version* en la reserva (migración 045). | booking *66a6998* y *348683e*; payment *6a924b4*. | Revisión del 2026-10-22 (BIZ-04, BIZ-05, BIZ-09) | 2026-10-24 |
| AC-78 | Los eventos y los avisos se podían perder sin aviso (OPS-04, OPS-05, OPS-06, antes ACO-21). | P2 | Riesgo aceptado en ADR-004 sin mitigación en la cola de operations ni en los envíos. | operations: 3 reintentos y cola de errores *operations-service.booking-confirmed.dlq*, con confirmación del broker; notification: tabla *pending_delivery* (migración 047) con reintentos de 1, 2, 4… minutos hasta 5 intentos, y reintento si security no responde al pedir los administradores. | operations *1fda373*; notification *511c022* y *a345ac9*. El Outbox sigue como riesgo aceptado (AP-02). | Revisión del 2026-10-22 (OPS-04, OPS-05) | 2026-10-24 |
| AC-79 | No había límite de solicitudes en el inicio de sesión ni en la recuperación, y el tiempo de respuesta revelaba qué cuentas existían (SEC-04, SEC-05, SEC-12, E-04, antes ACO-22). | P2 | No se configuró un límite en el gateway. | 60 peticiones por minuto por IP en */api/v1/auth/\** (429 *RATE_LIMITED* con *Retry-After*), con la IP real del cliente (*TRUSTED_PROXIES*); tiempo parejo con correos que no existen; tope de 5 intentos del código de recuperación y de la contraseña actual (423 *TOO_MANY_ATTEMPTS*). | api-gateway *3cfa9e9* y *5744839*; security *8987e9b*, *6450d2e* y *c2fd4ec*. | Revisión del 2026-10-22 (SEC-04) | 2026-10-24 |
| AC-80 | El servidor no exigía el consentimiento de la Ley 1581 al registrar (SEC-07, E-03, antes ACO-23). | P2 | *RegisterRequest* no validaba las casillas. | Sin las dos casillas, o con una sola, responde 400 *CONSENT_REQUIRED*. | security *c5ecf17*. | Revisión del 2026-10-22 (SEC-07) | 2026-10-24 |
| AC-81 | Dos reportes de pago simultáneos podían dejar dos pagos en revisión y la referencia no era única (A-16, antes ACO-27). | P2 | La regla se revisaba sin candado ni índice. | Migración 023: índice único filtrado (un solo pago abierto por reserva) y vista indexada para la referencia entre pagos vivos; 409 con el código de cada regla. | payment *3e79a00*. Salvedad menor en ACO-40. | Revisión del 2026-10-22 (A-16) | 2026-10-24 |
| AC-82 | Volver atrás en el servidor era manual y global, no se limpiaban las imágenes viejas y restaurar no corría *payment-migrate* (CD-10, CD-11, E-20, antes ACO-29). | P3 | Los scripts se pensaron para actualizar, no para volver atrás. | *update-server.sh* guarda la versión que corre como *:prev* y limpia imágenes y caché tras una actualización sana; *rollback.sh* vuelve a *:prev* sin compilar (pide escribir VOLVER, usa el candado y espera la salud); retención de 10 respaldos *pre-update*; *restore-db.sh* con candado y *payment-migrate*. | lavarapido-infra *166c121*, *bae88a8* y *1150821*. Salvedades en ACO-37. | Revisión del 2026-10-23 (CD-10, CD-11) | 2026-10-24 |
| AC-83 | La matriz de permisos por rol se guardaba pero no se aplicaba al autorizar (H-05, antes ACO-31). | P3 | La autorización usa el rol del token (ADR-015). | La pantalla de permisos queda de solo lectura y se quitó el guardado. | web *a4d3715*. | Evaluación del instructor (H-05) | 2026-10-24 |
| AC-84 | Salvedades de los arreglos del 23 de octubre (antes ACO-32): el operario no veía las notas, un cambio de estado del administrador no cancelaba los recordatorios, las cuentas con otro dominio no podían iniciar sesión, una placa larga guardada rompía la lectura, el historial del operario tenía datos fijos, faltaban textos en francés y portugués, y el perfil *production* de EAS no fijaba la URL (MOV-08). | P3 | Los arreglos se hicieron el mismo día de la revisión. | Notas visibles para el operario; IN_PROGRESS, COMPLETED o NO_SHOW cancelan los recordatorios (tipo *BOOKING_NO_SHOW*, semilla 116); el inicio de sesión acepta cualquier dominio y el registro sigue pidiendo gmail.com; la placa guardada se normaliza sin validarla; el estado del historial sale de los datos; textos completos en los 4 idiomas; URL HTTPS en *production*. | operations *b943858*, web *7ac143d*, móvil *558d848* (B-01); notification *952cd7e* (A-11); web *31594ac*, móvil *eb7621c* y security *0b5ed17* (A-01); customer *eaad74e* (E-16); web *86fea9b* (A-18) y *08d36ab*; móvil *7b5f095* (MOV-08). | Revisión del 2026-10-23 | 2026-10-24 |
| AC-85 | Los scripts nuevos del servidor podían bajarle la versión: al pasar de *develop* a un *main* que aún no tiene *v1.1.0*, *setup-server.sh* retrocedía los repositorios sin error y dejaba una mezcla de versiones (revisión cruzada C1). | P1 | *git checkout main* retrocede sin avisar cuando *main* es ancestro de *develop*. | *sync_repo* se detiene sin tocar nada si lo desplegado no está contenido en la rama pedida (*ALLOW_DOWNGRADE=1* lo permite a propósito). | lavarapido-infra *d5d6909* (merge *cf28ed0*). Antes del despliegue del 24 de octubre. | Revisión cruzada del 2026-10-24 (C1) | 2026-10-24 |
| AC-86 | La imagen de la web corría nginx como *root* (H-19, WEB-15). | P3 | Se usaba la imagen oficial de nginx en el puerto 80. | *nginx-unprivileged* en el puerto 8080, *npm ci --ignore-scripts* y SRI en las banderas; Caddy apunta a *web:8080* y el chequeo de salud también. | web *835395d*; lavarapido-infra *9e56536*. En producción desde el 24 de octubre. | Evaluación del instructor (H-19) | 2026-10-24 |
| AC-87 | Después del despliegue del 24 de octubre (hacia las 15:10) no salía ninguna push ni ningún correo de notification-service (bienvenida, reservas, pagos y recordatorios): el servicio no podía leer la tabla nueva *notification.pending_delivery* («The conversion from varchar to NCHAR is unsupported»). La bandeja de avisos sí funcionaba, y también el correo de recuperación de contraseña, que envía security-service directamente. Quedaron 2 entregas atascadas (1 push y 1 correo de bienvenida). Se detectó en la prueba en vivo después del despliegue. | P1 | La migración 047 creó *channel* VARCHAR(10) y *language* VARCHAR(5), pero Hibernate lee todo el texto como caracteres nacionales (*use_nationalized_character_data: true*) y el controlador de SQL Server no convierte VARCHAR a NCHAR. Las pruebas unitarias corren sin SQL Server y no lo detectaron. | Migración *048-notification-pending-delivery-nvarchar.sql*: *channel* pasa a NVARCHAR(10) NOT NULL y *language* a NVARCHAR(5) NULL, y se quitan y vuelven a crear *uq_pending_delivery_channel* UNIQUE (notification_id, channel) y *ck_pending_delivery_channel* CHECK (channel IN (N'PUSH', N'EMAIL')); precondición MARK_RAN (solo si *channel* sigue en VARCHAR) y *rollback*. Antes se probó en *tempdb* con los datos conservados, y la suite de notification pasó. | notification *612417a* (merge a *develop* *3f99df4*). Se desplegó solo notification-service el 24 de octubre a las 17:12 UTC; las 2 entregas salieron a las 17:13:50 (push) y a las 17:13:52 (correo) y desde entonces no hay errores. | Prueba en vivo después del despliegue del 2026-10-24 | 2026-10-24 |
| AC-88 | *Acción preventiva y de mejora* (mitigación de reservas masivas: modo de pago al reservar, efectivo con límite y vencimiento del pago digital). En la preparación de la sustentación se preguntó qué impide que un bot o una persona haga muchas reservas y bloquee la agenda. Había límite en */auth* (60 peticiones por minuto por IP y bloqueo tras 5 fallos), cupo por bahías y la regla de que un vehículo no tenga reservas cruzadas, pero ningún tope por cliente ni obligación de pagar. El instructor recomendó exigir el pago al reservar. | P2 | La reserva no pedía ningún compromiso de pago: una cuenta podía ocupar bahías con reservas que nunca pensaba pagar, y el pago solo se reportaba después, cuando el cliente quisiera. | Al reservar, el cliente elige *paymentMode* = *DIGITAL* o *CASH* (400 *INVALID_PAYMENT_MODE*; el administrador no está obligado). **Efectivo con límite:** como máximo *BOOKING_CASH_ACTIVE_LIMIT* (1) reservas activas en efectivo hechas por el propio cliente (*booked_by*), contadas con la agenda bloqueada (409 *CASH_BOOKING_LIMIT*); el cupo vuelve cuando la reserva deja de estar activa, no cuando el administrador registra el pago (decisión del equipo). **Vencimiento del pago digital:** *paymentDeadline* = min(creación + *BOOKING_PAYMENT_WINDOW* (30 min), inicio); vencida sin comprobante, *PaymentDeadlineJob* la cancela con el motivo *PAYMENT_TIMEOUT* y libera el horario. payment-service publica *payment.reported* y booking lo consume en *booking.payment-events*. Migración 049 y semilla 117 de booking. La web y la app agregan el paso "¿Cómo vas a pagar?", el plazo, la etiqueta de efectivo y los Términos del 26 de octubre. Las reservas anteriores quedan sin modo y sin reglas; reprogramar no cambia ni el modo ni el plazo. | booking *4c4f3f1*, *e2f339c* y *6af4a91* (merge a *develop* *db156cb*); payment *f9ba11e*, *bd2a721* y *c2fb5a8* (merge *d94cde5*); web *7b59401*, *abcfb7e*, *4932fda*, *3b3dd6f*, *64c9318*, *17af707* y *4a3c77c* (merge *7f8aca5*); móvil *c680d40*, *3c7aac6*, *c7a1c2f*, *50819d6*, *8980d9e* y *f9ef78f* (merge *acf4e6d*); lavarapido-infra *4fa22f3* (merge *0f949e5*). Pruebas: booking 133, payment 247, web 239 y móvil 100. Verificación: prueba en vivo del 26 de octubre en local (400 sin modo, 201 y 409 en efectivo, la digital sin pagar cancelada sola a los ~70 s, la pagada siguió confirmada) y despliegue en producción ese día hacia las 12:02 (hora de Colombia), con una reserva en efectivo (#12) y una digital (#13). La app llega a los celulares por OTA con *v1.1.0*. | Pregunta de la sustentación y recomendación del instructor (RNF-004) | 2026-10-26 |
| AC-89 | En la prueba en vivo del 26 de octubre de 2026 se perdió el evento *payment.reported* y *PaymentDeadlineJob* canceló con *PAYMENT_TIMEOUT* una reserva con pago digital que sí estaba pagada. | P1 | No hay Outbox (AP-02): un evento que no llega a RabbitMQ se pierde, y el job tomaba una decisión definitiva solo con lo que le había llegado por eventos (la misma causa de AC-53). | Antes de cancelar, el job le pregunta a payment-service por *GET /internal/v1/payments/bookings/{bookingId}/reported* (puerto *PaymentStatusDirectory*, adaptador *PaymentServiceDirectory*, 3 s, *X-Internal-Key*): con *reported=true* (pago *IN_REVIEW* o *APPROVED*) marca la reserva y no la cancela; con *false* la cancela como siempre; si payment no responde o da error, no la cancela en esa vuelta y la vuelve a mirar en la siguiente (*PaymentTimeoutOutcome*). payment-service exige ahora *INTERNAL_API_KEY* en */internal/\*\** (401 sin llave o con una errada; el JWT de un usuario no sirve) y el gateway no enruta esa ruta. | booking *1e44d9a* (merge *db156cb*); payment *c2fb5a8* (merge *d94cde5*); *PaymentDeadlineJobTest*, *PaymentServiceDirectoryTest* e *InternalApiKeyTests*. Fallas simuladas en local: (1) payment sin RabbitMQ, la reserva pagada no se canceló porque el job le preguntó a payment; (2) payment caído, no se canceló nada ("not cancelled this round: PAYMENT_SERVICE_UNAVAILABLE") y, al volver payment, la canceló. En producción, la ruta interna respondió 401 sin llave y 200 *reported:true* con llave para una reserva con pago aprobado. | Prueba en vivo del 2026-10-26 | 2026-10-26 |
| AC-90 | El despliegue continuo estaba fusionado pero nunca había corrido solo: faltaban los secretos *AZURE_SSH_KEY*, *DEPLOY_DISPATCH_TOKEN* y *EXPO_TOKEN*, y *main* no tenía *v1.1.0* (antes ACO-19; CD-01, H-15). | P2 | Los secretos de los repositorios de cada dueño solo los puede crear su dueño o un administrador. | Se crearon los secretos y se liberó *v1.1.0* en los 10 repositorios con infra al final; *v1.1.1* de infra quitó payment-service de *DEVELOP_ONLY*. | Fusión del PR «Release v1.1.1» a *main* de *lavarapido-infra*: el flujo **Desplegar en Azure** corrió solo (*automatic=true*) a las 22:19 del 26 de octubre (hora de Colombia) y los pedidos de los demás repositorios siguieron hasta las 22:48, todos en verde (servicios sanos y web y API en 200); *boot-update* está instalado desde el 24 de octubre. En el móvil, el APK *versionCode* 6 y la OTA se publicaron tras comprobar la huella nativa (Documento de Despliegue, sección 1.3.12). | Despliegue del 2026-10-26 | 2026-10-26 |
| AC-91 | booking-service respondía 500 cuando la lista de servicios de una reserva traía un valor nulo o era muy larga (una lista sin tope rompía el `IN` de SQL Server), y también cuando un día de la semana del horario era inválido. | P3 | La validación del cuerpo no revisaba cada elemento de la lista ni le ponía tope. | `serviceIds` con `@NotEmpty`, `@Size(max = 10)` y elementos `@NotNull @Positive` al crear y al reprogramar (máximo 10 servicios por reserva), y validación de cada día de la semana del horario: responden 400 `VALIDATION_ERROR`. | booking *87b25f5* y *54d4125* (merge *8032aef*); `BookingApiWebTest.nullOrTooManyServicesAreValidationErrors` y `anInvalidDayInTheWeekIsAValidationError`, en verde en el CI. Versión *v1.1.1*, desplegada el 27 de octubre. | Revisión del 2026-10-27 | 2026-10-27 |
| AC-92 | La respuesta de una reserva al cliente incluía `bookedBy`, el identificador de la cuenta que la creó, aunque solo lo necesita el personal. | P3 | Las vistas del cliente y del personal usaban la misma conversión de la respuesta. | `BookingResponse.from(view, includeOwner)`: `bookedBy` y el dueño del vehículo solo se llenan en las vistas del administrador y del operario. | booking *6cc2b86* (merge *04cdb94*). Versión *v1.1.1*, desplegada el 27 de octubre. | Revisión del 2026-10-27 | 2026-10-27 |
| AC-93 | Con la hora ocupada, booking ofrecía como alternativas horas que chocaban con otra reserva del mismo vehículo; al elegirlas, la reserva fallaba con 409 `VEHICLE_ALREADY_BOOKED`. | P3 | Las alternativas solo miraban las bahías libres, no las otras reservas del vehículo. | `AvailabilityPolicy.withoutBusyTimes` quita de las alternativas las horas que se cruzan con otra reserva del mismo vehículo. | booking *578c5a1* y *da233d5* (merge *6d9c8e9*). Versión *v1.1.1*, desplegada el 27 de octubre. | Revisión del 2026-10-27 | 2026-10-27 |

*Nota.* Elaborado por los autores a partir del historial de Git de los repositorios, el registro de riesgos y los ADR del proyecto (Equipo de Desarrollo Vehicle Washing, 2026a). AC-56 a AC-71 están en *develop* desde el 23 de octubre de 2026 y AC-73 a AC-87 desde el 24 de octubre, con el CI en verde. Todas corren en producción desde el despliegue del 24 de octubre de 2026 (el servidor quedó con *develop*; AC-87, desde la actualización de notification-service de las 17:12 UTC) y llegan a *main* con la versión *v1.1.0*. AC-88 y AC-89 están en *develop* y en producción desde el 26 de octubre de 2026 (booking-service, payment-service y la web); AC-88 es una acción preventiva y de mejora, pero se registra aquí con el número AC siguiente porque nació de un hallazgo concreto (la pregunta de la sustentación) y se cerró con evidencia. AC-91 a AC-93 están en *main* desde *v1.1.1* de booking-service (27 de octubre de 2026) y en producción desde el despliegue continuo de esa madrugada.

## 10. Registro de Acciones Correctivas Abiertas

**Tabla 7**

*Acciones Correctivas Abiertas*

| ID | No conformidad | Sev. | Causa raíz | Acción propuesta | Responsable | Prioridad | Criterio de cierre |
|---|---|---|---|---|---|---|---|
| ACO-04 | La tabla *audit.audit_log* existe pero nadie escribe en ella. | P2 | No se crearon los disparadores ni se fija *SESSION_CONTEXT(N'user_id')*. | Disparadores de auditoría en las tablas sensibles y contexto de sesión en cada servicio, o retirar la tabla del diseño mediante un ADR. | Líder de backend | Media | Cambios de precios, cuentas de pago y roles visibles en la auditoría. |
| ACO-13 | Las entidades que editan los administradores en customer-service y security-service no usan bloqueo optimista: en ediciones concurrentes gana la última escritura. Las reservas de booking-service (BIZ-09, AC-77) y las ejecuciones de operations-service (OPS-03, AC-76) ya están protegidas. | P3 | La columna *row_version* existe, pero no se mapea con *@Version*. | Mapear *@Version* en customer y security y responder 409 ante conflicto. | Líder de backend | Baja | Prueba de dos actualizaciones con la misma versión. (TD-008) |
| ACO-24 | *develop* no está protegida en ningún repositorio (H-02). Avance al 27 de octubre de 2026: los 10 repositorios tienen su versión en *main* (*v1.1.0*, *v1.1.1* o *v1.1.2*), *main* está protegida en todos y el servidor público corre *main*; payment-service se despliega desde *main* desde *v1.1.1* de infra. | P2 | Proteger *develop* quedó para después de las versiones. | Proteger *develop* en los 10 repositorios (PR y CI en verde). | Líder de backend | Alta | Etiqueta en los 10 repositorios (cumplido), *main* protegida (cumplido) y *develop* protegida. |
| ACO-25 | Siguen secretos expuestos: la contraseña antigua de SQL Server de desarrollo en el historial de Git y en *main* de payment-service, y la clave de Firebase en *google-services.json* (BIZ-11, H-09, MOV-18). Desde el 24 de octubre *payment-migrate* recibe la contraseña por variable de entorno y la guía del equipo usa marcadores (E-05, *lavarapido-infra* *b63f85a*). El 27 de octubre (payment-service PR #15, en *v1.1.3*, que se fusionó en *main* ese día y se etiquetó el 4 de noviembre) salieron del código las contraseñas de las cuentas demo del script de punta a punta, el servicio dejó de usar credenciales por defecto (*DB_PASSWORD* y RabbitMQ) y *.gitignore* cubre cualquier *.env.\**; la revisión de los 99 commits encontró en el historial dos contraseñas antiguas de SQL Server y las de las cuentas demo, ninguna igual a las del *.env* local. | P2 | Se versionaron al comienzo del proyecto. | Rotar la contraseña y restringir la clave de Firebase por paquete y huella SHA-1. | Líder de backend | Media | La contraseña antigua ya no funciona y la clave solo sirve para la app. |
| ACO-26 | Las pruebas de punta a punta no corren solas: la batería remota (*e2e.yml*, 44 casos) existe pero solo se ha probado contra un simulador, y las 19 de punta a punta locales no corren en el CI (F-01 a F-05, H-13). Avance al 27 de octubre de 2026: las 17 pruebas de integración de payment-service ya corren en el CI (job "Pruebas de integración", contra SQL Server 2022 con las migraciones de Liquibase) y las 19 de punta a punta pasaron 19 de 19 en local ese día. | P2 | El CI no levanta el sistema completo y faltan las cuentas de prueba en producción. | Crear las 3 cuentas de prueba y los secretos *E2E_\**, correr *e2e.yml* contra Azure (sin banderas, luego con *allow_writes*), llevar al CI las pruebas de punta a punta y agregar pruebas de integración en los demás servicios. | Equipo de desarrollo | Media | *e2e.yml* en verde contra Azure, las de punta a punta en el CI y las de integración de los demás servicios ejecutadas (las de payment, cumplido). |
| ACO-28 | La app permite tráfico sin cifrar en todas sus versiones (*usesCleartextTraffic*, MOV-11). El perfil *production* de EAS ya fija la URL HTTPS (MOV-08, AC-84). | P3 | Cambiar la configuración nativa cambia la huella de la app y obliga a repartir un APK nuevo. | Quitar *usesCleartextTraffic* en el próximo APK. | Líder de backend | Baja | Un APK nuevo sin tráfico en claro. |
| ACO-30 | No hay issues ni tablero de seguimiento en los repositorios (H-22). | P3 | El seguimiento se llevó en documentos. | Un issue por historia de usuario, PR enlazados y un tablero de GitHub Projects. | Equipo de desarrollo | Media | Cada PR cierra un issue y el tablero muestra el estado. |
| ACO-33 | La web pide la cola de pagos del administrador sin paginar, con la imagen de cada comprobante, y la vuelve a pedir cada 45 segundos: con datos reales arriesga la memoria de payment-service (384 MB). | P2 | La ruta paginada (*?page&size*, sin imagen, con *hasReceiptImage*) se agregó en payment (E-10) y en la app, pero no alcanzó a llegar a la web. | Pedir la cola con *page* y *size* y la imagen en el detalle (*GET /admin/payments/{id}*). | Líder de frontend | Alta | La cola de la web pide páginas de 20 sin imágenes. |
| ACO-34 | booking-service toma el candado de la agenda y, mientras lo tiene, llama a customer-service: si customer va lento, toda escritura de reservas queda en fila y puede responder 409 *SCHEDULE_BUSY* (E-11). | P2 | Cancelar, cambiar el estado y el avance del operario piden el vehículo después del candado. | Pedir el vehículo antes de tomar el candado y releer la reserva después. | Líder de frontend (booking-service) | Media | Prueba con customer lento que no bloquea las demás reservas. |
| ACO-35 | Una reserva que el administrador marca COMPLETED con líneas abiertas genera avisos falsos de "no se inició" al operario y a los administradores, y sale en "Por cerrar" (OPS-01). | P2 | El seguimiento revisa las líneas sin mirar el estado de la reserva. | No avisar a las reservas COMPLETED; cerrar sus líneas como atendidas o dejarlas solo en "Por cerrar". Antes del release, contar en la base las reservas COMPLETED con líneas abiertas. | Líder de proyecto | Media | Prueba con una reserva completada por el administrador que no genera avisos. |
| ACO-36 | En cada *BookingConfirmed*, operations-service consulta a security-service la cuenta de cada operario, también de los que no tienen turno; como la consulta falla cerrada (A-13), un solo fallo pasajero tumba la asignación (OPS-11). | P3 | La revisión de la cuenta se hace antes de filtrar a los candidatos. | Filtrar primero con *AssignmentPolicy* (sin red) y consultar solo a los candidatos. | Líder de proyecto | Media | Una confirmación consulta a security solo por los candidatos. |
| ACO-37 | Quedan tres riesgos en los scripts del servidor: *restore-db.sh* hace un respaldo con la retención de 14 que puede borrar el archivo que se va a restaurar; *rollback.sh* usa el compose actual con las imágenes *:prev* y no detecta un cambio de contrato (puerto, variables); y *update-server.sh* corrido a mano no toma el candado. | P2 | Los scripts nuevos del 24 de octubre se revisaron después de fusionarlos. | Descomprimir antes de respaldar; guardar los *repo-heads* junto a *:prev* y avisar si cambió infra; tomar el candado en *update-server.sh*. | Líder de proyecto | Media | Simulación de los tres casos con el resultado esperado. |
| ACO-38 | La inspección abierta por enlace o al recargar sale con el cliente, el estado, el servicio y la fecha vacíos en la web. | P3 | Con el refresco automático (WEB-16) los stores ya no cargan en su constructor y la inspección no pide la reserva. | Llamar a *load()* de las reservas al abrir la inspección. | Líder de frontend | Media | Abrir una inspección por enlace muestra todos sus datos. |
| ACO-39 | Si la sesión vence con la app en segundo plano, el celular no se da de baja de las push y sigue recibiéndolas (MOV-10). | P3 | El temporizador no corre en segundo plano y, al volver, la baja responde 401. | Revisar el vencimiento al volver a primer plano antes de cualquier petición, o dar de baja por el token de push en el servidor. | Líder de backend | Media | Prueba en un dispositivo: la sesión vence en segundo plano y no llegan más push. |
| ACO-40 | Hallazgos menores de la revisión cruzada del 24 de octubre de 2026: *reasonCode* sin usar en notification-service, el NO_SHOW automático sin evento, el paquete (PACKAGE) que no se edita, el monto FIXED en el aviso de cupón, variables viejas de booking (*BOOKING_EXPIRY_\**) en infra, *markDelivered* dentro del mismo *try* del envío (posible duplicado), el relleno de la migración 022 de payment con la promoción de hoy y la vista de la 023, más estricta que el servicio en una misma reserva; además, las variables nuevas del gateway y de operations (*AUTH_RATE_LIMIT_**, *TRUSTED_PROXIES*, *ROUTE_*_TIMEOUT*, *START_LATE_LIMIT*) no están en *.env.example* ni en el compose. | P3 | Detalles que no afectan los flujos principales. | Corregirlos uno por uno y, antes del release, buscar en la base los pagos abiertos duplicados que rechazó la 023. | Equipo de desarrollo | Baja | Cada punto con su commit o descartado con justificación. |
| ACO-41 | El efectivo con límite (AC-88) no tiene tolerancia por retraso: una reserva en efectivo cuyo cliente no llega sigue ocupando la bahía y su cupo de efectivo hasta que el administrador la cierra o el seguimiento de operations la resuelve. | P3 | Al diseñar el pago al reservar el equipo eligió solo el límite y el vencimiento del pago digital, y dejó para después la tolerancia por retraso. | Cancelar la reserva en efectivo si no se inició N minutos después de su hora de inicio (valor configurable), con un motivo propio que solo use el sistema, y avisar al cliente. | Líder de backend | Baja | Regla implementada con su prueba y probada en vivo, o descartada con justificación. |
| ACO-42 | Los Términos y Condiciones y la Política de Tratamiento de Datos de la web y de la app traen datos de contacto de ejemplo: correo *datos@expresscarwash.co*, teléfono *+57 300 123 4567*, NIT y domicilio. | P2 | Los textos legales se redactaron antes de tener los datos reales del responsable del tratamiento. | Reemplazar el correo, el teléfono, el NIT y el domicilio por los del lavadero en los 4 idiomas de la web y de la app. | Líder de proyecto | Alta | Los textos legales publicados muestran los datos reales del responsable (Ley 1581 de 2012). |
| ACO-43 | Las listas de reservas no se piden por páginas: *GET /bookings/me* devuelve todas las del cliente y *GET /admin/bookings* todas las del rango, sin tope. | P3 | Las pantallas se hicieron con pocos datos de prueba. | Agregar *page* y *size* a las dos rutas y pedirlas por páginas en la web y en la app. | Líder de frontend (booking-service) | Media | Las listas de reservas cargan por páginas con datos reales. |
| ACO-44 | La web no tiene pantalla de historial de pagos (RF-015 parcial): */client/history* es el historial de reservas y nadie usa los filtros de *GET /payments/me*. | P3 | El historial de pagos se cubrió con el estado del pago en cada reserva. | Pantalla de historial de pagos del cliente con los filtros de fecha y servicio de la API. | Líder de frontend | Media | RF-015 implementado y con su caso de prueba en verde. |

*Nota.* Elaborado por los autores a partir del backlog técnico, del anexo B del Manual Técnico y de la verificación del código. ACO-01 a ACO-03 y ACO-05 a ACO-12 pasaron a la Tabla 6 como AC-24 a AC-34, y ACO-14 como AC-55 (23 de octubre de 2026). El 24 de octubre de 2026 se cerraron ACO-15 a ACO-18, ACO-20 a ACO-23, ACO-27, ACO-29, ACO-31 y ACO-32 (AC-73 a AC-84) y se abrieron ACO-33 a ACO-40 con los pendientes de la revisión cruzada final de ese día. El 26 de octubre de 2026 se abrió ACO-41, la mejora pendiente del pago al reservar, y esa noche se cerró ACO-19 como AC-90 (primera corrida del despliegue continuo). El 27 de octubre de 2026 se abrieron ACO-42 a ACO-44 con pendientes reales que no estaban registrados.

## 11. Registro de Acciones Preventivas

Las acciones preventivas atacan las causas de riesgos que todavía no se han materializado o que podrían repetirse.

**Tabla 8**

*Acciones Preventivas*

| ID | Riesgo o causa potencial | Acción preventiva | Responsable | Prioridad | Estado | Origen |
|---|---|---|---|---|---|---|
| AP-01 | Regresiones que llegan a *develop* sin detectarse. | Integración continua por repositorio (*./mvnw verify*, *dotnet test*, *ng build*, *ng test* y *tsc*) y regla de integrar solo con el resultado en verde. | Líder de proyecto | Alta | Cerrada (2026-10-19): GitHub Actions en los 10 repositorios, en cada push o pull request a *develop* y *main* | R-003, TD-006 |
| AP-02 | Pérdida de un evento entre el commit y la publicación. | Patrón Outbox en security, booking, operations y payment. | Líder de backend | Baja | Aceptada como riesgo hasta salir del alcance académico; mitigada el 2026-10-24 con confirmaciones del broker, cola de errores en operations y entregas pendientes con reintento en notification (AC-78) | R-005, TD-011 |
| AP-03 | Fuga de secretos (*.env*, contraseña de aplicación del correo, *JWT_SECRET*). | Mantener *.env* ignorado, revisar con *git check-ignore* antes de cada implementación y rotar los secretos cada trimestre o ante sospecha. | Todo el equipo | Alta | En ejecución (permanente) | R-004 |
| AP-04 | Pérdida de datos antes de una demostración o en operación. | Respaldo diario automatizado con verificación y prueba de restauración (Manual de Administrador, sección 14). | Administrador de TI | Alta | Cerrada (2026-10-21): *backup-db.sh* respalda la base todos los días y antes de cada actualización, *verify-backup.sh* restaura cada respaldo con otro nombre para comprobarlo y se guardan los últimos 14 (Documento de Despliegue) | R-008 |
| AP-05 | La documentación vuelve a separarse del código. | Lista de revisión de pull requests que exige actualizar modelos, contratos OpenAPI, ADR y manuales afectados; revisión de los manuales antes de cada implementación. | Autor de cada pull request | Media | En ejecución (permanente) | R-010 |
| AP-06 | Implementar con *MESSAGING_ENABLED=false*, sin *INTERNAL_API_KEY* o con el perfil *dev*. | Lista de verificación del Anexo A del Procedimiento de Implementación. | Administrador de TI | Media | Cerrada (incluida en el procedimiento) | R-I02, R-I03 |
| AP-07 | Defectos de persistencia (bloqueos y disparadores) que solo se ven a mano. | Pruebas de persistencia con Testcontainers y SQL Server real en booking-service. | Líder de backend | Baja | En ejecución (la suite de punta a punta cubre parte del riesgo) | TD-010 |
| AP-08 | Uso de la cuenta *sa* por todos los servicios. | Un inicio de sesión por servicio con permisos solo sobre sus esquemas y una cuenta separada para las migraciones. | Líder de backend | Media | Aceptada como riesgo (2026-10-24): documentada en *DEPLOY.md* de *lavarapido-infra* (H-10), con el cambio propuesto para después de la exposición | Política de seguridad |
| AP-09 | Una acción de usuario no puede seguirse entre servicios. | Propagar *X-Correlation-Id* en todos los clientes REST y mensajes y generarlo en el gateway. | Líder de backend | Baja | En ejecución (booking, operations y payment ya reenvían el encabezado) | TD-007 |
| AP-10 | Falta de métricas para comprobar el tiempo de respuesta (RNF-001). | *micrometer-registry-prometheus* en los servicios Java y métricas equivalentes en .NET. | Líder de backend | Baja | Abierta | TD-012 |
| AP-11 | Ataques de fuerza bruta o abuso de la API si el sistema se expone. | Límite de solicitudes en el gateway o en Caddy (TD-022); el bloqueo de cuentas ya está activo (AC-25). | Líder de backend | Media | Cerrada (2026-10-24): 60 peticiones por minuto por IP en */api/v1/auth/\** en el gateway (AC-79) | Política de seguridad |
| AP-12 | Componentes con vulnerabilidades conocidas. | Revisión trimestral con *./mvnw versions:display-dependency-updates*, *dotnet list package --vulnerable* y *npm audit*. | Líderes de backend y frontend | Media | En ejecución (permanente) | OWASP A06 |
| AP-13 | Confusión por la numeración propia de los changelogs de pagos, que repite los números globales. | Documentar la regla en la estrategia de migraciones y usar el prefijo *pay-* en los identificadores. | Líder de backend | Baja | Cerrada (documentada) | TD-019 |
| AP-14 | Funciones construidas que contradicen decisiones ya registradas (causa de AC-11 y AC-12). | Revisar los ADR vigentes y el mapa de navegación antes de iniciar una función, y consultar al equipo ante una contradicción. | Todo el equipo | Media | En ejecución (permanente) | Lección de ADR-015 |
| AP-15 | Cambios de interfaz que rompen el diseño en el celular sin que nadie lo note (causa de AC-41). | Medir las pantallas a 360 y 768 px con Playwright antes de fusionar cambios de interfaz, a partir del script *e2e/width-360.js*, y revisar las capturas. | Líderes de frontend y backend | Media | En ejecución (permanente) | Lección de AC-41 |
| AP-16 | Que los registros de los contenedores llenen el disco del servidor. | Limitar los registros de cada contenedor a 3 archivos de 10 MB. | Líder de proyecto | Media | Cerrada (2026-10-20): *docker-compose.prod.yml* | Revisión del servidor |
| AP-17 | Ataques desde el navegador contra el sitio público (incrustarlo en otra página, cargar scripts de otros orígenes). | Encabezados de seguridad y política de contenido (CSP) en Caddy. | Líder de proyecto | Media | Cerrada (2026-10-20): *deploy/Caddyfile* | OWASP A05 |
| AP-18 | Que un contenedor quede colgado sin que nadie lo note. | Chequeo de salud cada 30 segundos en todos los contenedores, visible en *docker compose ps*. | Líder de proyecto | Media | Cerrada (2026-10-20): *docker-compose.prod.yml* | Revisión del servidor |
| AP-19 | Que una tarea automática registre un estado definitivo con datos incompletos (causa de AC-53). | Toda regla automática que cambie un estado definitivo solo actúa cuando no hay duda; lo dudoso pasa a una lista para que lo decida una persona, y la regla se revisa en equipo antes de fusionarla. | Todo el equipo | Alta | En ejecución (permanente) | Lección de AC-53 |

*Nota.* Elaborado por los autores a partir del registro de riesgos, el backlog técnico y la política de seguridad del proyecto.

## 12. Registro de Acciones de Mejoramiento

**Tabla 9**

*Acciones de Mejoramiento*

| ID | Oportunidad de mejora | Beneficio esperado | Responsable | Prioridad | Estado | Origen |
|---|---|---|---|---|---|---|
| AM-01 | Asignación automática de operarios al confirmarse una reserva, reutilizando *AssignmentPolicy*. | El administrador deja de asignar cada reserva a mano (RF-008). | Líder de backend | Media | Cerrada (2026-10-16): *BookingConfirmedListener* y *AutoAssignmentPolicy*, con la elección opcional del cliente; prueba *booking-flow.test.js* | TD-020, ADR-014 |
| AM-02 | Aplicar la matriz de permisos de *security.role_permission* en la autorización real de cada endpoint. | Control de acceso granular configurable desde el panel. | Líder de backend | Baja | Abierta (requiere ADR); desde el 2026-10-24 la pantalla es de solo lectura (AC-83) | ADR-015 |
| AM-03 | Reprogramación de reservas por el cliente desde la web y la app (la API ya ofrece *PUT /api/v1/bookings/{id}*). | El cliente no tiene que cancelar y reservar de nuevo. | Líder de frontend | Media | Abierta | Manual de Usuario |
| AM-04 | Completar la aplicación móvil y construir una versión con EAS para las notificaciones push. | Avisos inmediatos en el teléfono. | Líder de frontend | Media | Cerrada (2026-10-16): APK compilado con EAS y publicado en */app/* del servidor, con avisos push de versión nueva | ADR-011 |
| AM-05 | Firma RS256 y tokens de actualización (*refresh tokens*). | Menor impacto de una fuga del secreto y sesiones más largas sin volver a iniciar sesión. | Líder de backend | Baja | Abierta | ADR-006 |
| AM-06 | Almacenar comprobantes, códigos QR y fotos fuera de la base de datos. | Base de datos más liviana y respaldos más rápidos. | Líder de backend | Baja | Abierta | Evolución recomendada |
| AM-07 | Separar en *promotion.promotion* los campos de presentación y los de descuento real. | Evita que el paquete mostrado y el descuento aplicado se contradigan. | Líder de backend | Baja | Abierta | ADR-015 |
| AM-08 | Publicar la web compilada detrás del gateway con HTTPS. | Entorno apto para producción, sin servidor de desarrollo. | Líder de frontend | Media | Cerrada (2026-10-16): contenedor web con nginx detrás de Caddy y HTTPS en el servidor público (Documento de Despliegue) | Procedimiento de Implementación, R-I08 |
| AM-09 | Herramientas de cobertura (JaCoCo) y análisis estático (SonarQube). | Métricas objetivas de calidad del código. | Líder de proyecto | Baja | Abierta | Estrategia de pruebas |
| AM-10 | Prueba automática de punta a punta entre servicios (registro → reserva → notificación → pago). | Detección temprana de fallas de integración. | Líder de backend | Media | Cerrada (2026-10-16): *lavarapido-infra/e2e*, 19 pruebas aprobadas contra los contenedores | Estrategia de pruebas |
| AM-11 | Reemplazar los marcadores de figuras de los manuales por capturas reales de la versión final. | Manuales completos según la guía del SENA. | Líder de frontend | Alta | Cerrada (2026-10-16): manuales 4.0 con capturas reales del sistema | Anexo A de cada manual |
| AM-12 | Usar los estados de ejecución *PAUSED* y *WITH_ISSUE* en la interfaz del operario. | Registrar pausas y novedades del servicio. | Líder de frontend | Baja | Abierta | Modelo de datos |
| AM-13 | Exponer en el panel el envío de avisos generales (*SYSTEM_MESSAGE*) a grupos de usuarios. | Comunicación directa con clientes y operarios sin usar la API. | Líder de frontend | Baja | Abierta | Manual de Administrador |
| AM-14 | Gestionar el informe de inspección desde la aplicación móvil. | El administrador registra el estado del vehículo junto al vehículo, con fotos de la cámara que se reducen en el celular. | Líder de backend | Media | Cerrada (2026-10-17): pantalla *AdminInspection* en la app | Solicitud del equipo |
| AM-15 | Preguntas frecuentes en la página de inicio. | El visitante resuelve las dudas más comunes (reserva, pago, cancelación, puntos) sin escribir al lavadero. | Líder de backend | Baja | Cerrada (2026-10-17) | Revisión de la landing |
| AM-17 | Despliegue continuo: que cada fusión aprobada a *main* llegue sola al servidor y que, al encender la máquina, se despliegue lo que entró mientras estaba apagada. | Nadie entra por SSH para desplegar y el servidor no queda atrás de *main*. | Líder de proyecto | Alta | Implementada (2026-10-26): primera corrida automática al fusionar «Release v1.1.1» a *main* de infra y OTA del móvil publicada tras comprobar la huella nativa (AC-90) | Revisión del 2026-10-23 |
| AM-18 | Pruebas de comportamiento en los clientes. | Detectar a tiempo los errores de la web y de la app. | Equipo de desarrollo | Alta | En ejecución (2026-10-27): la web tiene 242 pruebas en 68 archivos, con cobertura en el CI (guard e interceptor reales, pago con cupón, refresco y traducciones de los 4 idiomas), y la app 106 pruebas (ACO-26) | Evaluación del instructor (H-03, H-04) |
| AM-16 | Obligar a instalar el APK nuevo cuando hay cambios nativos que no llegan por internet. | Nadie sigue usando una versión de la app sin los arreglos que dependen del APK. | Líder de backend | Media | Cerrada (2026-10-21): pantalla que solo deja descargar el APK publicado si su *versionCode* es mayor; si el servidor no responde, la app sigue | Revisión del 2026-10-21 |

*Nota.* Elaborado por los autores a partir de la evolución recomendada del Manual Técnico, de los ADR y de las limitaciones documentadas en los manuales.

## 13. Indicadores de Seguimiento

**Tabla 10**

*Indicadores de la Gestión de Acciones*

| Indicador | Fórmula | Meta | Valor al 2026-10-27 |
|---|---|---|---|
| Acciones correctivas cerradas | Correctivas cerradas / correctivas registradas × 100 | ≥ 70 % por entrega | 93 / 112 = 83 % (el 2026-10-26 eran 90 / 106 y por la tarde 89 / 106; al 2026-10-24, 87 / 103; al 2026-10-23, 72 / 92; al 2026-10-21, 54 / 57) |
| Correctivas P0 y P1 abiertas | Número de ACO con severidad P0 o P1 | 0 antes de una implementación | 0 (ACO-15 y ACO-16 se cerraron como AC-73 y AC-74) |
| Cierre en plazo | Acciones cerradas dentro del plazo de su severidad / acciones cerradas × 100 | ≥ 90 % | Por medir desde la versión 1.0 |
| Reincidencia | Correctivas cerradas cuyo problema reaparece / correctivas cerradas × 100 | 0 % | 0 % |
| Defectos escapados | Defectos encontrados en aceptación u operación / defectos totales del periodo × 100 | Tendencia a la baja | Por medir desde la primera implementación |
| Pruebas automatizadas | Pruebas ejecutadas y aprobadas en todos los repositorios | Creciente | 1 348 en el CI de *main* del 27 de octubre de 2026, todas aprobadas: gateway 62, security-service 200, customer-service 83, booking-service 137, operations-service 110, notification-service 133, payment-service 275 con *v1.1.3* (25 del proyecto de integración, 12 de ellas contra SQL Server), web 242 y app móvil 106; las 19 de punta a punta pasaron en local ese día. Antes: con *v1.1.2* de payment-service (264), 1 337; al 26 de octubre de 2026 cambiaron booking (133), payment-service (247, sin las de integración), la web (239) y la app móvil (100); con los demás artefactos del último CI del 24 de octubre, 1 306 sin el proyecto de infraestructura de payment (el 24 de octubre eran 1 187 con el mismo criterio y 1 192 con él); el 27 de octubre de 2026 booking subió a 137 y customer-service a 83, para 1 311 con el mismo criterio, y las pruebas de persistencia de payment empezaron a correr en el CI contra SQL Server. Además, la prueba en vivo del pago al reservar del 26 de octubre (11 pruebas, 2 de ellas fallas simuladas) y 42 casos funcionales el 19 de octubre |

*Nota.* Elaborado por los autores. El conteo de pruebas corresponde al último CI de *develop* de cada repositorio del 24 de octubre de 2026 (el 23 de octubre eran 838; en *v1.0.0*, 757; el 21 de octubre, 707); en la versión 1.0 se contaban anotaciones (unas 400). El 26 de octubre de 2026 se agregaron los conteos de booking, payment, la web y la app móvil (documento de Pruebas, Tabla 5f). El 27 de octubre de 2026 se tomó el conteo del CI de *main* (Tabla 5f del documento de Pruebas). Las correctivas registradas cuentan AC-01 a AC-93 (AC-88 es preventiva y de mejora, pero tiene número AC) y las 19 ACO abiertas.

El líder de proyecto revisa los indicadores en cada retrospectiva y antes de cada implementación. Al 27 de octubre de 2026 hay 19 correctivas abiertas (ACO-04, ACO-13, ACO-24 a ACO-26, ACO-28, ACO-30 y ACO-33 a ACO-44), todas P2 o P3 y con acción registrada. Como no queda ninguna P0 ni P1, **se cumple el criterio de entrada E6** del Procedimiento de Implementación. La de mayor prioridad es ACO-33 (la cola de pagos de la web sin paginar), que conviene cerrar antes de cargar datos reales.

## 14. Lecciones Aprendidas

**Tabla 11**

*Lecciones Aprendidas*

| N.° | Lección | Acciones relacionadas |
|---|---|---|
| L-01 | Guardar el tiempo en UTC y convertirlo solo en los bordes evita desplazamientos de hora. | AC-01 |
| L-02 | Un servicio no debe arrancar antes de que sus dependencias estén sanas; la orquestación lo debe garantizar. | AC-02 |
| L-03 | Acordar el modelo de datos antes de programar un servicio evita implementaciones duplicadas. | AC-03 |
| L-04 | Toda decisión ya registrada en un ADR debe revisarse antes de construir; una contradicción se consulta antes de programar. | AC-11, AC-12, AP-14 |
| L-05 | Una regla de negocio debe vivir en un solo servicio y las pantallas deben consultarla, no repetirla. | AC-15, AC-45 |
| L-06 | Una preferencia que la persona usuaria cambia debe guardarse en su cuenta para tener efecto. | AC-16, AC-17 |
| L-07 | Un nuevo servicio no está terminado hasta que tiene contenedor, ruta en el gateway, variables en Compose y eventos publicados. | AC-04, AC-05, AC-06 |
| L-08 | Los endpoints administrativos también necesitan validación estricta de entrada y listas permitidas. | AC-18, AC-19 |
| L-09 | La documentación se actualiza en el mismo pull request que el código; de lo contrario, se desactualiza en días. | AC-23, AP-05 |
| L-10 | Un ledger contable necesita movimientos de reversión y control de concurrencia desde el primer día. | AC-09 |
| L-11 | Una operación de "crear si no existe" debe manejar la carrera entre dos solicitudes simultáneas. | AC-35 |
| L-12 | Cada opción de la interfaz debe decir exactamente qué hace con los datos de la persona: desactivar no es eliminar. | AC-37 |
| L-13 | Cerrar sesión tiene que dejar sin efecto el token en todos los servicios, no solo borrarlo del navegador. | AC-44 |
| L-14 | Lo que se hace a mano y lo que hace el sistema solo sobre el mismo recurso necesitan el mismo candado. | AC-46 |
| L-15 | Un respaldo solo cuenta si se comprobó que se puede restaurar. | AP-04, AC-52 |
| L-16 | El sistema no debe decidir solo algo definitivo con datos incompletos: si el operario pudo olvidar registrar el servicio, lo decide el administrador. | AC-53, AP-19 |
| L-17 | Un script de despliegue nunca debe poder bajar de versión: antes de cambiar de rama se comprueba que lo desplegado esté contenido en la rama pedida. | AC-85 |
| L-18 | Un cambio de contrato entre dos piezas (el puerto de la web y el Caddyfile, un campo nuevo de la API) se despliega en el mismo paso o con el consumidor primero; si no, el primer despliegue deja la función caída. | AC-86, ACO-33, ACO-37 |
| L-19 | Una migración no está probada hasta que el servicio lee y escribe la tabla en SQL Server real: las pruebas unitarias, que corren sin base, no ven los choques de tipos entre la base, el controlador y Hibernate. La prueba en vivo después de desplegar encontró el de la 047; desde el 27 de octubre de 2026 las pruebas de integración de payment-service corren en el CI contra SQL Server 2022 con las migraciones de Liquibase, y siguen pendientes las de los demás servicios (RNF-003, ACO-26 y AP-07); se mantiene la prueba en vivo después de cada despliegue. | AC-87, ACO-26, AP-07 |
| L-20 | Mientras no haya Outbox, un evento puede perderse. Antes de que una tarea automática haga algo definitivo por lo que le llegó (o no le llegó) por eventos, debe confirmarlo con el servicio dueño del dato, y si ese servicio no responde, no actuar y volver a intentarlo. La prueba en vivo con fallas simuladas (sin RabbitMQ y con el servicio caído) lo mostró antes de llegar a producción. | AC-89, AP-02, AP-19 |

*Nota.* Elaborado por los autores a partir del registro de riesgos cerrados y de las acciones de este documento.

## 15. Glosario

**Tabla 12**

*Glosario*

| Término | Definición |
|---|---|
| Acción correctiva | Acción que elimina la causa de una no conformidad ocurrida para evitar que se repita. |
| Acción de mejoramiento | Acción que aumenta el valor o la calidad del software sin que exista una falla. |
| Acción preventiva | Acción que elimina la causa de una no conformidad potencial. |
| ADR | Registro de decisión de arquitectura. |
| Causa raíz | Causa fundamental de un problema sobre la que se puede actuar para que no se repita. |
| Corrección | Acción inmediata que elimina el síntoma de una no conformidad sin atacar su causa. |
| Deuda técnica | Trabajo pendiente que hace que el software funcione, pero no como debería. |
| Eficacia | Grado en que una acción logra el resultado planificado. |
| Ledger | Registro contable de movimientos de puntos, cuyo saldo es el del último movimiento. |
| No conformidad | Incumplimiento de un requisito, de una decisión registrada o de lo documentado. |
| PHVA | Ciclo planear, hacer, verificar y actuar de la mejora continua. |
| Post-mortem | Análisis sin culpables de un incidente grave, con su causa y sus acciones. |

*Nota.* Elaborado por los autores a partir de ISO (2015), ISO et al. (2022) y la documentación del proyecto.

## Referencias

Congreso de la República de Colombia. (2012). *Ley Estatutaria 1581 de 2012, por la cual se dictan disposiciones generales para la protección de datos personales*. http://www.secretariasenado.gov.co/senado/basedoc/ley_1581_2012.html

Equipo de Desarrollo Vehicle Washing. (2026a). *Vehicle washing management system: Project documentation* (vehicle-w-docs) [Documentación de proyecto no publicada]. GitHub. https://github.com/code-sena/vehicle-w-docs

International Organization for Standardization. (2015). *Quality management systems — Requirements* (ISO 9001:2015). https://www.iso.org/standard/62085.html

International Organization for Standardization, International Electrotechnical Commission e Institute of Electrical and Electronics Engineers. (2017). *Systems and software engineering — Software life cycle processes* (ISO/IEC/IEEE 12207:2017). https://www.iso.org/standard/63712.html

International Organization for Standardization, International Electrotechnical Commission e Institute of Electrical and Electronics Engineers. (2022). *Software engineering — Software life cycle processes — Maintenance* (ISO/IEC/IEEE 14764:2022). https://www.iso.org/standard/80710.html

Servicio Nacional de Aprendizaje. (2026). *Guía para elaborar manuales de software: Manual de usuario, manual técnico, manual de instalación y manual de administrador* [Material de formación no publicado]. Programa Tecnólogo en Análisis y Desarrollo de Software.

## 16. Anexos

### Anexo A. Formato de Registro de una Acción

**Tabla 13**

*Formato de Registro de una Acción*

| Campo | Contenido |
|---|---|
| ID | AC-NN, ACO-NN, AP-NN o AM-NN |
| Tipo | Correctiva / Preventiva / De mejoramiento |
| Fecha de registro y quién la detectó |  |
| Descripción de la no conformidad, el riesgo o la oportunidad |  |
| Evidencia inicial (registro, captura, prueba, código de error) |  |
| Severidad (P0 a P3) y prioridad (Alta, Media, Baja) |  |
| Corrección inmediata aplicada (si aplica) |  |
| Causa raíz y técnica usada |  |
| Acción planificada, responsable y fecha compromiso |  |
| Rama, pull request y ADR relacionado |  |
| Criterio de cierre |  |
| Verificación de eficacia (quién, cuándo, evidencia) |  |
| Estado y fecha de cierre |  |
| Lección aprendida |  |

### Anexo B. Ejemplo de Análisis de Causa Raíz

**Tabla 14**

*Cinco Porqués de la Acción AC-01 (Horas Corridas Cinco Horas)*

| Pregunta | Respuesta |
|---|---|
| ¿Por qué el cliente veía su reserva a otra hora? | Porque la hora mostrada difería en cinco horas de la elegida. |
| ¿Por qué difería? | Porque la hora se guardaba convertida con una zona y se leía con otra. |
| ¿Por qué se usaban zonas distintas? | Porque la JVM del servicio tomaba la zona del equipo donde corría. |
| ¿Por qué la zona del equipo afectaba los datos? | Porque el código convertía fechas sin una zona explícita. |
| ¿Por qué no había una zona explícita? | Porque no existía una regla del equipo para el manejo del tiempo. |
| **Causa raíz** | Ausencia de una convención de tiempo: se adoptó "guardar en UTC y convertir solo en los bordes con *BOOKING_ZONE*", y la JVM corre en UTC. |

*Nota.* Elaborado por los autores a partir del riesgo cerrado C-001 del proyecto.

### Anexo C. Trazabilidad Entre Acciones, Riesgos y Deuda Técnica

**Tabla 15**

*Trazabilidad*

| Riesgo del proyecto | Deuda técnica | Acciones |
|---|---|---|
| R-002 payment-service parcial (cerrado como C-005) | TD-014, TD-015, TD-016, TD-017, TD-018 | AC-04 a AC-10, AC-27 |
| R-003 Regresiones sin red de seguridad | TD-001, TD-006, TD-010, TD-027 | AC-24, AC-87, AP-01, AP-07, AM-10 |
| R-004 Fuga de secretos | — | AP-03 |
| R-005 Evento perdido | TD-011 | AP-02, AC-54, AC-89 |
| R-007 Brechas de requisitos conocidas (cerrado como C-006) | TD-002, TD-003, TD-009 | AC-16, AC-17, AC-25, AC-31 |
| R-008 Pérdida de datos | — | AP-04, AC-52 |
| R-010 Desactualización de la documentación | — | AC-23, AC-34, AC-38, AP-05 |
| R-011 Servidor público apagado | — | Procedimiento de Implementación, R-I04 |
| Abuso de la API y reservas masivas | TD-022 | AP-11, AC-79, AC-88, ACO-41 |
| Sesiones cerradas que siguen activas | TD-024 | AC-44, AC-73 |
| Despliegue, reversión y versiones del servidor | — | AC-82, AC-85, AC-86, AC-90, AM-17, ACO-24, ACO-37 |
| Agenda de reservas y operarios | TD-025, TD-026 | AC-45, AC-46, AC-53, AC-91, AC-93, AP-19, ACO-43 |
| Disponibilidad del servidor público | — | AP-16, AP-17, AP-18 |
| R-001 Integración de operations-service (cerrado como C-004) | TD-020 | AM-01 |
| Ley 1581 de 2012 | — | AC-26, AC-37, AC-92, ACO-42 |

*Nota.* Elaborado por los autores a partir del registro de riesgos y del backlog técnico del proyecto.

### Anexo D. Evaluación del Instructor

El instructor evaluó el sistema de extremo a extremo con corte al 20 de octubre de 2026 (informe del 21 de octubre, equipo 8): **85/100, juicio A**. Los cinco flujos principales (sesión, reserva, pago, ejecución del operario y avisos) quedaron completos. Las dimensiones más bajas fueron Documentación y trazabilidad (59,0), Proceso y colaboración (68,4), API y contratos (76,7) y Pruebas (78,8); el repositorio de documentación no estaba entre los que revisó. La Tabla 16 registra sus 25 hallazgos y su estado al 27 de octubre de 2026, con *main* en producción: 11 corregidos, 7 parciales, 5 abiertos, 1 aceptado como riesgo y 1 que no se puede corregir (el 24 de octubre eran 9, 8, 6, 1 y 1; el 23 de octubre, 5, 7, 12, 0 y 1).

**Tabla 16**

*Hallazgos de la Evaluación del Instructor y su Estado*

| ID | Hallazgo del instructor | Nivel | Estado al 2026-10-27 | Evidencia o acción |
|---|---|---|---|---|
| H-01 | *main* no contiene el trabajo entregable en ninguno de los 10 repositorios. | Alto | Corregido | Los 10 repositorios tienen su versión en *main*: *v1.1.0* (26 de octubre) en el gateway, security, operations y notification; *v1.1.1* (27 de octubre) en customer y booking, y *v1.1.2* (27 de octubre) en la web, la app móvil, payment e infra. Producción corre *main* por despliegue continuo (AC-90). |
| H-02 | Sin pull requests ni revisión de pares. | Medio | Corregido | *main* protegida en los 10 repositorios: solo recibe cambios por PR con la aprobación de otro integrante y el CI en verde; cada versión entra por un PR «Release» aprobado (las del 27 de octubre de customer, booking, payment e infra las aprobó Brayan Patiño). Queda proteger *develop* (ACO-24). |
| H-03 | Las pruebas de la web son plantillas. | Alto | Corregido | 242 pruebas en 68 archivos al 27 de octubre, con el guard y el interceptor reales, el pago con cupón, el refresco y las traducciones, y cobertura en el CI (web *7edd87f*, *2245758*; AM-18). |
| H-04 | La app móvil no tiene pruebas. | Alto | Parcial | 106 pruebas con *jest-expo* en el CI de *main* al 27 de octubre, entre ellas el pago (*useClientPayment*), los errores y el refresco; falta medir la cobertura (AM-18, ACO-26). |
| H-05 | La matriz de permisos se guarda pero no se aplica. | Alto | Corregido | La pantalla queda de solo lectura (AC-83, web *a4d3715*); aplicar la matriz queda como mejora (AM-02). |
| H-06 | El perfil por defecto es *dev* con cuentas demo de contraseña publicada. | Medio | Corregido | AC-58. |
| H-07 | La guía del equipo publica credenciales de desarrollo. | Medio | Corregido | AC-58 y *lavarapido-infra* *b63f85a* (marcadores en la guía). |
| H-08 | Contraseña de SQL Server de desarrollo en el compose y el README. | Medio | Corregido | AC-58; la contraseña antigua sigue en el historial (ACO-25). |
| H-09 | *google-services.json* versionado con la clave de API. | Bajo | Abierto | ACO-25. |
| H-10 | Una sola base de datos y un solo usuario administrador para los seis servicios. | Medio | Aceptado como riesgo | Esquema por servicio (Documentación de Diseño, Tabla 8); el usuario *sa* compartido queda como riesgo aceptado en *DEPLOY.md* (*4a865aa*), con el cambio propuesto (AP-08). |
| H-11 | Si RabbitMQ no responde, el evento se pierde. | Medio | Parcial | Confirmaciones del broker y cola de errores en operations, y entregas pendientes con reintento en notification (AC-78); sin Outbox (AP-02, ADR-004). |
| H-12 | *booking.created* se escucha pero no se publica. | Bajo | Corregido | notification ya no escucha *booking.created* (*8fde2ee*). |
| H-13 | Las pruebas *e2e* no corren en el CI. | Medio | Parcial | Batería remota y *e2e.yml* (a mano) en *lavarapido-infra* (*573d10b*); las 17 de integración de payment corren en el CI desde el 27 de octubre y las 19 de punta a punta pasaron 19 de 19 en local ese día, pero todavía no corren solas en el CI ni se ha corrido la batería contra Azure con las cuentas de prueba (ACO-26). |
| H-14 | Un build *production* de la app apuntaría al emulador. | Medio | Parcial | *production* usa la URL HTTPS y la app falla con un mensaje claro si la URL es local (*7b5f095*, AC-84); queda *usesCleartextTraffic* (ACO-28). |
| H-15 | El despliegue depende de encender la máquina y no se pudo comprobar en línea. | Medio | Parcial | Despliegue con la espera de salud, *:prev* y la protección contra bajar de versión; desde el 26 de octubre de 2026 el despliegue continuo corre solo al fusionar a *main* y comprueba la web y la API (AM-17, AC-90). Falta lo de la disponibilidad: la máquina se sigue encendiendo bajo demanda y no hay un monitor externo que avise si el sitio se cae (UptimeRobot, pendiente; R-011, AP-16 a AP-18). |
| H-16 | Los reportes del administrador se calculan en el navegador. | Medio | Abierto | Mejora pendiente (RF-023 parcial); los gráficos ya se actualizan al llegar los datos (WEB-04). |
| H-17 | El README del gateway contradecía el código. | Bajo | Corregido | Corregido el 22 de octubre; README al día con los límites y los riesgos aceptados el 24 de octubre (*07737a5*). |
| H-18 | El token se guarda en *localStorage*. | Bajo | Abierto | Mitigado con la política de contenido (CSP) de scripts en Caddy y con SRI en las banderas. |
| H-19 | La imagen de la web corre nginx como *root* y sin chequeo de salud. | Bajo | Corregido | *nginx-unprivileged* en el puerto 8080 y chequeo de salud (AC-86). |
| H-20 | *OperationsService* concentra 617 líneas y cinco responsabilidades. | Bajo | Abierto | Deuda técnica. |
| H-21 | La web versiona 8,2 MB de imágenes sin optimizar. | Bajo | Parcial | Los fondos de la portada y del ingreso se sirven en WebP y se quitó una imagen sin uso (web *a117d7f*, *v1.1.2*); quedan imágenes por optimizar (deuda técnica). |
| H-22 | Sin issues ni tablero de seguimiento. | Medio | Abierto | ACO-30. |
| H-23 | El contrato OpenAPI solo existe en desarrollo. | Bajo | Parcial | Contratos OpenAPI en *vehicle-w-docs* (ramas *docs* y *main*, al día desde el 4 de noviembre de 2026); no se publican por servicio. |
| H-24 | La cancelación acepta un motivo y lo ignora. | Bajo | Corregido | AC-68; el evento *booking.cancelled* lleva además *reasonCode* (booking *62eea3a*). |
| H-25 | Los servicios nacen como commits únicos de 63 a 142 archivos. | Bajo | No se puede corregir | Es historial; desde entonces se aplica un commit por cambio lógico (las 128 ramas del 24 de octubre tienen un commit cada una). |

*Nota.* Elaborado por los autores a partir de la evaluación extremo a extremo del instructor (corte del 20 de octubre de 2026), de la revisión del sistema del 23 de octubre y de la auditoría de cierre del 24 de octubre de 2026.
