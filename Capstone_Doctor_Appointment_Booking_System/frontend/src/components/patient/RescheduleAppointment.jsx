import React, { useState, useEffect } from 'react';
import { Modal, Button, Form, Row, Col } from 'react-bootstrap';
import { getDoctorAvailability } from '../../api/doctor';
import { rescheduleAppointment } from '../../api/appointment';
import toast from 'react-hot-toast';

const RescheduleAppointment = ({ show, onHide, appointment, onSuccess }) => {
  const [loading, setLoading] = useState(false);
  const [slots, setSlots] = useState([]);
  const [selectedDate, setSelectedDate] = useState('');
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [reason, setReason] = useState('');
  const [loadingSlots, setLoadingSlots] = useState(false);

  /**
   * Load availability when date changes.
   */
  useEffect(() => {
    if (selectedDate && appointment?.doctor_id) {
      loadAvailability();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedDate]);

  /**
   * Load availability for selected date.
   */
  const loadAvailability = async () => {
    setLoadingSlots(true);
    try {
      const data = await getDoctorAvailability(appointment.doctor_id, selectedDate);
      setSlots(data.availabilities || []);
      setSelectedSlot(null);
    } catch (error) {
      console.error('Error loading availability:', error);
      toast.error('Failed to load availability');
    } finally {
      setLoadingSlots(false);
    }
  };

  /**
   * Handle form submission.
   */
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!selectedDate) {
      toast.error('Please select a new date');
      return;
    }

    if (!selectedSlot) {
      toast.error('Please select a new time slot');
      return;
    }

    if (!reason.trim()) {
      toast.error('Please provide a reason for rescheduling');
      return;
    }

    setLoading(true);

    try {
      const data = {
        appointment_date: selectedDate,
        appointment_time: selectedSlot.start_time,
        reason,
      };

      await rescheduleAppointment(appointment.id, data);
      onSuccess(appointment.id, {
        appointment_date: selectedDate,
        appointment_time: selectedSlot.start_time,
        status: 'RESCHEDULED',
      });
      onHide();
    } catch (error) {
      // Error handled by axios interceptor
    } finally {
      setLoading(false);
    }
  };

  /**
   * Reset form on hide.
   */
  const handleClose = () => {
    setSelectedDate('');
    setSelectedSlot(null);
    setReason('');
    setSlots([]);
    onHide();
  };

  return (
    <Modal show={show} onHide={handleClose} centered size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Reschedule Appointment</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <p className="text-muted">
            Reschedule your appointment with <strong>{appointment?.doctor_name}</strong>.
          </p>

          <Row>
            <Col md={6}>
              <Form.Group className="mb-3">
                <Form.Label className="fw-semibold">New Date</Form.Label>
                <Form.Control
                  type="date"
                  value={selectedDate}
                  onChange={(e) => setSelectedDate(e.target.value)}
                  min={new Date().toISOString().split('T')[0]}
                  required
                  style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                />
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group className="mb-3">
                <Form.Label className="fw-semibold">New Time</Form.Label>
                {loadingSlots ? (
                  <p className="text-muted">Loading slots...</p>
                ) : slots.length === 0 ? (
                  <p className="text-muted">No slots available on this date</p>
                ) : (
                  <div className="d-flex flex-wrap gap-2">
                    {slots.map((slot) => (
                      <Button
                        key={slot.id}
                        variant={selectedSlot?.id === slot.id ? 'primary' : 'outline-primary'}
                        size="sm"
                        className="px-3 py-2"
                        style={{ borderRadius: '8px' }}
                        onClick={() => setSelectedSlot(slot)}
                      >
                        {slot.start_time} - {slot.end_time}
                      </Button>
                    ))}
                  </div>
                )}
              </Form.Group>
            </Col>
          </Row>

          <Form.Group className="mt-2">
            <Form.Label className="fw-semibold">
              Reason for rescheduling <span className="text-danger">*</span>
            </Form.Label>
            <Form.Control
              as="textarea"
              rows={2}
              placeholder="Please provide a reason for rescheduling"
              value={reason}
              onChange={(e) => setReason(e.target.value)}
              required
              style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
            />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose} disabled={loading}>
            Cancel
          </Button>
          <Button variant="primary" type="submit" disabled={loading}>
            {loading ? 'Rescheduling...' : 'Confirm Reschedule'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default RescheduleAppointment;
