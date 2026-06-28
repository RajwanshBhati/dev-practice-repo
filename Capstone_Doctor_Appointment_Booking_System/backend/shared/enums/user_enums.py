from enum import Enum

class Gender(str, Enum):
    MALE = "Male"
    FEMALE = "Female"
    OTHER = "Other"

    @classmethod
    def choices(cls):
        return [(gender.value, gender.name) for gender in cls]

    @classmethod
    def is_valid(cls, gender: str) -> bool:
        return gender in cls._value2member_map_

class UserStatus(str, Enum):
    ACTIVE = "ACTIVE"
    INACTIVE = "INACTIVE"
    SUSPENDED = "SUSPENDED"
    DELETED = "DELETED"

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
