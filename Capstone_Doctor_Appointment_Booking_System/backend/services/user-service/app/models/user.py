from typing import Optional
from datetime import datetime
from pydantic import EmailStr, Field, field_validator
from shared.models.base import BaseDBModel
from shared.constants.roles import UserRole
from shared.enums.user_enums import Gender, UserStatus

class User(BaseDBModel):
    """User model for authentication and user management"""
    email: EmailStr = Field(..., description="User email address")
    password_hash: str = Field(..., description="Hashed password")
    full_name: str = Field(..., min_length=2, description="Full name")
    phone: str = Field(..., min_length=10, max_length=15, description="Phone number")
    gender: Gender = Field(..., description="Gender")
    date_of_birth: str = Field(..., description="Date of birth (DD-MM-YYYY)")
    role: UserRole = Field(..., description="User role")
    status: UserStatus = Field(default=UserStatus.ACTIVE, description="User status")
    is_verified: bool = Field(default=False, description="Email verification status")
    last_login: Optional[datetime] = Field(default=None, description="Last login timestamp")

    @field_validator('id', mode='before')
    @classmethod
    def validate_id(cls, v):
        """Convert ObjectId to string if needed"""
        if v and not isinstance(v, str):
            return str(v)
        return v
