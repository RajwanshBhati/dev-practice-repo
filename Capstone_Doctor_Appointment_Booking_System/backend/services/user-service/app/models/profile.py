from typing import Optional
from pydantic import Field
from shared.models.base import BaseDBModel
from shared.enums.doctor_enums import Specialization

class PatientProfile(BaseDBModel):
    """Patient profile model"""
    user_id: str = Field(..., description="Reference to User ID")
    emergency_contact: Optional[str] = Field(None, description="Emergency contact number")
    address: Optional[str] = Field(None, description="Home address")
    blood_group: Optional[str] = Field(None, description="Blood group")
    allergies: Optional[str] = Field(None, description="Known allergies")
    medical_history: Optional[str] = Field(None, description="Medical history")

    class Config:
        collection = "patient_profiles"

class DoctorProfile(BaseDBModel):
    """Doctor profile model"""
    user_id: str = Field(..., description="Reference to User ID")
    qualification: str = Field(..., min_length=2, description="Medical qualification")
    specialization: Specialization = Field(..., description="Medical specialization")
    experience_years: int = Field(..., ge=0, le=50, description="Years of experience")
    license_number: str = Field(..., min_length=3, description="Medical license number")
    consultation_fee: float = Field(..., gt=0, description="Consultation fee")
    clinic_address: str = Field(..., min_length=5, description="Clinic address")
    clinic_phone: Optional[str] = Field(None, description="Clinic phone number")
    bio: Optional[str] = Field(None, description="Doctor biography")
    is_verified: bool = Field(default=False, description="Verification status")
    rating: float = Field(default=0.0, ge=0, le=5, description="Average rating")
    total_reviews: int = Field(default=0, description="Total number of reviews")

    class Config:
        collection = "doctor_profiles"
