from enum import Enum

class Gender(str, Enum):
    """Gender options available for a user profile."""
    MALE = "Male"
    FEMALE = "Female"
    OTHER = "Other"

    @classmethod
    def choices(cls):
        """Return (value, name) pairs, handy for dropdowns or model fields."""
        return [(gender.value, gender.name) for gender in cls]


class UserStatus(str, Enum):
    """Lifecycle state of a user account, from signup to deletion."""
    PENDING = "PENDING"
    ACTIVE = "ACTIVE"
    INACTIVE = "INACTIVE"
    SUSPENDED = "SUSPENDED"
    DELETED = "DELETED"

    @classmethod
    def choices(cls):
        """Return (value, name) pairs, handy for dropdowns or model fields."""
        return [(status.value, status.name) for status in cls]


class DoctorStatus(str, Enum):
    """Approval state of a doctor's account, controlled by admin."""
    PENDING = "PENDING"
    APPROVED = "APPROVED"
    REJECTED = "REJECTED"
    SUSPENDED = "SUSPENDED"

    @classmethod
    def choices(cls):
        """Return (value, name) pairs, handy for dropdowns or model fields."""
        return [(status.value, status.name) for status in cls]


class VerificationStatus(str, Enum):
    """Whether a doctor's credentials/documents have been verified."""
    PENDING = "PENDING"
    VERIFIED = "VERIFIED"
    REJECTED = "REJECTED"

    @classmethod
    def choices(cls):
        """Return (value, name) pairs, handy for dropdowns or model fields."""
        return [(status.value, status.name) for status in cls]


class AdminType(str, Enum):
    """Distinguishes the first super admin from admins created afterward."""
    SUPER_ADMIN = "SUPER_ADMIN"
    SUB_ADMIN = "SUB_ADMIN"

    @classmethod
    def choices(cls):
        """Return (value, name) pairs, handy for dropdowns or model fields."""
        return [(admin_type.value, admin_type.name) for admin_type in cls]
