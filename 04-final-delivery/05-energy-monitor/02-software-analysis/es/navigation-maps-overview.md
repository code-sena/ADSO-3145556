# Mapas de navegación

**Proyecto:** Monitor de Consumo Eléctrico — EnergyMonitor
**Ficha:** 3145556 — Análisis y Desarrollo de Software (ADSO), SENA CIES Regional Huila
**Versión:** 1.1 (27 de octubre de 2026)

---

## 1. Contenido

Estos mapas son la **versión gráfica** del recorrido de las dos aplicaciones. Cada aplicación se documenta con tres diagramas más el detalle de los puntos de control; la web tiene además un cuarto mapa con la administración.

| **Documento** | **Mapa 1** | **Mapa 2** | **Mapa 3** | **Mapa 4** |
|---|---|---|---|---|
| [`mapa-navegacion-web.md`](web-navigation-map.md) | Flujo general: entrada, zona pública, guardas (invitado, sesión y rol ADMIN) y destinos del menú lateral | Panel de hogares, estados y las seis secciones del detalle | Favoritos, notificaciones (cuatro pestañas), ajustes y Mi cuenta | Administración: usuarios, estado, rol, avisos y ajustes del administrador |
| [`mapa-navegacion-movil.md`](mobile-navigation-map.md) | Flujo general: apertura, restauración, guarda y destinos del menú lateral | Panel principal, estados y las seis secciones del detalle | Favoritos, notificaciones (cuatro pestañas), ajustes y la pantalla de cuenta | — (la app no tiene administración) |

**Se dividió en tres mapas por una razón práctica:** un único diagrama con todas las pantallas resultaba ilegible, porque las aristas entre zonas obligaban al motor de diagramas a reordenar las cajas y el resultado se leía de abajo arriba.

### Imágenes renderizadas

Cada diagrama incluye además su versión en PNG, lista para insertar en el documento:

| **Archivo** | **Diagrama** |
|---|---|
| `mapa-navegacion-web-1.png` | Flujo general de la web |
| `mapa-navegacion-web-2.png` | Panel de hogares y detalle del hogar |
| `mapa-navegacion-web-3.png` | Pantallas auxiliares de la web |
| `mapa-navegacion-web-4.png` | Administración (rol ADMIN) |
| `mapa-navegacion-movil-1.png` | Flujo general del móvil |
| `mapa-navegacion-movil-2.png` | Panel principal y detalle del hogar |
| `mapa-navegacion-movil-3.png` | Pantallas auxiliares del móvil |

## 2. Origen de la información

Estos mapas se construyeron **leyendo el código fuente**, no los mockups.

| **Dato del mapa** | **Archivo del proyecto del que se obtuvo** |
|---|---|
| Rutas web y sus protecciones | `Energy-Monitor-application/src/routes/AppRoutes.jsx` |
| Menú lateral web | `Energy-Monitor-application/src/components/layout/MainLayout/Sidebar/Sidebar.jsx` |
| Acordeón de hogares | `Energy-Monitor-application/src/components/layout/MainLayout/Sidebar/NavHomes.jsx` |
| Pestañas del detalle del hogar | `Energy-Monitor-application/src/features/DetailHome/Consumption/Consumption.jsx` |
| Rutas móviles | `Energy-Monitor-Mobile/src/app/` |
| Guarda de autenticación móvil | `Energy-Monitor-Mobile/src/components/layout/AuthGate/AuthGate.tsx` |
| Menú lateral móvil | `Energy-Monitor-Mobile/src/components/layout/Sidebar/Sidebar.tsx` |
| Barra inferior del detalle | `Energy-Monitor-Mobile/src/components/layout/BottomTabBar/BottomTabBar.tsx` |
| Secciones del detalle móvil | `Energy-Monitor-Mobile/src/features/homes/screens/HomeDetailScreen/HomeDetailScreen.tsx` |

> **Los mockups no son el flujo real.** Los mockups y wireframes describen el aspecto visual de cada pantalla; el flujo de navegación se determina por las rutas declaradas y por los menús implementados. Por eso un mapa de navegación construido a partir de un mockup puede mostrar pantallas que el sistema nunca monta o transiciones que nunca ocurren.

## 3. Formato

Los mapas están escritos en **Mermaid**, embebido dentro de los documentos Markdown.

| **Ventaja** | **Detalle** |
|---|---|
| Se ve sin instalar nada | GitHub, GitLab, VS Code (con la extensión Mermaid), Notion y Obsidian los renderizan automáticamente |
| Es texto | Se versiona con Git, se puede revisar en los diffs y editar sin herramientas |
| Se puede exportar | Con la extensión Mermaid de VS Code se exporta a PNG, SVG o PDF con un clic |

### Cómo exportarlos a imagen para el documento

| **Opción** | **Pasos** |
|---|---|
| **Extensión de VS Code** | Instalar *Markdown Preview Mermaid Support* → abrir el archivo → botón de exportar a PNG |
| **Mermaid Live Editor** | Pegar el contenido del bloque en <https://mermaid.live> → botón **Actions → PNG** |
| **PlantUML en línea** | No aplica: Mermaid no es PlantUML |

### Si necesitas el código en otro formato

| **Formato** | **Cómo obtenerlo** |
|---|---|
| `.mmd` aislado | Copiar el contenido del bloque ```mermaid a un archivo con extensión `.mmd` |
| draw.io | Exportar el PNG desde Mermaid Live y luego usar *Insert → Image* sobre un lienzo de draw.io |
| PlantUML | Requiere reescribir el diagrama; avisa y lo convierto |

## 4. Cómo leer los mapas

| **Símbolo** | **Significado** |
|---|---|
| Nodo azul redondeado | Punto de entrada o decisión |
| Nodo verde | Módulo o área principal de la aplicación |
| Nodo blanco | Pantalla, sección o acción concreta |
| Nodo ámbar | Guarda de navegación o decisión de estado |
| Nodo rojo | Fin de flujo: cierre de sesión o error de acceso |
| Flecha sólida | Navegación directa entre pantallas |
| Flecha punteada | Reintento o acción bajo demanda |

## 5. Diferencias entre la navegación web y la móvil

| **Aspecto** | **Web** | **Móvil** |
|---|---|---|
| Página de bienvenida | Sí | No: abre en el inicio de sesión |
| Recuperación de contraseña | 2 rutas | 3 rutas |
| Pantalla de cuenta | Pantalla `/account` protegida | Pantalla propia con título |
| Administración | Sí (`/admin/users`, rol ADMIN) | No |
| Vinculación de dispositivos | Web Bluetooth (Chrome o Edge) | Bluetooth nativo (BLE) |
| Menú lateral | Fijo o plegable | Capa deslizable animada |
| Secciones del detalle | Pestañas superiores | Barra inferior con iconos |
| Total de rutas | 16 + página no encontrada | 13 |

## 6. Observaciones

| **#** | **Observación** | **Detalle** |
|---|---|---|
| 1 | Resuelta: la ruta `/homes` del móvil | Ahora redirige a `/dashboard` |
| 2 | Resuelta: la opción «Agregar cuenta» | Ya no aparece en los menús de perfil |
| 3 | Resuelta: ruta de reserva de la web | `*` muestra la página «no encontrada» (`NotFound.jsx`) |
| 4 | El móvil no tiene ruta de reserva propia | Expo Router muestra su propia pantalla de «pantalla no encontrada» |
| 5 | Resuelta: `/account` en la web | Está dentro de `ProtectedRoute` |
| 6 | Una cuenta ADMIN en la app móvil | La app no distingue administradores; las pantallas de administración solo existen en la web |

## 6.1 Estado de los mockups frente al código

Los mockups HTML de `mockup/layout/` se dibujaron antes de las últimas funciones. No se redibujaron en esta versión; la tabla indica qué falta o qué quedó desactualizado.

| **Pantalla del código** | **Plataforma** | **Mockup** | **Estado** |
|---|---|---|---|
| Usuarios de la plataforma (`/admin/users`): búsqueda, tabla, estado y rol | Web | — | **Falta** (`mockup/layout/web/admin/users.html`) |
| Confirmaciones de cambio de estado y de rol de administrador | Web | — | **Falta** (`mockup/layout/web/admin/users-confirm-modal.html`) |
| Notificaciones de administración | Web | — | **Falta** (`mockup/layout/web/admin/notifications.html`) |
| Menú lateral y ajustes del administrador (sin notificaciones) | Web | `web/settings/settings.html` (de usuario) | **Falta** la variante ADMIN |
| Pestaña Actividad y filtro por hogar en Notificaciones | Web y móvil | `web/notifications/*.html`, `mobile/notifications/notifications.html` | Desactualizado: solo tres pestañas |
| Ventana de términos y privacidad en el registro | Web | `web/auth/register-legal-modal.html` | Vigente |
| Ventana de términos y privacidad en el registro | Móvil | — | **Falta** (`mockup/layout/mobile/auth/register-legal-modal.html`) |
| Ayuda permanente de reglas de contraseña | Web y móvil | `*/auth/register.html` | Revisar que muestre la ayuda permanente |
| Transferir propiedad (confirmación con casilla) | Web y móvil | `*/home-detail/*users.html` | **Falta** el modal |
| Eliminar hogar escribiendo su nombre | Web y móvil | `web/home-detail/owner/home.html`, `mobile/home-detail/home-detail-home.html` | **Falta** el modal |
| Vinculación por Bluetooth (asistente de pasos) | Móvil | `mobile/home-detail/home-detail-link-device.html` | Desactualizado: mostraba el código escrito a mano |
| Cambiar red Wi-Fi y editar dispositivo | Web y móvil | — | **Falta** |
| Eliminar cuenta de Google con código | Web y móvil | `*/account/account-delete-modal.html` | Desactualizado: solo pide contraseña |
| Títulos de página en Ajustes y Mi cuenta, iconos en lugar de emojis | Móvil | `mobile/settings/settings.html`, `mobile/account/account.html` | Desactualizado |
| Soporte con contacto de administradores y pie de página | Web | `web/landing/support.html` | Revisar |
| `mobile/dashboard/homes.html` | Móvil | Existe | Obsoleto: la ruta `/homes` ya no es una pantalla |

## 7. Artefactos relacionados

| **Artefacto** | **Ubicación** |
|---|---|
| Mockups en HTML (web y móvil) | `03-analisis-software/mockup/layout/web/` y `03-analisis-software/mockup/layout/mobile/` |
| Mapas de navegación gráficos (Mermaid) | `03-analisis-software/ES/navegacion/mapa-navegacion-web.md` y `03-analisis-software/ES/navegacion/mapa-navegacion-movil.md` |
| Diseño de interfaces gráficas | `05-diseno-software/ES/05.5-interfaces-graficas.md` |
| Diseño de la arquitectura de presentación | `05-diseno-software/ES/05.1-modelo-arquitectonico.md`, sección 6 |
| Versión en inglés | `03-analisis-software/EN/navigation/` |