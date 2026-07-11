import React from 'react';
import { Form, Row, Col } from 'react-bootstrap';

const AuditLogFilter = ({ filters, onFilterChange, admins }) => {
  /**
   * Handle filter change.
   */
  const handleChange = (e) => {
    const { name, value } = e.target;
    onFilterChange({ ...filters, [name]: value });
  };

  return (
    <Row className="g-3">
      <Col md={4}>
        <Form.Group>
          <Form.Label className="fw-semibold text-secondary">Admin</Form.Label>
          <Form.Select
            name="admin_id"
            value={filters.admin_id || ''}
            onChange={handleChange}
            style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
          >
            <option value="">All Admins</option>
            {admins.map((admin) => (
              <option key={admin.id} value={admin.id}>
                {admin.full_name} ({admin.email})
              </option>
            ))}
          </Form.Select>
        </Form.Group>
      </Col>
      <Col md={4}>
        <Form.Group>
          <Form.Label className="fw-semibold text-secondary">Action</Form.Label>
          <Form.Select
            name="action"
            value={filters.action || ''}
            onChange={handleChange}
            style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
          >
            <option value="">All Actions</option>
            <option value="CREATE_ADMIN">Create Admin</option>
            <option value="DELETE_ADMIN">Delete Admin</option>
            <option value="APPROVE_DOCTOR">Approve Doctor</option>
            <option value="REJECT_DOCTOR">Reject Doctor</option>
          </Form.Select>
        </Form.Group>
      </Col>
      <Col md={4} className="d-flex align-items-end">
        {filters.admin_id || filters.action ? (
          <Button
            variant="outline-secondary"
            onClick={() => onFilterChange({ admin_id: '', action: '' })}
            style={{ borderRadius: '8px' }}
          >
            Clear Filters
          </Button>
        ) : null}
      </Col>
    </Row>
  );
};

export default AuditLogFilter;
