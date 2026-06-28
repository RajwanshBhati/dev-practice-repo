import re
from datetime import datetime

class Validators:
    @staticmethod
    def validate_email(email: str) -> bool:
        pattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
        return bool(re.match(pattern, email))

    @staticmethod
    def validate_phone(phone: str) -> bool:
        return bool(re.match(r'^\d{10}$', phone))

    @staticmethod
    def validate_password(password: str) -> bool:
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
        return bool(re.match(r'^[a-zA-Z\s]{2,}$', name))
