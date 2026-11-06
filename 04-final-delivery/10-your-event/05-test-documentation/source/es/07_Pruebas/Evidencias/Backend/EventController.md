---
id: TE-0138
title: "EventController"
lang: es
source: 06_Pruebas/Backend/EventController.pdf
source_sha256: 4b2ef27a79fc916792d44e552aef01910168ea73c40dcf099ae034c2d766ba05
conversion: "pdfplumber 0.11.10 + conversor de PDF sin etiquetar (método C, piloto 3); sin revisión manual"
converted_on: 2026-10-23
fidelity: pendiente_revision
losses:
  - "Cabecera o pie de página omitido en las páginas 1–9: «{n}»."
  - "Palabras partidas con guion al final de línea unidas: indivi-duales, integra-ción, dis-tintos, Éxi-to, Ex-cepción, Éxi-to."
  - "114 tildes de LaTeX (glifo separado sobre la letra) recompuestas como caracteres únicos."
  - "Se conservan negritas, cursivas y fuente monoespaciada; no se conservan tamaños, colores, alineación ni sangrías."
  - "Verificación automática frente a pdftotext: cobertura 100.00%, precisión 100.00%, orden 100.00%; faltan {} (explicado arriba)."
  - "Los signos «¡» y «¿» de «ResponseDto¡EventDto¿» están así en el original (codificación OT1 de LaTeX, donde se escribió < y >); no se corrigen."
  - "Revisión por secciones: 18 títulos (título + secciones 1-17) localizados en el original; las 19 secciones coinciden en palabras y orden."
status: borrador
reviewed_by: ""
---
> **Documento histórico.** Esta evidencia corresponde al repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025): el controlador probado existe allí y no en el código actual (`lozano2303/TuEvento`), o se probó antes de que existiera el repositorio actual (primer commit: 2 de noviembre de 2025). Se conserva sin cambios como evidencia del proceso. Ver C9 en `Registro_de_Contradicciones.md`.

# FORMATO PRUEBA DE SOFTWARE - EventController

Keiner Cano

2 de octubre de 2025

## 1. Introducción

El propósito del siguiente documento es realizar seguimiento a las pruebas del EventController en el proyecto TuEvento por medio de la implementación de pruebas unitarias.

Las pruebas unitarias nos permiten asegurarnos de que los métodos individuales del código de un proyecto funcionen correctamente, así como la integración de todo el proyecto.

Se busca en los requerimientos funcionales: encontrar errores relacionados con la funcionalidad del sistema y en los no funcionales, fallas de rendimiento, seguridad, usabilidad, fiabilidad.

Existen 4 tipos de pruebas:

Pruebas unitarias. Diseñadas para probar una parte pequeña y específica de funcionalidad. Ej. Un método.

Pruebas de integración. Diseñadas para probar la interacción entre los distintos componentes de un sistema.

Pruebas de sistema. Diseñadas para probar el sistema en su totalidad como si de una caja negra se tratase.

Pruebas de aceptación. Diseñadas para verificar que el sistema cumple con los requisitos exigidos por el usuario.

## 2. Documentos de Referencia

Versión: 1.0<br>
Título: Pruebas EventController

## 3. Prueba: POST /api/v1/event/insert - Caso Éxito

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Insertar evento válido Objetivo: Verificar que el endpoint inserta un evento correctamente con datos válidos

Tipo de Prueba: Unitaria

Datos de entrada de la prueba: EventDto con todos los campos requeridos Procedimiento de Prueba: Enviar POST request a /api/v1/event/insert con EventDto válido

Datos de salida - Resultado Esperado: ResponseDto¡EventDto¿con status success y datos del evento

Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:

Comentario personal: Funciona correctamente con datos válidos.

## 4. Prueba: POST /api/v1/event/insert - Caso Excepción

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Insertar evento inválido Objetivo: Verificar que el endpoint maneja errores con datos inválidos Tipo de Prueba: Unitaria

Datos de entrada de la prueba: EventDto con campos faltantes Procedimiento de Prueba: Enviar POST request a /api/v1/event/insert con EventDto inválido

Datos de salida - Resultado Esperado: ResponseDto con error message<br>
Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:

Comentario personal: Maneja excepciones correctamente.

## 5. Prueba: PUT /api/v1/event/update - Caso Éxito

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Actualizar evento válido Objetivo: Verificar que el endpoint actualiza un evento correctamente Tipo de Prueba: Unitaria

Datos de entrada de la prueba: ResponseEvent y EventDto válidos Procedimiento de Prueba: Enviar PUT request a /api/v1/event/update con datos válidos

Datos de salida - Resultado Esperado: ResponseDto¡EventDto¿actualizado<br>
Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Actualización exitosa.

## 6. Prueba: PUT /api/v1/event/update - Caso Excepción

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Actualizar evento con ID inexistente Objetivo: Verificar manejo de errores en actualización

Tipo de Prueba: Unitaria

Datos de entrada de la prueba: ID de evento que no existe

Procedimiento de Prueba: Enviar PUT request con ID inválido<br>
Datos de salida - Resultado Esperado: ResponseDto con error<br>
Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Comentario personal: Maneja IDs inexistentes.

## 7. Prueba: DELETE /api/v1/event/cancel/{eventId} - Caso Éxito

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Cancelar evento existente<br>
Objetivo: Verificar cancelación de evento<br>
Tipo de Prueba: Unitaria

Datos de entrada de la prueba: eventId válido, usuario autenticado<br>
Procedimiento de Prueba: Enviar DELETE request con eventId válido<br>
Datos de salida - Resultado Esperado: ResponseDto con éxito<br>
Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Cancelación exitosa.

## 8. Prueba: DELETE /api/v1/event/cancel/{eventId} - Caso Excepción

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Cancelar evento sin autenticación<br>
Objetivo: Verificar seguridad en cancelación<br>
Tipo de Prueba: Unitaria

Datos de entrada de la prueba: Sin userID en request

Procedimiento de Prueba: Enviar DELETE sin autenticación<br>
Datos de salida - Resultado Esperado: Error Üsuario no autenticado”<br>
Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Seguridad implementada.

## 9. Prueba: PUT /api/v1/event/publish/{id} - Caso Éxito

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Publicar evento

Objetivo: Verificar publicación de evento<br>
Tipo de Prueba: Unitaria

Datos de entrada de la prueba: id de evento válido

Procedimiento de Prueba: Enviar PUT a /publish/id

Datos de salida - Resultado Esperado: Evento publicado

Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Publicación exitosa.

## 10. Prueba: PUT /api/v1/event/publish/{id} - Caso Excepción

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Publicar evento inexistente<br>
Objetivo: Manejo de errores en publicación<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada de la prueba: id inexistente<br>
Procedimiento de Prueba: PUT con id inválido<br>
Datos de salida - Resultado Esperado: Error<br>
Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Maneja IDs inexistentes.

## 11. Prueba: GET /api/v1/event/{id} - Caso Éxito

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Obtener evento por ID<br>
Objetivo: Verificar obtención de evento<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada de la prueba: id válido<br>
Procedimiento de Prueba: GET /id<br>
Datos de salida - Resultado Esperado: EventDto<br>
Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Obtención exitosa.

## 12. Prueba: GET /api/v1/event/{id} - Caso Excepción

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Obtener evento inexistente<br>
Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada de la prueba: id inexistente<br>
Procedimiento de Prueba: GET con id inválido<br>
Datos de salida - Resultado Esperado: Error<br>
Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:

Comentario personal: Maneja errores correctamente.

## 13. Prueba: GET /api/v1/event/getAllbyUser - Caso Éxito

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Obtener eventos por usuario<br>
Objetivo: Verificar lista de eventos del usuario<br>
Tipo de Prueba: Unitaria

Datos de entrada de la prueba: Usuario autenticado

Procedimiento de Prueba: GET /getAllbyUser

Datos de salida - Resultado Esperado: Lista de EventDto

Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Lista correcta.

## 14. Prueba: GET /api/v1/event/getAllbyUser - Caso Excepción

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Sin autenticación

Objetivo: Verificar seguridad<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada de la prueba: Sin userID<br>
Procedimiento de Prueba: GET sin autenticación<br>
Datos de salida - Resultado Esperado: Error<br>
Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Seguridad ok.

## 15. Prueba: GET /api/v1/event/getAll - Caso Éxito

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Obtener todos los eventos<br>
Objetivo: Verificar lista completa<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada de la prueba: Ninguno<br>
Procedimiento de Prueba: GET /getAll

Datos de salida - Resultado Esperado: Lista de EventDto

Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Funciona.

## 16. Prueba: GET /api/v1/event/filter - Caso Éxito

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Filtrar eventos

Objetivo: Verificar filtrado<br>
Tipo de Prueba: Unitaria

Datos de entrada de la prueba: Parámetros de filtro

Procedimiento de Prueba: GET /filter con params

Datos de salida - Resultado Esperado: Lista filtrada

Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Filtrado correcto.

## 17. Prueba: PUT /api/v1/event/complete/{eventId} - Caso Éxito

Fecha de realización: 2 de octubre de 2025<br>
Duración de la prueba: 5 min

Requerimientos Funcional de la prueba: Completar evento

Objetivo: Verificar completado<br>
Tipo de Prueba: Unitaria<br>
Datos de entrada de la prueba: eventId válido

Procedimiento de Prueba: PUT /complete/eventId

Datos de salida - Resultado Esperado: Evento completado

Resultado Obtenido: Prueba Exitosa<br>
¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Completado ok.
