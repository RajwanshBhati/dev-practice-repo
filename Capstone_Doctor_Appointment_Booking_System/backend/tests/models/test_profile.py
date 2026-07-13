import pytest
from backend.models.profile import DoctorProfile, PatientProfile
from backend.enums.user_enums import DoctorStatus, VerificationStatus
from backend.enums.doctor_enums import Specialization


class TestDoctorProfile:
    def test_doctor_profile_defaults(self):
        """Test doctor profile defaults."""
        profile = DoctorProfile(
            user_id="user123",
            qualification="MD",
            specialization=Specialization.CARDIOLOGIST,
            experience_years=5,
            license_number="LIC123",
            consultation_fee=100,
            clinic_address="123 Main St"
        )

        assert profile.user_id == "user123"
        assert profile.specialization == Specialization.CARDIOLOGIST
        assert profile.status == DoctorStatus.PENDING
        assert profile.verification_status == VerificationStatus.PENDING
        assert profile.rating == 0.0

    def test_doctor_profile_validation(self):
        """Test doctor profile validation."""
        profile = DoctorProfile(
            user_id="user123",
            qualification="MD",
            specialization=Specialization.CARDIOLOGIST,
            experience_years=5,
            license_number="LIC123",
            consultation_fee=100,
            clinic_address="123 Main St"
        )

        assert profile.qualification == "MD"
        assert profile.experience_years == 5
        assert profile.consultation_fee == 100
