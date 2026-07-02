"""
Availability model for doctor schedules.
"""

from datetime import datetime


class Availability:
    """
    Availability model representing doctor's available time slots.
    """

    def __init__(
        self,
        doctor_id: str,
        date: str,
        start_time: str,
        end_time: str,
        is_available: bool = True,
        **kwargs
    ):
        self.id = kwargs.get("id")
        self.doctor_id = doctor_id
        self.date = date
        self.start_time = start_time
        self.end_time = end_time
        self.is_available = is_available
        self.booked_by = kwargs.get("booked_by")
        self.booking_id = kwargs.get("booking_id")
        self.created_at = kwargs.get(
            "created_at",
            datetime.utcnow()
        )
        self.updated_at = kwargs.get(
            "updated_at",
            datetime.utcnow()
        )

    def to_dict(self) -> dict:
        return {
            "doctor_id": self.doctor_id,
            "date": self.date,
            "start_time": self.start_time,
            "end_time": self.end_time,
            "is_available": self.is_available,
            "booked_by": self.booked_by,
            "booking_id": self.booking_id,
            "created_at": self.created_at,
            "updated_at": self.updated_at
        }
