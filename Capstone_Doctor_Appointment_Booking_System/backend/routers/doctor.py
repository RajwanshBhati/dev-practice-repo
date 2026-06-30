from fastapi import APIRouter, HTTPException, Depends, Query
from typing import Optional
import logging

from backend.services.doctor_service import DoctorService
from backend.database.dependencies import (
    get_current_user,
    get_current_doctor,
    get_current_admin
)

from backend.schemas.request.doctor_request import (
    DoctorProfileUpdate,
    ProfilePictureUpdate
)

from backend.constants.http_status import HttpStatus
from backend.enums.user_enums import DoctorStatus

router = APIRouter(prefix="/api/v1/doctor", tags=["doctor"])
logger = logging.getLogger(__name__)

# Doctor Profile Management

@router.get("/profile")
async def get_doctor_profile(current_user: dict = Depends(get_current_doctor)):
    """
    Get doctor profile (Doctor only)
    """
    try:
        doctor_service = DoctorService()
        profile = await doctor_service.get_doctor_profile(current_user["user_id"])
        return profile
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.NOT_FOUND, detail=str(e))
    except Exception as e:
        logger.error(f"Error getting doctor profile: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to get doctor profile"
        )

@router.put("/profile")
async def update_doctor_profile(
    update_data: DoctorProfileUpdate,
    current_user: dict = Depends(get_current_doctor)
):
    """
    Update doctor profile (Doctor only)
    """
    try:
        doctor_service = DoctorService()
        result = await doctor_service.update_doctor_profile(
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
            detail="Failed to update doctor profile"
        )

@router.put("/profile-picture")
async def update_profile_picture(
    picture_data: ProfilePictureUpdate,
    current_user: dict = Depends(get_current_doctor)
):
    """
    Update doctor profile picture (Doctor only)
    """
    try:
        doctor_service = DoctorService()
        result = await doctor_service.update_profile_picture(
            current_user["user_id"],
            picture_data.profile_picture
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error updating profile picture: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to update profile picture"
        )

# Public Doctor Profile

@router.get("/public/{doctor_id}")
async def get_public_doctor_profile(doctor_id: str):
    """
    Get public doctor profile for patients
    """
    try:
        doctor_service = DoctorService()
        profile = await doctor_service.get_public_doctor_profile(doctor_id)
        return profile
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.NOT_FOUND, detail=str(e))
    except Exception as e:
        logger.error(f"Error getting public doctor profile: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to get doctor profile"
        )

# Doctor Search

@router.get("/search")
async def search_doctors(
    query: Optional[str] = Query(None, description="Search by name, specialization, qualification"),
    specialization: Optional[str] = Query(None, description="Filter by specialization"),
    location: Optional[str] = Query(None, description="Filter by location"),
    min_experience: Optional[int] = Query(None, ge=0, description="Minimum years of experience"),
    max_fee: Optional[float] = Query(None, gt=0, description="Maximum consultation fee"),
    min_rating: Optional[float] = Query(None, ge=0, le=5, description="Minimum rating"),
    limit: int = Query(20, ge=1, le=100, description="Number of results per page"),
    skip: int = Query(0, ge=0, description="Number of results to skip")
):
    """
    Search for doctors with filters
    """
    try:
        doctor_service = DoctorService()
        results = await doctor_service.search_doctors(
            query=query,
            specialization=specialization,
            location=location,
            min_experience=min_experience,
            max_fee=max_fee,
            min_rating=min_rating,
            limit=limit,
            skip=skip
        )
        return results
    except Exception as e:
        logger.error(f"Error searching doctors: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to search doctors"
        )

# Get All Specializations

@router.get("/specializations")
async def get_specializations():
    """
    Get all available specializations
    """
    from backend.enums.doctor_enums import Specialization
    return {
        "specializations": [spec.value for spec in Specialization]
    }

# Doctor Dashboard Stats

@router.get("/stats")
async def get_doctor_stats(current_user: dict = Depends(get_current_doctor)):
    """
    Get doctor statistics for dashboard
    """
    try:
        doctor_service = DoctorService()
        # Get doctor profile
        profile = await doctor_service.get_doctor_profile(current_user["user_id"])

        # Return basic stats
        return {
            "total_patients": 0,
            "total_appointments": 0,
            "today_appointments": 0,
            "upcoming_appointments": 0,
            "completed_appointments": 0,
            "cancelled_appointments": 0,
            "rating": profile.rating,
            "total_reviews": profile.total_reviews
        }
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.NOT_FOUND, detail=str(e))
    except Exception as e:
        logger.error(f"Error getting doctor stats: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to get doctor statistics"
        )
