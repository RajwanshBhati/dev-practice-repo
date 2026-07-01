from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from contextlib import asynccontextmanager
from backend.middleware.database import db
from backend.middleware.config import settings
from backend.routers import auth_router, user_router, protected_router, admin_router, doctor_router
import logging
from datetime import datetime

logging.basicConfig(
    level=getattr(logging, settings.LOG_LEVEL),
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

@asynccontextmanager
async def lifespan(app: FastAPI):
    """
    Runs once when the app starts and once when it shuts down. On startup,
    connects to the database and warns in the logs if no admin account
    exists yet. On shutdown, closes the database connection cleanly.
    """
    logger.info(f"Starting {settings.APP_NAME} v{settings.APP_VERSION}")
    await db.connect()

    from backend.services.admin_service import AdminService
    admin_service = AdminService()
    exists = await admin_service.check_first_admin_exists()
    if not exists:
        logger.warning("No admin found! Please create the first admin at /api/v1/admin/setup-first-admin")

    yield
    logger.info(f"Shutting down {settings.APP_NAME}")
    await db.disconnect()

app = FastAPI(
    title=settings.APP_NAME,
    description="Authentication and User Management Service with Doctor Approval",
    version=settings.APP_VERSION,
    docs_url="/docs",
    redoc_url="/redoc",
    lifespan=lifespan,
    openapi_tags=[
        {
            "name": "authentication",
            "description": "Authentication endpoints - Register, Login, Logout"
        },
        {
            "name": "users",
            "description": "User management endpoints"
        },
        {
            "name": "protected",
            "description": "Protected endpoints - Role & Permission based access"
        },
        {
            "name": "admin",
            "description": "Admin endpoints - Doctor approval, Admin management"
        },
        {
            "name": "default",
            "description": "Default endpoints - Health check"
        }
    ]
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.ALLOWED_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.exception_handler(ValueError)
async def value_error_handler(request: Request, exc: ValueError):
    """Turn any uncaught ValueError (used throughout the app for expected business-logic errors) into a clean 400 response."""
    return JSONResponse(
        status_code=400,
        content={"success": False, "message": str(exc)}
    )

@app.exception_handler(Exception)
async def general_exception_handler(request: Request, exc: Exception):
    """Catch-all for anything unexpected, so the client always gets a JSON response instead of a raw stack trace."""
    logger.error(f"Unhandled exception: {str(exc)}")
    return JSONResponse(
        status_code=500,
        content={"success": False, "message": "Internal server error"}
    )

app.include_router(auth_router)
app.include_router(user_router)
app.include_router(protected_router)
app.include_router(admin_router)
app.include_router(doctor_router)

@app.get("/health", tags=["default"])
async def health_check():
    """Basic liveness check used by monitoring/load balancers to confirm the service is up."""
    return {
        "status": "healthy",
        "service": settings.SERVICE_NAME,
        "version": settings.APP_VERSION,
        "timestamp": datetime.utcnow().isoformat()
    }

@app.get("/", tags=["default"])
async def root():
    """Landing endpoint with a friendly welcome message and links to the docs and health check."""
    return {
        "message": f"Welcome to {settings.APP_NAME}",
        "version": settings.APP_VERSION,
        "docs": "/docs",
        "health": "/health"
    }

if __name__ == "__main__":
    import uvicorn

    uvicorn.run(
        "backend.main:app",
        host="0.0.0.0",
        port=settings.SERVICE_PORT,
        reload=settings.DEBUG
    )
