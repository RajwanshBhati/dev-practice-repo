from enum import Enum

class Gender(str, Enum):
    MALE = "Male"
    FEMALE = "Female"
    OTHER = "Other"

    @classmethod
    def choices(cls):
        return [(gender.value, gender.name) for gender in cls]

class UserStatus(str, Enum):
    PENDING = "PENDING"          # Waiting for approval
    ACTIVE = "ACTIVE"            # Fully active
    INACTIVE = "INACTIVE"        # Deactivated
    SUSPENDED = "SUSPENDED"      # Temporarily suspended
    DELETED = "DELETED"          # Permanently deleted

    @classmethod
    def choices(cls):
        return [(status.value, status.name) for status in cls]

class DoctorStatus(str, Enum):
    PENDING = "PENDING"          # Waiting for admin approval
    APPROVED = "APPROVED"        # Approved by admin
    REJECTED = "REJECTED"        # Rejected by admin
    SUSPENDED = "SUSPENDED"      # Suspended by admin

    @classmethod
    def choices(cls):
        return [(status.value, status.name) for status in cls]

class VerificationStatus(str, Enum):
    PENDING = "PENDING"
    VERIFIED = "VERIFIED"
    REJECTED = "REJECTED"

    @classmethod
    def choices(cls):
        return [(status.value, status.name) for status in cls]

class AdminType(str, Enum):
    SUPER_ADMIN = "SUPER_ADMIN"  # First admin with full permissions
    SUB_ADMIN = "SUB_ADMIN"      # Admin created by super admin

    @classmethod
    def choices(cls):
        return [(admin_type.value, admin_type.name) for admin_type in cls]
