import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getCurrentUser, updateCurrentUser } from '../../api/users';
import { Container, Row, Col, Card, Form, Button } from 'react-bootstrap';
import { FaUserShield } from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';

const AdminProfile = () => {
  const { user, updateUser } = useAuth();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [formData, setFormData] = useState({
    full_name: '',
    phone: '',
  });

  useEffect(() => {
    loadProfile();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadProfile = async () => {
    setLoading(true);
    try {
      const data = await getCurrentUser();
      setFormData({
        full_name: data.full_name || '',
        phone: data.phone || '',
      });
    } catch (error) {
      console.error('Error loading admin profile:', error);
      toast.error('Failed to load profile');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.full_name || formData.full_name.trim().length < 2) {
      toast.error('Full name must be at least 2 characters');
      return;
    }

    setSaving(true);
    try {
      const response = await updateCurrentUser({
        full_name: formData.full_name,
        phone: formData.phone,
      });
      toast.success(response.message || 'Profile updated successfully');
      updateUser({ full_name: formData.full_name, phone: formData.phone });
    } catch (error) {
      // Error toast already shown by the axios interceptor
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <Loading message="Loading profile..." />;
  }

  return (
    <Container className="mt-4 mb-5">
      <h1 className="fw-bold mb-4" style={{ color: '#1a202c' }}>Admin Profile</h1>
      <Row>
        <Col lg={4} className="mb-4">
          <Card className="shadow-sm border-0" style={{ borderRadius: '16px' }}>
            <Card.Body className="p-4 text-center">
              <div
                className="rounded-circle d-flex align-items-center justify-content-center mx-auto mb-3"
                style={{ width: '90px', height: '90px', fontSize: '36px', color: 'white', background: 'linear-gradient(135deg, #4a90d9, #357abd)' }}
              >
                <FaUserShield />
              </div>
              <h5 className="fw-bold mb-0">{formData.full_name || user?.full_name}</h5>
              <p className="text-muted">Administrator</p>
              <hr />
              <div className="text-start">
                <p className="mb-1"><strong>Email:</strong> {user?.email}</p>
                <p className="mb-0"><strong>Role:</strong> {user?.role}</p>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={8}>
          <Card className="shadow-sm border-0" style={{ borderRadius: '16px' }}>
            <Card.Body className="p-4">
              <h5 className="fw-bold mb-4">Edit Profile</h5>

              <Form onSubmit={handleSubmit}>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label className="fw-semibold text-secondary">Full Name</Form.Label>
                      <Form.Control
                        type="text"
                        name="full_name"
                        value={formData.full_name}
                        onChange={handleChange}
                        style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                        required
                      />
                    </Form.Group>
                  </Col>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label className="fw-semibold text-secondary">Phone</Form.Label>
                      <Form.Control
                        type="tel"
                        name="phone"
                        placeholder="+1234567890"
                        value={formData.phone}
                        onChange={handleChange}
                        style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Form.Group className="mb-3">
                  <Form.Label className="fw-semibold text-secondary">Email</Form.Label>
                  <Form.Control
                    type="email"
                    value={user?.email || ''}
                    disabled
                    style={{ borderRadius: '8px', border: '2px solid #e2e8f0', background: '#f8f9fa' }}
                  />
                  <Form.Text className="text-muted">Email cannot be changed.</Form.Text>
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

export default AdminProfile;
