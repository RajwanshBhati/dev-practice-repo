from pydantic import BaseModel
from typing import Optional
from backend.constants.status import DoctorStatus
from backend.constants.enums import Specialization


class DoctorProfileResponse(BaseModel):
    """
    Complete doctor profile response.
    """

    id: str
    user_id: str
    full_name: str
    email: str
    phone: str
    qualification: str
    specialization: Specialization
    experience_years: int
    license_number: str
    consultation_fee: float
    clinic_address: str
    clinic_phone: Optional[str] = None
    bio: Optional[str] = None
    profile_picture: Optional[str] = None
    status: DoctorStatus
    rating: float
    total_reviews: int
    created_at: str
    updated_at: str


class DoctorStatsResponse(BaseModel):
    """
    Doctor statistics response.
    """

    total_patients: int
    total_appointments: int
    today_appointments: int
    upcoming_appointments: int
    completed_appointments: int
    cancelled_appointments: int
    rating: float
    total_reviews: int

class ProfilePictureResponse(BaseModel):
    """Response schema for profile picture update."""

    message: str
    profile_picture: str


class DoctorProfileUpdateResponse(BaseModel):
    """Response schema for doctor profile update."""

    message: str
    doctor_id: str
