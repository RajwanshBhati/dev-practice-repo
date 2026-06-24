from typing import Optional, List
from datetime import datetime
from app.models.doctor import Doctor


class DoctorRepository:
    """Repository for Doctor model operations"""

    async def create_doctor(self, doctor: Doctor) -> Doctor:
        """Create a new doctor profile"""
        return await doctor.insert()

    async def get_by_user_id(self, user_id: str) -> Optional[Doctor]:
        """Get doctor profile by user ID"""
        return await Doctor.find_one({"user.$id": user_id})

    async def get_by_license(self, license_number: str) -> Optional[Doctor]:
        """Get doctor by license number"""
        return await Doctor.find_one({"license_number": license_number})

    async def get_by_specialization(self, specialization: str) -> List[Doctor]:
        """Get doctors by specialization"""
        return await Doctor.find({"specialization": specialization}).to_list()

    async def update_doctor(self, doctor: Doctor) -> Doctor:
        """Update doctor profile"""
        doctor.updated_at = datetime.utcnow()
        return await doctor.save()
