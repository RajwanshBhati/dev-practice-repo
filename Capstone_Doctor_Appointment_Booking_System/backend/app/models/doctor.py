from datetime import datetime
from typing import Optional
from beanie import Document, Link
from pydantic import Field
from app.models.user import User
from app.constants.specialization import Specialization


class Doctor(Document):
    """Doctor model - extends User with doctor-specific fields"""
    user: Link[User]
    qualification: str = Field(..., min_length=2)
    specialization: Specialization = Field(...)
    experience: int = Field(..., ge=0, le=60)
    license_number: str = Field(..., unique=True)
    consultation_fee: float = Field(..., ge=0)
    clinic_address: str = Field(..., min_length=5)
    profile_photo: Optional[str] = None
    bio: Optional[str] = None
    rating: Optional[float] = Field(None, ge=0, le=5)
    total_reviews: int = Field(default=0)
    is_verified: bool = Field(default=False)
    created_at: datetime = Field(default_factory=datetime.utcnow)
    updated_at: datetime = Field(default_factory=datetime.utcnow)

    class Settings:
        name = "doctors"
        indexes = [
            "user",
            "specialization",
            "license_number",
            "is_verified"
        ]
