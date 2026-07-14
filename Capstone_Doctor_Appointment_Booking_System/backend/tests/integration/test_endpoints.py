import pytest
from unittest.mock import AsyncMock, patch
from fastapi.testclient import TestClient


class TestIntegrationEndpoints:
    """Integration tests for key endpoints."""

    @patch('backend.routers.auth.AuthService')
    def test_complete_patient_flow(self, mock_auth_service, test_client, sample_user_data):
        """Test complete patient flow: register -> login -> profile."""
        # Mock registration
        mock_auth_service.return_value.register_patient = AsyncMock(return_value={
            "message": "Registration successful",
            "access_token": "token123",
            "refresh_token": "refresh123",
            "user": {"id": "user123", "email": "john@example.com", "role": "PATIENT"}
        })

        # Register
        response = test_client.post("/api/v1/auth/register/patient", json=sample_user_data)
        assert response.status_code == 200

        # Mock login
        mock_auth_service.return_value.login_with_status_check = AsyncMock(return_value={
            "message": "Login successful",
            "access_token": "token123",
            "refresh_token": "refresh123",
            "user": {"id": "user123", "email": "john@example.com", "role": "PATIENT"}
        })

        # Login
        login_data = {"email": sample_user_data["email"], "password": sample_user_data["password"]}
        response = test_client.post("/api/v1/auth/login", json=login_data)
        assert response.status_code == 200
        data = response.json()
        assert data["message"] == "Login successful"

