from pydantic import BaseModel, EmailStr, Field, validator, model_validator
from typing import Optional
from shared.constants.roles import UserRole
from shared.enums.user_enums import Gender
from shared.enums.doctor_enums import Specialization
from shared.utils.validators import Validators
from datetime import datetime

class PatientRegister(BaseModel):
    """Patient registration schema"""
    full_name: str = Field(..., min_length=2, max_length=100, description="Full Name")
    email: EmailStr = Field(..., description="Email address")
    phone: str = Field(..., min_length=10, max_length=15, description="Phone number")
    gender: Gender = Field(..., description="Gender")
    date_of_birth: str = Field(..., description="Date of Birth (DD-MM-YYYY)")
    password: str = Field(..., min_length=8, max_length=12, description="Password")
    confirm_password: str = Field(..., description="Confirm Password")

    @validator('full_name')
    def validate_name(cls, v):
        if not Validators.validate_name(v):
            raise ValueError('Full name must contain only alphabets and spaces')
        return v

    @validator('phone')
    def validate_phone(cls, v):
        if not Validators.validate_phone(v):
            raise ValueError('Phone number must be 10-15 digits')
        return v

    @validator('date_of_birth')
    def validate_dob(cls, v):
        if not Validators.validate_date_format(v):
            raise ValueError('Date of birth must be in DD-MM-YYYY format')
        if Validators.validate_future_date(v):
            raise ValueError('Date of birth cannot be in the future')
        if not Validators.validate_age(v, min_age=1, max_age=120):
            raise ValueError('Invalid age. Patient must be at least 1 year old')
        return v

    @validator('password')
    def validate_password(cls, v):
        if not Validators.validate_password(v):
            raise ValueError('Password must be 8-12 characters with uppercase, lowercase, digit, and special character')
        return v

    @model_validator(mode='after')
    def validate_passwords_match(self):
        """Validate that password and confirm_password match"""
        if self.password != self.confirm_password:
            raise ValueError('Passwords do not match')
        return self

class DoctorRegister(PatientRegister):
    """Doctor registration schema extends PatientRegister"""
    qualification: str = Field(..., min_length=2, max_length=100, description="Medical qualification")
    specialization: Specialization = Field(..., description="Medical specialization")
    experience_years: int = Field(..., ge=0, le=50, description="Years of experience")
    license_number: str = Field(..., min_length=3, max_length=50, description="Medical license number")
    consultation_fee: float = Field(..., gt=0, description="Consultation fee")
    clinic_address: str = Field(..., min_length=5, max_length=500, description="Clinic address")
    bio: Optional[str] = Field(None, max_length=1000, description="Doctor biography")

class UserLogin(BaseModel):
    """User login schema"""
    email: EmailStr = Field(..., description="Email address")
    password: str = Field(..., description="Password")
    remember_me: bool = Field(default=False, description="Remember me")

class RefreshToken(BaseModel):
    """Refresh token request schema"""
    refresh_token: str = Field(..., description="Refresh token")

class LogoutRequest(BaseModel):
    """Logout request schema"""
    access_token: str = Field(..., description="Access token to invalidate")

class TokenResponse(BaseModel):
    """Token response schema"""
    access_token: str
    refresh_token: Optional[str] = None
    token_type: str = "bearer"
    expires_in: int = 1800
    user: Optional[dict] = None
    message: str
