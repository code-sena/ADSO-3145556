import { createContext, useContext } from 'react';

export const AuthContext = createContext({
  user: null,
  loading: true,
  isAuthenticated: false,
  isAdmin: false,
  isSuperAdmin: false,
  refresh: async () => null,
  logout: async () => {},
});

export function useAuth() {
  return useContext(AuthContext);
}
