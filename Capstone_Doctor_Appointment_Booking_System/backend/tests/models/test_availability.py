import pytest
from backend.models.availability import Availability
from datetime import datetime, timedelta


class TestAvailability:
    def test_availability_creation(self):
        """Test creating an availability slot."""
        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")

        availability = Availability(
            doctor_id="doctor123",
            date=future_date,
            start_time="09:00",
            end_time="10:00"
        )

        assert availability.doctor_id == "doctor123"
        assert availability.is_available is True
        assert availability.start_time == "09:00"
        assert isinstance(availability.created_at, datetime)

    def test_availability_to_dict(self):
        """Test converting availability to dict."""
        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")

        availability = Availability(
            doctor_id="doctor123",
            date=future_date,
            start_time="09:00",
            end_time="10:00"
        )

        result = availability.to_dict()

        assert result["doctor_id"] == "doctor123"
        assert result["start_time"] == "09:00"
        assert "created_at" in result
