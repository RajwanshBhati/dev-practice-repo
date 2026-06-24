from datetime import datetime
from typing import Optional
from beanie import Document
from pydantic import EmailStr, Field
from app.constants.roles import UserRole


class User(Document):
    """User model - base model for all users"""
    full_name: str = Field(..., min_length=2)
    email: EmailStr = Field(..., unique=True)
    password: str
    phone_number: str = Field(..., pattern=r'^[0-9]{10}$')
    role: UserRole
    is_active: bool = True
    created_at: datetime = Field(default_factory=datetime.utcnow)
    updated_at: datetime = Field(default_factory=datetime.utcnow)

    class Settings:
        name = "users"
        indexes = [
            "email",
            "role",
            "is_active"
        ]
