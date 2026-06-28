class ErrorMessages:
    # Authentication
    AUTH_1001 = "Invalid email or password"
    AUTH_1002 = "Your session has expired. Please login again"
    AUTH_1003 = "Invalid authentication token"
    AUTH_1004 = "Your account has been deactivated"
    AUTH_1005 = "You don't have permission to perform this action"

    # User
    USER_1101 = "User not found"
    USER_1102 = "User with this email already exists"
    USER_1103 = "Invalid user data provided"

    # Appointment
    APP_1201 = "Appointment not found"
    APP_1202 = "Selected slot is not available"
    APP_1203 = "This slot has already been booked"
    APP_1204 = "Cannot book appointment for past date"
    APP_1205 = "Cannot cancel appointment at this time"
    APP_1206 = "Appointment has already been completed"

    # Doctor
    DOC_1301 = "Doctor not found"
    DOC_1302 = "Doctor is not available"

    # Payment
    PAY_1401 = "Payment processing failed"

    # Validation
    VAL_1501 = "Validation error"
    VAL_1502 = "Invalid input provided"

    # Database
    DB_1601 = "Database error occurred"
    DB_1602 = "Duplicate entry found"

    # General
    GEN_9001 = "Internal server error"
    GEN_9002 = "Service temporarily unavailable"
