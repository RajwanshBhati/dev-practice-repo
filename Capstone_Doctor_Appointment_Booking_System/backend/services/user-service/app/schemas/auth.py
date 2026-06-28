from pydantic import BaseModel, EmailStr, Field, validator
from typing import Optional
from shared.constants.roles import UserRole
from shared.utils.validators import Validators

class UserRegister(BaseModel):
    email: EmailStr
    password: str = Field(..., min_length=8, max_length=12)
    full_name: str = Field(..., min_length=2)
    phone_number: str = Field(..., min_length=10, max_length=10)
    role: UserRole

    @validator('full_name')
    def validate_name(cls, v):
        if not Validators.validate_name(v):
            raise ValueError('Name must contain only alphabets')
        return v

    @validator('password')
    def validate_password(cls, v):
        if not Validators.validate_password(v):
            raise ValueError('Password must be 8-12 characters with uppercase and special character')
        return v

    @validator('phone_number')
    def validate_phone(cls, v):
        if not Validators.validate_phone(v):
            raise ValueError('Phone number must be 10 digits')
        return v

class PatientRegister(UserRegister):
    date_of_birth: str
    gender: str
    address: Optional[str] = None

    @validator('role')
    def validate_role(cls, v):
        if v != UserRole.PATIENT:
            raise ValueError('Role must be PATIENT')
        return v

class DoctorRegister(UserRegister):
    qualification: str
    specialization: str
    experience_years: int = Field(..., ge=0)
    license_number: str
    consultation_fee: float = Field(..., gt=0)
    clinic_address: str
    bio: Optional[str] = None

    @validator('role')
    def validate_role(cls, v):
        if v != UserRole.DOCTOR:
            raise ValueError('Role must be DOCTOR')
        return v

class UserLogin(BaseModel):
    email: EmailStr
    password: str

class TokenResponse(BaseModel):
    access_token: str
    token_type: str = "bearer"
    expires_in: int = 1800  # 30 minutes
