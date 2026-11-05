import apiClient from '../../../shared/services/apiClient'

/**
 * Ambientes educativos (ms-classroom-management) y favoritos
 * (ms-user-experience).
 *
 * <p>Antes apuntaba a `/api/v1/environments`, que no existe: la lista entera era
 * un 404. El backend sirve `/api/v1/educational-environments` y los favoritos en
 * `/api/v1/favorites`.
 */
const BASE = '/api/v1/educational-environments'

function toUi(env) {
  if (!env) return env
  return {
    id: env.id,
    code: env.code,
    name: env.name,
    floor: env.floor,
    capacity: env.occupancyCapacity,
    envType: env.environmentTypeId,
    campusId: env.campusId,
    status: env.status,
    isFavorite: Boolean(env.favorite),
  }
}

function toList(data) {
  if (Array.isArray(data)) return data
  return data?.data || data?.items || []
}

function toPayload(environment) {
  const payload = {}
  if (environment.campusId) payload.campusId = environment.campusId
  if (environment.code) payload.code = environment.code
  if (environment.name) payload.name = environment.name
  if (environment.environmentTypeId) payload.environmentTypeId = environment.environmentTypeId
  if (environment.floor !== undefined && environment.floor !== null) {
    payload.floor = Number(environment.floor)
  }
  if (environment.capacity !== undefined) payload.occupancyCapacity = Number(environment.capacity)
  if (environment.status) payload.status = environment.status
  return payload
}

const environmentService = {
  async getAll() {
    return toList(await apiClient.get(BASE, { params: { limit: 100 } })).map(toUi)
  },

  async getById(id) {
    return toUi(await apiClient.get(`${BASE}/${id}`))
  },

  /** Última medición por (ambiente, variable) de todos los ambientes. */
  async getMetrics() {
    const rows = await apiClient.get('/api/v1/environments/current')
    return Array.isArray(rows) ? rows : []
  },

  async getFavoriteIds() {
    const ids = await apiClient.get('/api/v1/favorites')
    return Array.isArray(ids) ? ids : []
  },

  async create(environment) {
    return toUi(await apiClient.post(BASE, toPayload(environment)))
  },

  async update(id, updates) {
    return toUi(await apiClient.patch(`${BASE}/${id}`, toPayload(updates)))
  },

  async delete(id) {
    return apiClient.delete(`${BASE}/${id}`)
  },

  async toggleFavorite(id) {
    const result = await apiClient.post(`/api/v1/favorites/${id}/toggle`)
    return Boolean(result?.favorite)
  },
}

export default environmentService
