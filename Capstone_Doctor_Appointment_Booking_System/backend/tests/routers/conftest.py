import pytest
from unittest.mock import AsyncMock, MagicMock, patch
from fastapi import FastAPI, Depends
from backend.main import app
from backend.database.dependencies import (
    get_current_user, get_current_admin, get_current_doctor,
    get_current_patient, require_permission, require_any_permission,
    require_role, security
)
from backend.constants.roles import Permission, UserRole
from backend.enums.user_enums import UserStatus


@pytest.fixture(autouse=True)
def override_dependencies():
    """Override all authentication dependencies for all tests"""

    # Create mock user dicts
    admin_user = {
        "user_id": "admin_123",
        "email": "admin@example.com",
        "role": UserRole.ADMIN.value,
        "status": UserStatus.ACTIVE.value,
        "sub": "admin_123",
        "is_first_admin": True,
        "full_name": "Admin User"
    }

    patient_user = {
        "user_id": "patient_123",
        "email": "patient@example.com",
        "role": UserRole.PATIENT.value,
        "status": UserStatus.ACTIVE.value,
        "sub": "patient_123",
        "full_name": "Patient User"
    }

    doctor_user = {
        "user_id": "doctor_123",
        "email": "doctor@example.com",
        "role": UserRole.DOCTOR.value,
        "status": UserStatus.ACTIVE.value,
        "sub": "doctor_123",
        "full_name": "Doctor User"
    }

    # Async functions that return user dicts
    async def mock_get_current_user():
        return patient_user

    async def mock_get_current_admin():
        return admin_user

    async def mock_get_current_doctor():
        return doctor_user

    async def mock_get_current_patient():
        return patient_user


    def mock_require_permission(permission):
        async def dependency(current_user=None):
            return admin_user
        return dependency

    def mock_require_any_permission(permissions):
        async def dependency(current_user=None):
            return admin_user
        return dependency

    def mock_require_role(role):
        async def dependency(current_user=None):
            if role == UserRole.ADMIN:
                return admin_user
            elif role == UserRole.DOCTOR:
                return doctor_user
            else:
                return patient_user
        return dependency

    # Override all dependencies
    app.dependency_overrides[get_current_user] = mock_get_current_user
    app.dependency_overrides[get_current_admin] = mock_get_current_admin
    app.dependency_overrides[get_current_doctor] = mock_get_current_doctor
    app.dependency_overrides[get_current_patient] = mock_get_current_patient
    app.dependency_overrides[require_permission] = mock_require_permission
    app.dependency_overrides[require_any_permission] = mock_require_any_permission
    app.dependency_overrides[require_role] = mock_require_role

    yield

    app.dependency_overrides.clear()
