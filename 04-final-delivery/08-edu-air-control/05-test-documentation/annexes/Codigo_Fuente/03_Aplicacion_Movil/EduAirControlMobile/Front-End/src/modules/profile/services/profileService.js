import apiClient from '../../../shared/services/apiClient'
import authService from '../../auth/services/authService'

/**
 * Perfil de usuario (ms-user-management).
 *
 * <p>Antes apuntaba a `/api/v1/profile`, que ni existe ni está routado por el
 * gateway. El backend sirve `/api/v1/users/by-user/{userId}` para leer (el id
 * sale del token) y `/api/v1/users/{id}` para actualizar.
 */
const BASE = '/api/v1/users'

const EMPTY = {
  id: null,
  fullName: '',
  email: '',
  phone: '',
  department: '',
  position: '',
  locale: '',
  avatarUrl: null,
}

function toUi(profile, fallbackEmail) {
  if (!profile) return { ...EMPTY, email: fallbackEmail || '' }
  return {
    id: profile.id,
    fullName: profile.fullName || '',
    email: fallbackEmail || '',
    phone: profile.phone || '',
    department: profile.department || '',
    position: profile.position || '',
    locale: profile.locale || '',
    avatarUrl: profile.avatarUrl || null,
  }
}

const profileService = {
  async get() {
    const user = authService.getUser()
    const email = user?.email || ''
    const userId = user?.id
    if (!userId) return { ...EMPTY, email }
    try {
      const remote = await apiClient.get(`${BASE}/by-user/${encodeURIComponent(userId)}`)
      return toUi(remote, email)
    } catch {
      // Sin perfil creado todavía: se devuelve el hueco y la UI lo permite editar.
      return { ...EMPTY, email }
    }
  },

  async save(profile) {
    const user = authService.getUser()
    const body = {
      fullName: profile.fullName,
      phone: profile.phone,
      department: profile.department,
      position: profile.position,
      locale: profile.locale,
      avatarUrl: profile.avatarUrl,
    }
    if (profile.id) {
      return apiClient.put(`${BASE}/${encodeURIComponent(profile.id)}`, body)
    }
    return apiClient.post(BASE, { ...body, userId: user?.id, fullName: profile.fullName })
  },
}

export default profileService
