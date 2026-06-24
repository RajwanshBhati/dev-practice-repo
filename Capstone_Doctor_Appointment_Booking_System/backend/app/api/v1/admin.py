from fastapi import APIRouter, HTTPException, Depends
from typing import List
from app.middleware.auth_middleware import require_admin, get_current_user
from app.models.user import User
from app.services.admin_service import AdminService
from app.schemas.response.common_response import APIResponse

router = APIRouter()
admin_service = AdminService()


@router.get("/doctors/pending")
async def get_pending_doctors(
    current_user: User = Depends(require_admin)
):
    """
    Get all doctors pending approval (Admin only)
    """
    try:
        pending_doctors = await admin_service.get_pending_doctors()
        pending_count = await admin_service.get_pending_doctor_count()

        # Format response
        doctors_data = []
        for doctor in pending_doctors:
            doctors_data.append({
                "id": str(doctor.id),
                "full_name": doctor.full_name,
                "email": doctor.email,
                "phone_number": doctor.phone_number,
                "created_at": doctor.created_at.isoformat(),
                "is_approved": doctor.is_approved
            })

        return {
            "message": "Pending doctors retrieved successfully",
            "count": pending_count,
            "doctors": doctors_data
        }
    except Exception as ex:
        raise HTTPException(status_code=400, detail=str(ex))


@router.put("/doctors/{doctor_id}/approve")
async def approve_doctor(
    doctor_id: str,
    current_user: User = Depends(require_admin)
):
    """
    Approve a doctor account (Admin only)
    """
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
                "is_approved": doctor.is_approved,
                "approved_at": doctor.approved_at.isoformat() if doctor.approved_at else None
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
    """
    Reject a doctor account (Admin only)
    """
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
                "is_active": doctor.is_active,
                "rejection_reason": doctor.rejection_reason,
                "rejected_at": doctor.approved_at.isoformat() if doctor.approved_at else None
            }
        }
    except ValueError as ex:
        raise HTTPException(status_code=400, detail=str(ex))


@router.get("/doctors/all")
async def get_all_doctors(
    skip: int = 0,
    limit: int = 10,
    include_pending: bool = False,
    current_user: User = Depends(require_admin)
):
    """
    Get all doctors with pagination (Admin only)
    """
    try:
        doctors = await admin_service.get_all_doctors(
            skip=skip,
            limit=limit,
            include_pending=include_pending
        )

        doctors_data = []
        for doctor in doctors:
            doctors_data.append({
                "id": str(doctor.id),
                "full_name": doctor.full_name,
                "email": doctor.email,
                "is_approved": doctor.is_approved,
                "is_active": doctor.is_active,
                "created_at": doctor.created_at.isoformat()
            })

        return {
            "message": "Doctors retrieved successfully",
            "count": len(doctors_data),
            "skip": skip,
            "limit": limit,
            "doctors": doctors_data
        }
    except Exception as ex:
        raise HTTPException(status_code=400, detail=str(ex))


@router.get("/dashboard/stats")
async def get_admin_stats(
    current_user: User = Depends(require_admin)
):
    """
    Get admin dashboard statistics
    """
    try:
        from app.constants.roles import UserRole

        # Get counts
        total_doctors = await User.find(User.role == UserRole.DOCTOR).count()
        total_patients = await User.find(User.role == UserRole.PATIENT).count()
        total_admins = await User.find(User.role == UserRole.ADMIN).count()
        pending_doctors = await admin_service.get_pending_doctor_count()
        active_doctors = await User.find(
            User.role == UserRole.DOCTOR,
            User.is_active == True,
            User.is_approved == True
        ).count()

        return {
            "message": "Dashboard statistics retrieved",
            "stats": {
                "total_doctors": total_doctors,
                "total_patients": total_patients,
                "total_admins": total_admins,
                "pending_doctors": pending_doctors,
                "active_doctors": active_doctors
            }
        }
    except Exception as ex:
        raise HTTPException(status_code=400, detail=str(ex))
