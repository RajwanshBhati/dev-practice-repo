import React, { useState, useEffect } from 'react';
import { Modal, Form, Button, Row, Col } from 'react-bootstrap';
import toast from 'react-hot-toast';

const SlotForm = ({ show, onHide, slot, onSave }) => {
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    date: '',
    start_time: '',
    end_time: '',
  });

  /**
   * Reset form when modal opens/closes.
   */
  useEffect(() => {
    if (show) {
      if (slot) {
        // Edit mode
        setFormData({
          date: slot.date || '',
          start_time: slot.start_time || '',
          end_time: slot.end_time || '',
        });
      } else {
        // Create mode
        setFormData({
          date: '',
          start_time: '',
          end_time: '',
        });
      }
    }
  }, [show, slot]);

  /**
   * Handle form input changes.
   */
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  /**
   * Validate form data.
   */
  const validateForm = () => {
    if (!formData.date) {
      toast.error('Please select a date');
      return false;
    }
    if (!formData.start_time) {
      toast.error('Please select a start time');
      return false;
    }
    if (!formData.end_time) {
      toast.error('Please select an end time');
      return false;
    }
    if (formData.start_time >= formData.end_time) {
      toast.error('End time must be after start time');
      return false;
    }

    const now = new Date();
    const today = now.toISOString().split('T')[0];
    if (formData.date === today) {
      // Doctors must give at least 2 hours' notice when creating a slot
      // for today, so patients have a fair chance to see and book it.
      const minStart = new Date(now.getTime() + 2 * 60 * 60 * 1000);
      const minStartTime = minStart.toTimeString().slice(0, 5);
      if (formData.start_time < minStartTime) {
        toast.error('For today, the start time must be at least 2 hours from now');
        return false;
      }
    }

    if (formData.start_time >= formData.end_time) {
  toast.error('End time must be after start time');
  return false;
}

// Slot must be at least 15 minutes long (no 5-minute slots etc.)
const MIN_SLOT_DURATION_MINUTES = 15;
const [startH, startM] = formData.start_time.split(':').map(Number);
const [endH, endM] = formData.end_time.split(':').map(Number);
const durationMinutes = (endH * 60 + endM) - (startH * 60 + startM);
if (durationMinutes < MIN_SLOT_DURATION_MINUTES) {
  toast.error(`Slot duration must be at least ${MIN_SLOT_DURATION_MINUTES} minutes`);
  return false;
}

    return true;
  };

  /**
   * Handle form submission.
   */
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setLoading(true);
    try {
      await onSave(formData);
      onHide();
    } catch (error) {
      // Error handled by axios interceptor
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>{slot ? 'Edit Slot' : 'Add New Slot'}</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <Form.Group className="mb-3">
            <Form.Label className="fw-semibold">Date</Form.Label>
            <Form.Control
              type="date"
              name="date"
              value={formData.date}
              onChange={handleChange}
              min={new Date().toISOString().split('T')[0]}
              style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
            />
            <Form.Text className="text-muted">
              For today's date, the start time must be at least 2 hours from now.
            </Form.Text>
          </Form.Group>

          <Row>
            <Col md={6}>
              <Form.Group className="mb-3">
                <Form.Label className="fw-semibold">Start Time</Form.Label>
                <Form.Control
                  type="time"
                  name="start_time"
                  value={formData.start_time}
                  onChange={handleChange}
                  style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                />
                <Form.Text className="text-muted">
                  Between 09:00 and 18:00
                </Form.Text>
              </Form.Group>
            </Col>
            <Col md={6}>
              <Form.Group className="mb-3">
                <Form.Label className="fw-semibold">End Time</Form.Label>
                <Form.Control
                  type="time"
                  name="end_time"
                  value={formData.end_time}
                  onChange={handleChange}
                  style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                />
                <Form.Text className="text-muted">
                  Must be after start time
                </Form.Text>
              </Form.Group>
            </Col>
          </Row>
        </Modal.Body>
        <Modal.Footer>
          <Button type="button" variant="secondary" onClick={onHide} disabled={loading}>
            Cancel
          </Button>
          <Button variant="primary" type="submit" disabled={loading}>
            {loading ? 'Saving...' : slot ? 'Update Slot' : 'Add Slot'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default SlotForm;
