from contextlib import asynccontextmanager
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from beanie import init_beanie
from app.database.mongodb import database
from app.models.user import User
from app.models.patient import Patient
from app.models.doctor import Doctor
from app.api.v1.health import router as health_router
from app.api.v1.auth import router as auth_router


@asynccontextmanager
async def lifespan(app: FastAPI):
    """Application lifespan manager"""
    # Startup: Initialize database connection
    await init_beanie(
        database=database,
        document_models=[User, Patient, Doctor]
    )
    print("MongoDB Connected Successfully")
    print("Doctor Appointment Booking System Started")
    yield
    # Shutdown: Cleanup if needed
    print("🔄 Shutting down...")


# Create FastAPI app
app = FastAPI(
    title="Doctor Appointment Booking System",
    description="A microservices-based appointment booking system",
    version="1.0.0",
    lifespan=lifespan
)

# Configure CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000", "http://localhost:5173", "http://localhost:8000"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Include routers
app.include_router(health_router, prefix="/api/v1", tags=["Health"])
app.include_router(auth_router, prefix="/api/v1/auth", tags=["Authentication"])


@app.get("/")
async def root():
    """Root endpoint"""
    return {
        "message": "Doctor Appointment Booking System API",
        "version": "1.0.0",
        "docs": "/docs",
        "redoc": "/redoc"
    }
