import apiClient from '../../../shared/services/apiClient';

const BASE = '/api/v1/admin';

/**
 * Panel de administración global — ms-security. Requiere rol SUPER_ADMIN.
 */
const adminService = {
  async listUsers() {
    const data = await apiClient.get(`${BASE}/users`);
    return Array.isArray(data) ? data : [];
  },

  async assignRoles(userId, roleNames) {
    return apiClient.put(`${BASE}/users/${userId}/roles`, { roleNames });
  },

  async listRoles() {
    const data = await apiClient.get(`${BASE}/roles`);
    return Array.isArray(data) ? data : [];
  },
};

export default adminService;
