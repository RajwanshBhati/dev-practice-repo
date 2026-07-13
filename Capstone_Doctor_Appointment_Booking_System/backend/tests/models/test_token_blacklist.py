import pytest
from backend.models.token_blacklist import TokenBlacklist
from datetime import datetime, timedelta


class TestTokenBlacklist:
    def test_token_blacklist_creation(self):
        """Test creating a token blacklist entry."""
        expires_at = datetime.now() + timedelta(hours=1)

        entry = TokenBlacklist(
            token="jwt_token_123",
            user_id="user123",
            expires_at=expires_at
        )

        assert entry.token == "jwt_token_123"
        assert entry.user_id == "user123"
        assert isinstance(entry.blacklisted_at, datetime)
