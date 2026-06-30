from backend.repositories.user_repository import UserRepository
from backend.constants.error_messages import ErrorMessages
import logging

logger = logging.getLogger(__name__)

class UserService:
    """User management service"""

    def __init__(self):
        self.user_repo = UserRepository()

    async def get_user_by_id(self, user_id: str):
        """Get user by ID"""
        user = await self.user_repo.find_by_id(user_id)
        if not user:
            raise ValueError(ErrorMessages.USER_1101)
        return user

    async def get_user_by_email(self, email: str):
        """Get user by email"""
        user = await self.user_repo.find_by_email(email)
        if not user:
            raise ValueError(ErrorMessages.USER_1101)
        return user

    async def update_user(self, user_id: str, update_data: dict):
        """Update user information"""
        user = await self.user_repo.update(user_id, update_data)
        if not user:
            raise ValueError(ErrorMessages.USER_1101)
        return user

    async def deactivate_user(self, user_id: str):
        """Deactivate user account"""
        user = await self.user_repo.update(user_id, {"status": "INACTIVE"})
        if not user:
            raise ValueError(ErrorMessages.USER_1101)
        return user

    async def activate_user(self, user_id: str):
        """Activate user account"""
        user = await self.user_repo.update(user_id, {"status": "ACTIVE"})
        if not user:
            raise ValueError(ErrorMessages.USER_1101)
        return user
