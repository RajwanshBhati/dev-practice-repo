from datetime import datetime
from typing import Optional
import uuid
import random


class Helpers:
    """
    Collection of helper utility functions.

    Provides methods for ID generation, formatting, and masking.
    """

    @staticmethod
    def generate_id() -> str:
        """Generate a random UUID4 string."""
        return str(uuid.uuid4())

    @staticmethod
    def generate_otp() -> str:
        """Generate a random 6-digit OTP."""
        return str(random.randint(100000, 999999))

    @staticmethod
    def generate_booking_reference() -> str:
        """Generate a human-readable booking reference."""
        timestamp = datetime.utcnow().strftime("%Y%m%d%H%M%S")
        random_part = uuid.uuid4().hex[:6].upper()
        return f"BOOK-{timestamp}-{random_part}"

    @staticmethod
    def generate_transaction_id() -> str:
        """Generate a unique transaction ID."""
        timestamp = datetime.utcnow().strftime("%Y%m%d")
        random_part = uuid.uuid4().hex[:6].upper()
        return f"TXN-{timestamp}-{random_part}"

    @staticmethod
    def generate_payment_id() -> str:
        """Generate a unique payment ID."""
        timestamp = datetime.utcnow().strftime("%Y%m%d")
        random_part = uuid.uuid4().hex[:6].upper()
        return f"PAY-{timestamp}-{random_part}"

    @staticmethod
    def generate_refund_id() -> str:
        """Generate a unique refund ID."""
        timestamp = datetime.utcnow().strftime("%Y%m%d")
        random_part = uuid.uuid4().hex[:6].upper()
        return f"REF-{timestamp}-{random_part}"

    @staticmethod
    def format_datetime(dt: datetime) -> str:
        """Convert datetime to ISO 8601 string."""
        return dt.isoformat()

    @staticmethod
    def parse_datetime(dt_str: str) -> datetime:
        """Parse ISO 8601 string to datetime."""
        return datetime.fromisoformat(dt_str)

    @staticmethod
    def mask_email(email: str) -> str:
        """Partially mask email for privacy."""
        if '@' in email:
            local, domain = email.split('@')
            if len(local) > 2:
                local = local[0] + '*' * (len(local) - 2) + local[-1]
            return f"{local}@{domain}"
        return email

    @staticmethod
    def mask_phone(phone: str) -> str:
        """Partially mask phone number for privacy."""
        phone = phone.replace('+', '').replace(' ', '')
        if len(phone) >= 10:
            return f"{phone[:2]}******{phone[-2:]}"
        return phone
