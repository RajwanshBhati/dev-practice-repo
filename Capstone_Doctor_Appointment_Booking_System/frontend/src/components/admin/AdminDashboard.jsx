import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getDoctorStatsAdmin } from '../../api/admin';
import { Container, Row, Col, Card } from 'react-bootstrap';
import {
  FaUsers,
  FaUserMd,
  FaCalendarCheck,
  FaCalendarTimes,
  FaMoneyBillWave,
  FaUserPlus,
  FaUserCheck,
  FaUserTimes,
} from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';

const AdminDashboard = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    total: 0,
    pending: 0,
    approved: 0,
    rejected: 0,
    suspended: 0,
  });

  /**
   * Load dashboard data on mount.
   */
  useEffect(() => {
    loadDashboardData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  /**
   * Load doctor statistics from API.
   */
  const loadDashboardData = async () => {
    setLoading(true);
    try {
      const data = await getDoctorStatsAdmin();
      setStats(data);
    } catch (error) {
      console.error('Error loading dashboard data:', error);
      toast.error('Failed to load dashboard data');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <Loading message="Loading dashboard..." />;
  }

  return (
    <Container className="mt-4">
      <div className="mb-4">
        <h1 className="fw-bold" style={{ color: '#1a202c' }}>
          Admin Dashboard
        </h1>
        <p className="text-muted">
          Welcome back, {user?.full_name}! Here's an overview of the platform.
        </p>
      </div>

      <Row className="mb-4">
        <Col lg={3} md={6} className="mb-3">
          <Card className="shadow-sm h-100" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                    Total Doctors
                  </p>
                  <h2 className="fw-bold mb-0" style={{ color: '#1a202c' }}>
                    {stats.total || 0}
                  </h2>
                </div>
                <div
                  className="rounded-circle d-flex align-items-center justify-content-center"
                  style={{
                    width: '48px',
                    height: '48px',
                    background: 'rgba(74, 144, 217, 0.1)',
                    color: '#4a90d9',
                    fontSize: '20px',
                  }}
                >
                  <FaUserMd />
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={3} md={6} className="mb-3">
          <Card className="shadow-sm h-100" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                    Pending
                  </p>
                  <h2 className="fw-bold mb-0" style={{ color: '#f59e0b' }}>
                    {stats.pending || 0}
                  </h2>
                </div>
                <div
                  className="rounded-circle d-flex align-items-center justify-content-center"
                  style={{
                    width: '48px',
                    height: '48px',
                    background: 'rgba(245, 158, 11, 0.1)',
                    color: '#f59e0b',
                    fontSize: '20px',
                  }}
                >
                  <FaUserPlus />
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={3} md={6} className="mb-3">
          <Card className="shadow-sm h-100" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                    Approved
                  </p>
                  <h2 className="fw-bold mb-0" style={{ color: '#48bb78' }}>
                    {stats.approved || 0}
                  </h2>
                </div>
                <div
                  className="rounded-circle d-flex align-items-center justify-content-center"
                  style={{
                    width: '48px',
                    height: '48px',
                    background: 'rgba(72, 187, 120, 0.1)',
                    color: '#48bb78',
                    fontSize: '20px',
                  }}
                >
                  <FaUserCheck />
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={3} md={6} className="mb-3">
          <Card className="shadow-sm h-100" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <p className="text-muted mb-0" style={{ fontSize: '0.85rem' }}>
                    Rejected
                  </p>
                  <h2 className="fw-bold mb-0" style={{ color: '#ef4444' }}>
                    {stats.rejected || 0}
                  </h2>
                </div>
                <div
                  className="rounded-circle d-flex align-items-center justify-content-center"
                  style={{
                    width: '48px',
                    height: '48px',
                    background: 'rgba(239, 68, 68, 0.1)',
                    color: '#ef4444',
                    fontSize: '20px',
                  }}
                >
                  <FaUserTimes />
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {/* Quick Actions */}
      <Row>
        <Col lg={6} className="mb-3">
          <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <h5 className="fw-bold mb-3">Quick Actions</h5>
              <div className="d-flex flex-wrap gap-2">
                <a href="/admin/doctors" className="btn btn-primary w-100 mb-2">
                  <FaUserMd className="me-2" /> Manage Doctors
                </a>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={6} className="mb-3">
          <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-4">
              <h5 className="fw-bold mb-3">Platform Status</h5>
              <div className="d-flex justify-content-between mb-2">
                <span className="text-muted">Total Doctors</span>
                <span className="fw-semibold">{stats.total || 0}</span>
              </div>
              <div className="progress" style={{ height: '8px' }}>
                <div
                  className="progress-bar bg-success"
                  style={{ width: '100%' }}
                />
              </div>
              <div className="d-flex justify-content-between mt-3">
                <span className="text-muted">Approval Rate</span>
                <span className="fw-semibold">
                  {stats.total > 0
                    ? `${Math.round((stats.approved / stats.total) * 100)}%`
                    : '0%'}
                </span>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default AdminDashboard;
