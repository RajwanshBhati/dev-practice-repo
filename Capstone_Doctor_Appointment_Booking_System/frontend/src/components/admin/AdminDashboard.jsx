import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { getDoctorStatsAdmin, getAuditLogs } from '../../api/admin';
import { Container, Row, Col, Card } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import {
  FaUserMd,
  FaUserPlus,
  FaUserCheck,
  FaUserTimes,
  FaChartLine,
  FaHistory,
  FaCheckCircle,
  FaTimesCircle,
  FaUserShield,
} from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';

const StatCard = ({ label, value, icon, color, bg }) => (
  <Col lg={3} md={6} className="mb-3">
    <Card
      className="shadow-sm h-100 border-0 stat-card"
      style={{ borderRadius: '16px', overflow: 'hidden' }}
    >
      <Card.Body className="p-4">
        <div className="d-flex justify-content-between align-items-start">
          <div>
            <p className="text-muted mb-1 text-uppercase" style={{ fontSize: '0.75rem', letterSpacing: '0.05em', fontWeight: 600 }}>
              {label}
            </p>
            <h2 className="fw-bold mb-0" style={{ color: '#1a202c', fontSize: '2rem' }}>
              {value || 0}
            </h2>
          </div>
          <div
            className="rounded-circle d-flex align-items-center justify-content-center"
            style={{ width: '52px', height: '52px', background: bg, color, fontSize: '22px' }}
          >
            {icon}
          </div>
        </div>
      </Card.Body>
    </Card>
  </Col>
);

const AdminDashboard = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({
    total: 0,
    pending: 0,
    approved: 0,
    rejected: 0,
  });
  const [activity, setActivity] = useState([]);
  const [activityLoading, setActivityLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
    loadRecentActivity();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadDashboardData = async () => {
    setStats((prev) => ({ ...prev, ...data }));
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

  /**
   * Load the 5 most recent audit log entries for the activity feed.
   */
  const loadRecentActivity = async () => {
    setActivityLoading(true);
    try {
      const data = await getAuditLogs({ limit: 5, skip: 0 });
      setActivity(data.logs || []);
    } catch (error) {
      console.error('Error loading recent activity:', error);
    } finally {
      setActivityLoading(false);
    }
  };


  const describeActivity = (log) => {
    const doctorName = log.details?.doctor_name || log.target_email || 'A doctor';
    switch (log.action) {
      case 'APPROVE_DOCTOR':
        return { text: `${doctorName} was approved`, icon: <FaCheckCircle />, color: '#48bb78' };
      case 'REJECT_DOCTOR':
        return { text: `${doctorName} was rejected`, icon: <FaTimesCircle />, color: '#ef4444' };
      case 'CREATE_ADMIN':
      case 'CREATE_FIRST_ADMIN':
        return { text: `New admin ${log.target_email || ''} was created`, icon: <FaUserShield />, color: '#4a90d9' };
      case 'DELETE_ADMIN':
        return { text: `Admin ${log.target_email || ''} was removed`, icon: <FaUserShield />, color: '#ef4444' };
      default:
        return { text: `${log.action} by ${log.admin_email}`, icon: <FaHistory />, color: '#718096' };
    }
  };

  /**
   * Format a timestamp as a short relative time (e.g. "2h ago").
   */
  const timeAgo = (dateStr) => {
    const diffMs = Date.now() - new Date(dateStr).getTime();
    const mins = Math.floor(diffMs / 60000);
    if (mins < 1) return 'just now';
    if (mins < 60) return `${mins}m ago`;
    const hours = Math.floor(mins / 60);
    if (hours < 24) return `${hours}h ago`;
    const days = Math.floor(hours / 24);
    return `${days}d ago`;
  };

  if (loading) {
    return <Loading message="Loading dashboard..." />;
  }

  const approvalRate = stats.total > 0 ? Math.round((stats.approved / stats.total) * 100) : 0;
  const firstName = user?.full_name?.split(' ')[0] || 'Admin';

  return (
    <Container className="mt-4 mb-5">
      {/* Hero header */}
      <div
        className="mb-4 p-4 p-md-5 text-white"
        style={{
          borderRadius: '20px',
          background: 'linear-gradient(135deg, #4a90d9, #2d5faa)',
          boxShadow: '0 10px 30px rgba(74, 144, 217, 0.25)',
        }}
      >
        <div className="d-flex align-items-center justify-content-between flex-wrap gap-3">
          <div>
            <h1 className="fw-bold mb-1">Welcome back, {firstName}</h1>
            <p className="mb-0" style={{ opacity: 0.9 }}>
              Monitor your healthcare platform at a glance.
            </p>
          </div>
          <div
            className="rounded-circle d-flex align-items-center justify-content-center"
            style={{ width: '64px', height: '64px', background: 'rgba(255,255,255,0.15)', fontSize: '28px' }}
          >
            <FaUserShield />
          </div>
        </div>
      </div>

      {/* Stats Cards */}
      <Row className="mb-4">
        <StatCard
          label="Total Doctors"
          value={stats.total}
          icon={<FaUserMd />}
          color="#4a90d9"
          bg="rgba(74, 144, 217, 0.12)"
        />
        <StatCard
          label="Pending"
          value={stats.pending}
          icon={<FaUserPlus />}
          color="#f59e0b"
          bg="rgba(245, 158, 11, 0.12)"
        />
        <StatCard
          label="Approved"
          value={stats.approved}
          icon={<FaUserCheck />}
          color="#48bb78"
          bg="rgba(72, 187, 120, 0.12)"
        />
        <StatCard
          label="Rejected"
          value={stats.rejected}
          icon={<FaUserTimes />}
          color="#ef4444"
          bg="rgba(239, 68, 68, 0.12)"
        />
      </Row>

      <Row>
        {/* Platform Status */}
        <Col lg={5} className="mb-3">
          <Card className="shadow-sm h-100 border-0" style={{ borderRadius: '16px' }}>
            <Card.Body className="p-4">
              <div className="d-flex align-items-center justify-content-between mb-3">
                <h5 className="fw-bold mb-0">Platform Status</h5>
                <FaChartLine className="text-primary" style={{ fontSize: '20px' }} />
              </div>

              <div className="mb-3">
                <div className="d-flex justify-content-between mb-1">
                  <span className="text-muted">Approval Rate</span>
                  <span className="fw-semibold">{approvalRate}%</span>
                </div>
                <div className="progress" style={{ height: '10px', borderRadius: '6px' }}>
                  <div
                    className="progress-bar bg-success"
                    style={{ width: `${approvalRate}%`, borderRadius: '6px' }}
                  />
                </div>
              </div>

              <Row className="text-center mt-4">
                <Col xs={4}>
                  <h4 className="fw-bold mb-0" style={{ color: '#f59e0b' }}>{stats.pending || 0}</h4>
                  <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>Awaiting Review</p>
                </Col>
                <Col xs={4}>
                  <h4 className="fw-bold mb-0" style={{ color: '#48bb78' }}>{stats.approved || 0}</h4>
                  <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>Active Doctors</p>
                </Col>
                <Col xs={4}>
                  <h4 className="fw-bold mb-0" style={{ color: '#ef4444' }}>{stats.rejected || 0}</h4>
                  <p className="text-muted mb-0" style={{ fontSize: '0.8rem' }}>Rejected</p>
                </Col>
              </Row>

              <Link
                to="/admin/doctors"
                className="btn btn-primary fw-semibold mt-4 w-100 d-flex align-items-center justify-content-center py-2"
                style={{ borderRadius: '10px' }}
              >
                <FaUserMd className="me-2" /> Review Doctors
              </Link>
            </Card.Body>
          </Card>
        </Col>

        {/* Recent Activity */}
        <Col lg={7} className="mb-3">
          <Card className="shadow-sm h-100 border-0" style={{ borderRadius: '16px' }}>
            <Card.Body className="p-4">
              <div className="d-flex align-items-center justify-content-between mb-3">
                <h5 className="fw-bold mb-0">Recent Activity</h5>
                <FaHistory className="text-primary" style={{ fontSize: '18px' }} />
              </div>

              {activityLoading ? (
                <p className="text-muted mb-0">Loading activity...</p>
              ) : activity.length === 0 ? (
                <div className="text-center py-4">
                  <p className="text-muted mb-0">No recent activity yet.</p>
                </div>
              ) : (
                <div className="d-flex flex-column gap-3">
                  {activity.map((log) => {
                    const { text, icon, color } = describeActivity(log);
                    return (
                      <div key={log.id} className="d-flex align-items-start">
                        <div
                          className="rounded-circle d-flex align-items-center justify-content-center me-3 flex-shrink-0"
                          style={{ width: '38px', height: '38px', background: `${color}1f`, color, fontSize: '16px' }}
                        >
                          {icon}
                        </div>
                        <div className="flex-grow-1">
                          <p className="mb-0 fw-semibold" style={{ color: '#1a202c', fontSize: '0.92rem' }}>
                            {text}
                          </p>
                          <span className="text-muted" style={{ fontSize: '0.78rem' }}>
                            {timeAgo(log.created_at)}
                          </span>
                        </div>
                      </div>
                    );
                  })}
                </div>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default AdminDashboard;
