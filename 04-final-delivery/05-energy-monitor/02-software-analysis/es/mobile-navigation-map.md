# Mapa de navegación — Aplicación móvil

**Proyecto:** Monitor de Consumo Eléctrico — EnergyMonitor
**Ficha:** 3145556 — Análisis y Desarrollo de Software (ADSO), SENA CIES Regional Huila
**Versión:** 1.1 (27 de octubre de 2026: ventana legal en el registro, vinculación por Bluetooth, transferencia y eliminación del hogar, pestaña Actividad, `/homes` redirige al panel; la app no tiene administración)

---

## 1. Mapa 1 — Flujo general

Recorre la aplicación desde la apertura hasta el cierre de sesión, mostrando la restauración de la sesión, la guarda de autenticación y los destinos del menú lateral.

```mermaid
---
title: Flujo general — Aplicación móvil
---
flowchart TD

  OPEN([" Apertura de la aplicación "])
  OPEN --> RESTORE

  RESTORE{"¿Restaurando la sesión<br/>desde el almacenamiento?"}
  RESTORE -->|sí| ESPERA["Pantalla en blanco<br/>evita el parpadeo"]
  RESTORE -->|no| GATE

  GATE{"AuthGate<br/>¿hay sesión válida?"}
  GATE -->|no| LOGIN
  GATE -->|sí| ESPERA
  ESPERA --> DRAWER
  GATE -.->|"sesión caducada"| LOGIN

  LOGINCAB["Cabecera de marca<br/>con degradado"]
  LOGINCAB --> LOGIN
  LOGINCAB --> REG
  LOGINCAB --> FORGOT

  LOGIN["Inicio de sesión<br/>correo y contraseña<br/>/"]
  REG["Registrarse<br/>/register<br/>ventana de términos y privacidad"]
  VERIFY["Verificación de correo<br/>código de 6 dígitos<br/>/verify-account"]
  FORGOT["Solicitar recuperación<br/>/recover-password"]
  VREC["Verificar código de recuperación<br/>/verify-recover-password"]
  NEWPASS["Definir nueva contraseña<br/>/new-password"]
  LOGINERR["Credenciales inválidas<br/>mensaje traducido"]

  REG -->|"envía el código por correo"| VERIFY
  FORGOT -->|"envía el código por correo"| VREC
  VREC --> NEWPASS

  VERIFY -.->|"cuenta verificada"| LOGIN
  NEWPASS -.->|"contraseña guardada"| LOGIN
  LOGIN -.->|"credenciales válidas"| DRAWER
  LOGIN --> LOGINERR

  DRAWER["MENÚ LATERAL<br/>capa deslizable animada"]

  NINICIO["Inicio"]
  NHOGAR["Mis hogares<br/>acordeón desplegable"]
  NFAV["Favoritos"]
  NNOTIF["Notificaciones<br/>insignia de pendientes"]
  NSET["Ajustes"]
  NPERFIL["Perfil<br/>avatar con menú"]

  DRAWER --> NINICIO
  DRAWER --> NHOGAR
  DRAWER --> NFAV
  DRAWER --> NNOTIF
  DRAWER --> NSET
  DRAWER --> NPERFIL

  NPERFIL -->|"Cerrar sesión"| LOGOUT
  DRAWER -.->|"sesión caducada"| LOGOUT
  LOGOUT(["Cierre de sesión<br/>regreso a /"])

  OPEN:::start
  LOGOUT:::exit
  LOGINERR:::exit
  classDef start fill:#eaf4ff,stroke:#4a90d9,stroke-width:2px,color:#123;
  classDef exit fill:#fcebeb,stroke:#d97b7b,stroke-width:2px,color:#5c1f1f;
  class DRAWER module
  classDef module fill:#d6f0e4,stroke:#6fbf9a,stroke-width:2px,color:#124;
  class RESTORE,GATE guard
  classDef guard fill:#fff4d6,stroke:#e0a800,stroke-width:2px,color:#4a3b00;
  class LOGINCAB,LOGIN,REG,VERIFY,FORGOT,VREC,NEWPASS,ESPERA,NINICIO,NHOGAR,NFAV,NNOTIF,NSET,NPERFIL screen
  classDef screen fill:#ffffff,stroke:#8a8a8a,color:#222;
```

**Figura 1.** [Descargar imagen en PNG](../images/mapa-navegacion-movil-1.png)

## 2. Mapa 2 — Panel principal y detalle del hogar

Detalla la pantalla principal y las seis secciones del detalle de un hogar, con las acciones que el rol permite.

```mermaid
---
title: Panel principal y detalle del hogar — Aplicación móvil
---
flowchart TD

  ENTRY([ Desde el menú lateral: Inicio o tarjeta de un hogar ])
  ENTRY --> DASH

  subgraph ZDASH["PANEL PRINCIPAL — /dashboard"]
    direction TB
    DASH["Panel principal"]
    DSTATE{"Estado de la lista"}
    DLOAD["Cargando<br/>indicador con texto"]
    DERR["Error<br/>mensaje traducido y botón Reintentar"]
    DEMPTY["Sin hogares<br/>invitación a crear el primero"]
    DCARD["Tarjetas de hogares<br/>nombre · descripción · favorito"]
    DPLUS["Botón + de la cabecera<br/>menú de acción rápida"]
    DCREATE["Crear hogar<br/>nombre · tipo · dirección · descripción"]
    DJOIN["Unirse a un hogar<br/>código de 8 caracteres"]
    DFAV["Marcar o desmarcar favorito<br/>el valor confirma el servidor"]

    DASH --> DSTATE
    DSTATE -->|cargando| DLOAD
    DSTATE -->|error| DERR
    DSTATE -->|sin hogares| DEMPTY
    DSTATE -->|con hogares| DCARD
    DERR -.->|reintento| DASH
    DEMPTY --> DCREATE
    DPLUS --> DCREATE
    DPLUS --> DJOIN
    DCARD --> DFAV
  end

  DERR -.->|"reintento"| DCREATE
  DEMPTY -.->|"crear el primero"| DJOIN
  DCARD -->|"tocar la tarjeta"| DG

  DG{"¿Existe el hogar<br/>en el contexto?"}
  DG -->|no| NOTFOUND["Hogar no encontrado<br/>y botón hacia el panel"]
  DG -->|sí| DETAIL

  subgraph ZTABS["DETALLE DEL HOGAR — /home/:id"]
    direction TB
    DETAIL["Detalle del hogar<br/>cabecera con nombre y botón atrás"]
    TABS{"Barra inferior<br/>de seis secciones"}

    TC["Consumo"]
    TH["Historial"]
    TU["Usuarios"]
    TD["Dispositivos"]
    TT["Umbrales"]
    THOME["Hogar"]

    DETAIL --> TABS
    TABS --> TC
    TABS --> TH
    TABS --> TU
    TABS --> TD
    TABS --> TT
    TABS --> THOME

    TCG["Indicadores: potencia actual,<br/>consumo de hoy, dispositivos activos"]
    TCG2["Gráficas: área de 24 h,<br/>circular y barras por dispositivo"]
    TCL["Barras de avance<br/>del límite diario y mensual"]
    THG["Barras del periodo<br/>y tabla de ranking"]
    TUO["Propietario: quitar miembro<br/>y transferir la propiedad"]
    TDO["Propietario: vincular por Bluetooth (BLE),<br/>editar, cambiar Wi-Fi y desvincular"]
    TTS["Interruptor de valores predeterminados<br/>y guardado de límites"]
    THP["Propietario: eliminar el hogar"]
    THM["Miembro: salir del hogar"]

    TC --> TCG
    TC --> TCG2
    TC --> TCL
    TH --> THG
    TU --> TUO
    TD --> TDO
    TT --> TTS
    THOME --> THP
    THOME --> THM
  end

  DETAIL -->|"botón atrás"| DASH
  NOTFOUND --> DASH

  ENTRY:::start
  NOTFOUND:::exit
  classDef start fill:#eaf4ff,stroke:#4a90d9,stroke-width:2px,color:#123;
  classDef exit fill:#fcebeb,stroke:#d97b7b,stroke-width:2px,color:#5c1f1f;
  class DASH,DETAIL module
  classDef module fill:#d6f0e4,stroke:#6fbf9a,stroke-width:2px,color:#124;
  class DSTATE,DG,TABS guard
  classDef guard fill:#fff4d6,stroke:#e0a800,stroke-width:2px,color:#4a3b00;
  class DLOAD,DERR,DEMPTY,DCARD,DPLUS,DCREATE,DJOIN,DFAV,TC,TH,TU,TD,TT,THOME,TCG,TCG2,TCL,THG,TUO,TDO,TTS,THP,THM screen
  classDef screen fill:#ffffff,stroke:#8a8a8a,color:#222;
```

**Figura 2.** [Descargar imagen en PNG](../images/mapa-navegacion-movil-2.png)

## 3. Mapa 3 — Pantallas auxiliares

Detalla las pantallas de favoritos, notificaciones, configuración y de cuenta.

```mermaid
---
title: Pantallas auxiliares — Aplicación móvil
---
flowchart TD

  MENU([ Desde el menú lateral o el menú del perfil ])

  FAVS["Favoritos<br/>/favorites"]
  FAVEMPTY["Estado vacío<br/>o tarjetas de hogares favoritos"]
  FAVS --> FAVEMPTY

  NOTIF["Notificaciones<br/>/notifications"]
  NTABS{"Cuatro pestañas con contador<br/>y filtro por hogar o cuenta"}
  NALL["Todas"]
  NAL["Alertas"]
  NREC["Recomendaciones"]
  NACTV["Actividad"]
  NACT["Marcar leída · eliminar<br/>Marcar todas como leídas"]
  NOTIF --> NTABS
  NTABS --> NALL
  NTABS --> NAL
  NTABS --> NREC
  NTABS --> NACTV
  NACTV --> NACT
  NALL --> NACT
  NAL --> NACT
  NREC --> NACT

  SETTINGS["Ajustes<br/>/settings"]
  SETTEMA["Apariencia<br/>cuatro temas con vista previa"]
  SETIDIOMA["Idioma<br/>es · en · fr · pt"]
  SETAVISO["Notificaciones<br/>correo · push de navegadores · combinado"]
  SETAYUDA["Ayuda<br/>documentación, soporte y novedades"]
  SETTINGS --> SETTEMA
  SETTINGS --> SETIDIOMA
  SETTINGS --> SETAVISO
  SETTINGS --> SETAYUDA

  ACCOUNT["Mi cuenta<br/>/account, con título de página"]
  ACCED["Editar nombre y apellido"]
  ACCFOTO["Cambiar fotografía<br/>seleccionar de la galería"]
  ACCPASS["Cambiar contraseña"]
  ACCDEL["Eliminar cuenta<br/>contraseña o código por correo<br/>si la cuenta es de Google"]
  ACCOUNT --> ACCED
  ACCOUNT --> ACCFOTO
  ACCOUNT --> ACCPASS
  ACCOUNT --> ACCDEL

  MENU --> FAVS
  MENU --> NOTIF
  MENU --> SETTINGS
  MENU --> ACCOUNT

  FAVS:::module
  NOTIF:::module
  SETTINGS:::module
  ACCOUNT:::module
  classDef module fill:#d6f0e4,stroke:#6fbf9a,stroke-width:2px,color:#124;
  class MENU start
  classDef start fill:#eaf4ff,stroke:#4a90d9,stroke-width:2px,color:#123;
  class NTABS guard
  classDef guard fill:#fff4d6,stroke:#e0a800,stroke-width:2px,color:#4a3b00;
  class FAVEMPTY,NALL,NAL,NREC,NACT,SETTEMA,SETIDIOMA,SETAVISO,SETAYUDA,ACCED,ACCFOTO,ACCPASS,ACCDEL screen
  classDef screen fill:#ffffff,stroke:#8a8a8a,color:#222;
```

**Figura 3.** [Descargar imagen en PNG](../images/mapa-navegacion-movil-3.png)

## 4. Cómo leer los mapas

| **Símbolo** | **Significado** |
|---|---|
| Nodo azul redondeado | Punto de entrada |
| Nodo verde | Módulo o área principal de la aplicación |
| Nodo blanco | Pantalla, sección o acción concreta |
| Nodo ámbar | Guarda de navegación o decisión de estado |
| Nodo rojo | Fin de flujo o error de acceso |
| Flecha sólida | Navegación directa |
| Flecha punteada | Regreso, reintento o cierre de sesión |

## 5. Puntos de control de la navegación

| **Punto de control** | **Comportamiento** |
|---|---|
| `AuthGate` | Envuelve toda la pila. Define seis rutas públicas; cualquier otra exige sesión |
| Restauración de sesión | Mientras lee la sesión del almacenamiento no renderiza nada, para evitar el parpadeo |
| Sesión caducada | El cliente HTTP **no** navega: avisa al guarda, que devuelve al inicio de sesión |
| Botón atrás del detalle | Si la pila tiene pantalla anterior, retrocede; si se entró por enlace directo, vuelve al panel |
| Rol `OWNER` o `MEMBER` | El detalle recibe el rol y muestra u oculta las acciones de gestión |
| Parámetros entre pantallas | El correo viaja en la URL (`?email=`); el código de recuperación viaja en memoria, no en la URL |

## 6. Rutas

| **Zona** | **Ruta** | **Pantalla** |
|---|---|---|
| Pública | `/` | Inicio de sesión |
| Pública | `/register` | Registrarse |
| Pública | `/verify-account` | Verificación de correo |
| Pública | `/recover-password` | Solicitar recuperación |
| Pública | `/verify-recover-password` | Verificar código de recuperación |
| Pública | `/new-password` | Definir nueva contraseña |
| Privada | `/dashboard` | Panel principal |
| Privada | `/homes` | Redirige a `/dashboard` (ya no es una pantalla) |
| Privada | `/home/[id]` | Detalle del hogar |
| Privada | `/favorites` | Favoritos |
| Privada | `/notifications` | Notificaciones |
| Privada | `/settings` | Ajustes |
| Privada | `/account` | Cuenta |

**Total: 13 rutas · 12 pantallas funcionales.** La app no tiene landing page ni pantallas de administración.

## 7. Estructura del menú lateral

Orden de los elementos, de arriba hacia abajo:

| **Posición** | **Elemento** | **Destino** |
|---|---|---|
| Cabecera | Logotipo y nombre de la aplicación | — |
| 1 | **Inicio** | `/dashboard` |
| 2 | **Mis hogares** — acordeón con la lista de hogares del usuario | `/home/:id` |
| 3 | **Favoritos** | `/favorites` |
| 4 | **Notificaciones** — con insignia de pendientes | `/notifications` |
| 5 | **Ajustes** | `/settings` |
| Pie | **Perfil** — avatar y nombre | Mi cuenta · Cerrar sesión |

La capa se abre con un botón fijo en la esquina superior izquierda y se cierra al tocar el fondo oscurecido o con el botón «atrás» de Android, con una animación de 300 milisegundos. Cerrada, queda oculta para el lector de pantalla.

## 8. Diferencias con la navegación web

| **Aspecto** | **Web** | **Móvil** |
|---|---|---|
| Página de bienvenida | Sí (`/home`) | No: abre en el inicio de sesión |
| Recuperación de contraseña | 2 rutas | 3 rutas |
| Pantalla de cuenta | Pantalla `/account` dentro de la zona protegida | Pantalla propia (`/account`) con título |
| Administración | Sí (`/admin/users`, rol ADMIN) | No |
| Menú lateral | Fijo o plegable | Capa deslizable con animación de 300 ms |
| Secciones del detalle | Pestañas horizontales superiores | Barra inferior con iconos |
| Alta de dispositivo | Web Bluetooth (Chrome o Edge) | Bluetooth nativo del teléfono (BLE) |
| Total de rutas | 16 + página no encontrada | 13 |

## 9. Artefactos relacionados

| **Artefacto** | **Ubicación** |
|---|---|
| Mapa de navegación web | `mapa-navegacion-web.md` |
| Mapa de navegación analítico (árbol, matriz de acceso, flujos y puntos débiles) | `../navigation-map-mobile.md` |
| Guarda de navegación | `Energy-Monitor-Mobile/src/components/layout/AuthGate/AuthGate.tsx` |
| Menú lateral | `Energy-Monitor-Mobile/src/components/layout/Sidebar/Sidebar.tsx` |
| Barra inferior | `Energy-Monitor-Mobile/src/components/layout/BottomTabBar/BottomTabBar.tsx` |
| Diseño de interfaces | `05-diseno-software/ES/05.5-interfaces-graficas.md`, sección 5 |