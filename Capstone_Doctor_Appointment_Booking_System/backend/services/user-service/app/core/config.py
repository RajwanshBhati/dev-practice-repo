import os
import sys
from pathlib import Path
from typing import List
from dotenv import load_dotenv

# Add backend to path
backend_path = Path(__file__).parent.parent.parent.parent
if str(backend_path) not in sys.path:
    sys.path.insert(0, str(backend_path))

load_dotenv()

class Settings:
    # Application
    APP_NAME: str = os.getenv("APP_NAME", "User Service")
    APP_VERSION: str = os.getenv("APP_VERSION", "1.0.0")
    DEBUG: bool = os.getenv("DEBUG", "True").lower() == "true"
    SERVICE_NAME: str = "user-service"
    SERVICE_PORT: int = int(os.getenv("SERVICE_PORT", "8001"))

    # MongoDB
    MONGODB_URL: str = os.getenv("MONGODB_URL", "mongodb://localhost:27017")
    DATABASE_NAME: str = os.getenv("DATABASE_NAME", "doctor_appointment_db")

    # JWT
    SECRET_KEY: str = os.getenv("SECRET_KEY", "your-secret-key-change-in-production")
    ALGORITHM: str = os.getenv("ALGORITHM", "HS256")
    ACCESS_TOKEN_EXPIRE_MINUTES: int = int(os.getenv("ACCESS_TOKEN_EXPIRE_MINUTES", "30"))
    REFRESH_TOKEN_EXPIRE_DAYS: int = int(os.getenv("REFRESH_TOKEN_EXPIRE_DAYS", "7"))

    # Security
    BCRYPT_ROUNDS: int = int(os.getenv("BCRYPT_ROUNDS", "12"))

    # Logging
    LOG_LEVEL: str = os.getenv("LOG_LEVEL", "INFO")

    @property
    def ALLOWED_ORIGINS(self) -> List[str]:
        """Parse CORS origins from environment variable"""
        origins = os.getenv("ALLOWED_ORIGINS", "http://localhost:3000")
        return [origin.strip() for origin in origins.split(",")]

settings = Settings()
