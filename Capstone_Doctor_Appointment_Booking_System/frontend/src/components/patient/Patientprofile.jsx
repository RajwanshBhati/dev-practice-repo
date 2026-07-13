import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getCurrentUser, updateCurrentUser } from '../../api/users';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';
import { FaUser } from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';

const PatientProfile = () => {
  const { user, updateUser } = useAuth();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [formData, setFormData] = useState({ full_name: '', phone: '' });
  const [details, setDetails] = useState(null);

  useEffect(() => {
    loadProfile();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadProfile = async () => {
    setLoading(true);
    try {
      const data = await getCurrentUser();
      setDetails(data);
      setFormData({ full_name: data.full_name || '', phone: data.phone || '' });
    } catch (error) {
      console.error('Error loading profile:', error);
      toast.error('Failed to load profile');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const response = await updateCurrentUser(formData);
      toast.success('Profile updated successfully');
      updateUser({ full_name: response.user.full_name, phone: response.user.phone });
    } catch (error) {
      // handled by axios interceptor
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <Loading message="Loading profile..." />;
  }

  return (
    <Container className="mt-4">
      <h1 className="fw-bold mb-4" style={{ color: '#1a202c' }}>My Profile</h1>

      <Row>
        <Col lg={4} className="mb-4">
          <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4 text-center">
              <div
                className="mx-auto mb-3 rounded-circle d-flex align-items-center justify-content-center"
                style={{
                  width: '90px',
                  height: '90px',
                  fontSize: '32px',
                  color: 'white',
                  background: 'linear-gradient(135deg, #4a90d9, #357abd)',
                }}
              >
                {user?.full_name?.charAt(0) || <FaUser />}
              </div>
              <h5 className="fw-bold mt-2">{details?.full_name}</h5>
              <p className="text-muted mb-0">{details?.email}</p>
              <hr />
              <div className="text-start">
                <p className="mb-1"><strong>Gender:</strong> {details?.gender}</p>
                <p className="mb-0"><strong>Member since:</strong> {details?.created_at ? new Date(details.created_at).toLocaleDateString() : 'N/A'}</p>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={8}>
          <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <h5 className="fw-bold mb-4">Edit Profile</h5>
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label className="fw-semibold text-secondary">Full Name</Form.Label>
                  <Form.Control
                    type="text"
                    name="full_name"
                    value={formData.full_name}
                    onChange={handleChange}
                    style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                  />
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label className="fw-semibold text-secondary">Phone</Form.Label>
                  <Form.Control
                    type="tel"
                    name="phone"
                    value={formData.phone}
                    onChange={handleChange}
                    style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                  />
                </Form.Group>

                <Form.Group className="mb-4">
                  <Form.Label className="fw-semibold text-secondary">Email (cannot be changed)</Form.Label>
                  <Form.Control type="email" value={details?.email || ''} disabled style={{ borderRadius: '8px' }} />
                </Form.Group>

                <Button type="submit" variant="primary" className="px-4 py-2 fw-semibold" style={{ borderRadius: '8px' }} disabled={saving}>
                  {saving ? 'Saving...' : 'Save Changes'}
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default PatientProfile;
