import {
    FaCrown,
    FaUserPlus,
    FaUserMinus,
    FaCheckCircle,
    FaTimesCircle,
} from 'react-icons/fa';


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

/**
 * Slot duration options.
 */
export const SLOT_DURATIONS = [
    { label: '30 minutes', value: 30 },
    { label: '45 minutes', value: 45 },
    { label: '60 minutes', value: 60 },
];

/**
 * Working hours.
 */
export const WORKING_HOURS = {
    START: 9,
    END: 18,
};

/**
 * Appointment status options for doctor actions.
 */
export const STATUS_OPTIONS = [
    { value: 'SCHEDULED', label: 'Scheduled' },
    { value: 'CONFIRMED', label: 'Confirmed' },
    { value: 'COMPLETED', label: 'Completed' },
    { value: 'CANCELLED', label: 'Cancelled' },
    { value: 'NO_SHOW', label: 'No Show' },
    { value: 'RESCHEDULED', label: 'Rescheduled' },
];

/**
 * Status transition rules for doctors.
 */
export const STATUS_TRANSITIONS = {
    SCHEDULED: ['CONFIRMED', 'CANCELLED'],
    CONFIRMED: ['COMPLETED', 'NO_SHOW', 'CANCELLED'],
    COMPLETED: [],
    CANCELLED: [],
    NO_SHOW: [],
    RESCHEDULED: ['CONFIRMED', 'CANCELLED'],
};

/**
 * Doctor status for admin view.
 */
export const DOCTOR_STATUS_OPTIONS = [
    { value: 'ALL', label: 'All Doctors' },
    { value: 'PENDING', label: 'Pending' },
    { value: 'APPROVED', label: 'Approved' },
    { value: 'REJECTED', label: 'Rejected' },
    { value: 'SUSPENDED', label: 'Suspended' },
];

export const DOCTOR_STATUS_COLORS = {
    PENDING: 'warning',
    APPROVED: 'success',
    REJECTED: 'danger',
    SUSPENDED: 'secondary',
};

export const DOCTOR_STATUS_LABELS = {
    PENDING: 'Pending',
    APPROVED: 'Approved',
    REJECTED: 'Rejected',
    SUSPENDED: 'Suspended',
};

/**
 * Audit action types and labels.
 */
export const AUDIT_ACTIONS = {
    CREATE_FIRST_ADMIN: 'Create First Admin',
    CREATE_ADMIN: 'Create Admin',
    DELETE_ADMIN: 'Delete Admin',
    APPROVE_DOCTOR: 'Approve Doctor',
    REJECT_DOCTOR: 'Reject Doctor',
};

export const AUDIT_ACTION_COLORS = {
    CREATE_FIRST_ADMIN: 'success',
    CREATE_ADMIN: 'primary',
    DELETE_ADMIN: 'danger',
    APPROVE_DOCTOR: 'success',
    REJECT_DOCTOR: 'danger',
};

export const AUDIT_ACTION_ICONS = {
    CREATE_FIRST_ADMIN: FaCrown,
    CREATE_ADMIN: FaUserPlus,
    DELETE_ADMIN: FaUserMinus,
    APPROVE_DOCTOR: FaCheckCircle,
    REJECT_DOCTOR: FaTimesCircle,
};
