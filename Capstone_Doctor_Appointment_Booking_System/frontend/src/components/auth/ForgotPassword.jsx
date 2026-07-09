import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Form, Button, Card, Container, Row, Col } from 'react-bootstrap';
import { FaEnvelope, FaUserMd } from 'react-icons/fa';
import { forgotPassword } from '../../api/auth';
import { isValidEmail } from '../../utils/validators';
import toast from 'react-hot-toast';

const ForgotPassword = () => {
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [sent, setSent] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isValidEmail(email)) {
      setError('Please enter a valid email address');
      return;
    }
    setError('');
    setLoading(true);
    try {
      await forgotPassword(email);
      setSent(true);
      toast.success('If an account exists, a reset link has been sent.');
    } catch {
      // handled by axios interceptor
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ minHeight: 'calc(100vh - 80px)', display: 'flex', alignItems: 'center', background: '#f0f4ff' }}>
      <Container>
        <Row className="justify-content-center">
          <Col lg={5} md={7} sm={10}>
            <Card className="auth-card shadow-lg border-0" style={{ borderRadius: '20px' }}>
              <Card.Body className="p-4 p-md-5">
                <div className="text-center mb-4">
                  <div className="login-icon-wrapper mx-auto mb-3">
                    <FaUserMd className="login-icon" />
                  </div>
                  <h2 className="fw-bold mb-1" style={{ color: '#1a202c' }}>Reset Password</h2>
                  <p className="text-muted">
                    {sent
                      ? 'Check your inbox for a reset link.'
                      : "Enter your email and we'll send you a reset link."}
                  </p>
                </div>

                {!sent && (
                  <Form onSubmit={handleSubmit} noValidate>
                    <Form.Group className="mb-4">
                      <Form.Label className="fw-semibold text-secondary">Email address</Form.Label>
                      <div className="input-group">
                        <span className="input-group-text bg-white">
                          <FaEnvelope className="text-secondary" />
                        </span>
                        <Form.Control
                          type="email"
                          placeholder="you@example.com"
                          value={email}
                          onChange={(e) => setEmail(e.target.value)}
                          isInvalid={!!error}
                        />
                        <Form.Control.Feedback type="invalid">{error}</Form.Control.Feedback>
                      </div>
                    </Form.Group>

                    <Button type="submit" variant="primary" className="w-100 py-3 fw-semibold" disabled={loading}>
                      {loading ? 'Sending...' : 'Send Reset Link'}
                    </Button>
                  </Form>
                )}

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

export default ForgotPassword;
