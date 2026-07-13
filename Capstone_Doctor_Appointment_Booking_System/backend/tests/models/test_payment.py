import pytest
from backend.models.payment import Payment
from backend.constants.status import PaymentStatus
from backend.enums.payment_enums import PaymentMethod
from datetime import datetime


class TestPayment:
    def test_payment_creation(self):
        """Test creating a payment."""
        payment = Payment(
            payment_id="PAY-123456",
            transaction_id="TXN-123456",
            appointment_id="appt123",
            patient_id="patient123",
            doctor_id="doctor123",
            amount=150.50,
            method=PaymentMethod.CREDIT_CARD
        )

        assert payment.payment_id == "PAY-123456"
        assert payment.amount == 150.50
        assert payment.status == PaymentStatus.PENDING
        assert isinstance(payment.created_at, datetime)

    def test_payment_to_dict(self):
        """Test converting payment to dict."""
        payment = Payment(
            payment_id="PAY-123456",
            transaction_id="TXN-123456",
            appointment_id="appt123",
            patient_id="patient123",
            doctor_id="doctor123",
            amount=150.50,
            method=PaymentMethod.CREDIT_CARD
        )

        result = payment.to_dict()

        assert result["payment_id"] == "PAY-123456"
        assert result["amount"] == 150.50
        assert "created_at" in result
