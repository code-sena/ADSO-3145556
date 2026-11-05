import { Navigate } from 'react-router-dom';
import { useAuth } from '../../../context/useAuth';
import Spinner from '../Spinner/Spinner';

function GuestRoute({ children }) {
  const { isAuthenticated, loading } = useAuth();

  if (loading) return <Spinner size="lg" label="Verificando sesión..." fullscreen />;
  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />;
  }

  return children;
}

export default GuestRoute;
