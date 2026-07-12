from pydantic import BaseModel, Field
from typing import Optional


class DoctorSearchRequest(BaseModel):
    """
    Request schema for searching doctors.
    """

    query: Optional[str] = Field(None, description="Search by name or specialization")
    specialization: Optional[str] = Field(None, description="Filter by specialization")
    location: Optional[str] = Field(None, description="Filter by location")
    min_experience: Optional[int] = Field(None, ge=0, description="Minimum experience")
    max_fee: Optional[float] = Field(None, gt=0, description="Maximum fee")
    min_rating: Optional[float] = Field(None, ge=0, le=5, description="Minimum rating")
    limit: int = Field(20, ge=1, le=100, description="Results per page")
    skip: int = Field(0, ge=0, description="Results to skip")
