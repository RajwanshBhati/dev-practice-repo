from contextlib import asynccontextmanager

from fastapi import FastAPI
from beanie import init_beanie

from app.database.mongodb import database
from app.models.user import User

from app.api.v1.health import router as health_router
from app.api.v1.auth import router as auth_router


@asynccontextmanager
async def lifespan(app: FastAPI):

    await init_beanie(
        database=database,
        document_models=[User]
    )

    print("MongoDB Connected Successfully")

    yield


app = FastAPI(
    title="Doctor Appointment Booking System",
    lifespan=lifespan
)

app.include_router(
    health_router,
    prefix="/api/v1",
    tags=["Health"]
)

app.include_router(
    auth_router,
    prefix="/api/v1/auth",
    tags=["Authentication"]
)
