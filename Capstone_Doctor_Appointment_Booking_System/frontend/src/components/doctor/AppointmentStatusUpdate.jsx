import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { updateAppointmentStatus } from '../../api/appointment';
import { STATUS_OPTIONS, STATUS_TRANSITIONS } from '../../utils/constants';
import toast from 'react-hot-toast';

const AppointmentStatusUpdate = ({ show, onHide, appointment, onSuccess }) => {
  const [loading, setLoading] = useState(false);
  const [status, setStatus] = useState('');
  const [notes, setNotes] = useState('');

  /**
   * Reset form when modal opens.
   */
  React.useEffect(() => {
    if (show && appointment) {
      setStatus(appointment.status || '');
      setNotes('');
    }
  }, [show, appointment]);

  /**
   * Get allowed status transitions.
   */
  const getAvailableStatuses = () => {
    if (!appointment) return [];
    const currentStatus = appointment.status;
    const allowed = STATUS_TRANSITIONS[currentStatus] || [];
    return STATUS_OPTIONS.filter((opt) => allowed.includes(opt.value));
  };

  /**
   * Handle form submission.
   */
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!status) {
      toast.error('Please select a status');
      return;
    }

    setLoading(true);

    try {
      await updateAppointmentStatus(appointment.id, { status, notes });
      toast.success(`Appointment marked as ${status}`);
      onSuccess(appointment.id, { status, notes });
      onHide();
    } catch (error) {
      // Error handled by axios interceptor
    } finally {
      setLoading(false);
    }
  };

  const availableStatuses = getAvailableStatuses();

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>Update Appointment Status</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <p>
            Patient: <strong>{appointment?.patient_name}</strong>
          </p>
          <p>
            Date: <strong>{appointment?.appointment_date}</strong> at{' '}
            <strong>{appointment?.appointment_time}</strong>
          </p>
          <p className="text-muted">
            Current Status: <strong>{appointment?.status}</strong>
          </p>

          <hr />

          <Form.Group className="mb-3">
            <Form.Label className="fw-semibold">New Status</Form.Label>
            <Form.Select
              value={status}
              onChange={(e) => setStatus(e.target.value)}
              required
              style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
            >
              <option value="">Select new status...</option>
              {availableStatuses.map((opt) => (
                <option key={opt.value} value={opt.value}>
                  {opt.label}
                </option>
              ))}
            </Form.Select>
            {availableStatuses.length === 0 && (
              <Form.Text className="text-danger">
                No status changes available for this appointment.
              </Form.Text>
            )}
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label className="fw-semibold">Notes (Optional)</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              placeholder="Add any notes about this status update..."
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
            />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onHide} disabled={loading}>
            Cancel
          </Button>
          <Button
            variant="primary"
            type="submit"
            disabled={loading || availableStatuses.length === 0}
          >
            {loading ? 'Updating...' : 'Update Status'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default AppointmentStatusUpdate;
