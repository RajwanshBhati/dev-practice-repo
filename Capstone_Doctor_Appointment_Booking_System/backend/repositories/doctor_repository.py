from typing import Optional, List
from datetime import datetime
from bson import ObjectId
from backend.middleware.database import db
from backend.models.profile import DoctorProfile
from backend.enums.user_enums import DoctorStatus
import logging

logger = logging.getLogger(__name__)

class DoctorRepository:
    """Repository for doctor operations"""

    def __init__(self):
        self.collection = db.get_db().doctor_profiles

    async def create(self, doctor: DoctorProfile) -> DoctorProfile:
        """Create a new doctor profile"""
        try:
            doctor_dict = doctor.model_dump(exclude={"id"}, by_alias=True)
            doctor_dict = {k: v for k, v in doctor_dict.items() if v is not None}
            result = await self.collection.insert_one(doctor_dict)
            doctor.id = str(result.inserted_id)
            return doctor
        except Exception as e:
            logger.error(f"Error creating doctor profile: {e}")
            raise

    async def find_by_user_id(self, user_id: str) -> Optional[DoctorProfile]:
        """Find doctor profile by user ID"""
        try:
            doctor_dict = await self.collection.find_one({"user_id": user_id})
            if doctor_dict:
                doctor_dict["id"] = str(doctor_dict["_id"])
                return DoctorProfile(**doctor_dict)
            return None
        except Exception as e:
            logger.error(f"Error finding doctor by user ID: {e}")
            raise

    async def find_by_id(self, doctor_id: str) -> Optional[DoctorProfile]:
        """Find doctor profile by ID"""
        try:
            if not ObjectId.is_valid(doctor_id):
                return None
            doctor_dict = await self.collection.find_one({"_id": ObjectId(doctor_id)})
            if doctor_dict:
                doctor_dict["id"] = str(doctor_dict["_id"])
                return DoctorProfile(**doctor_dict)
            return None
        except Exception as e:
            logger.error(f"Error finding doctor by ID: {e}")
            raise

    async def update(self, doctor_id: str, update_data: dict) -> Optional[DoctorProfile]:
        """Update doctor profile"""
        try:
            update_data["updated_at"] = datetime.utcnow()
            result = await self.collection.update_one(
                {"_id": ObjectId(doctor_id)},
                {"$set": update_data}
            )
            if result.modified_count > 0:
                return await self.find_by_id(doctor_id)
            return None
        except Exception as e:
            logger.error(f"Error updating doctor: {e}")
            raise

    async def update_status(
        self,
        doctor_id: str,
        status: DoctorStatus,
        admin_id: Optional[str] = None,
        rejection_reason: Optional[str] = None
    ) -> Optional[DoctorProfile]:
        """Update doctor status with approval/rejection"""
        try:
            update_data = {
                "status": status,
                "updated_at": datetime.utcnow()
            }

            if status == DoctorStatus.APPROVED:
                update_data["approved_by"] = admin_id
                update_data["approved_at"] = datetime.utcnow()
                update_data["rejection_reason"] = None
            elif status == DoctorStatus.REJECTED:
                update_data["rejected_by"] = admin_id
                update_data["rejected_at"] = datetime.utcnow()
                if rejection_reason:
                    update_data["rejection_reason"] = rejection_reason

            result = await self.collection.update_one(
                {"_id": ObjectId(doctor_id)},
                {"$set": update_data}
            )
            if result.modified_count > 0:
                return await self.find_by_id(doctor_id)
            return None
        except Exception as e:
            logger.error(f"Error updating doctor status: {e}")
            raise

    async def get_pending_doctors(self, limit: int = 100, skip: int = 0) -> List[DoctorProfile]:
        """Get all pending doctors"""
        try:
            cursor = self.collection.find({"status": DoctorStatus.PENDING}).skip(skip).limit(limit)
            doctors = []
            async for doctor_dict in cursor:
                doctor_dict["id"] = str(doctor_dict["_id"])
                doctors.append(DoctorProfile(**doctor_dict))
            return doctors
        except Exception as e:
            logger.error(f"Error getting pending doctors: {e}")
            return []

    async def get_doctors_by_status(
        self,
        status: DoctorStatus,
        limit: int = 100,
        skip: int = 0
    ) -> List[DoctorProfile]:
        """Get doctors by status"""
        try:
            cursor = self.collection.find({"status": status}).skip(skip).limit(limit)
            doctors = []
            async for doctor_dict in cursor:
                doctor_dict["id"] = str(doctor_dict["_id"])
                doctors.append(DoctorProfile(**doctor_dict))
            return doctors
        except Exception as e:
            logger.error(f"Error getting doctors by status: {e}")
            return []

    async def get_all_doctors(
        self,
        limit: int = 100,
        skip: int = 0,
        status: Optional[DoctorStatus] = None
    ) -> List[DoctorProfile]:
        """Get all doctors with optional status filter"""
        try:
            query = {}
            if status:
                query["status"] = status

            cursor = self.collection.find(query).skip(skip).limit(limit)
            doctors = []
            async for doctor_dict in cursor:
                doctor_dict["id"] = str(doctor_dict["_id"])
                doctors.append(DoctorProfile(**doctor_dict))
            return doctors
        except Exception as e:
            logger.error(f"Error getting all doctors: {e}")
            return []
