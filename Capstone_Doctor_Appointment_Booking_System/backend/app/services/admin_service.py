from datetime import datetime
from typing import List, Optional
from app.models.user import User
from app.constants.roles import UserRole
from app.repositories.user_repository import UserRepository
from app.utils.email import send_approval_email


class AdminService:
    def __init__(self):
        self.user_repository = UserRepository()

    async def get_pending_doctors(self) -> List[User]:
        """Get all doctors pending approval"""
        return await User.find(
            User.role == UserRole.DOCTOR,
            User.is_approved == False,
            User.is_active == True
        ).to_list()

    async def approve_doctor(
        self,
        doctor_id: str,
        admin_id: str
    ) -> User:
        """Approve a doctor account"""
        doctor = await self.user_repository.get_by_id(doctor_id)

        if not doctor:
            raise ValueError("Doctor not found")

        if doctor.role != UserRole.DOCTOR:
            raise ValueError("User is not a doctor")

        if doctor.is_approved:
            raise ValueError("Doctor already approved")

        # Update approval status
        doctor.is_approved = True
        doctor.approved_by = admin_id
        doctor.approved_at = datetime.utcnow()
        doctor.updated_at = datetime.utcnow()

        await self.user_repository.update_user(doctor)

        # Send email notification
        await send_approval_email(
            email=doctor.email,
            name=doctor.full_name,
            status="approved"
        )

        return doctor

    async def reject_doctor(
        self,
        doctor_id: str,
        admin_id: str,
        rejection_reason: str
    ) -> User:
        """Reject a doctor account"""
        doctor = await self.user_repository.get_by_id(doctor_id)

        if not doctor:
            raise ValueError("Doctor not found")

        if doctor.role != UserRole.DOCTOR:
            raise ValueError("User is not a doctor")

        if doctor.is_approved:
            raise ValueError("Doctor already approved")

        # Update rejection status
        doctor.is_approved = False
        doctor.is_active = False  # Deactivate account
        doctor.approved_by = admin_id
        doctor.approved_at = datetime.utcnow()
        doctor.rejection_reason = rejection_reason
        doctor.updated_at = datetime.utcnow()

        await self.user_repository.update_user(doctor)

        # Send rejection email
        await send_approval_email(
            email=doctor.email,
            name=doctor.full_name,
            status="rejected",
            reason=rejection_reason
        )

        return doctor
