from typing import Optional
from datetime import datetime
from pydantic import EmailStr, Field
from backend.models.base import BaseDBModel
from backend.constants.roles import UserRole
from backend.enums.user_enums import Gender, UserStatus

class User(BaseDBModel):
    """User model for authentication and user management"""
    email: EmailStr = Field(..., description="User email address")
    password_hash: str = Field(..., description="Hashed password")
    full_name: str = Field(..., min_length=2, description="Full name")
    phone: str = Field(..., min_length=10, max_length=15, description="Phone number")
    gender: Gender = Field(..., description="Gender")
    date_of_birth: str = Field(..., description="Date of birth (DD-MM-YYYY)")
    role: UserRole = Field(..., description="User role")
    status: UserStatus = Field(default=UserStatus.PENDING, description="User status")
    is_verified: bool = Field(default=False, description="Email verification status")
    last_login: Optional[datetime] = Field(default=None, description="Last login timestamp")
    is_first_admin: bool = Field(default=False, description="Is this the first admin")

    class Config:
        collection = "users"
