from typing import Dict, Any
from backend.database.repositories.doctor_repository import DoctorRepository
from backend.database.repositories.user_repository import UserRepository
from backend.schemas.request.doctor_profile_request import (
    DoctorProfileUpdateRequest,
    ProfilePictureUpdateRequest
)
from backend.schemas.response.doctor_profile_response import (
    DoctorProfileResponse,
    DoctorProfileUpdateResponse,
    DoctorStatsResponse,
    ProfilePictureResponse
)
from backend.constants import ErrorMessages, SuccessMessages
from backend.constants.status import DoctorStatus
import logging

logger = logging.getLogger(__name__)


class DoctorProfileService:
    """
    Doctor profile management service.
    """

    def __init__(self):
        self.doctor_repo = DoctorRepository()
        self.user_repo = UserRepository()

    async def get_profile(self, user_id: str) -> DoctorProfileResponse:
        """
        Get doctor profile by user ID.
        """
        doctor = await self.doctor_repo.find_by_user_id(user_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1303)

        user = await self.user_repo.find_by_id(user_id)
        if not user:
            raise ValueError(ErrorMessages.USER_1101)

        return DoctorProfileResponse(
            id=doctor.id,
            user_id=doctor.user_id,
            full_name=user.full_name,
            email=user.email,
            phone=user.phone,
            qualification=doctor.qualification,
            specialization=doctor.specialization,
            experience_years=doctor.experience_years,
            license_number=doctor.license_number,
            consultation_fee=doctor.consultation_fee,
            clinic_address=doctor.clinic_address,
            clinic_phone=doctor.clinic_phone,
            bio=doctor.bio,
            profile_picture=doctor.profile_picture,
            status=doctor.status,
            rating=doctor.rating,
            total_reviews=doctor.total_reviews,
            created_at=doctor.created_at.isoformat(),
            updated_at=doctor.updated_at.isoformat()
        )

    async def update_profile(
        self,
        user_id: str,
        update_data: DoctorProfileUpdateRequest
    ) -> Dict[str, Any]:
        """
        Update doctor profile.
        """
        doctor = await self.doctor_repo.find_by_user_id(user_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1303)

        if doctor.status != DoctorStatus.APPROVED:
            raise ValueError(ErrorMessages.DOC_1306)

        update_dict = update_data.model_dump(exclude_unset=True)
        if not update_dict:
            raise ValueError("No fields to update")

        await self.doctor_repo.update(doctor.id, update_dict)

        logger.info(f"Doctor profile updated: {user_id}")

        return DoctorProfileUpdateResponse(
        message=SuccessMessages.PROFILE_UPDATED,
        doctor_id=str(doctor.id)
        )

    async def update_profile_picture(
        self,
        user_id: str,
        picture_data: ProfilePictureUpdateRequest
    ) -> Dict[str, Any]:
        """
        Update doctor profile picture.
        """
        doctor = await self.doctor_repo.find_by_user_id(user_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1303)

        await self.doctor_repo.update(
            doctor.id,
            {"profile_picture": picture_data.profile_picture}
        )

        logger.info(f"Doctor profile picture updated: {user_id}")

        return ProfilePictureResponse(
        message="Profile picture updated successfully",
        profile_picture=picture_data.profile_picture
        )

    async def get_stats(self, user_id: str) -> DoctorStatsResponse:
        """
        Get doctor statistics.
        """
        doctor = await self.doctor_repo.find_by_user_id(user_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1303)

        return DoctorStatsResponse(
            total_patients=0,
            total_appointments=0,
            today_appointments=0,
            upcoming_appointments=0,
            completed_appointments=0,
            cancelled_appointments=0,
            rating=doctor.rating,
            total_reviews=doctor.total_reviews
        )
