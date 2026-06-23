from typing import Optional
from ..base.base_schemas import AuthResponseBase, UserResponseBase

class RegisterResponse(AuthResponseBase):
    """Registration response schema"""
    user: UserResponseBase

class LoginResponse(AuthResponseBase):
    """Login response schema"""
    access_token: str
    user: UserResponseBase
