"""
Script to create the first admin
Run this once to set up the initial admin user
"""

import sys
import asyncio
from pathlib import Path

# Add backend to path
backend_path = Path(__file__).parent.parent.parent
sys.path.insert(0, str(backend_path))

from app.core.database import db
from app.core.security import security
from app.models.user import User
from app.repositories.user_repository import UserRepository
from app.repositories.admin_repository import AdminRepository
from app.models.admin import AdminAuditLog
from shared.constants.roles import UserRole
from shared.enums.user_enums import Gender, UserStatus

async def setup_first_admin():
    """Create the first admin user"""

    print("=" * 60)
    print("Creating First Admin User")
    print("=" * 60)

    # Connect to database
    await db.connect()

    # Check if admin exists
    repo = UserRepository()
    existing = await repo.find_by_email("admin@example.com")

    if existing:
        print("Admin already exists!")
        print(f"   Email: {existing.email}")
        print("   If you forgot the password, please reset it.")
        await db.disconnect()
        return

    # Create admin user
    admin = User(
        email="admin@example.com",
        password_hash=security.hash_password("Admin@1234"),
        full_name="System Admin",
        phone="9876543210",
        gender=Gender.MALE,
        date_of_birth="01-01-1980",
        role=UserRole.ADMIN,
        status=UserStatus.ACTIVE,
        is_verified=True,
        is_first_admin=True
    )

    created_admin = await repo.create(admin)

    # Create audit log
    admin_repo = AdminRepository()
    audit_log = AdminAuditLog(
        admin_id=created_admin.id,
        admin_email=created_admin.email,
        action="CREATE_FIRST_ADMIN",
        target_id=created_admin.id,
        target_email=created_admin.email,
        details={
            "admin_name": created_admin.full_name,
            "admin_type": "SUPER_ADMIN"
        }
    )
    await admin_repo.create_audit_log(audit_log)

    print("\nFirst Admin Created Successfully!")
    print("=" * 60)
    print("Email: admin@example.com")
    print("Password: Admin@1234")
    print("=" * 60)
    print("\nIMPORTANT: Please change the password after first login!")

    await db.disconnect()

if __name__ == "__main__":
    asyncio.run(setup_first_admin())
