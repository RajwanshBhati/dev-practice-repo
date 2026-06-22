from fastapi import APIRouter
from fastapi import HTTPException

from app.schemas.request.auth_request import (
    PatientRegisterRequest,
    DoctorRegisterRequest
)

from app.schemas.request.auth_request import (
    LoginRequest
)

from app.schemas.response.auth_response import (
    LoginResponse
)

from app.schemas.response.auth_response import (
    RegisterResponse,
    UserResponse
)

from app.services.auth_service import AuthService

router = APIRouter()

auth_service = AuthService()

@router.post(
    "/register/patient",
    response_model=RegisterResponse
)
async def register_patient(
    payload: PatientRegisterRequest
):

    try:

        user = await auth_service.register_patient(
            payload
        )

        return RegisterResponse(
            message="Patient registered successfully",
            user=UserResponse(
                id=str(user.id),
                full_name=user.full_name,
                email=user.email,
                role=user.role.value,
                created_at=user.created_at
            )
        )

    except ValueError as ex:

        raise HTTPException(
            status_code=400,
            detail=str(ex)
        )

@router.post(
    "/register/doctor",
    response_model=RegisterResponse
)
async def register_doctor(
    payload: DoctorRegisterRequest
):

    try:

        user = await auth_service.register_doctor(
            payload
        )

        return RegisterResponse(
            message="Doctor registered successfully",
            user=UserResponse(
                id=str(user.id),
                full_name=user.full_name,
                email=user.email,
                role=user.role.value,
                created_at=user.created_at
            )
        )

    except ValueError as ex:

        raise HTTPException(
            status_code=400,
            detail=str(ex)
        )

@router.post(
    "/login",
    response_model=LoginResponse
)
async def login(
    payload: LoginRequest
):

    try:

        result = await auth_service.login(
            payload
        )

        user = result["user"]

        return LoginResponse(
            message="Login successful",
            access_token=result["access_token"],
            token_type="bearer",
            user=UserResponse(
                id=str(user.id),
                full_name=user.full_name,
                email=user.email,
                role=user.role.value,
                created_at=user.created_at
            )
        )

    except ValueError as ex:

        raise HTTPException(
            status_code=401,
            detail=str(ex)
        )
