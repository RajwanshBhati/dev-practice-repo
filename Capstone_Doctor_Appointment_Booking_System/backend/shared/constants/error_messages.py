class ErrorMessages:
    # Authentication
    AUTH_1001 = "Invalid email or password"
    AUTH_1002 = "Your session has expired. Please login again"
    AUTH_1003 = "Invalid authentication token"
    AUTH_1004 = "Your account has been deactivated"
    AUTH_1005 = "You don't have permission to perform this action"
    AUTH_1006 = "Account pending approval. Please wait for admin approval"
    AUTH_1007 = "Account has been rejected. Contact support for more information"

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
    DOC_1303 = "Doctor profile not found"
    DOC_1304 = "Doctor already exists"
    DOC_1305 = "Doctor registration pending approval"
    DOC_1306 = "Doctor account is not approved"
    DOC_1307 = "Doctor account has been rejected"
    DOC_1308 = "Doctor is not verified"

    # Admin
    ADM_1401 = "Admin not found"
    ADM_1402 = "Only super admin can create new admins"
    ADM_1403 = "Admin already exists"
    ADM_1404 = "Cannot delete the last super admin"
    ADM_1405 = "First admin already exists"
    ADM_1406 = "No admin exists. Please create the first admin"

    # Payment
    PAY_1501 = "Payment processing failed"

    # Validation
    VAL_1601 = "Validation error"
    VAL_1602 = "Invalid input provided"

    # Email
    EMAIL_1701 = "Failed to send email"
    EMAIL_1702 = "Email service is unavailable"

    # Database
    DB_1801 = "Database error occurred"
    DB_1802 = "Duplicate entry found"

    # General
    GEN_9001 = "Internal server error"
    GEN_9002 = "Service temporarily unavailable"
