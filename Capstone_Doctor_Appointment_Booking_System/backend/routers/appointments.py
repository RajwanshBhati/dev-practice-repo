from fastapi import APIRouter, HTTPException, Depends, Query
from typing import Optional
from backend.services.appointment_service import AppointmentService
from backend.database.dependencies import get_current_user, get_current_patient, get_current_doctor
from backend.schemas.request.appointment_request import (
    AppointmentBookRequest,
    AppointmentUpdateRequest,
    AppointmentCancelRequest,
    AppointmentRescheduleRequest
)
from backend.constants.http_status import HttpStatus
from backend.constants.error_messages import ErrorMessages
import logging

router = APIRouter()
logger = logging.getLogger(__name__)


@router.post("/appointments/book")
async def book_appointment(
    booking_data: AppointmentBookRequest,
    current_user: dict = Depends(get_current_patient)
):
    """
    Book an appointment with a doctor.

    Uses transaction to prevent double booking.
    """
    try:
        service = AppointmentService()
        result = await service.book_appointment(
            current_user["user_id"],
            booking_data
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error booking appointment: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.get("/patients/appointments")
async def get_patient_appointments(
    status: Optional[str] = Query(None, description="Filter by status"),
    limit: int = Query(20, ge=1, le=100, description="Results per page"),
    skip: int = Query(0, ge=0, description="Results to skip"),
    current_user: dict = Depends(get_current_patient)
):
    """Get appointments for the logged-in patient."""
    try:
        service = AppointmentService()
        status_enum = None
        if status:
            from backend.constants.status import AppointmentStatus
            status_enum = AppointmentStatus(status)

        result = await service.get_patient_appointments(
            current_user["user_id"],
            status_enum,
            limit,
            skip
        )
        return result
    except Exception as e:
        logger.error(f"Error getting patient appointments: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.get("/doctors/appointments")
async def get_doctor_appointments(
    status: Optional[str] = Query(None, description="Filter by status"),
    limit: int = Query(20, ge=1, le=100, description="Results per page"),
    skip: int = Query(0, ge=0, description="Results to skip"),
    current_user: dict = Depends(get_current_doctor)
):
    """Get appointments for the logged-in doctor."""
    try:
        service = AppointmentService()
        status_enum = None
        if status:
            from backend.constants.status import AppointmentStatus
            status_enum = AppointmentStatus(status)

        result = await service.get_doctor_appointments(
            current_user["user_id"],
            status_enum,
            limit,
            skip
        )
        return result
    except Exception as e:
        logger.error(f"Error getting doctor appointments: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.put("/appointments/{appt_id}/cancel")
async def cancel_appointment(
    appt_id: str,
    cancel_data: AppointmentCancelRequest,
    current_user: dict = Depends(get_current_patient)
):
    """Cancel an appointment."""
    try:
        service = AppointmentService()
        result = await service.cancel_appointment(
            appt_id,
            current_user["user_id"],
            cancel_data
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error cancelling appointment: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.put("/appointments/{appt_id}/reschedule")
async def reschedule_appointment(
    appt_id: str,
    reschedule_data: AppointmentRescheduleRequest,
    current_user: dict = Depends(get_current_patient)
):
    """Reschedule an appointment."""
    try:
        service = AppointmentService()
        result = await service.reschedule_appointment(
            appt_id,
            current_user["user_id"],
            reschedule_data
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error rescheduling appointment: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.put("/appointments/{appt_id}/status")
async def update_appointment_status(
    appt_id: str,
    update_data: AppointmentUpdateRequest,
    current_user: dict = Depends(get_current_doctor)
):
    """Update appointment status (Doctor only)."""
    try:
        service = AppointmentService()
        result = await service.update_appointment_status(
            appt_id,
            current_user["user_id"],
            update_data
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error updating appointment status: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.get("/appointments/stats")
async def get_appointment_stats(
    doctor_id: Optional[str] = Query(None, description="Doctor ID for doctor-specific stats"),
    current_user: dict = Depends(get_current_user)
):
    """Get appointment statistics. Doctors always see only their own stats."""
    try:
        service = AppointmentService()

        if current_user.get("role") == "DOCTOR":
            doctor_id = current_user["user_id"]

        stats = await service.get_appointment_stats(doctor_id)
        return stats
    except Exception as e:
        logger.error(f"Error getting appointment stats: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )
