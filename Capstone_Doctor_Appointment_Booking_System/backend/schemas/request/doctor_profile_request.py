from pydantic import BaseModel, Field, validator
from typing import Optional
from backend.constants.enums import Specialization
from backend.utils.validators import Validators


class DoctorProfileUpdateRequest(BaseModel):
    """
    Request schema for updating doctor profile.
    """

    qualification: Optional[str] = Field(None, min_length=2, max_length=100)
    specialization: Optional[Specialization] = None
    experience_years: Optional[int] = Field(None, ge=0, le=50)
    license_number: Optional[str] = Field(None, min_length=3, max_length=50)
    consultation_fee: Optional[float] = Field(None, gt=0)
    clinic_address: Optional[str] = Field(None, min_length=5, max_length=500)
    clinic_phone: Optional[str] = Field(None, min_length=10, max_length=15)
    bio: Optional[str] = Field(None, max_length=1000)
    profile_picture: Optional[str] = None

    @validator('clinic_phone')
    def validate_clinic_phone(cls, v):
        if v and not Validators.validate_phone(v):
            raise ValueError('Clinic phone number must be 10-15 digits')
        return v


class ProfilePictureUpdateRequest(BaseModel):
    """
    Request schema for updating profile picture.
    """

    profile_picture: str = Field(..., description="Profile picture URL")
