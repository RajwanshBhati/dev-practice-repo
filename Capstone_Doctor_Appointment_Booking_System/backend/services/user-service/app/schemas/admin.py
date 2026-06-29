from pydantic import BaseModel, EmailStr, Field, validator
from typing import Optional
from shared.enums.user_enums import Gender
from shared.utils.validators import Validators

class AdminCreateRequest(BaseModel):
    """Admin creation request schema"""
    full_name: str = Field(..., min_length=2, description="Full Name")
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
        if not Validators.validate_age(v, min_age=18, max_age=80):
            raise ValueError('Admin must be at least 18 years old')
        return v

    @validator('password')
    def validate_password(cls, v):
        if not Validators.validate_password(v):
            raise ValueError('Password must be 8-12 characters with uppercase, lowercase, digit, and special character')
        return v

    @validator('confirm_password')
    def validate_confirm_password(cls, v, values):
        if 'password' in values and v != values['password']:
            raise ValueError('Passwords do not match')
        return v

class AdminLoginRequest(BaseModel):
    """Admin login request schema"""
    email: EmailStr = Field(..., description="Email address")
    password: str = Field(..., description="Password")
