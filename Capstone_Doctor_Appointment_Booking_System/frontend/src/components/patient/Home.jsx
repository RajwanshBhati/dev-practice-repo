import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getPatientAppointments } from '../../api/appointment';
import { Container, Row, Col, Card } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { FaCalendarCheck, FaClipboardList, FaMoneyBillWave, FaUserMd, FaClock } from 'react-icons/fa';
import DashboardLayout from '../admin/DashboardLayout';

const Home = () => {
  const { user, isPatient, isDoctor, isAdmin } = useAuth();
  const [aptLoading, setAptLoading] = useState(true);
  const [appointments, setAppointments] = useState([]);
  const [totalAppointments, setTotalAppointments] = useState(0);

  useEffect(() => {
    if (isPatient) {
      loadPatientData();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isPatient]);

  /**
   * Load the patient's appointments to build the dashboard stats.
   */
  const loadPatientData = async () => {
    setAptLoading(true);
    try {
      const data = await getPatientAppointments({ limit: 100, skip: 0 });
      setAppointments(data.appointments || []);
      setTotalAppointments(data.total || 0);
    } catch (error) {
      console.error('Error loading appointments:', error);
    } finally {
      setAptLoading(false);
    }
  };

  const formatDate = (dateStr) => {
    return new Date(dateStr).toLocaleDateString('en-IN', {
      day: 'numeric',
      month: 'long',
      year: 'numeric',
    });
  };

  const formatTime = (timeStr) => {
    if (!timeStr) return '';
    const [h, m] = timeStr.split(':');
    const hour = parseInt(h, 10);
    const period = hour >= 12 ? 'PM' : 'AM';
    const hour12 = hour % 12 === 0 ? 12 : hour % 12;
    return `${hour12}:${m} ${period}`;
  };

  /**
   * Patient Home View — quick-glance dashboard with real appointment/payment stats.
   */
  if (isPatient) {
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const upcomingAppointments = appointments
      .filter((a) => ['SCHEDULED', 'CONFIRMED'].includes(a.status) && new Date(a.appointment_date) >= today)
      .sort((a, b) => new Date(`${a.appointment_date}T${a.appointment_time || '00:00'}`) - new Date(`${b.appointment_date}T${b.appointment_time || '00:00'}`));

    const nextAppointment = upcomingAppointments[0];

    const pendingAmount = appointments
      .filter((a) => a.payment_status === 'PENDING')
      .reduce((sum, a) => sum + (a.payment_amount || 0), 0);

    const firstName = user?.full_name?.split(' ')[0] || 'there';

    return (
      <DashboardLayout role="PATIENT">
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
            <h1 className="fw-bold mb-1">Welcome Back, {firstName}! </h1>
            <p className="mb-0" style={{ opacity: 0.9 }}>
              Good to see you again. Here's a quick overview of your healthcare activity.
            </p>
          </div>

          {/* Stat Cards */}
          <Row className="mb-4">
            <Col md={4} className="mb-3">
              <Card className="shadow-sm h-100 border-0" style={{ borderRadius: '16px' }}>
                <Card.Body className="p-4 d-flex justify-content-between align-items-center">
                  <div>
                    <p className="text-muted mb-1 text-uppercase" style={{ fontSize: '0.75rem', letterSpacing: '0.05em', fontWeight: 600 }}>
                      Upcoming Appointments
                    </p>
                    <h2 className="fw-bold mb-0" style={{ color: '#1a202c' }}>
                      {aptLoading ? '-' : upcomingAppointments.length}
                    </h2>
                  </div>
                  <div
                    className="rounded-circle d-flex align-items-center justify-content-center"
                    style={{ width: '52px', height: '52px', background: 'rgba(74, 144, 217, 0.12)', color: '#4a90d9', fontSize: '22px' }}
                  >
                    <FaCalendarCheck />
                  </div>
                </Card.Body>
              </Card>
            </Col>

            <Col md={4} className="mb-3">
              <Card className="shadow-sm h-100 border-0" style={{ borderRadius: '16px' }}>
                <Card.Body className="p-4 d-flex justify-content-between align-items-center">
                  <div>
                    <p className="text-muted mb-1 text-uppercase" style={{ fontSize: '0.75rem', letterSpacing: '0.05em', fontWeight: 600 }}>
                      Total Appointments
                    </p>
                    <h2 className="fw-bold mb-0" style={{ color: '#1a202c' }}>
                      {aptLoading ? '-' : totalAppointments}
                    </h2>
                  </div>
                  <div
                    className="rounded-circle d-flex align-items-center justify-content-center"
                    style={{ width: '52px', height: '52px', background: 'rgba(72, 187, 120, 0.12)', color: '#48bb78', fontSize: '22px' }}
                  >
                    <FaClipboardList />
                  </div>
                </Card.Body>
              </Card>
            </Col>

            <Col md={4} className="mb-3">
              <Card className="shadow-sm h-100 border-0" style={{ borderRadius: '16px' }}>
                <Card.Body className="p-4 d-flex justify-content-between align-items-center">
                  <div>
                    <p className="text-muted mb-1 text-uppercase" style={{ fontSize: '0.75rem', letterSpacing: '0.05em', fontWeight: 600 }}>
                      Pending Payments
                    </p>
                    <h2 className="fw-bold mb-0" style={{ color: '#1a202c' }}>
                      {aptLoading ? '-' : `₹${pendingAmount}`}
                    </h2>
                  </div>
                  <div
                    className="rounded-circle d-flex align-items-center justify-content-center"
                    style={{ width: '52px', height: '52px', background: 'rgba(245, 158, 11, 0.12)', color: '#f59e0b', fontSize: '22px' }}
                  >
                    <FaMoneyBillWave />
                  </div>
                </Card.Body>
              </Card>
            </Col>
          </Row>

          <Row>
            {/* Upcoming Appointment */}
            <Col lg={7} className="mb-3">
              <Card className="shadow-sm h-100 border-0" style={{ borderRadius: '16px' }}>
                <Card.Body className="p-4">
                  <h5 className="fw-bold mb-3">Upcoming Appointment</h5>

                  {aptLoading ? (
                    <p className="text-muted mb-0">Loading...</p>
                  ) : nextAppointment ? (
                    <div className="d-flex align-items-start">
                      <div
                        className="rounded-circle d-flex align-items-center justify-content-center me-3 flex-shrink-0"
                        style={{ width: '48px', height: '48px', background: 'rgba(74, 144, 217, 0.12)', color: '#4a90d9', fontSize: '20px' }}
                      >
                        <FaUserMd />
                      </div>
                      <div className="flex-grow-1">
                        <h6 className="fw-bold mb-0" style={{ color: '#1a202c' }}>
                          Dr. {nextAppointment.doctor_name}
                        </h6>
                        <p className="text-muted mb-2" style={{ fontSize: '0.85rem' }}>
                          <FaClock className="me-1" />
                          {formatDate(nextAppointment.appointment_date)} • {formatTime(nextAppointment.appointment_time)}
                        </p>
                        <Link
                          to="/my-appointments"
                          className="btn btn-sm btn-primary"
                          style={{ borderRadius: '8px' }}
                        >
                          View Details
                        </Link>
                      </div>
                    </div>
                  ) : (
                    <div className="text-center py-4">
                      <p className="text-muted mb-3">No upcoming appointments.</p>
                      <Link to="/search-doctors" className="btn btn-primary" style={{ borderRadius: '8px' }}>
                        Book an Appointment
                      </Link>
                    </div>
                  )}
                </Card.Body>
              </Card>
            </Col>

            {/* Health Tips */}
            <Col lg={5} className="mb-3">
              <Card className="shadow-sm h-100 border-0" style={{ borderRadius: '16px' }}>
                <Card.Body className="p-4">
                  <h5 className="fw-bold mb-3">Health Tips</h5>
                  <div className="d-flex flex-column gap-2">
                    <p className="mb-0" style={{ color: '#4a5568' }}>Drink 2-3L of water daily</p>
                    <p className="mb-0" style={{ color: '#4a5568' }}>Exercise for 30 minutes</p>
                    <p className="mb-0" style={{ color: '#4a5568' }}>Sleep 7-8 hours a night</p>
                    <p className="mb-0" style={{ color: '#4a5568' }}>Eat more fruits and vegetables</p>
                  </div>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </DashboardLayout>
    );
  }

  /**
   * Doctor Home View.
   * Shows cards for dashboard, availability, and appointments.
   */
  if (isDoctor) {
    return (
      <Container className="mt-4">
        <h1 className="mb-4">Welcome, Dr. {user?.full_name}!</h1>
        <p className="text-muted mb-4">Manage your practice and patient appointments.</p>

        <Row>
          <Col md={4} className="mb-3">
            <Card className="card-hover shadow-sm">
              <Card.Body className="text-center p-4">
                <div className="display-4 mb-3"></div>
                <Card.Title>Dashboard</Card.Title>
                <Card.Text className="text-muted">
                  View your practice statistics and performance metrics.
                </Card.Text>
                <Link to="/doctor/dashboard" className="btn btn-primary w-100">
                  Go to Dashboard
                </Link>
              </Card.Body>
            </Card>
          </Col>

          <Col md={4} className="mb-3">
            <Card className="card-hover shadow-sm">
              <Card.Body className="text-center p-4">
                <div className="display-4 mb-3"></div>
                <Card.Title>Manage Availability</Card.Title>
                <Card.Text className="text-muted">
                  Set your available time slots for patient appointments.
                </Card.Text>
                <Link to="/doctor/availability" className="btn btn-primary w-100">
                  Manage Slots
                </Link>
              </Card.Body>
            </Card>
          </Col>

          <Col md={4} className="mb-3">
            <Card className="card-hover shadow-sm">
              <Card.Body className="text-center p-4">
                <div className="display-4 mb-3"></div>
                <Card.Title>Appointments</Card.Title>
                <Card.Text className="text-muted">
                  View and manage all your patient appointments.
                </Card.Text>
                <Link to="/doctor/appointments" className="btn btn-primary w-100">
                  View Appointments
                </Link>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    );
  }

  /**
   * Admin Home View.
   * Shows cards for dashboard, manage doctors, and audit logs.
   */
  if (isAdmin) {
    return (
      <Container className="mt-4">
        <h1 className="mb-4">Welcome, {user?.full_name}!</h1>
        <p className="text-muted mb-4">Manage the platform and monitor all activities.</p>

        <Row>
          <Col md={4} className="mb-3">
            <Card className="card-hover shadow-sm">
              <Card.Body className="text-center p-4">
                <div className="display-4 mb-3"></div>
                <Card.Title>Dashboard</Card.Title>
                <Card.Text className="text-muted">
                  View platform statistics and key performance indicators.
                </Card.Text>
                <Link to="/admin/dashboard" className="btn btn-primary w-100">
                  Go to Dashboard
                </Link>
              </Card.Body>
            </Card>
          </Col>

          <Col md={4} className="mb-3">
            <Card className="card-hover shadow-sm">
              <Card.Body className="text-center p-4">
                <div className="display-4 mb-3"></div>
                <Card.Title>Manage Doctors</Card.Title>
                <Card.Text className="text-muted">
                  Approve or reject doctor applications and manage profiles.
                </Card.Text>
                <Link to="/admin/doctors" className="btn btn-primary w-100">
                  Manage Doctors
                </Link>
              </Card.Body>
            </Card>
          </Col>

          <Col md={4} className="mb-3">
            <Card className="card-hover shadow-sm">
              <Card.Body className="text-center p-4">
                <div className="display-4 mb-3"></div>
                <Card.Title>Audit Logs</Card.Title>
                <Card.Text className="text-muted">
                  View all admin actions and system activities.
                </Card.Text>
                <Link to="/admin/audit-logs" className="btn btn-primary w-100">
                  View Logs
                </Link>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    );
  }

  /**
   * Fallback for when user is not logged in or role is unknown.
   */
  return (
    <Container className="mt-5 text-center">
      <h1>Welcome to Doctor Appointment System</h1>
      <p className="text-muted">Please login to access your dashboard.</p>
    </Container>
  );
};

export default Home;
