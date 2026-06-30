from datetime import datetime
from typing import Optional, Any
from pydantic import BaseModel, Field, field_validator

class BaseDBModel(BaseModel):
    """Base model for all database models"""
    id: Optional[str] = Field(alias="_id", default=None)
    created_at: datetime = Field(default_factory=datetime.utcnow)
    updated_at: datetime = Field(default_factory=datetime.utcnow)

    class Config:
        populate_by_name = True
        arbitrary_types_allowed = True
        json_encoders = {
            datetime: lambda dt: dt.isoformat()
        }

    @field_validator('id', mode='before')
    @classmethod
    def convert_id_to_str(cls, v):
        """Convert ObjectId to string if needed"""
        if v is not None and not isinstance(v, str):
            return str(v)
        return v
