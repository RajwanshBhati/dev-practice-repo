import pytest
from backend.models.admin import AdminAuditLog, SystemSettings
from datetime import datetime


class TestAdminAuditLog:
    def test_admin_audit_log_creation(self):
        """Test creating admin audit log."""
        log = AdminAuditLog(
            admin_id="admin123",
            admin_email="admin@example.com",
            action="APPROVE_DOCTOR",
            target_id="doc123",
            target_email="doctor@example.com",
            details={"reason": "Approved"}
        )

        assert log.admin_id == "admin123"
        assert log.action == "APPROVE_DOCTOR"
        assert isinstance(log.created_at, datetime)


class TestSystemSettings:
    def test_system_settings_creation(self):
        """Test creating system settings."""
        settings = SystemSettings(
            key="app_config",
            value={"theme": "dark", "notifications": True},
            description="App configuration"
        )

        assert settings.key == "app_config"
        assert settings.value["theme"] == "dark"
        assert isinstance(settings.created_at, datetime)
