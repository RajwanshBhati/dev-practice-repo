import pytest
from backend.utils.validators import Validators
from datetime import datetime, timedelta


class TestValidators:
    """Test cases for Validators utility class."""

    def test_validate_email_valid(self):
        """Test valid email addresses."""
        valid_emails = [
            "test@example.com",
            "user.name@domain.co",
            "user+tag@domain.com",
            "user123@domain.com"
        ]
        for email in valid_emails:
            assert Validators.validate_email(email) is True

    def test_validate_email_invalid(self):
        """Test invalid email addresses."""
        invalid_emails = [
            "invalid",
            "invalid@",
            "@domain.com",
            "invalid@domain"
        ]
        for email in invalid_emails:
            assert Validators.validate_email(email) is False

    def test_validate_phone_valid(self):
        """Test valid phone numbers."""
        valid_phones = [
            "1234567890",
            "9876543210",
            "123456789012345"
        ]
        for phone in valid_phones:
            assert Validators.validate_phone(phone) is True

    def test_validate_phone_with_plus(self):
        """Test phone numbers with plus sign."""
        assert Validators.validate_phone("+1234567890") is True
        assert Validators.validate_phone("+919876543210") is True

    def test_validate_phone_invalid(self):
        """Test invalid phone numbers."""
        invalid_phones = [
            "123",
            "abcdefghij",
            "123456789",
            "1234567890123456"
        ]
        for phone in invalid_phones:
            assert Validators.validate_phone(phone) is False

    def test_validate_password_valid(self):
        """Test valid passwords."""
        valid_passwords = [
            "Test@1234",
            "Abc@2024",
            "Xyz!9876",
            "P@ssw0rd"
        ]
        for password in valid_passwords:
            assert Validators.validate_password(password) is True

    def test_validate_password_invalid(self):
        """Test invalid passwords."""
        invalid_passwords = [
            "short",
            "nouppercase123!",
            "NOLOWERCASE123!",
            "NoSpecial123",
            "NoDigits@!",
            "ThisIsWayTooLongForPassword@123"
        ]
        for password in invalid_passwords:
            assert Validators.validate_password(password) is False

    def test_validate_name_valid(self):
        """Test valid names."""
        valid_names = [
            "John Doe",
            "Jane",
            "Dr. Smith",
        ]
        # Only alpha and spaces should be valid
        assert Validators.validate_name("John Doe") is True
        assert Validators.validate_name("Jane") is True

    def test_validate_name_invalid(self):
        """Test invalid names."""
        invalid_names = [
            "J",
            "John123",
            "John@Doe",
            "John_Doe"
        ]
        for name in invalid_names:
            assert Validators.validate_name(name) is False

    def test_validate_date_format_valid(self):
        """Test valid date formats."""
        valid_dates = [
            "15-05-1990",
            "01-01-2000",
            "31-12-2023"
        ]
        for date_str in valid_dates:
            assert Validators.validate_date_format(date_str) is True

    def test_validate_date_format_invalid(self):
        """Test invalid date formats."""
        invalid_dates = [
            "1990-05-15",
            "15/05/1990",
            "15-13-1990",
            "32-01-2023"
        ]
        for date_str in invalid_dates:
            assert Validators.validate_date_format(date_str) is False

    def test_validate_future_date(self):
        """Test future date validation."""
        future_date = (datetime.now() + timedelta(days=1)).strftime("%d-%m-%Y")
        assert Validators.validate_future_date(future_date) is True

        past_date = (datetime.now() - timedelta(days=1)).strftime("%d-%m-%Y")
        assert Validators.validate_future_date(past_date) is False

    def test_validate_age(self):
        """Test age validation."""
        # 25 years old
        dob_25 = (datetime.now() - timedelta(days=25*365)).strftime("%d-%m-%Y")
        assert Validators.validate_age(dob_25, min_age=18, max_age=80) is True
        assert Validators.validate_age(dob_25, min_age=30, max_age=80) is False

        # 90 years old
        dob_90 = (datetime.now() - timedelta(days=90*365)).strftime("%d-%m-%Y")
        assert Validators.validate_age(dob_90, min_age=18, max_age=80) is False

        # Invalid date
        assert Validators.validate_age("invalid-date") is False

    def test_validate_time_format_valid(self):
        """Test valid time formats."""
        valid_times = [
            "09:00",
            "10:30",
            "17:45",
            "00:00"
        ]
        for time_str in valid_times:
            assert Validators.validate_time_format(time_str) is True

    def test_validate_time_format_invalid(self):
        """Test invalid time formats."""
        invalid_times = [
            "25:00",
            "09:60",
            "invalid"
        ]
        for time_str in invalid_times:
            assert Validators.validate_time_format(time_str) is False
