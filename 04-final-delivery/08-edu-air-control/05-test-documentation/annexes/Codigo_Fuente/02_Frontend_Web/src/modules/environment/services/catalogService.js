import apiClient from '../../../shared/services/apiClient';

/**
 * Catálogos de ms-classroom-management.
 *
 * <p>La creación de ambientes exige {@code campusId} y {@code environmentTypeId}
 * como UUID. El formulario los ofrecía como etiquetas ("Aula", "Área Técnica") y
 * el backend devolvía 400: sin estos catálogos la UI no tiene forma de mandar
 * los identificadores correctos.
 */

function unwrap(data) {
  if (Array.isArray(data)) return data;
  return data?.data || [];
}

const catalogService = {
  async getCampuses() {
    const data = await apiClient.get('/api/v1/campuses', { params: { limit: 100 } });
    return unwrap(data).map((c) => ({
      id: c.campusId ?? c.id,
      code: c.code,
      name: c.name,
      city: c.city,
      status: c.status,
    }));
  },

  async getEnvironmentTypes() {
    const data = await apiClient.get('/api/v1/environment-types', { params: { limit: 100 } });
    return unwrap(data).map((t) => ({
      id: t.environmentTypeId ?? t.id,
      code: t.code,
      name: t.name,
      description: t.description,
    }));
  },
};

export default catalogService;
