from typing import Optional, List, Tuple
from datetime import datetime
from bson import ObjectId
from backend.middleware.database import get_db
from backend.models.availability import Availability
import logging

logger = logging.getLogger(__name__)


class AvailabilityRepository:
    """
    Repository for availability database operations.

    This class provides methods for creating, reading, updating,
    and deleting availability slots for doctors.
    """

    def __init__(self):
        """Initialize the repository with the availabilities collection."""
        self.collection = get_db().availabilities

    async def create(self, availability: Availability) -> Availability:
        """
        Create a new availability slot in the database.

        This method inserts a new availability document and assigns
        the generated ObjectId as the slot's id.
        """
        try:
            avail_dict = availability.to_dict()
            result = await self.collection.insert_one(avail_dict)
            availability.id = str(result.inserted_id)
            return availability
        except Exception as e:
            logger.error(f"Error creating availability: {e}")
            raise

    async def find_by_id(self, avail_id: str) -> Optional[Availability]:
        """
        Find an availability slot by ID.

        Validates the ObjectId format before querying to avoid
        cast errors. Returns None if the ID is invalid or not found.
        """
        try:
            if not ObjectId.is_valid(avail_id):
                return None
            avail_dict = await self.collection.find_one(
                {"_id": ObjectId(avail_id)}
            )
            if avail_dict:
                avail_dict["id"] = str(avail_dict["_id"])
                return Availability(**avail_dict)
            return None
        except Exception as e:
            logger.error(f"Error finding availability by ID: {e}")
            raise

    async def find_by_doctor_and_date(
        self,
        doctor_id: str,
        date: str,
        include_booked: bool = False
    ) -> List[Availability]:
        """
        Find all availability slots for a doctor on a specific date.
        """
        try:
            query = {
                "doctor_id": doctor_id,
                "date": date
            }
            if not include_booked:
                query["is_available"] = True

            cursor = self.collection.find(query).sort("start_time", 1)
            availabilities = []
            async for avail_dict in cursor:
                avail_dict["id"] = str(avail_dict["_id"])
                availabilities.append(Availability(**avail_dict))
            return availabilities
        except Exception as e:
            logger.error(f"Error finding availability by doctor and date: {e}")
            return []

    async def find_by_doctor(
        self,
        doctor_id: str,
        limit: int = 100,
        skip: int = 0
    ) -> Tuple[List[Availability], int]:
        """
        Find all availability slots for a doctor with pagination.
        """
        try:
            query = {"doctor_id": doctor_id}
            total = await self.collection.count_documents(query)

            cursor = self.collection.find(query).sort("date", 1).skip(skip).limit(limit)
            availabilities = []
            async for avail_dict in cursor:
                avail_dict["id"] = str(avail_dict["_id"])
                availabilities.append(Availability(**avail_dict))

            return availabilities, total
        except Exception as e:
            logger.error(f"Error finding availability by doctor: {e}")
            return [], 0

    async def update(self, avail_id: str, update_data: dict) -> bool:
        """
        Update an existing availability slot.

        The updated_at timestamp is automatically set to the
        current UTC time.
        """
        try:
            update_data["updated_at"] = datetime.utcnow()
            result = await self.collection.update_one(
                {"_id": ObjectId(avail_id)},
                {"$set": update_data}
            )
            return result.modified_count > 0
        except Exception as e:
            logger.error(f"Error updating availability: {e}")
            return False

    async def delete(self, avail_id: str) -> bool:
        """
        Delete an availability slot.
        """
        try:
            result = await self.collection.delete_one(
                {"_id": ObjectId(avail_id)}
            )
            return result.deleted_count > 0
        except Exception as e:
            logger.error(f"Error deleting availability: {e}")
            return False

    async def check_overlap(
        self,
        doctor_id: str,
        date: str,
        start_time: str,
        end_time: str,
        exclude_id: Optional[str] = None
    ) -> bool:
        """
        Check if a new slot overlaps with existing slots.
        """
        try:
            query = {
                "doctor_id": doctor_id,
                "date": date,
                "$or": [
                    # New slot starts inside existing slot
                    {
                        "start_time": {"$lte": start_time},
                        "end_time": {"$gt": start_time}
                    },
                    # New slot ends inside existing slot
                    {
                        "start_time": {"$lt": end_time},
                        "end_time": {"$gte": end_time}
                    },
                    # New slot completely contains existing slot
                    {
                        "start_time": {"$gte": start_time},
                        "end_time": {"$lte": end_time}
                    }
                ]
            }

            if exclude_id:
                query["_id"] = {"$ne": ObjectId(exclude_id)}

            count = await self.collection.count_documents(query)
            return count > 0
        except Exception as e:
            logger.error(f"Error checking overlap: {e}")
            return True

    async def get_stats(self, doctor_id: str) -> dict:
        """
        Get availability statistics for a doctor
        """
        try:
            total = await self.collection.count_documents({"doctor_id": doctor_id})
            available = await self.collection.count_documents({
                "doctor_id": doctor_id,
                "is_available": True
            })
            booked = total - available

            return {
                "total_slots": total,
                "available_slots": available,
                "booked_slots": booked
            }
        except Exception as e:
            logger.error(f"Error getting availability stats: {e}")
            return {
                "total_slots": 0,
                "available_slots": 0,
                "booked_slots": 0
            }
