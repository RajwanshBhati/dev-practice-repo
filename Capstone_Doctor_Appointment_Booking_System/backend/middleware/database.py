from motor.motor_asyncio import AsyncIOMotorClient
from typing import Optional
from backend.middleware.config import settings
import logging

logger = logging.getLogger(__name__)

class Database:
    """
    Wraps the MongoDB connection as class-level state so the same client and
    db instance can be reused across the app instead of opening a new
    connection per request. AsyncIOMotorClient is used because the app runs
    on FastAPI's async event loop, and a sync driver would block it.
    """
    client: Optional[AsyncIOMotorClient] = None
    db = None

    @classmethod
    async def connect(cls):
        """Open the MongoDB connection, verify it's reachable, and set up indexes. Call this once on app startup."""
        try:
            cls.client = AsyncIOMotorClient(settings.MONGODB_URL)
            cls.db = cls.client[settings.DATABASE_NAME]

            await cls.client.admin.command('ping')

            await cls._create_indexes()

            logger.info(f"Connected to MongoDB: {settings.DATABASE_NAME}")
            return cls.db
        except Exception as e:
            logger.error(f"Failed to connect to MongoDB: {str(e)}")
            raise

    @classmethod
    async def disconnect(cls):
        """Close the MongoDB connection cleanly. Call this on app shutdown."""
        if cls.client:
            cls.client.close()
            logger.info("Disconnected from MongoDB")

    @classmethod
    async def _create_indexes(cls):
        """Set up the indexes we rely on for fast lookups, like unique email and role filtering."""
        try:
            await cls.db.users.create_index("email", unique=True)
            await cls.db.users.create_index("role")
            await cls.db.users.create_index("is_active")

            logger.info("Database indexes created")
        except Exception as e:
            logger.error(f"Failed to create indexes: {str(e)}")

    @classmethod
    def get_db(cls):
        """Return the active database instance. Raises if connect() hasn't been called yet."""
        if cls.db is None:
            raise ValueError("Database not initialized. Call connect() first")
        return cls.db

db = Database()
