from fastapi import APIRouter, HTTPException, Depends
from typing import List
from backend.database.dependencies import (
    get_current_user,
    get_current_patient,
    get_current_doctor,
    get_current_admin,
    require_permission,
    require_any_permission,
    Permission,
    UserRole
)
from backend.services.user_service import UserService
from backend.constants import HttpStatus
import logging

router = APIRouter(prefix="/api/v1/protected", tags=["protected"])

logger = logging.getLogger(__name__)

# Endpoint accessible by any authenticated user
@router.get("/profile", tags=["protected"])
async def get_profile(current_user: dict = Depends(get_current_user)):
    """Get user profile (any authenticated user)"""
    try:
        user_service = UserService()
        user = await user_service.get_user_by_id(current_user["user_id"])
        return {
            "id": user.id,
            "email": user.email,
            "full_name": user.full_name,
            "role": user.role.value,
            "status": user.status.value
        }
    except Exception as e:
        logger.error(f"Error getting profile: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to get profile"
        )

# Endpoint accessible only by patients
@router.get("/patient/dashboard", tags=["protected"])
async def patient_dashboard(current_user: dict = Depends(get_current_patient)):
    """Patient dashboard (patient only)"""
    return {
        "message": "Welcome to patient dashboard",
        "user": current_user
    }

# Endpoint accessible only by doctors
@router.get("/doctor/dashboard", tags=["protected"])
async def doctor_dashboard(current_user: dict = Depends(get_current_doctor)):
    """Doctor dashboard (doctor only)"""
    return {
        "message": "Welcome to doctor dashboard",
        "user": current_user
    }

# Endpoint accessible only by admins
@router.get("/admin/dashboard", tags=["protected"])
async def admin_dashboard(current_user: dict = Depends(get_current_admin)):
    """Admin dashboard (admin only)"""
    return {
        "message": "Welcome to admin dashboard",
        "user": current_user
    }

# Endpoint with permission-based access
@router.get("/doctors", tags=["protected"])
async def view_doctors(current_user: dict = Depends(require_permission(Permission.VIEW_DOCTORS))):
    """View doctors (requires VIEW_DOCTORS permission)"""
    return {
        "message": "List of doctors",
        "permission": "VIEW_DOCTORS"
    }

# Endpoint with multiple permission check
@router.post("/appointments", tags=["protected"])
async def create_appointment(
    current_user: dict = Depends(require_any_permission([
        Permission.BOOK_APPOINTMENT,
        Permission.MANAGE_SYSTEM
    ]))
):
    """Create appointment (requires BOOK_APPOINTMENT or MANAGE_SYSTEM)"""
    return {
        "message": "Appointment created",
        "permissions": ["BOOK_APPOINTMENT", "MANAGE_SYSTEM"]
    }

# Admin only user management
@router.get("/admin/users", tags=["protected"])
async def get_all_users(current_user: dict = Depends(get_current_admin)):
    """Get all users (admin only)"""
    try:
        return {"message": "List of all users"}
    except Exception as e:
        logger.error(f"Error getting users: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to get users"
        )

@router.put("/admin/users/{user_id}/activate", tags=["protected"])
async def activate_user(
    user_id: str,
    current_user: dict = Depends(require_permission(Permission.MANAGE_USERS))
):
    """Activate user (requires MANAGE_USERS permission)"""
    try:
        user_service = UserService()
        await user_service.activate_user(user_id)
        return {
            "message": "User activated successfully",
            "user_id": user_id
        }
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.NOT_FOUND, detail=str(e))
    except Exception as e:
        logger.error(f"Error activating user: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to activate user"
        )

@router.put("/admin/users/{user_id}/deactivate", tags=["protected"])
async def deactivate_user(
    user_id: str,
    current_user: dict = Depends(require_permission(Permission.MANAGE_USERS))
):
    """Deactivate user (requires MANAGE_USERS permission)"""
    try:
        user_service = UserService()
        await user_service.deactivate_user(user_id)
        return {
            "message": "User deactivated successfully",
            "user_id": user_id
        }
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.NOT_FOUND, detail=str(e))
    except Exception as e:
        logger.error(f"Error deactivating user: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to deactivate user"
        )
