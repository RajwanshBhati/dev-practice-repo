/**
 * Validate email format.
 * Uses a regular expression to check if the email follows the standard format.
 *
 * @param {string} email - Email address to validate
 * @returns {boolean} True if email is valid, false otherwise
 */
export const isValidEmail = (email) => {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
};

/**
 * Validate phone number format.
 * Phone number should be 10-15 digits and may contain + prefix.
 *
 * @param {string} phone - Phone number to validate
 * @returns {boolean} True if phone is valid, false otherwise
 */
export const isValidPhone = (phone) => {
    const cleaned = phone.replace(/[+\s]/g, '');
    const phoneRegex = /^[0-9]{10,15}$/;
    return phoneRegex.test(cleaned);
};

/**
 * Validate password strength
 *
 * @param {string} password - Password to validate
 * @returns {boolean} True if password meets requirements, false otherwise
 */
export const isValidPassword = (password) => {
    if (password.length < 8 || password.length > 12) {
        return false;
    }
    if (!/[A-Z]/.test(password)) {
        return false;
    }
    if (!/[a-z]/.test(password)) {
        return false;
    }
    if (!/[0-9]/.test(password)) {
        return false;
    }
    if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
        return false;
    }
    return true;
};

/**
 * Validate name format.
 * Name should contain only alphabets and spaces, with minimum 2 characters.
 *
 * @param {string} name - Name to validate
 * @returns {boolean} True if name is valid, false otherwise
 */
export const isValidName = (name) => {
    const nameRegex = /^[a-zA-Z\s]{2,}$/;
    return nameRegex.test(name);
};

/**
 * Get password validation error message.
 * Returns a user-friendly error message based on which validation fails.
 *
 * @param {string} password - Password to check
 * @returns {string} Error message or empty string if valid
 */
export const getPasswordError = (password) => {
    if (password.length < 8 || password.length > 12) {
        return 'Password must be between 8 and 12 characters';
    }
    if (!/[A-Z]/.test(password)) {
        return 'Password must contain at least one uppercase letter';
    }
    if (!/[a-z]/.test(password)) {
        return 'Password must contain at least one lowercase letter';
    }
    if (!/[0-9]/.test(password)) {
        return 'Password must contain at least one digit';
    }
    if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
        return 'Password must contain at least one special character';
    }
    return '';
};
