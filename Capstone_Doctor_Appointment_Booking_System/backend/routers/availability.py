from fastapi import APIRouter, HTTPException, Depends, Query
from typing import Optional
from backend.schemas.response.availability_response import DoctorAvailabilityResponse
from backend.services.availability_service import AvailabilityService
from backend.database.dependencies import get_current_user, get_current_doctor
from backend.schemas.request.availability_request import (
    AvailabilityCreateRequest,
    AvailabilityUpdateRequest
)
from backend.constants.http_status import HttpStatus
from backend.constants.error_messages import ErrorMessages
import logging

router = APIRouter()
logger = logging.getLogger(__name__)


@router.post("/doctors/availability")
async def create_availability_slot(
    slot_data: AvailabilityCreateRequest,
    current_user: dict = Depends(get_current_doctor)
):
    """
    Create a new availability slot for the logged-in doctor.

    This endpoint allows a doctor to create an availability slot
    for a specific date and time range. The doctor must be
    approved before creating availability.
    """
    try:
        service = AvailabilityService()
        result = await service.create_slot(
            current_user["user_id"],
            slot_data
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error creating availability slot: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.get("/doctors/availability")
async def get_doctor_availability_slots(
    date: Optional[str] = Query(None, description="Filter by date in YYYY-MM-DD format"),
    include_booked: bool = Query(False, description="Include booked slots"),
    limit: int = Query(100, ge=1, le=500, description="Results per page"),
    skip: int = Query(0, ge=0, description="Results to skip"),
    current_user: dict = Depends(get_current_doctor)
):
    """
    Get availability slots for the logged-in doctor.

    This endpoint returns all availability slots for the current
    doctor. Optional filters include date and booking status.
    """
    try:
        service = AvailabilityService()

        if date:
            # Get slots for specific date
            slots = await service.get_doctor_slots_by_date(
                current_user["user_id"],
                date,
                include_booked
            )
            return {
                "availabilities": slots,
                "total": len(slots),
                "date": date
            }
        else:
            result = await service.get_doctor_slots(
                current_user["user_id"],
                limit,
                skip
            )
            return result

    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error getting availability slots: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.get("/doctors/{doctor_id}/availability")
async def get_doctor_availability_by_id(
    doctor_id: str,
    date: Optional[str] = Query(None, description="Date in YYYY-MM-DD format"),
    limit: int = Query(100, ge=1, le=500, description="Results per page (ignored when date is set)"),
    skip: int = Query(0, ge=0, description="Results to skip (ignored when date is set)"),
    current_user: dict = Depends(get_current_user)
):
    """
    Get availability slots for any doctor.
    """
    try:
        service = AvailabilityService()

        if date:
            slots = await service.get_doctor_slots_by_date(
                doctor_id,
                date,
                include_booked=False
            )
        else:
            result = await service.get_doctor_slots(doctor_id, limit=limit, skip=skip)
            slots = result.get("availabilities", [])


        return DoctorAvailabilityResponse(
            doctor_id=doctor_id,
            date=date,
            availabilities=slots,
            total=len(slots)
        )

    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error getting doctor availability: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.get("/doctors/availability/{slot_id}")
async def get_availability_slot_by_id(
    slot_id: str,
    current_user: dict = Depends(get_current_user)
):
    """
    Get a specific availability slot by ID.
    """
    try:
        service = AvailabilityService()
        slot = await service.get_slot(slot_id)
        return slot
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.NOT_FOUND, detail=str(e))
    except Exception as e:
        logger.error(f"Error getting availability slot: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.put("/doctors/availability/{slot_id}")
async def update_availability_slot(
    slot_id: str,
    update_data: AvailabilityUpdateRequest,
    current_user: dict = Depends(get_current_doctor)
):
    """
    Update an availability slot.

    This endpoint allows a doctor to update an existing
    availability slot. Only available (non-booked) slots
    can be updated.
    """
    try:
        service = AvailabilityService()
        result = await service.update_slot(
            slot_id,
            current_user["user_id"],
            update_data
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error updating availability slot: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.delete("/doctors/availability/{slot_id}")
async def delete_availability_slot(
    slot_id: str,
    current_user: dict = Depends(get_current_doctor)
):
    """
    Delete an availability slot.

    This endpoint allows a doctor to delete an existing
    availability slot. Only available (non-booked) slots
    can be deleted.

    """
    try:
        service = AvailabilityService()
        result = await service.delete_slot(
            slot_id,
            current_user["user_id"]
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error deleting availability slot: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.get("/doctors/availability/stats")
async def get_availability_stats(
    current_user: dict = Depends(get_current_doctor)
):
    """
    Get availability statistics for the logged-in doctor.
    """
    try:
        service = AvailabilityService()
        stats = await service.get_stats(current_user["user_id"])
        return stats
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error getting availability stats: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )
