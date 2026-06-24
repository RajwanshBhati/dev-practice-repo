from pydantic import BaseModel, EmailStr, Field
from datetime import datetime
from typing import Optional


class UserBase(BaseModel):
    """Base user schema with common fields"""
    full_name: str = Field(
        ...,
        min_length=2,
        pattern=r'^[A-Za-z\s\.\-\']+$'
    )
    email: EmailStr
    phone_number: str = Field(..., pattern=r'^[0-9]{10}$')


class UserResponseBase(BaseModel):
    """Base response schema for user data"""
    id: str
    full_name: str
    email: EmailStr
    role: str
    created_at: datetime


class AuthResponseBase(BaseModel):
    """Base authentication response"""
    message: str
    token_type: str = "bearer"


class PaginationParams(BaseModel):
    """Pagination parameters"""
    page: int = Field(1, ge=1)
    limit: int = Field(10, ge=1, le=100)
