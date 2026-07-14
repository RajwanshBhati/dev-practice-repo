import pytest
from backend.models.user import User
from backend.enums.user_enums import Gender, UserStatus
from backend.constants.roles import UserRole
from datetime import datetime


class TestUser:
    """Test cases for User model."""

    def test_user_creation(self):
        """Test creating a user."""
        user = User(
            email="test@example.com",
            password_hash="hashed_password",
            full_name="Test User",
            phone="1234567890",
            gender=Gender.MALE,
            date_of_birth="15-05-1990",
            role=UserRole.PATIENT
        )

        assert user.email == "test@example.com"
        assert user.full_name == "Test User"
        assert user.role == UserRole.PATIENT
        assert user.status == UserStatus.PENDING
        assert isinstance(user.created_at, datetime)
