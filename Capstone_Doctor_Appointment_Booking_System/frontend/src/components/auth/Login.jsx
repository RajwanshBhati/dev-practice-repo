import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Form, Button, Card, Container, Row, Col } from 'react-bootstrap';
import { FaEnvelope, FaLock, FaUserMd, FaUser, FaStethoscope } from 'react-icons/fa';
import { BiShow, BiHide } from 'react-icons/bi';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [rememberMe, setRememberMe] = useState(false);
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const data = await login(email, password);
      const role = data.user.role;

      if (role === 'PATIENT') {
        navigate('/');
      } else if (role === 'DOCTOR') {
        navigate('/doctor/dashboard');
      } else if (role === 'ADMIN') {
        navigate('/admin/dashboard');
      }
    } catch {
      // Error handled by context
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
                  <div className="login-icon-wrapper mx-auto mb-3"
                       style={{ width: '70px', height: '70px', borderRadius: '50%', background: 'linear-gradient(135deg, #4a90d9, #357abd)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <FaUserMd style={{ fontSize: '32px', color: 'white' }} />
                  </div>
                  <h2 className="fw-bold mb-1" style={{ color: '#1a202c', fontSize: '1.8rem' }}>Welcome Back</h2>
                  <p className="text-muted" style={{ fontSize: '0.95rem' }}>Sign in to your HealthBook account</p>
                </div>

                <Form onSubmit={handleSubmit}>
                  <Form.Group className="mb-3">
                    <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>Email address</Form.Label>
                    <div className="input-group" style={{ border: '2px solid #e2e8f0', borderRadius: '10px', overflow: 'hidden' }}>
                      <span className="input-group-text bg-white border-0" style={{ paddingRight: '0' }}>
                        <FaEnvelope className="text-secondary" style={{ fontSize: '18px' }} />
                      </span>
                      <Form.Control
                        type="email"
                        placeholder="you@example.com"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        style={{ border: 'none', padding: '0.85rem 1rem', fontSize: '0.95rem' }}
                        required
                      />
                    </div>
                  </Form.Group>

                  <Form.Group className="mb-3">
                    <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>Password</Form.Label>
                    <div className="input-group" style={{ border: '2px solid #e2e8f0', borderRadius: '10px', overflow: 'hidden' }}>
                      <span className="input-group-text bg-white border-0" style={{ paddingRight: '0' }}>
                        <FaLock className="text-secondary" style={{ fontSize: '18px' }} />
                      </span>
                      <Form.Control
                        type={showPassword ? 'text' : 'password'}
                        placeholder="Enter your password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
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

                  <div className="d-flex justify-content-between align-items-center mb-4">
                    <Form.Check
                      type="checkbox"
                      label="Remember me"
                      checked={rememberMe}
                      onChange={(e) => setRememberMe(e.target.checked)}
                      style={{ fontSize: '0.9rem' }}
                    />
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
                    {loading ? 'Signing in...' : 'Sign in'}
                  </Button>
                </Form>

                <div className="text-center mt-4">
                  <p className="text-muted mb-3" style={{ fontSize: '0.95rem' }}>New to HealthBook?</p>
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
