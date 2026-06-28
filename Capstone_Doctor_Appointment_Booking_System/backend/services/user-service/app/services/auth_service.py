from typing import Dict, Any
from datetime import datetime
from app.core.security import security
from app.core.jwt_service import jwt_service
from app.repositories.user_repository import UserRepository
from app.repositories.token_blacklist_repository import TokenBlacklistRepository
from app.models.user import User
from app.models.profile import PatientProfile, DoctorProfile
from app.schemas.auth import PatientRegister, DoctorRegister, UserLogin, RefreshToken
from shared.constants import ErrorMessages, SuccessMessages
from shared.constants.roles import UserRole
from shared.enums.user_enums import UserStatus
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
            "expires_in": settings.ACCESS_TOKEN_EXPIRE_MINUTES * 60,
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
        # TODO: Save doctor profile

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
            "expires_in": settings.ACCESS_TOKEN_EXPIRE_MINUTES * 60,
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
            "expires_in": settings.ACCESS_TOKEN_EXPIRE_MINUTES * 60,
            "user": {
                "id": user.id,
                "email": user.email,
                "full_name": user.full_name,
                "role": user.role.value,
                "status": user.status.value
            }
        }

    async def refresh_token(self, refresh_token_data: RefreshToken) -> Dict[str, Any]:
        """Refresh access token using refresh token"""
        try:
            # Check if refresh token is blacklisted
            is_blacklisted = await self.blacklist_repo.is_blacklisted(
                refresh_token_data.refresh_token
            )
            if is_blacklisted:
                raise ValueError("Refresh token has been revoked")

            # Generate new tokens
            new_tokens = jwt_service.refresh_access_token(
                refresh_token_data.refresh_token
            )

            # Blacklist old refresh token
            try:
                payload = jwt_service.decode_token(refresh_token_data.refresh_token)
                await self.blacklist_repo.add_to_blacklist(
                    refresh_token_data.refresh_token,
                    payload.get("sub"),
                    datetime.fromtimestamp(payload.get("exp"))
                )
            except:
                pass

            return new_tokens
        except ValueError as e:
            raise
        except Exception as e:
            logger.error(f"Token refresh error: {str(e)}")
            raise ValueError("Invalid refresh token")

    async def logout(self, user_id: str, access_token: str) -> Dict[str, Any]:
        """Logout user by blacklisting tokens"""
        try:
            # Decode token to get expiry
            payload = jwt_service.decode_token(access_token)
            expires_at = datetime.fromtimestamp(payload.get("exp"))

            # Add token to blacklist
            await self.blacklist_repo.add_to_blacklist(
                access_token,
                user_id,
                expires_at
            )

            logger.info(f"User logged out: {user_id}")
            return {"message": SuccessMessages.LOGOUT_SUCCESS}
        except Exception as e:
            logger.error(f"Logout error: {str(e)}")
            raise

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
