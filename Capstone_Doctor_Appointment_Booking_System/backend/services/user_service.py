from backend.repositories.user_repository import UserRepository
from backend.constants.error_messages import ErrorMessages
import logging

logger = logging.getLogger(__name__)

class UserService:
    """Simple wrapper around UserRepository for common user lookups and status changes, used by non-auth routes."""

    def __init__(self):
        self.user_repo = UserRepository()

    async def get_user_by_id(self, user_id: str):
        """Fetch a user by ID, raising if they don't exist."""
        user = await self.user_repo.find_by_id(user_id)
        if not user:
            raise ValueError(ErrorMessages.USER_1101)
        return user

    async def get_user_by_email(self, email: str):
        """Fetch a user by email, raising if they don't exist."""
        user = await self.user_repo.find_by_email(email)
        if not user:
            raise ValueError(ErrorMessages.USER_1101)
        return user

    async def update_user(self, user_id: str, update_data: dict):
        """Apply a partial update to a user's fields."""
        user = await self.user_repo.update(user_id, update_data)
        if not user:
            raise ValueError(ErrorMessages.USER_1101)
        return user

    async def deactivate_user(self, user_id: str):
        """Mark a user's account inactive."""
        user = await self.user_repo.update(user_id, {"status": "INACTIVE"})
        if not user:
            raise ValueError(ErrorMessages.USER_1101)
        return user

    async def activate_user(self, user_id: str):
        """Mark a user's account active."""
        user = await self.user_repo.update(user_id, {"status": "ACTIVE"})
        if not user:
            raise ValueError(ErrorMessages.USER_1101)
        return user
