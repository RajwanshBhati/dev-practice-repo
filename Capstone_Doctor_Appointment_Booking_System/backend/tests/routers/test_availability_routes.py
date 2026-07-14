import pytest
from unittest.mock import AsyncMock, patch
from httpx import AsyncClient
from backend.main import app

class TestAvailabilityRouter:
    @pytest.mark.asyncio
    async def test_create_availability_slot_success(self, mocker):
        mock_availability_service = AsyncMock()
        mock_availability_service.create_slot = AsyncMock(return_value={
            "id": "slot_123",
            "doctor_id": "doctor_456",
            "date": "2026-07-20",
            "start_time": "09:00",
            "end_time": "10:00",
            "is_available": True
        })
        mocker.patch('backend.routers.availability.AvailabilityService', return_value=mock_availability_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.post(
                "/api/v1/doctors/availability",
                json={
                    "date": "2026-07-20",
                    "start_time": "09:00",
                    "end_time": "10:00"
                }
            )

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_create_availability_slot_value_error(self, mocker):
        mock_availability_service = AsyncMock()
        mock_availability_service.create_slot = AsyncMock(side_effect=ValueError("Doctor not found"))
        mocker.patch('backend.routers.availability.AvailabilityService', return_value=mock_availability_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.post(
                "/api/v1/doctors/availability",
                json={
                    "date": "2026-07-20",
                    "start_time": "09:00",
                    "end_time": "10:00"
                }
            )

        assert response.status_code == 400

    @pytest.mark.asyncio
    async def test_get_doctor_availability_slots_success(self, mocker):
        mock_availability_service = AsyncMock()
        mock_availability_service.get_doctor_slots = AsyncMock(return_value={
            "slots": [{"id": "slot_1", "date": "2026-07-20"}],
            "total": 1
        })
        mocker.patch('backend.routers.availability.AvailabilityService', return_value=mock_availability_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/doctors/availability?limit=10&skip=0")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_doctor_availability_by_date_success(self, mocker):
        mock_availability_service = AsyncMock()
        mock_availability_service.get_doctor_slots_by_date = AsyncMock(return_value=[
            {"id": "slot_1", "date": "2026-07-20", "start_time": "09:00"}
        ])
        mocker.patch('backend.routers.availability.AvailabilityService', return_value=mock_availability_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/doctors/availability?date=2026-07-20&include_booked=false")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_doctor_availability_stats_success(self, mocker):
        mock_availability_service = AsyncMock()
        mock_availability_service.get_stats = AsyncMock(return_value={
            "total_slots": 10,
            "available": 7,
            "booked": 3
        })
        mocker.patch('backend.routers.availability.AvailabilityService', return_value=mock_availability_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/doctors/availability/stats")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_doctor_availability_by_profile_id_success(self, mocker):
        mock_availability_service = AsyncMock()
        mock_availability_service.get_doctor_slots_by_profile_id = AsyncMock(return_value=[
            {"id": "slot_1", "date": "2026-07-20"}
        ])
        mocker.patch('backend.routers.availability.AvailabilityService', return_value=mock_availability_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/doctors/doctor_123/availability?date=2026-07-20")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_availability_slot_by_id_success(self, mocker):
        mock_availability_service = AsyncMock()
        mock_availability_service.get_slot = AsyncMock(return_value={
            "id": "slot_123",
            "date": "2026-07-20",
            "start_time": "09:00",
            "end_time": "10:00"
        })
        mocker.patch('backend.routers.availability.AvailabilityService', return_value=mock_availability_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/doctors/availability/slot_123")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_update_availability_slot_success(self, mocker):
        mock_availability_service = AsyncMock()
        mock_availability_service.update_slot = AsyncMock(return_value={
            "message": "Slot updated successfully"
        })
        mocker.patch('backend.routers.availability.AvailabilityService', return_value=mock_availability_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.put(
                "/api/v1/doctors/availability/slot_123",
                json={
                    "start_time": "10:00",
                    "end_time": "11:00"
                }
            )

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_delete_availability_slot_success(self, mocker):
        mock_availability_service = AsyncMock()
        mock_availability_service.delete_slot = AsyncMock(return_value={
            "message": "Slot deleted successfully"
        })
        mocker.patch('backend.routers.availability.AvailabilityService', return_value=mock_availability_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.delete("/api/v1/doctors/availability/slot_123")

        assert response.status_code == 200
