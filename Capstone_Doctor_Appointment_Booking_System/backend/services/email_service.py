import asyncio
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from typing import Optional
import logging
from backend.middleware.config import settings

logger = logging.getLogger(__name__)


class EmailService:
    """
    Sends transactional emails to users.
    """

    @staticmethod
    def _send_smtp_email(to_email: str, subject: str, body: str) -> bool:
        """
        Synchronously send an email over SMTP
        """
        message = MIMEMultipart()
        message["From"] = f"{settings.EMAIL_FROM_NAME} <{settings.EMAIL_FROM_ADDRESS}>"
        message["To"] = to_email
        message["Subject"] = subject
        message.attach(MIMEText(body, "plain"))

        with smtplib.SMTP(settings.SMTP_HOST, settings.SMTP_PORT, timeout=10) as server:
            if settings.SMTP_USE_TLS:
                server.starttls()
            if settings.SMTP_USER and settings.SMTP_PASSWORD:
                server.login(settings.SMTP_USER, settings.SMTP_PASSWORD)
            server.sendmail(settings.EMAIL_FROM_ADDRESS, [to_email], message.as_string())
        return True

    @classmethod
    async def _dispatch(cls, to_email: str, subject: str, body: str) -> bool:
        """
        Send the email if EMAIL_ENABLED + SMTP creds are configured,
        otherwise just log it. Never raises — a failed/unconfigured email
        should never break the calling business flow (e.g. doctor approval).
        """
        if not settings.EMAIL_ENABLED or not settings.SMTP_USER or not settings.SMTP_PASSWORD:
            logger.info(f"[EMAIL NOT SENT - EMAIL_ENABLED/SMTP not configured] To: {to_email}")
            logger.info(f"Subject: {subject}")
            logger.info(f"Body: {body}")
            return True

        try:
            await asyncio.to_thread(cls._send_smtp_email, to_email, subject, body)
            logger.info(f"Email sent to {to_email} - Subject: {subject}")
            return True
        except Exception as e:
            logger.error(f"Failed to send email to {to_email}: {str(e)}")
            return False

    @classmethod
    async def send_doctor_approval_email(
        cls,
        doctor_email: str,
        doctor_name: str,
        status: str,
        rejection_reason: Optional[str] = None
    ) -> bool:
        """Notify a doctor that their account was approved or rejected, including the reason if rejected."""
        login_url = f"{settings.FRONTEND_URL}/login"

        if status == "APPROVED":
            subject = "Your account is approved"
            body = f"""Dear Dr. {doctor_name},

Your account is approved! We are pleased to inform you that your doctor account has been APPROVED.

You can now login to your account and start managing your appointments.

Login URL: {login_url}

Best regards,
{settings.EMAIL_FROM_NAME}
"""
        else:
            subject = "Your Doctor Account Status Update"
            body = f"""Dear Dr. {doctor_name},

We regret to inform you that your doctor account application has been REJECTED.

Reason: {rejection_reason or "Not specified"}

If you have any questions, please contact our support team.

Best regards,
{settings.EMAIL_FROM_NAME}
"""

        return await cls._dispatch(doctor_email, subject, body)

    @classmethod
    async def send_account_created_email(
        cls,
        email: str,
        name: str,
        role: str
    ) -> bool:
        """Let a newly registered user know their account was created and, if applicable, is awaiting approval."""
        subject = "Account Created Successfully"
        body = f"""Dear {name},

Your {role} account has been created successfully!

Please wait for admin approval before you can access your account.

Best regards,
{settings.EMAIL_FROM_NAME}
"""
        return await cls._dispatch(email, subject, body)

    @classmethod
    async def send_password_reset_email(
        cls,
        email: str,
        name: str,
        reset_token: str
    ) -> bool:
        """Send the user a link containing their password-reset token."""
        reset_url = f"{settings.FRONTEND_URL}/reset-password?token={reset_token}"
        subject = "Reset your HealthBook password"
        body = f"""Dear {name},

We received a request to reset your password.

Click the link below to set a new password. This link expires in 30 minutes:
{reset_url}

If you did not request this, you can safely ignore this email.

Best regards,
{settings.EMAIL_FROM_NAME}
"""
        return await cls._dispatch(email, subject, body)

    @classmethod
    async def send_welcome_email(
        cls,
        email: str,
        name: str,
        role: str
    ) -> bool:
        """Send a welcome email once a user's account is fully active and ready to use."""
        login_url = f"{settings.FRONTEND_URL}/login"
        subject = "Welcome to HealthBook!"
        body = f"""Dear {name},

Welcome to our healthcare platform!

Your {role} account has been activated. You can now login and start using our services.

Login URL: {login_url}

Best regards,
{settings.EMAIL_FROM_NAME}
"""
        return await cls._dispatch(email, subject, body)
