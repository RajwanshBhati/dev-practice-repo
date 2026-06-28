from fastapi import Depends, HTTPException, Security
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from app.services.auth_service import AuthService
from shared.constants import HttpStatus, ErrorMessages
import logging

security = HTTPBearer()
logger = logging.getLogger(__name__)

async def get_current_user(
    credentials: HTTPAuthorizationCredentials = Security(security)
) -> dict:
    """
    Get current authenticated user from JWT token
    """
    try:
        auth_service = AuthService()
        user_info = await auth_service.validate_token(credentials.credentials)
        return user_info
    except ValueError as e:
        raise HTTPException(
            status_code=HttpStatus.UNAUTHORIZED,
            detail=str(e)
        )
    except Exception as e:
        logger.error(f"Authentication error: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.UNAUTHORIZED,
            detail=ErrorMessages.AUTH_1003
        )

async def get_current_patient(
    current_user: dict = Depends(get_current_user)
) -> dict:
    """Get current authenticated patient"""
    if current_user["role"] != "PATIENT":
        raise HTTPException(
            status_code=HttpStatus.FORBIDDEN,
            detail="Access denied. Patient role required."
        )
    return current_user

async def get_current_doctor(
    current_user: dict = Depends(get_current_user)
) -> dict:
    """Get current authenticated doctor"""
    if current_user["role"] != "DOCTOR":
        raise HTTPException(
            status_code=HttpStatus.FORBIDDEN,
            detail="Access denied. Doctor role required."
        )
    return current_user

async def get_current_admin(
    current_user: dict = Depends(get_current_user)
) -> dict:
    """Get current authenticated admin"""
    if current_user["role"] != "ADMIN":
        raise HTTPException(
            status_code=HttpStatus.FORBIDDEN,
            detail="Access denied. Admin role required."
        )
    return current_user
