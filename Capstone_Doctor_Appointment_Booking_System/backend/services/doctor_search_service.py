from typing import List, Dict, Any
from datetime import datetime
from backend.database.repositories.doctor_repository import DoctorRepository
from backend.database.repositories.user_repository import UserRepository
from backend.database.repositories.availability_repository import AvailabilityRepository
from backend.schemas.request.doctor_search_request import DoctorSearchRequest
from backend.schemas.response.doctor_search_response import (
    DoctorSearchResponse,
    DoctorSearchListResponse,
    DoctorPublicResponse,
    SpecializationsResponse
)
from backend.constants import ErrorMessages
from backend.constants.status import DoctorStatus
import logging

logger = logging.getLogger(__name__)


class DoctorSearchService:
    """
    Doctor search service Provides methods for searching doctors with filters.
    """

    def __init__(self):
        self.doctor_repo = DoctorRepository()
        self.user_repo = UserRepository()
        self.availability_repo = AvailabilityRepository()

    async def search_doctors(
        self,
        search_params: DoctorSearchRequest
    ) -> DoctorSearchListResponse:
        """
        Search for doctors with filters.
        """
        try:
            # Search in repository
            doctor_dicts, total = await self.doctor_repo.search_doctors(
                query=search_params.query,
                specialization=search_params.specialization,
                location=search_params.location,
                min_experience=search_params.min_experience,
                max_fee=search_params.max_fee,
                min_rating=search_params.min_rating,
                limit=search_params.limit,
                skip=search_params.skip
            )

            # Get user details and availability for each doctor
            doctors = []
            today = datetime.now().strftime('%Y-%m-%d')

            for doctor_dict in doctor_dicts:
                user = await self.user_repo.find_by_id(doctor_dict["user_id"])
                if not user:
                    continue

                # Check availability for today
                availabilities = await self.availability_repo.find_by_doctor_and_date(
                    doctor_dict["id"], today
                )
                is_available = len(availabilities) > 0

                doctors.append(
                    DoctorSearchResponse(
                        id=doctor_dict["id"],
                        full_name=user.full_name,
                        qualification=doctor_dict["qualification"],
                        specialization=doctor_dict["specialization"],
                        experience_years=doctor_dict["experience_years"],
                        consultation_fee=doctor_dict["consultation_fee"],
                        clinic_address=doctor_dict["clinic_address"],
                        clinic_phone=doctor_dict.get("clinic_phone"),
                        bio=doctor_dict.get("bio"),
                        profile_picture=doctor_dict.get("profile_picture"),
                        rating=doctor_dict.get("rating", 0.0),
                        total_reviews=doctor_dict.get("total_reviews", 0),
                        is_available=is_available
                    )
                )

            return DoctorSearchListResponse(
                doctors=doctors,
                total=total,
                skip=search_params.skip,
                limit=search_params.limit,
                has_more=search_params.skip + search_params.limit < total
            )

        except Exception as e:
            logger.error(f"Error searching doctors: {e}")
            return DoctorSearchListResponse(
                doctors=[],
                total=0,
                skip=search_params.skip,
                limit=search_params.limit,
                has_more=False
            )

    async def get_public_profile(self, doctor_id: str) -> DoctorPublicResponse:
        """
        Get public doctor profile for patients.
        """
        doctor = await self.doctor_repo.find_by_id(doctor_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1301)

        if doctor.status != DoctorStatus.APPROVED:
            raise ValueError(ErrorMessages.DOC_1306)

        user = await self.user_repo.find_by_id(doctor.user_id)
        if not user:
            raise ValueError(ErrorMessages.USER_1101)

        # Check availability for today
        today = datetime.now().strftime('%Y-%m-%d')
        availabilities = await self.availability_repo.find_by_doctor_and_date(
            doctor_id, today
        )
        is_available = len(availabilities) > 0

        return DoctorPublicResponse(
            id=doctor.id,
            full_name=user.full_name,
            qualification=doctor.qualification,
            specialization=doctor.specialization,
            experience_years=doctor.experience_years,
            consultation_fee=doctor.consultation_fee,
            clinic_address=doctor.clinic_address,
            clinic_phone=doctor.clinic_phone,
            bio=doctor.bio,
            profile_picture=doctor.profile_picture,
            rating=doctor.rating,
            total_reviews=doctor.total_reviews,
            is_available=is_available
        )

    async def get_specializations(self) -> SpecializationsResponse:
        """
        Get all available specializations.
        """
        specializations = await self.doctor_repo.get_specializations()
        return SpecializationsResponse(specializations=specializations)
