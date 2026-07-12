from typing import Optional, List, Tuple
from datetime import datetime
from bson import ObjectId
from backend.middleware.database import db
from backend.models.payment import Payment
from backend.constants.status import PaymentStatus
import logging

logger = logging.getLogger(__name__)


class PaymentRepository:
    """
    Repository for payment database operations.

    Provides methods for creating, reading, updating, and deleting
    payment records.
    """

    def __init__(self):
        self.collection = db.get_db().payments

    async def create(self, payment: Payment) -> Payment:
        """Create a new payment record."""
        try:
            payment_dict = payment.to_dict()
            result = await self.collection.insert_one(payment_dict)
            payment.id = str(result.inserted_id)
            return payment
        except Exception as e:
            logger.error(f"Error creating payment: {e}")
            raise

    async def find_by_id(self, payment_id: str) -> Optional[Payment]:
        """Find a payment by ID."""
        try:
            if not ObjectId.is_valid(payment_id):
                return None
            payment_dict = await self.collection.find_one({"_id": ObjectId(payment_id)})
            if payment_dict:
                payment_dict["id"] = str(payment_dict["_id"])
                return Payment(**payment_dict)
            return None
        except Exception as e:
            logger.error(f"Error finding payment: {e}")
            return None

    async def find_by_payment_id(self, payment_id: str) -> Optional[Payment]:
        """Find a payment by human-readable payment ID."""
        try:
            payment_dict = await self.collection.find_one({"payment_id": payment_id})
            if payment_dict:
                payment_dict["id"] = str(payment_dict["_id"])
                return Payment(**payment_dict)
            return None
        except Exception as e:
            logger.error(f"Error finding payment by payment ID: {e}")
            return None

    async def find_by_appointment_id(self, appointment_id: str) -> Optional[Payment]:
        """Find a payment by appointment ID."""
        try:
            payment_dict = await self.collection.find_one({"appointment_id": appointment_id},sort=[("created_at", -1)])
            if payment_dict:
                payment_dict["id"] = str(payment_dict["_id"])
                return Payment(**payment_dict)
            return None
        except Exception as e:
            logger.error(f"Error finding payment by appointment: {e}")
            return None

    async def update(self, payment_id: str, update_data: dict) -> bool:
        """Update a payment record."""
        try:
            update_data["updated_at"] = datetime.utcnow()
            result = await self.collection.update_one(
                {"_id": ObjectId(payment_id)},
                {"$set": update_data}
            )
            return result.modified_count > 0
        except Exception as e:
            logger.error(f"Error updating payment: {e}")
            return False

    async def update_status(self, payment_id: str, status: PaymentStatus) -> bool:
        """Update payment status."""
        try:
            result = await self.collection.update_one(
                {"_id": ObjectId(payment_id)},
                {
                    "$set": {
                        "status": status.value if hasattr(status, 'value') else status,
                        "updated_at": datetime.utcnow()
                    }
                }
            )
            return result.modified_count > 0
        except Exception as e:
            logger.error(f"Error updating payment status: {e}")
            return False

    async def get_payments_by_patient(
        self,
        patient_id: str,
        limit: int = 20,
        skip: int = 0
    ) -> Tuple[List[Payment], int]:
        """Get payments for a patient."""
        try:
            query = {"patient_id": patient_id}
            total = await self.collection.count_documents(query)

            cursor = self.collection.find(query).sort("created_at", -1).skip(skip).limit(limit)

            payments = []
            async for payment_dict in cursor:
                payment_dict["id"] = str(payment_dict["_id"])
                payments.append(Payment(**payment_dict))

            return payments, total
        except Exception as e:
            logger.error(f"Error getting patient payments: {e}")
            return [], 0

    async def get_total_revenue(self, doctor_id: Optional[str] = None) -> float:
        """Get total revenue from completed payments."""
        try:
            query = {"status": PaymentStatus.COMPLETED.value}
            if doctor_id:
                query["doctor_id"] = doctor_id

            pipeline = [
                {"$match": query},
                {"$group": {"_id": None, "total": {"$sum": "$amount"}}}
            ]
            result = await self.collection.aggregate(pipeline).to_list(None)
            return result[0]["total"] if result else 0.0
        except Exception as e:
            logger.error(f"Error getting total revenue: {e}")
            return 0.0
