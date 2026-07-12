import re
from datetime import datetime
from typing import Optional

class Validators:
    """Reusable input validation checks for emails, phone numbers, passwords, names, and dates."""

    @staticmethod
    def validate_email(email: str) -> bool:
        """Check that a string looks like a valid email address."""
        pattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
        return bool(re.match(pattern, email))

    @staticmethod
    def validate_phone(phone: str) -> bool:
        """Check that a phone number has 10-15 digits after stripping '+' and spaces."""
        phone = phone.replace('+', '').replace(' ', '')
        return bool(re.match(r'^\d{10,15}$', phone))

    @staticmethod
    def validate_password(password: str) -> bool:
        """
        Enforce our password policy: 8-12 characters, with at least one
        uppercase letter, one lowercase letter, one digit, and one special
        character.
        """
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
        """Check that a name is at least 2 characters and contains only letters and spaces."""
        return bool(re.match(r'^[a-zA-Z\s]{2,}$', name))

    @staticmethod
    def validate_date_format(date_str: str, format: str = '%d-%m-%Y') -> bool:
        """Check that a string matches the expected date format."""
        try:
            datetime.strptime(date_str, format)
            return True
        except ValueError:
            return False

    @staticmethod
    def validate_future_date(date_str: str, format: str = '%d-%m-%Y') -> bool:
        """Check that a date string represents a point in the future, e.g. for appointment booking."""
        try:
            date = datetime.strptime(date_str, format)
            return date > datetime.now()
        except ValueError:
            return False

    @staticmethod
    def validate_age(date_str: str, min_age: int = 0, max_age: int = 120, format: str = '%d-%m-%Y') -> bool:
        """Check that the age computed from a date of birth falls within an allowed range."""
        try:
            dob = datetime.strptime(date_str, format)
            age = (datetime.now() - dob).days // 365
            return min_age <= age <= max_age
        except ValueError:
            return False

    @staticmethod
    def validate_time_format(time_str: str, format: str = '%H:%M') -> bool:
        """Check that a string matches the expected HH:MM 24-hour time format."""
        try:
            datetime.strptime(time_str, format)
            return True
        except (ValueError, TypeError):
            return False
