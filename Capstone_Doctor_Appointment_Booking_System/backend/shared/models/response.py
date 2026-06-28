from typing import Optional, List, Generic, TypeVar
from pydantic import BaseModel
from datetime import datetime

T = TypeVar('T')

class APIResponse(BaseModel):
    success: bool = True
    message: str = "Operation successful"
    data: Optional[dict] = None
    error: Optional[dict] = None
    timestamp: datetime = Field(default_factory=datetime.utcnow)

    class Config:
        json_encoders = {
            datetime: lambda dt: dt.isoformat()
        }

class ErrorResponse(BaseModel):
    success: bool = False
    error_code: str
    message: str
    details: Optional[dict] = None
    timestamp: datetime = Field(default_factory=datetime.utcnow)
