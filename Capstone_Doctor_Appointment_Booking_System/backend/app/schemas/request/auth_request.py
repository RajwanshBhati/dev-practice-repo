from datetime import date

from pydantic import BaseModel, EmailStr


class PatientRegisterRequest(BaseModel):
    full_name: str
    email: EmailStr
    phone_number: str
    gender: str
    date_of_birth: date

    password: str
    confirm_password: str


class DoctorRegisterRequest(BaseModel):
    full_name: str
    email: EmailStr
    phone_number: str

    qualification: str
    specialization: str
    experience: int
    license_number: str

    password: str
    confirm_password: str

class LoginRequest(BaseModel):
    email: EmailStr
    password: str
