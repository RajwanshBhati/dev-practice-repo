from datetime import datetime, date
from typing import Optional
from beanie import Document, Link
from pydantic import Field
from app.models.user import User
from app.constants.gender import Gender


class Patient(Document):
    """Patient model - extends User with patient-specific fields"""
    user: Link[User]
    gender: Gender = Field(...)
    date_of_birth: date = Field(...)
    age: Optional[int] = None
    preferred_language: Optional[str] = None
    medical_history: Optional[str] = None
    created_at: datetime = Field(default_factory=datetime.utcnow)
    updated_at: datetime = Field(default_factory=datetime.utcnow)

    class Settings:
        name = "patients"
        indexes = [
            "user",
            "gender"
        ]

    def calculate_age(self) -> int:
        """Calculate age from date of birth"""
        today = date.today()
        return today.year - self.date_of_birth.year - (
            (today.month, today.day) < (self.date_of_birth.month, self.date_of_birth.day)
        )
