import { API_BASE } from '../config'
import storage from '../storage/storage'
import i18n from '../i18n/i18n'

/**
 * Rutas públicas de autenticación: sin token y un 401 ahí no significa sesión
 * caducada. El backend las sirve bajo `/api/v1/auth/*` — antes estaban sin el
 * prefijo y la detección de sesión caducada se activaba en el propio login.
 */
const PUBLIC_AUTH_ENDPOINTS = [
  '/api/v1/auth/login',
  '/api/v1/auth/register',
  '/api/v1/auth/forgot-password',
  '/api/v1/auth/verify-code',
  '/api/v1/auth/reset-password',
  '/api/v1/auth/resend-code',
  '/api/v1/auth/refresh',
  '/api/v1/auth/health',
  '/api/v1/auth/jwks',
]

let onUnauthorized = null

export function setOnUnauthorized(handler) {
  onUnauthorized = handler
}

function getToken() {
  return storage.getItem('token')
}

/**
 * Renueva el access token con el refresh token.
 *
 * <p>Antes no existía: un 401 borraba la sesión y mandaba al login aunque el
 * usuario tuviera un refresh token válido.
 */
async function tryRefreshToken() {
  const refreshToken = storage.getItem('refreshToken')
  if (!refreshToken) return false
  try {
    const res = await fetch(`${API_BASE}/api/v1/auth/refresh`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ refreshToken }),
    })
    if (!res.ok) return false
    const data = await res.json()
    if (!data?.accessToken) return false
    await storage.setItem('token', data.accessToken)
    if (data.refreshToken) await storage.setItem('refreshToken', data.refreshToken)
    return true
  } catch {
    return false
  }
}

async function clearSession() {
  await storage.removeItem('token')
  await storage.removeItem('refreshToken')
  await storage.removeItem('user')
}

async function request(endpoint, options = {}, baseUrl = API_BASE) {
  const token = getToken()
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...options.headers,
  }

  const doFetch = () =>
    fetch(`${baseUrl}${endpoint}`, {
      ...options,
      headers: { ...headers, ...(getToken() ? { Authorization: `Bearer ${getToken()}` } : {}) },
    })

  let response
  try {
    response = await doFetch()
  } catch {
    throw new Error(i18n.t('errors.network'))
  }

  // Un 401 con token puede ser un access token vencido: se intenta renovar una vez.
  if (response.status === 401 && token && !PUBLIC_AUTH_ENDPOINTS.some((p) => endpoint.startsWith(p))) {
    const refreshed = await tryRefreshToken()
    if (refreshed) {
      try {
        response = await doFetch()
      } catch {
        throw new Error(i18n.t('errors.network'))
      }
    }
  }

  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    const isPublicAuth = PUBLIC_AUTH_ENDPOINTS.some((p) => endpoint.startsWith(p))

    if (response.status === 401 && !isPublicAuth) {
      await clearSession()
      onUnauthorized?.()
      throw new Error(error.message || i18n.t('errors.sessionExpired'))
    }

    throw new Error(error.message || `Error ${response.status}`)
  }

  if (response.status === 204) return null
  return response.json()
}

const apiClient = {
  get: (endpoint, options = {}) => request(endpoint, options),
  post: (endpoint, body, options = {}) =>
    request(endpoint, { ...options, method: 'POST', body: JSON.stringify(body) }),
  put: (endpoint, body, options = {}) =>
    request(endpoint, { ...options, method: 'PUT', body: JSON.stringify(body) }),
  patch: (endpoint, body, options = {}) =>
    request(endpoint, { ...options, method: 'PATCH', body: JSON.stringify(body) }),
  delete: (endpoint, options = {}) => request(endpoint, { ...options, method: 'DELETE' }),
  request,
}

export { clearSession }
export default apiClient
