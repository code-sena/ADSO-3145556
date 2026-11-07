---
id: TE-0142
title: "LocationController"
lang: es
source: 06_Pruebas/Backend/LocationController.pdf
source_sha256: 6199c5017d36b87caafd7e88040ea328288ac4cf692dbfe8569782666af2fb28
conversion: "pdfplumber 0.11.10 + conversor de PDF sin etiquetar (método C, piloto 3); sin revisión manual"
converted_on: 2026-10-23
fidelity: pendiente_revision
losses:
  - "Cabecera o pie de página omitido en las páginas 1–5: «{n}»."
  - "Palabras partidas con guion al final de línea unidas: Ex-cepción."
  - "59 tildes de LaTeX (glifo separado sobre la letra) recompuestas como caracteres únicos."
  - "Se conservan negritas, cursivas y fuente monoespaciada; no se conservan tamaños, colores, alineación ni sangrías."
  - "Verificación automática frente a pdftotext: cobertura 100.00%, precisión 100.00%, orden 100.00%; faltan {} (explicado arriba)."
status: borrador
reviewed_by: ""
---
> **Documento histórico.** Esta evidencia corresponde al repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025): el controlador probado existe allí y no en el código actual (`lozano2303/TuEvento`), o se probó antes de que existiera el repositorio actual (primer commit: 2 de noviembre de 2025). Se conserva sin cambios como evidencia del proceso. Ver C9 en `Registro_de_Contradicciones.md`.

# FORMATO PRUEBA DE SOFTWARE - LocationController

Keiner Cano

2 de octubre de 2025

## 1. Introducción

Pruebas para LocationController.

## 2. Documentos de Referencia

Versión: 1.0

Título: Pruebas LocationController

## 3. Prueba: POST /api/v1/location - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Crear ubicación

Objetivo: Verificar creación<br>
Tipo de Prueba: Unitaria

Datos de entrada: LocationDto válido

Procedimiento: POST

Datos de salida - Resultado Esperado: LocationDto

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 4. Prueba: POST /api/v1/location - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Datos inválidos

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria

Datos de entrada: LocationDto inválido

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

## 5. Prueba: PUT /api/v1/location/{locationID} - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Actualizar ubicación

Objetivo: Verificar actualización<br>
Tipo de Prueba: Unitaria

Datos de entrada: locationID, LocationDto

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

## 6. Prueba: PUT /api/v1/location/{locationID} - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: locationID inexistente

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria

Datos de entrada: locationID inválido

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

## 7. Prueba: DELETE /api/v1/location/{locationID} - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Eliminar ubicación

Objetivo: Verificar eliminación<br>
Tipo de Prueba: Unitaria

Datos de entrada: locationID válido

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

## 8. Prueba: DELETE /api/v1/location/{locationID} - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Ubicación inexistente

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria

Datos de entrada: locationID inválido

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

## 9. Prueba: GET /api/v1/location - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Obtener todas las ubicaciones

Objetivo: Verificar lista<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada: Ninguno<br>
Procedimiento: GET

Datos de salida - Resultado Esperado: Lista de LocationDto

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 10. Prueba: GET /api/v1/location/{locationID} - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Obtener ubicación por ID

Objetivo: Verificar obtención<br>
Tipo de Prueba: Unitaria

Datos de entrada: locationID válido

Procedimiento: GET

Datos de salida - Resultado Esperado: LocationDto

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 11. Prueba: GET /api/v1/location/{locationID} - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Ubicación inexistente

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria

Datos de entrada: locationID inválido

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
