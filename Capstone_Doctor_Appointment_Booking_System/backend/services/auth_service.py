import token
from typing import Dict, Any
from datetime import datetime, timezone
from backend.middleware.security import security
from backend.middleware.jwt_service import jwt_service
from backend.repositories.user_repository import UserRepository
from backend.repositories.token_blacklist_repository import TokenBlacklistRepository
from backend.models.user import User
from backend.models.profile import PatientProfile, DoctorProfile
from backend.schemas.request.user_request import (
    PatientRegister,
    DoctorRegister
)

from backend.schemas.request.auth_request import UserLogin, RefreshToken
from backend.constants import ErrorMessages, SuccessMessages
from backend.constants.roles import UserRole
from backend.enums.user_enums import UserStatus, DoctorStatus
import logging

logger = logging.getLogger(__name__)

class AuthService:
    """Core auth logic: registering patients and doctors, logging in, validating tokens, and logging out."""

    def __init__(self):
        self.user_repo = UserRepository()
        self.blacklist_repo = TokenBlacklistRepository()

    async def register_patient(self, user_data: PatientRegister) -> Dict[str, Any]:
        """Create a new patient account and immediately log them in by issuing tokens."""
        existing_user = await self.user_repo.find_by_email(user_data.email)
        if existing_user:
            raise ValueError(ErrorMessages.USER_1102)

        password_hash = security.hash_password(user_data.password)

        user = User(
            email=user_data.email,
            password_hash=password_hash,
            full_name=user_data.full_name,
            phone=user_data.phone,
            gender=user_data.gender,
            date_of_birth=user_data.date_of_birth,
            role=UserRole.PATIENT,
            status=UserStatus.ACTIVE
        )

        created_user = await self.user_repo.create(user)

        patient_profile = PatientProfile(
            user_id=created_user.id
        )

        token_data = {
            "sub": created_user.id,
            "email": created_user.email,
            "role": created_user.role.value
        }
        access_token = jwt_service.create_access_token(token_data)
        refresh_token = jwt_service.create_refresh_token(token_data)

        logger.info(f"Patient registered: {created_user.email}")

        return {
            "message": SuccessMessages.REGISTRATION_SUCCESS,
            "access_token": access_token,
            "refresh_token": refresh_token,
            "token_type": "bearer",
            "expires_in": 1800,
            "user": {
                "id": created_user.id,
                "email": created_user.email,
                "full_name": created_user.full_name,
                "role": created_user.role.value,
                "status": created_user.status.value
            }
        }

    async def register_doctor(self, user_data: DoctorRegister) -> Dict[str, Any]:
        """
        Create a new doctor account and log them in right away. Unlike
        register_doctor_with_approval, this skips the admin approval step,
        so it's mainly useful for testing or seeding data.
        """
        existing_user = await self.user_repo.find_by_email(user_data.email)
        if existing_user:
            raise ValueError(ErrorMessages.USER_1102)

        password_hash = security.hash_password(user_data.password)

        user = User(
            email=user_data.email,
            password_hash=password_hash,
            full_name=user_data.full_name,
            phone=user_data.phone,
            gender=user_data.gender,
            date_of_birth=user_data.date_of_birth,
            role=UserRole.DOCTOR,
            status=UserStatus.ACTIVE
        )

        created_user = await self.user_repo.create(user)

        doctor_profile = DoctorProfile(
            user_id=created_user.id,
            qualification=user_data.qualification,
            specialization=user_data.specialization,
            experience_years=user_data.experience_years,
            license_number=user_data.license_number,
            consultation_fee=user_data.consultation_fee,
            clinic_address=user_data.clinic_address,
            bio=user_data.bio
        )

        token_data = {
            "sub": created_user.id,
            "email": created_user.email,
            "role": created_user.role.value
        }
        access_token = jwt_service.create_access_token(token_data)
        refresh_token = jwt_service.create_refresh_token(token_data)

        logger.info(f"Doctor registered: {created_user.email}")

        return {
            "message": SuccessMessages.REGISTRATION_SUCCESS,
            "access_token": access_token,
            "refresh_token": refresh_token,
            "token_type": "bearer",
            "expires_in": 1800,
            "user": {
                "id": created_user.id,
                "email": created_user.email,
                "full_name": created_user.full_name,
                "role": created_user.role.value,
                "status": created_user.status.value
            }
        }

    async def login(self, login_data: UserLogin) -> Dict[str, Any]:
        """Verify email/password and issue a fresh pair of tokens. Simpler variant without doctor-approval checks."""
        user = await self.user_repo.find_by_email(login_data.email)

        if not user:
            raise ValueError(ErrorMessages.AUTH_1001)

        if not security.verify_password(login_data.password, user.password_hash):
            raise ValueError(ErrorMessages.AUTH_1001)

        if user.status != UserStatus.ACTIVE:
            raise ValueError(ErrorMessages.AUTH_1004)

        await self.user_repo.update_last_login(user.id)

        token_data = {
            "sub": user.id,
            "email": user.email,
            "role": user.role.value
        }
        access_token = jwt_service.create_access_token(token_data)
        refresh_token = jwt_service.create_refresh_token(token_data)

        logger.info(f"User logged in: {user.email}")

        return {
            "message": SuccessMessages.LOGIN_SUCCESS,
            "access_token": access_token,
            "refresh_token": refresh_token,
            "token_type": "bearer",
            "expires_in": 1800,
            "user": {
                "id": user.id,
                "email": user.email,
                "full_name": user.full_name,
                "role": user.role.value,
                "status": user.status.value
            }
        }

    async def validate_token(self, token: str) -> Dict[str, Any]:
        """
        Decode a JWT, reject it if it's blacklisted or the user is no
        longer active, and return the minimal user info other parts of the
        app need for authorization checks.
        """
        try:
            is_blacklisted = await self.blacklist_repo.is_blacklisted(token)
            if is_blacklisted:
                raise ValueError("Token has been revoked")

            payload = jwt_service.decode_token(token)
            user_id = payload.get("sub")

            if not user_id:
                raise ValueError(ErrorMessages.AUTH_1003)

            user = await self.user_repo.find_by_id(user_id)
            if not user:
                raise ValueError(ErrorMessages.USER_1101)

            if user.status != UserStatus.ACTIVE:
                raise ValueError(ErrorMessages.AUTH_1004)

            return {
                "user_id": user.id,
                "email": user.email,
                "role": user.role.value,
                "full_name": user.full_name
            }
        except ValueError as e:
            raise
        except Exception as e:
            logger.error(f"Token validation error: {str(e)}")
            raise ValueError(ErrorMessages.AUTH_1003)


    async def refresh_token(self, refresh_data: RefreshToken) -> Dict[str, Any]:
        """
        Validate a refresh token, check if it's blacklisted, and issue a
        new access token if everything checks out.
        """
        token = refresh_data.refresh_token

        is_blacklisted = await self.blacklist_repo.is_blacklisted(token)
        if is_blacklisted:
           raise ValueError("Token has been revoked")

        try:
            payload = jwt_service.decode_token(token)
        except ValueError:
            raise ValueError(ErrorMessages.AUTH_1003)

        if payload.get("type") != "refresh":
            raise ValueError(ErrorMessages.AUTH_1003)

        user_id = payload.get("sub")
        if not user_id:
            raise ValueError(ErrorMessages.AUTH_1003)

        user = await self.user_repo.find_by_id(user_id)
        if not user:
            raise ValueError(ErrorMessages.USER_1101)

        if user.status != UserStatus.ACTIVE:
            raise ValueError(ErrorMessages.AUTH_1004)

        token_data = {
            "sub": user.id,
            "email": user.email,
            "role": user.role.value
        }
        access_token = jwt_service.create_access_token(token_data)

        return {
            "access_token": access_token,
            "token_type": "bearer",
            "expires_in": 1800
        }

    async def register_doctor_with_approval(self, user_data: DoctorRegister) -> Dict[str, Any]:
        """
        Create a doctor account that starts out PENDING on both the user
        and doctor profile, so they can't log in until an admin approves
        them. Sends a confirmation email once registration is done.
        """
        existing_user = await self.user_repo.find_by_email(user_data.email)
        if existing_user:
            raise ValueError(ErrorMessages.USER_1102)

        password_hash = security.hash_password(user_data.password)

        user = User(
            email=user_data.email,
            password_hash=password_hash,
            full_name=user_data.full_name,
            phone=user_data.phone,
            gender=user_data.gender,
            date_of_birth=user_data.date_of_birth,
            role=UserRole.DOCTOR,
            status=UserStatus.PENDING
        )

        created_user = await self.user_repo.create(user)

        doctor_profile = DoctorProfile(
            user_id=created_user.id,
            qualification=user_data.qualification,
            specialization=user_data.specialization,
            experience_years=user_data.experience_years,
            license_number=user_data.license_number,
            consultation_fee=user_data.consultation_fee,
            clinic_address=user_data.clinic_address,
            bio=user_data.bio,
            status=DoctorStatus.PENDING
        )

        from backend.repositories.doctor_repository import DoctorRepository
        doctor_repo = DoctorRepository()
        await doctor_repo.create(doctor_profile)

        from .email_service import EmailService
        await EmailService.send_account_created_email(
            created_user.email,
            created_user.full_name,
            "Doctor"
        )

        logger.info(f"Doctor registered with pending approval: {created_user.email}")

        return {
            "message": SuccessMessages.DOCTOR_REGISTRATION_PENDING,
            "user": {
                "id": created_user.id,
                "email": created_user.email,
                "full_name": created_user.full_name,
                "role": created_user.role.value,
                "status": created_user.status.value,
                "doctor_status": doctor_profile.status.value
            }
        }

    async def login_with_status_check(self, login_data: UserLogin) -> Dict[str, Any]:
        user = await self.user_repo.find_by_email(login_data.email)

        if not user:
            raise ValueError(ErrorMessages.AUTH_1001)

        if not security.verify_password(login_data.password, user.password_hash):
            raise ValueError(ErrorMessages.AUTH_1001)

        if user.status == UserStatus.PENDING:
            raise ValueError(ErrorMessages.AUTH_1006)
        elif user.status == UserStatus.INACTIVE:
            raise ValueError(ErrorMessages.AUTH_1004)
        elif user.status == UserStatus.SUSPENDED:
            raise ValueError("Account has been suspended")

        if user.role == UserRole.DOCTOR:
            from backend.repositories.doctor_repository import DoctorRepository
            doctor_repo = DoctorRepository()
            doctor = await doctor_repo.find_by_user_id(user.id)
            if doctor:
                if doctor.status == DoctorStatus.PENDING:
                    raise ValueError(ErrorMessages.AUTH_1006)
                elif doctor.status == DoctorStatus.REJECTED:
                    raise ValueError(ErrorMessages.AUTH_1007)
                elif doctor.status == DoctorStatus.SUSPENDED:
                    raise ValueError("Doctor account has been suspended")

        await self.user_repo.update_last_login(user.id)

        token_data = {
            "sub": user.id,
            "email": user.email,
            "role": user.role.value
        }
        access_token = jwt_service.create_access_token(token_data)
        refresh_token = jwt_service.create_refresh_token(token_data)

        logger.info(f"User logged in: {user.email}")

        return {
            "message": SuccessMessages.LOGIN_SUCCESS,
            "access_token": access_token,
            "refresh_token": refresh_token,
            "token_type": "bearer",
            "expires_in": 1800,
            "user": {
                "id": user.id,
                "email": user.email,
                "phone": user.phone,
                "full_name": user.full_name,
                "role": user.role.value,
                "status": user.status.value
            }
        }

    async def forgot_password(self, email: str) -> Dict[str, Any]:
        """
        Send a password reset email if an active account exists for this
        email.
        """
        user = await self.user_repo.find_by_email(email)

        if user and user.status == UserStatus.ACTIVE:
            token_data = {"sub": user.id, "email": user.email}
            reset_token = jwt_service.create_reset_token(token_data)

            from .email_service import EmailService
            await EmailService.send_password_reset_email(
                user.email,
                user.full_name,
                reset_token
            )
            logger.info(f"Password reset email sent to: {user.email}")

        return {
            "message": "If an account exists for this email, a reset link has been sent."
        }

    async def reset_password(self, token: str, new_password: str) -> Dict[str, Any]:
        """Validate a reset token and set the new password hash for that user."""
        try:
            payload = jwt_service.decode_token(token)
        except ValueError:
            raise ValueError("Reset link is invalid or has expired")

        if payload.get("type") != "reset":
            raise ValueError("Reset link is invalid or has expired")

        user_id = payload.get("sub")
        if not user_id:
            raise ValueError("Reset link is invalid or has expired")

        user = await self.user_repo.find_by_id(user_id)
        if not user:
            raise ValueError(ErrorMessages.USER_1101)

        password_hash = security.hash_password(new_password)
        updated_user = await self.user_repo.update(user.id, {"password_hash": password_hash})
        if not updated_user:
            raise ValueError("Failed to reset password")

        logger.info(f"Password reset successful for: {user.email}")

        return {"message": "Password has been reset successfully. Please login with your new password."}

    async def logout(self, user_id: str, access_token: str) -> Dict[str, Any]:
        """Invalidate the given access token immediately by adding it to the blacklist until its natural expiry."""
        try:
            payload = jwt_service.decode_token(access_token)

            exp = payload.get("exp")

            if not exp:
                raise ValueError("Invalid token")

            expires_at = datetime.fromtimestamp(exp, tz=timezone.utc)

            success = await self.blacklist_repo.add_to_blacklist(
                token=access_token,
                user_id=user_id,
                expires_at=expires_at
            )

            if not success:
                raise ValueError("Failed to blacklist token")

            logger.info(f"User logged out successfully: {user_id}")

            return {
                "message": "Logout successful"
            }

        except Exception as e:
            logger.error(f"Logout service error: {str(e)}")
            raise
