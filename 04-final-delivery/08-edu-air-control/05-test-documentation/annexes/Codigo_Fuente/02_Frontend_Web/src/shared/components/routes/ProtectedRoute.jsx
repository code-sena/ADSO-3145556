import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../../context/useAuth';
import Spinner from '../Spinner/Spinner';

function ProtectedRoute({ children }) {
  const { isAuthenticated, loading } = useAuth();
  const location = useLocation();

  if (loading) return <Spinner size="lg" label="Verificando sesión..." fullscreen />;
  if (!isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  return children;
}

export default ProtectedRoute;
