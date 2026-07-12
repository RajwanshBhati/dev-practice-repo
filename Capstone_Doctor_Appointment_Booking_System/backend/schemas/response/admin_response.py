from pydantic import BaseModel
from typing import Optional, List, Any
from datetime import datetime


class AdminSummary(BaseModel):
    """Shape of a single admin record returned by list/detail endpoints."""
    user_id: str
    name: str
    email: str
    role: str
    created_at: datetime


class AdminListResponse(BaseModel):
    admins: List[AdminSummary]


class AdminDeleteResponse(BaseModel):
    message: str
    admin_id: str


class DoctorSummary(BaseModel):
    """Adjust fields to match what DoctorService actually returns."""
    doctor_id: str
    name: str
    email: str
    status: str
    created_at: datetime


class DoctorListResponse(BaseModel):
    doctors: List[DoctorSummary]
    count: int


class PaginatedDoctorListResponse(BaseModel):
    doctors: List[DoctorSummary]
    count: int
    total: int
    total_pages: int


class DoctorActionResponse(BaseModel):
    message: str
    doctor_id: str
    status: str


class DoctorStatsResponse(BaseModel):
    total_doctors: int
    pending: int
    approved: int
    rejected: int


class AuditLogEntry(BaseModel):
    log_id: str
    admin_id: str
    action: str
    target_id: Optional[str] = None
    details: Optional[Any] = None
    created_at: datetime


class AuditLogListResponse(BaseModel):
    logs: List[AuditLogEntry]
    count: int
