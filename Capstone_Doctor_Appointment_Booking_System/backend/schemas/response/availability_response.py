from pydantic import BaseModel
from typing import Optional


class AvailabilityResponse(BaseModel):
    """
    Response schema for availability slots.

    Returns availability slot information including
    date, time, and availability status.
    """

    id: str
    doctor_id: str
    date: str
    start_time: str
    end_time: str
    is_available: bool
    created_at: str
    updated_at: str


class AvailabilityListResponse(BaseModel):
    """
    Response schema for list of availability slots.

    Returns paginated list of availability slots with
    total count and pagination metadata.
    """

    availabilities: list[AvailabilityResponse]
    total: int
    page: int
    per_page: int
    total_pages: int


class AvailabilityCreateResponse(BaseModel):
    """
    Response schema for availability creation.

    Wraps the availability response with a success message.
    """

    message: str
    availability: AvailabilityResponse


class AvailabilityUpdateResponse(BaseModel):
    """
    Response schema for availability update.

    Confirms update with a success message and the slot ID.
    """

    message: str
    availability_id: str


class AvailabilityDeleteResponse(BaseModel):
    """
    Response schema for availability deletion.

    Confirms deletion with a success message and the slot ID.
    """

    message: str
    availability_id: str
