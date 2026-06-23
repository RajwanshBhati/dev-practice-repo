from pydantic import field_validator
from datetime import date
import re

class PasswordValidator:
    """Reusable password validation logic"""

    @staticmethod
    def validate_password_strength(password: str) -> bool:
        """Validate password meets requirements"""
        if len(password) < 8:
            raise ValueError('Password must be at least 8 characters long')
        if len(password) > 12:
            raise ValueError('Password must not exceed 12 characters')
        if not any(char.isupper() for char in password):
            raise ValueError('Password must contain at least one uppercase letter')
        if not any(char.islower() for char in password):
            raise ValueError('Password must contain at least one lowercase letter')
        if not any(char.isdigit() for char in password):
            raise ValueError('Password must contain at least one number')
        if not any(char in '!@#$%^&*()_+-=[]{}|;:,.<>?' for char in password):
            raise ValueError('Password must contain at least one special character')
        return True

    @classmethod
    def validate_password(cls, v: str) -> str:
        """Field validator for password"""
        cls.validate_password_strength(v)
        return v

    @classmethod
    def validate_confirm_password(cls, v: str, info) -> str:
        """Field validator for confirm password"""
        if 'password' in info.data and v != info.data['password']:
            raise ValueError('Passwords do not match')
        return v

class DateValidator:
    """Reusable date validation logic"""

    @staticmethod
    def calculate_age(birth_date: date) -> int:
        """Calculate age from date of birth"""
        today = date.today()
        return today.year - birth_date.year - (
            (today.month, today.day) < (birth_date.month, birth_date.day)
        )

    @classmethod
    def validate_age(cls, v: date, min_age: int = 18, max_age: int = 120) -> date:
        """Validate age range"""
        age = cls.calculate_age(v)
        if age < min_age:
            raise ValueError(f'User must be at least {min_age} years old')
        if age > max_age:
            raise ValueError(f'Invalid date of birth. Age cannot exceed {max_age} years')
        return v

    @classmethod
    def validate_future_date(cls, v: date) -> date:
        """Validate date is not in the past"""
        if v < date.today():
            raise ValueError('Date must be in the future')
        return v

class PhoneValidator:
    """Reusable phone number validation"""

    @staticmethod
    def validate_phone_number(v: str) -> str:
        """Validate phone number format"""
        cleaned = re.sub(r'\D', '', v)
        if len(cleaned) != 10:
            raise ValueError('Phone number must be exactly 10 digits')
        return cleaned

class NameValidator:
    """Reusable name validation"""

    @staticmethod
    def validate_name(v: str) -> str:
        """Validate name contains only alphabets and spaces"""
        if not re.match(r'^[A-Za-z\s]+$', v):
            raise ValueError('Name must contain only alphabets and spaces')
        if len(v.strip()) < 2:
            raise ValueError('Name must be at least 2 characters long')
        return v.strip()

class LicenseValidator:
    """Reusable license validation"""

    @staticmethod
    def validate_license_number(v: str) -> str:
        """Validate license number format"""
        if len(v) < 3:
            raise ValueError('License number must be at least 3 characters')
        if not re.match(r'^[A-Z0-9\-]+$', v):
            raise ValueError('License number can only contain letters, numbers, and hyphens')
        return v.upper()

class MoneyValidator:
    """Reusable money/price validation"""

    @staticmethod
    def validate_positive_amount(v: float) -> float:
        """Validate amount is positive"""
        if v <= 0:
            raise ValueError('Amount must be greater than 0')
        if v > 999999.99:
            raise ValueError('Amount cannot exceed 999,999.99')
        return round(v, 2)

    @staticmethod
    def validate_non_negative(v: float) -> float:
        """Validate amount is non-negative"""
        if v < 0:
            raise ValueError('Amount cannot be negative')
        return round(v, 2)
