from pydantic import BaseModel
from typing import Optional
from backend.constants.status import PaymentStatus
from backend.enums.payment_enums import PaymentMethod


class PaymentResponse(BaseModel):
    """
    Response schema for payments.

    Returns complete payment information including
    payment details and status.
    """
    id: str
    payment_id: str
    transaction_id: str
    appointment_id: str
    patient_id: str
    doctor_id: str
    amount: float
    method: PaymentMethod
    status: PaymentStatus
    card_last_four: Optional[str] = None
    upi_id: Optional[str] = None
    refund_id: Optional[str] = None
    refund_reason: Optional[str] = None
    created_at: str
    updated_at: str


class PaymentInitiateResponse(BaseModel):
    """
    Response schema for initiating a payment.

    Returns payment initiation details and next steps.
    """
    message: str
    payment: PaymentResponse
    redirect_url: Optional[str] = None


class PaymentConfirmResponse(BaseModel):
    """
    Response schema for confirming a payment.

    Returns payment confirmation details.
    """
    message: str
    payment: PaymentResponse
    appointment_status: str


class PaymentRefundResponse(BaseModel):
    """
    Response schema for refunding a payment.

    Returns refund confirmation details.
    """
    message: str
    refund_id: str
    payment: PaymentResponse
