from datetime import datetime
from typing import Optional, Any
from pydantic import BaseModel, Field, field_validator

class BaseDBModel(BaseModel):
    """
    Common fields and behavior every DB model shares: a Mongo-friendly `id`,
    and auto-tracked created/updated timestamps. Other models inherit from
    this instead of repeating the same boilerplate.
    """
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
        """MongoDB returns _id as an ObjectId, but we want it as a plain string everywhere in the app."""
        if v is not None and not isinstance(v, str):
            return str(v)
        return v
