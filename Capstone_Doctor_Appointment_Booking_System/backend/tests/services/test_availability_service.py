import pytest
from unittest.mock import AsyncMock, patch
from datetime import datetime, timedelta
from backend.services.availability_service import AvailabilityService
from backend.schemas.request.availability_request import AvailabilityCreateRequest, AvailabilityUpdateRequest
from backend.enums.user_enums import DoctorStatus
from backend.enums.doctor_enums import Specialization
from backend.constants import ErrorMessages


class TestAvailabilityService:
    """Complete test cases for AvailabilityService - 100% coverage."""

    @pytest.fixture
    def mock_doctor_profile(self):
        from backend.models.profile import DoctorProfile
        from backend.enums.user_enums import VerificationStatus
        return DoctorProfile(
            id="doc123",
            user_id="doctor123",
            qualification="MD",
            specialization=Specialization.CARDIOLOGIST,
            experience_years=5,
            license_number="LIC123",
            consultation_fee=150.50,
            clinic_address="123 Main St",
            status=DoctorStatus.APPROVED,
            verification_status=VerificationStatus.VERIFIED
        )

    @pytest.fixture
    def mock_availability(self):
        from backend.models.availability import Availability
        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")
        return Availability(
            id="avail123",
            doctor_id="doctor123",
            date=future_date,
            start_time="09:00",
            end_time="10:00",
            is_available=True
        )

    @pytest.mark.asyncio
    async def test_create_slot_success(self, mock_db, mock_doctor_profile, mock_availability):
        """Test create slot - success."""
        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")
        slot_data = AvailabilityCreateRequest(
            date=future_date,
            start_time="09:00",
            end_time="10:00"
        )

        with patch('backend.services.availability_service.DoctorRepository') as mock_doctor_repo:
            mock_doctor_repo.return_value.find_by_user_id = AsyncMock(return_value=mock_doctor_profile)

            with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
                mock_avail_repo.return_value.check_overlap = AsyncMock(return_value=False)
                mock_avail_repo.return_value.create = AsyncMock(return_value=mock_availability)

                service = AvailabilityService()
                result = await service.create_slot("doctor123", slot_data)

                assert result.message == "Availability slot added successfully"
                assert result.availability.id == "avail123"

    @pytest.mark.asyncio
    async def test_create_slot_doctor_not_found(self, mock_db):
        """Test create slot - doctor not found."""
        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")
        slot_data = AvailabilityCreateRequest(
            date=future_date,
            start_time="09:00",
            end_time="10:00"
        )

        with patch('backend.services.availability_service.DoctorRepository') as mock_doctor_repo:
            mock_doctor_repo.return_value.find_by_user_id = AsyncMock(return_value=None)

            service = AvailabilityService()

            with pytest.raises(ValueError, match="Doctor profile not found"):
                await service.create_slot("unknown_doctor", slot_data)

    @pytest.mark.asyncio
    async def test_create_slot_doctor_not_approved(self, mock_db):
        """Test create slot - doctor not approved."""
        from backend.models.profile import DoctorProfile
        from backend.enums.user_enums import VerificationStatus

        unapproved_doctor = DoctorProfile(
            id="doc123",
            user_id="doctor123",
            qualification="MD",
            specialization=Specialization.CARDIOLOGIST,
            experience_years=5,
            license_number="LIC123",
            consultation_fee=150.50,
            clinic_address="123 Main St",
            status=DoctorStatus.PENDING,
            verification_status=VerificationStatus.PENDING
        )

        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")
        slot_data = AvailabilityCreateRequest(
            date=future_date,
            start_time="09:00",
            end_time="10:00"
        )

        with patch('backend.services.availability_service.DoctorRepository') as mock_doctor_repo:
            mock_doctor_repo.return_value.find_by_user_id = AsyncMock(return_value=unapproved_doctor)

            service = AvailabilityService()

            with pytest.raises(ValueError, match="Doctor account is not approved"):
                await service.create_slot("doctor123", slot_data)

    @pytest.mark.asyncio
    async def test_create_slot_overlap(self, mock_db, mock_doctor_profile):
        """Test create slot - overlap exists."""
        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")
        slot_data = AvailabilityCreateRequest(
            date=future_date,
            start_time="09:00",
            end_time="10:00"
        )

        with patch('backend.services.availability_service.DoctorRepository') as mock_doctor_repo:
            mock_doctor_repo.return_value.find_by_user_id = AsyncMock(return_value=mock_doctor_profile)

            with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
                mock_avail_repo.return_value.check_overlap = AsyncMock(return_value=True)

                service = AvailabilityService()

                with pytest.raises(ValueError, match="Overlapping slot exists"):
                    await service.create_slot("doctor123", slot_data)

    @pytest.mark.asyncio
    async def test_get_slot_success(self, mock_db, mock_availability):
        """Test get slot - success."""
        with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
            mock_avail_repo.return_value.find_by_id = AsyncMock(return_value=mock_availability)

            service = AvailabilityService()
            result = await service.get_slot("avail123")

            assert result.id == "avail123"
            assert result.doctor_id == "doctor123"

    @pytest.mark.asyncio
    async def test_get_slot_not_found(self, mock_db):
        """Test get slot - not found."""
        with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
            mock_avail_repo.return_value.find_by_id = AsyncMock(return_value=None)

            service = AvailabilityService()

            with pytest.raises(ValueError, match="Availability slot not found"):
                await service.get_slot("unknown_slot")

    @pytest.mark.asyncio
    async def test_get_doctor_slots_success(self, mock_db, mock_doctor_profile, mock_availability):
        """Test get doctor slots - success."""
        with patch('backend.services.availability_service.DoctorRepository') as mock_doctor_repo:
            mock_doctor_repo.return_value.find_by_user_id = AsyncMock(return_value=mock_doctor_profile)

            with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
                mock_avail_repo.return_value.find_by_doctor = AsyncMock(return_value=([mock_availability], 1))

                service = AvailabilityService()
                result = await service.get_doctor_slots("doctor123", limit=10, skip=0)

                assert result["total"] == 1
                assert len(result["availabilities"]) == 1

    @pytest.mark.asyncio
    async def test_get_doctor_slots_doctor_not_found(self, mock_db):
        """Test get doctor slots - doctor not found."""
        with patch('backend.services.availability_service.DoctorRepository') as mock_doctor_repo:
            mock_doctor_repo.return_value.find_by_user_id = AsyncMock(return_value=None)

            service = AvailabilityService()

            with pytest.raises(ValueError, match="Doctor profile not found"):
                await service.get_doctor_slots("unknown_doctor")

    @pytest.mark.asyncio
    async def test_update_slot_success(self, mock_db, mock_availability):
        """Test update slot - success."""
        update_data = AvailabilityUpdateRequest(start_time="10:00", end_time="11:00")

        with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
            mock_avail_repo.return_value.find_by_id = AsyncMock(return_value=mock_availability)
            mock_avail_repo.return_value.check_overlap = AsyncMock(return_value=False)
            mock_avail_repo.return_value.update = AsyncMock(return_value=True)

            service = AvailabilityService()
            result = await service.update_slot("avail123", "doctor123", update_data)

            assert result.message == "Availability slot updated successfully"
            assert result.availability_id == "avail123"

    @pytest.mark.asyncio
    async def test_update_slot_not_found(self, mock_db):
        """Test update slot - not found."""
        update_data = AvailabilityUpdateRequest(start_time="10:00", end_time="11:00")

        with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
            mock_avail_repo.return_value.find_by_id = AsyncMock(return_value=None)

            service = AvailabilityService()

            with pytest.raises(ValueError, match="Availability slot not found"):
                await service.update_slot("unknown_slot", "doctor123", update_data)

    @pytest.mark.asyncio
    async def test_update_slot_not_owner(self, mock_db, mock_availability):
        """Test update slot - not owner."""
        update_data = AvailabilityUpdateRequest(start_time="10:00", end_time="11:00")

        with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
            mock_avail_repo.return_value.find_by_id = AsyncMock(return_value=mock_availability)

            service = AvailabilityService()

            with pytest.raises(ValueError, match="You don't have permission to perform this action"):
                await service.update_slot("avail123", "wrong_doctor", update_data)

    @pytest.mark.asyncio
    async def test_update_slot_booked(self, mock_db):
        """Test update slot - already booked."""
        from backend.models.availability import Availability
        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")
        booked_slot = Availability(
            id="avail123",
            doctor_id="doctor123",
            date=future_date,
            start_time="09:00",
            end_time="10:00",
            is_available=False,
            booked_by="patient123"
        )

        update_data = AvailabilityUpdateRequest(start_time="10:00", end_time="11:00")

        with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
            mock_avail_repo.return_value.find_by_id = AsyncMock(return_value=booked_slot)

            service = AvailabilityService()

            with pytest.raises(ValueError, match="Cannot update a booked slot"):
                await service.update_slot("avail123", "doctor123", update_data)

    @pytest.mark.asyncio
    async def test_delete_slot_success(self, mock_db, mock_availability):
        """Test delete slot - success."""
        with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
            mock_avail_repo.return_value.find_by_id = AsyncMock(return_value=mock_availability)
            mock_avail_repo.return_value.delete = AsyncMock(return_value=True)

            service = AvailabilityService()
            result = await service.delete_slot("avail123", "doctor123")

            assert result.message == "Availability slot deleted successfully"
            assert result.availability_id == "avail123"

    @pytest.mark.asyncio
    async def test_delete_slot_not_found(self, mock_db):
        """Test delete slot - not found."""
        with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
            mock_avail_repo.return_value.find_by_id = AsyncMock(return_value=None)

            service = AvailabilityService()

            with pytest.raises(ValueError, match="Availability slot not found"):
                await service.delete_slot("unknown_slot", "doctor123")

    @pytest.mark.asyncio
    async def test_update_slot_booked(self, mock_db):
        """Test update slot - already booked."""
        from backend.models.availability import Availability
        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")
        booked_slot = Availability(
            id="avail123",
            doctor_id="doctor123",
            date=future_date,
            start_time="09:00",
            end_time="10:00",
            is_available=False,
            booked_by="patient123"
        )

        update_data = AvailabilityUpdateRequest(start_time="10:00", end_time="11:00")

        with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
            mock_avail_repo.return_value.find_by_id = AsyncMock(return_value=booked_slot)

            service = AvailabilityService()

            with pytest.raises(ValueError, match="Cannot update a booked slot"):
                await service.update_slot("avail123", "doctor123", update_data)

    @pytest.mark.asyncio
    async def test_create_slot_overlap(self, mock_db, mock_doctor_profile):
        """Test create slot - overlap exists."""
        future_date = (datetime.now() + timedelta(days=1)).strftime("%Y-%m-%d")
        slot_data = AvailabilityCreateRequest(
            date=future_date,
            start_time="09:00",
            end_time="10:00"
        )

        with patch('backend.services.availability_service.DoctorRepository') as mock_doctor_repo:
            mock_doctor_repo.return_value.find_by_user_id = AsyncMock(return_value=mock_doctor_profile)

            with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
                mock_avail_repo.return_value.check_overlap = AsyncMock(return_value=True)

                service = AvailabilityService()

                # Fix: Use the actual error message
                with pytest.raises(ValueError, match="Overlapping slot exists"):
                    await service.create_slot("doctor123", slot_data)

    @pytest.mark.asyncio
    async def test_get_slot_not_found(self, mock_db):
        """Test get slot - not found."""
        with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
            mock_avail_repo.return_value.find_by_id = AsyncMock(return_value=None)

            service = AvailabilityService()

            with pytest.raises(ValueError, match="Availability slot not found"):
                await service.get_slot("unknown_slot")

    @pytest.mark.asyncio
    async def test_update_slot_not_found(self, mock_db):
        """Test update slot - not found."""
        update_data = AvailabilityUpdateRequest(start_time="10:00", end_time="11:00")

        with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
            mock_avail_repo.return_value.find_by_id = AsyncMock(return_value=None)

            service = AvailabilityService()

            with pytest.raises(ValueError, match="Availability slot not found"):
                await service.update_slot("unknown_slot", "doctor123", update_data)

    @pytest.mark.asyncio
    async def test_get_slot_not_found(self, mock_db):
        """Test get slot - not found."""
        with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
            mock_avail_repo.return_value.find_by_id = AsyncMock(return_value=None)

            service = AvailabilityService()

            # Fix: Use the actual error message
            with pytest.raises(ValueError, match="Availability slot not found"):
                await service.get_slot("unknown_slot")

    @pytest.mark.asyncio
    async def test_delete_slot_not_owner(self, mock_db, mock_availability):
        """Test delete slot - not owner."""
        with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
            mock_avail_repo.return_value.find_by_id = AsyncMock(return_value=mock_availability)

            service = AvailabilityService()

            with pytest.raises(ValueError, match="You don't have permission to perform this action"):
                await service.delete_slot("avail123", "wrong_doctor")

    @pytest.mark.asyncio
    async def test_get_stats_success(self, mock_db, mock_doctor_profile):
        """Test get stats - success."""
        with patch('backend.services.availability_service.DoctorRepository') as mock_doctor_repo:
            mock_doctor_repo.return_value.find_by_user_id = AsyncMock(return_value=mock_doctor_profile)

            with patch('backend.services.availability_service.AvailabilityRepository') as mock_avail_repo:
                mock_avail_repo.return_value.get_stats = AsyncMock(return_value={
                    "total_slots": 10,
                    "available_slots": 7,
                    "booked_slots": 3
                })

                service = AvailabilityService()
                result = await service.get_stats("doctor123")

                assert result["total_slots"] == 10
                assert result["available_slots"] == 7
                assert result["booked_slots"] == 3
