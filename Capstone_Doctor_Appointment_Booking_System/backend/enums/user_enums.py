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
