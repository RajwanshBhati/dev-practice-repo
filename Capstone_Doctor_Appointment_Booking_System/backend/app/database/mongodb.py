from motor.motor_asyncio import AsyncIOMotorClient
from app.core.config import settings

# Create MongoDB client
client = AsyncIOMotorClient(settings.MONGODB_URL)

# Get database
database = client[settings.DATABASE_NAME]

# Function to get database instance
def get_database():
    return database
