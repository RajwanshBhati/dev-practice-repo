from fastapi import HTTPException
from typing import List, Optional
from shared.constants.roles import UserRole, Permission, ROLE_PERMISSIONS
from shared.constants import HttpStatus
import logging

logger = logging.getLogger(__name__)

class RBAC:
    """Role-Based Access Control utilities"""

    @staticmethod
    def has_permission(user_role: str, required_permission: Permission) -> bool:
        """Check if user role has required permission"""
        if user_role not in ROLE_PERMISSIONS:
            return False
        return required_permission in ROLE_PERMISSIONS.get(UserRole(user_role), [])

    @staticmethod
    def has_any_permission(user_role: str, required_permissions: List[Permission]) -> bool:
        """Check if user role has any of the required permissions"""
        if user_role not in ROLE_PERMISSIONS:
            return False
        user_permissions = ROLE_PERMISSIONS.get(UserRole(user_role), [])
        return any(perm in user_permissions for perm in required_permissions)

    @staticmethod
    def has_all_permissions(user_role: str, required_permissions: List[Permission]) -> bool:
        """Check if user role has all required permissions"""
        if user_role not in ROLE_PERMISSIONS:
            return False
        user_permissions = ROLE_PERMISSIONS.get(UserRole(user_role), [])
        return all(perm in user_permissions for perm in required_permissions)
