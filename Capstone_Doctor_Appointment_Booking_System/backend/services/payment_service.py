from typing import Dict, Any, Optional
from datetime import datetime
import random
import asyncio
from backend.repositories.payment_repository import PaymentRepository
from backend.repositories.appointment_repository import AppointmentRepository
from backend.models.payment import Payment
from backend.schemas.request.payment_request import (
    PaymentInitiateRequest,
    PaymentConfirmRequest,
    PaymentRefundRequest
)
from backend.schemas.response.payment_response import (
    PaymentResponse,
    PaymentInitiateResponse,
    PaymentConfirmResponse,
    PaymentRefundResponse
)
from backend.constants import ErrorMessages, SuccessMessages
from backend.constants.status import PaymentStatus, PaymentMethod, AppointmentStatus
from backend.utils.helpers import Helpers
import logging

logger = logging.getLogger(__name__)


class PaymentService:
    def __init__(self):
        self.payment_repo = PaymentRepository()
        self.appointment_repo = AppointmentRepository()

    async def initiate_payment(
        self,
        patient_id: str,
        payment_data: PaymentInitiateRequest
    ) -> PaymentInitiateResponse:
        """
        Initiate a payment for an appointment.

        Creates a pending payment record with a unique payment ID
        and transaction ID.
        """
        appointment = await self.appointment_repo.get_appointment_by_id(
            payment_data.appointment_id
        )
        if not appointment:
            raise ValueError(ErrorMessages.APP_1201)

        if appointment.patient_id != patient_id:
            raise ValueError(ErrorMessages.AUTH_1005)

        # Check if payment already exists
        existing_payment = await self.payment_repo.find_by_appointment_id(
            payment_data.appointment_id
        )
        if existing_payment:
            if existing_payment.status == PaymentStatus.COMPLETED:
                raise ValueError(ErrorMessages.PAY_1503)
            elif existing_payment.status == PaymentStatus.PENDING:
                raise ValueError("Payment already initiated")

        # Get doctor for consultation fee
        from backend.repositories.doctor_repository import DoctorRepository
        doctor_repo = DoctorRepository()
        doctor = await doctor_repo.find_by_id(appointment.doctor_id)
        amount = doctor.consultation_fee if doctor else 150.50

        # Create payment record
        payment = Payment(
            payment_id=Helpers.generate_payment_id(),
            transaction_id=Helpers.generate_transaction_id(),
            appointment_id=payment_data.appointment_id,
            patient_id=patient_id,
            doctor_id=appointment.doctor_id,
            amount=amount,
            method=payment_data.method,
            status=PaymentStatus.PENDING
        )

        created = await self.payment_repo.create(payment)

        logger.info(f"Payment initiated: {created.payment_id} for appointment {appointment.id}")

        # Build response
        response = PaymentResponse(
            id=created.id,
            payment_id=created.payment_id,
            transaction_id=created.transaction_id,
            appointment_id=created.appointment_id,
            patient_id=created.patient_id,
            doctor_id=created.doctor_id,
            amount=created.amount,
            method=created.method,
            status=created.status,
            card_last_four=created.card_last_four,
            upi_id=created.upi_id,
            refund_id=created.refund_id,
            refund_reason=created.refund_reason,
            created_at=created.created_at.isoformat(),
            updated_at=created.updated_at.isoformat()
        )

        return PaymentInitiateResponse(
            message=SuccessMessages.PAYMENT_INITIATED,
            payment=response,
            redirect_url=f"/payment/confirm/{created.payment_id}"
        )

    async def confirm_payment(
        self,
        patient_id: str,
        confirm_data: PaymentConfirmRequest
    ) -> PaymentConfirmResponse:

        # Get payment
        payment = await self.payment_repo.find_by_payment_id(confirm_data.payment_id)
        if not payment:
            raise ValueError(ErrorMessages.PAY_1502)

        if payment.patient_id != patient_id:
            raise ValueError(ErrorMessages.AUTH_1005)

        if payment.status == PaymentStatus.COMPLETED:
            raise ValueError(ErrorMessages.PAY_1503)

        if payment.status == PaymentStatus.REFUNDED:
            raise ValueError(ErrorMessages.PAY_1504)

        # Simulate payment processing
        await asyncio.sleep(2)

        # Mock success/failure
        success = random.random() < 0.9

        if success:
            # Update payment status
            await self.payment_repo.update_status(payment.id, PaymentStatus.COMPLETED)

            # Update appointment status
            await self.appointment_repo.update_appointment(
                payment.appointment_id,
                {
                    "status": AppointmentStatus.CONFIRMED.value,
                    "payment_status": PaymentStatus.COMPLETED.value,
                    "payment_amount": payment.amount
                }
            )

            message = SuccessMessages.PAYMENT_SUCCESS
            appointment_status = AppointmentStatus.CONFIRMED.value

            logger.info(f"Payment confirmed: {payment.payment_id} for appointment {payment.appointment_id}")
        else:
            # Update payment status to failed
            await self.payment_repo.update_status(payment.id, PaymentStatus.FAILED)

            message = ErrorMessages.PAY_1501
            appointment_status = AppointmentStatus.SCHEDULED.value

            logger.warning(f"Payment failed: {payment.payment_id}")

        # Get updated payment
        updated_payment = await self.payment_repo.find_by_id(payment.id)

        response = PaymentResponse(
            id=updated_payment.id,
            payment_id=updated_payment.payment_id,
            transaction_id=updated_payment.transaction_id,
            appointment_id=updated_payment.appointment_id,
            patient_id=updated_payment.patient_id,
            doctor_id=updated_payment.doctor_id,
            amount=updated_payment.amount,
            method=updated_payment.method,
            status=updated_payment.status,
            card_last_four=updated_payment.card_last_four,
            upi_id=updated_payment.upi_id,
            refund_id=updated_payment.refund_id,
            refund_reason=updated_payment.refund_reason,
            created_at=updated_payment.created_at.isoformat(),
            updated_at=updated_payment.updated_at.isoformat()
        )

        return PaymentConfirmResponse(
            message=message,
            payment=response,
            appointment_status=appointment_status
        )

    async def refund_payment(
        self,
        patient_id: str,
        payment_id: str,
        refund_data: PaymentRefundRequest
    ) -> PaymentRefundResponse:
        """
        Refund a completed payment.

        Only payments in COMPLETED status can be refunded.
        Creates a refund ID and updates payment status.
        """
        # Get payment
        payment = await self.payment_repo.find_by_payment_id(payment_id)
        if not payment:
            raise ValueError(ErrorMessages.PAY_1502)

        if payment.patient_id != patient_id:
            raise ValueError(ErrorMessages.AUTH_1005)

        if payment.status != PaymentStatus.COMPLETED:
            raise ValueError("Only completed payments can be refunded")

        # Generate refund ID
        refund_id = Helpers.generate_refund_id()

        # Update payment
        await self.payment_repo.update(
            payment.id,
            {
                "status": PaymentStatus.REFUNDED.value,
                "refund_id": refund_id,
                "refund_reason": refund_data.reason
            }
        )

        # Update appointment
        await self.appointment_repo.update_appointment(
            payment.appointment_id,
            {
                "payment_status": PaymentStatus.REFUNDED.value,
                "status": AppointmentStatus.CANCELLED.value
            }
        )

        logger.info(f"Payment refunded: {payment_id} with refund ID {refund_id}")

        # Get updated payment
        updated_payment = await self.payment_repo.find_by_id(payment.id)

        response = PaymentResponse(
            id=updated_payment.id,
            payment_id=updated_payment.payment_id,
            transaction_id=updated_payment.transaction_id,
            appointment_id=updated_payment.appointment_id,
            patient_id=updated_payment.patient_id,
            doctor_id=updated_payment.doctor_id,
            amount=updated_payment.amount,
            method=updated_payment.method,
            status=updated_payment.status,
            card_last_four=updated_payment.card_last_four,
            upi_id=updated_payment.upi_id,
            refund_id=updated_payment.refund_id,
            refund_reason=updated_payment.refund_reason,
            created_at=updated_payment.created_at.isoformat(),
            updated_at=updated_payment.updated_at.isoformat()
        )

        return PaymentRefundResponse(
            message=SuccessMessages.PAYMENT_REFUNDED,
            refund_id=refund_id,
            payment=response
        )

        """
        Get revenue statistics.

        Args:
            doctor_id: Optional doctor ID for doctor-specific stats

        Returns:
            Dict: Revenue statistics
        """
        total_revenue = await self.payment_repo.get_total_revenue(doctor_id)

        return {
            "total_revenue": total_revenue,
            "currency": "USD"
        }
