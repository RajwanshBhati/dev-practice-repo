from pydantic import BaseModel

class DoctorUserResponse(BaseModel):
    id: str
    email: str
    full_name: str
    role: str
    status: str
    doctor_status: str


class DoctorRegistrationResponse(BaseModel):
    message: str
    user: DoctorUserResponse
