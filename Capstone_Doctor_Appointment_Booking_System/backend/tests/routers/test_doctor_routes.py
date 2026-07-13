import pytest
from unittest.mock import AsyncMock, MagicMock, patch
from httpx import AsyncClient
from backend.main import app

class TestDoctorRouter:
    @pytest.mark.asyncio
    async def test_get_doctor_profile_success(self, mocker):
        mock_doctor_service = AsyncMock()
        mock_doctor_service.get_doctor_profile = AsyncMock(return_value={
            "id": "doc_123",
            "user_id": "user_456",
            "qualification": "MD",
            "specialization": "Cardiologist",
            "experience_years": 10,
            "status": "APPROVED"
        })
        mocker.patch('backend.routers.doctor.DoctorService', return_value=mock_doctor_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/doctor/profile")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_doctor_profile_not_found(self, mocker):
        mock_doctor_service = AsyncMock()
        mock_doctor_service.get_doctor_profile = AsyncMock(side_effect=ValueError("Doctor profile not found"))
        mocker.patch('backend.routers.doctor.DoctorService', return_value=mock_doctor_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/doctor/profile")

        assert response.status_code == 404

    @pytest.mark.asyncio
    async def test_update_doctor_profile_success(self, mocker):
        mock_doctor_service = AsyncMock()
        mock_doctor_service.update_doctor_profile = AsyncMock(return_value={
            "message": "Profile updated successfully"
        })
        mocker.patch('backend.routers.doctor.DoctorService', return_value=mock_doctor_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.put(
                "/api/v1/doctor/profile",
                json={
                    "qualification": "MD, PhD",
                    "experience_years": 12,
                    "consultation_fee": 200.00
                }
            )

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_public_doctor_profile_success(self, mocker):
        mock_doctor_service = AsyncMock()
        mock_doctor_service.get_public_doctor_profile = AsyncMock(return_value={
            "id": "doc_123",
            "full_name": "Dr. John Doe",
            "specialization": "Cardiologist",
            "qualification": "MD",
            "experience_years": 10,
            "rating": 4.5
        })
        mocker.patch('backend.routers.doctor.DoctorService', return_value=mock_doctor_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/doctor/public/doc_123")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_search_doctors_success(self, mocker):
        mock_doctor_service = AsyncMock()
        mock_doctor_service.search_doctors = AsyncMock(return_value={
            "doctors": [{"id": "doc_1", "full_name": "Dr. John"}],
            "total": 1
        })
        mocker.patch('backend.routers.doctor.DoctorService', return_value=mock_doctor_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/doctor/search?query=john&limit=10&skip=0")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_search_doctors_with_filters(self, mocker):
        mock_doctor_service = AsyncMock()
        mock_doctor_service.search_doctors = AsyncMock(return_value={
            "doctors": [{"id": "doc_1", "full_name": "Dr. John"}],
            "total": 1
        })
        mocker.patch('backend.routers.doctor.DoctorService', return_value=mock_doctor_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get(
                "/api/v1/doctor/search?specialization=Cardiologist&min_experience=5&max_fee=200&min_rating=4.0"
            )

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_specializations_success(self):
        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/doctor/specializations")

        assert response.status_code == 200
        assert "specializations" in response.json()

    @pytest.mark.asyncio
    async def test_get_doctor_stats_success(self, mocker):
        mock_doctor_service = AsyncMock()
        mock_doctor_service.get_doctor_profile = AsyncMock(return_value=MagicMock(
            rating=4.5,
            total_reviews=20
        ))
        mocker.patch('backend.routers.doctor.DoctorService', return_value=mock_doctor_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/doctor/stats")

        assert response.status_code == 200
