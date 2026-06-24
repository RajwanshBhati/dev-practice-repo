from ..base.base_schemas import UserResponseBase, AuthResponseBase


class RegisterResponse(AuthResponseBase):
    """Registration response schema"""
    user: UserResponseBase


class LoginResponse(AuthResponseBase):
    """Login response schema"""
    access_token: str
    user: UserResponseBase
