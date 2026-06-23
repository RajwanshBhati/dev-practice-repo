# Auth Requests
from .auth_request import (
    LoginRequest,
    RegisterMixin
)

# Patient Requests
from .patient_request import (
    PatientRegisterRequest
)

# Doctor Requests
from .doctor_request import (
    DoctorRegisterRequest
)


__all__ = [
    # Auth
    'LoginRequest',
    'RegisterMixin',

    # Patient
    'PatientRegisterRequest'

    # Doctor
    'DoctorRegisterRequest'
]
