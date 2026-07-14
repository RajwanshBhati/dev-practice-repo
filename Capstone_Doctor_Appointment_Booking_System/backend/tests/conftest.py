import pytest
import asyncio
from typing import AsyncGenerator, Generator, Dict, Any
from fastapi.testclient import TestClient
from unittest.mock import AsyncMock, MagicMock, patch
from datetime import datetime, timedelta
import uuid
from jose import jwt
from bson import ObjectId
import sys
import os


sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from backend.main import app
from backend.middleware.database import db
from backend.middleware.config import settings
from backend.middleware.security import security
from backend.middleware.jwt_service import jwt_service
from backend.enums.user_enums import Gender, UserStatus, DoctorStatus, VerificationStatus
from backend.constants.roles import UserRole, Permission
from backend.constants.status import AppointmentStatus, PaymentStatus
from backend.enums.payment_enums import PaymentMethod
from backend.enums.doctor_enums import Specialization
from backend.models.user import User
from backend.models.profile import DoctorProfile, PatientProfile
from backend.models.appointment import Appointment
from backend.models.availability import Availability
from backend.models.payment import Payment
from backend.models.admin import AdminAuditLog
from backend.models.token_blacklist import TokenBlacklist


@pytest.fixture
def mock_db():
    """Create a mock database for testing."""
    with patch('backend.middleware.database.db.get_db') as mock_get_db:
        mock_db_instance = AsyncMock()
        mock_get_db.return_value = mock_db_instance

        mock_db_instance.users = AsyncMock()
        mock_db_instance.doctor_profiles = AsyncMock()
        mock_db_instance.appointments = AsyncMock()
        mock_db_instance.availabilities = AsyncMock()
        mock_db_instance.payments = AsyncMock()
        mock_db_instance.admin_audit_logs = AsyncMock()
        mock_db_instance.token_blacklist = AsyncMock()
        mock_db_instance.system_settings = AsyncMock()
        mock_db_instance.patient_profiles = AsyncMock()

        async def mock_find(*args, **kwargs):
            return mock_cursor

        mock_cursor = AsyncMock()
        mock_cursor.__aiter__ = AsyncMock(return_value=iter([]))
        mock_cursor.to_list = AsyncMock(return_value=[])
        mock_cursor.sort = AsyncMock(return_value=mock_cursor)
        mock_cursor.skip = AsyncMock(return_value=mock_cursor)
        mock_cursor.limit = AsyncMock(return_value=mock_cursor)

        mock_db_instance.users.find = AsyncMock(return_value=mock_cursor)
        mock_db_instance.doctor_profiles.find = AsyncMock(return_value=mock_cursor)
        mock_db_instance.appointments.find = AsyncMock(return_value=mock_cursor)
        mock_db_instance.availabilities.find = AsyncMock(return_value=mock_cursor)
        mock_db_instance.payments.find = AsyncMock(return_value=mock_cursor)
        mock_db_instance.admin_audit_logs.find = AsyncMock(return_value=mock_cursor)

        yield mock_db_instance


@pytest.fixture
def test_client():
    """Create a test client for the FastAPI app."""
    return TestClient(app)


@pytest.fixture
def sample_user_data():
    """Sample user data for testing."""
    return {
        "full_name": "John Doe",
        "email": "john@example.com",
        "phone": "1234567890",
        "gender": "Male",
        "date_of_birth": "15-05-1990",
        "password": "Test@1234",
        "confirm_password": "Test@1234"
    }



@pytest.fixture
def sample_doctor_data(sample_user_data):
    """Sample doctor registration data."""
    data = sample_user_data.copy()
    data.update({
        "qualification": "MD, MBBS",
        "specialization": "Cardiologist",
        "experience_years": 5,
        "license_number": "LIC123456",
        "consultation_fee": 150.50,
        "clinic_address": "123 Main St, City",
        "bio": "Experienced cardiologist"
    })
    return data


@pytest.fixture
def sample_admin_data():
    """Sample admin creation data."""
    return {
        "full_name": "Admin User",
        "email": "admin@example.com",
        "phone": "9876543210",
        "gender": "Female",
        "date_of_birth": "10-12-1985",
        "password": "Admin@1234",
        "confirm_password": "Admin@1234"
    }


@pytest.fixture
def sample_appointment_data():
    """Sample appointment booking data."""
    future_date = (datetime.now() + timedelta(days=3)).strftime("%Y-%m-%d")
    return {
        "doctor_id": "doc123",
        "appointment_date": future_date,
        "appointment_time": "10:00",
        "reason": "Regular checkup",
        "notes": "Please bring previous reports"
    }


@pytest.fixture
def sample_availability_data():
    """Sample availability creation data."""
    future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")
    return {
        "date": future_date,
        "start_time": "09:00",
        "end_time": "10:00"
    }


@pytest.fixture
def mock_user():
    """Create a mock user."""
    return User(
        id="user123",
        email="john@example.com",
        password_hash=security.hash_password("Test@1234"),
        full_name="John Doe",
        phone="1234567890",
        gender=Gender.MALE,
        date_of_birth="15-05-1990",
        role=UserRole.PATIENT,
        status=UserStatus.ACTIVE,
        is_verified=True
    )


@pytest.fixture
def mock_doctor_user():
    """Create a mock doctor user."""
    return User(
        id="doctor123",
        email="doctor@example.com",
        password_hash=security.hash_password("Test@1234"),
        full_name="Dr. Smith",
        phone="1234567890",
        gender=Gender.MALE,
        date_of_birth="15-05-1985",
        role=UserRole.DOCTOR,
        status=UserStatus.ACTIVE,
        is_verified=True
    )


@pytest.fixture
def mock_admin_user():
    """Create a mock admin user."""
    return User(
        id="admin123",
        email="admin@example.com",
        password_hash=security.hash_password("Admin@1234"),
        full_name="Admin User",
        phone="9876543210",
        gender=Gender.FEMALE,
        date_of_birth="10-12-1985",
        role=UserRole.ADMIN,
        status=UserStatus.ACTIVE,
        is_verified=True,
        is_first_admin=True
    )


@pytest.fixture
def mock_doctor_profile(mock_doctor_user):
    """Create a mock doctor profile."""
    return DoctorProfile(
        id="doc_profile123",
        user_id=mock_doctor_user.id,
        qualification="MD, MBBS",
        specialization=Specialization.CARDIOLOGIST,
        experience_years=5,
        license_number="LIC123456",
        consultation_fee=150.50,
        clinic_address="123 Main St, City",
        clinic_phone="1234567890",
        bio="Experienced cardiologist",
        status=DoctorStatus.APPROVED,
        verification_status=VerificationStatus.VERIFIED,
        rating=4.5,
        total_reviews=10
    )


@pytest.fixture
def mock_pending_doctor_profile(mock_doctor_user):
    """Create a mock pending doctor profile."""
    return DoctorProfile(
        id="doc_profile456",
        user_id=mock_doctor_user.id,
        qualification="MD, MBBS",
        specialization=Specialization.CARDIOLOGIST,
        experience_years=3,
        license_number="LIC789012",
        consultation_fee=120.00,
        clinic_address="456 Oak St, City",
        status=DoctorStatus.PENDING,
        verification_status=VerificationStatus.PENDING
    )


@pytest.fixture
def mock_appointment(mock_user, mock_doctor_user):
    """Create a mock appointment."""
    future_date = (datetime.now() + timedelta(days=3)).strftime("%Y-%m-%d")
    return Appointment(
        id="appt123",
        patient_id=mock_user.id,
        patient_name=mock_user.full_name,
        doctor_id=mock_doctor_user.id,
        doctor_name=mock_doctor_user.full_name,
        appointment_date=future_date,
        appointment_time="10:00",
        status=AppointmentStatus.SCHEDULED,
        reason="Regular checkup",
        payment_amount=150.50
    )


@pytest.fixture
def mock_availability(mock_doctor_user):
    """Create a mock availability slot."""
    future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")
    return Availability(
        id="avail123",
        doctor_id=mock_doctor_user.id,
        date=future_date,
        start_time="09:00",
        end_time="10:00",
        is_available=True
    )


@pytest.fixture
def mock_payment(mock_user, mock_doctor_user, mock_appointment):
    """Create a mock payment."""
    return Payment(
        id="pay123",
        payment_id="PAY-20260701153000-A1B2C3",
        transaction_id="TXN-A1B2C3D4E5F6",
        appointment_id=mock_appointment.id,
        patient_id=mock_user.id,
        doctor_id=mock_doctor_user.id,
        amount=150.50,
        method=PaymentMethod.CREDIT_CARD,
        status=PaymentStatus.PENDING
    )


@pytest.fixture
def auth_headers(mock_user):
    """Create authentication headers with a mock token."""
    token_data = {
        "sub": mock_user.id,
        "email": mock_user.email,
        "role": mock_user.role.value
    }
    token = jwt_service.create_access_token(token_data)
    return {"Authorization": f"Bearer {token}"}


@pytest.fixture
def admin_auth_headers(mock_admin_user):
    """Create authentication headers for admin."""
    token_data = {
        "sub": mock_admin_user.id,
        "email": mock_admin_user.email,
        "role": mock_admin_user.role.value
    }
    token = jwt_service.create_access_token(token_data)
    return {"Authorization": f"Bearer {token}"}


@pytest.fixture
def doctor_auth_headers(mock_doctor_user):
    """Create authentication headers for doctor."""
    token_data = {
        "sub": mock_doctor_user.id,
        "email": mock_doctor_user.email,
        "role": mock_doctor_user.role.value
    }
    token = jwt_service.create_access_token(token_data)
    return {"Authorization": f"Bearer {token}"}
