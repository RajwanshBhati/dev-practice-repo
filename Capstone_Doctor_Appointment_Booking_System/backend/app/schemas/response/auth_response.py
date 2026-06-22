from datetime import datetime

from pydantic import BaseModel, EmailStr


class UserResponse(BaseModel):
    id: str
    full_name: str
    email: EmailStr
    role: str
    created_at: datetime


class RegisterResponse(BaseModel):
    message: str
    user: UserResponse
