from datetime import datetime
from pydantic import BaseModel

class UserResponse(BaseModel):
    id: str
    email: str
    full_name: str
    phone: str | None
    role: str
    status: str
    created_at: datetime
