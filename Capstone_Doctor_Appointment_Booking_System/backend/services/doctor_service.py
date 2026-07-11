from typing import Dict, Any, Optional, List
from datetime import datetime
from backend.repositories.doctor_repository import DoctorRepository
from backend.repositories.user_repository import UserRepository
from backend.repositories.admin_repository import AdminRepository
from backend.models.profile import DoctorProfile
from backend.models.admin import AdminAuditLog
from backend.models.user import User
from backend.schemas.request.doctor_request import DoctorProfileUpdate, DoctorApproveRequest, DoctorRejectRequest
from backend.services.email_service import EmailService
from backend.constants import ErrorMessages, SuccessMessages
from backend.constants.roles import UserRole
from backend.enums.user_enums import DoctorStatus, UserStatus
import logging

logger = logging.getLogger(__name__)

class DoctorService:
    """
    Everything related to doctor profiles: fetching them, letting doctors
    edit their own info, running the admin approval/rejection workflow,
    and searching/filtering the public doctor directory.
    """

    def __init__(self):
        self.doctor_repo = DoctorRepository()
        self.user_repo = UserRepository()
        self.admin_repo = AdminRepository()
        self.email_service = EmailService()

    async def get_doctor_profile(self, user_id: str) -> Dict[str, Any]:
        """Fetch a doctor's own profile using their linked user ID."""
        doctor = await self.doctor_repo.find_by_user_id(user_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1303)
        return doctor

    async def get_doctor_by_id(self, doctor_id: str) -> Dict[str, Any]:
        """Fetch a doctor's profile by its own document ID."""
        doctor = await self.doctor_repo.find_by_id(doctor_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1301)
        return doctor

    async def get_public_doctor_profile(self, doctor_id: str) -> Dict[str, Any]:
        """
        Return only the fields patients should see for a doctor, and only
        if that doctor has actually been approved by an admin.
        """
        doctor = await self.doctor_repo.find_by_id(doctor_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1301)

        user = await self.user_repo.find_by_id(doctor.user_id)
        if not user:
            raise ValueError(ErrorMessages.USER_1101)

        if doctor.status != DoctorStatus.APPROVED:
            raise ValueError(ErrorMessages.DOC_1306)

        return {
            "id": doctor.id,
            "full_name": user.full_name,
            "qualification": doctor.qualification,
            "specialization": doctor.specialization,
            "experience_years": doctor.experience_years,
            "consultation_fee": doctor.consultation_fee,
            "clinic_address": doctor.clinic_address,
            "clinic_phone": doctor.clinic_phone,
            "bio": doctor.bio,
            "profile_picture": doctor.profile_picture,
            "rating": doctor.rating,
            "total_reviews": doctor.total_reviews,
            "is_available": doctor.status == DoctorStatus.APPROVED
        }

    async def update_doctor_profile(
        self,
        user_id: str,
        update_data: DoctorProfileUpdate
    ) -> Dict[str, Any]:
        """Let an already-approved doctor update their own profile fields."""
        doctor = await self.doctor_repo.find_by_user_id(user_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1303)

        if doctor.status != DoctorStatus.APPROVED:
            raise ValueError("Doctor must be approved to update profile")

        update_dict = update_data.model_dump(exclude_unset=True)

        if not update_dict:
            raise ValueError("No fields to update")

        updated_doctor = await self.doctor_repo.update(doctor.id, update_dict)
        if not updated_doctor:
            raise ValueError(ErrorMessages.DOC_1301)

        logger.info(f"Doctor profile updated: {user_id}")

        return {
            "message": SuccessMessages.PROFILE_UPDATED,
            "doctor": updated_doctor
        }

    async def update_profile_picture(
        self,
        user_id: str,
        profile_picture: str
    ) -> Dict[str, Any]:
        """Update just the doctor's profile picture URL."""
        doctor = await self.doctor_repo.find_by_user_id(user_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1303)

        updated_doctor = await self.doctor_repo.update(
            doctor.id,
            {"profile_picture": profile_picture}
        )

        if not updated_doctor:
            raise ValueError(ErrorMessages.DOC_1301)

        logger.info(f"Doctor profile picture updated: {user_id}")

        return {
            "message": "Profile picture updated successfully",
            "profile_picture": profile_picture
        }

    async def _attach_user_info(self, doctor: DoctorProfile) -> Dict[str, Any]:
        doctor_dict = doctor.model_dump(by_alias=False)
        user = await self.user_repo.find_by_id(doctor.user_id)
        doctor_dict["full_name"] = user.full_name if user else None
        doctor_dict["email"] = user.email if user else None
        return doctor_dict

    async def get_pending_doctors(
        self,
        admin_id: str,
        limit: int = 100,
        skip: int = 0
    ) -> List[Dict[str, Any]]:
        """Fetch doctors waiting on approval, for the admin review queue."""
        doctors = await self.doctor_repo.get_pending_doctors(limit, skip)
        return [await self._attach_user_info(doctor) for doctor in doctors]

    async def get_doctors_by_status(
        self,
        status: DoctorStatus,
        limit: int = 100,
        skip: int = 0
    ) -> List[Dict[str, Any]]:
        """Fetch doctors matching a given status."""
        return await self.doctor_repo.get_doctors_by_status(status, limit, skip)

    async def get_all_doctors(
        self,
        admin_id: str,
        limit: int = 100,
        skip: int = 0,
        status: Optional[DoctorStatus] = None
    ) -> List[Dict[str, Any]]:
        """Fetch all doctors, optionally narrowed down by status, for the admin dashboard."""
        doctors = await self.doctor_repo.get_all_doctors(limit, skip, status)
        return [await self._attach_user_info(doctor) for doctor in doctors]

    async def count_all_doctors(self, status: Optional[DoctorStatus] = None) -> int:
        """Total number of doctors matching an optional status filter, for pagination."""
        return await self.doctor_repo.count_all_doctors(status)

    async def approve_doctor(
        self,
        doctor_id: str,
        admin_id: str,
        approve_data: DoctorApproveRequest
    ) -> Dict[str, Any]:
        """
        Approve a pending doctor: flips both the doctor profile and linked
        user account to active, emails the doctor the good news, and logs
        the action for audit purposes.
        """
        doctor = await self.doctor_repo.find_by_id(doctor_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1301)

        if doctor.status != DoctorStatus.PENDING:
            raise ValueError(f"Doctor is already {doctor.status.value}")

        updated_doctor = await self.doctor_repo.update_status(
            doctor_id,
            DoctorStatus.APPROVED,
            admin_id
        )

        if not updated_doctor:
            raise ValueError(ErrorMessages.DOC_1301)

        user = await self.user_repo.find_by_id(doctor.user_id)
        if user:
            await self.user_repo.update(
                user.id,
                {"status": UserStatus.ACTIVE}
            )

        await self.email_service.send_doctor_approval_email(
            user.email,
            user.full_name,
            "APPROVED"
        )

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
        """
        Reject a pending doctor: marks the doctor profile and user account
        accordingly, emails the doctor with the reason, and logs the action
        for audit purposes.
        """
        doctor = await self.doctor_repo.find_by_id(doctor_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1301)

        if doctor.status != DoctorStatus.PENDING:
            raise ValueError(f"Doctor is already {doctor.status.value}")

        updated_doctor = await self.doctor_repo.update_status(
            doctor_id,
            DoctorStatus.REJECTED,
            admin_id,
            reject_data.reason
        )

        if not updated_doctor:
            raise ValueError(ErrorMessages.DOC_1301)

        user = await self.user_repo.find_by_id(doctor.user_id)
        if user:
            await self.user_repo.update(
                user.id,
                {"status": UserStatus.INACTIVE}
            )

        await self.email_service.send_doctor_approval_email(
            user.email,
            user.full_name,
            "REJECTED",
            reject_data.reason
        )

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
        """Count doctors in each status bucket for the admin dashboard summary."""
        try:
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

    async def search_doctors(
        self,
        query: Optional[str] = None,
        specialization: Optional[str] = None,
        location: Optional[str] = None,
        min_experience: Optional[int] = None,
        max_fee: Optional[float] = None,
        min_rating: Optional[float] = None,
        limit: int = 20,
        skip: int = 0
    ) -> Dict[str, Any]:
        """
        Search approved doctors in memory against the given filters (name/
        specialization/qualification text match, location, experience, fee
        cap, rating floor), then paginate the results.
        """
        try:
            doctors = await self.doctor_repo.get_doctors_by_status(DoctorStatus.APPROVED)

            filtered_doctors = []

            for doctor in doctors:
                user = await self.user_repo.find_by_id(doctor.user_id)
                if not user:
                    continue

                if query:
                    query_lower = query.lower()
                    if query_lower not in user.full_name.lower() and \
                       query_lower not in doctor.specialization.value.lower() and \
                       query_lower not in doctor.qualification.lower():
                        continue

                if specialization and doctor.specialization.value != specialization:
                    continue

                if location and location.lower() not in doctor.clinic_address.lower():
                    continue

                if min_experience and doctor.experience_years < min_experience:
                    continue

                if max_fee and doctor.consultation_fee > max_fee:
                    continue

                if min_rating and doctor.rating < min_rating:
                    continue

                filtered_doctors.append({
                    "id": doctor.id,
                    "full_name": user.full_name,
                    "qualification": doctor.qualification,
                    "specialization": doctor.specialization,
                    "experience_years": doctor.experience_years,
                    "consultation_fee": doctor.consultation_fee,
                    "clinic_address": doctor.clinic_address,
                    "clinic_phone": doctor.clinic_phone,
                    "bio": doctor.bio,
                    "profile_picture": doctor.profile_picture,
                    "rating": doctor.rating,
                    "total_reviews": doctor.total_reviews,
                    "is_available": True
                })

            total = len(filtered_doctors)
            paginated = filtered_doctors[skip:skip + limit]

            return {
                "doctors": paginated,
                "total": total,
                "skip": skip,
                "limit": limit,
                "has_more": skip + limit < total
            }

        except Exception as e:
            logger.error(f"Error searching doctors: {e}")
            return {
                "doctors": [],
                "total": 0,
                "skip": skip,
                "limit": limit,
                "has_more": False
            }
