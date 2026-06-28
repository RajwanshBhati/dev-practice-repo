from pydantic import BaseModel, EmailStr
from typing import Optional
from shared.constants.roles import UserRole
from shared.enums.user_enums import Gender, UserStatus
from datetime import datetime

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
