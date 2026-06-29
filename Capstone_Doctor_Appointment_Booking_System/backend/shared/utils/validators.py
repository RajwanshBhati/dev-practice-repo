import re
from datetime import datetime
from typing import Optional

class Validators:
    @staticmethod
    def validate_email(email: str) -> bool:
        """Validate email format"""
        pattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
        return bool(re.match(pattern, email))

    @staticmethod
    def validate_phone(phone: str) -> bool:
        """Validate phone number (10-15 digits)"""
        phone = phone.replace('+', '').replace(' ', '')
        return bool(re.match(r'^\d{10,15}$', phone))

    @staticmethod
    def validate_password(password: str) -> bool:
        """Validate password strength"""
        # Check length (min 8, max 12)
        if len(password) < 8 or len(password) > 12:
            return False
        # Check for uppercase
        if not any(c.isupper() for c in password):
            return False
        # Check for lowercase
        if not any(c.islower() for c in password):
            return False
        # Check for digit
        if not any(c.isdigit() for c in password):
            return False
        # Check for special character
        if not any(c in '!@#$%^&*(),.?":{}|<>' for c in password):
            return False
        return True

    @staticmethod
    def validate_name(name: str) -> bool:
        """Validate name (alphabets only)"""
        return bool(re.match(r'^[a-zA-Z\s]{2,}$', name))

    @staticmethod
    def validate_date_format(date_str: str, format: str = '%d-%m-%Y') -> bool:
        """Validate date format"""
        try:
            datetime.strptime(date_str, format)
            return True
        except ValueError:
            return False

    @staticmethod
    def validate_future_date(date_str: str, format: str = '%d-%m-%Y') -> bool:
        """Check if date is in future"""
        try:
            date = datetime.strptime(date_str, format)
            return date > datetime.now()
        except ValueError:
            return False

    @staticmethod
    def validate_age(date_str: str, min_age: int = 0, max_age: int = 120, format: str = '%d-%m-%Y') -> bool:
        """Validate age range"""
        try:
            dob = datetime.strptime(date_str, format)
            age = (datetime.now() - dob).days // 365
            return min_age <= age <= max_age
        except ValueError:
            return False
