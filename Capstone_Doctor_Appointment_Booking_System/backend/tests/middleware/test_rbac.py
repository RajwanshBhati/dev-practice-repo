import pytest
from backend.middleware.rbac import RBAC
from backend.constants.roles import UserRole, Permission


class TestRBAC:
    """Test cases for RBAC class."""

    def test_has_permission_patient(self):
        """Test patient permissions."""
        assert RBAC.has_permission(UserRole.PATIENT.value, Permission.VIEW_PROFILE) is True
        assert RBAC.has_permission(UserRole.PATIENT.value, Permission.BOOK_APPOINTMENT) is True
        assert RBAC.has_permission(UserRole.PATIENT.value, Permission.MANAGE_DOCTORS) is False

    def test_has_permission_doctor(self):
        """Test doctor permissions."""
        assert RBAC.has_permission(UserRole.DOCTOR.value, Permission.MANAGE_AVAILABILITY) is True
        assert RBAC.has_permission(UserRole.DOCTOR.value, Permission.UPDATE_APPOINTMENT_STATUS) is True
        assert RBAC.has_permission(UserRole.DOCTOR.value, Permission.APPROVE_DOCTORS) is False

    def test_has_permission_admin(self):
        """Test admin permissions."""
        assert RBAC.has_permission(UserRole.ADMIN.value, Permission.MANAGE_DOCTORS) is True
        assert RBAC.has_permission(UserRole.ADMIN.value, Permission.APPROVE_DOCTORS) is True
        assert RBAC.has_permission(UserRole.ADMIN.value, Permission.VIEW_AUDIT_LOGS) is True

    def test_has_permission_invalid_role(self):
        """Test invalid role."""
        assert RBAC.has_permission("INVALID_ROLE", Permission.VIEW_PROFILE) is False

    def test_has_any_permission_patient(self):
        """Test patient has any permission."""
        permissions = [Permission.VIEW_PROFILE, Permission.BOOK_APPOINTMENT]
        assert RBAC.has_any_permission(UserRole.PATIENT.value, permissions) is True

        permissions = [Permission.MANAGE_DOCTORS, Permission.APPROVE_DOCTORS]
        assert RBAC.has_any_permission(UserRole.PATIENT.value, permissions) is False

    def test_has_any_permission_doctor(self):
        """Test doctor has any permission."""
        permissions = [Permission.MANAGE_AVAILABILITY, Permission.VIEW_DOCTOR_STATS]
        assert RBAC.has_any_permission(UserRole.DOCTOR.value, permissions) is True

    def test_has_any_permission_admin(self):
        """Test admin has any permission."""
        permissions = [Permission.MANAGE_DOCTORS, Permission.VIEW_AUDIT_LOGS]
        assert RBAC.has_any_permission(UserRole.ADMIN.value, permissions) is True

    def test_has_all_permissions_patient(self):
        """Test patient has all permissions."""
        permissions = [Permission.VIEW_PROFILE, Permission.UPDATE_PROFILE]
        assert RBAC.has_all_permissions(UserRole.PATIENT.value, permissions) is True

        permissions = [Permission.VIEW_PROFILE, Permission.MANAGE_DOCTORS]
        assert RBAC.has_all_permissions(UserRole.PATIENT.value, permissions) is False

    def test_has_all_permissions_doctor(self):
        """Test doctor has all permissions."""
        permissions = [Permission.MANAGE_AVAILABILITY, Permission.VIEW_APPOINTMENTS_DOCTOR]
        assert RBAC.has_all_permissions(UserRole.DOCTOR.value, permissions) is True

    def test_has_all_permissions_admin(self):
        """Test admin has all permissions."""
        permissions = [Permission.MANAGE_DOCTORS, Permission.APPROVE_DOCTORS, Permission.VIEW_AUDIT_LOGS]
        assert RBAC.has_all_permissions(UserRole.ADMIN.value, permissions) is True
