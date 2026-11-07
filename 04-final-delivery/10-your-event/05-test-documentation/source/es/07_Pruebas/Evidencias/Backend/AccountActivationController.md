---
id: TE-0131
title: "AccountActivationController"
lang: es
source: 06_Pruebas/Backend/AccountActivationController.pdf
source_sha256: 94fb46e09714260b30c3022d1a8526e0651929f381b57f68bbf8b6c2fbcc1979
conversion: "pdfplumber 0.11.10 + conversor de PDF sin etiquetar (método C, piloto 3); sin revisión manual"
converted_on: 2026-10-23
fidelity: pendiente_revision
losses:
  - "Cabecera o pie de página omitido en las páginas 1–4: «{n}»."
  - "48 tildes de LaTeX (glifo separado sobre la letra) recompuestas como caracteres únicos."
  - "Se conservan negritas, cursivas y fuente monoespaciada; no se conservan tamaños, colores, alineación ni sangrías."
  - "Verificación automática frente a pdftotext: cobertura 100.00%, precisión 100.00%, orden 100.00%; faltan {} (explicado arriba)."
status: borrador
reviewed_by: ""
---
> **Documento histórico.** Esta evidencia corresponde al repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (agosto–octubre de 2025): el controlador probado existe allí y no en el código actual (`lozano2303/TuEvento`), o se probó antes de que existiera el repositorio actual (primer commit: 2 de noviembre de 2025). Se conserva sin cambios como evidencia del proceso. Ver C9 en `Registro_de_Contradicciones.md`.

# FORMATO PRUEBA DE SOFTWARE - AccountActivationController

Keiner Cano

2 de octubre de 2025

## 1. Introducción

Pruebas para AccountActivationController.

## 2. Documentos de Referencia

Versión: 1.0

Título: Pruebas AccountActivationController

## 3. Prueba: POST /api/v1/account-activation/create - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Crear código de activación

Objetivo: Verificar creación<br>
Tipo de Prueba: Unitaria

Datos de entrada: AccountActivationDto válido

Procedimiento: POST

Datos de salida - Resultado Esperado: Código creado

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 4. Prueba: POST /api/v1/account-activation/create - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Usuario inexistente

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria

Datos de entrada: Usuario inválido

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

Comentario personal: Valida usuario.

## 5. Prueba: PUT /api/v1/account-activation/resend - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Reenviar código

Objetivo: Verificar reenvío<br>
Tipo de Prueba: Unitaria

Datos de entrada: AccountActivationDto válido

Procedimiento: PUT

Datos de salida - Resultado Esperado: Código reenviado

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 6. Prueba: PUT /api/v1/account-activation/resend - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Código ya enviado

Objetivo: Manejo de límites<br>
Tipo de Prueba: Unitaria

Datos de entrada: Reenvío excesivo

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

Comentario personal: Maneja límites.

## 7. Prueba: PUT /api/v1/account-activation/verify - Caso Éxito

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Verificar código

Objetivo: Verificar activación<br>
Tipo de Prueba: Unitaria

Datos de entrada: AccountActivationDto válido

Procedimiento: PUT

Datos de salida - Resultado Esperado: Cuenta activada

Resultado Obtenido: Prueba Exitosa

¿Prueba Exitosa? Sí<br>
Realizado por: Keiner Cano<br>
Firma:<br>
Fecha: 2 de octubre de 2025<br>
Aprobado por: CapySoft<br>
Firma:<br>
Fecha:<br>
Comentario personal: Ok.

## 8. Prueba: PUT /api/v1/account-activation/verify - Caso Excepción

Fecha de realización: 2 de octubre de 2025

Duración de la prueba: 5 min

Requerimientos Funcional: Código inválido

Objetivo: Manejo de errores<br>
Tipo de Prueba: Unitaria

Datos de entrada: Código incorrecto

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

Comentario personal: Valida código.
