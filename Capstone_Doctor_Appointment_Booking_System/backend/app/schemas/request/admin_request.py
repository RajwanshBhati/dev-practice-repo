from pydantic import Field, field_validator
from datetime import date
from .auth_request import RegisterMixin
from ..base.base_schemas import UserBase
from ..base.validators import DateValidator
from app.constants.gender import Gender


class AdminBase(UserBase):
    """Base admin schema"""
    gender: Gender
    date_of_birth: date


class AdminRegisterRequest(AdminBase, RegisterMixin):
    """Admin registration request"""
    password: str = Field(..., min_length=8, max_length=12)
    confirm_password: str

    @field_validator('date_of_birth')
    @classmethod
    def validate_age(cls, v: date) -> date:
        return DateValidator.validate_age(v, min_age=18, max_age=120)
