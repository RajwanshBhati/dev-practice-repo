from motor.motor_asyncio import AsyncIOMotorClient
from beanie import init_beanie
from backend.middleware.config import settings

"""
AsyncIOMotorClient is used instead of the sync PyMongo client because the app
is built on FastAPI, which runs on an async event loop.
"""
client = AsyncIOMotorClient(settings.MONGODB_URL)
database = client[settings.DATABASE_NAME]


async def init_database():
    """Set up Beanie with our document models and make sure required indexes exist. Called once at app startup."""
    from backend.models.user import User

    await init_beanie(
        database=database,
        document_models=[User]
    )

    await User.ensure_indexes()
    print("User Service Database initialized successfully")
