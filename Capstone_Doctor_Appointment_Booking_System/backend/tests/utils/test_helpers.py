import pytest
from datetime import datetime, timedelta
from backend.utils.helpers import Helpers


class TestHelpers:
    """Test cases for Helpers utility class."""

    def test_generate_id(self):
        """Test UUID generation."""
        id1 = Helpers.generate_id()
        id2 = Helpers.generate_id()

        assert len(id1) == 36
        assert id1 != id2
        assert isinstance(id1, str)

    def test_generate_otp(self):
        """Test OTP generation."""
        otp = Helpers.generate_otp()

        assert len(otp) == 6
        assert otp.isdigit()
        assert 100000 <= int(otp) <= 999999

    def test_generate_booking_reference(self):
        """Test booking reference generation."""
        ref = Helpers.generate_booking_reference()

        assert ref.startswith("BOOK-")
        assert len(ref) > 20
        assert "-" in ref

    def test_format_datetime(self):
        """Test datetime formatting."""
        dt = datetime(2026, 7, 1, 15, 30, 0)
        formatted = Helpers.format_datetime(dt)

        assert formatted == "2026-07-01T15:30:00"

    def test_parse_datetime(self):
        """Test datetime parsing."""
        dt_str = "2026-07-01T15:30:00"
        parsed = Helpers.parse_datetime(dt_str)

        assert parsed.year == 2026
        assert parsed.month == 7
        assert parsed.day == 1
        assert parsed.hour == 15
        assert parsed.minute == 30

    def test_mask_email(self):
        """Test email masking."""
        email = "john.doe@example.com"
        masked = Helpers.mask_email(email)

        assert masked in ["jo***oe@example.com", "j******e@example.com"]

    def test_mask_phone(self):
        """Test phone masking."""
        phone = "1234567890"
        masked = Helpers.mask_phone(phone)

        assert masked == "12******90"

        # Phone with plus
        phone_with_plus = "+919876543210"
        masked = Helpers.mask_phone(phone_with_plus)
        assert "******" in masked

    def test_calculate_age(self):
        """Test age calculation."""
        dob = (datetime.now() - timedelta(days=25*365)).strftime("%d-%m-%Y")
        age = Helpers.calculate_age(dob)

        assert age == 25 or age == 24

    def test_calculate_age_invalid(self):
        """Test age calculation with invalid date."""
        age = Helpers.calculate_age("invalid-date")
        assert age == 0

    def test_generate_payment_id(self):
        """Test payment ID generation."""
        payment_id = Helpers.generate_payment_id()

        assert payment_id.startswith("PAY-")
        assert len(payment_id) > 20

    def test_generate_transaction_id(self):
        """Test transaction ID generation."""
        txn_id = Helpers.generate_transaction_id()

        assert txn_id.startswith("TXN-")
        assert len(txn_id) > 15

    def test_generate_refund_id(self):
        """Test refund ID generation."""
        refund_id = Helpers.generate_refund_id()

        assert refund_id.startswith("REF-")
        assert len(refund_id) > 15
