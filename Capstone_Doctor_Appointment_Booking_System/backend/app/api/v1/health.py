from fastapi import APIRouter
from app.database.mongodb import database

router = APIRouter()


@router.get("/health")
async def health_check():
    """Health check endpoint"""
    return {
        "status": "success",
        "message": "Application is running",
        "version": "1.0.0"
    }


@router.get("/db-health")
async def db_health_check():
    """Database health check endpoint"""
    await database.command("ping")
    return {
        "status": "success",
        "database": "connected"
    }
