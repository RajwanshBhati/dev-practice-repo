from enum import Enum

class AppointmentType(str, Enum):
    IN_PERSON = "IN_PERSON"
    VIDEO_CALL = "VIDEO_CALL"
    PHONE_CALL = "PHONE_CALL"

    @classmethod
    def choices(cls):
        return [(type.value, type.name) for type in cls]

class AppointmentPriority(str, Enum):
    LOW = "LOW"
    MEDIUM = "MEDIUM"
    HIGH = "HIGH"
    URGENT = "URGENT"

    @classmethod
    def choices(cls):
        return [(priority.value, priority.name) for priority in cls]
