import pytest
from unittest.mock import patch, MagicMock, AsyncMock
from backend.services.email_service import EmailService
from backend.middleware.config import settings


class TestEmailService:
    """Complete test cases for EmailService - 100% coverage."""

    @pytest.mark.asyncio
    async def test_dispatch_email_disabled(self):
        """Test dispatch - email disabled."""
        with patch('backend.services.email_service.settings') as mock_settings:
            mock_settings.EMAIL_ENABLED = False
            mock_settings.SMTP_USER = ""
            mock_settings.SMTP_PASSWORD = ""

            result = await EmailService._dispatch("test@example.com", "Subject", "Body")

            assert result is True

    @pytest.mark.asyncio
    async def test_dispatch_email_enabled_success(self):
        """Test dispatch - email enabled success."""
        with patch('backend.services.email_service.settings') as mock_settings:
            mock_settings.EMAIL_ENABLED = True
            mock_settings.SMTP_USER = "user"
            mock_settings.SMTP_PASSWORD = "pass"
            mock_settings.EMAIL_FROM_NAME = "Test"
            mock_settings.EMAIL_FROM_ADDRESS = "test@example.com"

            with patch('backend.services.email_service.asyncio.to_thread') as mock_to_thread:
                mock_to_thread.return_value = True

                result = await EmailService._dispatch("test@example.com", "Subject", "Body")

                assert result is True

    @pytest.mark.asyncio
    async def test_dispatch_email_enabled_failure(self):
        """Test dispatch - email enabled failure."""
        with patch('backend.services.email_service.settings') as mock_settings:
            mock_settings.EMAIL_ENABLED = True
            mock_settings.SMTP_USER = "user"
            mock_settings.SMTP_PASSWORD = "pass"
            mock_settings.EMAIL_FROM_NAME = "Test"
            mock_settings.EMAIL_FROM_ADDRESS = "test@example.com"

            with patch('backend.services.email_service.asyncio.to_thread') as mock_to_thread:
                mock_to_thread.side_effect = Exception("SMTP error")

                result = await EmailService._dispatch("test@example.com", "Subject", "Body")

                assert result is False

    @pytest.mark.asyncio
    async def test_send_doctor_approval_email_approved(self):
        """Test send doctor approval email - approved."""
        with patch('backend.services.email_service.EmailService._dispatch') as mock_dispatch:
            mock_dispatch.return_value = True

            result = await EmailService.send_doctor_approval_email(
                "doctor@example.com",
                "Smith",
                "APPROVED"
            )

            assert result is True
            mock_dispatch.assert_called_once()

    @pytest.mark.asyncio
    async def test_send_doctor_approval_email_rejected(self):
        """Test send doctor approval email - rejected."""
        with patch('backend.services.email_service.EmailService._dispatch') as mock_dispatch:
            mock_dispatch.return_value = True

            result = await EmailService.send_doctor_approval_email(
                "doctor@example.com",
                "Smith",
                "REJECTED",
                "Insufficient credentials"
            )

            assert result is True
            mock_dispatch.assert_called_once()

    @pytest.mark.asyncio
    async def test_send_account_created_email(self):
        """Test send account created email."""
        with patch('backend.services.email_service.EmailService._dispatch') as mock_dispatch:
            mock_dispatch.return_value = True

            result = await EmailService.send_account_created_email(
                "user@example.com",
                "John Doe",
                "Doctor"
            )

            assert result is True

    @pytest.mark.asyncio
    async def test_send_password_reset_email(self):
        """Test send password reset email."""
        with patch('backend.services.email_service.EmailService._dispatch') as mock_dispatch:
            mock_dispatch.return_value = True

            result = await EmailService.send_password_reset_email(
                "user@example.com",
                "John Doe",
                "reset_token_123"
            )

            assert result is True

    @pytest.mark.asyncio
    async def test_send_welcome_email(self):
        """Test send welcome email."""
        with patch('backend.services.email_service.EmailService._dispatch') as mock_dispatch:
            mock_dispatch.return_value = True

            result = await EmailService.send_welcome_email(
                "user@example.com",
                "John Doe",
                "Patient"
            )

            assert result is True
