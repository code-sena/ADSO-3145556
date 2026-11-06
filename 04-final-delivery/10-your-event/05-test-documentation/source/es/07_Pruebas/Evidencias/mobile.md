---
id: TE-0154
title: "mobile"
lang: es
source: 06_Pruebas/mobile.pdf
source_sha256: 97036e5b861058479eddc5cdffabffe99802e9785189ba8f8e4cc038bb863229
conversion: "pdfplumber 0.11.10 + conversor de PDF sin etiquetar (método C, piloto 3); sin revisión manual"
converted_on: 2026-10-23
fidelity: pendiente_revision
losses:
  - "Cabecera o pie de página omitido en las páginas 1–4: «{n}»."
  - "Tablas partidas entre páginas unidas en una sola: 3→4."
  - "Palabras partidas con guion al final de línea unidas: prin-cipales."
  - "Palabras partidas con guion de corte dentro de celdas unidas (sin guion si sigue minúscula; con guion y sin espacio si sigue dígito o mayúscula): Esperado, AUTH-001, registrado, aplicación., Ingresar, válidos., pantalla, AUTH-002, contraseña, incorrectos., mensaje, AUTH-003, campos, Tocar, mensajes, validación, campos, Esperado, AUTH-001, registrado, aplicación., Ingresar, válidos., pantalla, AUTH-002, contraseña, incorrectos., mensaje, AUTH-003, campos, Tocar, mensajes, validación, campos, Esperado, AUTH-004, registrado, Register”., Ingresar, válidos., Ingresar, pantalla, AUTH-005, mensaje, AUTH-006, iniciado, código, mensaje, Esperado, AUTH-004, registrado, Register”., Ingresar, válidos., Ingresar, pantalla, AUTH-005, mensaje, AUTH-006, iniciado, código, mensaje, Esperado, AUTH-007, registrado, ”Forgot, Ingresar, contraseña., AUTH-008, registrado., mensaje, AUTH-009, iniciado, código, mensaje, Esperado, AUTH-007, registrado, ”Forgot, Ingresar, contraseña., AUTH-008, registrado., mensaje, AUTH-009, iniciado, código, mensaje, Esperado, EVENT-001, autenticado, disponibles., EVENT-002, Ingresar, eventos, EVENT-003, Esperado, EVENT-001, autenticado, disponibles., EVENT-002, Ingresar, eventos, EVENT-003, Esperado, EVENT-004, completo, información, EVENT-005, Esperado, EVENT-004, completo, información, EVENT-005, Esperado, PROF-001, autenticado, PROF-002, Esperado, PROF-001, autenticado, PROF-002, Esperado, autenticado, Escanear, válido., mostrar, mensaje, permiso., mensaje, Esperado, autenticado, Escanear, válido., mostrar, mensaje, permiso., mensaje."
  - "70 tildes de LaTeX (glifo separado sobre la letra) recompuestas como caracteres únicos."
  - "Se conservan negritas, cursivas y fuente monoespaciada; no se conservan tamaños, colores, alineación ni sangrías."
  - "Verificación automática frente a pdftotext: cobertura 100.00%, precisión 97.05%, orden 98.50%; faltan {} (explicado arriba)."
  - "Diferencias de la verificación explicadas: los «sobrantes» 001-006 son los números de los identificadores AUTH-001… que en el original quedan solos en una línea de la celda; la referencia (pdftotext) descarta los números solos en una línea."
status: borrador
reviewed_by: ""
---
> **Documento histórico.** Estas pruebas tienen fecha del 6 de octubre de 2025 y corresponden a la app móvil del repositorio anterior del proyecto, [`lozano2303/tu-evento`](https://github.com/lozano2303/tu-evento) (carpeta `TuEventoFrontend (Mobile)`). La app móvil del repositorio actual (`lozano2303/TuEvento`) se incorporó a partir del 24 de marzo de 2026. Se conserva sin cambios como evidencia del proceso. Ver C9 en `Registro_de_Contradicciones.md`.

# Manual de Pruebas - TuEvento Mobile

Equipo de Desarrollo

6 de octubre de 2025

## 1. Introducción

Este manual de pruebas describe los casos de prueba para la aplicación móvil TuEvento. La aplicación permite a los usuarios autenticarse, explorar eventos, ver detalles de eventos, gestionar su perfil y escanear códigos QR.

El propósito de este manual es asegurar que todas las funcionalidades principales funcionen correctamente a través de pruebas manuales.

## 2. Pruebas de Autenticación

### 2.1. Inicio de Sesión

| **ID** | **Descripción** | **Precondiciones** | **Pasos** | **Resultado Esperado** |
|---|---|---|---|---|
| AUTH-001 | Inicio de sesión exitoso | Usuario registrado en el sistema | 1. Abrir la aplicación. 2. Ingresar email y contraseña válidos. 3. Tocar el botón ”Login”. | Navegar a la pantalla principal (MainTabs). |
| AUTH-002 | Credenciales inválidas | Ninguna | 1. Ingresar email y contraseña incorrectos. 2. Tocar el botón ”Login”. | Mostrar mensaje de error de autenticación. |
| AUTH-003 | Campos vacíos | Ninguna | 1. Dejar campos de email y contraseña vacíos. 2. Tocar el botón ”Login”. | Mostrar mensajes de validación para campos requeridos. |

### 2.2. Registro de Usuario

| **ID** | **Descripción** | **Precondiciones** | **Pasos** | **Resultado Esperado** |
|---|---|---|---|---|
| AUTH-004 | Registro exitoso | Email no registrado | 1. Tocar Register”. 2. Ingresar datos válidos. 3. Tocar - egister”. 4. Ingresar código de verificación. 5. Confirmar. | Navegar a pantalla de login o principal. |
| AUTH-005 | Email ya registrado | Email existente | 1. Ingresar email existente. 2. Completar registro. | Mostrar mensaje de error. |
| AUTH-006 | Código de verificación inválido | Registro iniciado | 1. Ingresar código incorrecto. 2. Confirmar. | Mostrar mensaje de error. |

### 2.3. Recuperación de Contraseña

| **ID** | **Descripción** | **Precondiciones** | **Pasos** | **Resultado Esperado** |
|---|---|---|---|---|
| AUTH-007 | Recuperación exitosa | Email registrado | 1. Tocar ”Forgot Password”. 2. Ingresar email. 3. Ingresar código. 4. Nueva contraseña. 5. Confirmar. | Navegar a login. |
| AUTH-008 | Email no registrado | Email inválido | 1. Ingresar email no registrado. 2. Enviar. | Mostrar mensaje de error. |
| AUTH-009 | Código inválido | Proceso iniciado | 1. Ingresar código incorrecto. | Mostrar mensaje de error. |

## 3. Pruebas de Eventos

### 3.1. Lista de Eventos

| **ID** | **Descripción** | **Precondiciones** | **Pasos** | **Resultado Esperado** |
|---|---|---|---|---|
| EVENT-001 | Cargar lista de eventos | Usuario autenticado | 1. Navegar a pestaña Inicio. | Mostrar lista de eventos disponibles. |
| EVENT-002 | Filtrar eventos | Lista cargada | 1. Usar barra de búsqueda. 2. Ingresar término. | Mostrar eventos filtrados. |
| EVENT-003 | Navegar a detalle | Evento visible | 1. Tocar en un evento. | Abrir pantalla de detalle. |

### 3.2. Detalle de Evento

| **ID** | **Descripción** | **Precondiciones** | **Pasos** | **Resultado Esperado** |
|---|---|---|---|---|
| EVENT-004 | Mostrar detalle completo | Navegado desde lista | 1. Ver pantalla. | Mostrar información del evento (título, descripción, fecha, etc.). |
| EVENT-005 | Regresar a lista | En detalle | 1. Usar botón atrás. | Volver a lista de eventos. |

## 4. Pruebas de Perfil y Otras Funcionalidades

### 4.1. Pantalla de Perfil

| **ID** | **Descripción** | **Precondiciones** | **Pasos** | **Resultado Esperado** |
|---|---|---|---|---|
| PROF-001 | Mostrar información de perfil | Usuario autenticado | 1. Navegar a pestaña Perfil. | Mostrar datos del usuario (nombre, email, etc.). |
| PROF-002 | Cerrar sesión | En perfil | 1. Tocar botón de logout. | Cerrar sesión y volver a login. |

### 4.2. Lector QR

| **ID** | **Descripción** | **Precondiciones** | **Pasos** | **Resultado Esperado** |
|---|---|---|---|---|
| QR-001 | Escanear QR válido | Usuario autenticado | 1. Navegar a pestaña Lector QR. 2. Permitir cámara. 3. Escanear QR válido. | Procesar el código y mostrar resultado. |
| QR-002 | Escanear QR inválido | Cámara activa | 1. Escanear código no válido. | Mostrar mensaje de error. |
| QR-003 | Denegar permiso de cámara | Primera vez | 1. Denegar permiso. | Mostrar mensaje para habilitar cámara. |
