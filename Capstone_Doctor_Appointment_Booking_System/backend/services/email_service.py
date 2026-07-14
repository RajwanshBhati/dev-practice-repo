from typing import Optional
import logging
from datetime import datetime
from backend.middleware.config import settings

logger = logging.getLogger(__name__)

class EmailService:
    """
    Sends transactional emails to users. Right now this just logs the
    email content instead of actually sending it — swap in a real email
    provider here when one is wired up.
    """

    @staticmethod
    async def send_doctor_approval_email(
        doctor_email: str,
        doctor_name: str,
        status: str,
        rejection_reason: Optional[str] = None
    ) -> bool:
        """Notify a doctor that their account was approved or rejected, including the reason if rejected."""
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
        """Let a newly registered user know their account was created and, if applicable, is awaiting approval."""
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
        """Send a welcome email once a user's account is fully active and ready to use."""
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
