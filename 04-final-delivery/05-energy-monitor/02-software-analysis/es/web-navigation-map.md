# Mapa de navegación — Aplicación web

**Proyecto:** Monitor de Consumo Eléctrico — EnergyMonitor
**Ficha:** 3145556 — Análisis y Desarrollo de Software (ADSO), SENA CIES Regional Huila
**Versión:** 1.1 (27 de octubre de 2026: rol administrador, guardas de invitado, de permiso y de solo usuario, notificaciones de actividad, ventana legal y cuenta dentro de la zona protegida)

---

## 1. Mapa 1 — Flujo general

Recorre la aplicación desde la entrada hasta la salida, mostrando la zona pública, la guarda de autenticación y los destinos del menú lateral.

```mermaid
---
title: Flujo general — Aplicación web
---
flowchart TD

  START([" Entrada: URL del navegador "])
  REDIR{"¿Se pidió la ruta raíz?"}
  START --> REDIR
  REDIR -->|"sí"| HOME
  REDIR -->|no| GUARD

  HOME["Página de bienvenida<br/>/home"]
  ABOUT["Sobre nosotros<br/>/about"]
  SUPPORT["Soporte y preguntas frecuentes<br/>/support"]

  HOME --> ABOUT
  HOME --> SUPPORT
  HOME --> LOGIN
  HOME --> REG

  LOGIN["Iniciar sesión<br/>correo y contraseña<br/>/login"]
  REG["Registrarse<br/>nombre, correo y contraseña<br/>/register"]
  LEGAL["Aceptar términos de uso<br/>y política de privacidad<br/>ventana legal con dos pestañas"]
  VERIFY["Verificación de correo<br/>código de 6 dígitos<br/>/VerifyAccount"]
  FORGOT["Solicitar recuperación<br/>solo el correo<br/>/recover-password"]
  VRESET["Verificar código y definir<br/>la nueva contraseña<br/>/verifyRecoverPassword"]

  REG --> LEGAL
  LEGAL --> VERIFY
  LOGIN -->|"¿olvidaste tu contraseña?"| FORGOT
  FORGOT --> VRESET

  VERIFY -.->|"cuenta verificada"| LOGIN
  VRESET -.->|"contraseña guardada"| LOGIN
  LOGIN -.->|"credenciales válidas"| ROLE

  GUEST{"GuestRoute<br/>¿ya hay sesión?"}
  GUEST -->|"sí: /login y /register<br/>llevan al panel"| ROLE
  GUARD{"ProtectedRoute<br/>¿existe sesión?"}
  GUARD -->|no| LOGIN
  GUARD -->|sí| ROLE
  ROLE{"¿La cuenta tiene<br/>el rol ADMIN?"}
  ROLE -->|no| SIDE
  ROLE -->|sí| ADMIN
  ADMIN["ADMINISTRACIÓN<br/>/admin/users<br/>ver Mapa 4"]

  SIDE["MENÚ LATERAL<br/>presente en toda la zona privada"]

  NINICIO["Inicio"]
  NHOGAR["Mis hogares<br/>acordeón desplegable"]
  NFAV["Favoritos"]
  NNOTIF["Notificaciones<br/>insignia de pendientes"]
  NSET["Ajustes"]
  NPERFIL["Perfil<br/>avatar con menú"]

  SIDE --> NINICIO
  SIDE --> NHOGAR
  SIDE --> NFAV
  SIDE --> NNOTIF
  SIDE --> NSET
  SIDE --> NPERFIL

  NPERFIL -->|"Cerrar sesión"| LOGOUT
  SIDE -.->|"sesión caducada"| LOGOUT
  LOGOUT(["Cierre de sesión<br/>regreso a /login"])

  START:::start
  LOGOUT:::exit
  classDef start fill:#eaf4ff,stroke:#4a90d9,stroke-width:2px,color:#123;
  classDef exit fill:#fcebeb,stroke:#d97b7b,stroke-width:2px,color:#5c1f1f;
  class SIDE,ADMIN module
  classDef module fill:#d6f0e4,stroke:#6fbf9a,stroke-width:2px,color:#124;
  class REDIR,GUARD,GUEST,ROLE guard
  classDef guard fill:#fff4d6,stroke:#e0a800,stroke-width:2px,color:#4a3b00;
  class HOME,ABOUT,SUPPORT,LOGIN,REG,LEGAL,VERIFY,FORGOT,VRESET,NINICIO,NHOGAR,NFAV,NNOTIF,NSET,NPERFIL screen
  classDef screen fill:#ffffff,stroke:#8a8a8a,color:#222;
```

**Figura 1.** [Descargar imagen en PNG](../images/mapa-navegacion-web-1.png)

## 2. Mapa 2 — Panel de hogares y detalle del hogar

Detalla la pantalla principal y las seis secciones del detalle de un hogar, con las acciones que el rol permite.

```mermaid
---
title: Panel de hogares y detalle del hogar — Aplicación web
---
flowchart TD

  ENTRY([ Desde el menú lateral: Inicio o tarjeta de un hogar ])
  ENTRY --> DASH

  subgraph ZDASH["PANEL DE HOGARES — /dashboard"]
    direction TB
    DASH["Panel de hogares"]
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

  DG{"HomeRoute<br/>¿existe el hogar<br/>y es accesible?"}
  DG -->|no| NOTFOUND["Hogar no encontrado<br/>y botón de regreso al panel"]
  DG -->|sí| DETAIL

  subgraph ZTABS["DETALLE DEL HOGAR — /homes/:homeId"]
    direction TB
    DETAIL["Detalle del hogar<br/>recibe el rol OWNER o MEMBER por contexto"]
    TABS{"Barra de seis pestañas"}

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
    THG["Gráfica del periodo<br/>y tabla de ranking"]
    TUO["Propietario: quitar miembro<br/>y transferir la propiedad"]
    TDO["Propietario: vincular por Bluetooth,<br/>editar, cambiar Wi-Fi y desvincular"]
    TTS["Interruptor de valores predeterminados<br/>y guardado de límites"]
    THP["Propietario: copiar código de acceso<br/>y eliminar el hogar escribiendo su nombre"]
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

**Figura 2.** [Descargar imagen en PNG](../images/mapa-navegacion-web-2.png)

## 3. Mapa 3 — Pantallas auxiliares

Detalla las pantallas de favoritos, notificaciones, configuración y la de cuenta, que se abre como modal.

```mermaid
---
title: Pantallas auxiliares — Aplicación web
---
flowchart TD

  MENU([ Desde el menú lateral o el perfil ])

  FAVS["Favoritos<br/>/favorites"]
  FAVEMPTY["Estado vacío<br/>o tarjetas de hogares favoritos"]
  FAVS --> FAVEMPTY

  NOTIF["Notificaciones<br/>/notifications"]
  NTABS{"Cuatro pestañas con contador<br/>y filtro por hogar o cuenta"}
  NALL["Todas"]
  NAL["Alertas"]
  NREC["Recomendaciones"]
  NACTV["Actividad<br/>cuenta, hogares y dispositivos"]
  NACT["Marcar leída · eliminar<br/>Marcar todas como leídas<br/>Eliminar resueltas o leídas"]
  NOTIF --> NTABS
  NTABS --> NALL
  NTABS --> NAL
  NTABS --> NREC
  NTABS --> NACTV
  NALL --> NACT
  NAL --> NACT
  NREC --> NACT
  NACTV --> NACT

  SETTINGS["Ajustes<br/>/settings"]
  SETTEMA["Apariencia<br/>cuatro temas con vista previa"]
  SETIDIOMA["Idioma<br/>es · en · fr · pt"]
  SETAVISO["Notificaciones<br/>correo · push de navegadores · combinado"]
  SETAYUDA["Ayuda<br/>documentación, soporte y novedades"]
  SETTINGS --> SETTEMA
  SETTINGS --> SETIDIOMA
  SETTINGS --> SETAVISO
  SETTINGS --> SETAYUDA

  ACCOUNT["Mi cuenta<br/>/account (requiere sesión)"]
  ACCED["Editar nombre y apellido"]
  ACCFOTO["Cambiar fotografía<br/>redimensiona y convierte a data URL"]
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
  class FAVEMPTY,NALL,NAL,NREC,NACTV,NACT,SETTEMA,SETIDIOMA,SETAVISO,SETAYUDA,ACCED,ACCFOTO,ACCPASS,ACCDEL screen
  classDef screen fill:#ffffff,stroke:#8a8a8a,color:#222;
```

**Figura 3.** [Descargar imagen en PNG](../images/mapa-navegacion-web-3.png)

## 3.1 Mapa 4 — Administración (rol ADMIN)

Una cuenta con el rol global `ADMIN` solo ve la administración, sus notificaciones, los ajustes y su cuenta. `UserOnlyRoute` la devuelve a `/admin/users` si intenta abrir las pantallas de hogares, y `RequirePermission` envía al panel a quien no tiene `USER.READ`.

```mermaid
---
title: Administración — Aplicación web
---
flowchart TD

  AENTRY([" Inicio de sesión de una cuenta ADMIN "])
  APERM{"RequirePermission<br/>¿tiene USER.READ?"}
  AENTRY --> APERM
  APERM -->|no| ADASH["Panel de hogares<br/>/dashboard"]
  APERM -->|sí| AUSERS

  AUSERS["Usuarios de la plataforma<br/>/admin/users"]
  ASEARCH["Buscar por nombre o correo<br/>paginación de 20"]
  ASTATUS["Cambiar estado<br/>Activa · Inactiva · Bloqueada<br/>(USER.MANAGE)"]
  ACONF1["Confirmar el cambio<br/>si deja de estar Activa"]
  AROLE["Dar o quitar el rol ADMIN<br/>(ROLE.MANAGE)"]
  ACONF2["Confirmar el rol"]
  AUSERS --> ASEARCH
  AUSERS --> ASTATUS
  ASTATUS --> ACONF1
  AUSERS --> AROLE
  AROLE --> ACONF2

  ASIDE["MENÚ LATERAL DEL ADMINISTRADOR"]
  ANOTIF["Notificaciones de administración<br/>/notifications"]
  ANACT["Marcar leída · todas · eliminar"]
  ASET["Ajustes<br/>idioma, tema y ayuda<br/>sin preferencias de notificación"]
  AACC["Mi cuenta<br/>/account"]
  AUSERGUARD{"UserOnlyRoute<br/>/dashboard, /homes/:id, /favorites"}
  AUSERS --> ASIDE
  ASIDE --> ANOTIF
  ANOTIF --> ANACT
  ASIDE --> ASET
  ASIDE --> AACC
  ASIDE -.->|"intenta abrir hogares"| AUSERGUARD
  AUSERGUARD -.->|"redirige"| AUSERS
  ASIDE -->|"Cerrar sesión"| AOUT(["Regreso a /login"])

  AENTRY:::start
  AOUT:::exit
  classDef start fill:#eaf4ff,stroke:#4a90d9,stroke-width:2px,color:#123;
  classDef exit fill:#fcebeb,stroke:#d97b7b,stroke-width:2px,color:#5c1f1f;
  class AUSERS,ASIDE module
  classDef module fill:#d6f0e4,stroke:#6fbf9a,stroke-width:2px,color:#124;
  class APERM,AUSERGUARD guard
  classDef guard fill:#fff4d6,stroke:#e0a800,stroke-width:2px,color:#4a3b00;
  class ADASH,ASEARCH,ASTATUS,ACONF1,AROLE,ACONF2,ANOTIF,ANACT,ASET,AACC screen
  classDef screen fill:#ffffff,stroke:#8a8a8a,color:#222;
```

**Figura 4.** [Descargar imagen en PNG](../images/mapa-navegacion-web-4.png)

## 4. Cómo leer los mapas

| **Símbolo** | **Significado** |
|---|---|
| Nodo azul redondeado | Punto de entrada |
| Nodo verde | Módulo o área principal de la aplicación |
| Nodo blanco | Pantalla, sección o acción concreta |
| Nodo ámbar | Guarda de navegación o decisión de estado |
| Nodo rojo | Fin de flujo |
| Flecha sólida | Navegación directa |
| Flecha punteada | Regreso, reintento o cierre de sesión |

## 5. Puntos de control de la navegación

| **Punto de control** | **Comportamiento** |
|---|---|
| Redirección de `/` | La ruta raíz envía a `/home`; no hay pantalla intermedia |
| `GuestRoute` | Con una sesión activa, `/login` y `/register` llevan directo al panel |
| `ProtectedRoute` | Sin sesión, toda ruta privada (incluida `/account`) redirige a `/login` |
| `UserOnlyRoute` | Una cuenta ADMIN que abre `/dashboard`, `/homes/:homeId` o `/favorites` vuelve a `/admin/users` |
| `RequirePermission` | Sin el permiso `USER.READ`, `/admin/users` redirige a `/dashboard` (la API responde 403 de todos modos) |
| `HomeRoute` | Si el hogar de la URL no existe o no es accesible, muestra «Hogar no encontrado» con botón de regreso |
| Rol `OWNER` o `MEMBER` | El detalle recibe el rol por contexto de ruta y muestra u oculta las acciones de gestión |
| Sesión caducada | El cliente HTTP limpia la sesión y envía el navegador a `/login` una sola vez |
| Favorito | Se aplica el valor que devuelve el servidor, no una alternancia local |

## 6. Zonas y rutas

| **Zona** | **Rutas** | **Pantallas** |
|---|---|---|
| Raíz | `/` | Redirección a `/home`, sin pantalla propia |
| Pública | `/home`, `/about`, `/support`, `/login`, `/register`, `/VerifyAccount`, `/recover-password`, `/verifyRecoverPassword` | 8 |
| Privada (usuario) | `/dashboard`, `/homes/:homeId`, `/favorites` | 3 |
| Privada (usuario y administrador) | `/notifications`, `/settings`, `/account` | 3 |
| Administración | `/admin/users` | 1 |
| Cualquier otra ruta | `*` | Página no encontrada |
| **Total de rutas declaradas** | | **16 + `*`** |

**Nota:** desde la versión actual `/account` está **dentro** de `ProtectedRoute`: sin sesión redirige a `/login` (el punto débil que documentaba la versión 1.0 quedó resuelto).

## 7. Perfil de las pantallas privadas

Las pantallas privadas comparten la misma disposición:

- **Columna izquierda — menú lateral:** Inicio · Hogares · Favoritos · Notificaciones · Ajustes (cuenta ADMIN: Administración · Notificaciones · Ajustes)
- **Columna izquierda inferior — perfil:** avatar con Mi cuenta y Cerrar sesión
- **Columna derecha — cabecera:** migas de pan y botón de acción de la pantalla
- **Columna derecha — cuerpo:** el contenido de la pantalla, con desplazamiento propio

En pantallas de 1024 px o menos el menú lateral se convierte en una capa deslizable con fondo oscurecido y botón de menú fijo.

## 8. Artefactos relacionados

| **Artefacto** | **Ubicación** |
|---|---|
| Mapa de navegación móvil | `mapa-navegacion-movil.md` |
| Mapa de navegación analítico (árbol, matriz de acceso, flujos y puntos débiles) | `../navigation-map.md` |
| Definición de rutas | `Energy-Monitor-application/src/routes/AppRoutes.jsx` |
| Menú lateral | `Energy-Monitor-application/src/components/layout/MainLayout/Sidebar/Sidebar.jsx` |
| Diseño de interfaces | `05-diseno-software/ES/05.5-interfaces-graficas.md`, sección 4 |