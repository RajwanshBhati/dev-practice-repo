from datetime import datetime, timedelta, UTC
from jose import jwt
from typing import Dict, Any
from app.core.config import settings

def create_access_token(data: Dict[str, Any]) -> str:
    """Create JWT access token"""
    to_encode = data.copy()
    expire = datetime.now(UTC) + timedelta(minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES)
    to_encode.update({"exp": expire})
    return jwt.encode(to_encode, settings.JWT_SECRET_KEY, algorithm=settings.JWT_ALGORITHM)

def decode_token(token: str) -> Dict[str, Any]:
    """Decode JWT token"""
    return jwt.decode(token, settings.JWT_SECRET_KEY, algorithms=[settings.JWT_ALGORITHM])

def verify_token(token: str) -> bool:
    """Verify JWT token"""
    try:
        decode_token(token)
        return True
    except:
        return False

