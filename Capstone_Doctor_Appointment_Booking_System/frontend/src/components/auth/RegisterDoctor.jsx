import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { registerDoctor } from '../../api/auth';
import { isValidEmail, isValidPhone, isValidPassword, getPasswordError, isValidAge } from '../../utils/validators';
import { SPECIALIZATIONS } from '../../utils/constants';
import { Form, Button, Card, Container, Row, Col } from 'react-bootstrap';
import PasswordRequirements from '../common/PasswordRequirements';
import toast from 'react-hot-toast';
import { Spinner } from 'react-bootstrap';

import {
  FaUser,
  FaStethoscope,
  FaHospital,
} from 'react-icons/fa';
import { BiShow, BiHide } from 'react-icons/bi';

const RegisterDoctor = () => {
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
    qualification: '',
    specialization: 'Cardiologist',
    experience_years: '',
    license_number: '',
    consultation_fee: '',
    clinic_address: '',
    bio: '',
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

    // Personal Information
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
     } else if (!isValidAge(formData.date_of_birth, 18)) {
       newErrors.date_of_birth = 'Doctors must be at least 18 years old';
     }

    if (!formData.password) {
      newErrors.password = 'Please enter a password';
    } else if (!isValidPassword(formData.password)) {
      newErrors.password = getPasswordError(formData.password);
    }

    if (formData.password !== formData.confirm_password) {
      newErrors.confirm_password = 'Passwords do not match';
    }

    // Professional Information
    if (!formData.qualification || formData.qualification.length < 2) {
      newErrors.qualification = 'Please enter your qualification';
    }

    if (!formData.specialization) {
      newErrors.specialization = 'Please select your specialization';
    }

    if (!formData.experience_years || parseInt(formData.experience_years) < 0) {
      newErrors.experience_years = 'Please enter valid years of experience';
    }

    if (!formData.license_number || formData.license_number.length < 3) {
      newErrors.license_number = 'Please enter your license number';
    }

    if (!formData.consultation_fee || parseFloat(formData.consultation_fee) <= 0) {
      newErrors.consultation_fee = 'Please enter a valid consultation fee';
    }

    if (!formData.clinic_address || formData.clinic_address.length < 5) {
      newErrors.clinic_address = 'Please enter your clinic address';
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
      await registerDoctor(formData);
      toast.success('Doctor registered! Please wait for admin approval.');
      navigate('/login');
    } catch {
      // Error handled by axios interceptor
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ background: '#f0f4ff', minHeight: 'calc(100vh - 80px)', padding: '40px 0' }}>
      <Container>
        <Row className="justify-content-center">
          <Col lg={8} md={10} sm={11}>
            <Card className="shadow-lg border-0" style={{ borderRadius: '20px', overflow: 'hidden' }}>
              <Card.Body className="p-4 p-md-5">
                <div className="text-center mb-4">
                  <div className="register-icon-wrapper mx-auto mb-3"
                       style={{ width: '70px', height: '70px', borderRadius: '50%', background: 'linear-gradient(135deg, #4a90d9, #357abd)', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <FaStethoscope style={{ fontSize: '32px', color: 'white' }} />
                  </div>
                  <h2 className="fw-bold mb-1" style={{ color: '#1a202c', fontSize: '1.8rem' }}>Create Doctor Account</h2>
                  <p className="text-muted" style={{ fontSize: '0.95rem' }}>
                    Already have an account? <Link to="/login" className="text-primary fw-bold">Sign in</Link>
                  </p>
                </div>

                <div className="role-badge d-inline-block px-3 py-1 rounded-pill bg-primary-soft text-primary mb-4"
                     style={{ background: '#eaf2fc', color: '#357abd', fontSize: '0.9rem' }}>
                  <FaStethoscope className="me-1" /> Registering as: <strong>Doctor</strong>
                </div>

                <Form onSubmit={handleSubmit} noValidate>
                  <fieldset disabled={loading}>
                  {/* Personal Information Section */}
                  <h5 className="section-title mb-3" style={{ fontSize: '1.1rem', fontWeight: 600, color: '#2d3748', borderBottom: '3px solid #4a90d9', paddingBottom: '0.5rem', display: 'inline-block' }}>
                    <FaUser className="me-2" /> Personal Information
                  </h5>

                  <Row>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
                          Full Name <span className="text-danger">*</span>
                        </Form.Label>
                        <Form.Control
                          type="text"
                          name="full_name"
                          placeholder="Dr. John Doe"
                          value={formData.full_name}
                          onChange={handleChange}
                          isInvalid={!!errors.full_name}
                          style={{ padding: '0.75rem 1rem', borderRadius: '10px', border: '2px solid #e2e8f0' }}
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.full_name}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
                          Email <span className="text-danger">*</span>
                        </Form.Label>
                        <Form.Control
                          type="email"
                          name="email"
                          placeholder="you@example.com"
                          value={formData.email}
                          onChange={handleChange}
                          isInvalid={!!errors.email}
                          style={{ padding: '0.75rem 1rem', borderRadius: '10px', border: '2px solid #e2e8f0' }}
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
                        <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
                          Phone <span className="text-danger">*</span>
                        </Form.Label>
                        <Form.Control
                          type="tel"
                          name="phone"
                          placeholder="+1234567890"
                          value={formData.phone}
                          onChange={handleChange}
                          isInvalid={!!errors.phone}
                          style={{ padding: '0.75rem 1rem', borderRadius: '10px', border: '2px solid #e2e8f0' }}
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.phone}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
                          Gender <span className="text-danger">*</span>
                        </Form.Label>
                        <Form.Select
                          name="gender"
                          value={formData.gender}
                          onChange={handleChange}
                          style={{ padding: '0.75rem 1rem', borderRadius: '10px', border: '2px solid #e2e8f0' }}
                        >
                          <option value="Male">Male</option>
                          <option value="Female">Female</option>
                          <option value="Other">Other</option>
                        </Form.Select>
                      </Form.Group>
                    </Col>
                  </Row>

                  <Form.Group className="mb-3">
                    <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
                      Date of Birth <span className="text-danger">*</span>
                    </Form.Label>
                    <Form.Control
                      type="date"
                      name="date_of_birth"
                      value={formData.date_of_birth}
                      onChange={handleChange}
                      isInvalid={!!errors.date_of_birth}
                      style={{ padding: '0.75rem 1rem', borderRadius: '10px', border: '2px solid #e2e8f0' }}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errors.date_of_birth}
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Row>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
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
                            style={{ padding: '0.75rem 1rem', borderRadius: '10px', border: '2px solid #e2e8f0', paddingRight: '45px' }}
                          />
                          <span
                            className="position-absolute top-50 end-0 translate-middle-y me-3"
                            onClick={() => setShowPassword(!showPassword)}
                            style={{ cursor: 'pointer' }}
                          >
                            {showPassword ? <BiHide size={20} /> : <BiShow size={20} />}
                          </span>
                        </div>
                        <PasswordRequirements password={formData.password} />
                        <Form.Control.Feedback type="invalid">
                          {errors.password}
                        </Form.Control.Feedback>
                        <Form.Text className="text-muted" style={{ fontSize: '0.8rem' }}>
                          8-12 characters with uppercase, lowercase, digit &amp; special character
                        </Form.Text>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
                          Confirm Password <span className="text-danger">*</span>
                        </Form.Label>
                        <Form.Control
                          type="password"
                          name="confirm_password"
                          placeholder="Confirm password"
                          value={formData.confirm_password}
                          onChange={handleChange}
                          isInvalid={!!errors.confirm_password}
                          style={{ padding: '0.75rem 1rem', borderRadius: '10px', border: '2px solid #e2e8f0' }}
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.confirm_password}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                  </Row>

                  {/* Professional Information Section */}
                  <h5 className="section-title mb-3 mt-4" style={{ fontSize: '1.1rem', fontWeight: 600, color: '#2d3748', borderBottom: '3px solid #4a90d9', paddingBottom: '0.5rem', display: 'inline-block' }}>
                    <FaHospital className="me-2" /> Professional Information
                  </h5>

                  <Form.Group className="mb-3">
                    <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
                      Qualification <span className="text-danger">*</span>
                    </Form.Label>
                    <Form.Control
                      type="text"
                      name="qualification"
                      placeholder="MD, MBBS, etc."
                      value={formData.qualification}
                      onChange={handleChange}
                      isInvalid={!!errors.qualification}
                      style={{ padding: '0.75rem 1rem', borderRadius: '10px', border: '2px solid #e2e8f0' }}
                    />
                    <Form.Control.Feedback type="invalid">
                      {errors.qualification}
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Form.Group className="mb-3">
                    <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
                      Specialization <span className="text-danger">*</span>
                    </Form.Label>
                    <Form.Select
                      name="specialization"
                      value={formData.specialization}
                      onChange={handleChange}
                      isInvalid={!!errors.specialization}
                      style={{ padding: '0.75rem 1rem', borderRadius: '10px', border: '2px solid #e2e8f0' }}
                    >
                      {SPECIALIZATIONS.map((spec) => (
                        <option key={spec} value={spec}>{spec}</option>
                      ))}
                    </Form.Select>
                    <Form.Control.Feedback type="invalid">
                      {errors.specialization}
                    </Form.Control.Feedback>
                  </Form.Group>

                  <Row>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
                          Experience (years) <span className="text-danger">*</span>
                        </Form.Label>
                        <Form.Control
                          type="number"
                          name="experience_years"
                          placeholder="5"
                          value={formData.experience_years}
                          onChange={handleChange}
                          isInvalid={!!errors.experience_years}
                          style={{ padding: '0.75rem 1rem', borderRadius: '10px', border: '2px solid #e2e8f0' }}
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.experience_years}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
                          License Number <span className="text-danger">*</span>
                        </Form.Label>
                        <Form.Control
                          type="text"
                          name="license_number"
                          placeholder="LIC-12345"
                          value={formData.license_number}
                          onChange={handleChange}
                          isInvalid={!!errors.license_number}
                          style={{ padding: '0.75rem 1rem', borderRadius: '10px', border: '2px solid #e2e8f0' }}
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.license_number}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                  </Row>

                  <Row>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
                          Consultation Fee ($) <span className="text-danger">*</span>
                        </Form.Label>
                        <Form.Control
                          type="number"
                          name="consultation_fee"
                          placeholder="150"
                          value={formData.consultation_fee}
                          onChange={handleChange}
                          isInvalid={!!errors.consultation_fee}
                          style={{ padding: '0.75rem 1rem', borderRadius: '10px', border: '2px solid #e2e8f0' }}
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.consultation_fee}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                    <Col md={6}>
                      <Form.Group className="mb-3">
                        <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>
                          Clinic Address <span className="text-danger">*</span>
                        </Form.Label>
                        <Form.Control
                          type="text"
                          name="clinic_address"
                          placeholder="123 Healthcare Ave"
                          value={formData.clinic_address}
                          onChange={handleChange}
                          isInvalid={!!errors.clinic_address}
                          style={{ padding: '0.75rem 1rem', borderRadius: '10px', border: '2px solid #e2e8f0' }}
                        />
                        <Form.Control.Feedback type="invalid">
                          {errors.clinic_address}
                        </Form.Control.Feedback>
                      </Form.Group>
                    </Col>
                  </Row>

                  <Form.Group className="mb-3">
                    <Form.Label className="fw-semibold text-secondary" style={{ fontSize: '0.9rem' }}>Bio (Optional)</Form.Label>
                    <Form.Control
                      as="textarea"
                      rows={3}
                      name="bio"
                      placeholder="Brief description about yourself"
                      value={formData.bio}
                      onChange={handleChange}
                      style={{ padding: '0.75rem 1rem', borderRadius: '10px', border: '2px solid #e2e8f0' }}
                    />
                  </Form.Group>
                  </fieldset>
                  <Button
                    type="submit"
                    variant="primary"
                    className="w-100 py-3 fw-semibold"
                    style={{ borderRadius: '10px', fontSize: '1rem', background: 'linear-gradient(135deg, #4a90d9, #357abd)', border: 'none' }}
                    disabled={loading}
                  >
                     {loading ? (
                      <>
                     <Spinner animation="border" size="sm" className="me-2" />
                     Signing in...
                     </>
                     ) : 'Sign in'}
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

export default RegisterDoctor;
