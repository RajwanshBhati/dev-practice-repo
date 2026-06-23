from datetime import datetime
from app.models.patient import Patient

class PatientRepository:
    async def create_patient(self, patient: Patient):
        return await patient.insert()

    async def get_by_user_id(self, user_id: str):
        return await Patient.find_one({"user.$id": user_id})

    async def update_patient(self, patient: Patient):
        patient.updated_at = datetime.utcnow()
        return await patient.save()
