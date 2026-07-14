import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Card, Row, Col, Badge, Button } from 'react-bootstrap';
import { FaCalendarAlt, FaClock, FaUserMd, FaMoneyBillWave } from 'react-icons/fa';
import { STATUS_LABELS, STATUS_COLORS, PAYMENT_STATUS_LABELS, PAYMENT_STATUS_COLORS } from '../../utils/constants';

const AppointmentCard = ({ appointment, onCancel, onReschedule }) => {
  const navigate = useNavigate();
  const {
    id,
    doctor_name,
    doctor_id,
    appointment_date,
    appointment_time,
    status,
    reason,
    payment_status,
    payment_amount,
    created_at,
  } = appointment;

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
   * Get payment status label.
   */
  const getPaymentStatusLabel = (status) => {
    return PAYMENT_STATUS_LABELS[status] || status;
  };

  /**
   * Check if appointment can be cancelled.
   */
  const canCancel = () => {
    return ['SCHEDULED', 'CONFIRMED'].includes(status);
  };

  const canPay = () => {
    return payment_status !== 'COMPLETED' && status !== 'CANCELLED';
  };
  /**
   * Check if appointment can be rescheduled.
   */
  const canReschedule = () => {
    return ['SCHEDULED', 'CONFIRMED'].includes(status);
  };

  return (
    <Card className="mb-3 shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
      <Card.Body className="p-4">
        <Row>
          <Col md={7}>
            <div className="d-flex align-items-start">
              <div
                className="rounded-circle d-flex align-items-center justify-content-center me-3"
                style={{
                  width: '50px',
                  height: '50px',
                  fontSize: '20px',
                  color: 'white',
                  background: 'linear-gradient(135deg, #4a90d9, #357abd)',
                  flexShrink: 0,
                }}
              >
                <FaUserMd />
              </div>
              <div>
                <h5 className="fw-bold mb-1" style={{ color: '#1a202c' }}>
                  {doctor_name}
                </h5>
                <div className="d-flex flex-wrap gap-2 mb-1">
                  <Badge bg={getStatusColor(status)} className="px-3 py-2">
                    {getStatusLabel(status)}
                  </Badge>
                  <Badge
                    bg={PAYMENT_STATUS_COLORS[payment_status] || 'warning'}
                    className="px-3 py-2"
                  >
                    {getPaymentStatusLabel(payment_status)}
                  </Badge>
                </div>
                <p className="text-muted mb-0" style={{ fontSize: '0.9rem' }}>
                  {reason || 'No reason provided'}
                </p>
              </div>
            </div>
          </Col>

          <Col md={3}>
            <div className="mb-2">
              <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>
                <FaCalendarAlt className="me-1" /> Date
              </p>
              <p className="fw-semibold mb-0">{formatDate(appointment_date)}</p>
            </div>
            <div>
              <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>
                <FaClock className="me-1" /> Time
              </p>
              <p className="fw-semibold mb-0">{appointment_time}</p>
            </div>
          </Col>

          <Col md={2} className="text-end">
            {payment_amount && (
              <div className="mb-2">
                <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>
                  <FaMoneyBillWave className="me-1" /> Fee
                </p>
                <p className="fw-bold text-primary">${payment_amount}</p>
              </div>
            )}

            {canCancel() && (
              <Button
                variant="outline-danger"
                size="sm"
                className="mb-2 w-100"
                style={{ borderRadius: '8px' }}
                onClick={() => onCancel(appointment)}
              >
                Cancel
              </Button>
            )}

            {canReschedule() && (
              <Button
                variant="outline-primary"
                size="sm"
                className="w-100"
                style={{ borderRadius: '8px' }}
                onClick={() => onReschedule(appointment)}
              >
                Reschedule
              </Button>
            )}

            {canPay() && (
              <Button
                variant="success"
                size="sm"
                className="mb-2 w-100"
                style={{ borderRadius: '8px' }}
                onClick={() => navigate(`/payment/${id}`)}
              >
             <FaMoneyBillWave className="me-1" /> Pay Now
             </Button>
              )}
          </Col>
        </Row>
      </Card.Body>
    </Card>
  );
};

export default AppointmentCard;
