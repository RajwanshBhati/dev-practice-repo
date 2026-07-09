import axios from 'axios';
import toast from 'react-hot-toast';
import { API_URL } from '../utils/constants';

/**
 * Create axios instance with default configuration.
 */
const axiosInstance = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

/**
 * Request interceptor.
 * Adds the JWT token to the Authorization header for every request.
 * If the token is not present in localStorage, the request proceeds without it.
 */
axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('access_token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

/**
 * Response interceptor.
 * Handles API responses and errors globally.
 * - On 401 Unauthorized: Attempts to refresh the token
 * - On other errors: Shows toast notification with error message
 */
axiosInstance.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        /**
         * Handle token expiration.
         * If we get a 401 error and haven't retried yet, attempt to refresh the token.
         */
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            try {
                const refreshToken = localStorage.getItem('refresh_token');
                const response = await axios.post(`${API_URL}/auth/refresh-token`, {
                    refresh_token: refreshToken,
                });

                const { access_token } = response.data;
                localStorage.setItem('access_token', access_token);

                originalRequest.headers.Authorization = `Bearer ${access_token}`;
                return axiosInstance(originalRequest);
            } catch (refreshError) {
                /**
                 * If refresh fails, clear all stored tokens and redirect to login.
                 * This indicates the user's session is completely expired.
                 */
                localStorage.removeItem('access_token');
                localStorage.removeItem('refresh_token');
                localStorage.removeItem('user');
                window.location.href = '/login';
                return Promise.reject(refreshError);
            }
        }

        /**
         * Extract and display error message.
         * Shows a toast notification for any API error.
         */
        const message = error.response?.data?.detail ||
            error.response?.data?.message ||
            'Something went wrong. Please try again.';
        toast.error(message);

        return Promise.reject(error);
    }
);

export default axiosInstance;
