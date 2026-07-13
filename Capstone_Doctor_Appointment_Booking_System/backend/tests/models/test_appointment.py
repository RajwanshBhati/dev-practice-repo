import pytest
from backend.models.appointment import Appointment
from backend.constants.status import AppointmentStatus
from datetime import datetime, timedelta


class TestAppointment:
    def test_appointment_creation(self):
        """Test creating an appointment."""
        future_date = (datetime.now() + timedelta(days=3)).strftime("%Y-%m-%d")

        appointment = Appointment(
            patient_id="patient123",
            patient_name="John Doe",
            doctor_id="doctor123",
            doctor_name="Dr. Smith",
            appointment_date=future_date,
            appointment_time="10:00"
        )

        assert appointment.patient_id == "patient123"
        assert appointment.doctor_id == "doctor123"
        assert appointment.status == AppointmentStatus.SCHEDULED
        assert isinstance(appointment.created_at, datetime)

    def test_appointment_to_dict(self):
        """Test converting appointment to dict."""
        future_date = (datetime.now() + timedelta(days=3)).strftime("%Y-%m-%d")

        appointment = Appointment(
            patient_id="patient123",
            patient_name="John Doe",
            doctor_id="doctor123",
            doctor_name="Dr. Smith",
            appointment_date=future_date,
            appointment_time="10:00"
        )

        result = appointment.to_dict()

        assert result["patient_id"] == "patient123"
        assert result["doctor_id"] == "doctor123"
        assert "created_at" in result
