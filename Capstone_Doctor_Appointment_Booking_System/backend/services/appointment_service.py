from typing import Dict, Any, List, Optional
from datetime import datetime, timedelta

from bson import ObjectId
from backend.repositories.appointment_repository import AppointmentRepository
from backend.repositories.doctor_repository import DoctorRepository
from backend.repositories.payment_repository import PaymentRepository
from backend.repositories.user_repository import UserRepository
from backend.models.appointment import Appointment
from backend.schemas.request.appointment_request import (
    AppointmentBookRequest,
    AppointmentUpdateRequest,
    AppointmentCancelRequest,
    AppointmentRescheduleRequest
)
from backend.constants import ErrorMessages, SuccessMessages
from backend.constants.status import AppointmentStatus
from backend.enums.user_enums import DoctorStatus
import logging

logger = logging.getLogger(__name__)


class AppointmentService:
    """Service layer for appointment operations, handling business logic and validation."""
    def __init__(self):
        self.appointment_repo = AppointmentRepository()
        self.doctor_repo = DoctorRepository()
        self.user_repo = UserRepository()
        self.payment_repo = PaymentRepository()
    async def book_appointment(
        self,
        patient_id: str,
        booking_data: AppointmentBookRequest
    ) -> Dict[str, Any]:
        """
        Book an appointment with double booking prevention.

        Uses atomic transactions to prevent race conditions when multiple
        patients try to book the same slot simultaneously.
        """
        doctor = await self.doctor_repo.find_by_id(booking_data.doctor_id)
        if not doctor:
            raise ValueError(ErrorMessages.DOC_1301)

        if doctor.status != DoctorStatus.APPROVED:
            raise ValueError(ErrorMessages.DOC_1306)

        patient = await self.user_repo.find_by_id(patient_id)
        if not patient:
            raise ValueError(ErrorMessages.USER_1101)

        doctor_user = await self.user_repo.find_by_id(doctor.user_id)
        if not doctor_user:
            raise ValueError(ErrorMessages.DOC_1301)
        # Check availability
        availabilities = await self.appointment_repo.availability_collection.find({
            "doctor_id": doctor.user_id,
            "date": booking_data.appointment_date,
            "is_available": True
        }).to_list(None)

        matching_slot = None
        for slot in availabilities:
            slot["id"] = str(slot["_id"])
            if slot["start_time"] <= booking_data.appointment_time < slot["end_time"]:
                matching_slot = slot
                break

        if not matching_slot:
            raise ValueError(ErrorMessages.APP_1208)

        # Check minimum notice (2 hours)
        booking_datetime = datetime.strptime(
            f"{booking_data.appointment_date} {booking_data.appointment_time}",
            "%Y-%m-%d %H:%M"
        )
        if booking_datetime - datetime.now() < timedelta(hours=2):
            raise ValueError(ErrorMessages.APP_1210)

        # Create appointment with transaction
        appointment = Appointment(
            patient_id=patient_id,
            patient_name=patient.full_name,
            doctor_id=doctor.user_id,
            doctor_name=doctor_user.full_name,
            appointment_date=booking_data.appointment_date,
            appointment_time=booking_data.appointment_time,
            reason=booking_data.reason,
            notes=booking_data.notes
        )

        created = await self.appointment_repo.create_appointment_with_transaction(
            appointment,
            matching_slot["id"]
        )

        logger.info(f"Appointment booked: {created.id} by patient {patient_id}")

        return {
            "message": SuccessMessages.APPOINTMENT_BOOKED,
            "appointment": {
                "id": created.id,
                "patient_id": created.patient_id,
                "doctor_id": created.doctor_id,
                "appointment_date": created.appointment_date,
                "appointment_time": created.appointment_time,
                "status": created.status.value if hasattr(created.status, 'value') else created.status
            }
        }

    async def get_patient_appointments(
        self,
        patient_id: str,
        status: Optional[AppointmentStatus] = None,
        limit: int = 20,
        skip: int = 0
    ) -> Dict[str, Any]:
        """Get appointments for a patient with pagination."""
        appointments, total = await self.appointment_repo.get_appointments_by_patient(
            patient_id, status, limit, skip
        )

        return {
            "appointments": [
                {
                    "id": a.id,
                    "doctor_id": a.doctor_id,
                    "doctor_name": a.doctor_name,
                    "appointment_date": a.appointment_date,
                    "appointment_time": a.appointment_time,
                    "status": a.status.value if hasattr(a.status, 'value') else a.status,
                    "reason": a.reason,
                    "payment_status": a.payment_status.value if hasattr(a.payment_status, 'value') else a.payment_status,
                    "payment_amount": a.payment_amount,
                    "created_at": a.created_at.isoformat()
                }
                for a in appointments
            ],
            "total": total,
            "page": (skip // limit) + 1 if limit > 0 else 1,
            "per_page": limit,
            "total_pages": (total + limit - 1) // limit if limit > 0 else 1
        }

    async def get_doctor_appointments(
        self,
        doctor_id: str,
        status: Optional[AppointmentStatus] = None,
        limit: int = 20,
        skip: int = 0
    ) -> Dict[str, Any]:
        """Get appointments for a doctor with pagination."""
        appointments, total = await self.appointment_repo.get_appointments_by_doctor(
            doctor_id, status, limit, skip
        )

        return {
            "appointments": [
                {
                    "id": a.id,
                    "patient_id": a.patient_id,
                    "patient_name": a.patient_name,
                    "appointment_date": a.appointment_date,
                    "appointment_time": a.appointment_time,
                    "status": a.status.value if hasattr(a.status, 'value') else a.status,
                    "reason": a.reason,
                    "notes": a.notes,
                    "payment_status": a.payment_status.value if hasattr(a.payment_status, 'value') else a.payment_status,
                    "payment_amount": a.payment_amount,
                    "created_at": a.created_at.isoformat()
                }
                for a in appointments
            ],
            "total": total,
            "page": (skip // limit) + 1 if limit > 0 else 1,
            "per_page": limit,
            "total_pages": (total + limit - 1) // limit if limit > 0 else 1
        }

    async def cancel_appointment(
        self,
        appt_id: str,
        patient_id: str,
        cancel_data: AppointmentCancelRequest
    ) -> Dict[str, Any]:
        """
        Cancel an appointment and release the availability slot.

        Cancellation allowed only if appointment is not completed
        and within the cancellation window (2 hours before).
        """
        appointment = await self.appointment_repo.get_appointment_by_id(appt_id)
        if not appointment:
            raise ValueError(ErrorMessages.APP_1201)

        if appointment.patient_id != patient_id:
            raise ValueError(ErrorMessages.AUTH_1005)

        if appointment.status == AppointmentStatus.COMPLETED:
            raise ValueError(ErrorMessages.APP_1206)

        if appointment.status == AppointmentStatus.CANCELLED:
            raise ValueError("Appointment is already cancelled")

        # Check cancellation window (2 hours)
        appt_datetime = datetime.strptime(
            f"{appointment.appointment_date} {appointment.appointment_time}",
            "%Y-%m-%d %H:%M"
        )
        if appt_datetime - datetime.now() < timedelta(hours=2):
            raise ValueError(ErrorMessages.APP_1205)

        await self.appointment_repo.cancel_appointment(appt_id)

        logger.info(f"Appointment cancelled: {appt_id} by patient {patient_id}")

        return {
            "message": SuccessMessages.APPOINTMENT_CANCELLED,
            "appointment_id": appt_id
        }

    async def reschedule_appointment(
        self,
        appt_id: str,
        patient_id: str,
        reschedule_data: AppointmentRescheduleRequest
    ) -> Dict[str, Any]:
        """
        Reschedule an appointment to a new date and time.

        Releases the old slot and books a new one atomically.
        """
        appointment = await self.appointment_repo.get_appointment_by_id(appt_id)
        if not appointment:
            raise ValueError(ErrorMessages.APP_1201)

        if appointment.patient_id != patient_id:
            raise ValueError(ErrorMessages.AUTH_1005)

        if appointment.status == AppointmentStatus.COMPLETED:
            raise ValueError(ErrorMessages.APP_1206)

        # Check availability for new slot
        availabilities = await self.appointment_repo.availability_collection.find({
            "doctor_id": appointment.doctor_id,
            "date": reschedule_data.appointment_date,
            "is_available": True
        }).to_list(None)

        matching_slot = None
        for slot in availabilities:
            slot["id"] = str(slot["_id"])
            if slot["start_time"] <= reschedule_data.appointment_time < slot["end_time"]:
                matching_slot = slot
                break

        if not matching_slot:
            raise ValueError(ErrorMessages.APP_1208)

        # Release old slot
        await self.appointment_repo.availability_collection.update_one(
            {"booking_id": appt_id},
            {
                "$set": {
                    "is_available": True,
                    "booked_by": None,
                    "booking_id": None,
                    "updated_at": datetime.utcnow()
                }
            }
        )

        # Book new slot
        await self.appointment_repo.availability_collection.update_one(
            {"_id": ObjectId(matching_slot["id"])},
            {
                "$set": {
                    "is_available": False,
                    "booked_by": patient_id,
                    "booking_id": appt_id,
                    "updated_at": datetime.utcnow()
                }
            }
        )

        # Update appointment
        await self.appointment_repo.update_appointment(
            appt_id,
            {
                "appointment_date": reschedule_data.appointment_date,
                "appointment_time": reschedule_data.appointment_time,
                "status": AppointmentStatus.RESCHEDULED.value,
                "notes": reschedule_data.reason
            }
        )

        logger.info(f"Appointment rescheduled: {appt_id}")

        return {
            "message": SuccessMessages.APPOINTMENT_RESCHEDULED,
            "appointment_id": appt_id
        }

    async def update_appointment_status(
        self,
        appt_id: str,
        doctor_id: str,
        update_data: AppointmentUpdateRequest
    ) -> Dict[str, Any]:
        """Update appointment status (doctor only)."""
        appointment = await self.appointment_repo.get_appointment_by_id(appt_id)
        if not appointment:
            raise ValueError(ErrorMessages.APP_1201)

        if appointment.doctor_id != doctor_id:
            raise ValueError(ErrorMessages.AUTH_1005)

        # Validate status transition
        valid_transitions = {
            AppointmentStatus.SCHEDULED: [AppointmentStatus.CONFIRMED, AppointmentStatus.CANCELLED],
            AppointmentStatus.CONFIRMED: [AppointmentStatus.COMPLETED, AppointmentStatus.NO_SHOW, AppointmentStatus.CANCELLED],
            AppointmentStatus.COMPLETED: [],
            AppointmentStatus.CANCELLED: [],
            AppointmentStatus.NO_SHOW: [],
            AppointmentStatus.RESCHEDULED: [AppointmentStatus.CONFIRMED, AppointmentStatus.CANCELLED]
        }

        current = appointment.status
        new_status = update_data.status

        if new_status not in valid_transitions.get(current, []):
            raise ValueError(f"Cannot transition from {current.value} to {new_status.value}")

        await self.appointment_repo.update_appointment(
            appt_id,
            {
                "status": new_status.value,
                "notes": update_data.notes
            }
        )

        logger.info(f"Appointment status updated: {appt_id} to {new_status.value}")

        return {
            "message": f"Appointment marked as {new_status.value}",
            "appointment_id": appt_id
        }

    async def get_appointment_stats(
        self,
        doctor_id: Optional[str] = None
    ) -> Dict[str, Any]:
        """Get appointment statistics."""
        return await self.appointment_repo.get_appointment_stats(doctor_id)
