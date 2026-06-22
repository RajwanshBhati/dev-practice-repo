from app.constants.roles import UserRole
from app.models.user import User
from app.repositories.user_repository import UserRepository
from app.utils.password import hash_password


class AuthService:

    def __init__(self):
        self.user_repository = UserRepository()

    async def register_patient(
        self,
        payload
    ):

        existing_user = await self.user_repository.get_by_email(
            payload.email
        )

        if existing_user:
            raise ValueError(
                "Email already registered"
            )

        if payload.password != payload.confirm_password:
            raise ValueError(
                "Password and Confirm Password do not match"
            )

        user = User(
            full_name=payload.full_name,
            email=payload.email,
            password=hash_password(
                payload.password
            ),
            phone_number=payload.phone_number,
            gender=payload.gender,
            date_of_birth=payload.date_of_birth,
            role=UserRole.PATIENT
        )

        created_user = await self.user_repository.create_user(
            user
        )

        return created_user

    async def register_doctor(
        self,
        payload
    ):

        existing_user = await self.user_repository.get_by_email(
            payload.email
        )

        if existing_user:
            raise ValueError(
                "Email already registered"
            )

        if payload.password != payload.confirm_password:
            raise ValueError(
                "Password and Confirm Password do not match"
            )

        user = User(
            full_name=payload.full_name,
            email=payload.email,
            password=hash_password(
                payload.password
            ),
            phone_number=payload.phone_number,

            qualification=payload.qualification,
            specialization=payload.specialization,
            experience=payload.experience,
            license_number=payload.license_number,

            role=UserRole.DOCTOR
        )

        created_user = await self.user_repository.create_user(
            user
        )

        return created_user
