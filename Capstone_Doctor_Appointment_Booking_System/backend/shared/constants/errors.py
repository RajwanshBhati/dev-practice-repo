class ErrorCodes:
    # Authentication Errors
    INVALID_CREDENTIALS = "AUTH_1001"
    TOKEN_EXPIRED = "AUTH_1002"
    INVALID_TOKEN = "AUTH_1003"
    ACCOUNT_DEACTIVATED = "AUTH_1004"
    INSUFFICIENT_PERMISSIONS = "AUTH_1005"

    # User Errors
    USER_NOT_FOUND = "USER_1101"
    USER_ALREADY_EXISTS = "USER_1102"
    INVALID_USER_DATA = "USER_1103"

    # Appointment Errors
    APPOINTMENT_NOT_FOUND = "APP_1201"
    SLOT_NOT_AVAILABLE = "APP_1202"
    DOUBLE_BOOKING = "APP_1203"
    PAST_DATE_BOOKING = "APP_1204"
    CANCELLATION_NOT_ALLOWED = "APP_1205"
    APPOINTMENT_ALREADY_COMPLETED = "APP_1206"

    # Doctor Errors
    DOCTOR_NOT_FOUND = "DOC_1301"
    DOCTOR_UNAVAILABLE = "DOC_1302"

    # Payment Errors
    PAYMENT_FAILED = "PAY_1401"

    # Validation Errors
    VALIDATION_ERROR = "VAL_1501"
    INVALID_INPUT = "VAL_1502"

    # Database Errors
    DATABASE_ERROR = "DB_1601"
    DUPLICATE_ENTRY = "DB_1602"

    # General Errors
    INTERNAL_ERROR = "GEN_9001"
    SERVICE_UNAVAILABLE = "GEN_9002"

class ErrorMessages:
    AUTH_1001 = "Invalid email or password"
    AUTH_1002 = "Your session has expired. Please login again"
    AUTH_1003 = "Invalid authentication token"
    AUTH_1004 = "Your account has been deactivated"
    AUTH_1005 = "You don't have permission to perform this action"

    USER_1101 = "User not found"
    USER_1102 = "User with this email already exists"
    USER_1103 = "Invalid user data provided"

    APP_1201 = "Appointment not found"
    APP_1202 = "Selected slot is not available"
    APP_1203 = "This slot has already been booked"
    APP_1204 = "Cannot book appointment for past date"
    APP_1205 = "Cannot cancel appointment at this time"
    APP_1206 = "Appointment has already been completed"

    DOC_1301 = "Doctor not found"
    DOC_1302 = "Doctor is not available"

    PAY_1401 = "Payment processing failed"

    VAL_1501 = "Validation error"
    VAL_1502 = "Invalid input provided"

    DB_1601 = "Database error occurred"
    DB_1602 = "Duplicate entry found"

    GEN_9001 = "Internal server error"
    GEN_9002 = "Service temporarily unavailable"
