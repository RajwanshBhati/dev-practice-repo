from pydantic import BaseModel, Field, validator
from typing import Optional
from datetime import datetime
from backend.utils.validators import Validators


class AvailabilityCreateRequest(BaseModel):
    """
    Request schema for creating availability slots.
    """

    date: str = Field(..., description="Date of availability in YYYY-MM-DD format")
    start_time: str = Field(..., description="Start time in HH:MM format")
    end_time: str = Field(..., description="End time in HH:MM format")

    @validator('date')
    def validate_date(cls, v):
        """
        Validate that the date is not in the past.

        Args:
            v: Date string in YYYY-MM-DD format

        Returns:
            str: Validated date string

        Raises:
            ValueError: If date is in the past or invalid format
        """
        try:
            date_obj = datetime.strptime(v, '%Y-%m-%d').date()
        except ValueError:
            raise ValueError('Invalid date format. Use YYYY-MM-DD')

        today = datetime.now().date()
        if date_obj < today:
            raise ValueError('Date cannot be in the past')
        return v

    @validator('start_time')
    def validate_start_time(cls, v, values):
        """
        Validate time format, working hours, and that the slot isn't
        already in the past when the date being booked is today.

        Working hours are from 09:00 AM to 06:00 PM.

        Args:
            v: Time string in HH:MM format
            values: Previously validated fields (used to check today's date)

        Returns:
            str: Validated time string

        Raises:
            ValueError: If time format is invalid, outside working hours,
                or the time has already passed today
        """
        if not Validators.validate_time_format(v):
            raise ValueError('Invalid time format. Use HH:MM')
        hour = int(v.split(':')[0])
        if hour < 9 or hour >= 18:
            raise ValueError('Start time must be between 09:00 and 18:00')

        slot_date = values.get('date')
        if slot_date:
            today = datetime.now().date()
            try:
                date_obj = datetime.strptime(slot_date, '%Y-%m-%d').date()
            except ValueError:
                date_obj = None
            if date_obj == today and v <= datetime.now().strftime('%H:%M'):
                raise ValueError("Start time cannot be in the past for today's date")
        return v

    @validator('end_time')
    def validate_end_time(cls, v, values):
        """
        Validate end time and ensure it's after start time.

        Args:
            v: End time string in HH:MM format
            values: Dictionary containing previously validated fields

        Returns:
            str: Validated end time string

        Raises:
            ValueError: If end time is before start time or outside working hours
        """
        if not Validators.validate_time_format(v):
            raise ValueError('Invalid time format. Use HH:MM')
        if 'start_time' in values:
            start = values['start_time']
            if v <= start:
                raise ValueError('End time must be after start time')
        hour = int(v.split(':')[0])
        if hour < 9 or hour > 18:
            raise ValueError('End time must be between 09:00 and 18:00')
        return v


class AvailabilityUpdateRequest(BaseModel):
    """
    Request schema for updating availability slots.

    Allows updating the date and time of an existing
    availability slot.
    """

    date: Optional[str] = Field(None, description="New date in YYYY-MM-DD format")
    start_time: Optional[str] = Field(None, description="New start time in HH:MM format")
    end_time: Optional[str] = Field(None, description="New end time in HH:MM format")
    is_available: Optional[bool] = Field(None, description="Whether slot is available")

    @validator('date')
    def validate_date(cls, v):
        """Validate that the date is not in the past."""
        if v:
            try:
                date_obj = datetime.strptime(v, '%Y-%m-%d').date()
                today = datetime.now().date()
                if date_obj < today:
                    raise ValueError('Date cannot be in the past')
                return v
            except ValueError:
                raise ValueError('Invalid date format. Use YYYY-MM-DD')
        return v

    @validator('start_time')
    def validate_start_time(cls, v):
        """Validate time format and working hours."""
        if v:
            if not Validators.validate_time_format(v):
                raise ValueError('Invalid time format. Use HH:MM')
            hour = int(v.split(':')[0])
            if hour < 9 or hour >= 18:
                raise ValueError('Start time must be between 09:00 and 18:00')
        return v

    @validator('end_time')
    def validate_end_time(cls, v, values):
        """Validate end time and ensure it's after start time."""
        if v:
            if not Validators.validate_time_format(v):
                raise ValueError('Invalid time format. Use HH:MM')
            if 'start_time' in values and values['start_time']:
                start = values['start_time']
                if v <= start:
                    raise ValueError('End time must be after start time')
            hour = int(v.split(':')[0])
            if hour < 9 or hour > 18:
                raise ValueError('End time must be between 09:00 and 18:00')
        return v
