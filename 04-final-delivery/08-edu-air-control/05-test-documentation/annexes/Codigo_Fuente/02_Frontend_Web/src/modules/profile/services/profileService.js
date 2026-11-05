import apiClient from '../../../shared/services/apiClient';
import authService from '../../auth/services/authService';

const EMPTY = { fullName: '', email: '', title: '', phone: '', location: '', avatarUrl: '' };

/**
 * Perfil del usuario (ms-user-management). La identidad viene del BFF
 * (authService.getUser()); el perfil se consulta/crea por el userId.
 */
const profileService = {
  async get() {
    const user = authService.getUser();
    if (!user) return EMPTY;
    if (user.id) {
      try {
        const remote = await apiClient.get(`/api/v1/users/by-user/${user.id}`);
        return {
          ...EMPTY,
          fullName: remote.fullName || '',
          email: user.email || '',
          title: remote.position || '',
          phone: remote.phone || '',
          location: remote.department || '',
          avatarUrl: remote.avatarUrl || '',
        };
      } catch {
        // sin perfil todavía
      }
    }
    return { ...EMPTY, fullName: user.name || '', email: user.email || '' };
  },

  async save(profile) {
    const user = authService.getUser();
    const payload = {
      fullName: profile.fullName,
      position: profile.title,
      phone: profile.phone,
      department: profile.location,
      avatarUrl: profile.avatarUrl || '',
    };
    if (!user?.id) {
      return { ...EMPTY, ...payload };
    }
    try {
      const existing = await apiClient.get(`/api/v1/users/by-user/${user.id}`);
      return apiClient.put(`/api/v1/users/${existing.id}`, payload);
    } catch {
      return apiClient.post('/api/v1/users', { userId: user.id, ...payload });
    }
  },
};

export default profileService;
