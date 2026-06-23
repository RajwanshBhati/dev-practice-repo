from pydantic import BaseModel
from datetime import datetime
from typing import Optional, List

from ..base.base_schemas import UserResponseBase

class PatientResponse(BaseModel):
    """Patient profile response"""
    id: str
    user_id: str
    full_name: str
    email: str
    phone_number: str
    gender: str
    date_of_birth: str
    age: Optional[int] = None
    preferred_language: Optional[str] = None
    medical_history: Optional[str] = None
    is_active: bool
    created_at: datetime
    updated_at: datetime


