import apiClient from '../../../shared/services/apiClient'

const BASE = '/api/v1/institutions'

const institutionService = {
  async getAll() {
    const data = await apiClient.get(BASE)
    return Array.isArray(data) ? data : []
  },

  async getById(id) {
    return apiClient.get(`${BASE}/${id}`)
  },

  async create({ code, name, type }) {
    return apiClient.post(BASE, { code, name, type })
  },

  async update(id, { name, type, status }) {
    return apiClient.put(`${BASE}/${id}`, { name, type, status })
  },
}

export default institutionService
