from .auth_request import LoginRequest, RegisterMixin
from .patient_request import PatientRegisterRequest
from .doctor_request import DoctorRegisterRequest
from .admin_request import AdminRegisterRequest

__all__ = [
    'LoginRequest',
    'RegisterMixin',
    'PatientRegisterRequest',
    'DoctorRegisterRequest',
    'AdminRegisterRequest'
]
