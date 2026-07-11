import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getDoctorStats } from '../../api/doctor';
import { getAppointmentStats } from '../../api/appointment';
import { Container, Row, Col, Card } from 'react-bootstrap';
import {
  FaUsers,
  FaCalendarCheck,
  FaCalendarTimes,
  FaStar,
  FaMoneyBillWave,
  FaClock,
  FaUserMd,
} from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';

const DoctorDashboard = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [doctorStats, setDoctorStats] = useState({
    total_patients: 0,
    total_appointments: 0,
    today_appointments: 0,
    upcoming_appointments: 0,
    completed_appointments: 0,
    cancelled_appointments: 0,
    rating: 0,
    total_reviews: 0,
  });
  const [appointmentStats, setAppointmentStats] = useState({
    total: 0,
    scheduled: 0,
    confirmed: 0,
    completed: 0,
    cancelled: 0,
    no_show: 0,
    rescheduled: 0,
    revenue: 0,
  });

  /**
   * Load dashboard data.
   */
  useEffect(() => {
    loadDashboardData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  /**
   * Load doctor stats and appointment stats.
   */
  const loadDashboardData = async () => {
    setLoading(true);
    try {
      const [stats, appointments] = await Promise.all([
        getDoctorStats(),
        getAppointmentStats(),
      ]);

      setDoctorStats(stats);
      setAppointmentStats(appointments);
    } catch (error) {
      console.error('Error loading dashboard data:', error);
      toast.error('Failed to load dashboard data');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <Loading message="Loading dashboard..." />;
  }

  return (
    <Container className="mt-4">
      <div className="mb-4">
        <h1 className="fw-bold" style={{ color: '#1a202c' }}>
          Welcome, Dr. {user?.full_name}!
        </h1>
        <p className="text-muted">Here's an overview of your practice</p>
      </div>

      {/* Stats Cards */}
      <Row className="mb-4">
        <Col lg={3} md={6} className="mb-3">
          <Card className="shadow-sm h-100" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                    Total Patients
                  </p>
                  <h2 className="fw-bold mb-0" style={{ color: '#1a202c' }}>
                    {doctorStats.total_patients || 0}
                  </h2>
                </div>
                <div
                  className="rounded-circle d-flex align-items-center justify-content-center"
                  style={{
                    width: '48px',
                    height: '48px',
                    background: 'rgba(74, 144, 217, 0.1)',
                    color: '#4a90d9',
                    fontSize: '20px',
                  }}
                >
                  <FaUsers />
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={3} md={6} className="mb-3">
          <Card className="shadow-sm h-100" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                    Total Appointments
                  </p>
                  <h2 className="fw-bold mb-0" style={{ color: '#1a202c' }}>
                    {appointmentStats.total || 0}
                  </h2>
                </div>
                <div
                  className="rounded-circle d-flex align-items-center justify-content-center"
                  style={{
                    width: '48px',
                    height: '48px',
                    background: 'rgba(72, 187, 120, 0.1)',
                    color: '#48bb78',
                    fontSize: '20px',
                  }}
                >
                  <FaCalendarCheck />
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={3} md={6} className="mb-3">
          <Card className="shadow-sm h-100" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                    Completed
                  </p>
                  <h2 className="fw-bold mb-0" style={{ color: '#1a202c' }}>
                    {appointmentStats.completed || 0}
                  </h2>
                </div>
                <div
                  className="rounded-circle d-flex align-items-center justify-content-center"
                  style={{
                    width: '48px',
                    height: '48px',
                    background: 'rgba(72, 187, 120, 0.1)',
                    color: '#48bb78',
                    fontSize: '20px',
                  }}
                >
                  <FaCalendarCheck />
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={3} md={6} className="mb-3">
          <Card className="shadow-sm h-100" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                    Revenue
                  </p>
                  <h2 className="fw-bold mb-0" style={{ color: '#4a90d9' }}>
                    ${appointmentStats.revenue || 0}
                  </h2>
                </div>
                <div
                  className="rounded-circle d-flex align-items-center justify-content-center"
                  style={{
                    width: '48px',
                    height: '48px',
                    background: 'rgba(74, 144, 217, 0.1)',
                    color: '#4a90d9',
                    fontSize: '20px',
                  }}
                >
                  <FaMoneyBillWave />
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Appointment Status Breakdown */}
      <Row>
        <Col lg={6} className="mb-3">
          <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <h5 className="fw-bold mb-3">Appointment Status</h5>
              <div className="d-flex flex-wrap gap-3">
                <div className="flex-grow-1">
                  <div className="d-flex justify-content-between mb-1">
                    <span className="text-muted">Scheduled</span>
                    <span className="fw-semibold">{appointmentStats.scheduled || 0}</span>
                  </div>
                  <div className="progress" style={{ height: '6px' }}>
                    <div
                      className="progress-bar bg-warning"
                      style={{
                        width: `${(appointmentStats.scheduled / (appointmentStats.total || 1)) * 100}%`,
                      }}
                    />
                  </div>
                </div>
                <div className="flex-grow-1">
                  <div className="d-flex justify-content-between mb-1">
                    <span className="text-muted">Confirmed</span>
                    <span className="fw-semibold">{appointmentStats.confirmed || 0}</span>
                  </div>
                  <div className="progress" style={{ height: '6px' }}>
                    <div
                      className="progress-bar bg-info"
                      style={{
                        width: `${(appointmentStats.confirmed / (appointmentStats.total || 1)) * 100}%`,
                      }}
                    />
                  </div>
                </div>
                <div className="flex-grow-1">
                  <div className="d-flex justify-content-between mb-1">
                    <span className="text-muted">Completed</span>
                    <span className="fw-semibold">{appointmentStats.completed || 0}</span>
                  </div>
                  <div className="progress" style={{ height: '6px' }}>
                    <div
                      className="progress-bar bg-success"
                      style={{
                        width: `${(appointmentStats.completed / (appointmentStats.total || 1)) * 100}%`,
                      }}
                    />
                  </div>
                </div>
                <div className="flex-grow-1">
                  <div className="d-flex justify-content-between mb-1">
                    <span className="text-muted">Cancelled</span>
                    <span className="fw-semibold">{appointmentStats.cancelled || 0}</span>
                  </div>
                  <div className="progress" style={{ height: '6px' }}>
                    <div
                      className="progress-bar bg-danger"
                      style={{
                        width: `${(appointmentStats.cancelled / (appointmentStats.total || 1)) * 100}%`,
                      }}
                    />
                  </div>
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={6} className="mb-3">
          <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <h5 className="fw-bold mb-3">Quick Actions</h5>
              <div className="d-flex flex-wrap gap-2">
                <a href="/doctor/availability" className="btn btn-primary w-100 mb-2">
                  <FaClock className="me-2" /> Manage Availability
                </a>
                <a href="/doctor/profile" className="btn btn-outline-primary w-100">
                  <FaUserMd className="me-2" /> Update Profile
                </a>
              </div>
              <hr />
              <div className="d-flex align-items-center">
                <div className="me-3">
                  <FaStar className="text-warning" />
                  <FaStar className="text-warning" />
                  <FaStar className="text-warning" />
                  <FaStar className="text-warning" />
                  <FaStar className="text-warning" />
                </div>
                <span className="fw-semibold">{doctorStats.rating || 0}</span>
                <span className="text-muted ms-1">
                  ({doctorStats.total_reviews || 0} reviews)
                </span>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default DoctorDashboard;
