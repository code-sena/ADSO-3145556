# 5.5 Graphic interfaces

**Project:** Electrical Consumption Monitor — EnergyMonitor
**Record:** 3145556 — Software Analysis and Development (ADSO), SENA CIES Regional Huila
**Version:** 1.0

---

## 1. Purpose and relationship with point 03

This section documents the **user interface design**: the design system that supports it, the palette, the typography, the screen structure, the reusable components, the interaction patterns and the accessibility decisions.

> **The mockups of this project are in point 03 — Software analysis**, not here.
> This document **does not** replace them: it describes them, classifies them and relates them to the code that implements them. When this document and a mockup seem to contradict each other, **the mockup prevails**, because it is the source of truth of the design with which the implementation was validated.

| **Artifact** | **Location** | **What it contributes** |
|---|---|---|
| **Mockups** in HTML (web and mobile) | `03-analisis-software/mockup/layout/{web,mobile}/` | Approved visual look of each screen |
| **Navigation maps** web and mobile | `03-analisis-software/EN/navigation/` | Actual execution flow, taken from the code rather than from the mockups |
| **Requirements and user stories** | `02-especificacion-requisitos/` | Functional requirements that give rise to the screens |

## 2. User interface design principles

| **#** | **Principle** | **Concrete application** |
|---|---|---|
| 1 | **Clarity over complexity** | Consumption is understood with three high-level indicators (current power, today's consumption, active devices) and the detail is one level below |
| 2 | **Progressive information** | The Dashboard shows home cards; the detail opens in six tabs, never all at once |
| 3 | **One screen, one task** | Destructive actions (delete home, leave home, unlink device) are isolated in a confirmation dialog |
| 4 | **Immediate feedback** | Every on-screen change is visually confirmed: badge in the menu, tab title, toast and counter update |
| 5 | **Consistency between platforms** | Web and mobile share palette, typography, spacing and behavior; a user of both recognizes the application |
| 6 | **Real internationalization** | Four languages throughout the interface, including error messages, day names and number formats |
| 7 | **Explicit states** | Every data view declares its four states: loading, error, empty and content |

## 3. Design system

### 3.1 Architecture decision

**Both front-ends implement an own design system. No third-party component library is used.**

| **Technology** | **Used?** | **Comment** |
|---|---|---|
| **CSS Modules** (web) | **Yes** | Main styling technology, 40+ files |
| **CSS variables** (web) | **Yes** | Design tokens in `:root` |
| **`StyleSheet` + tokens** (mobile) | **Yes** | Replication of the web system |
| Tailwind CSS | No | Without configuration, without dependency |
| shadcn/ui · Radix · Headless UI · MUI · Chakra | No | Without dependency |
| Icons | `lucide-react` (web) · `Ionicons` and `MaterialCommunityIcons` (mobile) | Only third-party visual library |

**Consequence of the decision.** The project does not inherit design decisions from others: each control reflects a concrete need of this application. The cost is maintaining the system: each own component needs tests, documentation and attention to accessibility.

### 3.2 Components of the system

**Web — 7 components in `src/design/`**

| **Component** | **Variants** | **Function** |
|---|---|---|
| `Button` | `primary`, `secondary`, `ghost`, `danger`; sizes `small`, `medium`, `large` | Base button of the whole application |
| `Input` | `default`, `otp`, `bare` | Form field with label, actionable icon and code mode |
| `Card` | — | Surface container with optional title |
| `Header` | — | Inner page header with breadcrumbs and action area |
| `BreadCrumb` | — | Breadcrumbs; the item with a path is a link, the current one is text |
| `ActionMenu` | 4 positions | Dropdown menu of actions |
| `Badge` | `filled`, `outline` | Status badge |

**Mobile — 18 components in `src/components/`**

| **Component** | **Variants** | **Function** |
|---|---|---|
| `Button` | 4 variants, 3 sizes | Base button (`Pressable`) |
| `Card` | `default`, `soft`, `selected`; 4 padding levels | Surface; it becomes pressable if it receives `onPress` |
| `Input` | `default`, `otp` | Field with internal focus state |
| `Modal` | `default`, `danger` | Centered dialog with appearance animation |
| `Alert` | `success`, `warning`, `danger`, `info` | Banner with icon, title and message |
| `Header` / `Breadcrumb` | — | Header and breadcrumbs |
| `ActionMenu` | — | Button with menu anchored by position measurement |
| `OtpInput` | Configurable length | Single-digit code boxes |
| `Switch` | — | Switch with the theme colors |
| `Chart` | `area`, `bar`, `pie` | Charts wrapper |
| `Loader` | `small`, `large` | Loading indicator with text |
| `EmptyState` | `full`, `compact` | Empty state with image or icon |
| `ListItem` | — | Reusable list row |
| `LanguageSwitcher` | `dark`, `light` | Language selector with flag |
| `Sidebar`, `BottomTabBar`, `AuthGate` | — | Layout and navigation components |

> **Verified parity.** Seven of the web system components have a direct equivalent on mobile, with the same variants. It is evidence that this is a single design, not two.

### 3.3 Color palette

**Figure 8.** *Brand palette and semantic colors of the design system.*

**Brand and semantic colors**

| **Token** | **Value** | **Use** |
|---|---|---|
| `--color-primary` | `#0078D7` | Primary button, links, focus indicator |
| `--color-primary-hover` | `#3399FF` | Pressed state, focused field border |
| `--color-primary-dark` | `#0A2540` | Gradient end, limit bar background |
| `--color-primary-soft` | `#E8F3FF` | Subtle background of active tabs |
| `--color-secondary` | `#32CD32` | Accent and success green |
| `--color-success` | `#2ECC71` | Confirmation |
| `--color-warning` | `#FFD700` | Warning |
| `--color-danger` | `#E63946` | Destructive action |
| `--color-info` | `#17A2B8` | Information |

**Surfaces and text**

| **Token** | **Value** |
|---|---|
| `--color-background` | `#F8F9FA` |
| `--color-background-left` | `#3F6BAE` |
| `--color-surface` | `#FFFFFF` |
| `--color-text-primary` | `#212529` |
| `--color-text-secondary` | `#6C757D` |
| `--color-border` | `#E5E7EB` |

**State pairs** — each state has a soft background and a text color with sufficient contrast

| **State** | **Background** | **Text** |
|---|---|---|
| Error / critical | `#FCEBEB` | `#A32D2D` |
| Success / online | `#EAF3DE` | `#3B6D11` |
| Warning | `#FFF4D6` | `#854F0B` |
| Information | `#D1ECF1` | `#0C5460` |

> **Mobile replicates the 32 color tokens** in its `ThemeColors` contract, with the same hexadecimal values. The parity is documented in the code itself with comments that refer to the source file on the web side.

### 3.4 The four themes

Requirement RF-CLI-06 (color configuration) is implemented with **four themes**, two light and two dark. The preference is stored on the device (`energymonitor_theme`) and in the database (`user_configuration.color_theme`).

| **Theme** | **Mode** | **Sidebar** | **Primary** | **Character** |
|---|---|---|---|---|
| **EnergyMonitor** *(default)* | Light | `#3F6BAE` | `#0078D7` | Corporate, sober |
| **Eco Hogar** | Light | `#2E7D32` | `#32CD32` | Natural, oriented to savings |
| **EnergyMonitor Dark** | Dark | `#0F172A` | `#1E293B` | High-contrast dark |
| **Eco Dark** | Dark | `#102A1A` | `#183321` | Dark with a green hue |

**True dark mode, not inverted.** In the dark themes each token is reassigned: no inversion filter is applied over the light theme. The soft colors go from solid hexadecimals to translucent values (`rgba(…, 0.15)`), and the danger color is lightened to `#FCA5A5` to maintain contrast on dark backgrounds.

**Preview in the interface.** The configuration screen draws a thumbnail of each theme —sidebar, progress bar and color dots— built with the same values that will be applied. The user chooses by looking, not by reading.

### 3.5 Chart colors

Charts use a fixed categorical palette, independent of the theme, with a **variant for light and dark mode**:

| **Appliance** | **Light** | **Dark** |
|---|---|---|
| Refrigerator | `#2563EB` | `#60A5FA` |
| Washing machine | `#7C3AED` | `#A78BFA` |
| Television | `#DB2777` | `#F472B6` |
| Microwave | `#EA580C` | `#FB923C` |
| Air conditioner | `#0D9488` | `#2DD4BF` |
| Computer equipment | `#CA8A04` | `#FACC15` |
| Water heater | `#DC2626` | `#F87171` |
| Lighting | `#65A30D` | `#A3E635` |
| Other | `#64748B` | `#94A3B8` |

> **Why this palette.** The colors are not chosen at random: each one maintains a perceptible contrast relationship with the others, so that two adjacent segments of a pie chart are never confused. In addition, the same appliance always has the same color, which allows recognizing it from one screen to another without consulting the legend.

### 3.6 Typography

**Families**

| **Platform** | **Primary font** | **Secondary font** |
|---|---|---|
| Web | `Inter` | `Poppins` |
| Mobile | `System` (operating system font) | `System` |

> **Documented technical debt.** The web declares `Inter` and `Poppins` in its CSS variables, but does not load either of them through `@font-face` nor from a CDN. The browser falls back to the generic system font. To fix it, it is enough to add the `@font-face` or the corresponding link in `index.html`.

**Typographic scale (11 sizes, identical on both platforms)**

| **Token** | **Value** | **Typical use** |
|---|---|---|
| `size11` | 11 px | Chart text and legend labels |
| `xs` | 12 px | Auxiliary text |
| `size13` | 13 px | Small labels |
| `sm` | 14 px | Control text |
| `md` | 16 px | **Base body text** |
| `size18` | 18 px | Highlighted text |
| `lg` | 20 px | Subtitles |
| `size22` | 22 px | Card titles |
| `xl` | 24 px | Section titles |
| `xxl` | 32 px | Screen titles |
| `xxxl` | 60 px | Cover messages |

**Weights:** `normal` (400), `medium` (500), `bold` (600).

> The scale is deliberately irregular: the "round" names (`xs`, `sm`, `md`, `lg`, `xl`, `xxl`) replicate the web scale to preserve visual parity, and the "exact" ones (`size11`, `size13`, `size18`, `size22`) cover sizes that exist in the design but did not fit the scale.

### 3.7 Spacing and radii

**Spacing** — 4-point scale

| **Token** | **Value** |
|---|---|
| `xs` | 4 px |
| `sm` | 8 px |
| `md` | 16 px |
| `lg` | 24 px |
| `xl` | 32 px |

**Border radii** — `sm` 6 px · `md` 10 px · `lg` 16 px · `999px` for pills and avatars.

**Shadows**

| **Token** | **Web value** | **Android elevation** |
|---|---|---|
| `shadow-sm` | `0 1px 2px rgba(0,0,0,0.05)` | 1 |
| `shadow-md` | `0 4px 6px rgba(0,0,0,0.1)` | 4 |
| `shadow-lg` | `0 10px 15px rgba(0,0,0,0.25)` | 10 |

> **Mobile detail.** Each shadow is a pair: the `boxShadow` string for iOS and web, and the `elevation` value for Android. Defining only one of the two produces invisible shadows on one of the platforms.

### 3.8 Breakpoints (responsive)

| **Token** | **Value** | **Device** |
|---|---|---|
| `--bp-mobile` | 480 px | Small mobile |
| `--bp-tablet` | 768 px | Tablet and mobile |
| `--bp-laptop` | 1024 px | Laptop |

**Most relevant responsive transformations**

| **Component** | **Desktop** | **≤ 1024 px** | **≤ 768 px** |
|---|---|---|---|
| Sidebar | Fixed, 240 px, collapsible to 80 px | Same | Off-screen sliding layer, 85 % of the width with a maximum of 300 px |
| Top navigation | Complete with links | Reduced typography | Expandable hamburger menu |
| Home detail tabs | Horizontal with text | Fixed bar at the bottom | Icons only, with bottom bar |
| Breadcrumbs | All levels | All levels | Only first and last level, with horizontal scrolling |
| Dialogs | Centered | Centered | **Bottom sheet** (*bottom sheet*) attached to the bottom edge |
| Brand panel of the login | 50 % left | Padding adjustments | Hidden; the form takes the full width |
| Progress bars | Full width | Full width | Taller bars to make touching easier |

> **Mobile application strategy.** The app is locked to portrait orientation (`orientation: "portrait"` in `app.json`). There is no landscape design to solve.

## 4. Screen structure — Web application

**Figure 9.** *Navigation map of the web application.*

### 4.1 Route table

| **Route** | **Screen** | **Access** | **Layout** |
|---|---|---|---|
| `/` | Redirect to `/home` | Public | — |
| `/home` | Welcome page | Public | Navigation + cover + benefits |
| `/about` | About us | Public | Navigation + content |
| `/support` | Support and frequently asked questions | Public | Navigation + question search |
| `/login` | Sign in | Public | Brand panel + form |
| `/register` | Sign up | Public | Brand panel + form |
| `/VerifyAccount` | Email verification | Public | Brand panel + 6-digit code |
| `/recover-password` | Password recovery (step 1) | Public | Brand panel + form |
| `/verifyRecoverPassword` | Verification and new password (step 2) | Public | Brand panel + form |
| `/dashboard` | **Home panel** | **Protected** | Sidebar + home cards |
| `/homes/:homeId` | **Home detail** | **Protected** | Sidebar + six tabs |
| `/favorites` | Favorites | **Protected** | Sidebar + cards |
| `/notifications` | Notifications (ADMIN: administration notices) | **Protected** | Sidebar + four tabs and a home filter |
| `/settings` | Settings | **Protected** | Sidebar + four sections |
| `/account` | My account | **Protected** | Own screen inside the protected zone |
| `/admin/users` | **Administration: platform users** | **Protected + `USER.READ` permission** | Administrator sidebar + search + paginated table |
| `*` | Not found page | Public | Message and back link |

### 4.2 Protection levels

Routing applies **five guards** with distinct responsibilities:

| **Guard** | **What it verifies** | **If it fails** |
|---|---|---|
| `ProtectedRoute` | That a session with an access token exists | Redirects to `/login` |
| `HomeRoute` | That the home in the URL exists and is accessible | Shows "home not found" with a back button |
| `GuestRoute` | That there is no session when opening `/login` or `/register` | Goes to the dashboard |
| `UserOnlyRoute` | That the account is not an administrator (home screens) | Goes to `/admin/users` |
| `RequirePermission` | That the account has the given global permission (`USER.READ` on `/admin/users`) | Goes to `/dashboard`; the API answers 403 anyway |

**Figure 10.** *Decision flow for access to a protected route.*

**Design decision — outlet context.** `HomeRoute` delivers the home and the owner flag (`isOwner`) to the child screen through the router's outlet context. This avoids passing the home as a property through three levels of components and, above all, avoids having each screen query it again.

### 4.3 Detail of the main screens

#### Welcome page

| **Aspect** | **Detail** |
|---|---|
| **Elements** | Top navigation with Home, Benefits, How it works, About us and Support; language selector; theme button; "Sign in" and "Sign up" buttons; welcome message; benefits section in cards; "How it works" section |
| **Objective** | Explain what the system does in less than a minute and offer direct access |
| **Design** | Composition in two blocks: presentation and call to action |

#### Home panel (Dashboard)

| **Aspect** | **Detail** |
|---|---|
| **Elements** | Sidebar with home, favorites, notifications and settings; avatar indicator at the bottom; cards of the user's homes; "+" quick access button |
| **States** | **Loading** — indicator with text; **Error** — translated message and "Retry" button, with a distinction between "no connection" and "server error"; **Empty** — invitation to create the first home with image and button; **Content** — grid of cards |
| **Home card** | Name, identifying header color, description, favorite button (heart) and access to the detail |
| **Actions** | Create home (name, type, address, description) · Join a home (8-character code, with counter and automatic uppercase conversion) |

#### Home detail — **Consumption** tab (default tab)

| **Aspect** | **Detail** |
|---|---|
| **Indicators** | Three cards: **current power** (kW and risk level), **today's consumption** (kWh against the limit), **active devices** (active over total) |
| **Charts** | **Area** — average hourly active power over the last 24 hours · **Pie** — percentage distribution of consumption by appliance type · **Horizontal bars** — current consumption per device |
| **Limit bars** | Two progress bars: progress of the daily limit and of the monthly limit |
| **States** | Chart without data → empty component with icon, **never** a chart at zero |

#### Home detail — **History** tab

| **Aspect** | **Detail** |
|---|---|
| **Selectors** | Period: day, week, month or year. Within the month: specific week or "show all" |
| **Chart** | Bars with the distribution of consumption by device over the period |
| **Table** | Device ranking with a percentage bar per row |
| **Cards** | Total consumption of the period, average and the appliance that consumes the most |
| **Behavior** | If there are more than 8 points, the chart scrolls horizontally instead of being compressed |

#### **Users**, **Devices**, **Thresholds** and **Home** tabs

| **Tab** | **Owner** | **Member** |
|---|---|---|
| **Users** | List of members with role; can expel a member and transfer ownership to them ("Make … the responsible user", with confirmation) | List of members; read only |
| **Devices** | List with status and consumption; link, edit and unlink | List with status and consumption; read only |
| **Thresholds** | Default values switch and daily and monthly limit fields; "Save changes" button | Viewing of the limits; read-only fields |
| **Home** | Home data, owner data, access code to share and "Delete home" button (confirmed by typing the home name) | Home and owner data; "Leave home" button |

> **Interface rule.** The quick access button and the sidebar elements are shown from the start: the user discovers the available functions. However, **actions that the role does not allow are hidden** (for example, the "Delete home" button does not appear for a member).

#### Notifications screen

| **Aspect** | **Detail** |
|---|---|
| **Tabs** | All · Alerts · Recommendations · Activity, each with a pending counter, and a filter by home or "My account" |
| **Bulk action** | "Mark all as read" and "Delete resolved or read" (with confirmation) |
| **Card** | Icon according to the type · title · description · related home and device · date and time · status tag (New, Active or Resolved) · action (Resolve, Mark as read or delete) |
| **Iconography** | Connectivity → wifi off · Device → meter · Limit → gauge · Critical → lightning bolt · Warning → triangle · Recommendation → light bulb |

#### Settings screen

| **Section** | **Content** |
|---|---|
| **Appearance** | Grid of the four themes with preview thumbnail |
| **Language** | Selector for Spanish, English, French or Portuguese, with flag |
| **Notifications** | Email, push and combined switches; indication of subscribed browsers |
| **Help** | Documentation, contact and updates with its own brand icon (marked "Coming soon") |

An administrator account does not see the **Notifications** section of the settings.

#### Administration screens (web only, ADMIN role)

| **Aspect** | **Detail** |
|---|---|
| **Sidebar** | Administration · Notifications · Settings (no Home, Homes or Favorites) |
| **Platform users** (`/admin/users`) | Breadcrumb Home › Administration; title and subtitle; search by name or email; table User (with the "You" mark) · Status · Global roles · Registered · Last access; pages of 20 |
| **Row actions** | Active / Inactive / Blocked status selector (`USER.MANAGE` permission) and shield button to grant or revoke ADMIN (`ROLE.MANAGE`); hidden on one's own row and without the permission |
| **Confirmations** | `ConfirmModal` when leaving Active and always when granting or revoking the role |
| **Administration notifications** | List of notices (account registered, status changed, role granted or revoked) with New/Read, mark as read, mark all and delete |
| **Mockups** | Do not exist yet (see `03-analisis-software/EN/navigation/README.md`, section 6.1) |

**Figure 10b.** *"Platform users" screen with the status change confirmation* — image pending.

## 5. Screen structure — Mobile application

**Figure 11.** *Navigation map of the mobile application.*

### 5.1 Route table

| **Route** | **Screen** | **Access** |
|---|---|---|
| `/` | Sign in | Public |
| `/register` | Sign up | Public |
| `/recover-password` | Password recovery | Public |
| `/verify-account` | Email verification | Public |
| `/verify-recover-password` | Recovery verification | Public |
| `/new-password` | New password | Public |
| `/dashboard` | **Home panel** | **Protected** |
| `/homes` | Redirects to `/dashboard` | **Protected** |
| `/home/[id]` | **Home detail** | **Protected** |
| `/favorites` | Favorites | **Protected** |
| `/notifications` | Notifications | **Protected** |
| `/settings` | Settings | **Protected** |
| `/account` | Account | **Protected** |

**13 routes · 12 functional screens.** The app has no landing page and no administration screens; the registration opens the terms and the privacy policy in a window, and Settings and My account have a page title.

### 5.2 Navigation architecture

| **Element** | **Implementation** | **Design decision** |
|---|---|---|
| **Stack** | A single `Stack` of Expo Router, without header | Simple and predictable structure |
| **Sidebar** | Own component with `Animated` and 300 ms transition | Allows an accordion with the home list synchronized with the global context |
| **Tab bar** | Own component in the home detail | The "back" button exits the home and not the section |
| **Context menu** | `ActionMenu` with position measurement (`measureInWindow`) | The dropdown is anchored correctly even if the layout changes |

**Figure 12.** *Mobile navigation structure: single stack, own sidebar and own tab bar.*

**Navigation guard.** `AuthGate` wraps the complete stack. It defines the set of public routes and, on detecting that the session is not valid, redirects to the initial screen. While it restores the session from storage it renders nothing, to avoid a flicker of the loading screen.

### 5.3 Home detail — bottom bar of six sections

| **Section** | **Icon** | **Content** |
|---|---|---|
| **Consumption** | Pulse | Three indicators, 24 h area chart, distribution pie chart and bars per device, daily and monthly limit bars |
| **History** | Clock | Period selector, bar chart and ranking table with percentage bars |
| **Users** | People | List of members with role; the owner can expel |
| **Devices** | Wireless | List with connection status and consumption; the owner can link, edit and unlink |
| **Thresholds** | Sliders | Defaults switch and limit fields with period selector |
| **Home** | House | Home data, owner data and access code |

### 5.4 Adaptation of screens to different sizes

| **Technique** | **Application** |
|---|---|
| Safe areas | The 13 screens apply `paddingTop: max(insets.top, spacing.md)` and respect the bottom margin of the device |
| Derived widths | Dialogs set a maximum width of 440 px, avoiding giant dialogs on tablet |
| Sidebar | Width of `min(85 % of the screen, 300 px)` |
| Derived heights | The long lists of the dialogs occupy at most 55 %, 62 % or 45 % of the height depending on their content |
| Chart scrolling | If the series has more than 8 points, the chart scrolls horizontally with a width of `points × 56 px` |
| Keyboard | Nine screens use the container that avoids the keyboard, with different behavior on iOS and Android |
| Fluid grids | The theme, language and home selectors use wrapping with a base width of 45 % |

## 6. Interaction patterns

### 6.1 Forms and validation

| **Aspect** | **Web** | **Mobile** |
|---|---|---|
| **Engine** | `react-hook-form` + `zod` | `react-hook-form` + `zod` |
| **Schemas** | 6 authentication and account schemas | 10 schemas, including home and thresholds |
| **Messages** | Function that receives the translator: the message is written in the active language | Same, through a central helper |
| **Password** | Visibility eyes, complexity rules, matching confirmation | Same |
| **6-digit code** | 6 boxes with automatic advance and backspace | `OtpInput` component with the same behavior |

> **Two-layer validation.** The Zod schema acts on the client to give an immediate response; the backend validates everything again with Bean Validation. Client validation never replaces server validation: it is a usability aid, not an integrity guarantee.

### 6.2 States of a data view

Every screen that consumes information declares the same four states. This pattern is the backbone of visual consistency.

**Figure 13.** *State machine of a data view.*

| **State** | **Presentation** | **Action offered** |
|---|---|---|
| **Loading** | Indicator with text and `status` role | None; one waits |
| **Network error** | Translated message + wifi off icon | "Retry" |
| **Server error** | Message translated by status code + alert icon | "Retry" |
| **Empty** | Image or icon + explanatory title + primary action | "Create home", "Link device" |
| **Content** | The data | Those of the screen itself |

> **Hard rule.** The error message the user sees is **never** the one returned by the server. The server responds with an HTTP code and the client translates it through a catalog that combines the operation performed with the code received. This prevents leaking internal details and guarantees that all messages are translated.

### 6.3 Modal dialogs

| **Type** | **Use** | **Behavior** |
|---|---|---|
| **Simple confirmation** | Reversible or moderate actions | Title, message, "Cancel" and "Accept" |
| **Destructive confirmation** | Delete home, transfer ownership, leave home, expel member, unlink device, delete account | Same design, but with `alertdialog` role and danger color |
| **Form** | Create home, join home, link device, edit profile, change password | Fields with inline validation |
| **Step-by-step wizard** | Link a device | Five steps with progress indicators and signal bars |

**Common behavior**

| **Rule** | **Detail** |
|---|---|
| Close | Close button, Cancel or tap on the darkened background |
| Focus | On opening, the focus stays inside the dialog |
| Web — bottom sheet | On small screens the dialog becomes a sheet attached to the bottom edge |
| Mobile — keyboard | The keyboard does not cover the fields: the dialog scrolls |
| Double confirmation | No destructive action is executed without an explicit confirmation |

### 6.4 Multichannel notification system

A single event is communicated through several channels, according to the context of the user.

**Figure 14.** *Multichannel flow of an alert: from the measurement to the notice on the device.*

| **Channel** | **When** | **Presentation** |
|---|---|---|
| **Badge in the menu** | Always | Counter in the sidebar entry, with a visual cap at `99+` |
| **Tab title** | In the browser | The title becomes `(3) EnergyMonitor` |
| **Toast** | On screen | Panel that slides in, maximum 3 at a time, 8 seconds each |
| **Browser notification** | With the application open or closed | System notification, on click it focuses the application |
| **Email** | According to the user preference | Message sent via SMTP |
| **Audio and vibration** | Mobile | Reserved for the version with native notifications |

**System rules**

| **Rule** | **Justification** |
|---|---|
| Each type has its icon and its severity color | Distinction above color |
| The left border indicates the severity | Red for critical, amber for warning, blue for informational |
| Already seen notices are not repeated on reload | The identifiers already shown are stored |
| On tapping a notice it navigates to the inbox | The notice always leads to its origin |
| A network failure does not empty the inbox | The user keeps what they already had |

### 6.5 Charts

| **Aspect** | **Web** | **Mobile** |
|---|---|---|
| **Library** | `recharts` | `react-native-chart-kit` over `react-native-svg` |
| **Area** | 24-hour curve with gradient | 24-hour curve with gradient shadow |
| **Pie** | Donut with free center and separation between segments | Sector with legend |
| **Bars** | Horizontal per device | Vertical per device |
| **Limit bars** | Indicator built with CSS | Indicator built with native views |
| **Colors** | Categorical palette with a variant per theme | The same palette |
| **Scale** | Automatic | Automatic |
| **Interaction** | Tooltips with the system style | Component tooltips |
| **Empty** | `EmptyChart` component with icon and explanation | "No data" message — **never** a chart at zero |

> **Design decision — "no data" is not "zero".** In both front-ends the distinction between "there are no measurements" and "the consumption was zero" is preserved. A chart showing a line at zero asserts something the system does not know; an empty chart with an explanation is honest.

### 6.6 Real-time data

Requirement RF-MED-01 (automatic recording of consumption) is reflected in the interface through polling at three distinct moments.

| **Data** | **Frequency** | **Condition** |
|---|---|---|
| Devices and their status | 5 seconds | Only with the application in the foreground (mobile) |
| Consumption summary | 30 seconds | Only with the application in the foreground (mobile) |
| Alerts and recommendations of all homes | 30 seconds | Only with the application in the foreground (mobile) |

**Connection states derived from the absence of data**

| **State** | **Criterion** | **Presentation** |
|---|---|---|
| Online | Recent data | Green indicator + signal strength in percentage |
| Verifying | More than 75 s without data | Amber indicator "checking" |
| Disconnected | More than 150 s without data | Gray indicator + "Disconnected" |

> **Design decision.** An intermediate state of "verifying" avoids the false positive: if the device has not reported in 75 seconds, it is not declared disconnected yet. Without this state, a brief network outage would appear as a device disconnection.

### 6.7 Navigation and feedback

| **Element** | **Behavior** |
|---|---|
| **Back** | On mobile, the back button exits the home detail toward the panel; inside the web application, browser routes work normally |
| **Favorites** | Heart button on each card; the applied value is the one returned by the server, not a local toggle, so that two quick taps do not leave the heart reversed |
| **Language** | The change applies immediately throughout the application and is persisted |
| **Theme** | The change applies immediately without reloading the page |
| **Sign out** | Available from the profile menu, on both platforms |

## 7. Accessibility

**Requirement RNF-USA-01:** The interface must be clear, adaptable to different screen sizes and coherent, with immediate feedback. No formal WCAG conformance level is declared.

### 7.1 What is implemented

| **Category** | **Implementation** |
|---|---|
| **Roles and regions** | Dialogs with `dialog` role and `aria-modal`; destructive confirmations with `alertdialog` role; switches with `switch` role; radio groups with `radiogroup` role; loading states with `status` role; errors with `alert` role |
| **Labels** | All icon-only buttons have a label; decorative icons are marked to be skipped by the screen reader |
| **States** | Switches, tabs and selected cards expose their state; accordions expose whether they are expanded |
| **Keyboard navigation** | Dialogs close with `Escape`; the numeric code advances and goes back automatically; cards and pressable fields respond to `Enter` |
| **Visible focus** | 2 px outline with the primary color on buttons, menus and fields |
| **Live regions** | The toast stack uses a `polite` region; the device status is announced |
| **Contrast** | Each state has a background-text pair designed to meet contrast; the dark themes readjust the danger color to maintain it |
| **Touch size** | Controls with an enlarged touch zone in dense lists; on mobile the small touch targets were enlarged |
| **Mobile (October 2026)** | Screen reader labels on fields, icon buttons and code digits (which accept pasting); roles and states on the controls; screen titles marked as headings; side menu hidden from the reader when closed; icons instead of emojis |
| **Document language** | The `lang` attribute of the document changes with the chosen language |
| **Regional format** | Dates and numbers are formatted according to the language and time zone of the browser |

### 7.2 Identified gaps

| **#** | **Gap** | **Impact** | **Recommendation** |
|---|---|---|---|
| 1 | No close with `Escape` in most dialogs (only in one of the web ones) | A keyboard user has to look for the close button | Implement close with `Escape` in the base dialog component |
| 2 | No focus retention within dialogs | The focus can leave the dialog with `Tab` | Implement a focus trap and restore the focus on close |
| 3 | Tabs do not use the `tablist`, `tab` and `tabpanel` roles | A screen reader does not announce how many sections there are nor which one is active | Adopt the accessible tabs pattern |
| 4 | The graphs do not have an alternative textual description | The information of the charts is not accessible | Add a description and refer to the data table, which already exists in the history |
| 5 | Elements with `role="button"` over `div` do not respond to the spacebar | An expected keyboard action is skipped | Use the native `button` element |
| 6 | No skip link to the main content | A keyboard user has to traverse the whole navigation on each screen | Add a skip link |

### 7.3 Accessibility-oriented tests

The test suite of the web front-end uses role queries (`getByRole`) systematically, which constitutes an involuntary accessibility check: **if a control loses its role, the test fails**. It is a cheap and effective practice worth keeping.

## 8. Internationalization

| **Aspect** | **Detail** |
|---|---|
| **Languages** | Spanish (default), English, French and Portuguese |
| **Namespaces** | 31 on the web · 25 on mobile |
| **Total translation files** | 124 on the web · 100 on mobile |
| **What is translated** | All visible text: labels, error messages, field help, day names, units and system messages |
| **What is not translated** | The product name and the password placeholder |
| **Regional format** | Dates and numbers with `Intl` according to the active language; the time zone of the browser is sent to the backend to group the measurements by local hour |
| **Initial language** | Stored preference → device language → Spanish |
| **Anti-flicker** | On mobile, the language is resolved **before** mounting the provider, avoiding the flash of the interface in Spanish |
| **Validation messages** | The messages of the validation schemas are resolved with the active language, they are never hardcoded |

**Figure 15.** *Languages available in the selector, with flag and language name.*

## 9. Consistency between platforms

### 9.1 Equivalence map

| **Web** | **Mobile** | **Equivalence** |
|---|---|---|
| Fixed or collapsible sidebar | Sliding sidebar | Same destinations, same position of the elements |
| Horizontal top tab bar | Bottom bar with icons | Same six sections of the home detail |
| 50/50 brand panel | Branded header with gradient | Same purpose: product identity versus the form |
| Centered dialog | Centered dialog | Same components and variants |
| Dialog as bottom sheet on mobile | Native centered dialog | Coherent adaptation with the platform |
| Top navigation with hamburger | No equivalent: the sidebar fulfils the function | Own decision of each platform |
| Browser notification (Web Push) | Native push notification (Expo) | Same email and push preferences |
| Browser Web Bluetooth | Native Bluetooth (BLE) | Same linking wizard |
| Administration screens | No equivalent | Decision: administration is done only on the web |

### 9.2 Verified common elements

| **Element** | **Match** |
|---|---|
| 32 color tokens | Identical hexadecimal values |
| 4 theme palettes | Same identifiers, same bar and surface colors |
| 11 typographic sizes | Identical, including the "exact" values |
| Spacing scale | Identical (4, 8, 16, 24, 32) |
| Chart palette | Identical, with the same light and dark variant |
| Seven system components | Same variants in `Button`, `Card`, `Input`, `Header`, `Breadcrumb`, `ActionMenu`, `Alert` |
| Validation rules | Same set of password and email rules |
| Consumed endpoints | The same set of API routes |
| Error model | Same structure: status code and translate the message |

> **Recommendation for the next iteration.** Parity is maintained by manual replication of values, with comments that refer to the source file. This works, but it is fragile: a change in the web palette may not reach mobile. The solution would be a shared package with the tokens, which both front-ends import.

## 10. Interface gaps and pending work

| **#** | **Gap** | **Platform** | **Impact** | **Pending work** |
|---|---|---|---|---|
| 1 | Solved: device registration via native Bluetooth | Mobile | — | Implemented with `react-native-ble-plx` |
| 2 | Solved: copying the access code | Mobile | — | Implemented with `expo-clipboard` |
| 3 | Solved: native notifications | Mobile | — | Push with `expo-notifications` |
| 4 | Solved: Google and legal links | Mobile | — | Native Google sign-in and legal window at registration |
| 5 | Solved: fallback route | Web | — | `*` route with `NotFound.jsx` |
| 6 | Solved: lazy loading of routes | Web | — | Screens with `lazy` and `Suspense` |
| 6b | No administration screens | Mobile | An administrator must use the web | Scope decision; keep it documented |
| 7 | No lazy loading of images | Web | All images are downloaded at startup | Add lazy loading |
| 8 | No automated test | Mobile | Verification is manual | Incorporate a testing framework |
| 9 | Unused components | Both | `Badge` (web), `ListItem` and `Breadcrumb` (mobile) | Integrate or remove them |
| 10 | Orphan routes | Both | Files that do not correspond to any route | Remove them from the code |

## 11. Traceability with the requirements

| **Requirement (requirements specification)** | **Screen or design element** |
|---|---|
| RF-SEC-05 Social network login | "Continue with Google" button in sign up and sign in |
| RF-SEC-08 Information update | Account screen: first name, last name and photograph |
| RF-SEC-06 Recover password | Two-step recovery screens |
| RF-SEC-11 Delete account | Deletion dialog inside the account screen |
| RF-HOM-01 Register a home | Home creation form |
| RF-HOM-05 Permission management | Difference of actions between owner and member |
| RF-HOM-05 Responsible user | Home tab: owner data and access code |
| RF-HOM-07 Favorite homes | Heart button on the cards + favorites screen |
| RF-HOM-08 Threshold configuration | Thresholds tab with defaults switch |
| RF-CLI-06 Color configuration | Grid of four themes with preview |
| RF-CLI-05 Language configuration | Language selector with flag |
| RF-NOT-03 Alert configuration | Notification switches in settings |
| RF-MED-08 Selection of date ranges | Period selector in the history tab |
| RF-MED-08 Consumption comparison | Ranking table with percentage bar |
| RF-MED-08 Trend charts | Area, pie and bar charts |
| RF-ALE-05 Alert display | Notifications screen with three tabs |
| RF-REC-08 Recommendation display | Recommendations tab in notifications |
| RF-DEV-08 Device linking | Step-by-step linking wizard |
| RF-MED-04 to RF-MED-06 Connection status | Indicator per device with signal and derived status |
| RNF-USA-01 Usability and accessibility | Design system, four themes, four languages, declarative states |

## 12. Related artifacts

| **Artifact** | **Location** |
|---|---|
| **Mockups** | `03-analisis-software/mockup/layout/{web,mobile}/` — source of truth of the visual design |
| **Navigation maps** | `03-analisis-software/EN/navigation/` |
| Presentation architectural model | `05.1-architectural-model.md`, section 6 |
| Interface design patterns | `05.4-design-patterns.md`, section 7 |
| User manual (web and mobile, single document) | [`12-manual-usuario/EN/MD/User_Manual_EnergyMonitor_EN.md`](../../08-manuals/en/user-manual.md) |