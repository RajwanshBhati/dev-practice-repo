from app.models.user import User
from app.constants.roles import UserRole

class UserRepository:
    async def get_by_email(self, email: str):
        return await User.find_one(User.email == email)

    async def get_by_role(self, role: UserRole):
        return await User.find_one(User.role == role)

    async def get_all_by_role(self, role: UserRole):
        return await User.find(User.role == role).to_list()

    async def create_user(self, user: User):
        return await user.insert()

    async def update_user(self, user: User):
        from datetime import datetime
        user.updated_at = datetime.utcnow()
        return await user.save()

    async def get_all_users(self, skip: int = 0, limit: int = 100):
        return await User.find().skip(skip).limit(limit).to_list()

    async def count_users_by_role(self, role: UserRole):
        return await User.find(User.role == role).count()
