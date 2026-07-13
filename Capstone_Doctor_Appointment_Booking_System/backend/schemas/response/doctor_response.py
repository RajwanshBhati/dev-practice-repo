from pydantic import BaseModel
from typing import Optional

from backend.enums.doctor_enums import Specialization
from backend.enums.user_enums import DoctorStatus


class DoctorProfileResponse(BaseModel):
    """Doctor profile response schema."""

    id: str
    user_id: str
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
    verification_status: str
    approved_by: Optional[str] = None
    approved_at: Optional[str] = None
    rejected_by: Optional[str] = None
    rejected_at: Optional[str] = None
    rejection_reason: Optional[str] = None
    rating: float
    total_reviews: int
    created_at: str
    updated_at: str


class DoctorProfilePublic(BaseModel):
    """Public doctor profile schema for patients."""

    id: str
    full_name: str
    qualification: str
    specialization: Specialization
    experience_years: int
    consultation_fee: float
    clinic_address: str
    clinic_phone: Optional[str] = None
    bio: Optional[str] = None
    profile_picture: Optional[str] = None
    rating: float
    total_reviews: int
    is_available: bool = False

class DoctorDashboardStatsResponse(BaseModel):
    total_patients: int
    total_appointments: int
    today_appointments: int
    upcoming_appointments: int
    completed_appointments: int
    cancelled_appointments: int
    rating: float
    total_reviews: int

class DoctorUserResponse(BaseModel):
    id: str
    email: str
    full_name: str
    role: str
    status: str
    doctor_status: str


class DoctorRegistrationResponse(BaseModel):
    message: str
    user: DoctorUserResponse

class SpecializationsResponse(BaseModel):
    """Response schema for doctor specializations."""

    specializations: List[str]
