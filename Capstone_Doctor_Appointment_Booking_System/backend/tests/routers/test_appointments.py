import pytest
from unittest.mock import AsyncMock, patch
from httpx import AsyncClient
from backend.main import app

class TestAppointmentsRouter:
    @pytest.mark.asyncio
    async def test_book_appointment_success(self, mocker):
        mock_appointment_service = AsyncMock()
        mock_appointment_service.book_appointment = AsyncMock(return_value={
            "id": "app_123",
            "status": "SCHEDULED",
            "message": "Appointment booked successfully"
        })
        mocker.patch('backend.routers.appointments.AppointmentService', return_value=mock_appointment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.post(
                "/api/v1/appointments/book",
                json={
                    "doctor_id": "doctor_456",
                    "appointment_date": "2026-07-20",
                    "appointment_time": "10:00",
                    "reason": "Checkup"
                }
            )

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_book_appointment_value_error(self, mocker):
        mock_appointment_service = AsyncMock()
        mock_appointment_service.book_appointment = AsyncMock(side_effect=ValueError("Slot already booked"))
        mocker.patch('backend.routers.appointments.AppointmentService', return_value=mock_appointment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.post(
                "/api/v1/appointments/book",
                json={
                    "doctor_id": "doctor_456",
                    "appointment_date": "2026-07-20",
                    "appointment_time": "10:00"
                }
            )

        assert response.status_code == 400

    @pytest.mark.asyncio
    async def test_get_patient_appointments_success(self, mocker):
        mock_appointment_service = AsyncMock()
        mock_appointment_service.get_patient_appointments = AsyncMock(return_value={
            "appointments": [{"id": "app_1", "status": "SCHEDULED"}],
            "total": 1
        })
        mocker.patch('backend.routers.appointments.AppointmentService', return_value=mock_appointment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/patients/appointments?limit=10&skip=0")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_doctor_appointments_success(self, mocker):
        mock_appointment_service = AsyncMock()
        mock_appointment_service.get_doctor_appointments = AsyncMock(return_value={
            "appointments": [{"id": "app_1", "status": "SCHEDULED"}],
            "total": 1
        })
        mocker.patch('backend.routers.appointments.AppointmentService', return_value=mock_appointment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/doctors/appointments?limit=10&skip=0")

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_cancel_appointment_success(self, mocker):
        mock_appointment_service = AsyncMock()
        mock_appointment_service.cancel_appointment = AsyncMock(return_value={
            "message": "Appointment cancelled successfully"
        })
        mocker.patch('backend.routers.appointments.AppointmentService', return_value=mock_appointment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.put(
                "/api/v1/appointments/app_123/cancel",
                json={"reason": "Patient request"}
            )

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_reschedule_appointment_success(self, mocker):
        mock_appointment_service = AsyncMock()
        mock_appointment_service.reschedule_appointment = AsyncMock(return_value={
            "message": "Appointment rescheduled successfully"
        })
        mocker.patch('backend.routers.appointments.AppointmentService', return_value=mock_appointment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.put(
                "/api/v1/appointments/app_123/reschedule",
                json={
                    "appointment_date": "2026-07-21",
                    "appointment_time": "11:00"
                }
            )

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_update_appointment_status_success(self, mocker):
        mock_appointment_service = AsyncMock()
        mock_appointment_service.update_appointment_status = AsyncMock(return_value={
            "message": "Appointment status updated successfully"
        })
        mocker.patch('backend.routers.appointments.AppointmentService', return_value=mock_appointment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.put(
                "/api/v1/appointments/app_123/status",
                json={"status": "CONFIRMED"}
            )

        assert response.status_code == 200

    @pytest.mark.asyncio
    async def test_get_appointment_stats_success(self, mocker):
        mock_appointment_service = AsyncMock()
        mock_appointment_service.get_appointment_stats = AsyncMock(return_value={
            "total": 10,
            "scheduled": 5,
            "completed": 3,
            "cancelled": 2
        })
        mocker.patch('backend.routers.appointments.AppointmentService', return_value=mock_appointment_service)

        async with AsyncClient(app=app, base_url="http://test") as client:
            response = await client.get("/api/v1/appointments/stats")

        assert response.status_code == 200
