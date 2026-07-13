import axiosInstance from './axios';

/**
 * Get doctor statistics for admin dashboard.
 * @returns {Promise} Doctor statistics
 */
export const getDoctorStatsAdmin = async () => {
    const response = await axiosInstance.get('/admin/doctors/stats');
    return response.data;
};

/**
 * Get all doctors with optional status filter.
 * @param {Object} params - Query parameters
 * @param {string} params.status - Filter by status (PENDING, APPROVED, REJECTED)
 * @param {number} params.limit - Results per page
 * @param {number} params.skip - Results to skip
 * @returns {Promise} List of doctors
 */
export const getAllDoctorsAdmin = async (params) => {
    const response = await axiosInstance.get('/admin/doctors', { params });
    return response.data;
};

/**
 * Get pending doctors.
 * @param {Object} params - Query parameters
 * @param {number} params.limit - Results per page
 * @param {number} params.skip - Results to skip
 * @returns {Promise} List of pending doctors
 */
export const getPendingDoctors = async (params) => {
    const response = await axiosInstance.get('/admin/doctors/pending', { params });
    return response.data;
};

/**
 * Approve a doctor.
 * @param {string} doctorId - Doctor ID
 * @param {Object} data - Approval data
 * @param {string} data.notes - Approval notes
 * @returns {Promise} Approval confirmation
 */
export const approveDoctor = async (doctorId, data) => {
    const response = await axiosInstance.post(`/admin/doctors/${doctorId}/approve`, data);
    return response.data;
};

/**
 * Reject a doctor.
 * @param {string} doctorId - Doctor ID
 * @param {Object} data - Rejection data
 * @param {string} data.reason - Rejection reason
 * @returns {Promise} Rejection confirmation
 */
export const rejectDoctor = async (doctorId, data) => {
    const response = await axiosInstance.post(`/admin/doctors/${doctorId}/reject`, data);
    return response.data;
};

/**
 * Get audit logs.
 * @param {Object} params - Query parameters
 * @param {string} params.admin_id - Filter by admin ID
 * @param {number} params.limit - Results per page
 * @param {number} params.skip - Results to skip
 * @returns {Promise} List of audit logs
 */
export const getAuditLogs = async (params) => {
    const response = await axiosInstance.get('/admin/audit-logs', { params });
    return response.data;
};
