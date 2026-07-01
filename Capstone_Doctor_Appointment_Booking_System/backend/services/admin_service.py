from typing import Dict, Any, Optional, List
from datetime import datetime
from backend.repositories.user_repository import UserRepository
from backend.repositories.admin_repository import AdminRepository
from backend.models.user import User
from backend.models.admin import AdminAuditLog
from backend.middleware.security import security
from backend.schemas.request.admin_request import AdminCreateRequest
from backend.constants import ErrorMessages, SuccessMessages
from backend.constants.roles import UserRole
from backend.enums.user_enums import UserStatus, AdminType
import logging

logger = logging.getLogger(__name__)

class AdminService:
    """Handles admin account creation, deletion, and lookups, plus writes an audit log entry for every action taken."""

    def __init__(self):
        self.user_repo = UserRepository()
        self.admin_repo = AdminRepository()

    async def check_first_admin_exists(self) -> bool:
        """Check whether an admin account already exists anywhere in the system."""
        try:
            from backend.middleware.database import db
            collection = db.get_db().users
            admin = await collection.find_one({"role": UserRole.ADMIN})
            return admin is not None
        except Exception as e:
            logger.error(f"Error checking first admin: {e}")
            return False

    async def create_first_admin(self, admin_data: AdminCreateRequest) -> Dict[str, Any]:
        """
        Bootstrap the very first super admin account. Fails if an admin
        already exists or if the email is already taken.
        """
        exists = await self.check_first_admin_exists()
        if exists:
            raise ValueError(ErrorMessages.ADM_1405)

        existing_user = await self.user_repo.find_by_email(admin_data.email)
        if existing_user:
            raise ValueError(ErrorMessages.USER_1102)

        password_hash = security.hash_password(admin_data.password)

        admin_user = User(
            email=admin_data.email,
            password_hash=password_hash,
            full_name=admin_data.full_name,
            phone=admin_data.phone,
            gender=admin_data.gender,
            date_of_birth=admin_data.date_of_birth,
            role=UserRole.ADMIN,
            status=UserStatus.ACTIVE,
            is_verified=True,
            is_first_admin=True
        )

        created_admin = await self.user_repo.create(admin_user)

        audit_log = AdminAuditLog(
            admin_id=created_admin.id,
            admin_email=created_admin.email,
            action="CREATE_FIRST_ADMIN",
            target_id=created_admin.id,
            target_email=created_admin.email,
            details={
                "admin_name": created_admin.full_name,
                "admin_type": AdminType.SUPER_ADMIN.value
            }
        )
        await self.admin_repo.create_audit_log(audit_log)

        logger.info(f"First admin created: {created_admin.email}")

        return {
            "message": SuccessMessages.ADMIN_CREATED,
            "admin": {
                "id": created_admin.id,
                "email": created_admin.email,
                "full_name": created_admin.full_name,
                "role": created_admin.role.value,
                "is_first_admin": created_admin.is_first_admin
            }
        }

    async def create_admin(
        self,
        admin_data: AdminCreateRequest,
        creator_id: str
    ) -> Dict[str, Any]:
        """Create a new sub-admin. Only the first (super) admin is allowed to do this."""
        creator = await self.user_repo.find_by_id(creator_id)
        if not creator or not creator.is_first_admin:
            raise ValueError(ErrorMessages.ADM_1402)

        existing_user = await self.user_repo.find_by_email(admin_data.email)
        if existing_user:
            raise ValueError(ErrorMessages.USER_1102)

        password_hash = security.hash_password(admin_data.password)

        admin_user = User(
            email=admin_data.email,
            password_hash=password_hash,
            full_name=admin_data.full_name,
            phone=admin_data.phone,
            gender=admin_data.gender,
            date_of_birth=admin_data.date_of_birth,
            role=UserRole.ADMIN,
            status=UserStatus.ACTIVE,
            is_verified=True,
            is_first_admin=False
        )

        created_admin = await self.user_repo.create(admin_user)

        audit_log = AdminAuditLog(
            admin_id=creator_id,
            admin_email=creator.email,
            action="CREATE_ADMIN",
            target_id=created_admin.id,
            target_email=created_admin.email,
            details={
                "admin_name": created_admin.full_name,
                "admin_type": AdminType.SUB_ADMIN.value,
                "created_by": creator.full_name
            }
        )
        await self.admin_repo.create_audit_log(audit_log)

        logger.info(f"New admin created: {created_admin.email} by {creator.email}")

        return {
            "message": SuccessMessages.ADMIN_CREATED,
            "admin": {
                "id": created_admin.id,
                "email": created_admin.email,
                "full_name": created_admin.full_name,
                "role": created_admin.role.value,
                "is_first_admin": created_admin.is_first_admin
            }
        }

    async def get_all_admins(self) -> List[Dict[str, Any]]:
        """List every admin account with basic display fields."""
        try:
            from backend.middleware.database import db
            collection = db.get_db().users
            cursor = collection.find({"role": UserRole.ADMIN})
            admins = []
            async for admin_dict in cursor:
                admin_dict["id"] = str(admin_dict["_id"])
                admins.append({
                    "id": admin_dict["id"],
                    "email": admin_dict["email"],
                    "full_name": admin_dict["full_name"],
                    "phone": admin_dict["phone"],
                    "is_first_admin": admin_dict.get("is_first_admin", False),
                    "status": admin_dict.get("status", UserStatus.ACTIVE).value if hasattr(admin_dict.get("status"), 'value') else admin_dict.get("status"),
                    "created_at": admin_dict.get("created_at")
                })
            return admins
        except Exception as e:
            logger.error(f"Error getting all admins: {e}")
            return []

    async def delete_admin(self, admin_id: str, deleter_id: str) -> Dict[str, Any]:
        """
        Soft-delete an admin account. Only the super admin can do this, and
        we refuse to delete the last remaining super admin so the system
        never ends up with zero admins.
        """
        deleter = await self.user_repo.find_by_id(deleter_id)
        if not deleter or not deleter.is_first_admin:
            raise ValueError(ErrorMessages.ADM_1402)

        admin_to_delete = await self.user_repo.find_by_id(admin_id)
        if not admin_to_delete:
            raise ValueError(ErrorMessages.ADM_1401)

        if admin_to_delete.is_first_admin:
            admins = await self.get_all_admins()
            if len(admins) <= 1:
                raise ValueError(ErrorMessages.ADM_1404)

        await self.user_repo.update(
            admin_id,
            {"status": UserStatus.DELETED}
        )

        audit_log = AdminAuditLog(
            admin_id=deleter_id,
            admin_email=deleter.email,
            action="DELETE_ADMIN",
            target_id=admin_id,
            target_email=admin_to_delete.email,
            details={
                "admin_name": admin_to_delete.full_name,
                "deleted_by": deleter.full_name
            }
        )
        await self.admin_repo.create_audit_log(audit_log)

        logger.info(f"Admin deleted: {admin_to_delete.email} by {deleter.email}")

        return {
            "message": SuccessMessages.ADMIN_DELETED,
            "admin_id": admin_id
        }
