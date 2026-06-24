from pydantic import BaseModel, Field, field_validator
from ..base.validators import PasswordValidator


class LoginRequest(BaseModel):
    """Login request schema"""
    email: str = Field(..., description="User email")
    password: str = Field(..., min_length=8, description="User password")


class RegisterMixin:
    """Mixin for registration schemas with common validations"""

    @field_validator('password')
    @classmethod
    def validate_password(cls, v: str) -> str:
        return PasswordValidator.validate_password(v)

    @field_validator('confirm_password')
    @classmethod
    def validate_confirm_password(cls, v: str, info) -> str:
        return PasswordValidator.validate_confirm_password(v, info)
