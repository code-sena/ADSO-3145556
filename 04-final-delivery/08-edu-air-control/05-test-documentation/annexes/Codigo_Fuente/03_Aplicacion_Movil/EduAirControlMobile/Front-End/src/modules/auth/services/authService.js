import apiClient, { clearSession } from '../../../shared/services/apiClient'
import storage from '../../../shared/storage/storage'

const BASE = '/api/v1/auth'

function base64UrlDecode(str) {
const normalized = str.replace(/-/g, '+').replace(/_/g, '/')
const padded = normalized + '='.repeat((4 - (normalized.length % 4)) % 4)
return atob(padded)
}

function decodeJWT(token) {
try {
const payload = token.split('.')[1]
return JSON.parse(base64UrlDecode(payload))
} catch {
return null
}
}

/**
 * Guarda la sesión con la forma real de la respuesta de ms-security:
 * `{ accessToken, refreshToken, expiresIn, user }` (antes se leía `data.token`,
 * que el backend no devuelve nunca, y el rol salía de `claims.role` cuando el
 * token lleva `roles[]`).
 */
async function storeSession(data) {
  if (!data?.accessToken) throw new Error('Respuesta de autenticación inválida')
  await storage.setItem('token', data.accessToken)
  if (data.refreshToken) await storage.setItem('refreshToken', data.refreshToken)

  const claims = decodeJWT(data.accessToken) || {}
  const summary = data.user || {}
  const roles = Array.isArray(summary.roles) && summary.roles.length
    ? summary.roles
    : (Array.isArray(claims.roles) ? claims.roles : (claims.role ? [claims.role] : []))

  const user = {
    id: summary.id || claims.userId || claims.sub || null,
    email: summary.email || claims.email || null,
    username: summary.username || claims.username || null,
    roles,
    role: roles[0] || 'USER',
    institutionId: summary.institutionId || claims.institutionId || null,
    campusId: summary.campusId || claims.campusId || null,
  }
  await storage.setItem('user', JSON.stringify(user))
  return user
}

const authService = {
  async login(email, password, companyCode) {
    const data = await apiClient.post('/auth/login', { email, password, companyCode })
    await storage.setItem('token', data.token)
    const claims = decodeJWT(data.token)
    const user = { email: claims?.sub || email, role: claims?.role || 'USER', name: email.split('@')[0] }
    await storage.setItem('user', JSON.stringify(user))
    return data
  },

  async register(name, email, password, companyCode) {
    const data = await apiClient.post('/auth/register', { name, email, password, companyCode })
    await storage.setItem('token', data.token)
    const claims = decodeJWT(data.token)
    const user = { email: claims?.sub || email, role: claims?.role || 'USER', name }
    await storage.setItem('user', JSON.stringify(user))
    return data
  },

  async logout() {
    await storage.removeItem('token')
    await storage.removeItem('user')
  },

  async forgotPassword(email) {
    return apiClient.post('/auth/forgot-password', { email })
  },

  async verifyCode(email, code) {
    return apiClient.post('/auth/verify-code', { email, code })
  },

  async resendCode(email) {
    return apiClient.post('/auth/resend-code', { email })
  },

  async resetPassword(email, code, newPassword) {
    return apiClient.post('/auth/reset-password', { email, code, newPassword })
  },

  async changePassword(currentPassword, newPassword) {
    return apiClient.post('/auth/change-password', { currentPassword, newPassword })
  },

  getToken() {
    return storage.getItem('token')
  },

getUser() {
try {
return JSON.parse(storage.getItem('user'))
} catch {
return null
}
},

  isAuthenticated() {
    const token = storage.getItem('token')
    if (!token) return false
    const claims = decodeJWT(token)
    if (!claims) {
      this.logout()
      return false
    }
    if (typeof claims.exp === 'number' && claims.exp * 1000 <= Date.now()) {
      this.logout()
      return false
    }
    return true
  },

  isAdmin() {
    if (!this.isAuthenticated()) return false
    const role = this.getUser()?.role || decodeJWT(this.getToken())?.role || ''
    return String(role).toUpperCase() === 'ADMIN'
  },
}

export default authService
