import pytest
from datetime import datetime, timedelta
from unittest.mock import patch
from jose import jwt  # Changed from 'import jwt'
from backend.middleware.jwt_service import jwt_service
from backend.middleware.config import settings


class TestJWTService:
    """Test cases for JWTService class."""

    def test_create_access_token(self):
        """Test access token creation."""
        data = {"sub": "user123", "email": "test@example.com"}
        token = jwt_service.create_access_token(data)

        assert token is not None
        assert isinstance(token, str)

    def test_create_refresh_token(self):
        """Test refresh token creation."""
        data = {"sub": "user123", "email": "test@example.com"}
        token = jwt_service.create_refresh_token(data)

        assert token is not None
        assert isinstance(token, str)

    def test_create_reset_token(self):
        """Test reset token creation."""
        data = {"sub": "user123", "email": "test@example.com"}
        token = jwt_service.create_reset_token(data)

        assert token is not None
        assert isinstance(token, str)

    def test_decode_token_success(self):
        """Test token decoding - success."""
        data = {"sub": "user123", "email": "test@example.com"}
        token = jwt_service.create_access_token(data)
        decoded = jwt_service.decode_token(token)

        assert decoded["sub"] == "user123"
        assert decoded["email"] == "test@example.com"
        assert decoded["type"] == "access"

    def test_decode_token_invalid(self):
        """Test token decoding - invalid token."""
        with pytest.raises(ValueError, match="Invalid token"):
            jwt_service.decode_token("invalid.token.here")

    def test_decode_token_malformed(self):
        """Test token decoding - malformed token."""
        with pytest.raises(ValueError):
            jwt_service.decode_token("not_a_jwt_token")
