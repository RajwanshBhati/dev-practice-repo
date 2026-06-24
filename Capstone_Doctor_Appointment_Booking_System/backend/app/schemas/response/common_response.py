from pydantic import BaseModel, Field
from typing import Optional, Any, TypeVar, Generic
from datetime import datetime

T = TypeVar('T')


class APIResponse(BaseModel, Generic[T]):
    """Generic API response wrapper"""
    success: bool = True
    message: str
    data: Optional[T] = None
    timestamp: datetime = Field(default_factory=datetime.utcnow)


class ErrorResponse(BaseModel):
    """Error response schema"""
    success: bool = False
    message: str
    error_code: Optional[str] = None
    details: Optional[Any] = None
    timestamp: datetime = Field(default_factory=datetime.utcnow)
