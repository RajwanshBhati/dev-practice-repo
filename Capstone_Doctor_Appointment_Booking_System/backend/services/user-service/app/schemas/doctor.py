from pydantic import BaseModel, Field, validator
from typing import Optional
from shared.enums.doctor_enums import Specialization
from shared.enums.user_enums import DoctorStatus
from shared.utils.validators import Validators

class DoctorProfileUpdate(BaseModel):
    """Doctor profile update schema"""
    qualification: Optional[str] = Field(None, min_length=2, max_length=100, description="Medical qualification")
    specialization: Optional[Specialization] = Field(None, description="Medical specialization")
    experience_years: Optional[int] = Field(None, ge=0, le=50, description="Years of experience")
    license_number: Optional[str] = Field(None, min_length=3, max_length=50, description="Medical license number")
    consultation_fee: Optional[float] = Field(None, gt=0, description="Consultation fee")
    clinic_address: Optional[str] = Field(None, min_length=5, max_length=500, description="Clinic address")
    clinic_phone: Optional[str] = Field(None, min_length=10, max_length=15, description="Clinic phone number")
    bio: Optional[str] = Field(None, max_length=1000, description="Doctor biography")
    profile_picture: Optional[str] = Field(None, description="Profile picture URL")

    @validator('clinic_phone')
    def validate_clinic_phone(cls, v):
        if v:
            if not Validators.validate_phone(v):
                raise ValueError('Clinic phone number must be 10-15 digits')
        return v

class DoctorProfileResponse(BaseModel):
    """Doctor profile response schema"""
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
    """Public doctor profile for patients"""
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

class ProfilePictureUpdate(BaseModel):
    """Profile picture update schema"""
    profile_picture: str = Field(..., description="Profile picture URL")
