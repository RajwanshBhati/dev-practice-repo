from enum import Enum

class UserRole(str, Enum):
    """Roles a user can have in the system: patient, doctor, or admin."""
    PATIENT = "PATIENT"
    DOCTOR = "DOCTOR"
    ADMIN = "ADMIN"

    @classmethod
    def choices(cls):
        """Return (value, name) pairs, handy for dropdowns or model fields."""
        return [(role.value, role.name) for role in cls]

    @classmethod
    def is_valid(cls, role: str) -> bool:
        """Check if a given string is a valid role."""
        return role in cls._value2member_map_


class Permission(str, Enum):
    """All individual permissions a user can be granted, grouped by role type."""
    VIEW_DOCTORS = "VIEW_DOCTORS"
    VIEW_DOCTOR_DETAILS = "VIEW_DOCTOR_DETAILS"
    BOOK_APPOINTMENT = "BOOK_APPOINTMENT"
    CANCEL_APPOINTMENT = "CANCEL_APPOINTMENT"
    VIEW_APPOINTMENTS = "VIEW_APPOINTMENTS"
    MAKE_PAYMENT = "MAKE_PAYMENT"
    VIEW_PAYMENT_HISTORY = "VIEW_PAYMENT_HISTORY"

    MANAGE_AVAILABILITY = "MANAGE_AVAILABILITY"
    VIEW_APPOINTMENTS_DOCTOR = "VIEW_APPOINTMENTS_DOCTOR"
    UPDATE_APPOINTMENT_STATUS = "UPDATE_APPOINTMENT_STATUS"
    VIEW_PATIENT_INFO = "VIEW_PATIENT_INFO"
    MANAGE_DOCTOR_PROFILE = "MANAGE_DOCTOR_PROFILE"
    VIEW_DOCTOR_STATS = "VIEW_DOCTOR_STATS"

    MANAGE_DOCTORS = "MANAGE_DOCTORS"
    APPROVE_DOCTORS = "APPROVE_DOCTORS"
    REJECT_DOCTORS = "REJECT_DOCTORS"
    VIEW_STATISTICS = "VIEW_STATISTICS"
    MANAGE_USERS = "MANAGE_USERS"
    VIEW_ALL_APPOINTMENTS = "VIEW_ALL_APPOINTMENTS"
    MANAGE_SYSTEM = "MANAGE_SYSTEM"
    VIEW_AUDIT_LOGS = "VIEW_AUDIT_LOGS"
    MANAGE_ADMINS = "MANAGE_ADMINS"
    SEND_NOTIFICATIONS = "SEND_NOTIFICATIONS"

    VIEW_PROFILE = "VIEW_PROFILE"
    UPDATE_PROFILE = "UPDATE_PROFILE"
    CHANGE_PASSWORD = "CHANGE_PASSWORD"


"""Maps each role to the list of permissions it's allowed to use."""
ROLE_PERMISSIONS = {
    UserRole.PATIENT: [
        Permission.VIEW_PROFILE,
        Permission.UPDATE_PROFILE,
        Permission.CHANGE_PASSWORD,
        Permission.VIEW_DOCTORS,
        Permission.VIEW_DOCTOR_DETAILS,
        Permission.BOOK_APPOINTMENT,
        Permission.CANCEL_APPOINTMENT,
        Permission.VIEW_APPOINTMENTS,
        Permission.MAKE_PAYMENT,
        Permission.VIEW_PAYMENT_HISTORY,
    ],
    UserRole.DOCTOR: [
        Permission.VIEW_PROFILE,
        Permission.UPDATE_PROFILE,
        Permission.CHANGE_PASSWORD,
        Permission.VIEW_DOCTOR_DETAILS,
        Permission.MANAGE_AVAILABILITY,
        Permission.VIEW_APPOINTMENTS_DOCTOR,
        Permission.UPDATE_APPOINTMENT_STATUS,
        Permission.VIEW_PATIENT_INFO,
        Permission.MANAGE_DOCTOR_PROFILE,
        Permission.VIEW_DOCTOR_STATS,
        Permission.VIEW_APPOINTMENTS,
    ],
    UserRole.ADMIN: [
        Permission.VIEW_PROFILE,
        Permission.UPDATE_PROFILE,
        Permission.CHANGE_PASSWORD,
        Permission.VIEW_DOCTORS,
        Permission.VIEW_DOCTOR_DETAILS,
        Permission.VIEW_APPOINTMENTS,
        Permission.MANAGE_DOCTORS,
        Permission.APPROVE_DOCTORS,
        Permission.REJECT_DOCTORS,
        Permission.VIEW_STATISTICS,
        Permission.MANAGE_USERS,
        Permission.VIEW_ALL_APPOINTMENTS,
        Permission.MANAGE_SYSTEM,
        Permission.VIEW_AUDIT_LOGS,
        Permission.MANAGE_ADMINS,
        Permission.SEND_NOTIFICATIONS,
    ]
}
