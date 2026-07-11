import React, { useState } from 'react';
import { Modal, Button, Form, Alert } from 'react-bootstrap';
import { approveDoctor, rejectDoctor } from '../../api/admin';
import toast from 'react-hot-toast';

const DoctorApproval = ({ show, onHide, doctor, onSuccess }) => {
  const [loading, setLoading] = useState(false);
  const [notes, setNotes] = useState('');
  const [reason, setReason] = useState('');
  const [action, setAction] = useState('approve');

  /**
   * Reset form when modal opens.
   */
  React.useEffect(() => {
    if (show) {
      setNotes('');
      setReason('');
      setAction('approve');
    }
  }, [show]);

  /**
   * Handle approve action.
   */
  const handleApprove = async () => {
    if (!notes.trim()) {
      toast.error('Please add approval notes');
      return;
    }

    setLoading(true);
    try {
      await approveDoctor(doctor.id, { notes });
      onSuccess(doctor.id, 'APPROVED');
      onHide();
    } catch (error) {
      // Error handled by axios interceptor
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle reject action.
   */
  const handleReject = async () => {
    if (!reason.trim()) {
      toast.error('Please provide a rejection reason');
      return;
    }

    setLoading(true);
    try {
      await rejectDoctor(doctor.id, { reason });
      onSuccess(doctor.id, 'REJECTED');
      onHide();
    } catch (error) {
      // Error handled by axios interceptor
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle form submission based on action.
   */
  const handleSubmit = (e) => {
    e.preventDefault();
    if (action === 'approve') {
      handleApprove();
    } else {
      handleReject();
    }
  };

  return (
    <Modal show={show} onHide={onHide} centered size="lg">
      <Modal.Header closeButton>
        <Modal.Title>
          {action === 'approve' ? 'Approve Doctor' : 'Reject Doctor'}
        </Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <div className="mb-3">
            <h6 className="fw-bold">Doctor Information</h6>
            <p>
              <strong>Name:</strong> {doctor?.full_name}
            </p>
            <p>
              <strong>Email:</strong> {doctor?.email}
            </p>
            <p>
              <strong>Specialization:</strong> {doctor?.specialization}
            </p>
            <p>
              <strong>Qualification:</strong> {doctor?.qualification}
            </p>
            <p>
              <strong>License:</strong> {doctor?.license_number}
            </p>
            <p>
              <strong>Clinic:</strong> {doctor?.clinic_address}
            </p>
          </div>

          <hr />

          <div className="mb-3">
            <Form.Label className="fw-semibold">Action</Form.Label>
            <div className="d-flex gap-3">
              <Button
                variant={action === 'approve' ? 'success' : 'outline-success'}
                size="sm"
                onClick={() => setAction('approve')}
                style={{ borderRadius: '8px' }}
              >
                Approve
              </Button>
              <Button
                variant={action === 'reject' ? 'danger' : 'outline-danger'}
                size="sm"
                onClick={() => setAction('reject')}
                style={{ borderRadius: '8px' }}
              >
                Reject
              </Button>
            </div>
          </div>

          {action === 'approve' ? (
            <Form.Group className="mb-3">
              <Form.Label className="fw-semibold">
                Approval Notes <span className="text-danger">*</span>
              </Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                placeholder="Add notes about this approval..."
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
                required
                style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
              />
            </Form.Group>
          ) : (
            <Form.Group className="mb-3">
              <Form.Label className="fw-semibold">
                Rejection Reason <span className="text-danger">*</span>
              </Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                placeholder="Please provide a reason for rejection..."
                value={reason}
                onChange={(e) => setReason(e.target.value)}
                required
                style={{ borderRadius: '8px', border: '2px solid #e2e8f0' }}
              />
            </Form.Group>
          )}

          {action === 'reject' && (
            <Alert variant="warning" className="mt-2">
              <strong>Note:</strong> The doctor will receive an email with the rejection reason.
            </Alert>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onHide} disabled={loading}>
            Cancel
          </Button>
          <Button
            variant={action === 'approve' ? 'success' : 'danger'}
            type="submit"
            disabled={loading}
          >
            {loading
              ? 'Processing...'
              : action === 'approve'
              ? 'Approve Doctor'
              : 'Reject Doctor'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default DoctorApproval;
