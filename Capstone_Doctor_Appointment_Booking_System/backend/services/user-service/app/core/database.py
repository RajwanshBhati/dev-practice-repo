from motor.motor_asyncio import AsyncIOMotorClient
from typing import Optional
from app.core.config import settings
import logging

logger = logging.getLogger(__name__)

class Database:
    client: Optional[AsyncIOMotorClient] = None
    db = None

    @classmethod
    async def connect(cls):
        """Connect to MongoDB"""
        try:
            cls.client = AsyncIOMotorClient(settings.MONGODB_URL)
            cls.db = cls.client[settings.DATABASE_NAME]

            # Test connection
            await cls.client.admin.command('ping')

            # Create indexes
            await cls._create_indexes()

            logger.info(f"Connected to MongoDB: {settings.DATABASE_NAME}")
            return cls.db
        except Exception as e:
            logger.error(f"Failed to connect to MongoDB: {str(e)}")
            raise

    @classmethod
    async def disconnect(cls):
        """Disconnect from MongoDB"""
        if cls.client:
            cls.client.close()
            logger.info("Disconnected from MongoDB")

    @classmethod
    async def _create_indexes(cls):
        """Create indexes for better performance"""
        try:
            # Users collection indexes
            await cls.db.users.create_index("email", unique=True)
            await cls.db.users.create_index("role")
            await cls.db.users.create_index("is_active")

            logger.info("Database indexes created")
        except Exception as e:
            logger.error(f"Failed to create indexes: {str(e)}")

    @classmethod
    def get_db(cls):
        """Get database instance"""
        if cls.db is None:
            raise ValueError("Database not initialized. Call connect() first")
        return cls.db

db = Database()
