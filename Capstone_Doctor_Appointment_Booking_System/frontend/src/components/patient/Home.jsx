import { useAuth } from '../../context/AuthContext';
import { Container, Row, Col, Card } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const Home = () => {
  const { user, isPatient, isDoctor, isAdmin } = useAuth();

  /**
   * Patient Home View.
   * Shows cards for search doctors, my appointments, and profile.
   */
  if (isPatient) {
    return (
      <Container className="mt-4">
        <h1 className="mb-4">Welcome, {user?.full_name}!</h1>
        <p className="text-muted mb-4">Find and book appointments with the best doctors.</p>

        <Row>
          <Col md={4} className="mb-3">
            <Card className="card-hover shadow-sm">
              <Card.Body className="text-center p-4">
                <div className="display-4 mb-3">🔍</div>
                <Card.Title>Search Doctors</Card.Title>
                <Card.Text className="text-muted">
                  Find the best doctors near you based on specialization and location.
                </Card.Text>
                <Link to="/search-doctors" className="btn btn-primary w-100">
                  Search Now
                </Link>
              </Card.Body>
            </Card>
          </Col>

          <Col md={4} className="mb-3">
            <Card className="card-hover shadow-sm">
              <Card.Body className="text-center p-4">
                <div className="display-4 mb-3"></div>
                <Card.Title>My Appointments</Card.Title>
                <Card.Text className="text-muted">
                  View and manage all your upcoming and past appointments.
                </Card.Text>
                <Link to="/my-appointments" className="btn btn-primary w-100">
                  View Appointments
                </Link>
              </Card.Body>
            </Card>
          </Col>

          <Col md={4} className="mb-3">
            <Card className="card-hover shadow-sm">
              <Card.Body className="text-center p-4">
                <div className="display-4 mb-3"></div>
                <Card.Title>My Profile</Card.Title>
                <Card.Text className="text-muted">
                  Update your personal information and manage your account.
                </Card.Text>
                <Link to="/profile" className="btn btn-primary w-100">
                  Update Profile
                </Link>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
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