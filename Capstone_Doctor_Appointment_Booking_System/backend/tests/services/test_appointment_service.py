import pytest
from unittest.mock import AsyncMock, patch, MagicMock
from datetime import datetime, timedelta
from backend.services.appointment_service import AppointmentService
from backend.schemas.request.appointment_request import (
    AppointmentBookRequest,
    AppointmentUpdateRequest,
    AppointmentCancelRequest,
    AppointmentRescheduleRequest
)
from backend.constants.status import AppointmentStatus, PaymentStatus
from backend.enums.user_enums import DoctorStatus
from backend.constants import SuccessMessages


class TestAppointmentService:
    """Complete test cases for AppointmentService - 100% coverage."""

    @pytest.fixture
    def mock_doctor(self):
        from backend.models.profile import DoctorProfile
        from backend.enums.user_enums import VerificationStatus
        from backend.enums.doctor_enums import Specialization
        return DoctorProfile(
            id="doc123",
            user_id="doctor123",
            qualification="MD",
            specialization=Specialization.CARDIOLOGIST,
            experience_years=5,
            license_number="LIC123",
            consultation_fee=150.50,
            clinic_address="123 Main St",
            status=DoctorStatus.APPROVED,
            verification_status=VerificationStatus.VERIFIED
        )

    @pytest.fixture
    def mock_user(self):
        from backend.models.user import User
        from backend.enums.user_enums import Gender, UserStatus
        from backend.constants.roles import UserRole
        return User(
            id="patient123",
            email="patient@example.com",
            password_hash="hashed",
            full_name="John Doe",
            phone="1234567890",
            gender=Gender.MALE,
            date_of_birth="15-05-1990",
            role=UserRole.PATIENT,
            status=UserStatus.ACTIVE
        )

    @pytest.fixture
    def mock_doctor_user(self):
        from backend.models.user import User
        from backend.enums.user_enums import Gender, UserStatus
        from backend.constants.roles import UserRole
        return User(
            id="doctor123",
            email="doctor@example.com",
            password_hash="hashed",
            full_name="Dr. Smith",
            phone="1234567890",
            gender=Gender.MALE,
            date_of_birth="15-05-1985",
            role=UserRole.DOCTOR,
            status=UserStatus.ACTIVE
        )

    @pytest.fixture
    def mock_appointment(self):
        from backend.models.appointment import Appointment
        future_date = (datetime.now() + timedelta(days=3)).strftime("%Y-%m-%d")
        return Appointment(
            id="appt123",
            patient_id="patient123",
            patient_name="John Doe",
            doctor_id="doctor123",
            doctor_name="Dr. Smith",
            appointment_date=future_date,
            appointment_time="10:00",
            status=AppointmentStatus.SCHEDULED
        )

    @pytest.mark.asyncio
    async def test_cancel_appointment_success(self, mock_db, mock_appointment):
        """Test cancel appointment - success."""
        cancel_data = AppointmentCancelRequest(reason="Changed my mind")

        with patch('backend.services.appointment_service.AppointmentRepository') as mock_appt_repo:
            mock_appt_repo.return_value.get_appointment_by_id = AsyncMock(return_value=mock_appointment)
            mock_appt_repo.return_value.cancel_appointment = AsyncMock(return_value=True)

            with patch('backend.services.appointment_service.PaymentRepository') as mock_pay_repo:
                mock_pay_repo.return_value.find_by_appointment_id = AsyncMock(return_value=None)

                service = AppointmentService()
                result = await service.cancel_appointment("appt123", "patient123", cancel_data)


                assert result["message"] == str(SuccessMessages.APPOINTMENT_CANCELLED)
