from typing import Optional
from datetime import datetime
from pydantic import Field
from backend.models.base import BaseDBModel

class AdminAuditLog(BaseDBModel):
    """Records every sensitive action an admin takes"""
    admin_id: str = Field(..., description="Admin user ID")
    admin_email: str = Field(..., description="Admin email")
    action: str = Field(..., description="Action performed")
    target_id: Optional[str] = Field(None, description="Target user/doctor ID")
    target_email: Optional[str] = Field(None, description="Target email")
    details: dict = Field(default_factory=dict, description="Action details")
    ip_address: Optional[str] = Field(None, description="IP address")
    user_agent: Optional[str] = Field(None, description="User agent")

    class Config:
        collection = "admin_audit_logs"


class SystemSettings(BaseDBModel):
    """Stores app-wide configurable settings as key-value pairs."""
    key: str = Field(..., description="Setting key")
    value: dict = Field(..., description="Setting value")
    description: Optional[str] = Field(None, description="Setting description")
    updated_by: Optional[str] = Field(None, description="Admin who updated")

    class Config:
        collection = "system_settings"
