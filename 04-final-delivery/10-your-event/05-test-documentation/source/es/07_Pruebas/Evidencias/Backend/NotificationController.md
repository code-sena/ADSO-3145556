---
id: TE-0144
title: "NotificationController"
lang: es
source: 06_Pruebas/Backend/NotificationController.pdf
source_sha256: 622d23de3d61b940e32c13cff9ab354ef40295d06269073b84c1619d7513e3b7
conversion: "pdfplumber 0.11.10 + conversor de PDF sin etiquetar (método C, piloto 3); sin revisión manual"
converted_on: 2026-10-23
fidelity: pendiente_revision
losses:
  - "Cabecera o pie de página omitido en las páginas 1–6: «{n}»."
  - "Palabras partidas con guion al final de línea unidas: Ca-so, Ca-so."
  - "67 tildes de LaTeX (glifo separado sobre la letra) recompuestas como caracteres únicos."
  - "Se conservan negritas, cursivas y fuente monoespaciada; no se conservan tamaños, colores, alineación ni sangrías."
  - "Verificación automática frente a pdftotext: cobertura 100.00%, precisión 100.00%, orden 100.00%; faltan {} (explicado arriba)."
status: borrador
reviewed_by: ""
---
> **Documento histórico.** Esta evidencia corresponde al repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025): el controlador probado existe allí y no en el código actual (`lozano2303/TuEvento`), o se probó antes de que existiera el repositorio actual (primer commit: 2 de noviembre de 2025). Se conserva sin cambios como evidencia del proceso. Ver C9 en `Registro_de_Contradicciones.md`.

# FORMATO PRUEBA DE SOFTWARE - NotificationController

Keiner Cano

2 de octubre de 2025

## 1. Introducción

Pruebas para NotificationController.

## 2. Documentos de Referencia

Versión: 1.0

Título: Pruebas NotificationController

## 3. Prueba: POST /api/v1/notification/create - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Crear notificación

Objetivo: Verificar creación<br>
Tipo de Prueba: Unitaria

Datos de entrada: NotificationDto válido

Procedimiento: POST

Datos de salida - Resultado Esperado: NotificationDto

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 4. Prueba: POST /api/v1/notification/create - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Datos inválidos

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria

Datos de entrada: NotificationDto inválido

Procedimiento: POST

Datos de salida - Resultado Esperado: Error

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:

Comentario personal: Valida datos.

## 5. Prueba: PUT /api/v1/notification/update - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Actualizar notificación

Objetivo: Verificar actualización<br>
Tipo de Prueba: Unitaria

Datos de entrada: NotificationDto válido

Procedimiento: PUT

Datos de salida - Resultado Esperado: NotificationDto

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 6. Prueba: PUT /api/v1/notification/update - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Notificación inexistente

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada: ID inválido<br>
Procedimiento: PUT

Datos de salida - Resultado Esperado: Error

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:

Comentario personal: Maneja inexistentes.

## 7. Prueba: DELETE /api/v1/notification/{id} - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Eliminar notificación

Objetivo: Verificar eliminación<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada: id válido<br>
Procedimiento: DELETE

Datos de salida - Resultado Esperado: NotificationDto

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 8. Prueba: DELETE /api/v1/notification/{id} - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Notificación inexistente

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada: id inválido<br>
Procedimiento: DELETE

Datos de salida - Resultado Esperado: Error

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:

Comentario personal: Maneja inexistentes.

## 9. Prueba: GET /api/v1/notification/{id} - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Obtener notificación por ID

Objetivo: Verificar obtención<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada: id válido<br>
Procedimiento: GET

Datos de salida - Resultado Esperado: NotificationDto

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 10. Prueba: GET /api/v1/notification/{id} - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Notificación inexistente

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada: id inválido<br>
Procedimiento: GET

Datos de salida - Resultado Esperado: Error

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:

Comentario personal: Maneja inexistentes.

## 11. Prueba: GET /api/v1/notification/event/{eventID} - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Obtener notificaciones por evento

Objetivo: Verificar lista<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada: eventID válido<br>
Procedimiento: GET

Datos de salida - Resultado Esperado: Lista de NotificationDto

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 12. Prueba: GET /api/v1/notification/event/{eventID} - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Evento sin notificaciones

Objetivo: Manejo de vacío<br>
Tipo de Prueba: Unitaria

Datos de entrada: eventID sin notificaciones

Procedimiento: GET

Datos de salida - Resultado Esperado: Lista vacía

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:

Comentario personal: Maneja vacío.
