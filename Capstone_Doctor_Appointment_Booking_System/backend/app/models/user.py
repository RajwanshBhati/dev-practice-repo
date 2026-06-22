from datetime import datetime, date
from typing import Optional

from beanie import Document
from pydantic import EmailStr

from app.constants.roles import UserRole


class User(Document):
    full_name: str

    email: EmailStr
    password: str

    phone_number: str

    role: UserRole

    # Patient Fields
    gender: Optional[str] = None
    date_of_birth: Optional[date] = None

    # Doctor Fields
    qualification: Optional[str] = None
    specialization: Optional[str] = None
    experience: Optional[int] = None
    license_number: Optional[str] = None

    is_active: bool = True

    created_at: datetime = datetime.utcnow()

    class Settings:
        name = "users"

        indexes = [
            "email"
        ]
