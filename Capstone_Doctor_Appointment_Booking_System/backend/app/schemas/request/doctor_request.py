from pydantic import BaseModel, Field, field_validator
from datetime import date, datetime
from typing import Optional, List

from .auth_request import RegisterMixin
from ..base.base_schemas import UserBase, PaginationParams
from ..base.validators import LicenseValidator, NameValidator, MoneyValidator
from app.constants.specialization import Specialization

class DoctorBase(UserBase):
    """Base doctor schema"""
    qualification: str = Field(..., min_length=2)
    specialization: Specialization
    experience: int = Field(..., ge=0, le=60)
    license_number: str = Field(..., min_length=3)
    consultation_fee: float = Field(..., gt=0)
    clinic_address: str = Field(..., min_length=5)

class DoctorRegisterRequest(DoctorBase, RegisterMixin):
    """Doctor registration request"""
    password: str = Field(..., min_length=8, max_length=12)
    confirm_password: str

    @field_validator('license_number')
    @classmethod
    def validate_license(cls, v: str) -> str:
        return LicenseValidator.validate_license_number(v)

    @field_validator('consultation_fee')
    @classmethod
    def validate_fee(cls, v: float) -> float:
        return MoneyValidator.validate_positive_amount(v)

