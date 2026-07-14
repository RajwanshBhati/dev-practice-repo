from typing import Optional, List
from datetime import datetime
from bson import ObjectId
from backend.middleware.database import db
from backend.models.admin import AdminAuditLog, SystemSettings
import logging

logger = logging.getLogger(__name__)

class AdminRepository:
    """Handles database operations for admin audit logs and system-wide settings."""

    def __init__(self):
        self.logs_collection = db.get_db().admin_audit_logs
        self.settings_collection = db.get_db().system_settings

    async def create_audit_log(self, log: AdminAuditLog) -> AdminAuditLog:
        """Save a new audit log entry recording an admin's action."""
        try:
            log_dict = log.model_dump(exclude={"id"}, by_alias=True)
            log_dict = {k: v for k, v in log_dict.items() if v is not None}
            result = await self.logs_collection.insert_one(log_dict)
            log.id = str(result.inserted_id)
            return log
        except Exception as e:
            logger.error(f"Error creating audit log: {e}")
            raise

    async def get_audit_logs(
        self,
        admin_id: Optional[str] = None,
        limit: int = 100,
        skip: int = 0
    ) -> List[AdminAuditLog]:
        """Fetch audit logs, newest first, optionally filtered to a single admin."""
        try:
            query = {}
            if admin_id:
                query["admin_id"] = admin_id

            cursor = self.logs_collection.find(query).sort("created_at", -1).skip(skip).limit(limit)
            logs = []
            async for log_dict in cursor:
                log_dict["id"] = str(log_dict["_id"])
                logs.append(AdminAuditLog(**log_dict))
            return logs
        except Exception as e:
            logger.error(f"Error getting audit logs: {e}")
            return []

    async def get_setting(self, key: str) -> Optional[dict]:
        """Fetch the current value of a system setting by its key."""
        try:
            setting_dict = await self.settings_collection.find_one({"key": key})
            if setting_dict:
                return setting_dict.get("value")
            return None
        except Exception as e:
            logger.error(f"Error getting setting: {e}")
            return None

    async def update_setting(
        self,
        key: str,
        value: dict,
        updated_by: str
    ) -> bool:
        """Update a system setting's value, creating it if it doesn't exist yet."""
        try:
            result = await self.settings_collection.update_one(
                {"key": key},
                {
                    "$set": {
                        "value": value,
                        "updated_by": updated_by,
                        "updated_at": datetime.utcnow()
                    }
                },
                upsert=True
            )
            return True
        except Exception as e:
            logger.error(f"Error updating setting: {e}")
            return False
