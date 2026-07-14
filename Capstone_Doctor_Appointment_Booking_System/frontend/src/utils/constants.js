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
    { value: 'REJECTED', label: 'Rejected' }
];

export const DOCTOR_STATUS_COLORS = {
    PENDING: 'warning',
    APPROVED: 'success',
    REJECTED: 'danger'
};

export const DOCTOR_STATUS_LABELS = {
    PENDING: 'Pending',
    APPROVED: 'Approved',
    REJECTED: 'Rejected'
};

/**
 * Audit action types and labels.
 */
export const AUDIT_ACTIONS = {
    CREATE_FIRST_ADMIN: 'Create First Admin',
    APPROVE_DOCTOR: 'Approve Doctor',
    REJECT_DOCTOR: 'Reject Doctor',
};

export const AUDIT_ACTION_COLORS = {
    CREATE_FIRST_ADMIN: 'success',
    APPROVE_DOCTOR: 'success',
    REJECT_DOCTOR: 'danger',
};
/**
 * Payment method options.
 */
export const PAYMENT_METHODS = [
    { value: 'CREDIT_CARD', label: 'Credit Card' },
    { value: 'DEBIT_CARD', label: 'Debit Card' },
    { value: 'UPI', label: 'UPI' },
    { value: 'NET_BANKING', label: 'Net Banking' },
    { value: 'WALLET', label: 'Wallet' },
];

/**
 * Payment status.
 */
export const PAYMENT_STATUS = {
    PENDING: 'PENDING',
    COMPLETED: 'COMPLETED',
    FAILED: 'FAILED',
    REFUND_INITIATED: 'REFUND_INITIATED',
    REFUNDED: 'REFUNDED',
};

export const PAYMENT_STATUS_LABELS = {
    PENDING: 'Pending',
    COMPLETED: 'Completed',
    FAILED: 'Failed',
    REFUND_INITIATED: 'Refund Initiated',
    REFUNDED: 'Refunded',
};

export const PAYMENT_STATUS_COLORS = {
    PENDING: 'warning',
    COMPLETED: 'success',
    FAILED: 'danger',
    REFUND_INITIATED: 'info',
    REFUNDED: 'secondary',
};
