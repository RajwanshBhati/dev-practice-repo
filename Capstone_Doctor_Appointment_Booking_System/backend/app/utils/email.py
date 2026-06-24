import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from app.core.config import settings

# Here I used Mock Function

async def send_approval_email(email: str, name: str, status: str, reason: str = None):
    """
    Send approval/rejection email to doctor
    """
    # Mock email - just print for now
    # We'll implement actual email sending later

    if status == "approved":
        subject = "Your Doctor Account Has Been Approved!"
        body = f"""
        Dear Dr. {name},

        Congratulations! Your doctor account has been approved by the admin.

        You can now login to your account and start managing appointments.

        Login URL: http://localhost:3000/login

        Thank you for joining our platform!

        Best regards,
        HealthBook Team
        """
    else:
        subject = "Your Doctor Account Application Status"
        body = f"""
        Dear Dr. {name},

        We regret to inform you that your doctor account application has been rejected.

        Reason: {reason}

        If you have any questions, please contact our support team.

        Best regards,
        HealthBook Team
        """

    # Print for now (we'll implement actual email later)
    print(f"Email to: {email}")
    print(f"Subject: {subject}")
    print(f"Body: {body}")
    print("=" * 50)

    return True
