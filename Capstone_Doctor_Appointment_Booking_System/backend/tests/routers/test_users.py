import pytest
from unittest.mock import AsyncMock, MagicMock, patch
from httpx import AsyncClient
from backend.main import app

class TestUsersRouter:
    @pytest.mark.asyncio
    async def test_get_current_user_info_success(self, mocker):
        mock_user_service = AsyncMock()
        mock_user = MagicMock()
        mock_user.id = "user_123"
        mock_user.email = "test@example.com"
        mock_user.full_name = "John Doe"
        mock_user.phone = "1234567890"
        mock_user.gender = MagicMock(value="Male")
        mock_user.role = MagicMock(value="PATIENT")
        mock_user.status = MagicMock(value="ACTIVE")
        mock_user.is_verified = True
        mock_user.created_at = "2026-01-01T00:00:00"
        mock_user_service.get_user_by_id = AsyncMock(return_value=mock_user)
        mocker.patch('backend.routers.users.UserService', return_value=mock_user_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/users/me")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_current_user_info_not_found(self, mocker):
        mock_user_service = AsyncMock()
        mock_user_service.get_user_by_id = AsyncMock(side_effect=ValueError("User not found"))
        mocker.patch('backend.routers.users.UserService', return_value=mock_user_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/users/me")

        assert response.status_code == 404

    @pytest.mark.asyncio
    async def test_update_current_user_success(self, mocker):
        mock_user_service = AsyncMock()
        mock_user = MagicMock()
        mock_user.id = "user_123"
        mock_user.email = "test@example.com"
        mock_user.full_name = "John Updated"
        mock_user.phone = "9876543210"
        mock_user.role = MagicMock(value="PATIENT")
        mock_user_service.update_user = AsyncMock(return_value=mock_user)
        mocker.patch('backend.routers.users.UserService', return_value=mock_user_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.put(
                "/api/v1/users/me",
                json={
                    "full_name": "John Updated",
                    "phone": "9876543210"
                }
            )

        assert response.status_code == 200


    @pytest.mark.asyncio
    async def test_get_user_by_id_success(self, mocker):
        mock_user_service = AsyncMock()
        mock_user = MagicMock()
        mock_user.id = "user_456"
        mock_user.email = "other@example.com"
        mock_user.full_name = "Jane Doe"
        mock_user.phone = "1234567890"
        mock_user.role = MagicMock(value="PATIENT")
        mock_user.status = MagicMock(value="ACTIVE")
        mock_user.created_at = "2026-01-01T00:00:00"
        mock_user_service.get_user_by_id = AsyncMock(return_value=mock_user)
        mocker.patch('backend.routers.users.UserService', return_value=mock_user_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/users/user_456")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_user_by_id_not_found(self, mocker):
        mock_user_service = AsyncMock()
        mock_user_service.get_user_by_id = AsyncMock(side_effect=ValueError("User not found"))
        mocker.patch('backend.routers.users.UserService', return_value=mock_user_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/users/invalid_user")

        assert response.status_code == 404
