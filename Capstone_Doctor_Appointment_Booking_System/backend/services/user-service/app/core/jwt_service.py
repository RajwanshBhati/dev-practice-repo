from jose import jwt
from datetime import datetime, timedelta
from typing import Dict, Any
from app.core.config import settings
import logging

logger = logging.getLogger(__name__)

class JWTService:
    """Enhanced JWT service with refresh token support"""

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

    @staticmethod
    def create_refresh_token(data: Dict[str, Any]) -> str:
        """Create JWT refresh token"""
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
    def decode_token(token: str) -> Dict[str, Any]:
        """Decode and validate JWT token"""
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
