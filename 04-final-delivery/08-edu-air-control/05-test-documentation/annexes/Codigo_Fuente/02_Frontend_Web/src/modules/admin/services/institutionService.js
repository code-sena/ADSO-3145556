import apiClient from '../../../shared/services/apiClient';

const BASE = '/api/v1/institutions';

/**
 * Instituciones (tenants) — ms-security. Requiere rol SUPER_ADMIN.
 * El backend devuelve una lista (no paginada) de InstitutionResponse.
 */
const institutionService = {
  async getAll() {
    const data = await apiClient.get(BASE);
    return Array.isArray(data) ? data : [];
  },

  async getById(id) {
    return apiClient.get(`${BASE}/${id}`);
  },

  async create({ code, name, type }) {
    return apiClient.post(BASE, { code, name, type });
  },

  async update(id, { name, type, status }) {
    return apiClient.put(`${BASE}/${id}`, { name, type, status });
  },
};

export default institutionService;
