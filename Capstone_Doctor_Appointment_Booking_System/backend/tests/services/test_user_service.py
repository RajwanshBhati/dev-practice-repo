import pytest
from unittest.mock import AsyncMock, patch
from backend.services.user_service import UserService


class TestUserService:
    """Complete test cases for UserService - 100% coverage."""

    @pytest.fixture
    def mock_user(self):
        from backend.models.user import User
        from backend.enums.user_enums import Gender, UserStatus
        from backend.constants.roles import UserRole
        return User(
            id="user123",
            email="john@example.com",
            password_hash="hashed",
            full_name="John Doe",
            phone="1234567890",
            gender=Gender.MALE,
            date_of_birth="15-05-1990",
            role=UserRole.PATIENT,
            status=UserStatus.ACTIVE
        )

    @pytest.mark.asyncio
    async def test_get_user_by_id_success(self, mock_db, mock_user):
        """Test get user by ID - success."""
        with patch('backend.services.user_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_id = AsyncMock(return_value=mock_user)

            service = UserService()
            result = await service.get_user_by_id("user123")

            assert result.id == "user123"
            assert result.email == "john@example.com"

    @pytest.mark.asyncio
    async def test_get_user_by_id_not_found(self, mock_db):
        """Test get user by ID - not found."""
        with patch('backend.services.user_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_id = AsyncMock(return_value=None)

            service = UserService()

            with pytest.raises(ValueError, match="User not found"):
                await service.get_user_by_id("unknown_user")

    @pytest.mark.asyncio
    async def test_get_user_by_email_success(self, mock_db, mock_user):
        """Test get user by email - success."""
        with patch('backend.services.user_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_email = AsyncMock(return_value=mock_user)

            service = UserService()
            result = await service.get_user_by_email("john@example.com")

            assert result.id == "user123"
            assert result.email == "john@example.com"

    @pytest.mark.asyncio
    async def test_get_user_by_email_not_found(self, mock_db):
        """Test get user by email - not found."""
        with patch('backend.services.user_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_email = AsyncMock(return_value=None)

            service = UserService()

            with pytest.raises(ValueError, match="User not found"):
                await service.get_user_by_email("unknown@example.com")

    @pytest.mark.asyncio
    async def test_update_user_success(self, mock_db, mock_user):
        """Test update user - success."""
        with patch('backend.services.user_service.UserRepository') as mock_repo:
            mock_repo.return_value.update = AsyncMock(return_value=mock_user)

            service = UserService()
            result = await service.update_user("user123", {"full_name": "Updated Name"})

            assert result.id == "user123"
            assert result.full_name == "John Doe"

    @pytest.mark.asyncio
    async def test_update_user_not_found(self, mock_db):
        """Test update user - not found."""
        with patch('backend.services.user_service.UserRepository') as mock_repo:
            mock_repo.return_value.update = AsyncMock(return_value=None)

            service = UserService()

            with pytest.raises(ValueError, match="User not found"):
                await service.update_user("unknown_user", {"full_name": "Updated"})

    @pytest.mark.asyncio
    async def test_deactivate_user_success(self, mock_db, mock_user):
        """Test deactivate user - success."""
        with patch('backend.services.user_service.UserRepository') as mock_repo:
            mock_repo.return_value.update = AsyncMock(return_value=mock_user)

            service = UserService()
            result = await service.deactivate_user("user123")

            assert result.id == "user123"

    @pytest.mark.asyncio
    async def test_deactivate_user_not_found(self, mock_db):
        """Test deactivate user - not found."""
        with patch('backend.services.user_service.UserRepository') as mock_repo:
            mock_repo.return_value.update = AsyncMock(return_value=None)

            service = UserService()

            with pytest.raises(ValueError, match="User not found"):
                await service.deactivate_user("unknown_user")

    @pytest.mark.asyncio
    async def test_activate_user_success(self, mock_db, mock_user):
        """Test activate user - success."""
        with patch('backend.services.user_service.UserRepository') as mock_repo:
            mock_repo.return_value.update = AsyncMock(return_value=mock_user)

            service = UserService()
            result = await service.activate_user("user123")

            assert result.id == "user123"

    @pytest.mark.asyncio
    async def test_activate_user_not_found(self, mock_db):
        """Test activate user - not found."""
        with patch('backend.services.user_service.UserRepository') as mock_repo:
            mock_repo.return_value.update = AsyncMock(return_value=None)

            service = UserService()

            with pytest.raises(ValueError, match="User not found"):
                await service.activate_user("unknown_user")
