import os
import sys
from pathlib import Path
from typing import List
from dotenv import load_dotenv

backend_path = Path(__file__).parent.parent.parent.parent
if str(backend_path) not in sys.path:
    sys.path.insert(0, str(backend_path))

load_dotenv()

class Settings:
    """App configuration loaded from environment variables, with sensible defaults for local development."""
    APP_NAME: str = os.getenv("APP_NAME", "User Service")
    APP_VERSION: str = os.getenv("APP_VERSION", "1.0.0")
    DEBUG: bool = os.getenv("DEBUG", "True").lower() == "true"
    SERVICE_NAME: str = "user-service"
    SERVICE_PORT: int = int(os.getenv("SERVICE_PORT", "8001"))

    MONGODB_URL: str = os.getenv("MONGODB_URL", "mongodb://localhost:27017")
    DATABASE_NAME: str = os.getenv("DATABASE_NAME", "doctor_appointment_db")

    SECRET_KEY: str = os.getenv("SECRET_KEY", "sshh!_this_is_a_secret_key_for_jwt_token_generation")
    ALGORITHM: str = os.getenv("ALGORITHM", "HS256")
    ACCESS_TOKEN_EXPIRE_MINUTES: int = int(os.getenv("ACCESS_TOKEN_EXPIRE_MINUTES", "30"))
    REFRESH_TOKEN_EXPIRE_DAYS: int = int(os.getenv("REFRESH_TOKEN_EXPIRE_DAYS", "7"))

    BCRYPT_ROUNDS: int = int(os.getenv("BCRYPT_ROUNDS", "12"))

    LOG_LEVEL: str = os.getenv("LOG_LEVEL", "INFO")
    BACKEND_URL: str = os.getenv("BACKEND_URL", "http://localhost:8000")

    EMAIL_ENABLED: bool = os.getenv("EMAIL_ENABLED", "False").lower() == "true"
    SMTP_HOST: str = os.getenv("SMTP_HOST", "smtp.gmail.com")
    SMTP_PORT: int = int(os.getenv("SMTP_PORT", "587"))
    SMTP_USER: str = os.getenv("SMTP_USER", "")
    SMTP_PASSWORD: str = os.getenv("SMTP_PASSWORD", "")
    SMTP_USE_TLS: bool = os.getenv("SMTP_USE_TLS", "True").lower() == "true"
    EMAIL_FROM_NAME: str = os.getenv("EMAIL_FROM_NAME", "HealthBook Team")
    EMAIL_FROM_ADDRESS: str = os.getenv("EMAIL_FROM_ADDRESS", "") or SMTP_USER
    FRONTEND_URL: str = os.getenv("FRONTEND_URL", "http://localhost:5173")

    @property
    def ALLOWED_ORIGINS(self) -> List[str]:
        """Split the comma-separated ALLOWED_ORIGINS env value into a clean list of origins for CORS."""
        origins = os.getenv("ALLOWED_ORIGINS", "http://localhost:3000,http://localhost:5173,http://127.0.0.1:5173")
        return [origin.strip() for origin in origins.split(",")]

settings = Settings()
