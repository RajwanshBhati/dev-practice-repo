import { useState } from 'react';
import { useNavigate, useSearchParams, Link } from 'react-router-dom';
import { Form, Button, Card, Container, Row, Col } from 'react-bootstrap';
import { FaLock, FaUserMd } from 'react-icons/fa';
import { BiShow, BiHide } from 'react-icons/bi';
import { resetPassword } from '../../api/auth';
import { isValidPassword, getPasswordError } from '../../utils/validators';
import PasswordRequirements from '../common/PasswordRequirements';
import toast from 'react-hot-toast';

const ResetPassword = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token') || '';
  const navigate = useNavigate();

  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!token) {
      setError('Reset link is invalid or missing. Please request a new one.');
      return;
    }
    if (!isValidPassword(password)) {
      setError(getPasswordError(password));
      return;
    }
    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    setError('');
    setLoading(true);
    try {
      await resetPassword(token, password);
      toast.success('Password reset successfully! Please login.');
      navigate('/login');
    } catch {
      // handled by axios interceptor
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ minHeight: 'calc(100vh - 80px)', display: 'flex', alignItems: 'center', background: '#ffffff' }}>
      <Container>
        <Row className="justify-content-center">
          <Col lg={5} md={7} sm={10}>
            <Card className="auth-card shadow-lg border-0" style={{ borderRadius: '20px' }}>
              <Card.Body className="p-4 p-md-5">
                <div className="text-center mb-4">
                  <div className="login-icon-wrapper mx-auto mb-3">
                    <FaUserMd className="login-icon" />
                  </div>
                  <h2 className="fw-bold mb-1" style={{ color: '#1a202c' }}>Set New Password</h2>
                  <p className="text-muted">Choose a strong new password for your account.</p>
                </div>

                <Form onSubmit={handleSubmit} noValidate>
                  {error && <p className="text-danger mb-3" style={{ fontSize: '0.85rem' }}>{error}</p>}

                  <Form.Group className="mb-3">
                    <Form.Label className="fw-semibold text-secondary">New Password</Form.Label>
                    <div className="input-group">
                      <span className="input-group-text bg-white"><FaLock className="text-secondary" /></span>
                      <Form.Control
                        type={showPassword ? 'text' : 'password'}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Enter new password"
                      />
                      <span
                        className="input-group-text bg-white"
                        onClick={() => setShowPassword(!showPassword)}
                        style={{ cursor: 'pointer' }}
                      >
                        {showPassword ? <BiHide /> : <BiShow />}
                      </span>
                    </div>
                    <PasswordRequirements password={password} />
                  </Form.Group>

                  <Form.Group className="mb-4">
                    <Form.Label className="fw-semibold text-secondary">Confirm New Password</Form.Label>
                    <Form.Control
                      type={showPassword ? 'text' : 'password'}
                      value={confirmPassword}
                      onChange={(e) => setConfirmPassword(e.target.value)}
                      placeholder="Confirm new password"
                    />
                  </Form.Group>

                  <Button type="submit" variant="primary" className="w-100 py-3 fw-semibold" disabled={loading}>
                    {loading ? 'Resetting...' : 'Reset Password'}
                  </Button>
                </Form>

                <div className="text-center mt-4">
                  <Link to="/login" className="text-primary fw-bold text-decoration-none">Back to Sign in</Link>
                </div>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default ResetPassword;
