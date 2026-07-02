from pydantic import BaseModel
from typing import Optional
from backend.constants.status import AppointmentStatus, PaymentStatus


class AppointmentResponse(BaseModel):
    """
    Response schema for appointments.

    Returns complete appointment information including patient,
    doctor, status, and payment details.
    """
    id: str
    patient_id: str
    patient_name: str
    doctor_id: str
    doctor_name: str
    appointment_date: str
    appointment_time: str
    status: AppointmentStatus
    reason: Optional[str] = None
    notes: Optional[str] = None
    payment_status: PaymentStatus
    payment_amount: Optional[float] = None
    created_at: str


class AppointmentListResponse(BaseModel):
    """
    Response schema for appointment lists with pagination.
    """
    appointments: list[AppointmentResponse]
    total: int
    page: int
    per_page: int
    total_pages: int


class AppointmentStatsResponse(BaseModel):
    """
    Response schema for appointment statistics.
    """
    total: int
    scheduled: int
    confirmed: int
    completed: int
    cancelled: int
    no_show: int
    rescheduled: int
    revenue: float
