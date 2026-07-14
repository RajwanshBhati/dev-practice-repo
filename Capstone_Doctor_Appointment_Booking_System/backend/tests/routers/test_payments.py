import pytest
from unittest.mock import AsyncMock, patch
from httpx import AsyncClient
from backend.main import app

class TestPaymentsRouter:
    @pytest.mark.asyncio
    async def test_initiate_payment_success(self, mocker):
        mock_payment_service = AsyncMock()
        mock_payment_service.initiate_payment = AsyncMock(return_value={
            "payment_id": "pay_123",
            "transaction_id": "txn_456",
            "amount": 100.00,
            "status": "PENDING",
            "redirect_url": "http://localhost:8000/pay/pay_123"
        })
        mocker.patch('backend.routers.payments.PaymentService', return_value=mock_payment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.post(
                "/api/v1/payments/initiate",
                json={
                    "appointment_id": "app_789",
                    "method": "UPI"
                }
            )

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_initiate_payment_value_error(self, mocker):
        mock_payment_service = AsyncMock()
        mock_payment_service.initiate_payment = AsyncMock(side_effect=ValueError("Appointment not found"))
        mocker.patch('backend.routers.payments.PaymentService', return_value=mock_payment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.post(
                "/api/v1/payments/initiate",
                json={
                    "appointment_id": "invalid_app",
                    "method": "UPI"
                }
            )

        assert response.status_code == 400

    @pytest.mark.asyncio
    async def test_confirm_payment_success(self, mocker):
        mock_payment_service = AsyncMock()
        mock_payment_service.confirm_payment = AsyncMock(return_value={
            "payment_id": "pay_123",
            "status": "COMPLETED",
            "message": "Payment confirmed successfully"
        })
        mocker.patch('backend.routers.payments.PaymentService', return_value=mock_payment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.post(
                "/api/v1/payments/confirm",
                json={
                    "payment_id": "pay_123",
                    "card_last_four": "1234"
                }
            )

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_refund_payment_success(self, mocker):
        mock_payment_service = AsyncMock()
        mock_payment_service.refund_payment = AsyncMock(return_value={
            "payment_id": "pay_123",
            "status": "REFUNDED",
            "refund_id": "ref_456",
            "message": "Payment refunded successfully"
        })
        mocker.patch('backend.routers.payments.PaymentService', return_value=mock_payment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.post(
                "/api/v1/payments/pay_123/refund",
                json={
                    "reason": "Cancellation requested by patient"
                }
            )

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_payment_status_success(self, mocker):
        mock_payment_service = AsyncMock()
        mock_payment_service.get_payment_status = AsyncMock(return_value={
            "payment_id": "pay_123",
            "status": "COMPLETED",
            "amount": 100.00,
            "transaction_id": "txn_456"
        })
        mocker.patch('backend.routers.payments.PaymentService', return_value=mock_payment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/payments/pay_123/status")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_payment_status_not_found(self, mocker):
        mock_payment_service = AsyncMock()
        mock_payment_service.get_payment_status = AsyncMock(side_effect=ValueError("Payment not found"))
        mocker.patch('backend.routers.payments.PaymentService', return_value=mock_payment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/payments/invalid_pay/status")

        assert response.status_code == 404

    @pytest.mark.asyncio
    async def test_get_patient_payments_success(self, mocker):
        mock_payment_service = AsyncMock()
        mock_payment_service.get_patient_payments = AsyncMock(return_value={
            "payments": [{"id": "pay_1", "amount": 100.00, "status": "COMPLETED"}],
            "total": 1
        })
        mocker.patch('backend.routers.payments.PaymentService', return_value=mock_payment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/patients/payments?limit=10&skip=0")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_revenue_stats_success(self, mocker):
        mock_payment_service = AsyncMock()
        mock_payment_service.get_revenue_stats = AsyncMock(return_value={
            "total_revenue": 5000.00,
            "total_transactions": 50,
            "pending_amount": 500.00,
            "refunded_amount": 200.00
        })
        mocker.patch('backend.routers.payments.PaymentService', return_value=mock_payment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/payments/revenue")

        assert response.status_code == 200
