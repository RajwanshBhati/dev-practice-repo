import axios from 'axios';
import toast from 'react-hot-toast';
import { API_URL } from '../utils/constants';

const axiosInstance = axios.create({
    baseURL: API_URL,
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
    },
});

axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('access_token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        if (typeof FormData !== 'undefined' && config.data instanceof FormData) {
            delete config.headers['Content-Type'];
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// Concurrent refresh handling
let isRefreshing = false;
let failedQueue = [];
const MAX_REFRESH_ATTEMPTS = 1;

const processQueue = (error, token = null) => {
    failedQueue.forEach(({ resolve, reject }) => {
        if (error) reject(error);
        else resolve(token);
    });
    failedQueue = [];
};


const AUTH_ENDPOINTS = ['/auth/login', '/auth/register', '/auth/refresh-token'];


const extractErrorMessage = (error) => {
    const detail = error.response?.data?.detail;
    const clean = (msg) => (typeof msg === 'string' ? msg.replace(/^Value error,\s*/i, '') : msg);

    if (Array.isArray(detail)) {
        const messages = detail
            .map((item) => {
                if (typeof item === 'string') return item;
                if (item && typeof item === 'object' && item.msg) {
                    const field = Array.isArray(item.loc) ? item.loc[item.loc.length - 1] : null;
                    const msg = clean(item.msg);
                    return field ? `${field}: ${item.msg}` : item.msg;
                }
                return null;
            })
            .filter(Boolean);
        if (messages.length) return messages.join(', ');
    }

    if (typeof detail === 'string' && detail) return clean(detail);

    if (detail && typeof detail === 'object' && detail.msg) {
        return clean(detail.msg);
    }

    const message = error.response?.data?.message;
    if (typeof message === 'string' && message) return message;

    return 'Something went wrong. Please try again.';
};

axiosInstance.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;
        const isAuthEndpoint = AUTH_ENDPOINTS.some((path) =>
            originalRequest?.url?.includes(path)
        );

        if (error.code === 'ECONNABORTED') {
            toast.error('Request timed out. Please try again.');
            return Promise.reject(error);
        }

        if (error.response?.status === 401 && !originalRequest._retry && !isAuthEndpoint) {
            if (isRefreshing) {
                return new Promise((resolve, reject) => {
                    failedQueue.push({ resolve, reject });
                })
                    .then((token) => {
                        originalRequest.headers.Authorization = `Bearer ${token}`;
                        return axiosInstance(originalRequest);
                    })
                    .catch((err) => Promise.reject(err));
            }

            originalRequest._retry = true;
            originalRequest._refreshAttempts = (originalRequest._refreshAttempts || 0) + 1;
            isRefreshing = true;

            if (originalRequest._refreshAttempts > MAX_REFRESH_ATTEMPTS) {
                isRefreshing = false;
                localStorage.removeItem('access_token');
                localStorage.removeItem('refresh_token');
                localStorage.removeItem('user');
                window.location.href = '/login';
                return Promise.reject(error);
            }

            try {
                const refreshToken = localStorage.getItem('refresh_token');
                if (!refreshToken) throw new Error('No refresh token available');

                const response = await axios.post(`${API_URL}/auth/refresh-token`, {
                    refresh_token: refreshToken,
                });

                const { access_token } = response.data;
                localStorage.setItem('access_token', access_token);

                processQueue(null, access_token);
                originalRequest.headers.Authorization = `Bearer ${access_token}`;
                return axiosInstance(originalRequest);
            } catch (refreshError) {
                processQueue(refreshError, null);
                localStorage.removeItem('access_token');
                localStorage.removeItem('refresh_token');
                localStorage.removeItem('user');
                window.location.href = '/login';
                return Promise.reject(refreshError);
            } finally {
                isRefreshing = false;
            }
        }

        const message = extractErrorMessage(error);
        toast.error(message);

        return Promise.reject(error);
    }
);

export default axiosInstance;
