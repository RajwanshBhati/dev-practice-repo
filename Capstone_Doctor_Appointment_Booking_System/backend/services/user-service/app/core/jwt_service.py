import jwt
from datetime import datetime, timedelta
from typing import Dict, Any, Optional
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
        except jwt.InvalidTokenError as e:
            raise ValueError(f"Invalid token: {str(e)}")
        except Exception as e:
            logger.error(f"Token decoding error: {str(e)}")
            raise

    @staticmethod
    def refresh_access_token(refresh_token: str) -> Dict[str, Any]:
        """Generate new access token using refresh token"""
        try:
            payload = JWTService.decode_token(refresh_token)

            # Validate token type
            if payload.get("type") != "refresh":
                raise ValueError("Invalid refresh token")

            # Create new access token
            token_data = {
                "sub": payload.get("sub"),
                "email": payload.get("email"),
                "role": payload.get("role")
            }
            new_access_token = JWTService.create_access_token(token_data)

            return {
                "access_token": new_access_token,
                "token_type": "bearer",
                "expires_in": settings.ACCESS_TOKEN_EXPIRE_MINUTES * 60
            }
        except ValueError as e:
            raise
        except Exception as e:
            logger.error(f"Token refresh error: {str(e)}")
            raise

jwt_service = JWTService()
