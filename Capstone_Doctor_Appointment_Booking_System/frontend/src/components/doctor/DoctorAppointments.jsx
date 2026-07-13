import React, { useState, useEffect } from 'react';
import { getDoctorAppointments } from '../../api/appointment';
import { Container, Card, Row, Col, Nav, Button, Badge } from 'react-bootstrap';
import { FaEye, FaEdit, FaCalendarAlt, FaClock, FaUserMd } from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';
import AppointmentStatusUpdate from './AppointmentStatusUpdate';
import AppointmentDetail from './AppointmentDetail';
import { STATUS_LABELS, STATUS_COLORS } from '../../utils/constants';

const DoctorAppointments = () => {
  const [loading, setLoading] = useState(true);
  const [appointments, setAppointments] = useState([]);
  const [total, setTotal] = useState(0);
  const [activeTab, setActiveTab] = useState('all');
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [showStatusModal, setShowStatusModal] = useState(false);
  const [showDetailModal, setShowDetailModal] = useState(false);
  const limit = 10;

  /**
   * Load appointments on mount and tab change.
   */
  useEffect(() => {
    loadAppointments();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activeTab, page]);

  /**
   * Load appointments from API.
   */
  const loadAppointments = async () => {
    setLoading(true);
    try {
      const skip = (page - 1) * limit;
      const params = { limit, skip };

      if (activeTab !== 'all') {
        params.status = activeTab.toUpperCase();
      }

      const data = await getDoctorAppointments(params);
      if (page === 1) {
        setAppointments(data.appointments || []);
      } else {
        setAppointments((prev) => [...prev, ...(data.appointments || [])]);
      }
      setTotal(data.total || 0);
      setHasMore(data.total_pages > page);
    } catch (error) {
      console.error('Error loading appointments:', error);
      toast.error('Failed to load appointments');
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle tab change.
   */
  const handleTabChange = (tab) => {
    setActiveTab(tab);
    setPage(1);
  };

  /**
   * Handle load more.
   */
  const handleLoadMore = () => {
    setPage((prev) => prev + 1);
  };

  /**
   * Handle status update.
   */
  const handleStatusUpdate = (appointment) => {
    setSelectedAppointment(appointment);
    setShowStatusModal(true);
  };

  /**
   * Handle view details.
   */
  const handleViewDetails = (appointment) => {
    setSelectedAppointment(appointment);
    setShowDetailModal(true);
  };

  /**
   * Handle appointment update after status change.
   */
  const handleAppointmentUpdate = (appointmentId, newData) => {
    setAppointments((prev) =>
      prev.map((appt) =>
        appt.id === appointmentId ? { ...appt, ...newData } : appt
      )
    );
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
   * Format date for display.
   */
  const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    });
  };

  if (loading && appointments.length === 0) {
    return <Loading message="Loading appointments..." />;
  }

  return (
    <Container className="mt-4">
      <h1 className="fw-bold mb-4" style={{ color: '#1a202c' }}>
        Patient Appointments
      </h1>

      {/* Status Tabs */}
      <Card className="shadow-sm mb-4" style={{ borderRadius: '12px', border: 'none' }}>
        <Card.Body className="p-3">
          <Nav variant="pills" className="flex-wrap gap-2">
            <Nav.Item>
              <Nav.Link
                active={activeTab === 'all'}
                onClick={() => handleTabChange('all')}
                className="rounded-pill px-4"
              >
                All
              </Nav.Link>
            </Nav.Item>
            <Nav.Item>
              <Nav.Link
                active={activeTab === 'scheduled'}
                onClick={() => handleTabChange('scheduled')}
                className="rounded-pill px-4"
              >
                Scheduled
              </Nav.Link>
            </Nav.Item>
            <Nav.Item>
              <Nav.Link
                active={activeTab === 'confirmed'}
                onClick={() => handleTabChange('confirmed')}
                className="rounded-pill px-4"
              >
                Confirmed
              </Nav.Link>
            </Nav.Item>
            <Nav.Item>
              <Nav.Link
                active={activeTab === 'completed'}
                onClick={() => handleTabChange('completed')}
                className="rounded-pill px-4"
              >
                Completed
              </Nav.Link>
            </Nav.Item>
            <Nav.Item>
              <Nav.Link
                active={activeTab === 'cancelled'}
                onClick={() => handleTabChange('cancelled')}
                className="rounded-pill px-4"
              >
                Cancelled
              </Nav.Link>
            </Nav.Item>
            {/* <Nav.Item>
              <Nav.Link
                active={activeTab === 'no_show'}
                onClick={() => handleTabChange('no_show')}
                className="rounded-pill px-4"
              >
                No Show
              </Nav.Link>
            </Nav.Item> */}
          </Nav>
        </Card.Body>
      </Card>

      {/* Results Count */}
      <div className="mb-3">
        <p className="text-muted">
          Found <strong>{total}</strong> {total === 1 ? 'appointment' : 'appointments'}
        </p>
      </div>

      {/* Appointment List */}
      {appointments.length === 0 ? (
        <div className="text-center py-5">
          <div style={{ fontSize: '48px', marginBottom: '20px' }}>📋</div>
          <h4>No appointments found</h4>
          <p className="text-muted">
            You don't have any {activeTab !== 'all' ? activeTab : ''} appointments.
          </p>
        </div>
      ) : (
        <>
          {appointments.map((appointment) => (
            <Card
              key={appointment.id}
              className="mb-3 shadow-sm"
              style={{ borderRadius: '12px', border: 'none' }}
            >
              <Card.Body className="p-4">
                <Row>
                  <Col md={5}>
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
                          {appointment.patient_name}
                        </h5>
                        <Badge
                          bg={getStatusColor(appointment.status)}
                          className="px-3 py-2"
                        >
                          {getStatusLabel(appointment.status)}
                        </Badge>
                        <p className="text-muted mb-0 mt-1" style={{ fontSize: '0.85rem' }}>
                          {appointment.reason || 'No reason provided'}
                        </p>
                      </div>
                    </div>
                  </Col>

                  <Col md={3}>
                    <div className="mb-1">
                      <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>
                        <FaCalendarAlt className="me-1" /> Date
                      </p>
                      <p className="fw-semibold mb-0">{formatDate(appointment.appointment_date)}</p>
                    </div>
                    <div>
                      <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>
                        <FaClock className="me-1" /> Time
                      </p>
                      <p className="fw-semibold mb-0">{appointment.appointment_time}</p>
                    </div>
                  </Col>

                  <Col md={4} className="text-end">
                    <div className="d-flex flex-wrap gap-2 justify-content-end">
                      <Button
                        variant="outline-secondary"
                        size="sm"
                        onClick={() => handleViewDetails(appointment)}
                        style={{ borderRadius: '8px' }}
                      >
                        <FaEye className="me-1" /> View
                      </Button>
                      <Button
                        variant="outline-primary"
                        size="sm"
                        onClick={() => handleStatusUpdate(appointment)}
                        style={{ borderRadius: '8px' }}
                        disabled={appointment.status === 'COMPLETED' || appointment.status === 'CANCELLED'}
                      >
                        <FaEdit className="me-1" /> Update Status
                      </Button>
                    </div>
                  </Col>
                </Row>
              </Card.Body>
            </Card>
          ))}

          {/* Load More */}
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
        </>
      )}

      {/* Status Update Modal */}
      {selectedAppointment && (
        <AppointmentStatusUpdate
          show={showStatusModal}
          onHide={() => setShowStatusModal(false)}
          appointment={selectedAppointment}
          onSuccess={(appointmentId, newData) => {
            setShowStatusModal(false);
            handleAppointmentUpdate(appointmentId, newData);
          }}
        />
      )}

      {/* Detail Modal */}
      {selectedAppointment && (
        <AppointmentDetail
          show={showDetailModal}
          onHide={() => setShowDetailModal(false)}
          appointment={selectedAppointment}
        />
      )}
    </Container>
  );
};

export default DoctorAppointments;
