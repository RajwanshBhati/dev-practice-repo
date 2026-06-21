from fastapi import FastAPI
from motor.motor_asyncio import AsyncIOMotorClient
from dotenv import load_dotenv
import os

load_dotenv()

app = FastAPI()

@app.on_event("startup")
async def startup():
    app.mongodb_client = AsyncIOMotorClient(
        os.getenv("MONGODB_URL")
    )

    app.database = app.mongodb_client[
        os.getenv("DATABASE_NAME")
    ]

    await app.mongodb_client.admin.command("ping")

    print("MongoDB Connected")


@app.on_event("shutdown")
async def shutdown():
    app.mongodb_client.close()


@app.get("/")
async def root():
    return {"message": "API Running"}
