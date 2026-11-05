# Navigation maps

**Project:** Electrical Consumption Monitor — EnergyMonitor
**Record:** 3145556 — Software Analysis and Development (ADSO), SENA CIES Regional Huila
**Version:** 1.1 (October 27, 2026)

---

## 1. Contents

Each application is documented with **three maps**, plus the navigation control points; the web has a fourth map with the administration.

| **Document** | **Map 1** | **Map 2** | **Map 3** | **Map 4** |
|---|---|---|---|---|
| [`web-navigation-map.md`](web-navigation-map.md) | General flow: entry, public zone, guards (guest, session and ADMIN role) and side menu destinations | Dashboard, states and the six sections of the home detail | Favorites, notifications (four tabs), settings and My account | Administration: users, status, role, notices and administrator settings |
| [`mobile-navigation-map.md`](mobile-navigation-map.md) | General flow: launch, session restore, guard and side menu destinations | Main panel, states and the six sections of the home detail | Favorites, notifications (four tabs), settings and the account screen | — (the app has no administration) |

**They were split into three maps for a practical reason:** a single diagram containing every screen came out illegible, because the edges between zones forced the layout engine to reorder the boxes and the result read bottom-up.

### Rendered images

Each diagram also ships as a PNG, ready to drop into the document:

| **File** | **Diagram** |
|---|---|
| `web-navigation-map-1.png` | General flow of the web application |
| `web-navigation-map-2.png` | Dashboard and home detail |
| `web-navigation-map-3.png` | Auxiliary screens of the web application |
| `web-navigation-map-4.png` | Administration (ADMIN role) |
| `mobile-navigation-map-1.png` | General flow of the mobile application |
| `mobile-navigation-map-2.png` | Main panel and home detail |
| `mobile-navigation-map-3.png` | Auxiliary screens of the mobile application |

## 2. Source of the information

These maps were built by **reading the source code**, not the mockups.

| **Map data** | **Project file it came from** |
|---|---|
| Web routes and their protections | `Energy-Monitor-application/src/routes/AppRoutes.jsx` |
| Web side menu | `Energy-Monitor-application/src/components/layout/MainLayout/Sidebar/Sidebar.jsx` |
| Homes accordion | `Energy-Monitor-application/src/components/layout/MainLayout/Sidebar/NavHomes.jsx` |
| Home detail tabs | `Energy-Monitor-application/src/features/DetailHome/Consumption/Consumption.jsx` |
| Mobile routes | `Energy-Monitor-Mobile/src/app/` |
| Mobile auth guard | `Energy-Monitor-Mobile/src/components/layout/AuthGate/AuthGate.tsx` |
| Mobile side menu | `Energy-Monitor-Mobile/src/components/layout/Sidebar/Sidebar.tsx` |
| Home detail bottom bar | `Energy-Monitor-Mobile/src/components/layout/BottomTabBar/BottomTabBar.tsx` |
| Mobile detail sections | `Energy-Monitor-Mobile/src/features/homes/screens/HomeDetailScreen/HomeDetailScreen.tsx` |

> **Mockups are not the real flow.** Mockups and wireframes describe the visual appearance of each screen; the navigation flow is determined by the declared routes and the implemented menus. That is why a navigation map built from a mockup can show screens the system never mounts, or transitions that never happen.

## 3. Format

The maps are written in **Mermaid**, embedded inside Markdown documents.

| **Advantage** | **Detail** |
|---|---|
| Renders with no setup | GitHub, GitLab, VS Code (with the Mermaid extension), Notion and Obsidian render it automatically |
| It is text | It is versioned with Git, reviewable in diffs, and editable without tools |
| It can be exported | With the VS Code Mermaid extension it exports to PNG, SVG or PDF in one click |

### How to export them as images for the document

| **Option** | **Steps** |
|---|---|
| **VS Code extension** | Install *Markdown Preview Mermaid Support* → open the file → use the export-to-PNG button |
| **Mermaid Live Editor** | Paste the content of the ```mermaid block at <https://mermaid.live> → **Actions → PNG** |
| **PlantUML online** | Does not apply: Mermaid is not PlantUML |

### If you need the code in another format

| **Format** | **How to get it** |
|---|---|
| Standalone `.mmd` | Copy the content of the ```mermaid block into a file with the `.mmd` extension |
| draw.io | Export the PNG from Mermaid Live, then use *Insert → Image* on a draw.io canvas |
| PlantUML | Requires rewriting the diagram; say the word and I will convert it |

## 4. How to read the maps

| **Symbol** | **Meaning** |
|---|---|
| Rounded blue node | Entry point |
| Green node | Module or main area of the application |
| White node | Screen, section or concrete action |
| Amber node | Navigation guard or state decision |
| Red node | End of flow: log out or access error |
| Solid arrow | Direct navigation |
| Dotted arrow | Return, retry or log out |

## 5. Differences between web and mobile navigation

| **Aspect** | **Web** | **Mobile** |
|---|---|---|
| Welcome page | Yes | No: it opens on log in |
| Password recovery | 2 routes | 3 routes |
| Account screen | Protected `/account` screen | Own screen with a title |
| Administration | Yes (`/admin/users`, ADMIN role) | No |
| Device linking | Web Bluetooth (Chrome or Edge) | Native Bluetooth (BLE) |
| Side menu | Fixed or collapsible | Animated slide-in layer |
| Detail sections | Top horizontal tabs | Bottom bar with icons |
| Total routes | 16 + not found page | 13 |

## 6. Observations

| **#** | **Observation** | **Detail** |
|---|---|---|
| 1 | Solved: the mobile `/homes` route | It now redirects to `/dashboard` |
| 2 | Solved: the "Add account" option | It no longer appears in the profile menus |
| 3 | Solved: web catch-all route | `*` shows the "not found" page (`NotFound.jsx`) |
| 4 | The mobile app has no catch-all route of its own | Expo Router shows its own "screen not found" page |
| 5 | Solved: `/account` on the web | It is inside `ProtectedRoute` |
| 6 | An ADMIN account in the mobile app | The app does not distinguish administrators; the administration screens exist only on the web |

## 6.1 Status of the mockups against the code

The HTML mockups in `mockup/layout/` were drawn before the latest features. They were not redrawn in this version; the table shows what is missing or out of date.

| **Screen in the code** | **Platform** | **Mockup** | **Status** |
|---|---|---|---|
| Platform users (`/admin/users`): search, table, status and role | Web | — | **Missing** (`mockup/layout/web/admin/users.html`) |
| Status change and administrator role confirmations | Web | — | **Missing** (`mockup/layout/web/admin/users-confirm-modal.html`) |
| Administration notifications | Web | — | **Missing** (`mockup/layout/web/admin/notifications.html`) |
| Administrator side menu and settings (without notifications) | Web | `web/settings/settings.html` (user) | ADMIN variant **missing** |
| Activity tab and home filter in Notifications | Web and mobile | `web/notifications/*.html`, `mobile/notifications/notifications.html` | Out of date: only three tabs |
| Terms and privacy window at registration | Web | `web/auth/register-legal-modal.html` | Current |
| Terms and privacy window at registration | Mobile | — | **Missing** (`mockup/layout/mobile/auth/register-legal-modal.html`) |
| Persistent password rules hint | Web and mobile | `*/auth/register.html` | Check that it shows the persistent hint |
| Transfer ownership (confirmation with checkbox) | Web and mobile | `*/home-detail/*users.html` | Modal **missing** |
| Delete home typing its name | Web and mobile | `web/home-detail/owner/home.html`, `mobile/home-detail/home-detail-home.html` | Modal **missing** |
| Bluetooth linking (step wizard) | Mobile | `mobile/home-detail/home-detail-link-device.html` | Out of date: showed the code typed by hand |
| Change Wi-Fi network and edit device | Web and mobile | — | **Missing** |
| Delete a Google account with a code | Web and mobile | `*/account/account-delete-modal.html` | Out of date: only asks for the password |
| Page titles on Settings and My account, icons instead of emojis | Mobile | `mobile/settings/settings.html`, `mobile/account/account.html` | Out of date |
| Support with administrator contacts and footer | Web | `web/landing/support.html` | To review |
| `mobile/dashboard/homes.html` | Mobile | Exists | Obsolete: the `/homes` route is no longer a screen |

## 7. Related artifacts

| **Artifact** | **Location** |
|---|---|
| HTML mockups (web and mobile) | `03-analisis-software/mockup/layout/web/` and `03-analisis-software/mockup/layout/mobile/` |
| Graphic navigation maps (Mermaid) | `03-analisis-software/EN/navigation/web-navigation-map.md` and `03-analisis-software/EN/navigation/mobile-navigation-map.md` |
| Graphic interface design | `05-diseno-software/EN/05.5-graphic-interfaces.md` |
| Presentation architecture design | `05-diseno-software/EN/05.1-architectural-model.md`, section 6 |
| Spanish version | `03-analisis-software/ES/navegacion/` |