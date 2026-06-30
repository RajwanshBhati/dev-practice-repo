from typing import Optional
from datetime import datetime
from bson import ObjectId
from backend.middleware.database import db
from backend.models.user import User
import logging

logger = logging.getLogger(__name__)

class UserRepository:
    """Repository for user database operations"""

    def __init__(self):
        self.collection = db.get_db().users

    async def create(self, user: User) -> User:
        """Create a new user"""
        try:
            user_dict = user.model_dump(exclude={"id"}, by_alias=True)
            # Remove None values
            user_dict = {k: v for k, v in user_dict.items() if v is not None}
            result = await self.collection.insert_one(user_dict)
            user.id = str(result.inserted_id)
            return user
        except Exception as e:
            logger.error(f"Error creating user: {e}")
            raise

    async def find_by_email(self, email: str) -> Optional[User]:
        """Find user by email"""
        try:
            user_dict = await self.collection.find_one({"email": email})
            if user_dict:
                # Convert ObjectId to string
                if "_id" in user_dict:
                    user_dict["id"] = str(user_dict["_id"])
                    # Remove _id to avoid conflict
                    del user_dict["_id"]
                return User(**user_dict)
            return None
        except Exception as e:
            logger.error(f"Error finding user by email: {e}")
            raise

    async def find_by_id(self, user_id: str) -> Optional[User]:
        """Find user by ID"""
        try:
            if not ObjectId.is_valid(user_id):
                return None
            user_dict = await self.collection.find_one({"_id": ObjectId(user_id)})
            if user_dict:
                user_dict["id"] = str(user_dict["_id"])
                del user_dict["_id"]
                return User(**user_dict)
            return None
        except Exception as e:
            logger.error(f"Error finding user by ID: {e}")
            raise

    async def update(self, user_id: str, update_data: dict) -> Optional[User]:
        """Update user"""
        try:
            update_data["updated_at"] = datetime.utcnow()
            result = await self.collection.update_one(
                {"_id": ObjectId(user_id)},
                {"$set": update_data}
            )
            if result.modified_count > 0:
                return await self.find_by_id(user_id)
            return None
        except Exception as e:
            logger.error(f"Error updating user: {e}")
            raise

    async def update_last_login(self, user_id: str) -> bool:
        """Update last login timestamp"""
        try:
            result = await self.collection.update_one(
                {"_id": ObjectId(user_id)},
                {"$set": {"last_login": datetime.utcnow()}}
            )
            return result.modified_count > 0
        except Exception as e:
            logger.error(f"Error updating last login: {e}")
            return False

    async def deactivate_user(self, user_id: str) -> bool:
        """Deactivate user account"""
        try:
            from backend.enums.user_enums import UserStatus
            result = await self.collection.update_one(
                {"_id": ObjectId(user_id)},
                {"$set": {"status": UserStatus.INACTIVE, "updated_at": datetime.utcnow()}}
            )
            return result.modified_count > 0
        except Exception as e:
            logger.error(f"Error deactivating user: {e}")
            return False

    async def activate_user(self, user_id: str) -> bool:
        """Activate user account"""
        try:
            from backend.enums.user_enums import UserStatus
            result = await self.collection.update_one(
                {"_id": ObjectId(user_id)},
                {"$set": {"status": UserStatus.ACTIVE, "updated_at": datetime.utcnow()}}
            )
            return result.modified_count > 0
        except Exception as e:
            logger.error(f"Error activating user: {e}")
            return False
