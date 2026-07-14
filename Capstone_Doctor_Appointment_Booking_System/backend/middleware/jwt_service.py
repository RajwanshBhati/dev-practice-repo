from jose import jwt
from datetime import datetime, timedelta
from typing import Dict, Any
from backend.middleware.config import settings
import logging

logger = logging.getLogger(__name__)

class JWTService:
    """Handles creating and decoding JWT access and refresh tokens."""

    @staticmethod
    def create_access_token(data: Dict[str, Any]) -> str:
        """Create a short-lived access token used to authenticate normal API requests."""
        to_encode = data.copy()
        expire = datetime.utcnow() + timedelta(minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES)

        to_encode.update({
            "exp": expire,
            "iat": datetime.utcnow(),
            "type": "access"
        })

        token = jwt.encode(to_encode, settings.SECRET_KEY, algorithm=settings.ALGORITHM)
        return token

    @staticmethod
    def create_refresh_token(data: Dict[str, Any]) -> str:
        """Create a longer-lived refresh token used to get new access tokens without re-login."""
        to_encode = data.copy()
        expire = datetime.utcnow() + timedelta(days=settings.REFRESH_TOKEN_EXPIRE_DAYS)

        to_encode.update({
            "exp": expire,
            "iat": datetime.utcnow(),
            "type": "refresh"
        })

        token = jwt.encode(to_encode, settings.SECRET_KEY, algorithm=settings.ALGORITHM)
        return token

    @staticmethod
    def create_reset_token(data: Dict[str, Any]) -> str:
        """Create a short-lived token (30 min) used only for password-reset links."""
        to_encode = data.copy()
        expire = datetime.utcnow() + timedelta(minutes=30)

        to_encode.update({
            "exp": expire,
            "iat": datetime.utcnow(),
            "type": "reset"
        })

        token = jwt.encode(to_encode, settings.SECRET_KEY, algorithm=settings.ALGORITHM)
        return token

    @staticmethod
    def decode_token(token: str) -> Dict[str, Any]:
        """Decode a token and verify its signature/expiry. Raises ValueError if it's expired or invalid."""
        try:
            payload = jwt.decode(token, settings.SECRET_KEY, algorithms=[settings.ALGORITHM])
            return payload
        except jwt.ExpiredSignatureError:
            raise ValueError("Token has expired")
        except jwt.JWTError as e:
            raise ValueError(f"Invalid token: {str(e)}")
        except Exception as e:
            logger.error(f"Token decoding error: {str(e)}")
            raise

jwt_service = JWTService()
