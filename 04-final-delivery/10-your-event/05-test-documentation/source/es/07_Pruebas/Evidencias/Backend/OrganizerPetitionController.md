---
id: TE-0145
title: "OrganizerPetitionController"
lang: es
source: 06_Pruebas/Backend/OrganizerPetitionController.pdf
source_sha256: b90d037ca35c8169ea79ee0239d6512d41ff6f3cd3bc1320c7b4cfc1f5b5cb68
conversion: "pdfplumber 0.11.10 + conversor de PDF sin etiquetar (método C, piloto 3); sin revisión manual"
converted_on: 2026-10-23
fidelity: pendiente_revision
losses:
  - "Cabecera o pie de página omitido en las páginas 1–5: «{n}»."
  - "Palabras partidas con guion al final de línea unidas: Ca-so."
  - "52 tildes de LaTeX (glifo separado sobre la letra) recompuestas como caracteres únicos."
  - "Se conservan negritas, cursivas y fuente monoespaciada; no se conservan tamaños, colores, alineación ni sangrías."
  - "Verificación automática frente a pdftotext: cobertura 100.00%, precisión 100.00%, orden 100.00%; faltan {} (explicado arriba)."
status: borrador
reviewed_by: ""
---
> **Documento histórico.** Esta evidencia corresponde al repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025): el controlador probado existe allí y no en el código actual (`lozano2303/TuEvento`), o se probó antes de que existiera el repositorio actual (primer commit: 2 de noviembre de 2025). Se conserva sin cambios como evidencia del proceso. Ver C9 en `Registro_de_Contradicciones.md`.

# FORMATO PRUEBA DE SOFTWARE - OrganizerPetitionController

Keiner Cano

2 de octubre de 2025

## 1. Introducción

Pruebas para OrganizerPetitionController.

## 2. Documentos de Referencia

Versión: 1.0

Título: Pruebas OrganizerPetitionController

## 3. Prueba: POST /api/v1/organizer-petition - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Subir petición

Objetivo: Verificar subida<br>
Tipo de Prueba: Unitaria

Datos de entrada: MultipartFile, userID

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

## 4. Prueba: POST /api/v1/organizer-petition - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Archivo inválido

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria

Datos de entrada: Archivo corrupto

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

Comentario personal: Valida archivo.

## 5. Prueba: GET /api/v1/organizer-petition - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Obtener todas las peticiones

Objetivo: Verificar lista<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada: Ninguno<br>
Procedimiento: GET

Datos de salida - Resultado Esperado: Lista de OrganizerPetitionDto

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 6. Prueba: GET /api/v1/organizer-petition/user/{userID} - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Obtener petición por usuario

Objetivo: Verificar obtención<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada: userID válido<br>
Procedimiento: GET

Datos de salida - Resultado Esperado: OrganizerPetitionDto

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 7. Prueba: GET /api/v1/organizer-petition/user/{userID} - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Usuario sin petición

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria

Datos de entrada: userID sin petición

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

## 8. Prueba: PUT /api/v1/organizer-petition/{petitionID}/status - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Actualizar estado

Objetivo: Verificar actualización<br>
Tipo de Prueba: Unitaria

Datos de entrada: petitionID, status

Procedimiento: PUT

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

## 9. Prueba: PUT /api/v1/organizer-petition/{petitionID}/status - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: petitionID inexistente

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria

Datos de entrada: petitionID inválido

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

## 10. Prueba: GET /api/v1/organizer-petition/{petitionID}/docu - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Descargar documento

Objetivo: Verificar descarga<br>
Tipo de Prueba: Unitaria

Datos de entrada: petitionID válido

Procedimiento: GET

Datos de salida - Resultado Esperado: Byte array

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 11. Prueba: GET /api/v1/organizer-petition/{petitionID}/docu - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Petición sin documento

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria

Datos de entrada: petitionID sin documento

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
