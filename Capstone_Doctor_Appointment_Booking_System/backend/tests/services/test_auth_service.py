import pytest
from unittest.mock import AsyncMock, patch, MagicMock
from datetime import datetime, timedelta
from backend.services.auth_service import AuthService
from backend.schemas.request.user_request import PatientRegister, DoctorRegister
from backend.schemas.request.auth_request import UserLogin, RefreshToken, LogoutRequest
from backend.models.user import User
from backend.enums.user_enums import Gender, UserStatus, DoctorStatus
from backend.constants.roles import UserRole


class TestAuthServiceComplete:
    """Complete test cases for AuthService - 100% coverage."""

    @pytest.fixture
    def mock_user(self):
        return User(
            id="user123",
            email="john@example.com",
            password_hash="$2b$12$hashed_password",
            full_name="John Doe",
            phone="1234567890",
            gender=Gender.MALE,
            date_of_birth="15-05-1990",
            role=UserRole.PATIENT,
            status=UserStatus.ACTIVE,
            is_verified=True
        )

    @pytest.fixture
    def mock_doctor_user(self):
        return User(
            id="doctor123",
            email="doctor@example.com",
            password_hash="$2b$12$hashed_password",
            full_name="Dr. Smith",
            phone="1234567890",
            gender=Gender.MALE,
            date_of_birth="15-05-1985",
            role=UserRole.DOCTOR,
            status=UserStatus.PENDING,
            is_verified=True
        )

    @pytest.fixture
    def mock_admin_user(self):
        return User(
            id="admin123",
            email="admin@example.com",
            password_hash="$2b$12$hashed_password",
            full_name="Admin User",
            phone="9876543210",
            gender=Gender.FEMALE,
            date_of_birth="10-12-1985",
            role=UserRole.ADMIN,
            status=UserStatus.ACTIVE,
            is_verified=True,
            is_first_admin=True
        )

    # ============ REGISTER PATIENT TESTS ============
    @pytest.mark.asyncio
    async def test_register_patient_success(self, mock_db, mock_user):
        """Test register patient - success."""
        patient_data = PatientRegister(
            full_name="John Doe",
            email="john@example.com",
            phone="1234567890",
            gender=Gender.MALE,
            date_of_birth="15-05-1990",
            password="Test@1234",
            confirm_password="Test@1234"
        )

        with patch('backend.services.auth_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_email = AsyncMock(return_value=None)
            mock_repo.return_value.create = AsyncMock(return_value=mock_user)

            with patch('backend.services.auth_service.jwt_service') as mock_jwt:
                mock_jwt.create_access_token = MagicMock(return_value="access_token")
                mock_jwt.create_refresh_token = MagicMock(return_value="refresh_token")

                service = AuthService()
                result = await service.register_patient(patient_data)

                assert result["message"] == "Registration successful"
                assert result["access_token"] == "access_token"
                assert result["user"]["email"] == "john@example.com"

    @pytest.mark.asyncio
    async def test_register_patient_email_exists(self, mock_db, mock_user):
        """Test register patient - email already exists."""
        patient_data = PatientRegister(
            full_name="John Doe",
            email="john@example.com",
            phone="1234567890",
            gender=Gender.MALE,
            date_of_birth="15-05-1990",
            password="Test@1234",
            confirm_password="Test@1234"
        )

        with patch('backend.services.auth_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_email = AsyncMock(return_value=mock_user)

            service = AuthService()

            with pytest.raises(ValueError, match="User with this email already exists"):
                await service.register_patient(patient_data)


    # ============ LOGIN TESTS ============
    @pytest.mark.asyncio
    async def test_login_success(self, mock_db, mock_user):
        """Test login - success."""
        login_data = UserLogin(email="john@example.com", password="Test@1234")

        with patch('backend.services.auth_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_email = AsyncMock(return_value=mock_user)
            mock_repo.return_value.update_last_login = AsyncMock(return_value=True)

            with patch('backend.services.auth_service.security.verify_password') as mock_verify:
                mock_verify.return_value = True

                with patch('backend.services.auth_service.jwt_service') as mock_jwt:
                    mock_jwt.create_access_token = MagicMock(return_value="access_token")
                    mock_jwt.create_refresh_token = MagicMock(return_value="refresh_token")

                    service = AuthService()
                    result = await service.login(login_data)

                    assert result["message"] == "Login successful"
                    assert result["access_token"] == "access_token"

    @pytest.mark.asyncio
    async def test_login_user_not_found(self, mock_db):
        """Test login - user not found."""
        login_data = UserLogin(email="unknown@example.com", password="Test@1234")

        with patch('backend.services.auth_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_email = AsyncMock(return_value=None)

            service = AuthService()

            with pytest.raises(ValueError, match="Invalid email or password"):
                await service.login(login_data)

    @pytest.mark.asyncio
    async def test_login_invalid_password(self, mock_db, mock_user):
        """Test login - invalid password."""
        login_data = UserLogin(email="john@example.com", password="Wrong@1234")

        with patch('backend.services.auth_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_email = AsyncMock(return_value=mock_user)

            with patch('backend.services.auth_service.security.verify_password') as mock_verify:
                mock_verify.return_value = False

                service = AuthService()

                with pytest.raises(ValueError, match="Invalid email or password"):
                    await service.login(login_data)

    @pytest.mark.asyncio
    async def test_login_inactive_user(self, mock_db):
        """Test login - inactive user."""
        inactive_user = User(
            id="user123",
            email="john@example.com",
            password_hash="$2b$12$hashed_password",
            full_name="John Doe",
            phone="1234567890",
            gender=Gender.MALE,
            date_of_birth="15-05-1990",
            role=UserRole.PATIENT,
            status=UserStatus.INACTIVE
        )

        login_data = UserLogin(email="john@example.com", password="Test@1234")

        with patch('backend.services.auth_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_email = AsyncMock(return_value=inactive_user)

            with patch('backend.services.auth_service.security.verify_password') as mock_verify:
                mock_verify.return_value = True

                service = AuthService()

                with pytest.raises(ValueError, match="Your account has been deactivated"):
                    await service.login(login_data)

    # ============ LOGIN WITH STATUS CHECK TESTS ============
    @pytest.mark.asyncio
    async def test_login_with_status_check_success(self, mock_db, mock_user):
        """Test login with status check - success."""
        login_data = UserLogin(email="john@example.com", password="Test@1234")

        with patch('backend.services.auth_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_email = AsyncMock(return_value=mock_user)
            mock_repo.return_value.update_last_login = AsyncMock(return_value=True)

            with patch('backend.services.auth_service.security.verify_password') as mock_verify:
                mock_verify.return_value = True

                with patch('backend.services.auth_service.jwt_service') as mock_jwt:
                    mock_jwt.create_access_token = MagicMock(return_value="access_token")
                    mock_jwt.create_refresh_token = MagicMock(return_value="refresh_token")

                    service = AuthService()
                    result = await service.login_with_status_check(login_data)

                    assert result["message"] == "Login successful"
                    assert result["user"]["id"] == "user123"

    @pytest.mark.asyncio
    async def test_login_with_status_check_pending_user(self, mock_db):
        """Test login with status check - pending user."""
        pending_user = User(
            id="user123",
            email="john@example.com",
            password_hash="$2b$12$hashed_password",
            full_name="John Doe",
            phone="1234567890",
            gender=Gender.MALE,
            date_of_birth="15-05-1990",
            role=UserRole.PATIENT,
            status=UserStatus.PENDING
        )

        login_data = UserLogin(email="john@example.com", password="Test@1234")

        with patch('backend.services.auth_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_email = AsyncMock(return_value=pending_user)

            with patch('backend.services.auth_service.security.verify_password') as mock_verify:
                mock_verify.return_value = True

                service = AuthService()

                with pytest.raises(ValueError, match="Account pending approval"):
                    await service.login_with_status_check(login_data)

    @pytest.mark.asyncio
    async def test_login_with_status_check_suspended_user(self, mock_db):
        """Test login with status check - suspended user."""
        suspended_user = User(
            id="user123",
            email="john@example.com",
            password_hash="$2b$12$hashed_password",
            full_name="John Doe",
            phone="1234567890",
            gender=Gender.MALE,
            date_of_birth="15-05-1990",
            role=UserRole.PATIENT,
            status=UserStatus.SUSPENDED
        )

        login_data = UserLogin(email="john@example.com", password="Test@1234")

        with patch('backend.services.auth_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_email = AsyncMock(return_value=suspended_user)

            with patch('backend.services.auth_service.security.verify_password') as mock_verify:
                mock_verify.return_value = True

                service = AuthService()

                with pytest.raises(ValueError, match="Account has been suspended"):
                    await service.login_with_status_check(login_data)

    # ============ VALIDATE TOKEN TESTS ============
    @pytest.mark.asyncio
    async def test_validate_token_success(self, mock_db, mock_user):
        """Test validate token - success."""
        with patch('backend.services.auth_service.TokenBlacklistRepository') as mock_blacklist:
            mock_blacklist.return_value.is_blacklisted = AsyncMock(return_value=False)

            with patch('backend.services.auth_service.jwt_service') as mock_jwt:
                mock_jwt.decode_token.return_value = {
                    "sub": "user123",
                    "email": "john@example.com",
                    "role": "PATIENT"
                }

                with patch('backend.services.auth_service.UserRepository') as mock_repo:
                    mock_repo.return_value.find_by_id = AsyncMock(return_value=mock_user)

                    service = AuthService()
                    result = await service.validate_token("valid_token")

                    assert result["user_id"] == "user123"
                    assert result["email"] == "john@example.com"

    @pytest.mark.asyncio
    async def test_validate_token_blacklisted(self, mock_db):
        """Test validate token - blacklisted."""
        with patch('backend.services.auth_service.TokenBlacklistRepository') as mock_blacklist:
            mock_blacklist.return_value.is_blacklisted = AsyncMock(return_value=True)

            service = AuthService()

            with pytest.raises(ValueError, match="Token has been revoked"):
                await service.validate_token("blacklisted_token")

    @pytest.mark.asyncio
    async def test_validate_token_invalid(self, mock_db):
        """Test validate token - invalid."""
        with patch('backend.services.auth_service.TokenBlacklistRepository') as mock_blacklist:
            mock_blacklist.return_value.is_blacklisted = AsyncMock(return_value=False)

            with patch('backend.services.auth_service.jwt_service') as mock_jwt:
                mock_jwt.decode_token.side_effect = ValueError("Invalid token")

                service = AuthService()

                with pytest.raises(ValueError, match="Invalid authentication token"):
                    await service.validate_token("invalid_token")

    # ============ REFRESH TOKEN TESTS ============
    @pytest.mark.asyncio
    async def test_refresh_token_success(self, mock_db, mock_user):
        """Test refresh token - success."""
        refresh_data = RefreshToken(refresh_token="valid_refresh_token")

        with patch('backend.services.auth_service.TokenBlacklistRepository') as mock_blacklist:
            mock_blacklist.return_value.is_blacklisted = AsyncMock(return_value=False)

            with patch('backend.services.auth_service.jwt_service') as mock_jwt:
                mock_jwt.decode_token.return_value = {
                    "sub": "user123",
                    "type": "refresh"
                }
                mock_jwt.create_access_token = MagicMock(return_value="new_access_token")

                with patch('backend.services.auth_service.UserRepository') as mock_repo:
                    mock_repo.return_value.find_by_id = AsyncMock(return_value=mock_user)

                    service = AuthService()
                    result = await service.refresh_token(refresh_data)

                    assert result["access_token"] == "new_access_token"

    @pytest.mark.asyncio
    async def test_refresh_token_blacklisted(self, mock_db):
        """Test refresh token - blacklisted."""
        refresh_data = RefreshToken(refresh_token="blacklisted_refresh_token")

        with patch('backend.services.auth_service.TokenBlacklistRepository') as mock_blacklist:
            mock_blacklist.return_value.is_blacklisted = AsyncMock(return_value=True)

            service = AuthService()

            with pytest.raises(ValueError, match="Token has been revoked"):
                await service.refresh_token(refresh_data)

    @pytest.mark.asyncio
    async def test_refresh_token_invalid_type(self, mock_db):
        """Test refresh token - invalid type."""
        refresh_data = RefreshToken(refresh_token="access_token")

        with patch('backend.services.auth_service.TokenBlacklistRepository') as mock_blacklist:
            mock_blacklist.return_value.is_blacklisted = AsyncMock(return_value=False)

            with patch('backend.services.auth_service.jwt_service') as mock_jwt:
                mock_jwt.decode_token.return_value = {
                    "sub": "user123",
                    "type": "access"
                }

                service = AuthService()

                with pytest.raises(ValueError, match="Invalid authentication token"):
                    await service.refresh_token(refresh_data)



    @pytest.mark.asyncio
    async def test_forgot_password_user_not_found(self, mock_db):
        """Test forgot password - user not found."""
        with patch('backend.services.auth_service.UserRepository') as mock_repo:
            mock_repo.return_value.find_by_email = AsyncMock(return_value=None)

            service = AuthService()
            result = await service.forgot_password("unknown@example.com")

            assert "reset link" in result["message"]

    # ============ RESET PASSWORD TESTS ============
    @pytest.mark.asyncio
    async def test_reset_password_success(self, mock_db, mock_user):
        """Test reset password - success."""
        with patch('backend.services.auth_service.jwt_service') as mock_jwt:
            mock_jwt.decode_token.return_value = {
                "sub": "user123",
                "type": "reset"
            }

            with patch('backend.services.auth_service.UserRepository') as mock_repo:
                mock_repo.return_value.find_by_id = AsyncMock(return_value=mock_user)
                mock_repo.return_value.update = AsyncMock(return_value=mock_user)

                with patch('backend.services.auth_service.security.hash_password') as mock_hash:
                    mock_hash.return_value = "new_hashed_password"

                    service = AuthService()
                    result = await service.reset_password("reset_token", "NewPass@1234")

                    assert "reset successfully" in result["message"]

    @pytest.mark.asyncio
    async def test_reset_password_invalid_token(self, mock_db):
        """Test reset password - invalid token."""
        with patch('backend.services.auth_service.jwt_service') as mock_jwt:
            mock_jwt.decode_token.side_effect = ValueError("Invalid token")

            service = AuthService()

            with pytest.raises(ValueError, match="Reset link is invalid or has expired"):
                await service.reset_password("invalid_token", "NewPass@1234")

    @pytest.mark.asyncio
    async def test_reset_password_wrong_type(self, mock_db):
        """Test reset password - wrong token type."""
        with patch('backend.services.auth_service.jwt_service') as mock_jwt:
            mock_jwt.decode_token.return_value = {
                "sub": "user123",
                "type": "access"
            }

            service = AuthService()

            with pytest.raises(ValueError, match="Reset link is invalid or has expired"):
                await service.reset_password("access_token", "NewPass@1234")

    # ============ LOGOUT TESTS ============
    @pytest.mark.asyncio
    async def test_logout_success(self, mock_db):
        """Test logout - success."""
        with patch('backend.services.auth_service.jwt_service') as mock_jwt:
            mock_jwt.decode_token.return_value = {
                "sub": "user123",
                "exp": int((datetime.now() + timedelta(hours=1)).timestamp())
            }

            with patch('backend.services.auth_service.TokenBlacklistRepository') as mock_blacklist:
                mock_blacklist.return_value.add_to_blacklist = AsyncMock(return_value=True)

                service = AuthService()
                result = await service.logout("user123", "access_token")

                assert result["message"] == "Logout successful"

    @pytest.mark.asyncio
    async def test_logout_blacklist_failed(self, mock_db):
        """Test logout - blacklist failed."""
        with patch('backend.services.auth_service.jwt_service') as mock_jwt:
            mock_jwt.decode_token.return_value = {
                "sub": "user123",
                "exp": int((datetime.now() + timedelta(hours=1)).timestamp())
            }

            with patch('backend.services.auth_service.TokenBlacklistRepository') as mock_blacklist:
                mock_blacklist.return_value.add_to_blacklist = AsyncMock(return_value=False)

                service = AuthService()

                with pytest.raises(ValueError, match="Failed to blacklist token"):
                    await service.logout("user123", "access_token")



    async def test_validate_token_invalid(self, mock_db):
       """Test validate token - invalid."""
       with patch('backend.services.auth_service.TokenBlacklistRepository') as mock_blacklist:
           mock_blacklist.return_value.is_blacklisted = AsyncMock(return_value=False)

           with patch('backend.services.auth_service.jwt_service') as mock_jwt:
               mock_jwt.decode_token.side_effect = ValueError("Invalid token")

               service = AuthService()

               with pytest.raises(ValueError) as exc_info:
                   await service.validate_token("invalid_token")


               assert "Invalid" in str(exc_info.value) or "authentication" in str(exc_info.value).lower()
