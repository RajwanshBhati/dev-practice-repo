import axiosInstance from './axios';

/**
 * Get the currently logged-in user's full profile.
 * @returns {Promise} Current user details
 */
export const getCurrentUser = async () => {
    const response = await axiosInstance.get('/users/me');
    return response.data;
};

/**
 * Update the currently logged-in user's own profile.
 * Only full_name and phone are accepted by the backend.
 *
 * @param {Object} data
 * @param {string} [data.full_name]
 * @param {string} [data.phone]
 * @returns {Promise} Update confirmation with the updated user
 */
export const updateCurrentUser = async (data) => {
    const response = await axiosInstance.put('/users/me', data);
    return response.data;
};
