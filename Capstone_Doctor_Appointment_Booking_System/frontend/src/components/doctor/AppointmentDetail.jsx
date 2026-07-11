import React from 'react';
import { Modal, Button, Row, Col, Badge } from 'react-bootstrap';
import { FaUserMd, FaCalendarAlt, FaClock, FaMoneyBillWave, FaStethoscope } from 'react-icons/fa';
import { STATUS_LABELS, STATUS_COLORS } from '../../utils/constants';

const AppointmentDetail = ({ show, onHide, appointment }) => {
  if (!appointment) {
    return null;
  }

  /**
   * Get status color.
   */
  const getStatusColor = (status) => {
    return STATUS_COLORS[status] || 'secondary';
  };

  /**
   * Get status label.
   */
  const getStatusLabel = (status) => {
    return STATUS_LABELS[status] || status;
  };

  /**
   * Format date for display.
   */
  const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', {
      weekday: 'short',
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  return (
    <Modal show={show} onHide={onHide} centered size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Appointment Details</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Row>
          <Col md={6}>
            <div className="mb-3">
              <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>
                <FaUserMd className="me-1" /> Patient
              </p>
              <p className="fw-semibold mb-0">{appointment.patient_name}</p>
            </div>
          </Col>
          <Col md={6}>
            <div className="mb-3">
              <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>Status</p>
              <Badge bg={getStatusColor(appointment.status)} className="px-3 py-2">
                {getStatusLabel(appointment.status)}
              </Badge>
            </div>
          </Col>
        </Row>

        <Row>
          <Col md={6}>
            <div className="mb-3">
              <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>
                <FaCalendarAlt className="me-1" /> Date
              </p>
              <p className="fw-semibold mb-0">{formatDate(appointment.appointment_date)}</p>
            </div>
          </Col>
          <Col md={6}>
            <div className="mb-3">
              <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>
                <FaClock className="me-1" /> Time
              </p>
              <p className="fw-semibold mb-0">{appointment.appointment_time}</p>
            </div>
          </Col>
        </Row>

        <Row>
          <Col md={6}>
            <div className="mb-3">
              <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>
                <FaStethoscope className="me-1" /> Reason
              </p>
              <p className="mb-0">{appointment.reason || 'Not specified'}</p>
            </div>
          </Col>
          <Col md={6}>
            <div className="mb-3">
              <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>
                <FaMoneyBillWave className="me-1" /> Payment
              </p>
              <p className="fw-semibold mb-0">
                {appointment.payment_status === 'COMPLETED' ? (
                  <span className="text-success">Paid</span>
                ) : (
                  <span className="text-warning">Pending</span>
                )}
                {appointment.payment_amount && (
                  <span className="text-muted ms-2">(${appointment.payment_amount})</span>
                )}
              </p>
            </div>
          </Col>
        </Row>

        {appointment.notes && (
          <div className="mb-3">
            <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>Notes</p>
            <p className="mb-0">{appointment.notes}</p>
          </div>
        )}

        <div className="mt-3">
          <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>Booked On</p>
          <p className="mb-0" style={{ fontSize: '0.9rem' }}>
            {new Date(appointment.created_at).toLocaleString()}
          </p>
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default AppointmentDetail;
