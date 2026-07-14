import re
from datetime import datetime


class Validators:
    @staticmethod
    def validate_email(email: str) -> bool:
        """
        Check if email format is valid.

        Uses a regular expression to ensure the email follows
        the standard format: raj@gmail.com
        """
        pattern = r'^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'
        return bool(re.match(pattern, email))

    @staticmethod
    def validate_phone(phone: str) -> bool:
        """
        Check if phone number format is valid.

        Removes leading + and spaces before validation.
        Ensures the phone number contains only digits and is
        between 10 and 15 digits in length.
        """
        phone = phone.replace('+', '').replace(' ', '')
        return bool(re.match(r'^\d{10,15}$', phone))

    @staticmethod
    def validate_password(password: str) -> bool:
        """
        Check if password meets strength requirements.

        Ensures password meets security requirements:
        - Length between 8 and 12 characters
        - Contains at least one uppercase letter
        - Contains at least one lowercase letter
        - Contains at least one digit
        - Contains at least one special character
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
        """
        Check if name contains only alphabets and spaces.

        Ensures the name contains only alphabets and spaces,
        with a minimum length of 2 characters
        """
        return bool(re.match(r'^[a-zA-Z\s]{2,}$', name))

    @staticmethod
    def validate_date_format(date_str: str, format: str = '%d-%m-%Y') -> bool:
        """
        Check if date string matches the specified format.
        """
        try:
            datetime.strptime(date_str, format)
            return True
        except ValueError:
            return False

    @staticmethod
    def validate_time_format(time_str: str) -> bool:
        """
        Check if time string is in HH:MM format.
        """
        pattern = r'^([0-1][0-9]|2[0-3]):[0-5][0-9]$'
        return bool(re.match(pattern, time_str))

    @staticmethod
    def validate_future_date(date_str: str, format: str = '%Y-%m-%d') -> bool:
        """
        Check if date is in the future.

        Parses the date string and compares it to the current
        date to determine if it's in the future.
        """
        try:
            date = datetime.strptime(date_str, format)
            return date.date() > datetime.now().date()
        except ValueError:
            return False

    @staticmethod
    def validate_age(date_str: str, min_age: int = 0, max_age: int = 120, format: str = '%d-%m-%Y') -> bool:
        """
        Validate age range from date of birth.

        Calculates age from date of birth and checks if it falls
        within the specified min and max age range.
        """
        try:
            dob = datetime.strptime(date_str, format)
            today = datetime.now()
            age = today.year - dob.year - ((today.month, today.day) < (dob.month, dob.day))
            return min_age <= age <= max_age
        except (ValueError, AttributeError):
            return False

    @staticmethod
    def validate_date_not_past(date_str: str, format: str = '%Y-%m-%d') -> bool:
        """
        Check if date is not in the past.
        """
        try:
            date_obj = datetime.strptime(date_str, format).date()
            today = datetime.now().date()
            return date_obj >= today
        except ValueError:
            return False

    @staticmethod
    def validate_time_format(time_str: str) -> bool:
        """
        Validate time format (HH:MM)
        """
        try:
            datetime.strptime(time_str, "%H:%M")
            return True
        except ValueError:
            return False
