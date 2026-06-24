from datetime import datetime
from typing import List, Optional
from app.models.user import User
from app.constants.roles import UserRole
from app.repositories.user_repository import UserRepository
from app.utils.email import send_approval_email


class AdminService:
    """Service for admin operations"""

    def __init__(self):
        self.user_repository = UserRepository()

    async def get_pending_doctors(self) -> List[User]:
        """
        Get all doctors pending approval
        """
        pending_doctors = await User.find(
            User.role == UserRole.DOCTOR,
            User.is_approved == False,
            User.is_active == True
        ).to_list()

        return pending_doctors

    async def get_pending_doctor_count(self) -> int:
        """
        Get count of pending doctors
        """
        count = await User.find(
            User.role == UserRole.DOCTOR,
            User.is_approved == False,
            User.is_active == True
        ).count()

        return count

    async def approve_doctor(
        self,
        doctor_id: str,
        admin_id: str
    ) -> User:
        """
        Approve a doctor account

        Args:
            doctor_id: ID of doctor to approve
            admin_id: ID of admin approving
        """
        # Get doctor user
        doctor = await self.user_repository.get_by_id(doctor_id)

        if not doctor:
            raise ValueError("Doctor not found")

        if doctor.role != UserRole.DOCTOR:
            raise ValueError("User is not a doctor")

        if doctor.is_approved:
            raise ValueError("Doctor already approved")

        if not doctor.is_active:
            raise ValueError("Doctor account is deactivated")

        # Update approval status
        doctor.is_approved = True
        doctor.approved_by = admin_id
        doctor.approved_at = datetime.utcnow()
        doctor.updated_at = datetime.utcnow()

        # Save to database
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
        """
        Reject a doctor account

        Args:
            doctor_id: ID of doctor to reject
            admin_id: ID of admin rejecting
            rejection_reason: Reason for rejection
        """
        # Get doctor user
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

        # Save to database
        await self.user_repository.update_user(doctor)

        # Send rejection email
        await send_approval_email(
            email=doctor.email,
            name=doctor.full_name,
            status="rejected",
            reason=rejection_reason
        )

        return doctor

    async def get_doctor_by_id(self, doctor_id: str) -> Optional[User]:
        """
        Get doctor by ID
        """
        return await self.user_repository.get_by_id(doctor_id)

    async def get_all_doctors(
        self,
        skip: int = 0,
        limit: int = 100,
        include_pending: bool = False
    ) -> List[User]:
        """
        Get all doctors with pagination

        Args:
            skip: Number of records to skip
            limit: Number of records to return
            include_pending: Include pending doctors
        """
        query = User.role == UserRole.DOCTOR

        if not include_pending:
            query = query & (User.is_approved == True)

        doctors = await User.find(query).skip(skip).limit(limit).to_list()
        return doctors
