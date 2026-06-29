from pydantic import BaseModel, Field
from typing import Optional
from shared.enums.doctor_enums import Specialization
from shared.enums.user_enums import DoctorStatus

class DoctorApproveRequest(BaseModel):
    """Doctor approval request schema"""
    notes: Optional[str] = Field(None, max_length=500, description="Approval notes")

class DoctorRejectRequest(BaseModel):
    """Doctor rejection request schema"""
    reason: str = Field(..., min_length=5, max_length=500, description="Rejection reason")

class DoctorStatusUpdateRequest(BaseModel):
    """Doctor status update request schema"""
    status: DoctorStatus = Field(..., description="New doctor status")
    reason: Optional[str] = Field(None, max_length=500, description="Status change reason")

class DoctorResponse(BaseModel):
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
