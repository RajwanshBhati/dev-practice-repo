from typing import Dict, Any, List, Optional
from backend.repositories.availability_repository import AvailabilityRepository
from backend.repositories.doctor_repository import DoctorRepository
from backend.models.availability import Availability
from backend.schemas.request.availability_request import (
    AvailabilityCreateRequest,
    AvailabilityUpdateRequest
)
from backend.schemas.response.availability_response import (
    AvailabilityResponse,
    AvailabilityCreateResponse,
    AvailabilityUpdateResponse,
    AvailabilityDeleteResponse
)
from backend.constants import ErrorMessages, SuccessMessages
from backend.enums.user_enums import DoctorStatus
import logging

logger = logging.getLogger(__name__)


class AvailabilityService:
    """
    Availability management service.

    This class provides methods for managing doctor availability
    slots including creation, update, deletion, and querying.
    """

    def __init__(self):
        """Initialize the service with required repositories."""
        self.availability_repo = AvailabilityRepository()
        self.doctor_repo = DoctorRepository()

    async def create_slot(
        self,
        doctor_id: str,
        slot_data: AvailabilityCreateRequest
    ) -> AvailabilityCreateResponse:
        """
        Create a new availability slot for a doctor.
        """
        # Get doctor profile
        doctor = await self.doctor_repo.find_by_user_id(doctor_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1303)

        # Check if doctor is approved
        if doctor.status != DoctorStatus.APPROVED:
            raise ValueError(ErrorMessages.DOC_1306)

        # Check for overlapping slots
        has_overlap = await self.availability_repo.check_overlap(
            doctor_id=doctor_id,
            date=slot_data.date,
            start_time=slot_data.start_time,
            end_time=slot_data.end_time
        )

        if has_overlap:
            raise ValueError(ErrorMessages.AVAIL_1005)

        # Create availability slot
        availability = Availability(
            doctor_id=doctor_id,
            date=slot_data.date,
            start_time=slot_data.start_time,
            end_time=slot_data.end_time
        )

        created = await self.availability_repo.create(availability)

        logger.info(f"Availability slot created: {created.id} for doctor {doctor_id}")

        # Build response
        response = AvailabilityResponse(
            id=created.id,
            doctor_id=created.doctor_id,
            date=created.date,
            start_time=created.start_time,
            end_time=created.end_time,
            is_available=created.is_available,
            created_at=created.created_at.isoformat(),
            updated_at=created.updated_at.isoformat()
        )

        return AvailabilityCreateResponse(
            message=SuccessMessages.AVAILABILITY_ADDED,
            availability=response
        )

    async def get_slot(self, slot_id: str) -> AvailabilityResponse:
        """
        Get an availability slot by ID.

        """
        availability = await self.availability_repo.find_by_id(slot_id)
        if not availability:
            raise ValueError(ErrorMessages.AVAIL_1001)

        return AvailabilityResponse(
            id=availability.id,
            doctor_id=availability.doctor_id,
            date=availability.date,
            start_time=availability.start_time,
            end_time=availability.end_time,
            is_available=availability.is_available,
            created_at=availability.created_at.isoformat(),
            updated_at=availability.updated_at.isoformat()
        )


    async def get_doctor_slots_by_profile_id(
        self,
        profile_id: str,
        date: Optional[str] = None,
        include_booked: bool = False
    ) -> List[AvailabilityResponse]:
        doctor = await self.doctor_repo.find_by_id(profile_id)
        if not doctor:
            doctor = await self.doctor_repo.find_by_user_id(profile_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1301)

        if date:
            return await self.get_doctor_slots_by_date(
                doctor.user_id, date, include_booked
            )

        result = await self.get_doctor_slots(doctor.user_id, limit=100, skip=0)
        return result.get("availabilities", [])


    async def get_doctor_slots(
        self,
        doctor_id: str,
        limit: int = 100,
        skip: int = 0
    ) -> Dict[str, Any]:
        """
        Get all availability slots for a doctor with pagination.
        """
        # Get doctor profile
        doctor = await self.doctor_repo.find_by_user_id(doctor_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1303)

        # Get slots
        slots, total = await self.availability_repo.find_by_doctor(
            doctor_id, limit, skip
        )

        # Build response
        slot_responses = [
            AvailabilityResponse(
                id=slot.id,
                doctor_id=slot.doctor_id,
                date=slot.date,
                start_time=slot.start_time,
                end_time=slot.end_time,
                is_available=slot.is_available,
                created_at=slot.created_at.isoformat(),
                updated_at=slot.updated_at.isoformat()
            )
            for slot in slots
        ]

        return {
            "availabilities": slot_responses,
            "total": total,
            "page": (skip // limit) + 1 if limit > 0 else 1,
            "per_page": limit,
            "total_pages": (total + limit - 1) // limit if limit > 0 else 1
        }

    async def get_doctor_slots_by_date(
        self,
        doctor_id: str,
        date: str,
        include_booked: bool = False
    ) -> List[AvailabilityResponse]:
        """
        Get availability slots for a doctor on a specific date.

        Args:
            doctor_id: ID of the doctor
            date: Date in YYYY-MM-DD format
            include_booked: Whether to include booked slots

        Returns:
            List[AvailabilityResponse]: List of slots
        """
        # Get doctor profile
        doctor = await self.doctor_repo.find_by_user_id(doctor_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1303)

        # Get slots
        slots = await self.availability_repo.find_by_doctor_and_date(
            doctor_id, date, include_booked
        )

        return [
            AvailabilityResponse(
                id=slot.id,
                doctor_id=slot.doctor_id,
                date=slot.date,
                start_time=slot.start_time,
                end_time=slot.end_time,
                is_available=slot.is_available,
                created_at=slot.created_at.isoformat(),
                updated_at=slot.updated_at.isoformat()
            )
            for slot in slots
        ]

    async def update_slot(
        self,
        slot_id: str,
        doctor_id: str,
        update_data: AvailabilityUpdateRequest
    ) -> AvailabilityUpdateResponse:
        """
        Update an existing availability slot.
        """
        # Get the slot
        availability = await self.availability_repo.find_by_id(slot_id)
        if not availability:
            raise ValueError(ErrorMessages.AVAIL_1001)

        # Check if slot belongs to the doctor
        if availability.doctor_id != doctor_id:
            raise ValueError(ErrorMessages.AUTH_1005)

        # Check if slot is booked
        if not availability.is_available:
            raise ValueError(ErrorMessages.AVAIL_1004)

        # Prepare update data
        update_dict = update_data.model_dump(exclude_unset=True)

        if not update_dict:
            raise ValueError("No fields to update")

        # Check for overlapping slots (if date or time is changing)
        date = update_dict.get('date', availability.date)
        start_time = update_dict.get('start_time', availability.start_time)
        end_time = update_dict.get('end_time', availability.end_time)

        if (date != availability.date or
            start_time != availability.start_time or
            end_time != availability.end_time):
            has_overlap = await self.availability_repo.check_overlap(
                doctor_id=doctor_id,
                date=date,
                start_time=start_time,
                end_time=end_time,
                exclude_id=slot_id
            )
            if has_overlap:
                raise ValueError(ErrorMessages.AVAIL_1005)

        # Update the slot
        await self.availability_repo.update(slot_id, update_dict)

        logger.info(f"Availability slot updated: {slot_id}")

        return AvailabilityUpdateResponse(
            message=SuccessMessages.AVAILABILITY_UPDATED,
            availability_id=slot_id
        )

    async def delete_slot(
        self,
        slot_id: str,
        doctor_id: str
    ) -> AvailabilityDeleteResponse:
        """
        Delete an availability slot.
        """
        # Get the slot
        availability = await self.availability_repo.find_by_id(slot_id)
        if not availability:
            raise ValueError(ErrorMessages.AVAIL_1001)

        # Check if slot belongs to the doctor
        if availability.doctor_id != doctor_id:
            raise ValueError(ErrorMessages.AUTH_1005)

        # Check if slot is booked
        if not availability.is_available:
            raise ValueError(ErrorMessages.AVAIL_1003)

        # Delete the slot
        await self.availability_repo.delete(slot_id)

        logger.info(f"Availability slot deleted: {slot_id}")

        return AvailabilityDeleteResponse(
            message=SuccessMessages.AVAILABILITY_DELETED,
            availability_id=slot_id
        )

    async def get_stats(self, doctor_id: str) -> Dict[str, Any]:
        """
        Get availability statistics for a doctor.
        """
        doctor = await self.doctor_repo.find_by_user_id(doctor_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1303)

        return await self.availability_repo.get_stats(doctor_id)
