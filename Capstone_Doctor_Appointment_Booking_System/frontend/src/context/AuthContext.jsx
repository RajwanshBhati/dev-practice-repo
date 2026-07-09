import { createContext, useState, useContext, useEffect } from 'react';
import { loginUser as apiLogin, logoutUser as apiLogout } from '../api/auth';
import toast from 'react-hot-toast';


const IDLE_TIMEOUT_MS = 15 * 60 * 1000; // 15 minutes
const ACTIVITY_EVENTS = ['mousemove', 'keydown', 'click', 'scroll', 'touchstart'];
/**
 * Create Auth Context.
 * This context will be used to provide auth state to all components.
 */
const AuthContext = createContext();

/**
 * Custom hook to use the Auth Context.
 * This hook should be used instead of useContext(AuthContext) directly.
 * It provides better error handling and makes the code cleaner.
 *
 * @returns {Object} Auth context value
 */
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

/**
 * Auth Provider component.
 * Wraps the application and provides authentication state to all child components.
 *
 * @param {React.ReactNode} children - Child components
 * @returns {JSX.Element} Auth provider component
 */
export const AuthProvider = ({ children }) => {
  /**
   * State variables.
   * - user: Current user object (null if not logged in)
   * - loading: Whether auth state is being loaded from localStorage
   * - isAuthenticated: Whether user is logged in
   */
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  /**
   * Load user from localStorage on initial mount.
   * This restores the user session after page refresh.
   */
  useEffect(() => {
    const loadUser = async () => {
      const storedUser = localStorage.getItem('user');
      const token = localStorage.getItem('access_token');

      if (storedUser && token) {
        try {
          setUser(JSON.parse(storedUser));
          setIsAuthenticated(true);
        } catch {
          /**
           * If stored user data is corrupted, clear all stored data.
           * This prevents the application from getting stuck in an invalid state.
           */
          localStorage.removeItem('user');
          localStorage.removeItem('access_token');
          localStorage.removeItem('refresh_token');
        }
      }
      setLoading(false);
    };

    loadUser();
  }, []);

  /**
   * Login function.
   * Authenticates user and stores tokens and user data.
   *
   * @param {string} email - User email
   * @param {string} password - User password
   * @returns {Promise} Login response data
   * @throws {Error} If login fails
   */
  const login = async (email, password) => {
  try {
    const data = await apiLogin({ email, password });

    const userData = data.user;
    setUser(userData);
    setIsAuthenticated(true);

    localStorage.setItem('access_token', data.access_token);
    localStorage.setItem('refresh_token', data.refresh_token);
    localStorage.setItem('user', JSON.stringify(userData));

    toast.success(data.message || 'Login successful');

    return data;
  } catch (error) {
    setUser(null);
    setIsAuthenticated(false);
    throw error;
  }
};

  /**
   * Logout function.
   * Invalidates the session and clears all stored data.
   * Sends logout request to the backend to blacklist the token.
   */
  const logout = async () => {
    try {
      const token = localStorage.getItem('access_token');
      if (token) {
        await apiLogout(token);
      }
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      /**
       * Clear all stored authentication data regardless of API success.
       * This ensures the user is logged out even if the API call fails.
       */
      setUser(null);
      setIsAuthenticated(false);
      localStorage.removeItem('access_token');
      localStorage.removeItem('refresh_token');
      localStorage.removeItem('user');
      toast.success('Logged out successfully');
    }
  };

  useEffect(() => {
  if (!isAuthenticated) return;

  let idleTimer;

  const resetTimer = () => {
    clearTimeout(idleTimer);
    idleTimer = setTimeout(() => {
      toast.error('Session expired due to inactivity');
      logout();
    }, IDLE_TIMEOUT_MS);
  };

  ACTIVITY_EVENTS.forEach((evt) => window.addEventListener(evt, resetTimer));
  resetTimer();

  return () => {
    clearTimeout(idleTimer);
    ACTIVITY_EVENTS.forEach((evt) => window.removeEventListener(evt, resetTimer));
  };
}, [isAuthenticated]);

  /**
   * Context value.
   * Provides auth state and functions to all child components.
   */
  const value = {
    user,
    loading,
    isAuthenticated,
    login,
    logout,
    isPatient: user?.role === 'PATIENT',
    isDoctor: user?.role === 'DOCTOR',
    isAdmin: user?.role === 'ADMIN',
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
