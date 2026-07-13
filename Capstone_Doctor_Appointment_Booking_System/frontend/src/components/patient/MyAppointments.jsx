import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { getPatientAppointments } from '../../api/appointment';
import { Container, Row, Col, Card, Nav, Button, Badge } from 'react-bootstrap';
import { FaCalendarAlt, FaClock, FaUserMd, FaMoneyBillWave } from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';
import AppointmentCard from './AppointmentCard';
import CancelAppointment from './CancelAppointment';
import RescheduleAppointment from './RescheduleAppointment';
import { APPOINTMENT_STATUS, STATUS_LABELS, STATUS_COLORS } from '../../utils/constants';

const MyAppointments = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [appointments, setAppointments] = useState([]);
  const [total, setTotal] = useState(0);
  const [activeTab, setActiveTab] = useState('all');
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [showRescheduleModal, setShowRescheduleModal] = useState(false);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(false);
  const limit = 10;

  /**
   * Load appointments on component mount and tab change.
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
      const params = {
        limit,
        skip,
      };

      if (activeTab !== 'all') {
        params.status = activeTab.toUpperCase();
      }

      const data = await getPatientAppointments(params);
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
   * Handle appointment cancellation.
   */
  const handleCancel = (appointment) => {
    setSelectedAppointment(appointment);
    setShowCancelModal(true);
  };

  /**
   * Handle appointment reschedule.
   */
  const handleReschedule = (appointment) => {
    setSelectedAppointment(appointment);
    setShowRescheduleModal(true);
  };

  /**
   * Handle appointment update after cancel/reschedule.
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

  if (loading && appointments.length === 0) {
    return <Loading message="Loading your appointments..." />;
  }

  return (
    <Container className="mt-4">
      <h1 className="fw-bold mb-4" style={{ color: '#1a202c' }}>My Appointments</h1>

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
          <Button variant="primary" onClick={() => navigate('/search-doctors')}>
            Search Doctors
          </Button>
        </div>
      ) : (
        <>
          {appointments.map((appointment) => (
            <AppointmentCard
              key={appointment.id}
              appointment={appointment}
              onCancel={handleCancel}
              onReschedule={handleReschedule}
            />
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

      {/* Cancel Modal */}
      {selectedAppointment && (
        <CancelAppointment
          show={showCancelModal}
          onHide={() => setShowCancelModal(false)}
          appointment={selectedAppointment}
          onSuccess={(appointmentId) => {
            setShowCancelModal(false);
            handleAppointmentUpdate(appointmentId, { status: 'CANCELLED' });
            toast.success('Appointment cancelled successfully');
          }}
        />
      )}

      {/* Reschedule Modal */}
      {selectedAppointment && (
        <RescheduleAppointment
          show={showRescheduleModal}
          onHide={() => setShowRescheduleModal(false)}
          appointment={selectedAppointment}
          onSuccess={(appointmentId, newData) => {
            setShowRescheduleModal(false);
            handleAppointmentUpdate(appointmentId, newData);
            toast.success('Appointment rescheduled successfully');
          }}
        />
      )}
    </Container>
  );
};

export default MyAppointments;
