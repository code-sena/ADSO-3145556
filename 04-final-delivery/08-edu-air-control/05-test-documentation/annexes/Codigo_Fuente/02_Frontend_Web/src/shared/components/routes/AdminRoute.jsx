import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../../context/useAuth';
import Spinner from '../Spinner/Spinner';

function AdminRoute({ children }) {
  const { isAuthenticated, isAdmin, loading } = useAuth();
  const location = useLocation();

  if (loading) return <Spinner size="lg" label="Verificando sesión..." fullscreen />;
  if (!isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }
  if (!isAdmin) {
    return <Navigate to="/dashboard" replace />;
  }

  return children;
}

export default AdminRoute;
