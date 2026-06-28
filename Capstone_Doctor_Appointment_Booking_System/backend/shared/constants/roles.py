from enum import Enum

class UserRole(str, Enum):
    PATIENT = "PATIENT"
    DOCTOR = "DOCTOR"
    ADMIN = "ADMIN"

    @classmethod
    def choices(cls):
        return [(role.value, role.name) for role in cls]

    @classmethod
    def is_valid(cls, role: str) -> bool:
        return role in cls._value2member_map_

class Permission(str, Enum):
    # Patient permissions
    VIEW_DOCTORS = "VIEW_DOCTORS"
    BOOK_APPOINTMENT = "BOOK_APPOINTMENT"
    CANCEL_APPOINTMENT = "CANCEL_APPOINTMENT"
    VIEW_APPOINTMENTS = "VIEW_APPOINTMENTS"
    MAKE_PAYMENT = "MAKE_PAYMENT"

    # Doctor permissions
    MANAGE_AVAILABILITY = "MANAGE_AVAILABILITY"
    VIEW_APPOINTMENTS_DOCTOR = "VIEW_APPOINTMENTS_DOCTOR"
    UPDATE_APPOINTMENT_STATUS = "UPDATE_APPOINTMENT_STATUS"
    VIEW_PATIENT_INFO = "VIEW_PATIENT_INFO"
    MANAGE_PROFILE = "MANAGE_PROFILE"

    # Admin permissions
    MANAGE_DOCTORS = "MANAGE_DOCTORS"
    VIEW_STATISTICS = "VIEW_STATISTICS"
    MANAGE_USERS = "MANAGE_USERS"
    VIEW_ALL_APPOINTMENTS = "VIEW_ALL_APPOINTMENTS"

ROLE_PERMISSIONS = {
    UserRole.PATIENT: [
        Permission.VIEW_DOCTORS,
        Permission.BOOK_APPOINTMENT,
        Permission.CANCEL_APPOINTMENT,
        Permission.VIEW_APPOINTMENTS,
        Permission.MAKE_PAYMENT,
    ],
    UserRole.DOCTOR: [
        Permission.MANAGE_AVAILABILITY,
        Permission.VIEW_APPOINTMENTS_DOCTOR,
        Permission.UPDATE_APPOINTMENT_STATUS,
        Permission.VIEW_PATIENT_INFO,
        Permission.MANAGE_PROFILE,
    ],
    UserRole.ADMIN: [
        Permission.MANAGE_DOCTORS,
        Permission.VIEW_STATISTICS,
        Permission.MANAGE_USERS,
        Permission.VIEW_ALL_APPOINTMENTS,
        Permission.VIEW_DOCTORS,
        Permission.VIEW_APPOINTMENTS,
    ]
}
