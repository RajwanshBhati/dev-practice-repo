from pydantic import BaseModel, Field, field_validator
from datetime import date
from typing import Optional

from .auth_request import RegisterMixin
from ..base.base_schemas import UserBase, PaginationParams
from ..base.validators import DateValidator, NameValidator, PhoneValidator
from app.constants.gender import Gender

class PatientBase(UserBase):
    """Base patient schema"""
    gender: Gender
    date_of_birth: date

class PatientRegisterRequest(PatientBase, RegisterMixin):
    """Patient registration request"""
    password: str = Field(..., min_length=8, max_length=12)
    confirm_password: str

    @field_validator('date_of_birth')
    @classmethod
    def validate_age(cls, v: date) -> date:
        return DateValidator.validate_age(v, min_age=18, max_age=120)


