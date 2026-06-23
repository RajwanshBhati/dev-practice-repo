# Auth Responses
from .auth_response import (
    RegisterResponse,
    LoginResponse
)

# Patient Responses
from .patient_response import (
    PatientResponse
)

# Doctor Responses
from .doctor_response import (
    DoctorResponse,
)


__all__ = [
    # Auth
    'RegisterResponse',
    'LoginResponse',

    # Patient
    'PatientResponse'

    # Doctor
    'DoctorResponse'

]
