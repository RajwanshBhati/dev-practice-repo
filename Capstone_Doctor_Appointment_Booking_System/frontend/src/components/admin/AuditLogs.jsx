import React, { useState, useEffect } from 'react';
import { getAuditLogs, getAllAdmins } from '../../api/admin';
import { Container, Card, Table, Badge, Button, Form, Row, Col } from 'react-bootstrap';
import { FaSearch, FaClock, FaUser, FaEnvelope } from 'react-icons/fa';
import toast from 'react-hot-toast';
import Loading from '../common/Loading';
import {
  AUDIT_ACTION_COLORS,
  AUDIT_ACTION_ICONS,
  AUDIT_ACTIONS,
} from '../../utils/constants';

const AuditLogs = () => {
  const [loading, setLoading] = useState(true);
  const [logs, setLogs] = useState([]);
  const [admins, setAdmins] = useState([]);
  const [total, setTotal] = useState(0);
  const [filters, setFilters] = useState({
    admin_id: '',
    action: '',
  });
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(false);
  const limit = 20;

  /**
   * Load admins and logs on mount.
   */
  useEffect(() => {
    loadAdmins();
    loadLogs();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  /**
   * Load logs when filters or page changes.
   */
  useEffect(() => {
    loadLogs();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filters, page]);

  /**
   * Load all admins for filter dropdown.
   */
  const loadAdmins = async () => {
    try {
      const data = await getAllAdmins();
      setAdmins(data.admins || []);
    } catch (error) {
      console.error('Error loading admins:', error);
    }
  };

  /**
   * Load audit logs from API.
   */
  const loadLogs = async () => {
    setLoading(true);
    try {
      const skip = (page - 1) * limit;
      const params = { limit, skip };

      if (filters.admin_id) {
        params.admin_id = filters.admin_id;
      }

      const data = await getAuditLogs(params);
      if (page === 1) {
        setLogs(data.logs || []);
      } else {
        setLogs((prev) => [...prev, ...(data.logs || [])]);
      }
      setTotal(data.total || 0);
      setHasMore(data.total_pages > page);
    } catch (error) {
      console.error('Error loading audit logs:', error);
      toast.error('Failed to load audit logs');
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle filter change.
   */
  const handleFilterChange = (newFilters) => {
    setFilters(newFilters);
    setPage(1);
  };

  /**
   * Handle load more.
   */
  const handleLoadMore = () => {
    setPage((prev) => prev + 1);
  };

  /**
   * Format date for display.
   */
  const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    return date.toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
    });
  };

  /**
   * Get action display name.
   */
  const getActionLabel = (action) => {
    return AUDIT_ACTIONS[action] || action;
  };

  /**
   * Get action color.
   */
  const getActionColor = (action) => {
    return AUDIT_ACTION_COLORS[action] || 'secondary';
  };

  /**
   * Get action icon.
   */
  const getActionIcon = (action) => {
    return AUDIT_ACTION_ICONS[action] || '📋';
  };

  if (loading && logs.length === 0) {
    return <Loading message="Loading audit logs..." />;
  }

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="fw-bold" style={{ color: '#1a202c' }}>
          Audit Logs
        </h1>
        <span className="text-muted">
          Total: <strong>{total}</strong> entries
        </span>
      </div>

      {/* Filters */}
      <Card className="shadow-sm mb-4" style={{ borderRadius: '12px', border: 'none' }}>
        <Card.Body className="p-3">
          <AuditLogFilter
            filters={filters}
            onFilterChange={handleFilterChange}
            admins={admins}
          />
        </Card.Body>
      </Card>

      {/* Logs Table */}
      {logs.length === 0 ? (
        <div className="text-center py-5">
          <div style={{ fontSize: '48px', marginBottom: '20px' }}>📝</div>
          <h4>No audit logs found</h4>
          <p className="text-muted">No admin actions have been recorded yet.</p>
        </div>
      ) : (
        <>
          <Card className="shadow-sm" style={{ borderRadius: '12px', border: 'none' }}>
            <Card.Body className="p-0">
              <div className="table-responsive">
                <Table hover className="mb-0">
                  <thead style={{ background: '#f8f9fa' }}>
                    <tr>
                      <th style={{ padding: '12px 16px' }}>#</th>
                      <th style={{ padding: '12px 16px' }}>Admin</th>
                      <th style={{ padding: '12px 16px' }}>Action</th>
                      <th style={{ padding: '12px 16px' }}>Target</th>
                      <th style={{ padding: '12px 16px' }}>Details</th>
                      <th style={{ padding: '12px 16px' }}>Date</th>
                    </tr>
                  </thead>
                  <tbody>
                    {logs.map((log, index) => (
                      <tr key={log.id}>
                        <td style={{ padding: '12px 16px' }}>
                          {(page - 1) * limit + index + 1}
                        </td>
                        <td style={{ padding: '12px 16px' }}>
                          <div className="d-flex flex-column">
                            <span className="fw-semibold">{log.admin_email}</span>
                            <span className="text-muted" style={{ fontSize: '0.8rem' }}>
                              <FaUser className="me-1" /> {log.admin_id}
                            </span>
                          </div>
                        </td>
                        <td style={{ padding: '12px 16px' }}>
                          <Badge bg={getActionColor(log.action)} className="px-3 py-2">
                            {getActionIcon(log.action)} {getActionLabel(log.action)}
                          </Badge>
                        </td>
                        <td style={{ padding: '12px 16px' }}>
                          <div className="d-flex flex-column">
                            <span className="fw-semibold">{log.target_email || 'N/A'}</span>
                            <span className="text-muted" style={{ fontSize: '0.8rem' }}>
                              ID: {log.target_id || 'N/A'}
                            </span>
                          </div>
                        </td>
                        <td style={{ padding: '12px 16px' }}>
                          <div className="d-flex flex-column">
                            {log.details?.doctor_name && (
                              <span>Doctor: {log.details.doctor_name}</span>
                            )}
                            {log.details?.admin_name && (
                              <span>Admin: {log.details.admin_name}</span>
                            )}
                            {log.details?.specialization && (
                              <span className="text-muted" style={{ fontSize: '0.8rem' }}>
                                {log.details.specialization}
                              </span>
                            )}
                            {log.details?.rejection_reason && (
                              <span className="text-danger" style={{ fontSize: '0.8rem' }}>
                                Reason: {log.details.rejection_reason}
                              </span>
                            )}
                            {log.details?.notes && (
                              <span className="text-muted" style={{ fontSize: '0.8rem' }}>
                                Notes: {log.details.notes}
                              </span>
                            )}
                          </div>
                        </td>
                        <td style={{ padding: '12px 16px' }}>
                          <div className="d-flex flex-column">
                            <span>{formatDate(log.created_at)}</span>
                            <span className="text-muted" style={{ fontSize: '0.8rem' }}>
                              <FaClock className="me-1" />
                              {new Date(log.created_at).toLocaleDateString()}
                            </span>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              </div>
            </Card.Body>
          </Card>

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
    </Container>
  );
};

export default AuditLogs;
