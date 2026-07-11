import { Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import Loading from './Loading';

const PrivateRoute = ({ children, allowedRoles = [] }) => {
  const { isAuthenticated, user, loading } = useAuth();

  /**
   * Show loading state while authentication status is being determined.
   * This prevents flickering and unauthorized redirects during page load.
   */
  if (loading) {
    return <Loading message="Checking authentication..." />;
  }

  /**
   * Redirect to login if user is not authenticated.
   */
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  /**
   * Check role-based access.
   * If allowedRoles is provided, user must have one of those roles.
   * If allowedRoles is empty, any authenticated user can access.
   */
  if (allowedRoles.length > 0 && !allowedRoles.includes(user?.role)) {
    return <Navigate to="/" replace />;
  }

  return children;
};

export default PrivateRoute;
