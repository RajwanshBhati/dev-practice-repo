import pytest
from unittest.mock import AsyncMock, patch
from backend.services.admin_service import AdminService
from backend.schemas.request.admin_request import AdminCreateRequest


class TestAdminService:
    """Test cases for AdminService class."""

    @pytest.mark.asyncio
    async def test_create_first_admin_success(self, mock_db, sample_admin_data):
        """Test create first admin - success."""
        # Convert dict to Pydantic model
        admin_data = AdminCreateRequest(**sample_admin_data)

        from backend.models.user import User
        from backend.constants.roles import UserRole
        from backend.enums.user_enums import Gender, UserStatus

        mock_user = User(
            id="admin123",
            email="admin@example.com",
            password_hash="hashed_password",
            full_name="Admin User",
            phone="9876543210",
            gender=Gender.FEMALE,
            date_of_birth="10-12-1985",
            role=UserRole.ADMIN,
            status=UserStatus.ACTIVE,
            is_verified=True,
            is_first_admin=True
        )

        with patch('backend.services.admin_service.AdminService.check_first_admin_exists') as mock_exists:
            mock_exists.return_value = False

            with patch('backend.services.admin_service.UserRepository') as mock_repo:
                mock_repo.return_value.find_by_email = AsyncMock(return_value=None)
                mock_repo.return_value.create = AsyncMock(return_value=mock_user)

                with patch('backend.services.admin_service.AdminRepository') as mock_admin_repo:
                    mock_admin_repo.return_value.create_audit_log = AsyncMock(return_value=True)

                    service = AdminService()
                    result = await service.create_first_admin(admin_data)

                    assert result.message == "Admin created successfully"
                    assert result.admin.email == "admin@example.com"

    @pytest.mark.asyncio
    async def test_create_admin_success(self, mock_db, sample_admin_data):
        """Test create admin - success."""
        admin_data = AdminCreateRequest(**sample_admin_data)

        from backend.models.user import User
        from backend.constants.roles import UserRole
        from backend.enums.user_enums import Gender, UserStatus

        mock_admin = User(
            id="admin123",
            email="admin@example.com",
            password_hash="hashed_password",
            full_name="Admin User",
            phone="9876543210",
            gender=Gender.FEMALE,
            date_of_birth="10-12-1985",
            role=UserRole.ADMIN,
            status=UserStatus.ACTIVE,
            is_verified=True,
            is_first_admin=True
        )

        mock_new_admin = User(
            id="admin456",
            email="subadmin@example.com",
            password_hash="hashed_password",
            full_name="Sub Admin",
            phone="1234567890",
            gender=Gender.MALE,
            date_of_birth="15-05-1990",
            role=UserRole.ADMIN,
            status=UserStatus.ACTIVE,
            is_verified=True,
            is_first_admin=False
        )

        with patch('backend.services.admin_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_id = AsyncMock(return_value=mock_admin)
            mock_repo.return_value.find_by_email = AsyncMock(return_value=None)
            mock_repo.return_value.create = AsyncMock(return_value=mock_new_admin)

            with patch('backend.services.admin_service.AdminRepository') as mock_admin_repo:
                mock_admin_repo.return_value.create_audit_log = AsyncMock(return_value=True)

                service = AdminService()
                result = await service.create_admin(admin_data, "admin123")  # Pass model, not dict

                assert result.message == "Admin created successfully"
                assert result.admin.email == "subadmin@example.com"
