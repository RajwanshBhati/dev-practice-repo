from datetime import datetime
from app.models.doctor import Doctor

class DoctorRepository:
    async def create_doctor(self, doctor: Doctor):
        return await doctor.insert()

    async def get_by_user_id(self, user_id: str):
        return await Doctor.find_one({"user.$id": user_id})

    async def get_by_license(self, license_number: str):
        return await Doctor.find_one({"license_number": license_number})

    async def get_by_specialization(self, specialization: str):
        return await Doctor.find({"specialization": specialization}).to_list()

    async def update_doctor(self, doctor: Doctor):
        doctor.updated_at = datetime.utcnow()
        return await doctor.save()
