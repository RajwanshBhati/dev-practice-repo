from typing import Optional, List
from app.models.user import User
from app.constants.roles import UserRole
from beanie import PydanticObjectId


class UserRepository:
    """Repository for User model operations"""

    async def get_by_email(self, email: str) -> Optional[User]:
        """Get user by email"""
        return await User.find_one(User.email == email)

    async def get_by_id(self, user_id: str) -> Optional[User]:
        """Get user by ID"""
        try:
            return await User.get(PydanticObjectId(user_id))
        except:
            return None

    async def get_by_role(self, role: UserRole) -> Optional[User]:
        """Get first user with specific role"""
        return await User.find_one(User.role == role)

    async def get_all_by_role(self, role: UserRole) -> List[User]:
        """Get all users with specific role"""
        return await User.find(User.role == role).to_list()

    async def create_user(self, user: User) -> User:
        """Create a new user"""
        return await user.insert()

    async def update_user(self, user: User) -> User:
        """Update user"""
        from datetime import datetime
        user.updated_at = datetime.utcnow()
        return await user.save()

    async def get_all_users(self, skip: int = 0, limit: int = 100) -> List[User]:
        """Get all users with pagination"""
        return await User.find().skip(skip).limit(limit).to_list()

    async def count_users_by_role(self, role: UserRole) -> int:
        """Count users by role"""
        return await User.find(User.role == role).count()
