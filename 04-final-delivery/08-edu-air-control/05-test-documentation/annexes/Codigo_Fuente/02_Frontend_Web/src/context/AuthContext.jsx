import { useEffect, useState, useRef } from 'react';
import authService from '../modules/auth/services/authService';
import { AuthContext } from './useAuth';

const PUBLIC_PATHS = ['/landing', '/guide', '/terms', '/login', '/forgot-password', '/verify-code', '/change-password'];

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const fetchingRef = useRef(false);

  const hydrate = async () => {
    if (fetchingRef.current) return;
    fetchingRef.current = true;
    try {
      const me = await authService.getCurrentUser(true);
      setUser(me);
    } finally {
      setLoading(false);
      fetchingRef.current = false;
    }
  };

  useEffect(() => {
    const isPublic = PUBLIC_PATHS.some((p) => window.location.pathname.startsWith(p));
    if (!isPublic) {
      hydrate();
    } else {
      setLoading(false);
    }

    const handler = () => {
      authService.getCurrentUser(true).then((me) => setUser(me));
    };
    window.addEventListener('eduaircontrol:auth', handler);
    return () => window.removeEventListener('eduaircontrol:auth', handler);
  }, []);

  const roles = (user?.roles || []).map((r) => String(r).toUpperCase());
  const value = {
    user,
    loading,
    isAuthenticated: Boolean(user),
    isAdmin: roles.includes('ADMIN') || roles.includes('SUPER_ADMIN'),
    isSuperAdmin: roles.includes('SUPER_ADMIN'),
    refresh: hydrate,
    logout: () => authService.logout(),
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
