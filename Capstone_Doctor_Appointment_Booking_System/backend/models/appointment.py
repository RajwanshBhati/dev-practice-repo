from datetime import datetime
from typing import Optional
from backend.middleware.database import db
from backend.constants.status import AppointmentStatus, PaymentStatus


class Appointment:
    """
    Appointment model representing patient bookings with doctors.
    """

    collection = db.get_db().appointments

    def __init__(
        self,
        patient_id: str,
        patient_name: str,
        doctor_id: str,
        doctor_name: str,
        appointment_date: str,
        appointment_time: str,
        **kwargs
    ):
        self.id = kwargs.get('id')
        self.patient_id = patient_id
        self.patient_name = patient_name
        self.doctor_id = doctor_id
        self.doctor_name = doctor_name
        self.appointment_date = appointment_date
        self.appointment_time = appointment_time
        self.status = kwargs.get('status', AppointmentStatus.SCHEDULED)
        self.reason = kwargs.get('reason')
        self.notes = kwargs.get('notes')
        self.payment_status = kwargs.get('payment_status', PaymentStatus.PENDING)
        self.payment_amount = kwargs.get('payment_amount')
        self.created_at = kwargs.get('created_at', datetime.utcnow())
        self.updated_at = kwargs.get('updated_at', datetime.utcnow())

    def to_dict(self) -> dict:
        """Convert appointment to dictionary for database storage."""
        return {
            "patient_id": self.patient_id,
            "patient_name": self.patient_name,
            "doctor_id": self.doctor_id,
            "doctor_name": self.doctor_name,
            "appointment_date": self.appointment_date,
            "appointment_time": self.appointment_time,
            "status": self.status.value if hasattr(self.status, 'value') else self.status,
            "reason": self.reason,
            "notes": self.notes,
            "payment_status": self.payment_status.value if hasattr(self.payment_status, 'value') else self.payment_status,
            "payment_amount": self.payment_amount,
            "created_at": self.created_at,
            "updated_at": self.updated_at
        }
