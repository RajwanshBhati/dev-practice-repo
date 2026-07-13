import pytest
from unittest.mock import AsyncMock, patch
from datetime import datetime, timedelta
from backend.repositories.token_blacklist_repository import TokenBlacklistRepository
from backend.models.token_blacklist import TokenBlacklist


class TestTokenBlacklistRepository:
    """Complete test cases for TokenBlacklistRepository - 100% coverage."""

    @pytest.fixture
    def mock_token_data(self):
        return {
            "token": "jwt_token_123",
            "user_id": "user123",
            "expires_at": datetime.utcnow() + timedelta(hours=1)
        }

    @pytest.mark.asyncio
    async def test_add_to_blacklist_success(self, mock_db, mock_token_data):
        """Test add to blacklist - success."""
        mock_db.token_blacklist.insert_one = AsyncMock(return_value=AsyncMock(inserted_id="some_id"))

        repo = TokenBlacklistRepository()
        result = await repo.add_to_blacklist(
            mock_token_data["token"],
            mock_token_data["user_id"],
            mock_token_data["expires_at"]
        )

        assert result is True

    @pytest.mark.asyncio
    async def test_add_to_blacklist_failure(self, mock_db, mock_token_data):
        """Test add to blacklist - failure."""
        mock_db.token_blacklist.insert_one = AsyncMock(side_effect=Exception("Database error"))

        repo = TokenBlacklistRepository()
        result = await repo.add_to_blacklist(
            mock_token_data["token"],
            mock_token_data["user_id"],
            mock_token_data["expires_at"]
        )

        assert result is False

    @pytest.mark.asyncio
    async def test_is_blacklisted_true(self, mock_db, mock_token_data):
        """Test is blacklisted - returns True."""
        mock_db.token_blacklist.find_one = AsyncMock(return_value={"token": mock_token_data["token"]})

        repo = TokenBlacklistRepository()
        result = await repo.is_blacklisted(mock_token_data["token"])

        assert result is True

    @pytest.mark.asyncio
    async def test_is_blacklisted_false(self, mock_db):
        """Test is blacklisted - returns False."""
        mock_db.token_blacklist.find_one = AsyncMock(return_value=None)

        repo = TokenBlacklistRepository()
        result = await repo.is_blacklisted("unknown_token")

        assert result is False

    @pytest.mark.asyncio
    async def test_is_blacklisted_exception(self, mock_db):
        """Test is blacklisted - exception handled."""
        mock_db.token_blacklist.find_one = AsyncMock(side_effect=Exception("Database error"))

        repo = TokenBlacklistRepository()
        result = await repo.is_blacklisted("some_token")

        assert result is False

    @pytest.mark.asyncio
    async def test_clean_expired_tokens_success(self, mock_db):
        """Test clean expired tokens - success."""
        mock_db.token_blacklist.delete_many = AsyncMock(return_value=AsyncMock(deleted_count=5))

        repo = TokenBlacklistRepository()
        result = await repo.clean_expired_tokens()

        assert result == 5

    @pytest.mark.asyncio
    async def test_clean_expired_tokens_exception(self, mock_db):
        """Test clean expired tokens - exception handled."""
        mock_db.token_blacklist.delete_many = AsyncMock(side_effect=Exception("Database error"))

        repo = TokenBlacklistRepository()
        result = await repo.clean_expired_tokens()

        assert result == 0
