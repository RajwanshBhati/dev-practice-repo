from pydantic import BaseModel, EmailStr
from typing import Optional
from datetime import datetime
from backend.constants.roles import UserRole
from backend.enums.user_enums import Gender, UserStatus


class UserResponse(BaseModel):
    """User response schema"""

    id: str
    email: EmailStr
    full_name: str
    phone: str
    gender: Gender
    role: UserRole
    status: UserStatus
    is_verified: bool
    created_at: datetime


class UserUpdate(BaseModel):
    """User update schema"""

    full_name: Optional[str] = None
    phone: Optional[str] = None
    gender: Optional[Gender] = None
