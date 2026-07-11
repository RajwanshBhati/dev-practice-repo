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

/**
 * Calculate age in years from a date of birth string.
 * @param {string} dob - Date of birth (YYYY-MM-DD, from <input type="date">)
 * @returns {number} Age in years
 */
export const calculateAge = (dob) => {
    const birthDate = new Date(dob);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }
    return age;
};

/**
 * Check whether a date of birth meets a minimum age requirement.
 * @param {string} dob
 * @param {number} minAge
 * @returns {boolean}
 */
export const isValidAge = (dob, minAge) => {
    if (!dob) return false;
    return calculateAge(dob) >= minAge;
};

/**
 * List of password requirements with individual test functions.
 * Used to show a live checklist to the user instead of one error at a time.
 */
export const PASSWORD_REQUIREMENTS = [
    { id: 'length', label: '8-12 characters', test: (p) => p.length >= 8 && p.length <= 12 },
    { id: 'uppercase', label: 'One uppercase letter (A-Z)', test: (p) => /[A-Z]/.test(p) },
    { id: 'lowercase', label: 'One lowercase letter (a-z)', test: (p) => /[a-z]/.test(p) },
    { id: 'digit', label: 'One digit (0-9)', test: (p) => /[0-9]/.test(p) },
    { id: 'special', label: 'One special character (!@#$%^&* etc.)', test: (p) => /[!@#$%^&*(),.?":{}|<>]/.test(p) },
];

/**
 * Get a checklist showing which password requirements are met.
 * @param {string} password
 * @returns {Array<{id:string, label:string, met:boolean}>}
 */
export const getPasswordChecklist = (password) =>
    PASSWORD_REQUIREMENTS.map((req) => ({
        id: req.id,
        label: req.label,
        met: req.test(password || ''),
    }));



