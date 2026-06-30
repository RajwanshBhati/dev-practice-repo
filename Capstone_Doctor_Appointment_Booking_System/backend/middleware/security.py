from passlib.context import CryptContext
from datetime import datetime, timedelta
from typing import Dict, Any, Optional
from jose import jwt
from backend.middleware.config import settings
import logging

logger = logging.getLogger(__name__)

# Create password context with bcrypt
pwd_context = CryptContext(
    schemes=["bcrypt"],
    deprecated="auto"
)

class Security:
    """Security utilities for password hashing"""

    @staticmethod
    def hash_password(password: str) -> str:
        """Hash password using BCrypt with length check"""
        try:
            # BCrypt can only handle up to 72 bytes
            if len(password.encode('utf-8')) > 72:
                password = password[:72]
            return pwd_context.hash(password)
        except Exception as e:
            logger.error(f"Password hashing error: {e}")
            # Fallback - use pbkdf2_sha256
            from passlib.hash import pbkdf2_sha256
            return pbkdf2_sha256.hash(password)

    @staticmethod
    def verify_password(plain_password: str, hashed_password: str) -> bool:
        """Verify plain password against hashed password"""
        try:
            # BCrypt can only handle up to 72 bytes
            if len(plain_password.encode('utf-8')) > 72:
                plain_password = plain_password[:72]
            return pwd_context.verify(plain_password, hashed_password)
        except Exception as e:
            logger.error(f"Password verification error: {e}")
            # Try fallback verification
            try:
                from passlib.hash import pbkdf2_sha256
                return pbkdf2_sha256.verify(plain_password, hashed_password)
            except:
                return False

    @staticmethod
    def create_access_token(data: Dict[str, Any]) -> str:
        """Create JWT access token"""
        to_encode = data.copy()
        expire = datetime.utcnow() + timedelta(minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES)

        to_encode.update({
            "exp": expire,
            "iat": datetime.utcnow(),
            "type": "access"
        })

        token = jwt.encode(to_encode, settings.SECRET_KEY, algorithm=settings.ALGORITHM)
        return token

security = Security()
