"""
Availability model for doctor schedules.

This module defines the Availability model which represents
time slots when doctors are available for appointments.
"""

from datetime import datetime
from typing import Optional
from backend.middleware.database import get_db



class Availability:
    """
    Availability model representing doctor's available time slots.

    This class manages doctor availability including date, time slots,
    and booking status. It ensures that doctors can only be booked
    during their available hours.
    """

    collection = get_db().availabilities

    def __init__(
        self,
        doctor_id: str,
        date: str,
        start_time: str,
        end_time: str,
        is_available: bool = True,
        **kwargs
    ):
        self.id = kwargs.get('id')
        self.doctor_id = doctor_id
        self.date = date
        self.start_time = start_time
        self.end_time = end_time
        self.is_available = is_available
        self.booked_by = kwargs.get('booked_by')
        self.booking_id = kwargs.get('booking_id')
        self.created_at = kwargs.get('created_at', datetime.utcnow())
        self.updated_at = kwargs.get('updated_at', datetime.utcnow())

    def to_dict(self) -> dict:
        """
        Convert availability data to dictionary for database storage.

        Returns:
            dict: Dictionary representation of availability data
        """
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
