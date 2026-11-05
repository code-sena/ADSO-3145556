import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../../context/useAuth';
import Spinner from '../Spinner/Spinner';

function SuperAdminRoute({ children }) {
  const { isAuthenticated, isSuperAdmin, loading } = useAuth();
  const location = useLocation();

  if (loading) return <Spinner size="lg" label="Verificando sesión..." fullscreen />;
  if (!isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }
  if (!isSuperAdmin) {
    return <Navigate to="/dashboard" replace />;
  }

  return children;
}

export default SuperAdminRoute;
