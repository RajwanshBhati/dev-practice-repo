import axiosInstance from './axios';

/**
 * Create availability slot.
 * @param {Object} data - Slot data
 * @param {string} data.date - Date in YYYY-MM-DD format
 * @param {string} data.start_time - Start time in HH:MM format
 * @param {string} data.end_time - End time in HH:MM format
 * @returns {Promise} Created slot
 */
export const createAvailability = async (data) => {
    const response = await axiosInstance.post('/doctors/availability', data);
    return response.data;
};

/**
 * Get doctor availability slots.
 * @param {Object} params - Query parameters
 * @param {string} params.date - Filter by date
 * @param {boolean} params.include_booked - Include booked slots
 * @param {number} params.limit - Results per page
 * @param {number} params.skip - Results to skip
 * @returns {Promise} List of slots
 */
export const getAvailabilitySlots = async (params) => {
    const response = await axiosInstance.get('/doctors/availability', { params });
    return response.data;
};

/**
 * Update availability slot.
 * @param {string} slotId - Slot ID
 * @param {Object} data - Update data
 * @returns {Promise} Updated slot
 */
export const updateAvailabilitySlot = async (slotId, data) => {
    const response = await axiosInstance.put(`/doctors/availability/${slotId}`, data);
    return response.data;
};

/**
 * Delete availability slot.
 * @param {string} slotId - Slot ID
 * @returns {Promise} Deletion confirmation
 */
export const deleteAvailabilitySlot = async (slotId) => {
    const response = await axiosInstance.delete(`/doctors/availability/${slotId}`);
    return response.data;
};

/**
 * Get availability statistics.
 * @returns {Promise} Slot statistics
 */
export const getAvailabilityStats = async () => {
    const response = await axiosInstance.get('/doctors/availability/stats');
    return response.data;
};
