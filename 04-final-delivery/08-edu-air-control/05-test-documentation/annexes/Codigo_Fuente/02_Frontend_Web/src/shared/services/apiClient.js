const API_BASE =
  import.meta.env.VITE_API_URL ||
  `${window.location.protocol}//${window.location.hostname}:8080`;

const PUBLIC_PATHS = [
  '/landing',
  '/guide',
  '/terms',
  '/login',
  '/forgot-password',
  '/verify-code',
  '/change-password',
];

function getToken() {
  return localStorage.getItem('token');
}

function clearToken() {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
}

function handleUnauthorized() {
  if (PUBLIC_PATHS.some((path) => window.location.pathname.startsWith(path))) return;
  clearToken();
  window.dispatchEvent(new Event('eduaircontrol:auth'));
  window.location.assign('/login');
}

async function tryRefreshToken() {
  const refreshToken = localStorage.getItem('refreshToken');
  if (!refreshToken) return false;

  try {
    const res = await fetch(`${API_BASE}/api/v1/auth/refresh`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ refreshToken }),
    });
    if (!res.ok) return false;
    const data = await res.json();
    localStorage.setItem('token', data.accessToken);
    if (data.refreshToken) localStorage.setItem('refreshToken', data.refreshToken);
    return true;
  } catch {
    return false;
  }
}

function messageFor(status, body) {
  if (body?.message) return body.message;
  switch (status) {
    case 400:
      return 'Datos inválidos';
    case 401:
      return 'Sesión expirada o credenciales inválidas';
    case 403:
      return 'No tienes permiso para esta acción';
    case 404:
      return 'Recurso no encontrado';
    case 409:
      return 'El recurso ya existe';
    case 429:
      return 'Demasiadas solicitudes, intenta más tarde';
    case 500:
      return 'Error del servidor';
    default:
      return `Error ${status}`;
  }
}

/**
 * Convierte { params } en query string, descartando los vacíos.
 *
 * <p>Antes cada llamada que necesitaba filtros se los montaba a mano y había
 * llamadas que se los pasaban como segundo argumento de `get`, que los ignoraba:
 * los filtros se perdían en silencio.
 */
function buildUrl(endpoint, params) {
  if (!params) return endpoint;
  const query = new URLSearchParams(
    Object.entries(params).filter(([, v]) => v !== undefined && v !== null && v !== '')
  ).toString();
  if (!query) return endpoint;
  return `${endpoint}${endpoint.includes('?') ? '&' : '?'}${query}`;
}

async function request(endpoint, options = {}) {
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  const token = getToken();
  const fetchOptions = { ...options, headers };

  if (token) {
    headers.Authorization = `Bearer ${token}`;
  } else {
    fetchOptions.credentials = 'include';
  }

  let response = await fetch(`${API_BASE}${endpoint}`, fetchOptions);

  if (response.status === 401 && token && endpoint !== '/api/v1/me') {
    const refreshed = await tryRefreshToken();
    if (refreshed) {
      headers.Authorization = `Bearer ${getToken()}`;
      response = await fetch(`${API_BASE}${endpoint}`, { ...fetchOptions, headers });
    } else {
      handleUnauthorized();
      throw new Error(messageFor(401, null));
    }
  }

  if (!response.ok) {
    const body = await response.json().catch(() => ({}));
    if (response.status === 401 && !token && endpoint !== '/api/v1/me') {
      handleUnauthorized();
    }
    throw new Error(messageFor(response.status, body));
  }

  if (response.status === 204) return null;
  return response.json();
}

const apiClient = {
  get: (endpoint, options = {}) => request(buildUrl(endpoint, options.params), options),
  post: (endpoint, body) => request(endpoint, { method: 'POST', body: JSON.stringify(body) }),
  put: (endpoint, body) => request(endpoint, { method: 'PUT', body: JSON.stringify(body) }),
  patch: (endpoint, body) => request(endpoint, { method: 'PATCH', body: JSON.stringify(body) }),
  delete: (endpoint) => request(endpoint, { method: 'DELETE' }),
  deleteWithBody: (endpoint, body) =>
    request(endpoint, { method: 'DELETE', body: JSON.stringify(body) }),
  request,
};

export { API_BASE, getToken, clearToken, buildUrl };
export default apiClient;
