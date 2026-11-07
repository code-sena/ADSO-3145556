---
id: TE-0150
title: "TicketController"
lang: es
source: 06_Pruebas/Backend/TicketController.pdf
source_sha256: 9bbe5c4e95cfed23be2a893c67abc344d035fac5c5aaa6ade48a02fe5e105f17
conversion: "pdfplumber 0.11.10 + conversor de PDF sin etiquetar (método C, piloto 3); sin revisión manual"
converted_on: 2026-10-23
fidelity: pendiente_revision
losses:
  - "Cabecera o pie de página omitido en las páginas 1–6: «{n}»."
  - "Palabras partidas con guion al final de línea unidas: Éxi-to, Ex-cepción."
  - "61 tildes de LaTeX (glifo separado sobre la letra) recompuestas como caracteres únicos."
  - "Se conservan negritas, cursivas y fuente monoespaciada; no se conservan tamaños, colores, alineación ni sangrías."
  - "Verificación automática frente a pdftotext: cobertura 100.00%, precisión 100.00%, orden 100.00%; faltan {} (explicado arriba)."
status: borrador
reviewed_by: ""
---
> **Documento histórico.** Esta evidencia corresponde al repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025): el controlador probado existe allí y no en el código actual (`lozano2303/TuEvento`), o se probó antes de que existiera el repositorio actual (primer commit: 2 de noviembre de 2025). Se conserva sin cambios como evidencia del proceso. Ver C9 en `Registro_de_Contradicciones.md`.

# FORMATO PRUEBA DE SOFTWARE - TicketController

Keiner Cano

2 de octubre de 2025

## 1. Introducción

Pruebas para TicketController.

## 2. Documentos de Referencia

Versión: 1.0<br>
Título: Pruebas TicketController

## 3. Prueba: POST /api/v1/ticket/create-with-seats - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Crear ticket con asientos

Objetivo: Verificar creación<br>
Tipo de Prueba: Unitaria

Datos de entrada: TicketDto válido

Procedimiento: POST

Datos de salida - Resultado Esperado: Éxito

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 4. Prueba: POST /api/v1/ticket/create-with-seats - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Asientos no disponibles

Objetivo: Manejo de conflictos<br>
Tipo de Prueba: Unitaria

Datos de entrada: Asientos ocupados

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

Comentario personal: Maneja conflictos.

## 5. Prueba: DELETE /api/v1/ticket/cancel/{ticketID} - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Cancelar ticket

Objetivo: Verificar cancelación<br>
Tipo de Prueba: Unitaria

Datos de entrada: ticketID válido

Procedimiento: DELETE

Datos de salida - Resultado Esperado: Éxito

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 6. Prueba: DELETE /api/v1/ticket/cancel/{ticketID} - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Ticket inexistente

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria

Datos de entrada: ticketID inválido

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

## 7. Prueba: GET /api/v1/ticket/{id} - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Obtener ticket por ID

Objetivo: Verificar obtención<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada: id válido<br>
Procedimiento: GET

Datos de salida - Resultado Esperado: TicketDto

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 8. Prueba: GET /api/v1/ticket/{id} - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Ticket inexistente

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

## 9. Prueba: GET /api/v1/ticket/by-event/{eventId} - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Obtener tickets por evento

Objetivo: Verificar lista<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada: eventId válido<br>
Procedimiento: GET

Datos de salida - Resultado Esperado: Lista de TicketDto

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 10. Prueba: GET /api/v1/ticket/by-event/{eventId} - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Evento sin tickets

Objetivo: Manejo de lista vacía<br>
Tipo de Prueba: Unitaria

Datos de entrada: eventId sin tickets

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

## 11. Prueba: GET /api/v1/ticket/by-user/{userId} - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Obtener tickets por usuario

Objetivo: Verificar lista<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada: userId válido<br>
Procedimiento: GET

Datos de salida - Resultado Esperado: Lista de TicketDto

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 12. Prueba: GET /api/v1/ticket/by-user/{userId} - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Usuario sin tickets

Objetivo: Manejo de lista vacía<br>
Tipo de Prueba: Unitaria

Datos de entrada: userId sin tickets

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
