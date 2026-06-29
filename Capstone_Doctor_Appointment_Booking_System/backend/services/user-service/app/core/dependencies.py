from fastapi import Depends, HTTPException, Security
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from typing import List
from app.services.auth_service import AuthService
from shared.constants import HttpStatus, ErrorMessages
from shared.constants.roles import UserRole, Permission
from .rbac import RBAC
import logging

security = HTTPBearer()
logger = logging.getLogger(__name__)

async def get_current_user(
    credentials: HTTPAuthorizationCredentials = Security(security)
) -> dict:
    """Get current authenticated user from JWT token"""
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

def require_permission(permission: Permission):
    """Decorator factory for permission-based access control"""
    def dependency(current_user: dict = Depends(get_current_user)):
        user_role = current_user.get("role")
        if not RBAC.has_permission(user_role, permission):
            raise HTTPException(
                status_code=HttpStatus.FORBIDDEN,
                detail=f"Permission '{permission.value}' required"
            )
        return current_user
    return dependency

def require_any_permission(permissions: List[Permission]):
    """Decorator factory for OR permission-based access control"""
    def dependency(current_user: dict = Depends(get_current_user)):
        user_role = current_user.get("role")
        if not RBAC.has_any_permission(user_role, permissions):
            raise HTTPException(
                status_code=HttpStatus.FORBIDDEN,
                detail=f"Any of these permissions required: {[p.value for p in permissions]}"
            )
        return current_user
    return dependency

def require_role(role: UserRole):
    """Decorator factory for role-based access control"""
    def dependency(current_user: dict = Depends(get_current_user)):
        user_role = current_user.get("role")
        if user_role != role.value:
            raise HTTPException(
                status_code=HttpStatus.FORBIDDEN,
                detail=f"Role '{role.value}' required"
            )
        return current_user
    return dependency

def require_any_role(roles: List[UserRole]):
    """Decorator factory for OR role-based access control"""
    def dependency(current_user: dict = Depends(get_current_user)):
        user_role = current_user.get("role")
        if user_role not in [r.value for r in roles]:
            raise HTTPException(
                status_code=HttpStatus.FORBIDDEN,
                detail=f"Any of these roles required: {[r.value for r in roles]}"
            )
        return current_user
    return dependency

# Role-specific dependencies
get_current_patient = require_role(UserRole.PATIENT)
get_current_doctor = require_role(UserRole.DOCTOR)
get_current_admin = require_role(UserRole.ADMIN)

# Permission-based dependencies
can_view_doctors = require_permission(Permission.VIEW_DOCTORS)
can_book_appointment = require_permission(Permission.BOOK_APPOINTMENT)
can_manage_availability = require_permission(Permission.MANAGE_AVAILABILITY)
can_manage_users = require_permission(Permission.MANAGE_USERS)
