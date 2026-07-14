import React, { useState, useEffect } from 'react';
import { getPendingProfileUpdates, approveProfileUpdate, rejectProfileUpdate } from '../../api/admin';
import { Container, Card, Button, Badge } from 'react-bootstrap';
import { FaCheck, FaTimes, FaEdit, FaUserMd } from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';
import { useDashboardRefresh } from './DashboardLayout';

const FIELD_LABELS = {
  qualification: 'Qualification',
  specialization: 'Specialization',
  experience_years: 'Experience (years)',
  license_number: 'License Number',
  consultation_fee: 'Consultation Fee',
  clinic_address: 'Clinic Address',
  clinic_phone: 'Clinic Phone',
  bio: 'Bio',
  profile_picture: 'Profile Picture',
};

const ProfileUpdateRequests = () => {
  const [loading, setLoading] = useState(true);
  const [requests, setRequests] = useState([]);
  const refreshSidebarBadge = useDashboardRefresh();

  useEffect(() => {
    loadRequests();
  }, []);

  const loadRequests = async () => {
    setLoading(true);
    try {
      const data = await getPendingProfileUpdates();
      setRequests(data.doctors || []);
    } catch (error) {
      console.error('Error loading pending profile updates:', error);
      toast.error('Failed to load profile update requests');
    } finally {
      setLoading(false);
    }
  };

  const handleApprove = async (doctorId) => {
    try {
      await approveProfileUpdate(doctorId);
      toast.success('Profile update approved and applied');
      setRequests((prev) => prev.filter((d) => d.id !== doctorId));
      refreshSidebarBadge();
    } catch (error) {
      // handled by axios interceptor
    }
  };

  const handleReject = async (doctorId) => {
    const reason = window.prompt('Reason for rejecting this profile update (min 5 characters):');
    if (!reason || reason.trim().length < 5) {
      toast.error('Please provide a valid reason (min 5 characters)');
      return;
    }
    try {
      await rejectProfileUpdate(doctorId, { reason });
      toast.success('Profile update rejected');
      setRequests((prev) => prev.filter((d) => d.id !== doctorId));
      refreshSidebarBadge();
    } catch (error) {
      // handled by axios interceptor
    }
  };

  if (loading) {
    return <Loading message="Loading profile update requests..." />;
  }

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="fw-bold" style={{ color: '#1a202c' }}>
          Profile Update Requests
        </h1>
        <Badge bg="warning" text="dark" className="px-3 py-2" style={{ fontSize: '0.9rem' }}>
          {requests.length} pending
        </Badge>
      </div>

      {requests.length === 0 ? (
        <div className="text-center py-5">
          <div style={{ fontSize: '48px', marginBottom: '20px' }}></div>
          <h4>No pending requests</h4>
          <p className="text-muted">Every doctor's profile is up to date. New update requests will show up here.</p>
        </div>
      ) : (
        requests.map((doc) => (
          <Card key={doc.id} className="mb-3 shadow-sm border-0" style={{ borderRadius: '12px', borderLeft: '4px solid #f59e0b' }}>
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start flex-wrap gap-3">
                <div className="d-flex align-items-start">
                  <div
                    className="rounded-circle d-flex align-items-center justify-content-center me-3"
                    style={{
                      width: '50px',
                      height: '50px',
                      fontSize: '20px',
                      color: 'white',
                      background: 'linear-gradient(135deg, #4a90d9, #357abd)',
                      flexShrink: 0,
                    }}
                  >
                    <FaUserMd />
                  </div>
                  <div>
                    <h5 className="fw-bold mb-1" style={{ color: '#1a202c' }}>
                      {doc.full_name || doc.qualification || 'Doctor'}
                    </h5>
                    <p className="text-muted mb-2" style={{ fontSize: '0.85rem' }}>
                      <FaEdit className="me-1" /> Requested changes to:
                    </p>
                    <div className="d-flex flex-wrap gap-2">
                      {Object.entries(doc.pending_update || {}).map(([key, value]) => (
                        <Badge key={key} bg="light" text="dark" className="border px-2 py-1" style={{ fontSize: '0.78rem' }}>
                          <strong>{FIELD_LABELS[key] || key}:</strong> {String(value)}
                        </Badge>
                      ))}
                    </div>
                  </div>
                </div>

                <div className="d-flex gap-2">
                  <Button size="sm" variant="success" onClick={() => handleApprove(doc.id)} style={{ borderRadius: '8px' }}>
                    <FaCheck className="me-1" /> Approve
                  </Button>
                  <Button size="sm" variant="danger" onClick={() => handleReject(doc.id)} style={{ borderRadius: '8px' }}>
                    <FaTimes className="me-1" /> Reject
                  </Button>
                </div>
              </div>
            </Card.Body>
          </Card>
        ))
      )}
    </Container>
  );
};

export default ProfileUpdateRequests;
