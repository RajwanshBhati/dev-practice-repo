from enum import Enum

class AppointmentStatus(str, Enum):
    """Possible states an appointment can be in, from booking to completion."""
    SCHEDULED = "SCHEDULED"
    CONFIRMED = "CONFIRMED"
    COMPLETED = "COMPLETED"
    CANCELLED = "CANCELLED"
    NO_SHOW = "NO_SHOW"
    RESCHEDULED = "RESCHEDULED"

    @classmethod
    def choices(cls):
        """Return (value, name) pairs, handy for dropdowns or model fields."""
        return [(status.value, status.name) for status in cls]

    @classmethod
    def is_valid(cls, status: str) -> bool:
        """Check if a given string is a valid appointment status."""
        return status in cls._value2member_map_


class PaymentStatus(str, Enum):
    """Possible states a payment can be in."""
    PENDING = "PENDING"
    COMPLETED = "COMPLETED"
    FAILED = "FAILED"
    REFUNDED = "REFUNDED"
