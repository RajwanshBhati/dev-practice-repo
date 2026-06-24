from fastapi import APIRouter, HTTPException, Depends
from app.schemas.request import (
    LoginRequest,
    PatientRegisterRequest,
    DoctorRegisterRequest,
    AdminRegisterRequest
)
from app.schemas.response import (
    LoginResponse,
    RegisterResponse,
    UserResponseBase
)
from app.services.auth_service import AuthService
from app.middleware.auth_middleware import require_admin
from app.models.user import User
from app.constants.roles import UserRole

router = APIRouter()
auth_service = AuthService()


# ================ PUBLIC ENDPOINTS ================

@router.post("/register/patient", response_model=RegisterResponse)
async def register_patient(payload: PatientRegisterRequest):
    """Register a new patient (public)"""
    try:
        user = await auth_service.register_patient(payload)
        return RegisterResponse(
            message="Patient registered successfully",
            token_type="bearer",
            user=UserResponseBase(
                id=str(user.id),
                full_name=user.full_name,
                email=user.email,
                role=user.role.value,
                created_at=user.created_at
            )
        )
    except ValueError as ex:
        raise HTTPException(status_code=400, detail=str(ex))


@router.post("/register/doctor", response_model=RegisterResponse)
async def register_doctor(payload: DoctorRegisterRequest):
    """Register a new doctor (public)"""
    try:
        user = await auth_service.register_doctor(payload)
        return RegisterResponse(
            message="Doctor registered successfully",
            token_type="bearer",
            user=UserResponseBase(
                id=str(user.id),
                full_name=user.full_name,
                email=user.email,
                role=user.role.value,
                created_at=user.created_at
            )
        )
    except ValueError as ex:
        raise HTTPException(status_code=400, detail=str(ex))


@router.post("/register/first-admin", response_model=RegisterResponse)
async def register_first_admin(payload: AdminRegisterRequest):
    """Register the first admin (public - no authentication required)"""
    try:
        user = await auth_service.register_first_admin(payload)
        return RegisterResponse(
            message="First admin registered successfully",
            token_type="bearer",
            user=UserResponseBase(
                id=str(user.id),
                full_name=user.full_name,
                email=user.email,
                role=user.role.value,
                created_at=user.created_at
            )
        )
    except ValueError as ex:
        raise HTTPException(status_code=400, detail=str(ex))


@router.post("/login", response_model=LoginResponse)
async def login(payload: LoginRequest):
    """Login for all users (patient, doctor, admin)"""
    try:
        result = await auth_service.login(payload)
        user = result["user"]
        return LoginResponse(
            message="Login successful",
            access_token=result["access_token"],
            token_type="bearer",
            user=UserResponseBase(
                id=str(user.id),
                full_name=user.full_name,
                email=user.email,
                role=user.role.value,
                created_at=user.created_at
            )
        )
    except ValueError as ex:
        raise HTTPException(status_code=401, detail=str(ex))


# PROTECTED ADMIN ENDPOINTS

@router.post("/admin/create", response_model=RegisterResponse)
async def create_admin_by_admin(
    payload: AdminRegisterRequest,
    current_user: User = Depends(require_admin)
):
    """Create a new admin (admin only - requires authentication)"""
    try:
        user = await auth_service.register_admin_by_admin(payload, current_user)
        return RegisterResponse(
            message="Admin account created successfully",
            token_type="bearer",
            user=UserResponseBase(
                id=str(user.id),
                full_name=user.full_name,
                email=user.email,
                role=user.role.value,
                created_at=user.created_at
            )
        )
    except ValueError as ex:
        raise HTTPException(status_code=400, detail=str(ex))


# CHECK ADMIN STATUS

@router.get("/admin/check")
async def check_admin_exists():
    """Check if any admin exists in the system"""
    existing_admin = await auth_service.user_repository.get_by_role(UserRole.ADMIN)
    return {
        "admin_exists": existing_admin is not None,
        "message": "Admin exists" if existing_admin else "No admin found. Please create first admin."
    }
