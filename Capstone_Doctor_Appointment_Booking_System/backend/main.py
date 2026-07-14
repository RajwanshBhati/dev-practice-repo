import sys
from pathlib import Path

# Add backend to path
sys.path.insert(0, str(Path(__file__).parent))

from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from contextlib import asynccontextmanager
from backend.middleware.database import db
from backend.middleware.config import settings
from backend.routers import auth_router, user_router, protected_router, admin_router, doctor_router,appointments_router
import logging
from datetime import datetime

logging.basicConfig(
    level=getattr(logging, settings.LOG_LEVEL),
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)


@asynccontextmanager
async def lifespan(app: FastAPI):

    logger.info(f"Starting {settings.APP_NAME} v{settings.APP_VERSION}")
    await db.connect()

    # Check if admin exists
    from backend.services.admin_service import AdminService
    admin_service = AdminService()
    exists = await admin_service.check_first_admin_exists()
    if not exists:
        logger.warning("No admin found! Please create the first admin at /api/v1/admin/setup-first-admin")

    yield
    logger.info(f"Shutting down {settings.APP_NAME}")
    await db.disconnect()


# Create FastAPI application
app = FastAPI(
    title=settings.APP_NAME,
    description="Doctor Appointment System with Doctor Approval and Availability Management",
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
            "name": "doctor",
            "description": "Doctor endpoints - Profile management, Availability"
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
    """
    Handle ValueError exceptions.

    These are used throughout the app for expected business-logic
    errors and return a clean 400 response.
    """
    return JSONResponse(
        status_code=400,
        content={"success": False, "message": str(exc)}
    )


@app.exception_handler(Exception)
async def general_exception_handler(request: Request, exc: Exception):
    """
    Handle all other exceptions.

    Returns a clean 500 response instead of a raw stack trace.
    """
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
app.include_router(appointments_router, prefix="/api/v1")

app.include_router(auth_router, prefix="/api/v1")
app.include_router(user_router, prefix="/api/v1")
app.include_router(protected_router, prefix="/api/v1")
app.include_router(admin_router, prefix="/api/v1")
app.include_router(doctor_router, prefix="/api/v1")
app.include_router(availability_router, prefix="/api/v1")




@app.get("/", tags=["default"])
async def root():
    """
    Root endpoint with service information.

    Returns a welcome message with links to documentation
    and health check.
    """
    return {
        "message": f"Welcome to {settings.APP_NAME}",
        "version": settings.APP_VERSION,
        "docs": "/docs",
        "health": "/health"
    }


@app.get("/health", tags=["default"])
async def health_check():
    """
    Health check endpoint for monitoring.

    Used by load balancers and monitoring systems to verify
    the service is running properly.
    """
    return {
        "status": "healthy",
        "service": settings.APP_NAME,
        "version": settings.APP_VERSION,
        "timestamp": datetime.utcnow().isoformat()
    }


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(
        "backend.main:app",
        host="0.0.0.0",
        port=settings.PORT,
        reload=settings.DEBUG
    )
