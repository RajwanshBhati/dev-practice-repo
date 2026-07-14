from fastapi import APIRouter, HTTPException, Depends
from backend.services.doctor_profile_service import DoctorProfileService
from backend.core.dependencies import get_current_doctor
from backend.schemas.request.doctor_profile_request import (
    DoctorProfileUpdateRequest,
    ProfilePictureUpdateRequest
)
from backend.constants.http_status import HttpStatus
from backend.constants.messages import ErrorMessages
import logging

router = APIRouter()
logger = logging.getLogger(__name__)


@router.get("/doctor/profile")
async def get_doctor_profile(
    current_user: dict = Depends(get_current_doctor)
):
    """
    Get the logged-in doctor's profile.
    """
    try:
        service = DoctorProfileService()
        profile = await service.get_profile(current_user["user_id"])
        return profile
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error getting doctor profile: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.put("/doctor/profile")
async def update_doctor_profile(
    update_data: DoctorProfileUpdateRequest,
    current_user: dict = Depends(get_current_doctor)
):
    """
    Update the logged-in doctor's profile.
    """
    try:
        service = DoctorProfileService()
        result = await service.update_profile(
            current_user["user_id"],
            update_data
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error updating doctor profile: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.put("/doctor/profile-picture")
async def update_profile_picture(
    picture_data: ProfilePictureUpdateRequest,
    current_user: dict = Depends(get_current_doctor)
):
    """
    Update the logged-in doctor's profile picture.
    """
    try:
        service = DoctorProfileService()
        result = await service.update_profile_picture(
            current_user["user_id"],
            picture_data
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error updating profile picture: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.get("/doctor/stats")
async def get_doctor_stats(
    current_user: dict = Depends(get_current_doctor)
):
    """
    Get doctor statistics for dashboard.
    """
    try:
        service = DoctorProfileService()
        stats = await service.get_stats(current_user["user_id"])
        return stats
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error getting doctor stats: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )
