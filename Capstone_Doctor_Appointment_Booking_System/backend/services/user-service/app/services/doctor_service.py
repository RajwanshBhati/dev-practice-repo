from typing import Dict, Any, Optional, List
from datetime import datetime
from app.repositories.doctor_repository import DoctorRepository
from app.repositories.user_repository import UserRepository
from app.repositories.admin_repository import AdminRepository
from app.models.profile import DoctorProfile
from app.models.admin import AdminAuditLog
from app.models.user import User
from app.schemas.doctor import DoctorApproveRequest, DoctorRejectRequest
from app.services.email_service import EmailService
from shared.constants import ErrorMessages, SuccessMessages
from shared.constants.roles import UserRole
from shared.enums.user_enums import DoctorStatus, UserStatus
import logging

logger = logging.getLogger(__name__)

class DoctorService:
    """Doctor management service with approval workflow"""

    def __init__(self):
        self.doctor_repo = DoctorRepository()
        self.user_repo = UserRepository()
        self.admin_repo = AdminRepository()
        self.email_service = EmailService()

    async def get_doctor_profile(self, user_id: str) -> Dict[str, Any]:
        """Get doctor profile by user ID"""
        doctor = await self.doctor_repo.find_by_user_id(user_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1303)
        return doctor

    async def get_doctor_by_id(self, doctor_id: str) -> Dict[str, Any]:
        """Get doctor profile by ID"""
        doctor = await self.doctor_repo.find_by_id(doctor_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1301)
        return doctor

    async def get_pending_doctors(
        self,
        admin_id: str,
        limit: int = 100,
        skip: int = 0
    ) -> List[Dict[str, Any]]:
        """Get all pending doctors for admin approval"""
        return await self.doctor_repo.get_pending_doctors(limit, skip)

    async def get_doctors_by_status(
        self,
        status: DoctorStatus,
        limit: int = 100,
        skip: int = 0
    ) -> List[Dict[str, Any]]:
        """Get doctors by status"""
        return await self.doctor_repo.get_doctors_by_status(status, limit, skip)

    async def get_all_doctors(
        self,
        admin_id: str,
        limit: int = 100,
        skip: int = 0,
        status: Optional[DoctorStatus] = None
    ) -> List[Dict[str, Any]]:
        """Get all doctors with optional status filter"""
        return await self.doctor_repo.get_all_doctors(limit, skip, status)

    async def approve_doctor(
        self,
        doctor_id: str,
        admin_id: str,
        approve_data: DoctorApproveRequest
    ) -> Dict[str, Any]:
        """Approve a doctor"""
        # Get doctor profile
        doctor = await self.doctor_repo.find_by_id(doctor_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1301)

        if doctor.status != DoctorStatus.PENDING:
            raise ValueError(f"Doctor is already {doctor.status.value}")

        # Update doctor status
        updated_doctor = await self.doctor_repo.update_status(
            doctor_id,
            DoctorStatus.APPROVED,
            admin_id
        )

        if not updated_doctor:
            raise ValueError(ErrorMessages.DOC_1301)

        # Update user status to ACTIVE
        user = await self.user_repo.find_by_id(doctor.user_id)
        if user:
            await self.user_repo.update(
                user.id,
                {"status": UserStatus.ACTIVE}
            )

        # Send approval email
        await self.email_service.send_doctor_approval_email(
            user.email,
            user.full_name,
            "APPROVED"
        )

        # Create audit log
        audit_log = AdminAuditLog(
            admin_id=admin_id,
            admin_email=user.email,
            action="APPROVE_DOCTOR",
            target_id=doctor_id,
            target_email=user.email,
            details={
                "doctor_name": user.full_name,
                "specialization": doctor.specialization.value,
                "license_number": doctor.license_number,
                "notes": approve_data.notes
            }
        )
        await self.admin_repo.create_audit_log(audit_log)

        logger.info(f"Doctor approved: {user.email} by admin: {admin_id}")

        return {
            "message": SuccessMessages.DOCTOR_APPROVED,
            "doctor": updated_doctor
        }

    async def reject_doctor(
        self,
        doctor_id: str,
        admin_id: str,
        reject_data: DoctorRejectRequest
    ) -> Dict[str, Any]:
        """Reject a doctor"""
        # Get doctor profile
        doctor = await self.doctor_repo.find_by_id(doctor_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1301)

        if doctor.status != DoctorStatus.PENDING:
            raise ValueError(f"Doctor is already {doctor.status.value}")

        # Update doctor status
        updated_doctor = await self.doctor_repo.update_status(
            doctor_id,
            DoctorStatus.REJECTED,
            admin_id,
            reject_data.reason
        )

        if not updated_doctor:
            raise ValueError(ErrorMessages.DOC_1301)

        # Update user status to INACTIVE
        user = await self.user_repo.find_by_id(doctor.user_id)
        if user:
            await self.user_repo.update(
                user.id,
                {"status": UserStatus.INACTIVE}
            )

        # Send rejection email
        await self.email_service.send_doctor_approval_email(
            user.email,
            user.full_name,
            "REJECTED",
            reject_data.reason
        )

        # Create audit log
        audit_log = AdminAuditLog(
            admin_id=admin_id,
            admin_email=user.email,
            action="REJECT_DOCTOR",
            target_id=doctor_id,
            target_email=user.email,
            details={
                "doctor_name": user.full_name,
                "specialization": doctor.specialization.value,
                "license_number": doctor.license_number,
                "rejection_reason": reject_data.reason
            }
        )
        await self.admin_repo.create_audit_log(audit_log)

        logger.info(f"Doctor rejected: {user.email} by admin: {admin_id}")

        return {
            "message": SuccessMessages.DOCTOR_REJECTED,
            "doctor": updated_doctor
        }

    async def get_doctor_stats(self, admin_id: str) -> Dict[str, Any]:
        """Get doctor statistics for admin dashboard"""
        try:
            # Get counts by status
            pending = len(await self.doctor_repo.get_doctors_by_status(DoctorStatus.PENDING))
            approved = len(await self.doctor_repo.get_doctors_by_status(DoctorStatus.APPROVED))
            rejected = len(await self.doctor_repo.get_doctors_by_status(DoctorStatus.REJECTED))
            suspended = len(await self.doctor_repo.get_doctors_by_status(DoctorStatus.SUSPENDED))

            return {
                "total": pending + approved + rejected + suspended,
                "pending": pending,
                "approved": approved,
                "rejected": rejected,
                "suspended": suspended
            }
        except Exception as e:
            logger.error(f"Error getting doctor stats: {e}")
            return {
                "total": 0,
                "pending": 0,
                "approved": 0,
                "rejected": 0,
                "suspended": 0
            }
