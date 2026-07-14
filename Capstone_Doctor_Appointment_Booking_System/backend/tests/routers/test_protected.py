import pytest
from unittest.mock import AsyncMock, MagicMock, patch
from httpx import AsyncClient
from backend.main import app

class TestProtectedRouter:
    @pytest.mark.asyncio
    async def test_get_profile_success(self, mocker):
        mock_user_service = AsyncMock()
        mock_user = MagicMock()
        mock_user.id = "user_123"
        mock_user.email = "test@example.com"
        mock_user.full_name = "John Doe"
        mock_user.role = MagicMock(value="PATIENT")
        mock_user.status = MagicMock(value="ACTIVE")
        mock_user_service.get_user_by_id = AsyncMock(return_value=mock_user)
        mocker.patch('backend.routers.protected.UserService', return_value=mock_user_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/protected/profile")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_patient_dashboard_success(self, mocker):
        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/protected/patient/dashboard")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_doctor_dashboard_success(self, mocker):
        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/protected/doctor/dashboard")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_admin_dashboard_success(self, mocker):
        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/protected/admin/dashboard")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_view_doctors_success(self, mocker):
        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/protected/doctors")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_create_appointment_success(self, mocker):
        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.post("/api/v1/protected/appointments")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_all_users_success(self, mocker):
        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/protected/admin/users")

        assert response.status_code == 200
