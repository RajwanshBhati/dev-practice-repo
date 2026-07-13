/**
 * Doctor API service.
 * Handles doctor search, profile, and availability operations.
 */

import axiosInstance from './axios';

/**
 * Search doctors with filters.
 * @param {Object} params - Search parameters
 * @param {string} params.query - Search by name or specialization
 * @param {string} params.specialization - Filter by specialization
 * @param {string} params.location - Filter by location
 * @param {number} params.min_experience - Minimum years of experience
 * @param {number} params.max_fee - Maximum consultation fee
 * @param {number} params.min_rating - Minimum rating (0-5)
 * @param {number} params.limit - Results per page
 * @param {number} params.skip - Results to skip
 * @returns {Promise} Search results
 */
export const searchDoctors = async (params) => {
    const response = await axiosInstance.get('/doctor/search', { params });
    return response.data;
};

/**
 * Get public doctor profile by ID.
 * @param {string} doctorId - Doctor ID
 * @returns {Promise} Doctor profile
 */
export const getDoctorById = async (doctorId) => {
    const response = await axiosInstance.get(`/doctor/public/${doctorId}`);
    return response.data;
};

/**
 * Get all available specializations.
 * @returns {Promise} List of specializations
 */
export const getSpecializations = async () => {
    const response = await axiosInstance.get('/doctor/specializations');
    return response.data;
};

/**
 * Get doctor availability by doctor ID and date.
 * @param {string} doctorId - Doctor ID
 * @param {string} date - Date in YYYY-MM-DD format
 * @returns {Promise} Availability slots
 */
export const getDoctorAvailability = async (doctorId, date) => {
    const response = await axiosInstance.get(`/doctors/${doctorId}/availability`, {
        params: { date },
    });
    return response.data;
};

