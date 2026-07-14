from fastapi import APIRouter, HTTPException, Depends
from backend.services.user_service import UserService
from backend.database.dependencies import get_current_user, get_current_admin
from backend.constants import HttpStatus
import logging

router = APIRouter(prefix="/api/v1/users", tags=["users"])
logger = logging.getLogger(__name__)


@router.get("/profile")
async def get_current_user_info(current_user: dict = Depends(get_current_user)):
    """Return full profile details for whoever is currently logged in."""
    try:
        user_service = UserService()
        user = await user_service.get_user_by_id(current_user["user_id"])
        return {
            "id": user.id,
            "email": user.email,
            "full_name": user.full_name,
            "phone": user.phone,
            "gender": user.gender.value,
            "role": user.role.value,
            "status": user.status.value,
            "is_verified": user.is_verified,
            "created_at": user.created_at
        }
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.NOT_FOUND, detail=str(e))
    except Exception as e:
        logger.error(f"Error getting user info: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to get user information"
        )


@router.put("/me")
async def update_current_user(
    update_data: dict,
    current_user: dict = Depends(get_current_user)
):
    """Let the logged-in user update their own profile fields.

    Only a safe allow-list of fields can be changed here (full_name, phone).
    Sensitive fields like role/status/email/password are intentionally
    ignored even if present in the payload, to prevent privilege escalation.
    """
    try:
        ALLOWED_FIELDS = {"full_name", "phone"}
        safe_update_data = {
            key: value for key, value in update_data.items()
            if key in ALLOWED_FIELDS and value not in (None, "")
        }

        if not safe_update_data:
            raise HTTPException(
                status_code=HttpStatus.BAD_REQUEST,
                detail="No valid fields to update. Allowed fields: full_name, phone"
            )

        user_service = UserService()
        updated_user = await user_service.update_user(
            current_user["user_id"],
            safe_update_data
        )
        return {
            "message": "Profile updated successfully",
            "user": {
                "id": updated_user.id,
                "email": updated_user.email,
                "full_name": updated_user.full_name,
                "phone": updated_user.phone
            }
        }
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Error updating user: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to update user"
        )


@router.get("/{user_id}")
async def get_user_by_id(
    user_id: str,
    current_user: dict = Depends(get_current_admin)
):
    """Look up any user by ID. Restricted to admins."""
    try:
        user_service = UserService()
        user = await user_service.get_user_by_id(user_id)
        return UserResponse (
            id=user.id,
            email=user.email,
            full_name=user.full_name,
            phone=user.phone,
            role=user.role.value,
            status=user.status.value,
            created_at=user.created_at
        )
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.NOT_FOUND, detail=str(e))
    except Exception as e:
        logger.error(f"Error getting user: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to get user"
        )
