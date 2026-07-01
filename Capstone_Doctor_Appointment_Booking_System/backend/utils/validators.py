import re
from datetime import datetime


class Validators:
    """Collection of validation methods for user input."""

    @staticmethod
    def validate_email(email: str) -> bool:
        """Check if email format is valid."""
        pattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
        return bool(re.match(pattern, email))

    @staticmethod
    def validate_phone(phone: str) -> bool:
        """Check if phone number format is valid."""
        phone = phone.replace('+', '').replace(' ', '')
        return bool(re.match(r'^\d{10,15}$', phone))

    @staticmethod
    def validate_password(password: str) -> bool:
        """Check if password meets strength requirements."""
        if len(password) < 8 or len(password) > 12:
            return False
        if not any(c.isupper() for c in password):
            return False
        if not any(c.islower() for c in password):
            return False
        if not any(c.isdigit() for c in password):
            return False
        if not any(c in '!@#$%^&*(),.?":{}|<>' for c in password):
            return False
        return True

    @staticmethod
    def validate_name(name: str) -> bool:
        """Check if name contains only alphabets and spaces."""
        return bool(re.match(r'^[a-zA-Z\s]{2,}$', name))

    @staticmethod
    def validate_date_format(date_str: str, format: str = '%d-%m-%Y') -> bool:
        """Check if date string matches the specified format."""
        try:
            datetime.strptime(date_str, format)
            return True
        except ValueError:
            return False

    @staticmethod
    def validate_time_format(time_str: str) -> bool:
        """
        Check if time string is in HH:MM format.

        Args:
            time_str: Time string to validate

        Returns:
            bool: True if format is valid, False otherwise
        """
        pattern = r'^([0-1][0-9]|2[0-3]):[0-5][0-9]$'
        return bool(re.match(pattern, time_str))

    @staticmethod
    def validate_future_date(date_str: str, format: str = '%Y-%m-%d') -> bool:
        """Check if date is in the future."""
        try:
            date = datetime.strptime(date_str, format)
            return date.date() > datetime.now().date()
        except ValueError:
            return False
