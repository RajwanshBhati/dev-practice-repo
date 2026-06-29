import sys
from pathlib import Path

# Add backend to path at the very top
backend_path = Path(__file__).parent.parent.parent.parent
if str(backend_path) not in sys.path:
    sys.path.insert(0, str(backend_path))

from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from contextlib import asynccontextmanager
from app.core.database import db
from app.core.config import settings
from app.routes import auth_router, user_router
import logging
from datetime import datetime

# Configure logging
logging.basicConfig(
    level=getattr(logging, settings.LOG_LEVEL),
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

@asynccontextmanager
async def lifespan(app: FastAPI):
    # Startup
    logger.info(f"Starting {settings.APP_NAME} v{settings.APP_VERSION}")
    await db.connect()
    yield
    # Shutdown
    logger.info(f"Shutting down {settings.APP_NAME}")
    await db.disconnect()

# Create FastAPI app
app = FastAPI(
    title=settings.APP_NAME,
    description="Authentication and User Management Service",
    version=settings.APP_VERSION,
    docs_url="/docs" if settings.DEBUG else None,
    redoc_url="/redoc" if settings.DEBUG else None,
    lifespan=lifespan
)

# CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.ALLOWED_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Exception handlers
@app.exception_handler(ValueError)
async def value_error_handler(request: Request, exc: ValueError):
    return JSONResponse(
        status_code=400,
        content={"success": False, "message": str(exc)}
    )

@app.exception_handler(Exception)
async def general_exception_handler(request: Request, exc: Exception):
    logger.error(f"Unhandled exception: {str(exc)}")
    return JSONResponse(
        status_code=500,
        content={"success": False, "message": "Internal server error"}
    )

# Include routers
app.include_router(auth_router)
app.include_router(user_router)

@app.get("/health")
async def health_check():
    return {
        "status": "healthy",
        "service": settings.SERVICE_NAME,
        "version": settings.APP_VERSION,
        "timestamp": datetime.utcnow().isoformat()
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        "app.main:app",
        host="0.0.0.0",
        port=settings.SERVICE_PORT,
        reload=settings.DEBUG
    )
