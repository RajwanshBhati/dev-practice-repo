import axiosInstance from './axios';

/**
 * Register a new patient.
 * Sends patient registration data to the backend.
 *
 * @param {Object} data - Patient registration data
 * @param {string} data.full_name - Full name
 * @param {string} data.email - Email address
 * @param {string} data.phone - Phone number
 * @param {string} data.gender - Gender (Male/Female/Other)
 * @param {string} data.date_of_birth - Date of birth (DD-MM-YYYY)
 * @param {string} data.password - Password
 * @param {string} data.confirm_password - Password confirmation
 * @returns {Promise} API response with user data and tokens
 */
export const registerPatient = async (data) => {
    const response = await axiosInstance.post('/auth/register/patient', data);
    return response.data;
};

/**
 * Register a new doctor.
 * Sends doctor registration data to the backend.
 * Doctor accounts are created with PENDING status and require admin approval.
 *
 * @param {Object} data - Doctor registration data
 * @param {string} data.full_name - Full name
 * @param {string} data.email - Email address
 * @param {string} data.phone - Phone number
 * @param {string} data.gender - Gender (Male/Female/Other)
 * @param {string} data.date_of_birth - Date of birth (DD-MM-YYYY)
 * @param {string} data.password - Password
 * @param {string} data.confirm_password - Password confirmation
 * @param {string} data.qualification - Medical qualification
 * @param {string} data.specialization - Medical specialization
 * @param {number} data.experience_years - Years of experience
 * @param {string} data.license_number - Medical license number
 * @param {number} data.consultation_fee - Consultation fee
 * @param {string} data.clinic_address - Clinic address
 * @param {string} data.bio - Doctor biography (optional)
 * @returns {Promise} API response with user data and tokens
 */
export const registerDoctor = async (data) => {
    const response = await axiosInstance.post('/auth/register/doctor', data);
    return response.data;
};

/**
 * Login user.
 * Authenticates user with email and password.
 * Returns access token and refresh token on success.
 *
 * @param {Object} data - Login credentials
 * @param {string} data.email - Email address
 * @param {string} data.password - Password
 * @returns {Promise} API response with user data and tokens
 */
export const loginUser = async (data) => {
    const response = await axiosInstance.post('/auth/login', data);
    return response.data;
};

/**
 * Logout user.
 * Sends logout request to invalidate the current session.
 * Also clears local tokens on the client side.
 *
 * @param {string} accessToken - Current access token to blacklist
 * @returns {Promise} API response with logout confirmation
 */
export const logoutUser = async (accessToken) => {
    const response = await axiosInstance.post('/auth/logout', { access_token: accessToken });
    return response.data;
};

/**
 * Validate JWT token.
 * Checks if the current token is still valid.
 * Returns user information if token is valid.
 *
 * @returns {Promise} API response with user validation status
 */
export const validateToken = async () => {
    const response = await axiosInstance.post('/auth/validate-token');
    return response.data;
};
