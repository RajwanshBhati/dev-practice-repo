from datetime import datetime
from pydantic import Field
from backend.models.base import BaseDBModel

class TokenBlacklist(BaseDBModel):
    """Keeps track of JWTs that have been invalidated (e.g. on logout) so they're rejected even before they expire."""
    token: str = Field(..., description="JWT token")
    user_id: str = Field(..., description="User ID")
    expires_at: datetime = Field(..., description="Token expiry time")
    blacklisted_at: datetime = Field(default_factory=datetime.utcnow, description="When token was blacklisted")

    class Config:
        collection = "token_blacklist"
