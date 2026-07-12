from typing import Optional, List, Tuple
from datetime import datetime
from bson import ObjectId
from backend.middleware.database import db
from backend.models.appointment import Appointment
from backend.constants.status import AppointmentStatus,PaymentStatus
import logging

logger = logging.getLogger(__name__)


class AppointmentRepository:
    """Handles all database reads/writes for appointments, including transactional booking."""
    def __init__(self):
        self.db = db.get_db()
        self.availability_collection = self.db.availabilities
        self.appointment_collection = self.db.appointments

    async def create_appointment_with_transaction(
        self,
        appointment: Appointment,
        availability_id: str
    ) -> Optional[Appointment]:
        """
        Create appointment with transaction to prevent double booking.

        Atomically checks slot availability, marks it as booked, and creates
        the appointment record.
        """
        try:
            updated = await self.availability_collection.find_one_and_update(
                {
                    "_id": ObjectId(availability_id),
                    "is_available": True
                },
                {
                    "$set": {
                        "is_available": False,
                        "booked_by": appointment.patient_id,
                        "updated_at": datetime.utcnow()
                    }
                }
            )

            if not updated:
                raise ValueError("Slot is no longer available")

            # Create appointment
            appt_dict = appointment.to_dict()
            result = await self.appointment_collection.insert_one(appt_dict)
            appointment.id = str(result.inserted_id)

            # Link booking to slot
            await self.availability_collection.update_one(
                {"_id": ObjectId(availability_id)},
                {"$set": {"booking_id": appointment.id}}
            )

            return appointment

        except ValueError:
            raise
        except Exception as e:
            logger.error(f"Error creating appointment: {e}")
            raise

    async def get_appointment_by_id(self, appt_id: str) -> Optional[Appointment]:
        """Get appointment by ID."""
        try:
            if not ObjectId.is_valid(appt_id):
                return None
            appt_dict = await self.appointment_collection.find_one(
                {"_id": ObjectId(appt_id)}
            )
            if appt_dict:
                appt_dict["id"] = str(appt_dict["_id"])
                return Appointment(**appt_dict)
            return None
        except Exception as e:
            logger.error(f"Error getting appointment: {e}")
            return None

    async def get_appointments_by_patient(
        self,
        patient_id: str,
        status: Optional[AppointmentStatus] = None,
        limit: int = 20,
        skip: int = 0
    ) -> Tuple[List[Appointment], int]:
        """Get appointments for a patient with pagination."""
        try:
            query = {"patient_id": patient_id}
            if status:
                query["status"] = status.value

            total = await self.appointment_collection.count_documents(query)

            cursor = self.appointment_collection.find(query).sort(
                "appointment_date", -1
            ).skip(skip).limit(limit)

            appointments = []
            async for appt_dict in cursor:
                appt_dict["id"] = str(appt_dict["_id"])
                appointments.append(Appointment(**appt_dict))

            return appointments, total
        except Exception as e:
            logger.error(f"Error getting patient appointments: {e}")
            return [], 0

    async def get_appointments_by_doctor(
        self,
        doctor_id: str,
        status: Optional[AppointmentStatus] = None,
        limit: int = 20,
        skip: int = 0
    ) -> Tuple[List[Appointment], int]:
        """Get appointments for a doctor with pagination."""
        try:
            query = {"doctor_id": doctor_id}
            if status:
                query["status"] = status.value

            total = await self.appointment_collection.count_documents(query)

            cursor = self.appointment_collection.find(query).sort(
                "appointment_date", -1
            ).skip(skip).limit(limit)

            appointments = []
            async for appt_dict in cursor:
                appt_dict["id"] = str(appt_dict["_id"])
                appointments.append(Appointment(**appt_dict))

            return appointments, total
        except Exception as e:
            logger.error(f"Error getting doctor appointments: {e}")
            return [], 0

    async def update_appointment(self, appt_id: str, update_data: dict) -> bool:
        """Update an appointment."""
        try:
            update_data["updated_at"] = datetime.utcnow()
            result = await self.appointment_collection.update_one(
                {"_id": ObjectId(appt_id)},
                {"$set": update_data}
            )
            return result.modified_count > 0
        except Exception as e:
            logger.error(f"Error updating appointment: {e}")
            return False

    async def cancel_appointment(self, appt_id: str) -> bool:
        """
        Cancel appointment and release the availability slot.

        Updates appointment status to CANCELLED and makes the slot available.
        """
        try:
            appointment = await self.get_appointment_by_id(appt_id)
            if not appointment:
                return False

            # Update status
            result = await self.appointment_collection.update_one(
                {"_id": ObjectId(appt_id)},
                {"$set": {"status": AppointmentStatus.CANCELLED.value}}
            )

            if result.modified_count > 0:
                # Release the slot
                await self.availability_collection.update_one(
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
                return True

            return False
        except Exception as e:
            logger.error(f"Error cancelling appointment: {e}")
            return False

    async def get_appointment_stats(self, doctor_id: Optional[str] = None) -> dict:
        """
        Get appointment statistics.

        Returns counts by status and total revenue for completed appointments.
        """
        try:
            query = {}
            if doctor_id:
                query["doctor_id"] = doctor_id

            total = await self.appointment_collection.count_documents(query)

            # Count by status
            status_counts = {}
            for status in AppointmentStatus:
                count = await self.appointment_collection.count_documents({
                    **query,
                    "status": status.value
                })
                status_counts[status.value] = count

            # Calculate revenue
            pipeline = [
                {"$match": {**query, "status": AppointmentStatus.COMPLETED.value}},
                {"$match": {**query, "payment_status": PaymentStatus.COMPLETED.value}},
                {"$group": {"_id": None, "total": {"$sum": "$payment_amount"}}}
            ]
            revenue_result = await self.appointment_collection.aggregate(pipeline).to_list(None)
            revenue = revenue_result[0]["total"] if revenue_result else 0

            return {
                "total": total,
                "scheduled": status_counts.get(AppointmentStatus.SCHEDULED.value, 0),
                "confirmed": status_counts.get(AppointmentStatus.CONFIRMED.value, 0),
                "completed": status_counts.get(AppointmentStatus.COMPLETED.value, 0),
                "cancelled": status_counts.get(AppointmentStatus.CANCELLED.value, 0),
                "no_show": status_counts.get(AppointmentStatus.NO_SHOW.value, 0),
                "rescheduled": status_counts.get(AppointmentStatus.RESCHEDULED.value, 0),
                "revenue": revenue
            }
        except Exception as e:
            logger.error(f"Error getting appointment stats: {e}")
            return {
                "total": 0,
                "scheduled": 0,
                "confirmed": 0,
                "completed": 0,
                "cancelled": 0,
                "no_show": 0,
                "rescheduled": 0,
                "revenue": 0
            }
