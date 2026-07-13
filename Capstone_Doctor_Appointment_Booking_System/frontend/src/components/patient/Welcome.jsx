import React from 'react';
import { useAuth } from '../../context/AuthContext';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import {
  FaSearch,
  FaCalendarCheck,
  FaClock,
  FaUserMd,
  FaStethoscope,
  FaArrowRight,
  FaClipboardList
} from 'react-icons/fa';

const Welcome = () => {
  const { user, isAuthenticated, isPatient, isDoctor, isAdmin } = useAuth();

  /**
   * Hero Section Component
   */
  const HeroSection = () => (
    <section className="hero-section py-5">
      <Container>
        <Row className="align-items-center">
          <Col lg={6} className="mb-5 mb-lg-0">
            <h1 className="hero-title fw-bold mb-3" style={{ fontSize: '3rem', color: '#1a202c' }}>
              Health At Your Fingertips <br />
              <span style={{ color: '#4a90d9' }}>Your Wellness Matters</span>
            </h1>
            <p className="hero-subtitle text-muted mb-4" style={{ fontSize: '1.2rem' }}>
              Connect with experienced healthcare professionals, schedule appointments in minutes, and manage your healthcare journey with confidence.
            </p>
            <div className="d-flex flex-wrap gap-3">
              {!isAuthenticated ? (
                <>
                  <Button as={Link} to="/register/patient" variant="primary" size="lg" className="px-4">
                    Get Started <FaArrowRight className="ms-2" />
                  </Button>
                  <Button as={Link} to="/login" variant="outline-primary" size="lg" className="px-4">
                    Sign In
                  </Button>
                </>
              ) : (
                <Button as={Link} to={isPatient ? "/search-doctors" : isDoctor ? "/doctor/dashboard" : "/admin/dashboard"} variant="primary" size="lg" className="px-4">
                  Go to Dashboard <FaArrowRight className="ms-2" />
                </Button>
              )}
            </div>
          </Col>
          <Col lg={6} className="text-center">
            <div className="hero-image-wrapper">
              <div className="hero-image-placeholder" style={{
                background: 'linear-gradient(135deg, #4a90d9, #357abd)',
                borderRadius: '50%',
                width: '400px',
                height: '400px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                margin: '0 auto',
                boxShadow: '0 20px 60px rgba(74, 144, 217, 0.3)',
              }}>
                <FaUserMd style={{ fontSize: '120px', color: 'white' }} />
              </div>
            </div>
          </Col>
        </Row>
      </Container>
    </section>
  );

  /**
   * How It Works Section
   */
  const HowItWorksSection = () => (
    <section className="how-it-works py-5" style={{ background: '#f8f9fa' }}>
      <Container>
        <h2 className="text-center fw-bold mb-5" style={{ color: '#1a202c' }}>
          How It Works
        </h2>
        <Row>
          <Col md={4} className="mb-4">
            <Card className="h-100 text-center shadow-sm" style={{ borderRadius: '16px', border: 'none' }}>
              <Card.Body className="p-4">
                <div className="step-icon mb-3" style={{
                  width: '70px',
                  height: '70px',
                  borderRadius: '50%',
                  background: 'linear-gradient(135deg, #4a90d9, #357abd)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  margin: '0 auto',
                }}>
                  <FaSearch style={{ fontSize: '30px', color: 'white' }} />
                </div>
                <Card.Title className="fw-bold">Search Doctors</Card.Title>
                <Card.Text className="text-muted">
                  Browse doctors by specialization and find the right
                  expert for your needs.
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>

          <Col md={4} className="mb-4">
            <Card className="h-100 text-center shadow-sm" style={{ borderRadius: '16px', border: 'none' }}>
              <Card.Body className="p-4">
                <div className="step-icon mb-3" style={{
                  width: '70px',
                  height: '70px',
                  borderRadius: '50%',
                  background: 'linear-gradient(135deg, #48bb78, #38a169)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  margin: '0 auto',
                }}>
                  <FaCalendarCheck style={{ fontSize: '30px', color: 'white' }} />
                </div>
                <Card.Title className="fw-bold">Book Appointments</Card.Title>
                <Card.Text className="text-muted">
                  Pick an available time slot and confirm your
                  appointment with a quick, secure booking.
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>

          <Col md={4} className="mb-4">
            <Card className="h-100 text-center shadow-sm" style={{ borderRadius: '16px', border: 'none' }}>
              <Card.Body className="p-4">
                <div className="step-icon mb-3" style={{
                  width: '70px',
                  height: '70px',
                  borderRadius: '50%',
                  background: 'linear-gradient(135deg, #f59e0b, #d97706)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  margin: '0 auto',
                }}>
                  <FaClock style={{ fontSize: '30px', color: 'white' }} />
                </div>
                <Card.Title className="fw-bold">Manage Visits</Card.Title>
                <Card.Text className="text-muted">
                  Track your upcoming, completed, and cancelled
                  appointments all in one place.
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </section>
  );

  /**
   * Statistics Section
   */
  const StatsSection = () => (
    <section className="stats-section py-5">
      <Container>
        <Row>
          <Col md={3} sm={6} className="text-center mb-4">
            <h2 className="fw-bold" style={{ color: '#4a90d9', fontSize: '2.5rem' }}>500+</h2>
            <p className="text-muted">Trusted Doctors</p>
          </Col>
          <Col md={3} sm={6} className="text-center mb-4">
            <h2 className="fw-bold" style={{ color: '#4a90d9', fontSize: '2.5rem' }}>10K+</h2>
            <p className="text-muted">Happy Patients</p>
          </Col>
          <Col md={3} sm={6} className="text-center mb-4">
            <h2 className="fw-bold" style={{ color: '#4a90d9', fontSize: '2.5rem' }}>30+</h2>
            <p className="text-muted">Specializations</p>
          </Col>
          <Col md={3} sm={6} className="text-center mb-4">
            <h2 className="fw-bold" style={{ color: '#4a90d9', fontSize: '2.5rem' }}>98%</h2>
            <p className="text-muted">Satisfaction Rate</p>
          </Col>
        </Row>
      </Container>
    </section>
  );

  /**
   * Role-specific Dashboard
   */
  const DashboardSection = () => {
    if (!isAuthenticated) return null;

    return (
      <section className="dashboard-section py-5" style={{ background: '#f8f9fa' }}>
        <Container>
          <h2 className="text-center fw-bold mb-4" style={{ color: '#1a202c' }}>
            Welcome to Your Dashboard
          </h2>
          <Row className="justify-content-center">
            {isPatient && (
              <>
                <Col md={4} className="mb-3">
                  <Card className="card-hover shadow-sm text-center" style={{ borderRadius: '16px', border: 'none' }}>
                    <Card.Body className="p-4">
                      <div className="mb-3" style={{ fontSize: '40px' }}><FaSearch /></div>
                      <Card.Title className="fw-bold">Find a Doctor</Card.Title>
                      <Card.Text className="text-muted">
                        Search and book appointments with top doctors.
                      </Card.Text>
                      <Button as={Link} to="/search-doctors" variant="primary">
                        Search Now
                      </Button>
                    </Card.Body>
                  </Card>
                </Col>
                <Col md={4} className="mb-3">
                  <Card className="card-hover shadow-sm text-center" style={{ borderRadius: '16px', border: 'none' }}>
                    <Card.Body className="p-4">
                      <div className="mb-3" style={{ fontSize: '40px' }}></div>
                      <Card.Title className="fw-bold">My Appointments</Card.Title>
                      <Card.Text className="text-muted">
                        View and manage all your appointments.
                      </Card.Text>
                      <Button as={Link} to="/my-appointments" variant="primary">
                        View Appointments
                      </Button>
                    </Card.Body>
                  </Card>
                </Col>
              </>
            )}
          </Row>
        </Container>
      </section>
    );
  };

  /**
   * Footer / CTA Section
   */
  const CTASection = () => (
    <section className="cta-section py-5" style={{ background: 'linear-gradient(135deg, #4a90d9, #357abd)' }}>
      <Container className="text-center">
        <h2 className="fw-bold text-white mb-3">Ready to Get Started?</h2>
        <p className="text-white-50 mb-4" style={{ fontSize: '1.2rem' }}>
          Join thousands of patients who trust HealthBook for their healthcare needs.
        </p>
        {!isAuthenticated ? (
          <Button as={Link} to="/register/patient" variant="light" size="lg" className="px-5">
            Create Account <FaArrowRight className="ms-2" />
          </Button>
        ) : (
          <Button as={Link} to={isPatient ? "/search-doctors" : "/doctor/dashboard"} variant="light" size="lg" className="px-5">
            Go to Dashboard <FaArrowRight className="ms-2" />
          </Button>
        )}
      </Container>
    </section>
  );

  /**
   * Main Render
   */
  return (
    <div className="home-page">
      {/* Hero Section */}
      <HeroSection />

      {/* How It Works Section */}
      <HowItWorksSection />

      {/* Statistics Section */}
      <StatsSection />

      {/* Dashboard Section */}
      <DashboardSection />

      {/* CTA Section */}
      <CTASection />
    </div>
  );
};

export default Welcome;
