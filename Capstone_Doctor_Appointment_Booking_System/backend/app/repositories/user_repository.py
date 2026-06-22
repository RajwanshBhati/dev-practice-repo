from app.models.user import User


class UserRepository:

    async def get_by_email(
        self,
        email: str
    ):
        return await User.find_one(
            User.email == email
        )

    async def create_user(
        self,
        user: User
    ):
        return await user.insert()
