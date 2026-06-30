from datetime import datetime
from backend.middleware.database import db
from backend.models.token_blacklist import TokenBlacklist
import logging

logger = logging.getLogger(__name__)

class TokenBlacklistRepository:
    """Repository for token blacklist operations"""

    def __init__(self):
        self.collection = db.get_db().token_blacklist

    async def add_to_blacklist(self, token: str, user_id: str, expires_at: datetime) -> bool:
        """Add token to blacklist"""
        try:
            blacklist_entry = TokenBlacklist(
                token=token,
                user_id=user_id,
                expires_at=expires_at
            )
            blacklist_dict = blacklist_entry.model_dump(exclude={"id"}, by_alias=True)
            result = await self.collection.insert_one(blacklist_dict)
            return True
        except Exception as e:
            logger.error(f"Error adding token to blacklist: {e}")
            return False

    async def is_blacklisted(self, token: str) -> bool:
        """Check if token is blacklisted"""
        try:
            entry = await self.collection.find_one({"token": token})
            return entry is not None
        except Exception as e:
            logger.error(f"Error checking token blacklist: {e}")
            return False

    async def clean_expired_tokens(self) -> int:
        """Remove expired tokens from blacklist"""
        try:
            result = await self.collection.delete_many({
                "expires_at": {"$lt": datetime.utcnow()}
            })
            return result.deleted_count
        except Exception as e:
            logger.error(f"Error cleaning expired tokens: {e}")
            return 0
