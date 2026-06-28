from motor.motor_asyncio import AsyncIOMotorClient
from beanie import init_beanie
from app.core.config import settings

# MongoDB Client
client = AsyncIOMotorClient(settings.MONGODB_URL)
database = client[settings.DATABASE_NAME]

async def init_database():
    """Initialize database and create indexes"""
    from app.models.user import User

    await init_beanie(
        database=database,
        document_models=[User]
    )

    # Create indexes
    await User.ensure_indexes()
    print("User Service Database initialized successfully")
