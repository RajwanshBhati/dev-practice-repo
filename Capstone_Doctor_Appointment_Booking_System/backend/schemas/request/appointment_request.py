from pydantic import BaseModel, Field, validator
from typing import Optional
from datetime import datetime, timedelta
from backend.constants.status import AppointmentStatus
from backend.utils.validators import Validators


class AppointmentBookRequest(BaseModel):
    """
    Request schema for booking an appointment.

    Attributes:
        doctor_id: ID of the doctor to book
        appointment_date: Date in YYYY-MM-DD format
        appointment_time: Time in HH:MM format
        reason: Reason for the visit
        notes: Additional notes
    """
    doctor_id: str = Field(..., description="ID of the doctor")
    appointment_date: str = Field(..., description="Date in YYYY-MM-DD format")
    appointment_time: str = Field(..., description="Time in HH:MM format")
    reason: Optional[str] = Field(None, description="Reason for visit")
    notes: Optional[str] = Field(None, description="Additional notes")

    @validator('appointment_date')
    def validate_date(cls, v):
        """Validate that the appointment date is not in the past."""
        try:
            date_obj = datetime.strptime(v, '%Y-%m-%d').date()
            if date_obj < datetime.now().date():
                raise ValueError('Appointment date cannot be in the past')
            return v
        except ValueError:
            raise ValueError('Invalid date format. Use YYYY-MM-DD')

    @validator('appointment_time')
    def validate_time(cls, v):
        """Validate time format and working hours."""
        if not Validators.validate_time_format(v):
            raise ValueError('Invalid time format. Use HH:MM')
        hour = int(v.split(':')[0])
        if hour < 9 or hour >= 18:
            raise ValueError('Appointment time must be between 09:00 and 18:00')
        return v


class AppointmentUpdateRequest(BaseModel):
    """
    Request schema for updating appointment status.

    Used by doctors to update appointment status.
    """
    status: AppointmentStatus = Field(..., description="New status")
    notes: Optional[str] = Field(None, description="Notes about the status update")


class AppointmentCancelRequest(BaseModel):
    """
    Request schema for cancelling an appointment.
    """
    reason: Optional[str] = Field(None, max_length=500, description="Reason for cancellation")


class AppointmentRescheduleRequest(BaseModel):
    """
    Request schema for rescheduling an appointment.
    """
    appointment_date: str = Field(..., description="New date in YYYY-MM-DD format")
    appointment_time: str = Field(..., description="New time in HH:MM format")
    reason: Optional[str] = Field(None, description="Reason for rescheduling")

    @validator('appointment_date')
    def validate_date(cls, v):
        try:
            date_obj = datetime.strptime(v, '%Y-%m-%d').date()
            if date_obj < datetime.now().date():
                raise ValueError('Appointment date cannot be in the past')
            return v
        except ValueError:
            raise ValueError('Invalid date format. Use YYYY-MM-DD')

    @validator('appointment_time')
    def validate_time(cls, v):
        if not Validators.validate_time_format(v):
            raise ValueError('Invalid time format. Use HH:MM')
        hour = int(v.split(':')[0])
        if hour < 9 or hour >= 18:
            raise ValueError('Appointment time must be between 09:00 and 18:00')
        return v
