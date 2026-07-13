/**
 * User roles available in the system.
 * These roles determine what features and pages a user can access.
 */
export const ROLES = {
    PATIENT: 'PATIENT',
    DOCTOR: 'DOCTOR',
    ADMIN: 'ADMIN',
};

/**
 * Gender options for user registration.
 */
export const GENDERS = ['Male', 'Female', 'Other'];

/**
 * Medical specializations for doctor registration.
 */
export const SPECIALIZATIONS = [
    'Cardiologist',
    'Dermatologist',
    'Dentist',
    'Neurologist',
    'Orthopedic',
    'Pediatrician',
    'Psychiatrist',
    'Radiologist',
    'Surgeon',
    'Urologist',
    'Gynecologist',
    'Ophthalmologist',
    'ENT Specialist',
    'General Physician',
    'Other',
];

/**
 * API base URL.
 * This is the base URL for all API calls.
 */
export const API_URL =
    import.meta.env.VITE_API_URL || "http://localhost:8000/api/v1";

/**
 * Password validation rules.
 */
export const PASSWORD_RULES = {
    MIN_LENGTH: 8,
    MAX_LENGTH: 12,
    REQUIRE_UPPERCASE: true,
    REQUIRE_LOWERCASE: true,
    REQUIRE_DIGIT: true,
    REQUIRE_SPECIAL: true,
};


/**
 * Appointment status constants.
 */
export const APPOINTMENT_STATUS = {
    SCHEDULED: 'SCHEDULED',
    CONFIRMED: 'CONFIRMED',
    COMPLETED: 'COMPLETED',
    CANCELLED: 'CANCELLED',
    NO_SHOW: 'NO_SHOW',
    RESCHEDULED: 'RESCHEDULED',
};

/**
 * Appointment status colors for badges.
 */
export const STATUS_COLORS = {
    SCHEDULED: 'warning',
    CONFIRMED: 'info',
    COMPLETED: 'success',
    CANCELLED: 'danger',
    NO_SHOW: 'secondary',
    RESCHEDULED: 'primary',
};

/**
 * Appointment status labels for display.
 */
export const STATUS_LABELS = {
    SCHEDULED: 'Scheduled',
    CONFIRMED: 'Confirmed',
    COMPLETED: 'Completed',
    CANCELLED: 'Cancelled',
    NO_SHOW: 'No Show',
    RESCHEDULED: 'Rescheduled',
};

/**
 * Payment status constants.
 */
export const PAYMENT_STATUS = {
    PENDING: 'PENDING',
    COMPLETED: 'COMPLETED',
    FAILED: 'FAILED',
    REFUNDED: 'REFUNDED',
};

export const PAYMENT_STATUS_LABELS = {
    PENDING: 'Pending',
    COMPLETED: 'Completed',
    FAILED: 'Failed',
    REFUNDED: 'Refunded',
};
