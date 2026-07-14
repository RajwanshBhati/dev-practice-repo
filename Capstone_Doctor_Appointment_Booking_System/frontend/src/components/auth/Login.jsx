import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Form, Button, Card, Container, Row, Col, Spinner } from 'react-bootstrap';
import { FaEnvelope, FaLock, FaUserMd, FaUser, FaStethoscope } from 'react-icons/fa';
import { BiShow, BiHide } from 'react-icons/bi';
import { isValidEmail } from '../../utils/validators';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const { login, isAuthenticated, user } = useAuth();
  const navigate = useNavigate();

  const dashboardPathFor = (role) => {
    if (role === 'DOCTOR') return '/doctor/dashboard';
    if (role === 'ADMIN') return '/admin/dashboard';
    return '/home';
  };

  /**
   * If the user is already logged in (e.g. they hit the browser Back
   * button after logging in, or opened /login directly in another tab),
   * send them straight to their dashboard instead of showing the login
   * form again.
   */
  useEffect(() => {
    if (isAuthenticated && user?.role) {
      navigate(dashboardPathFor(user.role), { replace: true });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isAuthenticated]);

  const handleEmailChange = (e) => {
    setEmail(e.target.value);
    if (error) setError('');
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
    if (error) setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!isValidEmail(email)) {
      setError('Please enter a valid email address');
      return;
    }
    if (!password || password.length < 8) {
      setError('Password must be at least 8 characters');
      return;
    }
    setError('');
    setLoading(true);

    try {
      const data = await login(email, password);

      const role = data.user.role;
      navigate(dashboardPathFor(role), { replace: true });
    } catch {
      // Error toast handled by axios interceptor / AuthContext
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page" style={{ minHeight: 'calc(100vh - 80px)', display: 'flex', alignItems: 'center', background: '#f0f4ff' }}>
      <Container>
        <Row className="justify-content-center">
          <Col lg={5} md={7} sm={10}>
            <Card className="auth-card shadow-lg border-0" style={{ borderRadius: '20px', overflow: 'hidden' }}>
              <Card.Body className="p-4 p-md-5">
                <div className="text-center mb-4">
                  <div
                    className="login-icon-wrapper mx-auto mb-3"
                    style={{
                      width: '70px',
                      height: '70px',
                      borderRadius: '50%',
                      background: 'linear-gradient(135deg, #4a90d9, #357abd)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                    }}
                  >
                    <FaUserMd style={{ fontSize: '32px', color: 'white' }} />
                  </div>
                  <h2 className="fw-bold mb-1" style={{ color: '#1a202c', fontSize: '1.8rem' }}>
                    Welcome Back
                  </h2>
                  <p className="text-muted" style={{ fontSize: '0.95rem' }}>
                    Sign in to your HealthBook account
                  </p>
                </div>

                <Form onSubmit={handleSubmit}>
                  <fieldset disabled={loading}>
                    {error && (
                      <p className="text-danger mb-3" style={{ fontSize: '0.85rem' }}>
                        {error}
                      </p>
                    )}

                    <Form.Group className="mb-3">
                      <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
                        Email address
                      </Form.Label>
                      <div
                        className="input-group"
                        style={{
                          border: error ? '2px solid #dc3545' : '2px solid #e2e8f0',
                          borderRadius: '10px',
                          overflow: 'hidden',
                        }}
                      >
                        <span className="input-group-text bg-white border-0" style={{ paddingRight: '0' }}>
                          <FaEnvelope className="text-secondary" style={{ fontSize: '18px' }} />
                        </span>
                        <Form.Control
                          type="email"
                          placeholder="you@example.com"
                          value={email}
                          onChange={handleEmailChange}
                          style={{ border: 'none', padding: '0.85rem 1rem', fontSize: '0.95rem' }}
                          required
                        />
                      </div>
                    </Form.Group>

                    <Form.Group className="mb-3">
                      <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
                        Password
                      </Form.Label>
                      <div
                        className="input-group"
                        style={{
                          border: error ? '2px solid #dc3545' : '2px solid #e2e8f0',
                          borderRadius: '10px',
                          overflow: 'hidden',
                        }}
                      >
                        <span className="input-group-text bg-white border-0" style={{ paddingRight: '0' }}>
                          <FaLock className="text-secondary" style={{ fontSize: '18px' }} />
                        </span>
                        <Form.Control
                          type={showPassword ? 'text' : 'password'}
                          placeholder="Enter your password"
                          value={password}
                          onChange={handlePasswordChange}
                          style={{ border: 'none', padding: '0.85rem 1rem', fontSize: '0.95rem' }}
                          required
                        />
                        <span
                          className="input-group-text bg-white border-0"
                          onClick={() => setShowPassword(!showPassword)}
                          style={{ cursor: 'pointer', paddingLeft: '0' }}
                        >
                          {showPassword ? <BiHide style={{ fontSize: '20px' }} /> : <BiShow style={{ fontSize: '20px' }} />}
                        </span>
                      </div>
                    </Form.Group>
                  </fieldset>

                  <div className="d-flex justify-content-end align-items-center mb-4">
                    <Link to="/forgot-password" className="text-decoration-none text-primary fw-semibold" style={{ fontSize: '0.9rem' }}>
                      Forgot password?
                    </Link>
                  </div>

                  <Button
                    type="submit"
                    variant="primary"
                    className="w-100 py-3 fw-semibold"
                    style={{ borderRadius: '10px', fontSize: '1rem' }}
                    disabled={loading}
                  >
                    {loading ? (
                      <>
                        <Spinner animation="border" size="sm" className="me-2" />
                        Signing in...
                      </>
                    ) : (
                      'Sign in'
                    )}
                  </Button>
                </Form>

                <div className="text-center mt-4">
                  <p className="text-muted mb-3" style={{ fontSize: '0.95rem' }}>
                    New to HealthBook?
                  </p>
                  <div className="d-flex justify-content-center gap-3 flex-wrap">
                    <Link to="/register/patient" className="btn btn-outline-primary px-4 py-2" style={{ borderRadius: '10px', fontWeight: 500 }}>
                      <FaUser className="me-1" /> Patient
                    </Link>
                    <Link to="/register/doctor" className="btn btn-outline-success px-4 py-2" style={{ borderRadius: '10px', fontWeight: 500 }}>
                      <FaStethoscope className="me-1" /> Doctor
                    </Link>
                  </div>
                </div>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Login;
