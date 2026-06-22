from app.constants.roles import UserRole
from app.models.user import User
from app.repositories.user_repository import UserRepository
from app.utils.password import hash_password
from app.utils.password import verify_password
from app.core.security import create_access_token


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

    async def login(
        self,
        payload
    ):

        user = await self.user_repository.get_by_email(
            payload.email
        )

        if not user:
            raise ValueError(
                "Invalid email or password"
            )

        if not verify_password(
            payload.password,
            user.password
        ):
            raise ValueError(
                "Invalid email or password"
            )

        access_token = create_access_token(
            {
                "sub": str(user.id),
                "email": user.email,
                "role": user.role.value
            }
        )

        return {
            "user": user,
            "access_token": access_token
        }
