# Auth Requests
from .auth_request import (
    LoginRequest,
    RegisterMixin
)

# Patient Requests
from .patient_request import (
    PatientRegisterRequest,
    PatientUpdateRequest,
    PatientSearchRequest
)

# Doctor Requests
from .doctor_request import (
    DoctorRegisterRequest,
    DoctorUpdateRequest,
    DoctorSearchRequest,
    DoctorAvailabilityRequest,
    DoctorSlotUpdateRequest
)

# Admin Requests
from .admin_request import (
    AdminRegisterRequest,
    AdminUpdateRequest,
    AdminFilterRequest
)

__all__ = [
    # Auth
    'LoginRequest',
    'RegisterMixin',

    # Patient
    'PatientRegisterRequest',
    'PatientUpdateRequest',
    'PatientSearchRequest',

    # Doctor
    'DoctorRegisterRequest',
    'DoctorUpdateRequest',
    'DoctorSearchRequest',
    'DoctorAvailabilityRequest',
    'DoctorSlotUpdateRequest',

    # Admin
    'AdminRegisterRequest',
    'AdminUpdateRequest',
    'AdminFilterRequest'
]
