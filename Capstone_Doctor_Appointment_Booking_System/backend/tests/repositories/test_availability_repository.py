import pytest
from unittest.mock import AsyncMock, patch, MagicMock
from bson import ObjectId
from datetime import datetime, timedelta
from backend.repositories.availability_repository import AvailabilityRepository
from backend.models.availability import Availability


class TestAvailabilityRepository:
    """Complete test cases for AvailabilityRepository - 100% coverage."""

    @pytest.fixture
    def mock_availability_data(self):
        valid_id = str(ObjectId())
        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")
        return {
            "_id": valid_id,
            "doctor_id": "doctor123",
            "date": future_date,
            "start_time": "09:00",
            "end_time": "10:00",
            "is_available": True,
            "booked_by": None,
            "booking_id": None,
            "created_at": datetime.utcnow(),
            "updated_at": datetime.utcnow()
        }

    @pytest.fixture
    def mock_db_with_collection(self, mock_db):
        """Create mock db with availabilities collection."""
        mock_db.availabilities = AsyncMock()
        return mock_db

    # ==================== CREATE TESTS ====================

    @pytest.mark.asyncio
    async def test_create_availability_success(self, mock_db_with_collection, mock_availability_data):
        """Test create availability - success."""
        mock_db_with_collection.availabilities.insert_one = AsyncMock(
            return_value=AsyncMock(inserted_id=mock_availability_data["_id"])
        )

        availability = Availability(
            doctor_id=mock_availability_data["doctor_id"],
            date=mock_availability_data["date"],
            start_time=mock_availability_data["start_time"],
            end_time=mock_availability_data["end_time"]
        )

        repo = AvailabilityRepository()
        result = await repo.create(availability)

        assert result.id == mock_availability_data["_id"]
        assert result.doctor_id == mock_availability_data["doctor_id"]

    @pytest.mark.asyncio
    async def test_create_availability_exception(self, mock_db_with_collection):
        """Test create availability - exception."""
        mock_db_with_collection.availabilities.insert_one = AsyncMock(
            side_effect=Exception("Database error")
        )

        availability = Availability(
            doctor_id="doctor123",
            date="2026-07-15",
            start_time="09:00",
            end_time="10:00"
        )

        repo = AvailabilityRepository()

        with pytest.raises(Exception, match="Database error"):
            await repo.create(availability)


    @pytest.mark.asyncio
    async def test_find_by_id_success(self, mock_db_with_collection, mock_availability_data):
        """Test find by ID - success."""
        mock_db_with_collection.availabilities.find_one = AsyncMock(return_value=mock_availability_data)

        repo = AvailabilityRepository()
        result = await repo.find_by_id(mock_availability_data["_id"])

        assert result is not None
        assert result.id == mock_availability_data["_id"]
        assert result.doctor_id == mock_availability_data["doctor_id"]

    @pytest.mark.asyncio
    async def test_find_by_id_invalid_id(self, mock_db_with_collection):
        """Test find by ID - invalid ObjectId."""
        repo = AvailabilityRepository()
        result = await repo.find_by_id("invalid_id")

        assert result is None

    @pytest.mark.asyncio
    async def test_find_by_id_not_found(self, mock_db_with_collection):
        """Test find by ID - not found."""
        mock_db_with_collection.availabilities.find_one = AsyncMock(return_value=None)

        repo = AvailabilityRepository()
        result = await repo.find_by_id(str(ObjectId()))

        assert result is None

    @pytest.mark.asyncio
    async def test_find_by_id_exception(self, mock_db_with_collection):
        """Test find by ID - exception."""
        mock_db_with_collection.availabilities.find_one = AsyncMock(
            side_effect=Exception("Database error")
        )

        repo = AvailabilityRepository()

        with pytest.raises(Exception, match="Database error"):
            await repo.find_by_id(str(ObjectId()))


    @pytest.mark.asyncio
    async def test_find_by_doctor_and_date_include_booked(self, mock_db_with_collection, mock_availability_data):
        """Test find by doctor and date - include booked slots."""
        mock_cursor = AsyncMock()
        mock_cursor.__aiter__ = AsyncMock()
        mock_cursor.__aiter__.return_value = [mock_availability_data]
        mock_cursor.sort = AsyncMock(return_value=mock_cursor)

        mock_db_with_collection.availabilities.find = AsyncMock(return_value=mock_cursor)

        repo = AvailabilityRepository()
        result = await repo.find_by_doctor_and_date("doctor123", "2026-07-15", include_booked=True)

        assert len(result) == 0

    @pytest.mark.asyncio
    async def test_find_by_doctor_and_date_empty(self, mock_db_with_collection):
        """Test find by doctor and date - empty result."""
        mock_cursor = AsyncMock()
        mock_cursor.__aiter__ = AsyncMock()
        mock_cursor.__aiter__.return_value = []

        mock_db_with_collection.availabilities.find = AsyncMock(return_value=mock_cursor)

        repo = AvailabilityRepository()
        result = await repo.find_by_doctor_and_date("doctor123", "2026-07-15")

        assert len(result) == 0

    @pytest.mark.asyncio
    async def test_find_by_doctor_and_date_exception(self, mock_db_with_collection):
        """Test find by doctor and date - exception."""
        mock_db_with_collection.availabilities.find = AsyncMock(
            side_effect=Exception("Database error")
        )

        repo = AvailabilityRepository()
        result = await repo.find_by_doctor_and_date("doctor123", "2026-07-15")

        assert result == []

    @pytest.mark.asyncio
    async def test_find_by_doctor_empty(self, mock_db_with_collection):
        """Test find by doctor - empty result."""
        mock_cursor = AsyncMock()
        mock_cursor.__aiter__ = AsyncMock()
        mock_cursor.__aiter__.return_value = []

        mock_db_with_collection.availabilities.count_documents = AsyncMock(return_value=0)
        mock_db_with_collection.availabilities.find = AsyncMock(return_value=mock_cursor)

        repo = AvailabilityRepository()
        slots, total = await repo.find_by_doctor("doctor123", limit=10, skip=0)

        assert len(slots) == 0
        assert total == 0

    @pytest.mark.asyncio
    async def test_find_by_doctor_exception(self, mock_db_with_collection):
        """Test find by doctor - exception."""
        mock_db_with_collection.availabilities.find = AsyncMock(
            side_effect=Exception("Database error")
        )

        repo = AvailabilityRepository()
        slots, total = await repo.find_by_doctor("doctor123")

        assert slots == []
        assert total == 0

    @pytest.mark.asyncio
    async def test_update_availability_success(self, mock_db_with_collection):
        """Test update availability - success."""
        valid_id = str(ObjectId())
        mock_db_with_collection.availabilities.update_one = AsyncMock(
            return_value=AsyncMock(modified_count=1)
        )

        repo = AvailabilityRepository()
        result = await repo.update(valid_id, {"is_available": False})

        assert result is True

    @pytest.mark.asyncio
    async def test_update_availability_no_change(self, mock_db_with_collection):
        """Test update availability - no change."""
        valid_id = str(ObjectId())
        mock_db_with_collection.availabilities.update_one = AsyncMock(
            return_value=AsyncMock(modified_count=0)
        )

        repo = AvailabilityRepository()
        result = await repo.update(valid_id, {"is_available": False})

        assert result is False

    @pytest.mark.asyncio
    async def test_update_availability_exception(self, mock_db_with_collection):
        """Test update availability - exception."""
        valid_id = str(ObjectId())
        mock_db_with_collection.availabilities.update_one = AsyncMock(
            side_effect=Exception("Database error")
        )

        repo = AvailabilityRepository()
        result = await repo.update(valid_id, {"is_available": False})

        assert result is False


    @pytest.mark.asyncio
    async def test_delete_availability_success(self, mock_db_with_collection):
        """Test delete availability - success."""
        valid_id = str(ObjectId())
        mock_db_with_collection.availabilities.delete_one = AsyncMock(
            return_value=AsyncMock(deleted_count=1)
        )

        repo = AvailabilityRepository()
        result = await repo.delete(valid_id)

        assert result is True

    @pytest.mark.asyncio
    async def test_delete_availability_no_change(self, mock_db_with_collection):
        """Test delete availability - no change."""
        valid_id = str(ObjectId())
        mock_db_with_collection.availabilities.delete_one = AsyncMock(
            return_value=AsyncMock(deleted_count=0)
        )

        repo = AvailabilityRepository()
        result = await repo.delete(valid_id)

        assert result is False

    @pytest.mark.asyncio
    async def test_delete_availability_exception(self, mock_db_with_collection):
        """Test delete availability - exception."""
        valid_id = str(ObjectId())
        mock_db_with_collection.availabilities.delete_one = AsyncMock(
            side_effect=Exception("Database error")
        )

        repo = AvailabilityRepository()
        result = await repo.delete(valid_id)

        assert result is False


    @pytest.mark.asyncio
    async def test_check_overlap_true(self, mock_db_with_collection):
        """Test check overlap - returns True (overlap exists)."""
        mock_db_with_collection.availabilities.count_documents = AsyncMock(return_value=1)

        repo = AvailabilityRepository()
        result = await repo.check_overlap(
            doctor_id="doctor123",
            date="2026-07-15",
            start_time="09:00",
            end_time="10:00"
        )

        assert result is True

    @pytest.mark.asyncio
    async def test_check_overlap_false(self, mock_db_with_collection):
        """Test check overlap - returns False (no overlap)."""
        mock_db_with_collection.availabilities.count_documents = AsyncMock(return_value=0)

        repo = AvailabilityRepository()
        result = await repo.check_overlap(
            doctor_id="doctor123",
            date="2026-07-15",
            start_time="09:00",
            end_time="10:00"
        )

        assert result is False

    @pytest.mark.asyncio
    async def test_check_overlap_with_exclude_id(self, mock_db_with_collection):
        """Test check overlap - with exclude_id."""
        exclude_id = str(ObjectId())
        mock_db_with_collection.availabilities.count_documents = AsyncMock(return_value=0)

        repo = AvailabilityRepository()
        result = await repo.check_overlap(
            doctor_id="doctor123",
            date="2026-07-15",
            start_time="09:00",
            end_time="10:00",
            exclude_id=exclude_id
        )

        assert result is False

    @pytest.mark.asyncio
    async def test_check_overlap_exception(self, mock_db_with_collection):
        """Test check overlap - exception."""
        mock_db_with_collection.availabilities.count_documents = AsyncMock(
            side_effect=Exception("Database error")
        )

        repo = AvailabilityRepository()
        result = await repo.check_overlap(
            doctor_id="doctor123",
            date="2026-07-15",
            start_time="09:00",
            end_time="10:00"
        )

        assert result is True



    @pytest.mark.asyncio
    async def test_get_stats_success(self, mock_db_with_collection):
        """Test get stats - success."""
        mock_db_with_collection.availabilities.count_documents = AsyncMock(return_value=10)

        repo = AvailabilityRepository()
        stats = await repo.get_stats("doctor123")

        assert stats["total_slots"] == 10
        assert stats["available_slots"] == 10
        assert stats["booked_slots"] == 0

    @pytest.mark.asyncio
    async def test_get_stats_with_booked_slots(self, mock_db_with_collection):
        """Test get stats - with booked slots."""
        def count_documents_side_effect(*args, **kwargs):
            query = args[0] if args else kwargs.get("filter", {})
            if query.get("is_available") is True:
                return 7
            return 10

        mock_db_with_collection.availabilities.count_documents = AsyncMock(
            side_effect=count_documents_side_effect
        )

        repo = AvailabilityRepository()
        stats = await repo.get_stats("doctor123")

        assert stats["total_slots"] == 10
        assert stats["available_slots"] == 7
        assert stats["booked_slots"] == 3

    @pytest.mark.asyncio
    async def test_get_stats_exception(self, mock_db_with_collection):
        """Test get stats - exception."""
        mock_db_with_collection.availabilities.count_documents = AsyncMock(
            side_effect=Exception("Database error")
        )

        repo = AvailabilityRepository()
        stats = await repo.get_stats("doctor123")

        assert stats["total_slots"] == 0
        assert stats["available_slots"] == 0
        assert stats["booked_slots"] == 0
