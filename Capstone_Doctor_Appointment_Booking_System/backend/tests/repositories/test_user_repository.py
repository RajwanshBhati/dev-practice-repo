import pytest
from unittest.mock import AsyncMock, patch
from bson import ObjectId
from datetime import datetime
from backend.repositories.user_repository import UserRepository
from backend.models.user import User
from backend.enums.user_enums import Gender, UserStatus
from backend.constants.roles import UserRole


class TestUserRepository:
    """Complete test cases for UserRepository - 100% coverage."""

    @pytest.fixture
    def mock_user_data(self):
        valid_id = str(ObjectId())
        return {
            "_id": valid_id,
            "email": "test@example.com",
            "password_hash": "hashed_password",
            "full_name": "Test User",
            "phone": "1234567890",
            "gender": "Male",
            "date_of_birth": "15-05-1990",
            "role": "PATIENT",
            "status": "ACTIVE",
            "is_verified": True,
            "created_at": datetime.utcnow(),
            "updated_at": datetime.utcnow()
        }

    @pytest.mark.asyncio
    async def test_create_user_success(self, mock_db, mock_user_data):
        """Test create user - success."""
        mock_db.users.insert_one = AsyncMock(return_value=AsyncMock(inserted_id=mock_user_data["_id"]))

        user = User(
            email=mock_user_data["email"],
            password_hash=mock_user_data["password_hash"],
            full_name=mock_user_data["full_name"],
            phone=mock_user_data["phone"],
            gender=Gender.MALE,
            date_of_birth=mock_user_data["date_of_birth"],
            role=UserRole.PATIENT,
            status=UserStatus.ACTIVE
        )

        repo = UserRepository()
        result = await repo.create(user)

        assert result.id == mock_user_data["_id"]
        assert result.email == mock_user_data["email"]

    @pytest.mark.asyncio
    async def test_find_by_email_success(self, mock_db, mock_user_data):
        """Test find by email - success."""
        mock_db.users.find_one = AsyncMock(return_value=mock_user_data)

        repo = UserRepository()
        result = await repo.find_by_email("test@example.com")

        assert result is not None
        assert result.email == "test@example.com"

    @pytest.mark.asyncio
    async def test_find_by_email_not_found(self, mock_db):
        """Test find by email - not found."""
        mock_db.users.find_one = AsyncMock(return_value=None)

        repo = UserRepository()
        result = await repo.find_by_email("notfound@example.com")

        assert result is None

    @pytest.mark.asyncio
    async def test_find_by_id_success(self, mock_db, mock_user_data):
        """Test find by ID - success."""
        mock_db.users.find_one = AsyncMock(return_value=mock_user_data)

        repo = UserRepository()
        result = await repo.find_by_id(mock_user_data["_id"])

        assert result is not None
        assert result.email == mock_user_data["email"]

    @pytest.mark.asyncio
    async def test_find_by_id_invalid_id(self, mock_db):
        """Test find by ID - invalid ObjectId."""
        repo = UserRepository()
        result = await repo.find_by_id("invalid_id")

        assert result is None

    @pytest.mark.asyncio
    async def test_update_user_success(self, mock_db, mock_user_data):
        """Test update user - success."""
        mock_db.users.update_one = AsyncMock(return_value=AsyncMock(modified_count=1))
        mock_db.users.find_one = AsyncMock(return_value=mock_user_data)

        repo = UserRepository()
        result = await repo.update(mock_user_data["_id"], {"full_name": "Updated Name"})

        assert result is not None
        assert result.full_name == mock_user_data["full_name"]

    @pytest.mark.asyncio
    async def test_update_user_not_found(self, mock_db):
        """Test update user - not found."""
        valid_id = str(ObjectId())  # Use valid ObjectId
        mock_db.users.update_one = AsyncMock(return_value=AsyncMock(modified_count=0))

        repo = UserRepository()
        result = await repo.update(valid_id, {"full_name": "Updated"})

        assert result is None

    @pytest.mark.asyncio
    async def test_update_last_login_success(self, mock_db):
        """Test update last login - success."""
        valid_id = str(ObjectId())
        mock_db.users.update_one = AsyncMock(return_value=AsyncMock(modified_count=1))

        repo = UserRepository()
        result = await repo.update_last_login(valid_id)

        assert result is True

    @pytest.mark.asyncio
    async def test_update_last_login_failure(self, mock_db):
        """Test update last login - failure."""
        valid_id = str(ObjectId())
        mock_db.users.update_one = AsyncMock(return_value=AsyncMock(modified_count=0))

        repo = UserRepository()
        result = await repo.update_last_login(valid_id)

        assert result is False

    @pytest.mark.asyncio
    async def test_deactivate_user_success(self, mock_db):
        """Test deactivate user - success."""
        valid_id = str(ObjectId())
        mock_db.users.update_one = AsyncMock(return_value=AsyncMock(modified_count=1))

        repo = UserRepository()
        result = await repo.deactivate_user(valid_id)

        assert result is True

    @pytest.mark.asyncio
    async def test_activate_user_success(self, mock_db):
        """Test activate user - success."""
        valid_id = str(ObjectId())
        mock_db.users.update_one = AsyncMock(return_value=AsyncMock(modified_count=1))

        repo = UserRepository()
        result = await repo.activate_user(valid_id)

        assert result is True
