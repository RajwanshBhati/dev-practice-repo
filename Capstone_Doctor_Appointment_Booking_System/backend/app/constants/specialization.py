from enum import Enum


class Specialization(str, Enum):
    CARDIOLOGIST = "Cardiologist"
    DERMATOLOGIST = "Dermatologist"
    DENTIST = "Dentist"
    NEUROLOGIST = "Neurologist"
    ORTHOPEDIC = "Orthopedic"
    PEDIATRICIAN = "Pediatrician"
    PSYCHIATRIST = "Psychiatrist"
    OPHTHALMOLOGIST = "Ophthalmologist"
    GYNECOLOGIST = "Gynecologist"
    GENERAL_PHYSICIAN = "General Physician"

    @classmethod
    def get_values(cls):
        """Get all enum values as list of strings"""
        return [e.value for e in cls]

    @classmethod
    def get_keys(cls):
        """Get all enum keys as list of strings"""
        return [e.name for e in cls]
