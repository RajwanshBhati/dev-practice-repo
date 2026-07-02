from pydantic import BaseModel, Field, validator
from typing import Optional
from backend.constants.status import PaymentMethod


class PaymentInitiateRequest(BaseModel):
    """
    Request schema for initiating a payment.

    Attributes:
        appointment_id: ID of the appointment to pay for
        method: Payment method (credit_card, debit_card, upi, net_banking, wallet)
    """
    appointment_id: str = Field(..., description="ID of the appointment")
    method: PaymentMethod = Field(..., description="Payment method")

    @validator('method')
    def validate_method(cls, v):
        """Validate payment method."""
        allowed_methods = [m.value for m in PaymentMethod]
        if v.value not in allowed_methods:
            raise ValueError(f'Invalid payment method. Allowed: {", ".join(allowed_methods)}')
        return v


class PaymentConfirmRequest(BaseModel):
    """
    Request schema for confirming a payment.

    Attributes:
        payment_id: ID of the payment to confirm
        card_last_four: Last 4 digits of card (if credit/debit card)
        upi_id: UPI ID (if UPI payment)
    """
    payment_id: str = Field(..., description="ID of the payment")
    card_last_four: Optional[str] = Field(None, description="Last 4 digits of card")
    upi_id: Optional[str] = Field(None, description="UPI ID")

    @validator('card_last_four')
    def validate_card_last_four(cls, v):
        """Validate card last four digits."""
        if v:
            if not v.isdigit() or len(v) != 4:
                raise ValueError('Card last four must be 4 digits')
        return v


class PaymentRefundRequest(BaseModel):
    """
    Request schema for refunding a payment.
    """
    reason: str = Field(..., min_length=5, max_length=500, description="Reason for refund")
