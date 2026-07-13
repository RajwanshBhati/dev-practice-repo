import pytest
from backend.middleware.security import security


class TestSecurity:
    """Test cases for Security utility class."""

    def test_hash_password(self):
        """Test password hashing."""
        password = "Test@1234"
        hashed = security.hash_password(password)

        assert hashed is not None
        assert hashed != password
        assert len(hashed) > 20

    def test_hash_password_truncation(self):
        """Test password truncation for long passwords."""
        long_password = "A" * 100 + "Test@1234"
        hashed = security.hash_password(long_password)

        assert hashed is not None
        assert hashed != long_password

    def test_verify_password_success(self):
        """Test password verification - success."""
        password = "Test@1234"
        hashed = security.hash_password(password)

        assert security.verify_password(password, hashed) is True

    def test_verify_password_failure(self):
        """Test password verification - failure."""
        password = "Test@1234"
        wrong_password = "Wrong@1234"
        hashed = security.hash_password(password)

        assert security.verify_password(wrong_password, hashed) is False

    def test_verify_password_truncation(self):
        """Test password verification with long password."""
        password = "A" * 100 + "Test@1234"
        hashed = security.hash_password(password)

        assert security.verify_password(password, hashed) is True

    def test_create_access_token(self):
        """Test access token creation."""
        data = {"sub": "user123", "email": "test@example.com"}
        token = security.create_access_token(data)

        assert token is not None
        assert isinstance(token, str)
        assert len(token) > 20
