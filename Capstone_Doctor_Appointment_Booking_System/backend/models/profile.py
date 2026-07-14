from typing import Optional
from datetime import datetime
from pydantic import Field
from backend.enums.user_enums import DoctorStatus, VerificationStatus

class PatientProfile(BaseDBModel):
    """Extra patient-specific details linked to a User account, like medical history and emergency contact."""
    user_id: str = Field(..., description="Reference to User ID")
    emergency_contact: Optional[str] = Field(None, description="Emergency contact number")
    address: Optional[str] = Field(None, description="Home address")
    blood_group: Optional[str] = Field(None, description="Blood group")
    allergies: Optional[str] = Field(None, description="Known allergies")
    medical_history: Optional[str] = Field(None, description="Medical history")

    class Config:
        collection = "patient_profiles"


class DoctorProfile(BaseDBModel):
    """
    Extra doctor-specific details linked to a User account, including
    qualifications and clinic info, plus everything needed to track the
    admin approval and verification workflow before a doctor goes live.
    """
    user_id: str = Field(..., description="Reference to User ID")
    qualification: str = Field(..., min_length=2, description="Medical qualification")
    specialization: Specialization = Field(..., description="Medical specialization")
    experience_years: int = Field(..., ge=0, le=50, description="Years of experience")
    license_number: str = Field(..., min_length=3, description="Medical license number")
    consultation_fee: float = Field(..., gt=0, description="Consultation fee")
    clinic_address: str = Field(..., min_length=5, description="Clinic address")
    clinic_phone: Optional[str] = Field(None, description="Clinic phone number")
    bio: Optional[str] = Field(None, max_length=1000, description="Doctor biography")
    profile_picture: Optional[str] = Field(None, description="Profile picture URL")

    status: DoctorStatus = Field(default=DoctorStatus.PENDING, description="Doctor approval status")
    verification_status: VerificationStatus = Field(default=VerificationStatus.PENDING, description="Verification status")
    approved_by: Optional[str] = Field(None, description="Admin ID who approved")
    approved_at: Optional[datetime] = Field(None, description="Approval timestamp")
    rejected_by: Optional[str] = Field(None, description="Admin ID who rejected")
    rejected_at: Optional[datetime] = Field(None, description="Rejection timestamp")
    rejection_reason: Optional[str] = Field(None, max_length=500, description="Reason for rejection")

    rating: float = Field(default=0.0, ge=0, le=5, description="Average rating")
    total_reviews: int = Field(default=0, description="Total number of reviews")

    class Config:
        collection = "doctor_profiles"
