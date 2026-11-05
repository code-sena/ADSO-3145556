/**
 * Reads the dateFormat from localStorage and formats a Date accordingly.
 * Any component that calls this will always use the user's chosen format.
 */
export function formatDate(date, formatStr) {
  const dd   = String(date.getDate()).padStart(2, '0')
  const mm   = String(date.getMonth() + 1).padStart(2, '0')
  const yyyy = String(date.getFullYear())

  return (formatStr || 'DD/MM/YYYY')
    .replace('DD',   dd)
    .replace('MM',   mm)
    .replace('YYYY', yyyy)
}

export function getDateFormat() {
  return localStorage.getItem('dateFormat') || 'DD/MM/YYYY'
}

export function saveDateFormat(fmt) {
  localStorage.setItem('dateFormat', fmt)
  // Dispatch a custom event so any component can react immediately
  window.dispatchEvent(new CustomEvent('dateFormatChanged', { detail: fmt }))
}