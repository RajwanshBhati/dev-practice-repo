from pydantic import BaseModel
from datetime import datetime, time
from typing import Optional, List

class DoctorResponse(BaseModel):
    """Doctor profile response"""
    id: str
    user_id: str
    full_name: str
    email: str
    phone_number: str
    qualification: str
    specialization: str
    experience: int
    license_number: str
    consultation_fee: float
    clinic_address: str
    profile_photo: Optional[str] = None
    bio: Optional[str] = None
    rating: Optional[float] = None
    total_reviews: int
    is_verified: bool
    is_active: bool
    created_at: datetime
    updated_at: datetime
