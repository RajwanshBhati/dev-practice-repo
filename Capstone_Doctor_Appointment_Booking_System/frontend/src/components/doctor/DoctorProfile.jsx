/**
 * Doctor Profile page component.
 * Allows doctors to view and update their profile.
 */

import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getDoctorProfile, updateDoctorProfile } from '../../api/doctor';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';
import { FaUserMd, FaCamera } from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';
import ProfilePicture from './ProfilePicture';
import { SPECIALIZATIONS } from '../../utils/constants';

const DoctorProfile = () => {
  const { user, updateUser } = useAuth();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [profile, setProfile] = useState(null);
  const [formData, setFormData] = useState({
    qualification: '',
    specialization: '',
    experience_years: '',
    license_number: '',
    consultation_fee: '',
    clinic_address: '',
    clinic_phone: '',
    bio: '',
    profile_picture: '',
  });

  /**
   * Load doctor profile on mount.
   */
  useEffect(() => {
    loadProfile();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  /**
   * Load doctor profile from API.
   */
  const loadProfile = async () => {
    setLoading(true);
    try {
      const data = await getDoctorProfile();
      setProfile(data);
      setFormData({
        qualification: data.qualification || '',
        specialization: data.specialization || '',
        experience_years: data.experience_years || '',
        license_number: data.license_number || '',
        consultation_fee: data.consultation_fee || '',
        clinic_address: data.clinic_address || '',
        clinic_phone: data.clinic_phone || '',
        bio: data.bio || '',
        profile_picture: data.profile_picture || '',
      });
    } catch (error) {
      console.error('Error loading profile:', error);
      toast.error('Failed to load profile');
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle form input changes.
   */
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  /**
   * Handle profile picture update.
   */
  const handlePictureUpdate = (pictureUrl) => {
    setFormData({ ...formData, profile_picture: pictureUrl });
    toast.success('Profile picture updated');
  };

  /**
   * Handle form submission.
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);

    try {
      const data = { ...formData };
      // Convert numeric fields
      if (data.experience_years) {
        data.experience_years = parseInt(data.experience_years);
      }
      if (data.consultation_fee) {
        data.consultation_fee = parseFloat(data.consultation_fee);
      }
      // Remove empty fields
      Object.keys(data).forEach((key) => {
        if (data[key] === '' || data[key] === null) {
          delete data[key];
        }
      });

      const response = await updateDoctorProfile(data);
      toast.success('Profile updated successfully');

      // Update user context if name changed
      if (data.full_name) {
        updateUser({ full_name: data.full_name });
      }
    } catch (error) {
      // Error handled by axios interceptor
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <Loading message="Loading profile..." />;
  }

  return (
    <Container className="mt-4">
      <h1 className="fw-bold mb-4" style={{ color: '#1a202c' }}>Doctor Profile</h1>

      <Row>
        <Col lg={4} className="mb-4">
          <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4 text-center">
              <ProfilePicture
                profilePicture={formData.profile_picture}
                fullName={user?.full_name}
                onUpdate={handlePictureUpdate}
              />
              <h5 className="fw-bold mt-3">{user?.full_name}</h5>
              <p className="text-muted mb-0">{formData.specialization}</p>
              <p className="text-muted">{formData.qualification}</p>
              <hr />
              <div className="text-start">
                <p className="mb-1">
                  <strong>Email:</strong> {user?.email}
                </p>
                <p className="mb-0">
                  <strong>Phone:</strong> {user?.phone}
                </p>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={8}>
          <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <h5 className="fw-bold mb-4">Edit Profile</h5>

              <Form onSubmit={handleSubmit}>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label className="fw-semibold text-secondary">
                        Qualification
                      </Form.Label>
                      <Form.Control
                        type="text"
                        name="qualification"
                        placeholder="MD, MBBS, etc."
                        value={formData.qualification}
                        onChange={handleChange}
                        style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label className="fw-semibold text-secondary">
                        Specialization
                      </Form.Label>
                      <Form.Select
                        name="specialization"
                        value={formData.specialization}
                        onChange={handleChange}
                        style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                      >
                        <option value="">Select specialization</option>
                        {SPECIALIZATIONS.map((spec) => (
                          <option key={spec} value={spec}>{spec}</option>
                        ))}
                      </Form.Select>
                    </Form.Group>
                  </Col>
                </Row>

                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label className="fw-semibold text-secondary">
                        Experience (years)
                      </Form.Label>
                      <Form.Control
                        type="number"
                        name="experience_years"
                        placeholder="5"
                        value={formData.experience_years}
                        onChange={handleChange}
                        style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label className="fw-semibold text-secondary">
                        License Number
                      </Form.Label>
                      <Form.Control
                        type="text"
                        name="license_number"
                        placeholder="LIC-12345"
                        value={formData.license_number}
                        onChange={handleChange}
                        style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label className="fw-semibold text-secondary">
                        Consultation Fee ($)
                      </Form.Label>
                      <Form.Control
                        type="number"
                        name="consultation_fee"
                        placeholder="150"
                        value={formData.consultation_fee}
                        onChange={handleChange}
                        style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label className="fw-semibold text-secondary">
                        Clinic Phone
                      </Form.Label>
                      <Form.Control
                        type="tel"
                        name="clinic_phone"
                        placeholder="+1234567890"
                        value={formData.clinic_phone}
                        onChange={handleChange}
                        style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Form.Group className="mb-3">
                  <Form.Label className="fw-semibold text-secondary">
                    Clinic Address
                  </Form.Label>
                  <Form.Control
                    type="text"
                    name="clinic_address"
                    placeholder="123 Healthcare Ave"
                    value={formData.clinic_address}
                    onChange={handleChange}
                    style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label className="fw-semibold text-secondary">
                    Bio
                  </Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    name="bio"
                    placeholder="Brief description about yourself"
                    value={formData.bio}
                    onChange={handleChange}
                    style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                  />
                </Form.Group>

                <div className="d-flex gap-3">
                  <Button
                    type="submit"
                    variant="primary"
                    className="flex-grow-1 py-2 fw-semibold"
                    style={{ borderRadius: '8px' }}
                    disabled={saving}
                  >
                    {saving ? 'Saving...' : 'Save Changes'}
                  </Button>
                  <Button
                    variant="outline-secondary"
                    className="px-4"
                    style={{ borderRadius: '8px' }}
                    onClick={() => loadProfile()}
                    disabled={saving}
                  >
                    Reset
                  </Button>
                </div>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default DoctorProfile;
