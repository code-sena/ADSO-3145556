# Navigation map — Mobile application

**Project:** Electricity Consumption Monitor — EnergyMonitor
**Record:** 3145556 — Software Analysis and Development (ADSO), SENA CIES Regional Huila
**Version:** 1.1 (October 27, 2026: legal window at registration, Bluetooth linking, home transfer and deletion, Activity tab, `/homes` redirects to the dashboard; the app has no administration)

---

## 1. Map 1 — General flow

Walks through the application from startup to log out, showing session restoration, the authentication guard and the side menu destinations.

```mermaid
---
title: General flow — Mobile application
---
flowchart TD

  OPEN([" Application startup "])
  OPEN --> RESTORE

  RESTORE{"Restoring the session<br/>from storage?"}
  RESTORE -->|yes| ESPERA["Blank screen<br/>avoids flicker"]
  RESTORE -->|no| GATE

  GATE{"AuthGate<br/>Is there a valid session?"}
  GATE -->|no| LOGIN
  GATE -->|yes| ESPERA
  ESPERA --> DRAWER
  GATE -.->|"session expired"| LOGIN

  LOGINCAB["Branded header<br/>with a gradient"]
  LOGINCAB --> LOGIN
  LOGINCAB --> REG
  LOGINCAB --> FORGOT

  LOGIN["Log in<br/>email and password<br/>/"]
  REG["Sign up<br/>/register<br/>terms and privacy window"]
  VERIFY["Email verification<br/>6-digit code<br/>/verify-account"]
  FORGOT["Request recovery<br/>/recover-password"]
  VREC["Verify the recovery code<br/>/verify-recover-password"]
  NEWPASS["Set a new password<br/>/new-password"]
  LOGINERR["Invalid credentials<br/>translated message"]

  REG -->|"sends the code by email"| VERIFY
  FORGOT -->|"sends the code by email"| VREC
  VREC --> NEWPASS

  VERIFY -.->|"verified account"| LOGIN
  NEWPASS -.->|"password saved"| LOGIN
  LOGIN -.->|"valid credentials"| DRAWER
  LOGIN --> LOGINERR

  DRAWER["SIDE MENU<br/>animated sliding layer"]

  NINICIO["Home"]
  NHOGAR["My homes<br/>expandable accordion"]
  NFAV["Favorites"]
  NNOTIF["Notifications<br/>pending badge"]
  NSET["Settings"]
  NPERFIL["Profile<br/>avatar with menu"]

  DRAWER --> NINICIO
  DRAWER --> NHOGAR
  DRAWER --> NFAV
  DRAWER --> NNOTIF
  DRAWER --> NSET
  DRAWER --> NPERFIL

  NPERFIL -->|"Log out"| LOGOUT
  DRAWER -.->|"session expired"| LOGOUT
  LOGOUT(["Log out<br/>return to /"])

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

**Figure 1.** [Download PNG image](../images/mobile-navigation-map-1.png)

## 2. Map 2 — Main panel and home detail

Details the main screen and the six sections of a home detail, with the actions that the role allows.

```mermaid
---
title: Main panel and home detail — Mobile application
---
flowchart TD

  ENTRY([ From the side menu: Home or a home card ])
  ENTRY --> DASH

  subgraph ZDASH["MAIN PANEL — /dashboard"]
    direction TB
    DASH["Main panel"]
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

  DG{"Does the home exist<br/>in the context?"}
  DG -->|no| NOTFOUND["Home not found<br/>and a button back to the panel"]
  DG -->|yes| DETAIL

  subgraph ZTABS["HOME DETAIL — /home/:id"]
    direction TB
    DETAIL["Home detail<br/>header with name and back button"]
    TABS{"Bottom bar<br/>with six sections"}

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
    THG["Period bars<br/>and ranking table"]
    TUO["Owner: remove a member<br/>and transfer ownership"]
    TDO["Owner: link over Bluetooth (BLE),<br/>edit, change Wi-Fi and unlink"]
    TTS["Default values switch<br/>and saving of thresholds"]
    THP["Owner: delete the home"]
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

  DETAIL -->|"back button"| DASH
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

**Figure 2.** [Download PNG image](../images/mobile-navigation-map-2.png)

## 3. Map 3 — Auxiliary screens

Details the favorites, notifications, settings and account screens.

```mermaid
---
title: Auxiliary screens — Mobile application
---
flowchart TD

  MENU([ From the side menu or the profile menu ])

  FAVS["Favorites<br/>/favorites"]
  FAVEMPTY["Empty state<br/>or cards of favorite homes"]
  FAVS --> FAVEMPTY

  NOTIF["Notifications<br/>/notifications"]
  NTABS{"Four tabs with a counter<br/>and a filter by home or account"}
  NALL["All"]
  NAL["Alerts"]
  NREC["Recommendations"]
  NACT["Mark as read · delete<br/>Mark all as read"]
  NOTIF --> NTABS
  NTABS --> NALL
  NTABS --> NAL
  NTABS --> NREC
  NTABS --> NACTV
  NACTV["Activity"]
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

  ACCOUNT["My account<br/>/account, with a page title"]
  ACCED["Edit first and last name"]
  ACCFOTO["Change photo<br/>select from the gallery"]
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
  class FAVEMPTY,NALL,NAL,NREC,NACT,SETTEMA,SETIDIOMA,SETAVISO,SETAYUDA,ACCED,ACCFOTO,ACCPASS,ACCDEL screen
  classDef screen fill:#ffffff,stroke:#8a8a8a,color:#222;
```

**Figure 3.** [Download PNG image](../images/mobile-navigation-map-3.png)

## 4. How to read the maps

| **Symbol** | **Meaning** |
|---|---|
| Rounded blue node | Entry point |
| Green node | Module or main area of the application |
| White node | Screen, section or specific action |
| Amber node | Navigation guard or state decision |
| Red node | End of flow or access error |
| Solid arrow | Direct navigation |
| Dotted arrow | Return, retry or log out |

## 5. Navigation control points

| **Control point** | **Behavior** |
|---|---|
| `AuthGate` | Wraps the whole stack. Defines six public routes; any other one requires a session |
| Session restoration | While reading the session from storage it renders nothing, to avoid flicker |
| Expired session | The HTTP client **does not** navigate: it warns the guard, which returns to the log in screen |
| Detail back button | If the stack has a previous screen, it goes back; if the entry was through a direct link, it returns to the panel |
| `OWNER` or `MEMBER` role | The detail receives the role and shows or hides the management actions |
| Parameters between screens | The email travels in the URL (`?email=`); the recovery code travels in memory, not in the URL |

## 6. Routes

| **Zone** | **Route** | **Screen** |
|---|---|---|
| Public | `/` | Log in |
| Public | `/register` | Sign up |
| Public | `/verify-account` | Email verification |
| Public | `/recover-password` | Request recovery |
| Public | `/verify-recover-password` | Verify the recovery code |
| Public | `/new-password` | Set a new password |
| Private | `/dashboard` | Main panel |
| Private | `/homes` | Redirects to `/dashboard` (no longer a screen) |
| Private | `/home/[id]` | Home detail |
| Private | `/favorites` | Favorites |
| Private | `/notifications` | Notifications |
| Private | `/settings` | Settings |
| Private | `/account` | Account |

**Total: 13 routes · 12 functional screens.** The app has no landing page and no administration screens.

## 7. Side menu structure

Order of the items, from top to bottom:

| **Position** | **Item** | **Destination** |
|---|---|---|
| Header | Logo and application name | — |
| 1 | **Home** | `/dashboard` |
| 2 | **My homes** — accordion with the list of the user's homes | `/home/:id` |
| 3 | **Favorites** | `/favorites` |
| 4 | **Notifications** — with a pending badge | `/notifications` |
| 5 | **Settings** | `/settings` |
| Footer | **Profile** — avatar and name | My account · Sign out |

The layer opens with a fixed button in the top left corner and closes when the darkened backdrop is tapped or with Android's back button, with a 300 millisecond animation. When closed, it is hidden from the screen reader.

## 8. Differences with the web navigation

| **Aspect** | **Web** | **Mobile** |
|---|---|---|
| Welcome page | Yes (`/home`) | No: it opens at the log in screen |
| Password recovery | 2 routes | 3 routes |
| Account screen | `/account` screen inside the protected zone | Dedicated screen (`/account`) with a title |
| Administration | Yes (`/admin/users`, ADMIN role) | No |
| Side menu | Fixed or collapsible | Sliding layer with a 300 ms animation |
| Detail sections | Top horizontal tabs | Bottom bar with icons |
| Device registration | Web Bluetooth (Chrome or Edge) | Native phone Bluetooth (BLE) |
| Total routes | 16 + not found page | 13 |

## 9. Related artifacts

| **Artifact** | **Location** |
|---|---|
| Web navigation map | `web-navigation-map.md` |
| Navigation guard | `Energy-Monitor-Mobile/src/components/layout/AuthGate/AuthGate.tsx` |
| Side menu | `Energy-Monitor-Mobile/src/components/layout/Sidebar/Sidebar.tsx` |
| Bottom bar | `Energy-Monitor-Mobile/src/components/layout/BottomTabBar/BottomTabBar.tsx` |
| Interface design | `05-diseno-software/ES/05.5-interfaces-graficas.md`, section 5 |