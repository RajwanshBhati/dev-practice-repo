import os
import pytest
from backend.middleware.config import Settings, settings

class TestConfig:
    def test_settings_default_values(self):
        assert settings.APP_NAME == "Doctor Appointment System"
        assert settings.APP_VERSION == "1.0.0"
        assert settings.SERVICE_NAME == "user-service"
        assert settings.MONGODB_URL == "mongodb+srv://rajwanshbhati_db_user:Rajwansh%40123@cluster0.uu1txbn.mongodb.net/?appName=Cluster0"
        assert settings.DATABASE_NAME == "doctor_appointment_db_prod"
        assert settings.SECRET_KEY is not None
        assert settings.ALGORITHM == "HS256"
        assert settings.ACCESS_TOKEN_EXPIRE_MINUTES == 30
        assert settings.REFRESH_TOKEN_EXPIRE_DAYS == 7
        assert settings.BCRYPT_ROUNDS == 12
        assert settings.BACKEND_URL == "http://localhost:8000"
        assert settings.SMTP_HOST == "smtp.gmail.com"
        assert settings.SMTP_PORT == 587
        assert settings.EMAIL_FROM_NAME == "HealthBook Team"
        assert settings.SERVICE_PORT == 8000

    def test_settings_debug_true(self):
        assert settings.DEBUG is False

    def test_settings_email_enabled_false(self):
        assert settings.EMAIL_ENABLED is True

    def test_settings_smtp_use_tls_true(self):
        assert settings.SMTP_USE_TLS is True

    def test_allowed_origins_property(self):
        origins = settings.ALLOWED_ORIGINS
        assert isinstance(origins, list)
        assert "http://localhost:5173" in origins

    def test_settings_with_env_variables(self, monkeypatch):
        monkeypatch.setenv("APP_NAME", "Doctor Appointment System")
        monkeypatch.setenv("DEBUG", "false")
        monkeypatch.setenv("SERVICE_PORT", "8000")
        monkeypatch.setenv("ACCESS_TOKEN_EXPIRE_MINUTES", "30")
        monkeypatch.setenv("EMAIL_ENABLED", "true")
        monkeypatch.setenv("ALLOWED_ORIGINS", "http://test.com,http://test2.com")

        test_settings = Settings()
        assert test_settings.APP_NAME == "Doctor Appointment System"
        assert test_settings.DEBUG is False
        assert test_settings.SERVICE_PORT ==8000
        assert test_settings.ACCESS_TOKEN_EXPIRE_MINUTES == 30
        assert test_settings.EMAIL_ENABLED is True
        assert "http://test.com" in test_settings.ALLOWED_ORIGINS
        assert "http://test2.com" in test_settings.ALLOWED_ORIGINS

    def test_email_from_address_fallback(self, monkeypatch):
        monkeypatch.setenv("EMAIL_FROM_ADDRESS", "")
        monkeypatch.setenv("SMTP_USER", "rajwansh03112004@gmail.com")
        test_settings = Settings()
        assert test_settings.EMAIL_FROM_ADDRESS == "rajwansh03112004@gmail.com"

    def test_int_conversion(self, monkeypatch):
        monkeypatch.setenv("SERVICE_PORT", "8000")
        monkeypatch.setenv("BCRYPT_ROUNDS", "12")
        monkeypatch.setenv("SMTP_PORT", "587")

        test_settings = Settings()
        assert test_settings.SERVICE_PORT == 8000
        assert test_settings.BCRYPT_ROUNDS == 12
        assert test_settings.SMTP_PORT == 587

    def test_allowed_origins_strip_spaces(self, monkeypatch):
        monkeypatch.setenv("ALLOWED_ORIGINS", "http://test.com, http://test2.com , http://test3.com")
        test_settings = Settings()
        assert "http://test.com" in test_settings.ALLOWED_ORIGINS
        assert "http://test2.com" in test_settings.ALLOWED_ORIGINS
        assert "http://test3.com" in test_settings.ALLOWED_ORIGINS
