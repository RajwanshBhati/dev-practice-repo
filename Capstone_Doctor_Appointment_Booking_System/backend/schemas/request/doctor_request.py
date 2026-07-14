from pydantic import BaseModel, Field, validator
from typing import Optional

from backend.enums.doctor_enums import Specialization
from backend.enums.user_enums import DoctorStatus
from backend.utils.validators import Validators


class DoctorApproveRequest(BaseModel):
    """Doctor approval request schema."""

    notes: Optional[str] = Field(
        None,
        max_length=500,
        description="Approval notes"
    )


class DoctorRejectRequest(BaseModel):
    """Doctor rejection request schema."""

    reason: str = Field(
        ...,
        min_length=5,
        max_length=500,
        description="Rejection reason"
    )


class DoctorStatusUpdateRequest(BaseModel):
    """Doctor status update request schema."""

    status: DoctorStatus = Field(
        ...,
        description="New doctor status"
    )

    reason: Optional[str] = Field(
        None,
        max_length=500,
        description="Status change reason"
    )


class DoctorProfileUpdate(BaseModel):
    """Doctor profile update request schema."""

    qualification: Optional[str] = Field(
        None,
        min_length=2,
        max_length=100,
        description="Medical qualification"
    )

    specialization: Optional[Specialization] = Field(
        None,
        description="Medical specialization"
    )

    experience_years: Optional[int] = Field(
        None,
        ge=0,
        le=50,
        description="Years of experience"
    )

    license_number: Optional[str] = Field(
        None,
        min_length=3,
        max_length=50,
        description="Medical license number"
    )

    consultation_fee: Optional[float] = Field(
        None,
        gt=0,
        description="Consultation fee"
    )

    clinic_address: Optional[str] = Field(
        None,
        min_length=5,
        max_length=500,
        description="Clinic address"
    )

    clinic_phone: Optional[str] = Field(
        None,
        min_length=10,
        max_length=15,
        description="Clinic phone number"
    )

    bio: Optional[str] = Field(
        None,
        max_length=1000,
        description="Doctor biography"
    )

    profile_picture: Optional[str] = Field(
        None,
        description="Profile picture URL"
    )

    @validator('clinic_phone')
    def validate_clinic_phone(cls, v):
        if v:
            if not Validators.validate_phone(v):
                raise ValueError(
                    'Clinic phone number must be 10-15 digits'
                )
        return v


class ProfilePictureUpdate(BaseModel):
    """Profile picture update request schema."""

    profile_picture: str = Field(
        ...,
        description="Profile picture URL"
    )
