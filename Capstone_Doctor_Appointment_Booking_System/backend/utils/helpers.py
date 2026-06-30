from datetime import datetime
from typing import Optional
import uuid
import random

class Helpers:
    @staticmethod
    def generate_id() -> str:
        """Generate unique ID"""
        return str(uuid.uuid4())

    @staticmethod
    def generate_otp() -> str:
        """Generate 6-digit OTP"""
        return str(random.randint(100000, 999999))

    @staticmethod
    def generate_booking_reference() -> str:
        """Generate unique booking reference"""
        timestamp = datetime.utcnow().strftime("%Y%m%d%H%M%S")
        random_part = uuid.uuid4().hex[:6].upper()
        return f"BOOK-{timestamp}-{random_part}"

    @staticmethod
    def format_datetime(dt: datetime) -> str:
        """Format datetime to ISO format"""
        return dt.isoformat()

    @staticmethod
    def parse_datetime(dt_str: str) -> datetime:
        """Parse ISO datetime string"""
        return datetime.fromisoformat(dt_str)

    @staticmethod
    def mask_email(email: str) -> str:
        """Mask email for privacy"""
        if '@' in email:
            local, domain = email.split('@')
            if len(local) > 2:
                local = local[0] + '*' * (len(local) - 2) + local[-1]
            return f"{local}@{domain}"
        return email

    @staticmethod
    def mask_phone(phone: str) -> str:
        """Mask phone number for privacy"""
        phone = phone.replace('+', '').replace(' ', '')
        if len(phone) >= 10:
            return f"{phone[:2]}******{phone[-2:]}"
        return phone

    @staticmethod
    def calculate_age(date_str: str, format: str = '%d-%m-%Y') -> int:
        """Calculate age from date of birth"""
        try:
            dob = datetime.strptime(date_str, format)
            return (datetime.now() - dob).days // 365
        except ValueError:
            return 0
