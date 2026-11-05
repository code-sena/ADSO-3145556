import apiClient, { API_BASE, getToken, clearToken } from '../../../shared/services/apiClient';

const RESET_EMAIL_KEY = 'resetEmail';
const AUTH_EVENT = 'eduaircontrol:auth';

let currentUser = null;

function notifyAuthChanged() {
  window.dispatchEvent(new Event(AUTH_EVENT));
}

function base64UrlDecode(str) {
  const base64 = str.replace(/-/g, '+').replace(/_/g, '/');
  const padded = base64 + '='.repeat((4 - (base64.length % 4)) % 4);
  return atob(padded);
}

function decodeJWT(token) {
  try {
    const payload = token.split('.')[1];
    return JSON.parse(base64UrlDecode(payload));
  } catch {
    return null;
  }
}

function setSession(token, extraUser = {}) {
  localStorage.setItem('token', token);
  const decoded = decodeJWT(token);
  const user = {
    id: decoded?.sub || extraUser.id || null,
    email: decoded?.email || extraUser.email || '',
    username: decoded?.username || extraUser.username || '',
    role: decoded?.roles?.[0] || extraUser.role || 'USER',
    roles: decoded?.roles || (decoded?.roles?.[0] ? [decoded.roles[0]] : (extraUser.role ? [extraUser.role] : ['USER'])),
    name: decoded?.username || extraUser.name || (decoded?.email || extraUser.email || '').split('@')[0],
    institutionId: decoded?.institutionId || extraUser.institutionId || null,
    campusId: decoded?.campusId || extraUser.campusId || null,
    ...extraUser,
  };
  localStorage.setItem('user', JSON.stringify(user));
  currentUser = user;
  notifyAuthChanged();
  return user;
}

const authService = {
  async getCurrentUser(force = false) {
    if (currentUser && !force) return currentUser;

    const token = getToken();
    if (token) {
      const decoded = decodeJWT(token);
      if (decoded && decoded.exp * 1000 > Date.now()) {
        currentUser = {
          id: decoded.sub || null,
          email: decoded.email || '',
          username: decoded.username || '',
          role: decoded.roles?.[0] || 'USER',
          roles: decoded.roles || (decoded.roles?.[0] ? [decoded.roles] : ['USER']),
          name: decoded.username || (decoded.email || '').split('@')[0],
          institutionId: decoded.institutionId || null,
          campusId: decoded.campusId || null,
        };
        return currentUser;
      }
      clearToken();
    }

    try {
      const me = await apiClient.get('/api/v1/me');
      currentUser = {
        id: me.userId || null,
        email: me.email || '',
        role: me.role || 'USER',
        roles: me.roles || (me.role ? [me.role] : []),
        name: (me.email || '').split('@')[0],
        institutionId: me.institutionId || null,
        campusId: me.campusId || null,
      };
    } catch {
      currentUser = null;
    }
    return currentUser;
  },

  async loginWithCredentials(email, password) {
    const data = await apiClient.post('/api/v1/auth/login', { email, password });
    if (data.refreshToken) {
      localStorage.setItem('refreshToken', data.refreshToken);
    }
    return setSession(data.accessToken, data.user);
  },

  loginWithOAuth2() {
    window.location.assign(`${API_BASE}/oauth2/authorization/web`);
  },

  async completeSocialOnboarding(userId, companyCode) {
    return apiClient.post('/api/v1/auth/oauth2/onboarding', { userId, institutionId: companyCode });
  },

  async refreshToken() {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) return null;
    const data = await apiClient.post('/api/v1/auth/refresh', { refreshToken });
    if (data.refreshToken) {
      localStorage.setItem('refreshToken', data.refreshToken);
    }
    return setSession(data.accessToken, data.user);
  },

  async logout() {
    const token = getToken();
    if (token) {
      try {
        await apiClient.post('/api/v1/auth/logout', {});
      } catch {
        // ignore
      }
      clearToken();
    } else {
      try {
        await fetch(`${API_BASE}/logout`, { method: 'POST', credentials: 'include' });
      } catch {
        // ignore
      }
    }
    currentUser = null;
    notifyAuthChanged();
    window.location.assign('/login');
  },

  async register(name, email, password, companyCode) {
    return apiClient.post('/api/v1/auth/register', {
      name,
      email,
      password,
      username: (name || email.split('@')[0]).trim().slice(0, 100),
      companyCode,
    });
  },

  async forgotPassword(email) {
    return apiClient.post('/api/v1/auth/forgot-password', { email });
  },

  async verifyCode(email, code) {
    return apiClient.post('/api/v1/auth/verify-code', { email, code });
  },

  async resetPassword(email, code, newPassword) {
    return apiClient.post('/api/v1/auth/reset-password', { email, code, newPassword });
  },

  async resendCode(email) {
    return apiClient.post('/api/v1/auth/resend-code', { email });
  },

  async changePassword(currentPassword, newPassword) {
    return apiClient.post('/api/v1/auth/change-password', { currentPassword, newPassword });
  },

  async deleteAccount(password) {
    return apiClient.deleteWithBody('/api/v1/auth/account', { password });
  },

  setResetEmail(email) {
    sessionStorage.setItem(RESET_EMAIL_KEY, email);
  },

  getResetEmail() {
    return sessionStorage.getItem(RESET_EMAIL_KEY) || '';
  },

  clearResetEmail() {
    sessionStorage.removeItem(RESET_EMAIL_KEY);
  },

  getUser() {
    return currentUser;
  },

  isAuthenticated() {
    const token = getToken();
    if (token) {
      const decoded = decodeJWT(token);
      return decoded && decoded.exp * 1000 > Date.now();
    }
    return Boolean(currentUser);
  },

  isAdmin() {
    return String(currentUser?.role || '').toUpperCase() === 'ADMIN'
      || String(currentUser?.role || '').toUpperCase() === 'SUPER_ADMIN';
  },

  isSuperAdmin() {
    return String(currentUser?.role || '').toUpperCase() === 'SUPER_ADMIN';
  },
};

export default authService;
