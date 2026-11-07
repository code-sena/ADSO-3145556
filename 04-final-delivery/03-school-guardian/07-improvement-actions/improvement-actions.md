# Acciones correctivas, preventivas y de mejoramiento del software
## Guardian Escolar

**Equipo de desarrollo:**  
Johan Smith Santamaria Fernández  
Sharik Dayanna Rojas Ibarra  
Juan Pablo Chala Ramírez  

**Institución:** Servicio Nacional de Aprendizaje (SENA)  
**Ficha de formación:** ADSO-3145556  
**Centro de formación:** Neiva, Huila  
**Fecha:** 6 de octubre de 2026  

---

## Control de versiones

| Versión | Fecha | Autor | Descripción |
|---|---|---|---|
| 1.1 | 11/10/2026 | Equipo Guardian Escolar | Revisión del contenido para registrar las acciones y decisiones reportadas por el equipo y retirar cifras y escenarios no confirmados. |

## 1. Introducción

Durante el desarrollo de Guardian Escolar, el equipo identificó situaciones que llevaron a modificar el diseño de las interfaces, ajustar las prácticas de desarrollo, reforzar la validación de datos, atender una exposición accidental de credenciales y tomar decisiones sobre las tecnologías utilizadas. También se recibieron propuestas que fueron evaluadas sin que necesariamente se convirtieran en cambios del sistema.

Este documento registra las acciones correctivas, preventivas y de mejoramiento comunicadas por el equipo. Su propósito es dejar constancia de los hallazgos, las decisiones tomadas, el estado informado y los aspectos que requieren verificación adicional. Los estados descritos se basan en la información reportada por el equipo y no sustituyen la evidencia técnica de pruebas, commits o revisiones.

## 2. Objetivos

- Documentar los principales problemas, correcciones, decisiones y mejoras ocurridos durante el desarrollo de Guardian Escolar.
- Diferenciar las correcciones realizadas de las propuestas que el equipo decidió no adoptar.
- Registrar el estado comunicado para cada acción sin declarar resultados que no hayan sido confirmados.
- Identificar las verificaciones pendientes que pueden contribuir a la seguridad, la calidad y el mantenimiento del software.

## 3. Alcance

El registro cubre cambios de diseño web, página principal, adaptación responsive, prácticas de Git, eliminación de datos simulados, seguridad de credenciales, validaciones de formularios, gestión de superadministradores, migración de la aplicación móvil, integración GPS y selección del motor de base de datos.

Este documento no pretende demostrar por sí solo que todas las funcionalidades del sistema están terminadas ni que todas las acciones cuentan con pruebas formales de eficacia. Cuando se necesita una comprobación adicional, se indica expresamente.

## 4. Criterios de clasificación

- **Acción correctiva:** atiende un problema o incumplimiento que ya se presentó.
- **Acción preventiva:** busca reducir la probabilidad de que un problema potencial ocurra o se repita.
- **Acción de mejoramiento:** introduce un cambio para elevar la calidad, la usabilidad, la mantenibilidad o la organización del desarrollo.
- **Propuesta evaluada y no adoptada:** registra una alternativa considerada por el equipo que no se implementó. No se clasifica como una corrección realizada.

Una misma acción puede contribuir tanto a corregir un problema como a prevenir su repetición. La clasificación indicada en la tabla describe el propósito principal del cambio.

## 5. Registro de acciones

| ID | Hallazgo o situación | Tipo | Acción o decisión | Estado informado |
|---|---|---|---|---|
| AC-01 | El diseño inicial utilizaba amarillo como color principal. En la primera revisión se solicitó cambiarlo a azul. | Mejoramiento | Se rediseñaron las interfaces y se adoptó el azul como color principal. | Corregido |
| AC-02 | La página principal tenía una presentación sencilla e incluía una sección con un video explicativo del programa. | Mejoramiento | Se rediseñó la página principal para darle una presentación más profesional y se eliminó la sección del video. | Corregido |
| AC-03 | Se sugirió hacer el seguimiento mediante un celular en lugar de seguir directamente al vehículo. | Propuesta evaluada; decisión de diseño | El equipo decidió mantener el enfoque de seguimiento del bus, porque el objetivo es conocer el recorrido del vehículo escolar y no únicamente la ubicación de una persona. | Propuesta no adoptada |
| AC-04 | Los mensajes de commits no seguían un criterio uniforme y algunos describían los cambios de forma demasiado general. | Mejoramiento del proceso | Se ajustaron los mensajes para redactarlos en inglés y describir los cambios con mayor precisión, usando una estructura más consistente. | Corregido en GitHub |
| AC-05 | Se seguían usando datos simulados (*mock data*) en una etapa en la que se requería avanzar hacia datos provenientes de los servicios reales. | Correctiva | Se eliminaron los datos simulados identificados y se avanzó hacia el uso de información obtenida mediante las API y los servicios correspondientes. | Corregido según lo informado; verificar el consumo real de datos en las funcionalidades afectadas |
| AC-06 | Se requerían mejoras responsive en los paneles de administrador y superadministrador, así como en las pantallas para recuperar o cambiar contraseñas y actualizar teléfono y correo electrónico. | Mejoramiento | Se realizaron ajustes de diseño y adaptación de las interfaces involucradas. | Corregido |
| AC-07 | Credenciales de configuración, incluidas credenciales de la base de datos, se publicaron accidentalmente en el historial de GitHub. | Correctiva y preventiva | Se cambiaron las credenciales expuestas y se agregó `.env` a `.gitignore` para evitar nuevas inclusiones accidentales. | Medidas realizadas; comprobar que los secretos antiguos no sigan utilizables y evaluar el historial publicado |
| AC-08 | Los formularios web y móviles permitían intentar enviar datos vacíos o con caracteres y formatos no adecuados. | Correctiva y preventiva | Se agregaron validaciones en el frontend y controles en el backend. | Corregido según lo informado |
| AC-09 | La gestión de las escuelas y la asignación de administradores presentaban dificultades en el planteamiento inicial. | Mejoramiento funcional | Se incorporó un apartado de superadministrador para centralizar las funciones correspondientes de gestión de escuelas y administradores. | Corregido |
| AC-10 | La implementación móvil inicial con .NET MAUI resultó difícil de manejar para el equipo. | Mejoramiento tecnológico | Se migró la aplicación móvil a React Native con Expo. | Migración realizada |
| AC-11 | El dispositivo IoT/GPS existía, pero su integración con el proyecto todavía no estaba implementada por completo. | Correctiva y de mejoramiento | Se avanzó en la integración del GPS con el backend y el dispositivo físico. | Parcialmente implementado; falta completar o verificar el recorrido en tiempo real |
| AC-12 | La propuesta inicial contemplaba PostgreSQL, pero el equipo decidió utilizar otro motor de base de datos. | Mejoramiento tecnológico | Se adoptó Microsoft SQL Server como motor de base de datos del proyecto. | Cambio realizado |

## 6. Descripción de las acciones

### 6.1. Rediseño de la interfaz y de la página principal

Durante la primera revisión del proyecto se solicitó cambiar el color principal de amarillo a azul. El cambio requirió rediseñar las interfaces para mantener una identidad visual coherente. En ese mismo proceso se rediseñó la página principal para conseguir una presentación más profesional y se retiró la sección del video explicativo.

### 6.2. Estandarización de los mensajes de commits

Se identificó que los mensajes de commits debían describir mejor los cambios realizados y mantener una convención uniforme. Se ajustó su estructura para utilizar inglés y mensajes más específicos. El propósito es facilitar la lectura del historial y la comprensión de los cambios realizados en el repositorio.

### 6.3. Retiro de datos simulados

Durante una etapa del desarrollo se utilizaron datos simulados para construir o probar interfaces. Posteriormente, se identificó que las funcionalidades afectadas debían avanzar hacia el uso de información real procedente de las API y los servicios correspondientes. El equipo informó que eliminó los datos simulados identificados. Para confirmar el cierre funcional, se debe comprobar que las pantallas y operaciones involucradas consuman efectivamente los datos reales y no conserven valores de demostración.

### 6.4. Seguridad de las credenciales

Se detectó la publicación accidental de credenciales de configuración, incluidas credenciales de la base de datos, en el historial de GitHub. El equipo informó que cambió las credenciales y agregó `.env` a `.gitignore`.

Estas medidas reducen el riesgo de que se utilicen las credenciales anteriores y ayudan a evitar que el archivo se incluya accidentalmente en nuevos commits. Sin embargo, `.gitignore` no elimina por sí solo los secretos de los commits anteriores. Como seguimiento de seguridad, debe comprobarse que las credenciales antiguas ya no sean válidas y evaluarse si es necesario retirar los secretos del historial publicado.

### 6.5. Validaciones de datos en web, móvil y backend

Se identificó que los formularios permitían intentar enviar campos vacíos o valores con formatos no adecuados. Se agregaron validaciones en el frontend y controles en el backend. La comprobación debe incluir entradas vacías, formatos inválidos y solicitudes directas al backend, dado que la validación visual del cliente no reemplaza la validación del servidor.

### 6.6. Incorporación del superadministrador

La gestión inicial de escuelas y la asignación de administradores presentaban dificultades. Para atender esta situación se incorporó el rol y apartado de superadministrador, con funciones de gestión acordes con el alcance definido para ese rol.

### 6.7. Migración de la aplicación móvil

La aplicación móvil se había comenzado con .NET MAUI. Debido a las dificultades que el equipo encontró para trabajar con esa tecnología, se decidió migrarla a React Native con Expo. El estado comunicado para esta acción es que la migración se realizó.

### 6.8. Integración del GPS

En una etapa inicial, el dispositivo GPS/IoT no estaba integrado por completo en el proyecto. El equipo informó que actualmente existe integración en el backend y con el dispositivo, pero que todavía falta completar o verificar el recorrido en tiempo real. Por ese motivo, esta acción se registra como parcialmente implementada y no como finalizada.

### 6.9. Cambio del motor de base de datos

La propuesta inicial contemplaba PostgreSQL. Posteriormente, el equipo decidió utilizar **Microsoft SQL Server**. La documentación técnica y la configuración del proyecto deben mantener este nombre de forma consistente. Microsoft SQL Server no debe confundirse con MySQL.

### 6.10. Propuesta de seguimiento mediante celular

Se sugirió utilizar un celular para realizar el seguimiento en lugar de ubicar el dispositivo en el vehículo. El equipo decidió no adoptar esa alternativa porque el propósito de Guardian Escolar es seguir la ruta del bus escolar. Esta entrada documenta una decisión de diseño, no una corrección implementada.

## 7. Seguimiento y verificación

Los siguientes puntos se conservan como verificaciones recomendadas. No significan que el equipo haya omitido las acciones descritas; sirven para comprobar su eficacia y mantener el documento alineado con el estado del proyecto.

| ID relacionado | Verificación recomendada | Criterio de comprobación |
|---|---|---|
| AC-05 | Revisar las funcionalidades en las que se eliminaron datos simulados. | La información visible y las operaciones provienen de las API o servicios correspondientes y no de valores de demostración. |
| AC-07 | Confirmar la rotación de credenciales y revisar el historial publicado. | Las credenciales anteriores ya no son válidas; se determina si hace falta retirar secretos del historial. |
| AC-08 | Probar formularios web y móviles, además de solicitudes directas al backend. | Los datos vacíos o inválidos son rechazados de forma apropiada tanto en cliente como en servidor. |
| AC-11 | Probar la recepción de posiciones y el recorrido del vehículo. | Se verifica el comportamiento del recorrido en tiempo real antes de marcar la integración como completa. |
| AC-12 | Contrastar la documentación con la configuración y los scripts del proyecto. | La tecnología documentada coincide con Microsoft SQL Server en el entorno correspondiente. |

Cuando se disponga de evidencia concreta, como commits, resultados de pruebas o capturas de la funcionalidad, puede agregarse al registro. No se deben inventar fechas de cierre, responsables, resultados de pruebas ni referencias de commits.

## 8. Conclusiones

Las acciones registradas reflejan cambios realizados en el diseño de las interfaces, las prácticas de control de versiones, el uso de datos, la seguridad, las validaciones, la gestión de roles, la tecnología móvil, el GPS y la base de datos de Guardian Escolar.

El registro diferencia los cambios comunicados como corregidos, las decisiones de diseño que no implicaron una modificación y las funcionalidades que todavía requieren completar o verificar. En particular, el seguimiento GPS permanece parcialmente implementado según la información disponible, y el incidente de credenciales requiere considerar que el archivo `.gitignore` no elimina los secretos de los commits anteriores.

El documento debe actualizarse cuando existan nuevas correcciones o evidencias de verificación, conservando la distinción entre acciones realizadas, propuestas no adoptadas y tareas pendientes.

---

## Anexo A. Evidencias que pueden asociarse a las acciones

| Tipo de evidencia | Uso |
|---|---|
| Commit o pull request | Mostrar el cambio asociado al diseño, validaciones, migración o configuración. |
| Captura de pantalla | Ilustrar el estado de una interfaz antes o después del ajuste. |
| Resultado de prueba | Confirmar validaciones, consumo de API o comportamiento del GPS. |
| Evidencia de rotación de credenciales | Confirmar que las credenciales expuestas fueron reemplazadas, sin publicar los nuevos secretos. |
| Configuración técnica | Confirmar la tecnología de base de datos utilizada. |

No se deben incluir contraseñas, tokens, cadenas de conexión con secretos ni otros datos sensibles en las evidencias.
