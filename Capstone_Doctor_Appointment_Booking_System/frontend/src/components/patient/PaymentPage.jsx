import React, { useState, useEffect } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import { initiatePayment, confirmPayment, getPaymentStatus } from '../../api/payment';
import { Container, Row, Col, Card, Form, Button, Badge, Alert } from 'react-bootstrap';
import {
  FaCreditCard,
  FaMoneyBillWave,
  FaCheckCircle,
  FaTimesCircle,
  FaSpinner,
  FaArrowLeft,
} from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';
import { PAYMENT_METHODS, PAYMENT_STATUS, PAYMENT_STATUS_LABELS } from '../../utils/constants';

const PaymentPage = () => {
  const { appointmentId } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [confirming, setConfirming] = useState(false);
  const [payment, setPayment] = useState(null);
  const [paymentMethod, setPaymentMethod] = useState('CREDIT_CARD');
  const [cardLastFour, setCardLastFour] = useState('');
  const [upiId, setUpId] = useState('');
  const [paymentStatus, setPaymentStatus] = useState(null);
  const [isComplete, setIsComplete] = useState(false);

  /**
   * Load appointment data from navigation state.
   */
  const appointment = location.state?.appointment;
  const doctor = location.state?.doctor;

  /**
   * Initialize payment on mount.
   */
  useEffect(() => {
    if (!appointmentId) {
      toast.error('No appointment found');
      navigate('/my-appointments');
      return;
    }
    initPayment();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [appointmentId]);

  /**
   * Initialize payment.
   */
  const initPayment = async () => {
    setLoading(true);
    try {
      const data = await initiatePayment({
        appointment_id: appointmentId,
        method: paymentMethod,
      });
      setPayment(data.payment);
      setPaymentStatus(data.payment.status);
    } catch (error) {
      console.error('Error initiating payment:', error);
      toast.error('Failed to initiate payment');
      navigate('/my-appointments');
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle payment confirmation.
   */
  const handleConfirmPayment = async () => {
    if (paymentMethod === 'CREDIT_CARD' || paymentMethod === 'DEBIT_CARD') {
      if (!cardLastFour || cardLastFour.length !== 4) {
        toast.error('Please enter the last 4 digits of your card');
        return;
      }
      if (!/^\d{4}$/.test(cardLastFour)) {
        toast.error('Please enter valid 4-digit card number');
        return;
      }
    }

    if (paymentMethod === 'UPI' && !upiId) {
      toast.error('Please enter your UPI ID');
      return;
    }

    setConfirming(true);
    try {
      const confirmData = {
        payment_id: payment.payment_id,
      };

      if (paymentMethod === 'CREDIT_CARD' || paymentMethod === 'DEBIT_CARD') {
        confirmData.card_last_four = cardLastFour;
      } else if (paymentMethod === 'UPI') {
        confirmData.upi_id = upiId;
      }

      const response = await confirmPayment(confirmData);
      setPayment(response.payment);
      setPaymentStatus(response.payment.status);

      if (response.payment.status === PAYMENT_STATUS.COMPLETED) {
        toast.success('Payment successful!');
        setIsComplete(true);
        setTimeout(() => {
          navigate('/my-appointments');
        }, 3000);
      } else {
        toast.error('Payment failed. Please try again.');
        setIsComplete(true);
        setTimeout(() => {
          navigate('/my-appointments');
        }, 3000);
      }
    } catch (error) {
      console.error('Error confirming payment:', error);
      toast.error('Payment confirmation failed');
    } finally {
      setConfirming(false);
    }
  };

  /**
   * Get payment status display.
   */
  const getStatusDisplay = () => {
    switch (paymentStatus) {
      case PAYMENT_STATUS.COMPLETED:
        return (
          <div className="text-center">
            <div className="mb-3" style={{ fontSize: '64px' }}>
              <FaCheckCircle style={{ color: '#48bb78' }} />
            </div>
            <h4 className="fw-bold" style={{ color: '#48bb78' }}>Payment Successful!</h4>
            <p className="text-muted">Your appointment has been confirmed.</p>
          </div>
        );
      case PAYMENT_STATUS.FAILED:
        return (
          <div className="text-center">
            <div className="mb-3" style={{ fontSize: '64px' }}>
              <FaTimesCircle style={{ color: '#ef4444' }} />
            </div>
            <h4 className="fw-bold" style={{ color: '#ef4444' }}>Payment Failed</h4>
            <p className="text-muted">Please try again or use a different payment method.</p>
          </div>
        );
      default:
        return null;
    }
  };

  if (loading) {
    return <Loading message="Initializing payment..." />;
  }

  if (isComplete) {
    return (
      <Container className="mt-5">
        <Row className="justify-content-center">
          <Col lg={6}>
            <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
              <Card.Body className="p-4 text-center">
                {getStatusDisplay()}
                <p className="text-muted mt-3">Redirecting to appointments...</p>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <Button
        variant="outline-secondary"
        className="mb-4"
        onClick={() => navigate(-1)}
        style={{ borderRadius: '8px' }}
      >
        <FaArrowLeft className="me-2" /> Back
      </Button>

      <Row className="justify-content-center">
        <Col lg={8}>
          <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <h2 className="fw-bold mb-4" style={{ color: '#1a202c' }}>
                Payment Details
              </h2>

              {/* Appointment Summary */}
              <div className="bg-light p-3 rounded-3 mb-4">
                <Row>
                  <Col md={6}>
                    <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                      Appointment ID
                    </p>
                    <p className="fw-semibold">{appointmentId}</p>
                  </Col>
                  <Col md={6}>
                    <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                      Amount
                    </p>
                    <h4 className="fw-bold text-primary">${payment?.amount || '150.00'}</h4>
                  </Col>
                </Row>
                {payment && (
                  <Row className="mt-2">
                    <Col md={6}>
                      <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                        Payment ID
                      </p>
                      <p className="fw-semibold">{payment.payment_id}</p>
                    </Col>
                    <Col md={6}>
                      <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                        Status
                      </p>
                      <Badge
                        bg={payment.status === PAYMENT_STATUS.PENDING ? 'warning' : 'secondary'}
                        className="px-3 py-2"
                      >
                        {PAYMENT_STATUS_LABELS[payment.status] || payment.status}
                      </Badge>
                    </Col>
                  </Row>
                )}
              </div>

              {/* Payment Method Selection */}
              <Form.Group className="mb-4">
                <Form.Label className="fw-semibold">Select Payment Method</Form.Label>
                <div className="d-flex flex-wrap gap-2">
                  {PAYMENT_METHODS.map((method) => (
                    <Button
                      key={method.value}
                      variant={paymentMethod === method.value ? 'primary' : 'outline-primary'}
                      className="px-4 py-2"
                      style={{ borderRadius: '8px' }}
                      onClick={() => {
                        setPaymentMethod(method.value);
                        setCardLastFour('');
                        setUpId('');
                      }}
                    >
                      {method.icon} {method.label}
                    </Button>
                  ))}
                </div>
              </Form.Group>

              {/* Payment Details */}
              {(paymentMethod === 'CREDIT_CARD' || paymentMethod === 'DEBIT_CARD') && (
                <Form.Group className="mb-4">
                  <Form.Label className="fw-semibold">
                    Card Details
                  </Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter last 4 digits of card (e.g., 4242)"
                    value={cardLastFour}
                    onChange={(e) => setCardLastFour(e.target.value)}
                    maxLength={4}
                    style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                  />
                  <Form.Text className="text-muted">
                    For testing, use: 4242 (success) or 4000 (failure)
                  </Form.Text>
                </Form.Group>
              )}

              {paymentMethod === 'UPI' && (
                <Form.Group className="mb-4">
                  <Form.Label className="fw-semibold">
                    UPI ID
                  </Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter your UPI ID (e.g., user@upi)"
                    value={upiId}
                    onChange={(e) => setUpId(e.target.value)}
                    style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                  />
                  <Form.Text className="text-muted">
                    Enter your UPI ID for payment
                  </Form.Text>
                </Form.Group>
              )}

              <Alert variant="info" className="mb-4">
                <strong>Test Mode:</strong>
                <br />
                • Use <strong>4242</strong> for card last 4 digits → Payment Success
                <br />
                • Use <strong>4000</strong> for card last 4 digits → Payment Failure
              </Alert>

              <Button
                variant="primary"
                className="w-100 py-3 fw-semibold"
                style={{ borderRadius: '10px' }}
                onClick={handleConfirmPayment}
                disabled={confirming}
              >
                {confirming ? (
                  <>
                    <FaSpinner className="spinner-border spinner-border-sm me-2" />
                    Processing...
                  </>
                ) : (
                  <>
                    <FaCreditCard className="me-2" /> Pay ${payment?.amount || '150.00'}
                  </>
                )}
              </Button>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default PaymentPage;
