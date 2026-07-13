import axiosInstance from './axios';

/**
 * Book an appointment.
 * @param {Object} data - Appointment data
 * @param {string} data.doctor_id - Doctor ID
 * @param {string} data.appointment_date - Date in YYYY-MM-DD format
 * @param {string} data.appointment_time - Time in HH:MM format
 * @param {string} data.reason - Reason for visit
 * @param {string} data.notes - Additional notes
 * @returns {Promise} Booked appointment details
 */
export const bookAppointment = async (data) => {
    const response = await axiosInstance.post('/appointments/book', data);
    return response.data;
};

/**
 * Get patient appointments.
 * @param {Object} params - Query parameters
 * @param {string} params.status - Filter by status
 * @param {number} params.limit - Results per page
 * @param {number} params.skip - Results to skip
 * @returns {Promise} List of appointments
 */
export const getPatientAppointments = async (params) => {
    const response = await axiosInstance.get('/patients/appointments', { params });
    return response.data;
};

/**
 * Cancel an appointment.
 * @param {string} appointmentId - Appointment ID
 * @param {Object} data - Cancellation data
 * @param {string} data.reason - Reason for cancellation
 * @returns {Promise} Cancellation confirmation
 */
export const cancelAppointment = async (appointmentId, data) => {
    const response = await axiosInstance.put(`/appointments/${appointmentId}/cancel`, data);
    return response.data;
};

/**
 * Reschedule an appointment.
 * @param {string} appointmentId - Appointment ID
 * @param {Object} data - Reschedule data
 * @param {string} data.appointment_date - New date in YYYY-MM-DD format
 * @param {string} data.appointment_time - New time in HH:MM format
 * @param {string} data.reason - Reason for rescheduling
 * @returns {Promise} Reschedule confirmation
 */
export const rescheduleAppointment = async (appointmentId, data) => {
    const response = await axiosInstance.put(`/appointments/${appointmentId}/reschedule`, data);
    return response.data;
};

/**
 * Update appointment status (Doctor only).
 * @param {string} appointmentId - Appointment ID
 * @param {Object} data - Status update data
 * @param {string} data.status - New status
 * @param {string} data.notes - Notes about the status update
 * @returns {Promise} Status update confirmation
 */
export const updateAppointmentStatus = async (appointmentId, data) => {
    const response = await axiosInstance.put(`/appointments/${appointmentId}/status`, data);
    return response.data;
};


