from fastapi import APIRouter, HTTPException, Depends, Query
from typing import Optional
from backend.services.payment_service import PaymentService
from backend.database.dependencies import get_current_user, get_current_patient
from backend.schemas.request.payment_request import (
    PaymentInitiateRequest,
    PaymentConfirmRequest,
    PaymentRefundRequest
)
from backend.constants.http_status import HttpStatus
from backend.constants.error_messages import ErrorMessages
import logging

router = APIRouter()
logger = logging.getLogger(__name__)


@router.post("/payments/initiate")
async def initiate_payment(
    payment_data: PaymentInitiateRequest,
    current_user: dict = Depends(get_current_patient)
):
    """
    Initiate a payment for an appointment.

    Creates a pending payment record with a unique payment ID.
    The patient can then confirm the payment.

    Args:
        payment_data: Payment initiation data

    Returns:
        Payment initiation details with redirect URL
    """
    try:
        service = PaymentService()
        result = await service.initiate_payment(
            current_user["user_id"],
            payment_data
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error initiating payment: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.post("/payments/confirm")
async def confirm_payment(
    confirm_data: PaymentConfirmRequest,
    current_user: dict = Depends(get_current_patient)
):
    """
    Confirm and process a pending payment.

    Simulates payment processing with success/failure scenarios.
    On success, confirms the appointment.

    Args:
        confirm_data: Payment confirmation data

    Returns:
        Payment confirmation details
    """
    try:
        service = PaymentService()
        result = await service.confirm_payment(
            current_user["user_id"],
            confirm_data
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error confirming payment: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.post("/payments/{payment_id}/refund")
async def refund_payment(
    payment_id: str,
    refund_data: PaymentRefundRequest,
    current_user: dict = Depends(get_current_patient)
):
    """
    Refund a completed payment.

    Only completed payments can be refunded.
    Updates payment status to REFUNDED and cancels the appointment.

    Args:
        payment_id: ID of the payment to refund
        refund_data: Refund request data

    Returns:
        Refund confirmation details
    """
    try:
        service = PaymentService()
        result = await service.refund_payment(
            current_user["user_id"],
            payment_id,
            refund_data
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error refunding payment: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.get("/payments/{payment_id}/status")
async def get_payment_status(
    payment_id: str,
    current_user: dict = Depends(get_current_user)
):
    """
    Get payment status by payment ID.

    Args:
        payment_id: ID of the payment

    Returns:
        Payment details and status
    """
    try:
        service = PaymentService()
        result = await service.get_payment_status(payment_id)
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.NOT_FOUND, detail=str(e))
    except Exception as e:
        logger.error(f"Error getting payment status: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.get("/patients/payments")
async def get_patient_payments(
    limit: int = Query(20, ge=1, le=100, description="Results per page"),
    skip: int = Query(0, ge=0, description="Results to skip"),
    current_user: dict = Depends(get_current_patient)
):
    """
    Get all payments for the logged-in patient.

    Args:
        limit: Number of results per page
        skip: Number of results to skip

    Returns:
        List of payments with pagination
    """
    try:
        service = PaymentService()
        result = await service.get_patient_payments(
            current_user["user_id"],
            limit,
            skip
        )
        return result
    except Exception as e:
        logger.error(f"Error getting patient payments: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.get("/payments/revenue")
async def get_revenue_stats(
    doctor_id: Optional[str] = Query(None, description="Doctor ID for doctor-specific stats"),
    current_user: dict = Depends(get_current_user)
):
    """
    Get revenue statistics.

    Args:
        doctor_id: Optional doctor ID for doctor-specific stats

    Returns:
        Revenue statistics
    """
    try:
        service = PaymentService()
        result = await service.get_revenue_stats(doctor_id)
        return result
    except Exception as e:
        logger.error(f"Error getting revenue stats: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )
