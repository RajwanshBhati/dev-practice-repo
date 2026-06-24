from typing import Optional
from datetime import datetime
from app.models.patient import Patient


class PatientRepository:
    """Repository for Patient model operations"""

    async def create_patient(self, patient: Patient) -> Patient:
        """Create a new patient profile"""
        return await patient.insert()

    async def get_by_user_id(self, user_id: str) -> Optional[Patient]:
        """Get patient profile by user ID"""
        return await Patient.find_one({"user.$id": user_id})

    async def update_patient(self, patient: Patient) -> Patient:
        """Update patient profile"""
        patient.updated_at = datetime.utcnow()
        return await patient.save()
