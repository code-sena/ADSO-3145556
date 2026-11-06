---
title: "Calidad y mejoras"
lang: es
documents: [TE-0157, TE-0156]
status: borrador
---
# Calidad y mejoras — Tu Evento

> Documento consolidado de Tu Evento. Reúne los documentos siguientes, cuyo contenido se conserva sin cambios; los metadatos de conversión de cada uno van en un comentario HTML al inicio de su sección.

**Contenido**

1. [Calidad de software](#doc-te-0157)
2. [Acciones correctivas, preventivas y de mejora](#doc-te-0156)

---

<a id="doc-te-0157"></a>

<!--
ruta_anterior: es/08_Mejora_Software/Calidad_de_Software.md
id: TE-0157
title: "Calidad de Software"
lang: es
source: 08_Mejora_Software/Calidad de Software.docx
source_sha256: 6f4ac1a414fcad4453913c3fb834f79a859197cf7d5e06dea9fa4d22078a5bf7
conversion: "pandoc 3.9 (-t gfm --wrap=none --extract-media) + extracción de encabezados/pies; sin revisión manual"
converted_on: 2026-10-23
fidelity: parcial
losses:
  - "El original no usa estilos de título; los títulos visuales quedan como texto en negrita, sin jerarquía #. Pendiente de marcar a mano."
  - "Encabezados y pies de página extraídos aparte (pandoc no los convierte); en el original se repiten en cada página. Los campos se muestran como {PAGE}, {NUMPAGES}."
status: borrador
reviewed_by: ""
-->

# Calidad de software

**Calidad De Software**

Lozano Contreras Cristofer David

Santos Ortiz Jhampier

Análisis y Desarrollo de Softwares

\(3145556\)

Servicio Nacional de Aprendizaje

Septiembre 24, 2025

**Introducción**

La calidad del software es un aspecto fundamental en el desarrollo de cualquier aplicación, ya que garantiza que el sistema cumpla con los requisitos planteados, sea confiable y ofrezca una experiencia satisfactoria a los usuarios. Documentar los errores encontrados y las soluciones aplicadas, tanto en la etapa de desarrollo como en la documentación técnica, es esencial para mejorar los procesos, prevenir fallos futuros y mantener la trazabilidad del proyecto.

En este caso, se presenta el aplicativo **“Tu Evento”**, un sistema diseñado para la maquetación de espacios logísticos y la gestión de reservaciones. Este tipo de herramienta requiere un alto nivel de calidad, dado que maneja información clave en la organización de eventos y depende de la precisión en la asignación de espacios y recursos. Por ello, la identificación, análisis y solución de problemas resultan cruciales para asegurar que el software no solo funcione correctamente, sino que también cumpla con los estándares de usabilidad, eficiencia y confiabilidad que los usuarios demandan.

**Registro de Errores**

1.  **Proceso en la documentación**

| **Descripción** | **Consecuencia** | **Solución** |
|----|----|----|
| Se encontraron errores ortográficos en algunos documentos. | Disminución de la calidad de los archivos entregados. | Se corrigieron los errores ortográficos con ayuda de herramientas como Quillbot. |
| Algunos documentos no seguían las normas APA. | Revisión y corrección repetida de documentos, con pérdida de tiempo. | Se ajustaron esos documentos a las normas APA. |
| El diagrama UML no incluía todos los atributos o métodos necesarios. | Fue necesario revisar y completar el diagrama UML. | Se revisó y completó el diagrama UML. |
| El rol de usuario anónimo no estaba descrito en el sistema. | Faltaba claridad en los permisos y funciones de este rol, lo que podía afectar la seguridad y usabilidad. | Se definieron y documentaron las funciones y limitaciones del rol anónimo. |
| Las restricciones del sistema no estaban descritas con el nivel de detalle necesario. | Existía riesgo de malinterpretaciones durante el desarrollo. | Se ampliaron las restricciones con explicaciones técnicas y ejemplos claros. |
| No se habían especificado los procedimientos para manejar situaciones anormales en el sistema. | Riesgo de errores no controlados que afectaran la experiencia del usuario | Se agregaron los casos de manejo de errores y validaciones en los requerimientos. |
| Los requerimientos no se habían revisado con suficiente detalle. | Posibles inconsistencias y omisiones que afectarían la calidad del software. | Se revisaron, corrigieron y complementaron los requerimientos funcionales y no funcionales. |
| Los criterios de aceptación no estaban bien definidos ni eran medibles. | Dificultad en las pruebas de validación del sistema. | Se ajustaron los criterios de aceptación con métricas claras y verificables. |
| El flujo de solicitud y aprobación de ser organizador no estaba descrito. | Podía generar confusión en el proceso y retrasos en la gestión de eventos. | Se agregó el requerimiento y el flujo correspondiente para solicitud y aprobación de organizadores. |

2.  **Proceso en el desarrollo.**

| **Descripción** | **Consecuencia** | **Solución** |
|----|----|----|
| El frontend intenta consumir APIs del backend, pero no tiene permisos de origen cruzado. | El navegador bloquea las peticiones y la app no puede comunicarse con el backend. | Configurar CORS en Spring Boot mediante un filtro global que permita los dominios autorizados. |
| El backend expone APIs que no coinciden con lo que espera el frontend. | Peticiones fallidas y pérdida de tiempo en pruebas. | Documentar APIs con Swagger y mantener sincronización entre frontend y backend. |
| Rutas mal configuradas en React Router (web) o React Navigation (mobile). | Pantallas en blanco o navegación incorrecta. | Configurar correctamente rutas y usar parámetros/stack según la arquitectura de la app. |
| No se valida correctamente el acceso a recursos según roles (admin, usuario, anónimo). | Usuarios acceden a información restringida. | Implementar Spring Security en el backend . |
| Mal manejo de promesas, fetch sin capturar errores (.catch). | El usuario no recibe retroalimentación cuando algo falla. | Manejar correctamente errores de red con try/catch y mostrar alertas o modales en el frontend. |
| Reglas de seguridad demasiado abiertas o restrictivas. | Acceso no autorizado o bloqueo de funciones legítimas. | Definir roles, rutas seguras y probar con diferentes usuarios. |


---

## Encabezados y pies de página del original

- **Encabezado** (`word/header1.xml`): texto «0»<br>![Encabezado del original](https://raw.githubusercontent.com/TheJhampierBoy/TuEvento-Docs/main/_assets/08_Mejora_Software/Calidad_de_Software/header_footer/image1.png)

---

<a id="doc-te-0156"></a>

<!--
ruta_anterior: es/08_Mejora_Software/Acciones_Correctivas_Preventivas_Mejora_TuEvento.md
id: TE-0156
title: "Acciones_Correctivas_Preventivas_Mejora_TuEvento"
lang: es
source: 08_Mejora_Software/Acciones_Correctivas_Preventivas_Mejora_TuEvento.pdf
source_sha256: e8b79ac2c0e213e1a21375f61e23bff17ec158db8cb0fc08cefea160185abbab
conversion: "pdfplumber 0.11.10 + conversor de PDF sin etiquetar (método C, piloto 3); sin revisión manual"
converted_on: 2026-10-23
fidelity: pendiente_revision
losses:
  - "Cabecera o pie de página omitido en las páginas 2–21: «TuEvento - Acciones correctivas, preventivas y de mejoramiento»."
  - "Cabecera o pie de página omitido en las páginas 2–21: «{n}»."
  - "Tablas partidas entre páginas unidas en una sola: 5→7; 8→9."
  - "Fila de encabezado repetida por salto de página omitida en las páginas 6–7."
  - "PDF generado sin LaTeX: los guiones al final de línea son guiones reales y se conservan al unir las líneas (no son guiones de corte silábico)."
  - "Índice manual (páginas 2): se conservan los números de página del original y se quitan las líneas guía."
  - "Portada: líneas destacadas que no son el título se escriben como párrafos: «Servicio Nacional de Aprendizaje (SENA)»; «Documentación de acciones correctivas, preventivas y de mejoramiento del software»; «Autores»; «Instructor»."
  - "Tablas con celdas combinadas en HTML (colspan/rowspan calculados por la geometría de los bordes); no se conservan anchos de columna, fondos ni grosor de bordes."
  - "Se conservan negritas, cursivas y fuente monoespaciada; no se conservan tamaños, colores, alineación ni sangrías."
  - "Verificación automática frente a pdftotext: cobertura 99.76%, precisión 100.00%, orden 99.76%; faltan {'Estado': 2, 'Tipo': 2, 'ID': 2, 'Módulo': 2, 'Problema': 2, 'Evidencia': 2} (explicado arriba)."
  - "Diferencias de la verificación explicadas: las palabras que «faltan» (Estado, Tipo, ID, Módulo, Problema, Evidencia…) son las filas de encabezado repetidas por salto de página, omitidas al unir las tablas."
status: borrador
reviewed_by: ""
-->

# Acciones correctivas, preventivas y de mejora

**Servicio Nacional de Aprendizaje (SENA)**

Análisis y Desarrollo de Software (ADSO)

Ficha 3145556

# TuEvento

*Documentación de acciones correctivas, preventivas y de mejoramiento del software*

**Autores**

Jhampier Santos Ortiz

Cristofer David Lozano Contreras

**Instructor**

Carlos Julio Cadena

Octubre de 2026

## Tabla de contenido

1. Introducción 2

2. Alcance y metodología 2

3. Definiciones y clasificación 2

4. Resumen general 3

5. Tabla resumen de acciones 4

6. Detalle de las acciones 7

7. Resumen de mejora continua 19

8. Plan de acción sobre los casos abiertos 20

9. Conclusiones 20

## 1. Introducción

Este documento registra las acciones correctivas, preventivas y de mejora aplicadas, en proceso o pendientes sobre el sistema TuEvento durante su desarrollo. Su propósito es dejar trazabilidad de cada no conformidad o riesgo detectado, la causa identificada, la acción tomada, la evidencia que la respalda y el estado actual, de modo que el proceso de mejora continua del software sea verificable.

## 2. Alcance y metodología

El análisis abarca los repositorios del backend (Spring Boot), el frontend web (React y Vite), la aplicación móvil (React Native y Expo) y los servicios auxiliares (fake-payment-gateway y nsfw-service), así como la infraestructura de integración y despliegue (GitHub Actions, SonarQube Cloud, Docker y Kubernetes).

Las acciones se identificaron a partir de las siguientes fuentes: historial de git de los repositorios (commits, ramas y reversiones), CHANGELOG.md, docs/EVIDENCIAS.md, lista de chequeo, documento técnico del pipeline, manuales técnicos, comentarios TODO en el código, migraciones de base de datos y pruebas existentes. Cada caso se verificó contra el repositorio; los campos marcados como "No evidenciado" corresponden a datos que no pudieron comprobarse en las fuentes.

## 3. Definiciones y clasificación

| **Término** | **Definición** |
|---|---|
| Acción correctiva | Acción orientada a eliminar la causa de una no conformidad o de un error ya ocurrido, para evitar que se repita. |
| Acción preventiva | Acción orientada a eliminar la causa de una no conformidad potencial o de un riesgo detectado antes de que cause un fallo. |
| Acción de mejora | Acción orientada a aumentar el rendimiento, la calidad o la coherencia del producto y su documentación. |
| Estado: Cerrada | La acción fue ejecutada y existe evidencia de su aplicación. |
| Estado: En proceso | La acción se inició, pero no se ha completado ni verificado. |
| Estado: Pendiente | La acción fue identificada, pero aún no se ha ejecutado. |

## 4. Resumen general

Se documentaron 26 acciones: 16 correctivas, 5 preventivas y 5 de mejora. De ellas, 20 están cerradas, 1 en proceso y 5 pendientes.

### 4.1 Por tipo de acción

| **Tipo** | **Cantidad** | **IDs** |
|---|---|---|
| Correctiva | 16 | AC-001, AC-002, AC-003, AC-004, AC-005, AC-006, AC-007, AC-008, AC-009, AC-010, AC-011, AC-012, AC-013, AC-014, AC-015, AC-026 |
| Preventiva | 5 | AC-016, AC-017, AC-018, AC-019, AC-020 |
| Mejora | 5 | AC-021, AC-022, AC-023, AC-024, AC-025 |

### 4.2 Por estado

| **Estado** | **Cantidad** | **IDs** |
|---|---|---|
| Cerrada | 20 | AC-001, AC-002, AC-003, AC-004, AC-005, AC-006, AC-007, AC-008, AC-009, AC-010, AC-011, AC-012, AC-013, AC-014, AC-015, AC-016, AC-018, AC-019, AC-021, AC-022 |
| En proceso | 1 | AC-024 |
| Pendiente | 5 | AC-017, AC-020, AC-023, AC-025, AC-026 |

## 5. Tabla resumen de acciones

| **ID** | **Tipo** | **Módulo** | **Problema** | **Estado** | **Evidencia** |
|---|---|---|---|---|---|
| AC-001 | Correctiva | Backend - Seguridad | Siete vulnerabilidades de autenticación (activación sin validar email, enumeración de usuarios, logout sin ownership, entre otras). | Cerrada | Commit 91d4ada: fix(security): fix critical security vulnerabilities and improve exception handling. |
| AC-002 | Correctiva | Backend - Seguridad | RegisterUserUseCase y OauthLoginUseCase asignaban el rol ATTENDEE, inexistente; el usuario quedaba sin rol efectivo. | Cerrada | Commits 688c187 y bff4cee; registrados en el CHANGELOG. |
| AC-003 | Correctiva | Backend - Seguridad | Los endpoints con hasAuthority('ADMIN') devolvían 403 por el prefijo ROLE_ en las authorities. | Cerrada | Commit f8464f3: fix(security): resolve 403 on admin endpoints by fixing JWT authority extraction. |
| AC-004 | Correctiva | Backend - Seguridad | El bloqueo tras intentos fallidos no persistía y el desbloqueo automático no funcionaba. | Cerrada | Commits d8a3dbd y c8860ea. |
| AC-005 | Correctiva | Backend - Seguridad (OAuth) | Facebook podía no entregar email y se intentaba guardar NULL, violando una restricción NOT NULL. | Cerrada | Commit 311f244: fix: OAuth Facebook null email fallback and secure redirect whitelist. |
| AC-006 | Correctiva | Backend - Pagos | La pasarela sandbox y el backend firmaban distinto el webhook; todos los webhooks eran rechazados. | Cerrada | Commit c835d12: fix(payment): align webhook HMAC signature encoding between fake-gateway and backend. |
| AC-007 | Correctiva | Backend - Tickets y Asientos | Tras un pago fallido o cancelado, los asientos quedaban en RESERVED indefinidamente. | Cerrada | Commit ab29589: fix(ticket,seat): sync seat status with payment outcome. |
| AC-008 | Correctiva | Base de datos - Asientos | Las restricciones CHECK usaban minúsculas pero JPA guarda en MAYÚSCULAS; cada inserción violaba la restricción. | Cerrada | Migración 073-fix-seat-l og-status-constraints.ya ml, incorporada en el commit ab29589. |
| AC-009 | Correctiva | Base de datos - Comentarios | El código usaba reply_to_user_id y reply_to_user_name sin migración; la aplicación fallaba al arrancar. | Cerrada | Commit a5ea593: fix(comments): add missing Liquibase migration 090 for reply_to fields. |
| AC-010 | Correctiva | Frontend web - Comentarios | El mensaje WebSocket llegaba antes de que el POST resolviera, insertando el mismo comentario dos veces. | Cerrada | Commit 5538ec4 (documentado en el CHANGELOG). |
| AC-011 | Correctiva | Frontend web - Imágenes de eventos | Las dimensiones del canvas resultaban NaN y generaban imágenes en blanco o errores al subir. | Cerrada | Commits c663779 y 688cb70 (rama fix/event -media-zoom-crop). |
| AC-012 | Correctiva | Frontend móvil - Mapa de asientos | Asientos cortados en los bordes, zoom y centrado incorrectos, y setState dentro del render. | Cerrada | Commits baad416, 556ce89, 45e4b0c, e2f2309 y 90df241. |
| AC-013 | Correctiva | Servicio NSFW / Docker | El paquete libgl1-mesa-glx ya no existe en Debian Bookworm y fallaba la construcción de la imagen. | Cerrada | Commit d473708: fix(storage): fix OpenNSFW2 tensorflow compatibility and moderation cascade. |
| AC-014 | Correctiva | CI/CD - Jenkins | El pipeline salta las pruebas con -Dmaven.test.skip=true, contradiciendo el documento técnico del pipeline. | Cerrada | Commits c026dc0, 37e84ec, 1b7c4d9 y 4b12d99; commit aa93102 (PR #93): .github/workflows/ci.yml ejecuta ./mvnw -B verify. |
| AC-015 | Correctiva | Backend - Configuración general | El merge de Feature/login introdujo regresiones en la configuración y fue revertido. | Cerrada | Commit e7a4f29: Revert 'Feature/login' (PR #12). |
| AC-016 | Preventiva | Base de datos - Comentarios | rating NOT NULL impedía comentarios adicionales y respuestas sin estrellas. | Cerrada | Migración 087-event-rati ng-allow-multiple-nullab le-rating.yaml. |
| AC-017 | Preventiva | Backend - Seguridad | LOCKOUT_MINUTES = 1 es un valor de desarrollo; en producción deberían ser 30 minutos. | Pendiente | LoginUseCase.java, línea 34; manual del administrador, sección 4.5. |
| AC-018 | Preventiva | Base de datos - Perfil | full_name definido con 100 caracteres mientras el dominio valida un máximo de 30. | Cerrada | Migración 063-shrink-fu ll-name-to-30.yaml. |
| AC-019 | Preventiva | CI/CD - SonarQube | El Quality Gate está documentado como obligatorio, pero el stage de SonarQube solo ejecuta un echo. | Cerrada | Commit 4b12d99; commit aa93102 (PR #93): análisis en SonarQube Cloud desde .github/workflows/ci.yml. |
| AC-020 | Preventiva | Backend - Seguridad | La limpieza de tokens corre una vez al día; el TODO indica cada 30 minutos en producción. | Pendiente | SecurityDataCleanupTas k.java, línea 52; commit 126e1a9: feat(security): add scheduled cleanup task for expired tokens and codes. |
| AC-021 | Mejora | Rendimiento - Frontend móvil | El render con un círculo por asiento bloqueaba la aplicación en recintos muy grandes. | Cerrada | Commit cb31723: perf(mobile): implement DOD for 100k+ seats with TypedArrays and batch rendering. |
| AC-022 | Mejora | Rendimiento - Backend / Web | El listado disparaba una consulta adicional por evento para obtener su categoría. | Cerrada | Commit f61cf52: refactor(events): use embedded categoryId instead of N+1 fetch. |
| AC-023 | Mejora | Documentación - Base de datos | Algunos documentos citan Flyway, pero el proyecto usa exclusivamente Liquibase. | Pendiente | pom.xml, líneas 78-80; directorio db/changelog/changes/. |
| AC-024 | Mejora | Backend - Pagos | Wompi figura como pasarela real, pero solo existe el valor de enum; los pagos usan la pasarela sandbox. | En proceso | PaymentGateway.java, línea 8; ausencia de Wo mpiPaymentGatewayAd apter. |
| AC-025 | Mejora | Backend - Mensajería | La dependencia spring-kafka está en el pom.xml, pero no hay producers, consumers ni topics. | Pendiente | pom.xml, líneas 82-83 y 150-151; búsqueda en src/main/java sin resultados. |
| AC-026 | Correctiva | Backend / Frontend - Idiomas | El soporte multilenguaje exigido por el SRS no está implementado del todo. | Pendiente | Declaración del equipo de desarrollo; módulo language del backend sin pruebas automatizadas. |

## 6. Detalle de las acciones

### 6.1 Acciones correctivas

<table>
<tr>
<td colspan="2"><strong>AC-001</strong> Vulnerabilidades críticas de autenticación <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Backend - Seguridad</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>Se identificaron 7 vulnerabilidades: (a) código de activación no validado contra el email, (b) código de recuperación no validado contra el email, (c) logout sin verificar la propiedad de la sesión, (d) login sin verificar estados BLOCKED/INACTIVE/DELETED, (e) mensaje de bloqueo que exponía la marca de tiempo exacta, (f) recuperación de contraseña que revelaba si el email existía, (g) refresh token sin validar que el usuario siguiera activo.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Implementación inicial del módulo de seguridad enfocada en el flujo feliz, sin una revisión de vulnerabilidades tipo OWASP.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Corrección en 11 casos de uso de seguridad, mejora del GlobalExceptionHandler, homogenización de mensajes para evitar enumeración y validación de email en activación y recuperación.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commit 91d4ada: fix(security): fix critical security vulnerabilities and improve exception handling.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-04-13</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Cristofer Lozano</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>El commit modifica 11 casos de uso. No existe un test automatizado que cubra estos flujos de seguridad negativos.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-002</strong> Rol inexistente asignado al registrar usuarios <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Backend - Seguridad</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>Los casos de uso RegisterUserUseCase y OauthLoginUseCase asignaban el código de rol ATTENDEE, que no existe en la tabla de roles. Los usuarios registrados no obtenían un rol efectivo.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>El rol cambió de nombre de ATTENDEE a USER durante el diseño y el código no se actualizó.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Se corrigió el código de rol a USER en ambos casos de uso. Queda pendiente actualizar los diagramas de clase que aún muestran ATTENDEE.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commits 688c187 y bff4cee; registrados en el CHANGELOG.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-03-28</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Cristofer Lozano</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>OauthLoginUseCaseOnboardingTest cubre el flujo de OAuth; no hay un test explícito del rol asignado tras el registro.</td>
</tr>
<tr>
<td colspan="2"><strong>AC-003</strong> Error 403 en endpoints de administrador <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Backend - Seguridad</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>Los endpoints protegidos con hasAuthority('ADMIN') devolvían 403 porque SecurityUser registraba la authority como ROLE_ADMIN. Además, el filtro JWT usaba un parser manual en Base64 en lugar de JJWT.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Confusión entre hasRole() (agrega prefijo) y hasAuthority() (sin prefijo) en Spring Security 6.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Se eliminó el prefijo ROLE_, se reescribió el filtro JWT con TokenGeneratorPort y se añadió @EnableMethodSecurity en SecurityConfig.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commit f8464f3: fix(security): resolve 403 on admin endpoints by fixing JWT authority extraction.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-03-28</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Cristofer Lozano</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>Los endpoints /api/v1/admin/** operan con el rol ADMIN según el manual del administrador.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-004</strong> Bloqueo de cuenta sin persistencia transaccional <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Backend - Seguridad</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>El bloqueo automático tras 5 intentos fallidos no se guardaba por un problema de propagación transaccional en AccountLockoutService. El desbloqueo automático por expiración tampoco funcionaba.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Propagación REQUIRES_NEW mal aplicada y un job de desbloqueo que no consultaba los bloqueos expirados.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Se corrigió el flujo transaccional, se creó un job @Scheduled cada minuto para expirar bloqueos y el login ahora verifica el bloqueo antes de evaluar el estado del usuario.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commits d8a3dbd y c8860ea.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-04-14 / 2026-04-15</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Cristofer Lozano</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>El comportamiento está documentado en la sección 4.5 del manual del administrador.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-005</strong> Email nulo en OAuth con Facebook <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Backend - Seguridad (OAuth)</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>Facebook no siempre devuelve el email (el usuario puede no conceder el permiso). El código intentaba guardar NULL en oauth_account.email, violando la restricción NOT NULL y lanzando una excepción no controlada.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>La rama de cuenta vinculada no tenía el mismo fallback sintético que la rama de cuenta nueva.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Se agregó un email sintético providerUserId@facebook.oauth cuando el email es nulo o vacío.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commit 311f244: fix: OAuth Facebook null email fallback and secure redirect whitelist.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-09-21</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Jhampier Santos</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>No evidenciado un test específico para este caso.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-006</strong> Firma HMAC del webhook inconsistente <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Backend - Pagos</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>La pasarela sandbox firmaba el webhook con un encoding distinto al que el backend esperaba para verificar HMAC-SHA256. Todos los webhooks de pago llegaban con firma inválida y los pagos no se completaban.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Implementaciones independientes del gateway y del backend sin un contrato compartido de encoding.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Se alineó el algoritmo HMAC-SHA256 con HexFormat y StandardCharsets.UTF_8 en ambos lados.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commit c835d12: fix(payment): align webhook HMAC signature encoding between fake-gateway and backend.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-09-21</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Cristofer Lozano</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>No evidenciado un test que valide la firma HMAC.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-007</strong> Asientos no sincronizados con el resultado del pago <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Backend - Tickets y Asientos</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>Cuando un pago fallaba o era cancelado, los asientos permanecían en estado RESERVED, bloqueando su disponibilidad para otros compradores.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>La lógica de transición de asientos no contemplaba el flujo de pago rechazado o cancelado.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>SeatService ahora transiciona los asientos a AVAILABLE cuando el pago no es aprobado.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commit ab29589: fix(ticket,seat): sync seat status with payment outcome.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-09-21</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Cristofer Lozano</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>Sin test unitario específico; verificado funcionalmente en el checkout mejorado (commit e5209e6).</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-008</strong> Restricciones de seat_log con diferencia de mayúsculas <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Base de datos - Asientos</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>Las restricciones CHECK de seat_log usaban valores en minúsculas, pero JPA con EnumType.STRING persiste en MAYÚSCULAS. Cada inserción en seat_log violaba la restricción.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Las migraciones iniciales se escribieron en minúsculas sin considerar el comportamiento de EnumType.STRING.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>La migración 073 elimina las restricciones antiguas, actualiza los datos existentes a MAYÚSCULAS y recrea las restricciones correctas.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Migración 073-fix-seat-log-status-constraints.yaml, incorporada en el commit ab29589.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-09-21</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Cristofer Lozano</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>Las migraciones Liquibase se ejecutan al arranque; si la corrección fallara, el servidor no iniciaría.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-009</strong> Migración faltante para columnas reply_to <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Base de datos - Comentarios</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>El módulo de comentarios usaba las columnas reply_to_user_id y reply_to_user_name en entidades, DTOs y consultas, pero la migración que las crea no existía. La aplicación fallaba al arrancar.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>La migración 090 fue omitida en la rama de desarrollo y no se detectó hasta el intento de despliegue.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Se creó de forma retroactiva la migración 090-event-rating-add-reply-to-fields.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commit a5ea593: fix(comments): add missing Liquibase migration 090 for reply_to fields.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-10-07</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Jhampier Santos</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>El servidor arranca correctamente y las columnas están disponibles para el código que las usa.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-010</strong> Comentarios duplicados por condición de carrera <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Frontend web - Comentarios</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>Al publicar un comentario, el mensaje WebSocket llegaba antes de que el POST resolviera. El identificador aún no estaba en commentIdsRef, así que el manejador WS insertaba el comentario y luego el POST lo insertaba de nuevo, generando dos elementos con la misma clave y dos formularios de edición abiertos.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Actualización del estado local sin idempotencia; la referencia de IDs conocidos solo se actualizaba después del POST.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Guarda de idempotencia en todos los setComments, segunda guarda en el manejador WS y unión (en lugar de reemplazo) de commentIdsRef al recargar por REST.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commit 5538ec4 (documentado en el CHANGELOG).</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-10-08</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Jhampier Santos</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>No existen tests automatizados de frontend; verificación solo funcional.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-011</strong> Canvas de normalización de imagen devolvía NaN <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Frontend web - Imágenes de eventos</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>Al normalizar imágenes para la subida de eventos, las dimensiones del canvas resultaban NaN, generando una imagen en blanco o errores al subir.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Se usaba .map() como referencia de función en lugar de acceder al atributo correcto y URL.revokeObjectURL se llamaba antes de que toBlob terminara.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Se corrigió el acceso al atributo y se movió revokeObjectURL al callback de toBlob.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commits c663779 y 688cb70 (rama fix/event-media-zoom-crop).</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-09-22</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Jhampier Santos</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>No evidenciado</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-012</strong> Recorte visual y navegación incorrecta del mapa de asientos <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Frontend móvil - Mapa de asientos</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>Los asientos en los bordes de la vista se cortaban, el zoom de la vista general no centraba correctamente y se llamaba setState dentro del render, provocando re-renderizados.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Dimensionamiento fijo sin considerar márgenes, bounding box calculado sobre la sección completa y lógica de estado dentro del ciclo de render.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Dimensionamiento adaptativo con márgenes, bounding box por asiento real, movimiento de setState fuera del render y corrección de la paginación en secciones irregulares.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commits baad416, 556ce89, 45e4b0c, e2f2309 y 90df241.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-09-07 al 2026-09-15</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Cristofer Lozano</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>No evidenciado</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-013</strong> Fallo al construir la imagen del clasificador NSFW <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Servicio NSFW / Docker</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>El paquete libgl1-mesa-glx fue eliminado en Debian Bookworm y el Dockerfile del servicio NSFW fallaba durante apt-get install.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Cambio en las dependencias del sistema operativo base de la imagen Docker, sin validar compatibilidad al actualizar.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Se reemplazó libgl1-mesa-glx por libgl1 en el Dockerfile.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commit d473708: fix(storage): fix OpenNSFW2 tensorflow compatibility and moderation cascade.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-05-14</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Cristofer Lozano</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>La imagen se construye correctamente en el pipeline tras la corrección.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-014</strong> Pruebas unitarias deshabilitadas en el pipeline <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>CI/CD - Jenkins</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>El documento técnico del pipeline declara que el Stage 3 ejecuta pruebas unitarias y que un Quality Gate exige cobertura mayor al 80%. El Jenkinsfile actual usa -Dmaven.test.skip=true en el stage Unit Tests, por lo que las pruebas no se ejecutan.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>La base de datos PostgreSQL no era accesible desde el contenedor Jenkins (Docker-in-Docker), por lo que se deshabilitaron las pruebas temporalmente.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Varios commits iteraron sin éxito en la configuración de Testcontainers y Docker-in-Docker; como solución temporal se deshabilitaron las pruebas. Después, el pipeline de Jenkins se desactivó y se reemplazó por GitHub Actions, cuyo trabajo de backend ejecuta las pruebas con ./mvnw -B verify.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commits c026dc0, 37e84ec, 1b7c4d9 y 4b12d99; commit aa93102 (PR #93): .github/workflows/ci.yml.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-05-05 (inicio); 2026-10-23 (cierre)</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Jhampier Santos; Cristofer Lozano (cierre)</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>Los 13 archivos de prueba del backend se ejecutan en cada push o pull request; la ejecución del flujo CI en la rama develop (commit 33cd5ab) terminó con éxito.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-015</strong> Reversión del merge de Feature/login <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Backend - Configuración general</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>El merge de la rama Feature/login introdujo regresiones en la configuración general (pom.xml, application.yml, variables de entorno) y fue revertido por completo.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Se ejecutó git revert del merge y se reintegró el módulo de seguridad en ramas separadas con revisión.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commit e7a4f29: Revert 'Feature/login' (PR #12).</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-04-09</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Cristofer Lozano</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>El módulo de seguridad fue reintegrado correctamente en commits posteriores.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-026</strong> Módulo de idiomas (multilenguaje) incompleto frente al SRS <strong>Pendiente</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Backend / Frontend - Idiomas</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>El SRS exige soporte de múltiples idiomas, pero esta funcionalidad no está implementada por completo. El backend cuenta con un módulo language, que no tiene pruebas automatizadas.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Priorización de otras funcionalidades durante el desarrollo.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Pendiente de ejecución: completar la funcionalidad de idiomas conforme a los requisitos del SRS.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Declaración del equipo de desarrollo; módulo language del backend sin pruebas automatizadas.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>Pendiente: validar el cumplimiento contra los requisitos funcionales del SRS y agregar pruebas.</td>
</tr>
</table>

### 6.2 Acciones preventivas

<table>
<tr>
<td colspan="2"><strong>AC-016</strong> Modelo de calificación no permitía comentarios múltiples <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Base de datos - Comentarios</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>event_rating tenía rating NOT NULL entre 1 y 5, lo que obligaba a incluir calificación en cada comentario. La regla de negocio evolucionó: solo el primer comentario lleva estrellas.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Anticipación insuficiente del modelo de comentarios múltiples y respuestas durante el diseño inicial de la base de datos.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>La migración 087 hace rating nullable, actualiza el CHECK a rating IS NULL OR rating BETWEEN 1 AND 5 y añade un índice compuesto para el control antispam.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Migración 087-event-rating-allow-multiple-nullable-rating.yaml.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>No evidenciado (autor del changeset: capysoft)</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>AddEventRatingServiceTest incluye 18 casos que cubren las reglas de calificación, antispam, rol organizador y respuestas.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-017</strong> Tiempo de bloqueo de cuenta de 1 minuto <strong>Pendiente</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Backend - Seguridad</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>La constante LOCKOUT_MINUTES = 1 está configurada para desarrollo. En producción debería ser de 30 minutos según el manual del administrador (sección 4.5). Si se despliega sin cambio, la protección contra fuerza bruta es mínima.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Valor reducido para facilitar el desarrollo y las pruebas.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>TODO documentado en el código (restaurar a 30 para producción). Aún no se ha modificado.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>LoginUseCase.java, línea 34; manual del administrador, sección 4.5.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>Pendiente: externalizar el valor a una variable de entorno o perfil de configuración.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-018</strong> Inconsistencia entre VARCHAR(100) y la validación de 30 caracteres <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Base de datos - Perfil</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>full_name estaba definido como VARCHAR(100), pero la validación de dominio limita el nombre completo a 30 caracteres. El esquema no reflejaba la regla de negocio.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>La restricción de 30 caracteres se definió durante el desarrollo sin actualizar la migración original.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>La migración 063-shrink-full-name-to-30 reduce la columna a VARCHAR(30).</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Migración 063-shrink-full-name-to-30.yaml.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>No evidenciado (autor del changeset: capysoft)</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>La migración documenta la validación previa: ninguna fila existente superaba los 30 caracteres (la más larga tenía 21).</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-019</strong> Análisis de calidad (SonarQube) deshabilitado <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>CI/CD - SonarQube</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>PIPELINE-TECHNICAL-DOCUMENT.md describe un Quality Gate con cobertura mayor al 80%, 0 bugs críticos y 0 vulnerabilidades. El Jenkinsfile reemplaza el stage de SonarQube por un echo.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>El servidor SonarQube no estaba disponible de manera consistente en el entorno de CI.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Stage deshabilitado; el archivo sonar-project.properties existe en el repositorio como preparación. Después, el análisis se trasladó a SonarQube Cloud, ejecutado desde GitHub Actions con el archivo sonar-project.properties de la raíz del repositorio.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commit 4b12d99; commit aa93102 (PR #93): .github/workflows/ci.yml y sonar-project.properties.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-05-05; 2026-10-23 (cierre)</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Jhampier Santos; Cristofer Lozano (cierre)</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>El análisis de SonarQube Cloud se ejecuta en el trabajo de backend del flujo CI; la ejecución en la rama develop (commit 33cd5ab) terminó con éxito. Resultados: https://sonarcloud.io/project/overview?id=lozano2303_TuEvento.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-020</strong> Frecuencia de limpieza de tokens inadecuada para producción <strong>Pendiente</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Backend - Seguridad</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>El cron 0 0 2 * * * limpia tokens expirados solo una vez al día (2 AM). En producción pueden acumularse tokens revocados y códigos expirados durante horas. El TODO indica cambiarlo a 0 0/30 * * * *.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Valor de desarrollo elegido para reducir ruido en los registros durante las pruebas.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>TODO documentado en el código. Aún no se ha modificado.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>SecurityDataCleanupTask.java, línea 52; commit 126e1a9: feat(security): add scheduled cleanup task for expired tokens and codes.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-03-28</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Cristofer Lozano</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>Pendiente: externalizar el cron a una variable de entorno o perfil de Kubernetes.</td>
</tr>
</table>

### 6.3 Acciones de mejora

<table>
<tr>
<td colspan="2"><strong>AC-021</strong> Rendimiento del mapa de asientos con más de 100.000 asientos <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Rendimiento - Frontend móvil</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>El canvas de selección de asientos se bloqueaba con recintos de más de 100.000 asientos al renderizar un círculo individual por asiento.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Arquitectura orientada a objetos para datos que requieren renderizado masivo.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Patrón Data-Oriented Design: datos de asientos en TypedArrays (Float32Array, Uint8Array) y sceneFunc para renderizado por lotes con Skia.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commit cb31723: perf(mobile): implement DOD for 100k+ seats with TypedArrays and batch rendering.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-09-16</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Cristofer Lozano</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>No evidenciado un benchmark; la mejora está descrita en el mensaje del commit.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-022</strong> Consulta N+1 en el listado de eventos <strong>Cerrada</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Rendimiento - Backend / Web</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>El listado de eventos disparaba una consulta adicional por cada evento para obtener su categoría.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Uso de una relación JPA perezosa para la categoría sin fetch join.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Se usa el categoryId embebido en la entidad en lugar de navegar la relación.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>Commit f61cf52: refactor(events): use embedded categoryId instead of N+1 fetch.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>2026-08-04</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>Cristofer Lozano</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>No evidenciado un benchmark ni una prueba de rendimiento.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-023</strong> Inconsistencia documental entre Flyway y Liquibase <strong>Pendiente</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Documentación - Base de datos</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>Algunos documentos mencionaban Flyway como gestor de migraciones, cuando el proyecto usa exclusivamente Liquibase. Esto puede causar confusión al configurar entornos de despliegue.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>Inconsistencia documental heredada de versiones tempranas del manual técnico.</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Se verificó que el proyecto usa Liquibase (liquibase-core en pom.xml y 97 archivos de cambios YAML) y que no existe dependencia de Flyway. Falta corregir los documentos que mencionen Flyway.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>pom.xml, líneas 78-80; directorio db/changelog/changes/.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>Pendiente: revisar overview.md, PIPELINE-TECHNICAL-DOCUMENT.md y el manual técnico.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-024</strong> Pasarela Wompi documentada pero sin implementar <strong>En proceso</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Backend - Pagos</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>overview.md, el manual técnico y la propuesta técnica documentan Wompi como pasarela de pago real. En el código solo existe WOMPI como valor de enum en PaymentGateway.java (comentario: gateway real sin implementar todavía). Todos los pagos usan fake-payment-gateway.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>Se definió que la plataforma usa la pasarela de pagos sandbox fake-payment-gateway, para pruebas del flujo de pago y conectada al backend, en lugar de Wompi, y se ajustó la documentación del proyecto.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>PaymentGateway.java, línea 8; ausencia de WompiPaymentGatewayAdapter; FakePaymentGatewayAdapter.java y application-dev.yml (payment.gateway, valor por defecto fake).</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>Pendiente: el README del repositorio de código y los textos de términos y privacidad de la web y la app móvil aún mencionan Wompi.</td>
</tr>
</table>

<table>
<tr>
<td colspan="2"><strong>AC-025</strong> Apache Kafka declarado pero sin implementar <strong>Pendiente</strong></td>
</tr>
<tr>
<td><strong>Módulo / componente</strong></td>
<td>Backend - Mensajería</td>
</tr>
<tr>
<td><strong>Problema o no</strong> <strong>conformidad</strong></td>
<td>overview.md y el manual técnico listan Spring Kafka como tecnología de mensajería. La dependencia está declarada en pom.xml, pero no existe ningún producer, consumer ni topic configurado en el código.</td>
</tr>
<tr>
<td><strong>Causa raíz</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Acción tomada</strong></td>
<td>No evidenciado ningún commit de implementación.</td>
</tr>
<tr>
<td><strong>Evidencia</strong></td>
<td>pom.xml, líneas 82-83 y 150-151; búsqueda en src/main/java sin resultados.</td>
</tr>
<tr>
<td><strong>Fecha</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Responsable</strong></td>
<td>No evidenciado</td>
</tr>
<tr>
<td><strong>Verificación de</strong> <strong>efectividad</strong></td>
<td>Pendiente: verificar en el SRS si la mensajería asíncrona es un requisito; si lo es, debe implementarse; si no, retirar la dependencia y ajustar la documentación.</td>
</tr>
</table>

## 7. Resumen de mejora continua

El historial del proyecto muestra una evolución clara en cuatro dimensiones.

**Seguridad.** Abril concentra la mayor densidad de correcciones: las vulnerabilidades críticas de autenticación (enumeración de usuarios, activación sin validar el email, bloqueo sin persistencia transaccional) se cerraron en un bloque de unas tres semanas, lo que indica una revisión sistemática posterior a la implementación del módulo. La integración OAuth con Facebook y Google generó correcciones adicionales en septiembre, en particular sobre emails nulos y redirecciones.

**Rendimiento.** La mejora más significativa se dio en la aplicación móvil: el mapa de asientos pasó de renderizado por objetos a Data-Oriented Design con TypedArrays, lo que habilita recintos de más de 100.000 asientos. En el backend se corrigió un patrón N+1 en el listado de eventos. Ambas mejoras surgieron al probar con datos más realistas.

**Pruebas.** El proyecto pasó de no tener pruebas a contar con 13 archivos de prueba unitaria del backend (alrededor de 40 casos), concentrados en los módulos event, security y notification. El patrón de prueba es sólido y, desde la migración a GitHub Actions, estas pruebas se ejecutan en cada push o pull request; sin embargo, el alcance cubre 3 de 14 módulos, lo que constituye la principal deuda técnica pendiente.

**Despliegue.** El pipeline de Jenkins requirió al menos ocho commits de corrección entre abril y mayo para estabilizarse, pero quedó con las pruebas y el análisis estático deshabilitados. En octubre se desactivó y se reemplazó por GitHub Actions, que ejecuta las pruebas del backend, compila la web y la app móvil y envía el análisis a SonarQube Cloud; los manifiestos de Kubernetes y el docker-compose constituyen la base de despliegue declarativo. Queda pendiente automatizar la construcción de imágenes y el despliegue.

## 8. Plan de acción sobre los casos abiertos

Se proponen las siguientes acciones para cerrar los casos en proceso y pendientes, ordenadas por prioridad.

| **ID** | **Acción propuesta** | **Prioridad** |
|---|---|---|
| AC-026 | Completar el módulo de idiomas conforme a los requisitos del SRS y agregar pruebas automatizadas. | Alta |
| AC-017 | Externalizar LOCKOUT_MINUTES a una variable de entorno con valor de 30 minutos en producción. | Alta |
| AC-020 | Externalizar el cron de limpieza de tokens y fijarlo en 30 minutos para producción. | Media |
| AC-023 | Corregir en la documentación toda referencia a Flyway por Liquibase. | Media |
| AC-024 | Retirar las menciones a Wompi del README del repositorio de código y de los textos de términos y privacidad de la web y la app móvil. | Media |
| AC-025 | Verificar en el SRS si la mensajería asíncrona es un requisito; si lo es, implementar; si no, retirar la dependencia y ajustar la documentación. | Baja |
| AC-002 | Actualizar los diagramas de clase que aún muestran el rol ATTENDEE por USER. | Baja |

## 9. Conclusiones

El registro evidencia un proceso de mejora continua activo: la mayoría de las acciones (20 de 26) están cerradas y respaldadas por commits o migraciones verificables. Los casos abiertos se concentran en la configuración de valores propios de producción, la finalización de funcionalidades exigidas por el SRS y la alineación entre la documentación y el código implementado. Cerrarlos reducirá el riesgo técnico del sistema y mejorará la trazabilidad frente a la documentación técnica.
