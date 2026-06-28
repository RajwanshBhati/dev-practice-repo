from typing import Optional
from datetime import datetime
from pydantic import EmailStr, Field
from shared.models.base import BaseDBModel
from shared.constants.roles import UserRole

class User(BaseDBModel):
    email: EmailStr
    password_hash: str
    full_name: str = Field(..., min_length=2)
    phone_number: str = Field(..., min_length=10, max_length=10)
    role: UserRole
    is_active: bool = True

    class Config:
        collection = "users"
