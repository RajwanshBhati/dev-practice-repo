from datetime import datetime
from typing import Optional
from backend.middleware.database import db
from backend.constants.status import PaymentStatus
from backend.enums.payment_enums import PaymentMethod


class Payment:
    """
    Payment model representing payment transactions.
    """

    def __init__(
        self,
        payment_id: str,
        transaction_id: str,
        appointment_id: str,
        patient_id: str,
        doctor_id: str,
        amount: float,
        method: PaymentMethod,
        **kwargs
    ):
        self.id = kwargs.get('id')
        self.payment_id = payment_id
        self.transaction_id = transaction_id
        self.appointment_id = appointment_id
        self.patient_id = patient_id
        self.doctor_id = doctor_id
        self.amount = amount
        self.method = method
        self.status = kwargs.get('status', PaymentStatus.PENDING)
        self.card_last_four = kwargs.get('card_last_four')
        self.upi_id = kwargs.get('upi_id')
        self.refund_id = kwargs.get('refund_id')
        self.refund_reason = kwargs.get('refund_reason')
        self.created_at = kwargs.get('created_at', datetime.utcnow())
        self.updated_at = kwargs.get('updated_at', datetime.utcnow())

    def to_dict(self) -> dict:
        """Convert payment to dictionary for database storage."""
        return {
            "payment_id": self.payment_id,
            "transaction_id": self.transaction_id,
            "appointment_id": self.appointment_id,
            "patient_id": self.patient_id,
            "doctor_id": self.doctor_id,
            "amount": self.amount,
            "method": self.method.value if hasattr(self.method, 'value') else self.method,
            "status": self.status.value if hasattr(self.status, 'value') else self.status,
            "card_last_four": self.card_last_four,
            "upi_id": self.upi_id,
            "refund_id": self.refund_id,
            "refund_reason": self.refund_reason,
            "created_at": self.created_at,
            "updated_at": self.updated_at
        }
