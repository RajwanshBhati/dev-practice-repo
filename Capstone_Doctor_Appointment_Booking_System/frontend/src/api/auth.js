import axiosInstance from './axios';
import { isValidEmail, isValidPhone, isValidPassword } from '../utils/validators';

/**
 * Basic client-side guard before hitting the registration endpoints.
 * Throws a plain Error with a user-friendly message if something is invalid.
 * This is a defense-in-depth check — the actual form components already
 * validate and show inline errors; this just protects the API layer too.
 *
 * @param {Object} data
 */
const assertValidRegistration = (data) => {
    if (!data.full_name || data.full_name.trim().length < 2) {
        throw new Error('Full name must be at least 2 characters');
    }
    if (!isValidEmail(data.email)) {
        throw new Error('Please enter a valid email address');
    }
    if (!isValidPhone(data.phone)) {
        throw new Error('Please enter a valid phone number');
    }
    if (!isValidPassword(data.password)) {
        throw new Error('Password does not meet the required strength rules');
    }
    if (data.password !== data.confirm_password) {
        throw new Error('Passwords do not match');
    }
};

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
    assertValidRegistration(data);
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
    assertValidRegistration(data);
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
    if (!isValidEmail(data.email)) {
        throw new Error('Please enter a valid email address');
    }
    if (!data.password) {
        throw new Error('Please enter your password');
    }
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

/**
 * Request a password reset link/email.
 * @param {string} email
 */
export const forgotPassword = async (email) => {
    if (!isValidEmail(email)) {
        throw new Error('Please enter a valid email address');
    }
    const response = await axiosInstance.post('/auth/forgot-password', { email });
    return response.data;
};

/**
 * Reset password using the token received via email.
 * @param {string} token
 * @param {string} password
 */
export const resetPassword = async (token, password) => {
    if (!token) {
        throw new Error('Reset token is missing or invalid');
    }
    if (!isValidPassword(password)) {
        throw new Error('Password does not meet the required strength rules');
    }
    const response = await axiosInstance.post('/auth/reset-password', { token, password });
    return response.data;
};
