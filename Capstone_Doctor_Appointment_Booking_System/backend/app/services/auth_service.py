from datetime import datetime
from app.constants.roles import UserRole
from app.models.user import User
from app.models.patient import Patient
from app.models.doctor import Doctor
from app.repositories.user_repository import UserRepository
from app.repositories.patient_repository import PatientRepository
from app.repositories.doctor_repository import DoctorRepository
from app.utils.password import hash_password, verify_password
from app.core.security import create_access_token


class AuthService:
    """Authentication service with business logic"""

    def __init__(self):
        self.user_repository = UserRepository()
        self.patient_repository = PatientRepository()
        self.doctor_repository = DoctorRepository()

    async def register_patient(self, payload) -> User:
        """Register a new patient"""

        # Check if email already exists
        existing_user = await self.user_repository.get_by_email(payload.email)
        if existing_user:
            raise ValueError("Email already registered")

        # Create User
        user = User(
            full_name=payload.full_name.strip(),
            email=payload.email,
            password=hash_password(payload.password),
            phone_number=payload.phone_number,
            role=UserRole.PATIENT
        )

        created_user = await self.user_repository.create_user(user)

        # Create Patient Profile
        patient = Patient(
            user=created_user,
            gender=payload.gender,
            date_of_birth=payload.date_of_birth
        )
        await self.patient_repository.create_patient(patient)

        return created_user

    async def register_doctor(self, payload) -> User:
        """Register a new doctor"""

        # Check if email already exists
        existing_user = await self.user_repository.get_by_email(payload.email)
        if existing_user:
            raise ValueError("Email already registered")

        # Check if license number already exists
        existing_doctor = await self.doctor_repository.get_by_license(payload.license_number)
        if existing_doctor:
            raise ValueError("License number already registered")

        # Create User
        user = User(
            full_name=payload.full_name.strip(),
            email=payload.email,
            password=hash_password(payload.password),
            phone_number=payload.phone_number,
            role=UserRole.DOCTOR,
            is_approved=False
        )

        created_user = await self.user_repository.create_user(user)

        # Create Doctor Profile
        doctor = Doctor(
            user=created_user,
            qualification=payload.qualification,
            specialization=payload.specialization,
            experience=payload.experience,
            license_number=payload.license_number,
            consultation_fee=payload.consultation_fee,
            clinic_address=payload.clinic_address
        )
        await self.doctor_repository.create_doctor(doctor)

        return created_user

    async def register_first_admin(self, payload) -> User:
        """Register the first admin (no authentication required)"""

        # Check if any admin exists
        existing_admin = await self.user_repository.get_by_role(UserRole.ADMIN)
        if existing_admin:
            raise ValueError("Admin already exists. Please use admin login.")

        # Check if email already exists
        existing_user = await self.user_repository.get_by_email(payload.email)
        if existing_user:
            raise ValueError("Email already registered")

        # Create User
        user = User(
            full_name=payload.full_name.strip(),
            email=payload.email,
            password=hash_password(payload.password),
            phone_number=payload.phone_number,
            role=UserRole.ADMIN,
            is_active=True
        )

        created_user = await self.user_repository.create_user(user)
        return created_user

    async def register_admin_by_admin(self, payload, current_admin) -> User:
        """Create a new admin by existing admin (authentication required)"""

        # Verify current user is admin
        if current_admin.role != UserRole.ADMIN:
            raise ValueError("Only administrators can create new admin accounts")

        # Check if email already exists
        existing_user = await self.user_repository.get_by_email(payload.email)
        if existing_user:
            raise ValueError("Email already registered")

        # Create User
        user = User(
            full_name=payload.full_name.strip(),
            email=payload.email,
            password=hash_password(payload.password),
            phone_number=payload.phone_number,
            role=UserRole.ADMIN,
            is_active=True
        )

        created_user = await self.user_repository.create_user(user)
        return created_user

    async def login(self, payload) -> dict:
        """Authenticate user and return JWT token"""

        # Find user by email
        user = await self.user_repository.get_by_email(payload.email)

        if not user:
            raise ValueError("Invalid email or password")

        # Verify password
        if not verify_password(payload.password, user.password):
            raise ValueError("Invalid email or password")

        # Check if account is active
        if not user.is_active:
            raise ValueError("Account is deactivated. Please contact admin")


        if user.role == UserRole.DOCTOR and not user.is_approved:
            raise ValueError(
            "Your account is pending admin approval. "
            "You will receive an email notification once approved."
        )

        # Update last login time
        user.updated_at = datetime.utcnow()
        await self.user_repository.update_user(user)


        # Get role-specific profile info
        profile_info = {}
        if user.role == UserRole.PATIENT:
            patient = await self.patient_repository.get_by_user_id(str(user.id))
            if patient:
                profile_info = {
                    "gender": patient.gender.value if patient.gender else None,
                    "date_of_birth": patient.date_of_birth.isoformat() if patient.date_of_birth else None
                }
        elif user.role == UserRole.DOCTOR:
            doctor = await self.doctor_repository.get_by_user_id(str(user.id))
            if doctor:
                profile_info = {
                    "specialization": doctor.specialization.value if doctor.specialization else None,
                    "qualification": doctor.qualification,
                    "consultation_fee": doctor.consultation_fee
                }

        # Create JWT token
        access_token = create_access_token({
            "sub": str(user.id),
            "email": user.email,
            "role": user.role.value,
            "full_name": user.full_name,
            **profile_info
        })

        return {
            "user": user,
            "access_token": access_token,
            "profile": profile_info
        }
