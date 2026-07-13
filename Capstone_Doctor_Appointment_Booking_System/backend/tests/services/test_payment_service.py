import pytest
from unittest.mock import AsyncMock, patch
from backend.services.payment_service import PaymentService
from backend.schemas.request.payment_request import PaymentInitiateRequest, PaymentConfirmRequest, PaymentRefundRequest
from backend.constants.status import PaymentStatus, AppointmentStatus
from backend.enums.payment_enums import PaymentMethod


class TestPaymentService:
    """Complete test cases for PaymentService - 100% coverage."""

    @pytest.fixture
    def mock_appointment(self):
        from backend.models.appointment import Appointment
        from datetime import datetime, timedelta
        future_date = (datetime.now() + timedelta(days=3)).strftime("%Y-%m-%d")
        return Appointment(
            id="appt123",
            patient_id="patient123",
            patient_name="John Doe",
            doctor_id="doctor123",
            doctor_name="Dr. Smith",
            appointment_date=future_date,
            appointment_time="10:00",
            status=AppointmentStatus.SCHEDULED
        )

    @pytest.fixture
    def mock_payment(self):
        from backend.models.payment import Payment
        return Payment(
            id="pay123",
            payment_id="PAY-123456",
            transaction_id="TXN-123456",
            appointment_id="appt123",
            patient_id="patient123",
            doctor_id="doctor123",
            amount=150.50,
            method=PaymentMethod.CREDIT_CARD,
            status=PaymentStatus.PENDING
        )

    @pytest.mark.asyncio
    async def test_initiate_payment_success(self, mock_db, mock_appointment, mock_payment):
        """Test initiate payment - success."""
        payment_data = PaymentInitiateRequest(
            appointment_id="appt123",
            method=PaymentMethod.CREDIT_CARD
        )

        with patch('backend.services.payment_service.AppointmentRepository') as mock_appt_repo:
            mock_appt_repo.return_value.get_appointment_by_id = AsyncMock(return_value=mock_appointment)

            with patch('backend.services.payment_service.PaymentRepository') as mock_pay_repo:
                mock_pay_repo.return_value.find_by_appointment_id = AsyncMock(return_value=None)
                mock_pay_repo.return_value.create = AsyncMock(return_value=mock_payment)

                with patch('backend.services.payment_service.DoctorRepository') as mock_doctor_repo:
                    mock_doctor_repo.return_value.find_by_id = AsyncMock(return_value=None)

                    service = PaymentService()
                    result = await service.initiate_payment("patient123", payment_data)

                    assert result.message == "Payment initiated successfully."
                    assert result.payment.payment_id == "PAY-123456"

    @pytest.mark.asyncio
    async def test_initiate_payment_appointment_not_found(self, mock_db):
        """Test initiate payment - appointment not found."""
        payment_data = PaymentInitiateRequest(
            appointment_id="unknown_appt",
            method=PaymentMethod.CREDIT_CARD
        )

        with patch('backend.services.payment_service.AppointmentRepository') as mock_appt_repo:
            mock_appt_repo.return_value.get_appointment_by_id = AsyncMock(return_value=None)

            service = PaymentService()

            with pytest.raises(ValueError, match="Appointment not found"):
                await service.initiate_payment("patient123", payment_data)

    @pytest.mark.asyncio
    async def test_initiate_payment_wrong_patient(self, mock_db, mock_appointment):
        """Test initiate payment - wrong patient."""
        payment_data = PaymentInitiateRequest(
            appointment_id="appt123",
            method=PaymentMethod.CREDIT_CARD
        )

        with patch('backend.services.payment_service.AppointmentRepository') as mock_appt_repo:
            mock_appt_repo.return_value.get_appointment_by_id = AsyncMock(return_value=mock_appointment)

            service = PaymentService()

            with pytest.raises(ValueError, match="You don't have permission to perform this action"):
                await service.initiate_payment("wrong_patient", payment_data)

    @pytest.mark.asyncio
    async def test_confirm_payment_success(self, mock_db, mock_payment):
        """Test confirm payment - success."""
        confirm_data = PaymentConfirmRequest(
            payment_id="PAY-123456",
            card_last_four="1234"
        )

        with patch('backend.services.payment_service.PaymentRepository') as mock_pay_repo:
            mock_pay_repo.return_value.find_by_payment_id = AsyncMock(return_value=mock_payment)
            mock_pay_repo.return_value.update_status = AsyncMock(return_value=True)
            mock_pay_repo.return_value.find_by_id = AsyncMock(return_value=mock_payment)

            with patch('backend.services.payment_service.AppointmentRepository') as mock_appt_repo:
                mock_appt_repo.return_value.update_appointment = AsyncMock(return_value=True)

                with patch('backend.services.payment_service.random.random') as mock_random:
                    mock_random.return_value = 0.5  # Success

                    service = PaymentService()
                    result = await service.confirm_payment("patient123", confirm_data)

                    assert result.message == "Payment successful"
                    assert result.appointment_status == "CONFIRMED"

    @pytest.mark.asyncio
    async def test_confirm_payment_failure(self, mock_db, mock_payment):
        """Test confirm payment - failure."""
        confirm_data = PaymentConfirmRequest(
            payment_id="PAY-123456"
        )

        with patch('backend.services.payment_service.PaymentRepository') as mock_pay_repo:
            mock_pay_repo.return_value.find_by_payment_id = AsyncMock(return_value=mock_payment)
            mock_pay_repo.return_value.update_status = AsyncMock(return_value=True)
            mock_pay_repo.return_value.find_by_id = AsyncMock(return_value=mock_payment)

            with patch('backend.services.payment_service.random.random') as mock_random:
                mock_random.return_value = 0.95  # Failure

                service = PaymentService()
                result = await service.confirm_payment("patient123", confirm_data)

                assert result.message == "Payment processing failed"
                assert result.appointment_status == "SCHEDULED"

    @pytest.mark.asyncio
    async def test_refund_payment_success(self, mock_db, mock_payment):
        """Test refund payment - success."""
        mock_payment.status = PaymentStatus.COMPLETED
        refund_data = PaymentRefundRequest(reason="Patient requested refund")

        with patch('backend.services.payment_service.PaymentRepository') as mock_pay_repo:
            mock_pay_repo.return_value.find_by_payment_id = AsyncMock(return_value=mock_payment)
            mock_pay_repo.return_value.update = AsyncMock(return_value=True)
            mock_pay_repo.return_value.find_by_id = AsyncMock(return_value=mock_payment)

            with patch('backend.services.payment_service.AppointmentRepository') as mock_appt_repo:
                mock_appt_repo.return_value.update_appointment = AsyncMock(return_value=True)

                service = PaymentService()
                result = await service.refund_payment("patient123", "PAY-123456", refund_data)

                assert result.message == "Payment refunded successfully"
                assert result.refund_id is not None

    @pytest.mark.asyncio
    async def test_refund_payment_not_completed(self, mock_db, mock_payment):
        """Test refund payment - not completed."""
        refund_data = PaymentRefundRequest(reason="Patient requested refund")

        with patch('backend.services.payment_service.PaymentRepository') as mock_pay_repo:
            mock_pay_repo.return_value.find_by_payment_id = AsyncMock(return_value=mock_payment)

            service = PaymentService()

            with pytest.raises(ValueError, match="Only completed payments can be refunded"):
                await service.refund_payment("patient123", "PAY-123456", refund_data)

    @pytest.mark.asyncio
    async def test_get_payment_status_success(self, mock_db, mock_payment):
        """Test get payment status - success."""
        with patch('backend.services.payment_service.PaymentRepository') as mock_pay_repo:
            mock_pay_repo.return_value.find_by_payment_id = AsyncMock(return_value=mock_payment)

            service = PaymentService()
            result = await service.get_payment_status("PAY-123456")

            assert result.payment_id == "PAY-123456"
            assert result.amount == 150.50

    @pytest.mark.asyncio
    async def test_get_payment_status_not_found(self, mock_db):
        """Test get payment status - not found."""
        with patch('backend.services.payment_service.PaymentRepository') as mock_pay_repo:
            mock_pay_repo.return_value.find_by_payment_id = AsyncMock(return_value=None)

            service = PaymentService()

            with pytest.raises(ValueError, match="Payment not found"):
                await service.get_payment_status("UNKNOWN")

    @pytest.mark.asyncio
    async def test_get_patient_payments_success(self, mock_db, mock_payment):
        """Test get patient payments - success."""
        with patch('backend.services.payment_service.PaymentRepository') as mock_pay_repo:
            mock_pay_repo.return_value.get_payments_by_patient = AsyncMock(return_value=([mock_payment], 1))

            service = PaymentService()
            result = await service.get_patient_payments("patient123", limit=10, skip=0)

            assert result["total"] == 1
            assert len(result["payments"]) == 1

    @pytest.mark.asyncio
    async def test_get_revenue_stats_success(self, mock_db):
        """Test get revenue stats - success."""
        with patch('backend.services.payment_service.PaymentRepository') as mock_pay_repo:
            mock_pay_repo.return_value.get_total_revenue = AsyncMock(return_value=1000.0)

            service = PaymentService()
            result = await service.get_revenue_stats()

            assert result["total_revenue"] == 1000.0
            assert result["currency"] == "USD"
