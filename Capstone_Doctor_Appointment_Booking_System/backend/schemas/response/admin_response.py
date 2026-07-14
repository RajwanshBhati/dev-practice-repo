from pydantic import BaseModel


class AdminResponse(BaseModel):
    """Response schema containing admin user details."""

    id: str
    email: str
    full_name: str
    role: str
    is_first_admin: bool


class AdminCreateResponse(BaseModel):
    """Response schema returned after successfully creating an admin user."""

    message: str
    admin: AdminResponse
