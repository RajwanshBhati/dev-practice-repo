from fastapi import APIRouter, HTTPException, Depends
from typing import Optional, List
from backend.services.admin_service import AdminService
from backend.services.doctor_service import DoctorService
from backend.database.dependencies import get_current_admin, require_permission
from backend.schemas.request.admin_request import (
    AdminCreateRequest,
    AdminLoginRequest
)
from backend.schemas.request.doctor_request import DoctorApproveRequest, DoctorRejectRequest
from backend.constants import HttpStatus, Permission
from backend.enums.user_enums import DoctorStatus
from backend.schemas.response.admin_response import AdminCreateResponse
import logging

router = APIRouter(prefix="/api/v1/admin", tags=["admin"])
logger = logging.getLogger(__name__)


@router.post("/setup-first-admin",response_model=AdminCreateResponse)
async def create_first_admin(admin_data: AdminCreateRequest):
    """Bootstrap endpoint for creating the very first super admin. Only works when no admin exists yet."""
    try:
        admin_service = AdminService()
        result = await admin_service.create_first_admin(admin_data)
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"First admin creation error: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to create first admin"
        )


@router.post("/create-admin",response_model=AdminCreateResponse)
async def create_admin(
    admin_data: AdminCreateRequest,
    current_admin: dict = Depends(require_permission(Permission.MANAGE_ADMINS))
):
    """Create a new sub-admin. Only a super admin can call this."""
    try:
        admin_service = AdminService()
        result = await admin_service.create_admin(
            admin_data,
            current_admin["user_id"]
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Admin creation error: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to create admin"
        )


@router.get("/admins")
async def get_all_admins(
    current_admin: dict = Depends(require_permission(Permission.MANAGE_ADMINS))
):
    """List every admin account in the system."""
    try:
        admin_service = AdminService()
        admins = await admin_service.get_all_admins()
        return {"admins": admins}
    except Exception as e:
        logger.error(f"Error getting admins: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to get admins"
        )


@router.delete("/admins/{admin_id}")
async def delete_admin(
    admin_id: str,
    current_admin: dict = Depends(require_permission(Permission.MANAGE_ADMINS))
):
    """Remove an admin account. Only a super admin can call this."""
    try:
        admin_service = AdminService()
        result = await admin_service.delete_admin(
            admin_id,
            current_admin["user_id"]
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Admin deletion error: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to delete admin"
        )


@router.get("/doctors/pending")
async def get_pending_doctors(
    limit: int = 100,
    skip: int = 0,
    current_admin: dict = Depends(require_permission(Permission.APPROVE_DOCTORS))
):
    """List doctors still waiting on admin approval, paginated."""
    try:
        doctor_service = DoctorService()
        doctors = await doctor_service.get_pending_doctors(
            current_admin["user_id"],
            limit,
            skip
        )
        return {
            "doctors": doctors,
            "count": len(doctors)
        }
    except Exception as e:
        logger.error(f"Error getting pending doctors: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to get pending doctors"
        )


@router.get("/doctors")
async def get_all_doctors(
    status: Optional[DoctorStatus] = None,
    limit: int = 100,
    skip: int = 0,
    current_admin: dict = Depends(require_permission(Permission.MANAGE_DOCTORS))
):
    """List all doctors, optionally filtered by approval status."""
    try:
        doctor_service = DoctorService()
        doctors = await doctor_service.get_all_doctors(
            current_admin["user_id"],
            limit,
            skip,
            status
        )
        total = await doctor_service.count_all_doctors(status)
        total_pages = (total // limit) + (1 if total % limit else 0)
        return {
            "doctors": doctors,
            "count": len(doctors),
            "total": total,
            "total_pages": total_pages
        }
    except Exception as e:
        logger.error(f"Error getting doctors: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to get doctors"
        )


@router.post("/doctors/{doctor_id}/approve")
async def approve_doctor(
    doctor_id: str,
    approve_data: DoctorApproveRequest,
    current_admin: dict = Depends(require_permission(Permission.APPROVE_DOCTORS))
):
    """Approve a doctor's registration so they can start using the platform."""
    try:
        doctor_service = DoctorService()
        result = await doctor_service.approve_doctor(
            doctor_id,
            current_admin["user_id"],
            approve_data
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Doctor approval error: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to approve doctor"
        )


@router.post("/doctors/{doctor_id}/reject")
async def reject_doctor(
    doctor_id: str,
    reject_data: DoctorRejectRequest,
    current_admin: dict = Depends(require_permission(Permission.REJECT_DOCTORS))
):
    """Reject a doctor's registration, recording the reason given by the admin."""
    try:
        doctor_service = DoctorService()
        result = await doctor_service.reject_doctor(
            doctor_id,
            current_admin["user_id"],
            reject_data
        )
        return result
    except ValueError as e:
        raise HTTPException(status_code=HttpStatus.BAD_REQUEST, detail=str(e))
    except Exception as e:
        logger.error(f"Doctor rejection error: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to reject doctor"
        )


@router.get("/doctors/stats")
async def get_doctor_stats(
    current_admin: dict = Depends(require_permission(Permission.VIEW_STATISTICS))
):
    """Return aggregate doctor stats for the admin dashboard."""
    try:
        doctor_service = DoctorService()
        stats = await doctor_service.get_doctor_stats(current_admin["user_id"])
        return stats
    except Exception as e:
        logger.error(f"Error getting doctor stats: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to get doctor statistics"
        )


@router.get("/audit-logs")
async def get_audit_logs(
    limit: int = 100,
    skip: int = 0,
    current_admin: dict = Depends(require_permission(Permission.VIEW_AUDIT_LOGS))
):
    """Fetch the audit trail of admin actions, paginated."""
    try:
        from backend.repositories.admin_repository import AdminRepository
        admin_repo = AdminRepository()
        logs = await admin_repo.get_audit_logs(
            admin_id=current_admin["user_id"],
            limit=limit,
            skip=skip
        )
        return {
            "logs": logs,
            "count": len(logs)
        }
    except Exception as e:
        logger.error(f"Error getting audit logs: {str(e)}")
        raise HTTPException(
            status_code=HttpStatus.INTERNAL_SERVER_ERROR,
            detail="Failed to get audit logs"
        )
