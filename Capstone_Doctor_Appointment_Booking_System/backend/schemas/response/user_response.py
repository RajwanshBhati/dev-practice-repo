from pydantic import BaseModel, ConfigDict


class UserProfileResponse(BaseModel):
    """Response schema for the /protected/profile endpoint."""
    model_config = ConfigDict(from_attributes=True)

    id: str
    email: str
    full_name: str
    role: str
    status: str
