from pydantic import BaseModel, EmailStr, Field


class UserLogin(BaseModel):
    """User login schema"""

    email: EmailStr = Field(..., description="Email address")
    password: str = Field(..., description="Password")
    remember_me: bool = Field(default=False, description="Remember me")


class RefreshToken(BaseModel):
    """Refresh token request schema"""

    refresh_token: str = Field(..., description="Refresh token")


class LogoutRequest(BaseModel):
    """Logout request schema"""

    access_token: str = Field(..., description="Access token to invalidate")
