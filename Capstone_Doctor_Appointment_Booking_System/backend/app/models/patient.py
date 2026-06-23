from datetime import datetime, date
from typing import Optional
from beanie import Document, Link
from pydantic import Field
from app.models.user import User
from app.constants.gender import Gender

class Patient(Document):
    user: Link[User]
    gender: Gender = Field(...)
    date_of_birth: date = Field(...)
    age: Optional[int] = None
    preferred_language: Optional[str] = None
    medical_history: Optional[str] = None
    created_at: datetime = datetime.utcnow()
    updated_at: datetime = datetime.utcnow()

    class Settings:
        name = "patients"
        indexes = ["user", "gender"]
