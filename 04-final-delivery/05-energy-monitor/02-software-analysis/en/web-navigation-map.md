# Navigation map — Web application

**Project:** Electricity Consumption Monitor — EnergyMonitor
**Record:** 3145556 — Software Analysis and Development (ADSO), SENA CIES Regional Huila
**Version:** 1.1 (October 27, 2026: administrator role, guest, permission and user-only guards, activity notifications, legal window and account inside the protected zone)

---

## 1. Map 1 — General flow

Walks through the application from entry to exit, showing the public zone, the authentication guard and the side menu destinations.

```mermaid
---
title: General flow — Web application
---
flowchart TD

  START([" Entry: browser URL "])
  REDIR{"Was the root route requested?"}
  START --> REDIR
  REDIR -->|"yes"| HOME
  REDIR -->|no| GUARD

  HOME["Welcome page<br/>/home"]
  ABOUT["About us<br/>/about"]
  SUPPORT["Support and FAQ<br/>/support"]

  HOME --> ABOUT
  HOME --> SUPPORT
  HOME --> LOGIN
  HOME --> REG

  LOGIN["Log in<br/>email and password<br/>/login"]
  REG["Sign up<br/>name, email and password<br/>/register"]
  LEGAL["Accept the terms of use<br/>and the privacy policy<br/>legal window with two tabs"]
  VERIFY["Email verification<br/>6-digit code<br/>/VerifyAccount"]
  FORGOT["Request recovery<br/>email only<br/>/recover-password"]
  VRESET["Verify the code and set<br/>the new password<br/>/verifyRecoverPassword"]

  REG --> LEGAL
  LEGAL --> VERIFY
  LOGIN -->|"forgot your password?"| FORGOT
  FORGOT --> VRESET

  VERIFY -.->|"verified account"| LOGIN
  VRESET -.->|"password saved"| LOGIN
  LOGIN -.->|"valid credentials"| ROLE

  GUEST{"GuestRoute<br/>Is there a session already?"}
  GUEST -->|"yes: /login and /register<br/>go to the dashboard"| ROLE

  GUARD{"ProtectedRoute<br/>Is there a session?"}
  GUARD -->|no| LOGIN
  GUARD -->|yes| ROLE
  ROLE{"Does the account have<br/>the ADMIN role?"}
  ROLE -->|no| SIDE
  ROLE -->|yes| ADMIN
  ADMIN["ADMINISTRATION<br/>/admin/users<br/>see Map 4"]

  SIDE["SIDE MENU<br/>present throughout the private zone"]

  NINICIO["Home"]
  NHOGAR["My homes<br/>expandable accordion"]
  NFAV["Favorites"]
  NNOTIF["Notifications<br/>pending badge"]
  NSET["Settings"]
  NPERFIL["Profile<br/>avatar with menu"]

  SIDE --> NINICIO
  SIDE --> NHOGAR
  SIDE --> NFAV
  SIDE --> NNOTIF
  SIDE --> NSET
  SIDE --> NPERFIL

  NPERFIL -->|"Log out"| LOGOUT
  SIDE -.->|"session expired"| LOGOUT
  LOGOUT(["Log out<br/>return to /login"])

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

**Figure 1.** [Download PNG image](../images/web-navigation-map-1.png)

## 2. Map 2 — Dashboard and home detail

Details the main screen and the six sections of a home detail, with the actions that the role allows.

```mermaid
---
title: Dashboard and home detail — Web application
---
flowchart TD

  ENTRY([ From the side menu: Home or a home card ])
  ENTRY --> DASH

  subgraph ZDASH["DASHBOARD — /dashboard"]
    direction TB
    DASH["Dashboard"]
    DSTATE{"List state"}
    DLOAD["Loading<br/>indicator with text"]
    DERR["Error<br/>translated message and Retry button"]
    DEMPTY["No homes<br/>invitation to create the first one"]
    DCARD["Home cards<br/>name · description · favorite"]
    DPLUS["+ button in the header<br/>quick action menu"]
    DCREATE["Create home<br/>name · type · address · description"]
    DJOIN["Join a home<br/>8-character code"]
    DFAV["Mark or unmark as favorite<br/>the value is confirmed by the server"]

    DASH --> DSTATE
    DSTATE -->|loading| DLOAD
    DSTATE -->|error| DERR
    DSTATE -->|no homes| DEMPTY
    DSTATE -->|with homes| DCARD
    DERR -.->|retry| DASH
    DEMPTY --> DCREATE
    DPLUS --> DCREATE
    DPLUS --> DJOIN
    DCARD --> DFAV
  end

  DERR -.->|"retry"| DCREATE
  DEMPTY -.->|"create the first one"| DJOIN
  DCARD -->|"tap the card"| DG

  DG{"HomeRoute<br/>Does the home exist<br/>and is it accessible?"}
  DG -->|no| NOTFOUND["Home not found<br/>and a button back to the dashboard"]
  DG -->|yes| DETAIL

  subgraph ZTABS["HOME DETAIL — /homes/:homeId"]
    direction TB
    DETAIL["Home detail<br/>receives the OWNER or MEMBER role by context"]
    TABS{"Six-tab bar"}

    TC["Consumption"]
    TH["History"]
    TU["Users"]
    TD["Devices"]
    TT["Thresholds"]
    THOME["Home"]

    DETAIL --> TABS
    TABS --> TC
    TABS --> TH
    TABS --> TU
    TABS --> TD
    TABS --> TT
    TABS --> THOME

    TCG["Indicators: current power,<br/>today's consumption, active devices"]
    TCG2["Charts: 24 h area,<br/>donut and bars per device"]
    TCL["Progress bars<br/>for the daily and monthly limit"]
    THG["Period chart<br/>and ranking table"]
    TUO["Owner: remove a member<br/>and transfer ownership"]
    TDO["Owner: link via Bluetooth,<br/>edit, change Wi-Fi and unlink"]
    TTS["Default values switch<br/>and saving of thresholds"]
    THP["Owner: copy the access code<br/>and delete the home typing its name"]
    THM["Member: leave the home"]

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

**Figure 2.** [Download PNG image](../images/web-navigation-map-2.png)

## 3. Map 3 — Auxiliary screens

Details the favorites, notifications, settings and account screens, the latter opening as a modal.

```mermaid
---
title: Auxiliary screens — Web application
---
flowchart TD

  MENU([ From the side menu or the profile ])

  FAVS["Favorites<br/>/favorites"]
  FAVEMPTY["Empty state<br/>or cards of favorite homes"]
  FAVS --> FAVEMPTY

  NOTIF["Notifications<br/>/notifications"]
  NTABS{"Four tabs with a counter<br/>and a filter by home or account"}
  NALL["All"]
  NAL["Alerts"]
  NREC["Recommendations"]
  NACTV["Activity<br/>account, homes and devices"]
  NACT["Mark as read · delete<br/>Mark all as read<br/>Delete resolved or read"]
  NOTIF --> NTABS
  NTABS --> NALL
  NTABS --> NAL
  NTABS --> NREC
  NTABS --> NACTV
  NACTV --> NACT
  NALL --> NACT
  NAL --> NACT
  NREC --> NACT

  SETTINGS["Settings<br/>/settings"]
  SETTEMA["Appearance<br/>four themes with preview"]
  SETIDIOMA["Language<br/>es · en · fr · pt"]
  SETAVISO["Notifications<br/>email · browser push · combined"]
  SETAYUDA["Help<br/>documentation, support and news"]
  SETTINGS --> SETTEMA
  SETTINGS --> SETIDIOMA
  SETTINGS --> SETAVISO
  SETTINGS --> SETAYUDA

  ACCOUNT["My account<br/>/account (requires a session)"]
  ACCED["Edit first and last name"]
  ACCFOTO["Change photo<br/>resize and convert to data URL"]
  ACCPASS["Change password"]
  ACCDEL["Delete account<br/>password, or a mailed code<br/>for a Google account"]
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

**Figure 3.** [Download PNG image](../images/web-navigation-map-3.png)

## 3.1 Map 4 — Administration (ADMIN role)

An account with the global `ADMIN` role only sees the administration, its notifications, the settings and its account. `UserOnlyRoute` sends it back to `/admin/users` when it tries to open the home screens, and `RequirePermission` sends to the dashboard whoever lacks `USER.READ`.

```mermaid
---
title: Administration — Web application
---
flowchart TD

  AENTRY([" Sign-in of an ADMIN account "])
  APERM{"RequirePermission<br/>Has USER.READ?"}
  AENTRY --> APERM
  APERM -->|no| ADASH["Homes dashboard<br/>/dashboard"]
  APERM -->|yes| AUSERS

  AUSERS["Platform users<br/>/admin/users"]
  ASEARCH["Search by name or email<br/>pages of 20"]
  ASTATUS["Change status<br/>Active · Inactive · Blocked<br/>(USER.MANAGE)"]
  ACONF1["Confirm the change<br/>when leaving Active"]
  AROLE["Grant or revoke ADMIN<br/>(ROLE.MANAGE)"]
  ACONF2["Confirm the role"]
  AUSERS --> ASEARCH
  AUSERS --> ASTATUS
  ASTATUS --> ACONF1
  AUSERS --> AROLE
  AROLE --> ACONF2

  ASIDE["ADMINISTRATOR SIDE MENU"]
  ANOTIF["Administration notifications<br/>/notifications"]
  ANACT["Mark read · all · delete"]
  ASET["Settings<br/>language, theme and help<br/>no notification preferences"]
  AACC["My account<br/>/account"]
  AUSERGUARD{"UserOnlyRoute<br/>/dashboard, /homes/:id, /favorites"}
  AUSERS --> ASIDE
  ASIDE --> ANOTIF
  ANOTIF --> ANACT
  ASIDE --> ASET
  ASIDE --> AACC
  ASIDE -.->|"tries to open homes"| AUSERGUARD
  AUSERGUARD -.->|"redirects"| AUSERS
  ASIDE -->|"Sign out"| AOUT(["Back to /login"])

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

**Figure 4.** [Download PNG image](../images/web-navigation-map-4.png)

## 4. How to read the maps

| **Symbol** | **Meaning** |
|---|---|
| Rounded blue node | Entry point |
| Green node | Module or main area of the application |
| White node | Screen, section or specific action |
| Amber node | Navigation guard or state decision |
| Red node | End of flow |
| Solid arrow | Direct navigation |
| Dotted arrow | Return, retry or log out |

## 5. Navigation control points

| **Control point** | **Behavior** |
|---|---|
| Redirection of `/` | The root route sends to `/home`; there is no intermediate screen |
| `GuestRoute` | With an active session, `/login` and `/register` go straight to the dashboard |
| `ProtectedRoute` | Without a session, every private route (including `/account`) redirects to `/login` |
| `UserOnlyRoute` | An ADMIN account opening `/dashboard`, `/homes/:homeId` or `/favorites` goes back to `/admin/users` |
| `RequirePermission` | Without the `USER.READ` permission, `/admin/users` redirects to `/dashboard` (the API answers 403 anyway) |
| `HomeRoute` | If the home in the URL does not exist or is not accessible, it shows «Home not found» with a back button |
| `OWNER` or `MEMBER` role | The detail receives the role through the route context and shows or hides the management actions |
| Expired session | The HTTP client clears the session and sends the browser to `/login` a single time |
| Favorite | The value returned by the server is applied, not a local toggle |

## 6. Zones and routes

| **Zone** | **Routes** | **Screens** |
|---|---|---|
| Public | `/home`, `/about`, `/support`, `/login`, `/register`, `/VerifyAccount`, `/recover-password`, `/verifyRecoverPassword` | 8 |
| Private (user) | `/dashboard`, `/homes/:homeId`, `/favorites` | 3 |
| Private (user and administrator) | `/notifications`, `/settings`, `/account` | 3 |
| Administration | `/admin/users` | 1 |
| Any other route | `*` | Not found page |
| **Total** | | **15 + `*`** |

**Note:** `/account` is now **inside** `ProtectedRoute`: without a session it redirects to `/login`.

## 7. Private screen layout

The private screens share the same layout:

- **Left column — side menu:** Home · My homes · Favorites · Notifications · Settings
- **Lower left column — profile:** avatar with My account and Sign out (ADMIN account side menu: Administration · Notifications · Settings)
- **Right column — header:** breadcrumbs and the screen action button
- **Right column — body:** the screen content, with its own scrolling

At 1024 px or less, the side menu becomes a sliding layer with a darkened backdrop and a fixed menu button.

## 8. Related artifacts

| **Artifact** | **Location** |
|---|---|
| Mobile navigation map | `mobile-navigation-map.md` |
| Route definition | `Energy-Monitor-application/src/routes/AppRoutes.jsx` |
| Side menu | `Energy-Monitor-application/src/components/layout/MainLayout/Sidebar/Sidebar.jsx` |
| Interface design | `05-diseno-software/ES/05.5-interfaces-graficas.md`, section 4 |