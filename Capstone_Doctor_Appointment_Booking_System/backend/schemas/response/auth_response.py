from pydantic import BaseModel
from typing import Optional


class TokenResponse(BaseModel):
    """Token response schema"""

    access_token: str
    refresh_token: Optional[str] = None
    token_type: str = "bearer"
    expires_in: int = 1800
    user: Optional[dict] = None
    message: str
