from pydantic import BaseModel
from typing import Optional, List
from backend.constants.enums import Specialization


class DoctorSearchResponse(BaseModel):
    """
    Doctor search result item.
    """

    id: str
    full_name: str
    qualification: str
    specialization: Specialization
    experience_years: int
    consultation_fee: float
    clinic_address: str
    clinic_phone: Optional[str] = None
    bio: Optional[str] = None
    profile_picture: Optional[str] = None
    rating: float
    total_reviews: int
    is_available: bool


class DoctorSearchListResponse(BaseModel):
    """
    Doctor search list response with pagination.
    """

    doctors: List[DoctorSearchResponse]
    total: int
    skip: int
    limit: int
    has_more: bool


class DoctorPublicResponse(BaseModel):
    """
    Public doctor profile for patients.
    """

    id: str
    full_name: str
    qualification: str
    specialization: Specialization
    experience_years: int
    consultation_fee: float
    clinic_address: str
    clinic_phone: Optional[str] = None
    bio: Optional[str] = None
    profile_picture: Optional[str] = None
    rating: float
    total_reviews: int
    is_available: bool


class SpecializationsResponse(BaseModel):
    """
    Specializations list response.
    """

    specializations: List[str]
