from fastapi import Request, HTTPException, Depends
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from jose import jwt, JWTError
from typing import Optional
from app.core.config import settings
from app.models.user import User
from app.constants.roles import UserRole


class JWTBearer(HTTPBearer):
    """JWT Bearer authentication class"""

    def __init__(self, auto_error: bool = True):
        super(JWTBearer, self).__init__(auto_error=auto_error)

    async def __call__(self, request: Request):
        credentials: HTTPAuthorizationCredentials = await super(JWTBearer, self).__call__(request)
        if credentials:
            if not credentials.scheme == "Bearer":
                raise HTTPException(status_code=403, detail="Invalid authentication scheme.")
            if not self.verify_jwt(credentials.credentials):
                raise HTTPException(status_code=403, detail="Invalid token or expired token.")
            return credentials.credentials
        else:
            raise HTTPException(status_code=403, detail="Invalid authorization code.")

    def verify_jwt(self, jwtoken: str) -> bool:
        """Verify JWT token"""
        try:
            jwt.decode(jwtoken, settings.JWT_SECRET_KEY, algorithms=[settings.JWT_ALGORITHM])
            return True
        except JWTError:
            return False


async def get_current_user(token: str = Depends(JWTBearer())) -> User:
    """Get current user from JWT token"""
    try:
        payload = jwt.decode(
            token,
            settings.JWT_SECRET_KEY,
            algorithms=[settings.JWT_ALGORITHM]
        )

        email = payload.get("email")
        if not email:
            raise HTTPException(status_code=401, detail="Email not found in token")

        from app.repositories.user_repository import UserRepository
        user_repo = UserRepository()
        user = await user_repo.get_by_email(email)

        if not user:
            raise HTTPException(status_code=401, detail="User not found")

        if not user.is_active:
            raise HTTPException(status_code=401, detail="Account is deactivated")

        return user

    except JWTError:
        raise HTTPException(status_code=401, detail="Invalid authentication credentials")


async def require_admin(current_user: User = Depends(get_current_user)) -> User:
    """Require admin role"""
    if current_user.role != UserRole.ADMIN:
        raise HTTPException(
            status_code=403,
            detail="Admin access required"
        )
    return current_user


async def require_doctor_or_admin(current_user: User = Depends(get_current_user)) -> User:
    """Require doctor or admin role"""
    if current_user.role not in [UserRole.DOCTOR, UserRole.ADMIN]:
        raise HTTPException(
            status_code=403,
            detail="Doctor or Admin access required"
        )
    return current_user


async def require_patient(current_user: User = Depends(get_current_user)) -> User:
    """Require patient role"""
    if current_user.role != UserRole.PATIENT:
        raise HTTPException(
            status_code=403,
            detail="Patient access required"
        )
    return current_user
