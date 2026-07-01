from enum import Enum

class AppointmentType(str, Enum):
    """How an appointment will happen: in person, video call, or phone call."""
    IN_PERSON = "IN_PERSON"
    VIDEO_CALL = "VIDEO_CALL"
    PHONE_CALL = "PHONE_CALL"

    @classmethod
    def choices(cls):
        """Return (value, name) pairs, handy for dropdowns or model fields."""
        return [(type.value, type.name) for type in cls]


class AppointmentPriority(str, Enum):
    """Urgency level of an appointment."""
    LOW = "LOW"
    MEDIUM = "MEDIUM"
    HIGH = "HIGH"
    URGENT = "URGENT"

    @classmethod
    def choices(cls):
        """Return (value, name) pairs, handy for dropdowns or model fields."""
        return [(priority.value, priority.name) for priority in cls]
