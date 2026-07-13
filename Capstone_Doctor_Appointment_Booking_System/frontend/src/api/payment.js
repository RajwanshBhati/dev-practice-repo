import axiosInstance from './axios';

/**
 * Initiate a payment for an appointment.
 * @param {Object} data - Payment initiation data
 * @param {string} data.appointment_id - Appointment ID
 * @param {string} data.method - Payment method (CREDIT_CARD, UPI, etc.)
 * @returns {Promise} Payment initiation response
 */
export const initiatePayment = async (data) => {
    const response = await axiosInstance.post('/payments/initiate', data);
    return response.data;
};

/**
 * Confirm a payment.
 * @param {Object} data - Payment confirmation data
 * @param {string} data.payment_id - Payment ID
 * @param {string} data.card_last_four - Last 4 digits of card (optional)
 * @param {string} data.upi_id - UPI ID (optional)
 * @returns {Promise} Payment confirmation response
 */
export const confirmPayment = async (data) => {
    const response = await axiosInstance.post('/payments/confirm', data);
    return response.data;
};

/**
 * Get payment status by payment ID.
 * @param {string} paymentId - Payment ID
 * @returns {Promise} Payment status
 */
export const getPaymentStatus = async (paymentId) => {
    const response = await axiosInstance.get(`/payments/${paymentId}/status`);
    return response.data;
};

/**
 * Refund a payment.
 * @param {string} paymentId - Payment ID
 * @param {Object} data - Refund data
 * @param {string} data.reason - Reason for refund
 * @returns {Promise} Refund confirmation
 */
export const refundPayment = async (paymentId, data) => {
    const response = await axiosInstance.post(`/payments/${paymentId}/refund`, data);
    return response.data;
};

/**
 * Get patient payment history.
 * @param {Object} params - Query parameters
 * @param {number} params.limit - Results per page
 * @param {number} params.skip - Results to skip
 * @returns {Promise} Payment history
 */
export const getPatientPayments = async (params) => {
    const response = await axiosInstance.get('/patients/payments', { params });
    return response.data;
};

/**
 * Get revenue statistics (Admin only).
 * @param {string} doctorId - Optional doctor ID
 * @returns {Promise} Revenue stats
 */
export const getRevenueStats = async (doctorId) => {
    const response = await axiosInstance.get('/payments/revenue', {
        params: { doctor_id: doctorId },
    });
    return response.data;
};
