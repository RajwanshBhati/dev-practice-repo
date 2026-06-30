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
from backend.services.auth_service import AuthService
from backend.database.dependencies import get_current_user
from backend.constants import HttpStatus
import logging

router = APIRouter(prefix="/api/v1/auth", tags=["authentication"])
logger = logging.getLogger(__name__)

@router.post("/register/patient")
async def register_patient(user_data: PatientRegister):
    """
    Register a new patient
    """
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
    Register a new doctor (requires admin approval)

    After registration, doctor account will be in PENDING state.
    Admin needs to approve the doctor before they can login.
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
    Login user with status check

    For doctors: Will check if account is approved before allowing login
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
    """
    Refresh access token using refresh token
    """
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
    """
    Logout user by invalidating tokens
    """
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

@router.post("/validate-token")
async def validate_token(current_user: dict = Depends(get_current_user)):
    """
    Validate JWT token and return user info
    """
    return {
        "valid": True,
        "user": current_user
    }
