/**
 * Book Appointment page component.
 * Handles appointment booking with reason and notes.
 */

import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { bookAppointment } from '../../api/appointment';
import { Form, Button, Card, Container, Row, Col } from 'react-bootstrap';
import { FaCalendarAlt, FaClock, FaUserMd, FaStethoscope } from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';

const BookAppointment = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    reason: '',
    notes: '',
  });

  // Get data from navigation state
  const { doctor, selectedDate, selectedSlot } = location.state || {};

  /**
   * Validate that we have all required data.
   */
  if (!doctor || !selectedDate || !selectedSlot) {
    return (
      <Container className="mt-5 text-center">
        <h4>Missing appointment information</h4>
        <p className="text-muted">Please go back and select a time slot.</p>
        <Button variant="primary" onClick={() => navigate(-1)}>
          Go Back
        </Button>
      </Container>
    );
  }

  /**
   * Handle form input changes.
   */
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  /**
   * Handle form submission.
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const bookingData = {
        doctor_id: doctor.id,
        appointment_date: selectedDate,
        appointment_time: selectedSlot.start_time,
        reason: formData.reason,
        notes: formData.notes,
      };

      await bookAppointment(bookingData);
      toast.success('Appointment booked successfully!');
      navigate('/my-appointments');
    } catch {
      // Error handled by axios interceptor
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <Loading message="Booking your appointment..." />;
  }

  return (
    <Container className="mt-4">
      <Row className="justify-content-center">
        <Col lg={6} md={8}>
          <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <h2 className="fw-bold mb-4" style={{ color: '#1a202c' }}>Book Appointment</h2>

              {/* Appointment Summary */}
              <div className="bg-light p-3 rounded-3 mb-4" style={{ borderRadius: '10px' }}>
                <Row>
                  <Col md={6}>
                    <p className="text-muted mb-1" style={{ fontSize: '0.85rem' }}>Doctor</p>
                    <p className="fw-semibold mb-0">
                      <FaUserMd className="me-2" style={{ color: '#4a90d9' }} />
                      {doctor.full_name}
                    </p>
                    <p className="text-muted" style={{ fontSize: '0.85rem' }}>
                      {doctor.specialization}
                    </p>
                  </Col>
                  <Col md={6}>
                    <p className="text-muted mb-1" style={{ fontSize: '0.85rem' }}>Date & Time</p>
                    <p className="fw-semibold mb-0">
                      <FaCalendarAlt className="me-2" style={{ color: '#4a90d9' }} />
                      {selectedDate}
                    </p>
                    <p className="fw-semibold mb-0">
                      <FaClock className="me-2" style={{ color: '#4a90d9' }} />
                      {selectedSlot.start_time} - {selectedSlot.end_time}
                    </p>
                  </Col>
                </Row>
                <hr className="my-2" />
                <Row>
                  <Col>
                    <p className="text-muted mb-1" style={{ fontSize: '0.85rem' }}>Consultation Fee</p>
                    <p className="fw-bold text-primary mb-0">${doctor.consultation_fee}</p>
                  </Col>
                </Row>
              </div>

              {/* Booking Form */}
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label className="fw-semibold text-secondary">
                    Reason for Visit <span className="text-danger">*</span>
                  </Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={2}
                    name="reason"
                    placeholder="What is the reason for your visit?"
                    value={formData.reason}
                    onChange={handleChange}
                    required
                    style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                  />
                </Form.Group>

                <Form.Group className="mb-4">
                  <Form.Label className="fw-semibold text-secondary">
                    Additional Notes
                  </Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={2}
                    name="notes"
                    placeholder="Any additional information for the doctor"
                    value={formData.notes}
                    onChange={handleChange}
                    style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                  />
                </Form.Group>

                <div className="d-flex gap-3">
                  <Button
                    variant="outline-secondary"
                    className="flex-grow-1"
                    style={{ borderRadius: '8px' }}
                    onClick={() => navigate(-1)}
                  >
                    Cancel
                  </Button>
                  <Button
                    type="submit"
                    variant="primary"
                    className="flex-grow-1 py-2 fw-semibold"
                    style={{ borderRadius: '8px' }}
                    disabled={loading}
                  >
                    {loading ? 'Booking...' : 'Confirm Booking'}
                  </Button>
                </div>
              </Form>

              <div className="text-center mt-3">
                <p className="text-muted" style={{ fontSize: '0.8rem' }}>
                  <FaStethoscope className="me-1" /> You will receive a confirmation email after booking
                </p>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default BookAppointment;