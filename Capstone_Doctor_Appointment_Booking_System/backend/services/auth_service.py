from typing import Dict, Any
from datetime import datetime
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

from backend.schemas.request.auth_request import UserLogin
from backend.constants import ErrorMessages, SuccessMessages
from backend.constants.roles import UserRole
from backend.enums.user_enums import UserStatus, DoctorStatus
import logging

logger = logging.getLogger(__name__)

class AuthService:
    """Authentication service for user registration, login, and token management"""

    def __init__(self):
        self.user_repo = UserRepository()
        self.blacklist_repo = TokenBlacklistRepository()

    async def register_patient(self, user_data: PatientRegister) -> Dict[str, Any]:
        """Register a new patient"""
        # Check if user exists
        existing_user = await self.user_repo.find_by_email(user_data.email)
        if existing_user:
            raise ValueError(ErrorMessages.USER_1102)

        # Hash password
        password_hash = security.hash_password(user_data.password)

        # Create user
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

        # Create patient profile
        patient_profile = PatientProfile(
            user_id=created_user.id
        )
        # TODO: Save patient profile

        # Generate tokens
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
        """Register a new doctor"""
        # Check if user exists
        existing_user = await self.user_repo.find_by_email(user_data.email)
        if existing_user:
            raise ValueError(ErrorMessages.USER_1102)

        # Hash password
        password_hash = security.hash_password(user_data.password)

        # Create user
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

        # Create doctor profile
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

        # Generate tokens
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
        """Authenticate user and generate access token"""
        user = await self.user_repo.find_by_email(login_data.email)

        if not user:
            raise ValueError(ErrorMessages.AUTH_1001)

        if not security.verify_password(login_data.password, user.password_hash):
            raise ValueError(ErrorMessages.AUTH_1001)

        if user.status != UserStatus.ACTIVE:
            raise ValueError(ErrorMessages.AUTH_1004)

        # Update last login
        await self.user_repo.update_last_login(user.id)

        # Generate tokens
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
        """Validate JWT token and return user information"""
        try:
            # Check if token is blacklisted
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

    async def register_doctor_with_approval(self, user_data: DoctorRegister) -> Dict[str, Any]:
        """Register a new doctor with pending approval"""
        # Check if user exists
        existing_user = await self.user_repo.find_by_email(user_data.email)
        if existing_user:
            raise ValueError(ErrorMessages.USER_1102)

        # Hash password
        password_hash = security.hash_password(user_data.password)

        # Create user with PENDING status
        user = User(
            email=user_data.email,
            password_hash=password_hash,
            full_name=user_data.full_name,
            phone=user_data.phone,
            gender=user_data.gender,
            date_of_birth=user_data.date_of_birth,
            role=UserRole.DOCTOR,
            status=UserStatus.PENDING  # Pending approval
        )

        created_user = await self.user_repo.create(user)

        # Create doctor profile with PENDING status
        doctor_profile = DoctorProfile(
            user_id=created_user.id,
            qualification=user_data.qualification,
            specialization=user_data.specialization,
            experience_years=user_data.experience_years,
            license_number=user_data.license_number,
            consultation_fee=user_data.consultation_fee,
            clinic_address=user_data.clinic_address,
            bio=user_data.bio,
            status=DoctorStatus.PENDING  # Pending approval
        )

        # Save doctor profile
        from backend.repositories.doctor_repository import DoctorRepository
        doctor_repo = DoctorRepository()
        await doctor_repo.create(doctor_profile)

        # Send account created email
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
        """Login with status check for approval"""
        user = await self.user_repo.find_by_email(login_data.email)

        if not user:
            raise ValueError(ErrorMessages.AUTH_1001)

        if not security.verify_password(login_data.password, user.password_hash):
            raise ValueError(ErrorMessages.AUTH_1001)

        # Check user status
        if user.status == UserStatus.PENDING:
            raise ValueError(ErrorMessages.AUTH_1006)
        elif user.status == UserStatus.INACTIVE:
            raise ValueError(ErrorMessages.AUTH_1004)
        elif user.status == UserStatus.SUSPENDED:
            raise ValueError("Account has been suspended")

        # If doctor, check doctor status
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

        # Update last login
        await self.user_repo.update_last_login(user.id)

        # Generate tokens
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
