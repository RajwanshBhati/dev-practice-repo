from fastapi import APIRouter, HTTPException, Depends
from app.schemas.auth import PatientRegister, DoctorRegister, UserLogin
from app.services.auth_service import AuthService
from shared.constants import HttpStatus
import logging

router = APIRouter(prefix="/api/v1/auth", tags=["authentication"])
logger = logging.getLogger(__name__)

@router.post("/register/patient")
async def register_patient(user_data: PatientRegister):
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
    try:
        auth_service = AuthService()
        result = await auth_service.register_doctor(user_data)
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
    try:
        auth_service = AuthService()
        result = await auth_service.login(login_data)
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.UNAUTHORIZED, detail=str(e))
    except Exception as e:
        logger.error(f"Login error: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Login failed"
        )

@router.post("/logout")
async def logout():
    return {"message": "Logged out successfully"}

@router.post("/validate-token")
async def validate_token(current_user: dict = Depends(get_current_user)):
    """
    Validate JWT token and return user info
    """
    return {
        "valid": True,
        "user": current_user
    }

@router.post("/refresh-token")
async def refresh_token(refresh_data: RefreshToken):
    """
    Refresh access token using refresh token

    - **refresh_token**: Valid refresh token
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
