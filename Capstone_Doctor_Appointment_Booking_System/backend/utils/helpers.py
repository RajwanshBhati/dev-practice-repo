from datetime import datetime
from typing import Optional
import uuid
import random

class Helpers:
    """Small standalone utility functions used across the app — ID generation, formatting, and masking helpers."""

    @staticmethod
    def generate_id() -> str:
        """Generate a random UUID4 string, used as a generic unique ID."""
        return str(uuid.uuid4())

    @staticmethod
    def generate_otp() -> str:
        """Generate a random 6-digit OTP for things like email/phone verification."""
        return str(random.randint(100000, 999999))

    @staticmethod
    def generate_booking_reference() -> str:
        """Build a human-readable, time-sortable booking reference like BOOK-20260701153000-A1B2C3."""
        timestamp = datetime.utcnow().strftime("%Y%m%d%H%M%S")
        random_part = uuid.uuid4().hex[:6].upper()
        return f"BOOK-{timestamp}-{random_part}"

    @staticmethod
    def format_datetime(dt: datetime) -> str:
        """Convert a datetime to its ISO 8601 string form, for consistent API responses."""
        return dt.isoformat()

    @staticmethod
    def parse_datetime(dt_str: str) -> datetime:
        """Parse an ISO 8601 string back into a datetime object."""
        return datetime.fromisoformat(dt_str)

    @staticmethod
    def mask_email(email: str) -> str:
        """Partially hide an email's local part for display, e.g. jo***e@example.com."""
        if '@' in email:
            local, domain = email.split('@')
            if len(local) > 2:
                local = local[0] + '*' * (len(local) - 2) + local[-1]
            return f"{local}@{domain}"
        return email

    @staticmethod
    def mask_phone(phone: str) -> str:
        """Partially hide a phone number for display, keeping only the first and last two digits visible."""
        phone = phone.replace('+', '').replace(' ', '')
        if len(phone) >= 10:
            return f"{phone[:2]}******{phone[-2:]}"
        return phone

    @staticmethod
    def calculate_age(date_str: str, format: str = '%d-%m-%Y') -> int:
        """Compute age in years from a date-of-birth string. Returns 0 if the string can't be parsed."""
        try:
            dob = datetime.strptime(date_str, format)
            return (datetime.now() - dob).days // 365
        except ValueError:
            return 0
