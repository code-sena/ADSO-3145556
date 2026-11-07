# Mockups — Home detail (Member)

View of a home for a user whose Home Role is `MEMBER` (in React: `isOwner === false`, i.e. the home
card variant is not `"owned"`). Only the screens whose controls actually change are drawn here;
everything else is identical to `../owner/`.

## Files

| Tab / state | File | Stylesheet |
|-------------|------|------------|
| Devices (list, read-only) | `devices.html` | `css/devices-styles.css` |
| Devices (empty, read-only message) | `devices-empty.html` | `css/devices-styles.css` |
| Users (list, read-only) | `users.html` | `css/users-styles.css` |
| Home (leave home) | `home.html` | `css/home-styles.css` |
| Thresholds (system defaults, read-only) | `thresholds.html` | `css/thresholds-styles.css` |
| Thresholds (own limits, read-only) | `thresholds-edit.html` | `css/thresholds-styles.css` |

## What changes for a member (evidence in `src/features/DetailHome/`)

| Screen | Owner sees | Member sees | Code |
|--------|-----------|-------------|------|
| Users | Remove (×) button on every non-owner row, "Invite user by email" card, "Pending invitations" card | Member list only | `Users.jsx`: `isOwner && user.role !== "owner"` (remove), `isOwner && (…cardInvite)`, `isOwner && (…cardPending)` |
| Devices | "Link device" header button, delete button per row, link/delete modals, empty-state CTA | List only; empty state reads "El responsable del proyecto aún no ha vinculado dispositivos." with no button | `Devices.jsx`: `isOwner && (<Button …>)`, `isOwner && (<button removeBtn>)`, `{isOwner ? t("empty.subtitle") : t("empty.subtitleReadOnly")}`, `modalOpen && isOwner` |
| Home | Access-code section (hint + code + copy button) and "Eliminar hogar" | No access-code section; "Salirse del hogar" (log-out icon) instead of delete | `Home.jsx`: `isOwner ? (<accessCode…>) : null`, `isOwner ? deleteHome : leaveHome` |
| Thresholds | Lock notice absent, functional toggle, editable daily/monthly fields, "Personalizar límites" and the "Guardar cambios" button | Gray lock notice "Solo el responsable del hogar puede modificar estos umbrales.", toggle disabled (`opacity: .6`), fields disabled, and **no save button at all** — it is not rendered, not disabled | `Thresholds.jsx`: `editable = isOwner && !usingDefaults`, then `disabled={!editable}` on the period buttons and the input, and `{editable && (<div className={styles.actions}>…)}` for the save button |

Home information and the responsible-user card are read-only for **both** roles in `Home.jsx`
(there is no edit form), so those cards are identical to the owner view.

### Thresholds for a member

The owner view is split in two files (`thresholds.html` = system defaults on, `thresholds-edit.html`
= own limits) and so is the member one, because the member's home can be in either state. The two
member files differ from their owner counterparts in exactly four places:

| Element | Owner | Member |
|---|---|---|
| Lock notice above the card | absent | present: `Solo el responsable del hogar puede modificar estos umbrales.` |
| Toggle | functional | `disabled` → `opacity: .6`, `cursor: not-allowed` (`.switch.is-disabled`) |
| Limit fields | editable | `disabled` (the app relies on the browser's default look for `<input disabled>`) |
| "Guardar cambios" button | only in `thresholds-edit.html` | **absent in both files** |

Everything else is identical: title, subtitle, the toggle row, the "Configurar por" selector, the
`(Calculado)` badge, the `kWh` units and the closing scope note.

## Not duplicated here

- **Consumption** and **History** — identical for both roles (`Consumption.jsx` and
  `ComsumptionHistory.jsx` do not read `isOwner`). See `../owner/consumption.html` and
  `../owner/consumption-history.html`; the tab links in this folder point there.
- **Thresholds** — the tab links to `../owner/thresholds.html`. Note: `Thresholds.jsx` does change
  for a member (read-only notice with a lock icon "Solo el responsable del hogar puede modificar
  estos umbrales.", disabled toggles and inputs, no "Guardar cambios" button), but no member
  mockup has been drawn for it yet.
