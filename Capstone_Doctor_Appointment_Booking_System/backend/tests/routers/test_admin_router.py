import pytest
from unittest.mock import AsyncMock, patch, MagicMock
from fastapi.testclient import TestClient
from backend.main import app
from backend.database.dependencies import get_current_admin, require_permission, get_current_user


class TestAdminRouter:
    """Complete test cases for Admin Router."""

    @pytest.fixture
    def test_client(self):
        return TestClient(app)

    @pytest.fixture(autouse=True)
    def setup_dependency_overrides(self):
        """Setup dependency overrides for all tests."""
        admin_user = {
            "user_id": "admin_123",
            "email": "admin@example.com",
            "role": "ADMIN",
            "status": "ACTIVE",
            "sub": "admin_123",
            "is_first_admin": True,
            "full_name": "Admin User"
        }


        async def mock_get_current_admin():
            return admin_user

        async def mock_get_current_user():
            return admin_user


        def mock_require_permission(permission):
            async def dependency():
                return admin_user
            return dependency

        # Set overrides
        app.dependency_overrides[get_current_admin] = mock_get_current_admin
        app.dependency_overrides[get_current_user] = mock_get_current_user
        app.dependency_overrides[require_permission] = mock_require_permission

        yield

        # Clear overrides after test
        app.dependency_overrides.clear()

    @pytest.fixture
    def sample_admin_data(self):
        return {
            "full_name": "Admin User",
            "email": "admin@example.com",
            "phone": "9876543210",
            "gender": "Female",
            "date_of_birth": "10-12-1985",
            "password": "Admin@1234",
            "confirm_password": "Admin@1234"
        }

    @pytest.fixture
    def sample_sub_admin_data(self):
        return {
            "full_name": "Sub Admin",
            "email": "subadmin@example.com",
            "phone": "9876543211",
            "gender": "Male",
            "date_of_birth": "15-05-1990",
            "password": "Sub@1234",
            "confirm_password": "Sub@1234"
        }

    @pytest.fixture
    def sample_doctor_approve_data(self):
        return {"notes": "Approved"}

    @pytest.fixture
    def sample_doctor_reject_data(self):
        return {"reason": "Insufficient credentials"}

    # ==================== SETUP FIRST ADMIN TESTS ====================

    @patch('backend.routers.admin.AdminService')
    def test_create_first_admin_success(self, mock_admin_service, test_client, sample_admin_data):
        """Test create first admin - success."""
        mock_admin_service.return_value.create_first_admin = AsyncMock(return_value={
            "message": "Admin created successfully",
            "admin": {
                "id": "admin123",
                "email": "admin@example.com",
                "full_name": "Admin User",
                "role": "ADMIN",
                "is_first_admin": True
            }
        })

        response = test_client.post("/api/v1/admin/setup-first-admin", json=sample_admin_data)

        assert response.status_code == 200
        data = response.json()
        assert data["message"] == "Admin created successfully"

    @patch('backend.routers.admin.AdminService')
    def test_create_first_admin_value_error(self, mock_admin_service, test_client, sample_admin_data):
        """Test create first admin - ValueError."""
        mock_admin_service.return_value.create_first_admin = AsyncMock(
            side_effect=ValueError("First admin already exists")
        )

        response = test_client.post("/api/v1/admin/setup-first-admin", json=sample_admin_data)

        assert response.status_code == 400

    # ==================== AUTHENTICATED ADMIN TESTS ====================

    @patch('backend.routers.admin.AdminService')
    def test_create_admin_success(self, mock_admin_service, test_client, sample_sub_admin_data):
        """Test create admin - success."""
        mock_admin_service.return_value.create_admin = AsyncMock(return_value={
            "message": "Admin created successfully",
            "admin": {
                "id": "admin456",
                "email": "subadmin@example.com",
                "full_name": "Sub Admin",
                "role": "ADMIN",
                "is_first_admin": False
            }
        })

        response = test_client.post("/api/v1/admin/create-admin", json=sample_sub_admin_data)

        assert response.status_code == 200
        data = response.json()
        assert data["message"] == "Admin created successfully"

    @patch('backend.routers.admin.AdminService')
    def test_create_admin_value_error(self, mock_admin_service, test_client, sample_sub_admin_data):
        """Test create admin - ValueError."""
        mock_admin_service.return_value.create_admin = AsyncMock(
            side_effect=ValueError("Only super admin can create new admins")
        )

        response = test_client.post("/api/v1/admin/create-admin", json=sample_sub_admin_data)

        assert response.status_code == 400

    @patch('backend.routers.admin.AdminService')
    def test_get_all_admins_success(self, mock_admin_service, test_client):
        """Test get all admins - success."""
        mock_admin_service.return_value.get_all_admins = AsyncMock(return_value=[
            {
                "id": "admin123",
                "email": "admin@example.com",
                "full_name": "Admin User",
                "is_first_admin": True
            }
        ])

        response = test_client.get("/api/v1/admin/admins")

        assert response.status_code == 200
        data = response.json()
        assert len(data["admins"]) == 1

    @patch('backend.routers.admin.AdminService')
    def test_delete_admin_success(self, mock_admin_service, test_client):
        """Test delete admin - success."""
        mock_admin_service.return_value.delete_admin = AsyncMock(return_value={
            "message": "Admin deleted successfully",
            "admin_id": "admin456"
        })

        response = test_client.delete("/api/v1/admin/admins/admin456")

        assert response.status_code == 200
        data = response.json()
        assert data["message"] == "Admin deleted successfully"

    @patch('backend.routers.admin.DoctorService')
    def test_get_pending_doctors_success(self, mock_doctor_service, test_client):
        """Test get pending doctors - success."""
        mock_doctor_service.return_value.get_pending_doctors = AsyncMock(return_value=[
            {
                "id": "doc123",
                "full_name": "Dr. Smith",
                "specialization": "CARDIOLOGIST"
            }
        ])

        response = test_client.get("/api/v1/admin/doctors/pending?limit=10&skip=0")

        assert response.status_code == 200
        data = response.json()
        assert data["count"] == 1

    @patch('backend.routers.admin.DoctorService')
    def test_get_all_doctors_success(self, mock_doctor_service, test_client):
        """Test get all doctors - success."""
        mock_doctor_service.return_value.get_all_doctors = AsyncMock(return_value=[
            {
                "id": "doc123",
                "full_name": "Dr. Smith",
                "specialization": "CARDIOLOGIST",
                "status": "APPROVED"
            }
        ])
        mock_doctor_service.return_value.count_all_doctors = AsyncMock(return_value=1)

        response = test_client.get("/api/v1/admin/doctors?limit=10&skip=0")

        assert response.status_code == 200
        data = response.json()
        assert data["total"] == 1

    @patch('backend.routers.admin.DoctorService')
    def test_approve_doctor_success(self, mock_doctor_service, test_client, sample_doctor_approve_data):
        """Test approve doctor - success."""
        mock_doctor_service.return_value.approve_doctor = AsyncMock(return_value={
            "message": "Doctor approved successfully",
            "doctor": {"id": "doc123", "status": "APPROVED"}
        })

        response = test_client.post(
            "/api/v1/admin/doctors/doc123/approve",
            json=sample_doctor_approve_data
        )

        assert response.status_code == 200
        data = response.json()
        assert data["message"] == "Doctor approved successfully"

    @patch('backend.routers.admin.DoctorService')
    def test_reject_doctor_success(self, mock_doctor_service, test_client, sample_doctor_reject_data):
        """Test reject doctor - success."""
        mock_doctor_service.return_value.reject_doctor = AsyncMock(return_value={
            "message": "Doctor rejected successfully",
            "doctor": {"id": "doc123", "status": "REJECTED"}
        })

        response = test_client.post(
            "/api/v1/admin/doctors/doc123/reject",
            json=sample_doctor_reject_data
        )

        assert response.status_code == 200
        data = response.json()
        assert data["message"] == "Doctor rejected successfully"

    @patch('backend.routers.admin.DoctorService')
    def test_get_pending_profile_updates_success(self, mock_doctor_service, test_client):
        """Test get pending profile updates - success."""
        mock_doctor_service.return_value.get_pending_profile_updates = AsyncMock(return_value={
            "doctors": [{"id": "doc123", "full_name": "Dr. Smith"}],
            "total": 1
        })

        response = test_client.get("/api/v1/admin/doctors/profile-updates?limit=10&skip=0")

        assert response.status_code == 200
        data = response.json()
        assert data["total"] == 1

    @patch('backend.routers.admin.DoctorService')
    def test_approve_profile_update_success(self, mock_doctor_service, test_client):
        """Test approve profile update - success."""
        mock_doctor_service.return_value.approve_profile_update = AsyncMock(return_value={
            "message": "Profile update approved and applied",
            "doctor": {"id": "doc123"}
        })

        response = test_client.post("/api/v1/admin/doctors/doc123/approve-update")

        assert response.status_code == 200
        data = response.json()
        assert data["message"] == "Profile update approved and applied"

    @patch('backend.routers.admin.DoctorService')
    def test_reject_profile_update_success(self, mock_doctor_service, test_client, sample_doctor_reject_data):
        """Test reject profile update - success."""
        mock_doctor_service.return_value.reject_profile_update = AsyncMock(return_value={
            "message": "Profile update rejected",
            "doctor": {"id": "doc123"}
        })

        response = test_client.post(
            "/api/v1/admin/doctors/doc123/reject-update",
            json=sample_doctor_reject_data
        )

        assert response.status_code == 200
        data = response.json()
        assert data["message"] == "Profile update rejected"

    @patch('backend.routers.admin.DoctorService')
    def test_get_doctor_stats_success(self, mock_doctor_service, test_client):
        """Test get doctor stats - success."""
        mock_doctor_service.return_value.get_doctor_stats = AsyncMock(return_value={
            "total": 10,
            "pending": 3,
            "approved": 5,
            "rejected": 2,
            "suspended": 0
        })

        response = test_client.get("/api/v1/admin/doctors/stats")

        assert response.status_code == 200
        data = response.json()
        assert data["total"] == 10



@patch('backend.routers.admin.AdminRepository')
def test_get_audit_logs_success(self, mock_admin_repo, test_client):
    """Test get audit logs - success."""
    mock_logs = [
        {
            "admin_id": "admin123",
            "admin_email": "admin@example.com",
            "action": "APPROVE_DOCTOR",
            "target_id": "doc123",
            "target_email": "doctor@example.com",
            "details": {"doctor_name": "Dr. Smith"},
            "created_at": "2026-07-14T10:00:00"
        }
    ]

    mock_admin_repo.return_value.get_audit_logs = AsyncMock(return_value=mock_logs)
    mock_admin_repo.return_value.count_audit_logs = AsyncMock(return_value=1)

    response = test_client.get("/api/v1/admin/audit-logs?limit=10&skip=0")

    assert response.status_code == 200
    data = response.json()
    assert data["total"] == 1

    @patch('backend.routers.admin.AdminRepository')
    def test_get_audit_logs_with_filters(self, mock_admin_repo, test_client):
        """Test get audit logs with filters."""
        mock_logs = [
            {
                "admin_id": "admin123",
                "admin_email": "admin@example.com",
                "action": "APPROVE_DOCTOR",
                "target_id": "doc123",
                "target_email": "doctor@example.com",
                "details": {"doctor_name": "Dr. Smith"},
                "created_at": "2026-07-14T10:00:00"
            }
        ]

        mock_admin_repo.return_value.get_audit_logs = AsyncMock(return_value=mock_logs)
        mock_admin_repo.return_value.count_audit_logs = AsyncMock(return_value=1)

        response = test_client.get(
            "/api/v1/admin/audit-logs?limit=10&skip=0&admin_id=admin123&action=APPROVE_DOCTOR"
        )

        assert response.status_code == 200
        data = response.json()
        assert data["total"] == 1

    @patch('backend.routers.admin.AdminRepository')
    def test_get_audit_logs_exception(self, mock_admin_repo, test_client):
        """Test get audit logs - exception."""
        mock_admin_repo.return_value.get_audit_logs = AsyncMock(
            side_effect=Exception("Database error")
        )

        response = test_client.get("/api/v1/admin/audit-logs")

        assert response.status_code == 500
        assert "Failed to get audit logs" in response.json()["detail"]

