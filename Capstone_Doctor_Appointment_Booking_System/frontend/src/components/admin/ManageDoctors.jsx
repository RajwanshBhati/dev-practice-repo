import React, { useState, useEffect } from 'react';
import { getAllDoctorsAdmin, getPendingDoctors, approveDoctor, rejectDoctor } from '../../api/admin';
import { Container, Card, Row, Col, Button, Badge, Form } from 'react-bootstrap';
import { FaCheck, FaTimes, FaEye, FaUserMd, FaMapMarkerAlt, FaClock,FaEdit } from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';
import DoctorApproval from './DoctorApproval';
import {
  DOCTOR_STATUS_OPTIONS,
  DOCTOR_STATUS_COLORS,
  DOCTOR_STATUS_LABELS,
} from '../../utils/constants';
import { getPendingProfileUpdates, approveProfileUpdate, rejectProfileUpdate } from '../../api/admin';

const ManageDoctors = () => {
  const [loading, setLoading] = useState(true);
  const [doctors, setDoctors] = useState([]);
  const [total, setTotal] = useState(0);
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [selectedDoctor, setSelectedDoctor] = useState(null);
  const [showApprovalModal, setShowApprovalModal] = useState(false);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(false);
  const [pendingUpdates, setPendingUpdates] = useState([]);
  const limit = 10;

  /**
   * Load doctors on mount and filter change.
   */
  useEffect(() => {
    loadDoctors();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [statusFilter, page]);


useEffect(() => { loadPendingUpdates(); }, []);

const loadPendingUpdates = async () => {
  try {
    const data = await getPendingProfileUpdates();
    setPendingUpdates(data.doctors || []);
  } catch (error) {
    console.error('Error loading pending profile updates:', error);
  }
};

const handleApproveUpdate = async (doctorId) => {
  try {
    await approveProfileUpdate(doctorId);
    toast.success('Profile update approved');
    setPendingUpdates((prev) => prev.filter((d) => d.id !== doctorId));
    loadDoctors();
  } catch (error) {}
};

const handleRejectUpdate = async (doctorId) => {
  const reason = window.prompt('Reason for rejecting this profile update (min 5 characters):');
  if (!reason || reason.trim().length < 5) {
    toast.error('Please provide a valid reason (min 5 characters)');
    return;
  }
  try {
    await rejectProfileUpdate(doctorId, { reason });
    toast.success('Profile update rejected');
    setPendingUpdates((prev) => prev.filter((d) => d.id !== doctorId));
  } catch (error) {}
};
  /**
   * Load doctors from API.
   */
  const loadDoctors = async () => {
    setLoading(true);
    try {
      const skip = (page - 1) * limit;
      const params = { limit, skip };

      if (statusFilter !== 'ALL') {
        params.status = statusFilter;
      }

      const data = await getAllDoctorsAdmin(params);
      if (page === 1) {
        setDoctors(data.doctors || []);
      } else {
        setDoctors((prev) => [...prev, ...(data.doctors || [])]);
      }
      setTotal(data.total || 0);
      setHasMore(data.total_pages > page);
    } catch (error) {
      console.error('Error loading doctors:', error);
      toast.error('Failed to load doctors');
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle status filter change.
   */
  const handleFilterChange = (e) => {
    setStatusFilter(e.target.value);
    setPage(1);
  };

  /**
   * Handle load more.
   */
  const handleLoadMore = () => {
    setPage((prev) => prev + 1);
  };

  /**
   * Handle approve/reject action.
   */
  const handleAction = (doctor, action) => {
    setSelectedDoctor(doctor);
    setShowApprovalModal(true);
  };

  /**
   * Handle approval success.
   */
  const handleApprovalSuccess = (doctorId, status) => {
    setShowApprovalModal(false);
    setDoctors((prev) =>
      prev.map((doc) =>
        doc.id === doctorId ? { ...doc, status } : doc
      )
    );
    toast.success(`Doctor ${status.toLowerCase()} successfully`);
  };

  /**
   * Get status color.
   */
  const getStatusColor = (status) => {
    return DOCTOR_STATUS_COLORS[status] || 'secondary';
  };

  /**
   * Get status label.
   */
  const getStatusLabel = (status) => {
    return DOCTOR_STATUS_LABELS[status] || status;
  };

  /**
   * Format date for display.
   */
  const formatDate = (dateStr) => {
    if (!dateStr) return 'N/A';
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    });
  };

  if (loading && doctors.length === 0) {
    return <Loading message="Loading doctors..." />;
  }

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="fw-bold" style={{ color: '#1a202c' }}>
          Manage Doctors
        </h1>
      </div>

      {/* Filters */}
      <Card className="shadow-sm mb-4" style={{ borderRadius: '12px', border: 'none' }}>
        <Card.Body className="p-3">
          <div className="d-flex gap-3 align-items-center flex-wrap">
            <label className="fw-semibold text-secondary">Filter by Status:</label>
            <Form.Select
              value={statusFilter}
              onChange={handleFilterChange}
              style={{
                width: '200px',
                borderRadius: '8px',
                border: '2px solid #e2e8f0',
              }}
            >
              {DOCTOR_STATUS_OPTIONS.map((opt) => (
                <option key={opt.value} value={opt.value}>
                  {opt.label}
                </option>
              ))}
            </Form.Select>
            <span className="text-muted">
              Found <strong>{total}</strong> {total === 1 ? 'doctor' : 'doctors'}
            </span>
          </div>
        </Card.Body>
      </Card>

      {/* Doctor List */}
      {doctors.length === 0 ? (
        <div className="text-center py-5">
          <div style={{ fontSize: '48px', marginBottom: '20px' }}></div>
          <h4>No doctors found</h4>
          <p className="text-muted">No doctors match the current filter.</p>
        </div>
      ) : (
        <>
          {doctors.map((doctor) => (
            <Card
              key={doctor.id}
              className="mb-3 shadow-sm"
              style={{ borderRadius: '12px', border: 'none' }}
            >
              <Card.Body className="p-4">
                <Row>
                  <Col md={5}>
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
                          {doctor.full_name}
                        </h5>
                        <p className="text-muted mb-1" style={{ fontSize: '0.85rem' }}>
                          {doctor.qualification} • {doctor.specialization}
                        </p>
                        <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                          <FaMapMarkerAlt className="me-1" /> {doctor.clinic_address}
                        </p>
                      </div>
                    </div>
                  </Col>

                  <Col md={3}>
                    <div className="mb-1">
                      <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>
                        <FaClock className="me-1" /> Experience
                      </p>
                      <p className="fw-semibold mb-0">{doctor.experience_years} years</p>
                    </div>
                    <div>
                      <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>Fee</p>
                      <p className="fw-semibold mb-0">${doctor.consultation_fee}</p>
                    </div>
                  </Col>

                  <Col md={2}>
                    <Badge
                      bg={getStatusColor(doctor.status)}
                      className="px-3 py-2"
                    >
                      {getStatusLabel(doctor.status)}
                    </Badge>
                    <p className="text-muted mt-2" style={{ fontSize: '0.75rem' }}>
                      Joined: {formatDate(doctor.created_at)}
                    </p>
                  </Col>

                  <Col md={2} className="text-end">
                    {doctor.status === 'PENDING' && (
                      <div className="d-flex flex-wrap gap-2 justify-content-end">
                        <Button
                          variant="success"
                          size="sm"
                          onClick={() => handleAction(doctor, 'approve')}
                          style={{ borderRadius: '8px' }}
                        >
                          <FaCheck className="me-1" /> Approve
                        </Button>
                        <Button
                          variant="danger"
                          size="sm"
                          onClick={() => handleAction(doctor, 'reject')}
                          style={{ borderRadius: '8px' }}
                        >
                          <FaTimes className="me-1" /> Reject
                        </Button>
                      </div>
                    )}
                  </Col>
                </Row>
              </Card.Body>
            </Card>
          ))}


          {/* UI — Pending Profile Updates */}
{pendingUpdates.length > 0 && (
  <Card className="shadow-sm mb-4 border-0" style={{ borderRadius: '12px', borderLeft: '4px solid #f59e0b' }}>
    <Card.Body className="p-3">
      <h6 className="fw-bold mb-3">
        <FaEdit className="me-2 text-warning" /> Pending Profile Updates ({pendingUpdates.length})
      </h6>
      {pendingUpdates.map((doc) => (
        <div key={doc.id} className="d-flex justify-content-between align-items-center flex-wrap gap-2 py-2 border-bottom">
          <div>
            <strong>{doc.full_name || doc.qualification}</strong>
            <div className="text-muted" style={{ fontSize: '0.8rem' }}>
              Requested changes: {Object.keys(doc.pending_update || {}).join(', ')}
            </div>
          </div>
          <div className="d-flex gap-2">
            <Button size="sm" variant="success" onClick={() => handleApproveUpdate(doc.id)} style={{ borderRadius: '8px' }}>
              <FaCheck className="me-1" /> Approve
            </Button>
            <Button size="sm" variant="danger" onClick={() => handleRejectUpdate(doc.id)} style={{ borderRadius: '8px' }}>
              <FaTimes className="me-1" /> Reject
            </Button>
          </div>
        </div>
      ))}
    </Card.Body>
  </Card>
)}

          {/* Load More */}
          {hasMore && (
            <div className="text-center mt-4">
              <Button
                variant="outline-primary"
                onClick={handleLoadMore}
                disabled={loading}
                style={{ borderRadius: '8px', padding: '0.6rem 2rem' }}
              >
                {loading ? 'Loading...' : 'Load More'}
              </Button>
            </div>
          )}
        </>
      )}

      {/* Approval Modal */}
      {selectedDoctor && (
        <DoctorApproval
          show={showApprovalModal}
          onHide={() => setShowApprovalModal(false)}
          doctor={selectedDoctor}
          onSuccess={handleApprovalSuccess}
        />
      )}
    </Container>
  );
};

export default ManageDoctors;
