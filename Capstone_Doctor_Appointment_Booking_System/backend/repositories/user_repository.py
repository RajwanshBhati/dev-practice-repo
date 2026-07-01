from typing import Optional
from datetime import datetime
from bson import ObjectId
from backend.middleware.database import db
from backend.models.user import User
import logging

logger = logging.getLogger(__name__)

class UserRepository:
    """Handles all database reads/writes for user accounts."""

    def __init__(self):
        self.collection = db.get_db().users

    async def create(self, user: User) -> User:
        """Insert a new user into the database and attach the generated ID back to it."""
        try:
            user_dict = user.model_dump(exclude={"id"}, by_alias=True)
            user_dict = {k: v for k, v in user_dict.items() if v is not None}
            result = await self.collection.insert_one(user_dict)
            user.id = str(result.inserted_id)
            return user
        except Exception as e:
            logger.error(f"Error creating user: {e}")
            raise

    async def find_by_email(self, email: str) -> Optional[User]:
        """Look up a user by their email address, used mainly during login."""
        try:
            user_dict = await self.collection.find_one({"email": email})
            if user_dict:
                if "_id" in user_dict:
                    user_dict["id"] = str(user_dict["_id"])
                    del user_dict["_id"]
                return User(**user_dict)
            return None
        except Exception as e:
            logger.error(f"Error finding user by email: {e}")
            raise

    async def find_by_id(self, user_id: str) -> Optional[User]:
        """Look up a user by their document ID."""
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
        """Apply a partial update to a user and return the refreshed document."""
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
        """Stamp the user's last login time to now, e.g. right after a successful login."""
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
        """Mark a user account as inactive, blocking further access without deleting their data."""
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
        """Mark a previously deactivated user account as active again."""
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
