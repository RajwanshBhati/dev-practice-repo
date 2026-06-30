from typing import Optional
import logging
from datetime import datetime
from backend.middleware.config import settings

logger = logging.getLogger(__name__)

class EmailService:
    """Email service for sending notifications"""

    @staticmethod
    async def send_doctor_approval_email(
        doctor_email: str,
        doctor_name: str,
        status: str,  # "APPROVED" or "REJECTED"
        rejection_reason: Optional[str] = None
    ) -> bool:
        """
        Send doctor approval/rejection email
        """
        try:
            if status == "APPROVED":
                subject = "Your Doctor Account Has Been Approved!"
                body = f"""
Dear Dr. {doctor_name},

We are pleased to inform you that your doctor account has been APPROVED!

You can now login to your account and start managing your appointments.

Login URL: http://localhost:3000/login

Best regards,
Healthcare Team
                """
            else:
                subject = "Your Doctor Account Status Update"
                body = f"""
Dear Dr. {doctor_name},

We regret to inform you that your doctor account application has been REJECTED.

Reason: {rejection_reason or "Not specified"}

If you have any questions, please contact our support team.

Best regards,
Healthcare Team
                """

            logger.info(f"Email sent to {doctor_email}")
            logger.info(f"Subject: {subject}")
            logger.info(f"Body: {body}")

            return True

        except Exception as e:
            logger.error(f"Failed to send email: {str(e)}")
            return False

    @staticmethod
    async def send_account_created_email(
        email: str,
        name: str,
        role: str
    ) -> bool:
        """
        Send account created confirmation email
        """
        try:
            subject = "Account Created Successfully"
            body = f"""
Dear {name},

Your {role} account has been created successfully!

Please wait for admin approval before you can access your account.

Best regards,
Healthcare Team
            """

            logger.info(f"Account created email sent to {email}")
            return True

        except Exception as e:
            logger.error(f"Failed to send account email: {str(e)}")
            return False

    @staticmethod
    async def send_welcome_email(
        email: str,
        name: str,
        role: str
    ) -> bool:
        """
        Send welcome email to new user
        """
        try:
            subject = "Welcome to Healthcare Platform!"
            body = f"""
Dear {name},

Welcome to our healthcare platform!

Your {role} account has been activated. You can now login and start using our services.

Login URL: http://localhost:3000/login

Best regards,
Healthcare Team
            """

            logger.info(f"Welcome email sent to {email}")
            return True

        except Exception as e:
            logger.error(f"Failed to send welcome email: {str(e)}")
            return False
