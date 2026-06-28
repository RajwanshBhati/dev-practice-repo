from passlib.context import CryptContext
from .jwt_service import jwt_service
import logging

logger = logging.getLogger(__name__)

pwd_context = CryptContext(
    schemes=["bcrypt"],
    deprecated="auto"
)


class Security:
    """Security utilities for password hashing"""

    @staticmethod
    def hash_password(password: str) -> str:
        """Hash password using BCrypt"""
        return pwd_context.hash(password)

    @staticmethod
    def verify_password(
        plain_password: str,
        hashed_password: str
    ) -> bool:
        """Verify plain password against hashed password"""
        return pwd_context.verify(
            plain_password,
            hashed_password
        )


security = Security()
