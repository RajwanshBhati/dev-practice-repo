from passlib.context import CryptContext
from datetime import datetime, timedelta
from typing import Dict, Any, Optional
from jose import jwt
from backend.middleware.config import settings
import logging

logger = logging.getLogger(__name__)

pwd_context = CryptContext(
    schemes=["bcrypt"],
    deprecated="auto"
)

class Security:
    """Password hashing/verification helpers, plus a convenience method for issuing access tokens."""

    @staticmethod
    def hash_password(password: str) -> str:
        """
        Hash a plain-text password with bcrypt. Bcrypt silently ignores
        anything past 72 bytes, so we truncate first to avoid surprises,
        and fall back to pbkdf2_sha256 if bcrypt hashing fails for some reason.
        """
        try:
            if len(password.encode('utf-8')) > 72:
                password = password[:72]
            return pwd_context.hash(password)
        except Exception as e:
            logger.error(f"Password hashing error: {e}")
            from passlib.hash import pbkdf2_sha256
            return pbkdf2_sha256.hash(password)

    @staticmethod
    def verify_password(plain_password: str, hashed_password: str) -> bool:
        """Check a plain-text password against its hash, with the same 72-byte truncation and a pbkdf2 fallback."""
        try:
            if len(plain_password.encode('utf-8')) > 72:
                plain_password = plain_password[:72]
            return pwd_context.verify(plain_password, hashed_password)
        except Exception as e:
            logger.error(f"Password verification error: {e}")
            try:
                from passlib.hash import pbkdf2_sha256
                return pbkdf2_sha256.verify(plain_password, hashed_password)
            except:
                return False

    @staticmethod
    def create_access_token(data: Dict[str, Any]) -> str:
        """Create a short-lived JWT access token for an authenticated user."""
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
