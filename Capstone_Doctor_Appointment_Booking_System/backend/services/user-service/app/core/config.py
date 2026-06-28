from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    APP_NAME: str = "User Service"
    APP_VERSION: str = "1.0.0"
    SERVICE_PORT: int = 8001

    DATABASE_NAME: str
    MONGODB_URL: str

    JWT_SECRET_KEY: str
    JWT_ALGORITHM: str = "HS256"
    ACCESS_TOKEN_EXPIRE_MINUTES: int = 30

    class Config:
        env_file = ".env"
        env_file_encoding = "utf-8"


settings = Settings()
