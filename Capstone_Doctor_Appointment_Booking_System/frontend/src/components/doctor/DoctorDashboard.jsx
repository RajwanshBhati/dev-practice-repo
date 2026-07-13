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
  FaUserMd,
} from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';

const STATS_CACHE_TTL = 60 * 1000;
let statsCache = null;

const DoctorDashboard = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(!statsCache);
  const [doctorStats, setDoctorStats] = useState(
    statsCache?.doctorStats || {
      total_patients: 0,
      total_appointments: 0,
      today_appointments: 0,
      upcoming_appointments: 0,
      completed_appointments: 0,
      cancelled_appointments: 0,
      rating: 0,
      total_reviews: 0,
    }
  );
  const [appointmentStats, setAppointmentStats] = useState(
    statsCache?.appointmentStats || {
      total: 0,
      scheduled: 0,
      confirmed: 0,
      completed: 0,
      cancelled: 0,
      no_show: 0,
      rescheduled: 0,
      revenue: 0,
    }
  );

  /**
   * Load dashboard data.
   */
  useEffect(() => {
    const isCacheFresh =
      statsCache && Date.now() - statsCache.timestamp < STATS_CACHE_TTL;

    if (isCacheFresh) {
      setLoading(false);
      return;
    }

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
        getAppointmentStats(user?.id),
      ]);

      setDoctorStats(stats);
      setAppointmentStats(appointments);

      statsCache = {
        doctorStats: stats,
        appointmentStats: appointments,
        timestamp: Date.now(),
      };
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
    <Container className="mt-4 mb-5">
      {/* Hero header */}
      <div
        className="mb-4 p-4 p-md-5 text-white"
        style={{
          borderRadius: '20px',
          background: 'linear-gradient(135deg, #4a90d9, #2d5faa)',
          boxShadow: '0 10px 30px rgba(74, 144, 217, 0.25)',
        }}
      >
        <div className="d-flex align-items-center justify-content-between flex-wrap gap-3">
          <div>
            <h1 className="fw-bold mb-1">Welcome, Dr. {user?.full_name?.split(' ')[0] || ''} </h1>
            <p className="mb-0" style={{ opacity: 0.9 }}>
              Here's an overview of your practice today.
            </p>
          </div>
          <div
            className="rounded-circle d-flex align-items-center justify-content-center"
            style={{ width: '64px', height: '64px', background: 'rgba(255,255,255,0.15)', fontSize: '28px' }}
          >
            <FaUserMd />
          </div>
        </div>
      </div>

      {/* Stats Cards */}
      <Row className="mb-4">
        <Col lg={3} md={6} className="mb-3">
          <Card className="shadow-sm h-100 border-0" style={{ borderRadius: '16px' }}>
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <p className="text-muted mb-1 text-uppercase" style={{ fontSize: '0.75rem', letterSpacing: '0.05em', fontWeight: 600 }}>
                    Total Patients
                  </p>
                  <h2 className="fw-bold mb-0" style={{ color: '#1a202c', fontSize: '2rem' }}>
                    {doctorStats.total_patients || 0}
                  </h2>
                </div>
                <div
                  className="rounded-circle d-flex align-items-center justify-content-center"
                  style={{ width: '52px', height: '52px', background: 'rgba(74, 144, 217, 0.12)', color: '#4a90d9', fontSize: '22px' }}
                >
                  <FaUsers />
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={3} md={6} className="mb-3">
          <Card className="shadow-sm h-100 border-0" style={{ borderRadius: '16px' }}>
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <p className="text-muted mb-1 text-uppercase" style={{ fontSize: '0.75rem', letterSpacing: '0.05em', fontWeight: 600 }}>
                    Total Appointments
                  </p>
                  <h2 className="fw-bold mb-0" style={{ color: '#1a202c', fontSize: '2rem' }}>
                    {appointmentStats.total || 0}
                  </h2>
                </div>
                <div
                  className="rounded-circle d-flex align-items-center justify-content-center"
                  style={{ width: '52px', height: '52px', background: 'rgba(72, 187, 120, 0.12)', color: '#48bb78', fontSize: '22px' }}
                >
                  <FaCalendarCheck />
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={3} md={6} className="mb-3">
          <Card className="shadow-sm h-100 border-0" style={{ borderRadius: '16px' }}>
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <p className="text-muted mb-1 text-uppercase" style={{ fontSize: '0.75rem', letterSpacing: '0.05em', fontWeight: 600 }}>
                    Completed
                  </p>
                  <h2 className="fw-bold mb-0" style={{ color: '#1a202c', fontSize: '2rem' }}>
                    {appointmentStats.completed || 0}
                  </h2>
                </div>
                <div
                  className="rounded-circle d-flex align-items-center justify-content-center"
                  style={{ width: '52px', height: '52px', background: 'rgba(72, 187, 120, 0.12)', color: '#48bb78', fontSize: '22px' }}
                >
                  <FaCalendarCheck />
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={3} md={6} className="mb-3">
          <Card className="shadow-sm h-100 border-0" style={{ borderRadius: '16px' }}>
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <p className="text-muted mb-1 text-uppercase" style={{ fontSize: '0.75rem', letterSpacing: '0.05em', fontWeight: 600 }}>
                    Revenue
                  </p>
                  <h2 className="fw-bold mb-0" style={{ color: '#4a90d9', fontSize: '2rem' }}>
                    ${appointmentStats.revenue || 0}
                  </h2>
                </div>
                <div
                  className="rounded-circle d-flex align-items-center justify-content-center"
                  style={{ width: '52px', height: '52px', background: 'rgba(74, 144, 217, 0.12)', color: '#4a90d9', fontSize: '22px' }}
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
        <Col lg={7} className="mb-3">
          <Card className="shadow-sm h-100 border-0" style={{ borderRadius: '16px' }}>
            <Card.Body className="p-4">
              <h5 className="fw-bold mb-3">Appointment Status</h5>
              <div className="d-flex flex-column gap-3">
                <div>
                  <div className="d-flex justify-content-between mb-1">
                    <span className="text-muted">Scheduled</span>
                    <span className="fw-semibold">{appointmentStats.scheduled || 0}</span>
                  </div>
                  <div className="progress" style={{ height: '8px', borderRadius: '6px' }}>
                    <div
                      className="progress-bar bg-warning"
                      style={{
                        width: `${(appointmentStats.scheduled / (appointmentStats.total || 1)) * 100}%`,
                        borderRadius: '6px',
                      }}
                    />
                  </div>
                </div>
                <div>
                  <div className="d-flex justify-content-between mb-1">
                    <span className="text-muted">Confirmed</span>
                    <span className="fw-semibold">{appointmentStats.confirmed || 0}</span>
                  </div>
                  <div className="progress" style={{ height: '8px', borderRadius: '6px' }}>
                    <div
                      className="progress-bar bg-info"
                      style={{
                        width: `${(appointmentStats.confirmed / (appointmentStats.total || 1)) * 100}%`,
                        borderRadius: '6px',
                      }}
                    />
                  </div>
                </div>
                <div>
                  <div className="d-flex justify-content-between mb-1">
                    <span className="text-muted">Completed</span>
                    <span className="fw-semibold">{appointmentStats.completed || 0}</span>
                  </div>
                  <div className="progress" style={{ height: '8px', borderRadius: '6px' }}>
                    <div
                      className="progress-bar bg-success"
                      style={{
                        width: `${(appointmentStats.completed / (appointmentStats.total || 1)) * 100}%`,
                        borderRadius: '6px',
                      }}
                    />
                  </div>
                </div>
                <div>
                  <div className="d-flex justify-content-between mb-1">
                    <span className="text-muted">Cancelled</span>
                    <span className="fw-semibold">{appointmentStats.cancelled || 0}</span>
                  </div>
                  <div className="progress" style={{ height: '8px', borderRadius: '6px' }}>
                    <div
                      className="progress-bar bg-danger"
                      style={{
                        width: `${(appointmentStats.cancelled / (appointmentStats.total || 1)) * 100}%`,
                        borderRadius: '6px',
                      }}
                    />
                  </div>
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>

        {/* Rating & Today's Snapshot */}
        <Col lg={5} className="mb-3">
          <Card
            className="shadow-sm h-100 border-0 text-white"
            style={{
              borderRadius: '16px',
              background: 'linear-gradient(135deg, #48bb78, #38a169)',
            }}
          >
            <Card.Body className="p-4 d-flex flex-column justify-content-between h-100">
              <div>
                <div
                  className="rounded-circle d-flex align-items-center justify-content-center mb-3"
                  style={{ width: '52px', height: '52px', background: 'rgba(255,255,255,0.2)', fontSize: '22px' }}
                >
                  <FaStar />
                </div>
                <h5 className="fw-bold mb-1">
                  {doctorStats.rating || 0} / 5 Rating
                </h5>
                <p className="mb-0" style={{ opacity: 0.9, fontSize: '0.9rem' }}>
                  Based on {doctorStats.total_reviews || 0} patient review{doctorStats.total_reviews === 1 ? '' : 's'}.
                </p>
              </div>
              <Row className="text-center mt-4">
                <Col xs={6}>
                  <h4 className="fw-bold mb-0">{doctorStats.today_appointments || 0}</h4>
                  <p className="mb-0" style={{ fontSize: '0.8rem', opacity: 0.9 }}>Today</p>
                </Col>
                <Col xs={6}>
                  <h4 className="fw-bold mb-0">{doctorStats.upcoming_appointments || 0}</h4>
                  <p className="mb-0" style={{ fontSize: '0.8rem', opacity: 0.9 }}>Upcoming</p>
                </Col>
              </Row>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default DoctorDashboard;
