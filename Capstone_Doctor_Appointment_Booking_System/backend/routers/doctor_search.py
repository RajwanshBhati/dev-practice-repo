from fastapi import APIRouter, HTTPException, Depends, Query
from typing import Optional
from backend.services.doctor_search_service import DoctorSearchService
from backend.core.dependencies import get_current_user
from backend.schemas.request.doctor_search_request import DoctorSearchRequest
from backend.constants.http_status import HttpStatus
from backend.constants.messages import ErrorMessages
import logging

router = APIRouter()
logger = logging.getLogger(__name__)


@router.get("/doctor/search")
async def search_doctors(
    query: Optional[str] = Query(None, description="Search by name or specialization"),
    specialization: Optional[str] = Query(None, description="Filter by specialization"),
    location: Optional[str] = Query(None, description="Filter by location"),
    min_experience: Optional[int] = Query(None, ge=0, description="Minimum experience"),
    max_fee: Optional[float] = Query(None, gt=0, description="Maximum fee"),
    min_rating: Optional[float] = Query(None, ge=0, le=5, description="Minimum rating"),
    limit: int = Query(20, ge=1, le=100, description="Results per page"),
    skip: int = Query(0, ge=0, description="Results to skip"),
    current_user: dict = Depends(get_current_user)
):
    """
    Search for doctors with filters.
    """
    try:
        search_params = DoctorSearchRequest(
            query=query,
            specialization=specialization,
            location=location,
            min_experience=min_experience,
            max_fee=max_fee,
            min_rating=min_rating,
            limit=limit,
            skip=skip
        )

        service = DoctorSearchService()
        results = await service.search_doctors(search_params)
        return results
    except Exception as e:
        logger.error(f"Error searching doctors: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.get("/doctor/public/{doctor_id}")
async def get_public_doctor_profile(
    doctor_id: str,
    current_user: dict = Depends(get_current_user)
):
    """
    Get public doctor profile for patients.
    """
    try:
        service = DoctorSearchService()
        profile = await service.get_public_profile(doctor_id)
        return profile
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.NOT_FOUND, detail=str(e))
    except Exception as e:
        logger.error(f"Error getting public doctor profile: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )


@router.get("/doctor/specializations")
async def get_specializations():
    """
    Get all available specializations.
    """
    try:
        service = DoctorSearchService()
        result = await service.get_specializations()
        return result
    except Exception as e:
        logger.error(f"Error getting specializations: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail=ErrorMessages.GEN_9001
        )
