import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { cancelAppointment } from '../../api/appointment';
import toast from 'react-hot-toast';

const CancelAppointment = ({ show, onHide, appointment, onSuccess }) => {
  const [loading, setLoading] = useState(false);
  const [reason, setReason] = useState('');

  /**
   * Handle form submission.
   */
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!reason.trim()) {
      toast.error('Please provide a reason for cancellation');
      return;
    }

    setLoading(true);

    try {
      await cancelAppointment(appointment.id, { reason });
      onSuccess(appointment.id);
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
    setReason('');
    onHide();
  };

  return (
    <Modal show={show} onHide={handleClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>Cancel Appointment</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <p>
            Are you sure you want to cancel your appointment with{' '}
            <strong>{appointment?.doctor_name}</strong> on{' '}
            <strong>{appointment?.appointment_date}</strong> at{' '}
            <strong>{appointment?.appointment_time}</strong>?
          </p>

          <Form.Group className="mt-3">
            <Form.Label className="fw-semibold">
              Reason for cancellation <span className="text-danger">*</span>
            </Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              placeholder="Please provide a reason for cancelling this appointment"
              value={reason}
              onChange={(e) => setReason(e.target.value)}
              required
              style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
            />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose} disabled={loading}>
            Keep Appointment
          </Button>
          <Button variant="danger" type="submit" disabled={loading}>
            {loading ? 'Cancelling...' : 'Yes, Cancel'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default CancelAppointment;
