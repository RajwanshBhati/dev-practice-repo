from enum import Enum

class Specialization(str, Enum):
    """Medical specializations a doctor can be registered under."""
    CARDIOLOGIST = "Cardiologist"
    DERMATOLOGIST = "Dermatologist"
    DENTIST = "Dentist"
    NEUROLOGIST = "Neurologist"
    ORTHOPEDIC = "Orthopedic"
    PEDIATRICIAN = "Pediatrician"
    PSYCHIATRIST = "Psychiatrist"
    RADIOLOGIST = "Radiologist"
    SURGEON = "Surgeon"
    UROLOGIST = "Urologist"
    GYNECOLOGIST = "Gynecologist"
    OPHTHALMOLOGIST = "Ophthalmologist"
    ENT = "ENT Specialist"
    GENERAL_PHYSICIAN = "General Physician"
    OTHER = "Other"

    @classmethod
    def choices(cls):
        """Return (value, name) pairs, handy for dropdowns or model fields."""
        return [(spec.value, spec.name) for spec in cls]

    @classmethod
    def is_valid(cls, specialization: str) -> bool:
        """Check if a given string is a valid specialization."""
        return specialization in cls._value2member_map_


class ConsultationType(str, Enum):
    """Nature of the consultation being booked."""
    FIRST = "FIRST"
    FOLLOW_UP = "FOLLOW_UP"
    EMERGENCY = "EMERGENCY"
    ROUTINE = "ROUTINE"

    @classmethod
    def choices(cls):
        """Return (value, name) pairs, handy for dropdowns or model fields."""
        return [(type.value, type.name) for type in cls]
