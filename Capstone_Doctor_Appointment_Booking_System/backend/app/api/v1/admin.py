from fastapi import APIRouter, HTTPException, Depends
from typing import List
from app.middleware.auth_middleware import require_admin
from app.models.user import User
from app.services.admin_service import AdminService
from app.schemas.response.admin_response import AdminResponse

router = APIRouter()
admin_service = AdminService()


@router.get("/doctors/pending")
async def get_pending_doctors(
    current_user: User = Depends(require_admin)
):
    """Get all doctors pending approval (Admin only)"""
    try:
        pending_doctors = await admin_service.get_pending_doctors()
        return {
            "message": "Pending doctors retrieved successfully",
            "count": len(pending_doctors),
            "doctors": pending_doctors
        }
    except Exception as ex:
        raise HTTPException(status_code=400, detail=str(ex))


@router.put("/doctors/{doctor_id}/approve")
async def approve_doctor(
    doctor_id: str,
    current_user: User = Depends(require_admin)
):
    """Approve a doctor account (Admin only)"""
    try:
        doctor = await admin_service.approve_doctor(
            doctor_id=doctor_id,
            admin_id=str(current_user.id)
        )
        return {
            "message": f"Doctor {doctor.full_name} approved successfully",
            "doctor": {
                "id": str(doctor.id),
                "full_name": doctor.full_name,
                "email": doctor.email,
                "is_approved": doctor.is_approved
            }
        }
    except ValueError as ex:
        raise HTTPException(status_code=400, detail=str(ex))


@router.put("/doctors/{doctor_id}/reject")
async def reject_doctor(
    doctor_id: str,
    rejection_reason: str,
    current_user: User = Depends(require_admin)
):
    """Reject a doctor account (Admin only)"""
    try:
        doctor = await admin_service.reject_doctor(
            doctor_id=doctor_id,
            admin_id=str(current_user.id),
            rejection_reason=rejection_reason
        )
        return {
            "message": f"Doctor {doctor.full_name} rejected",
            "doctor": {
                "id": str(doctor.id),
                "full_name": doctor.full_name,
                "email": doctor.email,
                "is_approved": doctor.is_approved,
                "rejection_reason": doctor.rejection_reason
            }
        }
    except ValueError as ex:
        raise HTTPException(status_code=400, detail=str(ex))
