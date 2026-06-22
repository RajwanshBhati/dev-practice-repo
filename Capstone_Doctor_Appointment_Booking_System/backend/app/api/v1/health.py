from fastapi import APIRouter
from app.database.mongodb import database

router = APIRouter()


@router.get("/health")
async def health():
    return {
        "status": "success",
        "message": "Application is running"
    }


@router.get("/db-health")
async def db_health():
    await database.command("ping")

    return {
        "status": "success",
        "database": "connected"
    }
