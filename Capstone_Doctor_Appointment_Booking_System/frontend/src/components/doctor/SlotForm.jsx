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
          <Button variant="secondary" onClick={onHide} disabled={loading}>
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
