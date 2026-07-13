import React, { useState, useEffect } from 'react';
import { getPatientPayments } from '../../api/payment';
import { Container, Card, Table, Badge, Button } from 'react-bootstrap';
import { FaMoneyBillWave, FaClock, FaCalendarAlt } from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';
import { PAYMENT_STATUS_LABELS, PAYMENT_STATUS_COLORS } from '../../utils/constants';

const PaymentHistory = () => {
  const [loading, setLoading] = useState(true);
  const [payments, setPayments] = useState([]);
  const [total, setTotal] = useState(0);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(false);
  const limit = 10;

  /**
   * Load payment history on mount.
   */
  useEffect(() => {
    loadPayments();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page]);

  /**
   * Load payments from API.
   */
  const loadPayments = async () => {
    setLoading(true);
    try {
      const skip = (page - 1) * limit;
      const data = await getPatientPayments({ limit, skip });

      if (page === 1) {
        setPayments(data.payments || []);
      } else {
        setPayments((prev) => [...prev, ...(data.payments || [])]);
      }
      setTotal(data.total || 0);
      setHasMore(data.total_pages > page);
    } catch (error) {
      console.error('Error loading payments:', error);
      toast.error('Failed to load payment history');
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle load more.
   */
  const handleLoadMore = () => {
    setPage((prev) => prev + 1);
  };

  /**
   * Format date for display.
   */
  const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    return date.toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  /**
   * Get payment status color.
   */
  const getStatusColor = (status) => {
    return PAYMENT_STATUS_COLORS[status] || 'secondary';
  };

  /**
   * Get payment status label.
   */
  const getStatusLabel = (status) => {
    return PAYMENT_STATUS_LABELS[status] || status;
  };

  if (loading && payments.length === 0) {
    return <Loading message="Loading payment history..." />;
  }

  return (
    <Container className="mt-4">
      <h1 className="fw-bold mb-4" style={{ color: '#1a202c' }}>
        Payment History
      </h1>

      <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
        <Card.Body className="p-0">
          {payments.length === 0 ? (
            <div className="text-center py-5">
              <div style={{ fontSize: '48px', marginBottom: '20px' }}></div>
              <h4>No payments found</h4>
              <p className="text-muted">You haven't made any payments yet.</p>
            </div>
          ) : (
            <div className="table-responsive">
              <Table hover className="mb-0">
                <thead style={{ background: '#f8f9fa' }}>
                  <tr>
                    <th style={{ padding: '12px 16px' }}>Payment ID</th>
                    <th style={{ padding: '12px 16px' }}>Appointment</th>
                    <th style={{ padding: '12px 16px' }}>Amount</th>
                    <th style={{ padding: '12px 16px' }}>Method</th>
                    <th style={{ padding: '12px 16px' }}>Status</th>
                    <th style={{ padding: '12px 16px' }}>Date</th>
                  </tr>
                </thead>
                <tbody>
                  {payments.map((payment) => (
                    <tr key={payment.id}>
                      <td style={{ padding: '12px 16px' }}>
                        <span className="fw-semibold">{payment.payment_id}</span>
                      </td>
                      <td style={{ padding: '12px 16px' }}>
                        <div className="d-flex flex-column">
                          <span className="text-muted" style={{ fontSize: '0.85rem' }}>
                            {payment.appointment_id}
                          </span>
                          <span className="text-muted" style={{ fontSize: '0.75rem' }}>
                            Dr. {payment.doctor_id?.slice(0, 8)}...
                          </span>
                        </div>
                      </td>
                      <td style={{ padding: '12px 16px' }}>
                        <span className="fw-bold text-primary">
                          ${payment.amount}
                        </span>
                      </td>
                      <td style={{ padding: '12px 16px' }}>
                        <Badge bg="info" className="px-3 py-2">
                          {payment.method}
                        </Badge>
                      </td>
                      <td style={{ padding: '12px 16px' }}>
                        <Badge
                          bg={getStatusColor(payment.status)}
                          className="px-3 py-2"
                        >
                          {getStatusLabel(payment.status)}
                        </Badge>
                      </td>
                      <td style={{ padding: '12px 16px' }}>
                        <div className="d-flex flex-column">
                          <span style={{ fontSize: '0.85rem' }}>
                            {formatDate(payment.created_at)}
                          </span>
                          <span className="text-muted" style={{ fontSize: '0.75rem' }}>
                            <FaClock className="me-1" />
                            {new Date(payment.created_at).toLocaleDateString()}
                          </span>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            </div>
          )}
        </Card.Body>
      </Card>

      {hasMore && (
        <div className="text-center mt-4">
          <Button
            variant="outline-primary"
            onClick={handleLoadMore}
            disabled={loading}
            style={{ borderRadius: '8px', padding: '0.6rem 2rem' }}
          >
            {loading ? 'Loading...' : 'Load More'}
          </Button>
        </div>
      )}
    </Container>
  );
};

export default PaymentHistory;
