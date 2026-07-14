from fastapi import APIRouter, HTTPException, Depends
from backend.schemas.request.user_request import (
    PatientRegister,
    DoctorRegister
)

from backend.schemas.request.auth_request import (
    UserLogin,
    RefreshToken,
    LogoutRequest
)
from pydantic import BaseModel, EmailStr


class ForgotPasswordRequest(BaseModel):
    email: EmailStr


class ResetPasswordRequest(BaseModel):
    token: str
    password: str
from backend.services.auth_service import AuthService
from backend.database.dependencies import get_current_user
from backend.constants import HttpStatus
import logging

router = APIRouter(prefix="/api/v1/auth", tags=["authentication"])
logger = logging.getLogger(__name__)


@router.post("/register/patient")
async def register_patient(user_data: PatientRegister):
    """Register a new patient account. Patients are active immediately, no approval needed."""
    try:
        auth_service = AuthService()
        result = await auth_service.register_patient(user_data)
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Patient registration error: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Registration failed"
        )


@router.post("/register/doctor")
async def register_doctor(user_data: DoctorRegister):
    """
    Register a new doctor account. The account starts in PENDING status
    and can't log in until an admin approves it.
    """
    try:
        auth_service = AuthService()
        result = await auth_service.register_doctor_with_approval(user_data)
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Doctor registration error: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Registration failed"
        )


@router.post("/login")
async def login(login_data: UserLogin):
    """
    Authenticate a user and issue tokens. For doctors, this also checks
    their approval status before letting them in.
    """
    try:
        auth_service = AuthService()
        result = await auth_service.login_with_status_check(login_data)
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.UNAUTHORIZED, detail=str(e))
    except Exception as e:
        logger.error(f"Login error: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Login failed"
        )


@router.post("/refresh-token")
async def refresh_token(refresh_data: RefreshToken):
    """Exchange a valid refresh token for a new access token, without requiring the user to log in again."""
    try:
        auth_service = AuthService()
        result = await auth_service.refresh_token(refresh_data)
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.UNAUTHORIZED, detail=str(e))
    except Exception as e:
        logger.error(f"Token refresh error: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to refresh token"
        )


@router.post("/logout")
async def logout(
    logout_data: LogoutRequest,
    current_user: dict = Depends(get_current_user)
):
    """Log the user out by blacklisting their access token so it can't be reused."""
    try:
        auth_service = AuthService()
        result = await auth_service.logout(
            current_user["user_id"],
            logout_data.access_token
        )
        return result
    except Exception as e:
        logger.error(f"Logout error: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Logout failed"
        )


@router.post("/forgot-password")
async def forgot_password(data: ForgotPasswordRequest):
    """Send a password reset link to the given email if an account exists."""
    try:
        auth_service = AuthService()
        result = await auth_service.forgot_password(data.email)
        return result
    except Exception as e:
        logger.error(f"Forgot password error: {str(e)}")
        # Still return a generic success response to avoid leaking user existence
        return {"message": "If an account exists for this email, a reset link has been sent."}


@router.post("/reset-password")
async def reset_password(data: ResetPasswordRequest):
    """Reset a user's password using the token they received via email."""
    try:
        auth_service = AuthService()
        result = await auth_service.reset_password(data.token, data.password)
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Reset password error: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to reset password"
        )


@router.post("/validate-token")
async def validate_token(current_user: dict = Depends(get_current_user)):
    """Simple endpoint to check whether the caller's token is still valid, and return the decoded user info."""
    return {
        "valid": True,
        "user": current_user
    }
