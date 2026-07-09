import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registerPatient } from '../../api/auth';
import { isValidEmail, isValidPhone, isValidPassword, getPasswordError } from '../../utils/validators';
import { Form, Button, Card, Container, Row, Col } from 'react-bootstrap';
import toast from 'react-hot-toast';
import { FaUser, FaUserMd } from 'react-icons/fa';
import { BiShow, BiHide } from 'react-icons/bi';

const RegisterPatient = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
    full_name: '',
    email: '',
    phone: '',
    gender: 'Male',
    date_of_birth: '',
    password: '',
    confirm_password: '',
  });
  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    if (errors[name]) {
      setErrors({ ...errors, [name]: '' });
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.full_name || formData.full_name.length < 2) {
      newErrors.full_name = 'Full name must be at least 2 characters';
    }

    if (!formData.email || !isValidEmail(formData.email)) {
      newErrors.email = 'Please enter a valid email address';
    }

    if (!formData.phone || !isValidPhone(formData.phone)) {
      newErrors.phone = 'Please enter a valid phone number (10-15 digits)';
    }

    if (!formData.date_of_birth) {
      newErrors.date_of_birth = 'Please select your date of birth';
    }

    if (!formData.password) {
      newErrors.password = 'Please enter a password';
    } else if (!isValidPassword(formData.password)) {
      newErrors.password = getPasswordError(formData.password);
    }

    if (formData.password !== formData.confirm_password) {
      newErrors.confirm_password = 'Passwords do not match';
    }

    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const validationErrors = validateForm();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    setLoading(true);

    try {
      await registerPatient(formData);
      toast.success('Registration successful! Please login to continue.');
      navigate('/login');
    } catch {
      // Error handled by axios interceptor
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="register-page">
      <Container className="py-5">
        <Row className="justify-content-center">
          <Col lg={7} md={9} sm={11}>
            <Card className="auth-card shadow-lg border-0">
              <Card.Body className="p-4 p-md-5">
                <div className="text-center mb-4">
                  <div className="register-icon-wrapper mx-auto mb-3">
                    <FaUserMd className="register-icon" />
                  </div>
                  <h2 className="fw-bold mb-1" style={{ color: '#1a202c' }}>Create Patient Account</h2>
                  <p className="text-muted">
                    Already have an account? <Link to="/login" className="text-primary fw-bold">Sign in</Link>
                  </p>
                </div>

                <div className="role-badge d-inline-block px-3 py-1 rounded-pill bg-primary-soft text-primary mb-4">
                  <FaUser className="me-1" /> Registering as: <strong>Patient</strong>
                </div>

                <Form onSubmit={handleSubmit} noValidate>
                  <Row>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary">
                          Full Name <span className="text-danger">*</span>
                        </Form.Label>
                        <Form.Control
                          type="text"
                          name="full_name"
                          placeholder="Dr. John Doe"
                          value={formData.full_name}
                          onChange={handleChange}
                          isInvalid={!!errors.full_name}
                          className="py-2"
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.full_name}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary">
                          Email <span className="text-danger">*</span>
                        </Form.Label>
                        <Form.Control
                          type="email"
                          name="email"
                          placeholder="you@example.com"
                          value={formData.email}
                          onChange={handleChange}
                          isInvalid={!!errors.email}
                          className="py-2"
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.email}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                  </Row>

                  <Row>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary">
                          Phone <span className="text-danger">*</span>
                        </Form.Label>
                        <Form.Control
                          type="tel"
                          name="phone"
                          placeholder="+1234567890"
                          value={formData.phone}
                          onChange={handleChange}
                          isInvalid={!!errors.phone}
                          className="py-2"
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.phone}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary">
                          Gender <span className="text-danger">*</span>
                        </Form.Label>
                        <Form.Select
                          name="gender"
                          value={formData.gender}
                          onChange={handleChange}
                          className="py-2"
                        >
                          <option value="Male">Male</option>
                          <option value="Female">Female</option>
                          <option value="Other">Other</option>
                        </Form.Select>
                      </Form.Group>
                    </Col>
                  </Row>

                  <Form.Group className="mb-3">
                    <Form.Label className="fw-semibold text-secondary">
                      Date of Birth <span className="text-danger">*</span>
                    </Form.Label>
                    <Form.Control
                      type="date"
                      name="date_of_birth"
                      value={formData.date_of_birth}
                      onChange={handleChange}
                      isInvalid={!!errors.date_of_birth}
                      className="py-2"
                    />
                    <Form.Control.Feedback type="invalid">
                      {errors.date_of_birth}
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Row>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary">
                          Password <span className="text-danger">*</span>
                        </Form.Label>
                        <div className="position-relative">
                          <Form.Control
                            type={showPassword ? 'text' : 'password'}
                            name="password"
                            placeholder="Min 8 characters"
                            value={formData.password}
                            onChange={handleChange}
                            isInvalid={!!errors.password}
                            className="py-2 pe-5"
                          />
                          <span
                            className="position-absolute top-50 end-0 translate-middle-y me-3"
                            onClick={() => setShowPassword(!showPassword)}
                            style={{ cursor: 'pointer' }}
                          >
                            {showPassword ? <BiHide /> : <BiShow />}
                          </span>
                        </div>
                        <Form.Control.Feedback type="invalid">
                          {errors.password}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary">
                          Confirm Password <span className="text-danger">*</span>
                        </Form.Label>
                        <Form.Control
                          type="password"
                          name="confirm_password"
                          placeholder="Confirm password"
                          value={formData.confirm_password}
                          onChange={handleChange}
                          isInvalid={!!errors.confirm_password}
                          className="py-2"
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.confirm_password}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                  </Row>

                  <Form.Group className="mb-4">
                    <Form.Check
                      type="checkbox"
                      label="Show password"
                      checked={showPassword}
                      onChange={() => setShowPassword(!showPassword)}
                    />
                  </Form.Group>

                  <Button
                    type="submit"
                    variant="primary"
                    className="w-100 py-3 fw-semibold"
                    disabled={loading}
                  >
                    {loading ? 'Creating Account...' : 'Create Patient Account'}
                  </Button>
                </Form>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default RegisterPatient;
