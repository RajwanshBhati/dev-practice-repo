import pytest
from unittest.mock import AsyncMock, patch, MagicMock
from fastapi import HTTPException, Depends
from fastapi.security import HTTPAuthorizationCredentials
from backend.database.dependencies import (
    get_current_user,
    require_permission,
    require_any_permission,
    require_role,
    require_any_role,
    get_current_patient,
    get_current_doctor,
    get_current_admin,
    can_view_doctors,
    can_book_appointment,
    can_manage_availability,
    can_manage_users
)
from backend.constants.roles import UserRole, Permission
from backend.constants import HttpStatus, ErrorMessages


class TestDependencies:
    """Complete test cases for dependencies - 100% coverage."""

    @pytest.fixture
    def mock_credentials(self):
        return HTTPAuthorizationCredentials(
            scheme="Bearer",
            credentials="valid_token_123"
        )

    @pytest.fixture
    def mock_user_info(self):
        return {
            "user_id": "user123",
            "email": "john@example.com",
            "role": "PATIENT",
            "full_name": "John Doe"
        }

    @pytest.fixture
    def mock_admin_info(self):
        return {
            "user_id": "admin123",
            "email": "admin@example.com",
            "role": "ADMIN",
            "full_name": "Admin User"
        }

    @pytest.fixture
    def mock_doctor_info(self):
        return {
            "user_id": "doctor123",
            "email": "doctor@example.com",
            "role": "DOCTOR",
            "full_name": "Dr. Smith"
        }

    @pytest.mark.asyncio
    async def test_get_current_user_success(self, mock_credentials, mock_user_info):
        """Test get_current_user - success."""
        with patch('backend.database.dependencies.AuthService') as mock_auth_service:
            mock_auth_service.return_value.validate_token = AsyncMock(return_value=mock_user_info)

            result = await get_current_user(mock_credentials)

            assert result == mock_user_info
            assert result["user_id"] == "user123"
            assert result["role"] == "PATIENT"

    @pytest.mark.asyncio
    async def test_get_current_user_value_error(self, mock_credentials):
        """Test get_current_user - ValueError."""
        with patch('backend.database.dependencies.AuthService') as mock_auth_service:
            mock_auth_service.return_value.validate_token = AsyncMock(
                side_effect=ValueError("Invalid token")
            )

            with pytest.raises(HTTPException) as exc_info:
                await get_current_user(mock_credentials)

            assert exc_info.value.status_code == HttpStatus.UNAUTHORIZED
            assert exc_info.value.detail == "Invalid token"

    @pytest.mark.asyncio
    async def test_get_current_user_general_exception(self, mock_credentials):
        """Test get_current_user - general exception."""
        with patch('backend.database.dependencies.AuthService') as mock_auth_service:
            mock_auth_service.return_value.validate_token = AsyncMock(
                side_effect=Exception("Database error")
            )

            with pytest.raises(HTTPException) as exc_info:
                await get_current_user(mock_credentials)

            assert exc_info.value.status_code == HttpStatus.UNAUTHORIZED
            assert exc_info.value.detail == ErrorMessages.AUTH_1003

    def test_require_permission_success(self, mock_user_info):
        """Test require_permission - success."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            with patch('backend.database.dependencies.RBAC.has_permission') as mock_has_permission:
                mock_has_permission.return_value = True

                dependency = require_permission(Permission.VIEW_PROFILE)
                # Call the dependency directly (it's not async)
                result = dependency(mock_user_info)

                assert result == mock_user_info

    def test_require_permission_forbidden(self, mock_user_info):
        """Test require_permission - forbidden."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            with patch('backend.database.dependencies.RBAC.has_permission') as mock_has_permission:
                mock_has_permission.return_value = False

                dependency = require_permission(Permission.MANAGE_ADMINS)

                with pytest.raises(HTTPException) as exc_info:
                    dependency(mock_user_info)

                assert exc_info.value.status_code == HttpStatus.FORBIDDEN
                assert "Permission 'MANAGE_ADMINS' required" in exc_info.value.detail

    def test_require_any_permission_success(self, mock_user_info):
        """Test require_any_permission - success."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            with patch('backend.database.dependencies.RBAC.has_any_permission') as mock_has_any:
                mock_has_any.return_value = True

                permissions = [Permission.VIEW_PROFILE, Permission.UPDATE_PROFILE]
                dependency = require_any_permission(permissions)
                result = dependency(mock_user_info)

                assert result == mock_user_info

    def test_require_any_permission_forbidden(self, mock_user_info):
        """Test require_any_permission - forbidden."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            with patch('backend.database.dependencies.RBAC.has_any_permission') as mock_has_any:
                mock_has_any.return_value = False

                permissions = [Permission.MANAGE_ADMINS, Permission.APPROVE_DOCTORS]
                dependency = require_any_permission(permissions)

                with pytest.raises(HTTPException) as exc_info:
                    dependency(mock_user_info)

                assert exc_info.value.status_code == HttpStatus.FORBIDDEN
                assert "Any of these permissions required" in exc_info.value.detail

    def test_require_role_success_patient(self, mock_user_info):
        """Test require_role - success (patient)."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            dependency = require_role(UserRole.PATIENT)
            result = dependency(mock_user_info)

            assert result == mock_user_info

    def test_require_role_success_admin(self, mock_admin_info):
        """Test require_role - success (admin)."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_admin_info

            dependency = require_role(UserRole.ADMIN)
            result = dependency(mock_admin_info)

            assert result == mock_admin_info

    def test_require_role_success_doctor(self, mock_doctor_info):
        """Test require_role - success (doctor)."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_doctor_info

            dependency = require_role(UserRole.DOCTOR)
            result = dependency(mock_doctor_info)

            assert result == mock_doctor_info

    def test_require_role_forbidden(self, mock_user_info):
        """Test require_role - forbidden (wrong role)."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            dependency = require_role(UserRole.ADMIN)

            with pytest.raises(HTTPException) as exc_info:
                dependency(mock_user_info)

            assert exc_info.value.status_code == HttpStatus.FORBIDDEN
            assert "Role 'ADMIN' required" in exc_info.value.detail

    def test_require_any_role_success(self, mock_user_info):
        """Test require_any_role - success."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            roles = [UserRole.PATIENT, UserRole.DOCTOR]
            dependency = require_any_role(roles)
            result = dependency(mock_user_info)

            assert result == mock_user_info

    def test_require_any_role_forbidden(self, mock_user_info):
        """Test require_any_role - forbidden."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            roles = [UserRole.ADMIN, UserRole.DOCTOR]
            dependency = require_any_role(roles)

            with pytest.raises(HTTPException) as exc_info:
                dependency(mock_user_info)

            assert exc_info.value.status_code == HttpStatus.FORBIDDEN
            assert "Any of these roles required" in exc_info.value.detail

    def test_get_current_patient_success(self, mock_user_info):
        """Test get_current_patient - success."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            result = get_current_patient(mock_user_info)
            assert result == mock_user_info

    def test_get_current_patient_forbidden(self, mock_admin_info):
        """Test get_current_patient - forbidden (admin trying to access patient route)."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_admin_info

            with pytest.raises(HTTPException) as exc_info:
                get_current_patient(mock_admin_info)

            assert exc_info.value.status_code == HttpStatus.FORBIDDEN

    def test_get_current_doctor_success(self, mock_doctor_info):
        """Test get_current_doctor - success."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_doctor_info

            result = get_current_doctor(mock_doctor_info)
            assert result == mock_doctor_info

    def test_get_current_doctor_forbidden(self, mock_user_info):
        """Test get_current_doctor - forbidden (patient trying to access doctor route)."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            with pytest.raises(HTTPException) as exc_info:
                get_current_doctor(mock_user_info)

            assert exc_info.value.status_code == HttpStatus.FORBIDDEN

    def test_get_current_admin_success(self, mock_admin_info):
        """Test get_current_admin - success."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_admin_info

            result = get_current_admin(mock_admin_info)
            assert result == mock_admin_info

    def test_get_current_admin_forbidden(self, mock_user_info):
        """Test get_current_admin - forbidden (patient trying to access admin route)."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            with pytest.raises(HTTPException) as exc_info:
                get_current_admin(mock_user_info)

            assert exc_info.value.status_code == HttpStatus.FORBIDDEN

    def test_can_view_doctors_success(self, mock_user_info):
        """Test can_view_doctors - success."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            with patch('backend.database.dependencies.RBAC.has_permission') as mock_has:
                mock_has.return_value = True

                result = can_view_doctors(mock_user_info)
                assert result == mock_user_info

    def test_can_view_doctors_forbidden(self, mock_user_info):
        """Test can_view_doctors - forbidden."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            with patch('backend.database.dependencies.RBAC.has_permission') as mock_has:
                mock_has.return_value = False

                with pytest.raises(HTTPException) as exc_info:
                    can_view_doctors(mock_user_info)

                assert exc_info.value.status_code == HttpStatus.FORBIDDEN

    def test_can_book_appointment_success(self, mock_user_info):
        """Test can_book_appointment - success."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            with patch('backend.database.dependencies.RBAC.has_permission') as mock_has:
                mock_has.return_value = True

                result = can_book_appointment(mock_user_info)
                assert result == mock_user_info

    def test_can_manage_availability_success(self, mock_doctor_info):
        """Test can_manage_availability - success."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_doctor_info

            with patch('backend.database.dependencies.RBAC.has_permission') as mock_has:
                mock_has.return_value = True

                result = can_manage_availability(mock_doctor_info)
                assert result == mock_doctor_info

    def test_can_manage_users_success(self, mock_admin_info):
        """Test can_manage_users - success."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_admin_info

            with patch('backend.database.dependencies.RBAC.has_permission') as mock_has:
                mock_has.return_value = True

                result = can_manage_users(mock_admin_info)
                assert result == mock_admin_info

    def test_can_manage_users_forbidden(self, mock_user_info):
        """Test can_manage_users - forbidden."""
        with patch('backend.database.dependencies.get_current_user') as mock_get_user:
            mock_get_user.return_value = mock_user_info

            with patch('backend.database.dependencies.RBAC.has_permission') as mock_has:
                mock_has.return_value = False

                with pytest.raises(HTTPException) as exc_info:
                    can_manage_users(mock_user_info)

                assert exc_info.value.status_code == HttpStatus.FORBIDDEN
