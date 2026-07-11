from enum import Enum

class UserRole(str, Enum):
    """Roles a user can have in the system: patient, doctor, or admin."""
    PATIENT = "PATIENT"
    DOCTOR = "DOCTOR"
    ADMIN = "ADMIN"


