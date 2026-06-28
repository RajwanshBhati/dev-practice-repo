from datetime import datetime
import uuid

class Helpers:
    @staticmethod
    def generate_id() -> str:
        return str(uuid.uuid4())

    @staticmethod
    def format_datetime(dt: datetime) -> str:
        return dt.isoformat()

    @staticmethod
    def generate_booking_reference() -> str:
        timestamp = datetime.utcnow().strftime("%Y%m%d%H%M%S")
        random_part = uuid.uuid4().hex[:6].upper()
        return f"BOOK-{timestamp}-{random_part}"
